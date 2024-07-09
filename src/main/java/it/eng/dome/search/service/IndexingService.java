package it.eng.dome.search.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.ProductOffering;
import it.eng.dome.search.indexing.IndexingManager;
import it.eng.dome.search.repository.OfferingRepository;
import it.eng.dome.search.rest.web.util.RestUtil;

@Service
public class IndexingService {

	@Autowired
	OfferingRepository offeringRepo;

	@Autowired
	IndexingManager indexingManager;
	
	@Autowired
	private RestUtil restUtil;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();

	private static final Logger log = LoggerFactory.getLogger(IndexingService.class);
	
//	@Scheduled(fixedDelay = 3000)
//	private void test() {
//
//	    System.out.println("test -> " + new Date());
//
//	    try {
//	        Thread.sleep(2000);
//	    }
//	    catch (Exception e) {
//	        System.out.println("error");
//	    }
//	}
	

	@Scheduled(fixedDelay = 300000 )
	public void indexing() {
		log.info("Indexing is executing ..... ");
		//invoca endpoint e recupera lista di product offering
		String listProductOfferings = restUtil.getAllProductOfferings(); //-------> from BAE (change when needed)
		try {
			ProductOffering[] productList = objectMapper.readValue(listProductOfferings, ProductOffering[].class);
			log.info("***Tot of ProductOfferings =  ..... "+productList.length);
		//ArrayList<ProductOffering> productList = new ArrayList();

		for(ProductOffering product : productList) {

			String idProduct = product.getId();
			List<IndexingObject> listFromRepo = offeringRepo.findByProductOfferingId(idProduct);
			if(listFromRepo.isEmpty()) {
				IndexingObject indexingObjEmpty = new IndexingObject();
				//fare il mapping da productOffering a index
				indexingObjEmpty = indexingManager.processOffering(product, indexingObjEmpty);
				offeringRepo.save(indexingObjEmpty);
			}else {

				for(IndexingObject obj : listFromRepo) {

					IndexingObject indexingObjEmpty = new IndexingObject();
					//fare il mapping da productOffering a indx
					indexingObjEmpty = indexingManager.processOffering(product, indexingObjEmpty);
					indexingObjEmpty.setId(obj.getId());

					offeringRepo.save(indexingObjEmpty);

				}
			}
		}
		} catch (JsonMappingException e) {
			log.info("Error JsonMappingException in Indexing() ");
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			log.info("Error JsonProcessingException in Indexing() ");
			e.printStackTrace();
		}

		log.info("Indexing process terminated ");
	}

}
