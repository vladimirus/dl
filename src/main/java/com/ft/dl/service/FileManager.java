package com.ft.dl.service;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static com.ft.apiingester.converter.TimeUtil.stringToDate;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public class FileManager {
    private Path workDir;

    public FileManager(Path workDir) {
        this.workDir = workDir;
    }

    public Optional<Path> write(String s, Path file) {
        try {
            return Optional.of(Files.write(file, s.getBytes(), CREATE, TRUNCATE_EXISTING));
        } catch (Exception e) {
            return empty();
        }
    }

    public Optional<Path> append(String s, Path file) {
        try {
            return Optional.of(Files.write(file, s.getBytes(), CREATE, APPEND));
        } catch (Exception e) {
            return empty();
        }
    }

    public Optional<LocalDateTime> date(String date) {
        try {
            return of(stringToDate(date));
        } catch (Exception e) {
            return empty();
        }
    }

    public Path resolve(String name) {
        return workDir.resolve(name);
    }

    public Path createDir(Path dir) {
        try {
            if (!exists(dir)) {
                return createDirectories(dir);
            }
        } catch (Exception ignore) {
            //
        }
        return dir;
    }

    @SneakyThrows
    public String read(Path file) {
        return new String(readAllBytes(file), "UTF-8");
    }

    @SneakyThrows
    public Stream<Path> walk() {
        return Files.walk(workDir);
    }
}
