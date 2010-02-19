/*
 * Copyright (c) 2010.  The Apache Software Foundation
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hbase.hbql;

import org.apache.hadoop.hbase.hbql.client.HBqlException;
import org.apache.hadoop.hbase.hbql.client.HConnection;
import org.apache.hadoop.hbase.hbql.client.HConnectionManager;
import org.apache.hadoop.hbase.hbql.client.HPreparedStatement;
import org.apache.hadoop.hbase.hbql.client.HRecord;
import org.apache.hadoop.hbase.hbql.client.HResultSet;
import org.apache.hadoop.hbase.hbql.client.Util;
import org.apache.hadoop.hbase.hbql.util.TestSupport;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.Random;

public class NullValuesTest extends TestSupport {

    static HConnection connection = null;

    static Random randomVal = new Random();

    @BeforeClass
    public static void beforeClass() throws HBqlException {

        connection = HConnectionManager.newConnection();

        connection.execute("CREATE TEMP MAPPING table30"
                           + "("
                           + "keyval key, "
                           + "f1 ("
                           + "  val1 string alias val1, "
                           + "  val2 date alias val2, "
                           + "  val3 int alias val3, "
                           + "  val4 int[] alias val4, "
                           + "  val5 object alias val5 "
                           + "))");

        if (!connection.tableExists("table30"))
            System.out.println(connection.execute("create table table30 (f1())"));
        else
            System.out.println(connection.execute("delete from table30"));
    }

    /*
    public static void insertRecords(final HConnection connection,
                                     final int cnt,
                                     final String msg) throws HBqlException {

        HPreparedStatement stmt = connection.prepareStatement(
                "insert into table30 "
                + "(keyval, val1, val2, val5, val6, f3mapval1, f3mapval2, val8) values "
                + "(:key, :val1, :val2, :val5, :val6, :f3mapval1, :f3mapval2, :val8)");

        for (int i = 0; i < cnt; i++) {

            final String keyval = Util.getZeroPaddedNonNegativeNumber(i, TestSupport.keywidth);
            keyList.add(keyval);

            int val5 = randomVal.nextInt();
            String s_val5 = "" + val5;
            val1List.add(s_val5);
            val5List.add(val5);

            Map<String, String> mapval1 = Maps.newHashMap();
            mapval1.put("mapcol1", "mapcol1 val" + i + " " + msg);
            mapval1.put("mapcol2", "mapcol2 val" + i + " " + msg);

            Map<String, String> mapval2 = Maps.newHashMap();
            mapval2.put("mapcol1-b", "mapcol1-b val" + i + " " + msg);
            mapval2.put("mapcol2-b", "mapcol2-b val" + i + " " + msg);
            mapval2.put("mapcol3-b", "mapcol3-b val" + i + " " + msg);

            int[] intv1 = new int[5];
            val8check = new int[5];
            for (int j = 0; j < intv1.length; j++) {
                intv1[j] = j * 10;
                val8check[j] = intv1[j];
            }

            stmt.setParameter("key", keyval);
            stmt.setParameter("val1", s_val5);
            stmt.setParameter("val2", s_val5 + " " + msg);
            stmt.setParameter("val5", val5);
            stmt.setParameter("val6", i * 100);
            stmt.setParameter("f3mapval1", mapval1);
            stmt.setParameter("f3mapval2", mapval2);
            stmt.setParameter("val8", intv1);
            stmt.execute();
        }
    }
    */
    @Test
    public void simpleInsert() throws HBqlException {

        HPreparedStatement stmt = connection.prepareStatement(
                "insert into table30 "
                + "(keyval, val1, val2, val3, val4, val5) values "
                + "(:key, :val1, :val2, :val3, :val4, :val5)");

        final String keyval = Util.getZeroPaddedNonNegativeNumber(1, TestSupport.keywidth);

        stmt.setParameter("key", keyval);
        stmt.setParameter("val1", null);
        stmt.setParameter("val2", new Date(System.currentTimeMillis()));
        stmt.setParameter("val3", 0);
        stmt.setParameter("val4", null);
        stmt.setParameter("val5", null);

        stmt.execute();

        //stmt.

        final String query1 = "SELECT * FROM table30";

        HResultSet<HRecord> resultSet = connection.executeQuery(query1);

        int rec_cnt = 0;
        for (HRecord rec : resultSet) {

            String key = (String)rec.getCurrentValue("keyval");
            String val1 = (String)rec.getCurrentValue("val1");
            Date val2 = (Date)rec.getCurrentValue("val2");
            int val3 = (Integer)rec.getCurrentValue("val3");
            int[] val4 = (int[])rec.getCurrentValue("val4");
            Object val5 = (Object)rec.getCurrentValue("val5");

            System.out.println("Current Values: " + keyval
                               + " - " + rec.getCurrentValue("val1")
                               + " - " + rec.getCurrentValue("val2")
                               + " - " + rec.getCurrentValue("val3")
                               + " - " + rec.getCurrentValue("val4")
                               + " - " + rec.getCurrentValue("val5")
            );
            rec_cnt++;
        }

        assertTrue(rec_cnt == 1);
    }
}