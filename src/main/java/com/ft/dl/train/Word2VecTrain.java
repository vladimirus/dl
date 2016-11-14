package com.ft.dl.train;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.function.Consumer;

public class Word2VecTrain {

    public static final String MODEL = "/Users/vladimir/Downloads/all-body-model.txt";

    public static void main(String[] args) {

        Word2VecTrain word2Vec = new Word2VecTrain();

        WordVectors vec;
        if (Files.exists(Paths.get(MODEL))) {
            vec = loadModel(Paths.get(MODEL).toFile());
        } else {
            vec = word2Vec.train(Paths.get("/Users/vladimir/Downloads/all-body.txt"));
        }

        word2Vec.listen(p -> System.out.println(vec.wordsNearest(p.getLeft(), p.getRight(), 10)));
    }

    @SneakyThrows
    private static WordVectors loadModel(File file) {
        System.out.println("Loading vectors from a file");
        return WordVectorSerializer.loadTxtVectors(file);
    }

    private void listen(Consumer<Pair<Collection<String>, Collection<String>>> callback) {
        System.out.println("Ready");
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            Collection<String> positive = new ArrayList<>();
            Collection<String> negative = new ArrayList<>();
            Collection<String> current = positive;
            String[] array = scanner.nextLine().split("(?=[-+])|(?<=[-+])");
            for (String str : array) {
                str = str.trim();
                switch (str) {
                    case "+":
                        current = positive;
                        break;
                    case "-":
                        current = negative;
                        break;
                    default:
                        current.add(str);
                        break;
                }
            }
            callback.accept(Pair.of(positive, negative));
        }
    }

    @SneakyThrows
    private Word2Vec train(Path path) {
        SentenceIterator it = new LineSentenceIterator(path.toFile());
        it.setPreProcessor(String::toLowerCase);

        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(5)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(it)
                .tokenizerFactory(t)
                .build();

        vec.fit();

        WordVectorSerializer.writeWordVectors(vec, MODEL);
//
        return vec;
    }
}
