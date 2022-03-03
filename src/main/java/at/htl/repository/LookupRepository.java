package at.htl.repository;

import at.htl.entity.Lookup;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LookupRepository implements PanacheRepository<Lookup> {
}
