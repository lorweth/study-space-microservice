import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/question">
      <Translate contentKey="global.menu.entities.examStoreQuestion" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/question-group">
      <Translate contentKey="global.menu.entities.examStoreQuestionGroup" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/option">
      <Translate contentKey="global.menu.entities.examStoreOption" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/topic">
      <Translate contentKey="global.menu.entities.examStoreTopic" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/answer-sheet-item">
      <Translate contentKey="global.menu.entities.answerStoreAnswerSheetItem" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/group-time-table">
      <Translate contentKey="global.menu.entities.answerStoreGroupTimeTable" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/group-member">
      <Translate contentKey="global.menu.entities.groupStoreGroupMember" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/group">
      <Translate contentKey="global.menu.entities.groupStoreGroup" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/answer-sheet">
      <Translate contentKey="global.menu.entities.answerStoreAnswerSheet" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/exam-item">
      <Translate contentKey="global.menu.entities.examStoreExamItem" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/time-table">
      <Translate contentKey="global.menu.entities.answerStoreTimeTable" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/exam">
      <Translate contentKey="global.menu.entities.examStoreExam" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
