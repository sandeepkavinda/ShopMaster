package panels;

import GUI.Home;
import SubGUI.AddNewProduct;
import SubGUI.ProductDetails;
import java.awt.event.ItemEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import utils.ClipboardUtils;
import utils.ToastUtils;

/**
 *
 * @author Sandeep
 */
public class ProductManagement extends javax.swing.JPanel {

    Home home;
    HashMap<String, String> categoryMap = new HashMap<>();
    HashMap<String, String> measUnitsMap = new HashMap<>();

    /**
     * Creates new form ProductManagement
     */
    public ProductManagement(Home home) {
        this.home = home;
        initComponents();
        loadCategories();
        loadMeasurementUnits();
        loadProductTable();

        //Table Data Alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        productTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        productTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        productTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        productTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

    }

    private void loadCategories() {
        try {
            ResultSet result = MySQL.execute("SELECT * FROM `category`");
            Vector v = new Vector();
            v.add("All Categories");
            while (result.next()) {
                v.add(result.getString("name"));
                categoryMap.put(result.getString("name"), result.getString("id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            categotyComboBox.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMeasurementUnits() {
        try {
            ResultSet result = MySQL.execute("SELECT * FROM `measurement_unit`");
            Vector v = new Vector();
            v.add("All Units");
            while (result.next()) {
                v.add(result.getString("name"));
                measUnitsMap.put(result.getString("name"), result.getString("id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            measUnitsComboBox.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadProductTable() {
        try {
            String search = searchTextField.getText();
            String categoryId = categoryMap.get(String.valueOf(categotyComboBox.getSelectedItem()));
            String measUnitId = measUnitsMap.get(String.valueOf(measUnitsComboBox.getSelectedItem()));
            int sortBy = sortByComboBox.getSelectedIndex();

            String sortByColumn = "";
            String sortByType = "";

            if (sortBy == 0) {
                sortByColumn = "id";
                sortByType = "DESC";
            } else if (sortBy == 1) {
                sortByColumn = "id";
                sortByType = "ASC";
            } else if (sortBy == 2) {
                sortByColumn = "name";
                sortByType = "ASC";
            } else if (sortBy == 3) {
                sortByColumn = "name";
                sortByType = "DESC";
            }

            String searchByCategoryQueryPart = "";
            String searchByMeasUnitQueryPart = "";

            if (categoryId != null) {
                searchByCategoryQueryPart = "AND `product`.`category_id`='" + categoryId + "' ";
            }

            if (measUnitId != null) {
                searchByMeasUnitQueryPart = "AND `product`.`measurement_unit_id`='" + measUnitId + "' ";
            }

            DefaultTableModel model = (DefaultTableModel) productTable.getModel();
            model.setRowCount(0);

            ResultSet results = MySQL.execute(""
                    + "SELECT * FROM `product` "
                    + "INNER JOIN `category` ON `product`.`category_id`=`category`.`id` "
                    + "INNER JOIN `measurement_unit` ON `product`.`measurement_unit_id`=`measurement_unit`.`id` "
                    + "WHERE (`product`.`name` LIKE '%" + search + "%' OR `product`.`id` LIKE '%" + search + "%')"
                    + searchByCategoryQueryPart
                    + searchByMeasUnitQueryPart
                    + "ORDER BY `product`.`" + sortByColumn + "` " + sortByType + "");

            while (results.next()) {
                String productId = results.getString("id");
                ResultSet results2 = MySQL.execute("SELECT COUNT(*) AS stock_count FROM stock WHERE product_id ='" + productId + "' ");
                results2.next();

                Vector v = new Vector();
                v.add(results.getString("id"));
                v.add(results.getString("name"));
                v.add(results.getString("category.name"));
                v.add(results.getString("measurement_unit.name"));
                v.add(results2.getString("stock_count"));

                model.addRow(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearSearch() {
        searchTextField.setText("");
        categotyComboBox.setSelectedIndex(0);
        measUnitsComboBox.setSelectedIndex(0);
        sortByComboBox.setSelectedIndex(0);
    }

    private void deleteSelectedProduct() {

        int selectedRow = productTable.getSelectedRow();

        if (selectedRow != -1) {
            String productName = String.valueOf(productTable.getValueAt(selectedRow, 1));
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "<html>Are you sure you want to delete,<br> <b>\"" + productName + "\"?<b><html>",
                    "Delete Product",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {
                try {
                    String selectedId = String.valueOf(productTable.getValueAt(selectedRow, 0));
                    MySQL.execute("DELETE FROM product WHERE id = '" + selectedId + "'");
                    JOptionPane.showMessageDialog(this, "Product Deleted Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/resource/success.png")));
                    loadProductTable();
                } catch (SQLException e) {
                    // Check if the exception message indicates a foreign key constraint violation
                    if (e.getErrorCode() == 1451) {
                        JOptionPane.showMessageDialog(this, "Product linked to other records in the database.", "Cannot Delete", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete", "Warning", JOptionPane.WARNING_MESSAGE);

        }

    }

    private void openSelectedProductDetails(boolean editable) {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String productId = productTable.getValueAt(selectedRow, 0).toString();
            new ProductDetails(home, true, productId, this, editable);
        }
    }
    
      private void copySelectedProductId() {
        int selectedRow = productTable.getSelectedRow();

        if (selectedRow != -1) {
            String productId = String.valueOf(productTable.getValueAt(selectedRow, 0));
            ClipboardUtils.copyToClipboard(productId);
            ToastUtils.showBottomToast(home, "Copied To Clipboard",1500);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rightClickPopupMenu = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        categotyComboBox = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        measUnitsComboBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        sortByComboBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        addNewProductButton1 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        addNewProductButton = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();

        jMenuItem1.setText("Open Product Details");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        rightClickPopupMenu.add(jMenuItem1);

        jMenuItem2.setText("Edit");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        rightClickPopupMenu.add(jMenuItem2);

        jMenuItem3.setText("Copy Product Id");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        rightClickPopupMenu.add(jMenuItem3);

        setMinimumSize(new java.awt.Dimension(852, 617));
        setPreferredSize(new java.awt.Dimension(852, 617));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 105, 75));
        jLabel1.setText("Product Management");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jPanel7.setForeground(new java.awt.Color(255, 51, 51));

        jLabel6.setText("Search");

        searchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(searchTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel7);

        jPanel5.setForeground(new java.awt.Color(255, 51, 51));

        jLabel3.setText("Category");

        categotyComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                categotyComboBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(categotyComboBox, 0, 128, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categotyComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel5);

        jPanel3.setForeground(new java.awt.Color(255, 51, 51));

        measUnitsComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                measUnitsComboBoxItemStateChanged(evt);
            }
        });

        jLabel4.setText("Measurement Unit");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(measUnitsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 30, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(measUnitsComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel3);

        jPanel4.setForeground(new java.awt.Color(255, 51, 51));

        sortByComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Added (Newest First)", "Added (Oldest First)", "Product Name (A-Z)", "Product Name (Z-A)" }));
        sortByComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sortByComboBoxItemStateChanged(evt);
            }
        });

        jLabel5.setText("Sort By");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sortByComboBox, 0, 128, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sortByComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel4);

        jPanel10.setForeground(new java.awt.Color(255, 51, 51));

        addNewProductButton1.setBackground(new java.awt.Color(102, 102, 102));
        addNewProductButton1.setForeground(new java.awt.Color(255, 255, 255));
        addNewProductButton1.setText("Clear Search");
        addNewProductButton1.setBorder(null);
        addNewProductButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewProductButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addNewProductButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(addNewProductButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel10);

        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(204, 204, 204)));
        jPanel8.setForeground(new java.awt.Color(255, 51, 51));

        addNewProductButton.setText("New Product");
        addNewProductButton.setToolTipText("Add New Product");
        addNewProductButton.setBorder(null);
        addNewProductButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewProductButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(addNewProductButton, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(addNewProductButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel8);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id", "Product Name", "Category", "Measurement Unit", "Number Of Stocks"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        productTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        productTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productTableMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                productTableMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(productTable);
        if (productTable.getColumnModel().getColumnCount() > 0) {
            productTable.getColumnModel().getColumn(0).setMaxWidth(50);
            productTable.getColumnModel().getColumn(1).setMinWidth(200);
            productTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void categotyComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_categotyComboBoxItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            loadProductTable();
        }
    }//GEN-LAST:event_categotyComboBoxItemStateChanged

    private void sortByComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sortByComboBoxItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            loadProductTable();
        }
    }//GEN-LAST:event_sortByComboBoxItemStateChanged

    private void measUnitsComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_measUnitsComboBoxItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            loadProductTable();
        }
    }//GEN-LAST:event_measUnitsComboBoxItemStateChanged

    private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyReleased
        // TODO add your handling code here:
        loadProductTable();
    }//GEN-LAST:event_searchTextFieldKeyReleased

