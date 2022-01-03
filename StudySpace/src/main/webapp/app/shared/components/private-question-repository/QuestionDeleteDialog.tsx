import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteEntity } from 'app/entities/ExamStore/question/question.reducer';
import React, { useEffect, useState } from 'react';
import { Translate } from 'react-jhipster';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';

type PropType = {
  selectedQuestionId: number;
  setIsOpen: any;
  isOpen: boolean;
};

export const QuestionDeleteDialog = (props: PropType) => {
  const { selectedQuestionId, isOpen, setIsOpen } = props;

  const dispatch = useAppDispatch();

  const updateSuccess = useAppSelector(state => state.question.updateSuccess);

  const handleClose = () => {
    setIsOpen(false);
  };

  useEffect(() => {
    if (updateSuccess && isOpen) {
      handleClose();
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(selectedQuestionId));
  };

  return (
    <Modal isOpen={isOpen} toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="questionDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="studySpaceApp.examStoreQuestion.delete.question">
        <Translate contentKey="studySpaceApp.examStoreQuestion.delete.question" interpolate={{ id: selectedQuestionId }}>
          Are you sure you want to delete this Question?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-question" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default QuestionDeleteDialog;
