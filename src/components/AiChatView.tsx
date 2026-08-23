import React, { useState, useRef, useEffect } from 'react';
import { ChatMessage, GeminiRolePreset } from '../types';
import {
  Bot,
  User,
  Send,
  Sparkles,
  Trash2,
  Copy,
  Check,
  Zap,
  BrainCircuit,
  Sliders,
  ChevronDown,
  Info
} from 'lucide-react';

interface AiChatViewProps {
  messages: ChatMessage[];
  isLoading: boolean;
  selectedModel: string;
  selectedRole: GeminiRolePreset;
  rolePresets: GeminiRolePreset[];
  onSendMessage: (text: string) => void;
  onClearChat: () => void;
  onSelectModel: (model: string) => void;
  onSelectRole: (role: GeminiRolePreset) => void;
}

export const AiChatView: React.FC<AiChatViewProps> = ({
  messages,
  isLoading,
  selectedModel,
  selectedRole,
  rolePresets,
  onSendMessage,
  onClearChat,
  onSelectModel,
  onSelectRole
}) => {
  const [inputPrompt, setInputPrompt] = useState('');
  const [copiedId, setCopiedId] = useState<string | null>(null);
  const [showModelDropdown, setShowModelDropdown] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, isLoading]);

  const handleSend = (e?: React.FormEvent) => {
    if (e) e.preventDefault();
    if (inputPrompt.trim() && !isLoading) {
      const p = inputPrompt;
      setInputPrompt('');
      onSendMessage(p);
    }
  };

  const handleCopy = (id: string, text: string) => {
    navigator.clipboard.writeText(text);
    setCopiedId(id);
    setTimeout(() => setCopiedId(null), 2000);
  };

  const suggestionPills = [
    '🏏 T Sports Live Cricket Schedule',
    '🎭 Top Bengali Natok on Zee Bangla & NTV',
    '🎬 Action Movies on Star Gold & Sony Max',
    '📡 How to add custom IPTV M3U streams?'
  ];

  return (
    <div className="flex flex-col h-[calc(100vh-145px)] max-h-[850px] bg-[#131823] rounded-2xl border border-slate-800 shadow-2xl overflow-hidden">
      {/* Top Header with Model Picker & Role Selector */}
      <div className="bg-[#1C2232] p-3 sm:p-4 border-b border-slate-800 flex flex-col gap-3">
        <div className="flex items-center justify-between gap-2 flex-wrap">
          {/* Model Selector Menu */}
          <div className="relative">
            <button
              onClick={() => setShowModelDropdown(!showModelDropdown)}
              className="flex items-center gap-2 px-3 py-1.5 rounded-xl bg-[#0A0D14] border border-[#00E5FF]/40 text-xs sm:text-sm font-bold text-white hover:border-[#00E5FF] transition-all select-none"
            >
              <BrainCircuit className="w-4 h-4 text-[#00E5FF]" />
              <span className="text-[#00E5FF]">
                {selectedModel === 'gemini-3.1-pro-preview'
                  ? 'Gemini 3.1 Pro (Complex)'
                  : selectedModel === 'gemini-3.1-flash-lite'
                  ? 'Gemini 3.1 Flash Lite (Fast)'
                  : 'Gemini 3.5 Flash (General)'}
              </span>
              <ChevronDown className="w-3.5 h-3.5 text-slate-400" />
            </button>

            {showModelDropdown && (
              <div className="absolute left-0 mt-2 w-72 bg-[#0A0D14] border border-slate-700 rounded-xl shadow-2xl p-1.5 z-40">
                <div className="text-[10px] font-bold text-slate-400 px-2 py-1 uppercase tracking-wider">
                  Select Gemini Model
                </div>

                <button
                  onClick={() => {
                    onSelectModel('gemini-3.5-flash');
                    setShowModelDropdown(false);
                  }}
                  className={`w-full text-left p-2 rounded-lg text-xs transition-colors flex flex-col ${
                    selectedModel === 'gemini-3.5-flash'
                      ? 'bg-emerald-950/60 text-[#00E676] border border-emerald-500/40'
                      : 'text-slate-200 hover:bg-slate-800'
                  }`}
                >
                  <div className="font-bold flex items-center justify-between">
                    <span>gemini-3.5-flash</span>
                    <span className="text-[9px] bg-emerald-500/20 text-emerald-400 px-1.5 py-0.2 rounded font-mono">
                      General
                    </span>
                  </div>
                  <span className="text-[11px] text-slate-400">
                    Balanced, versatile model for general inquiries & recommendations.
                  </span>
                </button>

                <button
                  onClick={() => {
                    onSelectModel('gemini-3.1-pro-preview');
                    setShowModelDropdown(false);
                  }}
                  className={`w-full text-left p-2 rounded-lg text-xs transition-colors flex flex-col mt-1 ${
                    selectedModel === 'gemini-3.1-pro-preview'
                      ? 'bg-cyan-950/60 text-[#00E5FF] border border-cyan-500/40'
                      : 'text-slate-200 hover:bg-slate-800'
                  }`}
                >
                  <div className="font-bold flex items-center justify-between">
                    <span>gemini-3.1-pro-preview</span>
                    <span className="text-[9px] bg-cyan-500/20 text-cyan-400 px-1.5 py-0.2 rounded font-mono">
                      Deep Reasoning
                    </span>
                  </div>
                  <span className="text-[11px] text-slate-400">
                    High capability model for particularly complex tasks & detailed EPG breakdown.
                  </span>
                </button>

                <button
                  onClick={() => {
                    onSelectModel('gemini-3.1-flash-lite');
                    setShowModelDropdown(false);
                  }}
                  className={`w-full text-left p-2 rounded-lg text-xs transition-colors flex flex-col mt-1 ${
                    selectedModel === 'gemini-3.1-flash-lite'
                      ? 'bg-amber-950/60 text-amber-400 border border-amber-500/40'
                      : 'text-slate-200 hover:bg-slate-800'
                  }`}
                >
                  <div className="font-bold flex items-center justify-between">
                    <span>gemini-3.1-flash-lite</span>
                    <span className="text-[9px] bg-amber-500/20 text-amber-400 px-1.5 py-0.2 rounded font-mono">
                      Fastest
                    </span>
                  </div>
                  <span className="text-[11px] text-slate-400">
                    Low-latency model for tasks that should happen lightning fast.
                  </span>
                </button>
              </div>
            )}
          </div>

          {/* Clear history */}
          <button
            onClick={onClearChat}
            className="flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-[#0A0D14] hover:bg-rose-950/50 text-slate-300 hover:text-rose-400 border border-slate-700/60 text-xs font-semibold transition-colors"
            title="Clear Chat History"
          >
            <Trash2 className="w-3.5 h-3.5" />
            <span className="hidden sm:inline">Clear Chat</span>
          </button>
        </div>

        {/* Role Presets (System Instruction affordance) */}
        <div className="flex items-center gap-1.5 overflow-x-auto pb-1 no-scrollbar select-none">
          <span className="text-[11px] font-bold text-slate-400 uppercase tracking-wider mr-1 flex items-center gap-1 flex-shrink-0">
            <Sliders className="w-3 h-3 text-[#00E676]" />
            Role:
          </span>
          {rolePresets.map((role) => {
            const isSelected = selectedRole.id === role.id;
            return (
              <button
                key={role.id}
                onClick={() => onSelectRole(role)}
                className={`px-3 py-1 rounded-full text-xs font-semibold whitespace-nowrap transition-all border ${
                  isSelected
                    ? 'bg-[#00E676]/20 border-[#00E676] text-[#00E676] font-bold shadow-sm'
                    : 'bg-[#0A0D14] text-slate-400 border-slate-800 hover:text-slate-200 hover:border-slate-700'
                }`}
              >
                {role.title}
              </button>
            );
          })}
        </div>
      </div>

      {/* Scrollable Message Thread */}
      <div className="flex-1 overflow-y-auto p-4 flex flex-col gap-4">
        {messages.map((message) => {
          const isUser = message.role === 'user';
          return (
            <div
              key={message.id}
              className={`flex gap-3 max-w-[88%] sm:max-w-[80%] ${
                isUser ? 'ml-auto flex-row-reverse' : 'mr-auto'
              }`}
            >
              {/* Avatar */}
              <div
                className={`w-8 h-8 rounded-full flex items-center justify-center flex-shrink-0 text-xs font-bold ${
                  isUser
                    ? 'bg-[#00E676] text-black shadow-md shadow-emerald-500/20'
                    : 'bg-[#1C2232] text-[#00E5FF] border border-cyan-500/40'
                }`}
              >
                {isUser ? <User className="w-4 h-4" /> : <Bot className="w-4 h-4" />}
              </div>

              {/* Message Bubble */}
              <div
                className={`flex flex-col rounded-2xl p-3.5 text-sm leading-relaxed shadow-md select-text ${
                  isUser
                    ? 'bg-[#00E676] text-black font-medium rounded-tr-none'
                    : 'bg-[#1C2232] text-slate-100 border border-slate-700/80 rounded-tl-none'
                }`}
              >
                {!isUser && (
                  <div className="flex items-center justify-between gap-4 mb-1.5 pb-1 border-b border-slate-700/50">
                    <span className="text-[10px] font-bold text-[#00E5FF] tracking-wider uppercase">
                      {message.modelName}
                    </span>
                    <button
                      onClick={() => handleCopy(message.id, message.text)}
                      className="text-slate-400 hover:text-white p-0.5 rounded transition-colors"
                      title="Copy response"
                    >
                      {copiedId === message.id ? (
                        <Check className="w-3.5 h-3.5 text-[#00E676]" />
                      ) : (
                        <Copy className="w-3.5 h-3.5" />
                      )}
                    </button>
                  </div>
                )}

                <div className="whitespace-pre-wrap">{message.text}</div>
              </div>
            </div>
          );
        })}

        {isLoading && (
          <div className="flex gap-3 mr-auto max-w-[80%]">
            <div className="w-8 h-8 rounded-full bg-[#1C2232] text-[#00E5FF] border border-cyan-500/40 flex items-center justify-center flex-shrink-0">
              <Bot className="w-4 h-4 animate-pulse" />
            </div>
            <div className="bg-[#1C2232] text-slate-200 border border-slate-700/80 rounded-2xl rounded-tl-none p-3.5 flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-[#00E676] animate-bounce" />
              <span className="w-2 h-2 rounded-full bg-[#00E5FF] animate-bounce [animation-delay:0.2s]" />
              <span className="w-2 h-2 rounded-full bg-[#FFB800] animate-bounce [animation-delay:0.4s]" />
              <span className="text-xs text-slate-400 ml-1">Gemini is writing reply...</span>
            </div>
          </div>
        )}

        <div ref={messagesEndRef} />
      </div>

      {/* Suggested Quick Question Pills */}
      <div className="px-4 py-2 bg-[#1C2232]/60 border-t border-slate-800/80 flex items-center gap-2 overflow-x-auto no-scrollbar">
        {suggestionPills.map((pill, i) => (
          <button
            key={i}
            onClick={() => onSendMessage(pill.substring(3))}
            className="flex items-center gap-1.5 px-3 py-1 rounded-full bg-[#0A0D14] hover:bg-slate-800 text-slate-300 hover:text-white text-xs whitespace-nowrap border border-slate-700/60 transition-colors"
          >
            <Sparkles className="w-3 h-3 text-[#FFB800]" />
            <span>{pill}</span>
          </button>
        ))}
      </div>

      {/* Prompt Input Box */}
      <form onSubmit={handleSend} className="p-3 sm:p-4 bg-[#1C2232] border-t border-slate-800 flex items-center gap-2">
        <input
          type="text"
          value={inputPrompt}
          onChange={(e) => setInputPrompt(e.target.value)}
          placeholder={`Ask about cricket matches, Natok serials, movie timings... (${selectedModel})`}
          className="flex-1 bg-[#0A0D14] border border-slate-700/70 rounded-xl px-4 py-3 text-sm text-slate-100 placeholder-slate-500 focus:outline-none focus:border-[#00E676] focus:ring-1 focus:ring-[#00E676] transition-all"
        />
        <button
          type="submit"
          disabled={!inputPrompt.trim() || isLoading}
          className="p-3 rounded-xl bg-[#00E676] hover:bg-[#00B050] disabled:bg-slate-800 disabled:text-slate-600 text-black font-bold transition-all shadow-md shadow-emerald-500/20"
        >
          <Send className="w-5 h-5" />
        </button>
      </form>
    </div>
  );
};
