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
class RadioThread extends Thread{
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
    String adress;
    int ss;
    InetAddress group;
    boolean att=true;
public RadioThread(String adress, int ss) { //costruttore
        this.adress = adress;
        this.ss=ss;
    }


    public void run() {
        try {
            socket = new MulticastSocket(ss);
            group = InetAddress.getByName(adress);
            socket.joinGroup(group);
            while (att) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(
                        packet.getData(), 0, packet.getLength());
                System.out.println("radio: "+ received);
            }
        } catch (IOException ex) {
            Logger.getLogger(RadioThread.class.getName()).log(Level.SEVERE, null, ex);
        }try {
            socket.leaveGroup(group);
        } catch (IOException ex) {
            Logger.getLogger(RadioThread.class.getName()).log(Level.SEVERE, null, ex);
        }
            socket.close();}

    public void ferma(){
        att=false;
    }
    
}
