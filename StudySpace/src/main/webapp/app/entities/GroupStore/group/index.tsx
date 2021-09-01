import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Group from './group';
import GroupDetail from './group-detail';
import GroupUpdate from './group-update';
import GroupDeleteDialog from './group-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GroupDetail} />
      <ErrorBoundaryRoute path={match.url} component={Group} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GroupDeleteDialog} />
  </>
);

export default Routes;
