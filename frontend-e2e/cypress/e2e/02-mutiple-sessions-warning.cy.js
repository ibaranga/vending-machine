describe("mutiple-sessions-warning", () => {
  it("should register -> login -> logout -> login without getting any sort of warnings", () => {
    const username = `user_${randInt(100000)}`;
    const password = `secret_${randInt(100000)}`;

    cy.register(username, password, "buyer");
    cy.login(username, password);
    cy.logout();
    cy.login(username, password);
    cy.logout();
  });

  it("login -> logout -> should generate warning about multiple sessions", () => {
    const username = `user_${randInt(100000)}`;
    const password = `secret_${randInt(100000)}`;

    cy.register(username, password, "buyer");

    cy.session("session1", () => {
      cy.login(username, password);
      cy.get("#multiple-sessions-alert").should("not.exist");
    });
    cy.session("session2", () => {
      cy.login(username, password);
      cy.get("#multiple-sessions-alert").should("exist");
      cy.get("#logout-everywhere").click();
    });
  });

  function randInt(max) {
    return +(Math.random() * max).toFixed(0);
  }
});
