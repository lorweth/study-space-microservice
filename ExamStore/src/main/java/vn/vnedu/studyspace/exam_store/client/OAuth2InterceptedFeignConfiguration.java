package vn.vnedu.studyspace.exam_store.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import vn.vnedu.studyspace.exam_store.security.oauth2.AuthorizationHeaderUtil;

public class OAuth2InterceptedFeignConfiguration {

    @Bean(name = "oauth2RequestInterceptor")
    public RequestInterceptor getOAuth2RequestInterceptor(AuthorizationHeaderUtil authorizationHeaderUtil) {
        return new TokenRelayRequestInterceptor(authorizationHeaderUtil);
    }
}
