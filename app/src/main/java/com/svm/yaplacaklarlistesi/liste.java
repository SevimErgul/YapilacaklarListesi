package com.svm.yaplacaklarlistesi;

import java.io.Serializable;

public class liste implements Serializable {
    private int id;
    private int durum;
    private String yapilacak;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDurum() {
        return durum;
    }

    public void setDurum(int durum) {
        this.durum = durum;
    }

    public String getYapilacak() {
        return yapilacak;
    }

    public void setYapilacak(String yapilacak) {
        this.yapilacak = yapilacak;
    }
}
