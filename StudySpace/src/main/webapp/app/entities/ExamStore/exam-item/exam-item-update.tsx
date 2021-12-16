import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IQuestionGroup } from 'app/shared/model/ExamStore/question-group.model';
import { getEntities as getQuestionGroups } from 'app/entities/ExamStore/question-group/question-group.reducer';
import { IExam } from 'app/shared/model/ExamStore/exam.model';
import { getEntities as getExams } from 'app/entities/ExamStore/exam/exam.reducer';
import { getEntity, updateEntity, createEntity, reset } from './exam-item.reducer';
import { IExamItem } from 'app/shared/model/ExamStore/exam-item.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ExamItemUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const questionGroups = useAppSelector(state => state.questionGroup.entities);
  const exams = useAppSelector(state => state.exam.entities);
  const examItemEntity = useAppSelector(state => state.examItem.entity);
  const loading = useAppSelector(state => state.examItem.loading);
  const updating = useAppSelector(state => state.examItem.updating);
  const updateSuccess = useAppSelector(state => state.examItem.updateSuccess);
  const handleClose = () => {
    props.history.push('/exam-item');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getQuestionGroups({}));
    dispatch(getExams({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...examItemEntity,
      ...values,
      questionGroup: questionGroups.find(it => it.id.toString() === values.questionGroup.toString()),
      exam: exams.find(it => it.id.toString() === values.exam.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...examItemEntity,
          questionGroup: examItemEntity?.questionGroup?.id,
          exam: examItemEntity?.exam?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studySpaceApp.examStoreExamItem.home.createOrEditLabel" data-cy="ExamItemCreateUpdateHeading">
            <Translate contentKey="studySpaceApp.examStoreExamItem.home.createOrEditLabel">Create or edit a ExamItem</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="exam-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('studySpaceApp.examStoreExamItem.numOfQuestion')}
                id="exam-item-numOfQuestion"
                name="numOfQuestion"
                data-cy="numOfQuestion"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="exam-item-questionGroup"
                name="questionGroup"
                data-cy="questionGroup"
                label={translate('studySpaceApp.examStoreExamItem.questionGroup')}
                type="select"
              >
                <option value="" key="0" />
                {questionGroups
                  ? questionGroups.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="exam-item-exam"
                name="exam"
                data-cy="exam"
                label={translate('studySpaceApp.examStoreExamItem.exam')}
                type="select"
              >
                <option value="" key="0" />
                {exams
                  ? exams.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/exam-item" replace color="info">
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
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ExamItemUpdate;
