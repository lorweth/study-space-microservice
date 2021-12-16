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
  const answerSheetItemSample = { questionId: 50772, answerId: 53175 };

  let answerSheetItem: any;

  beforeEach(() => {
    cy.getOauth2Data();
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogin(oauth2Data, username, password);
    });
    cy.intercept('GET', '/services/answerstore/api/answer-sheet-items').as('entitiesRequest');
    cy.visit('');
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    Cypress.Cookies.preserveOnce('XSRF-TOKEN', 'JSESSIONID');
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/answerstore/api/answer-sheet-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/answerstore/api/answer-sheet-items').as('postEntityRequest');
    cy.intercept('DELETE', '/services/answerstore/api/answer-sheet-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (answerSheetItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/answerstore/api/answer-sheet-items/${answerSheetItem.id}`,
      }).then(() => {
        answerSheetItem = undefined;
      });
    }
  });

  afterEach(() => {
    cy.oauthLogout();
    cy.clearCache();
  });

  it('AnswerSheetItems menu should load AnswerSheetItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('answer-sheet-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AnswerSheetItem').should('exist');
    cy.url().should('match', answerSheetItemPageUrlPattern);
  });

  describe('AnswerSheetItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(answerSheetItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AnswerSheetItem page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/answer-sheet-item/new$'));
        cy.getEntityCreateUpdateHeading('AnswerSheetItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', answerSheetItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/answerstore/api/answer-sheet-items',
          body: answerSheetItemSample,
        }).then(({ body }) => {
          answerSheetItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/answerstore/api/answer-sheet-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [answerSheetItem],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(answerSheetItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AnswerSheetItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('answerSheetItem');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', answerSheetItemPageUrlPattern);
      });

      it('edit button click should load edit AnswerSheetItem page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AnswerSheetItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', answerSheetItemPageUrlPattern);
      });

      it('last delete button click should delete instance of AnswerSheetItem', () => {
        cy.intercept('GET', '/services/answerstore/api/answer-sheet-items/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('answerSheetItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', answerSheetItemPageUrlPattern);

        answerSheetItem = undefined;
      });
    });
  });

  describe('new AnswerSheetItem page', () => {
    beforeEach(() => {
      cy.visit(`${answerSheetItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AnswerSheetItem');
    });

    it('should create an instance of AnswerSheetItem', () => {
      cy.get(`[data-cy="questionId"]`).type('43917').should('have.value', '43917');

      cy.get(`[data-cy="answerId"]`).type('6811').should('have.value', '6811');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        answerSheetItem = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', answerSheetItemPageUrlPattern);
    });
  });
});
