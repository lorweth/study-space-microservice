import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IQuestion } from 'app/shared/model/ExamStore/question.model';
import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Translate, translate, ValidatedField } from 'react-jhipster';
import { Link } from 'react-router-dom';
import { Button, Form, FormFeedback, FormGroup, FormText, Input, Label } from 'reactstrap';
import { createEntity as createQuestion, updateEntity as updateQuestion } from 'app/entities/ExamStore/question/question.reducer';
import OptionFormComponent from './OptionFormComponent';
import OptionComponent from './OptionFormComponent';
import QuestionDeleteDialog from './QuestionDeleteDialog';
import { toast } from 'react-toastify';

type PropType = {
  questionGroupId: number;
  selectedQuestion: IQuestion;
  syncList: () => void;
};

const QuestionFormComponent = (props: PropType) => {
  const { selectedQuestion, questionGroupId, syncList } = props;
  const [isOpenDeleteModal, setIsOpenDeleteModal] = useState(false);

  const {
    handleSubmit,
    register,
    reset,
    setValue,
    formState: { errors, touchedFields, dirtyFields },
  } = useForm<IQuestion>({
    mode: 'onSubmit',
    reValidateMode: 'onChange',
    defaultValues: {},
    resolver: undefined,
    context: undefined,
    criteriaMode: 'firstError',
    shouldFocusError: true,
    shouldUnregister: true,
  });

  const dispatch = useAppDispatch();

  const loading = useAppSelector(state => state.question.loading);
  const updating = useAppSelector(state => state.question.updating);
  const updateSuccess = useAppSelector(state => state.question.updateSuccess);

  useEffect(() => {
    // window.alert('Reset');
    reset({
      ...selectedQuestion,
    });
  }, [selectedQuestion, reset]);

  // Save the form
  const save = (values: IQuestion) => {
    const newQuestion = values;
    newQuestion.questionGroup = { id: questionGroupId };

    if (selectedQuestion.id) {
      dispatch(updateQuestion(newQuestion));
    } else {
      dispatch(createQuestion(newQuestion));
    }
  };

  const handleClickDelete = () => {
    setIsOpenDeleteModal(true);
  };

  const onSubmit = values => {
    const questionData: IQuestion = values;
    const correctOptions = questionData.options.filter(option => option.isCorrect === true);
    if (correctOptions.length > 1 || correctOptions.length === 0) {
      // toast.error(translate('question.error.correctOption'));
      toast.error('Vui lòng chọn số đáp án đúng cho hợp lý');
    } else {
      save(values);
      // TODO: Sync List sau khi đã cập nhật, chuyển qua gọi bên QuestionRepositoryManager
    }
  };

  return (
    <>
      <Form onSubmit={handleSubmit(onSubmit)}>
        <ValidatedField name="id" readOnly register={register} id="question-id" label={translate('global.field.id')} />
        <ValidatedField
          register={register}
          error={errors.content}
          label={translate('studySpaceApp.examStoreQuestion.content')}
          id="question-content"
          name="content"
          data-cy="content"
          type="textarea"
          validate={{
            required: { value: true, message: translate('entity.validation.required') },
          }}
        />
        <ValidatedField
          register={register}
          error={errors.note}
          label={translate('studySpaceApp.examStoreQuestion.note')}
          id="question-note"
          name="note"
          data-cy="note"
          type="textarea"
        />
        {/* List option form */}
        {selectedQuestion.options &&
          selectedQuestion.options.map((option, index) => (
            <OptionFormComponent
              key={option.id}
              name={`options[${index}]`}
              register={register}
              error={errors.options && errors.options[index].content}
            />
          ))}
        {/* List option form */}
        <Button
          id="cancel-save"
          data-cy="entityCreateCancelButton"
          replace
          color="danger"
          type="button"
          onClick={handleClickDelete}
          disabled={selectedQuestion.id === undefined || loading}
        >
          <FontAwesomeIcon icon="trash" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.delete">Delete</Translate>
          </span>
        </Button>
        &nbsp;
        <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
          <FontAwesomeIcon icon="save" />
          &nbsp;
          <Translate contentKey="entity.action.save">Save</Translate>
        </Button>
      </Form>
      <QuestionDeleteDialog isOpen={isOpenDeleteModal} setIsOpen={setIsOpenDeleteModal} selectedQuestionId={selectedQuestion.id} />
    </>
  );
};
export default QuestionFormComponent;
