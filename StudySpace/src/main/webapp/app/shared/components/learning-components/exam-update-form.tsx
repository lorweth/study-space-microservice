import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import React, { useEffect, useState } from 'react';
import { isNumber, translate, Translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { getEntity as getExam, updateEntity as updateExam, createEntity as createExam } from 'app/entities/ExamStore/exam/exam.reducer';

const ExamUpdateForm = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const loading = useAppSelector(state => state.exam.loading);
  const examEntity = useAppSelector(state => state.exam.entity);
  const updating = useAppSelector(state => state.exam.updating);

  const saveEntity = values => {
    const entity = {
      ...examEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createExam(entity));
    } else {
      dispatch(updateExam(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...examEntity,
        };

  return (
    <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
      {!isNew ? (
        <ValidatedField name="id" required readOnly id="exam-id" label={translate('global.field.id')} validate={{ required: true }} />
      ) : null}
      <ValidatedField
        label={translate('studySpaceApp.examStoreExam.name')}
        id="exam-name"
        name="name"
        data-cy="name"
        type="text"
        validate={{
          required: { value: true, message: translate('entity.validation.required') },
          minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
          maxLength: { value: 155, message: translate('entity.validation.maxlength', { max: 155 }) },
        }}
      />
      <ValidatedField
        label={translate('studySpaceApp.examStoreExam.duration')}
        id="exam-duration"
        name="duration"
        data-cy="duration"
        type="select"
        validate={{
          required: { value: true, message: translate('entity.validation.required') },
          min: { value: 5, message: translate('entity.validation.min', { min: 5 }) },
          max: { value: 180, message: translate('entity.validation.max', { max: 180 }) },
          validate: v => isNumber(v) || translate('entity.validation.number'),
        }}
      >
        <option value="15">
          {
            // translate('studySpaceApp.examStoreExam.duration.15')
          }
          15 phút
        </option>
        <option value="30">
          {
            // translate('studySpaceApp.examStoreExam.duration.15')
          }
          30 phút
        </option>
        <option value="45">
          {
            // translate('studySpaceApp.examStoreExam.duration.15')
          }
          45 phút
        </option>
        <option value="60">
          {
            // translate('studySpaceApp.examStoreExam.duration.15')
          }
          60 phút
        </option>
        <option value="90">
          {
            // translate('studySpaceApp.examStoreExam.duration.15')
          }
          90 phút
        </option>
        <option value="120">
          {
            // translate('studySpaceApp.examStoreExam.duration.15')
          }
          120 phút
        </option>
        <option value="160">
          {
            // translate('studySpaceApp.examStoreExam.duration.15')
          }
          160 phút
        </option>
        <option value="180">
          {
            // translate('studySpaceApp.examStoreExam.duration.15')
          }
          180 phút
        </option>
      </ValidatedField>
      <ValidatedField
        label={translate('studySpaceApp.examStoreExam.mix')}
        id="exam-mix"
        name="mix"
        data-cy="mix"
        type="select"
        validate={{
          required: { value: true, message: translate('entity.validation.required') },
          min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
          max: { value: 2, message: translate('entity.validation.max', { max: 2 }) },
          validate: v => isNumber(v) || translate('entity.validation.number'),
        }}
      >
        <option value="0">
          {
            // translate('studySpaceApp.examStoreExam.mix.0')
          }
          Không trộn
        </option>
        <option value="1">
          {
            // translate('studySpaceApp.examStoreExam.mix.0')
          }
          trộn theo nhóm câu hỏi
        </option>
        <option value="2">
          {
            // translate('studySpaceApp.examStoreExam.mix.0')
          }
          trộn loạn xạ
        </option>
      </ValidatedField>
      <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/learning-manager" replace color="info">
        <FontAwesomeIcon icon="arrow-left" />
        &nbsp;
        <span className="d-none d-md-inline">
          <Translate contentKey="entity.action.back">Back</Translate>
        </span>
      </Button>
      &nbsp;
      <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
        <FontAwesomeIcon icon="save" />
        &nbsp;
        <Translate contentKey="entity.action.save">Save</Translate>
      </Button>
    </ValidatedForm>
  );
};

export default ExamUpdateForm;
