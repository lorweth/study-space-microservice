import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './option.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OptionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const optionEntity = useAppSelector(state => state.option.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="optionDetailsHeading">
          <Translate contentKey="studySpaceApp.examStoreOption.detail.title">Option</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{optionEntity.id}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="studySpaceApp.examStoreOption.content">Content</Translate>
            </span>
          </dt>
          <dd>{optionEntity.content}</dd>
          <dt>
            <span id="isCorrect">
              <Translate contentKey="studySpaceApp.examStoreOption.isCorrect">Is Correct</Translate>
            </span>
          </dt>
          <dd>{optionEntity.isCorrect ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="studySpaceApp.examStoreOption.question">Question</Translate>
          </dt>
          <dd>{optionEntity.question ? optionEntity.question.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/option" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/option/${optionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OptionDetail;
