/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx,css}'],
  theme: {
    extend: {
      colors: {
        verno: {
          bg: 'rgb(var(--verno-bg-rgb) / <alpha-value>)',
          surface: 'rgb(var(--verno-surface-rgb) / <alpha-value>)',
          'surface-light':
            'rgb(var(--verno-surface-light-rgb) / <alpha-value>)',
          dark: 'rgb(var(--verno-dark-rgb) / <alpha-value>)',
          'dark-hover': 'rgb(var(--verno-dark-hover-rgb) / <alpha-value>)',
          darker: 'rgb(var(--verno-darker-rgb) / <alpha-value>)',
          accent: 'rgb(var(--verno-accent-rgb) / <alpha-value>)',
        },
      },
    },
  },
  plugins: [],
}
