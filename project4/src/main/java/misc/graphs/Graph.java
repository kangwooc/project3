package misc.graphs;

import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.ISet;
import misc.Searcher;
import misc.exceptions.NoPathExistsException;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops,
 * parallel edges, and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a
 * graph. We stick with supporting just a few, core set of operations needed for
 * the remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated then usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    // any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've contrained Graph
    // so that E must always be an instance of Edge<V> AND Comparable<E>.
    //
    // What this means is that if you have an object of type E, you can use
    // any of the methods from both the Edge interface and from the Comparable
    // interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.
    //private IDictionary<V, IList<V>> graph;
    private IDictionary<V, IList<E>> edgesGraph;
    private IList<E> edges;
    private IList<V> vertices;

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException
     *             if any of the edges have a negative weight
     * @throws IllegalArgumentException
     *             if one of the edges connects to a vertex not present in the
     *             'vertices' list
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        this.edgesGraph = new ChainedHashDictionary<>();
        this.edges = edges;
        this.vertices = vertices;
        for (E edge : this.edges) {
            if (edge.getWeight() < 0 || !this.vertices.contains(edge.getVertex1())
                    || !this.vertices.contains(edge.getVertex2())) {
                throw new IllegalArgumentException();
            }
            if (!this.edgesGraph.containsKey(edge.getVertex1())) {
                this.edgesGraph.put(edge.getVertex1(), new DoubleLinkedList<>());
            }
            if (!this.edgesGraph.containsKey(edge.getVertex2())) {
                this.edgesGraph.put(edge.getVertex2(), new DoubleLinkedList<>());
            }
            this.edgesGraph.get(edge.getVertex1()).insert(0, edge);
            this.edgesGraph.get(edge.getVertex2()).insert(0, edge);
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return this.vertices.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return this.edges.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of this
     * graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        ISet<E> mst = new ChainedHashSet<>();
        IDisjointSet<V> temp = new ArrayDisjointSet<>();
        for (V vertex : this.vertices) {
            temp.makeSet(vertex);
        }
        IList<E> sorted = Searcher.topKSort(this.edges.size(), this.edges);
        for (E edge : sorted) {
            if (temp.findSet(edge.getVertex1()) != temp.findSet(edge.getVertex2())) {
                temp.union(edge.getVertex1(), edge.getVertex2());
                mst.add(edge);
            }
        }
        return mst;
    }

    /**
     * Returns the edges that make up the shortest path from the start to the end.
     *
     * The first edge in the output list should be the edge leading out of the
     * starting node; the last edge in the output list should be the edge connecting
     * to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException
     *             if there does not exist a path from the start to the end
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        IDictionary<V, E> backpointers = new ChainedHashDictionary<>();
        IList<E> shortestPath = new DoubleLinkedList<>();
        IDictionary<V, VerticeDist<V>> costs = new ChainedHashDictionary<>();
        ISet<V> visited = new ChainedHashSet<>();
        IPriorityQueue<VerticeDist<V>> minHeapDist = new ArrayHeap<>();

        if (start.equals(end)) {
            return shortestPath;
        }
        for (V vertex : this.vertices) {
            VerticeDist<V> ver = new VerticeDist<>(vertex, Double.POSITIVE_INFINITY);
            if (vertex.equals(start)) {
                ver.setDistance(0.0);
            }
            minHeapDist.insert(ver);
            costs.put(vertex, ver);
        }
        while (!minHeapDist.isEmpty()) {
            VerticeDist<V> current = minHeapDist.removeMin();
            V currentVertex = current.getVertex();
            if (!visited.contains(currentVertex)) {
                visited.add(currentVertex);
            }
            if (end.equals(currentVertex)) {
                while (!start.equals(currentVertex)) {
                    E edge = backpointers.get(currentVertex);
                    shortestPath.insert(0, edge);
                    currentVertex = edge.getOtherVertex(currentVertex);
                }
                return shortestPath;
            }
            if (current.getDistance() == Double.POSITIVE_INFINITY ||
                    !this.edgesGraph.containsKey(currentVertex)) {
                throw new NoPathExistsException();
            } else {
                for (E edge : this.edgesGraph.get(currentVertex)) {
                    V neighborVertex = edge.getOtherVertex(currentVertex);
                    VerticeDist<V> neighbor = costs.get(neighborVertex);
                    if (!visited.contains(neighborVertex)) {
                        Double newCost = neighbor.getDistance();
                        Double newDistance = current.getDistance() + edge.getWeight();
                        if (newDistance.compareTo(newCost) < 0) {
                            neighbor.setDistance(newDistance);
                            costs.put(neighborVertex, neighbor);
                            minHeapDist.insert(neighbor);
                            backpointers.put(neighborVertex, edge);
                        }
                    }
                }
            }
        }
        throw new NoPathExistsException();
    }

    private static class VerticeDist<V> implements Comparable<VerticeDist<V>> {
        private Double dist;
        private V vertice;

        public VerticeDist(V vertice, Double dist) {
            this.dist = dist;
            this.vertice = vertice;
        }

        public V getVertex() {
            return this.vertice;
        }

        public double getDistance() {
            return this.dist;
        }

        public void setDistance(double distance) {
            this.dist = distance;
        }

        @Override
        public int compareTo(VerticeDist<V> o) {
            return (int) (this.getDistance() - o.getDistance());
        }
    }
}