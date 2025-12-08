package MissionariesAndCannibals;

import java.util.*;

public class DLS {

    static class State {
        int missionariesLeft;
        int cannibalsLeft;
        boolean boatOnLeft; 

        State(int missionaries, int cannibals, boolean boat) {
            this.missionariesLeft = missionaries;
            this.cannibalsLeft = cannibals;
            this.boatOnLeft = boat;
        }

        @Override
        public String toString() {
            return missionariesLeft + "," + cannibalsLeft + "," + (boatOnLeft ? "L" : "R");
        }
    }

    static Set<String> visitedStates = new HashSet<>();
    static Map<String, String> parentMap = new HashMap<>();
    static int totalMissionaries = 3, totalCannibals = 3;
    static int depthLimit = 30; 

    public static boolean dfs(State currentState, int depth) {
     
        if (depth > depthLimit) {
            return false;
        }

        if (currentState.missionariesLeft == 0 &&
            currentState.cannibalsLeft == 0 &&
            !currentState.boatOnLeft) {

            printSolutionPath(currentState.toString());
            return true;
        }

        visitedStates.add(currentState.toString());

        int[][] possibleMoves = {
                { 1, 0 }, { 2, 0 }, { 0, 1 }, { 0, 2 }, { 1, 1 }
        };

        for (int[] move : possibleMoves) {
            int moveMissionaries = move[0];
            int moveCannibals = move[1];

            int newMissionariesLeft = currentState.missionariesLeft;
            int newCannibalsLeft = currentState.cannibalsLeft;
            boolean newBoatOnLeft = !currentState.boatOnLeft;

            if (currentState.boatOnLeft) {
                newMissionariesLeft -= moveMissionaries;
                newCannibalsLeft -= moveCannibals;
            } else {
                newMissionariesLeft += moveMissionaries;
                newCannibalsLeft += moveCannibals;
            }

            State nextState = new State(newMissionariesLeft, newCannibalsLeft, newBoatOnLeft);

            if (isValidState(nextState) && !visitedStates.contains(nextState.toString())) {
                parentMap.put(nextState.toString(), currentState.toString());
                if (dfs(nextState, depth + 1)) {
                    return true; 
                }
            }
        }

        return false;
    }

    private static boolean isValidState(State state) {
        int missionariesRight = totalMissionaries - state.missionariesLeft;
        int cannibalsRight = totalCannibals - state.cannibalsLeft;

        if (state.missionariesLeft < 0 || state.cannibalsLeft < 0 ||
            state.missionariesLeft > totalMissionaries || state.cannibalsLeft > totalCannibals)
            return false;

        if ((state.missionariesLeft > 0 && state.missionariesLeft < state.cannibalsLeft) ||
            (missionariesRight > 0 && missionariesRight < cannibalsRight))
            return false;

        return true;
    }

    private static void printSolutionPath(String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (DLS):");
        for (String step : path) {
            String[] parts = step.split(",");
            int missionariesLeft = Integer.parseInt(parts[0]);
            int cannibalsLeft = Integer.parseInt(parts[1]);
            boolean boatOnLeft = parts[2].equals("L");

            System.out.println(
                "Left[Missionaries=" + missionariesLeft + ", Cannibals=" + cannibalsLeft + "] | " +
                "Right[Missionaries=" + (totalMissionaries - missionariesLeft) +
                ", Cannibals=" + (totalCannibals - cannibalsLeft) + "] | " +
                "Boat: " + (boatOnLeft ? "Left" : "Right")
            );
        }
    }

    public static void main(String[] args) {
        State startState = new State(totalMissionaries, totalCannibals, true);
        parentMap.put(startState.toString(), null);

        if (!dfs(startState, 0)) {
            System.out.println("No solution found within depth limit " + depthLimit);
        }
    }
}


