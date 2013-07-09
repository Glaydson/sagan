package org.springframework.site.search;

import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.junit.Test;
import org.springframework.search.SearchEntry;
import org.springframework.search.SearchService;

import static org.mockito.Mockito.*;

public class SearchServiceTests {

	private JestClient jestClient = mock(JestClient.class);
	private SearchService searchService = new SearchService(jestClient);

	@Test
	public void saveAnEntry() throws Exception {
		searchService.saveToIndex(new SearchEntry());
		verify(jestClient).execute(any(Index.class));
	}
}
