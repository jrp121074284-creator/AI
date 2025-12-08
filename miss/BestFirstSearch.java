package MissionariesAndCannibals;

import java.util.*;

public class BestFirstSearch {

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

    private static int heuristic(State s) {
        return s.missionariesLeft + s.cannibalsLeft;
    }

    public static void bestFirstSearch(int totalMissionaries, int totalCannibals) {
        Set<String> visitedStates = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> heuristic(s)));

        State startState = new State(totalMissionaries, totalCannibals, true);
        pq.add(startState);
        visitedStates.add(startState.toString());
        parentMap.put(startState.toString(), null);

        while (!pq.isEmpty()) {
            State currentState = pq.poll();

            if (currentState.missionariesLeft == 0 &&
                currentState.cannibalsLeft == 0 &&
                !currentState.boatOnLeft) {

                printSolutionPath(totalMissionaries, totalCannibals, parentMap, currentState.toString());
                return;
            }

            int[][] possibleMoves = {
                    {1, 0}, {2, 0}, {0, 1}, {0, 2}, {1, 1}
            };

            List<State> nextStates = new ArrayList<>();
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
                nextStates.add(nextState);
            }

            for (State nextState : nextStates) {
                if (isValidState(totalMissionaries, totalCannibals, nextState)
                        && !visitedStates.contains(nextState.toString())) {
                    visitedStates.add(nextState.toString());
                    parentMap.put(nextState.toString(), currentState.toString());
                    pq.add(nextState);
                }
            }
        }
    }


    private static boolean isValidState(int totalMissionaries, int totalCannibals, State state) {
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

    private static void printSolutionPath(int totalMissionaries, int totalCannibals, Map<String, String> parentMap, String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (Best First Search):");
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
        int totalMissionaries = 3, totalCannibals = 3;
        bestFirstSearch(totalMissionaries, totalCannibals);
    }
}


