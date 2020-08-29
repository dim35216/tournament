package entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Ergebnis {
    private int heim_punkte;
    private int gast_punkte;
    private UUID eingetragenVon;
    private UUID bestaetigtVon;

    public Ergebnis() {}

    public boolean isEingetragen() {
        return eingetragenVon != null;
    }

    public boolean isBestaetigt() {
        return bestaetigtVon != null;
    }

    public int getHeim_punkte() {
        return heim_punkte;
    }

    public void setPunkte(int h, int g) {
        heim_punkte = h;
        gast_punkte = g;
    }

    public int getGast_punkte() {
        return gast_punkte;
    }

    public UUID getEingetragenVon() {
        return eingetragenVon;
    }

    public void setEingetragenVon(UUID eingetragenVon) {
        this.eingetragenVon = eingetragenVon;
    }

    public UUID getBestaetigtVon() {
        return bestaetigtVon;
    }

    public void setBestaetigtVon(UUID bestaetigtVon) {
        this.bestaetigtVon = bestaetigtVon;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Ergebnis)) return false;
        Ergebnis other = (Ergebnis) o;
        return (heim_punkte == other.heim_punkte && gast_punkte == other.gast_punkte);
    }

    @Override
    public int hashCode() {
        return heim_punkte * 1000 + gast_punkte;
    }

    @Override
    public String toString() {
        return heim_punkte + ":" + gast_punkte;
    }
}
