package entity;

import app.MyRestException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Partie {

    @XmlAttribute
    private UUID id;
    private Team heim;
    private Team gast;
    private Ergebnis ergebnis;

    public Partie() {}

    public Partie(Team heim, Team gast) {
        id = UUID.randomUUID();
        this.heim = heim;
        this.gast = gast;
        ergebnis = new Ergebnis();
    }

    public void eintragenErgebnis(int heim_punkte, int gast_punkte, Teilnehmer teilnehmer) {
        if(ergebnis.getEingetragenVon() == null) { //Eintragen
            if (teilnehmer.isSchiedsrichter() ||
                    heim.enthältTeilnehmerById(teilnehmer.getId()) || gast.enthältTeilnehmerById(teilnehmer.getId())) {
                ergebnis.setPunkte(heim_punkte, gast_punkte);
                ergebnis.setEingetragenVon(teilnehmer.getId());
                if (teilnehmer.isSchiedsrichter()) {
                    ergebnis.setBestaetigtVon(teilnehmer.getId());
                }
            }
        } else if(ergebnis.getBestaetigtVon() == null) { //Bestätigen
            if(teilnehmer.isSchiedsrichter()) {
                ergebnis.setPunkte(heim_punkte, gast_punkte);
                ergebnis.setEingetragenVon(teilnehmer.getId());
                ergebnis.setBestaetigtVon(teilnehmer.getId());
            } else if(heim.enthältTeilnehmerById(ergebnis.getEingetragenVon())) {
                if(heim.enthältTeilnehmerById(teilnehmer.getId())) {
                    ergebnis.setPunkte(heim_punkte, gast_punkte);
                    ergebnis.setEingetragenVon(teilnehmer.getId());
                } else if(gast.enthältTeilnehmerById(teilnehmer.getId())) {
                    if(ergebnis.getHeim_punkte() == heim_punkte && ergebnis.getGast_punkte() == gast_punkte) {
                        ergebnis.setBestaetigtVon(teilnehmer.getId());
                    } else {
                        ergebnis.setPunkte(heim_punkte, gast_punkte);
                        ergebnis.setEingetragenVon(teilnehmer.getId());
                    }
                } else {
                    throw new MyRestException(403, "Das Ergebnis kann vom Teilnehmer " + teilnehmer + " nicht eingetragen werden");
                }
            } else if(gast.enthältTeilnehmerById(ergebnis.getEingetragenVon())) {
                if(gast.enthältTeilnehmerById(teilnehmer.getId())) {
                    ergebnis.setPunkte(heim_punkte, gast_punkte);
                    ergebnis.setEingetragenVon(teilnehmer.getId());
                } else if(heim.enthältTeilnehmerById(teilnehmer.getId())) {
                    if(ergebnis.getHeim_punkte() == heim_punkte && ergebnis.getGast_punkte() == gast_punkte) {
                        ergebnis.setBestaetigtVon(teilnehmer.getId());
                    } else {
                        ergebnis.setPunkte(heim_punkte, gast_punkte);
                        ergebnis.setEingetragenVon(teilnehmer.getId());
                    }
                } else {
                    throw new MyRestException(403, "Das Ergebnis kann vom Teilnehmer " + teilnehmer + " nicht eingetragen werden");
                }
            }
        } else { //Von Schiedsrichter nachträglich ändern
            if(teilnehmer.isSchiedsrichter()) {
                ergebnis.setPunkte(heim_punkte, gast_punkte);
                ergebnis.setEingetragenVon(teilnehmer.getId());
                ergebnis.setBestaetigtVon(teilnehmer.getId());
            } else {
                throw new MyRestException(403, "Das Ergebnis kann vom Teilnehmer " + teilnehmer + " nicht mehr geändert werden");
            }
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Team getHeim() {
        return heim;
    }

    public void setHeim(Team heim) {
        this.heim = heim;
    }

    public Team getGast() {
        return gast;
    }

    public void setGast(Team gast) {
        this.gast = gast;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Partie)) return false;
        Partie other = (Partie) o;
        return id.compareTo(other.id) == 0;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return heim + " - " + gast + ": " + ergebnis;
    }
}
