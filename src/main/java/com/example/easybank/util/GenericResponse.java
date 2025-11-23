package com.example.easybank.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse {
    private String message;
    private Object data;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int pageNumber;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int pageSize;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long totalElements;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int totalPages;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean first;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean last;

    @Builder.Default
    @JsonIgnore
    private HttpStatus status = HttpStatus.OK;

    public ResponseEntity<GenericResponse> buildResponse() {
        return ResponseEntity.status(status).body(this);
    }
}

