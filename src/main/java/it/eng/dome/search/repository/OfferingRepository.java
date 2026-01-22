package it.eng.dome.search.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import it.eng.dome.search.domain.IndexingObject;

@Repository
public interface OfferingRepository extends ElasticsearchRepository<IndexingObject, String> {

	List<IndexingObject> findByProductOfferingIdIn(List<String> ids);

//	List<IndexingObject> findByProductOfferingId(String productOfferingId);
//
//	List<IndexingObject> findByServicesId(String servicesId);
//
//	@Query("{\"nested\": {\"path\": \"categories\", \"query\": {\"match\": {\"categories.name\": \"?0\"}}}}")
//	List<IndexingObject> findByCategoryName(String categoryName, Pageable pageable);
//
//	@Query("{\"nested\": {\"path\": \"categories\", \"query\": {\"term\": {\"categories.id\": \"?0\"}}}}")
//	List<IndexingObject> findByCategoryId(String categoryId);

	// Search by category IDs or names
	@Query("{\"nested\": {\"path\": \"categories\", \"query\": {\"bool\": {\"should\": [ {\"terms\": {\"categories.id\": ?0}}, {\"terms\": {\"categories.name\": ?0}} ]}}}}")
	List<IndexingObject> findByCategoryIdsOrNames(List<String> categoryValues);

	// Find all documents where relatedParties field exists
	@Query("{\"nested\": { \"path\": \"relatedParties\", \"query\": { \"exists\": { \"field\": \"relatedParties.id\" } } } }")
	List<IndexingObject> findAllWithRelatedParties();

	// Search by compliance levels
	@Query("{\"terms\": {\"complianceLevels\": ?0}}")
	List<IndexingObject> findByComplianceLevels(List<String> complianceLevels);
}