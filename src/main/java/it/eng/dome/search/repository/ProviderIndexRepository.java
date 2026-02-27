package it.eng.dome.search.repository;

import it.eng.dome.search.domain.ProviderIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderIndexRepository extends ElasticsearchRepository<ProviderIndex, String> {
    // qui rimangono i metodi derivati standard
    List<ProviderIndex> findByCountry(String country);
    List<ProviderIndex> findByComplianceLevelsIn(List<String> complianceLevels);
    List<ProviderIndex> findByTradingNameContainingIgnoreCase(String tradingName);
    List<ProviderIndex> findByCountryAndComplianceLevelsIn(String country, List<String> complianceLevels);
}