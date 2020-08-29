package service;

import app.MyRestException;
import entity.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("tournament")
public class TurnierService {

    private static Turnier turnier = new Turnier();

    @POST
    @Path("participant")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public Teilnehmer einschreibenTeilnehmer(Teilnehmer t) {
        if(t == null) {
            throw new MyRestException(400, "Es wird kein Teilnehmer mitgesendet");
        }
        t.setId(UUID.randomUUID());
        turnier.getTeilnehmer().put(t.getId(), t);
        turnier.newZustand_teilnehmer();
        return t;
    }

    @DELETE
    @Path("participant/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Teilnehmer löschenTeilnehmer(@PathParam("id") String id) {
        UUID uuid = convertId(id);
        if(turnier.getTeilnehmer().containsKey(uuid)) {
            turnier.newZustand_teilnehmer();
            return turnier.getTeilnehmer().remove(uuid);
        } else {
            throw new MyRestException(404, "Teilnehmer mit ID " + uuid + " ist nicht eingeschrieben");
        }
    }

    @GET
    @Path("participant/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Teilnehmer getTeilnehmerById(@PathParam("id") String id) {
        UUID uuid = convertId(id);
        Teilnehmer teilnehmer = turnier.getTeilnehmer().get(uuid);
        if(teilnehmer != null) {
            return teilnehmer;
        } else {
            throw new MyRestException(404, "Teilnehmer mit ID " + uuid + " ist nicht eingeschrieben");
        }
    }

    @GET
    @Path("participants")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Teilnehmer> getTeilnehmerByName(@QueryParam("firstname") String vorname, @QueryParam("lastname") String nachname) {
        Set<Teilnehmer> teilnehmerListe = new HashSet();
        for(Teilnehmer t : turnier.getTeilnehmer().values()) {
            if(vorname != null) {
                if(nachname != null) {
                    if(vorname.equals(t.getVorname()) && nachname.equals(t.getNachname())) {
                        teilnehmerListe.add(t);
                    }
                } else {
                    if(vorname.equals(t.getVorname())) {
                        teilnehmerListe.add(t);
                    }
                }
            } else {
                if(nachname != null) {
                    if(nachname.equals(t.getNachname())) {
                        teilnehmerListe.add(t);
                    }
                } else {
                    teilnehmerListe.add(t);
                }
            }
        }
        /*if(!teilnehmerListe.isEmpty()) {
            return teilnehmerListe;
        } else {
            String message = "Es sind keine Teilnehmer";
            if(vorname != null) {
                if(nachname != null) {
                    message += " mit dem Namen " + vorname + " " + nachname;
                } else {
                    message += " mit dem Vornamen " + vorname;
                }
            } else {
                if(nachname != null) {
                    message += " mit dem Nachnamen " + nachname;
                }
            }
            throw new MyRestException(404, message + " eingeschrieben");
        }*/
        return teilnehmerListe;
    }

    @GET
    @Path("participants/state")
    public String getZustand_teilnehmer() {
        return turnier.getZustand_teilnehmer().toString();
    }

    @PUT
    @Path("participant/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Teilnehmer updateStudentAccount(@PathParam("id") String id, Teilnehmer newData) {
        UUID uuid = convertId(id);
        if(newData.getId() != null) {
            throw new MyRestException(400, "Die ID eines Teilnehmers kann nicht überschrieben werden");
        }

        if(turnier.getTeilnehmer().containsKey(uuid)) {
            Teilnehmer t = turnier.getTeilnehmer().get(uuid);
            if(newData.getVorname() != null) {
                t.setVorname(newData.getVorname());
            }
            if(newData.getNachname() != null) {
                t.setNachname(newData.getNachname());
            }
            if(newData.isSchiedsrichter() != null) {
                t.setSchiedsrichter(newData.isSchiedsrichter());
            }
            turnier.newZustand_teilnehmer();
            return t;
        } else {
            throw new MyRestException(404, "Teilnehmer mit ID " + uuid + " ist nicht eingeschrieben");
        }

    }

    @POST
    @Path("match")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Partie erstellenPartie(Partie p) {
        p.setId(UUID.randomUUID());
        turnier.getPartien().put(p.getId(), p);
        return p;
    }

    @DELETE
    @Path("match/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Partie löschenPartie(@PathParam("id") String id) {
        UUID uuid = convertId(id);
        if(turnier.getTeilnehmer().containsKey(uuid)) {
            return turnier.getPartien().remove(uuid);
        } else {
            throw new MyRestException(404, "Partie mit ID " + uuid + " existiert nicht");
        }
    }

