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
package org.ta4j.core.rules.custom;

import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.AbstractRule;

/**
 * A stop-gain rule.
 *
 * <p>
 * Satisfied when the close price reaches the gain threshold.
 */
public class PredefinedTargetHitRule extends AbstractRule {

    /** The close price indicator. */
    private final ClosePriceIndicator closePrice;

    /**
     * Constructor.
     *
     * @param closePrice     the close price indicator
     * @param gainPercentage the gain percentage
     */
    public PredefinedTargetHitRule(ClosePriceIndicator closePrice) {
		this.closePrice = closePrice;
    }

    /** This rule uses the {@code tradingRecord}. */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        boolean satisfied = false;
        // No trading history or no position opened, no loss
        if (tradingRecord != null) {
            Position currentPosition = tradingRecord.getCurrentPosition();
            if (currentPosition.isOpened() && !currentPosition.isFirstTargetHit()) {

                Num targetLevel = currentPosition.getPredefinedFirstTargetLevel();// .getEntry().getNetPrice();
                Num currentPrice = closePrice.getValue(index);

                if (currentPosition.getEntry().isBuy()) {
                    satisfied = isBuyGainSatisfied(targetLevel, currentPrice);
                } else {
                    satisfied = isSellGainSatisfied(targetLevel, currentPrice);
                }
            }
        }
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }

    private boolean isBuyGainSatisfied(Num targetLevel, Num currentPrice) {
//        Num lossRatioThreshold = HUNDRED.plus(gainPercentage).dividedBy(HUNDRED);
//        Num threshold = entryPrice.multipliedBy(lossRatioThreshold);
        return currentPrice.isGreaterThanOrEqual(targetLevel);
    }

    private boolean isSellGainSatisfied(Num targetLevel, Num currentPrice) {
//        Num lossRatioThreshold = HUNDRED.minus(gainPercentage).dividedBy(HUNDRED);
//        Num threshold = entryPrice.multipliedBy(lossRatioThreshold);
        return currentPrice.isLessThanOrEqual(targetLevel);
    }
}
