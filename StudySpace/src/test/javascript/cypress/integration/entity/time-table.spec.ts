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

describe('TimeTable e2e test', () => {
  const timeTablePageUrl = '/time-table';
  const timeTablePageUrlPattern = new RegExp('/time-table(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const timeTableSample = { note: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=', userLogin: 'deposit turn-key' };

  let timeTable: any;

  beforeEach(() => {
    cy.getOauth2Data();
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogin(oauth2Data, username, password);
    });
    cy.intercept('GET', '/services/answerstore/api/time-tables').as('entitiesRequest');
    cy.visit('');
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    Cypress.Cookies.preserveOnce('XSRF-TOKEN', 'JSESSIONID');
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/answerstore/api/time-tables+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/answerstore/api/time-tables').as('postEntityRequest');
    cy.intercept('DELETE', '/services/answerstore/api/time-tables/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (timeTable) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/answerstore/api/time-tables/${timeTable.id}`,
      }).then(() => {
        timeTable = undefined;
      });
    }
  });

  afterEach(() => {
    cy.oauthLogout();
    cy.clearCache();
  });

  it('TimeTables menu should load TimeTables page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('time-table');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TimeTable').should('exist');
    cy.url().should('match', timeTablePageUrlPattern);
  });

  describe('TimeTable page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(timeTablePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TimeTable page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/time-table/new$'));
        cy.getEntityCreateUpdateHeading('TimeTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', timeTablePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/answerstore/api/time-tables',
          body: timeTableSample,
        }).then(({ body }) => {
          timeTable = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/answerstore/api/time-tables+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [timeTable],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(timeTablePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TimeTable page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('timeTable');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', timeTablePageUrlPattern);
      });

      it('edit button click should load edit TimeTable page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TimeTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', timeTablePageUrlPattern);
      });

      it('last delete button click should delete instance of TimeTable', () => {
        cy.intercept('GET', '/services/answerstore/api/time-tables/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('timeTable').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', timeTablePageUrlPattern);

        timeTable = undefined;
      });
    });
  });

  describe('new TimeTable page', () => {
    beforeEach(() => {
      cy.visit(`${timeTablePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TimeTable');
    });

    it('should create an instance of TimeTable', () => {
      cy.get(`[data-cy="title"]`).type('hierarchy Progressive JSON').should('have.value', 'hierarchy Progressive JSON');

      cy.get(`[data-cy="time"]`).type('2021-12-16T01:51').should('have.value', '2021-12-16T01:51');

      cy.get(`[data-cy="note"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="userLogin"]`).type('Land').should('have.value', 'Land');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        timeTable = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', timeTablePageUrlPattern);
    });
  });
});
