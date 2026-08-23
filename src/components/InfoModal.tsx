import React from 'react';
import { X, Tv, Sparkles, Zap, ShieldCheck, Globe, BrainCircuit } from 'lucide-react';

interface InfoModalProps {
  onClose: () => void;
}

export const InfoModal: React.FC<InfoModalProps> = ({ onClose }) => {
  return (
    <div
      className="fixed inset-0 bg-black/80 backdrop-blur-sm z-50 flex items-center justify-center p-4"
      onClick={onClose}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        className="bg-[#131823] border border-emerald-500/40 rounded-2xl max-w-md w-full overflow-hidden shadow-2xl flex flex-col"
      >
        <div className="p-4 border-b border-slate-800 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <div className="p-1.5 rounded-lg bg-[#00E676] text-black">
              <Tv className="w-5 h-5" />
            </div>
            <div>
              <h3 className="text-base font-bold text-white">About BD IPTV</h3>
              <p className="text-xs text-[#00E676] font-mono">tv.bdiptv.net Media Suite</p>
            </div>
          </div>
          <button onClick={onClose} className="p-1 rounded-lg hover:bg-slate-800 text-slate-400 hover:text-white">
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="p-5 flex flex-col gap-4 text-xs sm:text-sm text-slate-300">
          <p>
            <strong className="text-white">BD IPTV</strong> is an all-in-one live Bangladesh & International television
            streaming suite powered by high-speed BDIX routing, interactive TV guide, and Gemini AI assistance.
          </p>

          <div className="bg-[#0A0D14] p-3.5 rounded-xl border border-slate-800 flex flex-col gap-2.5">
            <div className="font-bold text-xs uppercase tracking-wider text-[#00E5FF] flex items-center gap-1.5">
              <Sparkles className="w-3.5 h-3.5" />
              Integrated AI Capabilities
            </div>

            <div className="flex items-start gap-2">
              <span className="text-[#00E676]">✓</span>
              <div>
                <strong className="text-white">Gemini Multi-Turn Chatbot:</strong> Toggle between{' '}
                <code className="text-cyan-400">gemini-3.5-flash</code>,{' '}
                <code className="text-cyan-400">gemini-3.1-pro-preview</code>, and{' '}
                <code className="text-cyan-400">gemini-3.1-flash-lite</code> with role presets.
              </div>
            </div>

            <div className="flex items-start gap-2">
              <span className="text-[#00E676]">✓</span>
              <div>
                <strong className="text-white">Gemini 4K Poster Generator:</strong> Powered by{' '}
                <code className="text-emerald-400">gemini-3-pro-image-preview</code> with <span className="text-amber-400 font-bold">1K, 2K, and 4K</span> image size selection.
              </div>
            </div>

            <div className="flex items-start gap-2">
              <span className="text-[#00E676]">✓</span>
              <div>
                <strong className="text-white">Live BDIX Video Engine:</strong> Native HLS (.m3u8) low-latency playback with quality selection and live equalizer.
              </div>
            </div>
          </div>
        </div>

        <div className="p-3 bg-[#0A0D14] border-t border-slate-800 flex items-center justify-between text-xs text-slate-400">
          <span>Gateway: tv.bdiptv.net</span>
          <button
            onClick={onClose}
            className="px-4 py-1.5 rounded-lg bg-[#00E676] text-black font-bold hover:bg-[#00B050] transition-colors"
          >
            Got it
          </button>
        </div>
      </div>
    </div>
  );
};
