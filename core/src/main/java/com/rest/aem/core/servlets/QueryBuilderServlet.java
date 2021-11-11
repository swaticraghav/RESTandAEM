package com.rest.aem.core.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.JsonObject;
import com.rest.aem.core.models.SearchDemoModel;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Query Buider Demo Servlet",
		ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
		ServletResolverConstants.SLING_SERVLET_PATHS + "=" + "/bin/query/builder" })
public class QueryBuilderServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	@Reference
	private QueryBuilder queryBuilder;

	private Session session;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.getWriter().println("Servlet Working!");

		try {

			ResourceResolver resourceResolver = request.getResourceResolver();
			session = resourceResolver.adaptTo(Session.class);

			Map<String, String> predicate = new HashMap<>();
			predicate.put("type", "cq:page");
			predicate.put("path", "/content/restwithaemcontent/en");
			predicate.put("1_property", "jcr:content/cq:template");
			predicate.put("1_property.value", "/conf/restwithaem/settings/wcm/templates/content-page");
			Query query = queryBuilder.createQuery(PredicateGroup.create(predicate), session);
			query.setStart(0);
			query.setHitsPerPage(20);

			SearchResult searchResult = query.getResult();
			ArrayList<String> paths = new ArrayList<String>();
			response.getWriter().println("Total Matches: " + searchResult.getTotalMatches());
			for (Hit hit : searchResult.getHits()) {
				paths.add(hit.getPath());
				JsonObject jsonObject = new JsonObject();
				for (int i = 0; i < searchResult.getTotalMatches(); i++) {
					jsonObject.addProperty(String.valueOf(i), hit.getPath());
				}
				response.getWriter().println(jsonObject.toString());

			}

			Resource res = request.getResourceResolver()
					.getResource("/content/restwithaemcontent/en/searchtest/jcr:content/root/responsivegrid/searchnew");
			if(res != null) {
				SearchDemoModel model = res.adaptTo(SearchDemoModel.class);
				model.setResultPaths(paths);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.logout();
			}
		}
	}

}
