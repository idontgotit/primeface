package com.example.controller;

import static org.elasticsearch.index.query.QueryBuilders.disMaxQuery;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;

import com.example.entity.SearchUser;

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
	private List<SearchUser> listUsers;

	@Inject
	private Bean bean;

	@PostConstruct
	public void init() {
		Settings settings = ImmutableSettings.settingsBuilder().put("client.transport.sniff", true)
				.put("cluster.name", "elasticsearch").build();
		transportClient = new TransportClient(settings);

		client = transportClient.addTransportAddress(new InetSocketTransportAddress("192.168.1.25", 9300));
	}

	public void search() {

		searchDocument(client, "users", "user", "user_name", input);
		bean.switchHomeTab("elastic");

	}

	public void searchDocument(Client client, String index, String type, String field, String value) {
		QueryBuilder qb = disMaxQuery().add(wildcardQuery(field, value)).tieBreaker(10f);
		SearchResponse response = client.prepareSearch(index).setTypes(type).setQuery(qb).setFrom(0).setSize(50)
				.setExplain(true).execute().actionGet();

		// termQuery search 1 wildcardQuery

		// System.out.println(response.toString());
		SearchHit[] results = response.getHits().getHits();

		System.out.println("Current results: " + results.length);
		this.listUsers = new ArrayList<SearchUser>();
		for (int i = 0; i < response.getHits().getTotalHits(); i++) {
			Integer id = (Integer) response.getHits().getAt(i).getSource().get("id");
			String userName = (String) response.getHits().getAt(i).getSource().get("user_name");
			String password = (String) response.getHits().getAt(i).getSource().get("password");
			String sex = (String) response.getHits().getAt(i).getSource().get("sex");
			SearchUser newUser = new SearchUser(id, userName, password, sex);
			listUsers.add(newUser);
		}

		System.out.println(listUsers.size());

	}

	public List<SearchUser> getListUsers() {
		return listUsers;
	}

	public void setListUsers(List<SearchUser> listUsers) {
		this.listUsers = listUsers;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

}
