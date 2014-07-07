package secretary.rest.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public ResponseEntity<FileThing> handleFileUpload(@RequestParam("description") String description, @RequestParam("file") MultipartFile file){
		
		if(!file.isEmpty()){
			byte[] bytes;
			try {
				
				FileThing fileThing = new FileThing();
				fileThing.setName(file.getOriginalFilename());
				fileThing.setDescription(description);
				fileThing.setFileType("");
				
				bytes = file.getBytes();
				ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
				fileService.UploadFile(inputStream, fileThing.getId());
				inputStream.close();
				return new ResponseEntity<FileThing>(fileThing, HttpStatus.OK);
				
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<FileThing>(HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<FileThing>(HttpStatus.BAD_REQUEST);
	}
}
