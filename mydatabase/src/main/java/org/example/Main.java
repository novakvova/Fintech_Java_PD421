package org.example;

import org.example.entities.CategoryEntity;
import org.example.utils.HibernateHelper;

public class Main {
    public static void main(String[] args) {
        //System.out.printf("Привіт Java!");
        var session = HibernateHelper.getSession();
        try {
            session.beginTransaction();
            var list = new CategoryEntity[2];
            list[0] = new CategoryEntity("Пиво");

            session.persist(list[0]);
            list[1] = new CategoryEntity("Морозиво");
            session.persist(list[1]);

            session.getTransaction().commit();
        }
        catch (Exception ex) {

        }
        finally {
            session.close();
        }

    }
}