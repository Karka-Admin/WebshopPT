package com.example.webshoppt.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Comment {
    private int id;
    private int rating;
    private User commentUser;
    private String title;
    private String commentBody;
    List<Comment> replies;
    LocalDate commentDate;
}
