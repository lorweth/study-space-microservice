import dayjs from 'dayjs';

export interface IGroupTimeTable {
  id?: number;
  examId?: number;
  startAt?: string;
  endAt?: string;
  groupId?: string;
  note?: string | null;
}

export const defaultValue: Readonly<IGroupTimeTable> = {};
