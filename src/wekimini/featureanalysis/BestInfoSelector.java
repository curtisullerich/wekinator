/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekimini.featureanalysis;

import java.util.logging.Level;
import java.util.logging.Logger;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.NumericToNominal;

/**
 *
 * @author louismccallum
 */
public class BestInfoSelector extends RankedFeatureSelector {
    
    public Classifier classifier;
    
    @Override
    public int[] getAttributeIndicesForInstances(Instances instances)
    {
        try {
            Discretize dis = new Discretize();
            dis.setInputFormat(instances);     
            Instances discreted = Filter.useFilter(instances, dis); 
            NumericToNominal nom = new NumericToNominal();
            nom.setInputFormat(discreted);   
            discreted = Filter.useFilter(discreted, nom);
            
            AttributeSelection attsel = new AttributeSelection();
            InfoGainAttributeEval eval = new InfoGainAttributeEval();
            Ranker search = new Ranker();
            search.setOptions(new String[]{"-T","0.05"});
            attsel.setEvaluator(eval);
            attsel.setSearch(search);
            int classIndex = discreted.numAttributes() - 1;
            discreted.setClassIndex(classIndex);
            
            //System.out.println("starting selection");
            attsel.SelectAttributes(discreted);
            //System.out.println("DONE");  
            
            //Return best results from ranked array
            int[] ranked =  attsel.selectedAttributes();
            
            if(useThreshold)
            {
                featuresToPick = (int)(((double)ranked.length)*threshold);
            }
            int[] thresholded = new int[featuresToPick];
            System.arraycopy(ranked, 0, thresholded, 0, featuresToPick);
            for(int s:thresholded)
            {
                if(s == classIndex)
                {
                    System.out.println("removing class index");
                    int[] noClass = new int[thresholded.length-1];
                    System.arraycopy(thresholded, 0, noClass, 0, noClass.length);
                    return noClass;
                }
            }
            return thresholded;
            
        } catch (Exception ex) {
            Logger.getLogger(InfoGainSelector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new int[0];
    }
    
}
