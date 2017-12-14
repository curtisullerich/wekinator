/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekimini;

import java.lang.reflect.Method;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import weka.core.Instance;
import weka.core.Instances;
import wekimini.modifiers.BufferedInput;
import wekimini.modifiers.ModifiedInput;
import wekimini.WekinatorSaver;

/**
 *
 * @author louismccallum
 */

public class TestSetTest {
    
    public Wekinator w;
    
    public void setUp() {
        String fileLocation = ("/Users/louismccallum/Documents/Goldsmiths/Wekinator_Projects/WekinatorTestSet/WekinatorTestSet/WekinatorTestSet.wekproj");
        try{
            w = WekinatorSaver.loadWekinatorFromFile(fileLocation);
        } 
        catch (Exception e)
        {
            
        }
    }
    
    @Test
    public void testRecordingData()
    {
        setUp();
        w.getDataManager().deleteTestSet();
        w.getSupervisedLearningManager().setRecordingState(SupervisedLearningManager.RecordingState.RECORDING_TEST);
        double[] inputs = {1,2,3};
        w.getSupervisedLearningManager().updateInputs(inputs);
        Instances testSet = w.getDataManager().getTestInstances();
        assertEquals(1, testSet.numInstances(), 0);
        //3 Metadata + 3 inputs + 3 outputs
        assertEquals(3 + 3 + 3, testSet.numAttributes(), 0);
        Instance in = testSet.firstInstance();
        assertEquals(1, in.value(3), 0);
        assertEquals(2, in.value(4), 0);
        assertEquals(3, in.value(5), 0);
    }
    
    @Test
    public void testAutoSelectingTestData() throws InterruptedException
    {
        String fileLocation = ("/Users/louismccallum/Documents/Goldsmiths/Wekinator_Projects/Circles_2/multimodel/multimodel.wekproj");
        try{
            w = WekinatorSaver.loadWekinatorFromFile(fileLocation);
        } 
        catch (Exception e)
        {
            
        }
        w.getDataManager().deleteTestSet();
        w.getSupervisedLearningManager().setRecordingState(SupervisedLearningManager.RecordingState.RECORDING_TEST);
        for(int i = 0; i < 100; i++)
        {
            double[] inputs = {i + 1, 2, 3, 4, 5, 6};
            w.getSupervisedLearningManager().updateInputs(inputs);
        }
        
        w.getSupervisedLearningManager().setLearningState(SupervisedLearningManager.LearningState.READY_TO_TRAIN);
        w.getSupervisedLearningManager().setRunningState(SupervisedLearningManager.RunningState.NOT_RUNNING);
        w.getSupervisedLearningManager().buildAll();
        
        Thread.sleep(50);
        
        w.getDataManager().selectFeaturesAutomatically(DataManager.AutoSelect.RANDOM, true);
        w.getDataManager().setUseAutomatic(true);
        Instances testSet = w.getDataManager().getTestingDataForOutput(0);
        assertEquals(5, testSet.numAttributes(), 0);
        testSet = w.getDataManager().getTestingDataForOutput(1);
        assertEquals(5, testSet.numAttributes(), 0);
        testSet = w.getDataManager().getTestingDataForOutput(2);
        assertEquals(5, testSet.numAttributes(), 0);
    }
    
