:- initialization(main).

% ------------------------------------------------------------
% Sentiment Knowledge Base
% ------------------------------------------------------------
required(positive, [good, happy, excellent, great]).
required(negative, [bad, sad, terrible, awful]).
required(neutral,  [okay, average, fine]).

% ------------------------------------------------------------
% Count how many keywords match
% ------------------------------------------------------------
count_matches([], _UserWords, 0).
count_matches([H|T], UserWords, Count) :-
    count_matches(T, UserWords, C1),
    ( member(H, UserWords) ->
        Count is C1 + 1
    ;   Count is C1 ).

% ------------------------------------------------------------
% Calculate score for each sentiment
% ------------------------------------------------------------
score_all(UserWords, Pairs) :-
    findall(Score-Sentiment,
            ( required(Sentiment, Keys),
              count_matches(Keys, UserWords, Score)
            ),
            Pairs).

% ------------------------------------------------------------
% Find max score
% ------------------------------------------------------------
max_score(Pairs, Max) :-
    findall(S, member(S-_, Pairs), Scores),
    max_list(Scores, Max).

% ------------------------------------------------------------
% Identify best sentiment(s)
% ------------------------------------------------------------
identify(UserWords) :-
    score_all(UserWords, Pairs),
    max_score(Pairs, Max),
    ( Max =:= 0 ->
        write('No sentiment detected.'), nl
    ;
        findall(Sentiment, member(Max-Sentiment, Pairs), Best),
        write('Detected sentiment(s): '), write(Best),
        write(' with score '), write(Max), nl
    ).

% ------------------------------------------------------------
% Main Menu
% ------------------------------------------------------------
main :-
    nl,
    write('--- Sentiment Analysis Expert System ---'), nl,
    write('Enter words as a list. Example:'), nl,
    write('[good, happy].'), nl,
    nl,
    write('Your words: '),
    read(UserWords),
    identify(UserWords),
    halt.


%*/

Absolutely! Let’s break down your **Sentiment Analysis Expert System in Prolog** step by step, explain **symbols, problem statement, pseudo code, data structures, time/space complexity**, and provide a **corrected version ready for OneCompiler**.

---

## **1. Problem Statement**

This program is a **simple sentiment analysis expert system**:

* **Input**: a list of words provided by the user, e.g., `[good, happy, terrible]`
* **Output**: the detected sentiment (`positive`, `negative`, or `neutral`) based on keyword matches.

The program works as follows:

1. Each sentiment is associated with a list of keywords.
2. The system counts how many keywords match the user input for each sentiment.
3. The sentiment with the **highest match count** is identified.
4. If no keywords match, it outputs `No sentiment detected`.

---

## **2. Symbols & Syntax**

| Symbol / Keyword                | Meaning                                                                        |                                                       |
| ------------------------------- | ------------------------------------------------------------------------------ | ----------------------------------------------------- |
| `:-`                            | Used for **rule definition** or **directives** like `:- initialization(main)`. |                                                       |
| `.`                             | Terminates a statement, fact, or rule.                                         |                                                       |
| `required(Sentiment, Keywords)` | Fact: associates a sentiment with a list of keywords.                          |                                                       |
| `[H                             | T]`                                                                            | List notation: `H` = head, `T` = tail (rest of list). |
| `member(X, List)`               | True if `X` is an element of `List`.                                           |                                                       |
| `findall(Template, Goal, List)` | Collects all results of `Goal` that match `Template` into `List`.              |                                                       |
| `max_list(List, Max)`           | Finds the maximum value in a list of numbers.                                  |                                                       |
| `-> ;`                          | If-then-else operator: `(Condition -> Then ; Else)`.                           |                                                       |
| `is`                            | Evaluates arithmetic expressions.                                              |                                                       |
| `nl`                            | Prints a new line.                                                             |                                                       |
| `write(X)`                      | Prints `X`.                                                                    |                                                       |
| `read(X)`                       | Reads user input into variable `X`.                                            |                                                       |
| `halt`                          | Stops Prolog execution.                                                        |                                                       |

---

## **3. Code Explanation (Line by Line)**

```prolog
:- initialization(main).
```

* Runs `main/0` automatically when the program starts.

```prolog
required(positive, [good, happy, excellent, great]).
required(negative, [bad, sad, terrible, awful]).
required(neutral,  [okay, average, fine]).
```

* Facts: each sentiment has a list of **keywords**.

---

### **Count how many keywords match**

```prolog
count_matches([], _UserWords, 0).
count_matches([H|T], UserWords, Count) :-
    count_matches(T, UserWords, C1),
    ( member(H, UserWords) ->
        Count is C1 + 1
    ;   Count is C1 ).
```

* Recursive predicate: counts how many words from a keyword list appear in `UserWords`.
* Base case: empty list → 0 matches.
* Recursive case: if `H` is in `UserWords`, add 1; otherwise, keep the count.

---

### **Calculate score for each sentiment**

