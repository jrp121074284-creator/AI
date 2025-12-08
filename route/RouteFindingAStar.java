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

public class RouteFindingAStar {
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

        aStarSearch(g, "A", "B");
    }

    static void aStarSearch(Graph g, String start, String goal) {
        class State {
            String node;
            int g; 
            int f; 

            State(String node, int g, int f) {
                this.node = node;
                this.g = g;
                this.f = f;
            }
        }

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.f));
        pq.add(new State(start, 0, g.getHeuristic(start)));

        Map<String, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();

        while (!pq.isEmpty()) {
            State cur = pq.poll();

            if (visited.contains(cur.node)) continue;
            visited.add(cur.node);

            System.out.println("Visited: " + cur.node + " (g=" + cur.g + ", h=" + g.getHeuristic(cur.node) + ", f=" + cur.f + ")");

            if (cur.node.equals(goal)) {
                System.out.println("Goal reached!");
                List<String> path = new ArrayList<>();
                String n = goal;
                while (n != null) {
                    path.add(n);
                    n = parent.get(n);
                }
                Collections.reverse(path);
                System.out.println("Path: " + path);
                System.out.println("Total Cost = " + cur.g);
                return;
            }

            for (String nb : g.adj.get(cur.node)) {
                int tentative_g = cur.g + g.getCost(cur.node, nb);
                if (tentative_g < gScore.getOrDefault(nb, Integer.MAX_VALUE)) {
                    gScore.put(nb, tentative_g);
                    int f = tentative_g + g.getHeuristic(nb);
                    pq.add(new State(nb, tentative_g, f));
                    parent.put(nb, cur.node);
                }
            }
        }
    }
}

