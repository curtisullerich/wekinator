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
import weka.attributeSelection.GreedyStepwise;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 *
 * @author louismccallum
 */
public class WrapperSelector implements FeatureSelector {
    
    public Classifier classifier;
    
    @Override
    public int[] getFeaturesForInstances(Instances instances)
    {
        try {
            AttributeSelection attsel = new AttributeSelection();
            WrapperSubsetEval eval = new WrapperSubsetEval();
            GreedyStepwise search = new GreedyStepwise();
            eval.setClassifier(classifier);
            search.setSearchBackwards(true);
            attsel.setEvaluator(eval);
            attsel.setSearch(search);
            int classIndex = instances.classAttribute().index();
            int [] toRemove = {classIndex};
            Remove remove = new Remove();   
            remove.setAttributeIndicesArray(toRemove);
            remove.setInputFormat(instances);                          
            Instances newData = Filter.useFilter(instances, remove);   
            attsel.SelectAttributes(newData);
            int[] indices = attsel.selectedAttributes();
            return indices;
        } catch (Exception ex) {
            Logger.getLogger(CFSelector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new int[0];
    }
    
    public Instances getFilteredInstances(Instances instances)
    {
        try
        {
            int classIndex = instances.classAttribute().index();
            int [] toRemove = {classIndex};
            Remove removeClass = new Remove();   
            removeClass.setAttributeIndicesArray(toRemove);
            removeClass.setInputFormat(instances);                          
            Instances noClass = Filter.useFilter(instances, removeClass);  

            AttributeSelection attsel = new AttributeSelection();
            WrapperSubsetEval eval = new WrapperSubsetEval();
            GreedyStepwise search = new GreedyStepwise();
            eval.setClassifier(classifier);
            search.setSearchBackwards(true);
            attsel.setEvaluator(eval);
            attsel.setSearch(search);
            attsel.SelectAttributes(noClass);
            int[] indices = attsel.selectedAttributes();
            int[] withClassIndex = new int[indices.length+1];
            System.arraycopy(indices, 0, withClassIndex, 0, indices.length);
            System.arraycopy(toRemove, 0, withClassIndex, indices.length, toRemove.length);
           
            Remove keep = new Remove();
            keep.setInvertSelection(true);
            keep.setAttributeIndicesArray(withClassIndex);
            keep.setInputFormat(instances);  
            Instances withClass = Filter.useFilter(instances, keep); 
            return withClass;  
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(CFSelector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return instances;
    }
    
}
