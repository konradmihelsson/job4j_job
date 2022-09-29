package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostDBStore;

import java.util.List;

@ThreadSafe
@Service
public class PostService {

    private final PostDBStore store;
    private final CityService cityService;

    public PostService(PostDBStore store, CityService cityService) {
        this.store = store;
        this.cityService = cityService;
    }

    public List<Post> findAll() {
        List<Post> posts = store.findAll();
        posts.forEach(post -> post.setCity(cityService.findById(post.getCity().getId())));
        return posts;
    }

    public void add(Post post) {
        this.store.add(post);
    }

    public void update(Post post) {
        this.store.update(post);
    }

    public Post findById(int id) {
        Post post = this.store.findById(id);
        post.setCity(cityService.findById(post.getCity().getId()));
        return post;
    }
}
