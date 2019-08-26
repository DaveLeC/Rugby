package rocks.lecomte.rugby.impl;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.Assert;
import org.junit.Test;
import rocks.lecomte.rugby.domain.GameResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PickAndGoResultObtainerTest {
    @Test
    public void getWRC2015() {
        PickAndGoResultObtainer pagro = new PickAndGoResultObtainer();

        List<GameResult> results = pagro.findByCompetition("WRC", "2015");
        Assert.assertEquals(48, results.size());
    }

    @Test
    public void getAllWRC() {
        PickAndGoResultObtainer pagro = new PickAndGoResultObtainer();
        List<GameResult> results = pagro.findByCompetition("WRC", "2015");
        results.addAll(
                pagro.findByCompetition("WRC", "2011"));
        results.addAll(
                pagro.findByCompetition("WRC", "2007"));
        results.addAll(
                pagro.findByCompetition("WRC", "2003"));
        results.addAll(
                pagro.findByCompetition("WRC", "1999"));
        results.addAll(
                pagro.findByCompetition("WRC", "1995"));
        results.addAll(
                pagro.findByCompetition("WRC", "1991"));
        results.addAll(
                pagro.findByCompetition("WRC", "1987"));

        Collections.sort(results);
        List<GameResult> all = pagro.findByCompetitionAndYears("WRC", "1987", "1991", "1995", "1999",
                "2003", "2007", "2011", "2015");

        Collections.sort(all);

        Assert.assertEquals(results, all);

    }


}
