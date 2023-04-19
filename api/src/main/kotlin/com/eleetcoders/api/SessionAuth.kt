package com.eleetcoders.api

import jakarta.servlet.*
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component
import java.io.IOException


@Configuration
//@EnableWebSecurity  //i tried using this but still nothing
class SessionAuth {

    @Autowired
    private lateinit var authProvider: CustomAuthenticationProvider;

    @Bean
    @Throws(java.lang.Exception::class)
    fun authManager(http: HttpSecurity): AuthenticationManager? {
        val authenticationManagerBuilder = http.getSharedObject(
            AuthenticationManagerBuilder::class.java
        )
        authenticationManagerBuilder.authenticationProvider(authProvider)
        return authenticationManagerBuilder.build()
    }

    fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authProvider)
    }

    @Bean
    fun SessionFilterRegistrationBean(): FilterRegistrationBean<SessionFilter>? {
        val registrationBean: FilterRegistrationBean<SessionFilter> = FilterRegistrationBean()
        registrationBean.setFilter(SessionFilter())
        registrationBean.addUrlPatterns("/michael", "/tyler")
        return registrationBean
    }
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        println("filterChain called")
        http
            .authorizeHttpRequests(
                Customizer { authz ->
                    authz
                        .requestMatchers("/michael")
                    authz.anyRequest().permitAll()
                }
            )
      //      .addFilterBefore(SessionFilter(), RequestMatcher::class.java)
//            .httpBasic(withDefaults())
//            .formLogin()
            .csrf().disable()
            .sessionManagement()
 //           .sessionAuthenticationStrategy(CookieSessionAuthenticationStrategy("JSESSIONID"))
            .sessionFixation().none()
        return http.build()
    }
}

/* Maybe use this as a work around*/
class SessionFilter : Filter {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        println("theres no way")
        val path: String = (request as? HttpServletRequest)?.requestURI ?: ""
        val cookies: Array<Cookie> = (request as? HttpServletRequest)?.cookies ?: emptyArray()
        println(path)
        val sessionId: String? = cookies.find { it.name == "JSESSIONID" }?.value
        println("JSESSIONID: $sessionId")
        if ("/tyler".equals(path)) {  /* Testing */
            val httpResponse: HttpServletResponse = response as HttpServletResponse
            httpResponse.status = HttpServletResponse.SC_UNAUTHORIZED
            httpResponse.writer.write("Unauthorized")
            return
        }
        // Check for a valid session here
        // If valid session, continue down chain
        // Else return
        chain.doFilter(request, response)
    }
}

@Component
class CustomAuthenticationProvider : AuthenticationProvider {
    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        println("this should run but its not")
//        val username = authentication.name
//        val password = authentication.credentials.toString()
        val username = "test"
        val password = "123"
        return UsernamePasswordAuthenticationToken(
            username, password, ArrayList()
        )
//        val username = authentication.name
//        val password = authentication.credentials.toString()
//        val user: User = userRepository.findByUsername(username)
//        return if (user != null && password == user.getPassword()) {
//            val authorities: MutableList<GrantedAuthority> = ArrayList()
//            authorities.add(SimpleGrantedAuthority(user.getRole().name()))
//            UsernamePasswordAuthenticationToken(user.getUsername(), password, authorities)
//        } else {
//            throw BadCredentialsException("Invalid username or password")
//        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        println("This should run too but its not")
        return true
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}

//class CookieSessionAuthenticationStrategy(private val cookieName: String) : SessionAuthenticationStrategy {
//
//    init {
//        println("CookieSessionAuthenticationStrategy initialized with cookie name: $cookieName")
//    }
//
//    override fun onAuthentication(authentication: Authentication, request: HttpServletRequest, response: HttpServletResponse) {
//
//        // Check if the session cookie is present
//        println("this should run")
//        authentication.isAuthenticated = true
//        val cookies = request.cookies
//        val sessionCookie = cookies?.find { it.name == cookieName }
//
//        if (sessionCookie != null) {
//            // Validate the session cookie and authenticate the user
//            val sessionId = sessionCookie.value
//            // Perform validation of session id and authenticate the user if it is valid
//            val authResult = UsernamePasswordAuthenticationToken(authentication.principal, null, authentication.authorities)
//            authResult.details = authentication.details
//            authResult.isAuthenticated = true
//            SecurityContextHolder.getContext().authentication = authResult
//            authentication.isAuthenticated = true
//        } else {
//            // Session cookie is not present, deny access
//            //throw AuthenticationCredentialsNotFoundException("Session cookie not found")
//        }
//    }
//}



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
