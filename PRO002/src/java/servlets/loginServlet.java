/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Hau
 */
public class loginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private final String xmlFile = "WEB-INF/studentAccounts.xml";
    private final String invalidPage = "invalid.html";
    private final String searchPage = "search.jsp";
    
    private boolean bFound;
    private String fullName;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String username = request.getParameter("txtUsername");
        String password = request.getParameter("txtPassword");
        String url = invalidPage;
        
        try  {
          String realPath = getServletContext().getRealPath("/");
          String xmlFilePath = realPath  + xmlFile;
          
          Document doc = Utils.XMLUtilities.parseDomFromFile(xmlFilePath);
          
          bFound = false;
          
            checkLogin(username, password, doc);
            
            if (bFound) {
                url = searchPage;
                HttpSession session = request.getSession();
                session.setAttribute("USER", username);
                session.setAttribute("FULLNAME", fullName);
                session.setAttribute("DOMTREE", doc);
            }
                
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(loginServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(loginServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
                RequestDispatcher dr = request.getRequestDispatcher(url);
                dr.forward(request, response);
            
            out.close();
        }
    }
    
    private void checkLogin(String username, String password, Node node) {
        if (node == null || bFound) {
            return;
        }
        
        if (node.getNodeName().equals("student")) {
          
            NamedNodeMap attrs = node.getAttributes();
            String id = attrs.getNamedItem("id").getNodeValue();
            
        System.out.println("ahihi current node " + node.getNodeName());
            System.out.println(username + " | " + password);
            System.out.println("ahihi id " + id);
            if (id.equals(username)) {
                NodeList childNodesOfStudent = node.getChildNodes();
                for (int i = 0; i < childNodesOfStudent.getLength(); i++) {
                    Node child = childNodesOfStudent.item(i);
                    if (child.getNodeName().equals("lastname")) {
                        fullName = child.getTextContent().trim();
                    } else if (child.getNodeName().equals("middlename")) {
                        fullName += child.getTextContent().trim();
                    } else if (child.getNodeName().equals("firstname")) {
                        fullName += child.getTextContent().trim();
                    } else if (child.getNodeName().equals("password")) {
                        String pass = child.getTextContent().trim();
                        if (!pass.equals(password)) {
                            break;
                        }
                        bFound = true;
                    } else if (child.getNodeName().equals("status")) {
                        String str = child.getTextContent().trim();
                        if (!str.equals("dropout")) {
                            return;
                        }
                        bFound = false;
                    }
                }
            }
        }
        NodeList children = node.getChildNodes();
        int i = 0;
        while (i < children.getLength()) {
            checkLogin(username, password, children.item(i++));
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
