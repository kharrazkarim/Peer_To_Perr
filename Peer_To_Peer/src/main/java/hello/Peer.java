package hello;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class Peer {

    private  String url;
    private  List <String> peer_list ;
    private List <File>file_list;
    
 // static variable peer of type Peer 
    private static Peer peer = null; 
    
 
    public Peer getInstance() 
    { 
        if (peer == null) 
            peer = new Peer(); 
        return peer; 
    } 
    
  
    public boolean addPeer (String url) {
    	
    	boolean response=false;
    	
    	if (! peer.getPeer_list().contains(url))
    	{peer.getPeer_list().add(url);
    	response= true;}
    	
    	
    	return response;
    	
    }
    
    public boolean deletePeer (String url) {
    	boolean response=false;
    	
    	if (peer.getPeer_list().contains(url))
    	{peer.getPeer_list().remove(url);
    	response=true ;}
    	
    	return response;
    }
    
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getPeer_list() {
		return peer_list;
	}

	public void setPeer_list(List<String> peer_list) {
		this.peer_list = peer_list;
	}

    

	public List <File> getFile_list() {
		return file_list;
	}

	public void setFile_list(List <File> file_list) {
		this.file_list = file_list;
	}
	
	public boolean addFile(File file) {
		
		boolean response=false;
		
		if (!peer.getFile_list().contains(file))
		{this.file_list.add(file);
		response=true;}
		
		return response;
	}
	
	public boolean deleteFile (File file) {
		boolean response=false;
	
		if (peer.getFile_list().contains(file))
		{
			this.file_list.remove(file);
			response=true;
		}
		
		return response;
	}
	
    private  Peer() {
    	 
         try(final DatagramSocket socket = new DatagramSocket()){
        	  try {
				socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	  url = socket.getLocalAddress().getHostAddress()+":8880";
        	} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         
        peer_list = new ArrayList<String>();
        peer_list.add(url);
      
        
        file_list= new ArrayList<File>();
        
        /*  File f= new File ("karim",(float) 2);
	        File f1= new File ("karim",(float) 4);
	        file_list.add(f);
	        file_list.add(f1);
        */
        
        
        
                
    }

    
    


}