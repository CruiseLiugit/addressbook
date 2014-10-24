package com.vaadin.demo.tutorial.addressbook.ui;

import com.vaadin.demo.tutorial.addressbook.AddressbookApplication;
import com.vaadin.demo.tutorial.addressbook.data.PersonReferenceContainer;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class PersonList extends Table {

    public PersonList(AddressbookApplication app) {
        setSizeFull();
        setContainerDataSource(app.getDataSource());

        setVisibleColumns(PersonReferenceContainer.NATURAL_COL_ORDER);   //列的排练顺序，从 dataSource 中读取数据的顺序
        setColumnHeaders(PersonReferenceContainer.COL_HEADERS_ENGLISH);  //列名

        setColumnCollapsingAllowed(true);
        setColumnReorderingAllowed(true);

        /*
         * Make table selectable, react immediatedly to user events, and pass
         * events to the controller (our main application)
         */
        setSelectable(true);
        setImmediate(true);
        addListener((ValueChangeListener) app);
        /* We don't want to allow users to de-select a row */
        setNullSelectionAllowed(false);

        // customize email column to have mailto: links using column generator
        addGeneratedColumn("email", new ColumnGenerator() {

            public Component generateCell(Table source, Object itemId,
                    Object columnId) {
                String email = (String) getContainerProperty(itemId, "email").getValue();
                Link l = new Link();
                l.setResource(new ExternalResource("mailto:" + email));
                l.setCaption(email);
                return l;
            }
        });
    }
}
