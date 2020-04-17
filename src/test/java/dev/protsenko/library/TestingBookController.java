package dev.protsenko.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.protsenko.library.controllers.BookController;
import dev.protsenko.library.entities.Author;
import dev.protsenko.library.entities.Book;
import dev.protsenko.library.services.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@TestPropertySource(locations = "classpath:application-testing.properties")
public class TestingBookController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookServiceImpl service;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    public Book getBookForTest(){
        Book book = new Book();
        book.setId(1L);
        book.setName("О дивный новый мир");
        book.setDescription("Антиутопический сатирический роман английского писателя Олдоса Хаксли, опубликованный в 1932 году.");
        Author author = new Author("Олдос","Хаксли","");
        author.setId(1L);
        book.getAuthors().add(author);
        return book;
    }

    @Test
    public void createCorrectBook() throws Exception{
        Book book = getBookForTest();
        String jsonBook = new ObjectMapper().writeValueAsString(book);
        Mockito.when(service.create(book)).thenReturn(book);
        mvc.perform(MockMvcRequestBuilders.post("/books").content(jsonBook).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isAccepted());
        Mockito.when(service.create(book)).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post("/books").content(jsonBook).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
    }

    @Test
    public void createIncorrectBook() throws Exception{
        Book book = getBookForTest();
        String jsonBook = new ObjectMapper().writeValueAsString(book);
        Mockito.when(service.create(book)).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post("/books").content(jsonBook).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
    }

    @Test
    public void updateCorrectBook() throws Exception{
        Book book = getBookForTest();
        String jsonBook = new ObjectMapper().writeValueAsString(book);
        Mockito.when(service.update(book)).thenReturn(book);
        mvc.perform(MockMvcRequestBuilders.put("/books").content(jsonBook).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isAccepted());
    }

    @Test
    public void updateInCorrectBook() throws Exception{
        Book book = getBookForTest();
        String jsonBook = new ObjectMapper().writeValueAsString(book);
        Mockito.when(service.update(book)).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.put("/books").content(jsonBook).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
    }

    @Test
    public void getBooks() throws Exception{
        Book book = getBookForTest();
        Set<Book> books = new HashSet<>();
        books.add(book);
        Mockito.when(service.getAll()).thenReturn(books);
        mvc.perform(MockMvcRequestBuilders.get("/books").contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    public void getBook() throws Exception{
        Book book = getBookForTest();
        Mockito.when(service.getById(1L)).thenReturn(book);
        mvc.perform(MockMvcRequestBuilders.get("/books/1").contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name",is(book.getName())))
                .andExpect(status().isOk());
    }



}
