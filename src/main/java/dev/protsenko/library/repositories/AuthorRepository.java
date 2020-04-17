package dev.protsenko.library.repositories;

import dev.protsenko.library.entities.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Set<Author> findAll();

    Author getById(long id);
}
