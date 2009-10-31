package org.apache.hadoop.hbase.contrib.hbql.statement;

import org.apache.expreval.client.HBqlException;
import org.apache.hadoop.hbase.contrib.hbql.client.HSchemaManager;
import org.apache.hadoop.hbase.contrib.hbql.schema.HBaseSchema;

public abstract class SchemaStatement implements ShellStatement {

    private final String schemaName;
    private volatile HBaseSchema schema = null;

    protected SchemaStatement(final String schemaName) {
        this.schemaName = schemaName;
    }

    protected final String getSchemaName() {
        return schemaName;
    }

    public final HBaseSchema getSchema() throws HBqlException {

        if (this.schema == null) {
            synchronized (this) {
                if (this.schema == null)
                    this.schema = HSchemaManager.findSchema(this.getSchemaName());
            }
        }
        return this.schema;
    }
}