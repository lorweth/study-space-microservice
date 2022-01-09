import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IAnswerSheetItem } from 'app/shared/model/AnswerStore/answer-sheet-item.model';
import { IQuestion } from 'app/shared/model/ExamStore/question.model';
import {
  createEntity as createAnswer,
  updateEntity as updateAnswer,
} from 'app/entities/AnswerStore/answer-sheet-item/answer-sheet-item.reducer';
import React, { useEffect, useState } from 'react';
import { ValidatedField } from 'react-jhipster';
import { RouteComponentProps } from 'react-router-dom';
import { Form, FormGroup, Input, Label } from 'reactstrap';

type PropType = {
  index: number;
  question: IQuestion;
};

const AnswerFormComponent = (props: PropType) => {
  const dispatch = useAppDispatch();

  const { question, index } = props;
  const [answer, setAnswer] = useState<IAnswerSheetItem>({ questionId: question.id });

  // Bài làm
  const sheetEntity = useAppSelector(state => state.answerSheet.entity);

  // Câu trả lời của từng câu hỏi trong bài làm.
  const answerEntity = useAppSelector(state => state.answerSheetItem.entity);
  // Cập nhật câu trả lời thành công.
  const answerUpdateSuccess = useAppSelector(state => state.answerSheetItem.updateSuccess);

  /**
   * Submit mỗi khi chọn đáp án khác.
   */
  useEffect(() => {
    if (answer.answerId) {
      onSubmit();
    }
  }, [answer.answerId]);

  /**
   * Cập nhật id vào state mỗi khi lưu lại câu trả lời xong.
   */
  useEffect(() => {
    if (question.id === answerEntity.questionId) {
      setAnswer({ ...answer, id: answerEntity.id });
    }
  }, [answerUpdateSuccess]);

  // Cập nhật câu trả lời khi chọn đáp án khác.
  const handleClickRadioButton = (answerId: number) => {
    setAnswer({ ...answer, answerId });
  };

  /**
   * Lưu lại câu trả lời.
   */
  const onSubmit = () => {
    const entity = { ...answer, answerSheet: { id: sheetEntity.id } } as IAnswerSheetItem;
    // window.alert(JSON.stringify(entity));

    if (entity.id) {
      dispatch(updateAnswer(entity));
    } else {
      dispatch(createAnswer(entity));
    }
  };

  return (
    <Form id={`answer-form-${question.id}`} className="answer-form shadow-sm p-3 mb-3 bg-white rounded">
      <legend>
        {index + 1}/. {question.content}
      </legend>
      <FormGroup check>
        <Input
          id={`question[${question.id}].option[0]`}
          name={`question[${question.id}].option`}
          type="radio"
          onClick={() => handleClickRadioButton(question.options[0].id)}
        />
        <Label htmlFor={`question[${question.id}].option[0]`} check>
          {question.options[0].content}
        </Label>
      </FormGroup>
      <FormGroup check>
        <Input
          id={`question[${question.id}].option[1]`}
          name={`question[${question.id}].option`}
          type="radio"
          onClick={() => handleClickRadioButton(question.options[1].id)}
        />
        <Label htmlFor={`question[${question.id}].option[1]`} check>
          {question.options[1].content}
        </Label>
      </FormGroup>
      <FormGroup check>
        <Input
          id={`question[${question.id}].option[2]`}
          name={`question[${question.id}].option`}
          type="radio"
          onClick={() => handleClickRadioButton(question.options[2].id)}
        />
        <Label htmlFor={`question[${question.id}].option[2]`} check>
          {question.options[2].content}
        </Label>
      </FormGroup>
      <FormGroup check>
        <Input
          id={`question[${question.id}].option[3]`}
          name={`question[${question.id}].option`}
          type="radio"
          onClick={() => handleClickRadioButton(question.options[3].id)}
        />
        <Label htmlFor={`question[${question.id}].option[3]`} check>
          {question.options[3].content}
        </Label>
      </FormGroup>
    </Form>
  );
};
export default AnswerFormComponent;
