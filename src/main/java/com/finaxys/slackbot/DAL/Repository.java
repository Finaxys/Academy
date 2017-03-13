package com.finaxys.slackbot.DAL;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@org.springframework.stereotype.Repository
@Transactional
public class Repository<T, K extends Serializable> {

	@Autowired
	private SessionFactory sessionFactory;
	private Class persistentClass;

	public Repository() {
	}

	public Repository(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	public void setPersistentClass(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
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
		return sessionFactory.getCurrentSession().createQuery("from " + persistentClass.getSimpleName()).list();
	}

	public List<T> getSomeUsers(int n) {
		return sessionFactory.getCurrentSession().createQuery("from " + persistentClass.getSimpleName())
				.setMaxResults(n).list();
	}

	public T findById(K id) {
		return (T) sessionFactory.getCurrentSession().get(persistentClass, id);
	}

	public void saveOrUpdate(T entity) {
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
	}
}
