import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AnswerSheet from './answer-sheet';
import AnswerSheetDetail from './answer-sheet-detail';
import AnswerSheetUpdate from './answer-sheet-update';
import AnswerSheetDeleteDialog from './answer-sheet-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AnswerSheetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AnswerSheetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AnswerSheetDetail} />
      <ErrorBoundaryRoute path={match.url} component={AnswerSheet} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AnswerSheetDeleteDialog} />
  </>
);

export default Routes;
