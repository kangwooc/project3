package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available
 * webpages. If a webpage has many different links to it, it should have a
 * higher page rank. See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages
     *            A set of all webpages we have parsed.
     * @param decay
     *            Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon
     *            When the difference in page ranks is less then or equal to this
     *            number, stop iterating.
     * @param limit
     *            The maximum number of iterations we spend computing page rank.
     *            This value is meant as a safety valve to prevent us from infinite
     *            looping in case our page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph, in
     * adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these links
     * from your graph: we want the final graph we build to be entirely
     * "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        // get all the uri
        ISet<URI> uri = new ChainedHashSet<>();
        for (Webpage webpage : webpages) {
            uri.add(webpage.getUri());
        }

        // build graph
        IDictionary<URI, ISet<URI>> graph = new ChainedHashDictionary<>();
        for (Webpage webpage : webpages) {
            ISet<URI> value = new ChainedHashSet<>();
            for (URI link : webpage.getLinks()) {
                // valid link
                if (uri.contains(link) && !link.equals(webpage.getUri())) {
                    value.add(link);
                }
            }
            graph.put(webpage.getUri(), value);
        }
        return graph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay
     *            Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon
     *            When the difference in page ranks is less then or equal to this
     *            number, stop iterating.
     * @param limit
     *            The maximum number of iterations we spend computing page rank.
     *            This value is meant as a safety valve to prevent us from infinite
     *            looping in case our page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph, double decay, int limit,
            double epsilon) {
        // Step 1: The initialize step should go here
        IDictionary<URI, Double> firstPageRanks = new ChainedHashDictionary<>();
        for (KVPair<URI, ISet<URI>> pair : graph) {
            firstPageRanks.put(pair.getKey(), 1.0 / graph.size());
        }

        // Step 2: The update step should go here
        IDictionary<URI, Double> newPageRanks = step2(firstPageRanks, graph, decay);
        for (int i = 1; i < limit; i++) {
            // Step 3: the convergence step should go here.
            // Return early if we've converged.
            boolean converge = true;
            for (KVPair<URI, Double> pair : firstPageRanks) {
                double difference = Math.abs(newPageRanks.get(pair.getKey()) - pair.getValue());
                if (difference > epsilon) {
                    converge = false;
                }
            }
            if (converge) {
                return newPageRanks;
            }
            firstPageRanks = newPageRanks;
            newPageRanks = step2(firstPageRanks, graph, decay);
        }
        return newPageRanks;
    }

    private IDictionary<URI, Double> step2(IDictionary<URI, Double> oldPageRanks, IDictionary<URI, ISet<URI>> graph,
            double decay) {
        IDictionary<URI, Double> newPageRanks = new ChainedHashDictionary<>();
        for (KVPair<URI, Double> pair : oldPageRanks) {
            newPageRanks.put(pair.getKey(), 0.0);
        }
        for (KVPair<URI, ISet<URI>> pair : graph) {
            for (KVPair<URI, ISet<URI>> otherPair : graph) {
                if (!otherPair.getKey().equals(pair.getKey())) {
                    if (otherPair.getValue().contains(pair.getKey())) {
                        newPageRanks.put(pair.getKey(), newPageRanks.get(pair.getKey())
                                + decay * (oldPageRanks.get(otherPair.getKey()) / otherPair.getValue().size()));
                    }
                }
            }
            if (pair.getValue().size() == 0) {
                for (KVPair<URI, ISet<URI>> tempPair : graph) {
                    newPageRanks.put(tempPair.getKey(), newPageRanks.get(tempPair.getKey())
                            + decay * (oldPageRanks.get(pair.getKey()) / graph.size()));
                }
            }
            newPageRanks.put(pair.getKey(), newPageRanks.get(pair.getKey()) + (1 - decay) / graph.size());
        }
        return newPageRanks;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     * webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return this.pageRanks.get(pageUri);
    }
}