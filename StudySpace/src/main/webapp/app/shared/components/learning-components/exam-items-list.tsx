import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { getSortState, Translate } from 'react-jhipster';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { getItemsOfExam as getExamItemList, setEntity as setItem } from 'app/entities/ExamStore/exam-item/exam-item.reducer';

type PropType = RouteComponentProps<{ id: string }> & { handleSyncList: any };

const ExamItemsList = (props: PropType) => {
  const dispatch = useAppDispatch();

  const { handleSyncList } = props;

  const [examId] = props.match.params.id;
  const examItemList = useAppSelector(state => state.examItem.entities);
  const loading = useAppSelector(state => state.examItem.loading);

  const handleClickItem = values => {
    // window.alert(JSON.stringify(values));
    dispatch(setItem(values));
  };

  const handleClickNewItem = () => {
    dispatch(setItem({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="exam-item-heading" data-cy="ExamItemHeading">
        <Translate contentKey="studySpaceApp.examStoreExamItem.home.title">Exam Items</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />
            &nbsp;
            <Translate contentKey="studySpaceApp.examStoreExamItem.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Button
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            onClick={handleClickNewItem}
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="studySpaceApp.examStoreExamItem.home.createLabel">Create new Exam Item</Translate>
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {examItemList && examItemList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand">
                  <Translate contentKey="studySpaceApp.examStoreExamItem.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="studySpaceApp.examStoreExamItem.questionGroup">Question Group</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand">
                  <Translate contentKey="studySpaceApp.examStoreExamItem.numOfQuestion">Num Of Question</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
              </tr>
            </thead>
            <tbody>
              {examItemList.map((examItem, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable" onClick={() => handleClickItem(examItem)}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${examItem.id}`} color="link" size="sm">
                      {examItem.id}
                    </Button>
                  </td>
                  <td>{examItem.questionGroup ? examItem.questionGroup.id : ''}</td>
                  <td>{examItem.numOfQuestion}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="studySpaceApp.examStoreExamItem.home.notFound">No Exam Items found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};
export default ExamItemsList;
