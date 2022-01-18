import './home.scss';

import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { getLoginUrl, REDIRECT_URL } from 'app/shared/util/url-utils';
import { useAppSelector } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  useEffect(() => {
    const redirectURL = localStorage.getItem(REDIRECT_URL);
    if (redirectURL) {
      localStorage.removeItem(REDIRECT_URL);
      location.href = `${location.origin}${redirectURL}`;
    }
  });

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <h2>
          <Translate contentKey="home.title">Welcome, StudySpace!</Translate>
        </h2>
        <p className="lead">
          <Translate contentKey="home.subtitle">This is your homepage</Translate>
        </p>
        {account?.login ? (
          <div>
            <Alert color="success">
              <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                You are logged in as user {account.login}.
              </Translate>
            </Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>

              <a href={getLoginUrl()} className="alert-link">
                <Translate contentKey="global.messages.info.authenticated.link">sign in</Translate>
              </a>
            </Alert>
          </div>
        )}
        <p>
          <Translate contentKey="home.question">If you have any question on StudySpace:</Translate>
        </p>

        <p>
          <FontAwesomeIcon icon={['fab', 'github-alt']} />
          <a href="https://witcher-vae.github.io/study-space-microservice" target="_blank" rel="noopener noreferrer">
            <Translate contentKey="home.link.homepage">StudySpace homepage</Translate>
          </a>
        </p>
        <p>
          <FontAwesomeIcon icon={['fab', 'telegram']} />
          <a href="https://web.telegram.org/" target="_blank" rel="noopener noreferrer">
            <Translate contentKey="home.link.chat">StudySpace chatroom</Translate>
          </a>
        </p>
      </Col>
    </Row>
  );
};

export default Home;
