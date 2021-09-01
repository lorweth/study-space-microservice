import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './exam-item.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ExamItemDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const examItemEntity = useAppSelector(state => state.examItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="examItemDetailsHeading">
          <Translate contentKey="studySpaceApp.examStoreExamItem.detail.title">ExamItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{examItemEntity.id}</dd>
          <dt>
            <span id="numOfQuestion">
              <Translate contentKey="studySpaceApp.examStoreExamItem.numOfQuestion">Num Of Question</Translate>
            </span>
          </dt>
          <dd>{examItemEntity.numOfQuestion}</dd>
          <dt>
            <Translate contentKey="studySpaceApp.examStoreExamItem.repo">Repo</Translate>
          </dt>
          <dd>{examItemEntity.repo ? examItemEntity.repo.id : ''}</dd>
          <dt>
            <Translate contentKey="studySpaceApp.examStoreExamItem.exam">Exam</Translate>
          </dt>
          <dd>{examItemEntity.exam ? examItemEntity.exam.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/exam-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/exam-item/${examItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExamItemDetail;
