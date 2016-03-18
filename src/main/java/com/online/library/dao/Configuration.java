package com.online.library.dao;

import com.online.library.dao.entity.Book;
import com.online.library.dao.entity.UserProfile;
import com.online.library.dao.entity.UsersBooks;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Created by Tim on 15.03.2016.
 */
public class Configuration {
    private static final SessionFactory ourSessionFactory;
    private static final StandardServiceRegistry serviceRegistry;
    private static Metadata metadata;

    static {
        try {
            serviceRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();


            metadata = new MetadataSources(serviceRegistry)
                    .addAnnotatedClass(Book.class)
                    .addAnnotatedClass(UserProfile.class)
                    .addAnnotatedClass(UsersBooks.class)
                    .addAnnotatedClassName("com.online.library.dao.entity.Book")
                    .addAnnotatedClassName("com.online.library.dao.entity.UserProfile")
                    .addAnnotatedClassName("com.online.library.dao.entity.UsersBooks")
                    .addResource("hibernate.cfg.xml")
                    .addResource("mapping.hbn.xml")
                    .getMetadataBuilder()
                    .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                    .build();

            ourSessionFactory = metadata.getSessionFactoryBuilder()
//                    .applyBeanManager(getBeanManagerFromSomewhere())
                    .build();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

}
