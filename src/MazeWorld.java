import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;

// Represents the Maze
public class MazeWorld extends World {
  static final int width = 100;
  static final int height = 60;
  static final int scale = 10;
  ArrayList<Vertex> loVertex;
  ArrayList<Edge> loEdge;
  BFS bfs;
  DFS dfs;
  boolean solveBFS;
  boolean solveDFS;

  // Constructor to initialize and restart a real Maze
  MazeWorld() {
    worldConstructor();
  }

  // Constructor for testing
  MazeWorld(String string) {
    loVertex = new ArrayList<Vertex>();
    loEdge = new ArrayList<Edge>();
  }

  // Used in the main constructor, in method form for restarting purposes
  void worldConstructor() {
    ArrayList<ArrayList<Vertex>> vertices = makeLoVertex();
    ArrayList<Edge> edges = getAdjacentEdges(vertices);
    vertices = assignEdges(vertices);
    loVertex = new ArrayList<Vertex>();
    loEdge = makeLoEdge(vertices, edges);
    for (ArrayList<Vertex> i : vertices) {
      for (Vertex vertex : i) {
        loVertex.add(vertex);
      }
    }
    bfs = new BFS(loVertex);
    dfs = new DFS(loVertex);
    solveBFS = false;
    solveDFS = false;
  }

  // Creates one 2D ArrayList of the vertices and assigns their adjacent edges
  ArrayList<ArrayList<Vertex>> makeLoVertex() {
    ArrayList<ArrayList<Vertex>> loVertex = new ArrayList<ArrayList<Vertex>>();
    for (int x = 0; x < width; x++) {
      ArrayList<Vertex> vertex = new ArrayList<Vertex>();
      for (int y = 0; y < height; y++) {
        vertex.add(new Vertex(x, y));
      }
      loVertex.add(vertex);
    }
    Random r = new Random();
    for (ArrayList<Vertex> i : loVertex) {
      for (Vertex vertex : i) {
        if (vertex.x != 0) {
          vertex.adjacentEdges
                  .add(new Edge(vertex, loVertex.get(vertex.x - 1).get(vertex.y), r.nextInt(1000)));
        }
        if (vertex.x != width - 1) {
          vertex.adjacentEdges
                  .add(new Edge(vertex, loVertex.get(vertex.x + 1).get(vertex.y), r.nextInt(1000)));
        }
        if (vertex.y != 0) {
          vertex.adjacentEdges
                  .add(new Edge(vertex, loVertex.get(vertex.x).get(vertex.y - 1), r.nextInt(1000)));
        }
        if (vertex.y != height - 1) {
          vertex.adjacentEdges
                  .add(new Edge(vertex, loVertex.get(vertex.x).get(vertex.y + 1), r.nextInt(1000)));
        }
      }
    }
    return loVertex;
  }

  // Creates the list of Edges for the maze
  ArrayList<Edge> makeLoEdge(ArrayList<ArrayList<Vertex>> vertices, ArrayList<Edge> edges) {
    ArrayList<Edge> loEdge = new ArrayList<Edge>();
    for (Edge edge1 : edges) {
      boolean real = true;
      for (ArrayList<Vertex> i : vertices) {
        for (Vertex vertex : i) {
          for (Edge edge2 : vertex.adjacentEdges) {
            if (edge1.equals(edge2) || (edge1.to == edge2.from && edge1.from == edge2.to)) {
              real = false;
            }
          }
        }
      }
      if (real) {
        loEdge.add(edge1);
      }
    }
    return loEdge;
  }

  // Creates list of every adjacent Edge of a list of Vertices
  ArrayList<Edge> getAdjacentEdges(ArrayList<ArrayList<Vertex>> vertices) {
    ArrayList<Edge> edges = new ArrayList<Edge>();
    for (ArrayList<Vertex> i : vertices) {
      for (Vertex vertex : i) {
        for (Edge edge : vertex.adjacentEdges) {
          edges.add(edge);
        }
      }
    }
    return edges;
  }

  // Uses Edge weights to determine the relevant Edges and assigns them to the
  // list of Vertices
  ArrayList<ArrayList<Vertex>> assignEdges(ArrayList<ArrayList<Vertex>> vertices) {
    ArrayList<Edge> preSortedEdges = getAdjacentEdges(vertices);
    for (ArrayList<Vertex> i : vertices) {
      for (Vertex vertex : i) {
        vertex.adjacentEdges = new ArrayList<Edge>();
      }
    }
    int numVertices = width * height;
    ArrayList<Edge> newEdges = new ArrayList<Edge>();
    ArrayList<Edge> sortedEdges = sort(preSortedEdges);
    HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
    for (int i = 0; i <= (1000 * height) + width; i++) {
      hashMap.put(i, i);
    }
    while (newEdges.size() < numVertices - 1) {
      Edge edge = sortedEdges.get(0);
      if (this.findHash(hashMap, edge.to.vertexHash()) != this.findHash(hashMap,
              edge.from.vertexHash())) {
        newEdges.add(edge);
        edge.from.adjacentEdges.add(edge);
        edge.to.adjacentEdges.add(new Edge(edge.to, edge.from, edge.weight));
        int hash = (findHash(hashMap, edge.to.vertexHash()));
        hashMap.remove(findHash(hashMap, edge.to.vertexHash()));
        hashMap.put(hash, findHash(hashMap, edge.from.vertexHash()));
      }
      sortedEdges.remove(0);
    }
    return vertices;
  }

