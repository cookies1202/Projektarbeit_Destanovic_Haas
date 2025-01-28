package model;

import java.time.LocalDate;

public class Patient {

    private int SVN;
    private String vorname;
    private String nachname;
    private LocalDate gebdatum;
    private int station;

    public Patient(int SVN, String vorname, String nachname, LocalDate gebdatum, int station) {
        this.SVN = SVN;
        this.vorname = vorname;
        this.nachname = nachname;
        this.gebdatum = gebdatum;
        this.station = station;
    }

    public int getSVN() {
        return SVN;
    }

    public void setSVN(int SVN) {
        this.SVN = SVN;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public LocalDate getGebdatum() {
        return gebdatum;
    }

    public void setGebdatum(LocalDate gebdatum) {
        this.gebdatum = gebdatum;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }


}//

