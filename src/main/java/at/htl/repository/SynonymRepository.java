package at.htl.repository;

import at.htl.entity.Synonym;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SynonymRepository implements PanacheRepository<Synonym> {
}
