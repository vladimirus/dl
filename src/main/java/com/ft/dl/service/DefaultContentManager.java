package com.ft.dl.service;

import static com.ft.apiingester.converter.TimeUtil.stringToDate;
import static com.ft.apiingester.model.api.content.IdUtils.idFromUrl;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.exists;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import com.ft.apiingester.model.api.content.enriched.EnrichedContent;
import com.ft.dl.converter.ContentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

public class DefaultContentManager implements ContentManager {
    private final Logger LOGGER = LoggerFactory.getLogger(DefaultContentManager.class);
    private ContentConverter contentConverter;
    private FileManager fileManager;

    public DefaultContentManager(ContentConverter contentConverter, FileManager fileManager) {
        this.contentConverter = contentConverter;
        this.fileManager = fileManager;
    }

    public Optional<Path> save(EnrichedContent enrichedContent) {
        return contentConverter.enrichedContent(enrichedContent)
                .flatMap(s -> {
                    LocalDateTime publishedDate = fileManager.date(enrichedContent.getPublishedDate()).orElseGet(LocalDateTime::now);
                    Path dir = fileManager.resolve(Integer.toString(publishedDate.getYear()))
                            .resolve(Integer.toString(publishedDate.getMonthValue()))
                            .resolve(Integer.toString(publishedDate.getDayOfMonth()));
                    Path file = fileManager.createDir(dir).resolve(idFromUrl(enrichedContent.getId()));
                    return fileManager.write(s, file).map(f -> setLastModifiedTime(publishedDate, f));
                });
    }

    private Path setLastModifiedTime(LocalDateTime publishedDate, Path file) {
        try {
            return Files.setLastModifiedTime(file, FileTime.from(publishedDate.toInstant(ZoneOffset.ofHours(0))));
        } catch (Exception e) {
            return file;
        }
    }
}
