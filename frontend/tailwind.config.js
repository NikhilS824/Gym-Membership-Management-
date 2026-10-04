/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        ink: '#111827',
        muted: '#667085',
        accent: '#c8f169',
        canvas: '#f5f6f3',
        line: '#e8eae5',
      },
      fontFamily: {
        sans: ['Inter', 'ui-sans-serif', 'system-ui', 'sans-serif'],
      },
      boxShadow: {
        card: '0 8px 28px rgba(24, 35, 23, .045)',
      },
    },
  },
  plugins: [],
}
