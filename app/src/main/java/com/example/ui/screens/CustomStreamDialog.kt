package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ChannelCategory
import com.example.ui.theme.BdiptvGreenPrimary

@Composable
fun CustomStreamDialog(
  onDismiss: () -> Unit,
  onAddStream: (name: String, url: String, category: ChannelCategory) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var streamUrl by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf(ChannelCategory.BANGLADESH) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.surface,
      border = BorderStroke(1.dp, BdiptvGreenPrimary),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("add_custom_stream_dialog")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Tv,
            contentDescription = null,
            tint = BdiptvGreenPrimary
          )
          Text(
            text = "Add Custom IPTV Stream",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Text(
          text = "Enter any direct HLS (.m3u8), MP4, or BDIX live TV streaming URL to play inside BD IPTV player.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Channel Name (e.g. My Sports HD)") },
          singleLine = true,
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BdiptvGreenPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("custom_channel_name_input")
        )

        OutlinedTextField(
          value = streamUrl,
          onValueChange = { streamUrl = it },
          label = { Text("Stream URL (.m3u8, .mp4, http://...)") },
          leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
          singleLine = true,
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BdiptvGreenPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("custom_channel_url_input")
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End,
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("Cancel")
          }

          Spacer(modifier = Modifier.width(8.dp))

          Button(
            onClick = {
              if (streamUrl.isNotBlank()) {
                onAddStream(name, streamUrl, selectedCategory)
              }
            },
            enabled = streamUrl.isNotBlank(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = BdiptvGreenPrimary,
              contentColor = Color.Black
            ),
            modifier = Modifier.testTag("submit_custom_stream_button")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add & Play", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
