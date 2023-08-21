module.exports = {
  "env": {
    "browser": true,
    "es2021": true
  },
  "parser": '@typescript-eslint/parser',
  "plugins": ['@typescript-eslint'],
  "root": true,
  "overrides": [
    {
      "env": {
        "node": true
      },
      "files": ['*.ts', '*.tsx'],
      "extends": [
        'eslint:recommended',
        'plugin:@typescript-eslint/recommended-type-checked',
        'plugin:@typescript-eslint/stylistic-type-checked',
        'airbnb-base',
        'airbnb-typescript/base'
      ],
      "parserOptions": {
        "sourceType": "script",
        "project": ['./tsconfig.json'],
      },
      "rules": {
        "indent": ["error", 2, { "SwitchCase": 1 }],
        "no-case-declarations": ["warn"],
        "class-methods-use-this": "off"
      }
    }
  ],
  "parserOptions": {
    "ecmaVersion": "latest",
    "sourceType": "module",
  },
}
