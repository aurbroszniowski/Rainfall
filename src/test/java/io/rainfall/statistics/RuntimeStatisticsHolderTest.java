/*
 * Copyright (c) 2014-2019 Aurélien Broszniowski
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

package io.rainfall.statistics;

import io.rainfall.statistics.collector.StatisticsCollector;
import org.junit.Ignore;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static io.rainfall.statistics.RuntimeStatisticsHolderTest.Results.ONE;

/**
 * @author Aurelien Broszniowski
 */

public class RuntimeStatisticsHolderTest {

  @Test
  @Ignore
  public void
  launchBenchmark() throws Exception {

    Options opt = new OptionsBuilder()
        // Specify which benchmarks to run.
        // You can be more specific if you'd like to run only one benchmark per test.
        .include(this.getClass().getName() + ".*")
        // Set the following options as needed
        .mode(Mode.AverageTime)
        .timeUnit(TimeUnit.MICROSECONDS)
        .warmupTime(TimeValue.seconds(10))
        .warmupIterations(10)
        .measurementTime(TimeValue.seconds(10))
        .measurementIterations(10)
        .threads(8)
        .forks(1)
        .shouldFailOnError(true)
        .shouldDoGC(true)
        //.jvmArgs("-XX:+UnlockDiagnosticVMOptions", "-XX:+PrintInlining")
        //.addProfiler(WinPerfAsmProfiler.class)
        .build();

    new Runner(opt).run();
  }

  @State(Scope.Thread)
  public static class BenchmarkState {
    RuntimeStatisticsHolder runtimeStatisticsHolder;

    @Setup(Level.Trial)
    public void
    initialize() {

      runtimeStatisticsHolder = new RuntimeStatisticsHolder(Results.values(), Results.values(), new HashSet<StatisticsCollector>());
//      runtimeStatisticsHolder.addStatistics("ONE", new Statistics("ONE", Results.values()));
    }
  }

  @Benchmark
  public void benchmark1(BenchmarkState state, Blackhole bh) {

    RuntimeStatisticsHolder runtimeStatisticsHolder = state.runtimeStatisticsHolder;

    for (int i = 0; i < 10000; i++) {
      runtimeStatisticsHolder.record("ONE", i, ONE);
    }
  }

  public static enum Results {
    ONE, TWO, THREE
  }
}
