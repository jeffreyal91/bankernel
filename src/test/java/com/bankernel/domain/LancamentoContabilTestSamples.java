package com.bankernel.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class LancamentoContabilTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LancamentoContabil getLancamentoContabilSample1() {
        return new LancamentoContabil().id(1L);
    }

    public static LancamentoContabil getLancamentoContabilSample2() {
        return new LancamentoContabil().id(2L);
    }

    public static LancamentoContabil getLancamentoContabilRandomSampleGenerator() {
        return new LancamentoContabil().id(longCount.incrementAndGet());
    }
}
