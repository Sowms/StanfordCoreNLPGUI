/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefChain.CorefMention;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author sowmya
 */
public class CoreNLPSuite {
    
    public static String parse(String input) {
        String ans = "";
        Annotation document = new Annotation(input);
	Parsers.pipeline.annotate(document);
	List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            Tree tree = sentence.get(TreeAnnotation.class);
            String parseExpr = tree.toString();
            ans = ans + parseExpr + "\n";
        }
        return ans;
    }
    public static String dependencyParse(String input) {
        String ans = "";
        Annotation document = new Annotation(input);
	Parsers.pipeline.annotate(document);
	List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
    	    ans = ans + dependencies.toString() + "\n";
        }
        return ans;
    }
    public static String resolveCoreferences(String input) {
        String ans = "";
        Annotation document = new Annotation(input);
	Parsers.pipeline.annotate(document);
	Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);
        //http://stackoverflow.com/questions/6572207/stanford-core-nlp-understanding-coreference-resolution
	for(Map.Entry<Integer, CorefChain> entry : graph.entrySet()) {
            CorefChain c = entry.getValue();
            //this is because it prints out a lot of self references which aren't that useful
            if(c.getMentionsInTextualOrder().size() <= 1)
                continue;
            CorefMention cm = c.getRepresentativeMention();
            String clust = "";
            List<CoreLabel> tks = document.get(SentencesAnnotation.class).get(cm.sentNum-1).get(TokensAnnotation.class);
            for(int i = cm.startIndex-1; i < cm.endIndex-1; i++)
                clust += tks.get(i).get(TextAnnotation.class) + " ";
            clust = clust.trim();
            ans = ans + "Representative mention: \"" + clust + "\" is mentioned by:";
            for(CorefMention m : c.getMentionsInTextualOrder()){
                String clust2 = "";
                tks = document.get(SentencesAnnotation.class).get(m.sentNum-1).get(TokensAnnotation.class);
                for(int i = m.startIndex-1; i < m.endIndex-1; i++)
                    clust2 += tks.get(i).get(TextAnnotation.class) + " ";
                clust2 = clust2.trim();
                //don't need the self mention
                if(clust.equals(clust2))
                    continue;
                ans = ans + "\t" + clust2 + "\n";
            }
        }
        return ans;
    }
    public static String pos(String input) {
        String ans = "";
        Annotation document = new Annotation(input);
	Parsers.pipeline.annotate(document);
	List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
	    for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                String word = token.get(TextAnnotation.class);
                String pos = token.get(PartOfSpeechAnnotation.class);
		ans = ans + word+"|"+pos+"\n";
            }
            ans = ans + "\n";
        }
        return ans;
    }
    public static String lemma(String input) {
        String ans = "";
        Annotation document = new Annotation(input);
	Parsers.pipeline.annotate(document);
	List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
	    for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                String word = token.get(TextAnnotation.class);
                String lemma = token.get(LemmaAnnotation.class);
		ans = ans + word+"|"+lemma+"\n";
            }
            ans = ans + "\n";
        }
        return ans;
    }
    public static String ner(String input) {
        String ans = "";
        Annotation document = new Annotation(input);
	Parsers.pipeline.annotate(document);
	List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
	    for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                String word = token.get(TextAnnotation.class);
                String ner = token.get(NamedEntityTagAnnotation.class);
		ans = ans + word+"|"+ner+"\n";
            }
            ans = ans + "\n";
        }
        return ans;
    }
}
