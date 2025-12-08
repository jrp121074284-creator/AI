:- initialization(main).

% ---------------- Facts ----------------
vehicle(car). vehicle(bike). vehicle(truck). vehicle(bus).

wheels(car, 4). wheels(bike, 2). wheels(truck, 6). wheels(bus, 4).

private_transport(car). private_transport(bike).
public_transport(truck). public_transport(bus).

fuel(car, petrol). fuel(bike, petrol). fuel(truck, diesel). fuel(bus, diesel).

% ---------------- Rules ----------------
two_wheeler(X) :- wheels(X, 2).
four_wheeler(X) :- wheels(X, 4).
public_vehicle(X) :- public_transport(X).
private_vehicle(X) :- private_transport(X).

identify(car, X) :- wheels(X, 4), private_transport(X), vehicle(X).
identify(bike, X) :- wheels(X, 2), private_transport(X), vehicle(X).
identify(truck, X) :- wheels(X, 6), public_transport(X), vehicle(X).
identify(bus, X) :- wheels(X, 4), public_transport(X), vehicle(X).

% ---------------- Main ----------------
main :-
    nl, write('--- Vehicle Classification ---'), nl,
    
    write('Two Wheelers:'), nl,
    two_wheeler(X), write(X), nl, fail; true,
    
    write('Four Wheelers:'), nl,
    four_wheeler(Y), write(Y), nl, fail; true,
    
    write('Public Vehicles:'), nl,
    public_vehicle(P), write(P), nl, fail; true,
    
    write('Private Vehicles:'), nl,
    private_vehicle(Q), write(Q), nl, fail; true,
    
    write('Identify Vehicles:'), nl,
    identify(car, C), write(C), nl, fail; true,
    identify(bike, B), write(B), nl, fail; true,
    identify(truck, T), write(T), nl, fail; true,
    identify(bus, U), write(U), nl, fail; true.


