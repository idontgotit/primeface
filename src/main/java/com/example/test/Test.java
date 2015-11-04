package com.example.test;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.boostingQuery;
import static org.elasticsearch.index.query.QueryBuilders.disMaxQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

public class Test {

	private static TransportClient transportClient;

	public static void main(String args[]) throws IOException {
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch2").build();
		Client client = new TransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress("192.168.1.25", 9302));
		SearchResponse response = client.prepareSearch("weathers").setQuery(QueryBuilders.matchAllQuery()).execute()
				.actionGet();
		System.out.println(response.toString());
	}

	public static void searchDocument(Client client, String index, String type, String field, String value) {
		QueryBuilder qb = wildcardQuery(field, value);
		QueryBuilder qb3 = matchQuery(field, value);
		QueryBuilder qb4 = termQuery(field, value);

		QueryBuilder qb1 = simpleQueryStringQuery("+" + value + "*").analyzeWildcard(true).lowercaseExpandedTerms(true);
		String allField = "_all";
		MultiMatchQueryBuilder mmqb = QueryBuilders.multiMatchQuery(value, allField);
		// AD* voi lowercase = wildcard

		SearchResponse response = client.prepareSearch().setQuery(QueryBuilders.matchAllQuery()).setFrom(0)
				.setSize(8000).execute().actionGet();
		// termQuery search 1 wildcardQuery
		// SearchResponse response2 = client.prepare
		// System.out.println(response.toString());
		SearchHit[] results = response.getHits().getHits();

		System.out.println("Current results: " + results.length);
		for (SearchHit hit : results) {
			System.out.println("------------------------------");
			Map<String, Object> result = hit.getSource();

			System.out.println(result);
		}
		System.out.println(response.getTook().getNanos());

	}

	public static void multiSearch(Client client, String index, String type, String field1, String field2,
			String value1, String value2) {
		QueryBuilder qb = boolQuery().should(wildcardQuery(field1, value1)).should(wildcardQuery(field2, value2));
		SearchResponse response = client.prepareSearch(index).setTypes(type).setQuery(qb).setExplain(true).execute()
				.actionGet();

		// termQuery search 1, wildcardQuery

		// System.out.println(response.toString());
		SearchHit[] results = response.getHits().getHits();

		System.out.println("Current results: " + results.length);
		for (SearchHit hit : results) {
			System.out.println("------------------------------");
			Map<String, Object> result = hit.getSource();
			System.out.println(hit.getExplanation());

			System.out.println(result);
		}

	}

	public static void boostSearch(Client client, String index, String type, String field, String value1,
			String value2) {
		QueryBuilder qb = boostingQuery().positive(termQuery(field, value1)).negative(termQuery(field, value2))
				.negativeBoost(1.2f);
		SearchResponse response = client.prepareSearch(index).setTypes(type).setQuery(qb).setExplain(true).execute()
				.actionGet();

		// termQuery search 1, wildcardQuery

		// System.out.println(response.toString());
		SearchHit[] results = response.getHits().getHits();

		System.out.println("Current results: " + results.length);
		for (SearchHit hit : results) {
			System.out.println("------------------------------");
			Map<String, Object> result = hit.getSource();
			System.out.println(hit.getExplanation());

			System.out.println(result);
		}

	}

	public static void disMax(Client client, String index, String type, String field1, String field2, String value1,
			String value2) {
		QueryBuilder qb = disMaxQuery().add(termQuery(field1, value1)).add(termQuery(field2, value2)).tieBreaker(0.2f);

		SearchResponse response = client.prepareSearch(index).setTypes(type).setQuery(qb).setExplain(true).execute()
				.actionGet();

		// termQuery search 1, wildcardQuery

		// System.out.println(response.toString());

		SearchHit[] results = response.getHits().getHits();

		System.out.println("Current results: " + results.length);
		for (SearchHit hit : results) {
			System.out.println("------------------------------");
			Map<String, Object> result = hit.getSource();
			System.out.println(hit.getExplanation());

			System.out.println(result);
		}

	}

	public static void multiIndex(Client client, String index1, String index2, String type1, String type2,
			String field1, String field2, String value1, String value2) {
		QueryBuilder qb = boolQuery().should(matchQuery(field1, value1)).should(matchQuery(field2, value2));

		SearchResponse response = client.prepareSearch(index1, index2).setTypes(type1, type2).setQuery(qb)
				.setExplain(true).execute().actionGet();

		// termQuery search 1, wildcardQuery

		// System.out.println(response.toString());

		SearchHit[] results = response.getHits().getHits();

		System.out.println("Current results: " + results.length);
		for (SearchHit hit : results) {

			System.out.println("------------------------------");
			Map<String, Object> result = hit.getSource();

			System.out.println(result.get("password"));
			System.out.println(hit.getExplanation());

			System.out.println(result);
			System.out.println(response.getSuccessfulShards());
			System.out.println(response.getTotalShards());
			System.out.println(response.getTook());

		}

	}

	public static void boolSearch(Client client, String index, String type, String field, String value1, String value2,
			String value3, String value4) {
		QueryBuilder qb = boolQuery().should(matchQuery(field, value1)).should(matchQuery(field, value2))
				.should(matchQuery(field, value3)).should(matchQuery(field, value4));

		SearchResponse response = client.prepareSearch(index).setTypes(type).setQuery(qb).setFrom(0).setSize(60)
				.setExplain(true).execute().actionGet();

		SearchHit[] results = response.getHits().getHits();

		System.out.println("Current results: " + results.length);
		for (SearchHit hit : results) {
			System.out.println("------------------------------");
			Map<String, Object> result = hit.getSource();
			System.out.println(hit.getExplanation());
			// System.out.println(hit.getScore());
			System.out.println(result);
		}
	}
}
