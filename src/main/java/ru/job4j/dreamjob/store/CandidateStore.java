package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class CandidateStore {

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final AtomicInteger ids = new AtomicInteger(4);

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Misha", "Junior Java Developer", new Date()));
        candidates.put(2, new Candidate(2, "Grisha", "Middle Java Developer", new Date()));
        candidates.put(3, new Candidate(3, "Poschipay", "Senior Java Developer", new Date()));
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public void add(Candidate candidate) {
        candidate.setId(ids.incrementAndGet());
        candidate.setCreated(new Date());
        candidates.put(candidate.getId(), candidate);
    }

    public void update(Candidate candidate) {
        this.candidates.replace(candidate.getId(), candidate);
    }

    public Candidate findById(int id) {
        return this.candidates.get(id);
    }
}