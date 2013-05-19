package ssu.csit.fileservice.core;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Used to interact with {@link HashedFile} objects.
 * 
 * @author Alexandra Fomina
 */
public class FileService {

    private static FileService instance;
    private static ServiceRegistry serviceRegistry;
    private static SessionFactory sessionFactory;
    private Session session;

    private FileService() {
        Configuration configuration = new Configuration().configure();
        Properties properties = configuration.getProperties();
        serviceRegistry = new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    /**
     * Returns a <code>FileService</code> instance.
     * 
     * @return the <code>FileService</code> object
     */
    public static FileService get() {
        if (instance == null) {
            instance = new FileService();
        }
        return instance;
    }

    private Session getSession() {
        if (session == null) {
            session = sessionFactory.openSession();
        }
        return session;
    }
    
    /**
     * Persists a <code>HashedFile</code> object in the storage.
     * 
     * @param file the file to save
     */
    public void save(HashedFile file) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.save(file);
        session.flush();
        transaction.commit();
    }
    
    /**
     * Updates the persistent <code>HashedFile</code> object in the storage.
     * 
     * @param file the file to save
     */
    public void update(HashedFile file) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.update(file);
        session.flush();
        transaction.commit();
    }

    /**
     * Deletes a specified <code>HashedFile</code> instance from the storage.
     * 
     * @param file the file to delete
     */
    public void delete(HashedFile file) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(file);
        session.flush();
        transaction.commit();
    }
    
    /**
     * Returns all the files from given directory.
     * 
     * @param directory
     * @return the files
     */
    @SuppressWarnings("unchecked")
    public List<HashedFile> listFiles(File directory) {
        try {
            return session.createQuery("FROM HASHES WHERE DIRECTORY = :dir").setParameter("dir", directory).list();
        } catch (NullPointerException e) {
            return Collections.emptyList();
        }
    }
    
}
