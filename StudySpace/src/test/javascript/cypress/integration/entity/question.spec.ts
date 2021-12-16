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

describe('Question e2e test', () => {
  const questionPageUrl = '/question';
  const questionPageUrlPattern = new RegExp('/question(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const questionSample = { content: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' };

  let question: any;

  beforeEach(() => {
    cy.getOauth2Data();
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogin(oauth2Data, username, password);
    });
    cy.intercept('GET', '/services/examstore/api/questions').as('entitiesRequest');
    cy.visit('');
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    Cypress.Cookies.preserveOnce('XSRF-TOKEN', 'JSESSIONID');
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/examstore/api/questions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/examstore/api/questions').as('postEntityRequest');
    cy.intercept('DELETE', '/services/examstore/api/questions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (question) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/examstore/api/questions/${question.id}`,
      }).then(() => {
        question = undefined;
      });
    }
  });

  afterEach(() => {
    cy.oauthLogout();
    cy.clearCache();
  });

  it('Questions menu should load Questions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('question');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Question').should('exist');
    cy.url().should('match', questionPageUrlPattern);
  });

  describe('Question page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(questionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Question page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/question/new$'));
        cy.getEntityCreateUpdateHeading('Question');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', questionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/examstore/api/questions',
          body: questionSample,
        }).then(({ body }) => {
          question = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/examstore/api/questions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [question],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(questionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Question page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('question');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', questionPageUrlPattern);
      });

      it('edit button click should load edit Question page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Question');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', questionPageUrlPattern);
      });

      it('last delete button click should delete instance of Question', () => {
        cy.intercept('GET', '/services/examstore/api/questions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('question').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', questionPageUrlPattern);

        question = undefined;
      });
    });
  });

  describe('new Question page', () => {
    beforeEach(() => {
      cy.visit(`${questionPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Question');
    });

    it('should create an instance of Question', () => {
      cy.get(`[data-cy="content"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="note"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        question = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', questionPageUrlPattern);
    });
  });
});
