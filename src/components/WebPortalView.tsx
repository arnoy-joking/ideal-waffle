import React, { useState } from 'react';
import {
  Globe,
  Lock,
  RefreshCw,
  ExternalLink,
  ShieldCheck,
  Tv,
  ArrowRight,
  Sparkles,
  AlertCircle
} from 'lucide-react';
import { Channel } from '../types';

interface WebPortalViewProps {
  onQuickPlay: (channelId: string) => void;
}

export const WebPortalView: React.FC<WebPortalViewProps> = ({ onQuickPlay }) => {
  const [url, setUrl] = useState('http://tv.bdiptv.net');
  const [iframeKey, setIframeKey] = useState(0);

  const reloadPortal = () => {
    setIframeKey((prev) => prev + 1);
  };

  const featuredQuickChannels = [
    { id: 't_sports', name: 'T Sports Live', category: 'Live Cricket & BPL', color: 'border-emerald-500/40 text-emerald-400' },
    { id: 'somoy_tv', name: 'Somoy News 24/7', category: 'Breaking BD News', color: 'border-rose-500/40 text-rose-400' },
    { id: 'zee_bangla', name: 'Zee Bangla HD', category: 'Mega Natok & Serials', color: 'border-cyan-500/40 text-cyan-400' },
    { id: 'star_sports_1', name: 'Star Sports 1', category: 'ICC World Sports', color: 'border-amber-500/40 text-amber-400' }
  ];

  return (
    <div className="flex flex-col gap-4 pb-20">
      {/* Web Navigation Bar */}
      <div className="bg-[#131823] p-3 rounded-xl border border-slate-800 flex items-center justify-between gap-2 shadow-lg">
        <div className="flex items-center gap-2 flex-1 min-w-0">
          <button
            onClick={reloadPortal}
            className="p-2 rounded-lg bg-[#0A0D14] hover:bg-slate-800 text-slate-300 transition-colors"
            title="Reload Portal"
          >
            <RefreshCw className="w-4 h-4" />
          </button>

          {/* Address Bar */}
          <div className="flex-1 flex items-center gap-2 bg-[#0A0D14] border border-slate-700/60 rounded-xl px-3 py-2 text-xs text-slate-200">
            <Lock className="w-3.5 h-3.5 text-[#00E676] flex-shrink-0" />
            <span className="font-mono truncate">{url}</span>
            <span className="ml-auto hidden sm:inline-block text-[10px] text-emerald-400 font-bold bg-emerald-950/60 px-1.5 py-0.5 rounded">
              BDIX Port 80
            </span>
          </div>
        </div>

        <a
          href={url}
          target="_blank"
          rel="noopener noreferrer"
          className="flex items-center gap-1.5 px-3 py-2 rounded-xl bg-[#00E5FF]/10 hover:bg-[#00E5FF]/20 text-[#00E5FF] text-xs font-bold transition-colors select-none"
        >
          <ExternalLink className="w-3.5 h-3.5" />
          <span className="hidden sm:inline">Open in New Tab</span>
        </a>
      </div>

      {/* Embedded Portal Frame */}
      <div className="relative w-full h-[600px] bg-[#131823] rounded-xl border border-slate-800 overflow-hidden shadow-2xl flex flex-col">
        <iframe
          key={iframeKey}
          src={url}
          title="tv.bdiptv.net Portal"
          className="w-full flex-1 border-0 bg-slate-900"
          sandbox="allow-scripts allow-same-origin allow-forms allow-popups"
        />

        {/* Fallback info bar */}
        <div className="bg-[#0A0D14] border-t border-slate-800/80 p-3 flex items-center justify-between text-xs text-slate-400">
          <div className="flex items-center gap-2">
            <ShieldCheck className="w-4 h-4 text-[#00E676]" />
            <span>Official BD IPTV Gateway (tv.bdiptv.net)</span>
          </div>
          <a
            href="http://tv.bdiptv.net"
            target="_blank"
            rel="noopener noreferrer"
            className="text-[#00E676] hover:underline font-semibold"
          >
            Direct Web Portal →
          </a>
        </div>
      </div>

      {/* Quick Launch BD IPTV Channels */}
      <div className="bg-[#131823] p-4 rounded-xl border border-slate-800">
        <h3 className="text-sm font-bold text-slate-100 mb-3 flex items-center gap-2">
          <Tv className="w-4 h-4 text-[#00E676]" />
          Instant BDIX Stream Launchers
        </h3>

        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-2.5">
          {featuredQuickChannels.map((item) => (
            <button
              key={item.id}
              onClick={() => onQuickPlay(item.id)}
              className={`flex items-center justify-between p-3 rounded-xl bg-[#0A0D14] border ${item.color} hover:bg-slate-900 transition-all text-left group`}
            >
              <div>
                <div className="font-bold text-xs sm:text-sm text-white group-hover:text-emerald-400 transition-colors">
                  {item.name}
                </div>
                <div className="text-[11px] text-slate-400">{item.category}</div>
              </div>
              <ArrowRight className="w-4 h-4 text-slate-500 group-hover:text-[#00E676] group-hover:translate-x-0.5 transition-all" />
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};
