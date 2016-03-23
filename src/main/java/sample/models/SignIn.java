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
    private Student student;

    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
    private boolean wasManual = false;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalDateTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalDateTime timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isWasManual() {
        return wasManual;
    }

    public void setWasManual(boolean wasManual) {
        this.wasManual = wasManual;
    }
}
