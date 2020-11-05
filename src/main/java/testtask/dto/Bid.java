package testtask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Bid {
    private Long id;
    @JsonProperty(value = "ts")
    private Timestamp timestamp;
    @JsonProperty(value = "ty")
    private String type;
    @JsonProperty(value = "pl")
    private String payLoad;
}
