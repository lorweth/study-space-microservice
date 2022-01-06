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
  const [examId, setExamId] = useState<string>('');
  const [isOpenDeleteDialog, setIsOpenDeleteDialog] = useState(false);

  const examUpdateSuccess = useAppSelector(state => state.exam.updateSuccess);
  const itemUpdateSuccess = useAppSelector(state => state.examItem.updateSuccess);
  const examEntity = useAppSelector(state => state.exam.entity);

  useEffect(() => {
    if (!isNew) {
      setExamId(props.match.params.id);
      dispatch(getExam(props.match.params.id)); // Lấy thông tin bài thi
      dispatch(resetItem()); // Xóa thông tin các câu hỏi của bài thi
      dispatch(getItemList({ id: props.match.params.id })); // Lấy danh sách câu hỏi của bài thi
      dispatch(getQuestionGroups({})); // Lấy danh mục câu hỏi của user.
    }
  }, []);

  useEffect(() => {
    if (examUpdateSuccess) {
      setIsNew(false);
      setExamId(examEntity.id.toString());
      dispatch(getQuestionGroups({})); // Lấy danh mục câu hỏi của user.
      props.history.push(`/learning-manager/${examEntity.id}/edit`);
    }
  }, [examUpdateSuccess]);

  useEffect(() => {
    if (!isNew && itemUpdateSuccess) {
      dispatch(getItemList({ id: examId }));
    }
  }, [itemUpdateSuccess]);

  const resetAll = () => {
    dispatch(resetItem()); // Reset toàn bộ state của exam item
    dispatch(getItemList({ id: examId })); // Lấy danh sách các exam item
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
      {!isNew && (
        <Row className="mt-3">
          <Col md="6">
            <ExamItemsList history={props.history} location={props.location} match={props.match} handleSyncList={handleSyncList} />
          </Col>
          <Col md="6">
            <ItemUpdateForm examId={props.match.params.id} setIsOpenDeleteDialog={setIsOpenDeleteDialog} />
            <ItemDeleteDialog isOpen={isOpenDeleteDialog} setIsOpen={setIsOpenDeleteDialog} />
          </Col>
        </Row>
      )}
    </div>
  );
};

export default ExamUpdate;
