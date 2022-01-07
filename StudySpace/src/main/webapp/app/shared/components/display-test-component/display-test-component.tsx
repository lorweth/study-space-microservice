import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getQuestionsFromExam } from 'app/entities/ExamStore/question/question.reducer';
import { getEntity as getExam } from 'app/entities/ExamStore/exam/exam.reducer';
import React, { useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Button, Col, Form, FormGroup, Input, Label, Row } from 'reactstrap';
import AnswerFormComponent from './answer-form-component';
import CountDownTimer from '../count-down-timer/count-down-timer';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './test.css';

/**
 * DisplayTestComponent
 * @param props RouteComponentProps contains id of the exam.
 */
const DisplayTestComponent = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [examId] = useState(props.match.params.id);

  const exam = useAppSelector(state => state.exam.entity);
  const questionList = useAppSelector(state => state.question.entities);

  useEffect(() => {
    dispatch(getExam(examId));
    dispatch(getQuestionsFromExam({ id: examId }));
  }, []);

  return (
    <Row className="justify-content-center">
      <Col md="8">
        <div className="mt-5 mb-5">
          <legend id="exam-title">
            {exam.name} <sub>{exam.duration} phút</sub>{' '}
          </legend>
          <hr />
          {/* <CountDownTimer duration={exam.duration} /> */}
        </div>
        {questionList && questionList.length > 0 ? (
          questionList.map((question, i) => <AnswerFormComponent key={i} index={i} question={question} />)
        ) : (
          <p>No question found</p>
        )}
        <div className="text-center">
          <Button color="primary">
            <FontAwesomeIcon icon="check" />
            &nbsp;
            <span>Hoàn tất</span>
          </Button>
        </div>
      </Col>
    </Row>
  );
};
export default DisplayTestComponent;
