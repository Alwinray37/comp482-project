# Comp482 - Project Priority Pass 
**Group 2 Team:**
- Alwin Roble
- Jessica Bright
- Rebecca Rian
- Esmaralda Munoz
- Leonid Vartanian

## Overview

This project focuses on solving the Priority Pass shortest path problem.

We are given a directed graph where each road has a travel time. The goal is to find the minimum travel time from a starting intersection `s` to a destination intersection `t`. The driver may use one Priority Pass on at most one road, which reduces that road’s travel time from `w` to `floor(w / 2)`.

If there is no path from `s` to `t`, the output should be `-1`.

## Approach

Our solution is based on a shortest path algorithm using two states for each node:

- one state where the Priority Pass has not been used yet
- one state where the Priority Pass has already been used

This allows us to track the minimum possible cost both before and after using the pass, and compute the best final answer efficiently.

## Project Structure

- `pseudocode/`
  Team members should place pseudocode drafts, planning notes, and algorithm ideas here.

- `tests/`
  Team members should place test cases here to help verify correctness.

- `solution.java`
  This file contains the Java implementation of the solution.


## Requirements

Ensure you have java
```bash 
java -version
javac -version 
```

## Build and Run

Compile the program:
```bash
javac Solution.java
```

Run the program:
```bash
java Solution
```

Run Tests: 
paste this in your terminal
update 'RaySolution' to your Solution.java filename
run all tests:
```bash
for f in tests/*.in; do
  name="${f%.in}"
  actual=$(java -cp src RaySolution < "$f" 2>/dev/null)
  expected=$(cat "${name}.out")
  if [ "$actual" = "$expected" ]; then
    echo "PASS: $name"
  else
    echo "FAIL: $name  (got: $actual, expected: $expected)"
  fi
done
```

Run one test
```bash
java -cp src RaySolution < tests/[testname].in

```

## Notes: 

Whenever youre starting, pull latest version if necessary:
```bash
git pull origin main
```
