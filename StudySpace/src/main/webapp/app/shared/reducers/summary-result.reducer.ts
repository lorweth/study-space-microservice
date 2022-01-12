import { createAsyncThunk, isFulfilled } from '@reduxjs/toolkit';
import axios from 'axios';
import { defaultValue, ISummaryResult } from '../model/AnswerStore/summary-result';
import { createEntitySlice, EntityState } from './reducer.utils';

const initialState: EntityState<ISummaryResult> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'services/answerstore/api/answers';

// Actions

export const getSummaryResults = createAsyncThunk('summaryResult/fetch_all_sumary_result_exam', async (id: string | number) => {
  const requestUrl = `${apiUrl}/exam/${id}/summary`;
  return axios.get<ISummaryResult[]>(requestUrl);
});

// Slice

export const AnswerSlice = createEntitySlice({
  name: 'summaryResult',
  initialState,
  extraReducers(builder) {
    builder.addMatcher(isFulfilled(getSummaryResults), (state, action) => {
      state.updating = false;
      state.entities = action.payload.data;
      state.totalItems = action.payload.data.length;
    });
  },
});

export const { reset } = AnswerSlice.actions;

// Reducer
export default AnswerSlice.reducer;
