import { createAsyncThunk, isFulfilled } from '@reduxjs/toolkit';
import axios from 'axios';
import { defaultValue, IAnswer } from '../model/AnswerStore/answer';
import { createEntitySlice, EntityState } from './reducer.utils';

const initialState: EntityState<IAnswer> = {
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

export const getAllWrongAnswers = createAsyncThunk('answerSheetItem/fetch_all_wrong_answer', async (id: string | number) => {
  const requestUrl = `${apiUrl}/${id}/wrong-answer`;
  return axios.get<IAnswer[]>(requestUrl);
});

// Slice

export const AnswerSlice = createEntitySlice({
  name: 'answer',
  initialState,
  extraReducers(builder) {
    builder.addMatcher(isFulfilled(getAllWrongAnswers), (state, action) => {
      state.updating = false;
      state.entities = action.payload.data;
      state.totalItems = action.payload.data.length;
    });
  },
});

export const { reset } = AnswerSlice.actions;

// Reducer
export default AnswerSlice.reducer;
