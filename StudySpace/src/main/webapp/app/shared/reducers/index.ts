import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from './user-management';
// prettier-ignore
import question from 'app/entities/ExamStore/question/question.reducer';
// prettier-ignore
import questionGroup from 'app/entities/ExamStore/question-group/question-group.reducer';
// prettier-ignore
import option from 'app/entities/ExamStore/option/option.reducer';
// prettier-ignore
import topic from 'app/entities/ExamStore/topic/topic.reducer';
// prettier-ignore
import answerSheetItem from 'app/entities/AnswerStore/answer-sheet-item/answer-sheet-item.reducer';
// prettier-ignore
import groupTimeTable from 'app/entities/AnswerStore/group-time-table/group-time-table.reducer';
// prettier-ignore
import groupMember from 'app/entities/GroupStore/group-member/group-member.reducer';
// prettier-ignore
import group from 'app/entities/GroupStore/group/group.reducer';
// prettier-ignore
import answerSheet from 'app/entities/AnswerStore/answer-sheet/answer-sheet.reducer';
// prettier-ignore
import examItem from 'app/entities/ExamStore/exam-item/exam-item.reducer';
// prettier-ignore
import timeTable from 'app/entities/AnswerStore/time-table/time-table.reducer';
// prettier-ignore
import exam from 'app/entities/ExamStore/exam/exam.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  question,
  questionGroup,
  option,
  topic,
  answerSheetItem,
  groupTimeTable,
  groupMember,
  group,
  answerSheet,
  examItem,
  timeTable,
  exam,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
