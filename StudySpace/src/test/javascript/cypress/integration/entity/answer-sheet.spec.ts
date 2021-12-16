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
  const answerSheetSample = { time: '2021-12-15T15:55:40.030Z', userLogin: 'JBOD payment' };

  let answerSheet: any;

  beforeEach(() => {
    cy.getOauth2Data();
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogin(oauth2Data, username, password);
    });
    cy.intercept('GET', '/services/answerstore/api/answer-sheets').as('entitiesRequest');
    cy.visit('');
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    Cypress.Cookies.preserveOnce('XSRF-TOKEN', 'JSESSIONID');
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/answerstore/api/answer-sheets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/answerstore/api/answer-sheets').as('postEntityRequest');
    cy.intercept('DELETE', '/services/answerstore/api/answer-sheets/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (answerSheet) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/answerstore/api/answer-sheets/${answerSheet.id}`,
      }).then(() => {
        answerSheet = undefined;
      });
    }
  });

  afterEach(() => {
    cy.oauthLogout();
    cy.clearCache();
  });

  it('AnswerSheets menu should load AnswerSheets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('answer-sheet');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AnswerSheet').should('exist');
    cy.url().should('match', answerSheetPageUrlPattern);
  });

  describe('AnswerSheet page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(answerSheetPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AnswerSheet page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/answer-sheet/new$'));
        cy.getEntityCreateUpdateHeading('AnswerSheet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', answerSheetPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/answerstore/api/answer-sheets',
          body: answerSheetSample,
        }).then(({ body }) => {
          answerSheet = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/answerstore/api/answer-sheets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [answerSheet],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(answerSheetPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AnswerSheet page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('answerSheet');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', answerSheetPageUrlPattern);
      });

      it('edit button click should load edit AnswerSheet page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AnswerSheet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', answerSheetPageUrlPattern);
      });

      it('last delete button click should delete instance of AnswerSheet', () => {
        cy.intercept('GET', '/services/answerstore/api/answer-sheets/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('answerSheet').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', answerSheetPageUrlPattern);

        answerSheet = undefined;
      });
    });
  });

  describe('new AnswerSheet page', () => {
    beforeEach(() => {
      cy.visit(`${answerSheetPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AnswerSheet');
    });

    it('should create an instance of AnswerSheet', () => {
      cy.get(`[data-cy="time"]`).type('2021-12-16T10:25').should('have.value', '2021-12-16T10:25');

      cy.get(`[data-cy="userLogin"]`).type('Checking').should('have.value', 'Checking');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        answerSheet = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', answerSheetPageUrlPattern);
    });
  });
});
