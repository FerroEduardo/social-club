<div align="center">

# Social Club
</div>

<div align="center">

[![build-core](https://github.com/FerroEduardo/social-club/actions/workflows/build-core.yaml/badge.svg)](https://github.com/FerroEduardo/social-club/actions/workflows/build-core.yaml)
[![build-frontend](https://github.com/FerroEduardo/social-club/actions/workflows/build-front.yaml/badge.svg)](https://github.com/FerroEduardo/social-club/actions/workflows/build-front.yaml)
[![build-upload-service](https://github.com/FerroEduardo/social-club/actions/workflows/build-upload-service.yaml/badge.svg)](https://github.com/FerroEduardo/social-club/actions/workflows/build-upload-service.yaml)
[![build-gateway](https://github.com/FerroEduardo/social-club/actions/workflows/build-gateway.yaml/badge.svg)](https://github.com/FerroEduardo/social-club/actions/workflows/build-gateway.yaml)

[![deploy-core](https://github.com/FerroEduardo/social-club/actions/workflows/deploy-core.yaml/badge.svg)](https://github.com/FerroEduardo/social-club/actions/workflows/deploy-core.yaml)
[![deploy-front](https://github.com/FerroEduardo/social-club/actions/workflows/deploy-front.yaml/badge.svg)](https://github.com/FerroEduardo/social-club/actions/workflows/deploy-front.yaml)
[![deploy-upload-service](https://github.com/FerroEduardo/social-club/actions/workflows/deploy-upload-service.yaml/badge.svg)](https://github.com/FerroEduardo/social-club/actions/workflows/deploy-upload-service.yaml)
[![deploy-gateway](https://github.com/FerroEduardo/social-club/actions/workflows/deploy-gateway.yaml/badge.svg)](https://github.com/FerroEduardo/social-club/actions/workflows/deploy-gateway.yaml)

</div>

<div align="justify">
Social Club is a social media platform centered around gaming enthusiasts, providing a photo-sharing experience akin to Instagram and Reddit. It empowers users to share their gaming adventures through images while fostering connections with fellow gaming enthusiasts.
</div>

## Features

Modules:
- [Frontend](/frontend)
- [Core](/core-backend)
- [Upload Service](/upload-service)

### Architecture

![architecture](/docs/architecture.png)

### Authentication via OAuth 2
- Social Club offers secure and convenient login options through OAuth 2 with support for Google and GitHub providers.

### Create, Edit, and Delete Posts
- Users can create, edit, and delete posts, with each post requiring at least one image.

### Commenting System
- Social Club enables users to engage in discussions by creating and removing comments on posts.

### Voting on Posts
- Users can express their opinions on posts with three types of votes: like, dislike, and neutral.

### Image Processing
- Before storage, all post images undergo processing, including compression and conversion.

### Storage Options
- Social Club offers multiple storage options for post images, including:
  - Local storage
  - Database storage
  - ~~AWS S3~~

## Main Technologies

- **Spring Boot (3.2.0-M2)**: Spring Boot powers the core backend API.
- **Vue 3 + Vite**: Framework for building user interfaces.
- **TypeScript**: A statically-typed superset of JavaScript.
- **Express**: Web framework for building web applications and APIs.
- **Sharp**: A library for image compression and conversion.

## Screenshots

### Homepage
![homepage](/docs/homepage.png)

### Timeline
![timeline](/docs/timeline.png)

### Create post
![post-create](/docs/post-create.png)

### Post Details
![post](/docs/post.png)

### Game List
![game-list](/docs/game-list.png)
