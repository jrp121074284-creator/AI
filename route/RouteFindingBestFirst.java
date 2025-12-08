package Route_Finding;

import java.util.*;

class Node {
    String name;
    int heuristic;

    Node(String name, int heuristic) {
        this.name = name;
        this.heuristic = heuristic;
    }
}

class Graph {
    Map<String, Node> nodes = new HashMap<>();
    Map<String, List<String>> adj = new HashMap<>();
    Map<String, Integer> cost = new HashMap<>();

    void addNode(String name, int heuristic) {
        nodes.put(name, new Node(name, heuristic));
        adj.putIfAbsent(name, new ArrayList<>());
    }

    void addEdge(String u, String v, int c) {
        adj.get(u).add(v);
        adj.get(v).add(u);
        cost.put(u + "-" + v, c);
        cost.put(v + "-" + u, c);
    }

    int getHeuristic(String name) {
        return nodes.get(name).heuristic;
    }

    int getCost(String u, String v) {
        return cost.getOrDefault(u + "-" + v, Integer.MAX_VALUE);
    }
}

public class RouteFindingBestFirst {
    public static void main(String[] args) {
        Graph g = new Graph();

        g.addNode("A", 366);
        g.addNode("B", 0);
        g.addNode("C", 160);
        g.addNode("D", 242);
        g.addNode("E", 161);
        g.addNode("F", 176);
        g.addNode("G", 77);
        g.addNode("H", 151);
        g.addNode("I", 226);
        g.addNode("L", 244);
        g.addNode("M", 241);
        g.addNode("N", 234);
        g.addNode("O", 380);
        g.addNode("P", 100);
        g.addNode("R", 193);
        g.addNode("S", 253);
        g.addNode("T", 329);
        g.addNode("U", 80);
        g.addNode("V", 199);
        g.addNode("Z", 374);

        g.addEdge("A","Z",75);
        g.addEdge("A","S",140);
        g.addEdge("A","T",118);
        g.addEdge("Z","O",71);
        g.addEdge("O","S",151);
        g.addEdge("T","L",111);
        g.addEdge("L","M",70);
        g.addEdge("M","D",75);
        g.addEdge("D","C",120);
        g.addEdge("C","R",146);
        g.addEdge("C","P",138);
        g.addEdge("R","S",80);
        g.addEdge("R","P",97);
        g.addEdge("S","F",99);
        g.addEdge("F","B",211);
        g.addEdge("P","B",101);
        g.addEdge("B","G",90);
        g.addEdge("B","U",85);
        g.addEdge("U","H",98);
        g.addEdge("H","E",86);
        g.addEdge("U","V",142);
        g.addEdge("V","I",92);
        g.addEdge("I","N",87);

        bestFirstSearch(g, "A", "B");
    }

    static void bestFirstSearch(Graph g, String start, String goal) {
        class State {
            String node;
            int g; 
            int h;

            State(String node, int g, int h) {
                this.node = node;
                this.g = g;
                this.h = h;
            }
        }

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.h));
        pq.add(new State(start, 0, g.getHeuristic(start)));

        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();

        while (!pq.isEmpty()) {
            State cur = pq.poll();

            if (visited.contains(cur.node)) continue;
            visited.add(cur.node);

            System.out.println("Visited: " + cur.node + " (h=" + cur.h + ")");

            if (cur.node.equals(goal)) {
                System.out.println("Goal reached!");
                List<String> path = new ArrayList<>();
                String n = goal;
                int totalCost = cur.g;
                while (n != null) {
                    path.add(n);
                    n = parent.get(n);
                }
                Collections.reverse(path);
                System.out.println("Path: " + path);
                System.out.println("Total Cost = " + totalCost);
                return;
            }

            for (String nb : g.adj.get(cur.node)) {
                if (!visited.contains(nb)) {
                    int newCost = cur.g + g.getCost(cur.node, nb);
                    pq.add(new State(nb, newCost, g.getHeuristic(nb)));
                    parent.put(nb, cur.node);
                }
            }
        }
    }
}


