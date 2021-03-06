package secretary.configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import secretary.core.services.ActivityService;
import secretary.core.services.FileService;
import secretary.core.services.MongoDbActivityServer;
import secretary.core.services.MongoDbFileService;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class CoreConfiguration {

	@Bean
	public ActivityService creatActivityService(){
//		return new StubActivityService();
		return new MongoDbActivityServer();
	}
	
	@Bean
	public FileService createFileService(GridFsOperations gridOperations){
		MongoDbFileService fileService = new MongoDbFileService(gridOperations);
		return fileService;
	}
	
	@Bean
	public ObjectMapper createObjectMapper(){
		ObjectMapper objectMapper = new ObjectMapper();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		objectMapper.setDateFormat(dateFormat);
		
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		//objectMapper.registerSubtypes(ToDoThing.class, FileThing.class, TextThing.class);
		return objectMapper;
	}
	
	@Bean
	public MappingJackson2HttpMessageConverter createMappingJackson2HttpMessageConverter(){
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		
		converter.setObjectMapper(createObjectMapper());
		
		return converter;
	}
	
	@Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(-1);
        factory.setMaxRequestSize(-1);
        return factory.createMultipartConfig();
    }
	
	
	
}
