import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ExamItem from './exam-item';
import ExamItemDetail from './exam-item-detail';
import ExamItemUpdate from './exam-item-update';
import ExamItemDeleteDialog from './exam-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ExamItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ExamItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ExamItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={ExamItem} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ExamItemDeleteDialog} />
  </>
);

export default Routes;
