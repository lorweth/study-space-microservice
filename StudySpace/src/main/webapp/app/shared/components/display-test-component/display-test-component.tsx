import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getQuestionsFromExam } from 'app/entities/ExamStore/question/question.reducer';
import { getEntity as getExam } from 'app/entities/ExamStore/exam/exam.reducer';
import { createEntity as createAnswerSheet } from 'app/entities/AnswerStore/answer-sheet/answer-sheet.reducer';
import React, { useEffect, useMemo, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Button, Col, Form, FormGroup, Input, Label, Row } from 'reactstrap';
import AnswerFormComponent from './answer-form-component';
import CountDownTimer from '../count-down-timer/count-down-timer';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './test.css';
import CompleteTestDialog from './complete-test-dialog';
import { MIX_TYPES } from 'app/config/constants';
import { IQuestion } from 'app/shared/model/ExamStore/question.model';

/**
 * DisplayTestComponent
 * @param props RouteComponentProps contains id of the exam.
 */
const DisplayTestComponent = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  // Lấy examId từ match.params.
  const [examId] = useState(props.match.params.id);

  // Quản lý đóng mở complete dialog.
  const [isOpenCompleteDialog, setIsOpenCompleteDialog] = useState(false);

  // Lưu trữ số câu hỏi đã được trả lời
  const [answeredQuestions, setAnsweredQuestions] = useState([] as number[]);

  const exam = useAppSelector(state => state.exam.entity);
  const questionList = useAppSelector(state => state.question.entities);

  const answer = useAppSelector(state => state.answerSheetItem.entity);
  const answerUpdateSuccess = useAppSelector(state => state.answerSheetItem.updateSuccess);

  useEffect(() => {
    dispatch(getExam(examId));
    dispatch(getQuestionsFromExam({ id: examId }));
    dispatch(createAnswerSheet({ examId: +examId }));
  }, []);

  // Hàm thêm câu hỏi vào danh sách câu hỏi đã được trả lời.
  const addAnsweredQuestion = (questionId: number) => {
    if (answeredQuestions.indexOf(questionId) === -1) {
      setAnsweredQuestions([...answeredQuestions, questionId]);
    }
  };

  useEffect(() => {
    if (answerUpdateSuccess) {
      addAnsweredQuestion(answer.questionId);
    }
  }, [answerUpdateSuccess]);

  // Hàm random câu hỏi.
  const randomizedQuestionList = () => {
    if (exam.mix === MIX_TYPES.BYGROUP) {
      const questionListMap = new Map<number, Array<IQuestion>>();
      const keys = [];

      questionList.forEach(question => {
        const key = question.questionGroup.id;
        if (keys.indexOf(key) === -1) {
          keys.push();
        }
        const collection = questionListMap.get(key);
        if (!collection) {
          questionListMap.set(key, [question]);
        } else {
          collection.push(question);
        }
      });

      questionListMap.forEach((value, key) => {
        value.sort(() => Math.random() - 0.5);
      });
      return [].concat(...Array.from(questionListMap.values()));
    } else if (exam.mix === MIX_TYPES.ALL) {
      return [...questionList].sort(() => Math.random() - 0.5);
    }
    return questionList;
  };

  // Hàm tính toán random câu hỏi khi cần thiết
  const randomQuestionList = useMemo(() => randomizedQuestionList(), [questionList]);

  return (
    <Row className="justify-content-center">
      <Col md="8">
        <div className="mt-5 mb-5">
          <legend id="exam-title">
            {exam.name} <sub>{exam.duration} phút</sub>{' '}
          </legend>
          <hr />
          <CountDownTimer duration={exam.duration} history={props.history} location={props.location} match={props.match} />
        </div>
        {questionList && questionList.length > 0 ? (
          randomQuestionList.map((question, i) => <AnswerFormComponent key={i} index={i} question={question} />)
        ) : (
          <p>No question found</p>
        )}
        <div className="text-center">
          <Button color="primary" onClick={() => setIsOpenCompleteDialog(true)} disabled={answeredQuestions.length !== questionList.length}>
            <FontAwesomeIcon icon="check" />
            &nbsp;
            <span>Hoàn tất</span>
          </Button>
        </div>
        {isOpenCompleteDialog && (
          <CompleteTestDialog setIsOpen={setIsOpenCompleteDialog} history={props.history} location={props.location} match={props.match} />
        )}
      </Col>
    </Row>
  );
};
export default DisplayTestComponent;
