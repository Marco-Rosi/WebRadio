/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.comunicazionewebradio;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author asusm
 */
public class ServerRadioThread {
    protected DatagramSocket socket = null;
    protected byte[] buf = new byte[256];
    String adress;
    int ss;
    InetAddress group;
    boolean att=true;
public ServerRadioThread(String adress, int ss) { //costruttore
        this.adress = adress;
        this.ss=ss;
    }
     public void run(String multicastMessage) {
     while (att) {
        try {
            socket = new DatagramSocket();
            group = InetAddress.getByName(adress);
            buf = multicastMessage.getBytes();
            
            DatagramPacket packet= new DatagramPacket(buf, buf.length, group, ss);
            socket.send(packet);
            
        } catch (SocketException ex) {
            Logger.getLogger(ServerRadioThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerRadioThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
             Logger.getLogger(ServerRadioThread.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
     socket.close();
     
     }
    public void ferma(){
        att=false;
    }
    
    
    
}
