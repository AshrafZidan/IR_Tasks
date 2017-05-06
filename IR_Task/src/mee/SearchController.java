package mee;

import java.io.File;
import java.util.ArrayList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.StringTokenizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;

public class SearchController {

    @FXML

    private JFXButton search;

    @FXML
    private JFXButton show;
    @FXML
    private JFXTextField txt1;

    @FXML
    private JFXButton browse;

    @FXML
    private JFXTextArea resultTextArea;

    DirectoryChooser dirchose = new DirectoryChooser();
    File file;
    StopWords stopWords = new StopWords();

    public String fileName;

    String Contents;
ArrayList<String>Stop_words =new ArrayList();
    File[] list_of_file;
    ArrayList<String> tokenn = new ArrayList<String>();
    ArrayList<String> Docs = new ArrayList<String>();
    HashMap<String, document> index = new HashMap<String, document>();
    ArrayList<String> InvertedIndex = new ArrayList();
    
    public void initialize(URL url, ResourceBundle rb) {
         Stop_words.add("the");
        Stop_words.add("an");
        Stop_words.add("is");
        Stop_words.add("are");
        Stop_words.add("am");
        Stop_words.add("was");
        Stop_words.add("where");
        Stop_words.add("can");
        Stop_words.add("@");
        Stop_words.add("#");
        Stop_words.add("$");
        Stop_words.add("%");
        Stop_words.add("*");
        Stop_words.add("in");
        Stop_words.add("what");
        Stop_words.add("why");
        Stop_words.add("we");
    }
    // Exit Button Action
    public void exitButtonAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void browesfile(ActionEvent e) throws Exception {
        ArrayList<String> Index_before_stemmer=new ArrayList();
           ArrayList<ArrayList> docs = new ArrayList();
         try {
           File[] list = new DirectoryChooser().showDialog(null).listFiles();
           
            for (File f : list) {
                    Scanner scan;
                
                    scan = new Scanner(f);
                
                    String line;
                    while (scan.hasNext()) {
                        line = scan.nextLine();
                        StringTokenizer toke = new StringTokenizer(line, " ");
                        while (toke.hasMoreTokens()) {
                            String s = toke.nextToken();
                            boolean a = false;
                            for (int i = 0; i < Index_before_stemmer.size(); i++) {
                                if (s.equalsIgnoreCase(Index_before_stemmer.get(i))) {
                                   docs.get(i).add(f.getName());
                                    a = true;
                                    break;
                                }
                            }
                            if (a == false) {
                                Index_before_stemmer.add(s);
                                ArrayList aa = new ArrayList();
                                aa.add(f.getName());
                               docs.add(aa);
                            }
                        }
                    } 
            }
            
            for (int i = 0; i <Stop_words.size(); i++) {
                for (int j = 0; j < Index_before_stemmer.size(); j++) {

                    if (Index_before_stemmer.get(j).toString().equalsIgnoreCase(Stop_words.get(i).toString())) {
                        Index_before_stemmer.remove(j);
                       docs.remove(j);
                    }
                }
            }
            
            InvertedIndex.addAll(Index_before_stemmer);
          
            Doc_stammer(Index_before_stemmer);
            
            for (int i = 0; i < InvertedIndex.size(); i++) {

                document d = new document();
                System.out.println ("Term is "+ InvertedIndex.get(i) + "   ");
                System.out.println("Doc_ID \t  \t  tf \t \t  (tf * idf) ");     
                int tf = 1; 
                
                for (int j = 0; j < docs.get(i).size(); j++) {
                    for (int k = j + 1; k < docs.get(i).size(); k++) {
                        if (docs.get(i).get(j).toString().equalsIgnoreCase(docs.get(i).get(k).toString())) {
                          docs.get(i).remove(k);
                            tf ++;
                            k--;
                        }

                    }
                    int idf=list.length-docs.get(i).size();
                    d.doc.put(docs.get(i).get(j), tf*idf);
                    System.out.print( docs.get(i).get(j)+ "\t \t "+ tf+"\t   \t "  + tf*idf + "   ");
                }
                System.out.println();
                index.put(InvertedIndex.get(i), d);
              
            }
            } catch (Exception ex) {
                
                }
           
        } 


    public void Doc_stammer(ArrayList a) {
        char[] w = new char[501];
        PorterStemmer s = new PorterStemmer();

        for (int i = 0; i < a.size(); i++) {
            try {
                int var = 0;
                int ch = a.get(i).toString().charAt(var);
                if (Character.isLetter((char) ch)) {
                    int j = 0;
                    while (true) {
                        var++;
                        ch = Character.toLowerCase((char) ch);
                        w[j] = (char) ch;
                        if (j < 500) {
                            j++;
                        }
                        if (var == a.get(i).toString().length()) {
                            for (int c = 0; c < j; c++) {
                                s.add(w[c]);
                            }
                            s.stem();
                            a.set(i, s.toString());
                            //  portred.add(s.toString());
                            break;
                        } else {
                            ch = a.get(i).toString().charAt(var);
                        }
                    }
                }
                if (ch < 0) {
                    break;
                }
            } catch (Exception ex) {
                resultTextArea.appendText("Stemmer error");
            }
        }
    }

    public String Quiry_stammer(ArrayList a) {
        char[] w = new char[501];
        PorterStemmer s = new PorterStemmer();

        for (int i = 0; i < a.size(); i++) {
            try {
                int var = 0;
                int ch = a.get(i).toString().charAt(var);
                if (Character.isLetter((char) ch)) {
                    int j = 0;
                    while (true) {
                        var++;
                        ch = Character.toLowerCase((char) ch);
                        w[j] = (char) ch;
                        if (j < 500) {
                            j++;
                        }
                        if (var == a.get(i).toString().length()) {
                            for (int c = 0; c < j; c++) {
                                s.add(w[c]);
                            }
                            s.stem();

                            break;
                        } else {
                            ch = a.get(i).toString().charAt(var);
                        }
                    }
                }
                if (ch < 0) {
                    break;
                }
            } catch (Exception ex) {
                resultTextArea.appendText("stemmer error in  Query");
            }
        }
        return s.toString();
    }

 public  void  SearchWord(){
            
            resultTextArea.setText("Doc_ID \t \t  weight * IDF");
            if (txt1.getText().equals("")) {
                resultTextArea.setText("enter keyword ");
            } else {
                ArrayList a = new ArrayList();
                a.add(txt1.getText());
                String quary= txt1.getText().toString();
           
                 quary = Quiry_stammer(a);
                try {
                    document docc = (document) index.get(quary);
                    HashMap c = docc.doc;
                    Collection au1 = c.keySet();
                    Collection au2 = c.values();
                    Iterator it = au1.iterator();
                    Iterator it2 = au2.iterator();
                    while (it.hasNext()) {
                        resultTextArea.setText(resultTextArea.getText() + "\n  " + it.next().toString() + "\t\t" + it2.next());
                    }

                } catch (Exception ex) {
                    resultTextArea.setText("not Result");
                }

            }
       
}
}
    

