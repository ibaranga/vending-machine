// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })

// support/commands.js
// Set CYPRESS_COMMAND_DELAY above zero for demoing to stakeholders,
// E.g. CYPRESS_COMMAND_DELAY=1000 node_modules/.bin/cypress open
const COMMAND_DELAY = Cypress.env("COMMAND_DELAY") || 0;
if (COMMAND_DELAY > 0) {
  for (const command of ["visit", "click", "trigger", "type", "clear", "reload", "contains"]) {
    Cypress.Commands.overwrite(command, (originalFn, ...args) => {
      const origVal = originalFn(...args);

      return new Promise((resolve) => {
        setTimeout(() => {
          resolve(origVal);
        }, COMMAND_DELAY);
      });
    });
  }
}

Cypress.Commands.add("register", (username, password, role) => {
  cy.visit("/", { failOnStatusCode: true });
  cy.get("#home-tab-register").click();
  cy.get("#register-username").type(username);
  cy.get("#register-password").type(password);
  cy.get("#register-confirm-password").type(password);
  cy.get("#register-role").click();
  cy.get(`#register-role-${role}`).click();
  cy.get("#register-submit").click();
});


Cypress.Commands.add("login", (username, password) => {
  cy.visit("/");
  cy.get("#home-tab-login").click();
  cy.get("#login-username").type(username);
  cy.get("#login-password").type(password);
  cy.get("#login-submit").click();
});

Cypress.Commands.add("logout", (username, password) => {
  cy.get("#logout").click();
});

Cypress.Commands.add("expectUrlPath", (path) => {
  cy.url().should("be.equals", `${Cypress.config("baseUrl")}${path}`);
});
