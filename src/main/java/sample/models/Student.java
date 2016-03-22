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

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    public Set<SignIn> signIns;

    @Column(unique = true)
    public int studentID;
    public String firstName;
    public String lastName;
}