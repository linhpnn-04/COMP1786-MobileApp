# M-HIKE APPLICATION - COURSEWORK SUBMISSION

**Student Name:** Nguyen Ngoc Phuong Linh
* **Student ID:** GCS220260
* **Course:** Mobile Application Development

---

## 1. PROJECT OVERVIEW
This submission contains two versions of the M-Hike application as required:
1.  **Android Native:** Built with Java in Android Studio.
2.  **Cross-Platform:** Built with C#/.NET MAUI in Visual Studio 2026.

Both applications allow hikers to store, view, search, and manage hiking trips and observations with offline SQLite database storage.

---

## 2. PREREQUISITES
To run these projects, the following environments are required:
* **For Android Native:** Android Studio.
* **For MAUI:** Visual Studio 2022 with ".NET Multi-platform App UI development" workload installed.
* **Emulator:** Pixel 5 (API 33 or API 31 recommended) for MAUI and Medium Phone for Android Studio.

---

## 3. HOW TO RUN THE APPS

### A. Android Native Version (Folder: `M-HikeApp`)
1.  Open **Android Studio**.
2.  Select **File > Open** and navigate to the `M-HikeApp` folder.
3.  Wait for Gradle sync to complete.
4.  Select an Emulator (e.g., Pixel 5 API 33).
5.  Before click the Run button please open Edit Configuration and choose the MHikeApp- the main page
5.  Click the **Run (Play)** button.

### B. Xamarin/MAUI Version (Folder: `M-HikeApp_MAUI`)
1.  Open **Visual Studio 2026**.
2.  Double-click the solution file (`.sln`).
3.  In the toolbar, ensure the target framework is set to **Android**.
4.  Select an Emulator.
5.  Click the **Run (Play)** button.

> **Note on Database:** Both apps use SQLite for local storage. The database is generated automatically upon the first launch.

---

## 4. IMPLEMENTED FEATURES
I have successfully implemented all mandatory and advanced requirements:

**Part A: Hike Management**
* [x] Add new hike with Validation (Required fields check).
* [x] **Creativity:** Added custom fields (Type, Scenic, etc.).
* [x] Edit/Delete hike details (with "Are you sure?" confirmation dialogs).
* [x] Reset Database feature.

**Part B: Observations**
* [x] Add observations to a specific hike (One-to-Many relationship).
* [x] Auto-populate time/date for observations.

**Part C: Advanced Features**
* [x] **Search:** Partial string matching (e.g., typing "Sno" finds "Snowdon").
* [x] **Image Attachment:** Integrated with native Photo Picker to attach images to observations (handles Permission & URI persistence).
* [x] **Cross-Platform:** Replicated full functionality in .NET MAUI using XAML/C#.

---

## 5. REFERENCES & ACKNOWLEDGEMENTS
The code logic is original and implemented based on the module's lecture notes and official documentation.

**Documentation Consulted:**
1.  *Android Developers Documentation* (https://developer.android.com) - For ConstraintLayout and Intents.
2.  *Microsoft .NET MAUI Documentation* (https://learn.microsoft.com) - For XAML Layouts and SQLite-net-pcl.
3.  *SQLite Official Documentation* - For database schema and Foreign Keys.

**Assets:**
* Icons used in the app are standard Android Vector Assets.

---

## 6. TROUBLESHOOTING
* **Emulator Issue:** If the emulator freezes or shows a black screen, please try Delete this emulator and download a new one.
* **Image Permissions:** On Android 13+ (API 33), the app uses the Photo Picker which does not require READ_EXTERNAL_STORAGE permission, adhering to modern security standards.


## 7. IMPORTANT: SETTING UP IMAGES FOR TESTING
**Note:** A freshly installed Android Emulator usually has an empty Gallery. To test the **"Image Attachment"** feature (M-Hike App) or the **"Information App"** (LogBook), please populate the emulator with images using one of the following methods:

### Method 1: Use the Virtual Camera (Recommended/Fastest)
1.  Open the **Camera** app inside the emulator.
2.  Take a few sample photos (the camera simulates a 3D room).
3.  These photos will immediately appear in the Gallery/Photos app, ready for selection.

### Method 2: Drag and Drop
1.  Prepare some `.jpg` or `.png` files on your computer.
2.  Drag and drop them directly onto the emulator screen.
3.  Check the **Files** app (Download folder) or **Google Photos** in the emulator to confirm they are saved.

### Method 3: Download via Emulator Browser
1.  Open **Chrome** inside the emulator.
2.  Search for images (e.g., "nature wallpapers").
3.  Long-press an image and select **Download image**.
