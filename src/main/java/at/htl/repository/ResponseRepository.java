package at.htl.repository;

import at.htl.entity.Response;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ResponseRepository implements PanacheRepository<Response> {
}
