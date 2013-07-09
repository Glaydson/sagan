package org.springframework.search.configuration;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.ClientConfig;
import io.searchbox.client.config.ClientConstants;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.util.LinkedHashSet;

@Configuration
public class SearchConfiguration {

	@Bean
	public ElasticsearchOperations elasticsearchTemplate(Client client) throws Exception {
		return new ElasticsearchTemplate(client);
	}

	@Value("${elasticsearch.cluster.nodes:localhost:9300}")
	private String clusterNodes = "localhost:9300";

	@Bean
	public ClientConfig clientConfig() {
		ClientConfig clientConfig = new ClientConfig();
		LinkedHashSet<String> servers = new LinkedHashSet<String>();
		for (String url : this.clusterNodes.split(",")) {
			if (!url.startsWith("http")) {
				url = "http://" + url;
			}
			if (url.endsWith("9300")) {
				url = url.replace("9300", "9200");
			}
			servers.add(url);
		}
		clientConfig.getProperties().put(ClientConstants.SERVER_LIST, servers);
		clientConfig.getProperties().put(ClientConstants.IS_MULTI_THREADED, true);
		return clientConfig;
	}

	@Bean
	public JestClient jestClient() {
		JestClientFactory factory = new JestClientFactory();
		factory.setClientConfig(clientConfig());
		return factory.getObject();
	}
}