    private void productTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productTableMouseClicked
        if (evt.getClickCount() == 2) {
            openSelectedProductDetails(false);
        }
    }//GEN-LAST:event_productTableMouseClicked

    private void addNewProductButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewProductButtonActionPerformed
        AddNewProduct addNewProduct = new AddNewProduct(home, true, this, null);
        addNewProduct.setVisible(true);
    }//GEN-LAST:event_addNewProductButtonActionPerformed

    private void addNewProductButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewProductButton1ActionPerformed
        // TODO add your handling code here:
        clearSearch();
        ToastUtils.showBottomToast(home, "Search Cleared", 2000);
    }//GEN-LAST:event_addNewProductButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        openSelectedProductDetails(false);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        copySelectedProductId();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void productTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productTableMouseReleased
        int row = productTable.rowAtPoint(evt.getPoint());
        if (row >= 0) {
            productTable.setRowSelectionInterval(row, row); // Select the row
            if (evt.isPopupTrigger()) { // Right-click
                rightClickPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_productTableMouseReleased

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
         openSelectedProductDetails(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewProductButton;
    private javax.swing.JButton addNewProductButton1;
    private javax.swing.JComboBox<String> categotyComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> measUnitsComboBox;
    private javax.swing.JTable productTable;
    private javax.swing.JPopupMenu rightClickPopupMenu;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JComboBox<String> sortByComboBox;
    // End of variables declaration//GEN-END:variables
}
