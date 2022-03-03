package at.htl.repository;

import at.htl.entity.Intent;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IntentRepository implements PanacheRepository<Intent> {
}
