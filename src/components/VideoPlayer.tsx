import React, { useEffect, useRef, useState } from 'react';
import Hls from 'hls.js';
import { Channel, StreamQuality } from '../types';
import {
  Play,
  Pause,
  Volume2,
  VolumeX,
  Maximize,
  Minimize,
  Radio,
  Star,
  Settings,
  Calendar,
  Sparkles,
  Zap,
  Check
} from 'lucide-react';

interface VideoPlayerProps {
  channel: Channel;
  isPlaying: boolean;
  isMuted: boolean;
  streamQuality: StreamQuality;
  onTogglePlay: () => void;
  onToggleMute: () => void;
  onQualityChange: (quality: StreamQuality) => void;
  onToggleFavorite: (channelId: string) => void;
  onOpenEpg: () => void;
}

export const VideoPlayer: React.FC<VideoPlayerProps> = ({
  channel,
  isPlaying,
  isMuted,
  streamQuality,
  onTogglePlay,
  onToggleMute,
  onQualityChange,
  onToggleFavorite,
  onOpenEpg
}) => {
  const videoRef = useRef<HTMLVideoElement>(null);
  const containerRef = useRef<HTMLDivElement>(null);
  const [isFullscreen, setIsFullscreen] = useState(false);
  const [showControls, setShowControls] = useState(true);
  const [showQualityMenu, setShowQualityMenu] = useState(false);
  const [hasError, setHasError] = useState(false);
  const controlsTimeoutRef = useRef<any>(null);

  // Setup Hls.js or HTML5 native video playback
  useEffect(() => {
    const video = videoRef.current;
    if (!video) return;

    setHasError(false);
    let hls: Hls | null = null;

    if (channel.streamUrl.endsWith('.m3u8')) {
      if (Hls.isSupported()) {
        hls = new Hls({
          enableWorker: true,
          lowLatencyMode: true
        });
        hls.loadSource(channel.streamUrl);
        hls.attachMedia(video);
        hls.on(Hls.Events.ERROR, (_, data) => {
          if (data.fatal) {
            console.warn('HLS fatal error, falling back to backup stream', data);
            setHasError(true);
          }
        });
      } else if (video.canPlayType('application/vnd.apple.mpegurl')) {
        video.src = channel.streamUrl;
      }
    } else {
      video.src = channel.streamUrl;
    }

    if (isPlaying) {
      video.play().catch(() => {
        // Autoplay policy fallback
      });
    }

    return () => {
      if (hls) {
        hls.destroy();
      }
    };
  }, [channel.streamUrl, channel.id]);

  useEffect(() => {
    const video = videoRef.current;
    if (!video) return;

    if (isPlaying) {
      video.play().catch(() => {});
    } else {
      video.pause();
    }
  }, [isPlaying]);

  useEffect(() => {
    const video = videoRef.current;
    if (!video) return;
    video.muted = isMuted;
  }, [isMuted]);

  // Handle Fullscreen change
  const handleToggleFullscreen = () => {
    if (!containerRef.current) return;
    if (!document.fullscreenElement) {
      containerRef.current.requestFullscreen().catch(() => {});
      setIsFullscreen(true);
    } else {
      document.exitFullscreen().catch(() => {});
      setIsFullscreen(false);
    }
  };

  useEffect(() => {
    const onFsChange = () => {
      setIsFullscreen(!!document.fullscreenElement);
    };
    document.addEventListener('fullscreenchange', onFsChange);
    return () => document.removeEventListener('fullscreenchange', onFsChange);
  }, []);

  const handleMouseMove = () => {
    setShowControls(true);
    if (controlsTimeoutRef.current) clearTimeout(controlsTimeoutRef.current);
    controlsTimeoutRef.current = setTimeout(() => {
      if (isPlaying) setShowControls(false);
    }, 4000);
  };

  const qualities: StreamQuality[] = ['AUTO', '1080p', '720p', '480p'];

  return (
    <div
      ref={containerRef}
      onMouseMove={handleMouseMove}
      onMouseLeave={() => isPlaying && setShowControls(false)}
      className="relative w-full aspect-video bg-black rounded-xl overflow-hidden shadow-2xl border border-slate-800 group select-none"
    >
      <video
        ref={videoRef}
        className="w-full h-full object-contain bg-black"
        playsInline
        loop
        onClick={onTogglePlay}
      />

      {/* Live Equalizer indicator */}
      {isPlaying && (
        <div className="absolute right-4 bottom-14 hidden sm:flex items-end gap-1 pointer-events-none z-10 bg-black/40 px-2 py-1.5 rounded-md backdrop-blur-sm">
          <div className="w-1 bg-[#00E676] rounded-full animate-eq-1" />
          <div className="w-1 bg-[#00E676] rounded-full animate-eq-2" />
          <div className="w-1 bg-[#00E676] rounded-full animate-eq-3" />
          <div className="w-1 bg-[#00E676] rounded-full animate-eq-4" />
          <span className="text-[10px] text-emerald-400 font-bold ml-1 tracking-wider uppercase">BDIX 60FPS</span>
        </div>
      )}

      {/* Controls Overlay */}
      <div
        className={`absolute inset-0 bg-gradient-to-t from-black/90 via-transparent to-black/80 flex flex-col justify-between p-3 sm:p-4 transition-opacity duration-300 ${
          showControls ? 'opacity-100' : 'opacity-0 pointer-events-none'
        }`}
      >
        {/* Top bar inside player */}
        <div className="flex items-center justify-between gap-3">
          <div className="flex items-center gap-3 min-w-0">
            <span className="flex items-center gap-1.5 bg-[#FF334B] text-white text-[11px] font-black px-2 py-0.5 rounded shadow-md">
              <span className="w-2 h-2 rounded-full bg-white animate-live-pulse" />
              LIVE
            </span>
            <div className="min-w-0">
              <h2 className="text-sm sm:text-base font-bold text-white truncate drop-shadow">
                {channel.name}
              </h2>
              <p className="text-xs text-slate-300 truncate drop-shadow flex items-center gap-1">
                <span className="text-[#00E676]">▶</span> {channel.currentProgram}
              </p>
            </div>
          </div>

          <div className="flex items-center gap-1 sm:gap-2">
            <button
              onClick={() => onToggleFavorite(channel.id)}
              className="p-2 rounded-lg bg-black/40 hover:bg-black/60 text-white transition-colors"
              title="Bookmark Favorite"
            >
              <Star
                className={`w-4 h-4 sm:w-5 sm:h-5 ${
                  channel.isFavorite ? 'fill-[#FF334B] text-[#FF334B]' : 'text-slate-300'
                }`}
              />
            </button>

            <div className="relative">
              <button
                onClick={() => setShowQualityMenu(!showQualityMenu)}
                className="flex items-center gap-1 px-2.5 py-1.5 rounded-lg bg-black/40 hover:bg-black/60 text-xs font-bold text-[#00E5FF] transition-colors"
                title="Stream Quality"
              >
                <Settings className="w-3.5 h-3.5" />
                <span>{streamQuality}</span>
              </button>

              {showQualityMenu && (
                <div className="absolute right-0 mt-2 w-36 bg-[#131823] border border-slate-700 rounded-xl shadow-xl p-1 z-30">
                  <div className="text-[10px] font-bold text-slate-400 px-2 py-1 uppercase tracking-wider">Quality</div>
                  {qualities.map((q) => (
                    <button
                      key={q}
                      onClick={() => {
                        onQualityChange(q);
                        setShowQualityMenu(false);
                      }}
                      className="w-full flex items-center justify-between px-2.5 py-1.5 text-xs text-slate-200 hover:bg-emerald-500/20 hover:text-emerald-400 rounded-lg text-left"
                    >
                      <span>{q === 'AUTO' ? 'Auto Adaptive' : `${q} HD`}</span>
                      {streamQuality === q && <Check className="w-3.5 h-3.5 text-[#00E676]" />}
                    </button>
                  ))}
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Center Play Button */}
        <div className="self-center flex items-center justify-center">
          <button
            onClick={onTogglePlay}
            className="w-12 h-12 sm:w-16 sm:h-16 rounded-full bg-black/60 hover:bg-black/80 hover:scale-105 active:scale-95 text-[#00E676] flex items-center justify-center transition-all border border-emerald-500/40 shadow-lg shadow-emerald-500/20"
          >
            {isPlaying ? <Pause className="w-6 h-6 sm:w-8 sm:h-8" /> : <Play className="w-6 h-6 sm:w-8 sm:h-8 ml-1" />}
          </button>
        </div>

        {/* Bottom Bar */}
        <div className="flex items-center justify-between gap-2 text-xs text-white">
          <div className="flex items-center gap-2 sm:gap-4">
            <button
              onClick={onToggleMute}
              className="p-1.5 rounded hover:bg-white/20 transition-colors"
              title={isMuted ? 'Unmute' : 'Mute'}
            >
              {isMuted ? <VolumeX className="w-5 h-5 text-red-400" /> : <Volume2 className="w-5 h-5 text-slate-200" />}
            </button>

            <span className="hidden sm:inline-block px-2 py-0.5 rounded bg-white/10 text-[11px] font-mono text-emerald-400 border border-emerald-500/30">
              {channel.resolution}
            </span>
            <span className="hidden md:inline-block px-2 py-0.5 rounded bg-white/10 text-[11px] text-amber-300 border border-amber-500/30">
              {channel.language}
            </span>
          </div>

          <div className="flex items-center gap-1 sm:gap-3">
            <button
              onClick={onOpenEpg}
              className="flex items-center gap-1.5 px-2.5 py-1 rounded-lg bg-black/40 hover:bg-black/60 text-slate-200 transition-colors"
            >
              <Calendar className="w-3.5 h-3.5 text-[#00E676]" />
              <span className="hidden xs:inline text-xs font-semibold">TV Guide (EPG)</span>
            </button>

            <button
              onClick={handleToggleFullscreen}
              className="p-1.5 rounded hover:bg-white/20 transition-colors"
              title={isFullscreen ? 'Exit Fullscreen' : 'Fullscreen'}
            >
              {isFullscreen ? <Minimize className="w-5 h-5" /> : <Maximize className="w-5 h-5" />}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
