package it.eng.dome.search.config;

import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
@EnableElasticsearchRepositories(basePackages = "it.eng.dome.search.repository")
//@EnableElasticsearchAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.cluster.name:elasticsearch}")
    private String clusterName;

    @Value("${elasticsearch.address.host:127.0.0.1}")
    private String elasticsearchHost;

    @Value("${elasticsearch.address.port:9200}")
    private int elasticsearchPort;

    @Value("${elasticsearch.address.username:}")
    private String elasticsearchUsername;

    @Value("${elasticsearch.address.password:}")
    private String elasticsearchPassword;

    @Value("${elasticsearch.address.ssl.enabled:false}")
    private boolean usingSsl;

    @Value("${elasticsearch.address.ssl.verification:true}")
    private boolean usingSslVerification;

    private final Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

    @Override
    public RestHighLevelClient elasticsearchClient() {

        ClientConfiguration.MaybeSecureClientConfigurationBuilder builder = ClientConfiguration.builder()
                .connectedTo(elasticsearchHost + ":" + elasticsearchPort);

        ClientConfiguration.TerminalClientConfigurationBuilder builderWithProtocol;
        if(usingSsl) {
            if(! usingSslVerification) {
                try {
                    SSLContextBuilder sslBuilder = SSLContexts.custom()
                            .loadTrustMaterial(null, (x509Certificates, s) -> true);

                    final SSLContext sslContext = sslBuilder.build();
                    builderWithProtocol = builder.usingSsl(sslContext);

                } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                    logger.warn("Cannot disable ssl verification: {}", e.getLocalizedMessage());
                    builderWithProtocol = builder.usingSsl();
                }

            } else {
                builderWithProtocol = builder.usingSsl();
            }
        } else {
            builderWithProtocol = builder;
        }

        if(elasticsearchUsername != null && elasticsearchPassword != null && !elasticsearchUsername.isBlank() && !elasticsearchPassword.isBlank()) {
            builderWithProtocol = builderWithProtocol
                    .withBasicAuth(elasticsearchUsername,elasticsearchPassword);
        }

        ClientConfiguration clientConfiguration = builderWithProtocol
                .withConnectTimeout(10000)
                .withSocketTimeout(10000)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}

