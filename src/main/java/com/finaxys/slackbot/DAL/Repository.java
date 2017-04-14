package com.finaxys.slackbot.DAL;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Repository
@Transactional
public class Repository<T, K extends Serializable> {

    @Autowired
    private SessionFactory 	sessionFactory;
    private Class 			persistentClass;

    public Repository() {
    }

    public Repository(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public T addEntity(T entity) {
        return (T) sessionFactory.getCurrentSession().save(entity);
    }

    public void updateEntity(T entity) {
        sessionFactory.getCurrentSession().update(entity);

    }

    public void delete(T entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    public List<T> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from " + persistentClass.getSimpleName())
                .list();
    }

    public List<T> getAllOrderedByAsList(String orderedByField, boolean ascending, int rowsCount) throws IllegalArgumentException {
    	
    	// args check
        if (orderedByField == null || orderedByField.isEmpty())
            throw new IllegalArgumentException("orderedByField must not be null or empty.");

        if (rowsCount < 1 && rowsCount != -1)
            throw new IllegalArgumentException("rowsCount=" + rowsCount + ". Must be > 0 or -1");
    	
    	Query query = sessionFactory.getCurrentSession().createQuery("from " + persistentClass.getSimpleName() + " f ORDER BY f." + orderedByField + (ascending ? "" : " DESC"));
    	
    	if (rowsCount > 0)
    	{
    		query.setMaxResults(rowsCount);
    	}
    	
    	List<T> x = query.list();
    	
    	return x;
    }

    public T findById(K id) {
        return (T) sessionFactory.getCurrentSession().get(persistentClass, id);
    }

    public void saveOrUpdate(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    public List<T> getByCriterion(Object ...objects)
    {
    	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(persistentClass);
    	
    	for (int i = 0; i < objects.length - 1; i+=2)
    		criteria.add(Restrictions.eq(objects[i].toString(), objects[i+1]));
    		
    	return criteria.list();
    }
}
