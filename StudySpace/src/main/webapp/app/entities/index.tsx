import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Question from './ExamStore/question';
import QuestionGroup from './ExamStore/question-group';
import Option from './ExamStore/option';
import Topic from './ExamStore/topic';
import AnswerSheetItem from './AnswerStore/answer-sheet-item';
import GroupTimeTable from './AnswerStore/group-time-table';
import GroupMember from './GroupStore/group-member';
import Group from './GroupStore/group';
import AnswerSheet from './AnswerStore/answer-sheet';
import ExamItem from './ExamStore/exam-item';
import TimeTable from './AnswerStore/time-table';
import Exam from './ExamStore/exam';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}question`} component={Question} />
      <ErrorBoundaryRoute path={`${match.url}question-group`} component={QuestionGroup} />
      <ErrorBoundaryRoute path={`${match.url}option`} component={Option} />
      <ErrorBoundaryRoute path={`${match.url}topic`} component={Topic} />
      <ErrorBoundaryRoute path={`${match.url}answer-sheet-item`} component={AnswerSheetItem} />
      <ErrorBoundaryRoute path={`${match.url}group-time-table`} component={GroupTimeTable} />
      <ErrorBoundaryRoute path={`${match.url}group-member`} component={GroupMember} />
      <ErrorBoundaryRoute path={`${match.url}group`} component={Group} />
      <ErrorBoundaryRoute path={`${match.url}answer-sheet`} component={AnswerSheet} />
      <ErrorBoundaryRoute path={`${match.url}exam-item`} component={ExamItem} />
      <ErrorBoundaryRoute path={`${match.url}time-table`} component={TimeTable} />
      <ErrorBoundaryRoute path={`${match.url}exam`} component={Exam} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
