package it.eng.dome.search.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import it.eng.dome.search.model.InfoSearch;


@Component
public class StartupListener implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger logger = LoggerFactory.getLogger(StartupListener.class);
	
	// RegEx to get the placeholder ${ENV_VAR} and extract the default value (if any)
    private static final Pattern PLACEHOLDER = Pattern.compile("\\$\\{([^:}]+)(?::([^}]*))?}");

	private final String INFO_PATH = "/search/info";
	private static RestTemplate restTemplate = new RestTemplate();

	@Value("${server.port}")
	private int serverPort;

	@Value("${server.servlet.context-path}")
	private String contextPath;


	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {

		String path = contextPath + INFO_PATH;
		String url = "http://localhost:" + serverPort + path.replaceAll("//+", "/");

		logger.info("Listener GET call to {}", url);
		try {
			InfoSearch response = restTemplate.getForObject(url, InfoSearch.class);
			logger.info("Started the {} version: {} ", response.getName(), response.getVersion());

		} catch (Exception e) {
			logger.error("Error calling {}: {}", url, e.getMessage());
		}
	}

	@Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableEnvironment env = event.getApplicationContext().getEnvironment();

        List<PropertySource<?>> yamlSources = new ArrayList<PropertySource<?>>();
        env.getPropertySources().forEach(ps -> {
        	 // filter -> verify only in the application.yaml file
            if (ps.getName().contains("application.")) {
                yamlSources.add(ps);
            }
        });

        // find all env_var that they get placeholder ${…}
        Map<String, String> vars = new ConcurrentHashMap<String, String>(); // to get order by key
                
        for (PropertySource<?> ps : yamlSources) {
            if (ps instanceof EnumerablePropertySource) {
                EnumerablePropertySource<?> eps = (EnumerablePropertySource<?>) ps;
                for (String key : eps.getPropertyNames()) {
                    Object raw = eps.getProperty(key);
                    if (raw != null) {
                        Matcher m = PLACEHOLDER.matcher(raw.toString());
                        while (m.find()) {
                            // get env_key names removing ${}
                            String env_key = m.group(1);
                            String env_value = env.getProperty(env_key);
                            if (env_value == null) {
                                env_value = m.group(2) + " (<DEFAULT>)";
                            }
                            vars.put(env_key, env_value);
                        }
                    }
                }
            }
        }

        logger.debug("Displaying ENV VARs for Search:");
        for (Map.Entry<String, String> entry : vars.entrySet()) {
            logger.debug("- {} = {}", entry.getKey(), entry.getValue());
        }
    }
}
