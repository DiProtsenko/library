package dev.protsenko.library.repositories;

import dev.protsenko.library.entities.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface BookRepository extends CrudRepository<Book, Long> {

    Book getById(long id);

    Set<Book> getBooksByName(String name);

    Set<Book> findAll();


}
