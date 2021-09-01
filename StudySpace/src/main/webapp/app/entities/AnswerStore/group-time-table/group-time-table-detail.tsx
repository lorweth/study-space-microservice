import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './group-time-table.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GroupTimeTableDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const groupTimeTableEntity = useAppSelector(state => state.groupTimeTable.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="groupTimeTableDetailsHeading">
          <Translate contentKey="studySpaceApp.answerStoreGroupTimeTable.detail.title">GroupTimeTable</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{groupTimeTableEntity.id}</dd>
          <dt>
            <span id="examId">
              <Translate contentKey="studySpaceApp.answerStoreGroupTimeTable.examId">Exam Id</Translate>
            </span>
          </dt>
          <dd>{groupTimeTableEntity.examId}</dd>
          <dt>
            <span id="startAt">
              <Translate contentKey="studySpaceApp.answerStoreGroupTimeTable.startAt">Start At</Translate>
            </span>
          </dt>
          <dd>
            {groupTimeTableEntity.startAt ? <TextFormat value={groupTimeTableEntity.startAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endAt">
              <Translate contentKey="studySpaceApp.answerStoreGroupTimeTable.endAt">End At</Translate>
            </span>
          </dt>
          <dd>
            {groupTimeTableEntity.endAt ? <TextFormat value={groupTimeTableEntity.endAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="groupId">
              <Translate contentKey="studySpaceApp.answerStoreGroupTimeTable.groupId">Group Id</Translate>
            </span>
          </dt>
          <dd>{groupTimeTableEntity.groupId}</dd>
          <dt>
            <span id="note">
              <Translate contentKey="studySpaceApp.answerStoreGroupTimeTable.note">Note</Translate>
            </span>
          </dt>
          <dd>{groupTimeTableEntity.note}</dd>
        </dl>
        <Button tag={Link} to="/group-time-table" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/group-time-table/${groupTimeTableEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GroupTimeTableDetail;
