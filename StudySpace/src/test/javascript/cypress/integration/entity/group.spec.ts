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

describe('Group e2e test', () => {
  const groupPageUrl = '/group';
  const groupPageUrlPattern = new RegExp('/group(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const groupSample = { name: 'Tuna California' };

  let group: any;

  beforeEach(() => {
    cy.getOauth2Data();
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogin(oauth2Data, username, password);
    });
    cy.intercept('GET', '/services/groupstore/api/groups').as('entitiesRequest');
    cy.visit('');
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    Cypress.Cookies.preserveOnce('XSRF-TOKEN', 'JSESSIONID');
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/groupstore/api/groups+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/groupstore/api/groups').as('postEntityRequest');
    cy.intercept('DELETE', '/services/groupstore/api/groups/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (group) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/groupstore/api/groups/${group.id}`,
      }).then(() => {
        group = undefined;
      });
    }
  });

  afterEach(() => {
    cy.oauthLogout();
    cy.clearCache();
  });

  it('Groups menu should load Groups page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('group');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Group').should('exist');
    cy.url().should('match', groupPageUrlPattern);
  });

  describe('Group page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(groupPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Group page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/group/new$'));
        cy.getEntityCreateUpdateHeading('Group');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/groupstore/api/groups',
          body: groupSample,
        }).then(({ body }) => {
          group = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/groupstore/api/groups+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [group],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(groupPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Group page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('group');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupPageUrlPattern);
      });

      it('edit button click should load edit Group page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Group');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupPageUrlPattern);
      });

      it('last delete button click should delete instance of Group', () => {
        cy.intercept('GET', '/services/groupstore/api/groups/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('group').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupPageUrlPattern);

        group = undefined;
      });
    });
  });

  describe('new Group page', () => {
    beforeEach(() => {
      cy.visit(`${groupPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Group');
    });

    it('should create an instance of Group', () => {
      cy.get(`[data-cy="name"]`).type('primary').should('have.value', 'primary');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        group = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', groupPageUrlPattern);
    });
  });
});