  // Finds hashed values in our HashMap
  int findHash(HashMap<Integer, Integer> hashmap, int hash) {
    if (hashmap.get(hash) == hash) {
      return hash;
    }
    else {
      return findHash(hashmap, hashmap.get(hash));
    }
  }

  // Splits the list of Edges in order to merge sort them
  ArrayList<Edge> sort(ArrayList<Edge> edges) {
    if (edges.size() <= 1) {
      return edges;
    }
    ArrayList<Edge> edges1 = new ArrayList<Edge>();
    ArrayList<Edge> edges2 = new ArrayList<Edge>();
    for (int i = 0; i < edges.size() / 2; i++) {
      edges1.add(edges.get(i));
    }
    for (int i = edges.size() / 2; i < edges.size(); i++) {
      edges2.add(edges.get(i));
    }
    edges1 = sort(edges1);
    edges2 = sort(edges2);
    return merge(edges1, edges2);
  }

  // Uses a comparator to merge two lists of Edges
  ArrayList<Edge> merge(ArrayList<Edge> edges1, ArrayList<Edge> edges2) {
    ArrayList<Edge> mergedEdges = new ArrayList<Edge>();
    CompareEdges c = new CompareEdges();
    while (edges1.size() > 0 && edges2.size() > 0) {
      if (c.compare(edges1.get(0), edges2.get(0)) > 0) {
        mergedEdges.add(edges1.get(0));
        edges1.remove(0);
      }
      else {
        mergedEdges.add(edges2.get(0));
        edges2.remove(0);
      }
    }
    while (edges1.size() > 0) {
      mergedEdges.add(edges1.get(0));
      edges1.remove(0);
    }
    while (edges2.size() > 0) {
      mergedEdges.add(edges2.get(0));
      edges2.remove(0);
    }
    return mergedEdges;
  }

  // Colors each vertex gray, distinguishing the top-left and bottom-right
  // vertices with green and magenta respectively
  Color vertexColor(Vertex vertex) {
    if (vertex.x == width - 1 && vertex.y == height - 1) {
      return Color.MAGENTA;
    }
    else if (vertex.x == 0 && vertex.y == 0) {
      return Color.GREEN;
    }
    else if (vertex.onPath) {
      return Color.BLUE;
    }
    else if (vertex.seen) {
      return Color.CYAN;
    }
    return Color.GRAY;
  }

  // EFFECT: Updates the world every tick so that it is displayed onPathly
  public void onTick() {
    if (solveBFS) {
      if (bfs.hasNext()) {
        bfs.next();
      }
    }
    if (solveDFS) {
      if (dfs.hasNext()) {
        dfs.next();
      }
    }
  }

  // EFFECT: Solves the maze using BFS, DFS, or restarts the program when the
  // corresponding keys are pressed
  public void onKeyEvent(String ke) {
    if (ke.equals("b")) {
      solveBFS = true;
      solveDFS = false;
      refresh();
    }
    else if (ke.equals("d")) {
      solveBFS = false;
      solveDFS = true;
      refresh();
    }
    else if (ke.equals("r")) {
      worldConstructor();
    }
  }

  // EFFECT: Refreshes the world with updated settings and undoes previous solve
  // progress
  // after a solve key is pressed
  public void refresh() {
    for (Vertex vertex : loVertex) {
      vertex.onPath = false;
      vertex.seen = false;
    }
    bfs = new BFS(loVertex);
    dfs = new DFS(loVertex);
  }

  // Creates the maze display that the user sees every tick
  public WorldScene makeScene() {
    WorldScene maze = new WorldScene(width * scale, height * scale);
    for (Vertex vertex : loVertex) {
      Color color = vertexColor(vertex);
      maze.placeImageXY(new RectangleImage(scale, scale, OutlineMode.SOLID, color),
              (vertex.x * scale) + (scale * 1 / 2), (vertex.y * scale) + (scale * 1 / 2));
    }
    for (Edge edge : loEdge) {
      if (edge.to.x == edge.from.x) {
        maze.placeImageXY(new RectangleImage(scale, scale / 10, OutlineMode.SOLID, Color.black),
                (edge.to.x * scale) + (scale * 1 / 2),
                ((edge.to.y + edge.from.y) * scale / 2) + (scale * 1 / 2));
      }
      else {
        maze.placeImageXY(new RectangleImage(scale / 10, scale, OutlineMode.SOLID, Color.black),
                ((edge.to.x + edge.from.x) * scale / 2) + (scale * 1 / 2),
                (edge.to.y * scale) + (scale * 1 / 2));
      }
    }
    return maze;
  }

  // Initializes the maze
  public void initMaze() {
    this.bigBang(MazeWorld.width * MazeWorld.scale, MazeWorld.height * MazeWorld.scale, 0.005);
  }
}
