package WaterJug;

import java.util.*;

public class WaterJugDFS {
    static class State {
        int jug1, jug2;
        State(int a, int b) {
            this.jug1 = a;
            this.jug2 = b;
        }
        public String toString() {
            return "(" + jug1 + ", " + jug2 + ")";
        }
    }

    static int cap1 = 4, cap2 = 3, target = 2;
    static Set<String> visited = new HashSet<>();
    static Map<String, String> parent = new HashMap<>();

    public static boolean dfs(State cur) {
       
        if (cur.jug1 == target || cur.jug2 == target) {
            printPath(cur.toString());
            return true;
        }

        visited.add(cur.toString());

        List<State> nextStates = new ArrayList<>();

        // 1. Fill Jug1
        nextStates.add(new State(cap1, cur.jug2));
        // 2. Fill Jug2
        nextStates.add(new State(cur.jug1, cap2));
        // 3. Empty Jug1
        nextStates.add(new State(0, cur.jug2));
        // 4. Empty Jug2
        nextStates.add(new State(cur.jug1, 0));
        // 5. Pour Jug1 -> Jug2
        int pourToJug2 = Math.min(cur.jug1, cap2 - cur.jug2);
        nextStates.add(new State(cur.jug1 - pourToJug2, cur.jug2 + pourToJug2));
        // 6. Pour Jug2 -> Jug1
        int pourToJug1 = Math.min(cur.jug2, cap1 - cur.jug1);
        nextStates.add(new State(cur.jug1 + pourToJug1, cur.jug2 - pourToJug1));

        for (State s : nextStates) {
            if (!visited.contains(s.toString())) {
                parent.put(s.toString(), cur.toString());
                if (dfs(s)) return true; // stop when found
            }
        }
        return false;
    }

    private static void printPath(String end) {
        List<String> path = new ArrayList<>();
        String cur = end;
        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }
        Collections.reverse(path);
        System.out.println("Steps to solve:");
        for (String step : path) {
            System.out.println(step);
        }
    }

    public static void main(String[] args) {
        State start = new State(0, 0);
        parent.put(start.toString(), null);
        if (!dfs(start)) {
            System.out.println("No solution found!");
        }
    }
}

