import { IOption } from 'app/shared/model/ExamStore/option.model';
import { IQuestionGroup } from 'app/shared/model/ExamStore/question-group.model';

export interface IQuestion {
  id?: number;
  content?: string;
  note?: string | null;
  options?: IOption[] | null;
  questionGroup?: IQuestionGroup | null;
}

export const defaultValue: Readonly<IQuestion> = {
  content: '',
  note: null,
  options: [
    { content: '', isCorrect: false },
    { content: '', isCorrect: false },
    { content: '', isCorrect: false },
    { content: '', isCorrect: true },
  ],
  questionGroup: null,
};
