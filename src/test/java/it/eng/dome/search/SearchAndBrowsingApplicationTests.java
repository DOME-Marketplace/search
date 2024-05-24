package it.eng.dome.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.service.OfferingProcessor;

@SpringBootTest
class SearchAndBrowsingApplicationTests {

	@Autowired
	private OfferingProcessor offering;

	@Test
	public void testSave() {

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

		IndexingObject indexTest = offering.save(index);

		assertNotNull(indexTest.getId());
		assertEquals(index.getProductOfferingName(), indexTest.getProductOfferingName());
		assertEquals(index.getProductOfferingIsBundle(), indexTest.getProductOfferingIsBundle());
		assertEquals(index.getProductOfferingNameText(), indexTest.getProductOfferingNameText());

		// https://mkyong.com/spring-boot/spring-boot-spring-data-elasticsearch-example/
	}

}
