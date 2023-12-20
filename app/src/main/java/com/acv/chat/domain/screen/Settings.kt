package com.acv.chat.domain.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.acv.chat.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
  onBackClick: () -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
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
    LazyColumn(modifier = Modifier.padding(it)) {
      item {
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
          Text(text = "Item", modifier = Modifier.weight(1f))
          Icon(Icons.Default.MoreVert, contentDescription = null)
        }
      }
      items(20) {
        when(it){
          0 -> {
            Row(
              modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
              Text("Account", style = MaterialTheme.typography.labelMedium)
            }
          }
          5-> {
            Row(
              modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
              Text("App", style = MaterialTheme.typography.labelMedium)
            }
          }
          7-> {
            Row(
              modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
              Text("Speech", style = MaterialTheme.typography.labelMedium)
            }
          }
          else -> {
            Row(
              modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
              Icon(Icons.Default.MoreVert, contentDescription = null)
              Spacer(Modifier.width(16.dp))
              Text("Item")
            }
          }
        }
      }
    }
  }
}