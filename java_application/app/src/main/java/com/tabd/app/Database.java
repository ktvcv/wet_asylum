/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tabd.app;

import java.math.BigDecimal;
import java.util.Map;
import java.sql.*;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.pool.OracleDataSource;

/**
 *
 * @author oem
 */
public class Database
{
    
    private ProcessBuilder pb;
    private Map<String, String> env;
    private Connection con;
    
    public Database(ProcessBuilder pb) throws SQLException
    {
        this.pb = pb;
        this.env = pb.environment();
        
        this.createConnection();
    }
    
    public Database(ProcessBuilder pb, boolean createConnection) throws SQLException
    {
        this.pb = pb;
        this.env = pb.environment();
        
        if(createConnection)
            this.createConnection();
    }
    
    public boolean createConnection()
    {
        try
        {
            String host   = this.env.get("DB_HOST");
            String dbuser = this.env.get("DB_USER");
            String dbpass = this.env.get("DB_PASS");
            
            if(this.con == null || this.con.isClosed())
            {
                OracleDataSource ds = new OracleDataSource();
                ds.setURL("jdbc:oracle:thin:@//" + host + ":1521/XE");
                this.con = ds.getConnection(dbuser, dbpass);
            }
            return true;
        } catch(Exception ex)
        {
            return false;
        }
    }
    
    public Connection getConnection()
    {
        return this.con;
    }
    
    public boolean destroyConnection()
    {
        try
        {
            if(!this.con.isClosed())
                this.con.close();
            
            return this.con.isClosed();
        } catch(Exception ex)
        {
            return false;
        }
    }
    
    public ResultSet selectByTable(String table) throws SQLException
    {
        return selectByTable(this.con, table);
    }
    
    public ResultSet selectByTable(Connection con, String table) throws SQLException
    {
        
        String query = "select * from " + table;
        Statement ps = this.con.createStatement();
        
        ResultSet rs = ps.executeQuery(query);
        
        return rs;
    }
    
    public ResultSet searchInTableByValue(String table, String[] columns, Object[] values, boolean condi) throws SQLException
    {
        String[] conditions = new String[columns.length];
        for (int i = 0; i < conditions.length; i++)
        {
            if(condi) conditions[i] = " and ";
            else      conditions[i] = " or ";
        }
        return searchInTableByValue(this.con, table, columns, values, conditions);
    }
    
    public ResultSet searchInTableByValue(String table, String[] columns, Object[] values, String[] conditions) throws SQLException
    {
        return searchInTableByValue(this.con, table, columns, values, conditions);
    }
    
    public ResultSet searchInTableByValue(String table, String[] columns, Object[] values) throws SQLException
    {
        String[] conditions = {};
        return searchInTableByValue(this.con, table, columns, values, conditions);
    }
    
    public ResultSet searchInTableByValue(Connection con, String table, String[] columns, Object[] values, String[] conditions) throws SQLException
    {
        if(columns.length > 0)
        {
            String query = "select * from " + table + " where ";
            
            for (int i = 0; i < columns.length - 1; i++)
            {
                String condi = (conditions.length > 0) ? conditions[i] : "";
                
                if(values[i] != null)
                    if(columns[i].contains("like"))
                        query += columns[i] + " ? " + condi;
                    else
                        query += columns[i] + " = ? " + condi;
                else
                    query += columns[i] + " is ? " + condi;
            }
            
            if(values[columns.length - 1] != null)
                if(columns[columns.length - 1].contains("like"))
                    query += columns[columns.length - 1] + " ? ";
                else
                    query += columns[columns.length - 1] + " = ? ";
            else
                query += columns[columns.length - 1] + " is ? ";
            
            PreparedStatement ps = con.prepareStatement(query);
            
            int i = 1;
            for (Object value : values)
            {
                ps.setObject(i++, value);
            }
            
            ResultSet rs = ps.executeQuery();
            return rs;
        } else
        {
            return this.selectByTable(con, table);
        }
    }
    
    // New code with call to procedures and functions of oracle database
    public Struct callFunction(String function, Object[] values, int TypeReturn, String TypeName) throws SQLException
    {
        String parameters;
        if(values.length == 1)
        {
            parameters = " (?) ";
        } else if(values.length > 1)
        {
            parameters = " (";
            for(int i = 0; i < values.length - 1; i++)
            {
                parameters += " ?,";
            }
            parameters += " ?) ";
        } else
        {
            return null;
        }
        OracleCallableStatement cs = (OracleCallableStatement) this.con.prepareCall ( "{? = call SHELTER." + function + parameters + "}" );
        int count = 2;
        for(Object o : values)
        {
            cs.setObject(count++, o);
        }
        cs.registerOutParameter(1, TypeReturn, "ADDRESS_OBJTYP"); 
        
        cs.execute();
        
        return (Struct) cs.getObject(1);
    }
    
    public Struct callFunction(String function, Object[] values, int TypeReturn) throws SQLException
    {
        String parameters;
        if(values.length == 1)
        {
            parameters = " (?) ";
        } else if(values.length > 1)
        {
            parameters = " (";
            for(int i = 0; i < values.length - 1; i++)
            {
                parameters += " ?,";
            }
            parameters += " ?) ";
        } else
        {
            return null;
        }
        OracleCallableStatement cs = (OracleCallableStatement) this.con.prepareCall ( "{? = call SHELTER." + function + parameters + "}" );
        int count = 2;
        for(Object o : values)
        {
            cs.setObject(count++, o);
        }
        cs.registerOutParameter(1, TypeReturn); 
        
        cs.execute();
        
        return (Struct) cs.getObject(1);
    }
    
    public int insertInTable(String procedure, Object[] values) throws SQLException
    {
        String parameters;
        if(values.length == 1)
        {
            parameters = " (?) ";
        } else if(values.length > 1)
        {
            parameters = " (";
            for(int i = 0; i < values.length - 1; i++)
            {
                parameters += " ?,";
            }
            parameters += " ?) ";
        } else
        {
            return 0;
        }
        
        CallableStatement cs = this.con.prepareCall ( "{call SHELTER." + procedure + parameters + "}" );
        int count = 1;
        for(Object o : values)
        {
            cs.setObject(count++, o);
        }
        
        return cs.executeUpdate();
    }
    
    public int updateInTable(String procedure, BigDecimal id, Object newValue) throws SQLException
    {
        CallableStatement cs = this.con.prepareCall ( "{call SHELTER." + procedure + " (?, ?)}" );
        cs.setBigDecimal(1, id);
        cs.setObject(2, newValue);
        return cs.executeUpdate();
    }
    
    public int destroyInTable(String procedure, BigDecimal id) throws SQLException
    {
        CallableStatement cs = this.con.prepareCall ( "{call SHELTER." + procedure + " (?)}" );
        cs.setBigDecimal(1, id);
        return cs.executeUpdate();
    }
}
