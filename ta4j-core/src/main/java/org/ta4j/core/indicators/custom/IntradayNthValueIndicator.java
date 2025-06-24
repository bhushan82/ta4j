/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2023 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.indicators.custom;

import org.ta4j.core.num.NaN;

import java.util.stream.Collectors;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

/**
 * Returns the (n-th) previous value of an indicator.
 *
 * If the (n-th) previous index is below the first index from the bar series,
 * then {@link NaN#NaN} is returned.
 */
public class IntradayNthValueIndicator extends CachedIndicator<Num> {

    private final int n;
    private final Indicator<Num> indicator;
    private final int unstableBars;

    /**
     * Constructor.
     *
     * @param indicator the indicator from which to calculate the previous value
     */
    public IntradayNthValueIndicator(Indicator<Num> indicator) {
        this(indicator, 0, 1);
    }

    /**
     * Constructor.
     *
     * @param indicator the indicator from which to calculate the previous value
     * @param n         parameter defines the previous n-th value
     */
    public IntradayNthValueIndicator(Indicator<Num> indicator, int n, int unstableBars) {
        super(indicator);
        if (n < 0) {
            throw new IllegalArgumentException("n must be greater than or equal to 0, but was: " + n);
        }
//        if (unstableBars  n) {
//            throw new IllegalArgumentException("n must be greater than unstableBars ("+unstableBars+"), but was not " + n);
//        }
        this.n = n;
        this.indicator = indicator;
        this.unstableBars = unstableBars;
    }

    @Override
    protected Num calculate(int index) {
    	if (this.indicator.stream().count() < this.getUnstableBars() ) {
    		return NaN.NaN;
    	}
    	if ((index <= this.getUnstableBars()) || (index < n)) {
            return NaN.NaN;
        }
        return indicator.getValue(n);
    }

    /** @return {@link #n} */
    @Override
    public int getUnstableBars() {
        return unstableBars;
    }

    @Override
    public String toString() {
        final String nInfo = n == 1 ? "" : "(" + n + ")";
        return getClass().getSimpleName() + nInfo + "[" + this.indicator + "]";
    }
}
