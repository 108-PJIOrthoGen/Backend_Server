package com.vietnam.pji.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Lightweight progress (thought-log) message published by the Python RAG worker
 * during recommendation processing. Relayed directly to the SSE stream — never
 * persisted.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMQProgressMessage implements Serializable {

    @JsonProperty("run_id")
    private Long runId;

    private String message;

    /** Optional: "start", "step", "done", "error". Defaults to "step" when null. */
    private String stage;
}
