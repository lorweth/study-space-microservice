export interface IAnswer {
  questionId?: number;
  questionContent?: string;
  answerId?: number;
  answerContent?: string;
}

export const defaultValue: Readonly<IAnswer> = {};
