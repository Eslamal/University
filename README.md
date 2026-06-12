# 🎓 University App - Your Smart Academic Companion

[![Available on Uptodown](https://img.shields.io/badge/Available_on-Uptodown-blue?style=for-the-badge&logo=uptodown)](https://en.uptodown.com/) <!-- ضيف لينك التطبيق بتاعك هنا بدل اللينك العام -->
[![Android Version](https://img.shields.io/badge/Android-5.0+-brightgreen.svg?style=flat-square)](https://developer.android.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**University App** is a comprehensive, native Android application meticulously designed to assist students in their daily academic life. From exploring global universities to managing weekly lecture schedules and calculating GPA, this app serves as an all-in-one student hub. It even features an integrated **Artificial Intelligence (Gemini)** assistant to act as a 24/7 personal academic advisor.

---

## 🎯 The Problem It Solves

University students often struggle with disorganization, relying on multiple fragmented apps to manage their academic life. 
* They use a browser to find scholarships and universities.
* A notepad or gallery image to check their lecture schedule.
* A separate calculator or website to compute their GPA.
* Forums or search engines to seek academic advice.

**The Solution:** This application aggregates all these essential tools into a single, cohesive, and user-friendly platform, saving time and boosting productivity.

---

## 📱 App Screenshots

Here is a glimpse of the app's clean and modern UI, fully supporting both Light and Dark modes:

| | | |
|:---:|:---:|:---:|
| ![Screen 1](screens/1.jpg) | ![Screen 2](screens/2.jpg) | ![Screen 3](screens/3.jpg) |
| ![Screen 4](screens/4.jpg) | ![Screen 5](screens/5.jpg) | ![Screen 6](screens/6.jpg) |
| ![Screen 7](screens/7.jpg) | ![Screen 8](screens/8.jpg) | ![Screen 9](screens/9.jpg) |
| ![Screen 10](screens/10.jpg) | ![Screen 11](screens/11.jpg) | |

---

## 🛠 Tech Stack & Architecture

This project is built using **Native Android (Java)** following the **MVVM (Model-View-ViewModel)** architecture pattern to ensure separation of concerns, scalability, and testability.

| Technology | Where it is used? | Why it was used? |
| :--- | :--- | :--- |
| **Java** | Entire Application | The core language used for robust Android development. |
| **MVVM Architecture** | Project Structure | To strictly separate UI components from business logic, making the codebase highly maintainable. |
| **Room Database** | Schedule & Favorites | To cache data locally. It provides an abstraction layer over SQLite to allow fluent database access and offline functionality. |
| **Retrofit 2** | University Search | To handle REST API requests efficiently. Chosen for its speed, type safety, and seamless JSON parsing. |
| **OkHttp 3** | AI Chat Module | Used to manually handle HTTP requests, timeouts, and JSON bodies for communicating directly with the Gemini API. |
| **Google Gemini API** | AI Advisor | To provide intelligent, context-aware, and conversational responses to student inquiries. |
| **LiveData & ViewModel** | UI Updates | To observe data changes dynamically and update the UI without memory leaks (Lifecycle-aware). |
| **Lottie Animations** | Loading & Empty States | To provide a modern, highly engaging user experience during data fetching or when lists are empty. |
| **Material Design** | UI/UX | Extensive use of `CardView`, `RecyclerView`, and custom drawables for a modern, adaptive interface. |

---

## 🚧 Challenges Faced & How We Solved Them

Building a feature-rich application comes with its hurdles. Here are the major challenges overcome during development:

### 1. 🌓 Seamless Dark/Light Mode Integration
* **Challenge:** Hardcoded colors (`#FFFFFF`, `#000000`) caused UI elements to become invisible or visually jarring when switching between system themes.
* **Solution:** Completely overhauled the XML layouts to use dynamic color references (`@color/main_background`, `@color/text_primary`, `@color/accent_color`). This ensured a 100% adaptive UI that looks native and elegant in both Dark and Light modes.

### 2. 🌍 Multi-Language Support (RTL & LTR)
* **Challenge:** Supporting both Arabic (Right-to-Left) and English (Left-to-Right) without breaking the layout alignment.
* **Solution:** Replaced all `layout_marginLeft` and `layout_marginRight` attributes with `marginStart` and `marginEnd`. Extracted all hardcoded text into localized `strings.xml` files, ensuring a flawless switch between languages.

### 3. 🔒 API Key Security for Gemini
* **Challenge:** Pushing the Google Gemini API key directly to GitHub exposes it to security risks and automatic revocation by Google.
* **Solution:** Moved the API key to the `local.properties` file (which is git-ignored) and used the `Secrets Gradle Plugin` / `BuildConfig` to securely inject the key into the app at compile time.

### 4. 📴 Offline Data Persistence
* **Challenge:** Students need access to their lecture schedules even without internet access.
* **Solution:** Implemented **Room Database** to save lectures and favorite universities locally on the device. This guarantees instant access to crucial data regardless of network connectivity.

---

## 💡 Core Features (Modules)

### 1. 🔍 Global University Search
* Live search functionality by university name or country via REST APIs.
* Direct external links to official university websites.
* Ability to save universities to a local "Favorites" list.

### 2. 🤖 AI Academic Advisor (Powered by Gemini)
* An interactive chat interface where students can seek advice on studying, scholarships, and career paths.
* Modern chat bubble UI mimicking popular messaging apps.

### 3. 📅 Offline Lecture Schedule
* A complete CRUD module to manage weekly classes.
* Add course name, time, professor, room number, and day.

### 4. 🎓 Scholarships & GPA Calculator
* **Scholarships Hub:** Browse fully funded global scholarships (e.g., Chevening) with one-click access to more details.
* **Smart GPA Calculator:** Dynamically add courses, credits, and grades to accurately calculate the semester GPA.

---

## 👨‍💻 Developed By

**Eslam Ali**
* 📱 Android Developer
* 📧 Email: eslameng776@gmail.com
* 🔗 GitHub: [Eslamal](https://github.com/Eslamal)

---
*If you find this project useful, don't forget to give it a ⭐ on GitHub!*
