const { defineConfig } = require("cypress");

module.exports = defineConfig({
  projectId: "acme-vending-machine",
  e2e: {
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
    baseUrl: "http://localhost:3000",
    env: {
      COMMAND_DELAY: 300,
    },
    videoCompression: 1,
    experimentalSessionAndOrigin: true,
  },
});
