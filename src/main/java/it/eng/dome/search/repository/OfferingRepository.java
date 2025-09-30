package it.eng.dome.search.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import it.eng.dome.search.domain.IndexingObject;

@Repository
public interface OfferingRepository extends ElasticsearchRepository<IndexingObject, String> {

	List<IndexingObject> findByProductOfferingId(String productOfferingId);
	
	List<IndexingObject> findByServicesId(String servicesId);
	
	@Query("{\"nested\": {\"path\": \"categories\", \"query\": {\"match\": {\"categories.name\": \"?0\"}}}}")
	List<IndexingObject> findByCategoryName(String categoryName, Pageable pageable);

//	@Query("{\"nested\": {\"path\": \"categories\", \"query\": {\"terms\": {\"categories.id\": \"?0\"}}}}")
//	List<IndexingObject> findByCategoryIds(List<String> categoryIds);

	@Query("{\"nested\": {\"path\": \"categories\", \"query\": {\"term\": {\"categories.id\": \"?0\"}}}}")
	List<IndexingObject> findByCategoryId(String categoryId);


}
