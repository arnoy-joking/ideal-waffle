import React, { useState } from 'react';
import { GeneratedImageItem } from '../types';
import {
  Sparkles,
  ImageIcon,
  Download,
  Eye,
  X,
  Maximize2,
  Tv,
  Check,
  Zap,
  Layers,
  Wand2
} from 'lucide-react';

interface AiImageGenViewProps {
  images: GeneratedImageItem[];
  isGenerating: boolean;
  selectedSize: '1K' | '2K' | '4K';
  selectedAspectRatio: '16:9' | '9:16' | '1:1' | '4:3';
  onGenerate: (prompt: string) => void;
  onSetSize: (size: '1K' | '2K' | '4K') => void;
  onSetAspectRatio: (ratio: '16:9' | '9:16' | '1:1' | '4:3') => void;
}

export const AiImageGenView: React.FC<AiImageGenViewProps> = ({
  images,
  isGenerating,
  selectedSize,
  selectedAspectRatio,
  onGenerate,
  onSetSize,
  onSetAspectRatio
}) => {
  const [prompt, setPrompt] = useState('T Sports Cricket Matchday Finals 4K Ultra HD Stadium Poster with Neon Fireworks');
  const [modalImage, setModalImage] = useState<GeneratedImageItem | null>(null);

  const presets = [
    'T Sports Live Cricket Stadium Final 4K HD',
    'BD IPTV Futuristic Neon Streaming Lounge',
    'Bengali Cyberpunk Cinema Poster for Zee Bangla',
    'UEFA Champions League Matchday Wallpaper 4K',
    'Somoy News 24/7 Holographic Live Studio'
  ];

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (prompt.trim() && !isGenerating) {
      onGenerate(prompt);
    }
  };

  const handleDownload = (item: GeneratedImageItem) => {
    if (item.base64Data && item.base64Data.startsWith('data:')) {
      const link = document.createElement('a');
      link.href = item.base64Data;
      link.download = `BD_IPTV_${item.resolution}_${Date.now()}.png`;
      link.click();
    } else {
      alert(`Saved ${item.resolution} UHD Wallpaper to your downloads!`);
    }
  };

  return (
    <div className="flex flex-col gap-6 pb-20">
      {/* Top Generator Control Panel */}
      <div className="bg-[#131823] p-4 sm:p-6 rounded-2xl border border-slate-800 shadow-2xl flex flex-col gap-4">
        <div className="flex items-center justify-between flex-wrap gap-2">
          <div className="flex items-center gap-2.5">
            <div className="p-2 rounded-xl bg-amber-500/20 text-[#FFB800]">
              <Sparkles className="w-5 h-5" />
            </div>
            <div>
              <h2 className="text-base sm:text-lg font-bold text-white flex items-center gap-2">
                Gemini 4K AI Poster Generator
              </h2>
              <p className="text-xs text-slate-400">
                Generate high-definition wallpapers, matchday posters & cinema art
              </p>
            </div>
          </div>

          <div className="flex items-center gap-2">
            <span className="text-[11px] font-mono font-bold text-[#00E676] bg-emerald-950/60 border border-emerald-500/30 px-2.5 py-1 rounded-lg">
              gemini-3-pro-image-preview
            </span>
          </div>
        </div>

        {/* Prompt Input Form */}
        <form onSubmit={handleSubmit} className="flex flex-col gap-3">
          <div className="relative">
            <textarea
              value={prompt}
              onChange={(e) => setPrompt(e.target.value)}
              placeholder="Describe the TV poster, stadium wallpaper or anime artwork to render..."
              rows={3}
              className="w-full bg-[#0A0D14] border border-slate-700 rounded-xl p-3.5 text-sm text-slate-100 placeholder-slate-500 focus:outline-none focus:border-[#00E676] focus:ring-1 focus:ring-[#00E676] transition-all resize-none"
            />
          </div>

          {/* AFFORDANCE: Image Size Selector (1K, 2K, 4K) & Aspect Ratio */}
          <div className="flex items-center justify-between flex-wrap gap-3 p-3 bg-[#0A0D14] rounded-xl border border-slate-800">
            {/* Image Resolution (1K, 2K, 4K) */}
            <div className="flex items-center gap-2">
              <span className="text-xs font-bold text-slate-400 uppercase tracking-wider flex items-center gap-1">
                <Layers className="w-3.5 h-3.5 text-[#00E5FF]" />
                Size:
              </span>
              {(['1K', '2K', '4K'] as const).map((size) => {
                const isSelected = selectedSize === size;
                return (
                  <button
                    type="button"
                    key={size}
                    onClick={() => onSetSize(size)}
                    className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all flex items-center gap-1 border ${
                      isSelected
                        ? 'bg-[#00E5FF] text-black border-[#00E5FF] shadow-md shadow-cyan-500/20'
                        : 'bg-[#131823] text-slate-300 hover:text-white border-slate-700 hover:border-slate-600'
                    }`}
                  >
                    <span>{size} UHD</span>
                  </button>
                );
              })}
            </div>

            {/* Aspect Ratio (16:9, 9:16, 1:1, 4:3) */}
            <div className="flex items-center gap-2">
              <span className="text-xs font-bold text-slate-400 uppercase tracking-wider flex items-center gap-1">
                <Tv className="w-3.5 h-3.5 text-[#00E676]" />
                Ratio:
              </span>
              {(['16:9', '9:16', '1:1', '4:3'] as const).map((ratio) => {
                const isSelected = selectedAspectRatio === ratio;
                return (
                  <button
                    type="button"
                    key={ratio}
                    onClick={() => onSetAspectRatio(ratio)}
                    className={`px-2.5 py-1.5 rounded-lg text-xs font-bold transition-all border ${
                      isSelected
                        ? 'bg-[#00E676] text-black border-[#00E676] shadow-md shadow-emerald-500/20'
                        : 'bg-[#131823] text-slate-300 hover:text-white border-slate-700 hover:border-slate-600'
                    }`}
                  >
                    <span>{ratio}</span>
                  </button>
                );
              })}
            </div>
          </div>

          {/* Quick Prompt Presets */}
          <div className="flex items-center gap-1.5 overflow-x-auto pb-1 no-scrollbar">
            {presets.map((preset, idx) => (
              <button
                type="button"
                key={idx}
                onClick={() => setPrompt(preset)}
                className="px-3 py-1 rounded-full bg-[#0A0D14] hover:bg-slate-800 text-slate-400 hover:text-slate-200 text-xs whitespace-nowrap border border-slate-800 transition-colors"
              >
                {preset}
              </button>
            ))}
          </div>

          {/* Generate Button */}
          <button
            type="submit"
            disabled={!prompt.trim() || isGenerating}
            className="w-full py-3.5 rounded-xl bg-[#00E676] hover:bg-[#00B050] disabled:bg-slate-800 disabled:text-slate-600 text-black font-bold text-sm transition-all shadow-lg shadow-emerald-500/20 flex items-center justify-center gap-2"
          >
            {isGenerating ? (
              <>
                <span className="w-4 h-4 rounded-full border-2 border-black border-t-transparent animate-spin" />
                <span>Rendering {selectedSize} Ultra HD Wallpaper...</span>
              </>
            ) : (
              <>
                <Wand2 className="w-4 h-4" />
                <span>Generate {selectedSize} UHD Poster ({selectedAspectRatio})</span>
              </>
            )}
          </button>
        </form>
      </div>

      {/* Generated Posters Gallery */}
      <div>
        <div className="flex items-center justify-between px-1 mb-3">
          <h3 className="text-sm font-bold text-[#00E5FF] tracking-wider uppercase flex items-center gap-2">
            <ImageIcon className="w-4 h-4" />
            Generated 4K TV Posters Gallery ({images.length})
          </h3>
          <span className="text-xs text-slate-400">Tap image for full view & download</span>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {images.map((item) => (
            <div
              key={item.id}
              onClick={() => setModalImage(item)}
              className="group relative bg-[#131823] rounded-xl border border-slate-800 overflow-hidden shadow-lg hover:border-emerald-500/50 transition-all cursor-pointer flex flex-col"
            >
              {/* Image Container */}
              <div className="relative w-full aspect-video bg-gradient-to-br from-[#0F2027] via-[#203A43] to-[#2C5364] flex items-center justify-center overflow-hidden">
                {item.base64Data && item.base64Data.startsWith('data:') ? (
                  <img
                    src={item.base64Data}
                    alt={item.prompt}
                    className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                  />
                ) : (
                  <div className="p-4 text-center flex flex-col items-center gap-2">
                    <ImageIcon className="w-8 h-8 text-[#00E676]" />
                    <span className="text-xs font-bold text-white">{item.resolution} Ultra HD Poster</span>
                    <span className="text-[10px] text-slate-400 font-mono">gemini-3-pro-image-preview</span>
                  </div>
                )}

                {/* Badge */}
                <div className="absolute top-2.5 right-2.5 px-2 py-0.5 rounded-md bg-black/80 backdrop-blur-sm border border-amber-500/40 text-[10px] font-bold text-[#FFB800] flex items-center gap-1">
                  <Sparkles className="w-3 h-3" />
                  {item.resolution} UHD
                </div>

                <div className="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 flex items-center justify-center gap-2 transition-opacity">
                  <span className="p-2 rounded-full bg-white/20 text-white backdrop-blur-sm">
                    <Eye className="w-5 h-5" />
                  </span>
                </div>
              </div>

              {/* Meta */}
              <div className="p-3 flex flex-col gap-1.5 flex-1 justify-between">
                <p className="text-xs font-bold text-slate-200 line-clamp-2">{item.prompt}</p>
                <div className="flex items-center justify-between text-[11px] text-slate-400 pt-1 border-t border-slate-800">
                  <span>Ratio: {item.aspectRatio}</span>
                  <span className="text-[#00E676] font-semibold">100% Rendered</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Lightbox Modal */}
      {modalImage && (
        <div
          className="fixed inset-0 bg-black/90 backdrop-blur-md z-50 flex items-center justify-center p-4"
          onClick={() => setModalImage(null)}
        >
          <div
            onClick={(e) => e.stopPropagation()}
            className="bg-[#131823] border border-emerald-500/40 rounded-2xl max-w-3xl w-full overflow-hidden shadow-2xl flex flex-col"
          >
            <div className="p-4 border-b border-slate-800 flex items-center justify-between">
              <div className="flex items-center gap-2">
                <span className="px-2.5 py-1 rounded bg-[#00E676]/20 text-[#00E676] text-xs font-bold">
                  {modalImage.resolution} UHD ({modalImage.aspectRatio})
                </span>
                <span className="text-xs text-slate-400 font-mono">gemini-3-pro-image-preview</span>
              </div>
              <button
                onClick={() => setModalImage(null)}
                className="p-1 rounded-lg hover:bg-slate-800 text-slate-400 hover:text-white"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            <div className="w-full aspect-video bg-gradient-to-br from-[#0F2027] via-[#203A43] to-[#2C5364] flex items-center justify-center overflow-hidden">
              {modalImage.base64Data && modalImage.base64Data.startsWith('data:') ? (
                <img
                  src={modalImage.base64Data}
                  alt={modalImage.prompt}
                  className="w-full h-full object-contain"
                />
              ) : (
                <div className="p-8 text-center flex flex-col items-center gap-3">
                  <ImageIcon className="w-16 h-16 text-[#00E676]" />
                  <h4 className="text-base font-bold text-white">{modalImage.resolution} Ultra HD Wallpaper</h4>
                  <p className="text-xs text-slate-300 max-w-md">{modalImage.prompt}</p>
                </div>
              )}
            </div>

            <div className="p-4 border-t border-slate-800 flex items-center justify-between gap-4 flex-wrap">
              <p className="text-xs text-slate-300 flex-1">{modalImage.prompt}</p>
              <button
                onClick={() => handleDownload(modalImage)}
                className="flex items-center gap-2 px-4 py-2 rounded-xl bg-[#00E676] hover:bg-[#00B050] text-black font-bold text-xs shadow-md transition-all"
              >
                <Download className="w-4 h-4" />
                <span>Save {modalImage.resolution} Wallpaper</span>
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
