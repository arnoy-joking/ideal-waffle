/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        bdiptv: {
          green: '#00E676',
          greenDark: '#00B050',
          red: '#FF334B',
          gold: '#FFB800',
          cyan: '#00E5FF',
          bg: '#0A0D14',
          surface: '#131823',
          surfaceElevated: '#1C2232',
          border: '#263044'
        }
      }
    },
  },
  plugins: [],
}
