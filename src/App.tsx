import React, { useState } from 'react';
import {
  Channel,
  ChannelCategory,
  ChatMessage,
  GeminiRolePreset,
  GeneratedImageItem,
  StreamQuality
} from './types';
import { INITIAL_CHANNELS } from './data/channels';
import { sendGeminiChatMessage, generateGeminiImage } from './services/geminiService';
import { ChannelsView } from './components/ChannelsView';
import { WebPortalView } from './components/WebPortalView';
import { AiChatView } from './components/AiChatView';
import { AiImageGenView } from './components/AiImageGenView';
import { CustomStreamModal } from './components/CustomStreamModal';
import { EpgModal } from './components/EpgModal';
import { InfoModal } from './components/InfoModal';
import {
  Tv,
  Globe,
  Bot,
  Sparkles,
  Plus,
  Info,
  Radio,
  Menu,
  X,
  Play,
  Heart,
  Share2
} from 'lucide-react';

const ROLE_PRESETS: GeminiRolePreset[] = [
  {
    id: 'sports_cricket',
    title: '🏏 Cricket & Sports Guide',
    roleDescription: 'Expert on Bangladesh Cricket, BPL, IPL, UEFA & live fixtures',
    systemInstruction:
      'You are the BD IPTV Live Sports & Cricket Concierge. Provide accurate, real-time match schedules, tournament updates for Bangladesh Tigers, BPL, IPL, and international football. Suggest channels like T Sports HD, Star Sports, and Sony Sports Ten.',
    recommendedModel: 'gemini-3.5-flash'
  },
  {
    id: 'drama_natok',
    title: '🎭 Natok & Serials Expert',
    roleDescription: 'Bangla Natok, Zee Bangla, Star Jalsha & Mega Serial Advisor',
    systemInstruction:
      'You are the Bengali Drama & Natok entertainment expert on BD IPTV. Recommend top trending serials on Zee Bangla, Star Jalsha, NTV, Channel i, and Colors Bangla with timings and cast highlights.',
    recommendedModel: 'gemini-3.5-flash'
  },
  {
    id: 'cinema_critic',
    title: '🎬 4K Cinema Critic',
    roleDescription: 'Blockbuster movies, Bollywood & Dhallywood recommendations',
    systemInstruction:
      'You are an elite cinema curator. Recommend action, romance, thrillers, and classics across Star Gold, Sony MAX, and Jalsha Movies. Explain plot hooks concisely without spoilers.',
    recommendedModel: 'gemini-3.1-pro-preview'
  },
  {
    id: 'stream_tech',
    title: '📡 IPTV & Stream Tech',
    roleDescription: 'Help with BDIX, HLS .m3u8 URLs, buffering & quality fixes',
    systemInstruction:
      'You are a technical IPTV stream engineer. Guide users on adding custom M3U/M3U8 URLs, optimizing BDIX routing, and resolving audio/video latency issues.',
    recommendedModel: 'gemini-3.1-flash-lite'
  }
];

