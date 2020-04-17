package dev.protsenko.library.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "Authors")
public class Author {

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<Book> books = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String familyName;
    private String fatherName;

    public Author() {

    }

    public Author(String firstName, String familyName, String fatherName) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.fatherName = fatherName;
    }


}
