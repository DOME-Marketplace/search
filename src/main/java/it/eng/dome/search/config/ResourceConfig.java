package it.eng.dome.search.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class ResourceConfig extends WebMvcConfigurerAdapter{

	@Override
  	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.
			addResourceHandler("/swagger-ui.html")
			//.addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
		   .addResourceLocations("classpath:/META-INF/resources/");
			//.resourceChain(false);

		registry.addResourceHandler("/webjars/**")
		  .addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

/*	@Override
	public void addViewControllers (ViewControllerRegistry registry) {
		registry.addViewController("/swagger-ui/").setViewName("forward:" + "/swagger-ui/index.html");
	}
*/
}