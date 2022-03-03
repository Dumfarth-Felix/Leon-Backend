package at.htl.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Intent {

    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ExampleSentence> example;

    public Intent() {
    }

    public Intent(String name, List<ExampleSentence> example) {
        this.name = name;
        this.example = example;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExampleSentence> getExample() {
        return example;
    }

    public void setExample(List<ExampleSentence> example) {
        this.example = example;
    }
}
