import { ITopic } from 'app/shared/model/ExamStore/topic.model';

export interface IQuestionGroup {
  id?: number;
  name?: string;
  groupId?: string | null;
  userLogin?: string;
  topic?: ITopic | null;
}

export const defaultValue: Readonly<IQuestionGroup> = {};
