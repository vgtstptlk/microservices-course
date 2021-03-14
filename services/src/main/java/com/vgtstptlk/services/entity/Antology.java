package com.vgtstptlk.services.entity;

import java.util.List;
import java.util.stream.Collectors;

public class Antology {
    Author writer;
    List<Book> books;

    public Antology(Author writer, List<Book> books) {
        this.writer = writer;
        this.books = books;

        this.books = books.stream().filter(
                book -> book.getWriter() == writer
        ).collect(Collectors.toList());
    }

    public Author getWriter() {
        return writer;
    }

    public List<Book> getBooks() {
        return books;
    }
}
