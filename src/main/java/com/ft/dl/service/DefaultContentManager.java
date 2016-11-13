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
    private Path workDir;
    private ContentConverter contentConverter;

    public DefaultContentManager(ContentConverter contentConverter) {
        this.contentConverter = contentConverter;
        this.workDir = Paths.get("/Volumes/Untitled/content");
//        this.workDir = Paths.get("/Users/vladimir/Downloads/content");
    }

    public Optional<Path> save(EnrichedContent enrichedContent) {
        return contentConverter.enrichedContent(enrichedContent)
                .flatMap(s -> {
                    LocalDateTime publishedDate = date(enrichedContent.getPublishedDate()).orElseGet(LocalDateTime::now);
                    Path dir = workDir.resolve(Integer.toString(publishedDate.getYear()))
                            .resolve(Integer.toString(publishedDate.getMonthValue()))
                            .resolve(Integer.toString(publishedDate.getDayOfMonth()));
                    Path file = createDir(dir).resolve(idFromUrl(enrichedContent.getId()));
                    return write(s, file).map(f -> setLastModifiedTime(publishedDate, f));
                });
    }

    private Path setLastModifiedTime(LocalDateTime publishedDate, Path file) {
        try {
            return Files.setLastModifiedTime(file, FileTime.from(publishedDate.toInstant(ZoneOffset.ofHours(0))));
        } catch (Exception e) {
            return file;
        }
    }

    private Optional<Path> write(String s, Path file) {
        try {
            return Optional.of(Files.write(file, s.getBytes(), CREATE, TRUNCATE_EXISTING));
        } catch (Exception e) {
            return empty();
        }
    }

    private Optional<LocalDateTime> date(String date) {
        try {
            return of(stringToDate(date));
        } catch (Exception e) {
            return empty();
        }
    }

    private Path createDir(Path dir) {
        try {
            if (!exists(dir)) {
                return createDirectories(dir);
            }
        } catch (Exception ignore) {
            //
        }
        return dir;
    }
}
