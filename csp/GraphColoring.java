package CSP;

public class GraphColoring {

    static int[][] graph = {
        {0, 1, 1, 1},
        {1, 0, 1, 0},
        {1, 1, 0, 1},
        {1, 0, 1, 0}
    };

    static int numVertices = graph.length;
    static int numColors = 3; 

    static int[] vertexColor = new int[numVertices];

    public static void main(String[] args) {
        if (!solve(0)) {
            System.out.println("No solution exists with " + numColors + " colors.");
        }
    }

    private static boolean solve(int vertex) {
        if (vertex == numVertices) {
            printSolution();
            return true; 
        }

        for (int color = 1; color <= numColors; color++) {
            if (isSafe(vertex, color)) {
                vertexColor[vertex] = color;

                if (solve(vertex + 1)) return true;

                vertexColor[vertex] = 0;
            }
        }
        return false;
    }

    private static boolean isSafe(int vertex, int color) {
        for (int i = 0; i < numVertices; i++) {
            if (graph[vertex][i] == 1 && vertexColor[i] == color) {
                return false;
            }
        }
        return true;
    }

    private static void printSolution() {
        System.out.println("Solution Found: Coloring of vertices");
        for (int v = 0; v < numVertices; v++) {
            System.out.println("Vertex " + v + " --> Color " + vertexColor[v]);
        }
    }
}


