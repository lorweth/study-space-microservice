import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity as getGroup } from 'app/entities/GroupStore/group/group.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, ROLES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getAllAdmins, getMemberDataOfCurrentUser, requestJoinGroup } from 'app/entities/GroupStore/group-member/group-member.reducer';

export const GroupDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getGroup(props.match.params.id));
    dispatch(getAllAdmins({ id: props.match.params.id }));
    dispatch(getMemberDataOfCurrentUser(props.match.params.id));
  }, []);

  const groupEntity = useAppSelector(state => state.group.entity);
  const adminList = useAppSelector(state => state.groupMember.entities);
  const currentUserMember = useAppSelector(state => state.groupMember.entity);

  const requestJoin = e => {
    dispatch(requestJoinGroup(props.match.params.id));
    e.preventDefault();
  };

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="groupDetailsHeading">
          <Translate contentKey="studySpaceApp.groupStoreGroup.detail.title">Group</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{groupEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="studySpaceApp.groupStoreGroup.name">Name</Translate>
            </span>
          </dt>
          <dd>{groupEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        {currentUserMember && currentUserMember.role === ROLES.ADMIN && (
          <>
            &nbsp;
            <Button tag={Link} to={`/group/${groupEntity.id}/edit`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" />{' '}
              <span className="d-none d-md-inline">
                <Translate contentKey="entity.action.edit">Edit</Translate>
              </span>
            </Button>
          </>
        )}
        {currentUserMember && currentUserMember.role === ROLES.ADMIN && (
          <>
            &nbsp;
            <Button tag={Link} to={`/group/${groupEntity.id}/management`} replace color="primary">
              <FontAwesomeIcon icon="user-lock" />{' '}
              <span className="d-none d-md-inline">
                {/* <Translate contentKey="entity.action.edit">Edit</Translate> */}
                Quản lý thành viên
              </span>
            </Button>
          </>
        )}
        {currentUserMember && (currentUserMember.role === ROLES.MEMBER || currentUserMember.role === ROLES.ADMIN) && (
          <>
            &nbsp;
            <Button tag={Link} to={`/group/${groupEntity.id}/management`} replace color="primary">
              <FontAwesomeIcon icon="feather-alt" />{' '}
              <span className="d-none d-md-inline">
                {/* <Translate contentKey="entity.action.edit">Edit</Translate> */}
                Làm bài
              </span>
            </Button>
          </>
        )}
        {!currentUserMember && (
          <>
            &nbsp;
            <Button color="primary" onClick={requestJoin}>
              <FontAwesomeIcon icon="user-plus" />
              <span className="d-none d-md-inline">
                {/* <Translate contentKey="entity.action.add">Add</Translate> */}
                Xin vào
              </span>
            </Button>
          </>
        )}
        {adminList && adminList.length > 0 && (
          <div className="mt-3">
            <h4>Admin nhóm:</h4>
            <ul>
              {adminList.map((mem, i) => (
                <li key={i}>{mem.userLogin}</li>
              ))}
            </ul>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default GroupDetail;
