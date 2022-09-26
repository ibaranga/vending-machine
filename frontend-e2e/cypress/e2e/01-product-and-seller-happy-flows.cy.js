describe("product-and-seller-happy-flows", () => {
  const sellerUsername = `seller_${randInt(100000)}`;
  const sellerPassword = `secret_${randInt(100000)}`;
  const products = [
    { name: "Cola", cost: 1.5, amount: 4 },
    { name: "Pepsi", cost: 1.4, amount: 0 },
    { name: "Sprite", cost: 1.54, amount: 3 },
    { name: "Fanta", cost: 1.2, amount: 1 },
  ];
  const buyerUsername = `buyer_${randInt(100000)}`;
  const buyerPassword = `secret_${randInt(100000)}`;

  it("should register & login as seller, be able to create and view products than logout", () => {
    cy.register(sellerUsername, sellerPassword, "seller");
    cy.login(sellerUsername, sellerPassword);
    for (let index = 0; index < products.length; index++) {
      createProduct(products[index], index);
    }
    cy.logout();
  });

  it("should register & login as buyer, be able to view products, deposit coins and buy", () => {
    cy.register(buyerUsername, buyerPassword, "buyer");
    cy.login(buyerUsername, buyerPassword);
    depositCoins(100);
    checkBalance(1.0);
    checkBuyDisabled("Fanta");
    checkBuyDisabled("Pepsi");

    depositCoins(50);
    checkBalance(1.5);
    depositCoins(5);
    checkBalance(1.55);
    checkBuyEnabled("Fanta");
    checkBuyDisabled("Pepsi");

    buyProduct("Fanta");
    checkBalance(0);
    cy.get("body").click();

    // logout();
  });

  function depositCoins(coin) {
    cy.get(`#deposit-coin-${coin}`).click();
  }

  function checkBalance(balance) {
    cy.get("#balance-value").contains(balance);
  }

  function checkBuyEnabled(product) {
    cy.get(`#buy-product-${product}`).should("be.enabled");
  }

  function checkBuyDisabled(product) {
    cy.get(`#buy-product-${product}`).should("not.be.enabled");
  }

  function buyProduct(product) {
    cy.get(`#buy-product-${product}`).click();
  }

  function createProduct(data, index) {
    cy.get("#add-product").click();
    updateProduct(data, index);
  }

  function updateProduct(data, index) {
    cy.get(`#product-${index}-productName-editor`).clear().type(data.name);
    cy.get(`#product-${index}-cost-editor`).clear().type(data.cost).blur();
    cy.get(`#product-${index}-amountAvailable-editor`)
      .clear()
      .type(data.amount);
    cy.get(`#save-product-${index}`).click();
  }

  function randInt(max) {
    return +(Math.random() * max).toFixed(0);
  }
});
