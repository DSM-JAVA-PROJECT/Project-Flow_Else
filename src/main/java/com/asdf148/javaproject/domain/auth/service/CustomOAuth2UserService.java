package com.asdf148.javaproject.domain.auth.service;

import com.asdf148.javaproject.domain.auth.entity.OAuthAttributes;
import com.asdf148.javaproject.domain.auth.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.
                of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Iterator<String> names = httpSession.getAttributeNames().asIterator();

        while (names.hasNext()){
            System.out.println("enum: " + names.next());
        }

        if(httpSession.getAttribute("OAuthToken") != null){
            httpSession.removeAttribute("OAuthToken " + oAuth2User.getName());
        }

        httpSession.setAttribute("OAuthToken", userRequest.getAccessToken().getTokenValue());
        System.out.println("setAttribute: " + httpSession.getAttribute("OAuthToken"));

        DefaultOAuth2User test = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());

        System.out.println("Test!!");

        return test;
    }

}