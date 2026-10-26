package org.acme.repro;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

/**
 * The one AI-backed step, matching the real-world shape (a classifier plus a deterministic extraction step). Its
 * model is never actually called: the generated workflow's input validation fails before any agent runs.
 */
@RegisterAiService
public interface ClassifierAgent {
	@Agent(value = "Classifies the input", outputKey = "classification")
	@UserMessage("Classify this input in one word: {{input}}")
	String classify(String input);
}
