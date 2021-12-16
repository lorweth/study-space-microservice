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
  const groupTimeTableSample = {
    examId: 59325,
    startAt: '2021-12-16T08:59:33.506Z',
    endAt: '2021-12-15T19:04:10.084Z',
    groupId: 'Facilitator lavender Chair',
  };

  let groupTimeTable: any;

  beforeEach(() => {
    cy.getOauth2Data();
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogin(oauth2Data, username, password);
    });
    cy.intercept('GET', '/services/answerstore/api/group-time-tables').as('entitiesRequest');
    cy.visit('');
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    Cypress.Cookies.preserveOnce('XSRF-TOKEN', 'JSESSIONID');
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/answerstore/api/group-time-tables+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/answerstore/api/group-time-tables').as('postEntityRequest');
    cy.intercept('DELETE', '/services/answerstore/api/group-time-tables/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (groupTimeTable) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/answerstore/api/group-time-tables/${groupTimeTable.id}`,
      }).then(() => {
        groupTimeTable = undefined;
      });
    }
  });

  afterEach(() => {
    cy.oauthLogout();
    cy.clearCache();
  });

  it('GroupTimeTables menu should load GroupTimeTables page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('group-time-table');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GroupTimeTable').should('exist');
    cy.url().should('match', groupTimeTablePageUrlPattern);
  });

  describe('GroupTimeTable page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(groupTimeTablePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GroupTimeTable page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/group-time-table/new$'));
        cy.getEntityCreateUpdateHeading('GroupTimeTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupTimeTablePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/answerstore/api/group-time-tables',
          body: groupTimeTableSample,
        }).then(({ body }) => {
          groupTimeTable = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/answerstore/api/group-time-tables+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [groupTimeTable],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(groupTimeTablePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GroupTimeTable page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('groupTimeTable');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupTimeTablePageUrlPattern);
      });

      it('edit button click should load edit GroupTimeTable page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GroupTimeTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupTimeTablePageUrlPattern);
      });

      it('last delete button click should delete instance of GroupTimeTable', () => {
        cy.intercept('GET', '/services/answerstore/api/group-time-tables/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('groupTimeTable').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupTimeTablePageUrlPattern);

        groupTimeTable = undefined;
      });
    });
  });

  describe('new GroupTimeTable page', () => {
    beforeEach(() => {
      cy.visit(`${groupTimeTablePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('GroupTimeTable');
    });

    it('should create an instance of GroupTimeTable', () => {
      cy.get(`[data-cy="examId"]`).type('6778').should('have.value', '6778');

      cy.get(`[data-cy="startAt"]`).type('2021-12-15T23:29').should('have.value', '2021-12-15T23:29');

      cy.get(`[data-cy="endAt"]`).type('2021-12-16T04:48').should('have.value', '2021-12-16T04:48');

      cy.get(`[data-cy="groupId"]`).type('Dam').should('have.value', 'Dam');

      cy.get(`[data-cy="note"]`).type('holistic Soft Wooden').should('have.value', 'holistic Soft Wooden');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        groupTimeTable = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', groupTimeTablePageUrlPattern);
    });
  });
});
