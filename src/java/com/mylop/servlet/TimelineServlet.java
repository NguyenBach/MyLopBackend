/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mylop.servlet;

import com.google.gson.Gson;
import com.mylop.bean.Timeline;
import com.mylop.model.TimelineModel;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author quangbach
 */
public class TimelineServlet extends HttpServlet {
    public static final String UPLOAD_DIRECTORY = "/var/lib/tomcat7/webapps/Timeline";
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
            out.println("<title>Servlet TimelineServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TimelineServlet at " + request.getContextPath() + "</h1>");
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
        if(act.equals("timeline")){
            getTimeLine(request, response);
        }
        if(act.equals("all")){
            getAllTimeline(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        HttpSession session = request.getSession();
        String userid = (String)session.getAttribute("userid");
        String status = "aaa";
        String urlTimelineImage = "";
        String title = "";
        String subtitle = "";
        String content = "";
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        TimelineModel tm = new TimelineModel();
        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            try {
                List<FileItem> multiparts = upload.parseRequest(request);
                for (FileItem item : multiparts) {

                    if (!item.isFormField()) {
                        String contentType = item.getContentType();
                        String fileName = UPLOAD_DIRECTORY + File.separator + userid + "_" + (tm.getLastIndex()+1);
                        File file = new File(fileName);
                        item.write(file);
                        urlTimelineImage = "http://mylop.tk:8080/Timeline/" + file.getName();
                    } else {
                        String fieldName = item.getFieldName();
                       
                        if (fieldName.equals("title")) {
                           title = item.getString();
                        }
                         if (fieldName.equals("subtitle")) {
                           subtitle = item.getString();
                        }
                          if (fieldName.equals("content")) {
                           content = item.getString();
                        }
                          if (fieldName.equals("date")) {
                            Long dateLong = Long.parseLong(item.getString());
                            date = new Date(dateLong);
                        }
                       
                    }
                }

            
             tm.addTimeline(userid, title, subtitle, date, content, urlTimelineImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String json = "{\"message\": \"success\"}";
        response.getWriter().write(json);
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
    
    public void getTimeLine(HttpServletRequest request, HttpServletResponse response){
        TimelineModel tm = new TimelineModel();
        String dateParameter = request.getParameter("date");
        String json = "";
        long dateLong = Long.parseLong(dateParameter);
        Date date = new Date(dateLong);
        List<Timeline> list = tm.getTimeLinesByDate(date);
        Gson gson = new Gson();
        json = gson.toJson(list);
        response.setHeader("Content-Type", "application/json");
        try {
            response.getWriter().write(json);
        } catch (IOException ex) {
            Logger.getLogger(TimelineServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
   public void getAllTimeline(HttpServletRequest request,HttpServletResponse response) {
        TimelineModel tm = new TimelineModel();
        String json = "";
        List<Timeline> list = tm.getAllTimeline();
        Gson gson = new Gson();
        json = gson.toJson(list);
        response.setHeader("Content-Type", "application/json");
        try {
            response.getWriter().write(json);
        } catch (IOException ex) {
            Logger.getLogger(TimelineServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
   }
    

}
