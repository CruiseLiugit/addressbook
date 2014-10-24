package com.vaadin.demo.tutorial.addressbook.ejb;

import com.vaadin.demo.tutorial.addressbook.dao.PersonManager;
import com.vaadin.demo.tutorial.addressbook.data.QueryMetaData;
import com.vaadin.demo.tutorial.addressbook.data.PersonReference;

import java.util.List;

import javax.persistence.PersistenceContext;

import com.vaadin.demo.tutorial.addressbook.data.Person;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 这个数据库查询的实现方式   仅供参考
 * @author lililiu
 *
 */
public class PersonManagerBean implements PersonManager {

	/*
    @PersistenceContext
    protected EntityManager entityManager;

    public Person getPerson(Long id) {
        return entityManager.find(Person.class, id);
    }
    */

    @SuppressWarnings("unchecked")
    public List<PersonReference> getPersonReferences(QueryMetaData queryMetaData, String... propertyNames) {
//        System.out.println("Getting person references using query metadata " + queryMetaData);

    		/*
        // Construct the query
        StringBuffer pqlBuf = new StringBuffer();
        pqlBuf.append("SELECT p.id");
        for (int i = 0; i < propertyNames.length; i++) {
            pqlBuf.append(",");
            pqlBuf.append("p.");
            pqlBuf.append(propertyNames[i]);
        }
        pqlBuf.append(" FROM Person p");

        if (queryMetaData.getPropertyName() != null) {
            pqlBuf.append(" WHERE p.");
            pqlBuf.append(queryMetaData.getPropertyName());
            if (queryMetaData.getSearchTerm() == null) {
                pqlBuf.append(" IS NULL");
            } else {
                pqlBuf.append(" = :searchTerm");
            }
        }

        if (queryMetaData != null && queryMetaData.getAscending().length > 0) {
            assert queryMetaData.getAscending().length == queryMetaData.getOrderBy().length : "orderBy and ascending arrays must be equal in length";
            pqlBuf.append(" ORDER BY ");
            for (int i = 0; i < queryMetaData.getAscending().length; i++) {
                if (i > 0) {
                    pqlBuf.append(",");
                }
                pqlBuf.append("p.");
                pqlBuf.append(queryMetaData.getOrderBy()[i]);
                if (!queryMetaData.getAscending()[i]) {
                    pqlBuf.append(" DESC");
                }
            }
        }

        String pql = pqlBuf.toString();

//        System.out.println("  Using PQL: " + pql);

        Query query = entityManager.createQuery(pql);
        if (queryMetaData.getPropertyName() != null && queryMetaData.getSearchTerm() != null) {
            query.setParameter("searchTerm", queryMetaData.getSearchTerm());
        }

        List<Object[]> result = query.getResultList();

//        System.out.println("  Received " + result.size() + " records");

        List<PersonReference> referenceList = new ArrayList<PersonReference>(result.size());

        HashMap<String, Object> valueMap;
        for (Object[] row : result) {
            assert row.length == propertyNames.length + 1 : "invalid number of returned values";
            valueMap = new HashMap<String, Object>();
            for (int i = 1; i < row.length; i++) {
                valueMap.put(propertyNames[i - 1], row[i]);
            }
            referenceList.add(new PersonReference((Long) row[0], valueMap));
        }


        return referenceList;
		*/
    		return null;
    }

    public Person savePerson(Person person) {
    	/*
//        System.out.println("Saving person " + person);
        if (person.getId() == null)
            entityManager.persist(person);
        else
            entityManager.merge(person);
        */
        return person;
    }

	public Person getPerson(int id) {
		// TODO Auto-generated method stub
		return null;
	}
    
   
}
