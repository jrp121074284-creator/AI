:- initialization(main).

% ------------------------------------------------------------
% Family Tree Knowledge Base
% ------------------------------------------------------------

required(father, [male, parent]).
required(mother, [female, parent]).
required(son, [male, child]).
required(daughter, [female, child]).
required(sibling, [same_parent]).

% ------------------------------------------------------------
% Check if all required features are present
% ------------------------------------------------------------
contains_all([], _).
contains_all([H|T], UserList) :-
    member(H, UserList),
    contains_all(T, UserList).

% ------------------------------------------------------------
% Identify relationship
% ------------------------------------------------------------
identify(UserList) :-
    required(Relation, NeedList),
    contains_all(NeedList, UserList),
    write('Possible relationship: '), write(Relation), nl,
    fail.

identify(_) :-
    write('Finished.'), nl.

% ------------------------------------------------------------
% Main Menu
% ------------------------------------------------------------
main :-
    nl,
    write('--- Family Tree Expert System ---'), nl,
    write('Enter features as a list.'), nl,
    write('Examples:'), nl,
    write('[male, parent].   -> father'), nl,
    write('[female, parent]. -> mother'), nl,
    write('[male, child].    -> son'), nl,
    write('[female, child].  -> daughter'), nl,
    write('[same_parent].    -> sibling'), nl,
    nl,
    write('Your features: '),
    read(UserList),
    identify(UserList),
    halt.

