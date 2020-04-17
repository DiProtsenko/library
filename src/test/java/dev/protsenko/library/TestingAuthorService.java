package dev.protsenko.library;

import dev.protsenko.library.Services.AuthorService;
import dev.protsenko.library.entities.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-testing.properties")
public class TestingAuthorService {

    @Autowired
    private AuthorService authorService;


    @Test
    public void createCorrectAuthor(){
        Author author = new Author("Олдос", "Хаксли", "");
        assert authorService.create(author) != null;
    }

    @Test
    public void createIncorrectAuthor(){
        Author author = new Author("Олдос", null, null);
        assert authorService.create(author) == null;
    }

    @Test
    public void deleteAuthor(){
        Author author = new Author("Олдос", "Хаксли", null);
        Author createdAuthor = authorService.create(author);
        assert authorService.delete(createdAuthor.getId());
    }

    @Test
    public void deleteIncorrectAuthor(){
        assert !authorService.delete(-1L);
    }

    @Test
    public void updateCorrect(){
        Author author = new Author("Олдос","Хаксли",null);
        Author authorCreated = authorService.create(author);
        authorCreated.setFirstName("Олдос Леонард");
        Author authorUpdated = authorService.update(authorCreated);
        assert authorUpdated != null;
    }

    @Test
    public void updateWithoutId(){
        Author author = new Author("Олдос","Хаксли",null);
        Author authorUpdated = authorService.update(author);
        assert authorUpdated == null;
    }

    @Test
    public void updateWithoutFamilyName(){
        Author author = new Author("Олдос","Хаксли",null);
        Author authorCreated = authorService.create(author);
        authorCreated.setFamilyName(null);
        Author authorUpdated = authorService.update(authorCreated);
        assert authorUpdated == null;
    }

    @Test
    public void operationWithNull(){
        Author authorUpdated = authorService.update(null);
        Author authorCreated = authorService.create(null);
        Author getAuthor = authorService.get(null);
        boolean deleteAuthor = authorService.delete(null);
        assert authorUpdated == null;
        assert authorCreated == null;
        assert getAuthor == null;
        assert !deleteAuthor;
    }

    @Test
    public void operationWithEmpty(){
        Author emptyAuthor =  new Author();
        Author authorUpdated = authorService.update(emptyAuthor);
        Author authorCreated = authorService.create(emptyAuthor);
        Author getAuthor = authorService.get(0L);
        boolean deleteAuthor = authorService.delete(0L);
        assert authorUpdated == null;
        assert authorCreated == null;
        assert getAuthor == null;
        assert !deleteAuthor;
    }

}
