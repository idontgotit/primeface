package com.example.controller;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.example.entity.Information;

@Named
@SessionScoped
public class Search implements Serializable {

	/**
	 * 
	 */
	/**
	 * 
	 */

	private static TransportClient transportClient;
	private Client client;
	private String input;
	private static final long serialVersionUID = 6352995993531155870L;
	private List<Information> listInformation;
	private ArrayList<String> listManu = null;
	private String selection;

	@Inject
	private Bean bean;

	@PostConstruct
	public void init() {
		Settings settings = ImmutableSettings.settingsBuilder().build();
		transportClient = new TransportClient(settings);

		client = new TransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress("192.168.1.25", 9300));
	}

	public void search() {

		searchDocument(client, "Information", "", "", input);
		bean.switchHomeTab("elastic");

	}

	public void searchAndFilter() {
		searchFilter(client, "com_url", input, selection.toLowerCase());
		bean.switchHomeTab("elastic");

	}

	public void searchDocument(Client client, String index, String type, String field, String value) {

		// SearchResponse response = client.prepareSearch()
		// .setQuery(
		// QueryBuilders.boolQuery().should(QueryBuilders.matchPhraseQuery("com_company_name",
		// value))
		// .should(QueryBuilders.matchPhraseQuery("com_company_furigana",
		// value)))
		// .setFrom(0).setSize(5000).execute().actionGet();
		SearchResponse response = client.prepareSearch()
				.setQuery(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("com_company_name", value))
						.should(QueryBuilders.matchQuery("com_company_furigana", value))
						.should(QueryBuilders.matchPhraseQuery("com_company_name", value).boost(2.0f))
						.should(QueryBuilders.matchPhraseQuery("com_company_furigana", value).boost(2.0f)))
				.setFrom(0).setSize(5000).execute().actionGet();
		// termQuery search 1 wildcardQuery
		// System.out.println(response.toString());

		this.listInformation = new ArrayList<Information>();
		this.listManu = new ArrayList<String>();
		SearchHit[] results = response.getHits().getHits();
		
		for (int i = 0; i < results.length; i++) {
			String com_url;
			String id = (String) (response.getHits().getAt(i).getId());
			String com_company_name = (String) response.getHits().getAt(i).getSource().get("com_company_name");
			String com_company_furigana = (String) response.getHits().getAt(i).getSource().get("com_company_furigana");
			String a = (String) response.getHits().getAt(i).getSource().get("com_url");
			if (a != null) com_url = a;
			else com_url = "";
			Information newProduct = new Information(id, com_company_name, com_company_furigana, com_url);
			listInformation.add(newProduct);
		}
		System.out.println(response.getTook().toString());
		// if (response.getHits().getTotalHits() != 0)
		// listManu.add((String)
		// response.getHits().getAt(0).getSource().get("product_Manu"));
		//
		// SearchHits results1 = response.getHits();
		// for (int i = 1; i < results.length; i++) {
		// String _manuName =
		// results1.getAt(i).getSource().get("product_Manu").toString();
		// if (isStringNotInList(_manuName, listManu) == false) {
		// listManu.add(_manuName);
		// }
		// }
		// System.out.println(listManu.toString());
	}

	public void searchFilter(Client client, String field, String value, String filter) {
		QueryBuilder qb3 = matchQuery(field, value);
		SearchResponse response = client.prepareSearch().setQuery(qb3)
				.setPostFilter(FilterBuilders.termFilter("product_Manu", filter)).setFrom(0).setSize(500).execute()
				.actionGet();
		SearchHit[] results = response.getHits().getHits();
		this.listInformation = new ArrayList<Information>();
		for (int i = 0; i < results.length; i++) {
			String id = (String) (response.getHits().getAt(i).getId());
			String com_company_name = (String) response.getHits().getAt(i).getSource().get("com_company_name");
			String com_company_furigana = (String) response.getHits().getAt(i).getSource().get("com_company_furigana");
			String com_url = (String) response.getHits().getAt(i).getSource().get("com_url");

			Information newProduct = new Information(id, com_company_name, com_company_furigana, com_url);
			listInformation.add(newProduct);
		}

	}

	public boolean isStringNotInList(String name, List<String> listManu) {
		for (int i = 0; i < listManu.size(); i++) {
			if (name.equals(listManu.get(i)) == true) {
				return true;
			}
		}
		return false;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public List<Information> getListInformation() {
		return listInformation;
	}

	public void setListInformation(List<Information> listInformation) {
		this.listInformation = listInformation;
	}

	public List<String> getListManu() {
		return listManu;
	}

	public void setListManu(ArrayList<String> listManu) {
		this.listManu = listManu;
	}

	public String getSelection() {
		return selection;
	}

	public void setSelection(String selection) {
		this.selection = selection;
	}

}
