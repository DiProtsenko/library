package dev.protsenko.library.controllers;

import dev.protsenko.library.entities.Author;
import dev.protsenko.library.services.AuthorService;
import dev.protsenko.library.services.AuthorServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("authors")
public class AuthorController {

    AuthorService authorService;

    AuthorController(AuthorServiceImpl authorService) {
        this.authorService = authorService;
    }

    @PostMapping(consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> addAuthor(@RequestBody Author author) {
        if (author != null) {
            Author createdAuthor = authorService.create(author);
            if (createdAuthor != null) {
                return new ResponseEntity<>("Author created with id: " + createdAuthor.getId(), HttpStatus.ACCEPTED);
            }

        }
        return new ResponseEntity<>("Author not created", HttpStatus.BAD_REQUEST);
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateAuthor(@RequestBody Author author) {
        if (author != null) {
            Author updatedAuthor = authorService.update(author);
            if (updatedAuthor != null) {
                return new ResponseEntity<>("Author was updated", HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);
    }


    @GetMapping(produces = "application/json")
    public Set<Author> getAuthors() {
        return authorService.getAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Author> getAuthor(@PathVariable long id) {
        Author author = authorService.get(id);
        if (author != null) {
            return new ResponseEntity<>(author, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable long id) {
        boolean authorIsDelete = authorService.delete(id);
        if (authorIsDelete) {
            return new ResponseEntity<>("Author was deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);
    }


}
