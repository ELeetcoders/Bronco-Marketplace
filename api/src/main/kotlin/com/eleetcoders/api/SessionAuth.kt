package com.eleetcoders.api

import jakarta.servlet.*
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
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
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component
import java.io.IOException


@Configuration
//@EnableWebSecurity  //i tried using this but still nothing
class SessionAuth {

    @Bean
    fun SessionFilterRegistrationBean(): FilterRegistrationBean<SessionFilter>? {
        val registrationBean: FilterRegistrationBean<SessionFilter> = FilterRegistrationBean()
        registrationBean.setFilter(SessionFilter())
        registrationBean.addUrlPatterns("/michael", "/tyler", "/login/verify")
        return registrationBean
    }
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        println("filterChain called")
        http
            .authorizeHttpRequests(
                Customizer { authz ->
                    authz.anyRequest().permitAll()
                }
            )
      //      .addFilterBefore(SessionFilter(), RequestMatcher::class.java)
//            .httpBasic(withDefaults())
//            .formLogin()
            .csrf().disable()
            .sessionManagement()
  //              .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  //this controls the JSESSIONID
 //           .sessionAuthenticationStrategy(CookieSessionAuthenticationStrategy("JSESSIONID"))
            .sessionFixation().none()
        return http.build()
    }
}

/* Maybe use this as a work around*/
class SessionFilter : Filter {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        val path: String = (request as? HttpServletRequest)?.requestURI ?: ""
        val cookies: Array<Cookie> = (request as? HttpServletRequest)?.cookies ?: emptyArray()
        val sessionId: String? = cookies.find { it.name == "JSESSIONID" }?.value
        val session: HttpSession? = (request as? HttpServletRequest)?.getSession(false)
        val email: String? = session?.getAttribute("email") as? String

        println("SessionId "+ sessionId)
        println("email "+ email)
        println("session "+ session)

        if (email.equals(null)) {
            val httpResponse: HttpServletResponse = response as HttpServletResponse
                httpResponse.status = HttpServletResponse.SC_UNAUTHORIZED
                httpResponse.writer.write("Unauthorized")
                return
        }
        else {
            chain.doFilter(request, response)
        }

        //println("JSESSIONID: $sessionId")
        //println("Email mapped to session: $email")
        //if ("/tyler".equals(path)) {  /* Testing */
        //    val httpResponse: HttpServletResponse = response as HttpServletResponse
        //    httpResponse.status = HttpServletResponse.SC_UNAUTHORIZED
        //    httpResponse.writer.write("Unauthorized")
        //    return
        //}
        // Check for a valid session here
        // If valid session, continue down chain
        // Else return
        //chain.doFilter(request, response)
    }
}
