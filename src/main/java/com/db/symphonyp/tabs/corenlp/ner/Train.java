package com.db.symphonyp.tabs.corenlp.ner;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.sequences.SeqClassifierFlags;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public class Train {
    private final String workingFolder;

    private Train() {
        workingFolder = Objects.requireNonNull(Train.class.getClassLoader().getResource(".")).getPath();
    }

    public static void main(String... args) {
        new Train().run();
    }

    private void run() {
        Properties properties = StringUtils.propFileToProperties(Paths.get(workingFolder, "ner.model.properties").toString());
        properties.setProperty("trainFileList", Paths.get(workingFolder, "train").toString());

        SeqClassifierFlags seqClassifierFlags = new SeqClassifierFlags(properties);
        CRFClassifier<CoreMap> coreMapCRFClassifier = new CRFClassifier<>(seqClassifierFlags);
        coreMapCRFClassifier.train();
        coreMapCRFClassifier.serializeClassifier(Paths.get(workingFolder, "ner.model.ser.gz").toString());
    }
}
