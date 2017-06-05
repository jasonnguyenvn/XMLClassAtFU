<%-- 
    Document   : search
    Created on : May 26, 2017, 1:08:29 PM
    Author     : Hau
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search page</title>
    </head>
    <body>
        <font color="red">
        Welcome, ${sessionScope.FULLNAME}
        </font>
        
        <form action="DispatchServlet">
            <input type="text" name="txtAddress" value="${param.txtAddress}">
            <input type="submit" name="btnAction" value="Search">
            
        </form>
            
            <a href="createNewStudent.html">Create new Account</a><br>
        
        
        <c:set var="searchValue" value="${param.txtAddress}" />
        <c:if test="${not empty searchValue}">
            
            <h2>Search Result</h2>
            <c:set var="result" value="${requestScope.SEARCHRESULT}" />
            <c:if test="${not empty result}">
                
                <table border="1">
                    <thead>
                        <tr>
                            <th>No.</th>
                            <th>Fullname</th>
                            <th>Class</th>
                            <th>Address</th>
                            <th>Status</th>
                            <th>Sex</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="dto" items="${result}" varStatus="counter">
                            
                            <tr>
                                
                    <form action="DispatchServlet" method="POST">
                        <input type="hidden" name="txtLastSearchValue"
                               value="${param.txtAddress}">
                                <td>
                                    ${counter.count}
                                    <input type="hidden" name="txtId"
                                           value="${dto.id}" >
                                </td>
                                <td>
                                    ${dto.lastname}
                                    ${dto.middlename}
                                    ${dto.firstname}
                                </td>
                                <td>
                                    <input type="text"
                                           name="txtClass"
                                           value="${dto.stdClass}" >
                                </td>
                                <td>
                                    ${dto.address}
                                    
                                </td>
                                <td>
                                    
                                    
                                    <input type="text"
                                           name="txtStatus"
                                           value="${dto.status}" >
                                </td>
                                <td>
                                    
                                        <input type="text"
                                               name="txtSex"
                                               value="${dto.sex}">
                                </td>
                                <td>
                                    <input type="submit" 
                                           name="btnAction"
                                           value="Delete" >
                                    
                                    
                                    <input type="submit" 
                                           name="btnAction"
                                           value="Update" >
                                </td>
                    
                        
                    </form>        
                    </tr>
                            
                        </c:forEach>
                    </tbody>
                </table>

            </c:if>
            <c:if test="${empty result}">
                <h3>No record found.</h3>
            </c:if>
        </c:if>
    </body>
</html>
