import { IOption } from 'app/shared/model/ExamStore/option.model';
import { IQuestionGroup } from 'app/shared/model/ExamStore/question-group.model';

export interface IQuestion {
  id?: number;
  content?: string;
  note?: string | null;
  options?: IOption[] | null;
  repo?: IQuestionGroup | null;
}

export const defaultValue: Readonly<IQuestion> = {};
