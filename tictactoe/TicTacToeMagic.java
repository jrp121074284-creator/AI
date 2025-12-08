package TicTacToc;

import java.util.*;

public class TicTacToeMagic {
    static int[] BOARD = new int[9];              
    static final int[] magicSquare = {8, 3, 4, 1, 5, 9, 6, 7, 2};
    static int userSymbol, computerSymbol;        
    static int TURN = 1;                   
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        for (int i = 0; i < 9; i++) BOARD[i] = 2;

        System.out.print("Choose X or O: ");
        char ch = sc.next().toUpperCase().charAt(0);
        userSymbol = (ch == 'O') ? 5 : 3;          
        computerSymbol = (userSymbol == 3) ? 5 : 3;

        System.out.println("Positions shown as 1..9 (top-left = 1). X always starts.");
        showPositions();

        while (TURN <= 9) {
            if ((TURN % 2 == 1 && userSymbol == 3) || (TURN % 2 == 0 && userSymbol == 5)) {
                userMove();
            } else {
                computerMove();
            }

            displayBoard();

            if (checkWin(userSymbol)) {
                System.out.println("You win!");
                return;
            } else if (checkWin(computerSymbol)) {
                System.out.println("Computer wins!");
                return;
            }
        }

        System.out.println("It's a draw!");
    }

    static void displayBoard() {
        System.out.println();
        for (int i = 0; i < 9; i++) {
            char symbol = (BOARD[i] == 3) ? 'X' : (BOARD[i] == 5) ? 'O' : ' ';
            System.out.print(" " + symbol + " ");
            if (i % 3 != 2) System.out.print("|");
            else if (i != 8) System.out.println("\n---+---+---");
        }
        System.out.println("\n");
    }

    static void userMove() {
        int pos;
        while (true) {
            System.out.print("Your move (" + (userSymbol == 3 ? "X" : "O") + ") - Enter position (1-9): ");
            if (!sc.hasNextInt()) {
                System.out.println("Please enter a number 1..9.");
                sc.next();
                continue;
            }
            pos = sc.nextInt();
            if (pos < 1 || pos > 9) {
                System.out.println("Out of range. Try again.");
                continue;
            }
            if (BOARD[pos - 1] != 2) {
                System.out.println("Position already taken. Try again.");
                continue;
            }
            GO(pos - 1, userSymbol);
            break;
        }
    }

    static void computerMove() {
        int move = -1;
        System.out.println("Computer's move (" + (computerSymbol == 3 ? "X" : "O") + "):");

        switch (TURN) {
            case 1:
                move = 0;
                break;
            case 2:
                move = (BOARD[4] == 2) ? 4 : 0;
                break;
            case 3:
                move = (BOARD[8] == 2) ? 8 : 2; 
                break;
            case 4:
                move = POSSWIN(userSymbol);
                if (move == -1) move = MAKE2();
                break;
            case 5:
                move = POSSWIN(computerSymbol); 
                if (move == -1) move = POSSWIN(userSymbol);
                if (move == -1) move = (BOARD[6] == 2) ? 6 : 2;
                break;
            case 6:
                move = POSSWIN(computerSymbol);
                if (move == -1) move = POSSWIN(userSymbol);
                if (move == -1) move = MAKE2();
                break;
            case 7:
                move = POSSWIN(computerSymbol);
                if (move == -1) move = POSSWIN(userSymbol);
                if (move == -1) move = anyBlank();
                break;
            case 8:
                move = POSSWIN(computerSymbol);
                if (move == -1) move = POSSWIN(userSymbol);
                if (move == -1) move = anyBlank();
                break;
            case 9:
                move = POSSWIN(computerSymbol);
                if (move == -1) move = POSSWIN(userSymbol);
                if (move == -1) move = anyBlank();
                break;
        }

        if (move == -1) move = anyBlank();
        if (move != -1) {
            GO(move, computerSymbol);
            System.out.println("Computer played at position " + (move + 1));
        } else {
            System.out.println("Computer has no legal move.");
        }
    }

    static void GO(int idx, int symbol) {
        BOARD[idx] = symbol;
        TURN++;
    }

    static int MAKE2() {
        if (BOARD[4] == 2) return 4;
        int[] sides = {1, 3, 5, 7};
        for (int s : sides) if (BOARD[s] == 2) return s;
        return anyBlank();
    }

    static int POSSWIN(int p) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < 9; i++) if (BOARD[i] == p) indices.add(i);

        if (indices.size() < 2) return -1;
        for (int a = 0; a < indices.size(); a++) {
            for (int b = a + 1; b < indices.size(); b++) {
                int i = indices.get(a), j = indices.get(b);
                int needed = 15 - (magicSquare[i] + magicSquare[j]);
                if (needed < 1 || needed > 9) continue;
                int k = indexOfMagic(needed);
                if (k != -1 && BOARD[k] == 2) return k;
            }
        }
        return -1;
    }

    static int indexOfMagic(int value) {
        for (int i = 0; i < 9; i++) if (magicSquare[i] == value) return i;
        return -1;
    }

    static int anyBlank() {
        for (int i = 0; i < 9; i++) if (BOARD[i] == 2) return i;
        return -1;
    }

    static boolean checkWin(int p) {
        ArrayList<Integer> pos = new ArrayList<>();
        for (int i = 0; i < 9; i++) if (BOARD[i] == p) pos.add(i);
        if (pos.size() < 3) return false;
        for (int a = 0; a < pos.size(); a++) {
            for (int b = a + 1; b < pos.size(); b++) {
                for (int c = b + 1; c < pos.size(); c++) {
                    int sum = magicSquare[pos.get(a)] + magicSquare[pos.get(b)] + magicSquare[pos.get(c)];
                    if (sum == 15) return true;
                }
            }
        }
        return false;
    }
    static void showPositions() {
        System.out.println(" 1 | 2 | 3 ");
        System.out.println("---+---+---");
        System.out.println(" 4 | 5 | 6 ");
        System.out.println("---+---+---");
        System.out.println(" 7 | 8 | 9 ");
    }
}

