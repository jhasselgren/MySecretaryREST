package secretary.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import secretary.core.domain.Activity;
import secretary.core.events.GetOneEvent;
import secretary.core.services.ActivityService;

@RestController
@RequestMapping("/activity")
public class ActivitiesQueryController {
	
	@Autowired
	ActivityService activityService;
	
	private final static Logger logger = LoggerFactory.getLogger(ActivitiesQueryController.class);
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Activity> getById(@PathVariable String id){
		GetOneEvent<Activity> event = activityService.getActivity(id);
		
		if(!event.isEntityFound()){
			return new ResponseEntity<Activity>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Activity>(event.getEntity(), HttpStatus.OK);
		
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public void handle(HttpMessageNotReadableException e) {
	    logger.warn("Returning HTTP 400 Bad Request", e);
	}
	
}
