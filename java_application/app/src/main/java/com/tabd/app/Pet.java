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
import java.util.ArrayList;
import java.util.Map;

public class Pet extends BasicObject
{
    public Pet(Database db, BigDecimal id) throws SQLException
    {
        super(db, id, " Pet_objtab ");
    }
    
    public boolean petExists() throws SQLException
    {
        return this.exists();
    }
    
    public boolean updateDateBirth(String date) throws SQLException
    {
        return this.update("setPetDateOfBirth", date);
    }
    
    public boolean updateName(String name) throws SQLException
    {
        return this.update("setPetName", name);
    }
    
    public boolean updateType(String type) throws SQLException
    {
        return this.update("setPetType", type);
    }
    
    public boolean setFamily(BigDecimal idFamily) throws SQLException
    {
        return this.update("adoptByFamily", idFamily);
    }
    
    public boolean deleteFamily() throws SQLException
    {
        return super.destroy("cancelAdoption");
    }
    
    public boolean destroy() throws SQLException
    {
        return super.destroy("deleteFamily");
    }
    
    public void printTreatmentList() throws SQLException
    {
        Map<String, Object> atributos = this.getAttributes();
        
        Array valueArray = (Array) atributos.get("TREATMENTS_LIST");
        ResultSet rs = valueArray.getResultSet();

        System.out.println("Treatments List: ");
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
    }
    
    public void printPet() throws SQLException
    {
        super.print();
    }
    
    public static boolean createPet(Database db, Object[] values) throws SQLException
    {
        return BasicObject.create(db, values, "createPet");
    }
    
    public static ArrayList<BasicObject> listAllPets(Database db) throws SQLException
    {
        return BasicObject.listAll(db, "Pet_objtab");   
    }
    
    public static void printPets(ArrayList<BasicObject> pets) throws SQLException
    {
        BasicObject.print(pets);
    }
    
    public static ArrayList<Pet> searchPets(Database db, String name, boolean useLike) throws SQLException
    {
        String[] columns = { " lower(name) like " };
        Object[] values  = { (useLike) ? "%" + name + "%" : name };
        ResultSet rset = db.searchInTableByValue("Pet_objtab", columns, values);
        
        ArrayList<Pet> p = new ArrayList<>();
            
        while(rset.next())
        {
            Object id = rset.getObject("id");
            p.add(new Pet(db, (BigDecimal) id));
        }
        
        return p;
    }
}
