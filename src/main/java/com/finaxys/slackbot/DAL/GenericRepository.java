package com.finaxys.slackbot.DAL;

import java.io.Serializable;
import java.util.List;

/**
 * Created by inesnefoussi on 3/6/17.
 */
public interface GenericRepository<T,K extends Serializable> {

    void addEntity(T entity);
    void updateEntity(T entity);
    void delete(T entity);
    List<T> getAll();

    public T findById(K id) ;

}
