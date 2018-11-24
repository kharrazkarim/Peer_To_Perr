package hello;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.storage.StorageFileNotFoundException;
import hello.storage.StorageService;


@RestController
public class PeerController {

 
    public final Peer peer;

    @Autowired
    private PeerController(Peer peer) {
        this.peer=peer;
    }
    
    
    @RequestMapping(method=RequestMethod.POST, value ="/peers")
    @ResponseStatus(value = HttpStatus.OK)
    public void register_peer (HttpServletRequest request) {
    	
    	
    	 java.util.List <String> peers = new ArrayList<String>();
    	 peers=peer.getPeer_list();
    	 
    	 String ip = request.getRemoteAddr();
    	 
    	 if  ( !peers.contains(ip))
    	 {	
    		peers.add(ip);
    	 	peer.setPeer_list(peers);
    	 }
    }
    
    
    @RequestMapping(method=RequestMethod.DELETE, value ="/peers" )
    @ResponseStatus(value = HttpStatus.OK)
    public void delete_peer (HttpServletRequest request) {
    	
    	
    	 java.util.List <String> peers = new ArrayList<String>();
    	 peers=peer.getPeer_list();
    	 
    	 String ip = request.getRemoteAddr();
    	 
    	 if (peers.contains(ip)==true ) 
    	 {
    		 peers.remove(ip);
    		 peer.setPeer_list(peers);
    		 
    	 }
    	        
    }

    @RequestMapping(method=RequestMethod.GET, value ="/peers",produces = "application/json" )
	public <String> java.util.List  peer_list() {
		
    return peer.getPeer_list() ;
    	}
       
    
    
 

}