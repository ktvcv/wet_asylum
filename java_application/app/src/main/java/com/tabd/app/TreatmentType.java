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

public class TreatmentType extends BasicObject
{
    public TreatmentType(Database db, BigDecimal id) throws SQLException
    {
        super(db, id, " TreatmentType_objtab ");
    }
    
    public void printTreatmentType() throws SQLException
    {
        super.print();
    }
    
    public static boolean createTreatmentType(Database db, Object[] values) throws SQLException
    {
        return BasicObject.create(db, values, "createTreatmentType");
    }
    
    public static ArrayList<BasicObject> listAllTreatmentTypes(Database db) throws SQLException
    {
        return BasicObject.listAll(db, "TreatmentType_objtab");   
    }
    
    public static void printTreatmentTypes(ArrayList<BasicObject> treatments) throws SQLException
    {
        BasicObject.print(treatments);
    }
}
