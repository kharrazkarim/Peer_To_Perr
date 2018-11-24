package hello;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Peer {

    private  String url;
    private  List <String> peer_list ;
 
    private static Peer peer = new Peer( );    
    
    /* Static 'instance' method */
    public static Peer getInstance( ) {
       return peer ;
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

    
    private Peer(String url , List <String> peer_list) {
    	
    	this.url=url;
    	this.peer_list=peer_list;
    	
    	
    }
    	
    public Peer() {
    	 
    	
         try(final DatagramSocket socket = new DatagramSocket()){
        	  try {
				socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	  url = socket.getLocalAddress().getHostAddress();
        	} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         
        peer_list = new ArrayList<String>();
        peer_list.add(url);
        
                
    }

}