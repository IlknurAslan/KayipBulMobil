package com.example.asus.kanpaylasmaproje;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASUS on 13.9.2017.
 */

public class KisiModel implements Serializable {

    private String AdSoyad;
    private String kullanici_adi;
    private String path;
    private String uid;
    private String konum;

    public KisiModel(String kullaniciAdi, String isim, String path, String uid,String konum) {

        this.kullanici_adi = kullaniciAdi;
        this.AdSoyad = isim;
        this.path = path;
        this.uid = uid;
        this.konum=konum;
    }


    public KisiModel() {

    }

    public String getKonum() {
        return konum;
    }

    public void setKonum(String konum) {
        this.konum = konum;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAdSoyad() {
        return AdSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        AdSoyad = adSoyad;
    }

    public String getKullanici_adi() {
        return kullanici_adi;
    }

    public void setKullanici_adi(String kullanici_adi) {
        this.kullanici_adi = kullanici_adi;
    }
/*
    public String getKonum() {
        return konum;
    }

    public void setKonum(String konum) {
        this.konum = konum;
    }
*/
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("konum", konum);
        result.put("adSoyad", AdSoyad);

        result.put("kullaniciadi", kullanici_adi);

        result.put("path", path);

        result.put("uid", uid);
        //result.put("konum", konum);
        return result;

    }
}