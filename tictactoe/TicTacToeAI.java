package TicTacToc;

import java.util.Scanner;

public class TicTacToeAI {

    static int[] BOARD = new int[9];
    static int TURN = 1;
    static int userSymbol;
    static int computerSymbol;
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        for (int i = 0; i < 9; i++)
            BOARD[i] = 2;
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
        System.out.println("Computer's move (" + (computerSymbol == 3 ? "X" : "O") + "):");
        int bestScore = (computerSymbol == 3) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestMove = -1;
        for (int i = 0; i < 9; i++) {
            if (BOARD[i] == 2) { 
                BOARD[i] = computerSymbol;
                int score = minimax(0, false);
                BOARD[i] = 2;
                if (computerSymbol == 3) {
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = i;
                    }
                } else {
                    if (score < bestScore) {
                        bestScore = score;
                        bestMove = i;
                    }
                }
            }
        }
        GO(bestMove, computerSymbol);
        System.out.println("Computer played at position " + (bestMove + 1));
    }


    static int minimax(int depth, boolean isMaximizing) {
        if (checkWin(computerSymbol))
            return (computerSymbol == 3 ? 10 : -10) - depth;
        if (checkWin(userSymbol))
            return (userSymbol == 3 ? 10 : -10) - depth;
        if (isBoardFull())
            return 0;
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (BOARD[i] == 2) {
                    BOARD[i] = computerSymbol;
                    int score = minimax(depth + 1, false);
                    BOARD[i] = 2;
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (BOARD[i] == 2) {
                    BOARD[i] = userSymbol;
                    int score = minimax(depth + 1, true);
                    BOARD[i] = 2;
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }

    static boolean checkWin(int p) {
        int target = p * p * p;
        int[][] lines = {
                { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 },
                { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
                { 0, 4, 8 }, { 2, 4, 6 }
        };
        for (int[] line : lines) {
            int prod = BOARD[line[0]] * BOARD[line[1]] * BOARD[line[2]];
            if (prod == target)
                return true;
        }
        return false;
    }

    static boolean isBoardFull() {
        for (int i = 0; i < 9; i++) {
            if (BOARD[i] == 2)
                return false;
        }
        return true;
    }

    static void GO(int idx, int symbol) {
        BOARD[idx] = symbol;
        TURN++;
    }


    static void displayBoard() {
        System.out.println();
        for (int i = 0; i < 9; i++) {
            char symbol = (BOARD[i] == 3) ? 'X' : (BOARD[i] == 5) ? 'O' : ' ';
            System.out.print(" " + symbol + " ");
            if (i % 3 != 2)
                System.out.print("|");
            else if (i != 8)
                System.out.println("\n---+---+---");
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
Absolutely! Let’s go **step by step** to explain this **Tic-Tac-Toe AI** Java program that uses the **Minimax algorithm**.

---

# **Problem Statement**

* Implement a **Tic-Tac-Toe game** where a human plays against a computer.
* The computer uses **Minimax algorithm** to make **optimal moves**.
* Game board is a 3×3 grid.
* Player who gets **three symbols in a row, column, or diagonal wins**.
* If the board fills with no winner, it’s a **draw**.

**Key Features:**

* Player can choose `X` or `O`.
* Computer plays optimally using **Minimax**.
* Uses simple array representation for the board.

---

# **Code Explanation**

---

### **Global Variables**

```java
static int[] BOARD = new int[9];
static int TURN = 1;
static int userSymbol;
static int computerSymbol;
static Scanner sc = new Scanner(System.in);
```

* `BOARD`: integer array to represent 3×3 board (flattened).

  * 2 = empty, 3 = 'X', 5 = 'O'. Using **prime numbers** allows **win checking via multiplication**.
* `TURN`: counts current turn number.
* `userSymbol` / `computerSymbol`: store player's and AI's symbols (3 or 5).
* `sc`: Scanner object for user input.

---

### **Main Function**

```java
for (int i = 0; i < 9; i++)
    BOARD[i] = 2;
```

* Initialize all board positions as empty (`2`).

```java
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
```

* Displays board positions for reference (1–9).
* User selects symbol (`X` or `O`).
* Assigns **prime numbers** to symbols (`3` for X, `5` for O).

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

    if (checkWin(userSymbol)) {
        System.out.println("You win!");
        return;
    } else if (checkWin(computerSymbol)) {
        System.out.println("Computer wins!");
        return;
    }
}
System.out.println("It's a draw!");
```

* Loop continues **maximum 9 turns** (board size).
* Checks whose turn it is: user or computer.
* Updates the board and displays it.
* Checks for **win** using `checkWin`.
* Declares winner or draw.

---

### **User Move**

```java
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
```

* Prompts user for a position (1–9).
* Checks validity (number, empty cell).
* Calls `GO()` to update board and increment turn.

---

### **Computer Move (Minimax)**

```java
static void computerMove() {
    int bestScore = (computerSymbol == 3) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    int bestMove = -1;

    for (int i = 0; i < 9; i++) {
        if (BOARD[i] == 2) {
            BOARD[i] = computerSymbol;
            int score = minimax(0, false);
            BOARD[i] = 2;
            if (computerSymbol == 3) {
                if (score > bestScore) { bestScore = score; bestMove = i; }
            } else {
                if (score < bestScore) { bestScore = score; bestMove = i; }
            }
        }
    }
    GO(bestMove, computerSymbol);
}
```

* **Iterates over empty cells**, simulates move.
* Uses `minimax` to evaluate the move.
* Chooses **highest score for maximizing AI** or **lowest score for minimizing AI**.

---

### **Minimax Algorithm**

```java
static int minimax(int depth, boolean isMaximizing) {
    if (checkWin(computerSymbol))
        return (computerSymbol == 3 ? 10 : -10) - depth;
    if (checkWin(userSymbol))
        return (userSymbol == 3 ? 10 : -10) - depth;
    if (isBoardFull()) return 0;
```

* Base case: check for **win** or **draw**.
* Uses `depth` to prefer **faster wins**.

```java
if (isMaximizing) {
    int bestScore = Integer.MIN_VALUE;
    for (int i = 0; i < 9; i++) {
        if (BOARD[i] == 2) {
            BOARD[i] = computerSymbol;
            int score = minimax(depth + 1, false);
            BOARD[i] = 2;
            bestScore = Math.max(score, bestScore);
        }
    }
    return bestScore;
} else {
    int bestScore = Integer.MAX_VALUE;
    for (int i = 0; i < 9; i++) {
        if (BOARD[i] == 2) {
            BOARD[i] = userSymbol;
            int score = minimax(depth + 1, true);
            BOARD[i] = 2;
            bestScore = Math.min(score, bestScore);
        }
    }
    return bestScore;
}
```

* Recursively **simulates moves** for both maximizing (AI) and minimizing (player) turns.
* Chooses **best possible outcome**.

---

### **Win Check**

```java
static boolean checkWin(int p) {
    int target = p * p * p; // product of three primes
    int[][] lines = {
        {0,1,2},{3,4,5},{6,7,8},
        {0,3,6},{1,4,7},{2,5,8},
        {0,4,8},{2,4,6}
    };
    for (int[] line : lines) {
        int prod = BOARD[line[0]] * BOARD[line[1]] * BOARD[line[2]];
        if (prod == target) return true;
    }
    return false;
}
```

* Uses **prime multiplication trick**:

  * If three positions in a line are the same symbol (3 or 5), their product = cube of that symbol.

---

### **Supporting Methods**

* `isBoardFull()`: checks if no empty cells remain.
* `GO(idx, symbol)`: places symbol and increments turn.
* `displayBoard()`: prints current board with X/O.
* `showPositions()`: prints 1–9 positions for reference.

---

# **Data Structures Used**

| Structure                                          | Purpose                                       |
| -------------------------------------------------- | --------------------------------------------- |
| `int[] BOARD`                                      | Represents 3×3 Tic-Tac-Toe board (flattened). |
| `Scanner sc`                                       | For user input.                               |
| Recursion (Minimax)                                | AI decision-making.                           |
| No explicit lists/maps needed (simple array used). |                                               |

---

# **Time and Space Complexity**

**Time Complexity of Minimax:**

* In worst case, AI explores **all possible moves**:

  * Maximum 9 moves → 9! possible sequences → O(9!) ≈ 362,880
  * Depth of recursion = number of empty cells

**Space Complexity:**

* Recursion stack depth = maximum 9 → O(9)
* Board array: O(1) (fixed size 9)

> Tic-Tac-Toe has **small enough state space**, so full Minimax is feasible.

---

# **Pseudo Code (Short)**

```
Initialize BOARD with empty cells
User chooses X or O
Loop until board full:
    if user turn:
        read input
        if valid, place symbol
    else:
        for each empty cell:
            simulate move
            score = minimax(depth=0, isMax=false)
            undo move
        pick move with best score
        place symbol
    display board
    if win: declare winner
    if full: declare draw

function minimax(depth, isMax):
    if computer wins: return score
    if user wins: return score
    if board full: return 0
    if isMax:
        maxScore = -inf
        for each empty cell:
            simulate move
            score = minimax(depth+1, false)
            undo move
            maxScore = max(score, maxScore)
        return maxScore
    else:
        minScore = +inf
        for each empty cell:
            simulate move
            score = minimax(depth+1, true)
            undo move
            minScore = min(score, minScore)
        return minScore
```

---

# ✅ **Summary**

* Implements **Tic-Tac-Toe game** with **AI using Minimax algorithm**.
* Uses **prime number trick** for efficient win checking.
* AI guarantees **optimal moves** → never loses.
* Efficient for 3×3 board because search space is small.

---

If you want, I can also make a **diagram showing how Minimax explores the Tic-Tac-Toe tree step by step**. This really helps visualize how the AI chooses moves.

Do you want me to make that diagram?
 */