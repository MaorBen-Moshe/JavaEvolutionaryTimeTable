package selection;

import models.TimeTable;
import utils.RandomUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

public class RouletteWheelSelection implements Selection<TimeTable>, Serializable {
    private static class TimeTableFit{
        private final double fitness;
        private final TimeTable solution;

        private TimeTableFit(TimeTable solution, double fitness){
            this.fitness = fitness;
            this.solution = solution;
        }

        private double getFitness() {
            return fitness;
        }

        private TimeTable getSolution() {
            return solution;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TimeTableFit that = (TimeTableFit) o;
            return Double.compare(that.fitness, fitness) == 0 && solution.equals(that.solution);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fitness, solution);
        }
    }

    @Override
    public List<TimeTable> select(Map<TimeTable, Double> population) {
        List<TimeTableFit> solutions = new ArrayList<>();
        population.forEach((key, val) -> solutions.add(new TimeTableFit(key, val)));
        double[] fitnessArr = new double[solutions.size()];
        fitnessArr[0] = solutions.get(0).getFitness();
        IntStream.range(1, solutions.size()).forEach(i -> fitnessArr[i] = fitnessArr[i - 1] + solutions.get(i).getFitness());

        List<TimeTable> selected = new ArrayList<>(population.size());
        IntStream.range(0, solutions.size()).forEach(i -> {
            double randomNumber = RandomUtils.nextDouble() * fitnessArr[fitnessArr.length - 1];
            int index = Arrays.binarySearch(fitnessArr, randomNumber);
            selected.add(solutions.get(index).getSolution());
        });

        return selected;
    }
}