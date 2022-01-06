import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';
import { loadMoreDataWhenScrolled, parseHeaderForLinks } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError, IQueryParamsExtended } from 'app/shared/reducers/reducer.utils';
import { IExamItem, defaultValue } from 'app/shared/model/ExamStore/exam-item.model';

const initialState: EntityState<IExamItem> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'services/examstore/api/exam-items';

// Actions

export const getEntities = createAsyncThunk('examItem/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<IExamItem[]>(requestUrl);
});

export const getItemsOfExam = createAsyncThunk('examItem/fetch_item_list_of_exam', async ({ id }: IQueryParamsExtended) => {
  const requestUrl = `${apiUrl}/exam/${id}?cacheBuster=${new Date().getTime()}`;
  return axios.get<IExamItem[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'examItem/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IExamItem>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'examItem/create_entity',
  async (entity: IExamItem, thunkAPI) => {
    return axios.post<IExamItem>(apiUrl, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'examItem/update_entity',
  async (entity: IExamItem, thunkAPI) => {
    return axios.put<IExamItem>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'examItem/partial_update_entity',
  async (entity: IExamItem, thunkAPI) => {
    return axios.patch<IExamItem>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'examItem/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    return await axios.delete<IExamItem>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

// slice

export const ExamItemSlice = createEntitySlice({
  name: 'examItem',
  initialState,
  reducers: {
    setEntity(state, action) {
      return {
        ...state,
        entity: action.payload,
      };
    },
  },
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
      .addMatcher(isFulfilled(getItemsOfExam), (state, action) => {
        return {
          ...state,
          loading: false,
          entities: action.payload.data,
          totalItems: parseInt(action.payload.headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const links = parseHeaderForLinks(action.payload.headers.link);

        return {
          ...state,
          loading: false,
          links,
          entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
          totalItems: parseInt(action.payload.headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getItemsOfExam, getEntity), state => {
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

export const { reset, setEntity } = ExamItemSlice.actions;

// Reducer
export default ExamItemSlice.reducer;