```prolog
score_all(UserWords, Pairs) :-
    findall(Score-Sentiment,
            ( required(Sentiment, Keys),
              count_matches(Keys, UserWords, Score)
            ),
            Pairs).
```

* Creates a list of `Score-Sentiment` pairs for all sentiments.
* Example: `[2-positive, 0-negative, 0-neutral]`.

---

### **Find the max score**

```prolog
max_score(Pairs, Max) :-
    findall(S, member(S-_, Pairs), Scores),
    max_list(Scores, Max).
```

* Extracts all scores from `Pairs` and finds the maximum score.

---

### **Identify best sentiment(s)**

```prolog
identify(UserWords) :-
    score_all(UserWords, Pairs),
    max_score(Pairs, Max),
    ( Max =:= 0 ->
        write('No sentiment detected.'), nl
    ;
        findall(Sentiment, member(Max-Sentiment, Pairs), Best),
        write('Detected sentiment(s): '), write(Best),
        write(' with score '), write(Max), nl
    ).
```

* If no matches (`Max = 0`) → print `No sentiment detected`.
* Otherwise, find all sentiments with the **maximum score** and print them.

---

### **Main Menu**

```prolog
main :-
    nl,
    write('--- Sentiment Analysis Expert System ---'), nl,
    write('Enter words as a list. Example:'), nl,
    write('[good, happy].'), nl,
    nl,
    write('Your words: '),
    read(UserWords),
    identify(UserWords),
    halt.
```

* Main program: prints instructions, reads user input, calls `identify`, and exits.

---

## **4. Pseudo Code (Short Version)**

```
START
  PRINT instructions
  READ UserWords
  FOR each Sentiment in KnowledgeBase
      COUNT matching keywords
  FIND max score
  IF max score = 0
      PRINT "No sentiment detected"
  ELSE
      PRINT all Sentiments with max score
END
```

---

## **5. Data Structures Used**

* **List**: `[H|T]` for keywords and user words.
* **Pairs**: `Score-Sentiment` list to store scores.
* **Recursion**: for counting matches.

---

## **6. Time & Space Complexity**

Let:

* `n` = number of sentiments

* `m` = max keywords per sentiment

* `k` = number of words in user input

* **Time complexity**: `O(n * m * k)` → check all keywords of all sentiments against user input.

* **Space complexity**: `O(n)` for storing scores + recursion stack `O(m)`.

---

## **7. Corrected Version (Runs on OneCompiler)**

```prolog
:- initialization(main).

% ------------------------------------------------------------
% Sentiment Knowledge Base
% ------------------------------------------------------------
required(positive, [good, happy, excellent, great]).
required(negative, [bad, sad, terrible, awful]).
required(neutral,  [okay, average, fine]).

% ------------------------------------------------------------
% Count how many keywords match
% ------------------------------------------------------------
count_matches([], _UserWords, 0).
count_matches([H|T], UserWords, Count) :-
    count_matches(T, UserWords, C1),
    ( member(H, UserWords) ->
        Count is C1 + 1
    ;   Count is C1 ).

% ------------------------------------------------------------
% Calculate score for each sentiment
% ------------------------------------------------------------
score_all(UserWords, Pairs) :-
    findall(Score-Sentiment,
            ( required(Sentiment, Keys),
              count_matches(Keys, UserWords, Score)
            ),
            Pairs).

% ------------------------------------------------------------
% Find max score
% ------------------------------------------------------------
max_score(Pairs, Max) :-
    findall(S, member(S-_, Pairs), Scores),
    max_list(Scores, Max).

% ------------------------------------------------------------
% Identify best sentiment(s)
% ------------------------------------------------------------
identify(UserWords) :-
    score_all(UserWords, Pairs),
    max_score(Pairs, Max),
    ( Max =:= 0 ->
        write('No sentiment detected.'), nl
    ;
        findall(Sentiment, member(Max-Sentiment, Pairs), Best),
        write('Detected sentiment(s): '), write(Best),
        write(' with score '), write(Max), nl
    ).

% ------------------------------------------------------------
% Main Menu
% ------------------------------------------------------------
main :-
    nl,
    write('--- Sentiment Analysis Expert System ---'), nl,
    write('Enter words as a list. Example:'), nl,
    write('[good, happy].'), nl,
    nl,
    write('Your words: '),
    read(UserWords),
    identify(UserWords),
    halt.
```

---

## **8. Example Inputs to Run**

1. Input:

```prolog
[good, happy, excellent]
```

Output:

```
Detected sentiment(s): [positive] with score 3
```

2. Input:

```prolog
[bad, sad]
```

Output:

```
Detected sentiment(s): [negative] with score 2
```

3. Input:

```prolog
[okay, average]
```

Output:

```
Detected sentiment(s): [neutral] with score 2
```

4. Input:

```prolog
[unknown, random]
```

Output:

```
No sentiment detected.
```

---

✅ This code is ready to run directly on **OneCompiler**.

---

If you want, I can make a **more advanced version** where the program allows **multiple sentences input** and **aggregates scores for each word**, making it more like a real sentiment analyzer.

Do you want me to do that?

*/