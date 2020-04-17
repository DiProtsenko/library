package dev.protsenko.library.controllers;

import dev.protsenko.library.entities.Book;
import dev.protsenko.library.services.BookService;
import dev.protsenko.library.services.BookServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("books")
public class BookController {

    private BookService bookService;

    BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        Book bookToCreate = bookService.create(book);
        if (bookToCreate != null) {
            return new ResponseEntity<>("Book was added with id " + bookToCreate.getId(), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Book not created", HttpStatus.BAD_REQUEST);

    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateBook(@RequestBody Book book) {
        Book bookToUpdate = bookService.update(book);
        if (bookToUpdate != null) {
            return new ResponseEntity<>("Book was updated", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Book not updated", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(produces = "application/json")
    public Set<Book> getBooks() {
        return bookService.getAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Book book = bookService.getById(id);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        boolean bookDeleted = bookService.delete(id);
        if (bookDeleted) {
            return new ResponseEntity<>("Book was deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Book to delete not found", HttpStatus.NOT_FOUND);
    }

}
