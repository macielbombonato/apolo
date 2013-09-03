package br.apolo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.annotation.AnnotationMetadataExtractor;

import br.apolo.data.enums.UserPermission;

public class EnumAnnotationMetadataExtractor implements
		AnnotationMetadataExtractor<SecuredEnum> {

	public Collection<? extends ConfigAttribute> extractAttributes(final SecuredEnum securityAnnotation) {
		final UserPermission[] attributeTokens = securityAnnotation.value();
		final List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>(attributeTokens.length);

		for (final UserPermission token : attributeTokens) {
			attributes.add(new SecurityConfig(token.getAttribute()));
		}
		
		// Administrators can access all operations of the application
		attributes.add(new SecurityConfig(UserPermission.ADMIN.getAttribute()));

		return attributes;
	}

}
