package dev.protsenko.library.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity(name = "Books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Book_authors",
            joinColumns = {@JoinColumn(name = "fk_book")},
            inverseJoinColumns = {@JoinColumn(name = "fk_author")})
    private Set<Author> authors = new HashSet<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    public Book(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Book() {

    }

}
