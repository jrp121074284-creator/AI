package WaterJug;

import java.util.*;

public class WaterJugAStar {

    static class State {
        int jug1, jug2;

        State(int a, int b) {
            this.jug1 = a;
            this.jug2 = b;
        }

        @Override
        public String toString() {
            return "(" + jug1 + ", " + jug2 + ")";
        }
    }

    private static int gCost(String state, Map<String, Integer> gValues) {
        return gValues.getOrDefault(state, Integer.MAX_VALUE);
    }

    private static int heuristic(State s, int target) {
        return Math.min(Math.abs(s.jug1 - target), Math.abs(s.jug2 - target));
    }

    private static int getPriority(State s, Map<String, Integer> gValues, int target) {
        return gCost(s.toString(), gValues) + heuristic(s, target);
    }

    public static void aStarSearch(int cap1, int cap2, int target) {
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> gValues = new HashMap<>();

        Comparator<State> comparator = (s1, s2) ->
                Integer.compare(getPriority(s1, gValues, target), getPriority(s2, gValues, target));

        PriorityQueue<State> pq = new PriorityQueue<>(comparator);

        State start = new State(0, 0);
        pq.add(start);
        parentMap.put(start.toString(), null);
        gValues.put(start.toString(), 0);

        while (!pq.isEmpty()) {
            State cur = pq.poll();

            if (cur.jug1 == target || cur.jug2 == target) {
                printPath(parentMap, cur.toString());
                System.out.println("Total steps (cost): " + gCost(cur.toString(), gValues));
                return;
            }

            List<State> nextStates = new ArrayList<>();

            nextStates.add(new State(cap1, cur.jug2));

            nextStates.add(new State(cur.jug1, cap2));

            nextStates.add(new State(0, cur.jug2));

            nextStates.add(new State(cur.jug1, 0));

            int pourToJug2 = Math.min(cur.jug1, cap2 - cur.jug2);
            nextStates.add(new State(cur.jug1 - pourToJug2, cur.jug2 + pourToJug2));

            int pourToJug1 = Math.min(cur.jug2, cap1 - cur.jug1);
            nextStates.add(new State(cur.jug1 + pourToJug1, cur.jug2 - pourToJug1));

            for (State next : nextStates) {
                int tentativeG = gCost(cur.toString(), gValues) + 1;

                if (tentativeG < gCost(next.toString(), gValues)) {
                    gValues.put(next.toString(), tentativeG);
                    parentMap.put(next.toString(), cur.toString());
                    pq.add(next);
                }
            }
        }

        System.out.println("No solution found!");
    }

