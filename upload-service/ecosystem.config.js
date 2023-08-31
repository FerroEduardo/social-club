module.exports = {
  apps: [{
    name: "upload-service",
    script: "./dist/index.js",
    cwd: "/var/www/project/upload-service/",
    instances: 1,
    watch: ["dist"],
    watch_delay: 10000,
  }]
}
