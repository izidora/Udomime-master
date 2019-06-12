package com.example.udomime;

import java.util.Date;

/**
 * Created by Abhi on 20 Jan 2018 020.
 */

public class User {
    String username;
    String fullName;
    //ja dodajem
    String grad;
    String zupanija;
    String email;

    Integer id;
    Integer uloga;
    //ja
    Date sessionExpiryDate;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    //ja dodajem
    public void setId(Integer id) {
        this.id = id;
    }

    public void setUloga(Integer uloga) {
        this.uloga = uloga;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public void setZupanija(String zupanija) {
        this.zupanija = zupanija;
    }
    //ja

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    //ja dodajem

    public Integer getId() {
        return id;
    }

    public Integer getUloga() {
        return uloga;
    }

    public String getEmail() {
        return email;
    }

    public String getGrad() {
        return grad;
    }

    public String getZupanija() {
        return zupanija;
    }
    //ja

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }
}