package org.acme.repro;

/**
 * Deliberately nullable: represents "nothing extracted yet" when both fields are {@code null}, the same pattern a
 * real extraction pipeline uses for "not yet known" state.
 */
public record ExtractedFields(String category, String location) {
}
