import { ITopic } from 'app/shared/model/ExamStore/topic.model';

export interface IQuestionGroup {
  id?: number;
  name?: string;
  groupId?: number | null;
  userLogin?: string;
  topic?: ITopic | null;
}

export const defaultValue: Readonly<IQuestionGroup> = {};
