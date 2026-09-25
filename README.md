# Study Companion 📚⚡

**Your AI-powered academic companion for smarter, more organized, and more effective studying.**

Study Companion is a native Android application designed to become a complete academic operating system — combining intelligent study planning, AI tutoring, learning roadmaps, syllabus management, notes, revision, practice, exams, projects, focus tools, analytics, and productivity into one unified experience.

The project is built with a **Kotlin + Jetpack Compose + Firebase** architecture and is being developed progressively through a structured feature roadmap.

## ✨ Vision

Study Companion aims to transform the way students manage their academic life by connecting:

* 🧠 AI-powered learning
* 📚 Subjects & syllabus
* 🗺️ Personalized learning roadmaps
* 📅 Intelligent study planning
* 🤖 AI tutoring
* 📝 Notes & knowledge management
* 🔄 Spaced repetition & revision
* 🎯 Practice & weakness tracking
* 🧪 AI-generated exams
* 📊 Academic analytics
* 🚀 Project management
* ⏱️ Focus & study sessions
* 🔔 Smart notifications
* ☁️ Google ecosystem integration
* 🎮 Gamification
* 🎨 Personalized themes & experiences

## 🛠️ Technology

* **Kotlin**
* **Jetpack Compose**
* **Material 3**
* **Firebase Authentication**
* **Google Sign-In**
* **Cloud Firestore**
* **Firebase Storage**
* **Gemini API**
* **Android SpeechRecognizer**
* **Android Text-to-Speech**
* **ML Kit**
* **WorkManager**

## 🎨 Design

The visual identity combines:

**Ink Anime × Neon × Lightning × Cinematic UI**

while keeping the core principle:

> **Productivity first. Decoration second.**

The interface is designed to feel energetic and futuristic without becoming distracting during long study sessions.

## 🏗️ Development Approach

Study Companion is being developed incrementally through a structured multi-phase roadmap.

Each phase establishes a specific layer of the system while preserving compatibility with previously implemented functionality.

### Current Development

**Phase 0 — Foundation**
Project architecture, Firebase, authentication, cloud data architecture, navigation, themes, and global UI states.

**Phase 1 — Visual System**
Ink Anime UI, neon components, animation engine, transitions, particles, AI orb, themes, animation controls, and motion settings.

More features will be implemented progressively.

## ☁️ Cloud-First Architecture

Study Companion uses a **cloud-first architecture**.

Firebase acts as the primary source of truth for user data.

The application intentionally avoids local persistent databases such as:

* Room
* SQLite
* SharedPreferences
* DataStore

Temporary in-memory UI state is permitted, while persistent user data belongs in the cloud.

## 🚧 Project Status

This project is actively being developed.

Features are introduced incrementally rather than attempting to build the entire system at once.

Expect architectural changes, UI evolution, experimentation, and new capabilities as development progresses.

## 🎯 Long-Term Goal

The long-term goal is to create a single academic workspace where a student can go from:

**“What should I study?”**

to

**“Teach me this.”**

to

**“Help me practice it.”**

to

**“Track my weaknesses.”**

to

**“Prepare me for the exam.”**

to

**“Help me improve.”**

—all within one intelligent system.

---

**Study Companion — Learn smarter. Plan better. Go further.** 🚀


## Run Locally

**Prerequisites:**  [Android Studio](https://developer.android.com/studio)


1. Open Android Studio
2. Select **Open** and choose the directory containing this project
3. Allow Android Studio to fix any incompatibilities as it imports the project.
4. Create a file named `.env` in the project directory and set `GEMINI_API_KEY` in that file to your Gemini API key (see `.env.example` for an example)
5. Remove this line from the app's `build.gradle.kts` file: `signingConfig = signingConfigs.getByName("debugConfig")`
6. Run the app on an emulator or physical device
7. If you have already published your app in AI Studio, please [request upload key reset](https://support.google.com/googleplay/android-developer/answer/9842756#zippy=%2Crequest-an-upload-key-reset) in Google Play Console.