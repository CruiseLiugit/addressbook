package com.vaadin.demo.tutorial.addressbook;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.demo.tutorial.addressbook.dao.PersonManager;
import com.vaadin.demo.tutorial.addressbook.dao.impl.PersonManagerBean;
import com.vaadin.demo.tutorial.addressbook.data.Person;
import com.vaadin.demo.tutorial.addressbook.data.PersonReferenceContainer;
import com.vaadin.demo.tutorial.addressbook.data.QueryMetaData;
import com.vaadin.demo.tutorial.addressbook.data.SearchFilter;
import com.vaadin.demo.tutorial.addressbook.ui.HelpWindow;
import com.vaadin.demo.tutorial.addressbook.ui.ListView;
import com.vaadin.demo.tutorial.addressbook.ui.NavigationTree;
import com.vaadin.demo.tutorial.addressbook.ui.PersonForm;
import com.vaadin.demo.tutorial.addressbook.ui.PersonList;
import com.vaadin.demo.tutorial.addressbook.ui.SearchView;
import com.vaadin.demo.tutorial.addressbook.ui.SharingOptions;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Dao;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.ioc.loader.combo.ComboIocLoader;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 这里跟 Nutz 不同入口，Nutz 的 ioc 控制不了这里
 * @author lililiu
 *
 */
