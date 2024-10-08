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
package org.springframework.data.solr.repository.query;

import java.util.List;

import org.springframework.data.repository.query.Parameters;
import org.springframework.data.repository.query.ParametersSource;

/**
 * @author Christoph Strobl
 */
public class SolrParameters extends Parameters<SolrParameters, SolrParameter> {

	public SolrParameters(ParametersSource parametersSource) {
		super(parametersSource, param -> new SolrParameter(param, parametersSource.getDomainTypeInformation()));
	}

	public SolrParameters(List<SolrParameter> parameters) {
		super(parameters);
	}

	@Override
	protected SolrParameters createFrom(List<SolrParameter> parameters) {
		return new SolrParameters(parameters);
	}

}
