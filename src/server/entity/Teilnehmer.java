package entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Teilnehmer {

    @XmlAttribute
    private UUID id;
    private String vorname;
    private String nachname;
    private Boolean schiedsrichter;

    // Default-Konstruktor zwingend notwendig
    public Teilnehmer() {}

    public Teilnehmer(String vorname, String nachname, boolean s) {
        id = UUID.randomUUID();
        this.vorname = vorname;
        this.nachname = nachname;
        schiedsrichter = s;
    }

    public UUID getId() {
        return id;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public Boolean isSchiedsrichter() {
        return schiedsrichter;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public void setSchiedsrichter(boolean schiedsrichter) {
        this.schiedsrichter = schiedsrichter;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Teilnehmer)) return false;
        Teilnehmer other = (Teilnehmer) o;
        return id.compareTo(other.id) == 0;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return vorname + " " + nachname;
    }

}
