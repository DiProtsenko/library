package dev.protsenko.library.services;

import dev.protsenko.library.entities.Author;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface AuthorService {

    Set<Author> getAll();

    Author get(Long id);

    Author create(Author author);

    Author update(Author author);

    boolean delete(Long id);

}
