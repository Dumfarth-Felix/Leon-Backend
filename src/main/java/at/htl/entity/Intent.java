package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.OneToOne;
import java.util.List;

public class Intent extends PanacheEntity {
    public String name;
    public List<String> examples;

    @OneToOne
    public Response response;
}
