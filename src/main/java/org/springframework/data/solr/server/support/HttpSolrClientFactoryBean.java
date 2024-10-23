/*
 * Copyright 2012 - 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.solr.server.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudHttp2SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * {@link HttpSolrClientFactoryBean} replaces HttpSolrServerFactoryBean from version 1.x.
 *
 * @author Christoph Strobl
 * @since 2.0
 */
public class HttpSolrClientFactoryBean extends HttpSolrClientFactory
		implements FactoryBean<SolrClient>, InitializingBean, DisposableBean {

	private static final String SERVER_URL_SEPARATOR = ",";
	private @Nullable String url;
	private @Nullable Integer timeout;
	private @Nullable Integer maxConnections;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(url, "Solr url must not be null nor empty");
		initSolrClient();
	}

	private void initSolrClient() {
		
		createCloudSolrClient();
		
//		if (this.url.contains(SERVER_URL_SEPARATOR)) {
//			createLoadBalancedHttpSolrClient();
//		} else {
//			createHttpSolrClient();
//		}
		
	}

//	private void createHttpSolrClient() {
//
//		Http2SolrClient.Builder builder = new Http2SolrClient.Builder(this.url);
//
//		if (timeout != null) {
//			builder = builder.withConnectionTimeout(timeout, TimeUnit.MILLISECONDS);
//		}
//
//		if (maxConnections != null) {
//			builder.withMaxConnectionsPerHost(maxConnections);
//		}
//
//		this.setSolrClient(builder.build());
//	}

//	private void createLoadBalancedHttpSolrClient() {
//
//		Http2SolrClient.Builder httpbuilder = new Http2SolrClient.Builder();
//
//		if (timeout != null) {
//			httpbuilder = httpbuilder.withConnectionTimeout(timeout, TimeUnit.MILLISECONDS);
//		}
//
//		if (maxConnections != null) {
//			httpbuilder.withMaxConnectionsPerHost(maxConnections);
//		}
//	    
//		Http2SolrClient httpclient = httpbuilder.build();
//
//		
//		String[] urls = StringUtils.split(this.url, SERVER_URL_SEPARATOR);
//		LBSolrClient.Endpoint[] endpoints = new LBSolrClient.Endpoint[urls.length];
//		for (int i=0; i<urls.length; i++) {
//			endpoints[i] = new LBSolrClient.Endpoint(urls[i]);
//		}
//		
//		LBHttp2SolrClient.Builder solrbuilder = new LBHttp2SolrClient.Builder(httpclient, endpoints);
//
//		this.setSolrClient(solrbuilder.build());
//	}

	private void createCloudSolrClient() {
		
		Http2SolrClient.Builder httpbuilder = new Http2SolrClient.Builder();

		if (timeout != null) {
			httpbuilder = httpbuilder.withConnectionTimeout(timeout, TimeUnit.MILLISECONDS);
		}

		if (maxConnections != null) {
			httpbuilder.withMaxConnectionsPerHost(maxConnections);
		}
		
		List<String> solrUrls = new ArrayList<>();
		for (String url : StringUtils.split(this.url, SERVER_URL_SEPARATOR)) {
			solrUrls.add(url);
		}

		CloudHttp2SolrClient.Builder builder = new CloudHttp2SolrClient.Builder(solrUrls);
		builder.withInternalClientBuilder(httpbuilder);

		this.setSolrClient(builder.build());
	}

	@Override
	public SolrClient getObject() throws Exception {
		return getSolrClient();
	}

	@Override
	public Class<?> getObjectType() {
		if (getSolrClient() == null) {
			return Http2SolrClient.class;
		}
		return getSolrClient().getClass();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public void setMaxConnections(Integer maxConnections) {
		this.maxConnections = maxConnections;
	}

}
