package it.eng.dome.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.ProductOffering;
import it.eng.dome.search.rest.web.util.RestUtil;

@Service
public class ResultProcessor {

	private static final Logger logger = LoggerFactory.getLogger(ResultProcessor.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private RestUtil restTemplate;

	public Page<ProductOffering> processResults(Page<IndexingObject> page, Pageable pageable){

		HashMap<String,ProductOffering> mapProductOffering = new HashMap<String,ProductOffering>();
		List<ProductOffering> listProductOffering = new ArrayList<ProductOffering>();

		try {
			List<IndexingObject> listIdexingObject = page.getContent();
			for(IndexingObject indexingObj : listIdexingObject) {

				if(indexingObj.getProductOfferingId()!= null) {
					if(indexingObj.getProductOfferingLifecycleStatus().contains("Launched") == true) {

					if(mapProductOffering.containsKey(indexingObj.getProductOfferingId())== false) {
						String requestForProductOfferingId = restTemplate.getProductOfferingById(indexingObj.getProductOfferingId());
						ProductOffering productOfferingDetails = objectMapper.readValue(requestForProductOfferingId, ProductOffering.class);

						mapProductOffering.put(indexingObj.getProductOfferingId(), productOfferingDetails);
					}

				}
				}

			}

			if(mapProductOffering.isEmpty() == false) {
				for(Entry<String,ProductOffering> entry : mapProductOffering.entrySet()) {

					listProductOffering.add(entry.getValue());
				}
			}

			return  new PageImpl<>(listProductOffering,pageable,mapProductOffering.size());
		} catch (JsonProcessingException e) {
			logger.warn("JsonProcessingException - Error during processResults(). Skipped: {}", e.getMessage());
			e.printStackTrace();
			return new PageImpl<>(new ArrayList<>());
		} catch (HttpServerErrorException e) {
			logger.warn("HttpServerErrorException - Error during processResults(). Skipped: {}", e.getMessage());
			e.printStackTrace();
			return new PageImpl<>(new ArrayList<>());
		}

	}

}

