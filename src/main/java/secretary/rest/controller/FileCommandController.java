package secretary.rest.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import secretary.core.domain.FileThing;
import secretary.core.services.FileService;

@Controller
@RequestMapping("/file")
public class FileCommandController {
	
	@Autowired
	FileService fileService;
	
	private final static Logger logger = LoggerFactory.getLogger(FileCommandController.class);
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public ResponseEntity<String> handleFileUpload(@RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile file){
		
		String id = fileName;
		
		if(!file.isEmpty()){
			byte[] bytes;
			try {
				bytes = file.getBytes();
				ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
				fileService.UploadFile(inputStream, id);
				inputStream.close();
				return new ResponseEntity<String>(id, HttpStatus.OK);
				
			} catch (IOException e) {
				logger.debug(e.getMessage());
				e.printStackTrace();
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	
	@RequestMapping(value="/download")
    public HttpEntity<byte[]> getFile(@RequestBody FileThing thing) throws Exception{
    	ByteArrayOutputStream outputFile = new ByteArrayOutputStream();
    	try{
    			fileService.fetchFile(outputFile, thing.getFileId());
    			
    			HttpHeaders httpHeaders = new HttpHeaders();
    			
    			MediaType mediaType = new MediaType(thing.getFileType());
    			
    			httpHeaders.setContentType(mediaType);
    			httpHeaders.add("Content-Disposition", "attachment; filename=\"" +thing.getName() + "\"");
    			
                return new ResponseEntity<byte[]>(outputFile.toByteArray(), httpHeaders, HttpStatus.OK);

	    }catch(Exception e){
	        logger.debug(e.getMessage());
	        e.printStackTrace();
	        throw e;
	    }
    	finally{
    		if(outputFile != null){
    			outputFile.close();
    		}
    	}
    }
    
    
    @RequestMapping(value="/download/{fileId}")
    public HttpEntity<byte[]> getFile(@PathVariable String fileId) throws Exception{
    	try{
    			ByteArrayOutputStream outputFile = new ByteArrayOutputStream();
    			fileService.fetchFile(outputFile, fileId);
    			
    			HttpHeaders httpHeaders = new HttpHeaders();
    			
    			
    			httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
    			httpHeaders.add("Content-Disposition", "attachment; filename=\"test\"");
    			
                return new ResponseEntity<byte[]>(outputFile.toByteArray(), httpHeaders, HttpStatus.OK);

	    }catch(Exception e){
	        logger.debug(e.getMessage());
	        e.printStackTrace();
	        throw e;
	    } 	
    }
    
	
}
