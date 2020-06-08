//Shubham Parth , 1155825
package Server;
import java.awt.Point;
import javax.swing.JOptionPane;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*; 
import java.util.*; 
import java.net.*; 
class ClientHandler implements Runnable  
{ 
    Scanner scn = new Scanner(System.in); 
    private String name; 
    private String uname;
    boolean Admin=false;
    boolean exit=false;
    boolean noadmin=false;
    boolean duplicate=false;
    boolean adminavail=false;
    boolean couldnotenter=false; 
    final ObjectOutputStream dos;
    final ObjectInputStream dis;
    Socket s; 
    boolean isloggedin; 
    private int i;
    private ConcurrentHashMap<String,ArrayList<Point>> map=new ConcurrentHashMap<String,ArrayList<Point>>(); 
    public static ArrayList<String> Clientsuname1 = new <String>ArrayList();
      
    // constructor 
    public ClientHandler(Socket s, String name,int i, 
    		ObjectInputStream dis, ObjectOutputStream dos) { 
        this.dis = dis; 
        this.dos = dos; 
        this.name = name; 
        this.s = s; 
        this.isloggedin=false; 
        this.i=i;
    } 
  
    @Override
    public void run() { 
    	try {
    		//System.out.println(this.name);
    		final Object obj1 = dis.readObject();
   	        String recieved1 = "no"; 
		      if(obj1 instanceof String) {
		    	 final String a1 = (String)obj1;
		    	 recieved1=a1;
		    	 String[] input1 = recieved1.split(",");
               	if(input1[0].equals("uname")) {
               		
               		this.uname=input1[1];
               		
               		
               	}
		      }
		      if (!Mainserver.Clientsuname.isEmpty()) {
		    	  for (ClientHandler mc : Mainserver.ar) {
		    		  if(!mc.name.equals(this.name)) {
		    		  if(mc.uname.equals(this.uname)) {
		    			  
		    			  
		    			  this.duplicate=true;
		    			  this.couldnotenter=true;
		    			  //System.out.println(this.duplicate);
	    				  
		    		  }
		    	  }
	    		 }
		      }
		   
    		if(this.name.equals("client 0")) {
    			String msg="admin"+","+"yes";
    			//System.out.println(msg);
    			this.Admin=true;
    			this.isloggedin=true;
    			this.dos.writeObject(msg);
    			this.dos.reset();
    		}
    		
    		
    		int sent=0;
    		if(!this.Admin) {
    			//System.out.println("entered not admin");
    			if(!this.duplicate) {
    			for (ClientHandler mc : Mainserver.ar)  
                { 
    				if(mc.Admin) {
    					String joinmsg="Newjoin"+","+this.uname;
    					mc.dos.writeObject(joinmsg);
    					mc.dos.reset();
    					this.adminavail=true;
    					
    				}
    				
                }
    			}
    			if(!this.adminavail) {
    				
    				this.noadmin=true;
    				this.couldnotenter=true;
    			}
    			if(!this.isloggedin && !this.duplicate && !this.noadmin && !this.exit && !this.couldnotenter) {
    				this.dos.writeObject("wait"+","+"yes");
					this.dos.reset();
    			}
    			while(!this.isloggedin) {
    				//System.out.println("entered not logged in");
    				if(this.exit) {
    					String msg="Exit"+","+"reject";
    					
    					this.dos.writeObject(msg);
    					this.dos.reset();
    					break;
    				}
    				if(this.duplicate) {
    					String msg="Exit"+","+"duplicate";
    					
    					this.dos.writeObject(msg);
    					this.dos.reset();
    					break;
    					
    				}
    				if(this.noadmin) {
    					String msg="Exit"+","+"noadmin";
    					
    					this.dos.writeObject(msg);
    					this.dos.reset();
    					break;
    					
    				}
    				
    				System.out.println("waiting for admin!");
    			}
    		}
    		 	if(this.isloggedin && !this.Admin) {
    		 		this.dos.writeObject("wait"+","+"no");
					this.dos.reset();
    		 	}
    			
    			if(this.isloggedin) {
    			Mainserver.Clientsuname.add(this.uname);
    			}
    				
    			
    			
    			if(this.isloggedin) {
                this.Clientsuname1=Mainserver.Clientsuname;
    			for (ClientHandler mc : Mainserver.ar) 
    			{
    				mc.Clientsuname1=Mainserver.Clientsuname;
    				mc.dos.writeObject(this.Clientsuname1);
    				mc.dos.reset();
    			}
    			}
  
        
        while (true)  
        { 
                
        	
        	     final Object obj = dis.readObject();
        	     String recieved = "no"; 
			      if(obj instanceof String) {
			    	 final String a = (String)obj;
			    	 recieved=a;
			    	 String[] input = recieved.split(",");
	                	
	                	if(input[0].equals("Newjoin")) {
	                		for (ClientHandler mc : Mainserver.ar) {
	                			if(mc.uname.equals(input[1])) {
	                				if(input[2].equals("yes")) {
	                					mc.isloggedin=true;
	                					
	                				}
	                				else {
	                					mc.isloggedin=false;
	                					
	                					
	                					mc.exit=true;
	                					mc.couldnotenter=true;
	                				}
	                			}
	                		}
	                	}
	                	if(input[0].equals("Kick")){
	                		for (ClientHandler mc : Mainserver.ar) {
	                			if (mc.uname.equals(input[1])) {
	                				mc.dos.writeObject("kick");
	                				mc.dos.reset();
	                				Mainserver.Clientsuname.remove(new String(input[1]));
	                				for (ClientHandler ac : Mainserver.ar) 
	                    			{
	                    				ac.Clientsuname1=Mainserver.Clientsuname;
	                    				ac.dos.writeObject(this.Clientsuname1);
	                    				ac.dos.reset();
	                    			}
	                			}
	                		}
	                	}
	                	if(input[0].equals("Newfile")) {
	                		Mainserver.backgroundpath=null;
	                		Mainserver.map.clear();
	                		for (ClientHandler ac : Mainserver.ar) 
                			{
                				if(!ac.name.equals(this.name)) {
                				ac.dos.writeObject("Newfile");
                				ac.dos.reset();
                				}
                			}
	                	}
	                	if(input[0].equals("Open")) {
	                		Mainserver.backgroundpath=input[1];
	                		for (ClientHandler ac : Mainserver.ar) 
                			{
                				if(!ac.name.equals(this.name)) {
                				ac.dos.writeObject("Open"+","+input[1]);
                				ac.dos.reset();
                				}
                			}
	                	}
				} else {
					final ConcurrentHashMap b = (ConcurrentHashMap)obj;
					if(this.isloggedin) {
					this.map = b;
					
					Mainserver.map=map;
					if(!this.map.isEmpty() && this.isloggedin) {
		                for (ClientHandler mc : Mainserver.ar)  
		                { 
		                     
		                 
		                    if(!mc.name.equals(this.name) && mc.isloggedin==true) {
		                    	
		                    	mc.dos.writeObject(this.map);
		            			
		            			mc.dos.reset();
		                    } 
		                } }
					}
				} 
                  
                if(recieved.equals("logout")){ 
                    this.isloggedin=false; 
                    this.s.close(); 
                    break; 
                }
                
                  
            }
    }catch(SocketException e) {
    	Mainserver.ar.remove(this);
    	if(!this.couldnotenter) {
    		if(this.Admin) {
    			for (ClientHandler mc : Mainserver.ar) {
        				try {
							mc.dos.writeObject("adminleft,"+"left");
							mc.dos.reset();
						} catch (IOException e1) {
							Mainserver.display.append("IO exception for admin while closing, ERROR " + "\n");
						}
        				
    		}
    			}   		
    		else {
    			for (int i=0;i<Mainserver.Clientsuname.size();i++)
    	{
    		if(this.uname.equals(Mainserver.Clientsuname.get(i))) {
    			Mainserver.Clientsuname.remove(new String(this.uname));
    		}
    	}
    	for (ClientHandler ac : Mainserver.ar) 
		{
			ac.Clientsuname1=Mainserver.Clientsuname;
			try {
				ac.dos.writeObject(this.Clientsuname1);
				ac.dos.reset();
			} catch (IOException e1) {
				Mainserver.display.append("IO exception, ERROR " + "\n");
			}
			
		}
    	}
    		
    	}
    	
		Mainserver.display.append("Socket Closed: \n");
		System.out.println("Socketclosed");
	}
    	
    	catch (IOException e) {
            Mainserver.display.append("Connection could not be closed, ERROR " + "\n");
        } catch (ClassNotFoundException e) {
        	 Mainserver.display.append("ClassnotFounderror!!, ERROR " + "\n");
		}
}
}