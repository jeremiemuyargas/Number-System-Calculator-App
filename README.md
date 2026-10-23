# Number System Calculator

A fully offline Java Swing desktop app for converting and calculating
between **Binary, Octal, Decimal, and Hexadecimal** number systems.

## Features

**Convert tab**
- Type a number using the on-screen digit pad (0–9, A–F).
- Digits that aren't valid for the selected "From" system are automatically
  greyed out (e.g. only 0/1 are clickable when "From" is Binary).
- Pick any "From" and "To" number system and press **Convert**.
- **C** clears the input, **Del** removes the last character.

**Arithmetic tab**
- Choose a number system, type two numbers in that system, and press
  **+**, **−**, **×**, or **÷**.
- Division shows a remainder when the result isn't exact
  (e.g. `101 ÷ 11 = 1 remainder 10` in binary).
- Handles arbitrarily large numbers (uses `BigInteger` internally).

## Requirements

- **Java Development Kit (JDK) 11 or newer** installed on your machine
  (this app uses only the standard library — no internet connection needed
  to run it).
- **Visual Studio Code** with the **Extension Pack for Java** (by Microsoft)
  installed from the VS Code Marketplace — this gives VS Code the ability
  to build and run Java projects.

## How to open and run in VS Code

1. Unzip this project folder anywhere on your computer.
2. Open VS Code → **File → Open Folder…** → select the `NumberSystemCalculator` folder.
3. Wait a few seconds for the Java extension to index the project (you'll
   see "Java: Ready" in the bottom status bar).
4. Open `src/Main.java`.
5. Click the **Run** ▷ button that appears above the `main` method
   (or press `F5`, or use the included `.vscode/launch.json` config).
6. The calculator window should appear.

### Running from a terminal instead (optional)

```bash
cd NumberSystemCalculator
javac -d bin src/*.java
java -cp bin Main
```

## Project structure

```
NumberSystemCalculator/
├── .vscode/
│   └── launch.json          # Run configuration for VS Code
├── src/
│   ├── Main.java             # Entry point
│   ├── CalculatorGUI.java    # Main window with tabs
│   ├── ConvertPanel.java     # "Convert" tab UI
│   ├── ArithmeticPanel.java  # "Arithmetic" tab UI
│   ├── Converter.java        # Conversion + arithmetic logic
│   ├── NumberSystem.java     # Enum: BINARY, OCTAL, DECIMAL, HEXADECIMAL
│   └── InvalidDigitException.java  # Custom exception for bad input
└── README.md
```

## Notes

- Everything runs 100% locally — no internet access is required once the
  JDK and VS Code extension are installed.
- Input validation catches invalid digits for the chosen number system
  (e.g. entering "9" while "Binary" is selected) and division by zero,
  showing a friendly error message instead of crashing.
