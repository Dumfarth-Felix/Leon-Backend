package at.htl.repository;

import at.htl.entity.ExampleSentence;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExampleSentenceRepository implements PanacheRepository<ExampleSentence> {
}
