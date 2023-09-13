# Upload Service

This API is responsible for receiving a image from a request, compress and convert the image using [Sharp](https://sharp.pixelplumbing.com/) and them saving it somewhere else (database,local,s3)

### Main technologies

- [Sharp](https://sharp.pixelplumbing.com/): A library for image compression and conversion
- [TypeScript](https://www.typescriptlang.org/): A statically-typed superset of JavaScript
- [Express](https://expressjs.com/): Web framework for building web applications and APIs

## Requirements

- Node v18

## Project setup

1. Install project packages
```sh
npm ci
```
2. Copy `.env.example` to `.env` and fill the variables
3. Generate Prisma database schema
```sh
npm run prisma:generate
```

### Compile code

```sh
npm run build
```

### Compile code and run

```sh
npm run start
```
