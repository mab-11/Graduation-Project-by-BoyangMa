package com.pxw.service;

/**
 * Experiment service interface
 */
public interface ExperimentService {

    /**
     * Run comparison experiment
     * @param experimentName Experiment name
     * @return Comparison result JSON
     */
    String runComparison(String experimentName);

    /**
     * Get convergence curve data
     * @return Generation data JSON
     */
    String getConvergenceData();

    /**
     * Get experiment history
     * @return History record JSON
     */
    String getHistory();

    /**
     * Clear history data
     * @return Status JSON
     */
    String clearHistory();

    /**
     * Multi-scale test
     * @return Test result JSON
     */
    String runMultiScaleTest();

    /**
     * Parameter sensitivity test
     * @return Test result JSON
     */
    String runParameterTest();
}
