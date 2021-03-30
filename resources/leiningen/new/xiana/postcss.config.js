const tailwindcss = require('tailwindcss');
const discard = require('postcss-discard-comments');
const cssnano = require('cssnano')({
    preset: 'default',
})
const purgecss = require('@fullhuman/postcss-purgecss')({
    content: [
        './src/frontend/**/*.css',
        './src/frontend/**/*.cljs',
        './resources/public/**/*.css',
    ],

    defaultExtractor: (content) => content.match(/[\w-/:]+(?<!:)/g) || [],
});

module.exports = {
    plugins: [
        tailwindcss('./tailwind.config.js'),
        require('autoprefixer'),
        ...process.env.NODE_ENV === 'production'
            ? [purgecss, discard, cssnano]
            : [],
    ],
};
