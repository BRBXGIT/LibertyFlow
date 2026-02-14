package com.example.data.models.common.request.request_parameters

/**
 * A base contract defining the minimum set of filtering criteria
 * required for any content query.
 * @property types The list of content types to filter by (e.g., TV, Movie, OVA).
 * @property search The raw search string entered by the user.
 * @property ageRatings Filter for specific age-appropriate content.
 * @property sorting The order in which results should be returned.
 */
interface RequestParametersBase {
    val types: List<Type>
    val search: String
    val ageRatings: List<AgeRating>
    val sorting: Sorting
}