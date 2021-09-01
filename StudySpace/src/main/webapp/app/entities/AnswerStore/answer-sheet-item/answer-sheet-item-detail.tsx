import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './answer-sheet-item.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AnswerSheetItemDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const answerSheetItemEntity = useAppSelector(state => state.answerSheetItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="answerSheetItemDetailsHeading">
          <Translate contentKey="studySpaceApp.answerStoreAnswerSheetItem.detail.title">AnswerSheetItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{answerSheetItemEntity.id}</dd>
          <dt>
            <span id="questionId">
              <Translate contentKey="studySpaceApp.answerStoreAnswerSheetItem.questionId">Question Id</Translate>
            </span>
          </dt>
          <dd>{answerSheetItemEntity.questionId}</dd>
          <dt>
            <span id="answerId">
              <Translate contentKey="studySpaceApp.answerStoreAnswerSheetItem.answerId">Answer Id</Translate>
            </span>
          </dt>
          <dd>{answerSheetItemEntity.answerId}</dd>
          <dt>
            <Translate contentKey="studySpaceApp.answerStoreAnswerSheetItem.answerSheet">Answer Sheet</Translate>
          </dt>
          <dd>{answerSheetItemEntity.answerSheet ? answerSheetItemEntity.answerSheet.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/answer-sheet-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/answer-sheet-item/${answerSheetItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AnswerSheetItemDetail;
