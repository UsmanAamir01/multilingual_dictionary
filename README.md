# 📖 Multilingual Dictionary

A premium, modern JavaFX desktop application for Arabic, Urdu, and Persian translations. Built with a clean architecture and a beautiful, responsive user interface.

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

## ✨ Features

### 🔍 Search & Discovery
- **Smart Search**: Search words across Arabic, Urdu, and Persian languages
- **Intelligent Suggestions**: Get relevant word suggestions as you type
- **Search History**: Track and revisit your recent searches

### 📚 Word Management
- **Add Words**: Easily add new words with translations in all three languages
- **Edit & Update**: Modify existing word entries
- **Remove Words**: Delete entries you no longer need
- **Word Normalization**: Automatically normalize and standardize word entries

### ⭐ Personal Collections
- **Favorites**: Save frequently used words for quick access
- **Organized Views**: Browse all words, favorites, or search history in dedicated views

### 🎨 Premium User Interface
- **Modern Design**: Clean, intuitive interface with smooth animations
- **Dark/Light Theme**: Toggle between themes based on your preference
- **Responsive Layout**: Sidebar navigation with a spacious content area
- **Consistent Styling**: Uniform button sizing and color scheme throughout

### 📁 Data Management
- **Import/Export**: Import word lists from files or export your dictionary
- **Persistent Storage**: Your data is safely stored between sessions

## 🛠️ Tech Stack

| Layer | Technology |
|-------|------------|
| **Frontend** | JavaFX 21 with FXML |
| **Styling** | Custom CSS with CSS Variables |
| **Architecture** | Clean 3-Tier (Presentation → Business Logic → Data Access) |
| **Data** | Text file-based storage |
| **Build** | Eclipse/IntelliJ compatible |

## 📂 Project Structure

```
multilingual_dictionary/
├── src/
│   ├── bl/                     # Business Logic Layer
│   │   ├── BLFacade.java       # Facade pattern implementation
│   │   ├── WordBO.java         # Word business operations
│   │   ├── UserBO.java         # User business operations
│   │   └── IWordBO.java        # Word interface
│   │
│   ├── dal/                    # Data Access Layer
│   │   └── ...                 # Database/file access classes
│   │
│   ├── dto/                    # Data Transfer Objects
│   │   └── Word.java           # Word entity
│   │
│   ├── fx/                     # JavaFX UI Layer
│   │   ├── Main.java           # Application entry point
│   │   ├── controller/         # FXML Controllers
│   │   │   ├── MainController.java
│   │   │   ├── AddWordController.java
│   │   │   ├── AllWordsController.java
│   │   │   ├── FavoritesController.java
│   │   │   ├── HistoryController.java
│   │   │   ├── SearchResultController.java
│   │   │   └── SettingsController.java
│   │   ├── fxml/               # FXML View Files
│   │   │   ├── MainView.fxml
│   │   │   ├── AddWordDialog.fxml
│   │   │   ├── AllWordsView.fxml
│   │   │   ├── FavoritesView.fxml
│   │   │   ├── HistoryView.fxml
│   │   │   ├── SearchResultView.fxml
│   │   │   └── SettingsView.fxml
│   │   ├── css/                # Stylesheets
│   │   │   ├── components.css  # Reusable component styles
│   │   │   ├── theme-light.css # Light theme
│   │   │   └── theme-dark.css  # Dark theme
│   │   └── util/
│   │       └── ThemeManager.java
│   │
│   ├── pl/                     # Presentation Layer (Legacy)
│   └── testing/                # Unit Tests
│
├── lib/                        # JavaFX Libraries
├── javafx-sdk-21.0.2/          # JavaFX SDK
├── config/                     # Configuration files
├── images/                     # Application assets
└── words.txt                   # Word database
```

## 🚀 Getting Started

### Prerequisites
- **Java 17+** (JDK)
- **JavaFX 21** (included in `javafx-sdk-21.0.2/`)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/UsmanAamir01/multilingual_dictionary.git
   cd multilingual_dictionary
   ```

2. **Open in your IDE**
   - Import as an existing Java project in Eclipse or IntelliJ
   - Ensure JavaFX libraries are added to the module path

3. **Configure VM Arguments**
   Add the following VM arguments to run configuration:
   ```
   --module-path "path/to/javafx-sdk-21.0.2/lib" --add-modules javafx.controls,javafx.fxml
   ```

4. **Run the Application**
   - Execute `src/fx/Main.java`

### Quick Start with Eclipse
1. Right-click project → Properties → Java Build Path
2. Add `javafx-sdk-21.0.2/lib/*.jar` to the classpath
3. Configure Run Configuration with VM arguments above
4. Run `Main.java`

## 🎨 UI Design System

### Color Palette
| Color | Light Mode | Dark Mode | Usage |
|-------|------------|-----------|-------|
| Primary | `#6366F1` | `#818CF8` | Buttons, links, accents |
| Success | `#10B981` | `#34D399` | Success actions |
| Danger | `#EF4444` | `#F87171` | Destructive actions |
| Background | `#F8FAFC` | `#0F172A` | Main background |
| Surface | `#FFFFFF` | `#1E293B` | Cards, dialogs |

### Button Variants
- **Primary**: Main call-to-action buttons
- **Success**: Positive actions (Save, Add)
- **Danger**: Destructive actions (Delete, Remove)
- **Outline**: Secondary actions
- **Text**: Tertiary/ghost buttons
- **Header**: Buttons on gradient backgrounds

## 📝 Usage

### Adding a Word
1. Click "➕ Add Word" in the sidebar
2. Enter the Arabic word
3. Add Urdu and Persian translations
4. Click "Add Word" to save

### Searching
1. Select the language from the dropdown
2. Type your search term
3. Press Enter or click "Search"

### Managing Favorites
1. View any word's details
2. Click the star icon to add/remove from favorites
3. Access all favorites via "⭐ Favourites" in the sidebar

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Usman Aamir**
- GitHub: [@UsmanAamir01](https://github.com/UsmanAamir01)

---

<p align="center">
  Made with ❤️ using JavaFX
</p>
