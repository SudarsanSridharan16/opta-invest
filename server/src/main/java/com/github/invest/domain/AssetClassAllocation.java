/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.invest.domain;

import com.github.invest.domain.util.InvestmentNumericUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@Data
@PlanningEntity()
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class AssetClassAllocation {

    public static long calculateSquaredStandardDeviationFemtosFromTo(AssetClassAllocation from, AssetClassAllocation to) {
        if (from == to) {
            long micros = from.getQuantifiedStandardDeviationRiskMicros();
            return micros * micros * 1000L;
        } else {
            long picos = from.getQuantifiedStandardDeviationRiskMicros() * to.getQuantifiedStandardDeviationRiskMicros();
            long correlationMillis = from.getAssetClass().getCorrelationMillisMap().get(to.getAssetClass());
            return picos * correlationMillis;
        }
    }

    @PlanningId
    private Long id;

    private AssetClass assetClass;

    // Planning variables: changes during planning, between score calculations.
    @PlanningVariable(valueRangeProviderRefs = {"quantityMillisRange"})
    private Long quantityMillis; // In millis (so multiplied by 1000)

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    public Region getRegion() {
        return assetClass.getRegion();
    }

    public Sector getSector() {
        return assetClass.getSector();
    }

    public long getQuantifiedExpectedReturnMicros() {
        if (quantityMillis == null) {
            return 0L;
        }
        return quantityMillis * assetClass.getExpectedReturnMillis();
    }

    public long getQuantifiedStandardDeviationRiskMicros() {
        if (quantityMillis == null) {
            return 0L;
        }
        return quantityMillis * assetClass.getStandardDeviationRiskMillis();
    }

    public String getQuantityLabel() {
        if (quantityMillis == null) {
            return "";
        }
        return InvestmentNumericUtil.formatMillisAsPercentage(quantityMillis);
    }

    public String getLabel() {
        return assetClass.getName();
    }

    @Override
    public String toString() {
        return assetClass.toString();
    }

}
