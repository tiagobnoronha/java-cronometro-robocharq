/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cronometrorobocharq.cronometro.robocharq;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;


/**
 *
 * @author tiagobnoronha
 */
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import jdk.nashorn.internal.parser.TokenType;

public class TelaCronometro extends JFrame implements SerialPortEventListener, KeyListener {
    
    private JLabel lblTempo;
    private SerialPort serial;
    private byte mskIni, mskFim;

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_A:
                estado = Estado.ARMADO;
                break;
            case KeyEvent.VK_C:
                estado = Estado.CONTANDO;
                break;
                
            case KeyEvent.VK_P:
                estado = Estado.PARADO;
                break;
                        
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
    private enum Estado{
        ARMADO,
        CONTANDO,
        PARADO
    }
    
    private Estado estado;
    private long t;
    
    private class Temporizador implements Runnable{

        @Override
        public void run() {
            while(true){
                switch(estado){
                    case ARMADO:
                        t=System.currentTimeMillis();
                        lblTempo.setText("00:00,00");
                        break;
                    case CONTANDO:
                        long dt = (System.currentTimeMillis()-t)/10;
                        
                        long cent = dt%100;
                        dt /= 100;
                        
                        

                        long seg = dt % 60;
                        dt /= 60;

                        long min = dt;

                        String strTempo = String.format("%02d:%02d,%02d", min, seg, cent);

                        lblTempo.setText(strTempo);               
                }
            }
            
    
            
            
            
        }
    
    }
    
    
    public  TelaCronometro(int monitor, String porta, int baud, int s1, int s2) {
        getContentPane().setBackground(Color.BLACK);
        
        lblTempo = new JLabel("00:00,00", SwingConstants.CENTER);
        lblTempo.setForeground(Color.green);
        
        Font font;   
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("digital-7 (mono).ttf"));
            lblTempo.setFont(font.deriveFont(Font.BOLD, 230f));
        } catch (FontFormatException ex) {
            Logger.getLogger(TelaCronometro.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TelaCronometro.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        
        add(lblTempo, BorderLayout.CENTER);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setUndecorated(true);
        
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = env.getScreenDevices()[monitor];
        dev.setFullScreenWindow(this);
        
        
        try {
            serial = new SerialPort(porta);
            serial.openPort();
            serial.setParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE );
            serial.addEventListener(this);
            
        } catch (SerialPortException ex) {
            Logger.getLogger(TelaCronometro.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        estado=Estado.PARADO;
        t=0;
        
        Thread thrTemporizador = new Thread(new Temporizador());
        thrTemporizador.start();
        
        addKeyListener(this);
        
        mskIni = (byte) (1<<(s1-4));
        mskFim = (byte) (1<<(s2-4));
        
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if(serialPortEvent.isRXCHAR()){
            try {
                byte[] buf = serial.readBytes();
                byte d = buf[buf.length-1];
                boolean s1 = !((mskIni&d)>0);
                boolean s2 = !((mskFim&d)>0);
                
                System.out.println(s1);
                System.out.println(s2);
                switch(estado){
                    case ARMADO:
                        if(s1){
                            estado=Estado.CONTANDO;
                        }
                        break;
                        
                    case CONTANDO:
                        if(s2){
                            estado=Estado.PARADO;
                        }
                        break;

                }
                
                
            } catch (SerialPortException ex) {
                Logger.getLogger(TelaCronometro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
