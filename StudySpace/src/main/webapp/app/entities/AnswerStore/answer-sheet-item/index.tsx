import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AnswerSheetItem from './answer-sheet-item';
import AnswerSheetItemDetail from './answer-sheet-item-detail';
import AnswerSheetItemUpdate from './answer-sheet-item-update';
import AnswerSheetItemDeleteDialog from './answer-sheet-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AnswerSheetItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AnswerSheetItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AnswerSheetItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={AnswerSheetItem} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AnswerSheetItemDeleteDialog} />
  </>
);

export default Routes;
