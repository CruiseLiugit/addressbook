package com.vaadin.demo.tutorial.addressbook.dao;

import java.util.List;

import com.vaadin.demo.tutorial.addressbook.data.Person;
import com.vaadin.demo.tutorial.addressbook.data.PersonReference;
import com.vaadin.demo.tutorial.addressbook.data.QueryMetaData;

public interface PersonManager {

    public List<PersonReference> getPersonReferences(QueryMetaData queryMetaData, String... propertyNames);

    public Person getPerson(int id);

    public Person savePerson(Person person);

}
