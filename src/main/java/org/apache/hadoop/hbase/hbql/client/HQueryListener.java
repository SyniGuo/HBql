package org.apache.hadoop.hbase.hbql.client;

public interface HQueryListener<T> {

    void onQueryInit();

    void onEachRow(T val);

    void onQueryComplete();
}
