/*
 * Copyright (c) 2014-2020 Aurélien Broszniowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.rainfall;

import io.rainfall.configuration.ConcurrencyConfig;
import io.rainfall.generator.ByteArrayGenerator;
import io.rainfall.statistics.StatisticsHolder;
import io.rainfall.store.client.StoreClientService;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.rainfall.configuration.ReportingConfig.report;
import static io.rainfall.configuration.ReportingConfig.text;
import static io.rainfall.execution.Executions.during;
import static io.rainfall.execution.Executions.times;
import static io.rainfall.store.client.StoreClientServiceFactory.defaultService;
import static io.rainfall.store.core.TestRun.Status.COMPLETE;
import static io.rainfall.store.core.TestRun.Status.FAILED;
import static io.rainfall.unit.TimeDivision.seconds;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @author Aurelien Broszniowski
 */

public class RainfallStoreTest {

  @Test
  public void testRecord() throws SyntaxException {


    ObjectGenerator<byte[]> valueGenerator = ByteArrayGenerator.fixedLengthByteArray(1000);

    System.out.println("----------> Test phase");
    StoreClientService perfStoreClientService = defaultService("http://localhost:4567/performance");
    long runId = perfStoreClientService.addRun("example1", getClass().getCanonicalName(), "1.0");

    Random rnd = new Random();

    try {
      Runner.setUp(
          Scenario.scenario("Test phase")
              .exec(new Operation() {
                      @Override
                      public void exec(StatisticsHolder statisticsHolder, Map<Class<? extends Configuration>, Configuration> configurations, List<AssertionEvaluator> assertions) throws TestException {
                        long start = statisticsHolder.getTimeInNs();

                        long end = statisticsHolder.getTimeInNs();

                        statisticsHolder.record("operation", (end - start), TestResult.ONE);
                      }

                      @Override
                      public List<String> getDescription() {
                        return Arrays.asList("Operation execution");
                      }
                    }
              )
      )
          .warmup(during(30, seconds))
          .executed(times(10000))
          .config(ConcurrencyConfig.concurrencyConfig()
              .threads(4).timeout(60, MINUTES), report(TestResult.class)
              .log(text())

          )
          .start();

      perfStoreClientService.setStatus(runId, COMPLETE);

    } catch (Exception e) {
      perfStoreClientService.setStatus(runId, FAILED);
      throw e;
    }

  }

  public static enum TestResult {
    ONE, TWO
  }
}
