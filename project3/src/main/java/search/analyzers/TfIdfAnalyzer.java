package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.
    private IDictionary<URI, Double> documentNormVector;

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.
        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.documentNormVector = this.computeDocumentNormVector(webpages);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.

    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement
    // exactly
    // these methods: feel free to change or modify these methods however you want.
    // The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found in every single
     * document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Double> termsCount = new ChainedHashDictionary<>();
        IDictionary<String, Double> idf = new ChainedHashDictionary<>();
        for (Webpage page : pages) {
            ISet<String> wordList = new ChainedHashSet<>();
            for (String word : page.getWords()) {
                wordList.add(word);
            }
            for (String word : wordList) {
                if (!termsCount.containsKey(word)) {
                    termsCount.put(word, 1.0);
                } else {
                    termsCount.put(word, termsCount.get(word) + 1.0);
                }
            }
        }
        for (KVPair<String, Double> term : termsCount) {
            idf.put(term.getKey(), Math.log(pages.size() / term.getValue()));
        }
        return idf;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list to
     * their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> tfScores = new ChainedHashDictionary<>();
        for (String word : words) {
            if (!tfScores.containsKey(word)) {
                tfScores.put(word, (1.0 / words.size()));
            } else {
                tfScores.put(word, tfScores.get(word) + (1.0 / words.size()));
            }
        }
        return tfScores;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        IDictionary<URI, IDictionary<String, Double>> result = new ChainedHashDictionary<>();
        for (Webpage page : pages) {
            IDictionary<String, Double> tfIdfVector = computeTfScores(page.getWords());
            for (KVPair<String, Double> pair : idfScores) {
                if (tfIdfVector.containsKey(pair.getKey())) {
                    tfIdfVector.put(pair.getKey(), pair.getValue() * tfIdfVector.get(pair.getKey()));
                    result.put(page.getUri(), tfIdfVector);
                }
            }
        }
        return result;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query
     * and the URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     * webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        // Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        IDictionary<String, Double> documentVector = documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> queryVector = this.queryTfIdfVector(query);

        double numerator = 0.0;
        double denominator = 0.0;

        for (KVPair<String, Double> pair : queryVector) {
            if (documentVector.containsKey(pair.getKey())) {
                numerator += documentVector.get(pair.getKey()) * queryVector.get(pair.getKey());
            }
        }

        denominator = this.documentNormVector.get(pageUri) * norm(queryVector);
        if (denominator != 0) {
            return numerator / denominator;
        }
        return 0.0;
    }

    private double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        for (KVPair<String, Double> pair : vector) {
            double score = pair.getValue();
            output += score * score;
        }
        return Math.sqrt(output);
    }

    private IDictionary<String, Double> queryTfIdfVector(IList<String> query) {
        IDictionary<String, Double> queryVector = new ChainedHashDictionary<>();
        IDictionary<String, Double> queryTfScores = this.computeTfScores(query);
        for (KVPair<String, Double> pair : queryTfScores) {
            double tfIdf = 0.0;
            if (idfScores.containsKey(pair.getKey())) {
                tfIdf = pair.getValue() * idfScores.get(pair.getKey());
            }
            queryVector.put(pair.getKey(), tfIdf);
        }
        return queryVector;
    }

    private IDictionary<URI, Double> computeDocumentNormVector(ISet<Webpage> webpages) {
        IDictionary<URI, Double> normDocumentVector = new ChainedHashDictionary<>();
        for (Webpage page : webpages) {
            normDocumentVector.put(page.getUri(), norm(documentTfIdfVectors.get(page.getUri())));
        }
        return normDocumentVector;
    }

}