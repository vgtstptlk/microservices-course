package com.vgtstptlk.services.controller;

import com.vgtstptlk.services.entity.Antology;
import com.vgtstptlk.services.entity.Author;
import com.vgtstptlk.services.entity.Book;
import com.vgtstptlk.services.repository.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Library")
public class LibraryController {

    @Autowired
    private final Library lib;

    public LibraryController(Library lib) {
        this.lib = lib;

    }

    @GetMapping("/")
    public ResponseEntity<Library> getLib(){
        return ResponseEntity.ok(lib);
    }

    @GetMapping(value = "/Books")
    public ResponseEntity<List<Book>> getBooks(@RequestParam(name = "search", required = false) String search){
        if (search != null){
            List<Book> book = this.lib.bookList.stream().filter(
                    (Book b) -> b.getCaption().contains(search)
            ).collect(Collectors.toList());
            return ResponseEntity.ok(book);
        }

        return ResponseEntity.ok(this.lib.bookList);
    }

    @GetMapping(value = "/Authors")
    public ResponseEntity<List<Author>> getAuthors(@RequestParam(name = "search", required = false) String search){
        if (search != null){
            List<Author> authors = this.lib.writers.stream().filter(
                    (Author b) -> b.getName().toLowerCase().contains(search.toLowerCase()) ||
                            b.getLastName().toLowerCase().contains(search.toLowerCase()) ||
                            b.getPatronym().toLowerCase().contains(search.toLowerCase())
            ).collect(Collectors.toList());
            return ResponseEntity.ok(authors);
        }

        return ResponseEntity.ok(this.lib.writers);
    }

    @GetMapping("/Authors/{lastname}")
    public ResponseEntity<?> getAntology(@PathVariable String lastname){
        Optional<Author> author = this.lib.writers.stream().filter(
                b -> b.getName().toLowerCase().contains(lastname.toLowerCase())
        ).findFirst();

        return author.map(value -> ResponseEntity.ok(new Antology(value, lib.bookList))).
                orElseGet(() -> ResponseEntity.ok(new Antology(new Author(null, lastname, null), lib.bookList)));
    }

    @GetMapping("/Books/{name}")
    public ResponseEntity<Book> getBookByName(@PathVariable String name){
        Optional<Book> optionalBook = this.lib.bookList.stream().filter(
                (Book b) -> b.getCaption().equalsIgnoreCase(name)
        ).findFirst();

        return optionalBook.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
