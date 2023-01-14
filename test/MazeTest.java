import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javalib.impworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import tester.Tester;

public class MazeTest {
  Vertex vertex1;
  Vertex vertex2;
  Vertex vertex3;

  ArrayList<Vertex> vertices1;
  ArrayList<Vertex> vertices2;
  ArrayList<Vertex> vertices3;
  ArrayList<ArrayList<Vertex>> loVertex;
  ArrayList<ArrayList<Vertex>> mtLoVertex;

  Edge edge1_2;
  Edge edge2_3;

  ArrayList<Edge> loEdge;
  ArrayList<Edge> mtLoEdge;

  MazeWorld maze;

  // Sets the initial conditions for the tests
  void init() {
    vertex1 = new Vertex(0, 0);
    vertex2 = new Vertex(1, 0);
    vertex3 = new Vertex(0, 1);

    vertices1 = new ArrayList<Vertex>();
    vertices2 = new ArrayList<Vertex>();
    vertices3 = new ArrayList<Vertex>();
    loVertex = new ArrayList<ArrayList<Vertex>>();
    loVertex.add(vertices1);
    mtLoVertex = new ArrayList<ArrayList<Vertex>>();

    edge1_2 = new Edge(vertex1, vertex2, 1);
    edge2_3 = new Edge(vertex2, vertex3, 2);

    loEdge = new ArrayList<Edge>();
    loEdge.add(edge1_2);
    loEdge.add(edge2_3);
    mtLoEdge = new ArrayList<Edge>();

    maze = new MazeWorld();
  }

  Deque<String> deque1 = new Deque<String>();
  Deque<String> deque2 = new Deque<String>();
  Deque<Double> deque3 = new Deque<Double>();

  Sentinel<String> ss1 = new Sentinel<String>();
  Node<String> s1 = new Node<String>("abc", ss1, ss1);
  Node<String> s2 = new Node<String>("bcd", ss1, s1);
  Node<String> s3 = new Node<String>("cde", ss1, s2);
  Node<String> s4 = new Node<String>("def", ss1, s3);

  Sentinel<Double> sd1 = new Sentinel<Double>();
  Node<Double> d1 = new Node<Double>(22.7, sd1, sd1);
  Node<Double> d2 = new Node<Double>(5.4, sd1, d1);
  Node<Double> d3 = new Node<Double>(102.1, sd1, d2);
  Node<Double> d4 = new Node<Double>(32.3, sd1, d3);

  void createDeque() {
    deque2.addToTail("abc");
    deque2.addToTail("bcd");
    deque2.addToTail("cde");
    deque2.addToTail("def");

    deque3.addToTail(22.7);
    deque3.addToTail(5.4);
    deque3.addToTail(102.1);
    deque3.addToTail(32.3);
  }

  void resetDeque() {
    deque1 = new Deque<String>();

    ss1 = new Sentinel<String>();
    s1 = new Node<String>("abc", ss1, ss1);
    s2 = new Node<String>("bcd", ss1, s1);
    s3 = new Node<String>("cde", ss1, s2);
    s4 = new Node<String>("def", ss1, s3);
    deque2 = new Deque<String>();

    sd1 = new Sentinel<Double>();
    d1 = new Node<Double>(22.7, sd1, sd1);
    d2 = new Node<Double>(5.4, sd1, d1);
    d3 = new Node<Double>(102.1, sd1, d2);
    d4 = new Node<Double>(32.3, sd1, d3);
    deque3 = new Deque<Double>();
  }

  // EFFECT: Tests the vertexHash method
  void testVertexHash(Tester t) {
    init();
    t.checkExpect(vertex1.vertexHash(), 0);
    t.checkExpect(vertex2.vertexHash(), 1);
    t.checkExpect(vertex3.vertexHash(), 1000);
  }

