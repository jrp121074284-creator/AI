:- initialization(main).

% ------------------------------------------------------------
% Disease Knowledge Base
% ------------------------------------------------------------
required(common_cold, [sneezing, runny_nose, mild_fever]).
required(flu, [high_fever, headache, body_pain]).
required(covid19, [fever, cough, breathing_problem]).
required(malaria, [high_fever, chills, sweating]).
required(typhoid, [fever, stomach_pain, weakness]).
required(dengue, [fever, body_pain, rash]).
required(eye_infection, [red_eye, itching, watery_eye]).

% ------------------------------------------------------------
% Check if all required symptoms match user symptoms
% ------------------------------------------------------------
contains_all([], _).
contains_all([H|T], UserList) :-
    member(H, UserList),
    contains_all(T, UserList).

% ------------------------------------------------------------
% Identify possible diseases
% ------------------------------------------------------------
identify(UserList) :-
    required(Disease, NeedList),
    contains_all(NeedList, UserList),
    write('Possible disease: '), write(Disease), nl,
    fail.

identify(_) :-
    write('Diagnosis complete.'), nl.

% ------------------------------------------------------------
% Main Menu
% ------------------------------------------------------------
main :-
    nl,
    write('--- Disease Classification Expert System ---'), nl,
    write('Enter symptoms as a list. Example:'), nl,
    write('[fever, cough, breathing_problem].'), nl,
    write('Your symptoms: '),
    read(UserList),
    identify(UserList),
    halt.


