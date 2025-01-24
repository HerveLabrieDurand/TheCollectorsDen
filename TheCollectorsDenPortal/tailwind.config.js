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
      highlight: {
        green: "#00D691",
        red: "#FF3B5E",
        blue: "#1E8DFE",
        orange: "#D9542F",
        yellow: "#FFDF1E",
      },
      dark: {
        background: "#0D1117",
        border: "#3C444D",
        text: "#EDEDEE",
        primary: "#1ADEDB",
        secondary: "#113883",
        accent: "#14249F",
        container: "#03161E",
      },
      light: {
        background: "#FFFFFF",
        border: "#DFE4EA",
        text: "#000000",
        primary: "#24E2E1",
        secondary: "#7BA3EE",
        accent: "#6071EB",
        container: "#D9F8F6",
      },
    },
  },
  plugins: [],
};