    @GET
    @Path("match/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Partie getPartieById(@PathParam("id") String id) {
        UUID uuid = convertId(id);
        Partie p = turnier.getPartien().get(uuid);
        if(p != null) {
            return p;
        } else {
            throw new MyRestException(404, "Partie mit ID " + uuid + " existiert nicht");
        }
    }

    @PUT
    @Path("match/{matchid}/result/{teilnehmerid}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Partie eintragenErgebnis(@PathParam("id") String mid, @PathParam("teilnehmerid") String tid, Ergebnis ergebnis) {
        UUID matchid = convertId(mid);
        Partie p = turnier.getPartien().get(matchid);
        if(p != null) {
            Teilnehmer teilnehmer = getTeilnehmerById(tid);
            p.eintragenErgebnis(ergebnis.getHeim_punkte(), ergebnis.getGast_punkte(), teilnehmer);
            return p;
        } else {
            throw new MyRestException(404, "Partie mit ID " + matchid + " existiert nicht");
        }
    }

    @POST
    @Path("team")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Team erstellenTeam(Team t) {
        t.setId(UUID.randomUUID());
        turnier.getTeams().put(t.getId(), t);
        turnier.newZustand_teams();
        return t;
    }

    @DELETE
    @Path("team/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Team löschenTeam(@PathParam("id") String id) {
        UUID uuid = convertId(id);
        if(turnier.getTeams().containsKey(uuid)) {
            turnier.newZustand_teams();
            return turnier.getTeams().remove(uuid);
        } else {
            throw new MyRestException(404, "Team mit ID " + uuid + " existiert nicht");
        }
    }

    @GET
    @Path("team/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Team getTeamById(@PathParam("id") String id) {
        UUID uuid = convertId(id);
        Team t = turnier.getTeams().get(uuid);
        if(t != null) {
            return t;
        } else {
            throw new MyRestException(404, "Team mit ID " + uuid + " existiert nicht");
        }
    }

    @GET
    @Path("teams")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Team> getAllTeams() {
        return turnier.getTeams().values();
    }

    @GET
    @Path("teams/state")
    public String getZustand_teams() {
        return turnier.getZustand_teams().toString();
    }

    @PUT
    @Path("team/{teamid}/add/{teilnehmerid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Team addTeilnehmerZuTeam(@PathParam("teamid") String teaid, @PathParam("teilnehmerid") String teiid) {
        UUID teamid = convertId(teaid);
        UUID teilnehmerid = convertId(teiid);
        if(turnier.getTeams().containsKey(teamid)) {
            Team team = turnier.getTeams().get(teamid);
            if(turnier.getTeilnehmer().containsKey(teilnehmerid)) {
                Teilnehmer teilnehmer = turnier.getTeilnehmer().get(teilnehmerid);
                team.addTeilnehmer(teilnehmer);
                turnier.newZustand_teams();
                return team;
            } else {
                throw new MyRestException(404, "Teilnehmer mit ID " + teilnehmerid + " ist nicht eingeschrieben");
            }
        } else {
            throw new MyRestException(404, "Team mit ID " + teamid + " existiert nicht");
        }
    }

    @PUT
    @Path("team/{teamid}/remove/{teilnehmerid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Team löschenTeilnehmerZuTeam(@PathParam("teamid") String teaid, @PathParam("teilnehmerid") String teiid) {
        UUID teamid = convertId(teaid);
        UUID teilnehmerid = convertId(teiid);
        if(turnier.getTeams().containsKey(teamid)) {
            Team team = turnier.getTeams().get(teamid);
            if(turnier.getTeilnehmer().containsKey(teilnehmerid)) {
                Teilnehmer teilnehmer = turnier.getTeilnehmer().get(teilnehmerid);
                team.löschenTeilnehmer(teilnehmer);
                turnier.newZustand_teams();
                return team;
            } else {
                throw new MyRestException(404, "Teilnehmer mit ID " + teilnehmerid + " ist nicht eingeschrieben");
            }
        } else {
            throw new MyRestException(404, "Team mit ID " + teamid + " existiert nicht");
        }
    }

    private UUID convertId(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch(IllegalArgumentException e) {
            throw new MyRestException(400, "Die eingegebene ID " + id + " ist invalide");
        }
        return uuid;
    }

}
