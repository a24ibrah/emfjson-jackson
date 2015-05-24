/*
 * Copyright (c) 2015 Guillaume Hillairet.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Guillaume Hillairet - initial API and implementation
 *
 */
package org.emfjson.jackson.junit.tests;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

import org.emfjson.jackson.junit.model.ModelFactory;
import org.emfjson.jackson.junit.model.Node;
import org.emfjson.jackson.junit.support.TestSupport;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ExternalReferencesTest extends TestSupport {

	@Test
	public void testSaveExternalReferenceOnSameBaseURI() throws IOException {
		Resource first = resourceSet.createResource(URI.createURI("file://folder/first.json"));
		Resource second = resourceSet.createResource(URI.createURI("file://folder/second.json"));

		Node n1 = ModelFactory.eINSTANCE.createNode();
		Node n2 = ModelFactory.eINSTANCE.createNode();
		n2.setTarget(n1);

		first.getContents().add(n1);
		second.getContents().add(n2);

		JsonNode result = mapper.valueToTree(second);

		assertEquals("first.json#/", result.get("target").get("$ref").asText());
	}

	@Test
	public void testSaveExternalReferenceWithDifferentBaseURI() throws IOException {
		Resource first = resourceSet.createResource(URI.createURI("file://folder/first.json"));
		Resource second = resourceSet.createResource(URI.createURI("file://folder/other/second.json"));

		Node n1 = ModelFactory.eINSTANCE.createNode();
		Node n2 = ModelFactory.eINSTANCE.createNode();
		n2.setTarget(n1);

		first.getContents().add(n1);
		second.getContents().add(n2);

		JsonNode result = mapper.valueToTree(second);

		assertEquals("../first.json#/", result.get("target").get("$ref").asText());
	}

}
