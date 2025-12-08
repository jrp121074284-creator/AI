package WaterJug;

import java.util.*;

public class WaterJugBFS{
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

    public static void solve(int cap1, int cap2, int target) {
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>(); // To trace path

        State start = new State(0, 0);
        queue.add(start);
        visited.add(start.toString());
        parent.put(start.toString(), null);

        while (!queue.isEmpty()) {
            State cur = queue.poll();

            if (cur.jug1 == target || cur.jug2 == target) {
                printPath(parent, cur.toString());
                return;
            }

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
                String key = s.toString();
                if (!visited.contains(key)) {
                    visited.add(key);
                    parent.put(key, cur.toString());
                    queue.add(s);
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
        int capacityJug1 = 4;
        int capacityJug2 = 3;
        int target = 2;

        solve(capacityJug1, capacityJug2, target);
    }
}


/*
Sure! Let's go **step by step** and explain your **Water Jug BFS Java program** in detail.

---

# **Problem Statement**

* You have **two jugs** with capacities `cap1` and `cap2`.
* Goal: Measure exactly **target liters** in one of the jugs.
* **Allowed actions:**

  1. Fill either jug completely.
  2. Empty either jug.
  3. Pour from one jug to the other until one is empty or the other is full.
* Use **Breadth-First Search (BFS)** to find **minimum steps** to reach the target.

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
* `toString()` gives a **unique representation** for each state.
* Example: `(4,2)` means Jug1 has 4 liters, Jug2 has 2 liters.

---

### **Solve Function**

```java
public static void solve(int cap1, int cap2, int target) {
    Queue<State> queue = new LinkedList<>();
    Set<String> visited = new HashSet<>();
    Map<String, String> parent = new HashMap<>();
```

* `queue` → **BFS queue** to explore states level by level.
* `visited` → keeps track of **already explored states** to avoid cycles.
* `parent` → maps each state to its **previous state** to reconstruct the path.

---

### **Starting State**

```java
State start = new State(0, 0);
queue.add(start);
visited.add(start.toString());
parent.put(start.toString(), null);
```

* Initial state: **both jugs empty** `(0,0)`.
* Add to queue, mark visited, and set parent to `null`.

---

### **BFS Loop**

```java
while (!queue.isEmpty()) {
    State cur = queue.poll();

    if (cur.jug1 == target || cur.jug2 == target) {
        printPath(parent, cur.toString());
        return;
    }
```

* Take the **front state** from the queue.
* If **goal reached**, print the path and return.

---

### **Generating Next States**

```java
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
```

* Generates **all possible moves** from current state:

  1. Fill Jug1 or Jug2 completely
  2. Empty Jug1 or Jug2
  3. Pour Jug1 → Jug2 or Jug2 → Jug1

---

### **Processing Next States**

```java
for (State s : nextStates) {
    String key = s.toString();
    if (!visited.contains(key)) {
        visited.add(key);
        parent.put(key, cur.toString());
        queue.add(s);
    }
}
```

* For each new state:

  * If not visited → mark visited, set parent, and add to queue.
* Ensures BFS explores **minimum steps first**.

---

### **Printing Path**

```java
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
```

* Backtracks from **goal to start** using `parent` map.
* Prints **sequence of states** leading to solution.

---

### **Main Method**

```java
public static void main(String[] args) {
    int capacityJug1 = 4;
    int capacityJug2 = 3;
    int target = 2;

    solve(capacityJug1, capacityJug2, target);
}
```

* Defines jug capacities and target.
* Calls `solve()` to run **BFS**.

---

# **Data Structures Used**

| Structure            | Purpose                                  |
| -------------------- | ---------------------------------------- |
| `Queue<State>`       | BFS traversal of states.                 |
| `Set<String>`        | Track visited states to avoid cycles.    |
| `Map<String,String>` | Track parent states to reconstruct path. |
| `List<State>`        | Temporarily holds next possible states.  |

---

# **Time and Space Complexity**

**Time Complexity:**

* Maximum number of states = `(cap1+1) * (cap2+1)`
* Each state is visited **once** → **O(cap1 × cap2)**

**Space Complexity:**

* `queue`, `visited`, `parent` → each stores up to `(cap1+1) × (cap2+1)` → **O(cap1 × cap2)**

---

# **Pseudo Code (Short)**

```
function BFS(cap1, cap2, target):
    start = (0,0)
    queue = [start]
    visited = {start}
    parent[start] = null

    while queue not empty:
        current = queue.pop()
        if current has target: printPath(parent, current) and return

        nextStates = generate all possible moves from current
        for each next in nextStates:
            if next not in visited:
                visited.add(next)
                parent[next] = current
                queue.add(next)
```

---

# ✅ **Summary**

* Solves **Water Jug Problem** using **BFS**.
* BFS ensures **shortest path** in terms of number of steps.
* Efficient for small jug capacities.
* Uses `Queue`, `Set`, and `Map` for **state exploration and path reconstruction**.

---

If you want, I can also make a **diagram showing BFS state exploration** for your 4L/3L/2L example to make it very visual.

Do you want me to do that?
 */