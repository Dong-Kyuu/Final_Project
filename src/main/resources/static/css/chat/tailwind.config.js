/**
 * npm install -D tailwindcss
 * npx tailwindcss init
 * npx tailwindcss -i ./tailwind.config.css -o ./tailwind.css
 * */

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["../../../**/**/*.{html,js}"],
  theme: {
    extend: {},
  },
  plugins: []
}

