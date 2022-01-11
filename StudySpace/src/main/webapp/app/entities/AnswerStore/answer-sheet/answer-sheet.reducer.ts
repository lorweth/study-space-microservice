import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';
import { loadMoreDataWhenScrolled, parseHeaderForLinks } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IAnswerSheet, defaultValue } from 'app/shared/model/AnswerStore/answer-sheet.model';

const initialState: EntityState<IAnswerSheet> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'services/answerstore/api/answer-sheets';

// Actions

export const getEntities = createAsyncThunk('answerSheet/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<IAnswerSheet[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'answerSheet/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IAnswerSheet>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const finishAnswerSheet = createAsyncThunk(
  'answerSheet/finish_answer_sheet',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}/finish`;
    return axios.put<IAnswerSheet>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'answerSheet/create_entity',
  async (entity: IAnswerSheet, thunkAPI) => {
    return axios.post<IAnswerSheet>(apiUrl, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'answerSheet/update_entity',
  async (entity: IAnswerSheet, thunkAPI) => {
    return axios.put<IAnswerSheet>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'answerSheet/partial_update_entity',
  async (entity: IAnswerSheet, thunkAPI) => {
    return axios.patch<IAnswerSheet>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'answerSheet/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    return await axios.delete<IAnswerSheet>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

// slice

export const AnswerSheetSlice = createEntitySlice({
  name: 'answerSheet',
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
      .addMatcher(isFulfilled(createEntity, updateEntity, finishAnswerSheet, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, finishAnswerSheet, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = AnswerSheetSlice.actions;

// Reducer
export default AnswerSheetSlice.reducer;
