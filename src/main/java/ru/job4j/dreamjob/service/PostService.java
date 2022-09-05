package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;

import java.util.ArrayList;
import java.util.List;

public class PostService {

    private static final PostService INST = new PostService();
    private final PostStore postStore = PostStore.instOf();

    public static PostService instOf() {
        return INST;
    }

    public List<Post> findAll() {
        return new ArrayList<>(postStore.findAll());
    }
}
