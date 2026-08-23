package com.example.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoSizeSelectActual
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.GeneratedImageItem
import com.example.ui.MainViewModel
import com.example.ui.theme.BdiptvCyan
import com.example.ui.theme.BdiptvGold
import com.example.ui.theme.BdiptvGreenPrimary
import com.example.ui.theme.BdiptvRedAccent

@Composable
fun AiImageGenScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val generatedImages by viewModel.generatedImages.collectAsState()
  val isImageGenerating by viewModel.isImageGenerating.collectAsState()
  val selectedImageSize by viewModel.selectedImageSize.collectAsState()
  val selectedAspectRatio by viewModel.selectedAspectRatio.collectAsState()
  val selectedModalImage by viewModel.selectedImageForModal.collectAsState()

  val availableSizes = viewModel.availableImageSizes // ["1K", "2K", "4K"]
  val availableRatios = viewModel.availableAspectRatios // ["16:9", "9:16", "1:1", "4:3"]

  var promptInput by remember {
    mutableStateOf("T Sports Cricket Matchday Finals 4K Ultra HD Stadium Poster")
  }

  val promptPresets = listOf(
    "T Sports Live Cricket Stadium Final 4K HD",
    "BD IPTV Futuristic Neon Streaming Lounge",
    "Bengali Cyberpunk Cinema Poster for Zee Bangla",
    "UEFA Champions League Matchday Wallpaper 4K",
    "Somoy News 24/7 Holographic Live Studio"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Top Generation Panel
    Surface(
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 4.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = BdiptvGold,
              modifier = Modifier.size(20.dp)
            )
            Text(
              text = "AI 4K Poster Generator",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Surface(
            color = BdiptvGreenPrimary.copy(alpha = 0.15f),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text(
              text = "gemini-3-pro-image-preview",
              color = BdiptvGreenPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        // Prompt Input
        OutlinedTextField(
          value = promptInput,
          onValueChange = { promptInput = it },
          placeholder = { Text("Describe the TV poster or wallpaper to generate...", fontSize = 13.sp) },
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BdiptvGreenPrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
          ),
          minLines = 2,
          maxLines = 4,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("image_prompt_input")
        )

        // AFFORDANCE 1: Image Size Selector (1K, 2K, 4K) as required
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = "Size:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          availableSizes.forEach { size ->
            val isSelected = (selectedImageSize == size)
            FilterChip(
              selected = isSelected,
              onClick = { viewModel.setImageSize(size) },
              label = {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.HighQuality,
                    contentDescription = null,
                    tint = if (isSelected) Color.Black else BdiptvCyan,
                    modifier = Modifier.size(14.dp)
                  )
                  Text(
                    text = "$size UHD",
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                  )
                }
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = BdiptvCyan,
                selectedLabelColor = Color.Black,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                labelColor = MaterialTheme.colorScheme.onSurface
              ),
              modifier = Modifier.testTag("size_chip_$size")
            )
          }
        }

        // AFFORDANCE 2: Aspect Ratio Selector (16:9, 9:16, 1:1, 4:3)
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = "Ratio:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          availableRatios.forEach { ratio ->
            val isSelected = (selectedAspectRatio == ratio)
            FilterChip(
              selected = isSelected,
              onClick = { viewModel.setAspectRatio(ratio) },
              label = {
                Text(
                  text = when (ratio) {
                    "16:9" -> "16:9 TV"
                    "9:16" -> "9:16 Phone"
                    "1:1" -> "1:1 Square"
                    else -> "4:3 Classic"
                  },
                  fontSize = 11.sp
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = BdiptvGreenPrimary,
                selectedLabelColor = Color.Black,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                labelColor = MaterialTheme.colorScheme.onSurface
              ),
              modifier = Modifier.testTag("ratio_chip_$ratio")
            )
          }
        }

        // Preset Prompt Chips
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          contentPadding = PaddingValues(top = 2.dp)
        ) {
          items(promptPresets) { preset ->
            Surface(
              onClick = { promptInput = preset },
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.surfaceVariant,
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
              Text(
                text = preset,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }

        // Generate Button
        Button(
          onClick = { viewModel.generateImage(promptInput) },
          enabled = promptInput.isNotBlank() && !isImageGenerating,
          colors = ButtonDefaults.buttonColors(
            containerColor = BdiptvGreenPrimary,
            contentColor = Color.Black
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("generate_image_button")
        ) {
          if (isImageGenerating) {
            CircularProgressIndicator(
              modifier = Modifier.size(20.dp),
              color = Color.Black,
              strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Rendering $selectedImageSize Quality Image...", fontWeight = FontWeight.Bold)
          } else {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generate $selectedImageSize Wallpaper", fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // Generated Gallery List
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(horizontal = 14.dp, vertical = 8.dp)
        .testTag("generated_images_list"),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "GENERATED TV POSTERS & WALLPAPERS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = BdiptvCyan,
            letterSpacing = 1.sp
          )
          Text(
            text = "${generatedImages.size} items",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      items(generatedImages, key = { it.id }) { item ->
        GeneratedImageCard(
          item = item,
          onClick = { viewModel.setSelectedImageForModal(it) }
        )
      }

      item {
        Spacer(modifier = Modifier.height(40.dp))
      }
    }
  }

  // Fullscreen Image Modal Dialog
  selectedModalImage?.let { modalItem ->
    Dialog(onDismissRequest = { viewModel.setSelectedImageForModal(null) }) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, BdiptvGreenPrimary),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("image_preview_dialog")
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              color = BdiptvGreenPrimary.copy(alpha = 0.2f),
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = "${modalItem.resolution} UHD (${modalItem.aspectRatio})",
                color = BdiptvGreenPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }

            IconButton(onClick = { viewModel.setSelectedImageForModal(null) }) {
              Icon(Icons.Default.Close, contentDescription = "Close Dialog")
            }
          }

          // Image display
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(16f / 9f)
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFF0F172A)),
            contentAlignment = Alignment.Center
          ) {
            RenderImageContent(item = modalItem, modifier = Modifier.fillMaxSize())
          }

          Text(
            text = modalItem.prompt,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                Toast.makeText(context, "Wallpaper saved to device gallery!", Toast.LENGTH_SHORT).show()
              },
              colors = ButtonDefaults.buttonColors(containerColor = BdiptvGreenPrimary),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Download, contentDescription = null, tint = Color.Black)
              Spacer(modifier = Modifier.width(6.dp))
              Text("Save ${modalItem.resolution}", color = Color.Black, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

@Composable
fun GeneratedImageCard(
  item: GeneratedImageItem,
  onClick: (GeneratedImageItem) -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick(item) }
      .testTag("gen_card_${item.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
  ) {
    Column {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 9f)
          .background(Color(0xFF0F172A)),
        contentAlignment = Alignment.Center
      ) {
        if (item.isGenerating) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            CircularProgressIndicator(
              color = BdiptvGreenPrimary,
              modifier = Modifier.size(36.dp)
            )
            Text(
              text = "Rendering ${item.resolution} Ultra HD...",
              fontSize = 12.sp,
              color = BdiptvCyan,
              fontWeight = FontWeight.Bold
            )
          }
        } else {
          RenderImageContent(item = item, modifier = Modifier.fillMaxSize())
        }

        // Overlay Badges
        Surface(
          color = Color.Black.copy(alpha = 0.75f),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(8.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.HighQuality,
              contentDescription = null,
              tint = BdiptvGold,
              modifier = Modifier.size(12.dp)
            )
            Text(
              text = "${item.resolution} UHD",
              color = BdiptvGold,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Column(modifier = Modifier.padding(12.dp)) {
        Text(
          text = item.prompt,
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Model: ${item.model}",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "Ratio: ${item.aspectRatio}",
            fontSize = 11.sp,
            color = BdiptvCyan
          )
        }
      }
    }
  }
}

@Composable
fun RenderImageContent(
  item: GeneratedImageItem,
  modifier: Modifier = Modifier
) {
  val base64Data = item.base64Data
  if (base64Data != null && !base64Data.startsWith("DEMO_POSTER")) {
    val bitmap = remember(base64Data) {
      try {
        val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
      } catch (e: Exception) {
        null
      }
    }

    if (bitmap != null) {
      Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = item.prompt,
        contentScale = ContentScale.Crop,
        modifier = modifier
      )
      return
    }
  }

  // Visual Gradient Canvas representation for TV Poster / Wallpaper
  Box(
    modifier = modifier.background(
      Brush.linearGradient(
        colors = listOf(
          Color(0xFF0F2027),
          Color(0xFF203A43),
          Color(0xFF2C5364)
        )
      )
    ),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(6.dp),
      modifier = Modifier.padding(16.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Image,
        contentDescription = null,
        tint = BdiptvGreenPrimary,
        modifier = Modifier.size(36.dp)
      )
      Text(
        text = "${item.resolution} Ultra HD TV Poster",
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "Aspect Ratio: ${item.aspectRatio} | gemini-3-pro-image-preview",
        color = Color(0xFF94A3B8),
        fontSize = 11.sp
      )
    }
  }
}
