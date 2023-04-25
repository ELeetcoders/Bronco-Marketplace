package com.eleetcoders.api

import jakarta.servlet.*
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import java.io.IOException


@Configuration
class SessionAuth {

    @Bean
    fun SessionFilterRegistrationBean(): FilterRegistrationBean<SessionFilter>? {
        val registrationBean: FilterRegistrationBean<SessionFilter> = FilterRegistrationBean()
        registrationBean.setFilter(SessionFilter())
        registrationBean.addUrlPatterns("/login/verify", "/user/create-listing")
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
            .csrf().disable()
            .sessionManagement()
            .sessionFixation().none()
        return http.build()
    }
}

/* Maybe use this as a work around*/
class SessionFilter : Filter {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        val request = req as HttpServletRequest
        if (request.method == "OPTIONS") {
            chain.doFilter(request, response)
        }
        val path: String = request.requestURI ?: ""
        val cookies: Array<Cookie> = request.cookies ?: emptyArray()
        val sessionId: String? = cookies.find { it.name == "JSESSIONID" }?.value
        val session: HttpSession? = request.getSession(false)
        val email: String? = session?.getAttribute("email") as? String

//        if (cookies.size == 0) {
//            println("no cookies")
//        }
//        cookies.forEach { cookie ->
//            println("FILTER: ${cookie.name}=${cookie.value}")
//        }
//
//        val cookieHeader = request.getHeader("Cookie")
//        println("Cookie header: " + cookieHeader)
//        chain.doFilter(request, response)
        //println("Request" + request)
        //println("SessionId "+ sessionId)
        //println("email "+ email)
        //println("session "+ session)

        if (email.equals(null)) {
            val httpResponse: HttpServletResponse = response as HttpServletResponse
                httpResponse.status = HttpServletResponse.SC_UNAUTHORIZED
                httpResponse.writer.write("Unauthorized")
                return
        }
        else {
            chain.doFilter(request, response)
        }
    }
}
