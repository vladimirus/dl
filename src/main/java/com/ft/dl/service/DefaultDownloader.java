package com.ft.dl.service;

import static com.ft.apiingester.converter.TimeUtil.dateToString;
import static com.ft.apiingester.model.api.content.IdUtils.idFromUrl;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static rx.Observable.from;
import static rx.schedulers.Schedulers.computation;

import com.ft.apiingester.connector.ContentConnector;
import com.ft.apiingester.converter.TimeUtil;
import com.ft.apiingester.model.api.content.enriched.EnrichedContent;
import com.ft.apiingester.model.api.content.notification.Link;
import com.ft.apiingester.model.api.content.notification.NotificationFeed;
import com.ft.dl.converter.ContentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

public class DefaultDownloader implements ArticleDownloader {
    private final Logger LOGGER = LoggerFactory.getLogger(DefaultDownloader.class);
    private PublishSubject<LocalDateTime> subjectJob;
    private ContentConnector contentConnector;
    private ContentConverter contentConverter;

    public DefaultDownloader(ContentConnector contentConnector, ContentConverter contentConverter) {
        this.contentConnector = contentConnector;
        this.contentConverter = contentConverter;
        this.subjectJob = PublishSubject.create();
    }

    @Override
    public Observable<EnrichedContent> download(LocalDateTime from) {
        return subjectJob.startWith(from)
                .observeOn(computation())
                .subscribeOn(Schedulers.computation())
                .map(date -> retry(() -> contentConnector.getNotifications(dateToString(date), "test"), 1))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(feed -> contentConverter.notificationFeed(feed))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .doOnNext(feed -> nextFeed(feed).ifPresent(d -> subjectJob.onNext(d)))
                .flatMap(feed -> from(feed.getNotifications()))
                .map(notification -> retry(() -> contentConnector.getEnrichedContent(idFromUrl(notification.getApiUrl()), "test"), 1))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(str -> contentConverter.enrichedContent(str))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public Optional<LocalDateTime> nextFeed(NotificationFeed feed) {
        try {
            return feed.getLinks().stream()
                    .filter(link -> "next".equals(link.getRel()))
                    .map(Link::getHref)
                    .map(href -> href.substring(href.indexOf("since=") + "since=".length()))
                    .map(this::unescapeHtmlIfNeeded)
                    .map(TimeUtil::stringToDate)
                    .findAny();
        } catch (Exception e) {
            LOGGER.error("Error", e);
            return empty();
        }
    }

    private String unescapeHtmlIfNeeded(String str) {
        return Optional.of(str)
                .filter(s -> s.contains("%"))
                .map(this::decode)
                .orElse(str);
    }

    private String decode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (Exception e) {
            return s;
        }
    }

    private <T> Optional<T> retry(Supplier<T> supp, Integer count) {
        try {
            return ofNullable(supp.get());
        } catch (Exception e) {
            if (count < 100) {
                return retry(supp, count + 1);
            }
        }
        return empty();
    }

}
