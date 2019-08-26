package rocks.lecomte.rugby;

import rocks.lecomte.rugby.domain.GameResult;

import java.util.List;

public interface ResultObtainer {
    List<GameResult> findByCompetition(String id, String year);
    List<GameResult> findByCompetitionAndYears(String id, String ...years);
}
