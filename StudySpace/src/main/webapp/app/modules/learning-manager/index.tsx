import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import React from 'react';
import { Switch } from 'react-router-dom';
import LearningManagerComponent from './learning-manager-component';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute path={match.url} component={LearningManagerComponent} />
    </Switch>
  </>
);

export default Routes;
