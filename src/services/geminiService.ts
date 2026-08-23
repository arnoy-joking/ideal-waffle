import { ChatMessage } from '../types';

export function getGeminiApiKey(): string {
  // Check import.meta.env or window global
  const envKey = (import.meta as any).env?.VITE_GEMINI_API_KEY || (import.meta as any).env?.GEMINI_API_KEY || '';
  return envKey;
}

export async function sendGeminiChatMessage(
  history: ChatMessage[],
  newPrompt: string,
  modelName: string,
  systemInstruction: string,
  userApiKey?: string
): Promise<string> {
  const apiKey = userApiKey || getGeminiApiKey();

  if (!apiKey) {
    return getOfflineSmartChatReply(newPrompt, modelName);
  }

  try {
    const url = `https://generativelanguage.googleapis.com/v1beta/models/${modelName}:generateContent?key=${apiKey}`;

    const contents = history
      .filter((m) => !m.isError && m.text.trim().length > 0)
      .map((m) => ({
        role: m.role === 'user' ? 'user' : 'model',
        parts: [{ text: m.text }]
      }));

    contents.push({
      role: 'user',
      parts: [{ text: newPrompt }]
    });

    const requestBody: any = {
      contents,
      generationConfig: {
        temperature: 0.7,
        maxOutputTokens: 2048
      }
    };

    if (systemInstruction) {
      requestBody.system_instruction = {
        parts: [{ text: systemInstruction }]
      };
    }

    const response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(requestBody)
    });

    if (!response.ok) {
      console.warn('Gemini chat API error:', response.status);
      return getOfflineSmartChatReply(newPrompt, modelName);
    }

    const data = await response.json();
    const replyText = data?.candidates?.[0]?.content?.parts?.[0]?.text;
    if (replyText) {
      return replyText;
    }

    return getOfflineSmartChatReply(newPrompt, modelName);
  } catch (error) {
    console.error('Error calling Gemini API:', error);
    return getOfflineSmartChatReply(newPrompt, modelName);
  }
}

export async function generateGeminiImage(
  prompt: string,
  modelName: string = 'gemini-3-pro-image-preview',
  sizeResolution: '1K' | '2K' | '4K' = '4K',
  aspectRatio: '16:9' | '9:16' | '1:1' | '4:3' = '16:9',
  userApiKey?: string
): Promise<string> {
  const apiKey = userApiKey || getGeminiApiKey();

  if (!apiKey) {
    // Generate an HD synthetic visual poster representation
    return `DEMO_POSTER:${sizeResolution}:${aspectRatio}:${prompt}`;
  }

  try {
    const url = `https://generativelanguage.googleapis.com/v1beta/models/${modelName}:generateContent?key=${apiKey}`;
    const enhancedPrompt = `${prompt}, Ultra High Definition ${sizeResolution} resolution wallpaper, ${aspectRatio} aspect ratio, cinematic lighting, photorealistic 8K detail, vivid colors, broadcast quality`;

    const requestBody = {
      contents: [
        {
          parts: [{ text: enhancedPrompt }]
        }
      ],
      generationConfig: {
        responseModalities: ['IMAGE'],
        imageConfig: {
          aspectRatio,
          imageSize: sizeResolution === '4K' ? '3840x2160' : sizeResolution === '2K' ? '2048x1152' : '1024x576'
        }
      }
    };

    const response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(requestBody)
    });

    if (response.ok) {
      const data = await response.json();
      const parts = data?.candidates?.[0]?.content?.parts || [];
      for (const part of parts) {
        if (part.inlineData?.data) {
          return `data:${part.inlineData.mimeType || 'image/jpeg'};base64,${part.inlineData.data}`;
        }
      }
    }

    // Fallback attempt with Imagen 3
    const imagenUrl = `https://generativelanguage.googleapis.com/v1beta/models/imagen-3.0-generate-002:predict?key=${apiKey}`;
    const imagenBody = {
      instances: [{ prompt: enhancedPrompt }],
      parameters: {
        sampleCount: 1,
        aspectRatio: aspectRatio,
        outputOptions: { mimeType: 'image/jpeg' }
      }
    };

    const imgResponse = await fetch(imagenUrl, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(imagenBody)
    });

    if (imgResponse.ok) {
      const imgData = await imgResponse.json();
      const b64 = imgData?.predictions?.[0]?.bytesBase64Encoded;
      if (b64) {
        return `data:image/jpeg;base64,${b64}`;
      }
    }

    return `DEMO_POSTER:${sizeResolution}:${aspectRatio}:${prompt}`;
  } catch (err) {
    console.error('Error generating image:', err);
    return `DEMO_POSTER:${sizeResolution}:${aspectRatio}:${prompt}`;
  }
}

