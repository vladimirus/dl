package com.ft.dl.converter;

import static com.jayway.jsonpath.Configuration.defaultConfiguration;
import static com.jayway.jsonpath.JsonPath.using;
import static com.jayway.jsonpath.Option.SUPPRESS_EXCEPTIONS;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.apiingester.model.api.content.enriched.EnrichedContent;
import com.ft.apiingester.model.api.content.notification.NotificationFeed;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import java.util.Optional;

public class ContentConverter {
    private final Configuration jsonConfig;
    private ObjectMapper mapper;

    public ContentConverter(ObjectMapper mapper) {
        this.mapper = mapper;
        this.jsonConfig = defaultConfiguration()
                .mappingProvider(new JacksonMappingProvider())
                .addOptions(SUPPRESS_EXCEPTIONS);
    }

    public Optional<NotificationFeed> notificationFeed(String str) {
        try {
            return ofNullable(using(jsonConfig).parse(str).read("$", NotificationFeed.class));
        } catch (Exception e) {
            return empty();
        }
    }

    public Optional<EnrichedContent> enrichedContent(String str) {
        try {
            return ofNullable(using(jsonConfig).parse(str).read("$", EnrichedContent.class));
        } catch (Exception e) {
            return empty();
        }
    }

    public Optional<String> enrichedContent(EnrichedContent content) {
        try {
            return ofNullable(mapper.writeValueAsString(content));
        } catch (Exception e) {
            return empty();
        }
    }
}
