package secretary.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import secretary.core.services.ActivityService;
import secretary.core.services.StubActivityService;

@Configuration
public class CoreConfiguration{

	@Bean
	public ActivityService creatActivityService(){
		return new StubActivityService();
	}
	
}
