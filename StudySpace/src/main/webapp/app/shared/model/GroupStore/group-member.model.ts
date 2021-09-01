import { IGroup } from 'app/shared/model/GroupStore/group.model';

export interface IGroupMember {
  id?: number;
  userLogin?: string;
  role?: number;
  group?: IGroup | null;
}

export const defaultValue: Readonly<IGroupMember> = {};
