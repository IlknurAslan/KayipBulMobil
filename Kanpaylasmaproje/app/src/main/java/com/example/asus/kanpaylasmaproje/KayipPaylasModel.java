package com.example.asus.kanpaylasmaproje;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASUS on 24.02.2018.
 */

public class KayipPaylasModel implements Serializable{

        private String kullanici_adi;
        private String aciklama;
        private String iletisim;
        private String path;

        private String id;
        private String yakinlik;
        private String kategori;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYakinlik() {
        return yakinlik;
    }

    public void setYakinlik(String yakinlik) {
        this.yakinlik = yakinlik;
    }


    public String getKullanici_adi() {
        return kullanici_adi;
    }

    public void setKullanici_adi(String kullanici_adi) {
        this.kullanici_adi = kullanici_adi;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIletisim() {
        return iletisim;
    }

    public void setIletisim(String iletisim) {
        this.iletisim = iletisim;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }


    public String getAciklama() {
            return aciklama;
        }

        public void setAciklama(String aciklama) {

            this.aciklama = aciklama;
        }




        public Map<String, Object> toMap() {

            HashMap<String, Object> result = new HashMap<>();

            result.put("kullanici_adi",kullanici_adi);
            result.put("aciklama", aciklama);
            result.put("iletisim", iletisim);
            result.put("path", path);
            result.put("resimid", id);
            result.put("yakinlik",yakinlik);
            result.put("kategori",kategori);


            return result;

        }
    }



