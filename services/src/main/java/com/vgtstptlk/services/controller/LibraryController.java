package com.vgtstptlk.services.controller;

import com.vgtstptlk.services.entity.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Library")
public class LibraryController {

    private final List<Book> bookList;

    public LibraryController() {
        this.bookList = new ArrayList<>();
        bookList.add(new Book("1Q84"));
    }

    @GetMapping("/Books")
    public ResponseEntity<List<Book>> getBooks(){
        return ResponseEntity.ok(bookList);
    }
}
