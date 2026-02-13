package com.example.network.common.common_utils

/**
 * Global constants for the networking layer.
 * Contains default query parameters, header keys, and pagination defaults.
 */
object CommonNetworkConstants {
    /** HTTP Method identifier for custom DELETE requests with bodies */
    const val DELETE_METHOD = "DELETE"

    /** Standard HTTP Header key for passing session tokens */
    const val AUTHORIZATION_HEADER = "Authorization"

    /** * Default comma-separated list of fields to include in the API response.
     * Optimizes payload size by requesting only necessary nested objects.
     */
    const val COMMON_INCLUDE = "id,poster.optimized,genres.name,genres.id,name"

    /** * Default comma-separated list of fields to exclude from the API response.
     * Used to strip out redundant or high-bandwidth data.
     */
    const val COMMON_EXCLUDE = "poster.src,poster.thumbnail,poster.preview"

    /** Default number of items to fetch per page */
    const val COMMON_LIMIT = 20

    /** The starting index for paginated requests */
    const val COMMON_START_PAGE = 1
}