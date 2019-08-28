package rocks.lecomte.rugby.domain;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class GameResult implements Serializable, Comparable<GameResult> {
    private Round round;
    private String year, teamA, teamB, scoreA, scoreB, triesA, triesB;
    private Double pointsA, pointsB;
    private String venue;

    @Override
    public int compareTo(GameResult gr) {

        return round == gr.round && teamA.compareTo(gr.teamA) == 0 && teamB.compareTo(gr.teamB) == 0
                && year.compareTo(gr.year) == 0 ? 0 : year.compareTo(gr.year);
    }
}
