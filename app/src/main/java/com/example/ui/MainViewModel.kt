package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ChannelRepository
import com.example.data.GeminiService
import com.example.data.model.Channel
import com.example.data.model.ChannelCategory
import com.example.data.model.ChatMessage
import com.example.data.model.GeminiRolePreset
import com.example.data.model.GeneratedImageItem
import com.example.data.model.ProgramSchedule
import com.example.data.model.StreamQuality
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val title: String) {
  CHANNELS("Live TV"),
  WEB_PORTAL("tv.bdiptv.net"),
  AI_CHAT("AI Guide"),
  AI_POSTER("AI 4K Posters")
}

class MainViewModel(
  private val repository: ChannelRepository = ChannelRepository(),
  private val geminiService: GeminiService = GeminiService()
) : ViewModel() {

  // Navigation state
  private val _currentTab = MutableStateFlow(AppTab.CHANNELS)
  val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

  // Channels state
  val channels: StateFlow<List<Channel>> = repository.channels

  private val _selectedCategory = MutableStateFlow(ChannelCategory.ALL)
  val selectedCategory: StateFlow<ChannelCategory> = _selectedCategory.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _onlyFavorites = MutableStateFlow(false)
  val onlyFavorites: StateFlow<Boolean> = _onlyFavorites.asStateFlow()

  // Player state
  private val _activeChannel = MutableStateFlow<Channel?>(null)
  val activeChannel: StateFlow<Channel?> = _activeChannel.asStateFlow()

  private val _isPlaying = MutableStateFlow(true)
  val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

  private val _isMuted = MutableStateFlow(false)
  val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

  private val _streamQuality = MutableStateFlow(StreamQuality.FHD)
  val streamQuality: StateFlow<StreamQuality> = _streamQuality.asStateFlow()

  private val _isPlayerFullscreen = MutableStateFlow(false)
  val isPlayerFullscreen: StateFlow<Boolean> = _isPlayerFullscreen.asStateFlow()

  private val _showEpgSheet = MutableStateFlow(false)
  val showEpgSheet: StateFlow<Boolean> = _showEpgSheet.asStateFlow()

  private val _showAddStreamDialog = MutableStateFlow(false)
  val showAddStreamDialog: StateFlow<Boolean> = _showAddStreamDialog.asStateFlow()

  // Filtered Channels list
  val filteredChannels: StateFlow<List<Channel>> = combine(
    channels,
    _selectedCategory,
    _searchQuery,
    _onlyFavorites
  ) { list, category, query, favOnly ->
    list.filter { channel ->
      val matchesCategory = (category == ChannelCategory.ALL) || (channel.category == category)
      val matchesQuery = query.isBlank() ||
        channel.name.contains(query, ignoreCase = true) ||
        channel.currentProgram.contains(query, ignoreCase = true) ||
        channel.language.contains(query, ignoreCase = true) ||
        channel.description.contains(query, ignoreCase = true)
      val matchesFav = !favOnly || channel.isFavorite

      matchesCategory && matchesQuery && matchesFav
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // AI Chat State
  val rolePresets = listOf(
    GeminiRolePreset(
      id = "specialist",
      title = "BD IPTV Guide Specialist",
      roleDescription = "Expert on Bangladesh channels, live sports fixtures, drama schedules and stream tips",
      systemInstruction = "You are the official BD IPTV AI Guide specialist for tv.bdiptv.net. You assist Bangladeshi and global users with live TV schedules, cricket/football matches (T Sports, Star Sports, Sony Ten), Bengali drama serials (Zee Bangla, Star Jalsha, NTV), movie recommendations, and IPTV streaming technology. Be friendly, accurate, and format responses with clean markdown and emojis.",
      recommendedModel = "gemini-3.5-flash"
    ),
    GeminiRolePreset(
      id = "sports_expert",
      title = "Live Sports & Cricket Analyst",
      roleDescription = "Specialized in cricket tournaments (BPL, ICC, IPL), Premier League, and match countdowns",
      systemInstruction = "You are a dynamic sports analyst and commentator for BD IPTV. You know all schedules for T Sports, Star Sports, Sony Sports Ten, and Willow TV. Provide match previews, squad news, pitch reports, and live TV channel recommendations.",
      recommendedModel = "gemini-3.1-pro-preview"
    ),
    GeminiRolePreset(
      id = "speed_assistant",
      title = "Fast Channel & EPG Assistant",
      roleDescription = "Quick channel lookup, match timings, and instant TV guide information",
      systemInstruction = "You are a fast, concise TV guide assistant for BD IPTV. Give concise answers regarding channel numbers, show timings, and streaming links without fluff.",
      recommendedModel = "gemini-3.1-flash-lite"
    )
  )

  private val _selectedRole = MutableStateFlow(rolePresets[0])
  val selectedRole: StateFlow<GeminiRolePreset> = _selectedRole.asStateFlow()

  private val _selectedChatModel = MutableStateFlow("gemini-3.5-flash")
  val selectedChatModel: StateFlow<String> = _selectedChatModel.asStateFlow()

  private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
    listOf(
      ChatMessage(
        role = "model",
        text = "Hello! I am your **BD IPTV AI Media Assistant**. Ask me about live cricket on T Sports, Bengali drama serial timings, movie blockbusters, or tv.bdiptv.net channels!",
        modelName = "gemini-3.5-flash"
      )
    )
  )
  val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

  private val _isChatLoading = MutableStateFlow(false)
  val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

  // AI Image Generator State
  val availableImageSizes = listOf("1K", "2K", "4K")
  val availableAspectRatios = listOf("16:9", "9:16", "1:1", "4:3")

  private val _selectedImageSize = MutableStateFlow("4K")
  val selectedImageSize: StateFlow<String> = _selectedImageSize.asStateFlow()

  private val _selectedAspectRatio = MutableStateFlow("16:9")
  val selectedAspectRatio: StateFlow<String> = _selectedAspectRatio.asStateFlow()

  private val _generatedImages = MutableStateFlow<List<GeneratedImageItem>>(
    listOf(
      GeneratedImageItem(
        prompt = "BD IPTV Live Cricket Matchday Stadium at night with glowing neon lights and T Sports Bangladesh banner",
        model = "gemini-3-pro-image-preview",
        resolution = "4K",
        aspectRatio = "16:9"
      ),
      GeneratedImageItem(
        prompt = "Bengali Cyberpunk Cinema Poster for Zee Bangla Sci-Fi Mega Drama in 4K HDR",
        model = "gemini-3-pro-image-preview",
        resolution = "4K",
        aspectRatio = "16:9"
      )
    )
  )
  val generatedImages: StateFlow<List<GeneratedImageItem>> = _generatedImages.asStateFlow()

  private val _isImageGenerating = MutableStateFlow(false)
  val isImageGenerating: StateFlow<Boolean> = _isImageGenerating.asStateFlow()

  private val _selectedImageForModal = MutableStateFlow<GeneratedImageItem?>(null)
  val selectedImageForModal: StateFlow<GeneratedImageItem?> = _selectedImageForModal.asStateFlow()

  init {
    // Select first channel by default
    viewModelScope.launch {
      val list = repository.channels.value
      if (list.isNotEmpty()) {
        _activeChannel.value = list.firstOrNull { it.id == "t_sports" } ?: list[0]
      }
    }
  }

  fun setTab(tab: AppTab) {
    _currentTab.value = tab
  }

  fun setCategory(category: ChannelCategory) {
    _selectedCategory.value = category
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun toggleFavoritesOnly() {
    _onlyFavorites.value = !_onlyFavorites.value
  }

  fun selectChannel(channel: Channel) {
    _activeChannel.value = channel
    _isPlaying.value = true
  }

  fun togglePlayPause() {
    _isPlaying.value = !_isPlaying.value
  }

  fun toggleMute() {
    _isMuted.value = !_isMuted.value
  }

  fun setQuality(quality: StreamQuality) {
    _streamQuality.value = quality
  }

  fun toggleFullscreen() {
    _isPlayerFullscreen.value = !_isPlayerFullscreen.value
  }

  fun toggleFavorite(channelId: String) {
    repository.toggleFavorite(channelId)
    if (_activeChannel.value?.id == channelId) {
      _activeChannel.value = _activeChannel.value?.let { it.copy(isFavorite = !it.isFavorite) }
    }
  }

  fun setEpgSheetVisible(visible: Boolean) {
    _showEpgSheet.value = visible
  }

  fun setAddStreamDialogVisible(visible: Boolean) {
    _showAddStreamDialog.value = visible
  }

  fun addCustomChannel(name: String, url: String, category: ChannelCategory) {
    repository.addCustomChannel(name, url, category)
    _showAddStreamDialog.value = false
  }

  fun getActiveChannelSchedules(): List<ProgramSchedule> {
    val channel = _activeChannel.value ?: return emptyList()
    return repository.getSchedulesForChannel(channel.id)
  }

  // AI Chat Functions
  fun setChatModel(model: String) {
    _selectedChatModel.value = model
  }

  fun setRolePreset(preset: GeminiRolePreset) {
    _selectedRole.value = preset
    _selectedChatModel.value = preset.recommendedModel
  }

  fun sendChatMessage(promptText: String) {
    if (promptText.isBlank() || _isChatLoading.value) return

    val userMessage = ChatMessage(
      role = "user",
      text = promptText,
      modelName = _selectedChatModel.value
    )

    _chatMessages.value = _chatMessages.value + userMessage
    _isChatLoading.value = true

    viewModelScope.launch {
      val history = _chatMessages.value
      val result = geminiService.sendChatMessage(
        history = history,
        newPrompt = promptText,
        modelName = _selectedChatModel.value,
        systemInstruction = _selectedRole.value.systemInstruction
      )

      val modelReply = ChatMessage(
        role = "model",
        text = result.getOrDefault("Sorry, I could not process your request at this moment."),
        modelName = _selectedChatModel.value,
        isError = result.isFailure
      )

      _chatMessages.value = _chatMessages.value + modelReply
      _isChatLoading.value = false
    }
  }

  fun clearChatHistory() {
    _chatMessages.value = listOf(
      ChatMessage(
        role = "model",
        text = "Conversation cleared. How can I assist you with BD IPTV today?",
        modelName = _selectedChatModel.value
      )
    )
  }

  // AI Image Generator Functions
  fun setImageSize(size: String) {
    _selectedImageSize.value = size
  }

  fun setAspectRatio(ratio: String) {
    _selectedAspectRatio.value = ratio
  }

  fun generateImage(promptText: String) {
    if (promptText.isBlank() || _isImageGenerating.value) return

    val newItem = GeneratedImageItem(
      prompt = promptText,
      model = "gemini-3-pro-image-preview",
      resolution = _selectedImageSize.value,
      aspectRatio = _selectedAspectRatio.value,
      isGenerating = true
    )

    _generatedImages.value = listOf(newItem) + _generatedImages.value
    _isImageGenerating.value = true

    viewModelScope.launch {
      val result = geminiService.generateImage(
        prompt = promptText,
        modelName = "gemini-3-pro-image-preview",
        sizeResolution = _selectedImageSize.value,
        aspectRatio = _selectedAspectRatio.value
      )

      val updatedList = _generatedImages.value.map { item ->
        if (item.id == newItem.id) {
          item.copy(
            base64Data = result.getOrNull(),
            isGenerating = false,
            errorMessage = if (result.isFailure) "Failed to generate image" else null
          )
        } else item
      }

      _generatedImages.value = updatedList
      _isImageGenerating.value = false
    }
  }

  fun setSelectedImageForModal(image: GeneratedImageItem?) {
    _selectedImageForModal.value = image
  }
}
