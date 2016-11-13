package com.ft.dl.service;

import com.ft.apiingester.model.api.content.enriched.EnrichedContent;

import java.nio.file.Path;
import java.util.Optional;

public interface ContentManager {
    Optional<Path> save(EnrichedContent enrichedContent);
}
