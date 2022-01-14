package at.htl.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Feedback extends PanacheEntity {

    public Feedback(String name, int rating, String text) {
        this.name = name;
        this.rating = rating;
        this.text = text;
    }

    public Feedback() {
    }

    public String name;
    public int rating;
    public String text;
}
