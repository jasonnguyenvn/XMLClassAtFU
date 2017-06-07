package servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dtos.Student.StudentDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.apache.tomcat.jni.Stdlib;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Hau
 */
public class SearchServlet extends HttpServlet {
    
    private final String xmlFile = "WEB-INF/studentAccounts.xml";
    
    private final String searchPage = "search.jsp";

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
        String searchValue = request.getParameter("txtAddress");
        String url = searchPage;
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                Document doc = (Document) session.getAttribute("DOMTREE");
                if (doc == null) {
                    
                    String realPath = getServletContext().getRealPath("/");
                    String xmlFilePath = realPath  + xmlFile;
                    doc = Utils.XMLUtilities.parseDomFromFile(xmlFilePath);
                    
                    session.setAttribute("DOMTREE", doc);
                }
                if (doc != null) {
                    String exp = "//student[contains(address, '"
                            + searchValue
                            + "')]";
                    
                    XPath xpath = Utils.XMLUtilities.getXpath();
                    NodeList listOfStudents = (NodeList) xpath.evaluate(exp, doc, 
                            XPathConstants.NODESET);
                    List<StudentDTO> resultList = null;
                    if (listOfStudents != null){
                        resultList = new ArrayList<StudentDTO>();
                        for (int i = 0; i < listOfStudents.getLength(); i++) {
                            Node student = listOfStudents.item(i);
                            if (student == null) {
                                continue;
                            }
                            if (student.getNodeName().equals("student")) {
                 
                                StudentDTO dto = new StudentDTO();
                                NamedNodeMap attrs = student.getAttributes();
                                dto.setId(attrs.getNamedItem("id").getNodeValue().trim());
                                dto.setStdClass(attrs.getNamedItem("class").getNodeValue().trim());
                               // System.out.println("ahihi du lieu cua thang: " 
                                //    + attrs.getNamedItem("id").getNodeValue());
                                NodeList childsOfStdNode = student.getChildNodes();
                                for (int j = 0; j < childsOfStdNode.getLength(); j++) {
                                    Node tmp = childsOfStdNode.item(j);
                                    if (tmp == null) {
                                        //System.out.println("ahihi node bi null ne :(");
                                        continue;
                                    }
                                    if (tmp.getNodeName().equals("lastname")) {
                                     //   System.out.println("ahihi tmp na: " + tmp);
                                       // System.out.println("ahihi node value na: " + tmp.getNodeValue());
                                        dto.setLastname(tmp.getTextContent().trim());
                                    } else if (tmp.getNodeName().equals("middlename")) {
                                        dto.setMiddlename(tmp.getTextContent().trim());
                                    } else if (tmp.getNodeName().equals("firstname")) {
                                        dto.setFirstname(tmp.getTextContent().trim());
                                    } else if (tmp.getNodeName().equals("sex")) {
                                        String nodeVal = tmp.getTextContent().trim();
                                        int value = Integer.parseInt(nodeVal);
                                        dto.setSex(value);
                                    } else if (tmp.getNodeName().equals("password")) {
                                        dto.setPassword(tmp.getTextContent().trim());
                                    } else if (tmp.getNodeName().equals("address")) {
                                        dto.setAddress(tmp.getTextContent().trim());
                                    }  else if (tmp.getNodeName().equals("status")) {
                                        dto.setStatus(tmp.getTextContent().trim());
                                    } 
                                    
                                }
                                
                               resultList.add(dto);
                            }
                        }
                    }
                    
                    request.setAttribute("SEARCHRESULT", resultList);
                }
            }
        } catch (XPathExpressionException ex) {
            Logger.getLogger(SearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NumberFormatException ex) {
            Logger.getLogger(SearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(SearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
            
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
