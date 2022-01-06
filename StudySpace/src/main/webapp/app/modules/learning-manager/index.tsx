import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import React from 'react';
import { Switch } from 'react-router-dom';
import ExamDeleteDialog from './exam-delete-dialog';
import ExamUpdate from './exam-update';
import ExamList from './exam-list';
import ExamDetail from './exam-detail';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ExamUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ExamDetail} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ExamUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ExamDeleteDialog} />
      <ErrorBoundaryRoute path={match.url} component={ExamList} />
    </Switch>
  </>
);

export default Routes;
