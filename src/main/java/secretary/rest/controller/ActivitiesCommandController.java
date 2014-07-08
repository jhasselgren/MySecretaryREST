package secretary.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import secretary.core.domain.Activity;
import secretary.core.domain.TextThing;
import secretary.core.domain.Thing;
import secretary.core.domain.ThingType;
import secretary.core.services.ActivityService;

@Controller
@RequestMapping("/activity")
public class ActivitiesCommandController {
	
	@Autowired
	ActivityService activityService;
	
	private final static Logger logger = LoggerFactory.getLogger(ActivitiesCommandController.class);
	
	@RequestMapping(value="/{activityId}/{thingId}", method=RequestMethod.POST)
	public ResponseEntity<Activity> addSubThing(@PathVariable String activityId, @PathVariable String thingId, @RequestBody Thing newSubThing){
		
		Activity activity = activityService.getActivity(activityId).getEntity();
		
		for (Thing thing : activity.getThings()) {
			if(thing.getId().equals(thingId)){
				if(thing.getType() == ThingType.TEXT){
					TextThing textThing = (TextThing) thing;
					textThing.addThing(newSubThing);
					break;
				}
				else{
					return new ResponseEntity<Activity>(HttpStatus.BAD_REQUEST);
				}
			}
		} 
		
		activity = activityService.updateActivity(activity).getEntity();
		
		return new ResponseEntity<Activity>(activity, HttpStatus.OK);
		
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public void handle(HttpMessageNotReadableException e) {
	    logger.warn("Returning HTTP 400 Bad Request", e);
	}
}