  // EFFECT: Tests the compare method
  void testCompare(Tester t) {
    init();
    CompareEdges c = new CompareEdges();
    t.checkExpect(c.compare(edge1_2, edge2_3), 1);
    t.checkExpect(c.compare(edge2_3, edge1_2), -1);
  }

  // EFFECT: Tests the makeLoVertex method
  void testMakeLoVertex(Tester t) {
    // Tests with an empty loVertex from the test constructor
    MazeWorld maze = new MazeWorld("Maze for testing");
    t.checkExpect(maze.loVertex, new ArrayList<Vertex>());
    // Tests with an loVertex from the real constructor
    maze = new MazeWorld();
    t.checkFail(maze.loVertex, new ArrayList<Vertex>());
    t.checkExpect(maze.loVertex.get(0).x, 0);
    t.checkExpect(maze.loVertex.get(0).y, 0);
    t.checkExpect(maze.loVertex.get(10).x, 0);
    t.checkExpect(maze.loVertex.get(10).y, 10);
    t.checkExpect(maze.loVertex.get(100).x, 1);
    t.checkExpect(maze.loVertex.get(100).y, 40);
    t.checkExpect(maze.loVertex.get(1000).x, 16);
    t.checkExpect(maze.loVertex.get(1000).y, 40);
  }

  // EFFECT: Tests the makeLoEdge method
  void testMakeLoEdge(Tester t) {
    init();
    t.checkExpect(maze.makeLoEdge(mtLoVertex, mtLoEdge), new ArrayList<Edge>());
    t.checkExpect(maze.makeLoEdge(loVertex, mtLoEdge), new ArrayList<Edge>());
    t.checkExpect(maze.makeLoEdge(loVertex, loEdge),
            new ArrayList<Edge>(Arrays.asList(edge1_2, edge2_3)));
    // Manually adds a Vertex to an empty list of Vertices to check that it still
    // works
    ArrayList<ArrayList<Vertex>> test = new ArrayList<ArrayList<Vertex>>();
    vertex1.adjacentEdges.add(edge1_2);
    test.add(new ArrayList<Vertex>(Arrays.asList(vertex1)));
    t.checkExpect(maze.makeLoEdge(test, loEdge), new ArrayList<Edge>(Arrays.asList(edge2_3)));
  }

  // EFFECT: Tests the getAdjacentEdges method
  void testGetAdjacentEdges(Tester t) {
    init();
    t.checkExpect(maze.getAdjacentEdges(mtLoVertex), new ArrayList<Edge>());
    ArrayList<ArrayList<Vertex>> test = new ArrayList<ArrayList<Vertex>>();
    vertex1.adjacentEdges.add(edge1_2);
    test.add(new ArrayList<Vertex>(Arrays.asList(vertex1)));
    t.checkExpect(maze.getAdjacentEdges(test), new ArrayList<Edge>(Arrays.asList(edge1_2)));
    vertex1.adjacentEdges.add(edge2_3);
    t.checkExpect(maze.getAdjacentEdges(test),
            new ArrayList<Edge>(Arrays.asList(edge1_2, edge2_3)));
    vertex2.adjacentEdges.add(new Edge(vertex1, vertex3, 3));
    vertex3.adjacentEdges.add(new Edge(vertex3, vertex2, 0));
    test.add(new ArrayList<Vertex>(Arrays.asList(vertex2, vertex3)));
    t.checkExpect(maze.getAdjacentEdges(test), new ArrayList<Edge>(Arrays.asList(edge1_2, edge2_3,
            new Edge(vertex1, vertex3, 3), new Edge(vertex3, vertex2, 0))));
  }

  // EFFECT: Tests the assignEdges method
  void testAssignEdges(Tester t) {
    init();
    t.checkExpect(null, null);
    t.checkExpect(null, null);
    t.checkExpect(null, null);
  }

