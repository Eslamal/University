# University Finder App 🎓

A simple and clean Android application built with Java that allows users to search for universities worldwide, save their favorite ones, and view their websites directly within the app. This project follows the MVVM architecture pattern and utilizes modern Android development components.

---

## ✨ Features

* **Search by Name:** Find universities by typing their name.
* **Search by Country:** Get a list of all universities in a specific country.
* **Combined Search:** Filter universities by name within a specific country.
* **Favorites System:** Add or remove any university from your personal favorites list for quick access.
* **Local Database:** Favorites are saved locally on your device using Room Persistence, so they are available even after you close the app.
* **In-App Browser:** University websites open inside a WebView, providing a seamless user experience without leaving the app.
* **Clean UI:** A user-friendly and intuitive interface built with modern Material Design components.

---

## 🛠️ Tech Stack & Architecture

This project is built using the **MVVM (Model-View-ViewModel)** architecture pattern, which promotes a clean separation of concerns and a scalable codebase.

* **Language:** **Java**
* **Architecture:** **MVVM**
* **Core Components:**
    * **ViewModel:** Manages UI-related data in a lifecycle-conscious way.
    * **LiveData:** A lifecycle-aware observable data holder for building data-driven UIs.
    * **Repository Pattern:** Mediates between different data sources (API and local database).
* **Libraries & Tools:**
    * **Retrofit2:** A type-safe HTTP client for making API calls to the [Universities API](http://universities.hipolabs.com/).
    * **Room Persistence Library:** Provides an abstraction layer over SQLite to allow for more robust database access.
    * **RecyclerView:** For displaying the list of universities efficiently.
    * **Gson:** For parsing JSON data from the API.
    * **WebView:** For displaying web content within the app.
    * **CardView & Material Components:** For creating a modern and responsive user interface.
