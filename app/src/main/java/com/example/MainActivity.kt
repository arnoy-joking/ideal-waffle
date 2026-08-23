package com.example.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.screens.AiChatScreen
import com.example.ui.screens.AiImageGenScreen
import com.example.ui.screens.ChannelsScreen
import com.example.ui.screens.CustomStreamDialog
import com.example.ui.screens.WebPortalScreen
import com.example.ui.theme.BdiptvCyan
import com.example.ui.theme.BdiptvGold
import com.example.ui.theme.BdiptvGreenPrimary
import com.example.ui.theme.BdiptvRedAccent
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme(darkTheme = true) {
        MainApp(viewModel = viewModel)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: MainViewModel) {
  val currentTab by viewModel.currentTab.collectAsState()
  val showAddStreamDialog by viewModel.showAddStreamDialog.collectAsState()
  var showInfoDialog by remember { mutableStateOf(false) }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .testTag("main_scaffold"),
    topBar = {
      TopAppBar(
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BdiptvGreenPrimary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Tv,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
              )
            }

            Column {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = "BD IPTV",
                  style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                  ),
                  color = BdiptvGreenPrimary
                )

                Surface(
                  color = BdiptvRedAccent,
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = "BDIX LIVE",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                  )
                }
              }
              Text(
                text = "tv.bdiptv.net Player & Guide",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
              )
            }
          }
        },
        actions = {
          IconButton(
            onClick = { viewModel.setAddStreamDialogVisible(true) },
            modifier = Modifier.testTag("topbar_add_stream_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Add Custom Stream",
              tint = BdiptvGreenPrimary
            )
          }

          IconButton(
            onClick = { showInfoDialog = true },
            modifier = Modifier.testTag("topbar_info_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = "App Information",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface,
          titleContentColor = MaterialTheme.colorScheme.onSurface
        )
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("bottom_nav_bar")
      ) {
        NavigationBarItem(
          selected = currentTab == AppTab.CHANNELS,
          onClick = { viewModel.setTab(AppTab.CHANNELS) },
          icon = { Icon(Icons.Default.LiveTv, contentDescription = "Live TV Channels") },
          label = { Text("Live TV", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Black,
            selectedTextColor = BdiptvGreenPrimary,
            indicatorColor = BdiptvGreenPrimary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
          ),
          modifier = Modifier.testTag("nav_item_channels")
        )

        NavigationBarItem(
          selected = currentTab == AppTab.WEB_PORTAL,
          onClick = { viewModel.setTab(AppTab.WEB_PORTAL) },
          icon = { Icon(Icons.Default.Language, contentDescription = "tv.bdiptv.net Portal") },
          label = { Text("Web Portal", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Black,
            selectedTextColor = BdiptvCyan,
            indicatorColor = BdiptvCyan,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
          ),
          modifier = Modifier.testTag("nav_item_web_portal")
        )

        NavigationBarItem(
          selected = currentTab == AppTab.AI_CHAT,
          onClick = { viewModel.setTab(AppTab.AI_CHAT) },
          icon = { Icon(Icons.Default.SmartToy, contentDescription = "AI Media Guide") },
          label = { Text("AI Guide", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Black,
            selectedTextColor = BdiptvGreenPrimary,
            indicatorColor = BdiptvGreenPrimary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
          ),
          modifier = Modifier.testTag("nav_item_ai_chat")
        )

        NavigationBarItem(
          selected = currentTab == AppTab.AI_POSTER,
          onClick = { viewModel.setTab(AppTab.AI_POSTER) },
          icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI 4K Posters") },
          label = { Text("4K Posters", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Black,
            selectedTextColor = BdiptvGold,
            indicatorColor = BdiptvGold,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
          ),
          modifier = Modifier.testTag("nav_item_ai_poster")
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentTab) {
        AppTab.CHANNELS -> ChannelsScreen(viewModel = viewModel)
        AppTab.WEB_PORTAL -> WebPortalScreen()
        AppTab.AI_CHAT -> AiChatScreen(viewModel = viewModel)
        AppTab.AI_POSTER -> AiImageGenScreen(viewModel = viewModel)
      }
    }
  }

  // Custom Stream Dialog
  if (showAddStreamDialog) {
    CustomStreamDialog(
      onDismiss = { viewModel.setAddStreamDialogVisible(false) },
      onAddStream = { name, url, category ->
        viewModel.addCustomChannel(name, url, category)
      }
    )
  }

  // Info Dialog
  if (showInfoDialog) {
    Dialog(onDismissRequest = { showInfoDialog = false }) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, BdiptvGreenPrimary),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("info_dialog")
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(Icons.Default.Tv, contentDescription = null, tint = BdiptvGreenPrimary)
              Text(
                text = "About BD IPTV",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
            }
            IconButton(onClick = { showInfoDialog = false }) {
              Icon(Icons.Default.Close, contentDescription = "Close")
            }
          }

          Text(
            text = "BD IPTV brings live television, sports, entertainment, and movies from tv.bdiptv.net and Bangladeshi BDIX networks to your device.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Text(
                text = "✨ Featured AI Capabilities",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = BdiptvGold
              )
              Text(
                text = "• Gemini 3.5 Flash & 3.1 Pro chat for live cricket & drama schedules",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "• gemini-3-pro-image-preview for 1K, 2K, and 4K Ultra HD TV posters",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "• Built-in IPTV Video Player with Low Latency streams",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }
    }
  }
}
