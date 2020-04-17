package dev.protsenko.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.protsenko.library.controllers.AuthorController;
import dev.protsenko.library.entities.Author;
import dev.protsenko.library.services.AuthorService;
import org.hamcrest.core.Is;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
@TestPropertySource(locations = "classpath:application-testing.properties")
public class TestingAuthorController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService service;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    public void createCorrectAuthor() throws Exception {

        Author author = new Author("Олдос","Хаксли","");
        String jsonAuthor = new ObjectMapper().writeValueAsString(author);
        Mockito.when(service.create(author)).thenReturn(author);
        mvc.perform(MockMvcRequestBuilders.post("/authors").content(jsonAuthor).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isAccepted());

    }

    @Test
    public void createIncorrectAuthors() throws Exception {

        Author author = new Author(null,"Хаксли","");
        String jsonAuthor = new ObjectMapper().writeValueAsString(author);
        Mockito.when(service.create(author)).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post("/authors").content(jsonAuthor).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());

    }


    @Test
    public void updateCorrectAuthors() throws Exception {
        Author author = new Author("Олдос","Хаксли","");
        author.setId(1L);
        Mockito.when(service.update(author)).thenReturn(author);
        String jsonAuthor = new ObjectMapper().writeValueAsString(author);
        mvc.perform(MockMvcRequestBuilders.put("/authors").content(jsonAuthor).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isAccepted());
    }


    @Test
    public void updateIncorrectAuthors() throws Exception {
        Author author = new Author("Олдос","Хаксли","");
        Mockito.when(service.update(author)).thenReturn(null);
        String jsonAuthor = new ObjectMapper().writeValueAsString(author);
        mvc.perform(MockMvcRequestBuilders.put("/authors").content(jsonAuthor).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isNotFound());
    }

    @Test
    public void getAuthors() throws Exception {
        Author author = new Author("Олдос","Хаксли","");
        author.setId(1L);
        Set<Author> authorSet = new HashSet<>();
        authorSet.add(author);
        Mockito.when(service.getAll()).thenReturn(authorSet);
        mvc.perform(MockMvcRequestBuilders.get("/authors").contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].familyName",Is.is(author.getFamilyName())))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteAuthor() throws Exception {
        Mockito.when(service.delete(1L)).thenReturn(true);
        mvc.perform(MockMvcRequestBuilders.delete("/authors/1").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteIncorrectAuthor() throws Exception {
        Mockito.when(service.delete(1L)).thenReturn(false);
        mvc.perform(MockMvcRequestBuilders.delete("/authors/1").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }


}
