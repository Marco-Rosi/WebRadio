/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.comunicazionewebradio;

/**
 *
 * @author marco
 */
public class UtenteRadio {
    String nome;
    String pass;
    FotoUtente foto;
    String pref1;
    String pref2;
    String pref3;

    public UtenteRadio(String nome, String pass, FotoUtente foto, String pref1, String pref2, String pref3) {
        this.nome = nome;
        this.pass = pass;
        this.foto = foto;
        this.pref1 = pref1;
        this.pref2 = pref2;
        this.pref3 = pref3;
    }

    
}
