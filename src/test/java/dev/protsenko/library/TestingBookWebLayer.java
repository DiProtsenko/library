package dev.protsenko.library;


import dev.protsenko.library.entities.Author;
import dev.protsenko.library.entities.Book;
import dev.protsenko.library.services.AuthorService;
import dev.protsenko.library.services.BookService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.nio.charset.StandardCharsets;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-testing.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestingBookWebLayer {

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookService bookService;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    //Данный тест тестирует создание книги, и пробует создать дубликат книги (автор и название)
    @Test
    @Order(1)
    public void userCreateBook() {
        final String bookUrl = "http://localhost:" + port + "/books";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
        Book book = new Book("О дивный новый мир", "Антиутопический сатирический роман английского писателя Олдоса Хаксли, опубликованный в 1932 году.");
        Author author = authorService.create(new Author("Олдос", "Хаксли", ""));
        book.getAuthors().add(author);
        HttpEntity<Book> bookRequest = new HttpEntity<>(book, headers);
        ResponseEntity<String> bookResponse = restTemplate.postForEntity(bookUrl, bookRequest, String.class);
        assert bookResponse.getStatusCodeValue() == 202;
        ResponseEntity<String> duplicateBookResponse = restTemplate.postForEntity(bookUrl, bookRequest, String.class);
        assert duplicateBookResponse.getStatusCodeValue() == 400;
    }

    @Test
    @Order(2)
    public void userCreateIncorrectBook() {
        final String bookUrl = "http://localhost:" + port + "/books";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
        Book book = new Book("О дивный новый мир", null);
        Author author = authorService.create(new Author("Олдос","Хаксли",""));
        book.getAuthors().add(author);
        HttpEntity<Book> bookRequest = new HttpEntity<>(book,headers);
        ResponseEntity<String> bookResponse = restTemplate.postForEntity(bookUrl,bookRequest,String.class);
        assert bookResponse.getStatusCodeValue() == 400;
    }

    @Test
    @Order(3)
    public void userGetCorrectBook() {
        final String bookUrl = "http://localhost:" + port + "/books/";
        Author author = authorService.create(new Author("Олдос","Хаксли",""));
        Book book = new Book("О дивный новый мир","Антиутопический сатирический роман английского писателя Олдоса Хаксли, опубликованный в 1932 году.");
        book.getAuthors().add(author);
        book = bookService.create(book);
        ResponseEntity<Book> bookResponse = restTemplate.getForEntity(bookUrl+book.getId(), Book.class);
        assert bookResponse.getBody().equals(book);
    }

    @Test
    @Order(4)
    void userGetInCorrectBook() {
        final String bookUrl = "http://localhost:" + port + "/books/0";
        ResponseEntity<Book> bookResponse = restTemplate.getForEntity(bookUrl, Book.class);
        assert bookResponse.getStatusCodeValue()==404;
    }

    @Test
    @Order(5)
    void userDeleteBook() {
        final String bookUrl = "http://localhost:" + port + "/books/";
        Author author = authorService.create(new Author("Олдос","Хаксли",""));
        Book book = new Book("О дивный новый мир","Антиутопический сатирический роман английского писателя Олдоса Хаксли, опубликованный в 1932 году.");
        book.getAuthors().add(author);
        book = bookService.create(book);
        ResponseEntity<String> bookResponse = restTemplate.exchange(bookUrl+book.getId(), HttpMethod.DELETE, null,String.class);
        assert bookResponse.getStatusCodeValue()==200;
    }

    @Test
    @Order(6)
    void userDeleteIncorrectBook() {
        final String bookUrl = "http://localhost:" + port + "/books/0";
        ResponseEntity<String> bookResponse = restTemplate.exchange(bookUrl, HttpMethod.DELETE, null,String.class);
        assert bookResponse.getStatusCodeValue()==404;
    }

}
