package com.example.ui.components

import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.Channel
import com.example.data.model.StreamQuality
import com.example.ui.theme.BdiptvCyan
import com.example.ui.theme.BdiptvGold
import com.example.ui.theme.BdiptvGreenPrimary
import com.example.ui.theme.BdiptvRedAccent
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerView(
  channel: Channel,
  isPlaying: Boolean,
  isMuted: Boolean,
  streamQuality: StreamQuality,
  isFullscreen: Boolean,
  onTogglePlay: () -> Unit,
  onToggleMute: () -> Unit,
  onToggleFullscreen: () -> Unit,
  onQualityChange: (StreamQuality) -> Unit,
  onToggleFavorite: () -> Unit,
  onOpenEpg: () -> Unit,
  onClosePlayer: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var showControls by remember { mutableStateOf(true) }
  var showQualityMenu by remember { mutableStateOf(false) }

  // Auto-hide controls after 4 seconds of inactivity
  LaunchedEffect(showControls, isPlaying) {
    if (showControls && isPlaying) {
      delay(4000)
      showControls = false
    }
  }

  // Pulsing animation for live badge
  val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "live_alpha"
  )

  Box(
    modifier = modifier
      .then(
        if (isFullscreen) Modifier.fillMaxSize()
        else Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 9f)
      )
      .background(Color.Black)
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null
      ) {
        showControls = !showControls
      }
      .testTag("video_player_container")
  ) {
    // Native VideoView embedded in AndroidView
    AndroidView(
      modifier = Modifier.fillMaxSize(),
      factory = { ctx ->
        VideoView(ctx).apply {
          val uri = Uri.parse(channel.streamUrl)
          setVideoURI(uri)
          setOnPreparedListener { mp ->
            mp.isLooping = true
            if (isMuted) {
              mp.setVolume(0f, 0f)
            } else {
              mp.setVolume(1f, 1f)
            }
            if (isPlaying) {
              start()
            }
          }
          setOnErrorListener { _, _, _ ->
            true
          }
        }
      },
      update = { videoView ->
        if (isPlaying) {
          if (!videoView.isPlaying) {
            videoView.start()
          }
        } else {
          if (videoView.isPlaying) {
            videoView.pause()
          }
        }
      }
    )

    // Animated Live Stream Equalizer Bars overlay (simulating active stream telemetry)
    if (isPlaying) {
      Row(
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(end = 12.dp, bottom = 48.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
      ) {
        repeat(4) { index ->
          val barHeight by infiniteTransition.animateFloat(
            initialValue = (6 + (index * 4)).toFloat(),
            targetValue = (18 + ((index % 2) * 8)).toFloat(),
            animationSpec = infiniteRepeatable(
              animation = tween(400 + (index * 150), easing = FastOutSlowInEasing),
              repeatMode = RepeatMode.Reverse
            ),
            label = "equalizer_bar_$index"
          )
          Box(
            modifier = Modifier
              .width(3.dp)
              .height(barHeight.dp)
              .background(BdiptvGreenPrimary, RoundedCornerShape(2.dp))
          )
        }
      }
    }

    // Controls Overlay
    AnimatedVisibility(
      visible = showControls,
      modifier = Modifier.fillMaxSize()
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color.Black.copy(alpha = 0.75f),
                Color.Transparent,
                Color.Black.copy(alpha = 0.85f)
              )
            )
          )
      ) {
        // Top Bar inside Player
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
          ) {
            // Live indicator
            Surface(
              color = BdiptvRedAccent.copy(alpha = pulseAlpha),
              shape = RoundedCornerShape(4.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                )
                Text(
                  text = "LIVE",
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Column {
              Text(
                text = channel.name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = channel.currentProgram,
                color = Color(0xFFCBD5E1),
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            // Favorite Button
            IconButton(
              onClick = onToggleFavorite,
              modifier = Modifier.testTag("player_favorite_button")
            ) {
              Icon(
                imageVector = if (channel.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite Channel",
                tint = if (channel.isFavorite) BdiptvRedAccent else Color.White
              )
            }

            // Quality Switcher
            Box {
              IconButton(
                onClick = { showQualityMenu = true },
                modifier = Modifier.testTag("player_quality_button")
              ) {
                Icon(
                  imageVector = Icons.Default.HighQuality,
                  contentDescription = "Stream Quality",
                  tint = BdiptvCyan
                )
              }

              DropdownMenu(
                expanded = showQualityMenu,
                onDismissRequest = { showQualityMenu = false }
              ) {
                StreamQuality.values().forEach { quality ->
                  DropdownMenuItem(
                    text = {
                      Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                      ) {
                        Text(quality.label)
                        if (streamQuality == quality) {
                          Text("✓", color = BdiptvGreenPrimary, fontWeight = FontWeight.Bold)
                        }
                      }
                    },
                    onClick = {
                      onQualityChange(quality)
                      showQualityMenu = false
                    }
                  )
                }
              }
            }

            // Close Player (if non-fullscreen embedded close requested)
            if (onClosePlayer != null && !isFullscreen) {
              IconButton(onClick = onClosePlayer) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Close Player",
                  tint = Color.White
                )
              }
            }
          }
        }

        // Center Play / Pause Button
        Box(
          modifier = Modifier
            .align(Alignment.Center)
            .size(56.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(onClick = onTogglePlay)
            .testTag("player_play_pause_center"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Play",
            tint = BdiptvGreenPrimary,
            modifier = Modifier.size(36.dp)
          )
        }

        // Bottom Controls Bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(horizontal = 12.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Mute / Unmute
            IconButton(
              onClick = onToggleMute,
              modifier = Modifier.testTag("player_mute_button")
            ) {
              Icon(
                imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                contentDescription = if (isMuted) "Unmute" else "Mute",
                tint = Color.White
              )
            }

            // Quality Badge
            Surface(
              color = Color.White.copy(alpha = 0.15f),
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = streamQuality.badge,
                color = BdiptvCyan,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }

            // Low Latency / BD IPTV server badge
            Surface(
              color = Color.White.copy(alpha = 0.15f),
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = "BDIX Ultra",
                color = BdiptvGold,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            // EPG Guide Button
            IconButton(
              onClick = onOpenEpg,
              modifier = Modifier.testTag("player_epg_button")
            ) {
              Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Program Schedule Guide",
                tint = Color.White
              )
            }

            // Fullscreen Toggle Button
            IconButton(
              onClick = onToggleFullscreen,
              modifier = Modifier.testTag("player_fullscreen_button")
            ) {
              Icon(
                imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                contentDescription = if (isFullscreen) "Exit Fullscreen" else "Enter Fullscreen",
                tint = Color.White
              )
            }
          }
        }
      }
    }
  }
}
