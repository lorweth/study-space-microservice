import { IAnswerSheet } from 'app/shared/model/AnswerStore/answer-sheet.model';

export interface IAnswerSheetItem {
  id?: number;
  questionId?: number;
  answerId?: number;
  answerSheet?: IAnswerSheet | null;
}

export const defaultValue: Readonly<IAnswerSheetItem> = {};
