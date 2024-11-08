package org.ssmartoffice.authenticationservice.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.ssmartoffice.authenticationservice.client.UserServiceClient
import org.ssmartoffice.authenticationservice.global.jwt.JwtTokenProvider
import org.ssmartoffice.authenticationservice.infrastructure.CookieAuthorizationRequestRepository
import org.ssmartoffice.authenticationservice.security.filter.LoginFilter
import org.ssmartoffice.authenticationservice.security.handler.OAuth2AuthenticationFailureHandler
import org.ssmartoffice.authenticationservice.security.handler.OAuth2AuthenticationSuccessHandler
import org.ssmartoffice.authenticationservice.service.CustomOauth2UserService

@EnableWebSecurity
@Configuration
class SecurityConfig(
    val customOauth2UserService: CustomOauth2UserService,
    val cookieAuthorizationRequestRepository: CookieAuthorizationRequestRepository,
    val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    val oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler,
    val jwtTokenProvider: JwtTokenProvider,
    val userServiceClient: UserServiceClient
) {


    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val authenticationManagerBuilder: AuthenticationManagerBuilder =
            http.getSharedObject(
                AuthenticationManagerBuilder::class.java
            )
        val authenticationManager: AuthenticationManager = authenticationManagerBuilder.build()

        val loginFilter = LoginFilter(authenticationManager, jwtTokenProvider, userServiceClient)

        http.authenticationManager(authenticationManager)

        http
            .httpBasic { obj: HttpBasicConfigurer<HttpSecurity?> -> obj.disable() }
            .csrf { obj: CsrfConfigurer<HttpSecurity?> -> obj.disable() }
            .formLogin { obj: FormLoginConfigurer<HttpSecurity?> -> obj.disable() }
            .sessionManagement { management: SessionManagementConfigurer<HttpSecurity?> ->
                management.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }

        http
            .authorizeHttpRequests { authz ->
                authz.anyRequest().permitAll()
            }

        //oauth2Login
        http.oauth2Login { login: OAuth2LoginConfigurer<HttpSecurity?> ->
            login
                .authorizationEndpoint { endpoint ->
                    endpoint
                        .baseUri("/api/v1/auth/oauth2/authorization")
                        .authorizationRequestRepository(cookieAuthorizationRequestRepository)
                }
                .redirectionEndpoint { endpoint ->
                    endpoint.baseUri("/api/v1/auth/oauth2/code/*")
                }
                .userInfoEndpoint { userInfoEndpoint ->
                    userInfoEndpoint.userService(customOauth2UserService)
                }
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
        }

        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }


    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