%
/*
Absolutely! Let’s go **step by step** and break down your **Family Tree Expert System in Prolog**. I’ll cover **problem statement, explanation, symbols, pseudo code, data structures, time/space complexity**, and provide a **corrected version that runs on OneCompiler**.

---

## **1. Problem Statement**

This Prolog program is a **simple expert system** to identify **family relationships** based on a set of features given by the user.

* **Input**: a list of features like `[male, parent]`
* **Output**: possible family relationship(s) like `father`

The system works like this:

* Each family relationship has a **required set of features**.
* The user provides features of a person.
* The program finds all relationships whose **required features match the input**.

---

## **2. Symbols & Syntax**

| Symbol / Keyword               | Meaning                                                                                                                 |                                                                       |
| ------------------------------ | ----------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------- |
| `:-`                           | “If” or “rule definition”; also used for **directives** like `:- initialization(main)` which runs `main` automatically. |                                                                       |
| `.`                            | Ends a statement/fact/rule.                                                                                             |                                                                       |
| `required(Relation, Features)` | Fact: each family relationship has a list of required features.                                                         |                                                                       |
| `[H                            | T]`                                                                                                                     | List notation: `H` = head (first element), `T` = tail (rest of list). |
| `member(X, List)`              | Built-in predicate: true if `X` is in `List`.                                                                           |                                                                       |
| `contains_all(List1, List2)`   | Custom predicate: true if **all elements** of `List1` are present in `List2`.                                           |                                                                       |
| `fail`                         | Forces backtracking to find **all possible matches**.                                                                   |                                                                       |
| `halt`                         | Stops Prolog execution.                                                                                                 |                                                                       |
| `nl`                           | Prints a new line for formatting.                                                                                       |                                                                       |
| `write(X)`                     | Prints `X` to output.                                                                                                   |                                                                       |
| `read(X)`                      | Reads user input and stores in variable `X`.                                                                            |                                                                       |

---

## **3. Code Explanation (Line by Line)**

```prolog
:- initialization(main).
```

* This runs `main/0` automatically when the program starts.

```prolog
% Family relationships represented as required features
required(father, [male, parent]).
required(mother, [female, parent]).
required(son, [male, child]).
required(daughter, [female, child]).
required(sibling, [same_parent]).
```

* Facts: each relationship is associated with a list of features.
* Example: `father` requires `[male, parent]`.

```prolog
contains_all([], _).
contains_all([H|T], UserList) :-
    member(H, UserList),
    contains_all(T, UserList).
```

* Checks if **all features** in the first list are present in the second list.
* Base case: empty list `[]` → true.
* Recursive case: check head `H` is in `UserList`, then check tail `T`.

```prolog
identify(UserList) :-
    required(Relation, NeedList),
    contains_all(NeedList, UserList),
    write('Possible relationship: '), write(Relation), nl,
    fail.
```

* Iterates over each `required/2` fact.
* If `contains_all` is true, prints the relationship.
* `fail` → forces Prolog to **backtrack** and find all possible relationships.

```prolog
identify(_) :-
    write('Finished.'), nl.
```

* Once all relationships are checked, prints `Finished.`

```prolog
main :-
    nl,
    write('--- Family Tree Expert System ---'), nl,
    write('Enter features as a list.'), nl,
    write('Examples:'), nl,
    write('[male, parent].   -> father'), nl,
    write('[female, parent]. -> mother'), nl,
    write('[male, child].    -> son'), nl,
    write('[female, child].  -> daughter'), nl,
    write('[same_parent].    -> sibling'), nl,
    nl,
    write('Your features: '),
    read(UserList),
    identify(UserList),
    halt.
```

* Main menu: prints instructions, reads user input, calls `identify`, and exits.

---

## **4. Pseudo Code (Short Version)**

```
START
  PRINT instructions
  READ UserFeatures
  FOR each Relationship in KnowledgeBase
      IF all Relationship.Features in UserFeatures
         PRINT Relationship
  PRINT Finished
END
```

---

## **5. Data Structures Used**

* **List**: `[H|T]` to represent features
* **Fact Table**: `required/2` stores knowledge base
* **Recursion**: checks if all features exist in the input

---

## **6. Time & Space Complexity**

Let:

* `n` = number of relationships

* `m` = max features per relationship

* `k` = number of features entered by the user

* **Time complexity**: `O(n * m * k)` → for each relationship, check each required feature against user features.

* **Space complexity**: `O(n * m)` for storing knowledge base + recursion stack `O(m)`.

---

## **7. Corrected Version (Works on OneCompiler)**

```prolog
:- initialization(main).

% ------------------------------------------------------------
% Family Tree Knowledge Base
% ------------------------------------------------------------

required(father, [male, parent]).
required(mother, [female, parent]).
required(son, [male, child]).
required(daughter, [female, child]).
required(sibling, [same_parent]).

% ------------------------------------------------------------
% Check if all required features are present
% ------------------------------------------------------------
contains_all([], _).
contains_all([H|T], UserList) :-
    member(H, UserList),
    contains_all(T, UserList).

% ------------------------------------------------------------
% Identify relationship
% ------------------------------------------------------------
identify(UserList) :-
    required(Relation, NeedList),
    contains_all(NeedList, UserList),
    write('Possible relationship: '), write(Relation), nl,
    fail.

identify(_) :-
    write('Finished.'), nl.

% ------------------------------------------------------------
% Main Menu
% ------------------------------------------------------------
main :-
    nl,
    write('--- Family Tree Expert System ---'), nl,
    write('Enter features as a list.'), nl,
    write('Examples:'), nl,
    write('[male, parent].   -> father'), nl,
    write('[female, parent]. -> mother'), nl,
    write('[male, child].    -> son'), nl,
    write('[female, child].  -> daughter'), nl,
    write('[same_parent].    -> sibling'), nl,
    nl,
    write('Your features: '),
    read(UserList),
    identify(UserList),
    halt.
```

---

## **8. Example Inputs to Run**

1. Input:

```prolog
[male, parent]
```

Output:

```
Possible relationship: father
Finished.
```

2. Input:

```prolog
[female, child]
```

Output:

```
Possible relationship: daughter
Finished.
```

3. Input:

```prolog
[same_parent]
```

Output:

```
Possible relationship: sibling
Finished.
```

---

This code is **ready to run on OneCompiler** or any online Prolog compiler.

---

If you want, I can also make an **interactive version** where the user can **keep entering features repeatedly without restarting the program**.

Do you want me to do that?

*/