import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import QuestionGroup from './question-group';
import QuestionGroupDetail from './question-group-detail';
import QuestionGroupUpdate from './question-group-update';
import QuestionGroupDeleteDialog from './question-group-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={QuestionGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={QuestionGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={QuestionGroupDetail} />
      <ErrorBoundaryRoute path={match.url} component={QuestionGroup} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={QuestionGroupDeleteDialog} />
  </>
);

export default Routes;
