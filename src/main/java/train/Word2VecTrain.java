package train;

import lombok.SneakyThrows;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Word2VecTrain {

    public static void main(String[] args) {

        Word2VecTrain word2Vec = new Word2VecTrain();

        word2Vec.train(Paths.get("/home/vov/Downloads/all-body.txt"));

    }

    @SneakyThrows
    private Word2Vec train(Path path) {
        SentenceIterator it = new LineSentenceIterator(path.toFile());
        it.setPreProcessor(String::toLowerCase);

        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(it)
                .tokenizerFactory(t)
                .build();

        vec.fit();

        System.out.println(vec.wordsNearest("putin", 10));

        WordVectorSerializer.writeWordVectors(vec, "/home/vov/Downloads/all-body-model.txt");
//        WordVectorSerializer.loadTxtVectors();
        return vec;
    }
}
