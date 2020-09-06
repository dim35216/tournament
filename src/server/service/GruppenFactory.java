package service;

import entity.Gruppe;
import entity.Team;

import java.util.*;

public class GruppenFactory {

    public static List<Gruppe> gruppenErstellen(int anzahlGruppen, Collection<Team> teams) {
        List<Gruppe> gruppen = new LinkedList<>();
        for(int i = 0; i < anzahlGruppen; i++) {
            gruppen.add(new Gruppe("Group " + (i + 1)));
        }

        // Klonen der Teams-Liste und zufälliges Umsortieren
        List<Team> cloned_teams = new LinkedList<>(teams);
        Collections.shuffle(cloned_teams, new Random(42));

        int i = 0;
        for( Team team : teams ) {
            if(i == anzahlGruppen) {
                i = 0;
            }
            gruppen.get(i).addTeam(team.getId());
            i++;
        }

        for( Gruppe gruppe : gruppen ) {

            Team[] teamsGruppe = new Team[((gruppe.getTeams().size() + 1) / 2) * 2];
            System.arraycopy(gruppe.getTeams().toArray(), 0, teamsGruppe, 0, gruppe.getTeams().size());

            boolean matrix[][] = new boolean[teamsGruppe.length][teamsGruppe.length];


        }

        return gruppen;
    }

    public static void main(String[] args) {
        List<Runde> runden;

        /*for(int i = 1; i < 10; i++) {
            System.out.println(i + ":");
            runden = GruppenFactory.erzeugePaarungen3(2 * i);
            druckeRunden(runden);
        }*/

        runden = GruppenFactory.erzeugePaarungen3_warmstart(9);
        System.out.println();
        druckeRunden(runden);
    }

