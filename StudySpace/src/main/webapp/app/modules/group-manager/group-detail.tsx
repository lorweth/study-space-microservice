import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from 'app/entities/GroupStore/group/group.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getAllAdmins } from 'app/entities/GroupStore/group-member/group-member.reducer';

export const GroupDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    dispatch(getAllAdmins({ id: props.match.params.id }));
  }, []);

  const groupEntity = useAppSelector(state => state.group.entity);
  const groupMembers = useAppSelector(state => state.groupMember.entities);

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
        &nbsp;
        <Button tag={Link} to={`/group/${groupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
        {groupMembers && groupMembers.length > 0 && (
          <div className="mt-3">
            <h4>Admin nhóm:</h4>
            <ul>
              {groupMembers.map((mem, i) => (
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
