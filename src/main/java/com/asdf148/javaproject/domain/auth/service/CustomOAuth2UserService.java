package com.asdf148.javaproject.domain.auth.service;

import com.asdf148.javaproject.domain.auth.entity.OAuthAttributes;
import com.asdf148.javaproject.domain.auth.entity.User;
import com.asdf148.javaproject.domain.auth.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        User user = User.builder().build();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.
                of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        if(userRepository.findByEmail(attributes.getEmail()).isEmpty()){
            user = save(attributes);
        }
        else{
            user = update(attributes);
        }

        return new DefaultOAuth2User(
                null,
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User save(OAuthAttributes attributes) {
        User user = User.builder()
                .name(attributes.getName())
                .email(attributes.getEmail())
                .profileImage(attributes.getEmail())
                .build();

        return userRepository.save(user);
    }

    private User update(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail()).orElseThrow();

        User updateUser = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .profileImage(user.getEmail())
                .build();

        return userRepository.save(updateUser);
    }
}