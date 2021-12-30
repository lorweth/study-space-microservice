import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import React from 'react';
import { Switch } from 'react-router-dom';
import QuestionRepositories from './QuestionRepositories';
import QuestionRepositoryManager from './QuestionRepositoryManager';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute path={`${match.url}/new`} exact component={QuestionRepositoryManager} />
      <ErrorBoundaryRoute path={`${match.url}/:id/edit`} exact component={QuestionRepositoryManager} />
      <ErrorBoundaryRoute path={match.url} component={QuestionRepositories} />
    </Switch>
  </>
);

export default Routes;
