package com.finaxys.slackbot.DAL;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

/**
 * Created by inesnefoussi on 3/6/17.
 */
@Repository
@Transactional
public class GenericRepositoryImpl<T,K extends Serializable> implements GenericRepository<T,K> {

    @Autowired
    private SessionFactory sessionFactory;
    private Class persistentClass;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public GenericRepositoryImpl() {
        persistentClass=getClass();

    }

    public GenericRepositoryImpl(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public void setPersistentClass(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
        return sessionFactory.getCurrentSession().createQuery("from "+persistentClass.getSimpleName()).list();
    }

    public T findById(K id) {
        return (T) sessionFactory.getCurrentSession().get(persistentClass,id);
    }
}