/*
Here’s a **detailed explanation** of your `RouteFindingAStar` Java program, including problem statement, line-by-line analysis, data structures, complexities, and pseudocode.

---

# **Problem Statement**

The **Route-Finding Problem**:

* Find the **shortest path** between a start city (`A`) and a goal city (`B`) in a weighted graph.
* Each node (city) has a **heuristic** value estimating distance to the goal.
* This program uses **A* Search Algorithm**:

  * Uses `f(n) = g(n) + h(n)` where:

    * `g(n)` = cost from start to current node
    * `h(n)` = heuristic estimate to goal
    * `f(n)` = estimated total cost
  * Expands nodes with **lowest f-value first**, guaranteeing shortest path if heuristic is **admissible**.

---

# **Code Explanation (Line by Line)**

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

* Represents a city/node.
* Stores `name` and heuristic estimate `h(n)` to goal.

---

### **Graph Class**

```java
class Graph {
    Map<String, Node> nodes = new HashMap<>();
    Map<String, List<String>> adj = new HashMap<>();
    Map<String, Integer> cost = new HashMap<>();
```

* `nodes`: maps name → Node object (stores heuristic).
* `adj`: adjacency list (neighbors for each node).
* `cost`: maps `"u-v"` → edge weight.

```java
    void addNode(String name, int heuristic) {
        nodes.put(name, new Node(name, heuristic));
        adj.putIfAbsent(name, new ArrayList<>());
    }
```

* Adds a node with heuristic.
* Initializes adjacency list.

```java
    void addEdge(String u, String v, int c) {
        adj.get(u).add(v);
        adj.get(v).add(u);
        cost.put(u + "-" + v, c);
        cost.put(v + "-" + u, c);
    }
```

* Adds **undirected edge** between `u` and `v` with cost `c`.
* Stores cost for both directions.

```java
    int getHeuristic(String name) {
        return nodes.get(name).heuristic;
    }

    int getCost(String u, String v) {
        return cost.getOrDefault(u + "-" + v, Integer.MAX_VALUE);
    }
}
```

* Helper functions: return heuristic or edge cost.

---

### **Main Class: RouteFindingAStar**

```java
public class RouteFindingAStar {
    public static void main(String[] args) {
        Graph g = new Graph();
```

* Initializes a graph.

```java
        g.addNode("A", 366);
        g.addNode("B", 0);
        ...
        g.addNode("Z", 374);
```

* Adds nodes with heuristic values (h(n)).
* Heuristic = estimated straight-line distance to goal (`B`).

```java
        g.addEdge("A","Z",75);
        g.addEdge("A","S",140);
        ...
        g.addEdge("I","N",87);
```

* Adds all undirected edges with weights.

```java
        aStarSearch(g, "A", "B");
    }
```

* Calls **A*** search from `A` to `B`.

---

### **A* Search Function**

```java
    static void aStarSearch(Graph g, String start, String goal) {
        class State {
            String node;
            int g; 
            int f; 
            State(String node, int g, int f) { this.node=node; this.g=g; this.f=f; }
        }
```

* `State` represents a node in the priority queue:

  * `node` = node name
  * `g` = cost from start
  * `f` = total estimated cost (`g + h`)

```java
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.f));
        pq.add(new State(start, 0, g.getHeuristic(start)));
```

* Priority queue orders states by **lowest f-value**.
* Start node: `g=0`, `f=g+h`.

```java
        Map<String, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();
```

* `gScore` → cost from start to each node.
* `parent` → for path reconstruction.
* `visited` → avoid revisiting nodes.

---

### **Main Loop**

```java
        while (!pq.isEmpty()) {
            State cur = pq.poll();
            if (visited.contains(cur.node)) continue;
            visited.add(cur.node);
```

* Pop node with lowest `f`.
* Skip if already visited.

```java
            System.out.println("Visited: " + cur.node + " (g=" + cur.g + ", h=" + g.getHeuristic(cur.node) + ", f=" + cur.f + ")");
```

* Debug: prints node info.

```java
            if (cur.node.equals(goal)) {
                System.out.println("Goal reached!");
                List<String> path = new ArrayList<>();
                String n = goal;
                while (n != null) { path.add(n); n = parent.get(n); }
                Collections.reverse(path);
                System.out.println("Path: " + path);
                System.out.println("Total Cost = " + cur.g);
                return;
            }
```

* If goal reached: reconstruct path using `parent`, print path and total cost.

---

### **Explore Neighbors**

```java
            for (String nb : g.adj.get(cur.node)) {
                int tentative_g = cur.g + g.getCost(cur.node, nb);
                if (tentative_g < gScore.getOrDefault(nb, Integer.MAX_VALUE)) {
                    gScore.put(nb, tentative_g);
                    int f = tentative_g + g.getHeuristic(nb);
                    pq.add(new State(nb, tentative_g, f));
                    parent.put(nb, cur.node);
                }
            }
        }
```

* For each neighbor:

  1. Compute `tentative_g = g(cur) + cost(cur, nb)`.
  2. If new g is better → update `gScore`, compute `f = g+h`.
  3. Add neighbor to priority queue and update parent.

---

# **Data Structures Used**

| Data Structure                  | Purpose                                         |
| ------------------------------- | ----------------------------------------------- |
| `PriorityQueue<State>`          | Stores nodes to expand by lowest `f`.           |
| `HashMap<String, Integer>`      | `gScore` → stores cost from start to each node. |
| `HashMap<String, String>`       | `parent` → reconstruct path.                    |
| `HashSet<String>`               | `visited` → avoid revisiting nodes.             |
| `HashMap<String, List<String>>` | Adjacency list of graph.                        |
| `HashMap<String, Integer>`      | Edge costs.                                     |

---

# **Time Complexity**

* Worst case: **O(E log V)**

  * `E` = number of edges
  * `V` = number of vertices
  * `log V` factor from priority queue operations.
* A* can be faster if heuristic is **admissible**.

# **Space Complexity**

* **O(V + E)** for graph representation
* **O(V)** for priority queue, `visited`, `parent`, `gScore`.

---

# **Pseudocode (Short)**

```
function AStar(Graph g, start, goal):
    pq = priority queue ordered by f = g + h
    gScore[start] = 0
    parent[start] = null
    pq.push(State(start, g=0, f=h(start)))

    while pq not empty:
        cur = pq.pop()
        if cur.node == goal:
            return reconstructPath(parent, goal)
        if cur.node in visited: continue
        visited.add(cur.node)

        for neighbor in g.adj[cur.node]:
            tentative_g = gScore[cur.node] + g.getCost(cur.node, neighbor)
            if tentative_g < gScore[neighbor]:
                gScore[neighbor] = tentative_g
                f = tentative_g + g.getHeuristic(neighbor)
                pq.push(State(neighbor, tentative_g, f))
                parent[neighbor] = cur.node
```

---

✅ **Summary**

* Solves **shortest path problem** using **A* Search**.
* Combines **actual cost** (`g`) and **heuristic** (`h`) for informed search.
* Uses **priority queue** to expand the most promising node.
* Guarantees optimal solution if heuristic is admissible.

---

I can also **draw a step-by-step A* search tree for this graph** showing `f`, `g`, and `h` values to make it visually clear.

Do you want me to do that?
 */