/*
Sure! Let’s go **step by step** to explain this **Route Finding using Best-First Search (Greedy BFS)** Java code.

---

# **Problem Statement**

**Route-Finding Problem (Greedy Best-First Search):**

* Find a path from a **start node** to a **goal node** in a weighted graph.
* Each node has a **heuristic value** `h(n)` estimating the cost to reach the goal.
* Greedy Best-First Search always expands the node with the **lowest heuristic value**.
* Unlike A*, it **does not consider actual path cost `g(n)`** when choosing which node to expand, so it may not always find the shortest path.

**In this example:**

* Cities are nodes (A, B, C, …), edges are distances between cities.
* Heuristic: straight-line distance to the goal city (B).

---

# **Code Explanation**

---

### **Node Class**

```java
class Node {
    String name;
    int heuristic;

    Node(String name, int heuristic) {
        this.name = name;
        this.heuristic = heuristic;
    }
}
```

* Represents a city or node.
* `name`: city name.
* `heuristic`: estimated distance to the goal node.

---

### **Graph Class**

```java
class Graph {
    Map<String, Node> nodes = new HashMap<>();
    Map<String, List<String>> adj = new HashMap<>();
    Map<String, Integer> cost = new HashMap<>();
```

* `nodes`: stores all nodes with their heuristics.
* `adj`: adjacency list representing graph connectivity.
* `cost`: stores edge costs between two nodes.

```java
    void addNode(String name, int heuristic) {
        nodes.put(name, new Node(name, heuristic));
        adj.putIfAbsent(name, new ArrayList<>());
    }
```

* Adds a node to the graph.
* Initializes its adjacency list.

```java
    void addEdge(String u, String v, int c) {
        adj.get(u).add(v);
        adj.get(v).add(u);
        cost.put(u + "-" + v, c);
        cost.put(v + "-" + u, c);
    }
```

* Adds a **bidirectional edge** with a cost `c` between `u` and `v`.

```java
    int getHeuristic(String name) {
        return nodes.get(name).heuristic;
    }

    int getCost(String u, String v) {
        return cost.getOrDefault(u + "-" + v, Integer.MAX_VALUE);
    }
}
```

* Returns heuristic value or edge cost between nodes.

---

### **Main Method**

```java
Graph g = new Graph();
```

* Creates a new graph instance.

```java
g.addNode("A", 366);
...
g.addEdge("A","Z",75);
...
bestFirstSearch(g, "A", "B");
```

* Adds nodes (cities) with heuristics.
* Adds weighted edges (distances).
* Calls **Best-First Search** from `"A"` to `"B"`.

---

### **Best-First Search Function**

```java
static void bestFirstSearch(Graph g, String start, String goal) {
    class State {
        String node;
        int g; 
        int h;

        State(String node, int g, int h) {
            this.node = node;
            this.g = g;
            this.h = h;
        }
    }
```

* Inner class `State` represents:

  * `node`: current city.
  * `g`: **actual cost from start** (for reporting).
  * `h`: **heuristic** to goal (used to prioritize node in the queue).

```java
PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.h));
pq.add(new State(start, 0, g.getHeuristic(start)));
```

* Priority queue to select node with **lowest heuristic** (`Greedy BFS`).
* Start node is added with `g = 0` and heuristic.

```java
Map<String, String> parent = new HashMap<>();
Set<String> visited = new HashSet<>();
```

* `parent`: track path for printing solution.
* `visited`: prevent revisiting nodes.

---

### **Main Loop**

```java
while (!pq.isEmpty()) {
    State cur = pq.poll();
    if (visited.contains(cur.node)) continue;
    visited.add(cur.node);
    System.out.println("Visited: " + cur.node + " (h=" + cur.h + ")");
```

* Pop the node with **lowest heuristic** from queue.
* Skip if already visited.
* Mark current node as visited.

```java
if (cur.node.equals(goal)) {
    System.out.println("Goal reached!");
    List<String> path = new ArrayList<>();
    String n = goal;
    int totalCost = cur.g;
    while (n != null) {
        path.add(n);
        n = parent.get(n);
    }
    Collections.reverse(path);
    System.out.println("Path: " + path);
    System.out.println("Total Cost = " + totalCost);
    return;
}
```

* If goal is reached:

  * Reconstruct path using `parent` map.
  * Print path and total cost.
  * Exit.

---

### **Expanding Neighbors**

```java
for (String nb : g.adj.get(cur.node)) {
    if (!visited.contains(nb)) {
        int newCost = cur.g + g.getCost(cur.node, nb);
        pq.add(new State(nb, newCost, g.getHeuristic(nb)));
        parent.put(nb, cur.node);
    }
}
```

* For each neighbor not visited:

  * Calculate new cost `g`.
  * Add neighbor to priority queue with its **heuristic**.
  * Record parent to reconstruct path.

---

# **Data Structures Used**

| Structure                   | Purpose                                        |
| --------------------------- | ---------------------------------------------- |
| `Graph`                     | Holds nodes, edges, and costs.                 |
| `Map<String, Node>`         | Store heuristic values for nodes.              |
| `Map<String, List<String>>` | Adjacency list for graph.                      |
| `Map<String, Integer>`      | Stores edge costs.                             |
| `PriorityQueue<State>`      | Picks node with lowest heuristic (Greedy BFS). |
| `Set<String>`               | Tracks visited nodes.                          |
| `Map<String,String>`        | Tracks parent nodes to reconstruct path.       |

---

# **Time & Space Complexity**

* **Time complexity:**

  * O(V + E) to expand all nodes.
  * In worst case (all nodes visited), O(V log V) due to priority queue operations.

* **Space complexity:**

  * O(V) for visited + parent + priority queue.

> V = number of nodes, E = number of edges.

**Note:** Greedy BFS **does not guarantee shortest path**, because it ignores actual path cost `g(n)` when choosing nodes.

---

# **Pseudo Code (Short)**

```
function BestFirstSearch(graph, start, goal):
    pq = PriorityQueue()
    pq.add(start, h(start))
    visited = set()
    parent = map()

    while pq not empty:
        cur = pq.poll()
        if cur in visited: continue
        visited.add(cur)
        if cur == goal:
            print path from parent
            return
        for neighbor in neighbors(cur):
            if neighbor not in visited:
                pq.add(neighbor, h(neighbor))
                parent[neighbor] = cur
```

---

# ✅ **Summary**

* Solves **Route-Finding using Greedy Best-First Search**.
* Expands nodes based on **heuristic only**.
* Uses priority queue to always select **node with lowest estimated cost to goal**.
* Prints **visited nodes, path, and total cost**.
* Time complexity: O(V log V), Space complexity: O(V).

---

If you want, I can make a **diagram showing how this BFS expands nodes step by step** for this graph. It helps visualize the Best-First Search order.

Do you want me to do that?
 */