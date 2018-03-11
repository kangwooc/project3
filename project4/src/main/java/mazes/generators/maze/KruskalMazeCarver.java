package mazes.generators.maze;

import datastructures.concrete.ChainedHashSet;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import misc.graphs.Graph;

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
        Random rand = new Random();
        for (Wall wall : maze.getWalls()) {
            wall.setDistance(rand.nextDouble());
        }
        Graph<Room, Wall> mazeGraph = new Graph<>(maze.getRooms(), maze.getWalls());
        ISet<Wall> wallMST = mazeGraph.findMinimumSpanningTree();
        ISet<Wall> toRemove = new ChainedHashSet<>();
        for (Wall wall : maze.getWalls()) {
            if (wallMST.contains(wall) && !maze.getUntouchableWalls().contains(wall)) {
                toRemove.add(wall);
            }
        }
        return toRemove;
    }
}