/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekimini.featureanalysis;

import java.util.logging.Level;
import java.util.logging.Logger;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.WrapperSubsetEval;
import weka.attributeSelection.BestFirst;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 *
 * @author louismccallum
 */

public class WrapperSelector extends FeatureSelector {
    
    public Classifier classifier;
    
    @Override
    public int[] getAttributeIndicesForInstances(Instances instances)
    {
        try {

            AttributeSelection attsel = new AttributeSelection();
            WrapperSubsetEval eval = new WrapperSubsetEval();
            BestFirst search = new BestFirst();
            
            eval.setClassifier(classifier);
            attsel.setEvaluator(eval);
            attsel.setSearch(search);
            int classIndex = instances.numAttributes() - 1;
            instances.setClassIndex(classIndex);
            double start = System.currentTimeMillis();
            System.out.println("starting selection");
            attsel.SelectAttributes(instances);
            double timeTaken = System.currentTimeMillis() - start;
            System.out.println("DONE: " + timeTaken / 1000.0 + "s : ");  
            int [] selected = attsel.selectedAttributes();
            //Remove classIndex if it picked it
            for(int s:selected)
            {
                if(s == classIndex)
                {
                    System.out.println("removing class index");
                    int[] noClass = new int[selected.length-1];
                    System.arraycopy(selected, 0, noClass, 0, noClass.length);
                    return noClass;
                }
            }
            return selected;
            
        } catch (Exception ex) {
            Logger.getLogger(WrapperSelector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new int[0];
    }
    

    
}