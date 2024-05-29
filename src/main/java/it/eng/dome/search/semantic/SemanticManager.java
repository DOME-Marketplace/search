/*
 * package it.eng.dome.search.semantic;
 * 
 * import java.net.URL; import java.util.HashMap; import java.util.Map;
 * 
 * import org.slf4j.Logger; import org.slf4j.LoggerFactory; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.HttpEntity; import
 * org.springframework.http.HttpHeaders; import
 * org.springframework.http.MediaType; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.stereotype.Service; import
 * org.springframework.web.client.RestTemplate;
 * 
 * import com.fasterxml.jackson.databind.ObjectMapper;
 * 
 * import it.eng.dome.search.domain.IndexingObject; import
 * it.eng.dome.search.domain.ProductOffering; import
 * it.eng.dome.search.indexing.MappingManager; import
 * it.eng.dome.search.rest.web.util.RestUtil; import
 * it.eng.dome.search.semantic.domain.CategorizationResultObject;
 * 
 * @Service public class SemanticManager {
 * 
 * @Autowired private RestTemplate restTemplate;
 * 
 * @Autowired private RestUtil restUtil;
 * 
 * @Autowired private MappingManager mappingManager;
 * 
 * private static final Logger log =
 * LoggerFactory.getLogger(SemanticManager.class);
 * 
 * private static final ObjectMapper objectMapper = new ObjectMapper();
 * 
 * 
 * public IndexingObject processCategorization(ProductOffering product,
 * IndexingObject objToIndex) {
 * 
 * String reqUrl = null; HttpHeaders header = new HttpHeaders();
 * header.setContentType(MediaType.TEXT_PLAIN);
 * 
 * Map<String, String> bodyParamMap = new HashMap<String, String>();
 * 
 * //Set your request body params bodyParamMap.put("key1", "value1");
 * bodyParamMap.put("key2", "value2"); bodyParamMap.put("key3", "value3");
 * 
 * String reqBodyData = new ObjectMapper().writeValueAsString(bodyParamMap);
 * HttpEntity<String> requestEnty = new HttpEntity<>(reqBodyData, header);
 * 
 * 
 * String requestForCategorizationResults =
 * restTemplate.getCategorizationResults();
 * 
 * ResponseEntity<CategorizationResultObject> result =
 * restTemplate.postForEntity(reqUrl, requestEnty,
 * CategorizationResultObject.class);
 * 
 * }
 * 
 * 
 * public IndexingObject processEntitiesExtraction(ProductOffering product,
 * IndexingObject objToIndex) {
 * 
 * return objToIndex;
 * 
 * }
 * 
 * 
 * }
 */