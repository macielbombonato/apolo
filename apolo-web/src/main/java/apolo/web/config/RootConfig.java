package apolo.web.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "apolo" })
@Import({ PersistenceConfig.class, SecurityConfig.class })
public class RootConfig {
	
	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		String resourcesLocationArgument = System.getProperty("appconfig");
		
		if (resourcesLocationArgument == null || "null".equals(resourcesLocationArgument)) {
			resourcesLocationArgument = "";
		}
		
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		
		ClassPathResource applicationConfigLocation = new ClassPathResource(resourcesLocationArgument + "/config.properties");
		ClassPathResource persistenceConfigLocation = new ClassPathResource(resourcesLocationArgument + "/persistence.properties");
		
		ClassPathResource[] resourcesLocation = new ClassPathResource[2];
		resourcesLocation[0] = applicationConfigLocation;
		resourcesLocation[1] = persistenceConfigLocation;
		
		ppc.setLocations(resourcesLocation);
		return ppc;
	}
	
}