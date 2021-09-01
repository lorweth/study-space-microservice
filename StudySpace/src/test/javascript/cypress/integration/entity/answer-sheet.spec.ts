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

describe('AnswerSheet e2e test', () => {
  const answerSheetPageUrl = '/answer-sheet';
  const answerSheetPageUrlPattern = new RegExp('/answer-sheet(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  beforeEach(() => {
    cy.getOauth2Data();
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogin(oauth2Data, username, password);
    });
    cy.intercept('GET', '/services/answerstore/api/answer-sheets').as('entitiesRequest');
    cy.visit('');
    cy.get(entityItemSelector).should('exist');
  });

  afterEach(() => {
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogout(oauth2Data);
    });
    cy.clearCache();
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/answerstore/api/answer-sheets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/answerstore/api/answer-sheets').as('postEntityRequest');
    cy.intercept('DELETE', '/services/answerstore/api/answer-sheets/*').as('deleteEntityRequest');
  });

  it('should load AnswerSheets', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('answer-sheet');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AnswerSheet').should('exist');
    cy.url().should('match', answerSheetPageUrlPattern);
  });

  it('should load details AnswerSheet page', function () {
    cy.visit(answerSheetPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('answerSheet');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', answerSheetPageUrlPattern);
  });

  it('should load create AnswerSheet page', () => {
    cy.visit(answerSheetPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('AnswerSheet');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', answerSheetPageUrlPattern);
  });

  it('should load edit AnswerSheet page', function () {
    cy.visit(answerSheetPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('AnswerSheet');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', answerSheetPageUrlPattern);
  });

  it('should create an instance of AnswerSheet', () => {
    cy.visit(answerSheetPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('AnswerSheet');

    cy.get(`[data-cy="time"]`).type('2021-09-01T06:24').should('have.value', '2021-09-01T06:24');

    cy.get(`[data-cy="userLogin"]`).type('Checking').should('have.value', 'Checking');

    cy.setFieldSelectToLastOfEntity('groupTimeTable');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', answerSheetPageUrlPattern);
  });

  it('should delete last instance of AnswerSheet', function () {
    cy.intercept('GET', '/services/answerstore/api/answer-sheets/*').as('dialogDeleteRequest');
    cy.visit(answerSheetPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('answerSheet').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', answerSheetPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
