package rocks.lecomte.rugby;

import rocks.lecomte.rugby.domain.GameResult;
import rocks.lecomte.rugby.domain.SimpleGameResult;
import rocks.lecomte.rugby.impl.PickAndGoResultObtainer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Main {
    public static void main(String... args) {
        PickAndGoResultObtainer pagro = new PickAndGoResultObtainer();
        averageForAllYears(pagro);
    }

    static void averageForAllYears(PickAndGoResultObtainer pagro) {
        List<GameResult> allyears = pagro.findByCompetitionAndYears("WRC", "1987", "1991", "1995", "1999",
                "2003", "2007", "2011", "2015");

        List<SimpleGameResult> results = new LinkedList<>();
        allyears.stream().forEach(r -> {
            results.add(new SimpleGameResult(r.getTeamA(), r.getPointsA()));
            results.add(new SimpleGameResult(r.getTeamB(), r.getPointsB()));
        });

        Map<String, List<SimpleGameResult>> groupedBy = results.stream().collect(groupingBy(SimpleGameResult::getTeam));

        List<SimpleGameResult> averages = new LinkedList<>();
        groupedBy.forEach((k, v) -> averages.add(
                new SimpleGameResult(k, v.stream().mapToDouble(SimpleGameResult::getPoints).sum() / v.size())));
        averages.sort((a, b) -> b.getPoints().compareTo(a.getPoints()));
        averages.forEach(System.out::println);
    }

    static void forYear(PickAndGoResultObtainer pagro, String year) {
        List<GameResult> results = pagro.findByCompetition("WRC", year);

        Map<String, List<GameResult>> teama = results.stream().collect(groupingBy(GameResult::getTeamA));
        Map<String, List<GameResult>> teamb = results.stream().collect(groupingBy(GameResult::getTeamB));

        List<String> totalPoints = new LinkedList<>();
        for (String team : teama.keySet()) {
            double points = teama.get(team).stream().mapToDouble(r -> r.getPointsA()).sum();
            List<GameResult> playedAsB = teamb.get(team);
            if (playedAsB != null) {
                points += playedAsB.stream().mapToDouble(r -> r.getPointsB()).sum();
            }
            totalPoints.add(points + " " + team);
        }
        totalPoints.stream()
                .sorted((a, b) -> new Integer(a.split(" ")[0]).compareTo(Integer.parseInt(b.split(" ")[0])))
                .forEach(System.out::println);
    }
}
