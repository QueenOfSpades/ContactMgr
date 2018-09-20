package com.teamtreehouse.contactmgr;

import com.teamtreehouse.contactmgr.model.Contact;
import com.teamtreehouse.contactmgr.model.Contact.ContactBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class Application {
    // Hold a reusable reference to a SessionFactory (since we need only one)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new ContactBuilder("Chris","Ramacciotti")
                .withEmail("rama@teamtreehouse.com")
                .withPhone(1234567890L)
                .build();
        int id = save(contact);

        //Display list of contacts before the update
        System.out.printf("%n%nBefore Update%n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        //Get the persistent contact
        Contact c = findContactByID(id);

        //Update the contact
        c.setFirstName("Ian");

        //Persist the changes
        System.out.printf("%n%nUpdating...%n%n");
        update(c);
        System.out.printf("%n%nUpdate Complete%n%n");

        //Display a list of contacts after update
        System.out.printf("%n%nAfter Update%n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        System.out.printf("%n%nDeleting...%n%n");
        delete(c);

        System.out.printf("%n%nAfter Delete%n%n");
        fetchAllContacts().stream().forEach(System.out::println);



    }

    private static Contact findContactByID(int id){
        //Open a session
        Session session = sessionFactory.openSession();

        //Retrieve the persistant object  (or null if not found)
        Contact contact = session.get(Contact.class, id);

        //Close the session
        session.close();

        //Return the object
        return contact;
    }

    private static void delete(Contact contact){
        //Open session
        Session session = sessionFactory.openSession();
        //Being transaction
        session.beginTransaction();
        //Use session to delete a contact
        session.delete(contact);
        //Commit transaction
        session.getTransaction().commit();
        //Close session
        session.close();
    }

    private static void update(Contact contact){
        //Open a session
        Session session = sessionFactory.openSession();

        //Begin a transaction
        session.beginTransaction();

        //Use the session to update a contact
        session.update(contact);

        //Commit the transaction
        session.getTransaction().commit();

        //Close session
        session.close();
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts(){
        //Open a session
        Session session = sessionFactory.openSession();

        //Create criteria
        Criteria criteria = session.createCriteria(Contact.class);

        //Get a list of Contact objects according to the Criteria object
        List<Contact> contacts = criteria.list();

        //Close a session
        session.close();

        return contacts;
    }

    private static int save(Contact contact){

        //Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to save the contact
        int id = (int)session.save(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();

        return id;

    }
}
