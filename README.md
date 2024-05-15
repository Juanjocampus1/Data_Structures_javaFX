# Java Data Structures Overview

This README provides a detailed overview of common data structures in Java, highlighting their differences, use cases, and implementation details.

## 1. Arrays

### Description
An array is a fixed-size data structure that stores elements of the same type in contiguous memory locations.

### Characteristics
- **Fixed Size**: Size must be specified at the time of creation and cannot be changed.
- **Random Access**: Elements can be accessed directly via indices.
- **Homogeneous Elements**: Only elements of the same type can be stored.

### Advantages
- **Fast Access**: Constant time complexity (O(1)) for accessing elements by index.
- **Memory Efficiency**: No overhead for pointers or additional structures.

### Disadvantages
- **Fixed Size**: Inflexible, cannot change size dynamically.
- **Costly Insertions/Deletions**: Adding or removing elements requires shifting, leading to O(n) time complexity.

### Example
```java
int[] numbers = {10, 20, 30, 40, 50};
System.out.println("Element at index 0: " + numbers[0]);
