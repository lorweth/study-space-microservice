import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GroupTimeTable from './group-time-table';
import GroupTimeTableDetail from './group-time-table-detail';
import GroupTimeTableUpdate from './group-time-table-update';
import GroupTimeTableDeleteDialog from './group-time-table-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GroupTimeTableUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GroupTimeTableUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GroupTimeTableDetail} />
      <ErrorBoundaryRoute path={match.url} component={GroupTimeTable} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GroupTimeTableDeleteDialog} />
  </>
);

export default Routes;
