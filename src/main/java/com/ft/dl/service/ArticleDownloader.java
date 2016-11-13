package com.ft.dl.service;

import com.ft.apiingester.model.api.content.enriched.EnrichedContent;
import rx.Observable;

import java.time.LocalDateTime;

public interface ArticleDownloader {
    Observable<EnrichedContent> download(LocalDateTime from);
}
