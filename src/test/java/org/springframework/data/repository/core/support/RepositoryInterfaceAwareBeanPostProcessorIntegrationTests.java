/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.repository.core.support;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.data.querydsl.User;
import org.springframework.data.repository.Repository;

/**
 * @author Oliver Gierke
 */
public class RepositoryInterfaceAwareBeanPostProcessorIntegrationTests {

	DefaultListableBeanFactory factory;

	@Before
	public void setUp() {

		factory = new DefaultListableBeanFactory();

		// Register factory bean for repository
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(DummyRepositoryFactoryBean.class);
		builder.addPropertyValue("repositoryInterface", UserRepository.class);
		factory.registerBeanDefinition("repository", builder.getBeanDefinition());

		// Register predicting BeanPostProcessor
		RepositoryInterfaceAwareBeanPostProcessor processor = new RepositoryInterfaceAwareBeanPostProcessor();
		processor.setBeanFactory(factory);
		factory.addBeanPostProcessor(processor);
	}

	@Test
	public void lookupBeforeInstantiation() {

		String[] strings = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(factory, RepositoryFactoryInformation.class,
				false, false);
		System.out.println("lookupBeforeInstantiation:" + Arrays.toString(strings));
	}

	@Test
	public void lookupAfterInstantiation() {

		factory.getBean(UserRepository.class);

		String[] strings = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(factory, RepositoryFactoryInformation.class,
				false, false);
		System.out.println("lookupAfterInstantiation:" + Arrays.toString(strings));
	}

	interface UserRepository extends Repository<User, Long> {

	}
}
