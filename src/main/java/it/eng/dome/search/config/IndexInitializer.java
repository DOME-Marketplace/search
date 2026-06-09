package it.eng.dome.search.config;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.ProviderIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;

import javax.annotation.PostConstruct;

@Configuration
public class IndexInitializer {

    private final ElasticsearchOperations elasticsearchOperations;
    private final Logger logger = LoggerFactory.getLogger(IndexInitializer.class);

    public IndexInitializer(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @PostConstruct
    public void configureIndices() {
        setupIndex(IndexingObject.class);
        setupIndex(ProviderIndex.class);
    }

    private void setupIndex(Class<?> clazz) {
        IndexOperations indexOps = elasticsearchOperations.indexOps(clazz);
        if (!indexOps.exists()) {
            String jsonSettings = "{\"analysis\": {\"normalizer\": {\"lowercase_normalizer\": {\"type\": \"custom\",\"filter\": [\"lowercase\"]}}}}";
            indexOps.create(Document.parse(jsonSettings));
            indexOps.putMapping(indexOps.createMapping(clazz));
            logger.info("Indice per {} creato con normalizer personalizzato.", clazz.getSimpleName());
        }
    }
}