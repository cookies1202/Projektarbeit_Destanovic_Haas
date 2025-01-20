package model;

import java.time.LocalDate;

public class Patient {

    private int SVN;
    private String vorname;
    private String nachname;
    private LocalDate gebdatum;
    private int siteid;

    public Patient(int SVN, String vorname, String nachname, LocalDate gebdatum, int siteid) {
        this.SVN = SVN;
        this.vorname = vorname;
        this.nachname = nachname;
        this.gebdatum = gebdatum;
        this.siteid = siteid;
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

    public int getSiteid() {
        return siteid;
    }

    public void setSiteid(int siteid) {
        this.siteid = siteid;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "SVN='" + SVN + '\'' +
                ", Vorname='" + vorname + '\'' +
                ", Nachname='" + nachname + '\'' +
                ", Geburtsdatum='" + gebdatum + '\'' +
                ", SiteID='" + siteid + '\'' +
                '}';
    }
}

