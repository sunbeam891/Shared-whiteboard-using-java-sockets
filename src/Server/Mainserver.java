//Shubham Parth , 1155825
package Server;
import java.awt.EventQueue;
import java.awt.Color;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JTextField;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JTextArea;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*; 
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.net.*;

public class Mainserver {
	
	private JFrame frame;
	private  JPanel Mainpan;
	public  static JTextArea display;
	private  JButton close;
	private Dictionarycomm clientincom;
	private JScrollPane scrollbar;
	public static String backgroundpath;
	static Vector<ClientHandler> ar = new Vector<>();
	public static ArrayList<String> Clientsuname = new <String>ArrayList();
	public static String clientslist="clients";
	static int i = 0;
	public static ConcurrentHashMap<String,ArrayList<Point>> map=new ConcurrentHashMap<String,ArrayList<Point>>();
	
	public Mainserver(){
		
		
		initialize();
		
    }
	
	private void initialize () {
		frame = new JFrame("Server for whiteboard");
		frame.setPreferredSize(new Dimension(700, 600));
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		Mainpan = new JPanel();
		display = new JTextArea();
		scrollbar = new JScrollPane(display);
		scrollbar.setPreferredSize(new Dimension(500,300));
		scrollbar.setBounds(10,10,300,300);
		display.setBounds(10,10,300,300);
		display.setEditable(false);
		scrollbar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		close =new JButton("Close");
		close.setPreferredSize(new Dimension(100,60));
		close.setBounds(30,30,100,60);
		close.setBackground(Color.pink);
		close.setOpaque(true);
		frame.setContentPane(Mainpan);
		Mainpan.add(close,BorderLayout.CENTER);
		Mainpan.add(scrollbar,BorderLayout.NORTH);
		frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int n = JOptionPane.showConfirmDialog(null,
                        "Do you want to exit?", "Confirm",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (n == 0) {
                    System.out.println("Server is offline.");
                    System.exit(0);
                }
            }
        });
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(null,
                        "Do you really want to exit?", "Confirm",
                        JOptionPane.YES_NO_OPTION);
                if (n == 0) {
                    System.out.println("Server is offline.");
                    System.exit(0);
                }
            }
        });
	}

	

	
	
	public static void main(String[] args) {
		ServerSocket listeningSocket = null;
        Socket clientSocket = null;
        Mainserver dic = null;
        Dictionarycomm clientincom = new Dictionarycomm();
        dic = new Mainserver();
        CmdLineParser parser = new CmdLineParser(clientincom);
		
        
        try { 
        	parser.parseArgument(args);
        	listeningSocket = new ServerSocket(clientincom.getPort());
        	dic.display.setText("Waiting for a client connection. " +"at "+clientincom.getPort()+"\n");
        	
        	while (true) {
        		clientSocket = listeningSocket.accept();
        		Info(dic, clientSocket, i);
        		
        		System.out.println("Client conection number " + i );
				System.out.println("Client Port: " + clientSocket.getPort());
				System.out.println("Client Hostname: " + clientSocket.getInetAddress().getHostName());
				System.out.println("Local Port: " + clientSocket.getLocalPort());
				ObjectInputStream dis=new ObjectInputStream(clientSocket.getInputStream());
				ObjectOutputStream dos= new ObjectOutputStream(clientSocket.getOutputStream());
	            System.out.println("Creating a new clienthandler for this client\n");
	            dic.display.append("Creating a new clienthandler for this client \n");
	            if(backgroundpath!=null) {
	            	dos.writeObject("Opennew"+","+backgroundpath);
	            	dos.reset();
	            }
	            dos.writeObject(map);
	            
	            ClientHandler mtch = new ClientHandler(clientSocket,"client " + i,i, dis, dos);
	            System.out.println("Adding this client to active client list\n");
	            dic.display.append("Adding this client to active client list \n");
	            ar.add(mtch);
        		Thread worker = new Thread(mtch);
        		worker.start();
        		
        		i++;
        	}
        }
        
       	catch (IOException e) {
      		System.out.println("IO Error (Could not connect)");
       	} catch (CmdLineException e) {
       		System.out.println("Wrong parameters entered, Please check and try again\n");
       		System.exit(0);
		}

    }
	private static void Info(Mainserver dic, Socket clientSocket, int i) {
        dic.display.append("Client number " + i + "connected"
                + "." + "\n");
        dic.display.append("client Port: " + clientSocket.getPort() + "\n");
        dic.display.append("client Hostname: "
                + clientSocket.getInetAddress().getHostName() + "\n");
        dic.display.append("Local Port: " + clientSocket.getLocalPort() + "\n");
    }
}

