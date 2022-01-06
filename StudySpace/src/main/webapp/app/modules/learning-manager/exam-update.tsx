import { useAppDispatch, useAppSelector } from 'app/config/store';
import ExamItemsList from 'app/shared/components/learning-components/exam-items-list';
import ExamUpdateForm from 'app/shared/components/learning-components/exam-update-form';
import React, { useEffect, useState } from 'react';
import { Translate } from 'react-jhipster';
import { RouteComponentProps } from 'react-router-dom';
import { Col, Row } from 'reactstrap';
import { getEntity as getExam } from 'app/entities/ExamStore/exam/exam.reducer';
import { getItemsOfExam as getItemList, reset as resetItem } from 'app/entities/ExamStore/exam-item/exam-item.reducer';
import { getEntities as getQuestionGroups } from 'app/entities/ExamStore/question-group/question-group.reducer';
import ItemUpdateForm from 'app/shared/components/learning-components/exam-item-update-form';
import ItemDeleteDialog from 'app/shared/components/learning-components/exam-item-delete-dialog';

const ExamUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);
  const [isOpenDeleteDialog, setIsOpenDeleteDialog] = useState(false);

  const itemUpdateSuccess = useAppSelector(state => state.examItem.updateSuccess);

  useEffect(() => {
    if (!isNew) {
      resetAll();
    }
  }, []);

  useEffect(() => {
    dispatch(getItemList({ id: props.match.params.id }));
  }, [itemUpdateSuccess]);

  const resetAll = () => {
    dispatch(getExam(props.match.params.id));
    dispatch(resetItem());
    dispatch(getItemList({ id: props.match.params.id }));
  };

  const handleSyncList = () => {
    resetAll();
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studySpaceApp.examStoreExam.home.createOrEditLabel" data-cy="ExamCreateUpdateHeading">
            <Translate contentKey="studySpaceApp.examStoreExam.home.createOrEditLabel">Create or edit a Exam</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          <ExamUpdateForm history={props.history} location={props.location} match={props.match} />
        </Col>
      </Row>
      <Row className="mt-3">
        <Col md="6">
          <ExamItemsList history={props.history} location={props.location} match={props.match} handleSyncList={handleSyncList} />
        </Col>
        <Col md="6">
          <ItemUpdateForm examId={props.match.params.id} setIsOpenDeleteDialog={setIsOpenDeleteDialog} />
          <ItemDeleteDialog isOpen={isOpenDeleteDialog} setIsOpen={setIsOpenDeleteDialog} />
        </Col>
      </Row>
    </div>
  );
};

export default ExamUpdate;
