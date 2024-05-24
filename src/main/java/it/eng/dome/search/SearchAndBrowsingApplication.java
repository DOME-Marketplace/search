package it.eng.dome.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.service.OfferingProcessor;


@SpringBootApplication
public class SearchAndBrowsingApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SearchAndBrowsingApplication.class, args);
	}

	@Autowired
	private OfferingProcessor offering;

	@Override
	public void run(String... args) throws Exception {
		
		IndexingObject index = new IndexingObject();
		index.setId("index-id1");
		index.setProductOfferingId("product-offering-id1");
		index.setProductOfferingDescription("product offering description");
		index.setProductOffering(null);
		index.setProductOfferingIsBundle(false);
		index.setProductOfferingLastUpdate("2024-05-21T10:02:40.254833831Z");
		index.setProductOfferingName("product-offering-name");
		index.setProductOfferingNameText("Name Text");
		index.setProductSpecification(null);
		index.setProductSpecificationBrand("");
		index.setProductSpecificationDescription("Product Specification Description");
		index.setProductSpecificationId("product-specification-id1");
		index.setProductSpecificationName("Specification Name");
		index.setServices(null);
		index.setResources(null);
		offering.save(index);
	}
}
