package MissionariesAndCannibals;

import java.util.*;

public class AStarSearch {

    static class State {
        int missionariesLeft;
        int cannibalsLeft;
        boolean boatOnLeft;

        State(int missionaries, int cannibals, boolean boat) {
            this.missionariesLeft = missionaries;
            this.cannibalsLeft = cannibals;
            this.boatOnLeft = boat;
        }

        
        public String toString() {
            return missionariesLeft + "," + cannibalsLeft + "," + (boatOnLeft ? "L" : "R");
        }
    }

    private static int gCost(String state, Map<String, Integer> gValues) {
        return gValues.getOrDefault(state, Integer.MAX_VALUE);
    }

    private static int heuristic(State s) {
        return s.missionariesLeft + s.cannibalsLeft;
    }

    private static int getPriority(State s, Map<String, Integer> gValues) {
        return gCost(s.toString(), gValues) + heuristic(s);
    }

    public static void aStarSearch(int totalMissionaries, int totalCannibals) {
        Set<String> visitedStates = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> gValues = new HashMap<>();

        Comparator<State> comparator = (s1, s2) ->
                Integer.compare(getPriority(s1, gValues), getPriority(s2, gValues));

        PriorityQueue<State> pq = new PriorityQueue<>(comparator);

        State startState = new State(totalMissionaries, totalCannibals, true);
        pq.add(startState);
        visitedStates.add(startState.toString());
        parentMap.put(startState.toString(), null);
        gValues.put(startState.toString(), 0);

        while (!pq.isEmpty()) {
            State currentState = pq.poll();

            if (currentState.missionariesLeft == 0 &&
                currentState.cannibalsLeft == 0 &&
                !currentState.boatOnLeft) {

                printSolutionPath(totalMissionaries, totalCannibals, parentMap, currentState.toString());
                System.out.println("Total steps (cost): " + gCost(currentState.toString(), gValues));
                return;
            }

            int[][] possibleMoves = {
                    {1, 0}, {2, 0}, {0, 1}, {0, 2}, {1, 1}
            };
            List<State> next = new ArrayList<>();
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

                next.add(new State(newMissionariesLeft, newCannibalsLeft, newBoatOnLeft));
                
            }
            for(State nextState : next){
                if (isValidState(totalMissionaries, totalCannibals, nextState)) {
                    int tentativeG = gCost(currentState.toString(), gValues) + 1;

                    if (tentativeG < gCost(nextState.toString(), gValues)) {
                        gValues.put(nextState.toString(), tentativeG);
                        parentMap.put(nextState.toString(), currentState.toString());
                        pq.add(nextState);
                    }
                }
            }
        }

        System.out.println("No solution found.");
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

    private static void printSolutionPath(int totalMissionaries, int totalCannibals,
                                          Map<String, String> parentMap, String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (A* Search):");
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
        aStarSearch(totalMissionaries, totalCannibals);
    }
}


