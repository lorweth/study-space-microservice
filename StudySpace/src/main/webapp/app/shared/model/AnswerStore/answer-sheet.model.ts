import dayjs from 'dayjs';
import { IAnswerSheetItem } from 'app/shared/model/AnswerStore/answer-sheet-item.model';
import { IGroupTimeTable } from 'app/shared/model/AnswerStore/group-time-table.model';

export interface IAnswerSheet {
  id?: number;
  createAt?: string;
  endAt?: string | null;
  userLogin?: string;
  examId?: number;
  answerSheetItems?: IAnswerSheetItem[] | null;
  groupTimeTable?: IGroupTimeTable | null;
}

export const defaultValue: Readonly<IAnswerSheet> = {};
