package mazes.generators.maze;

import datastructures.concrete.ChainedHashSet;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Wall;

import java.util.Random;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        // Note: make sure that the input maze remains unmodified after this method is
        // over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.
        // KRUSKAL(G):
        // 1 A = ∅
        // 2 foreach v ∈ G.V:
        // 3 MAKE-SET(v)
        // 4 foreach (u, v) in G.E ordered by weight(u, v), increasing:
        // 5 if FIND-SET(u) ≠ FIND-SET(v):
        // 6 A = A ∪ {(u, v)}
        // 7 UNION(u, v)
        // 8 return A
        Random rand = new Random();
        
        ISet<Wall> toRemove = new ChainedHashSet<>();
        for (Wall wall : maze.getWalls()) {
            
            toRemove.add(wall);
        }
        return toRemove;
    }
}