    private static void printPath(Map<String, String> parentMap, String end) {
        List<String> path = new ArrayList<>();
        String cur = end;

        while (cur != null) {
            path.add(cur);
            cur = parentMap.get(cur);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (A* Search):");
        for (String step : path) {
            System.out.println(step);
        }
    }

    public static void main(String[] args) {
        int capacityJug1 = 4;
        int capacityJug2 = 3;
        int target = 2;

        aStarSearch(capacityJug1, capacityJug2, target);
    }
}


/*
Sure! Let’s go **step by step** and explain your **Water Jug A* Search Java program** in detail.

---

# **Problem Statement**

* We have **two jugs** with capacities `cap1` and `cap2`.
* We want to measure exactly **target liters** using the two jugs.
* **Allowed actions**:

  1. Fill either jug completely.
  2. Empty either jug.
  3. Pour from one jug to the other until one is empty or the other is full.
* Goal: **Find the minimum steps** to reach the target using **A* Search**.

---

# **Code Explanation (Step by Step)**

---

### **Class State**

```java
static class State {
    int jug1, jug2;

    State(int a, int b) {
        this.jug1 = a;
        this.jug2 = b;
    }

    @Override
    public String toString() {
        return "(" + jug1 + ", " + jug2 + ")";
    }
}
```

* Represents **current water in both jugs**.
* `jug1` = amount in Jug1, `jug2` = amount in Jug2.
* `toString()` gives a **unique string** for each state, e.g., `(4, 2)`.

---

### **G-cost Function**

```java
private static int gCost(String state, Map<String, Integer> gValues) {
    return gValues.getOrDefault(state, Integer.MAX_VALUE);
}
```

* `gCost` returns **steps taken to reach this state**.
* If state is unseen, returns **infinity**.

---

### **Heuristic Function**

```java
private static int heuristic(State s, int target) {
    return Math.min(Math.abs(s.jug1 - target), Math.abs(s.jug2 - target));
}
```

* Estimates **distance to goal**.
* `h(n)` = minimum difference between the current jug amounts and the target.
* A small heuristic for A* ensures **admissibility**.

---

### **Priority Function**

```java
private static int getPriority(State s, Map<String, Integer> gValues, int target) {
    return gCost(s.toString(), gValues) + heuristic(s, target);
}
```

* A* priority = **g(n) + h(n)**.

  * `g(n)` = cost so far
  * `h(n)` = heuristic estimate to goal

---

### **A* Search Function**

```java
public static void aStarSearch(int cap1, int cap2, int target) {
    Map<String, String> parentMap = new HashMap<>();
    Map<String, Integer> gValues = new HashMap<>();

    Comparator<State> comparator = (s1, s2) ->
            Integer.compare(getPriority(s1, gValues, target), getPriority(s2, gValues, target));

    PriorityQueue<State> pq = new PriorityQueue<>(comparator);

    State start = new State(0, 0);
    pq.add(start);
    parentMap.put(start.toString(), null);
    gValues.put(start.toString(), 0);
```

* **Parent map**: keeps track of how we reached a state.
* **gValues**: cost to reach each state.
* **PriorityQueue**: explores states with **lowest f(n) = g + h** first.
* **Start state** = both jugs empty `(0, 0)`.

---

### **Main Loop**

```java
while (!pq.isEmpty()) {
    State cur = pq.poll();

    if (cur.jug1 == target || cur.jug2 == target) {
        printPath(parentMap, cur.toString());
        System.out.println("Total steps (cost): " + gCost(cur.toString(), gValues));
        return;
    }
```

* Poll **state with minimum priority** from the queue.
* Check if **goal reached**: either jug has the target amount.
* Print solution path and total steps if goal reached.

---

### **Generating Next States**

```java
List<State> nextStates = new ArrayList<>();

nextStates.add(new State(cap1, cur.jug2)); // Fill Jug1
nextStates.add(new State(cur.jug1, cap2)); // Fill Jug2
nextStates.add(new State(0, cur.jug2));    // Empty Jug1
nextStates.add(new State(cur.jug1, 0));    // Empty Jug2

int pourToJug2 = Math.min(cur.jug1, cap2 - cur.jug2);
nextStates.add(new State(cur.jug1 - pourToJug2, cur.jug2 + pourToJug2)); // Pour Jug1 → Jug2

int pourToJug1 = Math.min(cur.jug2, cap1 - cur.jug1);
nextStates.add(new State(cur.jug1 + pourToJug1, cur.jug2 - pourToJug1)); // Pour Jug2 → Jug1
```

* Generates **all possible next states** from current state.
* Considers: **fill, empty, pour between jugs**.

---

### **Updating Costs and Queue**

```java
for (State next : nextStates) {
    int tentativeG = gCost(cur.toString(), gValues) + 1;

    if (tentativeG < gCost(next.toString(), gValues)) {
        gValues.put(next.toString(), tentativeG);
        parentMap.put(next.toString(), cur.toString());
        pq.add(next);
    }
}
```

* Calculate **tentative cost** for next state = current g + 1 step.
* If it's **better than previous cost**, update gValues and parentMap.
* Add **next state to priority queue**.

---

### **Printing the Path**

```java
private static void printPath(Map<String, String> parentMap, String end) {
    List<String> path = new ArrayList<>();
    String cur = end;
    while (cur != null) {
        path.add(cur);
        cur = parentMap.get(cur);
    }
    Collections.reverse(path);
    System.out.println("Steps to solve (A* Search):");
    for (String step : path) System.out.println(step);
}
```

* Backtracks from goal using `parentMap`.
* Prints **sequence of states** from start to goal.

---

### **Main Method**

```java
public static void main(String[] args) {
    int capacityJug1 = 4;
    int capacityJug2 = 3;
    int target = 2;

    aStarSearch(capacityJug1, capacityJug2, target);
}
```

* **Jug capacities**: 4 liters and 3 liters.
* **Target**: 2 liters.
* Calls **A* search** to solve problem.

---

# **Data Structures Used**

| Structure                      | Purpose                                          |
| ------------------------------ | ------------------------------------------------ |
| `State` class                  | Represents a state (water in jugs).              |
| `PriorityQueue<State>`         | Explores states by minimum `f(n) = g + h`.       |
| `Map<String,Integer> gValues`  | Stores cost to reach each state.                 |
| `Map<String,String> parentMap` | Stores parent to reconstruct path.               |
| `List<State>`                  | Holds possible next states from a current state. |

---

# **Time and Space Complexity**

**Time Complexity:**

* Depends on number of states = `(cap1+1) * (cap2+1)`
* Worst case: each state is visited once → **O(cap1 × cap2 × log(cap1×cap2))** due to priority queue operations.

**Space Complexity:**

* `gValues` + `parentMap` → **O(cap1 × cap2)**
* Priority queue → **O(cap1 × cap2)**
* Overall space → **O(cap1 × cap2)**

---

# **Pseudo Code (Short)**

```
function aStarSearch(cap1, cap2, target):
    start = (0,0)
    g[start] = 0
    pq = priorityQueue ordered by g+heuristic

    while pq not empty:
        current = pq.pop()
        if current has target: print path and return

        nextStates = generate all possible moves from current
        for each next in nextStates:
            tentativeG = g[current] + 1
            if tentativeG < g[next]:
                g[next] = tentativeG
                parent[next] = current
                pq.add(next)
```

**Heuristic:** `h(state) = min(|jug1-target|, |jug2-target|)`

---

# ✅ **Summary**

* Solves **Water Jug Problem** using **A* Search**.
* Explores states intelligently using **priority = g + h**.
* Finds **shortest path in minimum steps**.
* Time/space complexity depends on jug capacities.
* Uses **PriorityQueue, HashMap, and custom State class**.

---

If you want, I can also **draw a step-by-step diagram showing the states explored by A*** for this 4L/3L/2L problem, which makes it very easy to visualize.

Do you want me to do that?
 */