/*
# Quick overview — problem statement (short)

This program solves the **graph coloring** decision problem by backtracking: given an undirected graph and a fixed number of colors `numColors`, assign a color (1..`numColors`) to each vertex so that no two adjacent vertices share the same color. If a valid coloring exists the program prints one and exits (it returns the first solution found).

---

# Line-by-line explanation (detailed)

I'll follow the code top-to-bottom and explain each declaration and block in detail.

```java
package CSP;

import java.util.*;
```

* `package CSP;` — places this class in the `CSP` package (just organizational).
* `import java.util.*;` — not strictly necessary for this code (no util classes used explicitly) but common.

```java
public class GraphColoring {
```

* Begins the `GraphColoring` class that contains the graph, parameters and solver.

```java
    static int[][] graph = {
        {0, 1, 1, 1},
        {1, 0, 1, 0},
        {1, 1, 0, 1},
        {1, 0, 1, 0}
    };
```

* `graph` is an **adjacency matrix** for 4 vertices.

  * `graph[i][j] == 1` means there is an edge between vertex `i` and `j`. `0` means no edge.
  * The matrix is symmetric for an undirected graph (`graph[i][j] == graph[j][i]`). Diagonal entries are `0` (no self-loops).

```java
    static int numVertices = graph.length;
    static int numColors = 3; 
```

* `numVertices` derives from the matrix size (here 4).
* `numColors = 3` — the algorithm will try to color the graph using 3 colors labeled `1,2,3`. This is the parameter of the decision problem: "Is the graph 3-colorable?"

```java
    static int[] vertexColor = new int[numVertices];
```

* `vertexColor` is an integer array of length `numVertices`.

  * `vertexColor[v]` holds the color assigned to vertex `v` (0 means “not yet assigned”, valid colors are `1..numColors`).

```java
    public static void main(String[] args) {
        if (!solve(0)) {
            System.out.println("No solution exists with " + numColors + " colors.");
        }
    }
```

* `main` starts the search by calling `solve(0)` — attempt to color vertices starting from index `0`.
* If `solve` returns `false` (no valid coloring found), print a message.

```java
    private static boolean solve(int vertex) {
        if (vertex == numVertices) {
            printSolution();
            return true; 
        }
```

* `solve(vertex)` is a recursive backtracking routine.
* Base case: if `vertex == numVertices`, every vertex has been assigned a color successfully — print the solution and return `true`. This is the success termination condition.

```java
        for (int color = 1; color <= numColors; color++) {
            if (isSafe(vertex, color)) {
                vertexColor[vertex] = color;

                if (solve(vertex + 1)) return true;

                vertexColor[vertex] = 0;
            }
        }
        return false;
    }
```

* For the current `vertex`, try each color `1..numColors`.
* `isSafe(vertex, color)` checks whether assigning `color` to `vertex` would conflict with any already-colored neighbors.

  * If `isSafe` returns `true`, the code assigns the color (`vertexColor[vertex] = color`) and recursively calls `solve(vertex + 1)` to color the rest.
  * If the recursive call returns `true`, a valid coloring for the whole graph exists; propagate `true` upwards (early exit — the first solution is returned).
  * If the recursive call returns `false` (dead end), undo the assignment (`vertexColor[vertex] = 0`) and try the next color.
* If no color leads to a solution, return `false` to backtrack.

```java
    private static boolean isSafe(int vertex, int color) {
        for (int i = 0; i < numVertices; i++) {
            if (graph[vertex][i] == 1 && vertexColor[i] == color) {
                return false;
            }
        }
        return true;
    }
```

* `isSafe(vertex, color)` scans all vertices `i`.

  * If there is an edge between `vertex` and `i` (`graph[vertex][i] == 1`) and `i` is already assigned the same `color` (`vertexColor[i] == color`), then assigning `color` to `vertex` would violate the coloring constraint → return `false`.
  * If no conflict is found among neighbors, return `true`.

```java
    private static void printSolution() {
        System.out.println("Solution Found: Coloring of vertices");
        for (int v = 0; v < numVertices; v++) {
            System.out.println("Vertex " + v + " --> Color " + vertexColor[v]);
        }
    }
}
```

* `printSolution` prints the color assigned to each vertex in a readable format.

---

# What the code guarantees

* If a coloring with `numColors` exists, the program will find one and print it (it returns the first valid coloring discovered by its search order).
* If no such coloring exists, it prints `"No solution exists..."`.

It does **not**:

* Guarantee the minimum number of colors (it just checks a fixed `numColors`).
* Find all possible colorings (it stops at the first).

---

# Data structures used

* `int[][] graph` — adjacency matrix representation of the graph. Good for small dense graphs; O(1) adjacency checks but O(V²) memory.
* `int[] vertexColor` — array mapping vertex index → color.
* Recursion (implicit call stack) to hold partial assignments & search state.

---

# Time complexity (worst-case)

Let `V = numVertices` and `C = numColors`.

* The algorithm is naive backtracking with branching factor up to `C` at each vertex: worst-case number of assignments tried is `C^V` (try all color combinations).
* For each attempted assignment, `isSafe` scans up to `V` neighbors (loop over `i = 0..V-1`) to check conflicts. So a simple upper bound on running time is:

[
\text{Time} = O(V \cdot C^V)
]

* More loosely: exponential in the number of vertices. For small graphs and small `C` this is fine; it becomes intractable quickly as `V` grows.

(If you consider the adjacency matrix check cost, you may say (O(C^V \cdot V^2)) if you account for repeated checks across recursion levels more conservatively — but the common concise bound is (O(V \cdot C^V)).)

---

# Space complexity

* `vertexColor[]` uses `O(V)` memory.
* Recursion depth is at most `V` (one frame per vertex) → `O(V)` stack space.
* Adjacency matrix uses `O(V^2)` space. So overall space: `O(V^2)` dominated by the matrix, or `O(V)` if the graph were stored as adjacency lists.

---

# Correctness and behavior notes

* The algorithm is **complete** for finite graphs: if a valid coloring with `C` colors exists, the algorithm will find it (because it exhaustively tries all possibilities in its search order).
* It is **sound**: any coloring it prints obeys the adjacency constraint because of the `isSafe` checks.
* It returns only **one** solution (first found according to vertex ordering and the color loop order). To enumerate all solutions you would remove the early `return true` and collect or print colorings when `vertex == numVertices`.

---

# Practical improvements / optimizations

This plain backtracking is OK for small inputs but easy to speed up:

1. **Vertex ordering / heuristic (MRV / degree heuristic)**

   * Choose the next vertex with the highest degree or with the fewest legal colors remaining (Most Constrained Variable). This drastically prunes the search tree.

2. **Forward checking**

   * When you assign a color to a vertex, remove that color from the domains of adjacent unassigned vertices. If any neighbor loses all legal colors, backtrack immediately.

3. **Constraint propagation / DSATUR**

   * Use saturation degree (number of different colors used by neighbors) ordering — a powerful heuristic for coloring.

4. **Use adjacency lists for sparse graphs**

   * Replace the adjacency matrix with adjacency lists to speed `isSafe` to iterate only over actual neighbors (`degree(vertex)` rather than `V`).

5. **Symmetry breaking**

   * Fix the color of the first vertex to `1` (or otherwise canonicalize) to reduce equivalent colorings produced by color permutations.

6. **Bitsets and incremental checks**

   * Use bitmasks to track used colors among neighbors for O(1) checks and fast updates.

---

# Short pseudocode

```
function GraphColoring(graph, numColors):
  V = number of vertices
  color[0..V-1] = 0    // 0 means unassigned

  if backtrack(0): 
    print color[]
  else
    print "no solution"

function backtrack(vertex):
  if vertex == V:
    return true        // all vertices colored

  for c in 1..numColors:
    if isSafe(vertex, c):
      color[vertex] = c
      if backtrack(vertex + 1): return true
      color[vertex] = 0  // undo (backtrack)

  return false

function isSafe(vertex, c):
  for each neighbor u of vertex:
    if color[u] == c: return false
  return true
```

---

# Example run (on given graph)

With the provided adjacency matrix and `numColors = 3`, the program will try to color vertex `0` with color `1`, then recursively color `1`, etc., backtracking as needed. Because 3 colors are usually sufficient for many small graphs, it is likely to find a solution quickly; the exact coloring printed depends on the loop ordering of vertices and colors.

---

# Final notes

* This is a clear, minimal and correct backtracking solver for graph coloring. It’s an excellent base to learn from before adding heuristics (MRV), propagation (forward checking), or more advanced search strategies (DSATUR, local search).
* If you want, I can:

  * provide an improved implementation that uses adjacency lists + MRV + forward checking, or
  * modify this code to **enumerate all** valid colorings, or
  * explain how to adapt it to print the chromatic number by trying `numColors = 1..V`.

Which of those would you like next?

*/