/*
Here’s a **detailed explanation** of your **Tic Tac Toe using Magic Square** program, including problem statement, line-by-line explanation, key data structures, time/space complexity, and pseudocode.

---

# **Problem Statement**

* Implement **Tic Tac Toe** where a human plays against the computer.
* Uses **Magic Square technique** to detect winning moves:

  * Standard 3x3 magic square:

    ```
    8 3 4
    1 5 9
    6 7 2
    ```

  * Winning condition: any **3 cells summing to 15**.
* Computer uses **strategy**:

  1. Try to win.
  2. Block the user from winning.
  3. Take **center**.
  4. Take **sides**.
  5. Take any blank.

---

# **Global Variables**

```java
static int[] BOARD = new int[9];              
static final int[] magicSquare = {8, 3, 4, 1, 5, 9, 6, 7, 2};
static int userSymbol, computerSymbol;        
static int TURN = 1;                   
static Scanner sc = new Scanner(System.in);
```

* `BOARD[9]` → 1D array for 3x3 board:

  * `2` → empty
  * `3` → X
  * `5` → O
* `magicSquare[9]` → helps check for win: **any three numbers sum to 15** = win.
* `TURN` → move counter.
* `Scanner` → for input.

---

# **Main Function**

```java
for (int i = 0; i < 9; i++) BOARD[i] = 2;
```

* Initialize all cells as empty.

```java
System.out.print("Choose X or O: ");
char ch = sc.next().toUpperCase().charAt(0);
userSymbol = (ch == 'O') ? 5 : 3;          
computerSymbol = (userSymbol == 3) ? 5 : 3;
```

* User chooses symbol. Assign numerical values.

```java
System.out.println("Positions shown as 1..9 (top-left = 1). X always starts.");
showPositions();
```

* Display position reference.

```java
while (TURN <= 9) {
    if ((TURN % 2 == 1 && userSymbol == 3) || (TURN % 2 == 0 && userSymbol == 5)) userMove();
    else computerMove();
    displayBoard();
    if (checkWin(userSymbol)) { System.out.println("You win!"); return; }
    else if (checkWin(computerSymbol)) { System.out.println("Computer wins!"); return; }
}
System.out.println("It's a draw!");
```

* Alternate **user** and **computer moves** based on turn and symbol.
* Display board after each move.
* Check **win condition** using `checkWin()`.
* Ends when a player wins or board is full.

---

# **Display Functions**

```java
static void displayBoard() { ... }
static void showPositions() { ... }
```

* `displayBoard()` → shows current board (X/O/blank).
* `showPositions()` → prints reference board 1–9.

---

# **User Move**

```java
while (true) {
    System.out.print("Your move ... Enter position (1-9): ");
    if (!sc.hasNextInt()) { sc.next(); continue; }
    pos = sc.nextInt();
    if (pos < 1 || pos > 9) continue;
    if (BOARD[pos-1] != 2) continue;
    GO(pos - 1, userSymbol);
    break;
}
```

* Validates input:

  1. Integer 1–9
  2. Cell is empty
* Calls `GO()` to place symbol and increment turn.

---

# **Computer Move**

```java
switch (TURN) {
    case 1: move = 0; break;
    case 2: move = (BOARD[4] == 2) ? 4 : 0; break;
    case 3: move = (BOARD[8] == 2) ? 8 : 2; break;
    ...
}
GO(move, computerSymbol);
```

* **Computer strategy by turn:**

  1. First moves: corner or center.
  2. Check **win**: `POSSWIN(computerSymbol)`
  3. Block user: `POSSWIN(userSymbol)`
  4. Take center or sides: `MAKE2()`
  5. Take any blank: `anyBlank()`

---

# **GO Function**

```java
static void GO(int idx, int symbol) {
    BOARD[idx] = symbol;
    TURN++;
}
```

* Place symbol and increment turn.

---

# **MAKE2 Function**

```java
static int MAKE2() {
    if (BOARD[4] == 2) return 4;       // center
    int[] sides = {1,3,5,7};
    for (int s : sides) if (BOARD[s] == 2) return s; // sides
    return anyBlank();                   // fallback
}
```

---

# **POSSWIN Function (Magic Square Logic)**

```java
static int POSSWIN(int p) {
    ArrayList<Integer> indices = new ArrayList<>();
    for (int i = 0; i < 9; i++) if (BOARD[i] == p) indices.add(i);

    if (indices.size() < 2) return -1;
    for (a=0..indices.size) for (b=a+1..) {
        int needed = 15 - (magicSquare[i]+magicSquare[j]);
        int k = indexOfMagic(needed);
        if (k != -1 && BOARD[k]==2) return k;
    }
    return -1;
}
```

* **Idea**: Any **3 numbers sum to 15** → win.
* Checks all **pairs of player's moves**, finds third needed to win.

---

# **checkWin Function**

```java
static boolean checkWin(int p) {
    ArrayList<Integer> pos = new ArrayList<>();
    for (i=0..8) if(BOARD[i]==p) pos.add(i);
    if(pos.size()<3) return false;
    for a,b,c in pos:
        if(magicSquare[pos[a]]+magicSquare[pos[b]]+magicSquare[pos[c]]==15) return true;
    return false;
}
```

* Checks all combinations of 3 positions.
* If **sum = 15**, player wins.

---

# **anyBlank Function**

```java
static int anyBlank() {
    for(i=0..8) if(BOARD[i]==2) return i;
    return -1;
}
```

* Returns **first empty cell**.

---

# **Data Structures Used**

| Data Structure       | Purpose                                 |
| -------------------- | --------------------------------------- |
| `int[] BOARD`        | Store current board (1D array).         |
| `int[] magicSquare`  | Check winning conditions efficiently.   |
| `Scanner`            | Input from user.                        |
| `ArrayList<Integer>` | Store occupied positions for win check. |

---

# **Time Complexity**

* User move: O(1)
* Computer move:

  * `POSSWIN`: O(n²) → check all pairs of moves
  * `MAKE2`/`anyBlank`: O(1)
* Total moves = 9 → overall **very efficient**

**Overall:** O(1) for practical use (small 3x3 board)

---

# **Space Complexity**

* `BOARD[9]`, `magicSquare[9]` → O(1)
* Temporary ArrayLists → O(1) (max 5–6 elements)
* Total → O(1)

---

# **Pseudo Code**

```
Initialize BOARD[9] as empty
Ask user to choose X or O
Assign numerical values for symbols

while TURN <= 9:
    if user’s turn: userMove()
    else: computerMove()
    displayBoard()
    if checkWin(user): print "You win"; return
    if checkWin(computer): print "Computer wins"; return
print "Draw"

function userMove():
    ask position 1..9 until valid
    GO(position-1, userSymbol)

function computerMove():
    use strategy based on TURN:
        POSSWIN(computer) -> win if possible
        POSSWIN(user) -> block
        MAKE2() -> center/sides
        anyBlank() -> fallback
    GO(move, computerSymbol)

function POSSWIN(player):
    for each pair of player positions:
        find third needed to sum=15
        if available -> return position
    return -1

function checkWin(player):
    for each 3-combination of player positions:
        if sum=15 -> return true
    return false
```

---

# ✅ **Summary**

* Implements **Tic Tac Toe AI using Magic Square**.
* Efficiently checks wins by **sum=15** property.
* Uses **turn-based computer strategy**.
* Space-efficient and simple logic.

---

I can also make a **diagram showing how Magic Square maps moves to winning combinations**, which makes this approach easier to understand.

Do you want me to make that diagram?

*/