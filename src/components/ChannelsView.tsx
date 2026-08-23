import React, { useMemo, useState } from 'react';
import { Channel, ChannelCategory, StreamQuality } from '../types';
import { ChannelCard } from './ChannelCard';
import { VideoPlayer } from './VideoPlayer';
import {
  Search,
  Star,
  Plus,
  Tv,
  Film,
  Trophy,
  Flame,
  Radio,
  Sparkles,
  Zap,
  Globe,
  X,
  SlidersHorizontal,
  Compass
} from 'lucide-react';

interface ChannelsViewProps {
  channels: Channel[];
  activeChannel: Channel | null;
  isPlaying: boolean;
  isMuted: boolean;
  streamQuality: StreamQuality;
  onSelectChannel: (channel: Channel) => void;
  onTogglePlay: () => void;
  onToggleMute: () => void;
  onQualityChange: (quality: StreamQuality) => void;
  onToggleFavorite: (channelId: string) => void;
  onOpenEpg: () => void;
  onOpenAddStream: () => void;
}

export const ChannelsView: React.FC<ChannelsViewProps> = ({
  channels,
  activeChannel,
  isPlaying,
  isMuted,
  streamQuality,
  onSelectChannel,
  onTogglePlay,
  onToggleMute,
  onQualityChange,
  onToggleFavorite,
  onOpenEpg,
  onOpenAddStream
}) => {
  const [selectedCategory, setSelectedCategory] = useState<ChannelCategory>('ALL');
  const [searchQuery, setSearchQuery] = useState('');
  const [onlyFavorites, setOnlyFavorites] = useState(false);

  const categories: { id: ChannelCategory; label: string; icon: any }[] = [
    { id: 'ALL', label: 'All Channels', icon: Tv },
    { id: 'BANGLADESH', label: 'Bangladesh TV', icon: Globe },
    { id: 'SPORTS', label: 'Live Sports', icon: Trophy },
    { id: 'ENTERTAINMENT', label: 'Drama & Serials', icon: Film },
    { id: 'MOVIES', label: 'Movies & Cinema', icon: Film },
    { id: 'NEWS', label: 'News 24/7', icon: Flame },
    { id: 'KIDS', label: 'Kids & Cartoons', icon: Sparkles },
    { id: 'INFOTAINMENT', label: 'Discovery & Docs', icon: Compass },
    { id: 'MUSIC', label: 'Music & Radio', icon: Radio },
    { id: 'RELIGIOUS', label: 'Religious', icon: Globe }
  ];

  const filteredChannels = useMemo(() => {
    return channels.filter((channel) => {
      const matchesCategory = selectedCategory === 'ALL' || channel.category === selectedCategory;
      const matchesSearch =
        searchQuery.trim() === '' ||
        channel.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        channel.currentProgram.toLowerCase().includes(searchQuery.toLowerCase()) ||
        channel.language.toLowerCase().includes(searchQuery.toLowerCase()) ||
        channel.description.toLowerCase().includes(searchQuery.toLowerCase());
      const matchesFav = !onlyFavorites || channel.isFavorite;

      return matchesCategory && matchesSearch && matchesFav;
    });
  }, [channels, selectedCategory, searchQuery, onlyFavorites]);

  return (
    <div className="flex flex-col gap-4 pb-20">
      {/* Active Streaming Video Player */}
      {activeChannel && (
        <div className="w-full">
          <VideoPlayer
            channel={activeChannel}
            isPlaying={isPlaying}
            isMuted={isMuted}
            streamQuality={streamQuality}
            onTogglePlay={onTogglePlay}
            onToggleMute={onToggleMute}
            onQualityChange={onQualityChange}
            onToggleFavorite={onToggleFavorite}
            onOpenEpg={onOpenEpg}
          />
        </div>
      )}

      {/* Search & Category Filter Section */}
      <div className="bg-[#131823] p-3 sm:p-4 rounded-xl border border-slate-800/80 flex flex-col gap-3 shadow-lg">
        <div className="flex items-center gap-2 sm:gap-3">
          <div className="relative flex-1">
            <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-emerald-400" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search 30+ BD & Global live channels, matches, movies..."
              className="w-full bg-[#0A0D14] border border-slate-700/60 rounded-xl pl-10 pr-9 py-2.5 text-sm text-slate-100 placeholder-slate-500 focus:outline-none focus:border-[#00E676] focus:ring-1 focus:ring-[#00E676] transition-all"
            />
            {searchQuery && (
              <button
                onClick={() => setSearchQuery('')}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-white"
              >
                <X className="w-4 h-4" />
              </button>
            )}
          </div>

          <button
            onClick={() => setOnlyFavorites(!onlyFavorites)}
            className={`flex items-center gap-1.5 px-3 py-2.5 rounded-xl border text-xs sm:text-sm font-bold transition-colors select-none ${
              onlyFavorites
                ? 'bg-rose-950/60 border-rose-500 text-rose-400'
                : 'bg-[#0A0D14] border-slate-700/60 text-slate-300 hover:border-slate-600'
            }`}
            title="Show only favorited channels"
          >
            <Star className={`w-4 h-4 ${onlyFavorites ? 'fill-rose-500 text-rose-500' : 'text-slate-400'}`} />
            <span className="hidden xs:inline">Favs</span>
          </button>

          <button
            onClick={onOpenAddStream}
            className="flex items-center gap-1.5 px-3.5 py-2.5 rounded-xl bg-[#00E676] hover:bg-[#00B050] text-black text-xs sm:text-sm font-bold shadow-md shadow-emerald-500/20 transition-all select-none"
            title="Add Custom IPTV URL"
          >
            <Plus className="w-4 h-4" />
            <span className="hidden sm:inline">Add Stream</span>
          </button>
        </div>

        {/* Scrollable Category Pills */}
        <div className="flex items-center gap-1.5 overflow-x-auto pb-1 no-scrollbar select-none">
          {categories.map((cat) => {
            const IconComponent = cat.icon;
            const isSelected = selectedCategory === cat.id;
            return (
              <button
                key={cat.id}
                onClick={() => setSelectedCategory(cat.id)}
                className={`flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-semibold whitespace-nowrap transition-all border ${
                  isSelected
                    ? 'bg-[#00E676] text-black border-[#00E676] font-bold shadow-md shadow-emerald-500/20'
                    : 'bg-[#0A0D14] text-slate-300 hover:text-white border-slate-800 hover:border-slate-700'
                }`}
              >
                <IconComponent className={`w-3.5 h-3.5 ${isSelected ? 'text-black' : 'text-slate-400'}`} />
                <span>{cat.label}</span>
              </button>
            );
          })}
        </div>
      </div>

      {/* Channel Grid / List */}
      <div>
        <div className="flex items-center justify-between px-1 mb-2">
          <span className="text-xs font-bold text-[#00E5FF] tracking-wider uppercase flex items-center gap-1.5">
            <Zap className="w-3.5 h-3.5 text-emerald-400" />
            {filteredChannels.length} Live Channels Available
          </span>
          <span className="text-xs text-[#FFB800] font-semibold">
            ⚡ Ultra Low Latency BDIX
          </span>
        </div>

        {filteredChannels.length === 0 ? (
          <div className="text-center py-16 bg-[#131823] rounded-xl border border-slate-800 p-6">
            <Tv className="w-12 h-12 text-slate-600 mx-auto mb-3" />
            <h3 className="text-base font-bold text-slate-300">No channels found</h3>
            <p className="text-xs text-slate-500 mt-1 mb-4">
              Try adjusting your search query or switching to All Channels.
            </p>
            <button
              onClick={() => {
                setSelectedCategory('ALL');
                setSearchQuery('');
                setOnlyFavorites(false);
              }}
              className="px-4 py-2 rounded-lg bg-[#00E676] text-black text-xs font-bold"
            >
              Reset Filters
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2.5 sm:gap-3">
            {filteredChannels.map((channel) => (
              <ChannelCard
                key={channel.id}
                channel={channel}
                isSelected={activeChannel?.id === channel.id}
                onSelect={onSelectChannel}
                onToggleFavorite={onToggleFavorite}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
