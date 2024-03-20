//////////////////// IMPORTS //////////////////////////////////////////////
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
//////////////////// IMPORTS //////////////////////////////////////////////






//////////////////// CLASS //////////////////////////////////////////////
public class ODESolverGUI {



//////////////////// INITIALIZATIONS //////////////////////////////////////////////
    private static final int MAX_EQUATIONS = 10; // maximum number of equations allowed
    private JFrame frame;                        
    private JPanel mainPanel;                    
    private ArrayList<JTextField> formulaFields; // list to hold the equations text fields
   // private ArrayList<JTextField> variableFields; // list to hold the variable fields (e.g. 'X') // not used right now 
    private ArrayList<JTextField> initialFields; // list to hold the initial value fields   
    private JButton addButton;                   // button to add new equation fields
    private JButton generateButton;              // button to generate results based on input
    private JTextField stepSizeField, timeField; // fields for inputting step size and time
    private JCheckBox graphCheckBox, phaseSpaceCheckBox, tableCheckBox; // checkboxes for options
    private JComboBox<String> solverTypeComboBox; // comboBox to select the type of solver
//////////////////// INITIALIZATIONS //////////////////////////////////////////////







 


 ////////////////////  GUI CONSTRUCTOR //////////////////////////////////////////////
    public ODESolverGUI() {
        Font largerFont = new Font("SansSerif", Font.PLAIN, 18); //font



//////////////////// creating the structure //////////////////////////////////////////////
        formulaFields = new ArrayList<>();
    //    variableFields = new ArrayList<>();
        initialFields = new ArrayList<>();
        frame = new JFrame("ODE Solvers");  //window name
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //adding new ones
        addButton = new JButton("+");
        addButton.setFont(largerFont);
        addButton.addActionListener(e -> {
            if (formulaFields.size() < MAX_EQUATIONS) {
                addEquationInput(true);
                frame.pack();
            }
        });

        addEquationInput(false); // this adds the first non-removable equation field
        mainPanel.add(addButton);
//////////////////// creating the structure //////////////////////////////////////////////








////////////////////  Creating fields, buttons and naming them //////////////////////////////////////////////
        stepSizeField = createPlaceholderTextField("Step size (e.g. 0.1)", 12, largerFont);
        timeField = createPlaceholderTextField("Time (e.g. 10.0)", 12, largerFont);
        graphCheckBox = new JCheckBox("Graph");
        graphCheckBox.setFont(largerFont);
        phaseSpaceCheckBox = new JCheckBox("Phase space");
        phaseSpaceCheckBox.setFont(largerFont);
        tableCheckBox = new JCheckBox("Table");
        tableCheckBox.setFont(largerFont);

        // ComboBox for solvers and final BUTTON
        String[] solvers = {"Euler", "Runge-Kutta"};
        solverTypeComboBox = new JComboBox<>(solvers);
        solverTypeComboBox.setFont(largerFont);
        generateButton = new JButton("GENERATE");
        generateButton.setFont(largerFont);
////////////////////  Creating fields, buttons and naming them //////////////////////////////////////////////





////////////////////  COntrol panel //////////////////////////////////////////////
        JPanel controlsPanel = new JPanel();
        controlsPanel.add(stepSizeField);
        controlsPanel.add(timeField);
        controlsPanel.add(graphCheckBox);
        controlsPanel.add(phaseSpaceCheckBox);
        controlsPanel.add(tableCheckBox);
        controlsPanel.add(solverTypeComboBox);
        controlsPanel.add(generateButton);

        mainPanel.add(controlsPanel);

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
 ////////////////////  COntrol panel //////////////////////////////////////////////

    }

 ////////////////////  GUI CONSTRUCTOR //////////////////////////////////////////////









 ////////////////////  METHODS //////////////////////////////////////////////


    // Method to create a text field with placeholder text
    private JTextField createPlaceholderTextField(String text, int columns, Font font) {
        JTextField textField = new JTextField(text, columns);
        textField.setFont(font);
        textField.setForeground(Color.GRAY);
        textField.addFocusListener(new FocusAdapter() {
            // Clear the text when the field gains focus
            public void focusGained(FocusEvent evt) {
                if (textField.getText().equals(text)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            // restore the placeholder text when the user clicks away and its empty
            public void focusLost(FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(text);
                }
            }
        });
        return textField;
    }






    // Method to add a new equation input field
    private void addEquationInput(boolean removable) {
        Font largerFont = new Font("SansSerif", Font.PLAIN, 20);

        // Placeholder text fields for formula, variable, and initial value
        JTextField formulaField = createPlaceholderTextField("y + x", 20, largerFont);  // change intials text and size of window
    //    JTextField variableField = createVariableField(largerFont);
        JTextField initialValueField = createNumberField(largerFont);

        formulaFields.add(formulaField);
    //    variableFields.add(variableField);
        initialFields.add(initialValueField);

        // panel to hold each equation input row
        JPanel equationPanel = new JPanel();
        equationPanel.add(new JLabel("⊕"));
       // equationPanel.add(variableField);  ///////////////// IF YOU WANT TO ADD THE INITIAL VALUE FIELD BACK
        equationPanel.add(new JLabel("="));
        equationPanel.add(formulaField);
        equationPanel.add(new JLabel("Initial Value"));
        equationPanel.add(initialValueField);

        // Buttin to remove an equation input row
        if (removable) {
            JButton removeButton = new JButton("-");
            removeButton.setFont(largerFont);
            removeButton.addActionListener(e -> {
               // removeEquationInput(equationPanel, formulaField, variableField, initialValueField);
                removeEquationInput(equationPanel, formulaField, initialValueField);
                frame.pack();
            });
            equationPanel.add(removeButton);
        }

        mainPanel.add(equationPanel, mainPanel.getComponentCount() - 1);
    }









    // Method to remove an equation input row
 /* WITH VARIABLE NAME
    private void removeEquationInput(JPanel equationPanel, JTextField formulaField, JTextField variableField, JTextField initialValueField) {
        formulaFields.remove(formulaField);
        variableFields.remove(variableField);
        initialFields.remove(initialValueField);
        mainPanel.remove(equationPanel);
    }

*/

    private void removeEquationInput(JPanel equationPanel, JTextField formulaField, JTextField initialValueField) {
        formulaFields.remove(formulaField);
        initialFields.remove(initialValueField);
        mainPanel.remove(equationPanel);
    }



 
 
    // Method to create a text field for variable input that allows only one alphabetical character   NOT USED RIGHT NOW 
    private JTextField createVariableField(Font font) {
        JTextField variableField = new JTextField("X", 2);
        variableField.setFont(font);
        variableField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (getLength() + str.length() > 1) {
                    return;
                }
                if (str.matches("[a-zA-Z]")) { // if its a character 
                    super.insertString(offs, str.toUpperCase(), a);
                }
            }
        });
        return variableField;
    }






    // Method that creates a text field for numeric input only
    private JTextField createNumberField(Font font) {
        JTextField numberField = new JTextField("TEST", 7);
        numberField.setFont(font);
        numberField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) {
                    return;
                }
                if (str.matches("[0-9]*\\.?[0-9]*")) { // if its a number
                    super.insertString(offs, str, a);
                }
            }
        });
        return numberField;
    }

 ////////////////////  METHODS //////////////////////////////////////////////












//////////////////// MAIn //////////////////////////////////////////////
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ODESolverGUI::new);
    }
//////////////////// MAIN //////////////////////////////////////////////

}


//////////////////// CLASS //////////////////////////////////////////////
