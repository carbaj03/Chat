package com.acv.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

enum class Theme {
  Default, Light, Dark
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
  onBackClick: () -> Unit,
  onThemeClick: (Theme) -> Unit,
  themeSelected: Theme
) {
  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
  var theme by remember { mutableStateOf(false) }

  if (theme)
    AlertDialog(
      onDismissRequest = { theme = false },
      modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(16.dp)).padding(16.dp),
    ) {
      Column(
        verticalArrangement = Arrangement.Center,
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          RadioButton(
            selected = themeSelected == Theme.Default,
            onClick = {
              theme = false
              onThemeClick(Theme.Default)
            },
          )
          Text(text = "Default")
        }

        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          RadioButton(
            selected = themeSelected == Theme.Light,
            onClick = {
              theme = false
              onThemeClick(Theme.Light)
            },
          )
          Text(text = "Light")
        }

        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          RadioButton(
            selected = themeSelected == Theme.Dark,
            onClick = {
              theme = false
              onThemeClick(Theme.Dark)
            },
          )
          Text(text = "Dark")
        }
      }
    }

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text("Settings") },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
          }
        }
      )
    }
  ) {
    Column(
      modifier = Modifier.padding(it).verticalScroll(rememberScrollState())
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Image(
          modifier = Modifier.size(40.dp).clip(CircleShape),
          painter = painterResource(R.drawable.avatar),
          contentDescription = null
        )

        Spacer(Modifier.width(16.dp))

        Text(
          text = "Alejandro Carbajo",
          modifier = Modifier.weight(1f)
        )
      }

      Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth()
      ) {
        Text(
          text = "Account",
          style = MaterialTheme.typography.labelMedium
        )
      }

      SettingsItem(
        title = "Email",
        subtitle = "Manage your account",
        icon = R.drawable.ic_email_24,
        onClick = {}
      )

      SettingsItem(
        title = "Subscription",
        subtitle = "Manage your app",
        icon = R.drawable.ic_edit_24,
        onClick = {}
      )

      SettingsItem(
        title = "Data Controls",
        icon = R.drawable.ic_email_24,
        onClick = {},
      )

      SettingsItem(
        title = "Custom Instructions",
        icon = R.drawable.ic_edit_24,
        onClick = {},
      )

      Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth()
      ) {
        Text(
          text = "Speech",
          style = MaterialTheme.typography.labelMedium
        )
      }

      SettingsItem(
        title = "Voice",
        subtitle = "Cove",
        icon = R.drawable.ic_edit_24,
        onClick = {},
      )

      SettingsItem(
        title = "Main Languaje",
        subtitle = "English",
        icon = R.drawable.ic_email_24,
        onClick = {},
      )

      Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth()
      ) {
        Text(
          text = "App",
          style = MaterialTheme.typography.labelMedium
        )
      }

      SettingsItem(
        title = "Color Scheme",
        subtitle = "Light",
        icon = R.drawable.ic_email_24,
        onClick = { theme = true },
      )

      Text(
        text = "App",
        style = MaterialTheme.typography.labelMedium
      )

    }
  }
}

@Composable
fun SettingsItem(
  title: String,
  icon: Int,
  onClick: () -> Unit,
  subtitle: String? = null,
) {
  Row(
    modifier = Modifier.clickable { onClick() }.padding(16.dp).fillMaxWidth(),
  ) {
    Icon(
      painter = painterResource(icon),
      contentDescription = null
    )

    Spacer(Modifier.width(16.dp))

    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium
      )

      Spacer(Modifier.height(4.dp))

      subtitle?.let {
        Text(
          text = it,
          style = MaterialTheme.typography.bodyMedium,
        )
      }
    }
  }
}
