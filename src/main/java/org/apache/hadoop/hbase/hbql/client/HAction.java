package org.apache.hadoop.hbase.hbql.client;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Oct 6, 2009
 * Time: 1:00:19 PM
 */
public class HAction {

    private enum Type {
        INSERT, DELETE
    }

    private final Type type;
    private final Object actionValue;

    HAction(final Type type, final Object actionValue) {
        this.type = type;
        this.actionValue = actionValue;
    }

    static HAction newInsert(final Put put) {
        return new HAction(Type.INSERT, put);
    }

    static HAction newDelete(final Delete delete) {
        return new HAction(Type.DELETE, delete);
    }

    private boolean isInsert() {
        return this.type == Type.INSERT;
    }

    private boolean isDelete() {
        return this.type == Type.DELETE;
    }

    private Put getPutValue() {
        return (Put)this.actionValue;
    }

    private Delete getDeleteValue() {
        return (Delete)this.actionValue;
    }

    public void apply(final org.apache.hadoop.hbase.client.HTable table) throws IOException {

        if (this.isInsert())
            table.put(this.getPutValue());

        if (this.isDelete())
            table.delete(this.getDeleteValue());
    }

    public String toString() {
        return this.type.name();
    }
}