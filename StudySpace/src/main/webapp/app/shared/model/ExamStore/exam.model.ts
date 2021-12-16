import { IExamItem } from 'app/shared/model/ExamStore/exam-item.model';

export interface IExam {
  id?: number;
  name?: string;
  duration?: number;
  mix?: number;
  groupId?: number;
  items?: IExamItem[] | null;
}

export const defaultValue: Readonly<IExam> = {};
