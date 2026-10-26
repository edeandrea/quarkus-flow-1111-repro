package org.acme.repro;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.invocation.InvocationParameters;

/**
 * A plain (non-AI) agent step, same pattern as a deterministic "no LLM" agent. It is only ever reached after the
 * generated workflow's upfront input validation has already run -- which is the bug: validation happens before this
 * step, or any step, executes.
 */
public final class ExtractorAgent {
	private ExtractorAgent() {
	}

	@Agent(value = "Extracts fields from the input", outputKey = "extracted")
	public static ExtractedFields extract(String input, InvocationParameters invocationParameters) {
		return new ExtractedFields("DEMO-CATEGORY", "DEMO-LOCATION");
	}
}
