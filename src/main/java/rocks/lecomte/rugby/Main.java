package rocks.lecomte.rugby;

import rocks.lecomte.rugby.domain.GameResult;
import rocks.lecomte.rugby.impl.PickAndGoResultObtainer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Main {
    public static void main(String ...args) {
        PickAndGoResultObtainer pagro = new PickAndGoResultObtainer();

        List<GameResult> results = pagro.findByCompetition("WRC", "2015");

        Map<String, List<GameResult>> teama = results.stream().collect(groupingBy(GameResult::getTeamA));
        Map<String, List<GameResult>> teamb = results.stream().collect(groupingBy(GameResult::getTeamB));

        for (String team : teama.keySet()) {
            System.out.println(team);
            int points = teama.get(team).stream().map(r -> Integer.valueOf(r.getPointsA())).collect(Collectors.summingInt(Integer::intValue));
            List<GameResult>playedAsB = teamb.get(team);
            if (playedAsB != null) {
                points += playedAsB.stream().map(r -> Integer.valueOf(r.getPointsB())).collect(Collectors.summingInt(Integer::intValue));
            }
            System.out.println("Points " + points);
        }
    }
}
