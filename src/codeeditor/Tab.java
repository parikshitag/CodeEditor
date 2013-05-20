package codeeditor;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.undo.*;
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/**
 *
 * @author norps
 */
public class Tab extends javax.swing.JPanel {

    /**
     * Creates new form Tab
     */
    Document doc;
    StyledDocument styledDoc;
    private File file;
    private FileWriter fw;
    HashMap<Object, Action> actions;
    //undo helpers
    protected UndoAction undoAction=new UndoAction();
    protected RedoAction redoAction = new RedoAction();
    protected UndoManager undo = new UndoManager();
     private List<String> words1=null;
     private List<String> words2=null;
    final Highlighter hilit;
       
    SimpleAttributeSet sa;
            
    public Tab() {
        initCode(); 
        hilit = new DefaultHighlighter();      
        textPane.setHighlighter(hilit);
    }
    public Tab(File file) throws IOException{
        initCode();

        this.file=file;
        textPane.read(new FileReader(file),file);
        
        hilit = new DefaultHighlighter();
        textPane.setHighlighter(hilit);
    }
    
    Action getUndoAction(){
        return undoAction;
    }
    
    Action getRedoAction(){
        return redoAction;
    }
    
    void initCode(){
        initComponents();
        
        words();
        
        sa=new SimpleAttributeSet();
        setLayout(new GridLayout(1, 1));
        actions=createActionTable(textPane);
        styledDoc = textPane.getStyledDocument();
        if (styledDoc instanceof AbstractDocument) {
            doc = (AbstractDocument)styledDoc;
        } else {
            System.err.println("Text pane's document isn't an AbstractDocument!");
            System.exit(-1);
        }
        doc.addDocumentListener(new MyDocumentListener());
        doc.addUndoableEditListener(new MyUndoableEditListener());
        
        
    }
    
    void words(){
        words1 = new ArrayList<String>();
        words1.add("abstract");
        words1.add("boolean");
        words1.add("byte");
        words1.add("char");
        words1.add("class");
        words1.add("const");
        words1.add("double");
        words1.add("enum");
        words1.add("final");
        words1.add("float");
        words1.add("int");
        words1.add("interface");
        words1.add("long");
        words1.add("native");
        words1.add("package");
        words1.add("private");
        words1.add("protected");
        words1.add("public");
        words1.add("short");
        words1.add("static");
        words1.add("strictfp");
        words1.add("synchronized");
        words1.add("transient");
        words1.add("void");
        words1.add("volatile");
        
        words2 = new ArrayList<String>();
        words2.add("assert");
        words2.add("break");
        words2.add("case");
        words2.add("catch");
        words2.add("continue");
        words2.add("default");
        words2.add("do");
        words2.add("else");
        words2.add("extends");
        words2.add("false");
        words2.add("finally");
        words2.add("for");
        words2.add("goto");
        words2.add("if");
        words2.add("implements");
        words2.add("import");
        words2.add("instanceof");
        words2.add("new");
        words2.add("null");
        words2.add("return");
        words2.add("super");
        words2.add("switch");
        words2.add("this");
        words2.add("throw");
        words2.add("throws");
        words2.add("true");
        words2.add("try");
        words2.add("while");
    }
    
    public JTextPane getTextPane(){
        return textPane;
    }
    
    public Highlighter getHilit(){
        return hilit;
    }
    public void saveFile(File file) throws IOException
    {
        this.file=file;
        saveFile();
      
    }
    
    public boolean saveFile() throws IOException
    {
        if(file!=null){
            fw= new FileWriter(file);
            fw.write(textPane.getText());
            fw.close();
            return true;
        }else
            return false;
    }
    
