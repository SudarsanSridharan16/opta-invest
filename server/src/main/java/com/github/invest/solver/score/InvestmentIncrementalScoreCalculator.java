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

package com.github.invest.solver.score;

import com.github.invest.domain.AssetClassAllocation;
import com.github.invest.domain.InvestmentSolution;
import com.github.invest.domain.Region;
import com.github.invest.domain.Sector;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.impl.score.director.incremental.AbstractIncrementalScoreCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvestmentIncrementalScoreCalculator extends AbstractIncrementalScoreCalculator<InvestmentSolution> {

    private InvestmentSolution solution;

    private long squaredStandardDeviationFemtosMaximum;
    private long squaredStandardDeviationFemtos;

    private Map<Region, Long> regionQuantityTotalMap;
    private Map<Sector, Long> sectorQuantityTotalMap;

    private long hardScore;
    private long softScore;

    // ************************************************************************
    // Lifecycle methods
    // ************************************************************************

    @Override
    public void resetWorkingSolution(InvestmentSolution solution) {
        this.solution = solution;
        squaredStandardDeviationFemtosMaximum = solution.getParametrization()
                .calculateSquaredStandardDeviationFemtosMaximum();
        squaredStandardDeviationFemtos = 0L;
        List<Region> regionList = solution.getRegionList();
        regionQuantityTotalMap = new HashMap<>();
        for (Region region : regionList) {
            regionQuantityTotalMap.put(region, 0L);
        }
        List<Sector> sectorList = solution.getSectorList();
        sectorQuantityTotalMap = new HashMap<>(sectorList.size());
        for (Sector sector : sectorList) {
            sectorQuantityTotalMap.put(sector, 0L);
        }
        hardScore = 0L;
        softScore = 0L;
        for (AssetClassAllocation allocation : solution.getAssetClassAllocationList()) {
            insertQuantityMillis(allocation, true);
        }
    }

    @Override
    public void beforeEntityAdded(Object entity) {
        // Do nothing
    }

    @Override
    public void afterEntityAdded(Object entity) {
        insertQuantityMillis((AssetClassAllocation) entity, false);
    }

    @Override
    public void beforeVariableChanged(Object entity, String variableName) {
        retractQuantityMillis((AssetClassAllocation) entity);
    }

    @Override
    public void afterVariableChanged(Object entity, String variableName) {
        insertQuantityMillis((AssetClassAllocation) entity, false);
    }

    @Override
    public void beforeEntityRemoved(Object entity) {
        retractQuantityMillis((AssetClassAllocation) entity);
    }

    @Override
    public void afterEntityRemoved(Object entity) {
        // Do nothing
    }

    // ************************************************************************
    // Modify methods
    // ************************************************************************

    private void insertQuantityMillis(AssetClassAllocation allocation, boolean reset) {
        // Standard deviation maximum
        if (squaredStandardDeviationFemtos > squaredStandardDeviationFemtosMaximum) {
            hardScore += squaredStandardDeviationFemtos - squaredStandardDeviationFemtosMaximum;
        }
        squaredStandardDeviationFemtos += calculateStandardDeviationSquaredFemtosDelta(allocation, reset);
        if (squaredStandardDeviationFemtos > squaredStandardDeviationFemtosMaximum) {
            hardScore -= squaredStandardDeviationFemtos - squaredStandardDeviationFemtosMaximum;
        }
        Long quantityMillis = allocation.getQuantityMillis();
        if (quantityMillis != null) {
            // Region quantity maximum
            Region region = allocation.getRegion();
            long regionQuantityMaximum = region.getQuantityMillisMaximum();
            long oldRegionQuantity = regionQuantityTotalMap.get(region);
            long oldRegionAvailable = regionQuantityMaximum - oldRegionQuantity;
            long newRegionQuantity = oldRegionQuantity + quantityMillis;
            long newRegionAvailable = regionQuantityMaximum - newRegionQuantity;
            hardScore += Math.min(newRegionAvailable, 0L) - Math.min(oldRegionAvailable, 0L);
            regionQuantityTotalMap.put(region, newRegionQuantity);
            // Sector quantity maximum
            Sector sector = allocation.getSector();
            long sectorQuantityMaximum = sector.getQuantityMillisMaximum();
            long oldSectorQuantity = sectorQuantityTotalMap.get(sector);
            long oldSectorAvailable = sectorQuantityMaximum - oldSectorQuantity;
            long newSectorQuantity = oldSectorQuantity + quantityMillis;
            long newSectorAvailable = sectorQuantityMaximum - newSectorQuantity;
            hardScore += Math.min(newSectorAvailable, 0L) - Math.min(oldSectorAvailable, 0L);
            sectorQuantityTotalMap.put(sector, newSectorQuantity);
        }
        // Maximize expected return
        softScore += allocation.getQuantifiedExpectedReturnMicros();
    }

    private void retractQuantityMillis(AssetClassAllocation allocation) {
        // Standard deviation maximum
        if (squaredStandardDeviationFemtos > squaredStandardDeviationFemtosMaximum) {
            hardScore += squaredStandardDeviationFemtos - squaredStandardDeviationFemtosMaximum;
        }
        squaredStandardDeviationFemtos -= calculateStandardDeviationSquaredFemtosDelta(allocation, false);
        if (squaredStandardDeviationFemtos > squaredStandardDeviationFemtosMaximum) {
            hardScore -= squaredStandardDeviationFemtos - squaredStandardDeviationFemtosMaximum;
        }
        Long quantityMillis = allocation.getQuantityMillis();
        if (quantityMillis != null) {
            // Region quantity maximum
            Region region = allocation.getRegion();
            long regionQuantityMaximum = region.getQuantityMillisMaximum();
            long oldRegionQuantity = regionQuantityTotalMap.get(region);
            long oldRegionAvailable = regionQuantityMaximum - oldRegionQuantity;
            long newRegionQuantity = oldRegionQuantity - quantityMillis;
            long newRegionAvailable = regionQuantityMaximum - newRegionQuantity;
            hardScore += Math.min(newRegionAvailable, 0L) - Math.min(oldRegionAvailable, 0L);
            regionQuantityTotalMap.put(region, newRegionQuantity);
            // Sector quantity maximum
            Sector sector = allocation.getSector();
            long sectorQuantityMaximum = sector.getQuantityMillisMaximum();
            long oldSectorQuantity = sectorQuantityTotalMap.get(sector);
            long oldSectorAvailable = sectorQuantityMaximum - oldSectorQuantity;
            long newSectorQuantity = oldSectorQuantity - quantityMillis;
            long newSectorAvailable = sectorQuantityMaximum - newSectorQuantity;
            hardScore += Math.min(newSectorAvailable, 0L) - Math.min(oldSectorAvailable, 0L);
            sectorQuantityTotalMap.put(sector, newSectorQuantity);
        }
        // Maximize expected return
        softScore -= allocation.getQuantifiedExpectedReturnMicros();
    }

    private long calculateStandardDeviationSquaredFemtosDelta(AssetClassAllocation allocation, boolean reset) {
        long squaredFemtos = 0L;
        for (AssetClassAllocation other : solution.getAssetClassAllocationList()) {
            if (allocation == other) {
                long micros = allocation.getQuantifiedStandardDeviationRiskMicros();
                squaredFemtos += micros * micros * 1000L;
            } else {
                long picos = allocation.getQuantifiedStandardDeviationRiskMicros() * other.getQuantifiedStandardDeviationRiskMicros();
                squaredFemtos += picos * allocation.getAssetClass().getCorrelationMillisMap().get(other.getAssetClass());
                // TODO FIXME the reset hack only works if there are no moves that mix multiple before/after notifications
                if (!reset) {
                    squaredFemtos += picos * other.getAssetClass().getCorrelationMillisMap().get(allocation.getAssetClass());
                }
            }
        }
        return squaredFemtos;
    }

    @Override
    public Score calculateScore() {
        return HardSoftLongScore.of(hardScore, softScore);
    }

}
