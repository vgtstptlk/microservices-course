package com.vgtstptlk.services.controller;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.vgtstptlk.services.entity.Antology;
import com.vgtstptlk.services.entity.Author;
import com.vgtstptlk.services.entity.Book;
import com.vgtstptlk.services.entity.Note;
import com.vgtstptlk.services.repository.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.Date;
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

    private void postNote(String message){
        Note note = new Note();
        note.setMessage(message);
        note.setTime(new Date());


        String txt = ClientBuilder.newBuilder()
                .register(JacksonJsonProvider.class)
                .build()
                .target("http://localhost:8080/")
                .path("NotificationDesk/Notes/")
                .request("application/json")
                .header("Content-Type", "application/json")
                .post((Entity.entity(note, MediaType.APPLICATION_JSON_TYPE)), String.class);
    }

    @GetMapping("/")
    public ResponseEntity<Library> getLib(){
        lib.refresh();
        postNote("Получить всю библиотеку");
        return ResponseEntity.ok(lib);
    }

    @GetMapping(value = "/Books")
    public ResponseEntity<List<Book>> getBooks(@RequestParam(name = "search", required = false) String search){
        lib.refresh();
        if (search != null){
            postNote("Найти книгу: " + search);
            List<Book> book = this.lib.bookList.stream().filter(
                    (Book b) -> b.getCaption().contains(search)
            ).collect(Collectors.toList());
            return ResponseEntity.ok(book);
        }

        postNote("Получить все книги");
        return ResponseEntity.ok(this.lib.bookList);
    }

    @GetMapping(value = "/Authors")
    public ResponseEntity<List<Author>> getAuthors(@RequestParam(name = "search", required = false) String search){
        lib.refresh();
        if (search != null){
            postNote("Получить автора: " + search);
            List<Author> authors = this.lib.writers.stream().filter(
                    (Author b) -> b.getName().toLowerCase().contains(search.toLowerCase()) ||
                            b.getLastName().toLowerCase().contains(search.toLowerCase()) ||
                            b.getPatronym().toLowerCase().contains(search.toLowerCase())
            ).collect(Collectors.toList());
            return ResponseEntity.ok(authors);
        }

        postNote("Получить всех авторов");
        return ResponseEntity.ok(this.lib.writers);
    }

    @GetMapping("/Authors/{lastname}")
    public ResponseEntity<?> getAntology(@PathVariable String lastname){
        lib.refresh();
        Optional<Author> author = this.lib.writers.stream().filter(
                b -> b.getName().toLowerCase().contains(lastname.toLowerCase())
        ).findFirst();

        postNote("Получить антологию");
        return author.map(value -> ResponseEntity.ok(new Antology(value, lib.bookList))).
                orElseGet(() -> ResponseEntity.ok(new Antology(new Author(null, lastname, null), lib.bookList)));
    }

    @GetMapping("/Books/{name}")
    public ResponseEntity<Book> getBookByName(@PathVariable String name){
        lib.refresh();
        Optional<Book> optionalBook = this.lib.bookList.stream().filter(
                (Book b) -> b.getCaption().equalsIgnoreCase(name)
        ).findFirst();

        postNote("Найти книгу по названию: " + name);
        return optionalBook.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
