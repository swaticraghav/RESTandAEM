package com.rest.aem.core.models;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class JcrUtilService.
 */
@Component(service = JcrUtilService.class, immediate = true, property = { "process.label=Takeda - JCR Service",
		Constants.SERVICE_DESCRIPTION + "=Takeda - JCR Service", Constants.SERVICE_VENDOR + "=Takeda",
		Constants.SERVICE_RANKING + ":Integer=1001" })

public class JcrUtilService {
	private static final Logger LOG = LoggerFactory.getLogger(JcrUtilService.class);

	/**
	 * The Constant SUBSERVICE.
	 */
	private static final String SUBSERVICE = "dataread";

	/**
	 * Activate.
	 *
	 * @param componentContext
	 *            the component context
	 * @throws RepositoryException
	 *             the repository exception
	 * @throws LoginException
	 *             the login exception
	 */
	@Activate
	protected void activate(final ComponentContext componentContext) throws RepositoryException, LoginException {
		LOG.info("JCR Util Service :: Activate Method");
	}

	/**
	 * Gets the service.
	 *
	 * @param serviceType
	 *            the service type
	 * @return the service
	 */
	@SuppressWarnings("unchecked")
	public static Object getService(final Class<?> serviceType) {
		final Bundle bundle = FrameworkUtil.getBundle(serviceType);
		final BundleContext bundleContext = bundle.getBundleContext();
		final ServiceReference serviceReference = bundleContext.getServiceReference(serviceType.getName());

		return bundleContext.getService(serviceReference);
	}

	/**
	 * Gets the resource resolver factory.
	 *
	 * @return the resource resolver factory
	 */
	private static ResourceResolverFactory getResourceResolverFactory() {
		return (ResourceResolverFactory) getService(ResourceResolverFactory.class);
	}

	/**
	 * Gets the resource resolver.
	 *
	 * @return the resource resolver
	 */
	public static ResourceResolver getResourceResolver() {
		ResourceResolver resourceResolver = null;
		try {
			Map<String, Object> param = new HashMap<>();
			param.put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE);
			resourceResolver = getResourceResolverFactory().getServiceResourceResolver(param);
		} catch (LoginException e) {
			LOG.error("Error while getting Resource Resolver :: {}", e);
		}
		return resourceResolver;
	}
}