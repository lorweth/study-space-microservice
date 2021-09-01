import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IAnswerSheet } from 'app/shared/model/AnswerStore/answer-sheet.model';
import { getEntities as getAnswerSheets } from 'app/entities/AnswerStore/answer-sheet/answer-sheet.reducer';
import { getEntity, updateEntity, createEntity, reset } from './answer-sheet-item.reducer';
import { IAnswerSheetItem } from 'app/shared/model/AnswerStore/answer-sheet-item.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AnswerSheetItemUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const answerSheets = useAppSelector(state => state.answerSheet.entities);
  const answerSheetItemEntity = useAppSelector(state => state.answerSheetItem.entity);
  const loading = useAppSelector(state => state.answerSheetItem.loading);
  const updating = useAppSelector(state => state.answerSheetItem.updating);
  const updateSuccess = useAppSelector(state => state.answerSheetItem.updateSuccess);

  const handleClose = () => {
    props.history.push('/answer-sheet-item');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getAnswerSheets({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...answerSheetItemEntity,
      ...values,
      answerSheet: answerSheets.find(it => it.id.toString() === values.answerSheetId.toString()),
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
          ...answerSheetItemEntity,
          answerSheetId: answerSheetItemEntity?.answerSheet?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studySpaceApp.answerStoreAnswerSheetItem.home.createOrEditLabel" data-cy="AnswerSheetItemCreateUpdateHeading">
            <Translate contentKey="studySpaceApp.answerStoreAnswerSheetItem.home.createOrEditLabel">
              Create or edit a AnswerSheetItem
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
                  id="answer-sheet-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('studySpaceApp.answerStoreAnswerSheetItem.questionId')}
                id="answer-sheet-item-questionId"
                name="questionId"
                data-cy="questionId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('studySpaceApp.answerStoreAnswerSheetItem.answerId')}
                id="answer-sheet-item-answerId"
                name="answerId"
                data-cy="answerId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="answer-sheet-item-answerSheet"
                name="answerSheetId"
                data-cy="answerSheet"
                label={translate('studySpaceApp.answerStoreAnswerSheetItem.answerSheet')}
                type="select"
              >
                <option value="" key="0" />
                {answerSheets
                  ? answerSheets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/answer-sheet-item" replace color="info">
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

export default AnswerSheetItemUpdate;
