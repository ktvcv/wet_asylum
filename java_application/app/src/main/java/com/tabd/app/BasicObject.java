/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tabd.app;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class BasicObject
{
    private Database db;
    private BigDecimal id;
    private String table;
    
    public BasicObject(Database db, BigDecimal id, String table) throws SQLException
    {
        this.db = db;
        this.id = id;
        this.table = table;
    }
    
    public Map<String, Object> getAttributes() throws SQLException
    {
        Map<String, Object> atributos = new LinkedHashMap<>();
        
        String[] columns    = { "id" };
        BigDecimal[] values = { this.id };
        ResultSet rs = this.db.searchInTableByValue(this.table, columns, values);
        
        if(rs.next())
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++ )
            {
                String nameAttr = rsmd.getColumnName(i);
                atributos.put(nameAttr, rs.getObject(nameAttr));
            }
        }
        
        return atributos;
    }
    
    public boolean exists() throws SQLException
    {
        return !this.getAttributes().isEmpty();
    }
    
    public boolean update(String procedure, Object newValue) throws SQLException
    {
        return this.db.updateInTable(procedure, this.id, newValue) > 0;
    }
    
    public boolean destroy(String procedure) throws SQLException
    {
        return this.db.destroyInTable(procedure, this.id) > 0;
    }
    
    public void print() throws SQLException
    {
        Map<String, Object> atributos = getAttributes();
        
        for(String key : atributos.keySet())
        {
            Object value = (Object) atributos.get(key);
            
            if(value instanceof Struct)
            {
                Struct valueStruct = (Struct) value;
                Object[] valueAttributes = valueStruct.getAttributes();
                
                System.out.println(key + ": ");
                System.out.println("======================");
                
                for(Object attribute : valueAttributes)
                {
                    System.out.println(attribute);
                }
                System.out.println("======================");
            } else if(value instanceof Array)
            {
                Array valueArray = (Array) value;
                ResultSet rs = valueArray.getResultSet();
                
                System.out.println(key + ": ");
                System.out.println("======================");
                
                while(rs.next())
                {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    
                    for(int i = 0; i < columnCount; i++)
                    {
                        System.out.println(rs.getObject(i));
                    }
                }
                
                System.out.println("======================");
            } else
            {
                System.out.println(key + ": " + value);
            }
        }
    }
    
    public static boolean create(Database db, Object[] values, String procedure) throws SQLException
    {
        return db.insertInTable(procedure, values) > 0;
    }
    
    public static ArrayList<BasicObject> listAll(Database db, String table) throws SQLException
    {
        ResultSet rset = db.selectByTable(table);
        ArrayList<BasicObject> array = new ArrayList<>();
        
        while(rset.next())
        {
            Object id = rset.getObject("id");
            array.add(new BasicObject(db, (BigDecimal) id, table));
        }
        
        return array;
    }
    
    public static void print(ArrayList<BasicObject> objects) throws SQLException
    {
        for(BasicObject bo : objects)
        {
            bo.print();
            System.out.println("=======================================");
            System.out.println("=======================================");
        }
    }
}