    private HashMap<Object, Action> createActionTable(JTextComponent textComponent) {
        HashMap<Object, Action> actions = new HashMap<Object, Action>();
        Action[] actionsArray = textComponent.getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            Action a = actionsArray[i];
            actions.put(a.getValue(Action.NAME), a);
        }
        return actions;
    }
    
    public Action getActionByName(String name) {
        return actions.get(name);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        textPane.setDragEnabled(true);
        jScrollPane2.setViewportView(textPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
         
    }//GEN-LAST:event_formComponentResized

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane textPane;
    // End of variables declaration//GEN-END:variables


//And this one listens for any changes to the document.
    protected class MyDocumentListener implements DocumentListener {
        private int braceCount=0;
            public void changedUpdate(DocumentEvent ev) {
            }
    
            public void removeUpdate(DocumentEvent ev) {
            }
    
        public void insertUpdate(DocumentEvent e) {
            indentHandling(e);
            colorCoding(e);
        }
        
        private void indentHandling(DocumentEvent e){
            int pos=e.getOffset();

            try{
                if("{".equals(doc.getText(pos, 1))){
                    ++braceCount;
                }
                else if("}".equals(doc.getText(pos, 1))){
                    --braceCount;
                }
                else if("\n".equals(doc.getText(pos, 1)))
                    SwingUtilities.invokeLater(new CompletionTask(pos+1,braceCount));
            } catch (BadLocationException ble) {
                ble.printStackTrace();
            }
        }
        
        private void colorCoding(DocumentEvent ev){
            if (ev.getLength() != 1) {
                return;
            }
        
        int pos = ev.getOffset();
        String content = null;
        try {
            content = doc.getText(0, pos + 1);
            
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        // Find where the word starts
        int w;
        for (w = pos; w >= 0; w--) {
            if (! Character.isLetter(content.charAt(w))) {
                break;
            }
        }
        if (pos - w < 2) {
            // Too few chars
            return;
        }
        StyleConstants.setForeground(sa, Color.black);
             SwingUtilities.invokeLater(new CompletionTask1(w+1, pos +1));
        String prefix = content.substring(w + 1).toLowerCase();
        
        int n1 = Collections.binarySearch(words1, prefix);
        
        if ((n1 < 0 && -n1 <= words1.size())) {
            String match1 = words1.get(-n1 - 1);
            if (match1.startsWith(prefix)) {
                StyleConstants.setForeground(sa, Color.red);
                SwingUtilities.invokeLater(
                        new CompletionTask1(w+1, pos +1));
            }
        }else if(n1>=0){
            String match1 = words1.get(n1);
            if (match1.startsWith(prefix)) {
                StyleConstants.setForeground(sa, Color.red);
                SwingUtilities.invokeLater(
                        new CompletionTask1(w+1, pos +1));
            }
        }
        
        int n2 = Collections.binarySearch(words2, prefix);
        
        if ((n2 < 0 && -n2 <= words2.size())) {
            String match2 = words2.get(-n2 - 1);
            if (match2.startsWith(prefix)) {
                StyleConstants.setForeground(sa, Color.blue);
                SwingUtilities.invokeLater(
                        new CompletionTask1(w+1, pos +1));
            }
        }else if(n2>=0){
            String match2 = words2.get(n2);
            if (match2.startsWith(prefix)) {
                StyleConstants.setForeground(sa, Color.blue);
                SwingUtilities.invokeLater(
                        new CompletionTask1(w+1, pos +1));
            }
        }
        
     }
       
        
    //this reacts to a change
    class CompletionTask implements Runnable {
     private int pos;
     private int braceCount;
        
         CompletionTask(int pos,int braceCount){
             this.pos=pos;
             this.braceCount=braceCount;
             new Thread(this).start();
         }
        
        public void run()
        {
            try{
                    doc.insertString(pos, returnWhitespaces(braceCount), null);
            }catch (BadLocationException ble) {
                    ble.printStackTrace();
                }

        }
 }
    class CompletionTask1 implements Runnable {
     int w;
     int position;
         
        CompletionTask1(int w, int position) {
            this.w = w;
            this.position = position;
        }
        public void run()
        {
                   styledDoc.setCharacterAttributes(w,position,sa,true);
                   
        }
 }

    private String returnWhitespaces(int braceCount){
        String str="";
        for(int i=0;i<braceCount;i++){
            str=str.concat("    ");
        }
        return str;

    }
}
    
//This one listens for edits that can be undone.
     class MyUndoableEditListener implements UndoableEditListener {
        public void undoableEditHappened(UndoableEditEvent e) {
            //Remember the edit and update the menus.
            undo.addEdit(e.getEdit());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        }
    }

 class UndoAction extends AbstractAction {
        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }
 
        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                ex.printStackTrace();
            }
            updateUndoState();
            redoAction.updateRedoState();
        }
 
        protected void updateUndoState() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }
 
    class RedoAction extends AbstractAction {
        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }
 
        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                ex.printStackTrace();
            }
            updateRedoState();
            undoAction.updateUndoState();
        }
 
        protected void updateRedoState() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }
    
    
    
}