    @Test
    public void testModifyingTestData()
    {
        setUp();
        w.getDataManager().deleteTestSet();
        w.getSupervisedLearningManager().setRecordingState(SupervisedLearningManager.RecordingState.RECORDING_TEST);
        for(int i = 0; i < 100; i++)
        {
            double[] inputs = {i + 1, 2, 3};
            w.getSupervisedLearningManager().updateInputs(inputs);
        }
        Instances testSet = w.getDataManager().getTestingDataForOutput(0);
        assertEquals(100, testSet.numInstances(), 0);
        Instance in = testSet.firstInstance();
        assertEquals(1, in.value(0), 0);
       
        int windowSize = 10;
        w.getDataManager().featureManager.removeAllModifiersFromOutput(0);
        w.getDataManager().featureManager.passThroughInputToOutput(false, 0);
        ModifiedInput buffer = new BufferedInput("input-1", 0, windowSize, 0);
        buffer.addRequiredModifierID(0);
        int id = w.getDataManager().featureManager.addModifierToOutput(buffer, 0);
        testSet = w.getDataManager().getTestingDataForOutput(0);
        assertEquals(100, testSet.numInstances(), 0);
        for(int instanceIndex = 0; instanceIndex < 100; instanceIndex++)
        {
            double[] inputs = testSet.instance(instanceIndex).toDoubleArray();
            for(int k = 0; k < windowSize; k++)
            {
                if((k + instanceIndex) < windowSize - 1)
                {
                    assertEquals(0.0, inputs[k], 0.0);
                }
                else
                {
                   assertEquals( k + (instanceIndex - (windowSize - 2)), inputs[k], 0.0); 
                }  
            }
        }
        
        //Remaining two models pass through data
        
        testSet = w.getDataManager().getTestingDataForOutput(1);
        assertEquals(100, testSet.numInstances(), 0);
        for(int instanceIndex = 0; instanceIndex < 100; instanceIndex++)
        {
            double[] inputs = testSet.instance(instanceIndex).toDoubleArray();
            assertEquals(instanceIndex + 1, inputs[0] ,0);
            assertEquals(2, inputs[1] ,0);
            assertEquals(3, inputs[2] ,0);
        }
        
        testSet = w.getDataManager().getTestingDataForOutput(2);
        assertEquals(100, testSet.numInstances(), 0);
        for(int instanceIndex = 0; instanceIndex < 100; instanceIndex++)
        {
            double[] inputs = testSet.instance(instanceIndex).toDoubleArray();
            assertEquals(instanceIndex + 1, inputs[0] ,0);
            assertEquals(2, inputs[1] ,0);
            assertEquals(3, inputs[2] ,0);
        }
    }
    
    public void testLoadedData()
    {
        setUp();
        
        Instances testSet = w.getDataManager().getTestingDataForOutput(0);
        assertEquals(200, testSet.numInstances(), 0);
        Instance in = testSet.firstInstance();
        assertEquals(1, in.value(0), 0);
       
        int windowSize = 10;
        w.getDataManager().featureManager.removeAllModifiersFromOutput(0);
        w.getDataManager().featureManager.passThroughInputToOutput(false, 0);
        ModifiedInput buffer = new BufferedInput("input-1", 0, windowSize, 0);
        buffer.addRequiredModifierID(0);
        int id = w.getDataManager().featureManager.addModifierToOutput(buffer, 0);
        testSet = w.getDataManager().getTestingDataForOutput(0);
        assertEquals(200, testSet.numInstances(), 0);
        for(int instanceIndex = 0; instanceIndex < 200; instanceIndex++)
        {
            double[] inputs = testSet.instance(instanceIndex).toDoubleArray();
            for(int k = 0; k < windowSize; k++)
            {
                if((k + instanceIndex) < windowSize - 1)
                {
                    assertEquals(0.0, inputs[k], 0.0);
                }
                else
                {
                   assertEquals( k + (instanceIndex - (windowSize - 2)), inputs[k], 0.0); 
                }  
            }
        }
    }
    
    @Test
    public void testSavingTestData()
    {
        setUp();
        
        w.getDataManager().deleteTestSet();
        
        w.getSupervisedLearningManager().setRecordingState(SupervisedLearningManager.RecordingState.RECORDING_TEST);
        for(int i = 0; i < 200; i++)
        {
            double[] inputs = {i + 1, 2, 3};
            w.getSupervisedLearningManager().updateInputs(inputs);
        }
        
        w.save();
        
        testLoadedData();
    }
    
    @Test
    public void testLoadingTestData()
    {
        testLoadedData();
    }
    
}