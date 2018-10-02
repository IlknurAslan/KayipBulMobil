package com.example.asus.kanpaylasmaproje;

/**
 * Created by ASUS on 03.03.2018.
 */

public class MesajModel {
    private String  gonderen;
    private String mesaj;
    private String zaman;

    public MesajModel() {

    }

    public MesajModel(String gonderen,String mesaj, String zaman){

        this.gonderen=gonderen;
        this.mesaj=mesaj;
        this.zaman=zaman;

    }

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gönderen) {
        this.gonderen = gönderen;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public String getZaman() {
        return zaman;
    }

    public void setZaman(String zaman) {
        this.zaman = zaman;
    }
}
