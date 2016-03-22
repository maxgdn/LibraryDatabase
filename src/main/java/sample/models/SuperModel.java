package sample.models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class SuperModel extends Model {
    @Id
    @GeneratedValue
    public Long id;

    @Column(updatable = false, insertable = true)
    public Date createdAt;

    @Column(updatable = false, insertable = true)
    @Version
    public Date updatedAt;


    @PrePersist
    public void initializeCreatedAt() {
        if (this.createdAt == null) this.createdAt = new Date(System.currentTimeMillis());
    }
}