import dayjs from 'dayjs';

export interface ITimeTable {
  id?: number;
  title?: string | null;
  time?: string | null;
  note?: string;
  userLogin?: string;
}

export const defaultValue: Readonly<ITimeTable> = {};
