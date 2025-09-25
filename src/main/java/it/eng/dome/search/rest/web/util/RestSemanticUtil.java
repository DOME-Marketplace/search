package it.eng.dome.search.rest.web.util;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Optional;

import javax.net.ssl.SSLContext;

@Component
public class RestSemanticUtil {

	@Value("${dome.classify.url}")
	private String classifyUrl;

	@Value("${dome.analyze.url}")
	private String analyzeUrl;

	private static final Logger log = LoggerFactory.getLogger(RestSemanticUtil.class);
	private static RestTemplate restTemplate = new RestTemplate();

	public RestSemanticUtil() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
		// Configura il contesto SSL per accettare tutti i certificati
		SSLContext sslContext = SSLContextBuilder
				.create()
				.loadTrustMaterial(new TrustSelfSignedStrategy())
				.build();

		// Crea il client HTTP con il contesto SSL configurato
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLContext(sslContext)
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
				.build();

		// Configura RestTemplate per utilizzare il client HTTP personalizzato
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		restTemplate = new RestTemplate(factory);
	}

	public String classifyText(String contentToClassify) {

		String url = classifyUrl;
		log.info("Call classifyText to URL {}", url);

		// Crea le intestazioni della richiesta
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		headers.setAccept(Collections.singletonList(MediaType.ALL));

		// Crea il corpo della richiesta
		HttpEntity<String> entity = new HttpEntity<>(contentToClassify, headers);

		try {
			// Invia la richiesta POST
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

			// Ottieni il corpo della risposta
			String result = response.getBody();
			log.info("Response for classifyText with status: {} - {}", Optional.of(response.getStatusCode().value()), response.getStatusCode().name());
			// log.debug("Response body: {}", result);
			return result;
		} catch (HttpStatusCodeException exception) {
			log.error("Error for classifyText with status: {} - {}", Optional.of(exception.getStatusCode().value()), exception.getStatusCode().name());
			return null;
		} catch (ResourceAccessException e) {
			log.error("Error for classifyText. Caught ResourceAccessException: {}", e.getMessage());
			return null;
		} catch (Exception e) {
			log.error("Error for classifyText. Caught general exception: {}", e.getMessage());
			return null;
		}
	}

	public String analyzeText(String contentToAnalyze) {

		String url = analyzeUrl;
		log.info("Call analyzeText to URL {}", url);

		// Crea le intestazioni della richiesta
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		headers.setAccept(Collections.singletonList(MediaType.ALL));

		// Crea il corpo della richiesta
		HttpEntity<String> entity = new HttpEntity<>(contentToAnalyze, headers);

		// Invia la richiesta POST
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			// Ottieni il corpo della risposta

			String result = response.getBody();
			log.info("Response for analyzeText with status: {} - {}", Optional.of(response.getStatusCode().value()), response.getStatusCode().name());
			// log.debug("Response body: {}", result);
			return result;
		} catch (HttpStatusCodeException exception) {
			log.error("Error for analyzeText with status: {} - {}", Optional.of(exception.getStatusCode().value()),
					exception.getStatusCode().name());
			return null;
		} catch (ResourceAccessException e) {
			log.error("Caught ResourceAccessException: {}", e.getMessage());
			return null;
		} catch (Exception e) {
			log.error("Caught general exception: {}", e.getMessage());
			return null;
		}
	}
}
