import { useAppDispatch } from 'app/config/store';
import { IAnswerSheetItem } from 'app/shared/model/AnswerStore/answer-sheet-item.model';
import { IQuestion } from 'app/shared/model/ExamStore/question.model';
import React, { useEffect, useState } from 'react';
import { ValidatedField } from 'react-jhipster';
import { RouteComponentProps } from 'react-router-dom';
import { Form, FormGroup, Input, Label } from 'reactstrap';

type PropType = {
  index: number;
  // answerSheetId: number;
  question: IQuestion;
};

const AnswerFormComponent = (props: PropType) => {
  const dispatch = useAppDispatch();

  const { question, index } = props;
  const [answer, setAnswer] = useState<IAnswerSheetItem>({ questionId: question.id });

  // Mới vào là chạy rồi
  useEffect(() => {
    if (answer.answerId) {
      onSubmit();
    }
  }, [answer.answerId]);

  const handleClickRadioButton = (answerId: number) => {
    setAnswer({ ...answer, answerId });
  };

  const onSubmit = () => {
    window.alert(JSON.stringify(answer));
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