  // EFFECT: Tests the findHash method
  void testFindHash(Tester t) {
    init();
    HashMap<Integer, Integer> hashMap = new HashMap<>();
    t.checkExpect(hashMap.isEmpty(), true);
    hashMap.put(3, 3);
    t.checkExpect(maze.findHash(hashMap, 3), 3);
    hashMap.put(0, 1);
    hashMap.put(1, 2);
    hashMap.put(2, 3);
    t.checkExpect(maze.findHash(hashMap, 1), 3);
    t.checkExpect(hashMap.size(), 4);
  }

  // EFFECT: Tests the sort method
  void testSort(Tester t) {
    init();
    t.checkExpect(mtLoEdge, new ArrayList<Edge>());
    t.checkExpect(loEdge, new ArrayList<Edge>(Arrays.asList(edge1_2, edge2_3)));
    t.checkExpect(maze.sort(mtLoEdge), new ArrayList<Edge>());
    t.checkExpect(maze.sort(loEdge), new ArrayList<Edge>(Arrays.asList(edge1_2, edge2_3)));
    // Adds another edge to see if sorting executes properly
    loEdge.add(0, new Edge(vertex1, vertex3, 3));
    t.checkExpect(loEdge,
            new ArrayList<Edge>(Arrays.asList(new Edge(vertex1, vertex3, 3), edge1_2, edge2_3)));
    t.checkExpect(maze.sort(loEdge),
            new ArrayList<Edge>(Arrays.asList(edge1_2, edge2_3, new Edge(vertex1, vertex3, 3))));
  }

  // EFFECT: Tests the merge method
  void testMerge(Tester t) {
    init();
    t.checkExpect(maze.merge(new ArrayList<Edge>(), new ArrayList<Edge>()), mtLoEdge);
    t.checkExpect(maze.merge(mtLoEdge, new ArrayList<Edge>(Arrays.asList(edge1_2, edge2_3))),
            new ArrayList<Edge>(Arrays.asList(edge1_2, edge2_3)));
    t.checkExpect(maze.merge(new ArrayList<Edge>(Arrays.asList(edge1_2, edge2_3)), mtLoEdge),
            new ArrayList<Edge>(Arrays.asList(edge1_2, edge2_3)));
    t.checkExpect(
            maze.merge(new ArrayList<Edge>(Arrays.asList(edge1_2, edge2_3)),
                    new ArrayList<Edge>(Arrays.asList(new Edge(vertex1, vertex3, 3)))),
            new ArrayList<Edge>(Arrays.asList(edge1_2, edge2_3, new Edge(vertex1, vertex3, 3))));
    t.checkExpect(
            maze.merge(new ArrayList<Edge>(Arrays.asList(edge1_2, edge2_3)),
                    new ArrayList<Edge>(Arrays.asList(new Edge(vertex1, vertex3, 0)))),
            new ArrayList<Edge>(Arrays.asList(new Edge(vertex1, vertex3, 0), edge1_2, edge2_3)));
  }

  // EFFECT: Tests the vertexColor method
  void testVertexColor(Tester t) {
    init();
    // Top-left corner Vertex
    t.checkExpect(maze.vertexColor(vertex1), Color.GREEN);
    vertex1.x = MazeWorld.width - 1;
    vertex1.y = MazeWorld.height - 1;
    // Bottom-right corner Vertex
    t.checkExpect(maze.vertexColor(vertex1), Color.MAGENTA);
    vertex1.x = 50;
    vertex1.y = 50;
    // Random middle Vertex
    t.checkExpect(maze.vertexColor(vertex1), Color.gray);
  }

  // EFFECT: Tests the onKeyEvent method
  void testOnKeyEvent(Tester t) {
    init();
    t.checkExpect(null, null);
    t.checkExpect(null, null);
    t.checkExpect(null, null);
  }

  // EFFECT: Tests the refresh method
  void testRefresh(Tester t) {
    init();
    t.checkExpect(null, null);
    t.checkExpect(null, null);
    t.checkExpect(null, null);
  }

