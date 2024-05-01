/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gui;

import static gui.BurgerToppings.jLabel1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Home
 */
public class IceCreamToppings extends javax.swing.JDialog {

    private Menu myFrame;
    private OrderContext orderContext;
    private ComboExpression comboExpression;

    public IceCreamToppings(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void setLabelText(String text, String btnTxt) {
        jLabel1.setText(text);
        jLabel3.setText(btnTxt);
    }
///////////////////////////////////////////////Builder Pattern ///////////////////////////////////////////////////////////////////////

    public class IceCreamOrderBuilder {

        private String flavor;
        private String toppings;
        private String command;
        private String price;

        public IceCreamOrderBuilder setFlavor(String flavor) {
            this.flavor = flavor;
            return this;
        }

        public IceCreamOrderBuilder setToppings(String toppings) {
            this.toppings = toppings;
            return this;
        }

        public IceCreamOrderBuilder setCommand(String command) {
            this.command = command;
            return this;
        }

        public IceCreamOrderBuilder setPrice(String price) {
            this.price = price;
            return this;
        }

        public IceCreamOrder build() {
            return new IceCreamOrder(this);
        }
    }

    public class IceCreamOrder {

        private String flavor;
        private String toppings;
        private String command;
        private String price;

        private IceCreamOrder(IceCreamOrderBuilder builder) {
            this.flavor = builder.flavor;
            this.toppings = builder.toppings;
            this.command = builder.command;
            this.price = builder.price;
        }

        public String getFullOrder() {
            return flavor + " " + toppings + "( " + command + ")";
        }

        public String getPrice() {
            return price;
        }
    }

///////////////////////////////////////////////Builder Pattern ///////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////////////////////Flyweight Pattern//////////////////////////////////////////////////////////////////////////////////////   
    interface Topping {

        void apply(JLabel label, boolean isSelected);

        double getPrice();
    }

    class CommonTopping implements Topping {

        private String name;
        private double price;

        public CommonTopping(String name, double price) {
            this.name = name;
            this.price = price;
        }

        @Override
        public void apply(JLabel label, boolean isSelected) {
            // Update jLabel2 based on the topping and its state
            if (isSelected) {
                label.setText(label.getText() + " with " + name);
            } else {
                label.setText(label.getText().replace(" with " + name, ""));
            }
        }

        public double getPrice() {
            return price;
        }
    }

    class ToppingFactory {

        private Map<String, Topping> toppingPool = new HashMap<>();

        public Topping getTopping(String name, double price) {
            toppingPool.putIfAbsent(name, new CommonTopping(name, price));
            return toppingPool.get(name);
        }
    }

    String price;
    double currentPrice;

    private ToppingFactory toppingFactory = new ToppingFactory();

    private void updatePrice(Topping topping, JLabel label, boolean isSelected) {
        price = jLabel3.getText();
        currentPrice = parsePrice(price);

        double updatePrice = isSelected ? topping.getPrice() : -topping.getPrice();
        double totalPrice = currentPrice + updatePrice;
        jLabel3.setText(String.format("%.2f", totalPrice));

        // Apply the topping
        topping.apply(label, isSelected);
    }
///////////////////////////////////////////Flyweight Pattern//////////////////////////////////////////////////////////////////////////////////////  

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////Interprter Pattern////////////////////////////////////////////////////////////////////////////////
    class OrderContext {

        private java.util.List<String> orderDetails;
        private String userInput;

        public OrderContext() {
            orderDetails = new ArrayList<>();
        }

        public void addToOrder(String details) {
            orderDetails.add(details);
        }

        public java.util.List<String> getOrderDetails() {
            return orderDetails;
        }

        public String getUserInput() {
            return userInput;
        }

        public void setUserInput(JTextField userInputField) {
            this.userInput = userInputField.getText();
        }
    }

// Abstract Expression
    interface OrderExpression {

        void interpret(OrderContext context);
    }

// Terminal Expressions
    class ExtraChocolateExpression implements OrderExpression {

        @Override
        public void interpret(OrderContext context) {
            context.addToOrder("Extra Chocolate");
        }
    }

    class ExtraCashewExpression implements OrderExpression {

        @Override
        public void interpret(OrderContext context) {
            context.addToOrder("Extra Cashew");
        }
    }

// Non-terminal Expression
    class ComboExpression implements OrderExpression {

        private java.util.List<OrderExpression> expressions;

        public ComboExpression() {
            expressions = new ArrayList<>();
        }

        public void addExpression(OrderExpression expression) {
            expressions.add(expression);
        }

        @Override
        public void interpret(OrderContext context) {
            for (OrderExpression expression : expressions) {
                expression.interpret(context);
            }
        }
    }

    private boolean interpretCommand(JTextField userInputField) {
        String userInput = userInputField.getText();

        if (userInput.isEmpty()) {
            // Empty command, show JOptionPane and return
            JOptionPane.showMessageDialog(null, "Please enter a command");
            return false;
        }

        // Clear previous order details
        comboExpression = new ComboExpression();

        String[] commands = userInput.split("\\+");
        for (String command : commands) {
            switch (command.trim().toLowerCase()) {
                case "extra chocolate":
                    comboExpression.addExpression(new ExtraChocolateExpression());
                    break;
                case "extra cashew":
                    comboExpression.addExpression(new ExtraCashewExpression());
                    break;

                default:
                    // Invalid command, show JOptionPane and return
                    JOptionPane.showMessageDialog(null, "Invalid command: " + command.trim());
                    return false;
            }
        }

        return true;
    }

/////////////////////////////////////////////Interprter Pattern////////////////////////////////////////////////////////////////////////////////
    private void updatePrice1(JCheckBox selectedCheckBox) {
        String selectedToppingName = selectedCheckBox.getText();
        double price = 100.00;  // Set the default price or retrieve it based on the topping name

        IceCreamToppings.Topping topping = toppingFactory.getTopping(selectedToppingName, price);
        updatePrice(topping, jLabel6, selectedCheckBox.isSelected());
    }

    private void updatePrice2(JCheckBox selectedCheckBox) {
        String selectedToppingName = selectedCheckBox.getText();
        double price = 80.00;  // Set the default price or retrieve it based on the topping name

        IceCreamToppings.Topping topping = toppingFactory.getTopping(selectedToppingName, price);
        updatePrice(topping, jLabel6, selectedCheckBox.isSelected());
    }

    private void updatePrice3(JCheckBox selectedCheckBox) {
        String selectedToppingName = selectedCheckBox.getText();
        double price = 220.00;  // Set the default price or retrieve it based on the topping name

        IceCreamToppings.Topping topping = toppingFactory.getTopping(selectedToppingName, price);
        updatePrice(topping, jLabel6, selectedCheckBox.isSelected());
    }

    private void updatePrice4(JCheckBox selectedCheckBox) {
        String selectedToppingName = selectedCheckBox.getText();
        double price = 160.00;  // Set the default price or retrieve it based on the topping name

        IceCreamToppings.Topping topping = toppingFactory.getTopping(selectedToppingName, price);
        updatePrice(topping, jLabel6, selectedCheckBox.isSelected());
    }

    private double parsePrice(String text) {
        try {
            // Extract the numeric part from the label text and parse it as a double
            return Double.parseDouble(text.replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
    ///////////////////////////////////////////Flyweight Pattern//////////////////////////////////////////////////////////////////////////////////////   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jCheckBox1.setFont(new java.awt.Font("Verdana", 1, 20)); // NOI18N
        jCheckBox1.setText("Chocolate Syrup");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setFont(new java.awt.Font("Verdana", 1, 20)); // NOI18N
        jCheckBox2.setText("Honey Syrup");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setFont(new java.awt.Font("Verdana", 1, 20)); // NOI18N
        jCheckBox3.setText("Cashew Nuts");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jCheckBox4.setFont(new java.awt.Font("Verdana", 1, 20)); // NOI18N
        jCheckBox4.setText(" Chocolate Chips");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 22)); // NOI18N
        jLabel2.setText("TOTAL :");

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 1, 22)); // NOI18N
        jLabel3.setText("0.00");

