package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.roles.team.Team;
import it.unicam.ids2026.hackhub.roles.team.Utente;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class TeamManager {

    @Getter
    private final Set<Team> teams;

    public Team getTeam(String teamName) {
        Team toReturn = null;
        for (Team team : teams) {
            if (team.getNome().equals(teamName)) {
                toReturn = team;
            }
        }
        return toReturn;
    };

    public Team getTeam(Utente user) {
        Team toReturn = null;
        for (Team team : teams) {
            if (team.getMembri().contains(user)) {
                toReturn = team;
            }
        }
        return toReturn;
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public void removeTeam(Team team) {
        teams.remove(team);
    }

}
