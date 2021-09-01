export interface IExam {
  id?: number;
  name?: string;
  duration?: number;
  mix?: number;
  groupId?: number;
}

export const defaultValue: Readonly<IExam> = {};
