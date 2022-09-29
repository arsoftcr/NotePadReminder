package com.mobile.notepadreminder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

object NoteTheme {


    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current


    @Composable
    fun NotePadReminderTheme(
        typography: AppTypography = NoteTheme.typography,
        content: @Composable () -> Unit
    ) {
        CompositionLocalProvider(
            LocalTypography provides typography,
        ) {
            content()
        }
    }
}