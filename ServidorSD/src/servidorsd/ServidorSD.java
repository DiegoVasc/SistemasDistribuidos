/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsd;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Tacio Moreira
 */
public class ServidorSD extends JFrame{
    
    float valor1, valor2, valor3;
    
    JLabel Sensor1 = new JLabel("Sensor 1");
    JLabel Sensor2 = new JLabel("Sensor 2");
    JLabel Sensor3 = new JLabel("Sensor 3");
    JLabel mostrarmedia = new JLabel("Média dos valores: ");
    JLabel dados1 = new JLabel();
    JLabel dados2 = new JLabel();
    JLabel dados3 = new JLabel();
    
    JTextField tfmedia = new JTextField();
    JTextField tfsensor1 = new JTextField();
    JTextField tfsensor2 = new JTextField();
    JTextField tfsensor3 = new JTextField();
    
    JButton Iniciar = new JButton("Receber Dados");
    JButton media = new JButton("Média");
    
    
    public ServidorSD(){
        
        
        
         JPanel tela = new JPanel();
         tela.setLayout(null);
         tela.setBackground(Color.white);
         
         
        tela.add(Iniciar);
        Iniciar.setBounds(150, 10, 150, 40);
        Iniciar.addActionListener(
            new ActionListener(){
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    Thread tsensor1 = new Thread(new Runnable() {
                           @Override
                           public void run() {
                               
                            try {
                        
                                ServerSocket servidor1 = new ServerSocket(9002);
                                System.out.println("Esperando cliente se conectar ao servidor pela porta 9002");
                                    while(true) {
                                        
                                      Socket cliente1 = servidor1.accept();
                                      System.out.println("Cliente " + cliente1.getInetAddress().getHostAddress() + " conectado...");
                                      ObjectOutputStream saida = new ObjectOutputStream(cliente1.getOutputStream());


                                        Scanner entrada = new Scanner(cliente1.getInputStream());  
                                        String a = entrada.nextLine();
                                        System.out.println("Celular 01: "+ a + " Hz");
                                        tfsensor1.setText(a + " Hz");
                                        valor1 = Float.parseFloat(a);

                                        saida.flush();
                                        saida.writeObject(new Date());
                                        saida.close(); 
                                        cliente1.close();
                                }  
                    }catch(Exception a) {
                         System.out.println("Ocorreu um problema com cliente 1" + a.getMessage());
                    }
                               
                       }});
                tsensor1.start();
                
                Thread tsensor2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ServerSocket servidor2 = new ServerSocket(9003);
                                System.out.println("Esperando cliente se conectar ao servidor pela porta 9003");
                                    while(true) {

                                      Socket cliente2 = servidor2.accept();
                                      System.out.println("Cliente " + cliente2.getInetAddress().getHostAddress() + "conectado...");
                                      ObjectOutputStream saida = new ObjectOutputStream(cliente2.getOutputStream());


                                      Scanner entrada = new Scanner(cliente2.getInputStream());  
                                      String a = entrada.nextLine();
                                      System.out.println("Celular 02: "+ a + " Hz");
                                      tfsensor2.setText(a + " Hz");
                                      valor2 = Float.parseFloat(a);

                                        saida.flush();
                                        saida.writeObject(new Date());
                                        saida.close(); 
                                        cliente2.close();
                                }  
                            }catch(Exception a){
                                System.out.println("Ocorreu um problema com cliente 2" + a.getMessage());
                            }
                        }
                    });
                    tsensor2.start();
                    
                Thread tsensor3 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                
                                ServerSocket servidor3 = new ServerSocket(9004);
                                System.out.println("Esperando cliente se conectar ao servidor pela porta 9004");
                                    while(true) {

                                      Socket cliente3 = servidor3.accept();
                                      System.out.println("Cliente " + cliente3.getInetAddress().getHostAddress() + "conectado...");
                                      ObjectOutputStream saida = new ObjectOutputStream(cliente3.getOutputStream());


                                      Scanner entrada = new Scanner(cliente3.getInputStream());  
                                      String a = entrada.nextLine();
                                      System.out.println("Celular 03: "+ a + " Hz");
                                      tfsensor3.setText(a + " Hz");
                                      valor3 = Float.parseFloat(a);

                                        saida.flush();
                                        saida.writeObject(new Date());
                                        saida.close(); 
                                        cliente3.close();
                                } 
                                
                            }catch(Exception a){
                                 System.out.println("Ocorreu um problema com cliente 3" + a.getMessage());

                            }}
                    });
                    tsensor3.start();
                }
            }
        );
        
        tela.add(media);
        media.setBounds(150, 250, 150, 30);
        media.addActionListener(
            new ActionListener(){
               
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        float media = (valor1 + valor2 + valor3)/3;
                        System.out.println("Média é: " + media + " Hz");
                        String a = media + "";
                        tfmedia.setText(a + " Hz");
                    }catch(Exception a){
                        
                        System.out.println("Ocorreu um problema ao calcular a média" + a.getMessage());

                    }
                    
                }}

        );
        
        tela.add(Sensor1);
        Sensor1.setBounds(50, 60, 350, 40);
        
        tela.add(tfsensor1);
        tfsensor1.setBounds(150, 64, 150, 30);
        
        tela.add(Sensor2);
        Sensor2.setBounds(50, 120, 350, 40);
        
        tela.add(tfsensor2);
        tfsensor2.setBounds(150, 124, 150, 30);
        
        tela.add(Sensor3);
        Sensor3.setBounds(50, 180, 350, 40);
        
        tela.add(tfsensor3);
        tfsensor3.setBounds(150, 184, 150, 30);
        
        tela.add(mostrarmedia);
        mostrarmedia.setBounds(20, 300, 350, 40);
        
        tela.add(tfmedia);
        tfmedia.setBounds(150, 304, 150, 30);
        
        add(tela);
        setVisible(true);
        setSize(440, 440);
        setLocation(440, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
   
    public static void main(String[] args) {
        // TODO code application logic here
        
        new ServidorSD();
    }
    
}
