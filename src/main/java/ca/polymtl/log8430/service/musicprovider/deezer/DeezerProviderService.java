package ca.polymtl.log8430.service.musicprovider.deezer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeloon.deezer.client.DeezerClient;
import com.zeloon.deezer.domain.Tracks;
import com.zeloon.deezer.domain.internal.search.Search;
import com.zeloon.deezer.io.HttpResourceConnection;

import ca.polymtl.log8430.domain.Track;
import ca.polymtl.log8430.service.musicprovider.MusicProviderService;

/**
 * Service class for managing Deezer API.
 */
@Service
@Transactional
class DeezerProviderService implements MusicProviderService {

	private static final String MUSIC_PROVIDER_NAME = "deezer"; //$NON-NLS-1$
	private final Logger log = LoggerFactory.getLogger(DeezerProviderService.class);
	private final DeezerClient deezerClient = new DeezerClient(new HttpResourceConnection());
	private final DeezerTrackTransformer deezerTrackTransformer = new DeezerTrackTransformer();

	@Override
	public List<Track> search(String query) {
		Tracks deezerTracks = deezerClient.search(new Search(query));
		List<Track> tracks = Optional.ofNullable(deezerTracks.getData())
                .orElseGet(Collections::emptyList)
				.stream()
				.map(deezerTrackTransformer::transform)
				.collect(Collectors.toList());

		return tracks;
	}

	@Override
	public String getProviderName() {
		return MUSIC_PROVIDER_NAME;
	}

}
