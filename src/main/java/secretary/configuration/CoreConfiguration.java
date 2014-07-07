package secretary.configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

import secretary.core.services.ActivityService;
import secretary.core.services.FileService;
import secretary.core.services.MongoDbFileService;
import secretary.core.services.StubActivityService;

@Configuration
public class CoreConfiguration{

	@Bean
	public ActivityService creatActivityService(){
		return new StubActivityService();
	}
	
	@Bean
	public FileService createFileService(GridFsOperations gridOperations){
		MongoDbFileService fileService = new MongoDbFileService(gridOperations);
		return fileService;
	}
	
	@Bean
	public MappingJackson2HttpMessageConverter createMappingJackson2HttpMessageConverter(ObjectMapper mapper){
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		mapper.setDateFormat(dateFormat);
		
		converter.setObjectMapper(mapper);
		
		return converter;
	}
}
