package WaterJug;

import java.util.*;

class WaterJugBestFirst {
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

    static int target = 2;

    static int heuristic(State s) {
        return Math.min(Math.abs(s.jug1 - target), Math.abs(s.jug2 - target));
    }

    public static void bestFirstSearch(int cap1,int cap2, int target) {
 
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> heuristic(s)));

        State start = new State(0, 0);
        pq.add(start);
        visited.add(start.toString());
        parent.put(start.toString(), null);

        while (!pq.isEmpty()) {
            State cur = pq.poll();

            if (cur.jug1 == target || cur.jug2 == target) {
                printPath(parent, cur.toString());
                return;
            }

            List<State> nextStates = new ArrayList<>();

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
                    visited.add(s.toString());
                    parent.put(s.toString(), cur.toString());
                    pq.add(s);
                }
            }
        }

        System.out.println("No solution found!");
    }

    private static void printPath(Map<String, String> parent, String end) {
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
        
        int cap1 = 4, cap2 = 3;
        bestFirstSearch(cap1, cap2, target);
    }
}

/*
Here’s a **detailed explanation** of your **Water Jug Problem using Best-First Search (BFS with heuristic)** code, including line-by-line explanation, problem statement, data structures, complexities, and pseudocode.

---

# **Problem Statement**

* **Given:** Two jugs with capacities `cap1` and `cap2` (e.g., 4 liters and 3 liters), and an unlimited water supply.
* **Goal:** Measure exactly `target` liters (e.g., 2 liters) using the jugs.
* **Allowed operations:**

  1. Fill a jug completely.
  2. Empty a jug.
  3. Pour water from one jug to the other until either the first is empty or the second is full.
* **Objective:** Find **steps** to reach the target using **Best-First Search** guided by a heuristic.

---

# **Step-by-Step Explanation**

### **State Class**

```java
static class State {
    int jug1, jug2;
    State(int a, int b) { this.jug1 = a; this.jug2 = b; }
    public String toString() { return "(" + jug1 + ", " + jug2 + ")"; }
}
```

* Represents a state `(jug1, jug2)`.
* `toString()` is used for **hashing** in visited states and **path tracking**.

---

### **Heuristic Function**

```java
static int heuristic(State s) {
    return Math.min(Math.abs(s.jug1 - target), Math.abs(s.jug2 - target));
}
```

* **Goal:** guide search toward target efficiently.
* Measures **how close either jug is to the target**.
* Smaller value → closer to goal.

---

### **Best-First Search Function**

```java
public static void bestFirstSearch(int cap1, int cap2, int target) {
```

* **Inputs:** Jug capacities and target amount.
* **Data Structures:**

  * `Set<String> visited` → track visited states.
  * `Map<String, String> parent` → store path to reconstruct solution.
  * `PriorityQueue<State> pq` → selects **state with smallest heuristic** first.

```java
State start = new State(0, 0);
pq.add(start);
visited.add(start.toString());
parent.put(start.toString(), null);
```

* Start from **empty jugs** `(0,0)`.

---

### **Main BFS Loop**

```java
while (!pq.isEmpty()) {
    State cur = pq.poll();
```

* Pop the **state with the smallest heuristic**.

```java
if (cur.jug1 == target || cur.jug2 == target) {
    printPath(parent, cur.toString());
    return;
}
```

* If **target achieved**, print solution path and terminate.

---

### **Generate Next States**

```java
List<State> nextStates = new ArrayList<>();

nextStates.add(new State(cap1, cur.jug2));       // Fill jug1
nextStates.add(new State(cur.jug1, cap2));       // Fill jug2
nextStates.add(new State(0, cur.jug2));          // Empty jug1
nextStates.add(new State(cur.jug1, 0));          // Empty jug2

int pourToJug2 = Math.min(cur.jug1, cap2 - cur.jug2);
nextStates.add(new State(cur.jug1 - pourToJug2, cur.jug2 + pourToJug2));  // Pour jug1 -> jug2

int pourToJug1 = Math.min(cur.jug2, cap1 - cur.jug1);
nextStates.add(new State(cur.jug1 + pourToJug1, cur.jug2 - pourToJug1));  // Pour jug2 -> jug1
```

* Enumerates **all valid moves** from current state.

---

### **Add Next States to Priority Queue**

```java
for (State s : nextStates) {
    if (!visited.contains(s.toString())) {
        visited.add(s.toString());
        parent.put(s.toString(), cur.toString());
        pq.add(s);
    }
}
```

* Only **add unvisited states** to prevent loops.
* Update **parent map** for reconstructing path.
* Priority queue ensures **best-first selection** using heuristic.

---

### **Print Path Function**

```java
private static void printPath(Map<String, String> parent, String end) {
    List<String> path = new ArrayList<>();
    String cur = end;
    while (cur != null) {
        path.add(cur);
        cur = parent.get(cur);
    }
    Collections.reverse(path);
    for (String step : path) System.out.println(step);
}
```

* Traces from **goal state to start state** using parent map.
* Prints **solution steps**.

---

### **Main Function**

```java
public static void main(String[] args) {
    int cap1 = 4, cap2 = 3;
    bestFirstSearch(cap1, cap2, target);
}
```

* Initializes **jug capacities** and calls **Best-First Search**.

---

# **Data Structures Used**

| Structure                    | Purpose                                     |
| ---------------------------- | ------------------------------------------- |
| `State` class                | Represents jug configuration `(jug1, jug2)` |
| `Set<String> visited`        | Avoid revisiting same state                 |
| `Map<String, String> parent` | Track path for solution reconstruction      |
| `PriorityQueue<State>`       | Select next state with minimum heuristic    |

---

# **Time Complexity**

* **Number of possible states:** `(cap1+1) * (cap2+1)`
* **Each state** may generate up to **6 next states**.
* Worst-case: **O((cap1+1)*(cap2+1) * log((cap1+1)*(cap2+1)))**

  * log factor → priority queue operations.
* **Practical:** Very fast for small jugs.

---

# **Space Complexity**

* `visited` → O(cap1 * cap2)
* `parent` → O(cap1 * cap2)
* `priority queue` → O(cap1 * cap2)
* **Overall:** O(cap1 * cap2)

---

# **Pseudo Code (Short)**

```
Start state = (0, 0)
PriorityQueue PQ = start state with heuristic
Visited = {start}
Parent[start] = null

while PQ not empty:
    cur = PQ.pop()
    if cur.jug1 == target or cur.jug2 == target:
        printPath(parent, cur)
        return

    nextStates = generate all valid moves from cur:
        fill jug1, fill jug2, empty jug1, empty jug2, pour jug1->jug2, pour jug2->jug1

    for s in nextStates:
        if s not visited:
            mark visited
            parent[s] = cur
            PQ.add(s with heuristic)

print "No solution found"
```

---

# ✅ **Summary**

* Solves **Water Jug Problem** using **Best-First Search**.
* Uses **heuristic = distance to target** for fast search.
* Efficient for small jug capacities.
* **Data structures:** PriorityQueue, Set, Map.
* **Time complexity:** O((cap1+1)*(cap2+1) * log((cap1+1)*(cap2+1)))
* **Space complexity:** O(cap1 * cap2)

---

If you want, I can also **draw the search tree** for `(4,3)` jug problem to visualize **how BFS explores states using heuristic**.

Do you want me to do that?
 */