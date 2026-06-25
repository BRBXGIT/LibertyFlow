package com.brbx.domain.local_dbs.watching_anime.use_case.anime.use_case

import com.brbx.domain.local_dbs.watching_anime.repository.WatchedEpisodesReader
import com.brbx.domain.local_dbs.watching_anime.repository.WatchingAnimeReader
import com.brbx.domain.local_dbs.watching_anime.use_case.anime.model.DomainLatestWatchingAnime
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.lastOrNull

class GetLatestWatchingAnimeUseCase(
    private val animeReader: WatchingAnimeReader,
    private val episodesReader: WatchedEpisodesReader,
) {
    suspend operator fun invoke(): DomainLatestWatchingAnime? {
        val latestAnime = animeReader.getAnime().first().firstOrNull()
        val episodes =
            episodesReader.getEpisodesIndexes(animeId = latestAnime?.id ?: return null)
        val latestEpisodeIndex = episodes.lastOrNull()?.lastOrNull() ?: return null
        return DomainLatestWatchingAnime(
            animeId = latestAnime.id,
            title = latestAnime.title,
            lastEpisodeIndex = latestEpisodeIndex,
        )
    }
}