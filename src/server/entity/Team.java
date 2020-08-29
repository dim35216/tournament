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
    private Set<Teilnehmer> teilnehmerListe;
    private String name;
    private int max_teilnehmer;

    public Team() {}

    public Team(String name, int max_teilnehmer) {
        this.name = name;
        this.max_teilnehmer = max_teilnehmer;
        id = UUID.randomUUID();
        teilnehmerListe = new HashSet<Teilnehmer>();
    }

    public void addTeilnehmer(Teilnehmer t) {
        if(teilnehmerListe.size() < max_teilnehmer) {
            teilnehmerListe.add(t);
        } else {
            throw new MyRestException(500, "Das Team " + this + " hat bereits de maximale Anzahl an Teilnehmer erreicht");
        }
    }

    public void löschenTeilnehmer(Teilnehmer t) {
        if(teilnehmerListe.contains(t)) {
            teilnehmerListe.remove(t);
        } else {
            throw new MyRestException(500, "Im Team " + this + " ist kein Teilnehmer " + t + " eingeschrieben");
        }
    }

    public boolean enthältTeilnehmerById(UUID teilnehmerid) {
        if(teilnehmerid == null) {
            return false;
        }
        for( Teilnehmer teilnehmer : teilnehmerListe) {
            if(teilnehmerid.compareTo(teilnehmer.getId()) == 0) {
                return true;
            }
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
    public String toString() {
        return name;
    }
}
