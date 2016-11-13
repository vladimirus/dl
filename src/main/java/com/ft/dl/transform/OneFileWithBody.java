package com.ft.dl.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.dl.converter.ContentConverter;
import com.ft.dl.service.FileManager;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.DocumentPreprocessor;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.jsoup.Jsoup.parseBodyFragment;
import static rx.Observable.from;

public class OneFileWithBody {
    private FileManager fileManager;
    private ContentConverter contentConverter;

    public OneFileWithBody(FileManager fileManager, ContentConverter contentConverter) {
        this.fileManager = fileManager;
        this.contentConverter = contentConverter;
    }

    public static void main(String[] args) {
        FileManager fileManager = new FileManager(Paths.get("/media/vov/Untitled/content"));
        ContentConverter contentConverter = new ContentConverter(new ObjectMapper());
        new OneFileWithBody(fileManager, contentConverter).transform();
    }

    public void transform() {
        Path file = Paths.get("/home/vov/Downloads/all-body.txt");
        from(() -> fileManager.walk().iterator())
                .filter(p -> !Files.isDirectory(p))
                .map(p -> contentConverter.enrichedContent(fileManager.read(p)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(e -> parseBodyFragment(e.getBodyXML()).body().text())
                .flatMap(p -> from(new DocumentPreprocessor(new StringReader(p))))
                .map(Sentence::listToString)
                .subscribe(s -> fileManager.append(s + '\n', file));
    }
}
