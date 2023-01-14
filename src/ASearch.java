import java.util.HashMap;

//Represents any type of Search
public abstract class ASearch {
  HashMap<Integer, Vertex> pathTracker;

  // EFFECT: Builds the onPath path through the maze after the Search is complete
  void buildPath(HashMap<Integer, Vertex> vertex, Vertex nextVertex) {
    while (vertex.containsKey(nextVertex.vertexHash())) {
      nextVertex.onPath = true;
      nextVertex = vertex.get(nextVertex.vertexHash());
    }
  }
}
