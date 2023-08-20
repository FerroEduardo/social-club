module.exports = {
  "env": {
    "browser": true,
    "es2021": true
  },
  "extends": [
    'eslint:recommended',
    'plugin:@typescript-eslint/recommended-type-checked',
    'plugin:@typescript-eslint/stylistic-type-checked',
    'airbnb-base',
    'airbnb-typescript/base'
  ],
  "parser": '@typescript-eslint/parser',
  "plugins": ['@typescript-eslint'],
  "root": true,
  "overrides": [
    {
      "env": {
        "node": true
      },
      "files": [
        ".eslintrc.{js,cjs}"
      ],
      "parserOptions": {
        "sourceType": "script"
      }
    }
  ],
  "parserOptions": {
    "ecmaVersion": "latest",
    "sourceType": "module",
    "project": './tsconfig.json'
  },
  "rules": {
    "indent": ["error", 2]
  }
}
