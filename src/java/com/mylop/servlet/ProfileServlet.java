/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mylop.servlet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mylop.bean.Profile;
import com.mylop.model.OtherInfomationModel;
import com.mylop.model.ProfileModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author quangbach
 */
public class ProfileServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProfileServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProfileServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String act = request.getParameter("act");
        if(act.equals("profile")){
            getProfile(request, response);
        }
        if(act.equals("avatar")){
            getAllProfile(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    @SuppressWarnings("empty-statement")
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        updateByJson(request, response);
       
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    public void getProfile(HttpServletRequest request,HttpServletResponse response){
        String userid = request.getParameter("userid");
        ProfileModel pm = new ProfileModel();
        Profile profile = pm.getProfileById(userid);
        Gson gson = new Gson();
        response.setHeader("Content-Type", "application/json");
        try {
            response.getWriter().write(gson.toJson(profile));
        } catch (IOException ex) {
            Logger.getLogger(ProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getAllProfile(HttpServletRequest request,HttpServletResponse response){
        ProfileModel pm = new ProfileModel();
        Gson gson = new Gson();
        String json = gson.toJson(pm.getAllProfile());
        try {
            response.getWriter().write(json);
        } catch (IOException ex) {
            Logger.getLogger(ProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateProfile(HttpServletRequest request, HttpServletResponse response){
        Map<String, String[]> params = request.getParameterMap();
      //  params.remove("act");
        String userid = params.get("userid")[0];
       // params.remove("userid");
        Map<String,String> update = new HashMap<String, String>();
        Iterator keys;
        keys = params.keySet().iterator();
        
        while(keys.hasNext()){
            String s = (String)keys.next();
             if(s != null && !s.equals("act") && !s.equals("userid")){
                update.put(s, params.get(s)[0]);
             }  
        }
        ProfileModel pm = new ProfileModel();
        boolean success = pm.updateProfile(userid, update);
        String json = "{\"success\":\""+success+"\"}";
        response.setHeader("Content-Type", "application/json");
//        json = update.get("nickname")+success;
//        Gson gson = new Gson();
//        json = gson.toJson(update);
        try {
            response.getWriter().write(json);
        } catch (IOException ex) {
            Logger.getLogger(ProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateByJson(HttpServletRequest request, HttpServletResponse response) throws IOException{
        OtherInfomationModel oi = new OtherInfomationModel();
        boolean success = true;
        boolean test;
        StringBuilder params = new StringBuilder();
        String line;
        BufferedReader br = request.getReader();
        while((line=br.readLine())!=null){
            params.append(line);
        }
        response.setHeader("Content-Type", "application/json");
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String,String>>(){}.getType();
        Map<String,String> paramMap = gson.fromJson(params.toString(), mapType);
        paramMap.remove("act");
        String userid = paramMap.get("userid");
        paramMap.remove("userid");
        List<String> listAttr = new ArrayList<String>();
        listAttr.add("userid");
        listAttr.add("fullname");
        listAttr.add("birthday");
        listAttr.add("address");
        listAttr.add("phonenumber");
        listAttr.add("nickname");
        listAttr.add("yearbook");
        listAttr.add("email");
        Map<String,String> profileUpdate = new HashMap<String, String>();
        Map<String,String> infoUpdate = new HashMap<String, String>();
        for (String s : paramMap.keySet()) {
            if(s != null && listAttr.contains(s)){
                profileUpdate.put(s, paramMap.get(s));
            }else{
                if(oi.getInfomatinByName(userid, s)!=0){
                    infoUpdate.put(s, paramMap.get(s));
                }else{
                    success = oi.addInfomation(userid, s, paramMap.get(s));
                    
                }
                
            }
        }
        
        ProfileModel pm = new ProfileModel();
        if(!profileUpdate.isEmpty()){
            test = pm.updateProfile(userid, profileUpdate);
            if(success) success = test;
        }
        if(!infoUpdate.isEmpty()){
            test = oi.updateInfo(userid, infoUpdate);
            if(success) success = test;
        }
        
        String json = "{\"success\":\""+success+"\"}";
        response.getWriter().write(json);
    }

}

