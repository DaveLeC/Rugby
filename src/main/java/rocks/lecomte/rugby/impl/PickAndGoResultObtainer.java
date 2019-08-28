package rocks.lecomte.rugby.impl;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import rocks.lecomte.rugby.ResultObtainer;
import rocks.lecomte.rugby.domain.GameResult;
import rocks.lecomte.rugby.domain.Round;

import java.io.IOException;
import java.util.*;

import static rocks.lecomte.rugby.domain.Round.*;

public class PickAndGoResultObtainer implements ResultObtainer {
    private ResourceBundle bundle = ResourceBundle.getBundle("pickandgo");

    public List<GameResult> findByCompetitionAndYears(String id, String ...years) {
        List<GameResult>all = new LinkedList<>();

        for (String year : years) {
            all.addAll(findByCompetition(id, year));
        }

        return all;
    }

    public List<GameResult> findByCompetition(String id, String year) {
        String uri = bundle.getString(id + ".url");

        uri = uri.replaceAll(":year", year);

        List<GameResult> results = new LinkedList<>();

        try (final WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage(uri);

            List<DomElement> tables = page.getElementsByTagName("table");
            DomElement el = tables.get(1);

            DomNodeList<HtmlElement> rows = el.getElementsByTagName("tr");

            ListIterator<HtmlElement> listIterator = rows.listIterator();

            listIterator.next(); // ignore header

            listIterator.forEachRemaining(r -> results.add(mapToGameResult(year, (HtmlTableRow) r)));

           // results.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return results;
    }

    /*
    Date
Tourn
Rnd
Match
Score
Tries
Pnts
Venue
     */
    private GameResult mapToGameResult(String year, HtmlTableRow r) {
        final DomNodeList<HtmlElement> td = r.getElementsByTagName("td");

        Round round;
        switch (td.get(2).getTextContent().split(" ")[0].toLowerCase()) {
            case "pool":
                round = POOL;
                break;
            case "quart":
                round = QUARTER;
                break;
            case "qfpo":
                round = QUARTER;
                break;
            case "semi":
                round = SEMI;
                break;
            case "3&4":
                round = THIRD_FOURTH;
                break;
            case "final":
                round = FINAL;
                break;
            default:
                throw new RuntimeException("Looks like the site has changed it's content");
        }

        String teams[] = td.get(3).getTextContent().split("v");
        String score[] = td.get(4).getTextContent().split("-");
        String tries[] = td.get(5).getTextContent().split(":");
        String points[] = td.get(6).getTextContent().split("-");
        String venue = td.get(7).getTextContent();

        return new GameResult(round, year, teams[0].trim(), teams[1].trim(),tries[0].trim(), tries[1].trim(),
                Double.valueOf(score[0].trim()), Double.valueOf(score[1].trim()),
                Double.valueOf(points[0].trim()),
                Double.valueOf(points[1].trim()), venue.trim()) {
        };
    }
}
