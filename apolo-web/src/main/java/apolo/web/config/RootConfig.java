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

	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		String resourcesLocationArgument = System.getProperty("appconfig");
		
		if (resourcesLocationArgument == null || "null".equals(resourcesLocationArgument)) {
			resourcesLocationArgument = "";
		}

		getEnviromentVariable(resourcesLocationArgument);

		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		
		ClassPathResource applicationConfigLocation = new ClassPathResource(resourcesLocationArgument + "/config.properties");
		ClassPathResource persistenceConfigLocation = new ClassPathResource(resourcesLocationArgument + "/persistence.properties");

		ClassPathResource[] resourcesLocation = new ClassPathResource[2];
		resourcesLocation[0] = applicationConfigLocation;
		resourcesLocation[1] = persistenceConfigLocation;
		
		ppc.setLocations(resourcesLocation);
		return ppc;
	}

	private static void getEnviromentVariable(String resourcesLocationArgument) {
		log.info("*************** Env Variables *************** ");

		if (System.getenv("APOLO_UPLOADED_FILES") != null) {
			try {
				Properties config = new Properties();
				config.setProperty("uploadedfiles.path"               , System.getenv("APOLO_UPLOADED_FILES"));
				config.setProperty("video.converter.executable.path"  , System.getenv("APOLO_VIDEO_CONVERTER_EXECUTABLE_PATH"));
				config.setProperty("pdfimageextractor.executable.path", System.getenv("APOLO_PDF_IMAGE_EXTRACTOR_EXECUTABLE_PATH"));

				log.info("uploadedfiles.path:                " + config.getProperty("uploadedfiles.path"));
				log.info("video.converter.executable.path:   " + config.getProperty("video.converter.executable.path"));
				log.info("pdfimageextractor.executable.path: " + config.getProperty("pdfimageextractor.executable.path"));

				File configFile = new File(String.valueOf(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcesLocationArgument + "/config.properties")));
				FileOutputStream configFOS = new FileOutputStream(configFile);

				config.store(configFOS, "stored by application with enviroment variables values");
			} catch (FileNotFoundException e) {
				log.error(e.getMessage(), e);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}

		if (System.getenv("APOLO_DATASOURCE_DRIVER_CLASS") != null
				|| System.getenv("APOLO_HIBERNATE_DIALECT") != null) {
			try {
				Properties persistence = new Properties();
				persistence.setProperty("dataSource.driverClassName"   , System.getenv("APOLO_DATASOURCE_DRIVER_CLASS"));
				persistence.setProperty("hibernate.dialect"            , System.getenv("APOLO_HIBERNATE_DIALECT"));
				persistence.setProperty("hibernate.hbm2ddl.auto"       , System.getenv("APOLO_HIBERNATE_HBM2DDL"));
				persistence.setProperty("dataSource.url"               , System.getenv("APOLO_DATASOURCE_URL"));
				persistence.setProperty("dataSource.username"          , System.getenv("APOLO_DATASOURCE_USERNAME"));
				persistence.setProperty("dataSource.password"          , System.getenv("APOLO_DATASOURCE_PASSWORD"));
				persistence.setProperty("hibernate.show.and.format.sql", System.getenv("APOLO_HIBERNATE_SHOW_AND_FORMAT_SQL"));

				log.info("dataSource.driverClassName:    " + persistence.getProperty("dataSource.driverClassName"));
				log.info("hibernate.dialect:             " + persistence.getProperty("hibernate.dialect"));
				log.info("hibernate.hbm2ddl.auto:        " + persistence.getProperty("hibernate.hbm2ddl.auto"));
				log.info("dataSource.url:                " + persistence.getProperty("dataSource.url"));
				log.info("dataSource.username:           " + persistence.getProperty("dataSource.username"));
				log.info("dataSource.password:           ***");
				log.info("hibernate.show.and.format.sql: " + persistence.getProperty("hibernate.show.and.format.sql"));

				File persistenceFile = new File(String.valueOf(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcesLocationArgument + "/persistence.properties")));
				FileOutputStream persistenceFOS = new FileOutputStream(persistenceFile);

				persistence.store(persistenceFOS, "stored by application with enviroment variables values");
			} catch (FileNotFoundException e) {
				log.error(e.getMessage(), e);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		log.info("*************** END Env Variables *************** ");
	}

//	@Bean
//	public UrlBasedViewResolver setupViewResolver() {
//		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
//		resolver.setPrefix("/WEB-INF/views/");
//		resolver.setSuffix(".jsp");
//		resolver.setViewClass(JstlView.class);
//		return resolver;
//	}

}