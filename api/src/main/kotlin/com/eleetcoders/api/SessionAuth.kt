package com.eleetcoders.api

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.Customizer
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.util.matcher.RequestMatcher


@Configuration
class SessionAuth {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        println("filterChain called")
        http
            .authorizeHttpRequests(
                Customizer { authz ->
                    authz
                        .requestMatchers("/michael").authenticated()
                    authz.anyRequest().permitAll()
                }
            )
//            .httpBasic(withDefaults())
//            .formLogin()
            .csrf().disable()
            .sessionManagement()
            .sessionAuthenticationStrategy(CookieSessionAuthenticationStrategy("JSESSIONID"))
            .sessionFixation().none()
        return http.build()
    }
}

class CookieSessionAuthenticationStrategy(private val cookieName: String) : SessionAuthenticationStrategy {

    init {
        println("CookieSessionAuthenticationStrategy initialized with cookie name: $cookieName")
    }

    override fun onAuthentication(authentication: Authentication, request: HttpServletRequest, response: HttpServletResponse) {

        // Check if the session cookie is present
        println("this should run")
        authentication.isAuthenticated = true
        val cookies = request.cookies
        val sessionCookie = cookies?.find { it.name == cookieName }

        if (sessionCookie != null) {
            // Validate the session cookie and authenticate the user
            val sessionId = sessionCookie.value
            // Perform validation of session id and authenticate the user if it is valid
            val authResult = UsernamePasswordAuthenticationToken(authentication.principal, null, authentication.authorities)
            authResult.details = authentication.details
            authResult.isAuthenticated = true
            SecurityContextHolder.getContext().authentication = authResult
            authentication.isAuthenticated = true
        } else {
            // Session cookie is not present, deny access
            //throw AuthenticationCredentialsNotFoundException("Session cookie not found")
        }
    }
}



//class AuthenticationFilter : OncePerRequestFilter() {
//    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
//        val session = request.getSession(false)
//        if (session?.getAttribute("username") == null) {
//            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")
//        } else {
//            filterChain.doFilter(request, response)
//        }
//    }
//}
