package it.eng.dome.search.rest.web.util;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import javax.net.ssl.SSLContext;

@Component
public class RestSemanticUtil {

	private static final Logger log = LoggerFactory.getLogger(RestSemanticUtil.class);
	private static RestTemplate restTemplate = new RestTemplate();

	private final String classifyUrl = "https://deployenv6.expertcustomers.ai:8086/services/dome/classify"; //to change
	private final String analyzeUrl = "https://deployenv6.expertcustomers.ai:8086/services/dome/analyze";


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
		this.restTemplate = new RestTemplate(factory);
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

		// Aggiungi log per il corpo della richiesta
		log.info("Request body: {}", contentToClassify);
		log.info("Request headers: {}", headers);

		try {
			// Invia la richiesta POST
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

			// Ottieni il corpo della risposta
			String result = response.getBody();
			log.debug("Response for classifyText with status code {}", response.getStatusCode().name());
			log.debug("Response body: {}", result);
			return result;
		} catch (Exception e) {
			log.error("Error during classifyText request", e);
			throw e;
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
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		// Ottieni il corpo della risposta
		String result = response.getBody();
		log.info("Response for analyzeText with status code {}", response.getStatusCode().name());
		return result;
	}

}