# Notes App 📝

A modern Android note-taking application built with Kotlin, following MVVM architecture and Material Design principles.

## Features

- **User Authentication**
  - Sign up with email and password
  - Sign in with validation
  - Session management with SharedPreferences

- **Notes Management**
  - Create new notes with title and content
  - View all notes in a colorful card layout
  - Edit existing notes
  - Delete notes
  - Notes list with color-coded cards (5 different colors)

- **User Interface**
  - Dark theme design
  - Material Design components
  - Smooth navigation between screens
  - Empty state illustrations
  - Loading indicators

  ## Tech Stack

### Architecture & Design Patterns
- **MVVM (Model-View-ViewModel)** - Separation of concerns
- **Repository Pattern** - Single source of truth for data
- **Clean Architecture** - Domain models separate from database entities

### Android Components
- **Kotlin** - Primary programming language
- **Coroutines & Flow** - Asynchronous programming and reactive streams
- **View Binding** - Type-safe view access
- **Navigation Component** - Fragment navigation with type-safe arguments
- **RecyclerView with ListAdapter** - Efficient list display with DiffUtil

### Data Persistence
- **Room Database** - Local SQLite database with compile-time verification
- **SharedPreferences** - User session management

### Dependency Injection
- **Koin** - Lightweight dependency injection framework

### UI Components
- **Material Design 3** - Modern UI components
- **ConstraintLayout** - Flexible layouts
- **FloatingActionButton** - Quick note creation
- **Custom drawables** - Rounded backgrounds and icons

## Setup & Installation 

1. **Clone the repository**
```bash
   git clone https://github.com/Pasha-007/NotesApp.git
   cd NotesApp
```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned repository

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for dependencies to download

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click "Run" or press Shift + F10

   ## Future Enhancements

- [ ] Note search functionality
- [ ] Note categories/tags
- [ ] Rich text formatting
- [ ] Image attachments
- [ ] Cloud sync
- [ ] Note sharing
- [ ] Dark/Light theme toggle
- [ ] Biometric authentication
- [ ] Note archiving
- [ ] Reminders and notifications

## Author

**Muntahaa AKA Pasha** 

## License

This project is for educational purposes and personal portfolio use.
