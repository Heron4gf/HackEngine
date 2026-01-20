package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.roles.Team;
import it.unicam.ids2026.hackhub.roles.Utente;

import java.util.Set;

public class TeamManager {
    private Set<Team> teams;

    public TeamManager() {
    }


    public Team getTeam(String teamName) {
        Team toReturn = null;
        for (Team team : teams) {
            if (team.getName().equals(teamName)) {
                toReturn = team;
            }
        }
        return toReturn;
    };

    public Team getTeam(Utente user) {
        Team toReturn = null;
        for (Team team : teams) {
            if (team.getUsers().contains(user)) {
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
