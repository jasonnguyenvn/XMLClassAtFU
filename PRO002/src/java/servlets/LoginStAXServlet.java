/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import Utils.XMLUtilities;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author Hau
 */
public class LoginStAXServlet extends HttpServlet {
    
    private final String xmlFile = "WEB-INF/studentAccounts.xml";
    
    private final String invalidPage = "invalid.html";
    private final String searchPage = "search.jsp";

    
    private boolean found = false;
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
        
        String username = request.getParameter("txtUsername");
        String password = request.getParameter("txtPassword");
        String url = invalidPage;
        
        String fullname = "";
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        InputStream is = null;
        XMLStreamReader reader = null;
        try {
            String realPath = this.getServletContext().getRealPath("/");
            String xmlFilePath = realPath + xmlFile;
            
            is = new FileInputStream(xmlFilePath);
            reader = XMLUtilities.parseFileToStAXCursor(is);
            
            found = false;
            
            while (reader.hasNext()) {
               int cursor = reader.next();
               String id = XMLUtilities.getAttributeValue("student", "", "id", reader);
               if (username.equals(id)) {
                   
                   
                   fullname += XMLUtilities.getTextContent("lastname", reader).trim();
                   fullname += XMLUtilities.getTextContent("middlename", reader).trim();
                   fullname += XMLUtilities.getTextContent("firstname", reader).trim();
                   String passFromXML = XMLUtilities.getTextContent("password", reader).trim();
                   if (passFromXML.equals(password)) {
                       String status = XMLUtilities.getTextContent("status", reader).trim();
                       if (!status.equals("dropout")) {
                           
                           found = true;
                            break;
                       }
                   }
               }
            }
            
            if (found) {
                url = searchPage;
                
                HttpSession session = request.getSession();
                session.setAttribute("USER", username);
                session.setAttribute("FULLNAME", fullname);
            }
            
        } catch (XMLStreamException ex) {
            Logger.getLogger(LoginStAXServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException ex) {
                    Logger.getLogger(LoginStAXServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (is != null) {
                is.close();
            }
            
            RequestDispatcher dr = request.getRequestDispatcher(url);
            dr.forward(request, response);
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
        processRequest(request, response);
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
        processRequest(request, response);
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

}
