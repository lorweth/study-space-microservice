import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getAllWrongAnswers } from 'app/shared/reducers/answer.reducer';
import React, { useEffect, useState } from 'react';
import './result.css';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Card, CardBody, CardHeader, CardText, CardTitle } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate } from 'react-jhipster';

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
              // <li key={i}>
              //   {answer.questionContent} : <p className="correct-answer">{answer.answerContent}</p>
              // </li>
              <Card key={i} color="success" outline className="m-3">
                <CardHeader>{answer.questionContent}</CardHeader>
                <CardBody>
                  <CardText>{answer.answerContent}</CardText>
                </CardBody>
              </Card>
            ))}
          </ul>
        </>
      ) : (
        <p>Perfect, ờ máy zing gút chóp em!</p>
      )}
      <div className="text-center">
        <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/learning-manager" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />
          &nbsp;
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
      </div>
    </div>
  );
};
export default DisplayResultComponent;
