package uk.ac.soton.app.model.ProcessedData;

import uk.ac.soton.app.model.CSVDataUnits.Click;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to calculate the data needed to build a histogram of click costs
 */
public class HistogramData {

    //List of each cost range's upper bound
    private final List<Double> costDistribution;
    //List of each cost range's frequency
    private final List<Integer> frequencyDistribution;

    /**
     * Creates an object containing the upper bounds and frequencies of cost ranges
     * @param clicks list of clicks whose costs we are dividing into ranges
     * @param bars number of cost ranges
     */
    public HistogramData(List<Click> clicks, int bars) {
        //Calculates and stores each cost range's upper bound
        costDistribution = new ArrayList<>();
        calculateCostDistribution(clicks, bars);

        //Calculates and stores each cost range's frequency
        frequencyDistribution = new ArrayList<>();
        calculateFrequencyDistribution(clicks);
    }

    /**
     * Calculates and stores each cost range's upper bound
     * @param clicks list of clicks whose costs we are dividing into ranges
     * @param bars number of cost ranges
     */
    private void calculateCostDistribution(List<Click> clicks, int bars) {
        //Gets the highest click cost
        double maxCost = 0;
        for (Click click : clicks) {if (click.getVisible() && click.getCost() > maxCost) maxCost = click.getCost();}

        //Calculates the length of each cost range
        //If the highest click cost is 0, sets the length of each cost range to 0.01
        double rangeLength;
        if (maxCost == 0) {rangeLength = 0.01;}
        else {rangeLength = Math.ceil(((maxCost / 100.0) / (double) bars) * 100.0) / 100.0;}

        //Stores each cost range's upper bound
        for (int i = 0; i < bars; i++) {this.costDistribution.add(rangeLength + (i * rangeLength));}

    }

    /**
     * Calculates and stores each cost range's frequency
     * @param clicks list of clicks whose costs we are dividing into ranges
     */
    private void calculateFrequencyDistribution(List<Click> clicks) {
        //Stores each cost range's start frequency
        for (Double ignored : costDistribution) {this.frequencyDistribution.add(0);}

        //Loops through each click
        //Increments a cost range's frequency if the click's cost is within that cost range
        for (Click click : clicks) {
            if (click.getVisible()) {
                if (0 == (click.getCost() / 100.0)) {this.frequencyDistribution.set(0, this.frequencyDistribution.get(0) + 1);}
                else {
                    double previousBound = 0;
                    for (Double upperBound : this.costDistribution) {
                        if ((previousBound < (click.getCost() / 100.0)) && ((click.getCost() / 100.0) <= upperBound)) {
                            int index = this.costDistribution.indexOf(upperBound);
                            this.frequencyDistribution.set(index, this.frequencyDistribution.get(index) + 1);
                        }
                        previousBound = upperBound;
                    }
                }
            }
        }
    }

    /**
     * Gets the list of each cost range's upper bound
     * @return list of upper bounds
     */
    public List<Double> getCostDistribution() {
        return this.costDistribution;
    }

    /**
     * Gets the list of each cost range's frequency
     * @return list of frequencies
     */
    public List<Integer> getFrequencyDistribution() {
        return this.frequencyDistribution;
    }




}
