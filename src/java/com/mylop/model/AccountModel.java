/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mylop.model;

import com.mylop.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author quangbach
 */
public class AccountModel {
    public String login(String username, String password){
        String success = "";
        Connection con = DatabaseConnection.getConnection();
        String sql = "SELECT userid,username,password FROM mylop.account";
        try {
            Statement stm = con.createStatement();
            ResultSet result = stm.executeQuery(sql);
            while(result.next()){
                if(result.getString("username").equals(username)){
                    if(result.getString("password").equals(password)){
                        success = result.getString("userid");
                        break;
                    }else{
                        success = "";
                    }
                }else{
                    success = "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }
    
    public boolean signUp(String username,String password, String fullname){
        boolean success = true;
        int id = getMemberCount() + 1;
        String userid = "lop_"+id;
        String sql = "INSERT INTO mylop.profile (userid,fullname) VALUES (?,?)";
        String sql2 = "INSERT INTO mylop.account (userid,username,password) VALUES(?,?,?)";
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement pr;
            pr = con.prepareStatement(sql);
            pr.setString(1, userid);
            pr.setString(2, fullname);
            int a = pr.executeUpdate();
            if(a != 0){
                pr = con.prepareStatement(sql2);
                pr.setString(1, userid);
                pr.setString(2,username);
                pr.setString(3, password);
                if(pr.executeUpdate() == 0){
                    success = false;
                }
            }else{
                success = false;
            }
            
               
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }
    
    public int getMemberCount(){
        int count = 0;
        String sql = "SELECT COUNT(*) FROM mylop.profile";
        Connection con = DatabaseConnection.getConnection();
        try {
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                count = rs.getInt(1);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }
    
    public static void main(String[] args) {
        AccountModel ac = new AccountModel();
        System.out.print(ac.login("dangian2", "helloworl1d"));
    }
}
