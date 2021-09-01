import { IQuestionGroup } from 'app/shared/model/ExamStore/question-group.model';
import { IExam } from 'app/shared/model/ExamStore/exam.model';

export interface IExamItem {
  id?: number;
  numOfQuestion?: number;
  repo?: IQuestionGroup | null;
  exam?: IExam | null;
}

export const defaultValue: Readonly<IExamItem> = {};
