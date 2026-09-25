package com.example.domain.model

enum class AiCompanionState(
    val label: String,
    val description: String
) {
    IDLE(
        label = "Idle",
        description = "Your companion is ready to help you study."
    ),
    LISTENING(
        label = "Listening",
        description = "Listening to your question or voice command..."
    ),
    THINKING(
        label = "Thinking",
        description = "Synthesizing your academic plan..."
    ),
    TEACHING(
        label = "Teaching",
        description = "Breaking down core concepts step by step."
    ),
    SPEAKING(
        label = "Speaking",
        description = "Reciting explanations and active recall questions."
    ),
    SUCCESS(
        label = "Success",
        description = "Target mastered! Knowledge updated."
    ),
    WARNING(
        label = "Warning",
        description = "Deadline approaching! Let's prioritize."
    ),
    FOCUS(
        label = "Focus",
        description = "Deep study mode active. Distractions shielded."
    ),
    CELEBRATION(
        label = "Celebration",
        description = "Streak milestone achieved! Incredible work!"
    )
}
