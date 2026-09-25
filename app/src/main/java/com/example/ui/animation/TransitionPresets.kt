package com.example.ui.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry

enum class TransitionPreset {
    STANDARD,
    CINEMATIC,
    INK,
    LIGHTNING,
    CALM,
    EXAM,
    REDUCE_MOTION
}

object StudyTransitions {

    fun getEnterTransition(
        preset: TransitionPreset,
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        config: AnimationConfig
    ): EnterTransition {
        if (config.reduceMotion || preset == TransitionPreset.REDUCE_MOTION) {
            return fadeIn(animationSpec = tween(durationMillis = 150))
        }

        val duration = config.transitionDurationMs

        return when (preset) {
            TransitionPreset.STANDARD -> {
                fadeIn(tween(duration, easing = LinearOutSlowInEasing)) +
                        scope.slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(duration, easing = FastOutSlowInEasing)
                        )
            }
            TransitionPreset.CINEMATIC -> {
                fadeIn(tween((duration * 1.2f).toInt())) +
                        scaleIn(
                            initialScale = 0.92f,
                            animationSpec = tween((duration * 1.2f).toInt(), easing = FastOutSlowInEasing)
                        )
            }
            TransitionPreset.INK -> {
                fadeIn(tween(duration, easing = LinearOutSlowInEasing)) +
                        slideInVertically(
                            initialOffsetY = { fullHeight -> (fullHeight * 0.08f).toInt() },
                            animationSpec = tween(duration, easing = FastOutSlowInEasing)
                        ) +
                        scaleIn(initialScale = 0.96f, animationSpec = tween(duration))
            }
            TransitionPreset.LIGHTNING -> {
                fadeIn(tween((duration * 0.8f).toInt())) +
                        scope.slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween((duration * 0.8f).toInt(), easing = FastOutSlowInEasing)
                        )
            }
            TransitionPreset.CALM -> {
                fadeIn(tween((duration * 1.3f).toInt(), easing = LinearOutSlowInEasing))
            }
            TransitionPreset.EXAM -> {
                fadeIn(tween((duration * 0.7f).toInt()))
            }
            TransitionPreset.REDUCE_MOTION -> {
                fadeIn(tween(150))
            }
        }
    }

    fun getExitTransition(
        preset: TransitionPreset,
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        config: AnimationConfig
    ): ExitTransition {
        if (config.reduceMotion || preset == TransitionPreset.REDUCE_MOTION) {
            return fadeOut(animationSpec = tween(durationMillis = 120))
        }

        val duration = config.transitionDurationMs

        return when (preset) {
            TransitionPreset.STANDARD -> {
                fadeOut(tween(duration, easing = FastOutSlowInEasing)) +
                        scope.slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(duration, easing = FastOutSlowInEasing)
                        )
            }
            TransitionPreset.CINEMATIC -> {
                fadeOut(tween((duration * 0.9f).toInt())) +
                        scaleOut(targetScale = 1.05f, animationSpec = tween((duration * 0.9f).toInt()))
            }
            TransitionPreset.INK -> {
                fadeOut(tween(duration)) +
                        slideOutVertically(
                            targetOffsetY = { fullHeight -> -(fullHeight * 0.06f).toInt() },
                            animationSpec = tween(duration)
                        )
            }
            TransitionPreset.LIGHTNING -> {
                fadeOut(tween((duration * 0.7f).toInt())) +
                        scope.slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween((duration * 0.7f).toInt())
                        )
            }
            TransitionPreset.CALM -> {
                fadeOut(tween((duration * 1.1f).toInt()))
            }
            TransitionPreset.EXAM -> {
                fadeOut(tween((duration * 0.6f).toInt()))
            }
            TransitionPreset.REDUCE_MOTION -> {
                fadeOut(tween(120))
            }
        }
    }
}
