import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo-jhipster.png" alt="Logo" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    <span className="brand-title">
      <Translate contentKey="global.title">StudySpace</Translate>
    </span>
    <span className="navbar-version">{VERSION}</span>
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>
        <Translate contentKey="global.menu.home">Home</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const GroupManager = () => (
  <NavItem>
    <NavLink tag={Link} to="/group-manager" className="d-flex align-items-center">
      <FontAwesomeIcon icon="users" />
      <span>
        {/* <Translate contentKey="global.menu.groupManager">Group Manager</Translate> */}
        Group Manager
      </span>
    </NavLink>
  </NavItem>
);

export const QuestionRepositoryManager = () => (
  <NavItem>
    <NavLink tag={Link} to="/question-repository-manager" className="d-flex align-items-center">
      <FontAwesomeIcon icon="archive" />
      <span>
        {/* <Translate contentKey="global.menu.questionRepositoryManager">Question Repository Manager</Translate> */}
        Question Repository Manager
      </span>
    </NavLink>
  </NavItem>
);
