package TicTacToc;

import java.util.Scanner;

public class TicTacToe2 {
    static int[] BOARD = new int[9];
    static int TURN = 1;
    static int userSymbol;
    static int computerSymbol;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        for (int i = 0; i < 9; i++) BOARD[i] = 2;

        System.out.println("Tic Tac Toe - Positions:");
        showPositions();

        System.out.print("Choose your symbol (X or O): ");
        char choice = sc.next().toUpperCase().charAt(0);
        if (choice == 'O') {
            userSymbol = 5;
            computerSymbol = 3;
        } else {
            userSymbol = 3;
            computerSymbol = 5;
        }

        while (TURN <= 9) {
            if ((TURN % 2 != 0 && userSymbol == 3) || (TURN % 2 == 0 && userSymbol == 5)) {
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

    static void userMove() {
        int pos;
        while (true) {
            System.out.print("Your move (" + (userSymbol == 3 ? "X" : "O") + ") - Enter position (1-9): ");
            if (!sc.hasNextInt()) {
                System.out.println("Enter a number between 1 and 9.");
                sc.next();
                continue;
            }
            pos = sc.nextInt();
            if (pos >= 1 && pos <= 9 && BOARD[pos - 1] == 2) {
                GO(pos - 1, userSymbol);
                break;
            } else {
                System.out.println("Invalid move! Try again.");
            }
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

        GO(move, computerSymbol);
        System.out.println("Computer played at position " + (move + 1));
    }

    static int MAKE2() {
        if (BOARD[4] == 2) return 4; 
        int[] sides = {1, 3, 5, 7};
        for (int s : sides) if (BOARD[s] == 2) return s;
        return anyBlank();
    }

    static int POSSWIN(int p) {
        int target = (p == 3) ? 18 : 50;
        int[][] lines = {
            {0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}
        };
        for (int[] line : lines) {
            int prod = BOARD[line[0]] * BOARD[line[1]] * BOARD[line[2]];
            if (prod == target) {
                for (int pos : line) {
                    if (BOARD[pos] == 2) return pos;
                }
            }
        }
        return -1;
    }

    static void GO(int idx, int symbol) {
        BOARD[idx] = symbol;
        TURN++;
    }

    static boolean checkWin(int p) {
        int target = p * p * p;
        int[][] lines = {
            {0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}
        };
        for (int[] line : lines) {
            int prod = BOARD[line[0]] * BOARD[line[1]] * BOARD[line[2]];
            if (prod == target) return true;
        }
        return false;
    }

    static int anyBlank() {
        for (int i = 0; i < 9; i++) if (BOARD[i] == 2) return i;
        return -1;
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

    static void showPositions() {
        System.out.println(" 1 | 2 | 3 ");
        System.out.println("---+---+---");
        System.out.println(" 4 | 5 | 6 ");
        System.out.println("---+---+---");
        System.out.println(" 7 | 8 | 9 ");
    }
}


/*
Here’s a **detailed explanation** of your Tic Tac Toe program, including problem statement, line-by-line description, key data structures, time/space complexity, and pseudocode.

---

# **Problem Statement**

* Implement a **Tic Tac Toe game** where the user plays against the computer.
* Board size: **3x3**.
* Player chooses symbol **X or O**.
* Game alternates turns:

  * **User moves** first if X, second if O.
  * **Computer moves** use a **basic strategy**:

    1. Try to **win** if possible.
    2. Block user from winning.
    3. Take the **center** if free.
    4. Take a **side**.
    5. Take any remaining blank space.
* The game ends when **someone wins** or the board is full (**draw**).

---

# **Code Explanation Line by Line**

### **Global Variables**

```java
static int[] BOARD = new int[9];
static int TURN = 1;
static int userSymbol;
static int computerSymbol;
static Scanner sc = new Scanner(System.in);
```

* `BOARD[9]` → represents the 3x3 board, flattened as 1D array.

  * `2` → empty
  * `3` → X
  * `5` → O
* `TURN` → counts moves (1 to 9)
* `userSymbol`, `computerSymbol` → numerical representation for comparison
* `Scanner sc` → input from user

---

### **Main Function**

```java
for (int i = 0; i < 9; i++) BOARD[i] = 2;
```

* Initialize board with empty cells (2).

```java
System.out.println("Tic Tac Toe - Positions:");
showPositions();
```

* Display the board positions (1-9) for user reference.

```java
System.out.print("Choose your symbol (X or O): ");
char choice = sc.next().toUpperCase().charAt(0);
if (choice == 'O') {
    userSymbol = 5;
    computerSymbol = 3;
} else {
    userSymbol = 3;
    computerSymbol = 5;
}
```

* User chooses X or O; assigns numerical values to symbols.

---

### **Game Loop**

```java
while (TURN <= 9) {
    if ((TURN % 2 != 0 && userSymbol == 3) || (TURN % 2 == 0 && userSymbol == 5)) {
        userMove();
    } else {
        computerMove();
    }

    displayBoard();

    if (checkWin(userSymbol)) { System.out.println("You win!"); return; }
    else if (checkWin(computerSymbol)) { System.out.println("Computer wins!"); return; }
}
System.out.println("It's a draw!");
```

* Alternate **user** and **computer** moves based on `TURN` and chosen symbols.
* Display board after each move.
* Check for **win condition** using `checkWin()`.
* Ends if someone wins or all 9 moves done.

---

### **User Move**

```java
int pos;
while (true) {
    System.out.print("Your move (" + (userSymbol == 3 ? "X" : "O") + ") - Enter position (1-9): ");
    if (!sc.hasNextInt()) { sc.next(); continue; }
    pos = sc.nextInt();
    if (pos >= 1 && pos <= 9 && BOARD[pos - 1] == 2) {
        GO(pos - 1, userSymbol);
        break;
    } else { System.out.println("Invalid move! Try again."); }
}
```

* Input validation:

  * Must be **integer 1–9**.
  * Must be **empty cell**.
* Calls `GO()` to place symbol and increase turn.

---

### **Computer Move (Strategy)**

```java
int move = -1;
switch (TURN) {
    case 1: move = 0; break; // corner first
    case 2: move = (BOARD[4] == 2) ? 4 : 0; break; // center if free
    case 3: move = (BOARD[8] == 2) ? 8 : 2; break;
    case 4: move = POSSWIN(userSymbol); if (move==-1) move = MAKE2(); break;
    case 5: move = POSSWIN(computerSymbol); ...
    ...
}
GO(move, computerSymbol);
```

* **TURN-based strategy**:

  1. First moves: corners or center.
  2. Check if **computer can win**: `POSSWIN(computerSymbol)`.
  3. Block user if **user can win**: `POSSWIN(userSymbol)`.
  4. Take **center** or **side**: `MAKE2()`.
  5. Pick any remaining blank: `anyBlank()`.

---

### **Supporting Functions**

#### `MAKE2()`

* Prioritize center, then sides, then any blank.

```java
static int MAKE2() {
    if (BOARD[4]==2) return 4;
    int[] sides = {1,3,5,7};
    for (int s:sides) if(BOARD[s]==2) return s;
    return anyBlank();
}
```

#### `POSSWIN(int p)`

* Check all **winning lines** for potential win.
* Lines are combinations: rows, columns, diagonals.

```java
int target = (p==3) ? 18 : 50; 
// 3*3*2 = 18 (X can win), 5*5*2=50 (O can win)
```

#### `GO(int idx, int symbol)`

* Place symbol and increment turn.

#### `checkWin(int p)`

* Check all rows/cols/diagonals:

  * If product of line equals `p*p*p`, player `p` wins.

#### `anyBlank()`

* Returns **first empty cell** index.

#### `displayBoard()`

* Prints board in **3x3 format** with `X`, `O`, or blank.

#### `showPositions()`

* Prints **reference positions** 1–9.

---

# **Data Structures Used**

| Data Structure | Purpose                        |
| -------------- | ------------------------------ |
| `int[] BOARD`  | 1D array to store board cells. |
| `Scanner`      | For user input.                |
| `int`, `TURN`  | Track turns and symbols.       |

* Simple arrays and constants; no complex structures needed.

---

# **Time Complexity**

* **User input and computer moves**: O(1) per move.
* **Total moves**: max 9.
* **Overall complexity**: O(1), very efficient.

# **Space Complexity**

* `BOARD[9]` → O(1)
* Auxiliary variables → O(1)
* Total → O(1)

---

# **Pseudo Code (Short)**

```
Initialize BOARD[9] to empty
Ask user to choose X or O
Assign numerical values to symbols

while TURN <= 9:
    if user's turn: userMove()
    else: computerMove()
    displayBoard()
    if user wins: print "You win"; exit
    if computer wins: print "Computer wins"; exit
print "Draw"

function userMove():
    input position
    validate input
    place symbol using GO()

function computerMove():
    decide move using strategy:
        POSSWIN (computer)
        POSSWIN (user) to block
        MAKE2() center/sides
        anyBlank()
    place symbol using GO()

function checkWin(symbol):
    for each winning line:
        if product(line) == symbol^3: return true
    return false
```

---

# ✅ **Summary**

* Implements **Tic Tac Toe with AI** using **rule-based strategy**.
* Uses **numeric multiplication trick** for fast win detection.
* Efficient: O(1) time/space.
* Covers **user input validation**, **display**, **AI moves**, and **win/draw detection**.

---

If you want, I can also **draw a flow diagram of the AI strategy for each turn** which makes it easier to visualize how the computer decides moves.

Do you want me to do that?
 */