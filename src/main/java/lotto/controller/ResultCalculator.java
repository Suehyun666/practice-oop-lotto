package lotto.controller;

import lotto.domain.Lotto;
import lotto.domain.MatchResult;
import lotto.utils.ResultConstants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lotto.utils.ResultConstants.*;

public class ResultCalculator {

    public Map<ResultConstants, Integer> calculateResults(List<Lotto> boughtLottos, List<Integer> winningNumbers, int bonusNumber) {
        Map<ResultConstants, Integer> resultMap = new HashMap<>();
        for (Lotto lotto : boughtLottos) {
            MatchResult result = getMatchResult(lotto.getNumbers(), winningNumbers, bonusNumber);
            ResultConstants rank = ResultConstants.valueOf(result.getMatchCount(), result.isBonusMatch());
            resultMap.put(rank, resultMap.getOrDefault(rank, ZERO) + ONE);
        }
        return resultMap;
    }

    public double calculateProfitRate(Map<ResultConstants, Integer> resultMap, int totalCost) {
        int totalPrize = resultMap.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getPrize() * entry.getValue())
                .sum();
        return (double) totalPrize / totalCost * PERCENTAGE_CONVERTER;
    }

    private MatchResult getMatchResult(List<Integer> numbers, List<Integer> winningNumbers, int bonusNumber) {
        int matchCount = getMatchCount(numbers, winningNumbers);
        boolean bonusMatch = isBonusMatch(numbers, bonusNumber);
        return new MatchResult(matchCount, bonusMatch);
    }

    private int getMatchCount(List<Integer> numbers, List<Integer> winningNumbers) {
        return (int) numbers.stream().filter(winningNumbers::contains).count();
    }

    private boolean isBonusMatch(List<Integer> numbers, int bonusNumber) {
        return numbers.contains(bonusNumber);
    }
}