%/*

Absolutely! Let’s create a **simple Vehicle Classification Expert System in Prolog** and explain it **step by step**, including **problem statement, symbols, pseudo code, data structures, complexity**, and a **ready-to-run version for OneCompiler**.

---

## **1. Problem Statement**

The purpose of this program is to **classify vehicles** based on their properties:

* **Input:** basic vehicle attributes like type, wheels, fuel, capacity.
* **Output:** classify vehicles as:

  * `car`, `bike`, `truck`, `bus`
  * `two_wheeler`, `four_wheeler`
  * `public_transport`, `private_transport`

The system uses **facts** to define vehicles and **rules** to infer classifications.

---

## **2. Symbols & Syntax**

| Symbol / Keyword | Meaning                                                                                                                                      |
| ---------------- | -------------------------------------------------------------------------------------------------------------------------------------------- |
| `.`              | Ends a fact/rule/statement.                                                                                                                  |
| `:-`             | Rule definition. Example: `car(X) :- four_wheeler(X), private_transport(X)` means X is a car if it has four wheels and is private transport. |
| `,`              | Logical AND. Both conditions must be true.                                                                                                   |
| `write(X)`       | Prints X.                                                                                                                                    |
| `nl`             | New line.                                                                                                                                    |
| `read(X)`        | Reads user input into variable X.                                                                                                            |
| `fail`           | Forces backtracking to find **all solutions**.                                                                                               |
| Variables        | Uppercase (X, Y) – placeholders.                                                                                                             |
| Constants        | Lowercase (car, bike) – specific vehicles.                                                                                                   |

---

## **3. Knowledge Base (Facts)**

```prolog
% Vehicle Types
vehicle(car).
vehicle(bike).
vehicle(truck).
vehicle(bus).

% Number of Wheels
wheels(car, 4).
wheels(bike, 2).
wheels(truck, 6).
wheels(bus, 4).

% Transport Type
private_transport(car).
private_transport(bike).
public_transport(bus).
public_transport(truck).

% Fuel Type
fuel(car, petrol).
fuel(bike, petrol).
fuel(truck, diesel).
fuel(bus, diesel).
```

* Facts define **basic attributes** for each vehicle.
* Example: `wheels(car, 4)` → car has 4 wheels.

---

## **4. Rules (Classification)**

```prolog
two_wheeler(X) :- wheels(X, 2).
four_wheeler(X) :- wheels(X, 4).
public_vehicle(X) :- public_transport(X).
private_vehicle(X) :- private_transport(X).

identify(car, X) :- wheels(X, 4), private_transport(X), vehicle(X).
identify(bike, X) :- wheels(X, 2), private_transport(X), vehicle(X).
identify(truck, X) :- wheels(X, 6), public_transport(X), vehicle(X).
identify(bus, X) :- wheels(X, 4), public_transport(X), vehicle(X).
```

* `two_wheeler(X)` → vehicle with 2 wheels
* `four_wheeler(X)` → vehicle with 4 wheels
* `identify/2` → maps attributes to a **specific vehicle**

---

## **5. Main Program**

```prolog
main :-
    nl, write('--- Vehicle Classification ---'), nl,
    
    write('Two Wheelers:'), nl,
    two_wheeler(X), write(X), nl, fail; true,
    
    write('Four Wheelers:'), nl,
    four_wheeler(Y), write(Y), nl, fail; true,
    
    write('Public Vehicles:'), nl,
    public_vehicle(P), write(P), nl, fail; true,
    
    write('Private Vehicles:'), nl,
    private_vehicle(Q), write(Q), nl, fail; true,
    
    write('Identify Vehicles:'), nl,
    identify(car, C), write(C), nl, fail; true,
    identify(bike, B), write(B), nl, fail; true,
    identify(truck, T), write(T), nl, fail; true,
    identify(bus, U), write(U), nl, fail; true.
```

* Queries all **rules** and prints results.
* `fail` → ensures backtracking to find **all matches**
* `; true` → ensures program continues after backtracking

---

## **6. Pseudo Code (Short Version)**

```
START
  DEFINE facts: vehicle, wheels, fuel, transport type
  DEFINE rules: two_wheeler, four_wheeler, public_vehicle, private_vehicle, identify
  FOR each classification:
      PRINT all matching vehicles
END
```

---

## **7. Data Structures Used**

* **Facts** → store vehicle attributes
* **Variables** → placeholders in rules
* **Backtracking** → used to enumerate all matching vehicles

---

## **8. Time & Space Complexity**

Let:

* `n` = number of vehicles

* `m` = number of attributes per vehicle

* **Time complexity:** `O(n*m)` → each rule checks relevant attributes

* **Space complexity:** `O(n*m)` for storing facts + recursion stack

---

## **9. Corrected Prolog Code (OneCompiler Ready)**

```prolog
:- initialization(main).

% ---------------- Facts ----------------
vehicle(car). vehicle(bike). vehicle(truck). vehicle(bus).

wheels(car, 4). wheels(bike, 2). wheels(truck, 6). wheels(bus, 4).

private_transport(car). private_transport(bike).
public_transport(truck). public_transport(bus).

fuel(car, petrol). fuel(bike, petrol). fuel(truck, diesel). fuel(bus, diesel).

% ---------------- Rules ----------------
two_wheeler(X) :- wheels(X, 2).
four_wheeler(X) :- wheels(X, 4).
public_vehicle(X) :- public_transport(X).
private_vehicle(X) :- private_transport(X).

identify(car, X) :- wheels(X, 4), private_transport(X), vehicle(X).
identify(bike, X) :- wheels(X, 2), private_transport(X), vehicle(X).
identify(truck, X) :- wheels(X, 6), public_transport(X), vehicle(X).
identify(bus, X) :- wheels(X, 4), public_transport(X), vehicle(X).

% ---------------- Main ----------------
main :-
    nl, write('--- Vehicle Classification ---'), nl,
    
    write('Two Wheelers:'), nl,
    two_wheeler(X), write(X), nl, fail; true,
    
    write('Four Wheelers:'), nl,
    four_wheeler(Y), write(Y), nl, fail; true,
    
    write('Public Vehicles:'), nl,
    public_vehicle(P), write(P), nl, fail; true,
    
    write('Private Vehicles:'), nl,
    private_vehicle(Q), write(Q), nl, fail; true,
    
    write('Identify Vehicles:'), nl,
    identify(car, C), write(C), nl, fail; true,
    identify(bike, B), write(B), nl, fail; true,
    identify(truck, T), write(T), nl, fail; true,
    identify(bus, U), write(U), nl, fail; true.
```

---

## **10. Example Output**

```
--- Vehicle Classification ---
Two Wheelers:
bike
Four Wheelers:
car
bus
Public Vehicles:
truck
bus
Private Vehicles:
car
bike
Identify Vehicles:
car
bike
truck
bus
```

---

✅ This is a **simple, fully functional vehicle classification Prolog system** ready for **OneCompiler**.

---

I can also make an **interactive version** where the user can **input vehicle attributes** (wheels, fuel, transport type) and the system will **identify the vehicle dynamically**.

Do you want me to do that?
*/