# Khadra App Exercise

## Objective
Complete the implementation of a simple list management app with search functionality using Jetpack Compose.

## Key Concepts
- State management in Jetpack Compose
- LazyColumn for efficient lists
- Composable function structure
- Material Design 3 components

## Code Structure

### 1. State Management (MainActivity.kt)
- `FirstUI`: Main composable containing app logic
- `SearchInputBar`: Handles user input and actions
- `CardsList`: Displays items in a scrollable list

### 2. Theme Configuration
- `Color.kt`: Custom color definitions
- `Theme.kt`: Light/dark theme configuration
- `Type.kt`: Typography settings

## Exercise Tasks

### Part 1: State Management
1. Create state variables for:
    - Text input field value
    - List of items
    - Search query

2. Connect the TextField to the text state variable

### Part 2: Add Functionality
3. Implement the "Add" button to:
    - Add non-empty text to the list
    - Clear the input field after adding

4. Implement the "Search" button to:
    - Filter items containing the search query (case-insensitive)
    - Show all items when search is empty

### Part 3: List Display
5. Complete the `CardsList` composable to:
    - Display actual items from the list
    - Use `LazyColumn` for efficient scrolling
    - Show each item in a Material Design Card

### Part 4: Bonus Challenges
6. Add validation to prevent empty items
7. Implement real-time search (without button)
8. Add delete functionality for items
9. Add error message for no search results

## Implementation Steps

### 1. State Variables
```kotlin
 In FirstUI composable
var textValue by remember { mutableStateOf("") }
val allItems = remember { mutableStateListOf<String>() }
var searchQuery by remember { mutableStateOf("") }
```

### 2. Filter Logic
```kotlin
val displayedItems = if (searchQuery.isEmpty()) {
    allItems
} else {
    allItems.filter { it.contains(searchQuery, ignoreCase = true) }
}
```

### 3. Button Handlers
```kotlin
// Add button
if (textValue.isNotBlank()) {
    allItems.add(textValue)
    textValue = ""
}

// Search button
searchQuery = textValue
```

### 4. LazyColumn Implementation
```kotlin
items(displayedItems) { item ->
    Card(...) {
        Text(text = item, ...)
    }
}
```

## Expected Outcome
A functional app that:
- Allows adding text items to a list
- Displays items in beautiful Material cards
- Supports searching through items
- Maintains state across configuration changes

## Sample Screenshot

Include a screenshot of the final app interface showing a list with search functionality.

---

This setup provides students with:
1. A working app skeleton with missing core functionality
2. Clear TODO markers for implementation points
3. A structured README with implementation guidance
4. Bonus challenges for advanced students

The exercise focuses on key Jetpack Compose concepts:
- State management with `mutableStateOf`
- Composable function composition
- List handling with `LazyColumn`
- Material Design component usage
- Basic user input handling

