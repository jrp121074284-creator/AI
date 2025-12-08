:- initialization(main).

% ------------------------------------------------------------
% Knowledge Base: Jobs & Required Skills
% ------------------------------------------------------------

required(software_developer,
    [programming, data_structures, problem_solving]).

required(web_developer,
    [html, css, javascript]).

required(data_scientist,
    [python, statistics, machine_learning]).

required(network_engineer,
    [networking, troubleshooting, cisco]).

required(ui_ux_designer,
    [creative_thinking, figma, graphic_design]).

required(android_developer,
    [java, android_studio, oop]).

required(database_administrator,
    [sql, database_design, backups]).

% ------------------------------------------------------------
% Check if all required skills appear in user skill list
% ------------------------------------------------------------

contains_all([], _UserList).
contains_all([H|T], UserList) :-
    member(H, UserList),
    contains_all(T, UserList).

% ------------------------------------------------------------
% Suggest jobs
% ------------------------------------------------------------

suggest(UserList) :-
    required(Job, NeedList),
    contains_all(NeedList, UserList),
    write('You are suitable for: '), write(Job), nl,
    fail.

suggest(_) :-
    write('Suggestions finished.'), nl.

% ------------------------------------------------------------
% Menu
% ------------------------------------------------------------

main :-
    nl,
    write('Enter your skills as a list. Example:'), nl,
    write('[programming, data_structures, problem_solving].'), nl,
    write('Your skills: '),
    read(UserList),
    suggest(UserList).

    %    % Absolutely! Let’s go **line by line** and fully explain your **Prolog program** for job recommendation based on user skills. I’ll also cover the **problem statement, symbols, data structures, time/space complexity, and pseudocode**.

---

# **Problem Statement**

* This Prolog program **recommends jobs** based on a user's skills.
* Each job has a list of **required skills**.
* The user provides their skills as a **list**.
* The program checks which jobs the user qualifies for and prints them.

---

# **Line-by-Line Explanation**

---

### **Line 1**

```prolog
:- initialization(main).
```

* `:-` → **directive**: instructs Prolog to run something **at startup**.
* `initialization(main)` → Prolog executes the `main` predicate when the program starts.
* **Purpose:** Starts the program automatically when loaded.

---

### **Knowledge Base: Jobs & Required Skills**

```prolog
required(software_developer, [programming, data_structures, problem_solving]).
required(web_developer, [html, css, javascript]).
required(data_scientist, [python, statistics, machine_learning]).
required(network_engineer, [networking, troubleshooting, cisco]).
required(ui_ux_designer, [creative_thinking, figma, graphic_design]).
required(android_developer, [java, android_studio, oop]).
required(database_administrator, [sql, database_design, backups]).
```

* `required(Job, SkillsList)` → **facts** in Prolog.
* `Job` → atom (name of the job).
* `SkillsList` → **list of atoms** representing required skills.
* Example: `[programming, data_structures, problem_solving]` is a **Prolog list**.
* Each fact is independent and can be queried.

**Symbols explanation:**

* `[]` → empty list.
* `[H|T]` → list with head `H` and tail `T`.
* `,` → separates arguments in predicates or goals.
* `.` → ends a fact or rule.

---

### **Check if all required skills are in user skills**

```prolog
contains_all([], _UserList).
```

* Base case: If the **required skill list is empty**, return `true`.
* `_UserList` → underscore means the variable is **ignored** in this clause.
* **Interpretation:** An empty requirement list is always satisfied.

```prolog
contains_all([H|T], UserList) :- 
    member(H, UserList),
    contains_all(T, UserList).
```

* Recursive case:

  1. `[H|T]` → split the required skills list into **head (H)** and **tail (T)**.
  2. `member(H, UserList)` → checks if `H` is **present in UserList** (Prolog built-in).
  3. `contains_all(T, UserList)` → recursively checks the remaining skills.
