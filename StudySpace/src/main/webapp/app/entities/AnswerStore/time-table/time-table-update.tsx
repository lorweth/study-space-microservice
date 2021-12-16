import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './time-table.reducer';
import { ITimeTable } from 'app/shared/model/AnswerStore/time-table.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TimeTableUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const timeTableEntity = useAppSelector(state => state.timeTable.entity);
  const loading = useAppSelector(state => state.timeTable.loading);
  const updating = useAppSelector(state => state.timeTable.updating);
  const updateSuccess = useAppSelector(state => state.timeTable.updateSuccess);
  const handleClose = () => {
    props.history.push('/time-table');
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
    values.time = convertDateTimeToServer(values.time);

    const entity = {
      ...timeTableEntity,
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
          time: displayDefaultDateTime(),
        }
      : {
          ...timeTableEntity,
          time: convertDateTimeFromServer(timeTableEntity.time),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studySpaceApp.answerStoreTimeTable.home.createOrEditLabel" data-cy="TimeTableCreateUpdateHeading">
            <Translate contentKey="studySpaceApp.answerStoreTimeTable.home.createOrEditLabel">Create or edit a TimeTable</Translate>
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
                  id="time-table-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('studySpaceApp.answerStoreTimeTable.title')}
                id="time-table-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('studySpaceApp.answerStoreTimeTable.time')}
                id="time-table-time"
                name="time"
                data-cy="time"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('studySpaceApp.answerStoreTimeTable.note')}
                id="time-table-note"
                name="note"
                data-cy="note"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('studySpaceApp.answerStoreTimeTable.userLogin')}
                id="time-table-userLogin"
                name="userLogin"
                data-cy="userLogin"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/time-table" replace color="info">
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

export default TimeTableUpdate;
