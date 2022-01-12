import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import React from 'react';
import { Switch } from 'react-router-dom';
import ExamDeleteDialog from './exam-delete-dialog';
import ExamUpdate from './exam-update';
import ExamList from './exam-list';
import ExamDetail from './exam-detail';
import DisplayTestComponent from 'app/shared/components/display-test-component/display-test-component';
import DisplayResultComponent from 'app/shared/components/display-result-component/display-result-component';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ExamUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ExamDetail} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ExamUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ExamDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/take-a-test`} component={DisplayTestComponent} />
      <ErrorBoundaryRoute exact path={`${match.url}/:sheet_id/result`} component={DisplayResultComponent} />
      <ErrorBoundaryRoute path={match.url} component={ExamList} />
    </Switch>
  </>
);

export default Routes;
