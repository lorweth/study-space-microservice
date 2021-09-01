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

describe('AnswerSheetItem e2e test', () => {
  const answerSheetItemPageUrl = '/answer-sheet-item';
  const answerSheetItemPageUrlPattern = new RegExp('/answer-sheet-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  beforeEach(() => {
    cy.getOauth2Data();
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogin(oauth2Data, username, password);
    });
    cy.intercept('GET', '/services/answerstore/api/answer-sheet-items').as('entitiesRequest');
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
    cy.intercept('GET', '/services/answerstore/api/answer-sheet-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/answerstore/api/answer-sheet-items').as('postEntityRequest');
    cy.intercept('DELETE', '/services/answerstore/api/answer-sheet-items/*').as('deleteEntityRequest');
  });

  it('should load AnswerSheetItems', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('answer-sheet-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AnswerSheetItem').should('exist');
    cy.url().should('match', answerSheetItemPageUrlPattern);
  });

  it('should load details AnswerSheetItem page', function () {
    cy.visit(answerSheetItemPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('answerSheetItem');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', answerSheetItemPageUrlPattern);
  });

  it('should load create AnswerSheetItem page', () => {
    cy.visit(answerSheetItemPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('AnswerSheetItem');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', answerSheetItemPageUrlPattern);
  });

  it('should load edit AnswerSheetItem page', function () {
    cy.visit(answerSheetItemPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('AnswerSheetItem');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', answerSheetItemPageUrlPattern);
  });

  it('should create an instance of AnswerSheetItem', () => {
    cy.visit(answerSheetItemPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('AnswerSheetItem');

    cy.get(`[data-cy="questionId"]`).type('43917').should('have.value', '43917');

    cy.get(`[data-cy="answerId"]`).type('6811').should('have.value', '6811');

    cy.setFieldSelectToLastOfEntity('answerSheet');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', answerSheetItemPageUrlPattern);
  });

  it('should delete last instance of AnswerSheetItem', function () {
    cy.intercept('GET', '/services/answerstore/api/answer-sheet-items/*').as('dialogDeleteRequest');
    cy.visit(answerSheetItemPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('answerSheetItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', answerSheetItemPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
