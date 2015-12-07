package ssu.csit.fileservice.core;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Used to interact with {@link HashedFile} objects.
 * 
 * @author Alexandra Fomina
 */
public class FileService {

    private static final FileService INSTANCE = new FileService();
    private static ServiceRegistry serviceRegistry;
    private static SessionFactory sessionFactory;

    private FileService() {}
    
    /**
     * Returns a single <code>FileService</code> instance.
     * 
     * @return the <code>FileService</code> object
     */
    public static FileService get() {
    	Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
    	Configuration configuration = new Configuration().configure();
        serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
        				  .buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return INSTANCE;
    }

    /**
     * Persists a <code>HashedFile</code> object in the storage.
     * 
     * @param file the file to save
     */
    public void save(HashedFile file) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.save(file);
		transaction.commit();
    }

    public void save(List<HashedFile> toStore) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        for (HashedFile hashedFile : toStore) {
            session.save(hashedFile);
        }
        transaction.commit();
    }
    
    /**
     * Updates the persistent <code>HashedFile</code> object in the storage.
     * 
     * @param file the file to save
     */
    public void update(HashedFile file) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(file);
        transaction.commit();
    }

    /**
     * Deletes a specified <code>HashedFile</code> instance from the storage.
     * 
     * @param file the file to delete
     */
    public void delete(HashedFile file) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(file);
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
    	Session session = sessionFactory.openSession();
        try {
        	session.beginTransaction();
        	List<HashedFile> result = session.createCriteria(HashedFile.class)
        								.add(Restrictions.eq("directory", directory.getPath()))
        								.list();
        	session.getTransaction().commit();
        	return result;
        } catch (NullPointerException e) {
            return Collections.emptyList();
        }
    }

}
