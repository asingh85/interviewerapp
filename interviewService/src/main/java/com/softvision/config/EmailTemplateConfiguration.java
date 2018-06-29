package com.softvision.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import com.softvision.constant.ServiceConstants;

/**
 * @author arun.p
 *
 */
@Configuration
public class EmailTemplateConfiguration {

	/**
	 * Email message source.
	 *
	 * @return the resource bundle message source
	 */
	@Bean
	public ResourceBundleMessageSource emailMessageSource() {
		final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename(ServiceConstants.MAIL_MAILMESSEGES);
		return messageSource;
	}

	/**
	 * Email template engine.
	 *
	 * @return the template engine
	 */
	@Bean
	public TemplateEngine emailTemplateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		// Resolver for TEXT emails
		templateEngine.addTemplateResolver(textTemplateResolver());
		// Resolver for HTML emails (except the editable one)
		templateEngine.addTemplateResolver(htmlTemplateResolver());
		// Resolver for HTML editable emails (which will be treated as a String)
		templateEngine.addTemplateResolver(stringTemplateResolver());
		// Message source, internationalization specific to emails
		templateEngine.setTemplateEngineMessageSource(emailMessageSource());
		return templateEngine;
	}

	/**
	 * Text template resolver.
	 *
	 * @return the i template resolver
	 */
	private ITemplateResolver textTemplateResolver() {
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setOrder(Integer.valueOf(1));
		templateResolver.setResolvablePatterns(
				Collections.singleton(ServiceConstants.TEXT + ServiceConstants.BACK_SLASH + ServiceConstants.STAR));
		templateResolver.setPrefix(ServiceConstants.BACK_SLASH);
		templateResolver.setSuffix(ServiceConstants.TXT);
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		templateResolver.setCharacterEncoding(ServiceConstants.ENCODING_UTF_EIGHT);
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	/**
	 * Html template resolver.
	 *
	 * @return the i template resolver
	 */
	private ITemplateResolver htmlTemplateResolver() {
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setOrder(Integer.valueOf(2));
		templateResolver.setResolvablePatterns(
				Collections.singleton(ServiceConstants.HTML + ServiceConstants.BACK_SLASH + ServiceConstants.STAR));
		templateResolver.setPrefix(ServiceConstants.BACK_SLASH);
		templateResolver.setSuffix(ServiceConstants.DOT + ServiceConstants.HTML);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding(ServiceConstants.ENCODING_UTF_EIGHT);
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	/**
	 * String template resolver.
	 *
	 * @return the i template resolver
	 */
	private ITemplateResolver stringTemplateResolver() {
		final StringTemplateResolver templateResolver = new StringTemplateResolver();
		templateResolver.setOrder(Integer.valueOf(3));
		// No resolvable pattern, will simply process as a String template everything
		// not previously matched
		templateResolver.setTemplateMode(ServiceConstants.HTML_FIVE);
		templateResolver.setCacheable(false);
		return templateResolver;
	}

}
