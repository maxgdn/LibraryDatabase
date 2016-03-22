package sample.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * Created by Max on 3/7/2016.
 */

@Entity
public class SignIn extends SuperModel {
    public static final Find<Long, SignIn> find = new Find<Long, SignIn>(){};

    @ManyToOne(optional = false)
    public Student student;

    public LocalDateTime timeIn;
    public LocalDateTime timeOut;
    public boolean wasManual = false;

}
