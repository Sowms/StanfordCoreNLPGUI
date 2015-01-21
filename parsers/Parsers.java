/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.util.Properties;

/**
 *
 * @author sowmya
 */
public class Parsers {

    /**
     * @param args the command line arguments
     */
    static StanfordCoreNLP pipeline;
    public static void main(String[] args) {
        
        Properties props = new Properties();
	props.put("annotators", "tokenize, ssplit, pos, lemma, ner,parse,dcoref");
	pipeline = new StanfordCoreNLP(props);
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ParserGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        ParserGUI mainInterface = new ParserGUI();
        mainInterface.setLocation(100, 100);
        mainInterface.setVisible(true);
    }
    
}
