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

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class ClientUtente {
     static byte[] bufferIN = new byte[1024];
        static byte[] bufferOUT = new byte[1024];
        static DatagramSocket socket ;
        static Scanner scan= new Scanner(System.in);
        static boolean attivo=true;
        static ObjectOutputStream a;
        static String pref1= null;
        static String pref2= null;
        static String pref3= null;
        static RadioThread r1;
        static RadioThread r2;
        static RadioThread r3;
        
        
        
    public static void main(String[] args) throws Exception {
        
                  
       
        avvio();
        InetAddress IPServer= InetAddress.getByName("localhost");
        int portaserver= 6789;
        System.out.println("server connesso");
        
        while(attivo==true){
        String messaggio=scan.nextLine();
        switch(messaggio){
            case "basta":
                attivo=false;
                break;
            case "foto":
                scriviFoto(IPServer,portaserver);
                leggi();
                break;
            case "accedi":
                scrivi(messaggio,IPServer,portaserver);
                
                messaggio=scan.nextLine();
                scrivi(messaggio,IPServer,portaserver);
                leggi();
                messaggio=scan.nextLine();
                scrivi(messaggio,IPServer,portaserver);
                leggi();
                leggi();
                leggi();
                leggiPreferenze();
            break;
            case "connettitiRadio":
                connessioneRadio();
                break;
            case "fermaRadio":
                connessioneRadio();
                break;
                
            case "ospite":
                scrivi(messaggio,IPServer,portaserver);
            break;
            case "elencoUtenti":
                boolean watt=true;
            while(watt){
            String elenco=leggiScarno();
            if(elenco.equals("finiti")){
                watt=false;
            }
            }
            
        break;
            default:
                scrivi(messaggio,IPServer,portaserver);
                
        }
        
        
        
        
        }
        
        
        
        
        chiusura();
   }
    
    static private void avvio(){
        try {
            socket = new DatagramSocket();
        } catch (SocketException ex) {
           
        }
        
        System.out.println("client avviato");
}
    static private void chiusura(){
         socket.close();
    }
    
    
    static private void scrivi(String messaggio, InetAddress IPServer,int portaserver) {
        try {
            
            bufferOUT=messaggio.getBytes();
        DatagramPacket packetout= new DatagramPacket(bufferOUT, bufferOUT.length, IPServer, portaserver);
              socket.send(packetout);
            
        } catch (IOException ex) {        }
        
    }
    static private void scriviFoto( InetAddress IPServer,int portaserver) {
        try {
            
            FotoUtente foto=new FotoUtente();
            ByteArrayOutputStream bout=new ByteArrayOutputStream( );
            ObjectOutputStream oo= new ObjectOutputStream(bout);
            oo.writeObject(foto);
            byte [ ] data=bout.toByteArray();
            DatagramPacket packetout= new DatagramPacket(data, data.length, IPServer, portaserver);
            socket.send(packetout);
            
        } catch (IOException ex) {        }
        
    }
static private void leggi() {
        
        try {
            DatagramPacket receivePacket = new DatagramPacket (bufferIN, bufferIN.length);
    socket.receive(receivePacket);
            String ricevuto= new String(receivePacket.getData());
            int numCaratteri =receivePacket.getLength();
    ricevuto= ricevuto.substring(0, numCaratteri);
            try{
                System.out.println("timestamp to date: "+timestampToDate(ricevuto));
            }catch(Exception e){}
            
            System.out.println("server risponde: "+ricevuto);
        } catch (IOException ex) {        }

    }

static private void leggiPreferenze(){
        pref1= leggiScarno();
        pref2= leggiScarno();
        pref3= leggiScarno();
    }

static private String leggiScarno() {
        String ricevuto= null;
        try {
            DatagramPacket receivePacket = new DatagramPacket (bufferIN, bufferIN.length);
            socket.receive(receivePacket);
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
static private String timestampToDate(String messaggio) {
        String[] dati = messaggio.split("sincronizzazione ");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(Long.parseLong(dati[1]));
        return sf.format(date);
    }
    
static private void connessioneRadio(){
    String preferito1[]= pref1.split(",");
    String preferito2[]= pref2.split(",");
    String preferito3[]= pref3.split(",");
    r1= new RadioThread(preferito1[0], Integer.parseInt(preferito1[1]));
    r2= new RadioThread(preferito1[0], Integer.parseInt(preferito1[1]));
    r2= new RadioThread(preferito1[0], Integer.parseInt(preferito1[1]));
    r1.start();
    r1.start();
    r1.start();
}
static private void fermaRadio(){
    r1.ferma();
    r2.ferma();
    r2.ferma();
    
}




    }
