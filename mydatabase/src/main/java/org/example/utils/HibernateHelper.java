package org.example.utils;

import org.example.entities.CategoryEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateHelper {
    private static SessionFactory sessionFactory;

    //Буде викликатися автоматично коли викоритовується даний клас
    static {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                //.configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(CategoryEntity.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch(Exception ex) {
            System.out.println("Exception: "+ ex);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }
    public static void shutDown() {
        if(sessionFactory != null)
            sessionFactory.close();
    }
}
