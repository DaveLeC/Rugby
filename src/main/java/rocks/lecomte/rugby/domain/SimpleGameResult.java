package rocks.lecomte.rugby.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SimpleGameResult implements Serializable {
    private String team;
    private Double points;
}
