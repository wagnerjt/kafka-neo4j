package com.github.wagnerjt.kafka.com.github.wagnerjt.kafka.models;

import java.util.List;

public class Data {
    private List<Author> authors;
    private List<Post> posts;

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