    private static int[][][] erzeugePaarungen(int n) {
        int anzahlRunden = (n % 2 == 0)?n - 1:n;
        int anzahlSpieleProRunde = n / 2;
        int paarungen[][][] = new int[anzahlRunden][anzahlSpieleProRunde][2];
        boolean matrix[][] = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i >= j) {
                    matrix[i][j] = true;
                }
            }
        }
        boolean[] liste;

        int i = 0; // Zeile
        int j = 1; // Spalte
        int i_laststart_r[] = new int[anzahlRunden];
        int j_laststart_r[] = new int[anzahlRunden];
        int i_start_s, j_start_s;
        int i_lastupdate = -1;
        int j_lastupdate = -1;
        int count_endlosschleifen;
        int nochFrei = n * (n - 1) / 2;
        boolean[][][] matrix_copy = new boolean[anzahlRunden][n][n];
        for(int r = 0; r < anzahlRunden; r++) {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("Runde " + (r + 1) + " (i = " + i + " / j = " + j + " / " + nochFrei + "):");

            int s = 0;
            liste = new boolean[n];
            count_endlosschleifen = 0;
            for(int ii = 0; ii < n; ii++) {
                for(int jj = 0; jj < n; jj++) {
                    matrix_copy[r][ii][jj] = matrix[ii][jj];
                }
            }
            while(s < anzahlSpieleProRunde) {
                i_start_s = i;
                j_start_s = j;
                // System.out.println("Spiel " + (s + 1) + " (i = " + i + " / j = " + j + " / " + nochFrei + "):");

                /*if(i == i_lastupdate && j == j_lastupdate) {
                    // System.out.println("Spiel " + (s + 1) + " (i = " + i + " / j = " + j + " / " + nochFrei + "):");

                    System.out.println("Endlosschleife!");
                    count_endlosschleifen++;
                    if(count_endlosschleifen == n * (n - 1) / 2) {
                        s = anzahlSpieleProRunde;
                        r--;
                        liste = new boolean[n];
                        matrix_copy = new boolean[n][n];
                        for(int ii = 0; ii < n; ii++) {
                            for(int jj = 0; jj < n; jj++) {
                                if(ii >= jj) {
                                    matrix_copy[ii][jj] = true;
                                }
                            }
                        }
                        i = i_laststart_r;
                        j = j_laststart_r;
                        do {
                            j++;
                            if (j == n) {
                                j = 0;
                                i = (i + 1) % n;
                            }
                        } while (matrix[i][j] == true);
                        i_laststart_r = i;
                        j_laststart_r = j;
                    } else {
                        s = 0;
                        liste = new boolean[n];
                        i = i_laststart_s;
                        j = j_laststart_s;
                        do {
                            j++;
                            if (j == n) {
                                j = 0;
                                i = (i + 1) % n;
                            }
                        } while (matrix[i][j] == true);
                        i_laststart_s = i;
                        j_laststart_s = j;
                        // Wiederherstellung der Matrix vom Rundenstart
                        for (int ii = 0; ii < n; ii++) {
                            for (int jj = 0; jj < n; jj++) {
                                matrix[ii][jj] = matrix_copy[ii][jj];
                            }
                        }
                    }

                    continue;
                }*/

                /*if(liste[i] == false && liste[j] == false && matrix[i][j] == false) {
                    System.out.println("Spiel " + (s + 1) + " (i = " + i + " / j = " + j + " / " + nochFrei + "):");

                    paarungen[r][s][0] = i;
                    paarungen[r][s][1] = j;
                    liste[i] = true;
                    liste[j] = true;
                    matrix[i][j] = true;
                    nochFrei--;
                    s++;
                    i_lastupdate = i;
                    j_lastupdate = j;

                    druckeMatrix(matrix);
                    druckeListe(liste);
                }*/

                int steps = 0;
                while(steps < nochFrei) {
                    do {
                        j++;
                        if (j == n) {
                            j = 0;
                            i = (i + 1) % n;
                        }
                    } while (matrix[i][j] == true);

                    if(liste[i] == false && liste[j] == false && matrix[i][j] == false) {
                        System.out.println("Spiel " + (s + 1) + " (i = " + i + " / j = " + j + " / " + nochFrei + "):");

                        paarungen[r][s][0] = i;
                        paarungen[r][s][1] = j;
                        liste[i] = true;
                        liste[j] = true;
                        matrix[i][j] = true;
                        nochFrei--;
                        s++;

                        // druckeMatrix(matrix);
                        // druckeListe(liste);

                        break;
                    }

                    steps++;
                }

                if(steps == nochFrei) { // Kein Feld gefunden
                    s--;

                }
            }

            // druckePaarungen(Arrays.copyOf(paarungen, r + 1));
        }

        return paarungen;
    }

    private static void druckeMatrix(Boolean[][] matrix) {
        int zeilen = matrix.length;
        int spalten = matrix[0].length;
        for(int i = 0; i < spalten; i++) {
            System.out.print("--");
        }
        System.out.println("-");
        for(int i = 0; i < zeilen; i++) {
            for(int j = 0; j < spalten; j++) {
                System.out.print("|" + (matrix[i][j]?(i < j?"X":"O"):" "));
            }
            System.out.println("|");
        }
        for(int i = 0; i < spalten; i++) {
            System.out.print("--");
        }
        System.out.println("-");
    }

    private static void druckeListe(Boolean[] liste) {
        int spalten = liste.length;
        for(int i = 0; i < spalten; i++) {
            System.out.print(i + 1 + " ");
        }
        System.out.println();
        for(int i = 0; i < spalten; i++) {
            System.out.print((liste[i]?"X":" ") + " ");
        }
        System.out.println();
    }

    private static void druckePaarungen(Integer[][][] paarungen) {
        int runden = paarungen.length;
        if(runden == 0) {
            return;
        }
        int spiele = paarungen[0].length;
        for(int i = 0; i < runden; i++) {
            for(int j = 0; j < spiele; j++) {
                System.out.print(paarungen[i][j][0] + 1 + " " + (paarungen[i][j][1] + 1) + "   ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void druckeRunden(List<Runde> runden) {
        for( Runde runde : runden ) {
            druckeRunde(runde);
        }
        System.out.println();
    }

    private static void druckeRunde(Runde runde) {
        for( Spiel spiel : runde.spiele ) {
            System.out.print(spiel.getX() + 1 + " " + (spiel.getY() + 1) + "   ");
        }
        System.out.println();
    }

    private static Integer[][][] erzeugePaarungen2(int n) {
        int anzahlRunden = (n % 2 == 0)?n - 1:n;
        int anzahlSpieleProRunde = n / 2;
        Integer paarungen[][][] = new Integer[anzahlRunden][anzahlSpieleProRunde][2];
        Boolean matrix[][] = new Boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i >= j) {
                    matrix[i][j] = Boolean.TRUE;
                } else {
                    matrix[i][j] = Boolean.FALSE;
                }
            }
        }

        if(paarungenFinden(paarungen, matrix, 0, 0) == 1) {
            return paarungen;
        } else {
            return null;
        }
    }

    private static int paarungenFinden(Integer[][][] paarungen, Boolean[][] matrix, Integer i, Integer j) {
        System.out.println("paarungenFinden");

        int n = matrix.length;
        int anzahlRunden = (n % 2 == 0)?n - 1:n;

        Boolean matrix_copy[][][] = new Boolean[anzahlRunden][n][n];
        int i_j_copy[][] = new int[anzahlRunden][2];

        int count_fehler = 0;
        int r_minus = 1;

        int r = 0;
        copy2DMatrix(matrix, matrix_copy[0]);
        i_j_copy[0] = new int[]{i, j};
        while(r < anzahlRunden) {
            System.out.println("r = " + r);

            Integer[] result;
            if((result = rundeFinden(paarungen[r], matrix, i, j)) != null) {
                i = result[0];
                j = result[1];
                r++;
                if(r != anzahlRunden) {
                    copy2DMatrix(matrix, matrix_copy[r]);
                    i_j_copy[r] = new int[]{i, j};
                }
            } else {
                if(++count_fehler > n * (n - 1) / 2) {
                    r_minus += 1;
                }
                r -= r_minus;
                if(r < 0) {
                    return -1;
                }
                copy2DMatrix(matrix_copy[r], matrix);
                i = i_j_copy[r][0];
                j = i_j_copy[r][1];
                System.out.println(i + " " + j + " " + r);
                do {
                    j++;
                    if (j == n) {
                        j = 0;
                        i = (i + 1) % n;
                    }
                    System.out.println(i + " " + j);
                    druckeMatrix(matrix);
                } while (matrix[i][j] == true);
            }
        }

        return 1;
    }

    private static Integer[] rundeFinden(Integer[][] runden_paarungen, Boolean[][] matrix, Integer i, Integer j) {
        System.out.println("rundenFinden");

        int n = matrix.length;
        int anzahlSpieleProRunde = n / 2;

        Boolean liste[] = new Boolean[n];
        for(int k = 0; k < n; k++) {
            liste[k] = Boolean.FALSE;
        }

        Boolean matrix_copy[][][] = new Boolean[anzahlSpieleProRunde][n][n];
        Boolean liste_copy[][] = new Boolean[anzahlSpieleProRunde][n];
        Integer i_j_copy[][] = new Integer[anzahlSpieleProRunde][2];

        int count_fehler = 0;
        int s_minus = 1;

        int s = 0;
        copy2DMatrix(matrix, matrix_copy[0]);
        copy1DListe(liste, liste_copy[0]);
        i_j_copy[0] = new Integer[]{i, j};
        while(s < anzahlSpieleProRunde) {
            System.out.println("s = " + s);

            Integer result[];
            if((result = spielFinden(matrix, liste, i, j)) != null) {
                i = result[0];
                j = result[1];
                s++;
                if(s != anzahlSpieleProRunde) {
                    copy2DMatrix(matrix, matrix_copy[s]);
                    copy1DListe(liste, liste_copy[s]);
                    i_j_copy[s] = new Integer[]{i, j};
                }
            } else {
                if(++count_fehler > n * (n - 1) / 2) {
                    s_minus += 1;
                }
                s -= s_minus;
                if(s < 0) {
                    return null;
                }
                copy2DMatrix(matrix_copy[s], matrix);
                copy1DListe(liste_copy[s], liste);
                i = i_j_copy[s][0];
                j = i_j_copy[s][1];
                do {
                    j++;
                    if (j == n) {
                        j = 0;
                        i = (i + 1) % n;
                    }
                } while (matrix[i][j] == true);
            }
        }

        System.arraycopy(i_j_copy, 1, runden_paarungen, 0, anzahlSpieleProRunde - 1);
        runden_paarungen[anzahlSpieleProRunde - 1][0] = i;
        runden_paarungen[anzahlSpieleProRunde - 1][1] = j;

        return new Integer[]{i, j};
    }

    private static Integer[] spielFinden(Boolean[][] matrix, Boolean[] liste, Integer i, Integer j) {
        System.out.println("spielFinden (" + i + " / " + j + ")");
        druckeMatrix(matrix);
        druckeListe(liste);

        int n = matrix.length;
        do {
            j++;
            if (j == n) {
                j = 0;
                i = (i + 1) % n;
            }
        } while (matrix[i][j] == Boolean.TRUE);
        int i_start = i;
        int j_start = j;
        do {
            do {
                j++;
                if (j == n) {
                    j = 0;
                    i = (i + 1) % n;
                }
            } while (matrix[i][j] == Boolean.TRUE);

            if(liste[i] == Boolean.FALSE && liste[j] == Boolean.FALSE && matrix[i][j] == Boolean.FALSE) {
                liste[i] = true;
                liste[j] = true;
                matrix[i][j] = Boolean.TRUE;

                // druckeMatrix(matrix);
                // druckeListe(liste);

                System.out.println("-> spielFinden " + 1);
                return new Integer[]{i, j};
            }
        } while(i != i_start || j != j_start);

        System.out.println("-> spielFinden " + (-1));
        return null;
    }

    private static void copy2DMatrix(Boolean[][] src, Boolean[][] dest) {
        int zeilen = src.length;
        if(zeilen == 0) {
            return;
        }
        int spalten = src[0].length;
        for(int i = 0; i < zeilen; i++) {
            for(int j = 0; j < spalten; j++) {
                dest[i][j] = src[i][j];
            }
        }
    }

    private static void copy1DListe(Boolean[] src, Boolean[] dest) {
        int n = src.length;
        for(int i = 0; i < n; i++) {
            dest[i] = src[i];
        }
    }

    private static List<Runde> erzeugePaarungen3(int n) {
        boolean matrix[][] = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(i >= j) {
                    matrix[i][j] = true;
                }
            }
        }

        return überprüfen(n, matrix);
    }

    private static List<Runde> erzeugePaarungen3_warmstart(int n) {
        // Warm start
        List<Runde> list;
        if(n > 4) {
            System.out.println("Warm start " + n);

            System.out.println(Math.pow(2, (int)(Math.log(n - 1) / Math.log(2))));

            int log_n = (int) Math.pow(2, (int)(Math.log(n - 1) / Math.log(2)));
            System.out.println("log_n: " + log_n);
            int neues_n = 2 * log_n;

            list = erzeugePaarungen3_warmstart(log_n);

            List<Runde> neueRunden = new LinkedList<>();
            for( Runde runde : list ) {

                for( Spiel spiel : runde.spiele ) {
                    Spiel neuesSpiel = new Spiel(spiel.getX() + log_n, spiel.getY() + log_n, neues_n);
                    runde = runde.erweitern(neuesSpiel);
                }

                neueRunden.add(runde);

            }

            druckeRunden(neueRunden);

            boolean matrix[][] = new boolean[neues_n][neues_n];
            for (int i = 0; i < neues_n; i++) {
                for (int j = 0; j < neues_n; j++) {
                    if (i >= j) {
                        matrix[i][j] = true;
                    }
                }
            }

            for( Runde runde : neueRunden ) {
                for( Spiel spiel : runde.spiele ) {
                    matrix[spiel.getX()][spiel.getY()] = true;
                }
            }

            List<Runde> result = überprüfen(neues_n, matrix);

            neueRunden.addAll(result);

            return neueRunden;
        }

        boolean matrix[][] = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i >= j) {
                    matrix[i][j] = true;
                }
            }
        }

        return überprüfen(n, matrix);
    }

    private static int counter = 0;
    private static List<Runde> überprüfen(int n, boolean[][] matrix) {

        System.out.println("überprüfen " + ++counter);

        List<Runde> runden = spiele_methode(n, matrix);

        System.out.println(runden.size());

        boolean matrix_copy[][] = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix_copy[i][j] = matrix[i][j];
            }
        }

        for( Runde runde : runden ) {

            druckeRunde(runde);

            for( Spiel spiel : runde.spiele ) {
                matrix[spiel.getX()][spiel.getY()] = true;
            }

            int nochFrei = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if(matrix[i][j] == false) {
                        nochFrei++;
                        break;
                    }
                }
            }
            if(nochFrei == 0) {
                List<Runde> list = new LinkedList<>();
                list.add(runde);
                return list;
            }

            List<Runde> result = überprüfen(n, matrix);

            System.out.println(result);

            if(result != null) {

                List<Runde> list = new LinkedList<>();
                list.add(runde);

                list.addAll(result);

                return list;

            } else {

                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        matrix[i][j] = matrix_copy[i][j];
                    }
                }

            }

        }

        return null;
    }

    private static List<Runde> spiele_methode(int n, boolean[][] matrix) {
        int anzahlSpieleProRunde = n / 2;

        List<Runde> runden = new LinkedList<>();
        runden.add(new Runde());

        List<Spiel> spiele = new LinkedList<>();

        for(int x = 0; x < n; x++) {

            for(int y = x + 1; y < n; y++) {

                Spiel spiel = new Spiel(x, y, n);

                if(istKonform(spiel, matrix)) {

                    spiele.add(spiel);

                }

            }

        }

        System.out.println("Spiele size: " + spiele.size());

        for( Spiel spiel : spiele ) {
            System.out.println(spiel.getX() + " " + spiel.getY());
        }

        for(int s = 0; s < anzahlSpieleProRunde; s++) {

            List<Runde> neueRunden = new LinkedList<>();
            for( Runde runde : runden ) {
                for( Spiel spiel : spiele ) {
                    Runde neueRunde = runde.erweitern(spiel);

                    if(istKonform(n, neueRunde)) {
                        neueRunden.add(neueRunde);
                    }
                }
            }
            runden = neueRunden;

            System.out.println("-> Runden size: " + runden.size());

        }

        /*List<Runde> neueRunden = new LinkedList<>();

        for( Runde runde : runden ) {

            if(istKonform(n, runde)) {
                neueRunden.add(runde);

                // System.out.println("konform");
            }

        }

        // System.out.println(neueRunden.size());

        return neueRunden;*/

        return runden;
    }

    private static boolean istKonform(int n, Runde runde) {
        boolean liste[] = new boolean[n];

        // System.out.println("-- " + runde.spiele.length);

        int last_x = -1;
        for( Spiel spiel : runde.spiele ) {
            if(last_x >= spiel.getX()) {
                return false;
            } else {
                last_x = spiel.getX();
            }

            if(liste[spiel.getX()] == true) {
                return false;
            } else {
                liste[spiel.getX()] = true;
            }

            if(liste[spiel.getY()] == true) {
                return false;
            } else {
                liste[spiel.getY()] = true;
            }

            /*if(matrix[spiel.getX()][spiel.getY()] == true) {
                return false;
            } else {
                matrix[spiel.getX()][spiel.getY()] = true;
            }*/
        }

        return true;
    }

    private static boolean istKonform(Spiel spiel, boolean[][] matrix) {

        if(matrix[spiel.getX()][spiel.getY()] == true) {
            return false;
        }

        return true;
    }

}

