//Shubham Parth , 1155825
package Client;
import java.awt.*;
import javax.swing.border.Border;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
public class Mouse extends JPanel implements MouseListener, MouseMotionListener {
    private static int x1=10000 ;
    private static int y1=10000 ;
    private static int cx=1000,cy=1000;
    static Socket bot=null;
    private static Mouse mostrecent;
    private static JFrame fr;
    private static JButton Rectanglebut;
    private static JButton Circlebut;
    private static JButton Linebut;
    private static JButton Kick;
    private static JButton Text;
    private static JTextArea Clientlist;
    private static JMenuBar br;
    private static JMenu file;
    private static JMenuItem New,Save,Saveas,Open,Close;
    private static String name;
    private static String textcontent="";
    private static String inputtext;
    private static boolean Rect=false;
    private static boolean Circle=false;
    private static boolean Line=false;
    private static boolean Admin=false;
    private static boolean Textentry=false;
    private static BufferedImage backgroundimage;
    private static int startstate=0;
    private static ObjectOutputStream mainout;
    private static ObjectInputStream mainis;
    private static ConcurrentHashMap<String,ArrayList<Point>> map=new ConcurrentHashMap<String,ArrayList<Point>>();
    private static ArrayList<Point> rectpoints;  
    private static ArrayList<Point> circpoints;  
    private static ArrayList<Point> linepoints;
    private static ArrayList<Point> textpoints;
    private static ArrayList<String> Clientsuname;
    private static ConcurrentHashMap<String,ArrayList<Point>> incommap=new ConcurrentHashMap<String,ArrayList<Point>>();
    
    
    public Mouse(String name,Socket socket) {
        mostrecent=this;
        mostrecent.addMouseListener(mostrecent);
        mostrecent.addMouseMotionListener(mostrecent);
        mostrecent.setPreferredSize( new Dimension( 500, 400 ) );
        Border blackline = BorderFactory.createLineBorder(Color.black);
        mostrecent.setBorder(blackline);
        fr = new JFrame(Mouse.name);
        
        Rectanglebut=new JButton("Rectangle");
        Circlebut=new JButton("Circle");
        Linebut=new JButton("line");
        Kick = new JButton("kick");
        Text = new JButton("Text Box");
        
        Clientlist= new JTextArea("Client list here");
        Clientlist.setEditable(false);
        Clientlist.setPreferredSize(new Dimension(130,100));
        
        JScrollPane scrollclient= new JScrollPane (Clientlist,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollclient.setPreferredSize(new Dimension(130,100));
        JPanel test=new JPanel();
        JLabel Clientlabel=new JLabel("Clients connected");
        JPanel Clientsection=new JPanel();
        
        br=new JMenuBar();
        file=new JMenu("File");
        New=new JMenuItem("New");
        Save=new JMenuItem("Save");
        Saveas=new JMenuItem("Save as");
        Open=new JMenuItem("Open");
        Close=new JMenuItem("Close");
        file.add(New);
        file.add(Open);
        file.add(Save);
        file.add(Saveas);
        file.add(Close);
        br.add(file);
       
        Clientsection.setLayout(new GridBagLayout());
        fr.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        Clientsection.setBorder(blackline);
        test.setBorder(blackline);
        gbc.insets=new Insets(0,0,4,0);
        gbc.gridx=0;
        gbc.gridy=0;
        Clientsection.add(Clientlabel,gbc);
        gbc.gridx=0;
        gbc.gridy=4;
        Clientsection.add(scrollclient,gbc);
        gbc.insets=new Insets(4,0,0,0);
        gbc.gridx=0;
        gbc.gridy=6;
        Clientsection.add(Kick,gbc);
        Clientsection.setPreferredSize(new Dimension(170,170));
        test.add(Rectanglebut);
        test.add(Circlebut);
        test.add(Linebut);
        test.add(Text);
        mostrecent.setBackground(Color.green);
        gbc.insets=new Insets(0,0,0,5);
        gbc.gridx=1;
        gbc.gridy=1;
        fr.getContentPane().add(test,gbc);
        gbc.gridx=1;
        gbc.gridy=0;
        fr.getContentPane().add(Clientsection,gbc);
        gbc.gridheight=2;
        gbc.gridx=0;
        gbc.gridy=0;
        test.setPreferredSize(new Dimension(100,130));
        fr.getContentPane().add(mostrecent,gbc);
        fr.add(br);
        fr.setJMenuBar(br);
        
        fr.setResizable(false);
        fr.setPreferredSize(new Dimension(800,500));
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.pack();
       
        rectpoints=new ArrayList<Point>();
        circpoints=new ArrayList<Point>();
        linepoints=new ArrayList<Point>();
        textpoints=new ArrayList<Point>();
        
        Kick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				String kicktext= JOptionPane.showInputDialog("Please enter the name of user you want to kick!");
				System.out.println("here1"+kicktext);
				if(!kicktext.equals(name)) {
					System.out.println(name);
					System.out.println("here"+kicktext);
				if(Admin) {
					int found=0;
				for(int i=0; i<Clientsuname.size();i++) {
				if(kicktext.equals(Clientsuname.get(i))) {
					String kickmsg="Kick"+","+kicktext;
					outputstr(kickmsg);
					JOptionPane.showMessageDialog(null,kicktext+" Kicked!","Success",JOptionPane.WARNING_MESSAGE);
					found=found+1;
					
				}
				}
				if(found==0) {
					JOptionPane.showMessageDialog(null,"Username not found in client list","Error",JOptionPane.WARNING_MESSAGE);
				}
			}
				else {
					JOptionPane.showMessageDialog(null,"You are not the manager!","Error",JOptionPane.WARNING_MESSAGE);
					
				}}
				else {
					JOptionPane.showMessageDialog(null,"You Can't Kick yourselves!","Error",JOptionPane.WARNING_MESSAGE);
				}
		}});
        
        Rectanglebut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				Circle=false;
				Textentry=false;
				Line=false;
				if(Rect) {
				Rect=false;
				}
				else {
					Rect=true;
				}
			}
		});
        Circlebut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				Rect=false;
				Line=false;
				Textentry=false;
				if(Circle) {
				Circle=false;
				}
				else {
					Circle=true;
				}
			}
		});
        Linebut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				Rect=false;
				Textentry=false;
				Circle=false;
				if(Line) {
				Line=false;
				}
				else {
					Line=true;
				}
			}
		});
        
        Save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
			if(Admin) {
				saveimage();
				JOptionPane.showMessageDialog(null,"Image Saved","Saved",JOptionPane.WARNING_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(null,"Not Manager!","Error",JOptionPane.WARNING_MESSAGE);
			}
			}});
        Saveas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
			if(Admin) {
				saveasimage();
				JOptionPane.showMessageDialog(null,"Image Saved","Saved",JOptionPane.WARNING_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(null,"Not Manager!","Error",JOptionPane.WARNING_MESSAGE);
			}
			}});
        Close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
			if(Admin) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Do you want to close the whiteboard?","Close",dialogButton);
				 if(dialogResult == JOptionPane.YES_OPTION){
					int dialogButton1 = JOptionPane.YES_NO_OPTION;
					int dialogResult1 = JOptionPane.showConfirmDialog (null, "Do you want to save the canvas? You will lose unsaved data","Warning",dialogButton1);
				    if(dialogResult1 == JOptionPane.YES_OPTION){
							 saveimage();
							 System.exit(0);
				    }
				    else {
				    	System.exit(0);
				    }
			}
			
			}else {
				JOptionPane.showMessageDialog(null,"Not Manager!","Error",JOptionPane.WARNING_MESSAGE);
			}
			}});
        New.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
			if(Admin) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Do you want to save the canvas?","Warning",dialogButton);
				 if(dialogResult == JOptionPane.YES_OPTION){
				saveimage();
				outputstr("Newfile,");
				JOptionPane.showMessageDialog(null,"Image Saved!","Saved",JOptionPane.WARNING_MESSAGE);
				  }
				 else {
					 outputstr("Newfile,");
				 }
				rectpoints.clear();
				circpoints.clear();
				linepoints.clear();
				textpoints.clear();
				textcontent="";
				backgroundimage=null;
				
				mostrecent.repaint();
				JOptionPane.showMessageDialog(null,"New Drawing area created","Saved",JOptionPane.WARNING_MESSAGE);
				
			}
			else {
				JOptionPane.showMessageDialog(null,"Not Manager!","Error",JOptionPane.WARNING_MESSAGE);
			}
			}});
        Open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
			if(Admin) {
				FileDialog fdialog = new FileDialog(fr,"Select file to be opened");
				fdialog.setFile("*.png");
		    	fdialog.setVisible(true);
		    	String path=fdialog.getDirectory() + fdialog.getFile();
		    	String openmsg="Open"+","+path;
		    	openimage(path);
		    	outputstr(openmsg);
		    	
			}
			else {
				JOptionPane.showMessageDialog(null,"Not Manager!","Error",JOptionPane.WARNING_MESSAGE);
			}
			}});
        Text.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				Rect=false;
				Circle=false;
				Line=false;
				inputtext= JOptionPane.showInputDialog("Please input text to be displayed and click where you want to display it");
				Textentry=true;
			}});
        fr.setVisible(true);

    }
    
    
    
    @Override
    public void paint(Graphics g) {
    	super.paint(g);
    	Graphics2D g2 = (Graphics2D) g;
    	g2.setStroke(new BasicStroke(3));
    	if(startstate!=0) {
    		if(backgroundimage!=null) {
    			g2.drawImage(backgroundimage, 0,0,mostrecent);
    		}
    		if(!rectpoints.isEmpty()) {
    		for (int i = 0; i < rectpoints.size() ; i=i+2)
            {
                Point p1 = rectpoints.get(i);
                Point p2 = rectpoints.get(i + 1);

                drawPerfectRect(g2, p1.x, p1.y, p2.x, p2.y);
            }}
    		if( !circpoints.isEmpty()) {
    		for (int i = 0; i < circpoints.size() ; i=i+2)
            {
                Point p1 = circpoints.get(i);
                Point p2 = circpoints.get(i + 1);

                drawPerfectCircle(g2, p1.x, p1.y, p2.x, p2.y);
            }
    		}
    		if(!linepoints.isEmpty()) {
    			for (int i = 0; i < linepoints.size() ; i=i+2)
                {
                    Point p1 = linepoints.get(i);
                    Point p2 = linepoints.get(i + 1);

                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
    		}
    		if(!textpoints.isEmpty()) {
    			String[] textcont = textcontent.split(",");
    			for (int i=0;i<textcont.length;i++) {
    				g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
    				g.drawString(textcont[i],(int)textpoints.get(i).getX(),(int)textpoints.get(i).getY());
    			}
    		}
    		
        
    	}
    }
    
    
    
    public void mouseDragged(MouseEvent e) {
    	if(Rect) {
    	}
    	else if(Circle) {
    		
    	}
    	else if(Line) {
    		
    	}
    	else {
    	x1 = e.getX();
        y1 = e.getY();
        cx = x1;
        cy = y1;
        
       
        mostrecent.repaint();
    	}
    }


    
    
    public void mousePressed(MouseEvent e) 
    {
     if(Rect) {
    	     rectpoints.add(setStartPoint(e.getPoint()));
    		
    	}
     else if (Circle) {
    	 circpoints.add(setStartPoint(e.getPoint()));
     }
     else if(Line) {
    	 linepoints.add(e.getPoint());
     }
     
    	else {
        cx = e.getX();
        cy = e.getY();
    	}
    }
    
    

    public void mouseExited(MouseEvent e) {
    }
    
    
    
    public void mouseClicked(MouseEvent e) {
    	
    }
    
    

    public void mouseEntered(MouseEvent e) {
    }
    
    

    public void mouseReleased(MouseEvent e) {
    	startstate++;
    	if (Rect) {
    	rectpoints.add(setEndPoint(e.getPoint()));
    	if(map.size()!=0) {
    	for (String i: map.keySet()) {
    		if (i.equals("rectangle")) {
    			map.remove("rectangle");
    			map.put("rectangle",rectpoints);
    			outputobj(map);
    			
    		}
    		else {
    			map.put("rectangle",rectpoints);
    			outputobj(map);
    		}
    	}
    	}else {
    		map.put("rectangle",rectpoints);
			outputobj(map);
    	}
    		
    	
    	mostrecent.repaint();
    	}
    	else if (Circle) {
        	circpoints.add(setEndPoint(e.getPoint()));
        	if(map.size()!=0) {
        	for (String i: map.keySet()) {
        		if (i.equals("circle")) {
        			map.remove("circle");
        			map.put("circle",circpoints);
        			outputobj(map);
        			
        		}
        		else {
        			map.put("circle",circpoints);
        			outputobj(map);
        		}
        	}
        	}else {
        		map.put("circle",circpoints);
    			outputobj(map);
        	}
        		
        	
        	mostrecent.repaint();
        	}
    	else if (Line) {
        	linepoints.add(e.getPoint());
        	if(map.size()!=0) {
        	for (String i: map.keySet()) {
        		if (i.equals("line")) {
        			map.remove("line");
        			map.put("line",linepoints);
        			outputobj(map);
        			
        		}
        		else {
        			map.put("line",linepoints);
        			outputobj(map);
        		}
        	}
        	}else {
        		map.put("line",linepoints);
    			outputobj(map);
        	}
        		
        	
        	mostrecent.repaint();
        	}
    	else if(Textentry) {
       	 Textentry=false;
       	 textpoints.add(e.getPoint());
       	 String presenttext=textcontent;
       	 textcontent=presenttext+inputtext+",";
       	 if(map.size()!=0) {
       	    	for (String i: map.keySet()) {
       	    		if (!i.equals("rectangle") && !i.equals("circle") && !i.equals("line")) {
       	    			map.remove(i);
       	    			map.put(textcontent,textpoints);
       	    			outputobj(map);
       	    		}
       	    		else {
       	    			map.put(textcontent,textpoints);
       	    			outputobj(map);
       	    		}
       	    	}
       	 }
       	 else {
    			map.put(textcontent,textpoints);
    			outputobj(map);}
       	 mostrecent.repaint();
        }
    	

    }
    
    

    public void mouseMoved(MouseEvent e) {
    }
    
    
    
    public static void doStuff(String clientMsg) {
		String[] input = clientMsg.split(",");
		if(!input[0].equals(name)) {
		cx=Integer. parseInt(input[1]);
		cy=Integer. parseInt(input[2]);
		x1=Integer. parseInt(input[3]);
		y1=Integer. parseInt(input[4]);
		mostrecent.repaint();
		}
	}
    
    
    public static void incomingrect(String clientMsg) {
    	String[] input = clientMsg.split(",");
    	if(!input[0].equals(name)) {
    		cx=Integer. parseInt(input[1]);
    		cy=Integer. parseInt(input[2]);
    		x1=Integer. parseInt(input[3]);
    		y1=Integer. parseInt(input[4]);
    		mostrecent.repaint();
    	}
    }
    
    
    
    public void saveimage() {
    	BufferedImage image = new BufferedImage(500,400,BufferedImage.TYPE_INT_BGR);
    	Graphics2D g=image.createGraphics();
    	File output = new File("output.png");
        this.paint(g);
    	try {
			ImageIO.write(image,"png",output);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"IO exception!","Error",JOptionPane.WARNING_MESSAGE);
		}
    	
    	
    }
    
    
    public void saveasimage() {
    	
    	FileDialog fdialog = new FileDialog(fr,"Save As",FileDialog.SAVE);
    	fdialog.setVisible(true);
    	String path=fdialog.getDirectory() + fdialog.getFile();
    	BufferedImage image = new BufferedImage(500,400,BufferedImage.TYPE_INT_BGR);
    	Graphics2D g=image.createGraphics();
    	File output = new File(path);
    	//drawing.repaint();
        this.paint(g);
    	try {
			ImageIO.write(image,"png",output);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"IO exception!","Error",JOptionPane.WARNING_MESSAGE);
		}
    	
    	
    }
    public static void openimage(String path) {
    	try {
			backgroundimage=ImageIO.read(new File(path));
		} catch (IOException e) {
			  JOptionPane.showMessageDialog(null,"Invalid file or File not found!!!","Error",JOptionPane.WARNING_MESSAGE);
		}
    	mostrecent.repaint();
    	
    }
    
    public Point setStartPoint(Point a) {
        x1 = a.x;
        y1 = a.y;
        Point b = new Point();
        b.x=a.x;
        b.y=a.y;
        return b;
    }
    

    public Point setEndPoint(Point a) {
        cx = (a.x);
        cy = (a.y);
        Point b = new Point();
        b.x=(a.x);
        b.y=(a.y);
        return b;
    }
    
    
    
    public void drawPerfectRect(Graphics g2, int x, int y, int x2, int y2) {
        int px = Math.min(x,x2);
        int py = Math.min(y,y2);
        int pw=Math.abs(x-x2);
        int ph=Math.abs(y-y2);
        g2.drawRect(px, py, pw, ph);
        

    }
    public void drawPerfectCircle(Graphics g2, int x, int y, int x2, int y2) {
        int px = Math.min(x,x2);
        int py = Math.min(y,y2);
        int pw=Math.abs(x-x2);
        int ph=Math.abs(y-y2);
        g2.drawOval(px, py, pw, ph);
        

    }
    
    

    public static void main(String[] args) throws IOException {
    	Socket socket=null;
    	Mouse mouse=null;
    	Clientcomm paras= new Clientcomm();
    	CmdLineParser parse = new CmdLineParser (paras);
    	try {
    		parse.parseArgument(args);
    		name=paras.Uname();
    		
    		socket = new Socket(paras.Hostin(), paras.Portin());
    		bot=socket;
    		mouse = new Mouse(name,bot); 
    		ObjectOutputStream out= new ObjectOutputStream(bot.getOutputStream());
    		mainout=out;
    		outputstr("uname"+","+name);
    		ObjectInputStream is=new ObjectInputStream(bot.getInputStream());
    		mainis=is;
    		
    		
    	while (true)
    	{
			String clientMsg = null;
			try 
			{
				
				for(;;) 
				{  
					final Object obj = mainis.readObject();
					if(obj instanceof String) {
						  final String a = (String)obj;
						  String[] input = a.split(",");
						  if(input[0].equals("admin"))
						  {
							  if(input[1].equals("yes")) {
								  Admin=true;
							  }
							  else {
								  Admin=false;
							  }
						  }
						  if(input[0].equals("Newjoin")) {
							  int dialogButton = JOptionPane.YES_NO_OPTION;
							  int dialogResult = JOptionPane.showConfirmDialog (null, "New user:"+input[1]+" wants to share your whiteboard","New user",dialogButton);
							  if(dialogResult == JOptionPane.YES_OPTION){
							  outputstr("Newjoin"+","+input[1]+","+"yes");
						  }
							  else {
								  
								  outputstr("Newjoin"+","+input[1]+","+"no");
							  }
						  
						} 
						  if(input[0].equals("Exit")) {
							  if(input[1].equals("reject")) {
							  JOptionPane.showMessageDialog(null,"Manager rejected entry!","Alert",JOptionPane.WARNING_MESSAGE);
							  System.exit(0);
							  }
							  if(input[1].equals("noadmin")) {
								  JOptionPane.showMessageDialog(null,"No Manager present!","Alert",JOptionPane.WARNING_MESSAGE);
								  System.exit(0);
								  }
							  if(input[1].equals("duplicate")) {
								  JOptionPane.showMessageDialog(null,"Client with same username is present, please change and try again!","Alert",JOptionPane.WARNING_MESSAGE);
								  System.exit(0);
								  }
						  }
						  
						  if(input[0].equals("kick")) {
							  JOptionPane.showMessageDialog(null,"You were Kicked by the Manager","Alert",JOptionPane.WARNING_MESSAGE);
							  System.exit(0);
						  }
						  
						  if(input[0].equals("adminleft")) {
							  JOptionPane.showMessageDialog(null,"Manager has left, the Whiteboard will be closed","Alert",JOptionPane.WARNING_MESSAGE);
							  System.exit(0);
						  }
						  if(input[0].equals("Newfile")) {
								  JOptionPane.showMessageDialog(null,"Manager has created a New canvas","Alert",JOptionPane.WARNING_MESSAGE);
								  rectpoints.clear();
									circpoints.clear();
									linepoints.clear();
									textpoints.clear();
									textcontent="";
									backgroundimage=null;
									mostrecent.repaint();
							  
							  
						  }
						  if(input[0].equals("Open")) {
							  JOptionPane.showMessageDialog(null,"Manager has opened a file","Alert",JOptionPane.WARNING_MESSAGE);
							  openimage(input[1]);
						  }
						  if(input[0].equals("Opennew")) {
							  openimage(input[1]);
						  }
						  if(input[0].equals("wait")) {
							  if(input[1].equals("yes")) {
								  JOptionPane.showMessageDialog(null,"Wait for manager to approve","Alert",JOptionPane.WARNING_MESSAGE);
							  }
							  else {
								  JOptionPane.showMessageDialog(null,"Manager approved entry","Alert",JOptionPane.WARNING_MESSAGE);
							  }
						  }
					}
					else if(obj instanceof ArrayList)
					{
						Clientlist.setText("");
						final ArrayList<String> c= (ArrayList<String>)obj;
						Clientsuname=c;
						
						for (int i=0; i< Clientsuname.size();i++)
						{
							Clientlist.append(Clientsuname.get(i)+"\n");
						}
						
					}
					else {
						  final ConcurrentHashMap b = (ConcurrentHashMap)obj;
						  incommap=b;
						  map=b;
						 
						  for (String i: incommap.keySet()) {
				        		if (i.equals("rectangle"))
				        		{
						            rectpoints=map.get("rectangle");
				        		}
				        		if(i.equals("circle")){
				        			circpoints=map.get("circle");
				        		}
				        		if(i.equals("line")) {
				        			linepoints=map.get("line");
				        		}
				        		if (!i.equals("rectangle") && !i.equals("circle") && !i.equals("line")) {
				        			textcontent=i;
				        			textpoints=map.get(i);
				        		}
						  
						  startstate++;
						  }
					mostrecent.repaint();
				}
					 	
			}
			}
			
			catch(SocketException e)
			{
				JOptionPane.showMessageDialog(null,"Server closed! Socket closed!","Error",JOptionPane.WARNING_MESSAGE);
				System.out.println("Socket closed");
				System.exit(0);
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null,"ClassNotFoundException!","Error",JOptionPane.WARNING_MESSAGE);
			}
    	}
    	}
    	catch (UnknownHostException e) {
			System.out.println("Host could not be resolved");
			JOptionPane.showMessageDialog(null,"Host could not be resolved","Error" , JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}
		catch (SocketException e) {
			System.out.println("Socket error");
			JOptionPane.showMessageDialog(null,"Socket error","Error!!",JOptionPane.PLAIN_MESSAGE);
			
		}
		catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"IOException","Error!!",JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		} catch (CmdLineException e) {
			System.out.println("Wrong Parameters entered, please have a look at the correct format and try again. ");
			System.exit(0);
		}
    	

    }
    private boolean outputobj (ConcurrentHashMap<String,ArrayList<Point>> mess) {
		try {
			mainout.writeObject(mess);
			mainout.reset();
			return true;
		}
		
	
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Connection lost","Error",JOptionPane.PLAIN_MESSAGE);
			return false;
		}
	}
    private static boolean outputstr (String mess) {
		try {
			mainout.writeObject(mess);
			mainout.reset();
			return true;
		}
		
	
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Connection lost","Error",JOptionPane.PLAIN_MESSAGE);
			return false;
		}
	}
}