/*

# A-star for Missionaries & Cannibals — line-by-line explanation, analysis & pseudocode

Nice program — this implements an **A*** search for the classic *Missionaries and Cannibals* crossing problem. Below I’ll:

* Give a one-line problem statement.
* Explain the code step-by-step (important lines / blocks).
* Describe the algorithm (what it does), data structures used, and important implementation notes (correctness issues and possible bugs).
* Give time & space complexity and the practical state-space bound for the problem.
* Provide a short pseudocode summary and suggested improvements.

---

# Problem statement (short)

There are `M` missionaries and `C` cannibals on the left bank of a river with a boat that carries at most two people. You must move everyone to the right bank without ever leaving more cannibals than missionaries on either bank (unless there are zero missionaries on that bank). Find a sequence of boat crossings that transfers everyone safely.

---

# Code walkthrough (step-by-step)

I'll explain each logical block rather than literally every single line (I'll call out the important lines by quoting them).

```java
package MissionariesAndCannibals;
import java.util.*;
public class AStarSearch {
```

* Package & imports; class declaration.

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

        public String toString() {
            return missionariesLeft + "," + cannibalsLeft + "," + (boatOnLeft ? "L" : "R");
        }
    }
```

* `State` object represents a configuration: how many missionaries/cannibals remain on the **left** bank, and whether the boat is on the left (`true`) or right (`false`).
* `toString()` serializes state as `"M,C,L|R"` which is used as a key in maps/sets.

```java
    private static int gCost(String state, Map<String, Integer> gValues) {
        return gValues.getOrDefault(state, Integer.MAX_VALUE);
    }
```

* Returns the current known `g` (cost from start to that state) stored in `gValues`. If absent, returns `Integer.MAX_VALUE` to mean "unknown / infinite".

```java
    private static int heuristic(State s) {
        return s.missionariesLeft + s.cannibalsLeft;
    }
```

* Heuristic `h(s)` = total number of people left on the left bank.

  * **Note:** this returns number of people, not remaining boat trips. (I’ll discuss admissibility and implications below.)

```java
    private static int getPriority(State s, Map<String, Integer> gValues) {
        return gCost(s.toString(), gValues) + heuristic(s);
    }
```

* `f(s) = g(s) + h(s)`. This is used in the priority comparator.

```java
    public static void aStarSearch(int totalMissionaries, int totalCannibals) {
        Set<String> visitedStates = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> gValues = new HashMap<>();
```

* `visitedStates` (declared but see note below about its usage).
* `parentMap` to reconstruct solution path.
* `gValues` stores best known `g` for each visited state.

```java
        Comparator<State> comparator = (s1, s2) ->
                Integer.compare(getPriority(s1, gValues), getPriority(s2, gValues));

        PriorityQueue<State> pq = new PriorityQueue<>(comparator);
```

* A `PriorityQueue` ordered by `f = g + h`, where `g` is read from `gValues` map. The comparator reads `gValues` at comparison time.

```java
        State startState = new State(totalMissionaries, totalCannibals, true);
        pq.add(startState);
        visitedStates.add(startState.toString());
        parentMap.put(startState.toString(), null);
        gValues.put(startState.toString(), 0);
```

* Initialize start: all people on left, boat on left, `g(start)=0`, parent `null`. Start is pushed to `pq`. Start is also added to `visitedStates` (more on that later).

```java
        while (!pq.isEmpty()) {
            State currentState = pq.poll();

            if (currentState.missionariesLeft == 0 &&
                currentState.cannibalsLeft == 0 &&
                !currentState.boatOnLeft) {

                printSolutionPath(..., currentState.toString());
                System.out.println("Total steps (cost): " + gCost(currentState.toString(), gValues));
                return;
            }
```

* Pop the state with smallest `f`. If it is the goal (no one left on left bank and boat on right), reconstruct and print the path and total cost `g(goal)`.

```java
            int[][] possibleMoves = {
                    {1, 0}, {2, 0}, {0, 1}, {0, 2}, {1, 1}
            };
            List<State> next = new ArrayList<>();
            for (int[] move : possibleMoves) {
                ...
                if (currentState.boatOnLeft) {
                    newMissionariesLeft -= moveMissionaries;
                    newCannibalsLeft -= moveCannibals;
                } else {
                    newMissionariesLeft += moveMissionaries;
                    newCannibalsLeft += moveCannibals;
                }

                next.add(new State(newMissionariesLeft, newCannibalsLeft, newBoatOnLeft));
            }
```

* Generate successors by trying all allowed boat passenger combinations (1 or 2 people: (1M,0C), (2M,0C), (0M,1C), (0M,2C), (1M,1C)). If the boat is on left, those people are subtracted from left; if boat is on right they are added back to left (because boat crosses back).

```java
            for(State nextState : next){
                if (isValidState(totalMissionaries, totalCannibals, nextState)) {
                    int tentativeG = gCost(currentState.toString(), gValues) + 1;

                    if (tentativeG < gCost(nextState.toString(), gValues)) {
                        gValues.put(nextState.toString(), tentativeG);
                        parentMap.put(nextState.toString(), currentState.toString());
                        pq.add(nextState);
                    }
                }
            }
```

* For each successor:

  * Check `isValidState(...)` — enforces bounds and the cannibal-not-outnumber rule on both banks.
  * Compute `tentativeG = g(current) + 1` (every crossing counts as cost 1).
  * If this `tentativeG` is better than previously known `g(next)`, update `gValues` and `parentMap` and push `next` into the priority queue.
  * **Important:** `visitedStates` is *not* consulted here. The code inserted the start into `visitedStates`, but never uses the set later to prevent reprocessing. (See implementation notes below.)

```java
        System.out.println("No solution found.");
    }
```

* If PQ empties without goal, no solution (shouldn't happen for valid solvable inputs).

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

* Validity check:

  * Counts on left/right in bounds.
  * Rejects states where missionaries are positive but outnumbered by cannibals on either bank.

```java
    private static void printSolutionPath(..., String endState) { ... }
```

* Reconstruct path from `parentMap` and print readable left/right counts and boat location per step.

```java
    public static void main(String[] args) {
        int totalMissionaries = 3, totalCannibals = 3;
        aStarSearch(totalMissionaries, totalCannibals);
    }
}
```

* Runs the search for the standard 3M/3C problem.

---

# Important implementation notes, correctness & potential bugs

1. **Heuristic admissibility (big issue):**

   * `heuristic(s) = missionariesLeft + cannibalsLeft` = number of people left on left bank.

   * A* requires an *admissible* heuristic (never overestimates remaining cost). Here the true remaining number of crossings is at least `ceil(peopleLeft / 2)` (boat capacity ≤ 2) and often more because boat may need to return. `h(s)` equals `peopleLeft`, which is usually **larger** than `ceil(peopleLeft/2)`. Therefore `h` **can overestimate** the true minimal remaining cost → **heuristic is not admissible**. Consequence: A* with this `h` **may not find optimal (shortest) paths**, and it may cut off promising nodes incorrectly if code used closed-set pruning. In short — this heuristic is *not* guaranteed admissible and undermines A*'s optimality guarantees.

   * *Fix / suggestion:* use an admissible heuristic such as `ceil((missionariesLeft + cannibalsLeft) / 2.0)` or a slightly tighter admissible estimate that accounts for boat return trips.

2. **`visitedStates` is unused after initialization:**

   * The code adds the start to `visitedStates` but never checks this set when expanding or popping nodes. Typical A* uses a `closed` set to avoid re-expanding nodes already processed with optimal `g`. This code instead relies on `gValues` and the `if (tentativeG < g(next))` test to update `g` and push new copies into the PQ. That works, but leaves duplicate `State` objects in the PQ and can cause extra processing. Consider using a proper `closed` set checked when a node is popped (and optionally skip if the popped `g` is worse than stored `g`).

3. **Comparator reads mutable `gValues`:**

   * The PQ comparator uses `gValues` map to compute `f(s)`. If `gValues` for a state changes **after** that state was already inserted into the PQ, the PQ ordering does not automatically reheapify existing entries. The code mitigates this by pushing the updated state again when `g` improves — but the old entry with outdated `f` remains in the PQ and will be popped later (at which point you must compare its `g` to the stored one and potentially discard it). The code does not explicitly discard stale pops; it still works because stale pops will fail the `tentativeG < g(next)` check when exploring successors (or will be processed redundantly). But it's inefficient and can confuse reasoning about correctness.

   * *Better design:* store `g` and `f` inside `State` and use immutable `f` for ordering at insertion, or when popping check whether popped state's `g` equals `gValues[state.toString()]` and skip if not equal.

4. **State space is small & finite for small inputs:**

   * For `M=3, C=3`, the maximum number of distinct serialized states is `(M+1)*(C+1)*2`. For `3,3` that's `4*4*2 = 32`. So even inefficient PQ behavior will finish quickly. But for larger M/C the inefficiency matters.

5. **Validity check is good:** prevents invalid negative or out-of-bound states and enforces cannibals-not-outnumbering rule on both banks.

---

# Data structures used

* `State` objects for board representation.
* `PriorityQueue<State>` with comparator using `f = g + h`.
* `Map<String,Integer> gValues` — best known cost from start to state.
* `Map<String,String> parentMap` — parent pointers for path reconstruction.
* `Set<String> visitedStates` — declared but (effectively) unused after start.
* `List<State> next` — temporary successor list.

---

# Time & Space complexity

Let `N` be the number of reachable states (for totals `M,C`): `N ≤ (M+1)*(C+1)*2` (left counts range 0..M and 0..C, boat 2 positions). Let `b` be max branching factor (here `≤ 5`, the number of possible moves).

* **Time (practical):** A* in the worst case explores many states; each expansion generates up to `b` successors. Each PQ insertion / removal is `O(log Q)` where `Q` ≤ number of items in the PQ (≤ `N` in practice). So cost ≈ `O(E · log N)` where `E` is number of insertions ≈ `O(b · N)` worst case. So `O(b · N log N)`. For `M=C=3`, `N ≤ 32`, so trivial.

* **Space:** `O(N)` for `gValues`, `parentMap`, `PQ` and the `closed` set (if used). For given M,C the upper bound `N = (M+1)*(C+1)*2` gives a concrete limit.

* **Asymptotic for large problems:** A* is exponential in the depth of the solution when the heuristic provides limited pruning. So worst-case exponential.

---

# Short pseudocode

```
function aStarSearch(M, C):
    start = State(M, C, boatOnLeft=true)
    g[start] = 0
    parent[start] = null
    open = priority queue ordered by f(s) = g(s) + h(s)
    open.push(start)

    while open not empty:
        current = open.pop()
        if current is goal: reconstruct and return path

        for each allowed move (m_move, c_move):
            successor = apply move to current (move people across)
            if not isValid(successor): continue
            tentativeG = g[current] + 1
            if tentativeG < g.get(successor, ∞):
                g[successor] = tentativeG
                parent[successor] = current
                open.push(successor)

    return failure
```

`h(state)` should be an admissible heuristic (e.g. `ceil((missionariesLeft + cannibalsLeft)/2)`).

---

# Suggestions & improvements (practical)

1. **Use an admissible heuristic.** Replace `h = peopleLeft` with `h = ceil(peopleLeft / 2.0)` or a slightly tighter admissible heuristic that accounts for boat position and required returns. This ensures A* finds an optimal (minimum-crossings) solution.

2. **Use a closed set and canonical PQ bookkeeping.**

   * When you pop a state from PQ, check if its `g` equals `gValues[state]`. If popped `g` > stored `g`, skip (this is a stale entry).
   * Maintain a `closed` set of states that have been fully expanded (optional if you check stale entries). Don’t add start to `visitedStates` only — either use it correctly or remove it.

3. **Store `g`/`f` inside `State` or store wrapper objects.** This avoids recomputing `getPriority` and avoids the mutable-map/comparator subtlety.

4. **Avoid adding invalid states:** you already check validity; also you could short-circuit moves that would move more people than available on that side.

5. **Add an explicit solvability or sanity check** for impossible inputs (e.g., more cannibals than missionaries globally is usually unsolvable unless numbers are special).

6. **Add comments & logging** to aid debugging and to show the order of node expansions when testing heuristics.

---

# Summary (quick)

* The code attempts A* with `f = g + h`, `g` = crossings so far, `h` = people left.
* **Main issues:** the heuristic as written is **not admissible**, so A* optimality is not guaranteed; the PQ comparator depends on a mutable `gValues` map which can make ordering stale; `visitedStates` is unused, which makes the algorithm allow duplicate states in the PQ.
* For `3M/3C` the full state space is tiny (`≤ 32` states), so the program will likely find a solution anyway — but for correctness and for larger instances, fix the heuristic and the PQ/closed-set handling.

If you want, I can:

* Provide a corrected version that uses an admissible heuristic, stores `g` and `f` inside each `State`, and uses a proper closed set (and then show it finding the optimal solution for `3,3`), **or**
* Derive a tighter admissible heuristic that accounts for boat returns and show how using it changes behavior.

Which would you like next?
*/