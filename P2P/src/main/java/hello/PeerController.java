package hello;

import java.awt.List;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class PeerController {

	Peer p = new Peer(); 	
 
	
    @RequestMapping(method=RequestMethod.GET, value ="/files" )
    public <file> java.util.List<hello.file> file_list() {
    	
        return p.getFile_list() ;
    }

    @RequestMapping(method=RequestMethod.POST, value ="/peers" )
    @ResponseStatus(value = HttpStatus.OK)
    public void register_peer (HttpServletRequest request) {
    	
    	
    	 java.util.List <String> peers = new ArrayList<String>();
    	 peers=p.getPeer_list();
    	 
    	 String ip = request.getRemoteAddr();
    	 
    	 peers.add(ip);
    	 p.setPeer_list(peers);
       
    }
    
    @RequestMapping(method=RequestMethod.DELETE, value ="/peers" )
    @ResponseStatus(value = HttpStatus.OK)
    public void delete_peer (HttpServletRequest request) {
    	
    	
    	 java.util.List <String> peers = new ArrayList<String>();
    	 peers=p.getPeer_list();
    	 
    	 String ip = request.getRemoteAddr();
    	 
    	 if (peers.contains(ip)==true ) 
    	 {
    		 peers.remove(ip);
    		 p.setPeer_list(peers);
    		 
    	 }
    	 
       
    }

    
    @RequestMapping(method=RequestMethod.GET, value ="/peers" )
	public <String> java.util.List  peer_list() {
		
    return p.getPeer_list() ;
    	}
    
 
    
}
    
   
    
