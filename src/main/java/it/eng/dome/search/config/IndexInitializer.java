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
        try {
            setupIndex(IndexingObject.class);
            setupIndex(ProviderIndex.class);
        } catch (Exception e) {
            logger.error("Critical error during index initialization", e);
            // Se non riusciamo a creare gli indici, l'applicazione deve crashare 
            // in modo controllato, ma ora sappiamo perché.
        }
    }

    private void setupIndex(Class<?> clazz) {
        IndexOperations indexOps = elasticsearchOperations.indexOps(clazz);
    
        //  forziamo la creazione "pulita" con i nostri settings.
        if (indexOps.exists()) {
            indexOps.delete();
            logger.info("Existing index for {} deleted to force reconfiguration.", clazz.getSimpleName());
        }

        // Ora creiamo l'indice da zero
        String jsonSettings = "{\"analysis\": {\"normalizer\": {\"lowercase_normalizer\": {\"type\": \"custom\",\"filter\": [\"lowercase\"]}}}}";
        
        indexOps.create(Document.parse(jsonSettings));
        indexOps.putMapping(indexOps.createMapping(clazz));
        
        logger.info("Index for {} created successfully with custom normalizer.", clazz.getSimpleName());
    }
}