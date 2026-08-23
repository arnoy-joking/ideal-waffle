package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Channel
import com.example.ui.theme.BdiptvCyan
import com.example.ui.theme.BdiptvGold
import com.example.ui.theme.BdiptvGreenPrimary
import com.example.ui.theme.BdiptvRedAccent

@Composable
fun ChannelCard(
  channel: Channel,
  isSelected: Boolean,
  onChannelClick: (Channel) -> Unit,
  onToggleFavorite: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onChannelClick(channel) }
      .testTag("channel_card_${channel.id}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
      } else {
        MaterialTheme.colorScheme.surfaceVariant
      }
    ),
    border = if (isSelected) {
      BorderStroke(1.5.dp, BdiptvGreenPrimary)
    } else {
      BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
    }
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Channel Logo Thumbnail with overlay play icon on select
      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFF0F172A)),
        contentAlignment = Alignment.Center
      ) {
        AsyncImage(
          model = channel.logoUrl,
          contentDescription = "${channel.name} Logo",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxWidth()
        )

        if (isSelected) {
          Box(
            modifier = Modifier
              .matchParentSize()
              .background(Color.Black.copy(alpha = 0.45f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.PlayCircleFilled,
              contentDescription = "Playing Now",
              tint = BdiptvGreenPrimary,
              modifier = Modifier.size(28.dp)
            )
          }
        }
      }

      // Channel Info
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(2.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = channel.name,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            ),
            color = if (isSelected) BdiptvGreenPrimary else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )

          // Live pill
          Surface(
            color = BdiptvRedAccent,
            shape = RoundedCornerShape(3.dp)
          ) {
            Text(
              text = "LIVE",
              color = Color.White,
              fontSize = 9.sp,
              fontWeight = FontWeight.Black,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
            )
          }
        }

        // Current playing program
        Text(
          text = "▶ ${channel.currentProgram}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        // Metadata badges
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.padding(top = 2.dp)
        ) {
          // Resolution badge
          Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(0.5.dp, BdiptvCyan.copy(alpha = 0.5f))
          ) {
            Text(
              text = channel.resolution,
              color = BdiptvCyan,
              fontSize = 10.sp,
              fontWeight = FontWeight.Medium,
              modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
            )
          }

          // Language badge
          Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
          ) {
            Text(
              text = channel.language,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 10.sp,
              modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
            )
          }
        }
      }

      // Favorite toggle button
      IconButton(
        onClick = { onToggleFavorite(channel.id) },
        modifier = Modifier.testTag("fav_btn_${channel.id}")
      ) {
        Icon(
          imageVector = if (channel.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
          contentDescription = "Toggle Favorite",
          tint = if (channel.isFavorite) BdiptvRedAccent else MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
