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

describe('GroupTimeTable e2e test', () => {
  const groupTimeTablePageUrl = '/group-time-table';
  const groupTimeTablePageUrlPattern = new RegExp('/group-time-table(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  beforeEach(() => {
    cy.getOauth2Data();
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogin(oauth2Data, username, password);
    });
    cy.intercept('GET', '/services/answerstore/api/group-time-tables').as('entitiesRequest');
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
    cy.intercept('GET', '/services/answerstore/api/group-time-tables+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/answerstore/api/group-time-tables').as('postEntityRequest');
    cy.intercept('DELETE', '/services/answerstore/api/group-time-tables/*').as('deleteEntityRequest');
  });

  it('should load GroupTimeTables', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('group-time-table');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GroupTimeTable').should('exist');
    cy.url().should('match', groupTimeTablePageUrlPattern);
  });

  it('should load details GroupTimeTable page', function () {
    cy.visit(groupTimeTablePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('groupTimeTable');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', groupTimeTablePageUrlPattern);
  });

  it('should load create GroupTimeTable page', () => {
    cy.visit(groupTimeTablePageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('GroupTimeTable');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', groupTimeTablePageUrlPattern);
  });

  it('should load edit GroupTimeTable page', function () {
    cy.visit(groupTimeTablePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('GroupTimeTable');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', groupTimeTablePageUrlPattern);
  });

  it('should create an instance of GroupTimeTable', () => {
    cy.visit(groupTimeTablePageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('GroupTimeTable');

    cy.get(`[data-cy="examId"]`).type('6778').should('have.value', '6778');

    cy.get(`[data-cy="startAt"]`).type('2021-08-31T19:28').should('have.value', '2021-08-31T19:28');

    cy.get(`[data-cy="endAt"]`).type('2021-09-01T00:47').should('have.value', '2021-09-01T00:47');

    cy.get(`[data-cy="groupId"]`).type('3959').should('have.value', '3959');

    cy.get(`[data-cy="note"]`).type('Gloves holistic').should('have.value', 'Gloves holistic');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', groupTimeTablePageUrlPattern);
  });

  it('should delete last instance of GroupTimeTable', function () {
    cy.intercept('GET', '/services/answerstore/api/group-time-tables/*').as('dialogDeleteRequest');
    cy.visit(groupTimeTablePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('groupTimeTable').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', groupTimeTablePageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
