import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getAllAdmins, getAllMembers, getAllWaitings } from 'app/entities/GroupStore/group-member/group-member.reducer';
import React, { useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Button, Card, CardText, CardTitle, Col, Nav, NavItem, NavLink, Row, TabContent, TabPane } from 'reactstrap';

const GroupMemberManager = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const TABS = {
    ADMINTAB: '3',
    MEMBERTAB: '2',
    WAITINGTAB: '1',
  };

  const groupMembers = useAppSelector(state => state.groupMember.entities);

  const [currentActiveTab, setCurrentActiveTab] = useState(TABS.WAITINGTAB);

  useEffect(() => {
    if (currentActiveTab === TABS.WAITINGTAB) {
      dispatch(getAllWaitings({ id: props.match.params.id }));
    }
    if (currentActiveTab === TABS.MEMBERTAB) {
      dispatch(getAllMembers({ id: props.match.params.id }));
    }
    if (currentActiveTab === TABS.ADMINTAB) {
      dispatch(getAllAdmins({ id: props.match.params.id }));
    }
  }, [currentActiveTab]);

  const toggle = tab => {
    if (currentActiveTab !== tab) setCurrentActiveTab(tab);
  };

  return (
    <div>
      <Nav tabs>
        <NavItem>
          <NavLink
            className={currentActiveTab === TABS.WAITINGTAB ? 'active' : ''}
            onClick={() => {
              toggle(TABS.WAITINGTAB);
            }}
          >
            Xin vào
          </NavLink>
        </NavItem>
        <NavItem>
          <NavLink
            className={currentActiveTab === TABS.MEMBERTAB ? 'active' : ''}
            onClick={() => {
              toggle(TABS.MEMBERTAB);
            }}
          >
            Thành viên
          </NavLink>
        </NavItem>
        <NavItem>
          <NavLink
            className={currentActiveTab === TABS.ADMINTAB ? 'active' : ''}
            onClick={() => {
              toggle(TABS.ADMINTAB);
            }}
          >
            Admin
          </NavLink>
        </NavItem>
      </Nav>
      <TabContent activeTab={currentActiveTab}>
        <TabPane tabId={TABS.WAITINGTAB}>
          <Row>
            <Col sm="12">
              <h4>Tab 1 Contents</h4>
            </Col>
          </Row>
        </TabPane>
        <TabPane tabId={TABS.MEMBERTAB}>
          <Row>
            <Col sm="6">
              <Card body>
                <CardTitle>Special Title Treatment</CardTitle>
                <CardText>With supporting text below as a natural lead-in to additional content.</CardText>
                <Button>Go somewhere</Button>
              </Card>
            </Col>
            <Col sm="6">
              <Card body>
                <CardTitle>Special Title Treatment</CardTitle>
                <CardText>With supporting text below as a natural lead-in to additional content.</CardText>
                <Button>Go somewhere</Button>
              </Card>
            </Col>
          </Row>
        </TabPane>
        <TabPane tabId={TABS.ADMINTAB}>
          <Row>
            <Col sm="12">
              <h4>Tab 3 Contents</h4>
            </Col>
          </Row>
        </TabPane>
      </TabContent>
    </div>
  );
};

export default GroupMemberManager;
