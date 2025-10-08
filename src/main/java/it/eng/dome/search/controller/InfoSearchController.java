package it.eng.dome.search.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/search")
public class InfoSearchController {

	private static final Logger log = LoggerFactory.getLogger(InfoSearchController.class);

	@Autowired
	private BuildProperties buildProperties;

	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = "application/json")
	@Operation(responses = {
			@ApiResponse(content = @Content(mediaType = "application/json", examples = @ExampleObject(value = INFO))) })
	public Map<String, String> getInfo() {
		log.info("Request getInfo");
		Map<String, String> map = new ConcurrentHashMap<String, String>();
		map.put("version", buildProperties.getVersion());
		map.put("name", buildProperties.getName());
		map.put("release_time", getFormatterTimestamp(buildProperties.getTime()));
		log.debug(map.toString());
		return map;
	}

	private final String INFO = "{\"name\":\"Search\", \"version\":\"1.0.0\", \"release_time\":\"08-10-2025 15:23:51\"}";
	
	
	private String getFormatterTimestamp(Instant time) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        ZonedDateTime zonedDateTime = time.atZone(ZoneId.of("Europe/Rome"));
    	return zonedDateTime.format(formatter);        
    }
}
