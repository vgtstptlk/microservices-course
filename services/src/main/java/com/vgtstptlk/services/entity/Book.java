package com.vgtstptlk.services.entity;

public class Book {
    private String caption;
    private Author writer;

    public Book(String caption, Author writer) {
        this.caption = caption;
        this.writer = writer;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Author getWriter() {
        return writer;
    }

    public void setWriter(Author writer) {
        this.writer = writer;
    }
}
