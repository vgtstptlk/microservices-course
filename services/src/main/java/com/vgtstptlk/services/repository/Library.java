package com.vgtstptlk.services.repository;

import com.vgtstptlk.services.entity.Author;
import com.vgtstptlk.services.entity.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class Library {
    public List<Book> bookList;
    public List<Author> writers;

    public Library() {
        this.bookList = new ArrayList<>();
        this.writers = new ArrayList<>();

        Author writer = new Author("Харуки", "Мураками", "");
        this.writers.addAll(List.of(writer, new Author("Федор", "Достоевский", "Михайлович"),
                new Author("Максим", "Горький", "")));
        this.bookList.addAll(List.of(new Book("1Q84", writer), new Book("Норвежский лес", writer),
                new Book("Охота на овец", writer),
                new Book("Идиот", this.writers.get(1)), new Book("Братья Карамазовы", this.writers.get(1)), new Book("Бесы", this.writers.get(1)),
                new Book("На дне", this.writers.get(2)), new Book("Старуха Изергиль", this.writers.get(2)), new Book("Мои университеты", this.writers.get(2))));
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public List<Author> getWriters() {
        return writers;
    }
}
