package org.acme.repro;

import dev.langchain4j.agentic.declarative.SequenceAgent;
import dev.langchain4j.invocation.InvocationParameters;

/**
 * Root of the agentic system. {@code soFar} starts all-null (nothing known yet -- a completely ordinary input for an
 * extraction pipeline), and {@code invocationParameters} is always supplied by every caller.
 */
public interface RootAgent {
	@SequenceAgent(outputKey = "extracted", subAgents = {
		ClassifierAgent.class,
		ExtractorAgent.class
	})
	ExtractedFields process(String input, ExtractedFields soFar, InvocationParameters invocationParameters);
}
