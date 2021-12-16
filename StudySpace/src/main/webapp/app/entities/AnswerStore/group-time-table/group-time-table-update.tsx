import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './group-time-table.reducer';
import { IGroupTimeTable } from 'app/shared/model/AnswerStore/group-time-table.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GroupTimeTableUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const groupTimeTableEntity = useAppSelector(state => state.groupTimeTable.entity);
  const loading = useAppSelector(state => state.groupTimeTable.loading);
  const updating = useAppSelector(state => state.groupTimeTable.updating);
  const updateSuccess = useAppSelector(state => state.groupTimeTable.updateSuccess);
  const handleClose = () => {
    props.history.push('/group-time-table');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.startAt = convertDateTimeToServer(values.startAt);
    values.endAt = convertDateTimeToServer(values.endAt);

    const entity = {
      ...groupTimeTableEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          startAt: displayDefaultDateTime(),
          endAt: displayDefaultDateTime(),
        }
      : {
          ...groupTimeTableEntity,
          startAt: convertDateTimeFromServer(groupTimeTableEntity.startAt),
          endAt: convertDateTimeFromServer(groupTimeTableEntity.endAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studySpaceApp.answerStoreGroupTimeTable.home.createOrEditLabel" data-cy="GroupTimeTableCreateUpdateHeading">
            <Translate contentKey="studySpaceApp.answerStoreGroupTimeTable.home.createOrEditLabel">
              Create or edit a GroupTimeTable
            </Translate>
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
                  id="group-time-table-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('studySpaceApp.answerStoreGroupTimeTable.examId')}
                id="group-time-table-examId"
                name="examId"
                data-cy="examId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('studySpaceApp.answerStoreGroupTimeTable.startAt')}
                id="group-time-table-startAt"
                name="startAt"
                data-cy="startAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('studySpaceApp.answerStoreGroupTimeTable.endAt')}
                id="group-time-table-endAt"
                name="endAt"
                data-cy="endAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('studySpaceApp.answerStoreGroupTimeTable.groupId')}
                id="group-time-table-groupId"
                name="groupId"
                data-cy="groupId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('studySpaceApp.answerStoreGroupTimeTable.note')}
                id="group-time-table-note"
                name="note"
                data-cy="note"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/group-time-table" replace color="info">
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

export default GroupTimeTableUpdate;
