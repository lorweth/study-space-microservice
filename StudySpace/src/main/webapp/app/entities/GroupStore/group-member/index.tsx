import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GroupMember from './group-member';
import GroupMemberDetail from './group-member-detail';
import GroupMemberUpdate from './group-member-update';
import GroupMemberDeleteDialog from './group-member-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GroupMemberUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GroupMemberUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GroupMemberDetail} />
      <ErrorBoundaryRoute path={match.url} component={GroupMember} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GroupMemberDeleteDialog} />
  </>
);

export default Routes;
