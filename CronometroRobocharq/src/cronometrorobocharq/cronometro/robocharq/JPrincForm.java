/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cronometrorobocharq.cronometro.robocharq;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author tiagobnoronha
 */
public class JPrincForm extends JFrame {
    
    private JLabel lblPorta, lblBaud, lblSensorIni, lblSensorFim, lblMonitor;
    private JComboBox<Integer> cmbBaud, cmbSensorIni, cmbSensorFim;
    private JComboBox<String> cmbPorta, cmbMonitor;
    private JButton btnIniciar;
    private JPanel pnlComponentes;
        
    final Integer[] bauds = new Integer[]{9600, 19200, 38400, 57600, 115200};
    final Integer[] sensorsPorts = new Integer[]{4,5,6,7};
    public JPrincForm(){
        super("Configuração Cronometro");
        
        
        getContentPane().setLayout(new BorderLayout());
        
        pnlComponentes = new JPanel(new GridLayout(0,2));
        
        lblMonitor = new JLabel("Monitor:");
        cmbMonitor = new JComboBox<>();
        int nroMonitores = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length;
        for(int i=0; i<nroMonitores; i++){
            cmbMonitor.addItem("Monitor "+i);
        }
        
        pnlComponentes.add(lblMonitor);
        pnlComponentes.add(cmbMonitor);
        
        lblPorta = new JLabel("Porta:");
        cmbPorta = new JComboBox<>(jssc.SerialPortList.getPortNames());
        pnlComponentes.add(lblPorta);
        pnlComponentes.add(cmbPorta);
        
        lblBaud = new JLabel("Baudrate:");
        cmbBaud = new JComboBox<>(bauds);
        pnlComponentes.add(lblBaud);
        pnlComponentes.add(cmbBaud);
        
        lblSensorIni = new JLabel("Sensor Inicio:");
        cmbSensorIni = new JComboBox<>(sensorsPorts);
        pnlComponentes.add(lblSensorIni);
        pnlComponentes.add(cmbSensorIni);
        
        lblSensorFim = new JLabel("Sensor Fim:");
        cmbSensorFim = new JComboBox<>(sensorsPorts);
        pnlComponentes.add(lblSensorFim);
        pnlComponentes.add(cmbSensorFim);
        
        add(pnlComponentes, BorderLayout.CENTER);
        
        btnIniciar = new JButton("Iniciar");
        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TelaCronometro tela;
                String strPorta = (String) cmbPorta.getSelectedItem();
                Integer intBaud = (Integer)cmbBaud.getSelectedItem();
                Integer intS1 = (Integer)cmbSensorIni.getSelectedItem();
                Integer intS2 = (Integer)cmbSensorFim.getSelectedItem();
                tela = new TelaCronometro(
                        cmbMonitor.getSelectedIndex(),
                        strPorta,
                        intBaud,
                        intS1,
                        intS2
                );
                tela.setVisible(true);
            }
        });
        
        add(btnIniciar, BorderLayout.SOUTH);
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        pack();
    }
    
}