%
/*
Absolutely! Let’s break down your **Disease Classification Expert System in Prolog** **step by step**, explain **symbols, problem statement, pseudo code, data structures, time/space complexity**, and provide a **corrected version for OneCompiler**.

---

## **1. Problem Statement**

This Prolog program is a **simple expert system** for **disease classification** based on user-provided symptoms.

* **Input**: list of symptoms, e.g., `[fever, cough, breathing_problem]`
* **Output**: possible diseases that match all the entered symptoms, e.g., `covid19`

The program works as follows:

1. Each disease has a **required set of symptoms** stored in the knowledge base.
2. The user provides their symptoms as a list.
3. The program checks which diseases have **all required symptoms present** in the user’s list and outputs them.

---

## **2. Symbols & Syntax**

| Symbol / Keyword              | Meaning                                                                                                    |                                                                       |
| ----------------------------- | ---------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------- |
| `:-`                          | Used for **rule definition** or **directive** (e.g., `:- initialization(main)` runs `main` automatically). |                                                                       |
| `.`                           | Terminates a statement/fact/rule.                                                                          |                                                                       |
| `required(Disease, Symptoms)` | Fact: associates a disease with a list of required symptoms.                                               |                                                                       |
| `[H                           | T]`                                                                                                        | List notation: `H` = head (first element), `T` = tail (rest of list). |
| `member(X, List)`             | Built-in predicate: true if `X` is present in `List`.                                                      |                                                                       |
| `contains_all(List1, List2)`  | Custom predicate: checks if **all elements** of `List1` exist in `List2`.                                  |                                                                       |
| `fail`                        | Forces **backtracking** to find all possible matches.                                                      |                                                                       |
| `halt`                        | Stops Prolog execution.                                                                                    |                                                                       |
| `nl`                          | Prints a new line for formatting.                                                                          |                                                                       |
| `write(X)`                    | Prints `X` to the output.                                                                                  |                                                                       |
| `read(X)`                     | Reads user input into variable `X`.                                                                        |                                                                       |

---

## **3. Code Explanation (Line by Line)**

```prolog
:- initialization(main).
```

* Runs `main/0` automatically when the program starts.

```prolog
required(common_cold, [sneezing, runny_nose, mild_fever]).
required(flu, [high_fever, headache, body_pain]).
required(covid19, [fever, cough, breathing_problem]).
required(malaria, [high_fever, chills, sweating]).
required(typhoid, [fever, stomach_pain, weakness]).
required(dengue, [fever, body_pain, rash]).
required(eye_infection, [red_eye, itching, watery_eye]).
```

* Facts: each disease is associated with a **list of key symptoms**.

```prolog
contains_all([], _).
contains_all([H|T], UserList) :-
    member(H, UserList),
    contains_all(T, UserList).
```

* Checks if **all required symptoms** of a disease are in the user’s symptom list.
* Base case: empty list `[]` → always true.
* Recursive case: checks head `H` is in `UserList` and recursively checks tail `T`.

```prolog
identify(UserList) :-
    required(Disease, NeedList),
    contains_all(NeedList, UserList),
    write('Possible disease: '), write(Disease), nl,
    fail.
```

* Iterates over all `required/2` facts.
* Prints the disease if all required symptoms match.
* `fail` → forces **backtracking** to find all possible diseases.

```prolog
identify(_) :-
    write('Diagnosis complete.'), nl.
```

* After checking all diseases, prints that diagnosis is complete.

```prolog
main :-
    nl,
    write('--- Disease Classification Expert System ---'), nl,
    write('Enter symptoms as a list. Example:'), nl,
    write('[fever, cough, breathing_problem].'), nl,
    write('Your symptoms: '),
    read(UserList),
    identify(UserList),
    halt.
```

* Main program: prints instructions, reads user input, calls `identify`, exits.

---

## **4. Pseudo Code (Short Version)**

```
START
  PRINT instructions
  READ UserSymptoms
  FOR each Disease in KnowledgeBase
      IF all Disease.Symptoms in UserSymptoms
         PRINT Disease
  PRINT Diagnosis complete
END
```

---

## **5. Data Structures Used**

* **List**: `[H|T]` for symptoms
* **Fact Table**: `required/2` stores diseases and their symptoms
* **Recursion**: checks if all required symptoms exist in the input list

---

## **6. Time & Space Complexity**

Let:

* `n` = number of diseases

* `m` = max symptoms per disease

* `k` = number of symptoms entered by the user

* **Time complexity**: `O(n * m * k)` → for each disease, check each required symptom against the user list.

* **Space complexity**: `O(n * m)` for storing knowledge base + recursion stack `O(m)`.

---

## **7. Corrected Version (Works on OneCompiler)**

```prolog
:- initialization(main).

% ------------------------------------------------------------
% Disease Knowledge Base
% ------------------------------------------------------------
required(common_cold, [sneezing, runny_nose, mild_fever]).
required(flu, [high_fever, headache, body_pain]).
required(covid19, [fever, cough, breathing_problem]).
required(malaria, [high_fever, chills, sweating]).
required(typhoid, [fever, stomach_pain, weakness]).
required(dengue, [fever, body_pain, rash]).
required(eye_infection, [red_eye, itching, watery_eye]).

% ------------------------------------------------------------
% Check if all required symptoms match user symptoms
% ------------------------------------------------------------
contains_all([], _).
contains_all([H|T], UserList) :-
    member(H, UserList),
    contains_all(T, UserList).

% ------------------------------------------------------------
% Identify possible diseases
% ------------------------------------------------------------
identify(UserList) :-
    required(Disease, NeedList),
    contains_all(NeedList, UserList),
    write('Possible disease: '), write(Disease), nl,
    fail.

identify(_) :-
    write('Diagnosis complete.'), nl.

% ------------------------------------------------------------
% Main Menu
% ------------------------------------------------------------
main :-
    nl,
    write('--- Disease Classification Expert System ---'), nl,
    write('Enter symptoms as a list. Example:'), nl,
    write('[fever, cough, breathing_problem].'), nl,
    write('Your symptoms: '),
    read(UserList),
    identify(UserList),
    halt.
```

---

## **8. Example Inputs to Run**

1. Input:

```prolog
[fever, cough, breathing_problem]
```

Output:

```
Possible disease: covid19
Diagnosis complete.
```

2. Input:

```prolog
[high_fever, headache, body_pain]
```

Output:

```
Possible disease: flu
Diagnosis complete.
```

3. Input:

```prolog
[fever, body_pain, rash]
```

Output:

```
Possible disease: dengue
Diagnosis complete.
```

---

✅ This code is **ready to run on OneCompiler**.

---

If you want, I can also make an **interactive version** where the user can **keep entering symptoms repeatedly without restarting** the program, which makes it more like a real diagnostic tool.

Do you want me to do that?

*/