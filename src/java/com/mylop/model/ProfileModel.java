/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mylop.model;

import com.mylop.bean.Profile;
import com.mylop.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author quangbach
 */
public class ProfileModel {
    public static String table;
    
    public ProfileModel(){
        table = "mylop.profile";
    }
    
    public ProfileModel(String s){
        table = s;
    }
    
    public Profile getProfileById(String userid){
        Profile profile = null;
        String sql = "SELECT * FROM "+table+" WHERE userid = ?";
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement pr = con.prepareStatement(sql);
            pr.setString(1, userid);
            ResultSet rs = pr.executeQuery();
            while(rs.next()){
                profile = toProfile(rs);
            }
            con.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ProfileModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return profile;
    }
    
    public boolean updateProfile(String userid, Map<String,String> update){
        boolean success = false;
        String presql = "";
        Iterator keys;
        keys = update.keySet().iterator();
       
        while(keys.hasNext()){
            String s = (String)keys.next();
             if(s != null){
                 presql += s + "=\"" + update.get(s);
                 if(keys.hasNext()) presql += "\", ";
                 else presql += "\" ";
             }  
        }
        if(!presql.equals("")){
             String sql = "UPDATE " + table+" SET " + presql + "WHERE userid=?";
             Connection con = DatabaseConnection.getConnection();
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1,userid);
                success = ps.executeUpdate() != 0;
                con.close();
            } catch (SQLException ex) {
                success = false;
                Logger.getLogger(TimelineModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         
        return success;
    }
    
    public HashMap<Integer,Map<String,String>> getAllProfile(){
        HashMap<Integer, Map<String,String>> result = new HashMap<Integer, Map<String,String>>();
        String sql = "SELECT userid,fullname,avatar FROM "+table;
        Connection con  = DatabaseConnection.getConnection();
        int i = 0;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Map<String,String> list = new HashMap<String, String>();
                list.put("userid", rs.getString("userid"));
                list.put("fullname", rs.getString("fullname"));
                list.put("avatar", rs.getString("avatar"));
                result.put(i, list);
                i++;
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProfileModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    private Profile toProfile(ResultSet rs){
        Profile profile = new Profile();
        try {
            profile.setUserId(rs.getString("userid"));
            profile.setBirthday(rs.getDate("birthday"));
            profile.setFullname(rs.getString("fullname"));
            profile.setAddress(rs.getString("address"));
            profile.setAvatar(rs.getString("avatar"));
            profile.setEmail(rs.getString("email"));
            profile.setNickname(rs.getString("nickname"));
            profile.setPhonenumber(rs.getString("phonenumber"));
            profile.setYearbook(rs.getString("yearbook"));
        } catch (SQLException ex) {
            Logger.getLogger(ProfileModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return profile;
    }
    public static void main(String[] args) {
        ProfileModel pm = new ProfileModel();
        Map<String,String> map = new HashMap<String, String>();
        map.put("nickname", "asdfasdfasdfa");
        boolean success = pm.updateProfile("1",map);
        System.out.print(success);
    }

   
}
