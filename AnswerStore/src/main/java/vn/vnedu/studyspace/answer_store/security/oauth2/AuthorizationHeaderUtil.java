package vn.vnedu.studyspace.answer_store.security.oauth2;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderUtil {

    private final ReactiveOAuth2AuthorizedClientService clientService;

    public AuthorizationHeaderUtil(ReactiveOAuth2AuthorizedClientService clientService){
        this.clientService = clientService;
    }

    /**
     * Get accessToken
     * @return the accessToken
     */
    public Mono<String> getAccessToken() {
        return ReactiveSecurityContextHolder
            .getContext()
            .map(SecurityContext::getAuthentication)
            .flatMap(authentication -> {
                if(authentication instanceof OAuth2AuthenticationToken){
                    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                    String name = oauthToken.getName();
                    String registrationId = oauthToken.getAuthorizedClientRegistrationId();
                    return clientService.loadAuthorizedClient(registrationId, name)
                        .switchIfEmpty(Mono.error(new OAuth2AuthorizationException(new OAuth2Error("access_denied", "The token is expired", null))))
                        .map(OAuth2AuthorizedClient::getAccessToken)
                        .map(AbstractOAuth2Token::getTokenValue);
                }else if(authentication instanceof JwtAuthenticationToken){
                    JwtAuthenticationToken accessToken = (JwtAuthenticationToken) authentication;
                    return Mono.just(accessToken.getToken().getTokenValue());
                }
                return Mono.just("");
            });
    }

}