/*
Nice — this is a clean greedy **Best-First Search** solver for the classic **Missionaries and Cannibals** problem. Below I’ll:

1. state the problem very briefly,
2. explain the code **line-by-line / block-by-block**,
3. list the data structures used,
4. give time & space complexity (practical + theoretical),
5. discuss correctness, guarantees and small pitfalls, and
6. give a short, clear pseudocode you can copy.

# Problem statement (short)

There are `M` missionaries and `C` cannibals on the left bank of a river. A boat can carry 1 or 2 people. You must move everyone to the right bank without ever leaving more cannibals than missionaries on either bank (unless there are zero missionaries there). Find a sequence of legal boat crossings that transports everyone safely.

---

# High-level idea of this program

* A **state** = `(missionariesLeft, cannibalsLeft, boatOnLeft?)`.
* Best-First Search uses the **heuristic** `h(s) = missionariesLeft + cannibalsLeft` (i.e. number of people still on left).
* The search expands the state that looks *closest* to the goal (fewest people left). It generates all legal boat moves (1 or 2 people, combos of missionaries/cannibals) and enqueues valid, previously unseen states ordered by `h`.
* When the state `(0,0,boatOnLeft=false)` is popped, the program reconstructs and prints the path using `parentMap`.

---

# Line-by-line / block explanation

```java
package MissionariesAndCannibals;

import java.util.*;
```

* Package and imports. `java.util.*` supplies `Set`, `Map`, `PriorityQueue`, etc.

```java
public class BestFirstSearch {
```

* Top-level class.

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

* `State` stores the three integers describing a configuration.
* `toString()` returns a compact key like `"3,3,L"` used by `visitedStates` and `parentMap`. Using a string simplifies hashing and map lookup.

```java
    private static int heuristic(State s) {
        return s.missionariesLeft + s.cannibalsLeft;
    }
```

* Simple heuristic: number of people still on left bank. Lower = nearer to goal. Admissible in the sense it never overestimates number of *people* left, but it does **not** measure number of moves (so greedy may not be optimal in steps).

```java
    public static void bestFirstSearch(int totalMissionaries, int totalCannibals) {
        Set<String> visitedStates = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> heuristic(s)));
```

* `visitedStates`: prevents revisiting same configuration (pruning). Stored as `String` keys (`State.toString()`).
* `parentMap`: `childKey -> parentKey` used to reconstruct the path when goal found.
* `pq`: priority queue ordered by heuristic value (smallest `h` first). This is classic greedy best-first search.

```java
        State startState = new State(totalMissionaries, totalCannibals, true);
        pq.add(startState);
        visitedStates.add(startState.toString());
        parentMap.put(startState.toString(), null);
```

* Initialize with start: all people on left, boat on left (`true`). Mark visited and set parent `null`.

```java
        while (!pq.isEmpty()) {
            State currentState = pq.poll();
```

* Main loop: pop the state with smallest `h`.

```java
            if (currentState.missionariesLeft == 0 &&
                currentState.cannibalsLeft == 0 &&
                !currentState.boatOnLeft) {

                printSolutionPath(totalMissionaries, totalCannibals, parentMap, currentState.toString());
                return;
            }
```

* Goal test: zero people on left, boat on right. If satisfied, reconstruct and print the path and exit.

```java
            int[][] possibleMoves = {
                    {1, 0}, {2, 0}, {0, 1}, {0, 2}, {1, 1}
            };
```

* Encodes the 5 legal boat payloads: 1M, 2M, 1C, 2C, 1M+1C.

```java
            List<State> nextStates = new ArrayList<>();
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
                nextStates.add(nextState);
            }
```

* For each possible move, compute the successor state depending on boat side:

  * If boat on left: move people from left to right (subtract).
  * Else: move people from right to left (add to left).
* `newBoatOnLeft` flips the boat side for every move.
* This loop generates *candidate* successor states (some will be invalid and filtered next).

```java
            for (State nextState : nextStates) {
                if (isValidState(totalMissionaries, totalCannibals, nextState)
                        && !visitedStates.contains(nextState.toString())) {
                    visitedStates.add(nextState.toString());
                    parentMap.put(nextState.toString(), currentState.toString());
                    pq.add(nextState);
                }
            }
        }
    }
```

* For each candidate:

  * `isValidState(...)` enforces bounds and safety (no side with more cannibals than missionaries, unless missionaries are zero).
  * Only if valid and not visited: mark visited, set parent, and push into the PQ.

```java
    private static boolean isValidState(int totalMissionaries, int totalCannibals, State state) {
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

* Validity checks:

  * Bounds: left counts not negative and not exceed totals.
  * Safety: left bank must not have cannibals > missionaries (unless no missionaries), and same for right bank.
* Ensures we never add illegal states to the frontier.

```java
    private static void printSolutionPath(int totalMissionaries, int totalCannibals, Map<String, String> parentMap, String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);
```

* Standard path reconstruction by following parent pointers from goal to start, then reverse to start→goal.

```java
        System.out.println("Steps to solve (Best First Search):");
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

* Parses each state string and prints a readable line showing left & right counts and boat position.

```java
    public static void main(String[] args) {
        int totalMissionaries = 3, totalCannibals = 3;
        bestFirstSearch(totalMissionaries, totalCannibals);
    }
}
```

* Runs the search for the classic `3M,3C` instance.

---

# Data structures used (summary)

* `State` objects (small POD).
* `PriorityQueue<State>` ordered by `heuristic(state)`.
* `HashSet<String> visitedStates` — fast O(1) membership check, prevents re-expansion.
* `HashMap<String,String> parentMap` — stores parent links for path reconstruction.
* small `List<State>` to hold generated successors temporarily.

---

# Time complexity

Let `M = totalMissionaries`, `C = totalCannibals`. The full state space size is bounded by `(M+1) * (C+1) * 2` (missionariesLeft 0..M, cannibalsLeft 0..C, boat side 2). That’s small for typical inputs (e.g., 4×4×2=32 states for 3×3).

* **Worst-case time:** you may visit every valid state once, and for each popped state you try up to 5 moves and do constant work (validity checks, PQ operations). So time ~ `O(S log S)` where `S` is number of valid states (the `log` from PQ operations). In big-O terms (abstractly) you can say **exponential** in input size for larger parameterizations, but for fixed small `M,C` it’s essentially `O(S log S)`.
* If you consider larger generalized problem with parameter `n`, complexity becomes exponential in `n` (search problems of this kind are typically exponential).

Practically for `3,3` this is tiny — program finishes instantly.

---

# Space complexity

* `visitedStates` and `parentMap` hold up to `O(S)` strings, where `S` is number of reachable valid states.
* `pq` holds frontier up to `O(S)`.
* So space = `O(S)`.

---

# Correctness, guarantees & caveats

**What it guarantees:**

* The program will find a solution if one is reachable and will stop (search space finite) because visited states prevent infinite loops.

**What it does *not* guarantee:**

* Best-First using `h = people left` is **greedy**, not A*. It does not minimize the number of steps — it always expands the state with fewest people left, but that may not have the fewest remaining moves. So the returned path may not be the shortest sequence of crossings (in number of boat trips). It’s *fast* and often finds a valid solution quickly, but not necessarily optimal.

**Good aspects of this implementation:**

* `isValidState` properly enforces problem constraints.
* `visitedStates` prevents duplicate exploration.

**Potential improvements / notes:**

* If you need the **shortest** solution (fewest boat trips), use **A*** with a consistent `f = g + h`, where `g` = number of moves so far and `h` is an admissible estimate of remaining moves (e.g., `ceil((missionariesLeft + cannibalsLeft)/2)` might estimate minimal boat trips remaining). A* would require storing `g` values and handling PQ stale entries as discussed earlier.
* `toString()` is convenient; using a compact integer encoding (bitfields) would be slightly faster and use less memory.
* The heuristic here is admissible regarding number of people remaining but not directly estimating moves (so not admissible for move-count optimality); if you want admissible `h` for moves choose a lower bound on number of trips needed (people/boatCapacity rounded up plus boat direction considerations).
* The code marks visited when enqueuing — this is correct for greedy search & prevents multiple enqueues of same state.

---

# Short pseudocode (concise)

```
function BestFirst(totalM, totalC):
  start = State(totalM, totalC, boatOnLeft=true)
  visited = set()
  parent = map()
  pq = priority queue ordered by heuristic(s) = s.mLeft + s.cLeft

  pq.push(start); visited.add(start.key); parent[start.key] = null

  while pq not empty:
    current = pq.pop()
    if current.mLeft == 0 and current.cLeft == 0 and boat on right:
      return reconstruct_path(parent, current)
    for each move in [{1,0},{2,0},{0,1},{0,2},{1,1}]:
      next = applyMove(current, move)  // subtract or add depending on boat side
      if isValid(next, totalM, totalC) and next.key not in visited:
        visited.add(next.key)
        parent[next.key] = current.key
        pq.push(next)
  return failure
```

---

# Final notes & recommendation

* For learning / demonstration this implementation is clear and concise.
* If your goal is **any** valid solution quickly — keep it as-is.
* If you want the **shortest** solution in number of crossings, I can:

  * convert this to **A*** with `g` tracking and a better admissible `h` for moves, or
  * implement a BFS (guaranteed shortest path when each crossing counts 1) — which is very natural here because every move has equal cost.

Which do you want next — A* (optimal using a move-based heuristic) or BFS (guaranteed shortest path and easy to implement)?
 */