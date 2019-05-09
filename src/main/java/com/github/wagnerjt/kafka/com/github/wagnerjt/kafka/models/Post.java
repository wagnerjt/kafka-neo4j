package com.github.wagnerjt.kafka.com.github.wagnerjt.kafka.models;

/**
 * Identifies a Post a user writes, with some name and description
 */
public class Post {
    private String id;
    private String name;
    private String description;
    private String authorId;


//    public Post(String id, String name, String description, String authorId) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.authorId = authorId;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}
