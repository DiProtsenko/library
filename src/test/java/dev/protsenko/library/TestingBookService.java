package dev.protsenko.library;

import dev.protsenko.library.entities.Author;
import dev.protsenko.library.entities.Book;
import dev.protsenko.library.services.AuthorServiceImpl;
import dev.protsenko.library.services.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-testing.properties")
public class TestingBookService {

    @Autowired
    BookServiceImpl bookService;

    @Autowired
    AuthorServiceImpl authorService;

    public Book getBookForTest(){
        Author author = new Author("Олдос","Хаксли","");
        author = authorService.create(author);
        Set<Author> authors = new HashSet<>();
        authors.add(author);
        Book book = new Book();
        book.setAuthors(authors);
        book.setName("О дивный новый мир");
        book.setDescription("Антиутопический сатирический роман английского писателя Олдоса Хаксли, опубликованный в 1932 году.");
        return book;
    }

    @Test
    public void createIncorrectBooks(){
        assert bookService.create(null) == null;
        Book book = new Book();
        assert bookService.create(book) == null;
        book.setName("");
        book.setDescription("");
        assert bookService.create(book) == null;
        book.setName("О дивный новый мир");
        assert bookService.create(book) == null;
        book.setDescription("Легендарная книга анти-утопия");
        assert bookService.create(book) == null;
        book.setAuthors(new HashSet<>());
        assert bookService.create(book) == null;
        Set<Author> authorList =  new HashSet<>();
        authorList.add(new Author());
        book.setAuthors(authorList);
        assert bookService.create(book) == null;
    }

    @Test
    public void createCorrectBook() {
        assert bookService.create(getBookForTest()) != null;
    }

    @Test
    public void createDuplicateBook(){
        Book book = getBookForTest();
        assert bookService.create(book) != null;
        assert bookService.create(book) == null;
        book.setName("Остров");
        assert bookService.create(book) != null;
        book.getAuthors().add(new Author("Александр","Пушкин",null));
        assert bookService.create(book) == null;
    }

    @Test
    public void updateIncorrect(){
        Author author = new Author("Олдос","Хаксли","");
        author = authorService.create(author);
        Book book = new Book();
        book.getAuthors().add(author);
        book.setName("О дивный новый мир!");
        book.setDescription("Антиутопический сатирический роман английского писателя Олдоса Хаксли, опубликованный в 1932 году.");
        Book newBook = bookService.create(book);
        assert newBook != null;
        newBook.setId(null);
        assert bookService.update(newBook)==null;
        newBook.setId(0L);
        assert bookService.update(newBook)==null;
        newBook.setId(3L);
        assert bookService.update(newBook)==null;
    }

    @Test
    public void operationWithNull(){
        assert bookService.create(null) == null;
        assert bookService.update(null) == null;
        assert bookService.getById(null) == null;
        assert !bookService.delete(null);
    }


}
