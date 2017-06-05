/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import Utils.XMLUtilities;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Hau
 */
public class CreateStudentServlet extends HttpServlet {
    
    private final String xmlFile = "WEB-INF/studentAccounts.xml";

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
        
        String url = "login.html";
        
        String txtId = request.getParameter("txtId");
        String txtClass = request.getParameter("txtClass");
        String txtLastName = request.getParameter("txtLastName");
        String txtMiddleName = request.getParameter("txtMiddleName");
        String txtFirstName = request.getParameter("txtFirstName");
        String chkSex = request.getParameter("chkSex");
        String txtSex = "0";
        if (chkSex != null) {
            txtSex = "1";
        }
        String txtPassword = request.getParameter("txtPassword");
        String txtAddress = request.getParameter("txtAddress");
   
        try {
            HttpSession session = request.getSession(false);
           if (session != null) {
               Document doc = (Document) session.getAttribute("DOMTREE");
               if (doc != null) {
                   Map<String, String> studentAttrs = new HashMap<String, String>();
                   
                   studentAttrs.put("id", txtId);
                   studentAttrs.put("class", txtClass);
                   
                   Element student = XMLUtilities.createNode(doc, "student", 
                           null, studentAttrs);
                   Element lastname = XMLUtilities.createNode(doc, "lastname", 
                           txtLastName, null);
                   Element middlename = XMLUtilities.createNode(doc, "middlename", 
                           txtMiddleName, null);
                   Element firstname = XMLUtilities.createNode(doc, "firstname", 
                           txtFirstName, null);
                   Element sex = XMLUtilities.createNode(doc, "sex", 
                           txtSex, null);
                   Element password = XMLUtilities.createNode(doc, "password", 
                           txtPassword, null);
                   Element address = XMLUtilities.createNode(doc, "address", 
                           txtAddress, null);
                   Element status = XMLUtilities.createNode(doc, "status", 
                           "break", null);
                   
                   student.appendChild(lastname);
                   student.appendChild(middlename);
                   student.appendChild(firstname);
                   student.appendChild(sex);
                   student.appendChild(password);
                   student.appendChild(address);
                   student.appendChild(status);
                   
                   
                   // get root node of document : doc.getDocumentElement();
                   
                   NodeList studentsLst = doc.getElementsByTagName("students");
                   Node students = studentsLst.item(0);
                   students.appendChild(student);
                   
                   String realPath = this.getServletContext().getRealPath("/");
                    String xmlOutputFilePath = realPath + xmlFile;
                    XMLUtilities.transformDOMToStream(doc, xmlOutputFilePath);
               }
           }
        } catch (TransformerException ex) {
            Logger.getLogger(CreateStudentServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {            
            response.sendRedirect(url);
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
