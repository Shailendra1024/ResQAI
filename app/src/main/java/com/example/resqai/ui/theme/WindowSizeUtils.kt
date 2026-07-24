package com.example.resqai.ui.theme

import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Caps content width on large screens (tablets/landscape) so cards and forms
 * don't stretch uncomfortably wide, while staying full-width on phones.
 * Apply to the outermost Column/Box of scrollable screen content.
 */
fun Modifier.responsiveContentWidth(): Modifier = this.widthIn(max = 600.dp)