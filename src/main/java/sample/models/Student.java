package sample.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Created by Max on 3/7/2016.
 */

@Entity
public class Student extends SuperModel {
    public static final Find<Long, Student> find = new Find<Long, Student>(){};

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    public Set<SignIn> signIns;

    @Column(unique = true)
    private int studentID;
    private String firstName;
    private String lastName;

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}