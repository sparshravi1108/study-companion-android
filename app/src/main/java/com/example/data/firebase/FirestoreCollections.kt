package com.example.data.firebase

object FirestoreCollections {
    const val USERS = "users"
    const val SUBJECTS = "subjects"
    const val TOPICS = "topics"
    const val ROADMAPS = "roadmaps"
    const val TASKS = "tasks"
    const val DEADLINES = "deadlines"
    const val REVISIONS = "revisions"
    const val SETTINGS = "settings"
    const val PROJECTS = "projects"
    const val STUDY_SESSIONS = "studySessions"

    // Subcollection path helpers
    fun userPath(uid: String) = "$USERS/$uid"
    fun subjectsPath(uid: String) = "$USERS/$uid/$SUBJECTS"
    fun topicsPath(uid: String) = "$USERS/$uid/$TOPICS"
    fun roadmapsPath(uid: String) = "$USERS/$uid/$ROADMAPS"
    fun tasksPath(uid: String) = "$USERS/$uid/$TASKS"
    fun deadlinesPath(uid: String) = "$USERS/$uid/$DEADLINES"
    fun revisionsPath(uid: String) = "$USERS/$uid/$REVISIONS"
    fun dashboardSettingsPath(uid: String) = "$USERS/$uid/$SETTINGS/dashboard"
    fun projectsPath(uid: String) = "$USERS/$uid/$PROJECTS"
    fun studySessionsPath(uid: String) = "$USERS/$uid/$STUDY_SESSIONS"
}

object FirebaseStoragePaths {
    fun userRoot(uid: String) = "users/$uid"
    fun documents(uid: String) = "users/$uid/documents"
    fun images(uid: String) = "users/$uid/images"
    fun notes(uid: String) = "users/$uid/notes"
    fun audio(uid: String) = "users/$uid/audio"
    fun projectFiles(uid: String) = "users/$uid/project-files"
}

object FirebaseSecurityRulesDoc {
    const val FIRESTORE_RULES = """
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Cloud-only single user academic workspace rules
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
      
      match /{allSubcollections=**} {
        allow read, write: if request.auth != null && request.auth.uid == userId;
      }
    }
  }
}
"""

    const val STORAGE_RULES = """
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /users/{userId}/{allPaths=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
"""
}
