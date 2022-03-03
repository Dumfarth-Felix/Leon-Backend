package at.htl.entity;

import javax.persistence.*;
import java.util.List;

public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ExampleSentence> sentences;

    public Response() {
    }

    public Response(List<ExampleSentence> sentences) {
        this.sentences = sentences;
    }

    public Long getId() {
        return id;
    }

    public List<ExampleSentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<ExampleSentence> sentences) {
        this.sentences = sentences;
    }
}
