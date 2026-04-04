package com.example.BookStore_Production.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//@Getter
//@Setter
public class BookEntryResponseDTO {

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    private Long id;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    private String title;
    private String content;
    private LocalDateTime date;
}
