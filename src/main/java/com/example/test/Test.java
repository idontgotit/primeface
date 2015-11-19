package com.example.test;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.boostingQuery;
import static org.elasticsearch.index.query.QueryBuilders.disMaxQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;
import static org.elasticsearch.index.query.FilterBuilders.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.child.CustomQueryWrappingFilter;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.CustomQueryScorer;

import com.example.entity.Information;
import com.example.entity.Products;
import com.sun.mail.util.SharedByteArrayOutputStream;

public class Test {

	private static TransportClient transportClient;
	private static List<String> listManu = new ArrayList<String>();
	private static List<Information> listInfo = new ArrayList<Information>();

	public static void main(String args[]) throws IOException {
		Settings settings = ImmutableSettings.settingsBuilder().build();
		Client client = new TransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress("104.155.194.6", 9300));

		searchAll(client, "だいわｈじどうしゃせいび");

	}

	public static void searchAll(Client client, String value) {

		QueryBuilder qb = moreLikeThisQuery("_all", "_all").likeText(value).minTermFreq(1).maxQueryTerms(12);
		SearchResponse response = client.prepareSearch()
				.setQuery(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("com_company_furigana", value)))

				.execute().actionGet();

		SearchResponse response2 = client.prepareSearch().setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();

		System.out.println(response.toString());

	}

	public static void searchIndexType(Client client, String index, String type, String field, String value) {
		SearchResponse response = client.prepareSearch(index).setTypes(type)
				.setQuery(QueryBuilders.filteredQuery(QueryBuilders.termQuery(field, value),
						FilterBuilders.andFilter(FilterBuilders.termFilter(field, "benq"))))
				.execute().actionGet();

		/*
		 * SearchResponse response2 = client.prepareSearch(index).setTypes(type)
		 * .setQuery(QueryBuilders.boolQuery()
		 * .must(QueryBuilders.matchQuery(field, value))
		 * .should(QueryBuilders.constantScoreQuery(FilterBuilders.termFilter(
		 * "products_id", "1609")).boost(10f))
		 * .should(QueryBuilders.matchQuery(field, "a")))
		 * .execute().actionGet();
		 */
		SearchResponse response4 = client.prepareSearch("twitter").setQuery(QueryBuilders.matchQuery(field, value))
				.setExplain(true).execute().actionGet();

		SearchResponse response2 = client.prepareSearch("products")
				.setQuery(QueryBuilders.disMaxQuery()
						.add(QueryBuilders.matchQuery("manufacturers_name", "kingston 2g").boost(50f))
						.add(QueryBuilders.matchQuery("manufacturers_name", "kingston 2g").boost(50f)).tieBreaker(0.f)

		).execute().actionGet();

		System.out.println(response4.toString());
	}

	public static void searchDocument(Client client, String index, String type, String field, String value,
			String filter) {
		QueryBuilder qb = wildcardQuery(field, value);
		QueryBuilder qb3 = matchQuery(field, value);

		QueryBuilder qb1 = simpleQueryStringQuery("+" + value + "*").analyzeWildcard(true).lowercaseExpandedTerms(true);
		// AD* voi lowercase = wildcard
		// SearchResponse response =
		// client.prepareSearch("weathers").setQuery(QueryBuilders.matchAllQuery()).execute()
		// .actionGet();

		SearchResponse response = client.prepareSearch().setQuery(qb3)
				.setPostFilter(FilterBuilders.termFilter("product_Manu", filter)).setFrom(0).setSize(1000).execute()
				.actionGet();

		if (response.getHits().getTotalHits() != 0)
			listManu.add((String) response.getHits().getAt(0).getSource().get("product_Manu"));
		System.out.println(response.getTook().getMicros());

		SearchHits results = response.getHits();
		System.out.println(results.getTotalHits());

		System.out.println(response.getHits().getTotalHits());
		for (SearchHit hit : results) {
			System.out.println("------------------------------");
			Map<String, Object> result = hit.getSource();
			System.out.println(result);
		}

		for (int i = 1; i < results.getHits().length; i++) {
			String _manuName = results.getAt(i).getSource().get("product_Manu").toString();
			if (isStringNotInList(_manuName, listManu) == false) {
				listManu.add(_manuName);
			}
		}

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

	public static boolean isStringNotInList(String name, List<String> listManu) {
		for (int i = 0; i < listManu.size(); i++) {
			if (name.equals(listManu.get(i)) == true) {
				return true;
			}
		}
		return false;
	}
}
