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

describe('ExamItem e2e test', () => {
  const examItemPageUrl = '/exam-item';
  const examItemPageUrlPattern = new RegExp('/exam-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const examItemSample = { numOfQuestion: 36087 };

  let examItem: any;

  beforeEach(() => {
    cy.getOauth2Data();
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogin(oauth2Data, username, password);
    });
    cy.intercept('GET', '/services/examstore/api/exam-items').as('entitiesRequest');
    cy.visit('');
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    Cypress.Cookies.preserveOnce('XSRF-TOKEN', 'JSESSIONID');
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/examstore/api/exam-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/examstore/api/exam-items').as('postEntityRequest');
    cy.intercept('DELETE', '/services/examstore/api/exam-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (examItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/examstore/api/exam-items/${examItem.id}`,
      }).then(() => {
        examItem = undefined;
      });
    }
  });

  afterEach(() => {
    cy.oauthLogout();
    cy.clearCache();
  });

  it('ExamItems menu should load ExamItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('exam-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ExamItem').should('exist');
    cy.url().should('match', examItemPageUrlPattern);
  });

  describe('ExamItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(examItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ExamItem page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/exam-item/new$'));
        cy.getEntityCreateUpdateHeading('ExamItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', examItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/examstore/api/exam-items',
          body: examItemSample,
        }).then(({ body }) => {
          examItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/examstore/api/exam-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [examItem],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(examItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ExamItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('examItem');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', examItemPageUrlPattern);
      });

      it('edit button click should load edit ExamItem page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExamItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', examItemPageUrlPattern);
      });

      it('last delete button click should delete instance of ExamItem', () => {
        cy.intercept('GET', '/services/examstore/api/exam-items/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('examItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', examItemPageUrlPattern);

        examItem = undefined;
      });
    });
  });

  describe('new ExamItem page', () => {
    beforeEach(() => {
      cy.visit(`${examItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ExamItem');
    });

    it('should create an instance of ExamItem', () => {
      cy.get(`[data-cy="numOfQuestion"]`).type('27483').should('have.value', '27483');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        examItem = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', examItemPageUrlPattern);
    });
  });
});