class Spiel {
    private int x, y, n;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Spiel(int x, int y, int n) {
        this.x = x;
        this.y = y;
        this.n = n;
    }

    public void inc() {
        y++;
        if(y >= n) {
            y = 0;
            x = x++ % n;
        }
    }
}

class Runde {
    Spiel spiele[];

    public Runde erweitern(Spiel spiel) {
        Spiel neue_spiele[];
        if(spiele == null) {
            neue_spiele = new Spiel[1];
            neue_spiele[0] = spiel;
        } else {
            neue_spiele = new Spiel[spiele.length + 1];
            System.arraycopy(spiele, 0, neue_spiele, 0, spiele.length);
            neue_spiele[neue_spiele.length - 1] = spiel;
        }

        Runde neueRunde = new Runde();
        neueRunde.spiele = neue_spiele;
        return neueRunde;
    }

    /*public static List<Runde> spieleHinzufügen(List<Runde> runden, List<Spiel> spiele) {
        List<Runde> neueRunden = new LinkedList<>();
        for( Runde runde : runden ) {
            // System.out.println("---- " + ((runde.spiele!=null)?runde.spiele.length:0));
            for( Spiel spiel : spiele ) {
                neueRunden.add(runde.erweitern(spiel));
            }
        }
        return neueRunden;
    }*/
}
