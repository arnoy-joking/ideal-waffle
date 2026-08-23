package com.example.data.model

data class Channel(
  val id: String,
  val name: String,
  val category: ChannelCategory,
  val logoUrl: String,
  val streamUrl: String,
  val backupStreamUrl: String = "",
  val resolution: String = "1080p HD",
  val language: String = "Bengali",
  val isLive: Boolean = true,
  val currentProgram: String = "Live Broadcast",
  val nextProgram: String = "Scheduled Program",
  val description: String = "",
  val isFavorite: Boolean = false,
  val country: String = "Bangladesh"
)

enum class ChannelCategory(val displayName: String, val iconName: String) {
  ALL("All Channels", "Tv"),
  BANGLADESH("Bangladesh TV", "Flag"),
  SPORTS("Live Sports", "Sports"),
  ENTERTAINMENT("Entertainment", "Theaters"),
  MOVIES("Movies & Cinema", "Movie"),
  NEWS("News 24/7", "Feed"),
  KIDS("Kids & Cartoons", "SmartDisplay"),
  INFOTAINMENT("Discovery & Docs", "Explore"),
  MUSIC("Music & Radio", "MusicNote"),
  RELIGIOUS("Religious", "Mosque"),
  INTERNATIONAL("International", "Public")
}

data class ProgramSchedule(
  val id: String,
  val channelId: String,
  val programName: String,
  val startTime: String,
  val endTime: String,
  val progress: Float = 0.45f,
  val description: String = "",
  val genre: String = "General"
)

data class ChatMessage(
  val id: String = java.util.UUID.randomUUID().toString(),
  val role: String, // "user" or "model"
  val text: String,
  val timestamp: Long = System.currentTimeMillis(),
  val modelName: String = "gemini-3.5-flash",
  val isError: Boolean = false
)

data class GeminiRolePreset(
  val id: String,
  val title: String,
  val roleDescription: String,
  val systemInstruction: String,
  val recommendedModel: String
)

data class GeneratedImageItem(
  val id: String = java.util.UUID.randomUUID().toString(),
  val prompt: String,
  val model: String = "gemini-3-pro-image-preview",
  val resolution: String = "4K", // 1K, 2K, 4K
  val aspectRatio: String = "16:9",
  val base64Data: String? = null,
  val timestamp: Long = System.currentTimeMillis(),
  val isGenerating: Boolean = false,
  val errorMessage: String? = null
)

enum class StreamQuality(val label: String, val badge: String) {
  AUTO("Auto (Adaptive)", "AUTO"),
  FHD("1080p Full HD", "1080p"),
  HD("720p HD", "720p"),
  SD("480p SD", "480p")
}
