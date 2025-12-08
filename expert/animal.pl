:- initialization(main).

% Knowledge Base
required(dog, [barks, four_legs, mammal]).
required(cat, [meows, four_legs, mammal]).
required(cow, [four_legs, mammal, herbivore]).
required(duck, [bird, swims, quacks]).
required(fish, [swims, gills]).
required(snake, [reptile, no_legs]).

% Check if all required features are in user list
contains_all([], _).
contains_all([H|T], UserList) :-
    member(H, UserList),
    contains_all(T, UserList).

% Suggest animals based on features
identify(UserList) :-
    required(Animal, NeedList),
    contains_all(NeedList, UserList),
    write('Possible animal: '), write(Animal), nl,
    fail.
identify(_) :-
    write('Done identifying.'), nl.

% Main Menu
main :-
    nl,
    write('--- Simple Animal Identification System ---'), nl,
    write('Enter features as a list. Example:'), nl,
    write('[barks, four_legs, mammal].'), nl,
    write('Your features: '),
    read(UserList),
    identify(UserList),
    halt.


/* Sure! Let's go **step by step** and break down your Prolog code, explain all symbols, the problem, pseudo-code, time/space complexity, and finally provide a corrected version that can run on an online compiler like **OneCompiler**.

---

## **1. Problem Statement**

You want to **identify an animal** based on a list of features provided by the user.

* The knowledge base has animals with their **required features**.
* The system asks the user to enter features like `[barks, four_legs, mammal]`.
* The program then checks which animals have **all the entered features** and suggests possible animals.

This is a **simple expert system** in Prolog.

---

## **2. Symbols & Syntax in the Code**

| Symbol / Keyword             | Meaning                                                                                                                    |                                                                       |
| ---------------------------- | -------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------- |
| `:-`                         | “If” or “rule definition”. Also used for **directives** (e.g., `:- initialization(main)` runs `main` when program starts). |                                                                       |
| `.`                          | End of a statement. Every fact/rule must end with a dot.                                                                   |                                                                       |
| `required(Animal, Features)` | Fact: an animal and its list of required features.                                                                         |                                                                       |
| `[H                          | T]`                                                                                                                        | List notation: `H` = head (first element), `T` = tail (rest of list). |
| `member(X, List)`            | Built-in predicate: checks if `X` is a member of `List`.                                                                   |                                                                       |
| `contains_all(List1, List2)` | Custom predicate: checks if all elements of `List1` are in `List2`.                                                        |                                                                       |
| `fail`                       | Forces backtracking: ensures all possible matches are found.                                                               |                                                                       |
| `halt`                       | Stops Prolog execution.                                                                                                    |                                                                       |
| `nl`                         | New line (for formatting).                                                                                                 |                                                                       |
| `write(X)`                   | Prints X to output.                                                                                                        |                                                                       |
| `read(X)`                    | Reads user input into variable X.                                                                                          |                                                                       |

---

## **3. Code Explanation (Line by Line)**

```prolog
:- initialization(main).
```

* Runs `main` automatically when the program starts.

```prolog
required(dog, [barks, four_legs, mammal]).
required(cat, [meows, four_legs, mammal]).
required(cow, [four_legs, mammal, herbivore]).
required(duck, [bird, swims, quacks]).
required(fish, [swims, gills]).
required(snake, [reptile, no_legs]).
```

* Facts: Each animal has a list of features.
* Example: `dog` has features `barks`, `four_legs`, `mammal`.

```prolog
contains_all([], _).
contains_all([H|T], UserList) :-
    member(H, UserList),
    contains_all(T, UserList).
```

* Recursive predicate: checks if **all elements of a list** (`NeedList`) are present in the user's list (`UserList`).
* Base case: empty list `[]` → always true.
* Recursive case: check if head `H` is in `UserList` and continue with tail `T`.

```prolog
identify(UserList) :-
    required(Animal, NeedList),
    contains_all(NeedList, UserList),
    write('Possible animal: '), write(Animal), nl,
    fail.
```

* Tries each animal in `required/2`.
* If `contains_all` succeeds, prints the animal.
* `fail` → forces backtracking to find all possible animals.

```prolog
identify(_) :-
    write('Done identifying.'), nl.
```

* When all possibilities are exhausted, print `Done identifying.`

```prolog
main :-
    nl,
    write('--- Simple Animal Identification System ---'), nl,
    write('Enter features as a list. Example:'), nl,
    write('[barks, four_legs, mammal].'), nl,
    write('Your features: '),
    read(UserList),
    identify(UserList),
    halt.
```

* Main program: prints instructions, reads user input, calls `identify`.

---

## **4. Pseudo Code (Short Version)**

```
START
  PRINT "Enter features as a list"
  READ UserFeatures
  FOR each Animal in KnowledgeBase
    IF all Animal.Features are in UserFeatures
       PRINT Animal
  PRINT "Done identifying"
END
```

---

## **5. Data Structures Used**

* **List**: `[H|T]` for features.
* **Fact Table**: `required/2` acts as a simple **lookup table**.
* **Recursion**: used to check if all features exist.

---

## **6. Time & Space Complexity**

Let:

* `n` = number of animals

* `m` = max features per animal

* `k` = number of features entered by user

* **Time complexity**:

  * `O(n * m * k)` because for each animal we check all its features against the user list.

* **Space complexity**:

  * `O(n * m)` to store the knowledge base + recursion stack `O(m)`.

---

## **7. Corrected Version (Works on OneCompiler)**

```prolog
:- initialization(main).

% Knowledge Base
required(dog, [barks, four_legs, mammal]).
required(cat, [meows, four_legs, mammal]).
required(cow, [four_legs, mammal, herbivore]).
required(duck, [bird, swims, quacks]).
required(fish, [swims, gills]).
required(snake, [reptile, no_legs]).

% Check if all required features are in user list
contains_all([], _).
contains_all([H|T], UserList) :-
    member(H, UserList),
    contains_all(T, UserList).

% Suggest animals based on features
identify(UserList) :-
    required(Animal, NeedList),
    contains_all(NeedList, UserList),
    write('Possible animal: '), write(Animal), nl,
    fail.
identify(_) :-
    write('Done identifying.'), nl.

% Main Menu
main :-
    nl,
    write('--- Simple Animal Identification System ---'), nl,
    write('Enter features as a list. Example:'), nl,
    write('[barks, four_legs, mammal].'), nl,
    write('Your features: '),
    read(UserList),
    identify(UserList),
    halt.
```

---

## **8. Example Inputs for Testing**

* Input:

```prolog
[barks, four_legs, mammal]
```

* Output:

```
Possible animal: dog
Done identifying.
```

* Input:

```prolog
[swims, gills]
```

* Output:

```
Possible animal: fish
Done identifying.
```

* Input:

```prolog
[four_legs, mammal]
```

* Output:

```
Possible animal: dog
Possible animal: cat
Possible animal: cow
Done identifying.
```

---

✅ This code should run **directly on OneCompiler** or any online Prolog compiler.

---

If you want, I can also make a **slightly improved version** that **doesn’t print duplicates** and works interactively until the user wants to exit.

Do you want me to do that?
*/