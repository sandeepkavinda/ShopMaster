package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import panels.Dashboard;
import panels.GrnManagement;
import panels.InvoiceManagement;
import panels.ProductManagement;
import panels.Quatations;
import panels.Reports;
import panels.ReturnManagement;
import panels.StockManagement;
import panels.UserManagement;

/**
 *
 * @author Sandeep
 */
public class Home extends javax.swing.JFrame {

    int maxMenuSize = 200;
    int minMenuSize = 40;
    Boolean isStartingMenuMaximizing = true;
    Boolean isMenuMaximized = true;
    Boolean isAnimationOn = true;
    Color dafaultLightColor = Color.decode("#ffffff");
    Color selectedButtonColor = Color.decode("#dddddd");
    Color primaryColor = Color.decode("#00694B");

    public Home() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource/icon.png")));
        initComponents();
        showMenu();
//        loadPanel(new Dashboard(), dashboardButton, dashboardActiveBar);
          loadPanel(new InvoiceManagement(this), invoiceManagementButton, invoiceManagementActiveBar);
    }

    private void loadPanel(JPanel panel, JButton selectedButton, JPanel selectActiveBar) {
        MainPanel.removeAll();
        MainPanel.add(panel, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(MainPanel);

        dashboardButton.setBackground(dafaultLightColor);
        userManagementButton.setBackground(dafaultLightColor);
        productManagementButton.setBackground(dafaultLightColor);
        stockManagementButton.setBackground(dafaultLightColor);
        grnManagementButton.setBackground(dafaultLightColor);
        invoiceManagementButton.setBackground(dafaultLightColor);
        returnManagementButton.setBackground(dafaultLightColor);
        quatationsButton.setBackground(dafaultLightColor);
        reportsButton.setBackground(dafaultLightColor);

        dashboardActiveBar.setBackground(dafaultLightColor);
        userManagementActiveBar.setBackground(dafaultLightColor);
        productManagementActiveBar.setBackground(dafaultLightColor);
        stockManagementActiveBar.setBackground(dafaultLightColor);
        grnManagementActiveBar.setBackground(dafaultLightColor);
        invoiceManagementActiveBar.setBackground(dafaultLightColor);
        returnManagementActiveBar.setBackground(dafaultLightColor);
        quatationsActiveBar.setBackground(dafaultLightColor);
        reportsActiveBar.setBackground(dafaultLightColor);

        selectedButton.setBackground(selectedButtonColor);
        selectActiveBar.setBackground(primaryColor);

    }

    private void showMenu() {
        if (isStartingMenuMaximizing) {
            //Maximize Menu
            NavigationPanel.setPreferredSize(new Dimension(maxMenuSize, NavigationPanel.getHeight()));
            SwingUtilities.updateComponentTreeUI(NavigationPanel);
            isMenuMaximized = true;
        } else {
            //Minimize Menu
            NavigationPanel.setPreferredSize(new Dimension(minMenuSize, NavigationPanel.getHeight()));
            SwingUtilities.updateComponentTreeUI(NavigationPanel);
            isMenuMaximized = false;
        }
    }

    private void changeMenu() {
        if (isMenuMaximized) {
            //Minimize Menu
            if (isAnimationOn) {
                //If animationStatus is true Menu Open With a Animation
                Thread t = new Thread(
                        () -> {
                            for (int i = maxMenuSize; i >= minMenuSize; i -= 40) {
                                NavigationPanel.setPreferredSize(new Dimension(i, NavigationPanel.getHeight()));
                                SwingUtilities.updateComponentTreeUI(NavigationPanel);
                                try {
                                    Thread.sleep(5);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                t.start();

            } else {
                NavigationPanel.setPreferredSize(new Dimension(minMenuSize, NavigationPanel.getHeight()));
                SwingUtilities.updateComponentTreeUI(NavigationPanel);
            }
            isMenuMaximized = false;

        } else {
            //Maximize Menu
            if (isAnimationOn) {
                //If animationStatus is true Menu Open With a Animation
                Thread t = new Thread(
                        () -> {
                            for (int i = minMenuSize; i <= maxMenuSize; i += 40) {
                                NavigationPanel.setPreferredSize(new Dimension(i, NavigationPanel.getHeight()));
                                SwingUtilities.updateComponentTreeUI(NavigationPanel);
                                try {
                                    Thread.sleep(5);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                t.start();
            } else {
                NavigationPanel.setPreferredSize(new Dimension(maxMenuSize, NavigationPanel.getHeight()));
                SwingUtilities.updateComponentTreeUI(NavigationPanel);
            }
            isMenuMaximized = true;
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        notoficationsButton = new javax.swing.JButton();
        settingsButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        MainPanel = new javax.swing.JPanel();
        NavigationPanel = new javax.swing.JPanel();
        dashboardActiveBar = new javax.swing.JPanel();
        productManagementActiveBar = new javax.swing.JPanel();
        stockManagementActiveBar = new javax.swing.JPanel();
        grnManagementActiveBar = new javax.swing.JPanel();
        invoiceManagementActiveBar = new javax.swing.JPanel();
        returnManagementActiveBar = new javax.swing.JPanel();
        quatationsActiveBar = new javax.swing.JPanel();
        reportsActiveBar = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        menuChangeButton = new javax.swing.JButton();
        dashboardButton = new javax.swing.JButton();
        productManagementButton = new javax.swing.JButton();
        stockManagementButton = new javax.swing.JButton();
        grnManagementButton = new javax.swing.JButton();
        invoiceManagementButton = new javax.swing.JButton();
        returnManagementButton = new javax.swing.JButton();
        quatationsButton = new javax.swing.JButton();
        reportsButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        quatationButton2 = new javax.swing.JButton();
        quatationButton3 = new javax.swing.JButton();
        quatationButton4 = new javax.swing.JButton();
        userManagementActiveBar = new javax.swing.JPanel();
        userManagementButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Shop Master POS System");
        setMinimumSize(new java.awt.Dimension(1108, 696));

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setMinimumSize(new java.awt.Dimension(1090, 673));

        jPanel4.setPreferredSize(new java.awt.Dimension(158, 44));

        notoficationsButton.setBackground(new java.awt.Color(255, 255, 255));
        notoficationsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/notification.png"))); // NOI18N
        notoficationsButton.setToolTipText("Notifications");
        notoficationsButton.setBorder(null);
        notoficationsButton.setMaximumSize(new java.awt.Dimension(23, 23));
        notoficationsButton.setMinimumSize(new java.awt.Dimension(23, 23));
        notoficationsButton.setPreferredSize(new java.awt.Dimension(23, 23));
        notoficationsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notoficationsButtonActionPerformed(evt);
            }
        });

        settingsButton.setBackground(new java.awt.Color(255, 255, 255));
        settingsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/settings.png"))); // NOI18N
        settingsButton.setToolTipText("Settings");
        settingsButton.setBorder(null);
        settingsButton.setMaximumSize(new java.awt.Dimension(23, 23));
        settingsButton.setMinimumSize(new java.awt.Dimension(23, 23));
        settingsButton.setPreferredSize(new java.awt.Dimension(23, 23));
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/logout.png"))); // NOI18N
        jButton3.setToolTipText("Logout");
        jButton3.setBorder(null);
        jButton3.setMaximumSize(new java.awt.Dimension(23, 23));
        jButton3.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton3.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/database-download.png"))); // NOI18N
        jButton4.setToolTipText("Download Database Backup");
        jButton4.setBorder(null);
        jButton4.setMaximumSize(new java.awt.Dimension(23, 23));
        jButton4.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton4.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(notoficationsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(notoficationsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/logo-extra-sm.png"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setPreferredSize(new java.awt.Dimension(158, 44));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 158, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(153, 255, 153));

        MainPanel.setBackground(new java.awt.Color(204, 255, 102));
        MainPanel.setPreferredSize(new java.awt.Dimension(852, 617));
        MainPanel.setLayout(new java.awt.BorderLayout());

        dashboardActiveBar.setBackground(new java.awt.Color(0, 105, 75));
        dashboardActiveBar.setPreferredSize(new java.awt.Dimension(4, 40));

        javax.swing.GroupLayout dashboardActiveBarLayout = new javax.swing.GroupLayout(dashboardActiveBar);
        dashboardActiveBar.setLayout(dashboardActiveBarLayout);
        dashboardActiveBarLayout.setHorizontalGroup(
            dashboardActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        dashboardActiveBarLayout.setVerticalGroup(
            dashboardActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        productManagementActiveBar.setBackground(new java.awt.Color(0, 105, 75));
        productManagementActiveBar.setPreferredSize(new java.awt.Dimension(4, 40));

        javax.swing.GroupLayout productManagementActiveBarLayout = new javax.swing.GroupLayout(productManagementActiveBar);
        productManagementActiveBar.setLayout(productManagementActiveBarLayout);
        productManagementActiveBarLayout.setHorizontalGroup(
            productManagementActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        productManagementActiveBarLayout.setVerticalGroup(
            productManagementActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        stockManagementActiveBar.setBackground(new java.awt.Color(0, 105, 75));
        stockManagementActiveBar.setPreferredSize(new java.awt.Dimension(4, 40));

        javax.swing.GroupLayout stockManagementActiveBarLayout = new javax.swing.GroupLayout(stockManagementActiveBar);
        stockManagementActiveBar.setLayout(stockManagementActiveBarLayout);
        stockManagementActiveBarLayout.setHorizontalGroup(
            stockManagementActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        stockManagementActiveBarLayout.setVerticalGroup(
            stockManagementActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        grnManagementActiveBar.setBackground(new java.awt.Color(0, 105, 75));
        grnManagementActiveBar.setPreferredSize(new java.awt.Dimension(4, 40));

        javax.swing.GroupLayout grnManagementActiveBarLayout = new javax.swing.GroupLayout(grnManagementActiveBar);
        grnManagementActiveBar.setLayout(grnManagementActiveBarLayout);
        grnManagementActiveBarLayout.setHorizontalGroup(
            grnManagementActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        grnManagementActiveBarLayout.setVerticalGroup(
            grnManagementActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        invoiceManagementActiveBar.setBackground(new java.awt.Color(0, 105, 75));
        invoiceManagementActiveBar.setPreferredSize(new java.awt.Dimension(4, 40));

        javax.swing.GroupLayout invoiceManagementActiveBarLayout = new javax.swing.GroupLayout(invoiceManagementActiveBar);
        invoiceManagementActiveBar.setLayout(invoiceManagementActiveBarLayout);
        invoiceManagementActiveBarLayout.setHorizontalGroup(
            invoiceManagementActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        invoiceManagementActiveBarLayout.setVerticalGroup(
            invoiceManagementActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        returnManagementActiveBar.setBackground(new java.awt.Color(0, 105, 75));
        returnManagementActiveBar.setPreferredSize(new java.awt.Dimension(4, 40));

        javax.swing.GroupLayout returnManagementActiveBarLayout = new javax.swing.GroupLayout(returnManagementActiveBar);
        returnManagementActiveBar.setLayout(returnManagementActiveBarLayout);
        returnManagementActiveBarLayout.setHorizontalGroup(
            returnManagementActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        returnManagementActiveBarLayout.setVerticalGroup(
            returnManagementActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        quatationsActiveBar.setBackground(new java.awt.Color(0, 105, 75));
        quatationsActiveBar.setPreferredSize(new java.awt.Dimension(4, 40));

        javax.swing.GroupLayout quatationsActiveBarLayout = new javax.swing.GroupLayout(quatationsActiveBar);
        quatationsActiveBar.setLayout(quatationsActiveBarLayout);
        quatationsActiveBarLayout.setHorizontalGroup(
            quatationsActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        quatationsActiveBarLayout.setVerticalGroup(
            quatationsActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        reportsActiveBar.setBackground(new java.awt.Color(0, 105, 75));
        reportsActiveBar.setPreferredSize(new java.awt.Dimension(4, 40));

        javax.swing.GroupLayout reportsActiveBarLayout = new javax.swing.GroupLayout(reportsActiveBar);
        reportsActiveBar.setLayout(reportsActiveBarLayout);
        reportsActiveBarLayout.setHorizontalGroup(
            reportsActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        reportsActiveBarLayout.setVerticalGroup(
            reportsActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jSeparator2.setBackground(new java.awt.Color(0, 105, 75));
        jSeparator2.setForeground(new java.awt.Color(0, 105, 75));

        menuChangeButton.setBackground(new java.awt.Color(255, 255, 255));
        menuChangeButton.setForeground(new java.awt.Color(0, 0, 0));
        menuChangeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/menu.png"))); // NOI18N
        menuChangeButton.setToolTipText("Maximize Menu");
        menuChangeButton.setBorder(null);
        menuChangeButton.setBorderPainted(false);
        menuChangeButton.setMaximumSize(new java.awt.Dimension(30, 30));
        menuChangeButton.setMinimumSize(new java.awt.Dimension(30, 30));
        menuChangeButton.setPreferredSize(new java.awt.Dimension(30, 30));
        menuChangeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuChangeButtonActionPerformed(evt);
            }
        });

        dashboardButton.setBackground(new java.awt.Color(255, 255, 255));
        dashboardButton.setForeground(new java.awt.Color(51, 51, 51));
        dashboardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/dashboard.png"))); // NOI18N
        dashboardButton.setText("Dashboard");
        dashboardButton.setToolTipText("Dashboard");
        dashboardButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        dashboardButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dashboardButton.setIconTextGap(15);
        dashboardButton.setPreferredSize(new java.awt.Dimension(228, 40));
        dashboardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardButtonActionPerformed(evt);
            }
        });

        productManagementButton.setBackground(new java.awt.Color(255, 255, 255));
        productManagementButton.setForeground(new java.awt.Color(51, 51, 51));
        productManagementButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/product.png"))); // NOI18N
        productManagementButton.setText("Product Management");
        productManagementButton.setToolTipText("Product Management");
        productManagementButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        productManagementButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        productManagementButton.setIconTextGap(15);
        productManagementButton.setPreferredSize(new java.awt.Dimension(228, 40));
        productManagementButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productManagementButtonActionPerformed(evt);
            }
        });

        stockManagementButton.setBackground(new java.awt.Color(255, 255, 255));
        stockManagementButton.setForeground(new java.awt.Color(51, 51, 51));
        stockManagementButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/stock.png"))); // NOI18N
        stockManagementButton.setText("Stock Management");
        stockManagementButton.setToolTipText("Stock Management");
        stockManagementButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        stockManagementButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        stockManagementButton.setIconTextGap(15);
        stockManagementButton.setPreferredSize(new java.awt.Dimension(228, 40));
        stockManagementButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockManagementButtonActionPerformed(evt);
            }
        });

        grnManagementButton.setBackground(new java.awt.Color(255, 255, 255));
        grnManagementButton.setForeground(new java.awt.Color(51, 51, 51));
        grnManagementButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/grn.png"))); // NOI18N
        grnManagementButton.setText("GRN Management");
        grnManagementButton.setToolTipText("GRN Management");
        grnManagementButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        grnManagementButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        grnManagementButton.setIconTextGap(15);
        grnManagementButton.setPreferredSize(new java.awt.Dimension(228, 40));
        grnManagementButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grnManagementButtonActionPerformed(evt);
            }
        });

        invoiceManagementButton.setBackground(new java.awt.Color(255, 255, 255));
        invoiceManagementButton.setForeground(new java.awt.Color(51, 51, 51));
        invoiceManagementButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/invoice.png"))); // NOI18N
        invoiceManagementButton.setText("Invoice Management");
        invoiceManagementButton.setToolTipText("Invoice Management");
        invoiceManagementButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        invoiceManagementButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        invoiceManagementButton.setIconTextGap(15);
        invoiceManagementButton.setPreferredSize(new java.awt.Dimension(228, 40));
        invoiceManagementButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceManagementButtonActionPerformed(evt);
            }
        });

        returnManagementButton.setBackground(new java.awt.Color(255, 255, 255));
        returnManagementButton.setForeground(new java.awt.Color(51, 51, 51));
        returnManagementButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/return.png"))); // NOI18N
        returnManagementButton.setText("Return Management");
        returnManagementButton.setToolTipText("Return Management");
        returnManagementButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        returnManagementButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        returnManagementButton.setIconTextGap(15);
        returnManagementButton.setPreferredSize(new java.awt.Dimension(228, 40));
        returnManagementButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnManagementButtonActionPerformed(evt);
            }
        });

        quatationsButton.setBackground(new java.awt.Color(255, 255, 255));
        quatationsButton.setForeground(new java.awt.Color(51, 51, 51));
        quatationsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/quatation.png"))); // NOI18N
        quatationsButton.setText("Quatations");
        quatationsButton.setToolTipText("Quatations");
        quatationsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        quatationsButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        quatationsButton.setIconTextGap(15);
        quatationsButton.setPreferredSize(new java.awt.Dimension(228, 40));
        quatationsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quatationsButtonActionPerformed(evt);
            }
        });

        reportsButton.setBackground(new java.awt.Color(255, 255, 255));
        reportsButton.setForeground(new java.awt.Color(51, 51, 51));
        reportsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/report.png"))); // NOI18N
        reportsButton.setText("Reports");
        reportsButton.setToolTipText("Reports");
        reportsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        reportsButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        reportsButton.setIconTextGap(15);
        reportsButton.setPreferredSize(new java.awt.Dimension(228, 40));
        reportsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportsButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Shortcuts");

        quatationButton2.setBackground(new java.awt.Color(255, 255, 255));
        quatationButton2.setForeground(new java.awt.Color(51, 51, 51));
        quatationButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/plus.png"))); // NOI18N
        quatationButton2.setText("New GRN");
        quatationButton2.setToolTipText("Dashboard");
        quatationButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 14, 0, 0));
        quatationButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        quatationButton2.setIconTextGap(15);
        quatationButton2.setPreferredSize(new java.awt.Dimension(228, 40));

        quatationButton3.setBackground(new java.awt.Color(255, 255, 255));
        quatationButton3.setForeground(new java.awt.Color(51, 51, 51));
        quatationButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/plus.png"))); // NOI18N
        quatationButton3.setText("New Invoice");
        quatationButton3.setToolTipText("Dashboard");
        quatationButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 14, 0, 0));
        quatationButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        quatationButton3.setIconTextGap(15);
        quatationButton3.setPreferredSize(new java.awt.Dimension(228, 40));

        quatationButton4.setBackground(new java.awt.Color(255, 255, 255));
        quatationButton4.setForeground(new java.awt.Color(51, 51, 51));
        quatationButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/plus.png"))); // NOI18N
        quatationButton4.setText("Get Sales Report");
        quatationButton4.setToolTipText("Dashboard");
        quatationButton4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 14, 0, 0));
        quatationButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        quatationButton4.setIconTextGap(15);
        quatationButton4.setPreferredSize(new java.awt.Dimension(228, 40));

        userManagementActiveBar.setBackground(new java.awt.Color(0, 105, 75));
        userManagementActiveBar.setPreferredSize(new java.awt.Dimension(4, 40));

        javax.swing.GroupLayout userManagementActiveBarLayout = new javax.swing.GroupLayout(userManagementActiveBar);
        userManagementActiveBar.setLayout(userManagementActiveBarLayout);
        userManagementActiveBarLayout.setHorizontalGroup(
            userManagementActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        userManagementActiveBarLayout.setVerticalGroup(
            userManagementActiveBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        userManagementButton.setBackground(new java.awt.Color(255, 255, 255));
        userManagementButton.setForeground(new java.awt.Color(51, 51, 51));
        userManagementButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/user.png"))); // NOI18N
        userManagementButton.setText("User Management");
        userManagementButton.setToolTipText("User Management");
        userManagementButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        userManagementButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        userManagementButton.setIconTextGap(15);
        userManagementButton.setPreferredSize(new java.awt.Dimension(228, 40));
        userManagementButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userManagementButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout NavigationPanelLayout = new javax.swing.GroupLayout(NavigationPanel);
        NavigationPanel.setLayout(NavigationPanelLayout);
        NavigationPanelLayout.setHorizontalGroup(
            NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(NavigationPanelLayout.createSequentialGroup()
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(menuChangeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addComponent(dashboardActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(dashboardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addComponent(productManagementActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(productManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addComponent(stockManagementActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(stockManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addComponent(grnManagementActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(grnManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addComponent(invoiceManagementActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(invoiceManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addComponent(returnManagementActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(returnManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addComponent(quatationsActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(quatationsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addComponent(reportsActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(reportsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(quatationButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quatationButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quatationButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addComponent(userManagementActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(userManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        NavigationPanelLayout.setVerticalGroup(
            NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationPanelLayout.createSequentialGroup()
                .addComponent(menuChangeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dashboardActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dashboardButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userManagementActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productManagementActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(productManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stockManagementActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stockManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(grnManagementActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grnManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(invoiceManagementActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(invoiceManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(returnManagementActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(returnManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(quatationsActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quatationsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reportsActiveBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reportsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(quatationButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(quatationButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(quatationButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(NavigationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(NavigationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuChangeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuChangeButtonActionPerformed
        // TODO add your handling code here:
        changeMenu();
    }//GEN-LAST:event_menuChangeButtonActionPerformed

    private void dashboardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardButtonActionPerformed
        loadPanel(new Dashboard(), dashboardButton, dashboardActiveBar);
    }//GEN-LAST:event_dashboardButtonActionPerformed

    private void productManagementButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productManagementButtonActionPerformed
        loadPanel(new ProductManagement(this), productManagementButton, productManagementActiveBar);
    }//GEN-LAST:event_productManagementButtonActionPerformed

    private void stockManagementButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockManagementButtonActionPerformed
        loadPanel(new StockManagement(this), stockManagementButton, stockManagementActiveBar);
    }//GEN-LAST:event_stockManagementButtonActionPerformed

    private void grnManagementButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grnManagementButtonActionPerformed
        loadPanel(new GrnManagement(this), grnManagementButton, grnManagementActiveBar);
    }//GEN-LAST:event_grnManagementButtonActionPerformed

    private void invoiceManagementButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceManagementButtonActionPerformed
        loadPanel(new InvoiceManagement(this), invoiceManagementButton, invoiceManagementActiveBar);
    }//GEN-LAST:event_invoiceManagementButtonActionPerformed

    private void returnManagementButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnManagementButtonActionPerformed
        loadPanel(new ReturnManagement(this), returnManagementButton, returnManagementActiveBar);
    }//GEN-LAST:event_returnManagementButtonActionPerformed

    private void quatationsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quatationsButtonActionPerformed
        loadPanel(new Quatations(), quatationsButton, quatationsActiveBar);
    }//GEN-LAST:event_quatationsButtonActionPerformed

    private void reportsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportsButtonActionPerformed
        loadPanel(new Reports(), reportsButton, reportsActiveBar);
    }//GEN-LAST:event_reportsButtonActionPerformed

    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        // TODO add your handling code here:
        Settings settings = new Settings(this);
        this.setEnabled(false);
        settings.setVisible(true);
    }//GEN-LAST:event_settingsButtonActionPerformed

    private void notoficationsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notoficationsButtonActionPerformed
        Notifications notifications = new Notifications(this);
        this.setEnabled(false);
        notifications.setVisible(true);
    }//GEN-LAST:event_notoficationsButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Signin signin = new Signin();
        signin.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:\

    }//GEN-LAST:event_jButton4ActionPerformed

    private void userManagementButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userManagementButtonActionPerformed
        loadPanel(new UserManagement(this), userManagementButton, userManagementActiveBar);
    }//GEN-LAST:event_userManagementButtonActionPerformed

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
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MainPanel;
    private javax.swing.JPanel NavigationPanel;
    private javax.swing.JPanel dashboardActiveBar;
    private javax.swing.JButton dashboardButton;
    private javax.swing.JPanel grnManagementActiveBar;
    private javax.swing.JButton grnManagementButton;
    private javax.swing.JPanel invoiceManagementActiveBar;
    private javax.swing.JButton invoiceManagementButton;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton menuChangeButton;
    private javax.swing.JButton notoficationsButton;
    private javax.swing.JPanel productManagementActiveBar;
    private javax.swing.JButton productManagementButton;
    private javax.swing.JButton quatationButton2;
    private javax.swing.JButton quatationButton3;
    private javax.swing.JButton quatationButton4;
    private javax.swing.JPanel quatationsActiveBar;
    private javax.swing.JButton quatationsButton;
    private javax.swing.JPanel reportsActiveBar;
    private javax.swing.JButton reportsButton;
    private javax.swing.JPanel returnManagementActiveBar;
    private javax.swing.JButton returnManagementButton;
    private javax.swing.JButton settingsButton;
    private javax.swing.JPanel stockManagementActiveBar;
    private javax.swing.JButton stockManagementButton;
    private javax.swing.JPanel userManagementActiveBar;
    private javax.swing.JButton userManagementButton;
    // End of variables declaration//GEN-END:variables
}
