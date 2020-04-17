package dev.protsenko.library.Services;

import dev.protsenko.library.entities.Author;
import dev.protsenko.library.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthorService {

    final AuthorRepository authorRepository;

    AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author get(Long id) {
        if (id != null) {
            Author author = authorRepository.getById(id);
            return author;
        }
        return null;
    }

    public Author create(Author author) {
        if (author != null) {
            if (isCorrect(author)) {
                Author createdAuthor = authorRepository.save(author);
                return createdAuthor;
            }
        }
        return null;
    }

    public Author update(Author author) {
        if (author != null && author.getId() != null) {
            Author updatedAuthor = authorRepository.getById(author.getId());
            if (updatedAuthor != null && isCorrect(author)) {
                updatedAuthor.setFatherName(author.getFatherName());
                updatedAuthor.setFirstName(author.getFirstName());
                updatedAuthor.setFamilyName(author.getFamilyName());
                updatedAuthor = authorRepository.save(updatedAuthor);
                return updatedAuthor;
            }
        }
        return null;
    }

    public Set<Author> getAll() {
        return authorRepository.findAll();
    }

    public boolean delete(Long id) {
        if (id != null) {
            Author authorToDelete = authorRepository.getById(id);
            if (authorToDelete != null) {
                authorRepository.delete(authorToDelete);
                return true;
            }
        }
        return false;
    }

    private boolean isCorrect(Author author) {
        if (author == null) {
            return false;
        }
        if (author.getFirstName() != null && author.getFamilyName() != null) {
            return author.getFirstName().length() > 0 && author.getFamilyName().length() > 0;
        }
        return false;
    }

}
