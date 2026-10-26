# quarkus-flow#1111 minimal reproducer

Reproduces [quarkiverse/quarkus-flow#1111](https://github.com/quarkiverse/quarkus-flow/issues/1111): quarkus-flow
1.3.0's input schema validation (#1077, default on) rejects any call to a `quarkus-flow-langchain4j` generated
agentic workflow whose agent topology has a nullable record field or an `InvocationParameters` parameter.

No LLM call, no network access, and no real API key are needed -- the failure happens before any agent runs, at
workflow-instance validation time.

## Run it

```
./mvnw test
```

## What it shows

`RootAgent` is a `@SequenceAgent` of a trivial AI-backed `ClassifierAgent` and a plain-Java `ExtractorAgent`. It's
called with `soFar = new ExtractedFields(null, null)` (an ordinary "nothing extracted yet" input) and an
`InvocationParameters` that every caller supplies. The test fails with:

```
WorkflowError[type=https://serverlessworkflow.io/spec/1.0.0/errors/validation, status=400,
instance=org-acme-repro:root-agent:0.0.1, title=Schema validation errors, detail=null found, string expected
null found, string expected
required property 'invocationParameters' not found]
```

- The two `null found, string expected` errors are `ExtractedFields.category()` / `.location()` -- legitimately
  `null`, rejected because the generated schema has no nullable/`Optional` support.
- `required property 'invocationParameters' not found` fires even though the parameter is always supplied, because
  `langchain4j-agentic` deliberately keeps `InvocationParameters`-typed parameters out of `AgenticScope.state()`
  (it's execution context, not a data/prompt variable), and the generated schema has no equivalent awareness.

Add `quarkus.flow.validation.input.enabled=false` to `application.properties` to confirm this is the blocker: the test
then fails differently -- at the `ClassifierAgent.classify()` call itself (it tries to reach the dummy `base-url`
configured above), proving execution got past workflow-input validation and into the agent topology.
