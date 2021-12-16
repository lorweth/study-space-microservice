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

describe('Exam e2e test', () => {
  const examPageUrl = '/exam';
  const examPageUrlPattern = new RegExp('/exam(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const examSample = { name: 'Utah', duration: 176, mix: 1, groupId: 85152 };

  let exam: any;

  beforeEach(() => {
    cy.getOauth2Data();
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogin(oauth2Data, username, password);
    });
    cy.intercept('GET', '/services/examstore/api/exams').as('entitiesRequest');
    cy.visit('');
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    Cypress.Cookies.preserveOnce('XSRF-TOKEN', 'JSESSIONID');
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/examstore/api/exams+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/examstore/api/exams').as('postEntityRequest');
    cy.intercept('DELETE', '/services/examstore/api/exams/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (exam) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/examstore/api/exams/${exam.id}`,
      }).then(() => {
        exam = undefined;
      });
    }
  });

  afterEach(() => {
    cy.oauthLogout();
    cy.clearCache();
  });

  it('Exams menu should load Exams page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('exam');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Exam').should('exist');
    cy.url().should('match', examPageUrlPattern);
  });

  describe('Exam page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(examPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Exam page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/exam/new$'));
        cy.getEntityCreateUpdateHeading('Exam');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', examPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/examstore/api/exams',
          body: examSample,
        }).then(({ body }) => {
          exam = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/examstore/api/exams+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [exam],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(examPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Exam page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('exam');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', examPageUrlPattern);
      });

      it('edit button click should load edit Exam page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Exam');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', examPageUrlPattern);
      });

      it('last delete button click should delete instance of Exam', () => {
        cy.intercept('GET', '/services/examstore/api/exams/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('exam').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', examPageUrlPattern);

        exam = undefined;
      });
    });
  });

  describe('new Exam page', () => {
    beforeEach(() => {
      cy.visit(`${examPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Exam');
    });

    it('should create an instance of Exam', () => {
      cy.get(`[data-cy="name"]`).type('TCP').should('have.value', 'TCP');

      cy.get(`[data-cy="duration"]`).type('62').should('have.value', '62');

      cy.get(`[data-cy="mix"]`).type('1').should('have.value', '1');

      cy.get(`[data-cy="groupId"]`).type('31577').should('have.value', '31577');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        exam = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', examPageUrlPattern);
    });
  });
});
