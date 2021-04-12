/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.comunicazionewebradio;

/**
 *
 * @author asusm
 */
public class Utente {
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
      byte[] bufferIN = new byte[1024];
         byte[] bufferOUT = new byte[1024];
         DatagramSocket socket ;
        Scanner scan= new Scanner(System.in);
 boolean attivo=true;
   
       ObjectOutputStream a;
        
  
    
     private void avvio(){
        try {
            socket = new DatagramSocket();
        } catch (SocketException ex) {
           
        }
        
        System.out.println("client avviato");
}
    private void chiusura(){
         socket.close();
    }
    
    
    private void scrivi(String messaggio, InetAddress IPServer,int portaserver) {
        try {
            
            bufferOUT=messaggio.getBytes();
        DatagramPacket packetout= new DatagramPacket(bufferOUT, bufferOUT.length, IPServer, portaserver);
              socket.send(packetout);
            
        } catch (IOException ex) {        }
        
    }
   private void scriviFoto( InetAddress IPServer,int portaserver) {
        try {
            
            FotoUtente foto=new FotoUtente();
           ByteArrayOutputStream bufferByte = new ByteArrayOutputStream();
           ObjectOutputStream out= new ObjectOutputStream(bufferByte);
           
        DatagramPacket packetout= new DatagramPacket(bufferOUT, bufferOUT.length, IPServer, portaserver);
              socket.send(packetout);
            
        } catch (IOException ex) {        }
        
    }
 private void leggi() {
        
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

private String timestampToDate(String messaggio) {
        String[] dati = messaggio.split("sincronizzazione ");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(Long.parseLong(dati[1]));
        return sf.format(date);
    }
    
    }

}
