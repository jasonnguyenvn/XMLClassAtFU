/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dtos.Student;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Hau
 */
public class StudentHandler extends DefaultHandler {
    private String username;
    private String password;
    
    private boolean foundUserName;
    private String curTagName;
    private String fullName;
    private boolean foundPassword;
    private boolean found;
    
    private void resetFlags() {
        foundUserName = false;
        foundPassword = false;
        found = false;
    }

    public StudentHandler() {
        resetFlags();
    }

    public StudentHandler(String username, String password) {
        this.username = username;
        this.password = password;
        resetFlags();
    }
    
    
    

    @Override
    public void startElement(String uri, String localName, 
            String qName, Attributes attributes) throws SAXException {
        if (this.found) {
            return;
        }
        this.curTagName = qName;
        if (qName.equals("student")) {
            String id = attributes.getValue("id");
            if (id.equals(username)) {
                this.foundUserName = true;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
       this.curTagName  = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.found) {
            return;
        }
       if (this.foundUserName) {
           String str = new String(ch, start, length);
           if (this.curTagName.equals("lastname")) {
               this.fullName = str.trim();
           } else if (this.curTagName.equals("middlename")) {
               this.fullName += " " +  str.trim();
           } else  if (this.curTagName.equals("firstname")) {
               this.fullName += " " + str.trim();
           } else  if (this.curTagName.equals("password")) {
               resetFlags();
               if (password.equals(str.trim())) {
                   this.foundPassword = true;
               }
           }
       } // end if check username
       
       if (this.foundPassword) {
           String str = new String(ch, start, length);
           if (this.curTagName.equals("status")) {
               resetFlags();
               if (!str.trim().equals("dropout")) {
                   this.found = true;
               }
           }
       }
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isFound() {
        return found;
    }   
    
    
}
