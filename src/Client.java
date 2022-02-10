import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.Room;
import sfs2x.client.entities.User;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LeaveRoomRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.requests.LogoutRequest;
import sfs2x.client.requests.PublicMessageRequest;
import sfs2x.client.util.ConfigData;
import java.util.Scanner;

public class Client  {
	
   SmartFox sfs;
   ConfigData cfg;
   String Clientname;
   String Clientpassword;
   boolean success;
   
      
   
    public Client (String name,String password)
    {
    	  
    	  Clientname=name;
    	  Clientpassword=password;
    	   

        // Configure client connection settings
        cfg = new ConfigData();
        cfg.setHost("localhost");
        cfg.setPort(9933);
        cfg.setZone("BasicExamples");
        cfg.setDebug(false);

        // Set up event handlers
        sfs = new SmartFox();
        sfs.addEventListener(SFSEvent.CONNECTION, this::onConnection);
        sfs.addEventListener(SFSEvent.CONNECTION_LOST, this::onConnectionLost);
        sfs.addEventListener(SFSEvent.LOGIN, this::onLogin);
        sfs.addEventListener(SFSEvent.LOGIN_ERROR, this::onLoginError);
        sfs.addEventListener(SFSEvent.LOGOUT,this::onLogout);
        sfs.addEventListener(SFSEvent.ROOM_JOIN, this::onRoomJoin);
    	sfs.addEventListener(SFSEvent.ROOM_JOIN_ERROR,this::onRoomJoinError);
    	sfs.addEventListener(SFSEvent.USER_COUNT_CHANGE, this::onUserCountChange);
    	//sfs.addEventListener(SFSEvent.USER_ENTER_ROOM, this::onUserEnterRoom );
    	sfs.addEventListener(SFSEvent.USER_EXIT_ROOM, this::onUserExitRoom);
    	sfs.addEventListener(SFSEvent.PUBLIC_MESSAGE, this::onPublicMessage);

        
        // Connect to server
        sfs.connect(cfg);
    }

    // ----------------------------------------------------------------------
    // Event Handlers
    // ----------------------------------------------------------------------

    
 
    public void onConnection(BaseEvent evt)
    {
  
  
    	 success = (boolean) evt.getArguments().get("success");
    	 System.out.println("chh"+success);

        if (success)
        {
            System.out.println("Connection success");
            sfs.send(new LoginRequest(Clientname, Clientpassword, "BasicExamples"));
            
        }
        else {
        	System.out.println("Connection Failed. Is the server running?");

        }
             
        
    }

     
    public void onConnectionLost(BaseEvent evt)
    {
        System.out.println("-- Connection lost --");
    }

    
    
    public void onLogin(BaseEvent evt)
    {
    	
    	System.out.println("Login successful!");
        System.out.println("Logged in as: " + sfs.getMySelf().getName());
        sfs.send(new JoinRoomRequest("The Lobby"));
    }

    
    
    public void onLoginError(BaseEvent evt)
    {
        String message = (String) evt.getArguments().get("errorMessage");
        System.out.println("Login failed. Cause: " + message);
    }
    
    
    
    public void onLogout(BaseEvent evt)
    {
    	 sfs.send(new LogoutRequest());
    	 System.out.println("Logout successful!");
    }
    
    
    

    public void onRoomJoin(BaseEvent evt)
    {
        Room room = (Room) evt.getArguments().get("room");
        System.out.println("Joined Room: " + room.getName());
    	String message;
      
    }
    
    
    
    public void onRoomJoinError(BaseEvent evt)
    {
    	 String message = (String) evt.getArguments().get("errorMessage");
         System.out.println("Room Joining failed. Cause: " + message);
    }

    public int onUserCountChange(BaseEvent evt)
    {
    	Room room = (Room) evt.getArguments().get("room");
    	int count=room.getUserCount();
    	System.out.println(count);
    	
		return count;
    }
    
    private void onUserEnterRoom(BaseEvent evt)
    {
    	 //User sender = (User)evt.getArguments().get("sender");
        // System.out.println("User " + sender.getName() + "entered the room");	 
    
    }
    
    private void onUserExitRoom(BaseEvent evt)
    {
    	 sfs.send(new LeaveRoomRequest());
    	 Room room = (Room)evt.getArguments().get("room");
    	 User user = (User)evt.getArguments().get("user");
    	 System.out.println("User " + user.getName() + " just left Room " + room.getName());
    }
    
    public void onPublicMessage(BaseEvent evt)
    {
    	
    	 User sender = (User)evt.getArguments().get("sender");
    	 if (sender == sfs.getMySelf()) {
    	 System.out.println("YOU:" + evt.getArguments().get("message"));}
    	 
    	 else {
         System.out.println("User " + sender.getName() + " said:" + evt.getArguments().get("message"));	 
    	 }   	 
 
    }

    public void sendMSG(String Msg) {
    	sfs.send(new PublicMessageRequest(Msg));
    }
    
    
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		Scanner myObj = new Scanner(System.in);  // Create a Scanner object
		
		System.out.println("Login to The lobby chat room");
	    System.out.println("Enter username");

	    String userName = myObj.nextLine();  // Read user input
	    System.out.println("Enter Password");

	    String password = myObj.nextLine();  // Read user input
	    
	    Client c1=new Client(userName,password);
	    
	    	
	        System.out.println("Enter message : "); 
	    	String Msg = myObj.nextLine();	
	    	c1.sendMSG(Msg);
	    	
	      
	}

}
