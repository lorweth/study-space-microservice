import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import OptionComponent from 'app/shared/components/OptionFormComponent';
import QuestionFormComponent from 'app/shared/components/QuestionFormComponent';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import {
  createEntity as createRepo,
  getEntity as getRepo,
  updateEntity as updateRepo,
} from 'app/entities/ExamStore/question-group/question-group.reducer';
import {
  reset as resetQuestion,
  getQuestionsFromRepository as getQuestions,
  updateEntity as updateQuestion,
} from 'app/entities/ExamStore/question/question.reducer';
import { getEntities as getTopics } from 'app/entities/ExamStore/topic/topic.reducer';
import { IQuestion } from 'app/shared/model/ExamStore/question.model';
import { cleanEntity, overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { getSortState, translate, Translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { defaultValue as defaultQuestion } from 'app/shared/model/ExamStore/question.model';

const QuestionGroupManager = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );
  const [sorting, setSorting] = useState(false);

  const topics = useAppSelector(state => state.topic.entities);
  const questionGroupEntity = useAppSelector(state => state.questionGroup.entity);
  const loading = useAppSelector(state => state.questionGroup.loading);
  const updating = useAppSelector(state => state.questionGroup.updating);
  const updateSuccess = useAppSelector(state => state.questionGroup.updateSuccess);

  const questionList = useAppSelector(state => state.question.entities);
  const questionListLoading = useAppSelector(state => state.question.loading);
  const questionUpdating = useAppSelector(state => state.question.updating);
  const questionUpdateSuccess = useAppSelector(state => state.question.updateSuccess);
  const links = useAppSelector(state => state.question.links);

  const [selectedQuestion, setSelectedQuestion] = useState<IQuestion>({});

  // Handle Load More question
  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  // Handle click sort
  const sort = p => () => {
    // dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  // Handle click a question in list.
  const handleClickQuestion = (question: IQuestion) => {
    setSelectedQuestion(cleanEntity(question));
  };

  // Handle Click Create new Question.
  const handleClickNewQuestion = () => {
    setSelectedQuestion({ ...defaultQuestion });
  };

  // Handle submit Form QuestionGroup.
  const saveEntity = values => {
    const entity = {
      ...questionGroupEntity,
      ...values,
      topic: topics.find(it => it.id.toString() === values.topic.toString()),
    };

    if (isNew) {
      dispatch(createRepo(entity));
    } else {
      dispatch(updateRepo(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...questionGroupEntity,
          topic: questionGroupEntity?.topic?.id,
        };

  const resetAll = () => {
    dispatch(resetQuestion());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getQuestions({ id: props.match.params.id }));
  };

  const handleSyncList = () => {
    resetAll();
  };

  const handleClose = () => {
    props.history.push('/question-repository-manager');
  };

  useEffect(() => {
    if (!isNew) {
      // Lấy thông tin của QuestionGroup.
      dispatch(getRepo(props.match.params.id));
      // Lấy các Questions của QuestionGroup.
      dispatch(
        getQuestions({
          id: props.match.params.id,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.sort}`,
        })
      );
    }

    dispatch(getTopics({}));
  }, []);

  useEffect(() => {
    if (questionUpdateSuccess) {
      resetAll();
    }
  }, [questionUpdating, questionUpdateSuccess]);

  return (
    <div>
      {/* Tiêu đề QuestionGroup */}
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studySpaceApp.examStoreQuestionGroup.home.createOrEditLabel" data-cy="QuestionGroupCreateUpdateHeading">
            <Translate contentKey="studySpaceApp.examStoreQuestionGroup.home.createOrEditLabel">Create or edit a QuestionGroup</Translate>
          </h2>
        </Col>
      </Row>
      {/* Form QuestionGroup */}
      <Row>
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="question-group-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('studySpaceApp.examStoreQuestionGroup.name')}
                id="question-group-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 5, message: translate('entity.validation.minlength', { min: 5 }) },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                id="question-group-topic"
                name="topic"
                data-cy="topic"
                label={translate('studySpaceApp.examStoreQuestionGroup.topic')}
                type="select"
              >
                <option value="" key="0" />
                {topics
                  ? topics.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/question-repository-manager" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
      {/* Quản lý câu hỏi */}
      <Row className="mt-4" hidden={isNew}>
        {/* Danh sách câu hỏi */}
        <Col md="6">
          <div className="d-flex justify-content-between">
            <Translate contentKey="studySpaceApp.examStoreQuestion.home.title">Question</Translate>
            <div>
              <Button className="me-2" color="info" onClick={handleSyncList} disabled={questionListLoading}>
                <FontAwesomeIcon icon="sync" spin={questionListLoading} />{' '}
                <Translate contentKey="studySpaceApp.examStoreQuestion.home.refreshListLabel">Refresh List</Translate>
              </Button>
              <Button
                color="primary"
                id="new-question"
                data-cy="entityCreateNewQuestion"
                type="button"
                className=""
                onClick={handleClickNewQuestion}
              >
                <FontAwesomeIcon icon="plus" />
                &nbsp;
                <Translate contentKey="studySpaceApp.examStoreQuestion.home.createLabel">Create new Question</Translate>
              </Button>
            </div>
          </div>
          <div className="table-responsive">
            <InfiniteScroll
              dataLength={questionList ? questionList.length : 0}
              next={handleLoadMore}
              hasMore={paginationState.activePage - 1 < links.next}
              loader={<div className="loader">Loading ...</div>}
            >
              {questionList && questionList.length > 0 ? (
                <Table responsive>
                  <thead>
                    <tr>
                      <th className="hand" onClick={sort('id')}>
                        <Translate contentKey="studySpaceApp.examStoreQuestion.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                      </th>
                      <th className="hand" onClick={sort('content')}>
                        <Translate contentKey="studySpaceApp.examStoreQuestion.content">Content</Translate> <FontAwesomeIcon icon="sort" />
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {questionList.map((question, i) => (
                      <tr
                        key={`entity-${i}`}
                        data-cy="entityTable"
                        onClick={() => {
                          handleClickQuestion(question);
                        }}
                      >
                        <td>{question.id}</td>
                        <td>{question.content}</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              ) : (
                !loading && (
                  <div className="alert alert-warning">
                    <Translate contentKey="studySpaceApp.examStoreQuestion.home.notFound">No Questions found</Translate>
                  </div>
                )
              )}
            </InfiniteScroll>
          </div>
        </Col>
        {/* Form Question */}
        <Col md="6">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <QuestionFormComponent selectedQuestion={selectedQuestion} questionGroupId={questionGroupEntity.id} syncList={handleSyncList} />
          )}
        </Col>
      </Row>
    </div>
  );
};
export default QuestionGroupManager;
