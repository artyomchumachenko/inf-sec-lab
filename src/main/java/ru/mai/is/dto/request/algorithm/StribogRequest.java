package ru.mai.is.dto.request.algorithm;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class StribogRequest {
    private String text;
    private StribogMode mode;

    @Getter
    @RequiredArgsConstructor
    public enum StribogMode {

        @JsonProperty("256")
        MODE_256(false),
        @JsonProperty("512")
        MODE_512(true);

        private final boolean mode;
    }
}
