package apolo.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "apolo" })
@Import({ PersistenceConfig.class, SecurityConfig.class, SecurityConfig.class, WebMvcConfig.class })
public class RootConfig {

	private static final Logger log = LoggerFactory.getLogger(RootConfig.class);

	/*
	 * If you need more properties files, uncomment this method and use your files.
	 */
//	@Bean
//	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
//		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
//
//		ClassPathResource applicationConfigLocation = new ClassPathResource("file1.properties");
//		ClassPathResource persistenceConfigLocation = new ClassPathResource("file2.properties");
//
//		ClassPathResource[] resourcesLocation = new ClassPathResource[2];
//		resourcesLocation[0] = applicationConfigLocation;
//		resourcesLocation[1] = persistenceConfigLocation;
//
//		ppc.setLocations(resourcesLocation);
//		return ppc;
//	}

}