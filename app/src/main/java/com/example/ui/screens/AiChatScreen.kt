package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.MainViewModel
import com.example.ui.theme.BdiptvCyan
import com.example.ui.theme.BdiptvGold
import com.example.ui.theme.BdiptvGreenPrimary
import com.example.ui.theme.BdiptvRedAccent

@Composable
fun AiChatScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val messages by viewModel.chatMessages.collectAsState()
  val isChatLoading by viewModel.isChatLoading.collectAsState()
  val selectedModel by viewModel.selectedChatModel.collectAsState()
  val selectedRole by viewModel.selectedRole.collectAsState()
  val rolePresets = viewModel.rolePresets

  var inputText by remember { mutableStateOf("") }
  var showModelMenu by remember { mutableStateOf(false) }
  var showRoleMenu by remember { mutableStateOf(false) }
  val listState = rememberLazyListState()

  // Auto scroll to bottom when new messages arrive
  LaunchedEffect(messages.size, isChatLoading) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  val suggestedPrompts = listOf(
    "T Sports Live Cricket Schedule",
    "Top Bengali Natok & Drama Serials",
    "Blockbuster Movies on Star Gold & Sony Max",
    "How to play custom IPTV M3U streams?"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Model & Role Selector Header Bar
    Surface(
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Model Picker Pill
          Box {
            Surface(
              onClick = { showModelMenu = true },
              shape = RoundedCornerShape(20.dp),
              color = MaterialTheme.colorScheme.surfaceVariant,
              border = BorderStroke(1.dp, BdiptvCyan.copy(alpha = 0.6f)),
              modifier = Modifier.testTag("model_picker_chip")
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = when (selectedModel) {
                    "gemini-3.1-pro-preview" -> Icons.Default.Psychology
                    "gemini-3.1-flash-lite" -> Icons.Default.Speed
                    else -> Icons.Default.Bolt
                  },
                  contentDescription = null,
                  tint = BdiptvCyan,
                  modifier = Modifier.size(16.dp)
                )
                Text(
                  text = when (selectedModel) {
                    "gemini-3.1-pro-preview" -> "Gemini 3.1 Pro (Complex)"
                    "gemini-3.1-flash-lite" -> "Gemini 3.1 Flash Lite (Fast)"
                    else -> "Gemini 3.5 Flash (General)"
                  },
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                  imageVector = Icons.Default.ExpandMore,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(16.dp)
                )
              }
            }

            DropdownMenu(
              expanded = showModelMenu,
              onDismissRequest = { showModelMenu = false }
            ) {
              DropdownMenuItem(
                text = {
                  Column {
                    Text("gemini-3.5-flash", fontWeight = FontWeight.Bold)
                    Text("General tasks & media recommendation", fontSize = 11.sp, color = Color.Gray)
                  }
                },
                onClick = {
                  viewModel.setChatModel("gemini-3.5-flash")
                  showModelMenu = false
                }
              )
              DropdownMenuItem(
                text = {
                  Column {
                    Text("gemini-3.1-pro-preview", fontWeight = FontWeight.Bold)
                    Text("Particularly complex tasks & deep analysis", fontSize = 11.sp, color = Color.Gray)
                  }
                },
                onClick = {
                  viewModel.setChatModel("gemini-3.1-pro-preview")
                  showModelMenu = false
                }
              )
              DropdownMenuItem(
                text = {
                  Column {
                    Text("gemini-3.1-flash-lite", fontWeight = FontWeight.Bold)
                    Text("Tasks that should happen fast", fontSize = 11.sp, color = Color.Gray)
                  }
                },
                onClick = {
                  viewModel.setChatModel("gemini-3.1-flash-lite")
                  showModelMenu = false
                }
              )
            }
          }

          // Clear Chat History Button
          IconButton(
            onClick = { viewModel.clearChatHistory() },
            modifier = Modifier.testTag("clear_chat_button")
          ) {
            Icon(
              imageVector = Icons.Default.DeleteSweep,
              contentDescription = "Clear Chat History",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        // Role Presets Chips
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          contentPadding = PaddingValues(top = 8.dp)
        ) {
          items(rolePresets) { role ->
            val isSelected = (selectedRole.id == role.id)
            FilterChip(
              selected = isSelected,
              onClick = { viewModel.setRolePreset(role) },
              label = {
                Text(
                  text = role.title,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = BdiptvGreenPrimary.copy(alpha = 0.2f),
                selectedLabelColor = BdiptvGreenPrimary,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = if (isSelected) BdiptvGreenPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                enabled = true,
                selected = isSelected
              )
            )
          }
        }
      }
    }

    // Scrollable Chat Message Thread
    LazyColumn(
      state = listState,
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(horizontal = 12.dp)
        .testTag("chat_messages_list"),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(vertical = 12.dp)
    ) {
      items(messages, key = { it.id }) { message ->
        ChatMessageBubble(
          message = message,
          onCopyText = { text ->
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Gemini Reply", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Copied message to clipboard", Toast.LENGTH_SHORT).show()
          }
        )
      }

      if (isChatLoading) {
        item {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
          ) {
            CircularProgressIndicator(
              modifier = Modifier.size(18.dp),
              color = BdiptvGreenPrimary,
              strokeWidth = 2.dp
            )
            Text(
              text = "Gemini is generating response...",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    // Suggested prompt pills
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
    ) {
      items(suggestedPrompts) { prompt ->
        Surface(
          onClick = {
            inputText = prompt
            viewModel.sendChatMessage(prompt)
            inputText = ""
          },
          shape = RoundedCornerShape(16.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = BdiptvGold,
              modifier = Modifier.size(12.dp)
            )
            Text(
              text = prompt,
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }

    // Chat Message Input Box
    Surface(
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 8.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = inputText,
          onValueChange = { inputText = it },
          placeholder = { Text("Ask about cricket, drama, matches, streams...", fontSize = 13.sp) },
          shape = RoundedCornerShape(24.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BdiptvGreenPrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
          ),
          maxLines = 3,
          modifier = Modifier
            .weight(1f)
            .testTag("chat_input_field")
        )

        IconButton(
          onClick = {
            if (inputText.isNotBlank()) {
              val promptToSend = inputText
              inputText = ""
              viewModel.sendChatMessage(promptToSend)
            }
          },
          enabled = inputText.isNotBlank() && !isChatLoading,
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
              if (inputText.isNotBlank() && !isChatLoading) BdiptvGreenPrimary else MaterialTheme.colorScheme.surfaceVariant
            )
            .testTag("chat_send_button")
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send Message",
            tint = if (inputText.isNotBlank() && !isChatLoading) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

@Composable
fun ChatMessageBubble(
  message: ChatMessage,
  onCopyText: (String) -> Unit
) {
  val isUser = message.role == "user"

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
  ) {
    if (!isUser) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(BdiptvGreenPrimary.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.SmartToy,
          contentDescription = "Gemini",
          tint = BdiptvGreenPrimary,
          modifier = Modifier.size(18.dp)
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
    }

    Surface(
      shape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (isUser) 16.dp else 4.dp,
        bottomEnd = if (isUser) 4.dp else 16.dp
      ),
      color = if (isUser) {
        BdiptvGreenPrimary
      } else {
        MaterialTheme.colorScheme.surfaceVariant
      },
      border = if (!isUser) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
      } else null,
      modifier = Modifier
        .fillMaxWidth(if (isUser) 0.82f else 0.90f)
        .testTag(if (isUser) "user_message_bubble" else "model_message_bubble")
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        if (!isUser) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = message.modelName,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = BdiptvCyan
            )

            IconButton(
              onClick = { onCopyText(message.text) },
              modifier = Modifier.size(20.dp)
            ) {
              Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy Text",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp)
              )
            }
          }
          Spacer(modifier = Modifier.height(4.dp))
        }

        Text(
          text = message.text,
          style = MaterialTheme.typography.bodyMedium,
          color = if (isUser) Color.Black else MaterialTheme.colorScheme.onSurface,
          lineHeight = 20.sp
        )
      }
    }
  }
}
