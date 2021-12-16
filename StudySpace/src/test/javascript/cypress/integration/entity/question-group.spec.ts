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

describe('QuestionGroup e2e test', () => {
  const questionGroupPageUrl = '/question-group';
  const questionGroupPageUrlPattern = new RegExp('/question-group(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const questionGroupSample = { name: 'Rubber deposit redundant', userLogin: 'AGP neural' };

  let questionGroup: any;

  beforeEach(() => {
    cy.getOauth2Data();
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogin(oauth2Data, username, password);
    });
    cy.intercept('GET', '/services/examstore/api/question-groups').as('entitiesRequest');
    cy.visit('');
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    Cypress.Cookies.preserveOnce('XSRF-TOKEN', 'JSESSIONID');
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/examstore/api/question-groups+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/examstore/api/question-groups').as('postEntityRequest');
    cy.intercept('DELETE', '/services/examstore/api/question-groups/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (questionGroup) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/examstore/api/question-groups/${questionGroup.id}`,
      }).then(() => {
        questionGroup = undefined;
      });
    }
  });

  afterEach(() => {
    cy.oauthLogout();
    cy.clearCache();
  });

  it('QuestionGroups menu should load QuestionGroups page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('question-group');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('QuestionGroup').should('exist');
    cy.url().should('match', questionGroupPageUrlPattern);
  });

  describe('QuestionGroup page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(questionGroupPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create QuestionGroup page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/question-group/new$'));
        cy.getEntityCreateUpdateHeading('QuestionGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', questionGroupPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/examstore/api/question-groups',
          body: questionGroupSample,
        }).then(({ body }) => {
          questionGroup = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/examstore/api/question-groups+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [questionGroup],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(questionGroupPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details QuestionGroup page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('questionGroup');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', questionGroupPageUrlPattern);
      });

      it('edit button click should load edit QuestionGroup page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuestionGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', questionGroupPageUrlPattern);
      });

      it('last delete button click should delete instance of QuestionGroup', () => {
        cy.intercept('GET', '/services/examstore/api/question-groups/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('questionGroup').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', questionGroupPageUrlPattern);

        questionGroup = undefined;
      });
    });
  });

  describe('new QuestionGroup page', () => {
    beforeEach(() => {
      cy.visit(`${questionGroupPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('QuestionGroup');
    });

    it('should create an instance of QuestionGroup', () => {
      cy.get(`[data-cy="name"]`).type('bluetooth input').should('have.value', 'bluetooth input');

      cy.get(`[data-cy="groupId"]`).type('mobile').should('have.value', 'mobile');

      cy.get(`[data-cy="userLogin"]`).type('Metal customized Strategist').should('have.value', 'Metal customized Strategist');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        questionGroup = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', questionGroupPageUrlPattern);
    });
  });
});
