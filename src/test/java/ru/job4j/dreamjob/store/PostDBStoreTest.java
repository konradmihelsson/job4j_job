package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

public class PostDBStoreTest {

    static Connection connection;

    @BeforeAll
    public static void initConnection() {
        try (InputStream in = PostDBStoreTest.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("jdbc.driver"));
            connection = DriverManager.getConnection(
                    config.getProperty("jdbc.url"),
                    config.getProperty("jdbc.username"),
                    config.getProperty("jdbc.password")

            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @AfterEach
    public void wipeTables() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "TRUNCATE TABLE posts;"
                        + "ALTER TABLE posts ALTER COLUMN id RESTART WITH 1;")
        ) {
            ps.execute();
        }
    }

    @Test
    public void whenCreatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java Job");
        City city = new City(1, "Санкт-Петербург");
        post.setCity(city);
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName()).isEqualTo(post.getName());
    }

    @Test
    public void whenUpdatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java Job");
        City city = new City(1, "Новосибирск");
        String updatedName = "Mid-Level Java Backend Developer Job (Remote)";

        post.setCity(city);
        store.add(post);

        post.setName(updatedName);
        store.update(post);

        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName()).isEqualTo(updatedName);
    }

    @Test
    public void whenFindAll() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post1 = new Post(0, "Java Job");
        Post post2 = new Post(0, "Mid-Level Java Backend Developer Job (Remote)");
        Post post3 = new Post(0, "Mid-Level Java Backend Developer Job (Office)");
        City city = new City(1, "Владивосток");

        post1.setCity(city);
        post2.setCity(city);
        post3.setCity(city);

        List<Post> expected = Arrays.asList(post1, post2, post3);

        store.add(post1);
        store.add(post2);
        store.add(post3);

        List<Post> resultFromDB = store.findAll();
        assertThat(resultFromDB).hasSameSizeAs(expected).hasSameElementsAs(expected);
    }
}