/*
Here’s a **detailed explanation** of your **Water Jug Problem using DFS (Depth-First Search)** code, including line-by-line explanation, problem statement, data structures, complexities, and pseudocode.

---

# **Problem Statement**

* **Given:** Two jugs with capacities `cap1` and `cap2` (e.g., 4 liters and 3 liters) and unlimited water supply.
* **Goal:** Measure exactly `target` liters (e.g., 2 liters) using the jugs.
* **Allowed operations:**

  1. Fill a jug completely.
  2. Empty a jug.
  3. Pour water from one jug to the other until either the first is empty or the second is full.
* **Objective:** Find **steps** to reach the target using **DFS**.

---

# **Step-by-Step Explanation**

---

### **State Class**

```java
static class State {
    int jug1, jug2;
    State(int a, int b) { this.jug1 = a; this.jug2 = b; }
    public String toString() { return "(" + jug1 + ", " + jug2 + ")"; }
}
```

* Represents a **state of the system**: `(amount in jug1, amount in jug2)`.
* `toString()` is used for **hashing** in visited states and **tracking the path**.

---

### **Global Variables**

```java
static int cap1 = 4, cap2 = 3, target = 2;
static Set<String> visited = new HashSet<>();
static Map<String, String> parent = new HashMap<>();
```

* `cap1`, `cap2`, `target` → jug capacities and target volume.
* `visited` → to **avoid cycles** in DFS.
* `parent` → to reconstruct the **solution path**.

---

### **DFS Function**

```java
public static boolean dfs(State cur) {
```

* DFS explores **one path completely** before backtracking.

```java
if (cur.jug1 == target || cur.jug2 == target) {
    printPath(cur.toString());
    return true;
}
```

* If **target is reached**, print solution and terminate recursion.

```java
visited.add(cur.toString());
```

* Mark current state as **visited** to avoid revisiting.

---

### **Generate Next States**

```java
List<State> nextStates = new ArrayList<>();
nextStates.add(new State(cap1, cur.jug2));  // Fill jug1
nextStates.add(new State(cur.jug1, cap2));  // Fill jug2
nextStates.add(new State(0, cur.jug2));     // Empty jug1
nextStates.add(new State(cur.jug1, 0));     // Empty jug2

int pourToJug2 = Math.min(cur.jug1, cap2 - cur.jug2);
nextStates.add(new State(cur.jug1 - pourToJug2, cur.jug2 + pourToJug2)); // Pour jug1 -> jug2

int pourToJug1 = Math.min(cur.jug2, cap1 - cur.jug1);
nextStates.add(new State(cur.jug1 + pourToJug1, cur.jug2 - pourToJug1)); // Pour jug2 -> jug1
```

* Enumerates **all possible moves** from current state.

---

### **DFS Recursive Exploration**

```java
for (State s : nextStates) {
    if (!visited.contains(s.toString())) {
        parent.put(s.toString(), cur.toString());
        if (dfs(s)) return true; // stop when found
    }
}
```

* For each **unvisited state**, recursively explore.
* Stops recursion immediately if **solution found**.

```java
return false;
```

* If **all paths explored** and target not reached, return false.

---

### **Print Path Function**

```java
private static void printPath(String end) {
    List<String> path = new ArrayList<>();
    String cur = end;
    while (cur != null) {
        path.add(cur);
        cur = parent.get(cur);
    }
    Collections.reverse(path);
    System.out.println("Steps to solve:");
    for (String step : path) {
        System.out.println(step);
    }
}
```

* Reconstructs path from **goal to start** using `parent`.
* Prints solution **step by step**.

---

### **Main Function**

```java
public static void main(String[] args) {
    State start = new State(0, 0);
    parent.put(start.toString(), null);
    if (!dfs(start)) {
        System.out.println("No solution found!");
    }
}
```

* Initializes **empty jugs** as starting state `(0,0)`.
* Calls DFS to explore all possible paths to target.

---

# **Data Structures Used**

| Structure                    | Purpose                                               |
| ---------------------------- | ----------------------------------------------------- |
| `State` class                | Represents jug configuration `(jug1, jug2)`           |
| `Set<String> visited`        | Prevent revisiting same state (avoids infinite loops) |
| `Map<String, String> parent` | Track path for reconstructing solution                |
| `List<State>`                | Holds **next possible states** for DFS                |

---

# **Time Complexity**

* **Number of states:** `(cap1+1) * (cap2+1)`
* **DFS explores all states in worst case:** O(cap1 * cap2)
* **Each state generates 6 possible moves** → still O(cap1 * cap2) overall.

**Practical:** Efficient for small jug capacities.

---

# **Space Complexity**

* `visited` → O(cap1 * cap2)
* `parent` → O(cap1 * cap2)
* **Recursion stack** → max depth = O(cap1 * cap2)
* **Overall:** O(cap1 * cap2)

---

# **Pseudo Code (Short)**

```
Start state = (0, 0)
Visited = {}
Parent[start] = null

DFS(state):
    if state.jug1 == target or state.jug2 == target:
        print path
        return true

    mark state as visited
    nextStates = generate all possible moves
    for s in nextStates:
        if s not visited:
            parent[s] = state
            if DFS(s) return true
    return false

if DFS(start) == false:
    print "No solution"
```

---

# ✅ **Summary**

* Solves **Water Jug Problem** using **DFS**.
* Explores **one path fully** before backtracking.
* Stops when **target reached**.
* Efficient for **small jug capacities**, but may be slow for larger states.
* Uses simple **Set and Map** to track visited states and path.

---

I can also make a **comparison table of DFS vs BFS vs Best-First Search** for Water Jug Problem if you want, showing **when each approach is better**.

Do you want me to create that?
 */