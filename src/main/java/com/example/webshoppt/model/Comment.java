package com.example.webshoppt.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Comment {
    private int id;
    private int parentCommentId;
    private int productId;
    private int rating;
    private int userId;
    private String title;
    private String commentBody;
    List<Comment> replies;
    LocalDate commentDate;

    public Comment() {
        this.id = 0;
        this.parentCommentId = 0;
        this.productId = 0;
        this.rating = 0;
        this.userId = 0;
        this.title = null;
        this.commentBody = null;
        this.commentDate = null;
    }

    public Comment(int id,
                   int parentCommentId,
                   int productId,
                   int rating,
                   int userId,
                   String title,
                   String commentBody,
                   LocalDate commentDate) {
        this.id = id;
        this.parentCommentId = parentCommentId;
        this.productId = productId;
        this.rating = rating;
        this.userId = userId;
        this.title = title;
        this.commentBody = commentBody;
        this.commentDate = commentDate;
    }

    @Override
    public String toString() {
        return Integer.toString(userId) + "\t|\t" + Integer.toString(rating) + "\t|\t" + title + " | " + commentBody;
    }
}
