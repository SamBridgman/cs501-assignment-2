# Focus Plan Builder

## Description

Focus Plan Builder is a single-screen Android application that creates a simple study plan. The user enters a study subject and a duration between 10 and 180 minutes. 

## Running the application

1. Clone this repository.
2. Open the project folder in Android Studio.
3. Allow Android Studio to complete the Gradle sync.
4. Start a standard Android phone emulator 
5. Select the app run configuration and click run


## Screenshot

<img width="1728" height="1117" alt="Screenshot 2026-09-20 at 10 14 30 AM" src="https://github.com/user-attachments/assets/fc6da70d-8b4e-45d4-9f94-2c4e4a09ee59" />


## State and recomposition

FocusPlanRoute owns the application state, including the subject text, duration text, and the created FocusPlan. FocusPlanScreen receives those values and uses callback functions to report interaction, which keeps the displayed interface separate from state management.

The text-field values are stored as String because text fields receive editable text. A user could enter an empty or nonnumeric value, so an Int cannot represent every possible input state. 

toIntOrNull() safely returns null when conversion isn't possible, where toInt() throws an exception and could crash the application.

When either text field changes, FocusPlanRoute updates its state. Compose then refreshes the screen and recalculates canCreatePlan. This automatically enables or disables the button based on the current input.

RememberSaveable keeps the text-field values when the screen is refreshed or the device is rotated. A regular local variable would reset and lose the user’s input. Changing either input also removes the old study plan.

## Generative-AI assistance

No gen-ai was used.
