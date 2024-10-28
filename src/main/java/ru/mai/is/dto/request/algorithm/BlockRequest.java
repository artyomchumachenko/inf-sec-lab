package ru.mai.is.dto.request.algorithm;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BlockRequest {
    private String text;
    private BlockMode mode;
    private String key;

    public enum BlockMode {
        @JsonProperty("64")
        MODE_64,
        @JsonProperty("128")
        MODE_128
    }
}
