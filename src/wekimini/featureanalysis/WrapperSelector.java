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

            int classIndex = instances.classAttribute().index();
            int [] toRemove = {classIndex};
            
            Remove removeClass = new Remove();   
            removeClass.setAttributeIndicesArray(toRemove);
            removeClass.setInputFormat(instances);                          
            Instances noClass = Filter.useFilter(instances, removeClass);  

            AttributeSelection attsel = new AttributeSelection();
            WrapperSubsetEval eval = new WrapperSubsetEval();
            BestFirst search = new BestFirst();
            
            eval.setClassifier(classifier);
            attsel.setEvaluator(eval);
            attsel.setSearch(search);
            instances.setClassIndex(instances.numAttributes() - 1);
            
            System.out.println("starting selection");
            attsel.SelectAttributes(noClass);
            System.out.println("DONE");  
            
            return attsel.selectedAttributes();
            
        } catch (Exception ex) {
            Logger.getLogger(WrapperSelector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new int[0];
    }
    

    
}
