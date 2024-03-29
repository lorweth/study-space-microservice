import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './exam.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ExamDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const examEntity = useAppSelector(state => state.exam.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="examDetailsHeading">
          <Translate contentKey="studySpaceApp.examStoreExam.detail.title">Exam</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{examEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="studySpaceApp.examStoreExam.name">Name</Translate>
            </span>
          </dt>
          <dd>{examEntity.name}</dd>
          <dt>
            <span id="duration">
              <Translate contentKey="studySpaceApp.examStoreExam.duration">Duration</Translate>
            </span>
          </dt>
          <dd>{examEntity.duration}</dd>
          <dt>
            <span id="mix">
              <Translate contentKey="studySpaceApp.examStoreExam.mix">Mix</Translate>
            </span>
          </dt>
          <dd>{examEntity.mix}</dd>
          <dt>
            <span id="groupId">
              <Translate contentKey="studySpaceApp.examStoreExam.groupId">Group Id</Translate>
            </span>
          </dt>
          <dd>{examEntity.groupId}</dd>
          <dt>
            <span id="userLogin">
              <Translate contentKey="studySpaceApp.examStoreExam.userLogin">User Login</Translate>
            </span>
          </dt>
          <dd>{examEntity.userLogin}</dd>
        </dl>
        <Button tag={Link} to="/exam" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/exam/${examEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExamDetail;
