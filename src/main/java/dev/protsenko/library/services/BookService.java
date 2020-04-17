package dev.protsenko.library.services;

import dev.protsenko.library.entities.Book;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface BookService {

    Set<Book> getAll();

    Book getById(Long id);

    Book create(Book author);

    Book update(Book author);

    boolean delete(Long id);

}
