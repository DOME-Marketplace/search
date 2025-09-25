package it.eng.dome.search.service;

import java.util.List;

import it.eng.dome.brokerage.api.ProductOfferingApis;
import it.eng.dome.search.tmf.TmfApiFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.indexing.IndexingManager;
import it.eng.dome.search.repository.OfferingRepository;
import it.eng.dome.tmforum.tmf620.v4.model.ProductOffering;

@Service
public class IndexingService implements InitializingBean {

	@Autowired
	OfferingRepository offeringRepo;

	@Autowired
	IndexingManager indexingManager;

//	private static final ObjectMapper objectMapper = new ObjectMapper();

	private static final Logger log = LoggerFactory.getLogger(IndexingService.class);

	// Factory for TMF APIss
	@Autowired
	private TmfApiFactory tmfApiFactory;

	private ProductOfferingApis productOfferingApis;

	@Override
	public void afterPropertiesSet () throws Exception {
		this.productOfferingApis = new ProductOfferingApis(tmfApiFactory.getTMF620ProductCatalogApiClient());

		log.info("IndexingService initialized with productOfferingApis");
	}

	// @Scheduled(fixedDelay = 3000)
	// private void test() {
	//
	// System.out.println("test -> " + new Date());
	//
	// try {
	// Thread.sleep(2000);
	// }
	// catch (Exception e) {
	// System.out.println("error");
	// }
	// }

	@Scheduled(fixedDelay = 300000)
	public void indexing() {
		log.info("Indexing is executing ..... ");
		// invoca endpoint e recupera lista di product offering
		//String listProductOfferings = restUtil.getAllProductOfferings(); // -------> from BAE (change when needed)
		List<ProductOffering> listProductOfferings = productOfferingApis.getAllProductOfferings(null, null); //----> now from TMF
		if (listProductOfferings == null) {
			log.warn("listProductOfferings cannot be null");
		} else {
			//ProductOffering[] productList = objectMapper.readValue(listProductOfferings, ProductOffering[].class);
			//log.info("Total of ProductOfferings found: {}", productList.length);
			log.info("Total of ProductOfferings found: {}", listProductOfferings.size());

			for (ProductOffering product : listProductOfferings) {
				String idProduct = product.getId();
				log.debug("Id product: {} ", idProduct);

				List<IndexingObject> listFromRepo = offeringRepo.findByProductOfferingId(idProduct);

				if (listFromRepo.isEmpty()) {
					log.debug("ProductOffering listFromRepo empty");
					IndexingObject indexingObjEmpty = new IndexingObject();
					// mapping from productOffering to index
					indexingObjEmpty = indexingManager.processOfferingFromTMForum(product, indexingObjEmpty);
					offeringRepo.save(indexingObjEmpty);
				} else {
					log.debug("ProductOffering listFromRepo size: {}", listFromRepo.size());
					for (IndexingObject obj : listFromRepo) {

						IndexingObject indexingObjEmpty = new IndexingObject();
						// mapping from productOffering to index
						indexingObjEmpty.setId(obj.getId());
						indexingObjEmpty = indexingManager.processOfferingFromTMForum(product, indexingObjEmpty);

						offeringRepo.save(indexingObjEmpty);
					}
				}
			}
		}
		log.info("Indexing process terminated ");
	}

}
