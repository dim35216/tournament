package entity;

import app.MyRestException;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Team {

    @XmlAttribute
    private UUID id;
    private String name;
    private int max_teilnehmer;
    private Set<UUID> teilnehmerListe;

    public Team() {}

    public Team(String name, int max_teilnehmer) {
        this.name = name;
        this.max_teilnehmer = max_teilnehmer;
        id = UUID.randomUUID();
        teilnehmerListe = new HashSet<UUID>();
    }

    public void addTeilnehmer(Teilnehmer t) {
        if(teilnehmerListe.size() < max_teilnehmer) {
            teilnehmerListe.add(t.getId());
        } else {
            throw new MyRestException(500, "Das Team " + this + " hat bereits de maximale Anzahl an Teilnehmer erreicht");
        }
    }

    public void löschenTeilnehmer(Teilnehmer t) {
        if(teilnehmerListe.contains(t.getId())) {
            teilnehmerListe.remove(t.getId());
        } else {
            throw new MyRestException(500, "Im Team " + this + " ist kein Teilnehmer " + t + " eingeschrieben");
        }
    }

    public boolean enthältTeilnehmerById(UUID teilnehmerid) {
        if(teilnehmerListe.contains(teilnehmerid)) {
            return true;
        }
        return false;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Team)) return false;
        Team other = (Team) o;
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
