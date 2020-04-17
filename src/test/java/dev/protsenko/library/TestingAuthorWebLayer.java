package dev.protsenko.library;

import dev.protsenko.library.entities.Author;
import dev.protsenko.library.services.AuthorServiceImpl;
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
public class TestingAuthorWebLayer {

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorServiceImpl authorService;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    @Order(1)
    public void userCreateCorrectAuthor() {
        final String url = "http://localhost:" + port + "/authors";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
        Author author = new Author("Олдос", "Хаксли", null);
        HttpEntity<Author> authorRequest = new HttpEntity<>(author, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, authorRequest, String.class);
        assert response.getStatusCodeValue() == 202;
    }

    @Test
    @Order(2)
    public void userCreateIncorrectAuthor() {
        final String url = "http://localhost:" + port + "/authors";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
        Author author = new Author(null, "Хаксли", null);
        HttpEntity<Author> authorRequest = new HttpEntity<>(author, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, authorRequest, String.class);
        assert response.getStatusCodeValue() == 400;
    }

    @Test
    @Order(3)
    public void userTryCreateAuthorWithGarbageData() {
        final String url = "http://localhost:" + port + "/authors";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
        HttpEntity<String> authorRequest = new HttpEntity<>("TESTING TEST", headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, authorRequest, String.class);
        assert response.getStatusCodeValue() == 400;
    }

    @Test
    @Order(4)
    public void userUpdateAuthorInfo() {
        final String url = "http://localhost:" + port + "/authors";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);

        Author author = authorService.create(new Author("Олдос","Хаксли",""));
        author.setFirstName("Олдос Леонард");

        HttpEntity<Author> authorRequest = new HttpEntity<>(author, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, authorRequest, String.class);
        assert response.getStatusCodeValue() == 202;
    }

    @Test
    @Order(5)
    public void userTryUpdateIncorrectAuthor() {
        final String url = "http://localhost:" + port + "/authors";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
        Author author = new Author("Олдос", "Хаксли", null);
        HttpEntity<Author> authorRequest = new HttpEntity<>(author, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, authorRequest, String.class);
        assert response.getStatusCodeValue() == 404;
    }

    @Test
    @Order(6)
    public void userTryUpdateAuthorWithGarbageData() {
        final String url = "http://localhost:" + port + "/authors";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
        HttpEntity<String> authorRequest = new HttpEntity<>("TESTING TEST", headers);
        ResponseEntity<String> response = restTemplate.exchange(url,HttpMethod.PUT, authorRequest, String.class);
        assert response.getStatusCodeValue() == 400;
    }

    @Test
    @Order(7)
    public void userGetAuthor() {
        final String url = "http://localhost:" + port + "/authors/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
        Author author = authorService.create(new Author("Олдос","Хаксли",""));
        ResponseEntity<Author> authorResponse = restTemplate.getForEntity(url+author.getId(),Author.class);
        assert authorResponse.getBody().equals(author);
    }

    @Test
    @Order(8)
    public void userGetIncorrectAuthor() {
        final String url = "http://localhost:" + port + "/authors/0";
        ResponseEntity<Author> response = restTemplate.getForEntity(url,Author.class);
        assert response.getStatusCodeValue() == 404;
    }

    @Test
    @Order(9)
    public void userDeleteCorrectAuthor() {
        final String url = "http://localhost:" + port + "/authors/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
        Author author = authorService.create(new Author("Олдос","Хаксли",""));
        ResponseEntity<String> response = restTemplate.exchange(url+author.getId(),HttpMethod.DELETE,null,String.class);
        assert response.getStatusCodeValue() == 200;
    }

    @Test
    @Order(10)
    public void userDeleteIncorrectAuthor() {
        final String url = "http://localhost:" + port + "/authors/0";
        ResponseEntity<String> response = restTemplate.exchange(url,HttpMethod.DELETE,null,String.class);
        assert response.getStatusCodeValue() == 404;
    }




}
