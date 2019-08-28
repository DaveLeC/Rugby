package rocks.lecomte.rugby;

import com.fasterxml.jackson.databind.ObjectMapper;
import rocks.lecomte.rugby.domain.GameResult;
import rocks.lecomte.rugby.domain.SimpleGameResult;
import rocks.lecomte.rugby.impl.PickAndGoResultObtainer;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Main {
    public static void main(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        GameResult[] allyears = mapper.readValue(new File("data/wrc.json"), GameResult[].class);
        String teamA = "NZL";
        String teamB = "WAL";

        System.out.println(winProbability(teamA, teamB, allyears));
        System.out.println(Arrays.stream(allyears).filter(r -> r.getTeamA().compareTo(teamA) == 0 && r.getTeamB().compareTo(teamB) == 0 ||
                r.getTeamA().compareTo(teamB) == 0 && r.getTeamB().compareTo(teamA) == 0).collect(Collectors.toList()));
        //averageForAllYears();
    }


    static double winProbability(String teamA, String teamB, GameResult[] allyears) {
        return Arrays.stream(allyears).filter(r -> r.getTeamA().compareTo(teamA) == 0 && r.getTeamB().compareTo(teamB) == 0 ||
                r.getTeamA().compareTo(teamB) == 0 && r.getTeamB().compareTo(teamA) == 0)
                .mapToDouble(r -> r.result()).average().orElse(-1);
    }

    static void averageForAllYears(GameResult[] allyears) {
        List<SimpleGameResult> results = new LinkedList<>();

        Arrays.stream(allyears).forEach(r -> {
            results.add(new SimpleGameResult(r.getTeamA(), r.getPointsA()));
            results.add(new SimpleGameResult(r.getTeamB(), r.getPointsB()));
        });

        Map<String, List<SimpleGameResult>> groupedBy = results.stream().collect(groupingBy(SimpleGameResult::getTeam));

        List<SimpleGameResult> averages = new LinkedList<>();
        groupedBy.forEach((k, v) -> averages.add(
                new SimpleGameResult(k, v.stream().mapToDouble(SimpleGameResult::getPoints).average().getAsDouble())));
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
