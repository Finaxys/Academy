package com.finaxys.slackbot.DAL;

import com.finaxys.slackbot.Utilities.Timer;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

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
        Timer.elapsed("hibernate1 ");
        // args check
        if (orderedByField == null || orderedByField.isEmpty())
            throw new IllegalArgumentException("orderedByField must not be null or empty.");
        if (rowsCount < 1)
            throw new IllegalArgumentException("rowsCount=" + rowsCount + ". Must be > 0.");
        // work
        Timer.elapsed("hibernate2 ");
        List<T> x = sessionFactory.getCurrentSession()
                .createQuery("from " + persistentClass.getSimpleName()
                        + " f ORDER BY f." + orderedByField + (ascending ? "" : " DESC"))
                .setMaxResults(rowsCount)
                .list();
        Timer.elapsed("hibernate3 ");
        return x;
    }

    public T findById(K id) {
        return (T) sessionFactory.getCurrentSession().get(persistentClass, id);
    }

    public void saveOrUpdate(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    public List<T> getByCriterion(String criterion, Object criterionValue) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(persistentClass);
        criteria.add(Restrictions.eq(criterion, criterionValue));
        return criteria.list();
    }
}
