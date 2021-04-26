package com.vgtstptlk.services.repository;

import com.vgtstptlk.services.entity.Author;
import com.vgtstptlk.services.entity.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Library {
    public List<Book> bookList;
    public List<Author> writers;
    @Value("${db.url}")
    private String dbUrl;
    @Value("${db.username}")
    private String dbUser;

    public boolean addAuthor(String name, String lastname, String patronym){
        try {
            Class.forName("org.postgresql.Driver");
            Connection c = DriverManager.getConnection(dbUrl, dbUser, "");
            PreparedStatement st = c.prepareStatement(
                    "INSERT INTO authors (name, lastname, patronym) VALUES (?, ?, ?);"
            );

            st.setString(1, name);
            st.setString(2, lastname);
            st.setString(3, patronym);
            st.execute();
        } catch (ClassNotFoundException | SQLException e) {
            return false;
        }

        return true;
    }

    public String addBook(String name, String lastname, String patronym, String caption){
        try {
            Class.forName("org.postgresql.Driver");
            Connection c = DriverManager.getConnection(dbUrl, dbUser, "");
            PreparedStatement st = c.prepareStatement(
                    "select id from authors where name like ? and lastname like ? and patronym like ?;"
            );

            st.setString(1, name);
            st.setString(2, lastname);
            st.setString(3, patronym);
            ResultSet resultSet = st.executeQuery();

            if (!resultSet.next()){
                return String.format("%s %s %s not found", name, lastname, patronym);
            }
        } catch (ClassNotFoundException | SQLException e) {
            return "error";
        }

        return "ok";
    }

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

    public boolean refresh(){
        bookList.clear();
        writers.clear();

        try {
            Class.forName("org.postgresql.Driver");
            Connection c = DriverManager.getConnection(dbUrl, dbUser, "");
            Statement st = c.createStatement();
            ResultSet resultSet = st.executeQuery("select id, name, lastname, patronym from authors;");
            while (resultSet.next()){
                writers.add(new Author(resultSet.getString("name"),
                        resultSet.getString("lastname"), resultSet.getString("patronym")));

                PreparedStatement st2 = c.prepareStatement("select caption from books where author = ?");
                st2.setInt(1, resultSet.getInt("id"));
                ResultSet resultSet2 = st2.executeQuery();
                while (resultSet2.next()){
                    bookList.add(new Book(resultSet2.getString("caption"), writers.get(writers.size()-1)));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            return false;
        }

        return true;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public List<Author> getWriters() {
        return writers;
    }
}
