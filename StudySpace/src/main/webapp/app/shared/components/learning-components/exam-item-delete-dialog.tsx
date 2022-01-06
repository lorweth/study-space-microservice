import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import React from 'react';
import { Translate } from 'react-jhipster';
import { deleteEntity } from 'app/entities/ExamStore/exam-item/exam-item.reducer';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';

type PropType = {
  isOpen: boolean;
  setIsOpen: any;
};

const ItemDeleteDialog = (props: PropType) => {
  const dispatch = useAppDispatch();

  const { isOpen, setIsOpen } = props;

  const examItemEntity = useAppSelector(state => state.examItem.entity);

  const handleClose = () => {
    setIsOpen(false);
  };
  const confirmDelete = () => {
    dispatch(deleteEntity(examItemEntity.id));
    handleClose();
  };

  return (
    <Modal isOpen={isOpen}>
      <ModalHeader data-cy="examItemDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="studySpaceApp.examStoreExamItem.delete.question">
        <Translate contentKey="studySpaceApp.examStoreExamItem.delete.question" interpolate={{ id: examItemEntity.id }}>
          Are you sure you want to delete this ExamItem?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-examItem" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default ItemDeleteDialog;
