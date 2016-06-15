package apolo.api.config;

import apolo.common.config.model.ApplicationProperties;
import apolo.common.config.enums.EnviromentVariables;
import apolo.common.config.enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.File;
import java.util.List;

@Configuration
@EnableWebMvc
@Import({ ThymeleafConfig.class })
@ComponentScan(basePackages = { "apolo" })
public class WebMvcConfig extends WebMvcConfigurationSupport {

	private static final Logger log = LoggerFactory.getLogger(WebMvcConfig.class);

	public WebMvcConfig() {
		super();
	}

	private static final String MESSAGE_SOURCE = "/WEB-INF/classes/messages";
	private static final String VERSION_SOURCE = "/WEB-INF/classes/version";

	private static final String RESOURCES_LOCATION = "/resources/";
	private static final String RESOURCES_HANDLER = RESOURCES_LOCATION + "**";

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(500000000);
		return multipartResolver;
	}

    @Bean
    public ApplicationProperties applicationProperties() {
    	ApplicationProperties result = new ApplicationProperties();

		log.info("*************** Env Variables CONFIG FILE *************** ");

		String defaultTenant = System.getenv(EnviromentVariables.DEFAULT_TENANT.getCode());
		String uploadedFilesPath = System.getenv(EnviromentVariables.UPLOADED_FILES.getCode());
		String emailFrom = System.getenv(EnviromentVariables.DEFAULT_emailFrom.getCode());
		String emailUsername = System.getenv(EnviromentVariables.DEFAULT_emailUsername.getCode());
		String emailPassword = System.getenv(EnviromentVariables.DEFAULT_emailPassword.getCode());
		String smtpHost = System.getenv(EnviromentVariables.DEFAULT_smtpHost.getCode());
		String smtpPort = System.getenv(EnviromentVariables.DEFAULT_smtpPort.getCode());
		String useTLS = System.getenv(EnviromentVariables.DEFAULT_useTLS.getCode());
		String googleAdClient = System.getenv(EnviromentVariables.DEFAULT_GOOGLE_ADCLIENT.getCode());
		String googleAdSlotOne = System.getenv(EnviromentVariables.DEFAULT_GOOGLE_ADSLOT_ONE.getCode());
		String googleAdSlotTwo = System.getenv(EnviromentVariables.DEFAULT_GOOGLE_ADSLOT_TWO.getCode());
		String googleAdSlotThree = System.getenv(EnviromentVariables.DEFAULT_GOOGLE_ADSLOT_THREE.getCode());
		String googleAnalyticsUserAccount = System.getenv(EnviromentVariables.DEFAULT_GOOGLE_ANALYTICS_USER_ACCOUNT.getCode());
		String sendAuthEmail = System.getenv(EnviromentVariables.SEND_AUTH_EMAIL.getCode());

		String facebookAppId = System.getenv(EnviromentVariables.FACEBOOK_APP_ID.getCode());
		String facebookAppSecret = System.getenv(EnviromentVariables.FACEBOOK_APP_SECRET.getCode());

		String secretKey = System.getenv(EnviromentVariables.SECRET_KEY.getCode());
		String ivKey = System.getenv(EnviromentVariables.IV_KEY.getCode());

		log.info("uploadedfiles.path:                 " + uploadedFilesPath);
		log.info("default.tenant:                     " + defaultTenant);

		log.info("default.emailFrom:                  " + emailFrom);
		log.info("default.emailUsername:              " + emailUsername);
		log.info("default.emailPassword:              " + emailPassword);
		log.info("default.smtpHost:                   " + smtpHost);
		log.info("default.smtpPort:                   " + smtpPort);
		log.info("default.useTLS:                     " + useTLS);

		log.info("default.googleAdClient:             " + googleAdClient);
		log.info("default.googleAdSlotOne:            " + googleAdSlotOne);
		log.info("default.googleAdSlotTwo:            " + googleAdSlotTwo);
		log.info("default.googleAdSlotThree:          " + googleAdSlotThree);
		log.info("default.googleAnalyticsUserAccount: " + googleAnalyticsUserAccount);
		log.info("sendAuthEmail:                      " + sendAuthEmail);

		log.info("secretKey:                          " + secretKey);
		log.info("ivKey:                              " + ivKey);

		log.info("facebookAppId:                      " + facebookAppId);
		log.info("facebookAppSecret:                  " + facebookAppSecret);


    	result.setDefaultTenant(defaultTenant);
    	result.setUploadedFilesPath(uploadedFilesPath);

		result.setEmailFrom(emailFrom);
		result.setEmailUsername(emailUsername);
		result.setEmailPassword(emailPassword);
		result.setSmtpHost(smtpHost);
		result.setSmtpPort(smtpPort);
		result.setUseTLS("true".equals(useTLS) ? true : false);

		result.setGoogleAdClient(googleAdClient);
		result.setGoogleAdSlotOne(googleAdSlotOne);
		result.setGoogleAdSlotTwo(googleAdSlotTwo);
		result.setGoogleAdSlotThree(googleAdSlotThree);
		result.setGoogleAnalyticsUserAccount(googleAnalyticsUserAccount);
		result.setSendAuthEmail("true".equals(sendAuthEmail) ? true : false);

		result.setSecretKey(secretKey);
		result.setIvKey(ivKey);

		result.setFacebookAppId(facebookAppId);
		result.setFacebookAppSecret(facebookAppSecret);

		log.info("*************** END Env Variables CONFIG FILE *************** ");

    	return result;
    }

	@Override
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping requestMappingHandlerMapping = super.requestMappingHandlerMapping();
		requestMappingHandlerMapping.setUseSuffixPatternMatch(false);
		return requestMappingHandlerMapping;
	}

	@Bean(name = "messageSource")
	public MessageSource configureMessageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames(MESSAGE_SOURCE, VERSION_SOURCE);
		messageSource.setCacheSeconds(5);
		return messageSource;
	}

    @Bean(name = "localeResolver")
    public LocaleResolver getLocaleResolver() {
        CookieLocaleResolver ret = new CookieLocaleResolver();
        ret.setDefaultLocale(Language.BR.getLocale());
        return ret;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor localeChangeInterceptor=new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
	   registry.addInterceptor(localeChangeInterceptor());
	}

	@Override
	public Validator getValidator() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(configureMessageSource());
		return validator;
	}

	@Bean
	@Override
	public HandlerMapping resourceHandlerMapping() {
		return super.resourceHandlerMapping();
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(RESOURCES_HANDLER).addResourceLocations(RESOURCES_LOCATION).setCachePeriod(175316);

		isValidFilesLocation();
	}

	private boolean isValidFilesLocation() {
		boolean result = false;

		try {
			String uploadedFilesPath = System.getenv(EnviromentVariables.UPLOADED_FILES.getCode());

			File dir = new File(uploadedFilesPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			result = dir.exists();

			log.info("*************** Files Location *************** " + dir.getAbsolutePath());

		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return result;
	}

	@Override
	protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new UserDetailsHandlerMethodArgumentResolver());
	}

	private static class UserDetailsHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

		public boolean supportsParameter(MethodParameter parameter) {
			return UserDetails.class.isAssignableFrom(parameter.getParameterType());
		}

		public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
			Authentication auth = (Authentication) webRequest.getUserPrincipal();
			return auth != null && auth.getPrincipal() instanceof UserDetails ? auth.getPrincipal() : null;
		}
	}

	@Bean
	public FormattingConversionService conversionService() {

		// Use the DefaultFormattingConversionService but do not register defaults
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

		// Ensure @NumberFormat is still supported
		conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());

		// Register date conversion with a specific global format
		DateFormatterRegistrar invertedDate = new DateFormatterRegistrar();
		invertedDate.setFormatter(new DateFormatter("yyyyMMdd"));
		invertedDate.registerFormatters(conversionService);

		DateFormatterRegistrar sqlDateFormat = new DateFormatterRegistrar();
		sqlDateFormat.setFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss.S"));
		sqlDateFormat.registerFormatters(conversionService);

		return conversionService;
	}
}