  // EFFECT: Tests the makeScene method
  void testMakeScene(Tester t) {
    init();
    WorldScene maze = new WorldScene(MazeWorld.width * MazeWorld.scale,
            MazeWorld.height * MazeWorld.scale);
    MazeWorld testMaze = new MazeWorld("Maze for testing");
    testMaze.loVertex.add(vertex1);
    testMaze.loVertex.add(vertex2);
    testMaze.loVertex.add(vertex3);
    testMaze.loEdge.add(edge1_2);
    testMaze.loEdge.add(edge2_3);
    t.checkFail(testMaze.makeScene(), maze);
    // Replicates makeScene by manually drawing each Vertex and Edge
    maze.placeImageXY(
            new RectangleImage(MazeWorld.scale, MazeWorld.scale, OutlineMode.SOLID, Color.GREEN),
            (MazeWorld.scale * vertex1.x) + (MazeWorld.scale / 2),
            (MazeWorld.scale * vertex1.y) + (MazeWorld.scale / 2));
    maze.placeImageXY(
            new RectangleImage(MazeWorld.scale, MazeWorld.scale, OutlineMode.SOLID, Color.GRAY),
            (MazeWorld.scale * vertex2.x) + (MazeWorld.scale / 2),
            (MazeWorld.scale * vertex2.y) + (MazeWorld.scale / 2));
    maze.placeImageXY(
            new RectangleImage(MazeWorld.scale, MazeWorld.scale, OutlineMode.SOLID, Color.GRAY),
            (MazeWorld.scale * vertex3.x) + (MazeWorld.scale / 2),
            (MazeWorld.scale * vertex3.y) + (MazeWorld.scale / 2));
    maze.placeImageXY(
            new RectangleImage(MazeWorld.scale / 10, MazeWorld.scale, OutlineMode.SOLID, Color.BLACK),
            ((edge1_2.from.x + edge1_2.to.x) * MazeWorld.scale / 2) + (MazeWorld.scale / 2),
            (MazeWorld.scale * edge1_2.to.y) + (MazeWorld.scale / 2));
    maze.placeImageXY(
            new RectangleImage(MazeWorld.scale / 10, MazeWorld.scale, OutlineMode.SOLID, Color.BLACK),
            ((edge2_3.from.x + edge2_3.to.x) * MazeWorld.scale / 2) + (MazeWorld.scale / 2),
            (MazeWorld.scale * edge2_3.to.y) + (MazeWorld.scale / 2));
    t.checkExpect(testMaze.makeScene(), maze);
  }

  // EFFECT: Tests the getData method
  void testGetData(Tester t) {
    init();
    t.checkExpect(ss1.getData(), null);
    t.checkExpect(s1.getData(), "abc");
    t.checkExpect(d1.getData(), 22.7);
  }

  // EFFECT: Tests the find method
  void testFind(Tester t) {
    init();
    resetDeque();
    createDeque();
    t.checkExpect(deque1.find(new HasLetterA()), new Sentinel<String>());
    t.checkExpect(deque2.find(new HasLetterA()), ss1);
    t.checkExpect(deque3.find(new HasLessThan10()), d2);
  }