        jTextField1.setFont(new java.awt.Font("Verdana", 0, 16)); // NOI18N

        jButton1.setFont(new java.awt.Font("Showcard Gothic", 1, 18)); // NOI18N
        jButton1.setText("DONE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(204, 204, 0));
        jLabel4.setOpaque(true);

        jLabel6.setFont(new java.awt.Font("Segoe UI Semilight", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 0));

        jLabel5.setBackground(new java.awt.Color(204, 204, 0));
        jLabel5.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 278, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox1)
                            .addComponent(jCheckBox2)
                            .addComponent(jCheckBox3)
                            .addComponent(jCheckBox4))
                        .addGap(255, 255, 255))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jCheckBox1)
                .addGap(24, 24, 24)
                .addComponent(jCheckBox2)
                .addGap(24, 24, 24)
                .addComponent(jCheckBox3)
                .addGap(24, 24, 24)
                .addComponent(jCheckBox4)
                .addGap(33, 33, 33)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(204, 204, 0));

        jLabel1.setFont(new java.awt.Font("Showcard Gothic", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        updatePrice1(jCheckBox1);
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
        updatePrice2(jCheckBox2);
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        // TODO add your handling code here:
        updatePrice3(jCheckBox3);
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        // TODO add your handling code here:
        updatePrice4(jCheckBox4);
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        /////////////////////////Builder Pattern//////////////////////////////////
        if (interpretCommand(jTextField1)) {
            IceCreamOrder order = new IceCreamOrderBuilder()
                    .setFlavor(jLabel1.getText())
                    .setToppings(jLabel6.getText())
                    .setCommand(jTextField1.getText())
                    .setPrice(jLabel3.getText())
                    .build();

            myFrame.addRowToTable(order.getFullOrder(), order.getPrice());
            this.dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IceCreamToppings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IceCreamToppings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IceCreamToppings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IceCreamToppings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IceCreamToppings dialog = new IceCreamToppings(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    public static javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
