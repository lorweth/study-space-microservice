import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './question-group.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const QuestionGroupDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const questionGroupEntity = useAppSelector(state => state.questionGroup.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="questionGroupDetailsHeading">
          <Translate contentKey="studySpaceApp.examStoreQuestionGroup.detail.title">QuestionGroup</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{questionGroupEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="studySpaceApp.examStoreQuestionGroup.name">Name</Translate>
            </span>
          </dt>
          <dd>{questionGroupEntity.name}</dd>
          <dt>
            <span id="groupId">
              <Translate contentKey="studySpaceApp.examStoreQuestionGroup.groupId">Group Id</Translate>
            </span>
          </dt>
          <dd>{questionGroupEntity.groupId}</dd>
          <dt>
            <span id="userLogin">
              <Translate contentKey="studySpaceApp.examStoreQuestionGroup.userLogin">User Login</Translate>
            </span>
          </dt>
          <dd>{questionGroupEntity.userLogin}</dd>
          <dt>
            <Translate contentKey="studySpaceApp.examStoreQuestionGroup.topic">Topic</Translate>
          </dt>
          <dd>{questionGroupEntity.topic ? questionGroupEntity.topic.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/question-group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/question-group/${questionGroupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuestionGroupDetail;
