package com.vgtstptlk.services.controller;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.vgtstptlk.services.entity.Book;
import com.vgtstptlk.services.entity.Note;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.client.ClientBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/NotificationDesk")
public class NotificationDesk {
    List<Note> notes;

    public NotificationDesk() {
        notes = new ArrayList<>();
    }

    @GetMapping("/Notes")
    public List<Note> getNotes(){
        return notes;
    }

    @PostMapping("/Notes")
    public ResponseEntity<?> createNotes(@RequestBody Note note){
        notes.add(note);
        return ResponseEntity.ok(String.format("Сообщение %s добавлено в %s", note.getMessage(),
                note.getTime().toString()));
    }

    @DeleteMapping("/Notes")
    public ResponseEntity<?> deleteNotes(@RequestBody Note note){
        Optional<Note> optionalNote = this.notes.stream().filter(
                (Note b) -> b.getMessage().equalsIgnoreCase(note.getMessage())
        ).findFirst();

        if (optionalNote.isEmpty()){
            return ResponseEntity.ok(String.format("Сообщение не найдено %s", note.getMessage()));
        }

        notes.remove(optionalNote.get());
        return ResponseEntity.ok(String.format("Сообщение %s удалено", note.getMessage()));
    }

    @GetMapping("/Notes/Flush")
    public ResponseEntity<?> clearNotes(){
        notes.clear();
        return ResponseEntity.ok("Все сообщения удалены");
    }
}
