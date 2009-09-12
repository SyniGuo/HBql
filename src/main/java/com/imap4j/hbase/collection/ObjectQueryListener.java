package com.imap4j.hbase.collection;

import com.imap4j.hbase.hbase.HPersistException;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Aug 20, 2009
 * Time: 10:38:45 PM
 */
public interface ObjectQueryListener<T> {

    void onQueryInit();

    void onEachObject(T val) throws HPersistException;

    void onQueryComplete();

}