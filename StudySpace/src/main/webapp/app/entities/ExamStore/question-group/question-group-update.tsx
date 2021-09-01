import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ITopic } from 'app/shared/model/ExamStore/topic.model';
import { getEntities as getTopics } from 'app/entities/ExamStore/topic/topic.reducer';
import { getEntity, updateEntity, createEntity, reset } from './question-group.reducer';
import { IQuestionGroup } from 'app/shared/model/ExamStore/question-group.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const QuestionGroupUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const topics = useAppSelector(state => state.topic.entities);
  const questionGroupEntity = useAppSelector(state => state.questionGroup.entity);
  const loading = useAppSelector(state => state.questionGroup.loading);
  const updating = useAppSelector(state => state.questionGroup.updating);
  const updateSuccess = useAppSelector(state => state.questionGroup.updateSuccess);

  const handleClose = () => {
    props.history.push('/question-group');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTopics({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...questionGroupEntity,
      ...values,
      topic: topics.find(it => it.id.toString() === values.topicId.toString()),
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
          ...questionGroupEntity,
          topicId: questionGroupEntity?.topic?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studySpaceApp.examStoreQuestionGroup.home.createOrEditLabel" data-cy="QuestionGroupCreateUpdateHeading">
            <Translate contentKey="studySpaceApp.examStoreQuestionGroup.home.createOrEditLabel">Create or edit a QuestionGroup</Translate>
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
                  id="question-group-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('studySpaceApp.examStoreQuestionGroup.name')}
                id="question-group-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 5, message: translate('entity.validation.minlength', { min: 5 }) },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('studySpaceApp.examStoreQuestionGroup.groupId')}
                id="question-group-groupId"
                name="groupId"
                data-cy="groupId"
                type="text"
              />
              <ValidatedField
                label={translate('studySpaceApp.examStoreQuestionGroup.userLogin')}
                id="question-group-userLogin"
                name="userLogin"
                data-cy="userLogin"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="question-group-topic"
                name="topicId"
                data-cy="topic"
                label={translate('studySpaceApp.examStoreQuestionGroup.topic')}
                type="select"
              >
                <option value="" key="0" />
                {topics
                  ? topics.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/question-group" replace color="info">
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

export default QuestionGroupUpdate;
