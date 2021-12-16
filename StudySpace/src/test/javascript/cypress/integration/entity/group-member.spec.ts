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

describe('GroupMember e2e test', () => {
  const groupMemberPageUrl = '/group-member';
  const groupMemberPageUrlPattern = new RegExp('/group-member(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const groupMemberSample = { userLogin: 'Buckinghamshire', role: 0 };

  let groupMember: any;

  beforeEach(() => {
    cy.getOauth2Data();
    cy.get('@oauth2Data').then(oauth2Data => {
      cy.oauthLogin(oauth2Data, username, password);
    });
    cy.intercept('GET', '/services/groupstore/api/group-members').as('entitiesRequest');
    cy.visit('');
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    Cypress.Cookies.preserveOnce('XSRF-TOKEN', 'JSESSIONID');
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/groupstore/api/group-members+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/groupstore/api/group-members').as('postEntityRequest');
    cy.intercept('DELETE', '/services/groupstore/api/group-members/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (groupMember) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/groupstore/api/group-members/${groupMember.id}`,
      }).then(() => {
        groupMember = undefined;
      });
    }
  });

  afterEach(() => {
    cy.oauthLogout();
    cy.clearCache();
  });

  it('GroupMembers menu should load GroupMembers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('group-member');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GroupMember').should('exist');
    cy.url().should('match', groupMemberPageUrlPattern);
  });

  describe('GroupMember page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(groupMemberPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GroupMember page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/group-member/new$'));
        cy.getEntityCreateUpdateHeading('GroupMember');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupMemberPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/groupstore/api/group-members',
          body: groupMemberSample,
        }).then(({ body }) => {
          groupMember = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/groupstore/api/group-members+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [groupMember],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(groupMemberPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GroupMember page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('groupMember');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupMemberPageUrlPattern);
      });

      it('edit button click should load edit GroupMember page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GroupMember');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupMemberPageUrlPattern);
      });

      it('last delete button click should delete instance of GroupMember', () => {
        cy.intercept('GET', '/services/groupstore/api/group-members/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('groupMember').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', groupMemberPageUrlPattern);

        groupMember = undefined;
      });
    });
  });

  describe('new GroupMember page', () => {
    beforeEach(() => {
      cy.visit(`${groupMemberPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('GroupMember');
    });

    it('should create an instance of GroupMember', () => {
      cy.get(`[data-cy="userLogin"]`).type('compress').should('have.value', 'compress');

      cy.get(`[data-cy="role"]`).type('2').should('have.value', '2');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        groupMember = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', groupMemberPageUrlPattern);
    });
  });
});
