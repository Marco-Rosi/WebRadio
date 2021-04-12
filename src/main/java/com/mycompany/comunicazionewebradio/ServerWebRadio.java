/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.comunicazionewebradio;


import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 
 * @author marco
 */
public class ServerWebRadio {
    static byte[] bufferIN = new byte[1024];
        static byte[] bufferOUT = new byte[1024];
        static DatagramSocket serverSocket ;
    static Timestamp timestamp;
    static String mes;
    static boolean attivo ; 
    static ArrayList<UtenteRadio> utente = new ArrayList<UtenteRadio>();
    static ArrayList<String> pref = new ArrayList<String>();
    static ArrayList<String> prefID = new ArrayList<String>();
    
    public static void main(String[] args) throws Exception {
        
        avvio(6789);
        while(attivo){
           leggi();
        }
       chiusura();
        
        
       
        
    }
    
static private void avvio(int num){
        try {
            serverSocket = new DatagramSocket(num);
        } catch (SocketException ex) {
            Logger.getLogger(ServerWebRadio.class.getName()).log(Level.SEVERE, null, ex);
        }
         attivo = true;
        timestamp = new Timestamp(System.currentTimeMillis());
        preferenze();
        avvioMulticast();
        System.out.println("server avviato");
}

static private void avvioMulticast(){
    String preferito1[]= prefID.get(0).toString().split(",");
    String preferito2[]= prefID.get(1).toString().split(",");
    String preferito3[]= prefID.get(2).toString().split(",");
    String preferito4[]= prefID.get(3).toString().split(",");
    String preferito5[]= prefID.get(4).toString().split(",");
    String preferito6[]= prefID.get(5).toString().split(",");
    
    ServerRadioThread s1=new ServerRadioThread(preferito1[0], Integer.parseInt(preferito1[1]));
    ServerRadioThread s2=new ServerRadioThread(preferito2[0], Integer.parseInt(preferito2[1]));
    ServerRadioThread s3=new ServerRadioThread(preferito3[0], Integer.parseInt(preferito3[1]));
    ServerRadioThread s4=new ServerRadioThread(preferito4[0], Integer.parseInt(preferito4[1]));
    ServerRadioThread s5=new ServerRadioThread(preferito5[0], Integer.parseInt(preferito5[1]));
    ServerRadioThread s6=new ServerRadioThread(preferito6[0], Integer.parseInt(preferito6[1]));
}
    
static private void chiusura(){
         serverSocket.close();
    }

static private void scrivi(String messaggio, InetAddress IPServer,int portaserver) {
        try {
            
            bufferOUT=messaggio.getBytes();
        DatagramPacket packetout= new DatagramPacket(bufferOUT, bufferOUT.length, IPServer, portaserver);
              serverSocket.send(packetout);
            
        } catch (IOException ex) {        }
        
    }


//ascolta i messaggi ricevuti e li filtra per poi consegnare le risposte 
static private void leggi() {
        
        try {
            DatagramPacket receivePacket = new DatagramPacket (bufferIN, bufferIN.length);
            serverSocket.receive(receivePacket);
            String ricevuto= new String(receivePacket.getData());
            InetAddress cadaddress = receivePacket.getAddress();
             int cport = receivePacket.getPort();
            int numCaratteri =receivePacket.getLength();
            ricevuto= ricevuto.substring(0, numCaratteri);
    
    switch (ricevuto){
        case "preferenzePossibili":
            scrivi(preferenze(), cadaddress, cport);
            break;
        case "sync":           
                scrivi("sincronizzazione "+timestamp.getTime(), cadaddress,cport);
                System.out.println("invio timestamp");
                break;
        case "nuovoUtente":
            System.out.println("creazione nuovo utente");
            scrivi( "creazione nuovo utente, scrivi nome, pass,foto, e 3 preferenze:", cadaddress,cport);
            String nome = leggiScarno();
            String pass= leggiScarno();
            FotoUtente foto= leggiFoto();
            String pref1=leggiScarno();
            String pref2=leggiScarno();
            String pref3=leggiScarno();
            UtenteRadio e= new UtenteRadio(nome,pass, foto,pref1,pref2,pref3);
            utente.add(e);
            
       break;
        case "elencoUtenti":
            for(int i=0; i<utente.size();i++){
            scrivi(utente.get(i).toString(), cadaddress, cport);
            }
            scrivi("finiti", cadaddress, cport);
        break;
        case "spengi":
            System.out.println("Server spento");
            scrivi("SPENTO", cadaddress, cport);
            attivo=false;
        break;
        case "ospite":
            FotoUtente fotou= new FotoUtente();
            UtenteRadio ospite= new UtenteRadio("ospite","pass",fotou ,"pop","indi","rock");
            utente.add(ospite);
        break;
        case "accedi":
            String nom = leggiScarno();
            String pas= leggiScarno();
            scrivi("tentativo di accesso", cadaddress,cport); 
            for(int i=0; i<utente.size();i++){
                if(utente.get(i).nome==nom && utente.get(i).pass==pas){
                    scrivi("accesso riuscito, invio delle preferenze", cadaddress,cport); 
                    for(int y=0; y<pref.size();y++){
                        if(utente.get(y).pref1.equals(pref.get(y).toString())){
                            for(int q=0; q<pref.size();q++){
                                if(utente.get(q).pref2.equals(pref.get(q).toString())){
                            for(int l=0; l<pref.size();l++){if(utente.get(l).pref3.equals(pref.get(l).toString())){
                                scrivi(prefID.get(y).toString(), cadaddress, cport);
                                scrivi(prefID.get(q).toString(), cadaddress, cport);
                                scrivi(prefID.get(l).toString(), cadaddress, cport);
                            }
                            }
                        }
                        
                    }
                }
            }}}
        break;
        default:
            System.out.println("server ha letto: "+ricevuto);  
                scrivi( ricevuto, cadaddress,cport); 
    }
            
            
        } catch (IOException ex) {        }
        
    }
static private String preferenze() {

pref.add("classic");
pref.add("funk");
pref.add("indi");
pref.add("rock");
pref.add("latino");
pref.add("pop");
prefID.add("229.0.0.1,10000");
prefID.add("229.0.0.2,10001");
prefID.add("229.0.0.3,10002");
prefID.add("229.0.0.4,10003");
prefID.add("229.0.0.5,10004");
prefID.add("229.0.0.6,10005");
return "classic, funk, indi, rock, latino, pop";

}




//Legge i messaggi ricevuto al server senza rispondere o filtrare le richieste
static private String leggiScarno() {
        String ricevuto= null;
        try {
            DatagramPacket receivePacket = new DatagramPacket (bufferIN, bufferIN.length);
            serverSocket.receive(receivePacket);
            ricevuto= new String(receivePacket.getData());
            InetAddress cadaddress = receivePacket.getAddress();
            int cport = receivePacket.getPort();
            int numCaratteri =receivePacket.getLength();
            ricevuto= ricevuto.substring(0, numCaratteri);
            
        scrivi("arrivato", cadaddress, cport);
        }
           
        catch (IOException ex) {        }
        return ricevuto;
}
       
static private FotoUtente leggiFoto()  {           //classe che prende la foto dalla comunicazione seriale
        FotoUtente ma= new FotoUtente();
        try {
            DatagramPacket receivePacket = new DatagramPacket (bufferIN, bufferIN.length);
            serverSocket.receive(receivePacket);
            ByteArrayInputStream bais= new
            ByteArrayInputStream(receivePacket.getData());
            ObjectInputStream ois= new ObjectInputStream (bais);
            try {
                ma = (FotoUtente) ois.readObject();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerWebRadio.class.getName()).log(Level.SEVERE, null, ex);
            }
        InetAddress cadaddress = receivePacket.getAddress();
        int cport = receivePacket.getPort();
        scrivi("arrivata foto", cadaddress, cport);
        }
           
        catch (IOException ex) {        }
        
        return ma;
}
static private String timestampToDate(String messaggio) {
        String[] dati = messaggio.split("sincronizzazione ");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(Long.parseLong(dati[1]));
        return sf.format(date);
    }




}
