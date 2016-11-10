/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mylop.model;

import com.mylop.bean.OtherInfomation;
import com.mylop.database.DatabaseConnection;
import static com.mylop.model.ProfileModel.table;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class OtherInfomationModel {
    public static String table;
    
    public OtherInfomationModel(){
        table = "mylop.otherinfomation";
    }
    
    public OtherInfomationModel(String s){
        table = s;
    }
    
    public List<OtherInfomation> getInfomationsById(String userid){
        List<OtherInfomation> list = new ArrayList<OtherInfomation>();
        String sql = "SELECT * FROM "+table+" WHERE userid = ?";
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, userid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(toInfomation(rs));
            }
            con.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(OtherInfomationModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    
    public int getInfomatinByName(String userid,String info){
        int hasInfo = 0;
        String sql = "SELECT * FROM "+table+" WHERE userid = ?";
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, userid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                if(rs.getString("infomation").equals(info)){
                    hasInfo = rs.getInt("id"); 
                    break;
                }
            }
            con.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(OtherInfomationModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasInfo;
    }
    
    public boolean deleteInfomation(String userid,String info){
        boolean success = true;
        String sql = "DELETE FROM "+table+" WHERE userid = ? AND infomation = ?";
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, userid);
            pst.setString(2, info);
            if(pst.executeUpdate() == 0){
                success = false;
            }
            con.close();
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(OtherInfomationModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }
    
    public boolean addInfomation(String userid,String infomation, String value){
        boolean success = true;
        String sql = "INSERT INTO "+table+" (userid,infomation,value) VALUES (? , ? ,?)";
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,userid);
            ps.setString(2, infomation);
            ps.setString(3, value);
            if(ps.executeUpdate() == 0){
                success = false;
            }
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(OtherInfomationModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }
    
    public boolean updateInfomation(OtherInfomation info){
        boolean success = true;
        String sql = "UPDATE "+table+" SET infomation = ?, value = ? WHERE id = ?";
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, info.getInfomation());
            ps.setString(2, info.getValue());
            ps.setInt(3, info.getId());
            if(ps.executeUpdate() == 0){
                success = false;
            }
            con.close();
        } catch (SQLException ex) {
            success = false;
            
            Logger.getLogger(OtherInfomationModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return success;
    }
    
     public boolean updateInfo(String userid, Map<String,String> update){
        boolean success = false;
        Iterator keys = update.keySet().iterator();
        while(keys.hasNext()){
            String s = (String)keys.next();
             if(s != null){
                 int id = getInfomatinByName(userid, s);
                 if(id != 0){
                     OtherInfomation ot = new OtherInfomation(userid, s, update.get(s));
                     ot.setId(id);
                    success = updateInfomation(ot);
                 }
                
             }  
        }
        
         
        return success;
    }
    
    private OtherInfomation toInfomation(ResultSet rs) throws SQLException{
        OtherInfomation result = new OtherInfomation();
        result.setId(rs.getInt("id"));
        result.setUserId(rs.getString("userid"));
        result.setInfomation(rs.getString("infomation"));
        result.setValue(rs.getString("value"));
        return result;
    }
    public static void main(String[] args) {
        Map<String,String> map = new HashMap<String, String>();
        map.put("abc", "asdfasdfasdf");
        OtherInfomationModel m = new OtherInfomationModel();
        boolean success = m.updateInfo("lop_2", map);
        System.out.print(success);
    }
}
