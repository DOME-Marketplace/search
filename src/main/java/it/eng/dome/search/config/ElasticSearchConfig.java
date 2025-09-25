package it.eng.dome.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;

@Configuration
public class ElasticSearchConfig {

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

    @Bean
    public ElasticsearchClient elasticsearchClient() throws Exception {

        HttpHost httpHost = new HttpHost(
                elasticsearchHost,
                elasticsearchPort,
                usingSsl ? "https" : "http"
        );

        RestClientBuilder builder = RestClient.builder(httpHost);

        // Autenticazione
        if (!elasticsearchUsername.isBlank() && !elasticsearchPassword.isBlank()) {
            final BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    AuthScope.ANY,
                    new UsernamePasswordCredentials(elasticsearchUsername, elasticsearchPassword)
            );

            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                httpClientBuilder.setDefaultCredentialsProvider(credsProvider);

                // SSL senza verifica
                if (usingSsl && !usingSslVerification) {
                    try {
                        SSLContext sslContext = SSLContext.getDefault();
                        httpClientBuilder.setSSLContext(sslContext)
                                .setSSLHostnameVerifier((s, sslSession) -> true);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                return httpClientBuilder;
            });
        }

        RestClient restClient = builder.build();

        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }
}