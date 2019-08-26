package rocks.lecomte.rugby;

import rocks.lecomte.rugby.domain.GameResult;
import rocks.lecomte.rugby.impl.PickAndGoResultObtainer;

import java.util.LinkedList;
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

        List<String>totalPoints = new LinkedList<>();
        for (String team : teama.keySet()) {
            int points = teama.get(team).stream().mapToInt(r -> Integer.valueOf(r.getPointsA())).sum();
            List<GameResult>playedAsB = teamb.get(team);
            if (playedAsB != null) {
                points += playedAsB.stream().mapToInt(r -> Integer.valueOf(r.getPointsB())).sum();
            }
            totalPoints.add(points + " " + team);
        }
        totalPoints.stream().sorted((a,b) -> new Integer(a.split(" ")[0]).compareTo(Integer.parseInt(b.split(" ")[0]))).forEach(System.out::println);
    }
}
