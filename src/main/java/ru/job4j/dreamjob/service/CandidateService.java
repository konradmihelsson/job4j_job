package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.CandidateDBStore;

import java.util.ArrayList;
import java.util.List;

@ThreadSafe
@Service
public class CandidateService {

    private final CandidateDBStore store;

    public CandidateService(CandidateDBStore store) {
        this.store = store;
    }

    public List<Candidate> findAll() {
        return new ArrayList<>(store.findAll());
    }

    public void add(Candidate candidate) {
        this.store.add(candidate);
    }

    public void update(Candidate candidate) {
        this.store.update(candidate);
    }

    public Candidate findById(int id) {
        return this.store.findById(id);
    }
}
