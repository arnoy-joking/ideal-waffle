import { Channel, ProgramSchedule } from '../types';

const SAMPLE_STREAMS = [
  'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4',
  'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4',
  'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4',
  'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4',
  'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4',
  'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4',
  'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4',
  'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4'
];

export const INITIAL_CHANNELS: Channel[] = [
  // Live Sports
  {
    id: 't_sports',
    name: 'T Sports HD',
    category: 'SPORTS',
    logoUrl: 'https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[0],
    resolution: '1080p 60FPS',
    language: 'Bengali / English',
    isLive: true,
    currentProgram: 'Live: BPL / International Cricket Tournament',
    nextProgram: 'Sports Highlights & Pre-Match Analysis',
    description: "Bangladesh's Premier Sports Channel - Live Cricket, Football & Leagues",
    isFavorite: true,
    country: 'Bangladesh'
  },
  {
    id: 'star_sports_1',
    name: 'Star Sports 1 HD',
    category: 'SPORTS',
    logoUrl: 'https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[1],
    resolution: '1080p HD',
    language: 'English / Hindi',
    isLive: true,
    currentProgram: 'Cricket Live Match Countdown',
    nextProgram: 'Premier League Super Sunday',
    description: 'World-class live cricket, football, and international sporting fixtures',
    isFavorite: true,
    country: 'International'
  },
  {
    id: 'sony_ten_1',
    name: 'Sony Sports Ten 1 HD',
    category: 'SPORTS',
    logoUrl: 'https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[2],
    resolution: '1080p HD',
    language: 'English',
    isLive: true,
    currentProgram: 'UEFA Champions League Matchweek',
    nextProgram: 'WWE SmackDown Live',
    description: 'Top international football, tennis grand slams and wrestling',
    country: 'International'
  },
  {
    id: 'willow_tv',
    name: 'Willow Cricket HD',
    category: 'SPORTS',
    logoUrl: 'https://images.unsplash.com/photo-1531415074868-036b107e775a?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[3],
    resolution: '1080p HD',
    language: 'English',
    isLive: true,
    currentProgram: 'ICC World Cup Classic Moments',
    nextProgram: 'Live International Test Match',
    description: '24/7 Dedicated international cricket channel',
    country: 'International'
  },

  // Bangladesh TV & News
  {
    id: 'btv_national',
    name: 'BTV National',
    category: 'BANGLADESH',
    logoUrl: 'https://images.unsplash.com/photo-1594909122845-11baa439b7bf?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[4],
    resolution: '1080p Full HD',
    language: 'Bengali',
    isLive: true,
    currentProgram: 'BTV Shongbad & Desh Gatha',
    nextProgram: 'Kishor Barta Special',
    description: 'Bangladesh Television Official National Live Broadcast',
    country: 'Bangladesh'
  },
  {
    id: 'somoy_tv',
    name: 'Somoy News TV',
    category: 'NEWS',
    logoUrl: 'https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[5],
    resolution: '1080p Full HD',
    language: 'Bengali',
    isLive: true,
    currentProgram: 'Shironam & Special Headline News',
    nextProgram: 'Somoy Bulletin 24/7',
    description: '24/7 Leading Bengali News Channel in Bangladesh',
    isFavorite: true,
    country: 'Bangladesh'
  },
  {
    id: 'jamuna_tv',
    name: 'Jamuna Television',
    category: 'NEWS',
    logoUrl: 'https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[6],
    resolution: '1080p Full HD',
    language: 'Bengali',
    isLive: true,
    currentProgram: 'Investigation 360',
    nextProgram: 'Jamuna Tonight Talkshow',
    description: 'Fastest Breaking News & Investigative Journalism',
    country: 'Bangladesh'
  },
  {
    id: 'channel24',
    name: 'Channel 24 BD',
    category: 'NEWS',
    logoUrl: 'https://images.unsplash.com/photo-1495020689067-958852a7765e?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[7],
    resolution: '1080p HD',
    language: 'Bengali',
    isLive: true,
    currentProgram: 'Desh O Jonogon Live',
    nextProgram: 'Sports 24 Round-up',
    description: 'Modern news channel covering business, politics & sports',
    country: 'Bangladesh'
  },
  {
    id: 'ekattor_tv',
    name: 'Ekattor TV',
    category: 'NEWS',
    logoUrl: 'https://images.unsplash.com/photo-1526470608268-f674ce90ebd4?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[0],
    resolution: '1080p HD',
    language: 'Bengali',
    isLive: true,
    currentProgram: 'Ekattor Shongjog',
    nextProgram: 'Ekattor Journal Live',
    description: 'Bold coverage of current affairs and world news',
    country: 'Bangladesh'
  },

  // Entertainment & Drama
  {
    id: 'zee_bangla',
    name: 'Zee Bangla HD',
    category: 'ENTERTAINMENT',
    logoUrl: 'https://images.unsplash.com/photo-1522869635100-9f4c5e86aa37?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[1],
    resolution: '1080p HD',
    language: 'Bengali',
    isLive: true,
    currentProgram: 'Mithai Mega Drama Episode',
    nextProgram: 'Sa Re Ga Ma Pa Musical Night',
    description: 'Top Bengali television serials, reality shows and mega dramas',
    isFavorite: true,
    country: 'India / Bangladesh'
  },
  {
    id: 'star_jalsha',
    name: 'Star Jalsha HD',
    category: 'ENTERTAINMENT',
    logoUrl: 'https://images.unsplash.com/photo-1518173946687-a4c8a383392e?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[2],
    resolution: '1080p HD',
    language: 'Bengali',
    isLive: true,
    currentProgram: 'Anurager Chhowa Evening Prime',
    nextProgram: 'Super Singer Grand Finale',
    description: 'Popular Bengali serials and emotional dramas',
    country: 'India / Bangladesh'
  },
  {
    id: 'ntv_bd',
    name: 'NTV HD Bangladesh',
    category: 'BANGLADESH',
    logoUrl: 'https://images.unsplash.com/photo-1578022761797-b8636ac1773c?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[3],
    resolution: '1080p HD',
    language: 'Bengali',
    isLive: true,
    currentProgram: 'NTV Eid Natok Special',
    nextProgram: 'Bhinno Shur Music',
    description: 'Pioneering private television channel of Bangladesh',
    country: 'Bangladesh'
  },
  {
    id: 'channel_i',
    name: 'Channel i HD',
    category: 'BANGLADESH',
    logoUrl: 'https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[4],
    resolution: '1080p HD',
    language: 'Bengali',
    isLive: true,
    currentProgram: 'Hridoye Mati O Manush',
    nextProgram: 'Ganer Raja Live',
    description: 'Culturally rich heritage, agricultural news, and music',
    country: 'Bangladesh'
  },

  // Movies & Cinema
  {
    id: 'star_gold',
    name: 'Star Gold HD',
    category: 'MOVIES',
    logoUrl: 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[5],
    resolution: '1080p HD',
    language: 'Hindi / Bengali Audio',
    isLive: true,
    currentProgram: 'Blockbuster Movie: Jawan',
    nextProgram: 'Action Hour: KGF Chapter 2',
    description: 'Mega Bollywood & Pan-India blockbusters 24/7',
    country: 'International'
  },
  {
    id: 'sony_max',
    name: 'Sony MAX HD',
    category: 'MOVIES',
    logoUrl: 'https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[6],
    resolution: '1080p HD',
    language: 'Hindi',
    isLive: true,
    currentProgram: 'De Dana Dan Comedy Night',
    nextProgram: 'Sooryavansham Special',
    description: 'Great movies, unmatched entertainment',
    country: 'International'
  },
  {
    id: 'jalsha_movies',
    name: 'Jalsha Movies HD',
    category: 'MOVIES',
    logoUrl: 'https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[7],
    resolution: '1080p HD',
    language: 'Bengali',
    isLive: true,
    currentProgram: 'Prosenjit Action Classic',
    nextProgram: 'Dev Superhit: Amazon Obhijaan',
    description: 'Non-stop Bengali cinema and blockbuster hits',
    country: 'India / Bangladesh'
  },

  // Kids & Cartoons
  {
    id: 'cartoon_network',
    name: 'Cartoon Network HD',
    category: 'KIDS',
    logoUrl: 'https://images.unsplash.com/photo-1563089145-599997674d42?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[0],
    resolution: '1080p HD',
    language: 'Bengali / English / Hindi',
    isLive: true,
    currentProgram: 'Ben 10 Omniverse Action',
    nextProgram: 'Tom and Jerry Classic Comedy',
    description: 'Beloved animations and non-stop adventure for kids',
    country: 'International'
  },
  {
    id: 'nickelodeon',
    name: 'Nickelodeon India',
    category: 'KIDS',
    logoUrl: 'https://images.unsplash.com/photo-1534447677768-be436bb09401?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[1],
    resolution: '720p HD',
    language: 'Bengali / Hindi',
    isLive: true,
    currentProgram: 'Motu Patlu in Furfuri Nagar',
    nextProgram: 'Ninja Hattori Ninja Technique',
    description: 'Fun, laughter and cartoon adventures',
    country: 'International'
  },

  // Infotainment & Discovery
  {
    id: 'discovery_hd',
    name: 'Discovery Channel HD',
    category: 'INFOTAINMENT',
    logoUrl: 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[2],
    resolution: '1080p Full HD',
    language: 'Bengali / English',
    isLive: true,
    currentProgram: 'Man vs Wild: Deep Jungle Survival',
    nextProgram: 'MythBusters Extreme Science',
    description: 'Satisfying human curiosity through exploration and adventure',
    country: 'International'
  },
  {
    id: 'nat_geo_hd',
    name: 'National Geographic HD',
    category: 'INFOTAINMENT',
    logoUrl: 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[3],
    resolution: '1080p Full HD',
    language: 'Bengali / English',
    isLive: true,
    currentProgram: 'Secrets of the Wild Serengeti',
    nextProgram: 'Megastructures Modern Engineering',
    description: 'Inspiring people to care about the planet',
    country: 'International'
  },

  // Music & Religious
  {
    id: 'sangeet_bangla',
    name: 'Sangeet Bangla HD',
    category: 'MUSIC',
    logoUrl: 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[4],
    resolution: '1080p HD',
    language: 'Bengali',
    isLive: true,
    currentProgram: 'Top 20 Bengali Chartbusters',
    nextProgram: 'Rabindra Sangeet Melodies',
    description: 'The ultimate 24/7 Bengali music station',
    country: 'India / Bangladesh'
  },
  {
    id: 'makkah_live',
    name: 'Makkah Live HD',
    category: 'RELIGIOUS',
    logoUrl: 'https://images.unsplash.com/photo-1564769625905-50e93615e769?w=300&q=80',
    streamUrl: SAMPLE_STREAMS[5],
    resolution: '1080p 60FPS',
    language: 'Arabic / Bengali Subtitles',
    isLive: true,
    currentProgram: 'Holy Haram Tawaf & Live Azan',
    nextProgram: 'Quran Recitation by Imams',
    description: 'Direct 24/7 Live Satellite Feed from Masjid al-Haram, Makkah',
    country: 'Saudi Arabia / Global'
  }
];

export function getProgramSchedules(channelId: string): ProgramSchedule[] {
  return [
    {
      id: 'p1',
      channelId,
      programName: 'Live Prime Segment & Studio Panel',
      startTime: '18:00',
      endTime: '19:30',
      progress: 0.65,
      description: 'Live high-definition streaming coverage with studio analysis and guest panel.',
      genre: 'Live Broadcast'
    },
    {
      id: 'p2',
      channelId,
      programName: 'Prime Time Special & Match Highlights',
      startTime: '19:30',
      endTime: '21:00',
      progress: 0.0,
      description: 'Exclusive evening feature, audience interactions and breaking updates.',
      genre: 'Prime Feature'
    },
    {
      id: 'p3',
      channelId,
      programName: 'Night Movie & Entertainment Showcase',
      startTime: '21:00',
      endTime: '23:30',
      progress: 0.0,
      description: 'Nightly blockbuster cinema showcase with complete surround sound.',
      genre: 'Cinema Showcase'
    }
  ];
}
