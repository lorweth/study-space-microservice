import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import React from 'react';
import { isNumber, translate, Translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { Link } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { createEntity as createItem, updateEntity as updateItem } from 'app/entities/ExamStore/exam-item/exam-item.reducer';

type PropType = {
  examId: string;
  setIsOpenDeleteDialog: any;
};

// Xử lý trực tiếp trên ExamItemEntity trong redux
const ItemUpdateForm = (props: PropType) => {
  const dispatch = useAppDispatch();
  const { examId, setIsOpenDeleteDialog } = props;

  const item = useAppSelector(state => state.examItem.entity);
  const questionGroups = useAppSelector(state => state.questionGroup.entities);
  const itemLoading = useAppSelector(state => state.examItem.loading);
  const itemUpdating = useAppSelector(state => state.examItem.updating);

  const saveEntity = values => {
    const entity = {
      ...item,
      ...values,
      questionGroup: questionGroups.find(it => it.id.toString() === values.questionGroup.toString()),
      exam: { id: examId },
    };

    if (item.id) {
      dispatch(updateItem(entity));
    } else {
      dispatch(createItem(entity));
    }
  };

  const defaultValues = () => (item.id ? { ...item } : {});

  return (
    <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity} disabled={itemLoading}>
      {item.id ? (
        <ValidatedField name="id" required readOnly id="exam-item-id" label={translate('global.field.id')} validate={{ required: true }} />
      ) : null}
      <ValidatedField
        id="exam-item-questionGroup"
        name="questionGroup"
        data-cy="questionGroup"
        label={translate('studySpaceApp.examStoreExamItem.questionGroup')}
        type="select"
        validate={{ required: true }}
      >
        {questionGroups
          ? questionGroups.map(otherEntity => (
              <option value={otherEntity.id} key={otherEntity.id}>
                {otherEntity.id}
              </option>
            ))
          : null}
      </ValidatedField>
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
      <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={itemUpdating}>
        <FontAwesomeIcon icon="save" />
        &nbsp;
        <Translate contentKey="entity.action.save">Save</Translate>
      </Button>
      &nbsp;
      <Button
        color="danger"
        id="delete-entity"
        data-cy="entityCreateDeleteButton"
        type="button"
        disabled={itemUpdating || itemLoading || !item.id}
        onClick={() => setIsOpenDeleteDialog(true)}
      >
        <FontAwesomeIcon icon="trash" />
        &nbsp;
        <Translate contentKey="entity.action.delete">Delete</Translate>
      </Button>
    </ValidatedForm>
  );
};
export default ItemUpdateForm;