export function App() {
  const [activeTab, setActiveTab] = useState<'channels' | 'web-portal' | 'ai-chat' | 'ai-poster'>('channels');
  const [channels, setChannels] = useState<Channel[]>(INITIAL_CHANNELS);
  const [activeChannel, setActiveChannel] = useState<Channel | null>(INITIAL_CHANNELS[0]);
  const [isPlaying, setIsPlaying] = useState(true);
  const [isMuted, setIsMuted] = useState(false);
  const [streamQuality, setStreamQuality] = useState<StreamQuality>('AUTO');

  // Modals
  const [showAddStream, setShowAddStream] = useState(false);
  const [showEpg, setShowEpg] = useState(false);
  const [showInfo, setShowInfo] = useState(false);

  // AI Chat state
  const [chatMessages, setChatMessages] = useState<ChatMessage[]>([
    {
      id: 'welcome',
      role: 'model',
      text:
        '👋 **Welcome to BD IPTV AI Guide!**\n\n' +
        'I am your live entertainment assistant for `tv.bdiptv.net`. Ask me about live cricket fixtures on **T Sports**, Bengali Natok serials on **Zee Bangla & Star Jalsha**, movies on **Star Gold**, or IPTV stream setup!',
      timestamp: Date.now(),
      modelName: 'gemini-3.5-flash'
    }
  ]);
  const [isChatLoading, setIsChatLoading] = useState(false);
  const [selectedChatModel, setSelectedChatModel] = useState('gemini-3.5-flash');
  const [selectedRole, setSelectedRole] = useState<GeminiRolePreset>(ROLE_PRESETS[0]);

  // AI 4K Image Generation state
  const [generatedImages, setGeneratedImages] = useState<GeneratedImageItem[]>([
    {
      id: 'init_1',
      prompt: 'T Sports Cricket Matchday Finals 4K Ultra HD Stadium Poster with Neon Fireworks',
      model: 'gemini-3-pro-image-preview',
      resolution: '4K',
      aspectRatio: '16:9',
      timestamp: Date.now() - 60000
    },
    {
      id: 'init_2',
      prompt: 'BD IPTV Futuristic Neon Streaming Lounge with Holographic Displays',
      model: 'gemini-3-pro-image-preview',
      resolution: '4K',
      aspectRatio: '16:9',
      timestamp: Date.now() - 120000
    }
  ]);
  const [isImageGenerating, setIsImageGenerating] = useState(false);
  const [selectedImageSize, setSelectedImageSize] = useState<'1K' | '2K' | '4K'>('4K');
  const [selectedAspectRatio, setSelectedAspectRatio] = useState<'16:9' | '9:16' | '1:1' | '4:3'>('16:9');

  // Handlers for Channels
  const handleSelectChannel = (channel: Channel) => {
    setActiveChannel(channel);
    setIsPlaying(true);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleToggleFavorite = (channelId: string) => {
    setChannels((prev) =>
      prev.map((c) => (c.id === channelId ? { ...c, isFavorite: !c.isFavorite } : c))
    );
  };

  const handleAddCustomStream = (name: string, url: string, category: ChannelCategory) => {
    const newChan: Channel = {
      id: `custom_${Date.now()}`,
      name,
      category,
      logoUrl: 'https://images.unsplash.com/photo-1593784991095-a205069470b6?w=300&q=80',
      streamUrl: url,
      resolution: '1080p HD',
      language: 'Custom Audio',
      isLive: true,
      currentProgram: 'Custom IPTV Direct Feed',
      nextProgram: 'Continuous Stream',
      description: `Custom live stream: ${url}`,
      isFavorite: true,
      country: 'Custom'
    };

    setChannels((prev) => [newChan, ...prev]);
    setActiveChannel(newChan);
    setIsPlaying(true);
    setActiveTab('channels');
  };

  // Handlers for AI Chat
  const handleSendChatMessage = async (text: string) => {
    const userMsg: ChatMessage = {
      id: `u_${Date.now()}`,
      role: 'user',
      text,
      timestamp: Date.now(),
      modelName: selectedChatModel
    };

    const newHistory = [...chatMessages, userMsg];
    setChatMessages(newHistory);
    setIsChatLoading(true);

    try {
      const reply = await sendGeminiChatMessage(
        newHistory,
        text,
        selectedChatModel,
        selectedRole.systemInstruction
      );

      const botMsg: ChatMessage = {
        id: `m_${Date.now()}`,
        role: 'model',
        text: reply,
        timestamp: Date.now(),
        modelName: selectedChatModel
      };

      setChatMessages((prev) => [...prev, botMsg]);
    } catch (err) {
      console.error(err);
    } finally {
      setIsChatLoading(false);
    }
  };

  const handleClearChat = () => {
    setChatMessages([
      {
        id: `welcome_${Date.now()}`,
        role: 'model',
        text: 'Chat history cleared. How can I help you with BD IPTV streams today?',
        timestamp: Date.now(),
        modelName: selectedChatModel
      }
    ]);
  };

  // Handlers for Image Gen
  const handleGenerateImage = async (prompt: string) => {
    setIsImageGenerating(true);
    const newImageItem: GeneratedImageItem = {
      id: `img_${Date.now()}`,
      prompt,
      model: 'gemini-3-pro-image-preview',
      resolution: selectedImageSize,
      aspectRatio: selectedAspectRatio,
      timestamp: Date.now(),
      isGenerating: true
    };

    setGeneratedImages((prev) => [newImageItem, ...prev]);

    try {
      const resultData = await generateGeminiImage(
        prompt,
        'gemini-3-pro-image-preview',
        selectedImageSize,
        selectedAspectRatio
      );

      setGeneratedImages((prev) =>
        prev.map((img) =>
          img.id === newImageItem.id
            ? { ...img, base64Data: resultData, isGenerating: false }
            : img
        )
      );
    } catch (err: any) {
      setGeneratedImages((prev) =>
        prev.map((img) =>
          img.id === newImageItem.id
            ? { ...img, isGenerating: false, errorMessage: err?.message || 'Failed' }
            : img
        )
      );
    } finally {
      setIsImageGenerating(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#0A0D14] text-slate-100 flex flex-col">
      {/* Top Header */}
      <header className="sticky top-0 z-40 bg-[#131823]/95 backdrop-blur-md border-b border-slate-800">
        <div className="max-w-7xl mx-auto px-3 sm:px-6 h-16 flex items-center justify-between gap-3">
          {/* Brand Logo & Name */}
          <div
            onClick={() => setActiveTab('channels')}
            className="flex items-center gap-2.5 cursor-pointer select-none group"
          >
            <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-[#00B050] to-[#00E676] text-black flex items-center justify-center shadow-lg shadow-emerald-500/20 group-hover:scale-105 transition-transform">
              <Tv className="w-5 h-5" />
            </div>
            <div>
              <div className="flex items-center gap-2">
                <span className="font-extrabold text-lg sm:text-xl tracking-tight text-white group-hover:text-[#00E676] transition-colors">
                  BD IPTV
                </span>
                <span className="bg-[#FF334B] text-white text-[10px] font-black px-1.5 py-0.5 rounded shadow-sm">
                  BDIX LIVE
                </span>
              </div>
              <p className="text-[11px] text-slate-400 font-medium">tv.bdiptv.net Stream Suite</p>
            </div>
          </div>

          {/* Desktop Navigation Tabs */}
          <nav className="hidden md:flex items-center gap-1 bg-[#0A0D14] p-1 rounded-xl border border-slate-800">
            <button
              onClick={() => setActiveTab('channels')}
              className={`flex items-center gap-2 px-3.5 py-2 rounded-lg text-xs font-bold transition-all ${
                activeTab === 'channels'
                  ? 'bg-[#00E676] text-black shadow-md shadow-emerald-500/20'
                  : 'text-slate-300 hover:text-white hover:bg-slate-800/60'
              }`}
            >
              <Tv className="w-4 h-4" />
              <span>Live TV Channels</span>
            </button>

            <button
              onClick={() => setActiveTab('web-portal')}
              className={`flex items-center gap-2 px-3.5 py-2 rounded-lg text-xs font-bold transition-all ${
                activeTab === 'web-portal'
                  ? 'bg-[#00E5FF] text-black shadow-md shadow-cyan-500/20'
                  : 'text-slate-300 hover:text-white hover:bg-slate-800/60'
              }`}
            >
              <Globe className="w-4 h-4" />
              <span>tv.bdiptv.net Portal</span>
            </button>

            <button
              onClick={() => setActiveTab('ai-chat')}
              className={`flex items-center gap-2 px-3.5 py-2 rounded-lg text-xs font-bold transition-all ${
                activeTab === 'ai-chat'
                  ? 'bg-[#00E676] text-black shadow-md shadow-emerald-500/20'
                  : 'text-slate-300 hover:text-white hover:bg-slate-800/60'
              }`}
            >
              <Bot className="w-4 h-4" />
              <span>Gemini AI Guide</span>
            </button>

            <button
              onClick={() => setActiveTab('ai-poster')}
              className={`flex items-center gap-2 px-3.5 py-2 rounded-lg text-xs font-bold transition-all ${
                activeTab === 'ai-poster'
                  ? 'bg-[#FFB800] text-black shadow-md shadow-amber-500/20'
                  : 'text-slate-300 hover:text-white hover:bg-slate-800/60'
              }`}
            >
              <Sparkles className="w-4 h-4" />
              <span>4K Posters</span>
            </button>
          </nav>

          {/* Action Buttons */}
          <div className="flex items-center gap-2">
            <button
              onClick={() => setShowAddStream(true)}
              className="flex items-center gap-1.5 px-3 py-2 rounded-xl bg-[#00E676]/10 hover:bg-[#00E676]/20 text-[#00E676] border border-emerald-500/30 text-xs font-bold transition-all select-none"
              title="Add Custom IPTV URL"
            >
              <Plus className="w-4 h-4" />
              <span className="hidden sm:inline">Add Stream</span>
            </button>

            <button
              onClick={() => setShowInfo(true)}
              className="p-2 rounded-xl bg-[#0A0D14] hover:bg-slate-800 text-slate-400 hover:text-white border border-slate-800 transition-colors"
              title="About BD IPTV"
            >
              <Info className="w-4 h-4" />
            </button>
          </div>
        </div>
      </header>

      {/* Main Content Area */}
      <main className="flex-1 max-w-7xl w-full mx-auto p-3 sm:p-6">
        {activeTab === 'channels' && (
          <ChannelsView
            channels={channels}
            activeChannel={activeChannel}
            isPlaying={isPlaying}
            isMuted={isMuted}
            streamQuality={streamQuality}
            onSelectChannel={handleSelectChannel}
            onTogglePlay={() => setIsPlaying(!isPlaying)}
            onToggleMute={() => setIsMuted(!isMuted)}
            onQualityChange={setStreamQuality}
            onToggleFavorite={handleToggleFavorite}
            onOpenEpg={() => setShowEpg(true)}
            onOpenAddStream={() => setShowAddStream(true)}
          />
        )}

        {activeTab === 'web-portal' && (
          <WebPortalView
            onQuickPlay={(chanId) => {
              const target = channels.find((c) => c.id === chanId);
              if (target) {
                handleSelectChannel(target);
                setActiveTab('channels');
              }
            }}
          />
        )}

        {activeTab === 'ai-chat' && (
          <AiChatView
            messages={chatMessages}
            isLoading={isChatLoading}
            selectedModel={selectedChatModel}
            selectedRole={selectedRole}
            rolePresets={ROLE_PRESETS}
            onSendMessage={handleSendChatMessage}
            onClearChat={handleClearChat}
            onSelectModel={setSelectedChatModel}
            onSelectRole={(r) => {
              setSelectedRole(r);
              setSelectedChatModel(r.recommendedModel);
            }}
          />
        )}

        {activeTab === 'ai-poster' && (
          <AiImageGenView
            images={generatedImages}
            isGenerating={isImageGenerating}
            selectedSize={selectedImageSize}
            selectedAspectRatio={selectedAspectRatio}
            onGenerate={handleGenerateImage}
            onSetSize={setSelectedImageSize}
            onSetAspectRatio={setSelectedAspectRatio}
          />
        )}
      </main>

      {/* Mobile Bottom Navigation Bar */}
      <div className="md:hidden fixed bottom-0 left-0 right-0 z-40 bg-[#131823]/95 backdrop-blur-md border-t border-slate-800 px-3 py-2 flex items-center justify-around">
        <button
          onClick={() => setActiveTab('channels')}
          className={`flex flex-col items-center gap-1 text-[10px] font-bold ${
            activeTab === 'channels' ? 'text-[#00E676]' : 'text-slate-400'
          }`}
        >
          <Tv className="w-5 h-5" />
          <span>Live TV</span>
        </button>

        <button
          onClick={() => setActiveTab('web-portal')}
          className={`flex flex-col items-center gap-1 text-[10px] font-bold ${
            activeTab === 'web-portal' ? 'text-[#00E5FF]' : 'text-slate-400'
          }`}
        >
          <Globe className="w-5 h-5" />
          <span>Portal</span>
        </button>

        <button
          onClick={() => setActiveTab('ai-chat')}
          className={`flex flex-col items-center gap-1 text-[10px] font-bold ${
            activeTab === 'ai-chat' ? 'text-[#00E676]' : 'text-slate-400'
          }`}
        >
          <Bot className="w-5 h-5" />
          <span>AI Guide</span>
        </button>

        <button
          onClick={() => setActiveTab('ai-poster')}
          className={`flex flex-col items-center gap-1 text-[10px] font-bold ${
            activeTab === 'ai-poster' ? 'text-[#FFB800]' : 'text-slate-400'
          }`}
        >
          <Sparkles className="w-5 h-5" />
          <span>4K Posters</span>
        </button>
      </div>

      {/* Modals */}
      {showAddStream && (
        <CustomStreamModal
          onClose={() => setShowAddStream(false)}
          onAddStream={handleAddCustomStream}
        />
      )}

      {showEpg && activeChannel && (
        <EpgModal channel={activeChannel} onClose={() => setShowEpg(false)} />
      )}

      {showInfo && <InfoModal onClose={() => setShowInfo(false)} />}
    </div>
  );
}
export default App;
