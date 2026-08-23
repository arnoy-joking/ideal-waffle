import React from 'react';
import { Channel } from '../types';
import { Play, Star, Radio, Film, Tv, Trophy, Sparkles } from 'lucide-react';

interface ChannelCardProps {
  channel: Channel;
  isSelected: boolean;
  onSelect: (channel: Channel) => void;
  onToggleFavorite: (channelId: string) => void;
}

export const ChannelCard: React.FC<ChannelCardProps> = ({
  channel,
  isSelected,
  onSelect,
  onToggleFavorite
}) => {
  return (
    <div
      onClick={() => onSelect(channel)}
      className={`group relative flex items-center gap-3 p-2.5 sm:p-3 rounded-xl cursor-pointer transition-all duration-200 border select-none ${
        isSelected
          ? 'bg-emerald-950/40 border-[#00E676] shadow-lg shadow-emerald-500/10'
          : 'bg-[#131823] hover:bg-[#1A2232] border-slate-800/80 hover:border-slate-700'
      }`}
    >
      {/* Logo container with overlay play button on hover/select */}
      <div className="relative w-14 h-14 sm:w-16 sm:h-16 rounded-lg overflow-hidden bg-slate-900 flex-shrink-0 border border-slate-800">
        <img
          src={channel.logoUrl}
          alt={channel.name}
          className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
          loading="lazy"
        />
        {isSelected ? (
          <div className="absolute inset-0 bg-emerald-950/70 flex items-center justify-center">
            <span className="w-6 h-6 rounded-full bg-[#00E676] text-black flex items-center justify-center animate-pulse">
              <Play className="w-3.5 h-3.5 fill-black ml-0.5" />
            </span>
          </div>
        ) : (
          <div className="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 flex items-center justify-center transition-opacity">
            <Play className="w-5 h-5 text-white fill-white" />
          </div>
        )}
      </div>

      {/* Channel Meta */}
      <div className="flex-1 min-w-0">
        <div className="flex items-center gap-2 mb-0.5">
          <h3
            className={`font-bold text-sm sm:text-base truncate ${
              isSelected ? 'text-[#00E676]' : 'text-slate-100 group-hover:text-white'
            }`}
          >
            {channel.name}
          </h3>
          <span className="bg-[#FF334B] text-white text-[9px] font-black px-1.5 py-0.2 rounded uppercase">
            LIVE
          </span>
        </div>

        <p className="text-xs text-slate-400 truncate mb-1">
          <span className="text-emerald-400">▶</span> {channel.currentProgram}
        </p>

        <div className="flex items-center gap-2 flex-wrap">
          <span className="text-[10px] font-semibold text-[#00E5FF] bg-cyan-950/50 border border-cyan-500/30 px-1.5 py-0.5 rounded">
            {channel.resolution}
          </span>
          <span className="text-[10px] text-slate-400 bg-slate-800/80 px-1.5 py-0.5 rounded border border-slate-700/50">
            {channel.language}
          </span>
        </div>
      </div>

      {/* Favorite Button */}
      <button
        onClick={(e) => {
          e.stopPropagation();
          onToggleFavorite(channel.id);
        }}
        className="p-2 rounded-lg hover:bg-white/10 text-slate-400 hover:text-white transition-colors"
        title="Favorite"
      >
        <Star
          className={`w-4 h-4 sm:w-5 sm:h-5 ${
            channel.isFavorite ? 'fill-[#FF334B] text-[#FF334B]' : 'text-slate-500'
          }`}
        />
      </button>
    </div>
  );
};
