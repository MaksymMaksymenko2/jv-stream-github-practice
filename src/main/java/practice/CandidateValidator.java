package practice;

import java.util.Arrays;
import java.util.stream.Stream;
import java.util.function.Predicate;
import model.Candidate;

public class CandidateValidator implements Predicate<Candidate> {
    private static final int REQUIRED_AGE = 35;
    private static final String REQUIRED_NATIONALITY = "Ukrainian";
    private static final int REQUIRED_YEARS_IN_UKR = 10;

    @Override
    public boolean test(Candidate candidate) {
        return Stream.of(candidate)
                .filter(currentCandidate -> currentCandidate != null)
                .filter(currentCandidate -> currentCandidate.getAge() >= REQUIRED_AGE)
                .filter(currentCandidate -> REQUIRED_NATIONALITY.equals(currentCandidate.getNationality()))
                .filter(Candidate::isAllowedToVote)
                .filter(currentCandidate -> Arrays.stream(currentCandidate.getPeriodsInUkr().split(","))
                        .map(String::trim)
                        .filter(period -> !period.isEmpty())
                        .mapToInt(period -> {
                            String[] years = period.split("-");
                            if (years.length != 2) return 0;
                            try {
                                int startYear = Integer.parseInt(years[0].trim());
                                int endYear = Integer.parseInt(years[1].trim());
                                return Math.max(0, endYear - startYear);
                            } catch (NumberFormatException e) {
                                return 0;
                            }
                        })
                        .sum() >= REQUIRED_YEARS_IN_UKR)
                .findAny()
                .isPresent();
    }
}



