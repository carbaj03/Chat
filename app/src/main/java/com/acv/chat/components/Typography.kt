package com.acv.chat.components;

import androidx.compose.runtime.Composable
import arrow.optics.optics

@optics data class Typography(
  val displayLarge: TextStyle = TextStyle.Display,
  val displayMedium: TextStyle = TextStyle.Display,
  val displaySmall: TextStyle = TextStyle.Display,
  val headlineLarge: TextStyle = TextStyle.Body,
  val headlineMedium: TextStyle = TextStyle.Body,
  val headlineSmall: TextStyle = TextStyle.Body,
  val titleLarge: TextStyle = TextStyle.TitleLarge,
  val titleMedium: TextStyle = TextStyle.Title,
  val titleSmall: TextStyle = TextStyle.TitleSmall,
  val bodyLarge: TextStyle = TextStyle.BodyLarge,
  val bodyMedium: TextStyle = TextStyle.Body,
  val bodySmall: TextStyle = TextStyle.BodySmall,
  val labelLarge: TextStyle = TextStyle.Label,
  val labelMedium: TextStyle = TextStyle.Label,
  val labelSmall: TextStyle = TextStyle.Label,
) {
  companion object
}

@Composable
operator fun Typography.invoke(): androidx.compose.material3.Typography = androidx.compose.material3.Typography(
  displayLarge = displayLarge(),
  displayMedium = displayMedium(),
  displaySmall = displaySmall(),
  headlineLarge = headlineLarge(),
  headlineMedium = headlineMedium(),
  headlineSmall = headlineSmall(),
  titleLarge = titleLarge(),
  titleMedium = titleMedium(),
  titleSmall = titleSmall(),
  bodyLarge = bodyLarge(),
  bodyMedium = bodyMedium(),
  bodySmall = bodySmall(),
  labelLarge = labelLarge(),
  labelMedium = labelMedium(),
  labelSmall = labelSmall(),
)