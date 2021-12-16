import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Option e2e test', () => {
  const optionPageUrl = '/option';
  const optionPageUrlPattern = new RegExp('/option(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const optionSample = { content: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=', isCorrect: true };

  let option: any;

  beforeEach(() => {
    cy.getOauth2Data();
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogin(oauth2Data, username, password);
    });
    cy.intercept('GET', '/services/examstore/api/options').as('entitiesRequest');
    cy.visit('');
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    Cypress.Cookies.preserveOnce('XSRF-TOKEN', 'JSESSIONID');
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/examstore/api/options+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/examstore/api/options').as('postEntityRequest');
    cy.intercept('DELETE', '/services/examstore/api/options/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (option) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/examstore/api/options/${option.id}`,
      }).then(() => {
        option = undefined;
      });
    }
  });

  afterEach(() => {
    cy.oauthLogout();
    cy.clearCache();
  });

  it('Options menu should load Options page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('option');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Option').should('exist');
    cy.url().should('match', optionPageUrlPattern);
  });

  describe('Option page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(optionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Option page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/option/new$'));
        cy.getEntityCreateUpdateHeading('Option');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', optionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/examstore/api/options',
          body: optionSample,
        }).then(({ body }) => {
          option = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/examstore/api/options+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [option],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(optionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Option page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('option');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', optionPageUrlPattern);
      });

      it('edit button click should load edit Option page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Option');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', optionPageUrlPattern);
      });

      it('last delete button click should delete instance of Option', () => {
        cy.intercept('GET', '/services/examstore/api/options/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('option').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', optionPageUrlPattern);

        option = undefined;
      });
    });
  });

  describe('new Option page', () => {
    beforeEach(() => {
      cy.visit(`${optionPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Option');
    });

    it('should create an instance of Option', () => {
      cy.get(`[data-cy="content"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="isCorrect"]`).should('not.be.checked');
      cy.get(`[data-cy="isCorrect"]`).click().should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        option = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', optionPageUrlPattern);
    });
  });
});
