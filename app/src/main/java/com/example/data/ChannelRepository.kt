package com.example.data

import com.example.data.model.Channel
import com.example.data.model.ChannelCategory
import com.example.data.model.ProgramSchedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChannelRepository {

  private val sampleStreams = listOf(
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"
  )

  private val initialChannels: List<Channel> = listOf(
    // Bangladesh News & National
    Channel(
      id = "btv_national",
      name = "BTV National",
      category = ChannelCategory.BANGLADESH,
      logoUrl = "https://images.unsplash.com/photo-1594909122845-11baa439b7bf?w=200&q=80",
      streamUrl = sampleStreams[0],
      resolution = "1080p Full HD",
      language = "Bengali",
      currentProgram = "BTV Shongbad & Desh Gatha",
      nextProgram = "Kishor Barta Special",
      description = "Bangladesh Television Official National Live Broadcast"
    ),
    Channel(
      id = "somoy_tv",
      name = "Somoy News TV",
      category = ChannelCategory.NEWS,
      logoUrl = "https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=200&q=80",
      streamUrl = sampleStreams[1],
      resolution = "1080p Full HD",
      language = "Bengali",
      currentProgram = "Shironam & Special Headline News",
      nextProgram = "Somoy Bulletin 24/7",
      description = "24/7 Leading Bengali News Channel in Bangladesh"
    ),
    Channel(
      id = "jamuna_tv",
      name = "Jamuna Television",
      category = ChannelCategory.NEWS,
      logoUrl = "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=200&q=80",
      streamUrl = sampleStreams[2],
      resolution = "1080p Full HD",
      language = "Bengali",
      currentProgram = "Investigation 360",
      nextProgram = "Jamuna Tonight Talkshow",
      description = "Fastest Breaking News & Investigative Journalism"
    ),
    Channel(
      id = "channel24",
      name = "Channel 24 BD",
      category = ChannelCategory.NEWS,
      logoUrl = "https://images.unsplash.com/photo-1495020689067-958852a7765e?w=200&q=80",
      streamUrl = sampleStreams[3],
      resolution = "1080p HD",
      language = "Bengali",
      currentProgram = "Desh O Jonogon Live",
      nextProgram = "Sports 24 Round-up",
      description = "Modern news channel covering business, politics & sports"
    ),
    Channel(
      id = "ekattor_tv",
      name = "Ekattor TV",
      category = ChannelCategory.NEWS,
      logoUrl = "https://images.unsplash.com/photo-1526470608268-f674ce90ebd4?w=200&q=80",
      streamUrl = sampleStreams[4],
      resolution = "1080p HD",
      language = "Bengali",
      currentProgram = "Ekattor Shongjog",
      nextProgram = "Ekattor Journal Live",
      description = "Bold coverage of current affairs and world news"
    ),

    // Live Sports
    Channel(
      id = "t_sports",
      name = "T Sports HD",
      category = ChannelCategory.SPORTS,
      logoUrl = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=200&q=80",
      streamUrl = sampleStreams[5],
      resolution = "1080p 60FPS",
      language = "Bengali / English",
      currentProgram = "Live: BPL / International Cricket Tournament",
      nextProgram = "Sports Highlights & Pre-Match Analysis",
      description = "Bangladesh's Premier Sports Channel - Live Cricket, Football & Leagues",
      isFavorite = true
    ),
    Channel(
      id = "star_sports_1",
      name = "Star Sports 1 HD",
      category = ChannelCategory.SPORTS,
      logoUrl = "https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?w=200&q=80",
      streamUrl = sampleStreams[6],
      resolution = "1080p HD",
      language = "English / Hindi",
      currentProgram = "Cricket Live Match Countdown",
      nextProgram = "Premier League Super Sunday",
      description = "World-class live cricket, football, and international sporting fixtures",
      isFavorite = true
    ),
    Channel(
      id = "sony_ten_1",
      name = "Sony Sports Ten 1 HD",
      category = ChannelCategory.SPORTS,
      logoUrl = "https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=200&q=80",
      streamUrl = sampleStreams[7],
      resolution = "1080p HD",
      language = "English",
      currentProgram = "UEFA Champions League Matchweek",
      nextProgram = "WWE SmackDown Live",
      description = "Top international football, tennis grand slams and wrestling"
    ),
    Channel(
      id = "willow_tv",
      name = "Willow Cricket HD",
      category = ChannelCategory.SPORTS,
      logoUrl = "https://images.unsplash.com/photo-1531415074868-036b107e775a?w=200&q=80",
      streamUrl = sampleStreams[0],
      resolution = "1080p HD",
      language = "English",
      currentProgram = "ICC World Cup Classic Moments",
      nextProgram = "Live International Test Match",
      description = "24/7 Dedicated international cricket channel"
    ),

    // Entertainment & Drama
    Channel(
      id = "zee_bangla",
      name = "Zee Bangla HD",
      category = ChannelCategory.ENTERTAINMENT,
      logoUrl = "https://images.unsplash.com/photo-1522869635100-9f4c5e86aa37?w=200&q=80",
      streamUrl = sampleStreams[1],
      resolution = "1080p HD",
      language = "Bengali",
      currentProgram = "Mithai Mega Drama Episode",
      nextProgram = "Sa Re Ga Ma Pa Musical Night",
      description = "Top Bengali television serials, reality shows and mega dramas",
      isFavorite = true
    ),
    Channel(
      id = "star_jalsha",
      name = "Star Jalsha HD",
      category = ChannelCategory.ENTERTAINMENT,
      logoUrl = "https://images.unsplash.com/photo-1518173946687-a4c8a383392e?w=200&q=80",
      streamUrl = sampleStreams[2],
      resolution = "1080p HD",
      language = "Bengali",
      currentProgram = "Anurager Chhowa Evening Prime",
      nextProgram = "Super Singer Grand Finale",
      description = "Popular Bengali serials and emotional dramas"
    ),
    Channel(
      id = "colors_bangla",
      name = "Colors Bangla HD",
      category = ChannelCategory.ENTERTAINMENT,
      logoUrl = "https://images.unsplash.com/photo-1598899134739-24c46f58b8c0?w=200&q=80",
      streamUrl = sampleStreams[3],
      resolution = "1080p HD",
      language = "Bengali",
      currentProgram = "Dutta & Bouma Family Special",
      nextProgram = "Bangla Golden Hits",
      description = "Vibrant Bengali serials and reality entertainment"
    ),
    Channel(
      id = "ntv_bd",
      name = "NTV HD Bangladesh",
      category = ChannelCategory.BANGLADESH,
      logoUrl = "https://images.unsplash.com/photo-1578022761797-b8636ac1773c?w=200&q=80",
      streamUrl = sampleStreams[4],
      resolution = "1080p HD",
      language = "Bengali",
      currentProgram = "NTV Eid Natok Special",
      nextProgram = "Bhinno Shur Music",
      description = "Pioneering private television channel of Bangladesh"
    ),
    Channel(
      id = "channel_i",
      name = "Channel i HD",
      category = ChannelCategory.BANGLADESH,
      logoUrl = "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=200&q=80",
      streamUrl = sampleStreams[5],
      resolution = "1080p HD",
      language = "Bengali",
      currentProgram = "Hridoye Mati O Manush",
      nextProgram = "Ganer Raja Live",
      description = "Culturally rich heritage, agricultural news, and music"
    ),

    // Movies & Cinema
    Channel(
      id = "star_gold",
      name = "Star Gold HD",
      category = ChannelCategory.MOVIES,
      logoUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=200&q=80",
      streamUrl = sampleStreams[6],
      resolution = "1080p HD",
      language = "Hindi / Bengali Audio",
      currentProgram = "Blockbuster Movie: Jawan",
      nextProgram = "Action Hour: KGF Chapter 2",
      description = "Mega Bollywood & Pan-India blockbusters 24/7"
    ),
    Channel(
      id = "sony_max",
      name = "Sony MAX HD",
      category = ChannelCategory.MOVIES,
      logoUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=200&q=80",
      streamUrl = sampleStreams[7],
      resolution = "1080p HD",
      language = "Hindi",
      currentProgram = "De Dana Dan Comedy Night",
      nextProgram = "Sooryavansham Special",
      description = "Great movies, unmatched entertainment"
    ),
    Channel(
      id = "jalsha_movies",
      name = "Jalsha Movies HD",
      category = ChannelCategory.MOVIES,
      logoUrl = "https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=200&q=80",
      streamUrl = sampleStreams[0],
      resolution = "1080p HD",
      language = "Bengali",
      currentProgram = "Prosenjit Action Classic",
      nextProgram = "Dev Superhit: Amazon Obhijaan",
      description = "Non-stop Bengali cinema and blockbuster hits"
    ),

    // Kids & Cartoons
    Channel(
      id = "cartoon_network",
      name = "Cartoon Network HD",
      category = ChannelCategory.KIDS,
      logoUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=200&q=80",
      streamUrl = sampleStreams[1],
      resolution = "1080p HD",
      language = "Bengali / English / Hindi",
      currentProgram = "Ben 10 Omniverse Action",
      nextProgram = "Tom and Jerry Classic Comedy",
      description = "Beloved animations and non-stop adventure for kids"
    ),
    Channel(
      id = "nickelodeon",
      name = "Nickelodeon India",
      category = ChannelCategory.KIDS,
      logoUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=200&q=80",
      streamUrl = sampleStreams[2],
      resolution = "720p HD",
      language = "Bengali / Hindi",
      currentProgram = "Motu Patlu in Furfuri Nagar",
      nextProgram = "Ninja Hattori Ninja Technique",
      description = "Fun, laughter and cartoon adventures"
    ),

    // Infotainment & Docs
    Channel(
      id = "discovery_hd",
      name = "Discovery Channel HD",
      category = ChannelCategory.INFOTAINMENT,
      logoUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=200&q=80",
      streamUrl = sampleStreams[3],
      resolution = "1080p Full HD",
      language = "Bengali / English",
      currentProgram = "Man vs Wild: Deep Jungle Survival",
      nextProgram = "MythBusters Extreme Science",
      description = "Satisfying human curiosity through exploration and adventure"
    ),
    Channel(
      id = "nat_geo_hd",
      name = "National Geographic HD",
      category = ChannelCategory.INFOTAINMENT,
      logoUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=200&q=80",
      streamUrl = sampleStreams[4],
      resolution = "1080p Full HD",
      language = "Bengali / English",
      currentProgram = "Secrets of the Wild Serengeti",
      nextProgram = "Megastructures Modern Engineering",
      description = "Inspiring people to care about the planet"
    ),

    // Music & Radio
    Channel(
      id = "sangeet_bangla",
      name = "Sangeet Bangla HD",
      category = ChannelCategory.MUSIC,
      logoUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=200&q=80",
      streamUrl = sampleStreams[5],
      resolution = "1080p HD",
      language = "Bengali",
      currentProgram = "Top 20 Bengali Chartbusters",
      nextProgram = "Rabindra Sangeet Melodies",
      description = "The ultimate 24/7 Bengali music station"
    ),
    Channel(
      id = "9xm",
      name = "9XM Music Live",
      category = ChannelCategory.MUSIC,
      logoUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=200&q=80",
      streamUrl = sampleStreams[6],
      resolution = "1080p HD",
      language = "Hindi / Pop",
      currentProgram = "Smash Hits with Bade Chote",
      nextProgram = "Party Blast Midnight",
      description = "High energy music videos and animated fun"
    ),

    // Religious
    Channel(
      id = "makkah_live",
      name = "Makkah Live HD",
      category = ChannelCategory.RELIGIOUS,
      logoUrl = "https://images.unsplash.com/photo-1564769625905-50e93615e769?w=200&q=80",
      streamUrl = sampleStreams[7],
      resolution = "1080p 60FPS",
      language = "Arabic / Bengali Subtitles",
      currentProgram = "Holy Haram Tawaf & Live Azan",
      nextProgram = "Quran Recitation by Imams",
      description = "Direct 24/7 Live Satellite Feed from Masjid al-Haram, Makkah"
    ),
    Channel(
      id = "peace_tv_bangla",
      name = "Peace TV Bangla",
      category = ChannelCategory.RELIGIOUS,
      logoUrl = "https://images.unsplash.com/photo-1584551246679-0daf3d275d0f?w=200&q=80",
      streamUrl = sampleStreams[0],
      resolution = "720p HD",
      language = "Bengali",
      currentProgram = "Islamic Q&A Sessions",
      nextProgram = "Tafseer of Surah Al-Baqarah",
      description = "Authentic Islamic lectures and moral guidance in Bengali"
    )
  )

  private val _channels = MutableStateFlow(initialChannels)
  val channels: StateFlow<List<Channel>> = _channels.asStateFlow()

  fun toggleFavorite(channelId: String) {
    _channels.value = _channels.value.map { channel ->
      if (channel.id == channelId) channel.copy(isFavorite = !channel.isFavorite) else channel
    }
  }

  fun addCustomChannel(name: String, streamUrl: String, category: ChannelCategory, resolution: String = "1080p HD") {
    val newChannel = Channel(
      id = "custom_${System.currentTimeMillis()}",
      name = name.ifBlank { "Custom IPTV Channel" },
      category = category,
      logoUrl = "https://images.unsplash.com/photo-1594909122845-11baa439b7bf?w=200&q=80",
      streamUrl = streamUrl,
      resolution = resolution,
      language = "Bengali / Multi",
      currentProgram = "Custom Stream Playback",
      nextProgram = "Continuous Stream",
      description = "User added stream: $streamUrl",
      isFavorite = true
    )
    _channels.value = listOf(newChannel) + _channels.value
  }

  fun getSchedulesForChannel(channelId: String): List<ProgramSchedule> {
    return listOf(
      ProgramSchedule(
        id = "prog_1",
        channelId = channelId,
        programName = "Live Main Broadcast & Prime Segment",
        startTime = "18:00",
        endTime = "19:30",
        progress = 0.65f,
        description = "Live high-definition streaming coverage with studio analysis and guest panel.",
        genre = "Live Broadcast"
      ),
      ProgramSchedule(
        id = "prog_2",
        channelId = channelId,
        programName = "Prime Time Special & Match Highlights",
        startTime = "19:30",
        endTime = "21:00",
        progress = 0.0f,
        description = "Exclusive evening feature, audience interactions and breaking updates.",
        genre = "Prime Feature"
      ),
      ProgramSchedule(
        id = "prog_3",
        channelId = channelId,
        programName = "Night Movie & Entertainment Showcase",
        startTime = "21:00",
        endTime = "23:30",
        progress = 0.0f,
        description = "Nightly blockbuster cinema showcase with complete surround sound.",
        genre = "Cinema Showcase"
      )
    )
  }
}
