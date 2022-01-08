import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './answer-sheet.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AnswerSheetDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const answerSheetEntity = useAppSelector(state => state.answerSheet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="answerSheetDetailsHeading">
          <Translate contentKey="studySpaceApp.answerStoreAnswerSheet.detail.title">AnswerSheet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{answerSheetEntity.id}</dd>
          <dt>
            <span id="time">
              <Translate contentKey="studySpaceApp.answerStoreAnswerSheet.time">Time</Translate>
            </span>
          </dt>
          <dd>{answerSheetEntity.time ? <TextFormat value={answerSheetEntity.time} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="userLogin">
              <Translate contentKey="studySpaceApp.answerStoreAnswerSheet.userLogin">User Login</Translate>
            </span>
          </dt>
          <dd>{answerSheetEntity.userLogin}</dd>
          <dt>
            <span id="examId">
              <Translate contentKey="studySpaceApp.answerStoreAnswerSheet.examId">Exam Id</Translate>
            </span>
          </dt>
          <dd>{answerSheetEntity.examId}</dd>
          <dt>
            <Translate contentKey="studySpaceApp.answerStoreAnswerSheet.groupTimeTable">Group Time Table</Translate>
          </dt>
          <dd>{answerSheetEntity.groupTimeTable ? answerSheetEntity.groupTimeTable.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/answer-sheet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/answer-sheet/${answerSheetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AnswerSheetDetail;
