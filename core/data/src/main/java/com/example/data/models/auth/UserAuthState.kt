package com.example.data.models.auth

/**
 * Represents the restricted hierarchy of user authentication states within the application.
 *
 * This interface is used to drive UI logic and determine access levels for protected
 * resources. Using a `sealed interface` ensures compile-time safety and exhaustive
 * checks when used in `when` expressions.
 */
sealed interface UserAuthState {

    /**
     * Indicates that the user is currently authenticated.
     * * In this state, the application provides access to protected features
     * and maintains an active user session.
     */
    data object LoggedIn: UserAuthState

    /**
     * Indicates that the user is not authenticated or has been logged out.
     * * In this state, the application doesn't provides access to protected features
     */
    data object LoggedOut: UserAuthState
}