/*
Sure! Let’s go **step by step** and explain this **Depth-Limited Search (DLS)** solution for the Missionaries and Cannibals problem in detail.

---

# **Problem Statement**

We have:

* `M` missionaries and `C` cannibals on the left bank of a river.
* A boat that can carry **1 or 2 people**.
* **Constraint:** At any time, cannibals must not outnumber missionaries on either bank (unless no missionaries are present).
* **Goal:** Move all missionaries and cannibals to the right bank **safely**.

**Depth-Limited Search (DLS)** explores the state space using DFS but with a **maximum depth limit** to avoid infinite recursion or going too deep.

---

# **Code Explanation**

```java
package MissionariesAndCannibals;
import java.util.*;
public class DLS {
```

* Package and imports.
* Class `DLS` for Depth-Limited Search.

---

### **State Representation**

```java
    static class State {
        int missionariesLeft;
        int cannibalsLeft;
        boolean boatOnLeft; 

        State(int missionaries, int cannibals, boolean boat) {
            this.missionariesLeft = missionaries;
            this.cannibalsLeft = cannibals;
            this.boatOnLeft = boat;
        }

        @Override
        public String toString() {
            return missionariesLeft + "," + cannibalsLeft + "," + (boatOnLeft ? "L" : "R");
        }
    }
```

* `State` represents a configuration of the problem:

  1. `missionariesLeft`: number on left bank.
  2. `cannibalsLeft`: number on left bank.
  3. `boatOnLeft`: `true` if boat is on left bank.
* `toString()` converts a state into a **string key**, e.g., `"3,3,L"`.

  * Used for storing visited states and parent mapping.

---

### **Global variables**

```java
    static Set<String> visitedStates = new HashSet<>();
    static Map<String, String> parentMap = new HashMap<>();
    static int totalMissionaries = 3, totalCannibals = 3;
    static int depthLimit = 30; 
```

* `visitedStates`: keeps track of visited states to avoid cycles.
* `parentMap`: maps a state → parent state for reconstructing the path.
* `totalMissionaries` / `totalCannibals`: problem size.
* `depthLimit`: maximum recursion depth for DLS. Prevents infinite DFS loops.

---

### **DFS function with depth limit**

```java
    public static boolean dfs(State currentState, int depth) {
        if (depth > depthLimit) {
            return false;
        }
```

* If `depth > depthLimit`, we **stop recursion** and backtrack.

```java
        if (currentState.missionariesLeft == 0 &&
            currentState.cannibalsLeft == 0 &&
            !currentState.boatOnLeft) {

            printSolutionPath(currentState.toString());
            return true;
        }
```

* **Goal check:** All missionaries and cannibals are on the right bank, and the boat is on the right.
* If goal reached → print path and return `true`.

```java
        visitedStates.add(currentState.toString());
```

* Mark current state as **visited**.

```java
        int[][] possibleMoves = {
                { 1, 0 }, { 2, 0 }, { 0, 1 }, { 0, 2 }, { 1, 1 }
        };
```

* The boat can move:

  * 1 or 2 missionaries,
  * 1 or 2 cannibals,
  * 1 missionary + 1 cannibal.

---

### **Generate successor states**

```java
        for (int[] move : possibleMoves) {
            int moveMissionaries = move[0];
            int moveCannibals = move[1];

            int newMissionariesLeft = currentState.missionariesLeft;
            int newCannibalsLeft = currentState.cannibalsLeft;
            boolean newBoatOnLeft = !currentState.boatOnLeft;

            if (currentState.boatOnLeft) {
                newMissionariesLeft -= moveMissionaries;
                newCannibalsLeft -= moveCannibals;
            } else {
                newMissionariesLeft += moveMissionaries;
                newCannibalsLeft += moveCannibals;
            }

            State nextState = new State(newMissionariesLeft, newCannibalsLeft, newBoatOnLeft);
```

* For each allowed boat move, calculate new state:

  1. Toggle boat position.
  2. Adjust left bank counts: subtract if boat moves from left → right, add if boat moves from right → left.

---

### **Validate state and recursive DFS**

```java
            if (isValidState(nextState) && !visitedStates.contains(nextState.toString())) {
                parentMap.put(nextState.toString(), currentState.toString());
                if (dfs(nextState, depth + 1)) {
                    return true; 
                }
            }
        }

        return false;
    }
```

* Check validity of `nextState` and whether it has been visited.
* Record parent state for path reconstruction.
* Recurse with `depth + 1`.
* Return `true` if goal is found, otherwise backtrack.

---

### **State validation**

```java
    private static boolean isValidState(State state) {
        int missionariesRight = totalMissionaries - state.missionariesLeft;
        int cannibalsRight = totalCannibals - state.cannibalsLeft;

        if (state.missionariesLeft < 0 || state.cannibalsLeft < 0 ||
            state.missionariesLeft > totalMissionaries || state.cannibalsLeft > totalCannibals)
            return false;

        if ((state.missionariesLeft > 0 && state.missionariesLeft < state.cannibalsLeft) ||
            (missionariesRight > 0 && missionariesRight < cannibalsRight))
            return false;

        return true;
    }
```

* Ensures state is **within bounds** and **safe** (cannibals never outnumber missionaries on either bank).

---

### **Print solution path**

```java
    private static void printSolutionPath(String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (DLS):");
        for (String step : path) {
            String[] parts = step.split(",");
            int missionariesLeft = Integer.parseInt(parts[0]);
            int cannibalsLeft = Integer.parseInt(parts[1]);
            boolean boatOnLeft = parts[2].equals("L");

            System.out.println(
                "Left[Missionaries=" + missionariesLeft + ", Cannibals=" + cannibalsLeft + "] | " +
                "Right[Missionaries=" + (totalMissionaries - missionariesLeft) +
                ", Cannibals=" + (totalCannibals - cannibalsLeft) + "] | " +
                "Boat: " + (boatOnLeft ? "Left" : "Right")
            );
        }
    }
```

* Reconstructs path from `parentMap` and prints each step in **human-readable format**.

---

### **Main method**

```java
    public static void main(String[] args) {
        State startState = new State(totalMissionaries, totalCannibals, true);
        parentMap.put(startState.toString(), null);

        if (!dfs(startState, 0)) {
            System.out.println("No solution found within depth limit " + depthLimit);
        }
    }
}
```

* Starts DFS at depth `0` from `(3,3,true)`
* Prints the path if found, otherwise outputs a **failure message**.

---

# **Data Structures Used**

| Structure                      | Purpose                                                                   |
| ------------------------------ | ------------------------------------------------------------------------- |
| `State` class                  | Represents a configuration of missionaries, cannibals, and boat position. |
| `Set<String> visitedStates`    | Keeps track of visited states to avoid cycles.                            |
| `Map<String,String> parentMap` | Maps child → parent to reconstruct solution path.                         |
| `int[][] possibleMoves`        | Stores valid boat moves.                                                  |
| `List<String> path`            | Temporary storage to print solution path.                                 |

---

# **Time & Space Complexity**

Let `M` = total missionaries, `C` = total cannibals, `d` = depth limit.

* **State space size:** `(M+1)*(C+1)*2` (missionariesLeft × cannibalsLeft × boat positions).

  * Example: M=C=3 → 32 states.
* **Time complexity:** DFS may explore all states up to `depthLimit` → **O(min(N, b^d))**, where `b=5` (possible moves).
* **Space complexity:**

  * `visitedStates` + `parentMap` → **O(N)**, where `N` is number of unique states.
  * Call stack → O(depthLimit).

> **Note:** DLS may **fail** if the solution requires depth > `depthLimit`. BFS guarantees the shortest path, DLS does not guarantee it unless depth limit is sufficient.

---

# **Pseudo code (short)**

```
function DLS(state, depth):
    if depth > depthLimit: return false
    if state is goal: print path; return true
    mark state as visited
    for each move in possibleMoves:
        nextState = apply move
        if nextState is valid and not visited:
            parent[nextState] = state
            if DLS(nextState, depth+1): return true
    return false

startState = (M, C, boatLeft)
if not DLS(startState, 0):
    print "No solution within depth limit"
```

---

# ✅ **Summary**

* Implements **Depth-Limited Search** for the Missionaries & Cannibals problem.
* Keeps track of visited states and parent mapping for path reconstruction.
* Validates each move for safety.
* Can **fail** if solution depth > `depthLimit`.
* Uses **DFS recursion** and **state space pruning**.

---

If you want, I can also **draw a visual tree of DLS states** for `(3,3)` to show exactly how recursion explores nodes — it’s very helpful to understand DLS vs BFS.

Do you want me to do that?
 */