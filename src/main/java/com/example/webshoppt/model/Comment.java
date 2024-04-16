package com.example.webshoppt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private int id;
    private int parentId;
    private int productId;
    private int userId;
    private int chatId;
    private int rating;
    private String title;
    private String body;
    ArrayList<Comment> replies;
    LocalDate commentDate;

    @Override
    public String toString() {
        return userId + "\t|\t" + rating + "\t|\t" + title + " | " + body;
    }
}
