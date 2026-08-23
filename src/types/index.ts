export type ChannelCategory =
  | 'ALL'
  | 'BANGLADESH'
  | 'SPORTS'
  | 'ENTERTAINMENT'
  | 'MOVIES'
  | 'NEWS'
  | 'KIDS'
  | 'INFOTAINMENT'
  | 'MUSIC'
  | 'RELIGIOUS'
  | 'INTERNATIONAL';

export interface Channel {
  id: string;
  name: string;
  category: ChannelCategory;
  logoUrl: string;
  streamUrl: string;
  backupStreamUrl?: string;
  resolution: string;
  language: string;
  isLive: boolean;
  currentProgram: string;
  nextProgram: string;
  description: string;
  isFavorite?: boolean;
  country: string;
}

export interface ProgramSchedule {
  id: string;
  channelId: string;
  programName: string;
  startTime: string;
  endTime: string;
  progress: number;
  description: string;
  genre: string;
}

export interface ChatMessage {
  id: string;
  role: 'user' | 'model';
  text: string;
  timestamp: number;
  modelName: string;
  isError?: boolean;
}

export interface GeminiRolePreset {
  id: string;
  title: string;
  roleDescription: string;
  systemInstruction: string;
  recommendedModel: string;
}

export interface GeneratedImageItem {
  id: string;
  prompt: string;
  model: string;
  resolution: '1K' | '2K' | '4K';
  aspectRatio: '16:9' | '9:16' | '1:1' | '4:3';
  base64Data?: string | null;
  timestamp: number;
  isGenerating?: boolean;
  errorMessage?: string | null;
}

export type StreamQuality = 'AUTO' | '1080p' | '720p' | '480p';
