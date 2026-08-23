import React from 'react';
import { Channel } from '../types';
import { getProgramSchedules } from '../data/channels';
import { X, Calendar, Clock, Sparkles, Radio } from 'lucide-react';

interface EpgModalProps {
  channel: Channel;
  onClose: () => void;
}

export const EpgModal: React.FC<EpgModalProps> = ({ channel, onClose }) => {
  const schedules = getProgramSchedules(channel.id);

  return (
    <div
      className="fixed inset-0 bg-black/80 backdrop-blur-sm z-50 flex items-center justify-center p-4"
      onClick={onClose}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        className="bg-[#131823] border border-slate-700 rounded-2xl max-w-lg w-full overflow-hidden shadow-2xl flex flex-col"
      >
        {/* Header */}
        <div className="p-4 border-b border-slate-800 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-lg overflow-hidden border border-slate-700 bg-black">
              <img src={channel.logoUrl} alt={channel.name} className="w-full h-full object-cover" />
            </div>
            <div>
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                {channel.name}
                <span className="text-[10px] bg-[#00E676] text-black font-extrabold px-1.5 py-0.2 rounded">
                  EPG GUIDE
                </span>
              </h3>
              <p className="text-xs text-slate-400">{channel.language} • {channel.resolution}</p>
            </div>
          </div>
          <button onClick={onClose} className="p-1 rounded-lg hover:bg-slate-800 text-slate-400 hover:text-white">
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Schedule List */}
        <div className="p-4 flex flex-col gap-3 max-h-[400px] overflow-y-auto">
          {schedules.map((item, idx) => {
            const isNow = idx === 0;
            return (
              <div
                key={item.id}
                className={`p-3 rounded-xl border flex flex-col gap-1.5 ${
                  isNow
                    ? 'bg-emerald-950/40 border-[#00E676]/60 shadow-md shadow-emerald-500/10'
                    : 'bg-[#0A0D14] border-slate-800'
                }`}
              >
                <div className="flex items-center justify-between gap-2">
                  <div className="flex items-center gap-2">
                    <Clock className={`w-3.5 h-3.5 ${isNow ? 'text-[#00E676]' : 'text-slate-400'}`} />
                    <span className="text-xs font-mono font-bold text-slate-300">
                      {item.startTime} - {item.endTime}
                    </span>
                    {isNow && (
                      <span className="text-[9px] font-black uppercase bg-[#FF334B] text-white px-1.5 py-0.2 rounded">
                        NOW PLAYING
                      </span>
                    )}
                  </div>
                  <span className="text-[10px] text-cyan-400 font-semibold">{item.genre}</span>
                </div>

                <div className="font-bold text-sm text-white">{item.programName}</div>
                <p className="text-xs text-slate-400">{item.description}</p>

                {isNow && (
                  <div className="w-full bg-slate-800 h-1.5 rounded-full overflow-hidden mt-1">
                    <div className="bg-[#00E676] h-full rounded-full" style={{ width: '65%' }} />
                  </div>
                )}
              </div>
            );
          })}
        </div>

        <div className="p-3 bg-[#0A0D14] border-t border-slate-800 text-center text-xs text-slate-500">
          EPG Schedule synchronized with tv.bdiptv.net broadcast servers
        </div>
      </div>
    </div>
  );
};
