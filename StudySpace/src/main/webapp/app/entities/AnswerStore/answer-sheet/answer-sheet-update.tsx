import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IGroupTimeTable } from 'app/shared/model/AnswerStore/group-time-table.model';
import { getEntities as getGroupTimeTables } from 'app/entities/AnswerStore/group-time-table/group-time-table.reducer';
import { getEntity, updateEntity, createEntity, reset } from './answer-sheet.reducer';
import { IAnswerSheet } from 'app/shared/model/AnswerStore/answer-sheet.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AnswerSheetUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const groupTimeTables = useAppSelector(state => state.groupTimeTable.entities);
  const answerSheetEntity = useAppSelector(state => state.answerSheet.entity);
  const loading = useAppSelector(state => state.answerSheet.loading);
  const updating = useAppSelector(state => state.answerSheet.updating);
  const updateSuccess = useAppSelector(state => state.answerSheet.updateSuccess);

  const handleClose = () => {
    props.history.push('/answer-sheet');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getGroupTimeTables({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.time = convertDateTimeToServer(values.time);

    const entity = {
      ...answerSheetEntity,
      ...values,
      groupTimeTable: groupTimeTables.find(it => it.id.toString() === values.groupTimeTableId.toString()),
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
          ...answerSheetEntity,
          time: convertDateTimeFromServer(answerSheetEntity.time),
          groupTimeTableId: answerSheetEntity?.groupTimeTable?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studySpaceApp.answerStoreAnswerSheet.home.createOrEditLabel" data-cy="AnswerSheetCreateUpdateHeading">
            <Translate contentKey="studySpaceApp.answerStoreAnswerSheet.home.createOrEditLabel">Create or edit a AnswerSheet</Translate>
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
                  id="answer-sheet-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('studySpaceApp.answerStoreAnswerSheet.time')}
                id="answer-sheet-time"
                name="time"
                data-cy="time"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('studySpaceApp.answerStoreAnswerSheet.userLogin')}
                id="answer-sheet-userLogin"
                name="userLogin"
                data-cy="userLogin"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="answer-sheet-groupTimeTable"
                name="groupTimeTableId"
                data-cy="groupTimeTable"
                label={translate('studySpaceApp.answerStoreAnswerSheet.groupTimeTable')}
                type="select"
              >
                <option value="" key="0" />
                {groupTimeTables
                  ? groupTimeTables.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/answer-sheet" replace color="info">
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

export default AnswerSheetUpdate;
