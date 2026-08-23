package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Channel
import com.example.data.model.ChannelCategory
import com.example.ui.MainViewModel
import com.example.ui.components.ChannelCard
import com.example.ui.components.VideoPlayerView
import com.example.ui.theme.BdiptvCyan
import com.example.ui.theme.BdiptvGold
import com.example.ui.theme.BdiptvGreenPrimary
import com.example.ui.theme.BdiptvRedAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelsScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val channels by viewModel.filteredChannels.collectAsState()
  val activeChannel by viewModel.activeChannel.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val onlyFavorites by viewModel.onlyFavorites.collectAsState()
  val isPlaying by viewModel.isPlaying.collectAsState()
  val isMuted by viewModel.isMuted.collectAsState()
  val streamQuality by viewModel.streamQuality.collectAsState()
  val isPlayerFullscreen by viewModel.isPlayerFullscreen.collectAsState()
  val showEpgSheet by viewModel.showEpgSheet.collectAsState()

  Box(modifier = modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize()) {

      // Active Video Player Header
      activeChannel?.let { channel ->
        VideoPlayerView(
          channel = channel,
          isPlaying = isPlaying,
          isMuted = isMuted,
          streamQuality = streamQuality,
          isFullscreen = isPlayerFullscreen,
          onTogglePlay = { viewModel.togglePlayPause() },
          onToggleMute = { viewModel.toggleMute() },
          onToggleFullscreen = { viewModel.toggleFullscreen() },
          onQualityChange = { viewModel.setQuality(it) },
          onToggleFavorite = { viewModel.toggleFavorite(channel.id) },
          onOpenEpg = { viewModel.setEpgSheetVisible(true) }
        )
      }

      // Search & Filters Header
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surface)
          .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Search bar row
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("Search 30+ BD & Global Channels...", fontSize = 13.sp) },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.primary
              )
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                  Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = BdiptvGreenPrimary,
              unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            modifier = Modifier
              .weight(1f)
              .height(50.dp)
              .testTag("channel_search_input")
          )

          // Favorites toggle pill
          Surface(
            onClick = { viewModel.toggleFavoritesOnly() },
            shape = RoundedCornerShape(20.dp),
            color = if (onlyFavorites) BdiptvRedAccent.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
            border = BorderStroke(
              1.dp,
              if (onlyFavorites) BdiptvRedAccent else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            ),
            modifier = Modifier
              .height(48.dp)
              .testTag("fav_filter_toggle")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = if (onlyFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorites Only",
                tint = if (onlyFavorites) BdiptvRedAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
              )
              Text(
                text = "Favs",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (onlyFavorites) BdiptvRedAccent else MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }

        // Horizontal Category Pills
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
          items(ChannelCategory.values()) { category ->
            val isSelected = (selectedCategory == category)
            FilterChip(
              selected = isSelected,
              onClick = { viewModel.setCategory(category) },
              label = {
                Text(
                  text = category.displayName,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = BdiptvGreenPrimary,
                selectedLabelColor = Color.Black,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                labelColor = MaterialTheme.colorScheme.onSurface
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = if (isSelected) BdiptvGreenPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                enabled = true,
                selected = isSelected
              ),
              modifier = Modifier.testTag("category_chip_${category.name}")
            )
          }
        }
      }

      // Channel List Section
      if (channels.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.LiveTv,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
              modifier = Modifier.size(64.dp)
            )
            Text(
              text = "No channels match your filter",
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(
              onClick = {
                viewModel.setCategory(ChannelCategory.ALL)
                viewModel.setSearchQuery("")
              },
              colors = ButtonDefaults.buttonColors(containerColor = BdiptvGreenPrimary)
            ) {
              Text("Show All Channels", color = Color.Black)
            }
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .testTag("channels_lazy_list"),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          item {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${channels.size} CHANNELS ONLINE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = BdiptvCyan,
                letterSpacing = 1.sp
              )
              Text(
                text = "⚡ Low Latency BDIX",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = BdiptvGold
              )
            }
          }

          items(channels, key = { it.id }) { channel ->
            ChannelCard(
              channel = channel,
              isSelected = (activeChannel?.id == channel.id),
              onChannelClick = { viewModel.selectChannel(it) },
              onToggleFavorite = { viewModel.toggleFavorite(it) }
            )
          }

          item {
            Spacer(modifier = Modifier.height(72.dp))
          }
        }
      }
    }

    // Floating Action Button to Add Custom Stream
    FloatingActionButton(
      onClick = { viewModel.setAddStreamDialogVisible(true) },
      containerColor = BdiptvGreenPrimary,
      contentColor = Color.Black,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
        .testTag("add_custom_stream_fab")
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = "Add Custom Stream")
        Text("Add Stream", fontWeight = FontWeight.Bold)
      }
    }

    // Program Guide (EPG) Bottom Sheet
    if (showEpgSheet && activeChannel != null) {
      val epgState = rememberModalBottomSheetState()
      val schedules = viewModel.getActiveChannelSchedules()

      ModalBottomSheet(
        onDismissRequest = { viewModel.setEpgSheetVisible(false) },
        sheetState = epgState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("epg_bottom_sheet")
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "${activeChannel?.name} - TV Guide",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = BdiptvGreenPrimary
              )
              Text(
                text = "Today's Broadcast Timeline & EPG",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          schedules.forEachIndexed { index, prog ->
            Surface(
              color = MaterialTheme.colorScheme.surfaceVariant,
              shape = RoundedCornerShape(10.dp),
              border = BorderStroke(
                1.dp,
                if (index == 0) BdiptvGreenPrimary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
              ),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "${prog.startTime} - ${prog.endTime}",
                    color = if (index == 0) BdiptvGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                  if (index == 0) {
                    Surface(
                      color = BdiptvRedAccent,
                      shape = RoundedCornerShape(4.dp)
                    ) {
                      Text(
                        text = "NOW PLAYING",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }
                }

                Text(
                  text = prog.programName,
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                  text = prog.description,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (index == 0 && prog.progress > 0f) {
                  LinearProgressIndicator(
                    progress = prog.progress,
                    color = BdiptvGreenPrimary,
                    trackColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                      .fillMaxWidth()
                      .height(6.dp)
                      .clip(RoundedCornerShape(3.dp))
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}
