import React, { useState } from 'react';
import { ChannelCategory } from '../types';
import { X, Plus, Link, Tv, Radio } from 'lucide-react';

interface CustomStreamModalProps {
  onClose: () => void;
  onAddStream: (name: string, url: string, category: ChannelCategory) => void;
}

export const CustomStreamModal: React.FC<CustomStreamModalProps> = ({ onClose, onAddStream }) => {
  const [name, setName] = useState('');
  const [url, setUrl] = useState('');
  const [category, setCategory] = useState<ChannelCategory>('BANGLADESH');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (url.trim()) {
      onAddStream(name || 'Custom Live Stream', url, category);
      onClose();
    }
  };

  return (
    <div
      className="fixed inset-0 bg-black/80 backdrop-blur-sm z-50 flex items-center justify-center p-4"
      onClick={onClose}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        className="bg-[#131823] border border-emerald-500/50 rounded-2xl max-w-md w-full overflow-hidden shadow-2xl flex flex-col"
      >
        <div className="p-4 border-b border-slate-800 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Tv className="w-5 h-5 text-[#00E676]" />
            <h3 className="text-base font-bold text-white">Add Custom IPTV Stream</h3>
          </div>
          <button onClick={onClose} className="p-1 rounded-lg hover:bg-slate-800 text-slate-400 hover:text-white">
            <X className="w-5 h-5" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-5 flex flex-col gap-4">
          <p className="text-xs text-slate-400">
            Paste any BDIX, HLS (<code className="text-emerald-400 font-mono">.m3u8</code>), or direct MP4 live video
            link to play immediately.
          </p>

          <div>
            <label className="block text-xs font-bold text-slate-300 mb-1">Channel Name</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="e.g. My Sports HD, Local BD Stream"
              className="w-full bg-[#0A0D14] border border-slate-700 rounded-xl px-3.5 py-2.5 text-sm text-slate-100 placeholder-slate-500 focus:outline-none focus:border-[#00E676]"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-300 mb-1">Streaming URL *</label>
            <div className="relative">
              <Link className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
              <input
                type="url"
                required
                value={url}
                onChange={(e) => setUrl(e.target.value)}
                placeholder="https://.../stream.m3u8 or http://..."
                className="w-full bg-[#0A0D14] border border-slate-700 rounded-xl pl-10 pr-3.5 py-2.5 text-sm text-slate-100 placeholder-slate-500 focus:outline-none focus:border-[#00E676]"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-300 mb-1">Category</label>
            <select
              value={category}
              onChange={(e) => setCategory(e.target.value as ChannelCategory)}
              className="w-full bg-[#0A0D14] border border-slate-700 rounded-xl px-3.5 py-2.5 text-sm text-slate-100 focus:outline-none focus:border-[#00E676]"
            >
              <option value="BANGLADESH">Bangladesh TV</option>
              <option value="SPORTS">Live Sports</option>
              <option value="ENTERTAINMENT">Drama & Serials</option>
              <option value="MOVIES">Movies & Cinema</option>
              <option value="NEWS">News 24/7</option>
              <option value="INTERNATIONAL">International</option>
            </select>
          </div>

          <div className="flex items-center justify-end gap-2 pt-2">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 rounded-xl bg-slate-800 text-slate-300 hover:text-white text-xs font-semibold"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={!url.trim()}
              className="flex items-center gap-1.5 px-5 py-2 rounded-xl bg-[#00E676] hover:bg-[#00B050] text-black text-xs font-bold shadow-md shadow-emerald-500/20 disabled:opacity-50"
            >
              <Plus className="w-4 h-4" />
              <span>Add & Play Stream</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
