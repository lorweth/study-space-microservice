import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity as getExam } from 'app/entities/ExamStore/exam/exam.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ExamDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getExam(props.match.params.id));
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
        </dl>
        <Button tag={Link} to="/learning-manager" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button to="/take-a-test" replace color="primary" data-cy="entityTakeATestButton">
          <FontAwesomeIcon icon="play" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.take-a-test">Take a test</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExamDetail;
