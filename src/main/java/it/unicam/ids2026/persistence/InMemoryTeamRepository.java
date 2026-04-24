package it.unicam.ids2026.persistence;

import it.unicam.ids2026.core.roles.team.Team;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryTeamRepository extends InMemoryRepository<Team, String> implements TeamRepository {

    public InMemoryTeamRepository() {
        super(Team::getNome);
    }
}
