package de.tomreno.assessment.fullstack.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * Serializing PageImpl instances as-is is not supported, meaning that there is no guarantee about the stability of the
 * resulting JSON structure! For a stable JSON structure, Spring Data's PagedModel is used globally via
 * @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)).
 */
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class JacksonConfig {
}
