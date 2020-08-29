package entity;

import java.util.*;

public class Turnier {

    private Map<UUID, Teilnehmer> teilnehmer;
    private Map<UUID, Team> teams;
    private Map<UUID, Partie> partien;

    private UUID zustand_teilnehmer;
    private UUID zustand_teams;

    public Turnier() {
        teilnehmer = new HashMap<UUID, Teilnehmer>();
        teams = new HashMap<UUID, Team>();
        partien = new HashMap<UUID, Partie>();

        zustand_teilnehmer = UUID.randomUUID();
        zustand_teams = UUID.randomUUID();
    }

    public Map<UUID, Teilnehmer> getTeilnehmer() {
        return teilnehmer;
    }

    public Map<UUID, Team> getTeams() {
        return teams;
    }

    public Map<UUID, Partie> getPartien() {
        return partien;
    }

    public UUID getZustand_teilnehmer() {
        return zustand_teilnehmer;
    }

    public UUID getZustand_teams() {
        return zustand_teams;
    }

    public void newZustand_teilnehmer() {
        zustand_teilnehmer = UUID.randomUUID();
    }

    public void newZustand_teams() {
        zustand_teams = UUID.randomUUID();
    }

}
