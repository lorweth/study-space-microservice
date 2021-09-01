import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './time-table.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TimeTableDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const timeTableEntity = useAppSelector(state => state.timeTable.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="timeTableDetailsHeading">
          <Translate contentKey="studySpaceApp.answerStoreTimeTable.detail.title">TimeTable</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{timeTableEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="studySpaceApp.answerStoreTimeTable.title">Title</Translate>
            </span>
          </dt>
          <dd>{timeTableEntity.title}</dd>
          <dt>
            <span id="time">
              <Translate contentKey="studySpaceApp.answerStoreTimeTable.time">Time</Translate>
            </span>
          </dt>
          <dd>{timeTableEntity.time ? <TextFormat value={timeTableEntity.time} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="note">
              <Translate contentKey="studySpaceApp.answerStoreTimeTable.note">Note</Translate>
            </span>
          </dt>
          <dd>{timeTableEntity.note}</dd>
          <dt>
            <span id="userLogin">
              <Translate contentKey="studySpaceApp.answerStoreTimeTable.userLogin">User Login</Translate>
            </span>
          </dt>
          <dd>{timeTableEntity.userLogin}</dd>
        </dl>
        <Button tag={Link} to="/time-table" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/time-table/${timeTableEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TimeTableDetail;
