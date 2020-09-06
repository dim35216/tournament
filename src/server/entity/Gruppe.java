package entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Gruppe {

    @XmlAttribute
    private UUID id;
    private String name;
    private List<UUID> teams;
    private List<Partie> partien;

    public Gruppe() {}

    public Gruppe(String name) {
        this.name = name;
        id = UUID.randomUUID();
        teams = new LinkedList<>();
        partien = new LinkedList<>();
    }

    public void addTeam(UUID teamid) {
        teams.add(teamid);
    }

    public List<UUID> getTeams() {
        return teams;
    }

    public void addPartie(Partie partie) {
        partien.add(partie);
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Gruppe)) return false;
        Gruppe other = (Gruppe) o;
        return id.compareTo(other.id) == 0;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

}
