import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getAllWrongAnswers } from 'app/shared/reducers/answer.reducer';
import React, { useEffect, useState } from 'react';
import './result.css';
import { RouteComponentProps } from 'react-router-dom';

const DisplayResultComponent = (props: RouteComponentProps<{ sheet_id: string }>) => {
  const dispatch = useAppDispatch();

  const [answerSheetId] = useState(props.match.params.sheet_id);
  const wrongAnswers = useAppSelector(state => state.answer.entities);
  const wrongTotals = useAppSelector(state => state.answer.totalItems);

  useEffect(() => {
    dispatch(getAllWrongAnswers(answerSheetId));
  }, []);

  return (
    <div>
      {wrongAnswers && wrongAnswers.length > 0 ? (
        <>
          <legend>
            Bạn đã sai <b className="total-wrong">{wrongTotals}</b> câu, dưới đây là kết quả đúng
          </legend>
          <ul>
            {wrongAnswers.map((answer, i) => (
              <li key={i}>
                {answer.questionContent} - <p className="correct-answer">{answer.answerContent}</p>
              </li>
            ))}
          </ul>
        </>
      ) : (
        <p>Perfect, ờ máy zing gút chóp em!</p>
      )}
    </div>
  );
};
export default DisplayResultComponent;
