import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import GroupMemberComponent from 'app/shared/components/group-member-management/group-member-component';
import React, { useState } from 'react';
import { JhiItemCount, JhiPagination, Translate } from 'react-jhipster';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Card, CardText, CardTitle, Col, Nav, NavItem, NavLink, Row, TabContent, Table, TabPane } from 'reactstrap';

const GroupManager = (props: RouteComponentProps<{ id: string; url: string }>) => {
  const [currentActiveTab, setCurrentActiveTab] = useState('1');

  const toggle = tab => {
    if (currentActiveTab !== tab) setCurrentActiveTab(tab);
  };

  return (
    <div>
      <Nav tabs>
        <NavItem>
          <NavLink
            className={currentActiveTab === '1' ? 'active' : ''}
            onClick={() => {
              toggle('1');
            }}
          >
            Quản lý thành viên
          </NavLink>
        </NavItem>
        <NavItem>
          <NavLink
            className={currentActiveTab === '2' ? 'active' : ''}
            onClick={() => {
              toggle('2');
            }}
          >
            Quản lý kho câu hỏi
          </NavLink>
        </NavItem>
      </Nav>
      <TabContent activeTab={currentActiveTab}>
        <TabPane tabId="1">
          <Row>
            <Col sm="12">
              <GroupMemberComponent history={props.history} location={props.location} match={props.match} />
            </Col>
          </Row>
        </TabPane>
        <TabPane tabId="2">
          <Row>
            <Col sm="12">
              <h3>Quản lý kho câu hỏi</h3>
            </Col>
          </Row>
        </TabPane>
      </TabContent>
    </div>
  );
};

export default GroupManager;
