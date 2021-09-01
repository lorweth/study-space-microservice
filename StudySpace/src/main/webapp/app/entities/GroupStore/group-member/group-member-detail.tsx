import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './group-member.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GroupMemberDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const groupMemberEntity = useAppSelector(state => state.groupMember.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="groupMemberDetailsHeading">
          <Translate contentKey="studySpaceApp.groupStoreGroupMember.detail.title">GroupMember</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{groupMemberEntity.id}</dd>
          <dt>
            <span id="userLogin">
              <Translate contentKey="studySpaceApp.groupStoreGroupMember.userLogin">User Login</Translate>
            </span>
          </dt>
          <dd>{groupMemberEntity.userLogin}</dd>
          <dt>
            <span id="role">
              <Translate contentKey="studySpaceApp.groupStoreGroupMember.role">Role</Translate>
            </span>
          </dt>
          <dd>{groupMemberEntity.role}</dd>
          <dt>
            <Translate contentKey="studySpaceApp.groupStoreGroupMember.group">Group</Translate>
          </dt>
          <dd>{groupMemberEntity.group ? groupMemberEntity.group.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/group-member" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/group-member/${groupMemberEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GroupMemberDetail;
