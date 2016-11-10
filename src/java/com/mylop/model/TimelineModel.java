/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mylop.model;

import com.mylop.bean.Timeline;
import com.mylop.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class TimelineModel {
    public static String table;
    
    public TimelineModel(){
        table = "mylop.timeline";
    }
    
    public TimelineModel (String tablename){
        table = tablename;
    }
    
    public List<Timeline> getTimeLinesByDate(Date date){
        List<Timeline> list = new ArrayList<Timeline>();
        String sql = "SELECT * FROM "+table +" WHERE dateadd = ?";
        Connection con = DatabaseConnection.getConnection();
        java.sql.Date sqldate = new java.sql.Date(date.getTime());
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, sqldate);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(toTimeline(rs));
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(TimelineModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public Timeline getTimelineById(int id){
        Timeline timeline = null;
        String sql = "SELECT * FROM "+table+" WHERE id = ?";
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                timeline = toTimeline(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimelineModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return timeline;
    }
    
    public boolean addTimeline(String userid, String t, String st,Date date,String ct,String img){
        boolean success = true;
        String sql = "INSERT INTO "+table+" (userid,title,subtitle,dateadd,content,imageimage) VALUES(?,?,?,?,?,?)";
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, userid);
            ps.setString(2, t);
            ps.setString(3, st);
            java.sql.Date sqldate = new java.sql.Date(date.getTime());
            ps.setDate(4, sqldate);
            ps.setString(5, ct);
            ps.setString(6, img);
            int a = ps.executeUpdate();
            if(a == 0) success = false;
     
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(TimelineModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }
    
    public boolean updateTimeline(int id, Map<String,String> update){
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
             String sql = "UPDATE " + table+" SET " + presql + "WHERE id=?";
             Connection con = DatabaseConnection.getConnection();
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, id);
                success = ps.executeUpdate() != 0;
                con.close();
            } catch (SQLException ex) {
                success = false;
                Logger.getLogger(TimelineModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         
        return success;
    }
    
    public List<Timeline> getAllTimeline() {
        List<Timeline> timelines = new ArrayList<Timeline>();
        String sql = "SELECT * FROM "+ table ;
        Connection con = DatabaseConnection.getConnection();
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
             while(rs.next()){
                timelines.add(toTimeline(rs));
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(TimelineModel.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return timelines;
    }
    
    public int getLastIndex(){
        int lastIndex = 0;
        String sql = "SELECT id FROM "+table+" ORDER BY id DESC LIMIT 1";
        Connection con = DatabaseConnection.getConnection();
        try {
            Statement ps = con.createStatement();
           
            ResultSet rs = ps.executeQuery(sql);
            while(rs.next()){
                lastIndex = rs.getInt("id");
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(TimelineModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lastIndex;
    }
    
    private Timeline toTimeline(ResultSet rs) throws SQLException{
        Timeline timeline = new Timeline();
        timeline.setId(rs.getInt("id"));
        timeline.setUserId(rs.getString("userid"));
        timeline.setContent(rs.getString("content"));
        timeline.setDateAdd(rs.getDate("dateadd"));
        timeline.setImageUrl(rs.getString("imageimage"));
        timeline.setTitle(rs.getString("title"));
        timeline.setSubTitile(rs.getString("subtitle"));
        return timeline;
    }
    public static void main(String[] args) throws SQLException {
        TimelineModel tm = new TimelineModel();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 9);
        calendar.set(Calendar.MONTH, 9);
        calendar.set(Calendar.YEAR, 2016);
         Date date = calendar.getTime();
      
       System.out.print(tm.getAllTimeline());
    }
}
