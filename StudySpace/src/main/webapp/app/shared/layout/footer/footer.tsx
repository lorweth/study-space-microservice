import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Row } from 'reactstrap';

const Footer = () => (
  <footer className="footer-04">
    <div className="container">
      <div className="row">
        <div className="col-md-6 col-lg-3 mb-md-0 mb-4">
          <h2 className="footer-heading">
            <a href="#" className="logo">
              StudySpace
            </a>
          </h2>
          <p>Phần mềm quản lý và hỗ trợ tự học trắc nghiệm.</p>
          <a href="https://witcher-vae.github.io/study-space-microservice">
            read more <span className="ion-ios-arrow-round-forward"></span>
          </a>
        </div>
        <div className="col-md-6 col-lg-3 mb-md-0 mb-4">
          <h2 className="footer-heading">Thực hiện</h2>
          <ul className="list-unstyled">
            <li>
              <a href="https://www.facebook.com/loc.yen.512" className="py-1 d-block">
                Đặng Hữu Lộc
              </a>
            </li>
            <li>
              <a href="https://www.facebook.com/yen.loc.805" className="py-1 d-block">
                Nguyễn Ngọc Minh Yến
              </a>
            </li>
          </ul>
        </div>

        <div className="col-md-6 col-lg-3 mb-md-0 mb-4">
          <h2 className="footer-heading">Hướng dẫn</h2>
          <ul className="list-unstyled">
            <li>
              <a href="https://www.facebook.com/loc.yen.512" className="py-1 d-block">
                ThS. Trần Thị Diễm Trang
              </a>
            </li>
            <li>
              <a href="https://www.facebook.com/yen.loc.805" className="py-1 d-block">
                Ks. Nguyễn Thái Duy
              </a>
            </li>
          </ul>
        </div>

        <div className="col-md-6 col-lg-3 mb-md-0 mb-4">
          <h2 className="footer-heading">Tag cloud</h2>
          <div className="tagcloud">
            <a href="#" className="tag-cloud-link">
              Microservice
            </a>
            <a href="#" className="tag-cloud-link">
              React
            </a>
            <a href="#" className="tag-cloud-link">
              Spring Boot
            </a>
            <a href="#" className="tag-cloud-link">
              Spring WebFlux
            </a>
            <a href="#" className="tag-cloud-link">
              Spring Security
            </a>
            <a href="#" className="tag-cloud-link">
              Keycloak
            </a>
            <a href="#" className="tag-cloud-link">
              Kafka
            </a>
            <a href="#" className="tag-cloud-link">
              Kubernetes
            </a>
          </div>
        </div>
      </div>
    </div>
    <div className="w-100 mt-5 border-top py-5">
      <div className="container">
        <div className="row">
          <div className="col-md-6 col-lg-8">
            <p className="copyright">
              Copyright &copy; 2022 All rights reserved | This application is made with love{' '}
              <i className="ion-ios-heart" aria-hidden="true"></i> by{' '}
              <a href="https://github.com/witcher-vae" target="_blank" rel="noreferrer">
                Witcher-vae
              </a>
            </p>
          </div>
          <div className="col-md-6 col-lg-4 text-md-right">
            <p className="mb-0 list-unstyled">
              <a className="mr-md-3" href="#">
                Terms
              </a>
              <a className="mr-md-3" href="#">
                Privacy
              </a>
              <a className="mr-md-3" href="#">
                Compliances
              </a>
            </p>
          </div>
        </div>
      </div>
    </div>
  </footer>
);

export default Footer;