function getOfflineSmartChatReply(prompt: string, modelName: string): string {
  const lower = prompt.toLowerCase();
  if (lower.includes('t sports') || lower.includes('cricket') || lower.includes('bpl') || lower.includes('match')) {
    return `**🏏 BD IPTV Sports Schedule & Live Coverage**\n\n` +
      `• **T Sports HD**: Live coverage of Bangladesh Cricket Series & BPL with Bangla commentary.\n` +
      `• **Star Sports 1 HD**: Live international ICC & IPL tournaments with 4K HDR feed.\n` +
      `• **Sony Sports Ten 1 HD**: UEFA Champions League, Premier League & tennis grand slams.\n\n` +
      `💡 *Pro Tip:* Tap on the **T Sports HD** channel on your TV guide to start direct live streaming in Full HD 60FPS!`;
  }

  if (lower.includes('drama') || lower.includes('natok') || lower.includes('zee bangla') || lower.includes('serial')) {
    return `**🎭 Top Bengali Natok & Drama Serials on BD IPTV**\n\n` +
      `1. **Mithai / Anurager Chhowa** (Zee Bangla & Star Jalsha HD) - Airing evenings at 19:30.\n` +
      `2. **Eid & Weekend Special Natok** (NTV HD & Channel i) - Featuring top Bangladeshi directors.\n` +
      `3. **Dutta & Bouma** (Colors Bangla HD) - Daily family primetime special.\n\n` +
      `Would you like me to set a reminder or pull up the live stream for any of these?`;
  }

  if (lower.includes('movie') || lower.includes('cinema') || lower.includes('film')) {
    return `**🎬 Blockbuster Cinema Showcase on BD IPTV**\n\n` +
      `• **Star Gold HD & Sony MAX**: Action blockbusters with dual Bengali / Hindi audio.\n` +
      `• **Jalsha Movies HD**: Classic and modern Bengali films starring Prosenjit, Dev & Shakib Khan.\n` +
      `• **Star Movies & HBO**: Hollywood cinematic hits in crisp 1080p surround sound.`;
  }

  if (lower.includes('url') || lower.includes('m3u') || lower.includes('stream')) {
    return `**📡 BD IPTV Custom Stream & Playlist Setup**\n\n` +
      `BD IPTV supports standard HLS (\`.m3u8\`), MP4, and TS streams. You can click the **'+ Add Stream'** button on the top navigation bar to paste any custom BDIX or IPTV stream URL and play it instantly with the built-in video player!`;
  }

  return `👋 **Welcome to BD IPTV AI Media Guide!** (Powered by \`${modelName}\`)\n\n` +
    `I can assist you with:\n` +
    `• Live Cricket & Football match timings and channel guides (T Sports, Star Sports, Sony Ten)\n` +
    `• Bengali Drama & Natok schedules on Zee Bangla, Star Jalsha, NTV, Channel i\n` +
    `• Movie recommendations and international TV guides\n` +
    `• Stream troubleshooting and 4K AI TV poster generation\n\n` +
    `What would you like to watch or discover today?`;
}
