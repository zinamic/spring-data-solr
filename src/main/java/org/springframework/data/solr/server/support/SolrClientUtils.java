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

import java.io.Closeable;
import java.lang.reflect.Method;

import org.apache.solr.client.solrj.SolrClient;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * {@link SolrClientUtils} replaces SolrServerUtils from version 1.x
 *
 * @author Christoph Strobl
 * @since 2.0
 */
public class SolrClientUtils {

	private SolrClientUtils() {}

	/**
	 * Close the {@link SolrClient} by calling {@link SolrClient#close()} or {@code shutdown} for the generation 5
	 * libraries.
	 *
	 * @param solrClient must not be {@literal null}.
	 * @throws DataAccessResourceFailureException
	 * @since 2.1
	 */
	public static void close(SolrClient solrClient) {

		Assert.notNull(solrClient, "SolrClient must not be null");

		try {
			if (solrClient instanceof Closeable) {
				solrClient.close();
			} else {
				Method shutdownMethod = ReflectionUtils.findMethod(solrClient.getClass(), "shutdown");
				if (shutdownMethod != null) {
					shutdownMethod.invoke(solrClient);
				}
			}
		} catch (Exception e) {
			throw new DataAccessResourceFailureException("Cannot close SolrClient", e);
		}
	}
}
