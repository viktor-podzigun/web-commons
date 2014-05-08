
package com.googlecode.common.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import com.googlecode.common.dao.GenericDao;


/**
 * Generic implementation of common DAO. Contains session initialization,
 * and basic CRUD methods.
 *
 * @param <T> entity type
 * @param <K> key type
 */
public class GenericDaoHib<T, K extends Serializable> implements GenericDao<T, K> {

	@PersistenceContext
	private EntityManager  entityManager;

	protected Class<T>     entityClass;
	protected String       className;
	
	
	public GenericDaoHib(Class<T> entityClass) {
		this.entityClass  = entityClass;
		this.className    = entityClass.getSimpleName();
	}

    /**
     * Configure the entity manager to be used.
     * 
     * @param em the {@link EntityManager} to set.
     */
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }
	
	/**
	 * Get only the first entity from the given query or null if none
	 * 
	 * @param query    query to execute
	 * @return         first entity
	 * 
	 * @throws IllegalStateException   if query returns more than one entity
	 */
	protected T getOne(TypedQuery<T> query) {
		return getOneResult(query.getResultList());
	}

    /**
     * Get only the first element from the given result list or null if none
     * 
     * @param resultList    result list of entities
     * @return              first element
     * 
     * @throws IllegalStateException    if list contains more than one entity
     */
    public static <E> E getOneResult(List<E> resultList) {
        final int size = resultList.size();
        if (size == 0) {
            return null;
        }
        
        if (size > 1) {
            throw new IllegalStateException("Obtained " + 
                    size + " elements from DB instead of one.");
        }
        
        return resultList.get(0);
    }

	@Override
	public T get(K key) {
		return getOne(entityManager.createQuery("SELECT d FROM " + className 
		            + " d WHERE d.id = :id", entityClass)
				.setParameter("id", key));		
	}
	
	public T getAndLock(K key, LockModeType lockType) {
		return getOne(entityManager.createQuery("SELECT d FROM " + className 
		            + " d WHERE d.id = :id", entityClass)
				.setLockMode(lockType)
				.setParameter("id", key));
	}

	@Override
	public List<T> get(int firstResult, int maxResults) {
        return entityManager.createQuery("select o from " + className 
                    + " o", entityClass)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
	
	@Override
	public List<T> getAll() {
		return entityManager.createQuery("select o from " + className 
		            + " o", entityClass).getResultList();
	}
    
	/**
	 * Insert the record into DB
	 * 
	 * @param obj
	 */
	@Override
	public void save(T entity) {
		entityManager.persist(entity);
	}

	/**
	 * Saves the record into DB
	 * 
	 * @param obj
	 */
	public void save(T... entities) {
		for(T entity : entities) {
			entityManager.persist(entity);
		}
	}
	
	/**
	 * Update the record in DB
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public T merge(T entity) {
		return entityManager.merge(entity);
	}

	/**
	 * Refreshes entity from database.
	 * 
	 * @param entity entity to refresh.
	 * @return same entity instance.
	 */
	public T refresh(T entity) {
		entityManager.refresh(entity);
		return entity;
	}
	
	/**
	 * Refreshes entity from database and locks it.
	 * <p>!Note: should be called from transaction only.</p>
	 * 
	 * @param entity entity to refresh.
	 * @param lock type to obtain.
	 * @return same entity instance.
	 */
	public T refresh(T entity, LockModeType lockType) {
		entityManager.refresh(entity, lockType);
		return entity;
	}	
	
	@Override
	public void delete(T entity) {
		entityManager.remove(entity);
	}

	@Override
	public long getCount() {
		return entityManager.createQuery(
    		        new StringBuilder("select count(o) from ")
    					.append(className)
    					.append(" o").toString(), 
					Long.class).getSingleResult();
	}

	/**
	 * Remove the given entity from the persistence context, causing a managed 
	 * entity to become detached. Unflushed changes made to the entity if any 
	 * (including removal of the entity), will not be synchronized to the database. 
	 * Entities which previously referenced the detached entity will continue to reference it.
	 * 
	 * @param entity entity instance
	 * @throws IllegalArgumentException - if the instance is not an entity
	 */
	public void detach(T entity) {
		 entityManager.detach(entity);
	}
	
	public void flush() {
		entityManager.flush();
	}
	
	protected EntityManager getEntityManager(){
		return entityManager;
	}

}
