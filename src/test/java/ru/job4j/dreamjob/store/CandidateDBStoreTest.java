package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class CandidateDBStoreTest {

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
                "TRUNCATE TABLE candidates;"
                        + "ALTER TABLE candidates ALTER COLUMN id RESTART WITH 1;")
        ) {
            ps.execute();
        }
    }

    @Test
    void whenCreateCandidate() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Jorik", "Java Backend Developer");
        store.add(candidate);
        Candidate candidateFromDB = store.findById(candidate.getId());
        assertThat(candidateFromDB.getName()).isEqualTo(candidate.getName());
    }

    @Test
    void whenUpdateCandidate() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Jorik", "Java Backend Developer");
        String updatedDesc = "Mid-Level Java Backend Developer";

        store.add(candidate);
        candidate.setDesc(updatedDesc);
        store.update(candidate);

        Candidate candidateFromDB = store.findById(candidate.getId());
        assertThat(candidateFromDB.getDesc()).isEqualTo(candidate.getDesc());
    }

    @Test
    void whenFindAll() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate1 = new Candidate(0, "Mishka", "Java Developer");
        Candidate candidate2 = new Candidate(0, "Grishka", "Rust Developer");
        Candidate candidate3 = new Candidate(0, "Poschipai", "Bash scripts Developer");

        List<Candidate> expected = Arrays.asList(candidate1, candidate2, candidate3);

        store.add(candidate1);
        store.add(candidate2);
        store.add(candidate3);

        List<Candidate> resultFromDB = store.findAll();
        assertThat(resultFromDB).hasSameSizeAs(expected).hasSameElementsAs(expected);
    }
}
