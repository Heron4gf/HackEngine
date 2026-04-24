package it.unicam.ids2026.persistence;

import it.unicam.ids2026.core.hackathon.Hackathon;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class InMemoryHackathonRepository extends InMemoryRepository<Hackathon, UUID> implements HackathonRepository {

    public InMemoryHackathonRepository() {
        super(Hackathon::getId);
    }
}
