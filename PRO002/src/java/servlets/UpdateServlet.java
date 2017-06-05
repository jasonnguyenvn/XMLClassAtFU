/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import Utils.XMLUtilities;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author Hau
 */
public class UpdateServlet extends HttpServlet {
    
    
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
        String txtId = request.getParameter("txtId").trim();
        String searchValue = request.getParameter("txtLastSearchValue");
        
        String txtClass = request.getParameter("txtClass").trim();
        String txtSex = request.getParameter("txtSex").trim();
        String txtStatus = request.getParameter("txtStatus").trim();
        
        String url = "DispatchServlet?btnAction=Search&txtAddress=" 
                + searchValue;
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
           HttpSession session = request.getSession(false);
           if (session != null) {
               Document doc = (Document) session.getAttribute("DOMTREE");
               if (doc != null) {
                   String exp = "//student[@id='"
                           + txtId
                           + "']";//student[@id='98765']
                   XPath xpath = XMLUtilities.getXpath();
                   Node student = (Node) xpath.evaluate(exp, doc, 
                            XPathConstants.NODE);
                   if (student != null) {
                       
                   System.out.println("ahihi update nak " + txtId);
                       NamedNodeMap attrs = student.getAttributes();
                       
                       attrs.getNamedItem("class").setNodeValue(txtClass);
                       
                       Node nStatus = (Node) xpath.evaluate("status", student, 
                            XPathConstants.NODE);
                       if (nStatus != null) {
                        nStatus.setTextContent(txtStatus);
                           System.out.println("ahihi " + nStatus.getTextContent());
                       }
                       
                       Node nSex = (Node) xpath.evaluate("sex", student, 
                            XPathConstants.NODE);
                       if (nSex != null) {
                        nSex.setTextContent(txtSex);
                       }
                       String realPath = this.getServletContext().getRealPath("/");
                       String xmlOutputFilePath = realPath + xmlFile;
                       System.out.println("ahihi link na " + xmlOutputFilePath);
                       XMLUtilities.transformDOMToStream(doc, xmlOutputFilePath);
                       
                   }
               }
           }
        } catch (XPathExpressionException ex) {
            Logger.getLogger(DeleteServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(UpdateServlet.class.getName()).log(Level.SEVERE, null, ex);
        }  finally {
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
