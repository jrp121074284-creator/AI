package WaterJug;

import java.util.*;

class WaterJugDLS {
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
    static int depthLimit = 10; 

    public static boolean dls(State cur, int depth) {
        
        if (depth > depthLimit) return false;

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

        // Recursive DLS
        for (State s : nextStates) {
            if (!visited.contains(s.toString())) {
                parent.put(s.toString(), cur.toString());
                if (dls(s, depth + 1)) return true; // found solution
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
        if (!dls(start, 0)) {
            System.out.println("No solution found within depth limit " + depthLimit);
        }
    }
}


/*
Sure! Let’s go **step by step** and explain your **Water Jug DLS (Depth-Limited Search) Java program** in detail.

---

# **Problem Statement**

* You have **two jugs** with capacities `cap1` and `cap2`.
* Goal: Measure exactly **target liters** in one of the jugs.
* **Allowed actions:**

  1. Fill either jug completely.
  2. Empty either jug.
  3. Pour from one jug to the other until one is empty or the other is full.
* Use **Depth-Limited Search (DLS)** with a **depth limit** to find a solution.

---

# **Code Explanation (Step by Step)**

---

### **State Class**

```java
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
```

* Represents **current water in Jug1 and Jug2**.
* `toString()` gives a **unique string representation** for each state.
* Example: `(4,2)` means Jug1 has 4 liters, Jug2 has 2 liters.

---

### **Global Variables**

```java
static int cap1 = 4, cap2 = 3, target = 2;
static Set<String> visited = new HashSet<>();
static Map<String, String> parent = new HashMap<>();
static int depthLimit = 10; 
```

* `cap1, cap2` → Jug capacities.
* `target` → Goal amount.
* `visited` → Tracks states already visited to avoid loops.
* `parent` → Stores previous state for **path reconstruction**.
* `depthLimit` → Max depth allowed for **DLS recursion**.

---

### **Depth-Limited Search Function**

```java
public static boolean dls(State cur, int depth) {
    if (depth > depthLimit) return false;
```

* If the **current depth exceeds limit**, stop recursion.

```java
    if (cur.jug1 == target || cur.jug2 == target) {
        printPath(cur.toString());
        return true;
    }
```

* If the **goal state** is reached, print the solution path and return `true`.

```java
    visited.add(cur.toString());
```

* Mark the current state as **visited** to avoid revisiting.

---

### **Generating Next States**

```java
List<State> nextStates = new ArrayList<>();

// Fill Jug1
nextStates.add(new State(cap1, cur.jug2));
// Fill Jug2
nextStates.add(new State(cur.jug1, cap2));
// Empty Jug1
nextStates.add(new State(0, cur.jug2));
// Empty Jug2
nextStates.add(new State(cur.jug1, 0));
// Pour Jug1 -> Jug2
int pourToJug2 = Math.min(cur.jug1, cap2 - cur.jug2);
nextStates.add(new State(cur.jug1 - pourToJug2, cur.jug2 + pourToJug2));
// Pour Jug2 -> Jug1
int pourToJug1 = Math.min(cur.jug2, cap1 - cur.jug1);
nextStates.add(new State(cur.jug1 + pourToJug1, cur.jug2 - pourToJug1));
```

* Generates **all possible moves** from the current state.
* Includes filling, emptying, and pouring between jugs.

---

### **Recursive DLS Call**

```java
for (State s : nextStates) {
    if (!visited.contains(s.toString())) {
        parent.put(s.toString(), cur.toString());
        if (dls(s, depth + 1)) return true; // found solution
    }
}
```

* For each **next state**:

  * If not visited → mark parent and recursively call DLS with `depth + 1`.
  * If **solution found**, return `true`.

```java
return false;
```

* If all next states are explored and **no solution**, return `false`.

---

### **Printing Path**

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

* Reconstructs path from **goal to start** using the `parent` map.
* Prints the **sequence of states** to reach the goal.

---

### **Main Method**

```java
public static void main(String[] args) {
    State start = new State(0, 0);
    parent.put(start.toString(), null);
    if (!dls(start, 0)) {
        System.out.println("No solution found within depth limit " + depthLimit);
    }
}
```

* Creates initial state `(0,0)` and sets its parent to `null`.
* Calls `dls()` starting at depth `0`.
* If solution not found within `depthLimit`, prints a message.

---

# **Data Structures Used**

| Structure            | Purpose                                               |
| -------------------- | ----------------------------------------------------- |
| `Set<String>`        | Track visited states.                                 |
| `Map<String,String>` | Store parent states for path reconstruction.          |
| `List<State>`        | Temporarily stores next possible states in each step. |

---

# **Time and Space Complexity**

* Maximum number of states = `(cap1 + 1) * (cap2 + 1)`
* **Time Complexity (worst case):** `O(branch^depthLimit)` → exponential in `depthLimit`.
* **Space Complexity:**

  * Call stack for recursion → `O(depthLimit)`
  * Visited and parent map → `O(cap1 * cap2)`

> ⚠ Note: DLS may **not explore all states** if `depthLimit` is too small.

---

# **Pseudo Code (Short)**

```
function DLS(current, depth):
    if depth > depthLimit:
        return false
    if current jug1 or jug2 == target:
        printPath(current)
        return true
    mark current as visited
    nextStates = generate all possible moves
    for each next in nextStates:
        if next not visited:
            parent[next] = current
            if DLS(next, depth+1) return true
    return false
```

---

# ✅ **Summary**

* Solves **Water Jug Problem** using **Depth-Limited Search**.
* Recursively explores possible states **up to a max depth**.
* Efficient in terms of memory compared to BFS but **may fail if solution depth exceeds limit**.
* Uses `Set` for visited states and `Map` for reconstructing the solution path.

---

If you want, I can also **draw a diagram showing DLS exploration for the 4L/3L/2L example**, which makes it very easy to visualize how recursion works.

Do you want me to do that?
 */