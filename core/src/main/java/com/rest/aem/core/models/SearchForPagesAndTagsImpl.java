package com.rest.aem.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.rest.aem.core.models.interfaces.SearchForPagesAndTagsService;

@Component(service = SearchForPagesAndTagsService.class, immediate = true)
public class SearchForPagesAndTagsImpl implements SearchForPagesAndTagsService {

	ArrayList<String> resultPaths;

	@Reference
	private QueryBuilder queryBuilder;

	private Session session;

	@Reference
	private ResourceResolverFactory factory;

	public ArrayList<String> getResultPaths() {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ResourceResolverFactory.SUBSERVICE, "writeService");

		try {
			ResourceResolver resourceResolver = factory.getServiceResourceResolver(param);
			session = resourceResolver.adaptTo(Session.class);
			Map<String, String> predicate = new HashMap<>();
			predicate.put("path", "/content/restwithaemcontent/en/books");
			predicate.put("property", "jcr:primaryType");
			predicate.put("property.value", "cq:Page");

			Query query = queryBuilder.createQuery(PredicateGroup.create(predicate), session);
			query.setStart(0);
			query.setHitsPerPage(20);

			SearchResult searchResult = query.getResult();
			if(searchResult != null) {
				searchResult.getQueryStatement();
				for (Hit hit : searchResult.getHits()) {
					resultPaths.add(hit.getPath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.logout();
			}
		}

		return resultPaths;
	}

}
