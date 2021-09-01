import { IQuestion } from 'app/shared/model/ExamStore/question.model';

export interface IOption {
  id?: number;
  content?: string;
  isCorrect?: boolean;
  question?: IQuestion | null;
}

export const defaultValue: Readonly<IOption> = {
  isCorrect: false,
};
