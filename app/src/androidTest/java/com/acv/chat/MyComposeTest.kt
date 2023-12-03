package com.acv.chat

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.core.net.toUri
import arrow.core.raise.either
import com.acv.chat.domain.rememberPhotoLauncher
import com.acv.chat.util.rememberFileProvider
import org.junit.Rule
import org.junit.Test

class MyComposeTest {

  @get:Rule
  val composeTestRule = createComposeRule()
  // use createAndroidComposeRule<YourActivity>() if you need access to
  // an activity

  @Test
  fun myTest() {
    composeTestRule.setContent {

      val p = rememberPhotoLauncher(rememberFileProvider())

      LaunchedEffect(Unit) {
        either {
          val p = p.takePhoto()
          println(p.file.name)
        }
      }
    }

//        composeTestRule.onNodeWithText("Welcome").assertIsDisplayed()
  }
}