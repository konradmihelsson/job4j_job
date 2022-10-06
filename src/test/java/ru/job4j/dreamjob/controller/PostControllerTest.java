package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PostControllerTest {

    @Test
    public void whenPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1, "New post"),
                new Post(2, "New post")
        );
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        when(postService.findAll()).thenReturn(posts);
        CityService cityService = mock(CityService.class);
        HttpSession httpSession = mock(HttpSession.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.posts(model, httpSession);
        verify(model).addAttribute("posts", posts);
        assertThat(page).isEqualTo("posts");
    }

    @Test
    public void whenCreatePost() {
        City city = new City(0, "Samarkand");
        Post input = new Post(1, "New post");
        input.setCity(city);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.savePost(input);
        verify(postService).add(input);
        assertThat(page).isEqualTo("redirect:/posts");
    }

    @Test
    public void whenUpdatePost() {
        City city = new City(0, "Samarkand");
        Post input = new Post(1, "New post");
        input.setCity(city);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.updatePost(input);
        verify(postService).update(input);
        assertThat(page).isEqualTo("redirect:/posts");
    }

    @Test
    public void whenFormAddPost() {
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        HttpSession httpSession = mock(HttpSession.class);
        String page = postController.addPost(model, httpSession);
        verify(model).addAttribute("cities", cityService.getAllCities());
        assertThat(page).isEqualTo("addPost");
    }
}
