export interface ISummaryResult {
  sheetId?: number;
  wrongAnswerCount?: number;
  time?: string;
}

export const defaultValue: Readonly<ISummaryResult> = {};
