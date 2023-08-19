module.exports = {
  env: {
    browser: true,
    es2021: true,
  },
  extends: [
    'airbnb-base',
    'plugin:vue/vue3-essential',
  ],
  overrides: [
    {
      env: {
        node: true,
      },
      files: [
        '.eslintrc.{js,cjs}',
      ],
      parserOptions: {
        sourceType: 'script',
      },
    },
  ],
  parserOptions: {
    ecmaVersion: 'latest',
    sourceType: 'module',
  },
  plugins: [
    'vue',
  ],
  rules: {
    'vue/no-reserved-component-names': ['warn', {}],
    'no-unused-vars': 'warn',
    'max-len': ['error', {
      code: 140,
      ignorePattern: '\\s*\\w+=["\'].*["\']', // html attributes
    }],
    'vue/multi-word-component-names': ['warn', {
      ignores: [],
    }],
  },
};