@SuppressWarnings("serial")
public class AddressbookApplication extends Application implements
		ClickListener, ValueChangeListener, ItemClickListener {
	
	private static final Log log = Logs.get();

	public PersonManagerBean personManager;
	private PersonReferenceContainer dataSource;
	
	private BasicDao baseDao= null;
	private Dao nutzDao = null;
	
	///////////////////////////////////////
	//代码，这里再次初始化 Nutz IOC 并创建 dao 对象
	public Dao initNutzDao() throws Throwable{
		ComboIocLoader loader = new ComboIocLoader(new String[]{
				"*org.nutz.ioc.loader.json.JsonLoader","ioc/",
				"*org.nutz.ioc.loader.annotation.AnnotationIocLoader","com.vaadin.demo.tutorial.addressbook"
		});
		NutIoc ioc = new NutIoc(loader);
		nutzDao  = ioc.get(Dao.class);
		return nutzDao;
	}
	///////////////////////////////////////
	
	//public AddressbookApplication(){}
	
	/*
	public static class Servlet extends AbstractApplicationServlet {

		//PersonManager personManager = new PersonManagerBean();

		@Override
		protected Application getNewApplication(HttpServletRequest request)
				throws ServletException {
			System.out.println("-------------static servlet getNewApplication()");
			return new AddressbookApplication();
		}

		@Override
		protected Class<? extends Application> getApplicationClass()
				throws ClassNotFoundException {
			System.out.println("-------------static servlet getApplicationClass()");
			return AddressbookApplication.class;
		}
	}
	*/

	private NavigationTree tree = new NavigationTree(this);
	private Button newContact = new Button("Add contact");
	private Button search = new Button("Search");
	private Button share = new Button("Share");
	private Button help = new Button("Help");
	private HorizontalSplitPanel horizontalSplit = new HorizontalSplitPanel();
	// Lazyly created ui references
	private ListView listView = null;
	private SearchView searchView = null;
	private PersonList personList = null;
	private PersonForm personForm = null;
	private HelpWindow helpWindow = null;
	private SharingOptions sharingOptions = null;
	
	

	@Override
	public void init() {
		log.info("---------init()--------");
		try {
			this.nutzDao = this.initNutzDao();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("-----------------nutzDao ="+nutzDao);
		
		
		if (personManager == null) {
			this.personManager = new PersonManagerBean(nutzDao);
		}
		
		
		log.info("-----------------personManager ="+personManager);
		
		dataSource = new PersonReferenceContainer(personManager);
		log.info("-----------------dataSource ="+dataSource);
		
		dataSource.refresh(); // Load initial data
		
		buildMainLayout();
		setMainComponent(getListView());
	}

	private void buildMainLayout() {
		setMainWindow(new Window("Address Book Demo application"));

		setTheme("contacts");

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		layout.addComponent(createToolbar());
		layout.addComponent(horizontalSplit);
		layout.setExpandRatio(horizontalSplit, 1);

		horizontalSplit.setSplitPosition(200, HorizontalSplitPanel.UNITS_PIXELS);
		horizontalSplit.setFirstComponent(tree);

		getMainWindow().setContent(layout);
	}

	private HorizontalLayout createToolbar() {
		HorizontalLayout lo = new HorizontalLayout();
		lo.addComponent(newContact);
		lo.addComponent(search);
		lo.addComponent(share);
		lo.addComponent(help);

		search.addListener((ClickListener) this);
		share.addListener((ClickListener) this);
		help.addListener((ClickListener) this);
		newContact.addListener((ClickListener) this);

		search.setIcon(new ThemeResource("icons/32/folder-add.png"));
		share.setIcon(new ThemeResource("icons/32/users.png"));
		help.setIcon(new ThemeResource("icons/32/help.png"));
		newContact.setIcon(new ThemeResource("icons/32/document-add.png"));

		lo.setMargin(true);
		lo.setSpacing(true);

		lo.setStyleName("toolbar");

		lo.setWidth("100%");

		Embedded em = new Embedded("", new ThemeResource("images/logo.png"));
		lo.addComponent(em);
		lo.setComponentAlignment(em, Alignment.MIDDLE_RIGHT);
		lo.setExpandRatio(em, 1);

		return lo;
	}

	private void setMainComponent(Component c) {
		horizontalSplit.setSecondComponent(c);
	}

	/*
	 * View getters exist so we can lazily generate the views, resulting in
	 * faster application startup time.
	 */
	private ListView getListView() {
		if (listView == null) {
			personList = new PersonList(this);
			personForm = new PersonForm(this);
			listView = new ListView(personList, personForm);
		}
		return listView;
	}

	private SearchView getSearchView() {
		if (searchView == null) {
			searchView = new SearchView(this);
		}
		return searchView;
	}

	private HelpWindow getHelpWindow() {
		if (helpWindow == null) {
			helpWindow = new HelpWindow();
		}
		return helpWindow;
	}

	private SharingOptions getSharingOptions() {
		if (sharingOptions == null) {
			sharingOptions = new SharingOptions();
		}
		return sharingOptions;
	}

	public PersonReferenceContainer getDataSource() {
		return dataSource;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton();

		if (source == search) {
			showSearchView();
		} else if (source == help) {
			showHelpWindow();
		} else if (source == share) {
			showShareWindow();
		} else if (source == newContact) {
			addNewContanct();
		}
	}

	private void showHelpWindow() {
		getMainWindow().addWindow(getHelpWindow());
	}

	private void showShareWindow() {
		getMainWindow().addWindow(getSharingOptions());
	}

	private void showListView() {
		setMainComponent(getListView());
	}

	private void showSearchView() {
		setMainComponent(getSearchView());
	}

	/**
	 * PersonList 表格操作监听器
	 */
	public void valueChange(ValueChangeEvent event) {
		Property property = event.getProperty();
		//1 判断是否是 PersonList 表格对象
		if (property == personList) {
			//2 根据属性值，查询出所选中的 Person 对象
			Person person = personManager.getPerson( (Integer)personList
					.getValue());
			//3 把这个数据传递给 PersonForm 
			personForm.editContact(person);
		}
	}

	/**
	 * 左侧 Tree 操作方法
	 * 
	 */
	public void itemClick(ItemClickEvent event) {
		if (event.getSource() == tree) {
			Object itemId = event.getItemId();
			if (itemId != null) {
				if (itemId == NavigationTree.SHOW_ALL) {
					getDataSource().refresh(
							PersonReferenceContainer.defaultQueryMetaData);
					showListView();
				} else if (itemId == NavigationTree.SEARCH) {
					showSearchView();
				} else if (itemId instanceof SearchFilter) {
					search((SearchFilter) itemId);
				}
			}
		}
	}

	private void addNewContanct() {
		showListView();
		personForm.addContact();
	}

	public void search(SearchFilter searchFilter) {
		QueryMetaData qmd = new QueryMetaData(
				(String) searchFilter.getPropertyId(), searchFilter.getTerm(),
				getDataSource().getQueryMetaData().getOrderBy(),
				getDataSource().getQueryMetaData().getAscending());
		getDataSource().refresh(qmd);
		showListView();

		getMainWindow().showNotification(
				"Searched for " + searchFilter.getPropertyId() + "="
						+ searchFilter.getTerm() + ", found "
						+ getDataSource().size() + " item(s).",
				Notification.TYPE_TRAY_NOTIFICATION);
	}

	public void saveSearch(SearchFilter searchFilter) {
		tree.addItem(searchFilter);
		tree.setParent(searchFilter, NavigationTree.SEARCH);
		// mark the saved search as a leaf (cannot have children)
		tree.setChildrenAllowed(searchFilter, false);
		// make sure "Search" is expanded
		tree.expandItem(NavigationTree.SEARCH);
		// select the saved search
		tree.setValue(searchFilter);
	}
}
