import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import React from 'react';
import { Switch } from 'react-router-dom';
import Group from './group';
import GroupDetail from './group-detail';
import GroupUpdate from './group-update';
import GroupManager from './GroupManager';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GroupDetail} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/management`} component={GroupManager} />
      <ErrorBoundaryRoute path={match.url} component={Group} />
    </Switch>
  </>
);

export default Routes;
