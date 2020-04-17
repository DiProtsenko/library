package dev.protsenko.library.Services;

import dev.protsenko.library.entities.Author;
import dev.protsenko.library.entities.Book;
import dev.protsenko.library.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BookService {

    BookRepository bookRepository;
    AuthorService authorService;

    public BookService(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    public Book create(Book book) {
        if (isCorrect(book)) {
            if (!haveDuplicates(book)) {
                Book savedBook = bookRepository.save(book);
                return savedBook;
            }
        }
        return null;
    }

    public Book update(Book book) {
        if (book != null && book.getId() != null) {
            Book updatedBook = bookRepository.getById(book.getId());
            if (updatedBook != null && isCorrect(book)) {
                updatedBook.setAuthors(book.getAuthors());
                updatedBook.setName(book.getName());
                updatedBook.setDescription(book.getDescription());
                bookRepository.save(updatedBook);
                return updatedBook;
            }
        }
        return null;
    }

    public Book getById(Long id) {
        if (id != null) {
            return bookRepository.getById(id);
        }
        return null;
    }

    public Set<Book> getAll() {
        return bookRepository.findAll();
    }

    public boolean delete(Long id) {
        if (id != null) {
            Book bookToDelete = bookRepository.getById(id);
            if (bookToDelete != null) {
                bookRepository.delete(bookToDelete);
                return true;
            }
        }
        return false;
    }

    private boolean isCorrect(Book book) {
        if (book == null) {
            return false;
        }
        if (book.getAuthors() != null && book.getName() != null && book.getDescription() != null) {
            if (book.getAuthors().size() > 0 && book.getName().length() > 0 && book.getDescription().length() > 0) {
                boolean authorsCorrect = book.getAuthors().stream().allMatch(author -> author != null && authorService.get(author.getId()) != null);
                return authorsCorrect;
            }
        }
        return false;
    }

    private boolean haveDuplicates(Book book) {

        if (book != null) {
            Set<Book> bookList = bookRepository.getBooksByName(book.getName());
            for (Book bookFromDb : bookList) {
                for (Author author : bookFromDb.getAuthors()) {
                    if (book.getAuthors().contains(author)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
