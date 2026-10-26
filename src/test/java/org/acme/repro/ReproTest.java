package org.acme.repro;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import dev.langchain4j.invocation.InvocationParameters;
import io.quarkus.test.junit.QuarkusTest;

/**
 * Reproduces quarkiverse/quarkus-flow#1111: quarkus-flow 1.3.0's input schema validation (#1077, default on) rejects
 * every call to a quarkus-flow-langchain4j generated agentic workflow whose agent topology has nullable fields or an
 * {@link InvocationParameters} parameter. No LLM, no network, no API key needed -- the failure happens before any
 * agent runs.
 */
@QuarkusTest
class ReproTest {
	@Inject
	RootAgent agent;

	@Test
	void legitimateInputFailsSchemaValidation() {
		// soFar represents "nothing extracted yet" -- a completely ordinary input.
		// invocationParameters is always supplied by every caller.
		// Both are rejected: io.serverlessworkflow.impl.WorkflowException: WorkflowError[... Schema validation
		// errors, detail=required property 'category' not found / 'location' not found / 'extracted' not found /
		// 'invocationParameters' not found]
		var result = agent.process("hello", new ExtractedFields(null, null), new InvocationParameters());

		System.out.println("Result: " + result);
	}
}
