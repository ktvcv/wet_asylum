/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tabd.app;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;

public class Family extends BasicObject
{
    public Family(Database db, BigDecimal id) throws SQLException
    {
        super(db, id, " Family_objtab ");
    }
    
    public boolean familyExists() throws SQLException
    {
        return this.exists();
    }
    
    public boolean updatePhone(String phone) throws SQLException
    {
        return this.update("setFamilyPhone", phone);
    }
    
    public boolean updateEmail(String email) throws SQLException
    {
        return this.update("setFamilyEmail", email);
    }
    
    public boolean updateAddress(Struct address) throws SQLException
    {
        return this.update("setFamilyAddress", address);
    }
    
    public boolean destroy() throws SQLException
    {
        return super.destroy("deleteFamily");
    }
    
    public void printFamily() throws SQLException
    {
        super.print();
    }
    
    public static boolean createFamily(Database db, Object[] values) throws SQLException
    {
        return BasicObject.create(db, values, "createFamily");
    }
    
    public static ArrayList<BasicObject> listAllFamilys(Database db) throws SQLException
    {
        return BasicObject.listAll(db, "Family_objtab");   
    }
    
    public static void printFamilys(ArrayList<BasicObject> familys) throws SQLException
    {
        BasicObject.print(familys);
    }
    
    public static ArrayList<BasicObject> searchFamilys(Database db, String familyName, boolean useLike) throws SQLException
    {
        String[] columns = { " lower(familyname) like " };
        Object[] values  = { (useLike) ? "%" + familyName + "%" : familyName };
        ResultSet rset = db.searchInTableByValue("Family_objtab", columns, values);
        
        ArrayList<BasicObject> f = new ArrayList<>();
            
        while(rset.next())
        {
            Object id = rset.getObject("id");
            f.add(new Family(db, (BigDecimal) id));
        }
        
        return f;
    }
}
