package io.rainfall;


import io.rainfall.configuration.ConcurrencyConfig;
import io.rainfall.configuration.ReportingConfig;
import io.rainfall.statistics.StatisticsHolder;
import org.HdrHistogram.Histogram;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.rainfall.configuration.ReportingConfig.html;
import static io.rainfall.configuration.ReportingConfig.text;
import static io.rainfall.execution.Executions.during;
import static io.rainfall.unit.TimeDivision.seconds;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @author Aurelien Broszniowski
 */
public class CoordinatedOmissionTest {

  @Test
  public void testHdrHistogram() {
    Histogram histogram = new Histogram(110000L, 3);
    for (int i = 0; i < 100000; i++) {      // record 1 ms
      histogram.recordValue(1L);
    }
    histogram.recordValue(100000L);  // record 100 sec
    Histogram correctedHisto = histogram.copyCorrectedForCoordinatedOmission(1);
    histogram.outputPercentileDistribution(System.out, 5, 1d, false);
    correctedHisto.outputPercentileDistribution(System.out, 5, 1d, false);
  }

  @Test
  public void testRainfall() throws SyntaxException {
    ConcurrencyConfig concurrency = ConcurrencyConfig.concurrencyConfig()
        .threads(4).timeout(50, MINUTES);

    DummyServer dummyServer = new DummyServer();
    dummyServer.start();

    Runner.setUp(
        Scenario.scenario("Warm up phase")
            .exec(
                new DummyOperation(dummyServer)
            ))
        .executed(during(110, seconds))
        .config(concurrency, ReportingConfig.report(DummyResult.class).log(text(), html()))
        .start();
    dummyServer.interrupt();
  }

  public static class DummyOperation extends Operation {
    private DummyServer dummyServer;

    public DummyOperation(final DummyServer dummyServer) {
      this.dummyServer = dummyServer;
    }

    @Override
    public void exec(final StatisticsHolder statisticsHolder, final Map<Class<? extends Configuration>, Configuration> configurations, final List<AssertionEvaluator> assertions) throws TestException {
      long start = getTimeInNs();
      dummyServer.dummyCall();
      long end = getTimeInNs();
      statisticsHolder.record("Test", (end - start), DummyResult.OK);
    }

    @Override
    public List<String> getDescription() {
      return Arrays.asList("dummy operation");
    }
  }

  public enum DummyResult {
    OK
  }

  public static class DummyServer extends Thread {

    private int responseTime;

    public void dummyCall() {
      try {
        if (this.responseTime > 1)
          System.out.println("Call received");
        Thread.sleep(this.responseTime);
        if (this.responseTime > 1)
          System.out.println("Call resp");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

    @Override
    public void run() {
      this.responseTime = 1;      // It will give a resp time of 1 ms during 100 sec
      try {
        Thread.sleep(50 * 1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      System.out.println("----------------- now the server is stalled for 100 sec -----------");

      this.responseTime = 50 * 1000;      // It will give a resp time of 100 s during 100 sec
      try {
        Thread.sleep(50 * 1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      this.responseTime = 1;      // It will give a resp time of 1 ms during 100 sec
      try {
        Thread.sleep(50 * 1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
