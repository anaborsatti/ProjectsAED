# ProjectsAED
This repository contains exercises, projects, and notes developed during the Algorithms and Data Structures (AED) course at the University of Algarve, using the Java programming language.

**Project 1 – Abstract Data Types**  
This project, developed in Java, implements a "Spellbook" inspired by the Dungeons & Dragons universe, focusing on the application of Abstract Data Types (ADTs). The main classes are `Spell`, which represents a spell with attributes such as name, level, and magic school; `SpellBook`, which stores and organizes the spells; and `SMS`, which provides an interface for interacting with and manipulating the book. The system allows adding, removing, filtering, sorting, and saving spells, exploring concepts like generic lists, predicates, custom sorting, and data persistence in files. The project also emphasizes the separation between data logic and textual interface.

**Project 2 - Collections: Implementation, Usage, and Analysis (2024/2025)**  
In this project, we implemented the `StackList` class, a generic stack based on linked lists, following the Last In, First Out (LIFO) principle. The stack provides operations such as push, pop, peek, isEmpty, and size. We then applied this stack in a backtracking algorithm to solve a Sudoku puzzle. The `SudokuState` class is used to represent the game state and allows generating valid states and checking solutions. The backtracking algorithm is implemented iteratively using the stack, instead of recursion, optimizing the search process for Sudoku solutions.

**Project 3 – Sorting and Trees**  
This project implements the QuickSort algorithm, an efficient sorting technique that uses the divide-and-conquer strategy. QuickSort selects a pivot and partitions the list into two subarrays: one with smaller elements and another with larger elements than the pivot. This process is recursively repeated for the subarrays, ensuring efficient sorting. The implementation includes variants like random QuickSort, which selects the pivot randomly, improving performance in certain cases.  
Additionally, a balanced search tree called FatTree was developed. This tree is designed to optimize insertion, search, and deletion operations, maintaining a dynamic balance to ensure these operations are performed efficiently, with execution time proportional to the logarithm of the number of elements in the tree.

**Project 4 – Hash Tables**  
This project aims to implement and study hash tables, addressing two main problems. Problem A involves implementing a hash table with open addressing and double hashing, with functionalities such as insertion, lazy deletion, and dynamic resizing. Problem B focuses on implementing sparse matrices using hash tables to store only non-zero elements, optimizing operations like matrix multiplication and addition. The project also includes efficiency tests and comparisons between traditional and sparse approaches.



