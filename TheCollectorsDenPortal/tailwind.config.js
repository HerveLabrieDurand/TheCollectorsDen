/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: "class",
  content: ["./src/**/*.{html,ts}", "./src/styles.css"],
  theme: {
    fontFamily: {
      dynapuf: ["DynaPuff", "serif"],
    },
    colors: {
      white: "#FFFFFF",
      black: "#000000",
      dark: {
        background: "#0D1117",
        container: "#151B24",
        border: "#3C444D",
        title: "#EDEDEE",
        text: "#AAAEBD",
        highlight: {
          cta: "#5640B0",
          green: "#00D691",
          red: "#FF3B5E",
          blue: "#1E8DFE",
          orange: "#D9542F",
          yellow: "#FFDF1E",
        },
      },
      light: {
        background: "#FFFFFF",
        container: "#F6F8FA",
        border: "#DFE4EA",
        title: "#000000",
        text: "#57595D",
        highlight: {
          cta: "#7965C8",
          green: "#00D691",
          red: "#FF3B5E",
          blue: "#1E8DFE",
          orange: "#D9542F",
          yellow: "#FFDF1E",
        },
      },
    },
  },
  plugins: [],
};
