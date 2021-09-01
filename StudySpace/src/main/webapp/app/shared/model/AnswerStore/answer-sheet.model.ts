import dayjs from 'dayjs';
import { IGroupTimeTable } from 'app/shared/model/AnswerStore/group-time-table.model';

export interface IAnswerSheet {
  id?: number;
  time?: string;
  userLogin?: string;
  groupTimeTable?: IGroupTimeTable | null;
}

export const defaultValue: Readonly<IAnswerSheet> = {};
