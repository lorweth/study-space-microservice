import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { finishAnswerSheet } from 'app/entities/AnswerStore/answer-sheet/answer-sheet.reducer';
import useCountDown from 'app/shared/custom-hook/useCountDown';
import React, { useEffect, useState } from 'react';
import { Translate } from 'react-jhipster';
import { RouteComponentProps } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';

type PropType = {
  setIsOpen: any;
} & RouteComponentProps<{ id: string }>;

const CompleteTestDialog = (props: PropType) => {
  const dispatch = useAppDispatch();

  const { setIsOpen, history, match, location } = props;
  const [timeLeft, isFinish] = useCountDown(5);
  const answerSheet = useAppSelector(state => state.answerSheet.entity);

  // Close the modal
  const handleClose = () => setIsOpen(false);

  // Stop this test
  const confirmStopTest = () => {
    dispatch(finishAnswerSheet(answerSheet.id));
    history.push(`/learning-manager/${match.params.id}`);
    handleClose();
  };

  return (
    <Modal isOpen>
      <ModalHeader data-cy="examItemDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="studySpaceApp.examStoreExamItem.delete.question">
        {/* <Translate contentKey="studySpaceApp.examStoreExamItem.delete.question" interpolate={{ id: examItemEntity.id }}>
          Are you sure you want to delete this ExamItem?
        </Translate> */}
        Bạn muốn kết thúc bài làm này ?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button
          id="jhi-confirm-delete-examItem"
          data-cy="entityConfirmDeleteButton"
          color="danger"
          disabled={!isFinish}
          onClick={confirmStopTest}
        >
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <span>{timeLeft !== 0 && timeLeft} Kết thúc</span>
          {/* <Translate contentKey="entity.action.delete">Delete</Translate> */}
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default CompleteTestDialog;
