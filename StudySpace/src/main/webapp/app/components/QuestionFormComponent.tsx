import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IQuestion } from 'app/shared/model/ExamStore/question.model';
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Translate, translate, ValidatedField } from 'react-jhipster';
import { Link } from 'react-router-dom';
import { Button, Form, FormFeedback, FormGroup, FormText, Input, Label } from 'reactstrap';
import OptionComponent from './OptionComponent';

type PropType = {
  selectedQuestion: IQuestion;
};

const QuestionFormComponent = (props: PropType) => {
  const { selectedQuestion } = props;

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
    reset({
      ...selectedQuestion,
    });
  }, [selectedQuestion, reset]);

  const onSubmit = values => {
    // TODO: submit question
    window.alert(JSON.stringify(values));
  };

  return (
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
      <ValidatedField
        register={register}
        label={translate('studySpaceApp.examStoreOption.isCorrect')}
        id="option0-isCorrect"
        name="options[0].isCorrect"
        data-cy="isCorrect"
        check
        type="checkbox"
      />
      <ValidatedField
        register={register}
        label={translate('studySpaceApp.examStoreOption.content')}
        id="option0-content"
        name="options[0].content"
        data-cy="content"
        type="textarea"
        validate={{
          required: { value: true, message: translate('entity.validation.required') },
        }}
      />
      <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/question" replace color="danger">
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
  );
};
export default QuestionFormComponent;

{
  /* <ValidatedForm defaultValues={defaultValues} onSubmit={handleSubmit}>
      <ValidatedField name="id" readOnly id="question-id" label={translate('global.field.id')} />
      <ValidatedField
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
        label={translate('studySpaceApp.examStoreQuestion.note')}
        id="question-note"
        name="note"
        data-cy="note"
        type="textarea"
      />
      <OptionComponent />
      <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/question" replace color="danger">
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
    </ValidatedForm> */
}
