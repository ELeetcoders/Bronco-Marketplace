import jakarta.servlet.http.HttpServletRequest

/**
 * Ensure HTTP request is currently logged in to a session.
 */
fun assertAuthenticated(request: HttpServletRequest): Boolean {
    val session = request.getSession(false)
    val loggedIn = (session != null)
    println("Logged in: " + loggedIn)
    return loggedIn
}