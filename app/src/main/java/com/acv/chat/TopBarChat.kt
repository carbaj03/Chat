package com.acv.chat

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.acv.chat.Model.GPT35
import com.acv.chat.Model.GPT4
import com.acv.chat.components.Surface
import com.acv.chat.components.TextSpanned
import com.acv.chat.components.TextSpannedStyle
import com.acv.chat.components.Theme
import com.acv.chat.ui.theme.invoke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarChat(
  title: String,
  onMenuClick: () -> Unit,
  onViewDetailsClick: () -> Unit,
  onNewChatClick: () -> Unit,
  model: Model,
  onChangeModelClick: (Model) -> Unit,
  isNew: Boolean
) {
  TopAppBar(
    title = {
      TextSpanned(
        TextSpannedStyle(
          text = "4",
          color = Color.Green
        ),
        color = MaterialTheme.colorScheme.onSurface,
        text = title
      )
    },
    navigationIcon = {
      IconButton(onClick = onMenuClick) {
        Icon(
          imageVector = Icons.Default.Menu,
          contentDescription = null
        )
      }
    },
    actions = {
      Actions(
        onChangeModelClick = onChangeModelClick,
        onViewDetailsClick = onViewDetailsClick,
        onNewChatClick = onNewChatClick,
        isNew = isNew,
        model = model
      )
    },
  )
}

@Composable
fun RowScope.Actions(
  onChangeModelClick: (Model) -> Unit,
  onViewDetailsClick: () -> Unit,
  onNewChatClick: () -> Unit,
  isNew: Boolean,
  model: Model
) {
  var menu by remember { mutableStateOf(false) }

  IconButton(
    onClick = onNewChatClick,
    enabled = !isNew
  ) {
    Icon(
      painter = painterResource(R.drawable.ic_open_in_new_24),
      contentDescription = null
    )
  }

  Box(modifier = Modifier.align(Alignment.CenterVertically)) {
    DropdownMenu(
      expanded = menu,
      modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
      onDismissRequest = { menu = false },
      properties = PopupProperties(focusable = false)
    ) {
      if (isNew) {
        NewMemu(
          onDetailsClick = {
            onViewDetailsClick()
            menu = false
          },
          onChangeModelClick = {
            onChangeModelClick(GPT35)
            menu = false
          },
          onChangeModel2Click = {
            onChangeModelClick(GPT4)
            menu = false
          },
          model = model
        )
      } else {
        OptionItem(
          text = "View Details",
          onClick = {
            onViewDetailsClick()
            menu = false
          },
          selected = false,
          icon = R.drawable.ic_info_outline_24
        )
        OptionItem(
          text = "Share",
          onClick = { menu = false },
          selected = false,
          icon = R.drawable.ic_share_24
        )
        Divider()
        OptionItem(
          text = "Rename",
          onClick = { menu = false },
          selected = false,
          icon = R.drawable.ic_edit_24
        )
        OptionItem(
          text = "Delete",
          onClick = { menu = false },
          selected = false,
          icon = R.drawable.ic_delete_outline_24
        )
      }
    }
    IconButton(onClick = { menu = !menu }) {
      Icon(
        imageVector = Icons.Default.MoreVert,
        contentDescription = null
      )
    }
  }

}

@Composable
fun ColumnScope.NewMemu(
  model: Model,
  onDetailsClick: () -> Unit,
  onChangeModelClick: () -> Unit,
  onChangeModel2Click: () -> Unit,
) {
  OptionItem(
    text = "View Details",
    onClick = onDetailsClick,
    selected = false,
    icon = R.drawable.ic_info_outline_24
  )
  Divider()
  OptionItem(
    text = "GPT-3.5",
    onClick = onChangeModelClick,
    selected = model == GPT35,
    icon = R.drawable.ic_voice
  )
  OptionItem(
    text = "GPT-4",
    onClick = onChangeModel2Click,
    selected = model == GPT4,
    icon = R.drawable.ic_voice
  )
}

@Composable
fun OptionItem(
  text: String,
  onClick: () -> Unit,
  selected: Boolean,
  @DrawableRes icon: Int,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .clickable { onClick() }
      .padding(16.dp)
      .fillMaxWidth()
  ) {
    Icon(
      painter = painterResource(icon),
      contentDescription = null,
    )

    Spacer(modifier = Modifier.width(8.dp))

    Text(text = text)

    if (selected) {
      Spacer(modifier = Modifier.weight(1f))

      Icon(
        painter = painterResource(R.drawable.ic_check_24),
        contentDescription = null,
      )
    }
  }
}

@Preview
@Composable
fun TopBarChatPreview() {
  val theme: Theme by remember { mutableStateOf(Theme.Dark(Surface())) }
  theme {
    TopBarChat(
      title = "Title",
      onMenuClick = {},
      onViewDetailsClick = {},
      onNewChatClick = {},
      model = GPT35,
      onChangeModelClick = {},
      isNew = false
    )
  }
}

@Preview
@Composable
fun OptionItemPreview() {
  OptionItem(
    text = "View Details",
    onClick = {},
    selected = false,
    icon = R.drawable.ic_info_outline_24
  )
}