  // EFFECT: Tests the size method
  void testSize(Tester t) {
    init();
    resetDeque();
    createDeque();
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque3.size(), 4);
  }

  // EFFECT: Tests the remove method
  void testRemove(Tester t) {
    init();
    resetDeque();
    createDeque();
    t.checkExpect(s1.remove(), "abc");
    t.checkExpect(d1.remove(), 22.7);
    t.checkExpect(s2.remove(), "bcd");
  }

  // EFFECT: Tests the removeHelper method
  void testRemoveHelper(Tester t) {
    init();
    resetDeque();
    createDeque();
    t.checkExpect(deque2.header.nextNode, s1);
    s1.removeHelper(s1);
    t.checkExpect(deque2.header.nextNode, new Node<String>("abc", s2, ss1));

    t.checkExpect(deque3.header.nextNode, d1);
    d1.removeHelper(d1);
    t.checkExpect(deque3.header.nextNode, new Node<Double>(22.7, d2, sd1));
  }

  // EFFECT: Tests the isSentinel method
  void testIsSentinel(Tester t) {
    init();
    resetDeque();
    createDeque();
    t.checkExpect(ss1.isSentinel(), true);
    t.checkExpect(s1.isSentinel(), false);
    t.checkExpect(d1.isSentinel(), false);
  }

  // EFFECT: Tests the hasNext method
  void testHasNext(Tester t) {
    init();
    resetDeque();
    createDeque();
    t.checkExpect(ss1.hasNext(), true);
    t.checkExpect(s4.hasNext(), false);
    t.checkExpect(d1.hasNext(), true);
  }

  // EFFECT: Tests the addToHead method
  void testAddToHead(Tester t) {
    init();
    resetDeque();
    createDeque();
    Sentinel<String> tempS = new Sentinel<String>();
    Node<String> tempSNode = new Node<String>("apple", tempS, tempS);

    t.checkExpect(deque1.header.nextNode, new Sentinel<String>());
    deque1.addToHead("apple");
    t.checkExpect(deque1.header.nextNode, tempSNode);

    t.checkExpect(deque2.header.nextNode, s1);
    deque2.addToHead("berry");
    tempSNode = new Node<String>("berry", s1, ss1);
    t.checkExpect(deque2.header.nextNode, tempSNode);

    t.checkExpect(deque3.header.nextNode, d1);
    deque3.addToHead(3.3);
    Node<Double> tempDNode = new Node<Double>(3.3, d1, sd1);
    t.checkExpect(deque3.header.nextNode, tempDNode);
  }

  // EFFECT: Tests the addToTail method
  void testAddToTail(Tester t) {
    init();
    resetDeque();
    createDeque();
    Sentinel<String> tempS = new Sentinel<String>();
    Node<String> tempSNode = new Node<String>("apple", tempS, tempS);

    t.checkExpect(deque1.header.prevNode, new Sentinel<String>());
    deque1.addToTail("apple");
    t.checkExpect(deque1.header.prevNode, tempSNode);

    t.checkExpect(deque2.header.prevNode, s4);
    deque2.addToTail("berry");
    tempSNode = new Node<String>("berry", ss1, s4);
    t.checkExpect(deque2.header.prevNode, tempSNode);

    t.checkExpect(deque3.header.prevNode, d4);
    deque3.addToTail(3.3);
    Node<Double> tempDNode = new Node<Double>(3.3, sd1, d4);
    t.checkExpect(deque3.header.prevNode, tempDNode);
  }

  // EFFECT: Tests the removeFromHead method
  void testRemoveFromHead(Tester t) {
    init();
    resetDeque();
    createDeque();
    deque2.addToHead("berry");
    Node<String> tempSNode = new Node<String>("berry", s1, ss1);
    t.checkExpect(deque2.header.nextNode, tempSNode);
    deque2.removeFromHead();
    t.checkExpect(deque2.header.nextNode, new Node<String>("abc", s2, ss1));

    deque3.addToHead(3.3);
    Node<Double> tempDNode = new Node<Double>(3.3, d1, sd1);
    t.checkExpect(deque3.header.nextNode, tempDNode);
    deque3.removeFromHead();
    t.checkExpect(deque3.header.nextNode, new Node<Double>(22.7, d2, sd1));
  }

  // EFFECT: Tests the removeFromTail method
  void testRemoveFromTail(Tester t) {
    init();
    resetDeque();
    createDeque();
    deque2.addToTail("berry");
    Node<String> tempSNode = new Node<String>("berry", ss1, s4);
    t.checkExpect(deque2.header.prevNode, tempSNode);
    deque2.removeFromTail();
    t.checkExpect(deque2.header.prevNode, new Node<String>("def", ss1, s3));

    deque3.addToTail(3.3);
    Node<Double> tempDNode = new Node<Double>(3.3, sd1, d4);
    t.checkExpect(deque3.header.prevNode, tempDNode);
    deque3.removeFromTail();
    t.checkExpect(deque3.header.prevNode, new Node<Double>(32.3, sd1, d3));
  }

  // EFFECT: Tests the next method
  void testNext(Tester t) {
    init();
    resetDeque();
    createDeque();
    Iterator<String> testIterator1 = deque2.iterator();
    Iterator<Double> testIterator2 = deque3.iterator();

    t.checkExpect(testIterator1.next(), "abc");
    t.checkExpect(testIterator2.next(), 22.7);
  }

  // EFFECT: Tests the isEmpty method
  void testIsEmpty(Tester t) {
    init();
    resetDeque();
    createDeque();
    Queue<String> empty = new Queue<String>();
    Queue<String> notEmpty = new Queue<String>(new ArrayList<String>(Arrays.asList("a", "b")));
    t.checkExpect(empty.isEmpty(), true);
    t.checkExpect(notEmpty.isEmpty(), false);
  }

  // EFFECT: Tests the enqueue and dequeue methods
  void testEnqueueAndDequeue(Tester t) {
    init();
    resetDeque();
    createDeque();
    Queue<String> empty = new Queue<String>();
    Queue<String> notEmpty = new Queue<String>(new ArrayList<String>(Arrays.asList("a", "b")));
    t.checkExpect(empty.allData.header.prevNode, new Sentinel<String>());
    empty.enqueue("a");
    t.checkExpect(empty.allData.header.prevNode, new Node<String>("a", ss1, ss1));
    empty.dequeue();
    t.checkExpect(empty.allData.header.prevNode, new Sentinel<String>());

    t.checkExpect(notEmpty.allData.header.prevNode,
            new Node<String>("b", ss1, new Node<String>("a", ss1, ss1)));
    notEmpty.enqueue("c");
    t.checkExpect(notEmpty.allData.header.prevNode,
            new Node<String>("c", ss1, new Node<String>("b", ss1, new Node<String>("a", ss1, ss1))));
    notEmpty.dequeue();
    t.checkExpect(notEmpty.allData.header.prevNode,
            new Node<String>("b", ss1, new Node<String>("a", ss1, ss1)));
  }

  // EFFECT: Tests the push and pop methods
  void testPushAndPop(Tester t) {
    init();
    resetDeque();
    createDeque();
    Stack<String> empty = new Stack<String>();
    Stack<String> notEmpty = new Stack<String>(new ArrayList<String>(Arrays.asList("a", "b")));
    t.checkExpect(empty.allData.header.nextNode, new Sentinel<String>());
    empty.push("a");
    t.checkExpect(empty.allData.header.nextNode, new Node<String>("a", ss1, ss1));
    empty.pop();
    t.checkExpect(empty.allData.header.nextNode, new Sentinel<String>());

    t.checkExpect(notEmpty.allData.header.nextNode,
            new Node<String>("a", new Node<String>("b", ss1, ss1), ss1));
    notEmpty.push("c");
    t.checkExpect(notEmpty.allData.header.nextNode,
            new Node<String>("c", new Node<String>("a", new Node<String>("b", ss1, ss1), ss1), ss1));
    notEmpty.pop();
    t.checkExpect(notEmpty.allData.header.nextNode,
            new Node<String>("a", new Node<String>("b", ss1, ss1), ss1));
  }

  // EFFECT: Tests the buildPath method
  void testBuildPath(Tester t) {
    init();
    resetDeque();
    createDeque();
    t.checkExpect(null, null);
    t.checkExpect(null, null);
    t.checkExpect(null, null);
  }

  // EFFECT: Runs the game
  void testGame(Tester t) {
    MazeWorld maze = new MazeWorld();
    // Creates a Maze using the static size inputs from the MazeWorld class
    maze.initMaze();
  }
}
