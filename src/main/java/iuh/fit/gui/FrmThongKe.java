/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.gui;

//import dao.DAO_Sach;
//import dao.DAO_ThongKe;
//import dao.DAO_VanPhongPham;
//import entity.MonthlyRevenueInfo;
//import entity.ProductInfo;
//import entity.Sach;
//import entity.VanPhongPham;
//import groovyjarjarcommonscli.ParseException;
import lookup.LookupNaming;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.CategoryPlot;
//import org.jfree.chart.plot.PiePlot;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.chart.plot.XYPlot;
//import org.jfree.chart.renderer.category.BarRenderer;
//import org.jfree.chart.renderer.category.LineAndShapeRenderer;
//import org.jfree.data.category.DefaultCategoryDataset;
//import org.jfree.data.general.DefaultPieDataset;
//import org.jfree.data.statistics.HistogramDataset;

public class FrmThongKe extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6504346945575867108L;
	DecimalFormat deciFormat = new DecimalFormat("###.###");
    /**
     * Creates new form FrmDSKhachHang
     */
    private DAO_VanPhongPham dao_vpp = LookupNaming.lookup_VanPhongPham();
    private DAO_Sach dao_sach = LookupNaming.lookup_Sach();
    private DAO_ThongKe dao_thongKe = LookupNaming.lookup_ThongKe();

    private FrmChinh frm = new FrmChinh();
    private Thread thread = null;

    public FrmThongKe() throws RemoteException {
//        ConnectDB.getInstance().connect();
        initComponents();
        showPieChart();
        showLineChart();
        thongKeToanBo();
        ganNamVaoCbo();

        thread = new Thread(this::setTimeAuto);
        thread.start();

    }

    public void showPanelChange(JPanel a, JPanel b) {
        a.removeAll();
        a.add(b);
        a.repaint();
        a.revalidate();
    }

    public void setTimeAuto() {
        try {
            Date thoiGianHienTai = new Date();
            SimpleDateFormat sdf_Gio = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sdf_Ngay = new SimpleDateFormat("dd/MM/yyyy");
            while (true) {
                thoiGianHienTai = new Date(); // lấy thời gian hiện tại
                String ngayTrongTuan = "";
                if (thoiGianHienTai.getDay() == 0) {
                    ngayTrongTuan = "Chủ nhật, ";
                } else {
                    ngayTrongTuan = "Thứ " + (thoiGianHienTai.getDay() + 1) + ", ";// do getDay() tính từ 1.
                }
                thread.sleep(1);
                // lấy thời gian hiện tại vào giờ vào
                date.setText(
                        sdf_Gio.format(thoiGianHienTai) + " " + ngayTrongTuan
                        + sdf_Ngay.format(thoiGianHienTai)
                );

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void showPieChart() throws RemoteException {
        DefaultPieDataset barDataset = new DefaultPieDataset();
        
        List<ProductInfo> topProducts = dao_thongKe.getTopSellingProducts();
        for (ProductInfo product : topProducts) {
            if(product.getProductId().startsWith("S")) {
                Sach s = dao_sach.getSachtheoMa(product.getProductId());
                barDataset.setValue(s.getTenSanPham(), Double.valueOf(product.getTotalQuantity())); 
            } else {
                VanPhongPham vpp = dao_vpp.getVPPtheoMa(product.getProductId());
                barDataset.setValue(vpp.getTenSanPham(), Double.valueOf(product.getTotalQuantity()));
            }
        } 
        //create chart
        JFreeChart piechart = ChartFactory.createPieChart("Sản phẩm bán chạy", barDataset, false, true, false);//explain

        PiePlot piePlot = (PiePlot) piechart.getPlot();

        //changing pie chart blocks colors
//       piePlot.setSectionPaint("IPhone 5s", new Color(255,255,102));
//       piePlot.setSectionPaint("SamSung Grand", new Color(102,255,102));
//       piePlot.setSectionPaint("MotoG", new Color(255,102,153));
//       piePlot.setSectionPaint("Nokia Lumia", new Color(0,204,204));
        piePlot.setBackgroundPaint(Color.white);

        //create chartPanel to display chart(graph)
        ChartPanel barChartPanel = new ChartPanel(piechart);
        pnl1.removeAll();
        pnl1.add(barChartPanel, BorderLayout.CENTER);
        pnl1.validate();
    }

    /*=============================================================================*/
    public void showLineChart() throws RemoteException {
        //create dataset for the graph
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<MonthlyRevenueInfo> monthlyRevenueList = dao_thongKe.tongTienTheoThang();
        // In thông tin từ danh sách
        for (MonthlyRevenueInfo info : monthlyRevenueList) {
//            System.out.println("Month " + info.getMonth() + ": " + info.getTotalRevenue());
            dataset.setValue(info.getTotalRevenue(), "Amount", info.getMonth() + "");
        }
        
        
        


        //create chart
        JFreeChart linechart = ChartFactory.createLineChart("Doanh thu trong năm", "Tháng", "",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        //create plot object
        CategoryPlot lineCategoryPlot = linechart.getCategoryPlot();
        // lineCategoryPlot.setRangeGridlinePaint(Color.BLUE);
        lineCategoryPlot.setBackgroundPaint(Color.white);

        //create render object to change the moficy the line properties like color
        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) lineCategoryPlot.getRenderer();
        Color lineChartColor = new Color(204, 0, 51);
        lineRenderer.setSeriesPaint(0, lineChartColor);

        //create chartPanel to display chart(graph)
        ChartPanel lineChartPanel = new ChartPanel(linechart);
        pnl2.removeAll();
        pnl2.add(lineChartPanel, BorderLayout.CENTER);
        pnl2.validate();
    }

    /*========================================================================================*/
    public void showHistogram() {

        double[] values = {95, 49, 14, 59, 50, 66, 47, 40, 1, 67,
            12, 58, 28, 63, 14, 9, 31, 17, 94, 71,
            49, 64, 73, 97, 15, 63, 10, 12, 31, 62,
            93, 49, 74, 90, 59, 14, 15, 88, 26, 57,
            77, 44, 58, 91, 10, 67, 57, 19, 88, 84
        };

        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", values, 20);

        JFreeChart chart = ChartFactory.createHistogram("JFreeChart Histogram", "Data", "Frequency", dataset, PlotOrientation.VERTICAL, false, true, false);
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);

        ChartPanel barpChartPanel2 = new ChartPanel(chart);
//        pnl3.removeAll();
//        pnl3.add(barpChartPanel2, BorderLayout.CENTER);
//        pnl3.validate();
    }

    /*========================================================================================*/
    public void showBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(20000000, "Amount", "january");
        dataset.setValue(1500000, "Amount", "february");
        dataset.setValue(18000000, "Amount", "march");
        dataset.setValue(100000000, "Amount", "april");
        dataset.setValue(80000000, "Amount", "may");
        dataset.setValue(25000000, "Amount", "june");

        JFreeChart chart = ChartFactory.createBarChart("Doanh thu theo tháng", "Tháng", "Tiền",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        CategoryPlot categoryPlot = chart.getCategoryPlot();
        //categoryPlot.setRangeGridlinePaint(Color.BLUE);
        categoryPlot.setBackgroundPaint(Color.WHITE);
        BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
        Color clr3 = new Color(204, 0, 51);
        renderer.setSeriesPaint(0, clr3);

        ChartPanel barpChartPanel = new ChartPanel(chart);
//        pnl4.removeAll();
//        pnl4.add(barpChartPanel, BorderLayout.CENTER);
//        pnl4.validate();

    }
    
  //hàm thống kê theo ngày 
    private void thongKeTheoNgay() throws RemoteException {
        try {
            
            String ngayDau = cboSortTabelChonSP.getSelectedItem().toString();
            String ngayCuoi= cboSortTabelChonSP3.getSelectedItem().toString();
            String thang = cboSortTabelChonSP2.getSelectedItem().toString();
            String nam = cboSortTabelChonSP1.getSelectedItem().toString();
            
            String ngayBatDau= ngayDau + "/" + thang + "/" + nam;
            String ngayKetThuc= ngayCuoi + "/" + thang + "/" + nam;
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateBatDau = sdf.parse(ngayBatDau.trim());
            Date dateKetThuc = sdf.parse(ngayKetThuc.trim());
//            System.out.println("Ngay bat đau: "+ngayBatDau);
//            System.out.println("Ngay ket thuc :"+ngayKetThuc);

            // Thực hiện thống kê và cập nhật Label
            double tongDoanhThu = dao_thongKe.thongKeDoanhThu(dateBatDau, dateKetThuc);
            lblTongDoanhThu.setText(deciFormat.format(tongDoanhThu));
            
            int tongHoaDonDaBan = dao_thongKe.thongKeSoHoaDon(dateBatDau, dateKetThuc);
            lblTongHoaDon.setText(tongHoaDonDaBan+"");
            
            lblTongHoaDonTra.setText(dao_thongKe.thongKeSoHoaDonHoanTra(dateBatDau, dateKetThuc)+"");
            
            int soSanPhamDaBan = dao_thongKe.thongKeSoLuongSanPhamDaBan(dateBatDau, dateKetThuc);
            lblTongSanPhamBan.setText(soSanPhamDaBan+"");
            
        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ");
            ex.printStackTrace();
        }
        jRadioButton3.setSelected(true);
    }

    
    //Hàm THống kê theo tháng
    private void thongKeTheoThang() throws RemoteException {
        String thangBatDau = cboSortTabelChonSP7.getSelectedItem().toString();
        String thangKetThuc = cboSortTabelChonSP8.getSelectedItem().toString();
        String nam = cboSortTabelChonSP6.getSelectedItem().toString();

        String monthBD = thangBatDau + "-" + nam;
        String monthKT = thangKetThuc + "-" + nam;

        SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
        Calendar calendarBD = Calendar.getInstance();
		try {
			calendarBD.setTime(sdf.parse(monthBD));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		Calendar calendarKT = Calendar.getInstance();
		try {
			calendarKT.setTime(sdf.parse(monthKT));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		// Đặt ngày cuối cùng của tháng cho ngày kết thúc
		calendarKT.set(Calendar.DAY_OF_MONTH, calendarKT.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date thangNamBD = calendarBD.getTime();
		Date thangNamKT = calendarKT.getTime();

		 double tongDoanhThu = dao_thongKe.thongKeDoanhThu(thangNamBD, thangNamKT);
         lblTongDoanhThu.setText(deciFormat.format(tongDoanhThu));
         lblTongHoaDon.setText(dao_thongKe.thongKeSoHoaDon(thangNamBD, thangNamKT)+"");
         lblTongHoaDonTra.setText(dao_thongKe.thongKeSoHoaDonHoanTra(thangNamBD, thangNamKT)+"");
         lblTongSanPhamBan.setText(dao_thongKe.thongKeSoLuongSanPhamDaBan(thangNamBD, thangNamKT)+"");
         jRadioButton1.setSelected(true);
    }

  //Hàm THống kê theo năm
    private void thongKeTheoNam() throws RemoteException {
    	String bd = cboSortTabelChonSP9.getSelectedItem().toString();
    	String kt = cboSortTabelChonSP10.getSelectedItem().toString();
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    	try {
			Date namBD =sdf.parse(bd);
			Date namKT = sdf.parse(kt);
			Calendar cal = Calendar.getInstance();
			cal.setTime(namKT);
			cal.set(Calendar.MONTH, Calendar.DECEMBER);
			cal.set(Calendar.DAY_OF_MONTH, 31);
			namKT = cal.getTime();
//			System.out.println(namBD +"\n"+namKT);
			
			double tongDoanhThu = dao_thongKe.thongKeDoanhThu(namBD, namKT);
	         lblTongDoanhThu.setText(deciFormat.format(tongDoanhThu));
	         lblTongHoaDon.setText(dao_thongKe.thongKeSoHoaDon(namBD, namKT)+"");
	         lblTongHoaDonTra.setText(dao_thongKe.thongKeSoHoaDonHoanTra(namBD, namKT)+"");
	         lblTongSanPhamBan.setText(dao_thongKe.thongKeSoLuongSanPhamDaBan(namBD, namKT)+"");
			
			
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	jRadioButton2.setSelected(true);
    }
    
    private void thongKeToanBo() throws RemoteException
    {
    	lblTongDoanhThu.setText(deciFormat.format(dao_thongKe.ThongKeTongDoanhThu()));
        lblTongHoaDon.setText(dao_thongKe.thongKeTongSoHoaDon()+"");
        lblTongHoaDonTra.setText(dao_thongKe.thongKeTongSoHoaDonHoanTra()+"");
        lblTongSanPhamBan.setText(dao_thongKe.thongKeTongSoLuongSanPhamDaBan() +"");
        jRadioButton4.setSelected(true);
    }
    
    
     public void ganNamVaoCbo() throws RemoteException {
        List<Integer> years = dao_thongKe.getDistinctYears();

        for (int year : years) {
            cboSortTabelChonSP1.addItem(String.valueOf(year));
            cboSortTabelChonSP6.addItem(String.valueOf(year));
            cboSortTabelChonSP9.addItem(String.valueOf(year));
            cboSortTabelChonSP10.addItem(String.valueOf(year));
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        pnlChange = new JPanel();
        pnlCenter = new JPanel();
        jPanel2 = new JPanel();
        menuScrollPane11 = new menuGui.MenuScrollPane();
        jPanel8 = new JPanel();
        pnlTopLeft = new JPanel();
        jPanel12 = new JPanel();
        jPanel13 = new JPanel();
        jPanel1 = new JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblTongHoaDon = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        cboSortTabelChonSP = new javax.swing.JComboBox<>();
        jPanel5 = new JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblTongHoaDonTra = new javax.swing.JLabel();
        jPanel11 = new JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblTongDoanhThu = new javax.swing.JLabel();
        jPanel15 = new JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblTongSanPhamBan = new javax.swing.JLabel();
        cboSortTabelChonSP1 = new javax.swing.JComboBox<>();
        cboSortTabelChonSP2 = new javax.swing.JComboBox<>();
        cboSortTabelChonSP3 = new javax.swing.JComboBox<>();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jLabel25 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        cboSortTabelChonSP6 = new javax.swing.JComboBox<>();
        cboSortTabelChonSP7 = new javax.swing.JComboBox<>();
        cboSortTabelChonSP8 = new javax.swing.JComboBox<>();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        cboSortTabelChonSP9 = new javax.swing.JComboBox<>();
        cboSortTabelChonSP10 = new javax.swing.JComboBox<>();
        pnlCenterKHchange = new JPanel();
        pnlInit = new JPanel();
        pnl1 = new JPanel();
        pnl2 = new JPanel();
        jPanel3 = new JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblNameLogin = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();

        setBackground(new Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1690, 787));
        setLayout(new BorderLayout());

        pnlChange.setLayout(new BorderLayout());

        pnlCenter.setBackground(new Color(153, 153, 153));
        pnlCenter.setLayout(new BorderLayout());

        jPanel2.setBackground(new Color(153, 153, 153));
        jPanel2.setLayout(new BorderLayout());

        menuScrollPane11.setBorder(null);
        menuScrollPane11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jPanel8.setBackground(new Color(240, 242, 245));

        pnlTopLeft.setBackground(new Color(204, 204, 255));

        jPanel12.setBackground(new Color(255, 255, 255));

        jPanel13.setBackground(new Color(255, 255, 255));

        jPanel1.setBackground(new Color(153, 255, 200));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/bill.png"))); // NOI18N

        lblTongHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTongHoaDon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTongHoaDon.setText("1000");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Tổng hóa đơn đã bán");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTongHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(35, 35, 35))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel24)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(lblTongHoaDon)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        cboSortTabelChonSP.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cboSortTabelChonSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        cboSortTabelChonSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cboSortTabelChonSPActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new Color(255, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Tổng hóa đơn trả hàng");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/exchange.png"))); // NOI18N

        lblTongHoaDonTra.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTongHoaDonTra.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTongHoaDonTra.setText("1000");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTongHoaDonTra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(35, 35, 35))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel16)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(lblTongHoaDonTra)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new Color(255, 255, 204));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Tổng doanh thu");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/coin.png"))); // NOI18N

        lblTongDoanhThu.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTongDoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTongDoanhThu.setText("1000");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTongDoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(35, 35, 35))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addGap(71, 71, 71))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(lblTongDoanhThu)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.setBackground(new Color(153, 204, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Tổng sản phẩm bán");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/refund.png"))); // NOI18N

        lblTongSanPhamBan.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTongSanPhamBan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTongSanPhamBan.setText("1000");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTongSanPhamBan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(35, 35, 35))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(lblTongSanPhamBan)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        cboSortTabelChonSP1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cboSortTabelChonSP1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2021", "2022" }));
        cboSortTabelChonSP1.setToolTipText("");
        cboSortTabelChonSP1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cboSortTabelChonSP1ActionPerformed(evt);
            }
        });

        cboSortTabelChonSP2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cboSortTabelChonSP2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cboSortTabelChonSP2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cboSortTabelChonSP2ActionPerformed(evt);
            }
        });

        cboSortTabelChonSP3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cboSortTabelChonSP3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        cboSortTabelChonSP3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cboSortTabelChonSP3ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jRadioButton1.setText("Thống kê theo tháng:");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jRadioButton2.setText("Thống kê theo năm:");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jRadioButton3.setText("Thống kê theo ngày:");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jRadioButton4.setSelected(true);
        jRadioButton4.setText("Toàn bộ");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Từ ngày:");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Đến ngày:");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Năm:");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Tháng:");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Từ tháng:");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Đến tháng:");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Năm:");

        cboSortTabelChonSP6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cboSortTabelChonSP6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2021", "2022" }));
        cboSortTabelChonSP6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cboSortTabelChonSP6ActionPerformed(evt);
            }
        });

        cboSortTabelChonSP7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cboSortTabelChonSP7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cboSortTabelChonSP7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cboSortTabelChonSP7ActionPerformed(evt);
            }
        });

        cboSortTabelChonSP8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cboSortTabelChonSP8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cboSortTabelChonSP8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cboSortTabelChonSP8ActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Từ năm:");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Đến năm:");

        cboSortTabelChonSP9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cboSortTabelChonSP9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2021", "2022" }));
        cboSortTabelChonSP9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cboSortTabelChonSP9ActionPerformed(evt);
            }
        });

        cboSortTabelChonSP10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cboSortTabelChonSP10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2021", "2022" }));
        cboSortTabelChonSP10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cboSortTabelChonSP10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jRadioButton3)
                                .addGap(54, 54, 54)
                                .addComponent(jLabel25))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(cboSortTabelChonSP, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel27)
                                        .addGap(18, 18, 18)
                                        .addComponent(cboSortTabelChonSP3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel29)
                                        .addGap(18, 18, 18))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(cboSortTabelChonSP7, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cboSortTabelChonSP9, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel13Layout.createSequentialGroup()
                                                .addComponent(jLabel34)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cboSortTabelChonSP10, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel13Layout.createSequentialGroup()
                                                .addComponent(jLabel31)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(cboSortTabelChonSP8, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGap(184, 184, 184)
                                        .addComponent(jLabel32)
                                        .addGap(18, 18, 18)
                                        .addComponent(cboSortTabelChonSP6, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(cboSortTabelChonSP2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(91, 91, 91)
                                        .addComponent(jLabel28)
                                        .addGap(18, 18, 18)
                                        .addComponent(cboSortTabelChonSP1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(109, 109, 109))))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jRadioButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(47, 47, 47)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(jLabel30))
                        .addContainerGap())))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboSortTabelChonSP, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSortTabelChonSP3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSortTabelChonSP2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSortTabelChonSP1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSortTabelChonSP6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSortTabelChonSP8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSortTabelChonSP7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSortTabelChonSP9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSortTabelChonSP10, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jRadioButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pnlCenterKHchange.setBackground(new Color(250, 250, 250));

        pnlInit.setBackground(new Color(250, 250, 250));
        pnlInit.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pnl1.setPreferredSize(new java.awt.Dimension(420, 310));
        pnl1.setLayout(new BorderLayout());

        pnl2.setPreferredSize(new java.awt.Dimension(420, 310));
        pnl2.setLayout(new BorderLayout());

        javax.swing.GroupLayout pnlInitLayout = new javax.swing.GroupLayout(pnlInit);
        pnlInit.setLayout(pnlInitLayout);
        pnlInitLayout.setHorizontalGroup(
            pnlInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInitLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(pnl2, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnl1, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        pnlInitLayout.setVerticalGroup(
            pnlInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInitLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnlInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnl2, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                    .addComponent(pnl1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout pnlCenterKHchangeLayout = new javax.swing.GroupLayout(pnlCenterKHchange);
        pnlCenterKHchange.setLayout(pnlCenterKHchangeLayout);
        pnlCenterKHchangeLayout.setHorizontalGroup(
            pnlCenterKHchangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlInit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        pnlCenterKHchangeLayout.setVerticalGroup(
            pnlCenterKHchangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCenterKHchangeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlInit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCenterKHchange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlTopLeftLayout = new javax.swing.GroupLayout(pnlTopLeft);
        pnlTopLeft.setLayout(pnlTopLeftLayout);
        pnlTopLeftLayout.setHorizontalGroup(
            pnlTopLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        pnlTopLeftLayout.setVerticalGroup(
            pnlTopLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(pnlTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(401, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(pnlTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        menuScrollPane11.setViewportView(jPanel8);

        jPanel2.add(menuScrollPane11, BorderLayout.CENTER);

        pnlCenter.add(jPanel2, BorderLayout.CENTER);

        jPanel3.setBackground(new Color(250, 250, 250));
        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setText("Thống kê doanh thu");

        lblNameLogin.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblNameLogin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameLogin.setText("Nguyễn Châu Tình ");
        lblNameLogin.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblNameLoginAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        date.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/icons8-user-50.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1192, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNameLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblNameLogin)
                        .addGap(2, 2, 2)
                        .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 4, Short.MAX_VALUE))
        );

        pnlCenter.add(jPanel3, BorderLayout.PAGE_START);

        pnlChange.add(pnlCenter, BorderLayout.CENTER);

        add(pnlChange, BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void cboSortTabelChonSPActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cboSortTabelChonSPActionPerformed
        // TODO add your handling code here:
        try {
			thongKeTheoNgay();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        sortTableChooseProDuct();
    }//GEN-LAST:event_cboSortTabelChonSPActionPerformed

    private void cboSortTabelChonSP1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cboSortTabelChonSP1ActionPerformed
        // TODO add your handling code here:
        try {
			thongKeTheoNgay();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_cboSortTabelChonSP1ActionPerformed

    private void cboSortTabelChonSP2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cboSortTabelChonSP2ActionPerformed
        // TODO add your handling code here:
        try {
			thongKeTheoNgay();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_cboSortTabelChonSP2ActionPerformed

    private void cboSortTabelChonSP3ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cboSortTabelChonSP3ActionPerformed
        // TODO add your handling code here:
        try {
			thongKeTheoNgay();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_cboSortTabelChonSP3ActionPerformed

    private void cboSortTabelChonSP6ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cboSortTabelChonSP6ActionPerformed
        // TODO add your handling code here:
        try {
			thongKeTheoThang();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_cboSortTabelChonSP6ActionPerformed

    private void cboSortTabelChonSP7ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cboSortTabelChonSP7ActionPerformed
        // TODO add your handling code here:
        try {
			thongKeTheoThang();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_cboSortTabelChonSP7ActionPerformed

    private void cboSortTabelChonSP8ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cboSortTabelChonSP8ActionPerformed
        // TODO add your handling code here:
        try {
			thongKeTheoThang();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_cboSortTabelChonSP8ActionPerformed

    private void cboSortTabelChonSP9ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cboSortTabelChonSP9ActionPerformed
        // TODO add your handling code here:
        try {
			thongKeTheoNam();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_cboSortTabelChonSP9ActionPerformed

    private void cboSortTabelChonSP10ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cboSortTabelChonSP10ActionPerformed
        // TODO add your handling code here:
        try {
			thongKeTheoNam();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_cboSortTabelChonSP10ActionPerformed

    private void lblNameLoginAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblNameLoginAncestorAdded
        // TODO add your handling code here:
        lblNameLogin.setText(gui.FrmLogin.tenNguoiDung);
    }//GEN-LAST:event_lblNameLoginAncestorAdded

    private void jRadioButton4ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
        try {
			thongKeToanBo();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jRadioButton3ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
        try {
			thongKeTheoNgay();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        try {
			thongKeTheoThang();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        try {
			thongKeTheoNam();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_jRadioButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboSortTabelChonSP;
    private javax.swing.JComboBox<String> cboSortTabelChonSP1;
    private javax.swing.JComboBox<String> cboSortTabelChonSP10;
    private javax.swing.JComboBox<String> cboSortTabelChonSP2;
    private javax.swing.JComboBox<String> cboSortTabelChonSP3;
    private javax.swing.JComboBox<String> cboSortTabelChonSP6;
    private javax.swing.JComboBox<String> cboSortTabelChonSP7;
    private javax.swing.JComboBox<String> cboSortTabelChonSP8;
    private javax.swing.JComboBox<String> cboSortTabelChonSP9;
    private javax.swing.JLabel date;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private JPanel jPanel1;
    private JPanel jPanel11;
    private JPanel jPanel12;
    private JPanel jPanel13;
    private JPanel jPanel15;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel5;
    private JPanel jPanel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JLabel lblNameLogin;
    private javax.swing.JLabel lblTongDoanhThu;
    private javax.swing.JLabel lblTongHoaDon;
    private javax.swing.JLabel lblTongHoaDonTra;
    private javax.swing.JLabel lblTongSanPhamBan;
    private menuGui.MenuScrollPane menuScrollPane11;
    private JPanel pnl1;
    private JPanel pnl2;
    private JPanel pnlCenter;
    private JPanel pnlCenterKHchange;
    private JPanel pnlChange;
    private JPanel pnlInit;
    private JPanel pnlTopLeft;
    // End of variables declaration//GEN-END:variables
}