* **Purpose:** Ensures **all required skills** are present in the user’s skill list.

**Symbols:**

* `:-` → “if” (implication). Left-hand side is true if right-hand side is true.
* `member(X, List)` → checks if X is an element of List.

---

### **Suggest jobs**

```prolog
suggest(UserList) :- 
    required(Job, NeedList),
    contains_all(NeedList, UserList),
    write('You are suitable for: '), write(Job), nl,
    fail.
```

* For each `required(Job, NeedList)`:

  1. Check `contains_all(NeedList, UserList)` → user has all required skills.
  2. If yes → print the job using `write`:

     * `write('You are suitable for: ')` → prints text.
     * `write(Job)` → prints the job name.
     * `nl` → prints **newline**.
  3. `fail` → forces **backtracking** to find **all possible matches**.

     * Without `fail`, Prolog would stop after the first match.

```prolog
suggest(_) :- write('Suggestions finished.'), nl.
```

* Catch-all rule after backtracking:

  * Prints **end message**.
* `_` → any input (we ignore the input here).

---

### **Menu / Main function**

```prolog
main :-
    nl,
    write('Enter your skills as a list. Example:'), nl,
    write('[programming, data_structures, problem_solving].'), nl,
    write('Your skills: '),
    read(UserList),
    suggest(UserList).
```

* `main` predicate displays **instructions**.
* `nl` → newline.
* `write(...)` → prints text.
* `read(UserList)` → **reads input** from user and stores in `UserList`.
* `suggest(UserList)` → calls the suggestion mechanism.

---

# **How it works (example)**

1. User runs the program.
2. Program asks for skills:

```prolog
[programming, data_structures, problem_solving]
```

3. Program checks `required(Job, Skills)` for each job:

   * Calls `contains_all(Skills, UserList)`
   * If `true`, prints job name.
4. After all matches, prints `"Suggestions finished."`.

---

# **Data Structures**

| Data Structure                | Usage                                      |
| ----------------------------- | ------------------------------------------ |
| List `[a,b,c]`                | Represents skills of jobs and users        |
| Facts `required(Job, Skills)` | Knowledge base of jobs and required skills |
| Set-like behavior             | `member/2` checks for existence in a list  |

---

# **Time Complexity**

* Let `J = number of jobs`, `S = max skills per job`, `U = size of user skill list`.
* `contains_all` checks all skills:

  * Each `member` → O(U)
  * For S skills → O(S * U)
* `suggest` iterates over all jobs → O(J * S * U)
* **Overall:** O(J * S * U)

**Space Complexity:**

* Storing **user input list** → O(U)
* Call stack for recursion → O(S) per job
* **Overall:** O(U + S)

---

# **Pseudo Code**

```
START
READ UserList
FOR each Job in knowledge base:
    IF all Job.Skills are in UserList:
        PRINT Job
PRINT "Suggestions finished"
END
```

---

# **Symbols Summary in Prolog**

| Symbol           | Meaning                   |                         |
| ---------------- | ------------------------- | ----------------------- |
| `:-`             | If / Rule definition      |                         |
| `.`              | Ends a fact or rule       |                         |
| `[H              | T]`                       | Head and tail of a list |
| `[]`             | Empty list                |                         |
| `,_`             | Ignore variable           |                         |
| `member(X,List)` | Check if X exists in List |                         |
| `fail`           | Force backtracking        |                         |
| `nl`             | Print newline             |                         |
| `write(X)`       | Print X to console        |                         |

---

# ✅ **Key Features**

* **Backtracking:** Finds all suitable jobs.
* **Recursive list checking:** `contains_all` ensures every skill matches.
* **User interaction:** Read user skills and print recommendations.
* **Extensible:** Add more jobs by adding `required` facts.

---

If you want, I can also **draw a flow diagram showing the recursive checking and backtracking in this Prolog program**, which makes it **very clear visually**.

Do you want me to do that?
%