/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dtos.Student;

/**
 *
 * @author Hau
 */
public class StudentDTO {
    private String id;
    private String stdClass;
    private String lastname;
    private String middlename;
    private String firstname;
    private int sex;
    private String password;
    private String address;
    private String status;

    public StudentDTO(String id, String stdClass, String lastname, String middlename, String firstname, int sex, String password, String address, String status) {
        this.id = id;
        this.stdClass = stdClass;
        this.lastname = lastname;
        this.middlename = middlename;
        this.firstname = firstname;
        this.sex = sex;
        this.password = password;
        this.address = address;
        this.status = status;
    }

    public StudentDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStdClass() {
        return stdClass;
    }

    public void setStdClass(String stdClass) {
        this.stdClass = stdClass;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    
}
