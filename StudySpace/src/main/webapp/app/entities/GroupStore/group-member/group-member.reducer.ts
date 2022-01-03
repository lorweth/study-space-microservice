import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError, IQueryParamsExtended } from 'app/shared/reducers/reducer.utils';
import { IGroupMember, defaultValue } from 'app/shared/model/GroupStore/group-member.model';

const initialState: EntityState<IGroupMember> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'services/groupstore/api/group-members';

// Actions

export const getAllAdmins = createAsyncThunk('group-member/fetch_admin_list', async ({ id, page, size, sort }: IQueryParamsExtended) => {
  const requestUrl = `${apiUrl}/group/${id}/admin${
    sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'
  }cacheBuster=${new Date().getTime()}`;
  return axios.get<IGroupMember[]>(requestUrl);
});

export const getEntities = createAsyncThunk('groupMember/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<IGroupMember[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'groupMember/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IGroupMember>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'groupMember/create_entity',
  async (entity: IGroupMember, thunkAPI) => {
    const result = await axios.post<IGroupMember>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'groupMember/update_entity',
  async (entity: IGroupMember, thunkAPI) => {
    const result = await axios.put<IGroupMember>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'groupMember/partial_update_entity',
  async (entity: IGroupMember, thunkAPI) => {
    const result = await axios.patch<IGroupMember>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'groupMember/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IGroupMember>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const GroupMemberSlice = createEntitySlice({
  name: 'groupMember',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities, getAllAdmins), (state, action) => {
        return {
          ...state,
          loading: false,
          entities: action.payload.data,
          totalItems: parseInt(action.payload.headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getAllAdmins, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = GroupMemberSlice.actions;

// Reducer
export default GroupMemberSlice.reducer;
