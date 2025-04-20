/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.table.DefaultTableModel;

//import dao.DAO_KhachHang;
//import entity.KhachHang;
//import entity.NhomKhachHang;
import iuh.fit.gui.FrmChinh;
import lookup.LookupNaming;

import java.time.Month;

import javax.swing.table.TableRowSorter;

//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class FrmKhachHang extends javax.swing.JPanel {

=
	private static final long serialVersionUID = 6540902861327977992L;

    private FrmChinh frm = new FrmChinh();
    private DAO_KhachHang dao_KhachHang = LookupNaming.lookup_KhachHang();
	private boolean checkTenKH;
	private boolean checkSDT;
	private boolean checkMa= true;
	private Object nhomKhachHangFilter;
    private Thread thread = null; 
        
    public FrmKhachHang() throws SQLException {

//        ConnectDB.getInstance().connect();
        initComponents();
        try {
			loadDataKhachHang();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			txtTimKH11.setText(autoCreateMaKhachHang());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        txtTimKH11.setEnabled(false); // chỉ cho xem mã khách hàng 
//        jComboBox3.setEnabled(false);
        thread = new Thread(this::setTimeAuto);
        thread.start();
        
        //F1 thêm khách hàng
        InputMap inputMap1 = btnThemKH.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap1.put(KeyStroke.getKeyStroke("F1"), "doSomething1");
        Action action1 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	btnThemKHActionPerformed(e);
            }
        };
        btnThemKH.getActionMap().put("doSomething1", action1);
        
        //F2 Sửa khách Hàng
        InputMap inputMap2 = btnSuaKH2.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap2.put(KeyStroke.getKeyStroke("F2"), "doSomething2");

        Action action2 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	btnSuaKH2ActionPerformed(e);
            }
        };
        btnSuaKH2.getActionMap().put("doSomething2", action2);
        
        
     // F3 làm mới
        InputMap inputMap3 = btnSuaKH3.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap3.put(KeyStroke.getKeyStroke("F3"), "doSomething3");

        Action action3 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	btnSuaKH3ActionPerformed(e);
            }
        };
        btnSuaKH3.getActionMap().put("doSomething3", action3);
        
        

            // Su Kien Tim Kiem
        txtTimKH.selectAll();
        txtTimKH.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                locDuLieu();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                locDuLieu();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                locDuLieu();
            }
        });
    }

    public void setTimeAuto(){
        try {
            Date thoiGianHienTai = new Date();
            SimpleDateFormat sdf_Gio = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sdf_Ngay = new SimpleDateFormat("dd/MM/yyyy");
            while (true) {
                    thoiGianHienTai = new Date(); // lấy thời gian hiện tại
                    String ngayTrongTuan = "";
                    if (thoiGianHienTai.getDay() == 0)
                        ngayTrongTuan = "Chủ nhật, ";
                    else
                        ngayTrongTuan = "Thứ " + (thoiGianHienTai.getDay() + 1) + ", ";// do getDay() tính từ 1.
                    thread.sleep(1); 
                    // lấy thời gian hiện tại vào giờ vào
                    date.setText(
                            sdf_Gio.format(thoiGianHienTai) + " " + ngayTrongTuan +
                            sdf_Ngay.format(thoiGianHienTai)
                            );

            } 

        } catch (InterruptedException e) {
                e.printStackTrace();
        }
    }
    
    public void loadDataKhachHang() throws RemoteException {
        deleteTable();
        dao_KhachHang.capNhatNhomKhachHang();
        List<KhachHang> dsKhachHang = dao_KhachHang.getAllKhachHang_20();
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        for (int i = 0; i < dsKhachHang.size(); i++) {
            KhachHang kh = dsKhachHang.get(i);
            if (!kh.getTenKhachHang().isEmpty() && !kh.getSoDienThoai().isEmpty()) {
                model.addRow(new Object[]{
                    i + 1, kh.getMaKhachHang(), kh.getTenKhachHang(), kh.getSoDienThoai(),
                    kh.getNhomKhachHang(), kh.getTongTienMua(), kh.getSoLuongHoaDon()
                });
            }
        }
    }
    

    public void deleteTable() {
        DefaultTableModel dm = (DefaultTableModel) jTable2.getModel();
        dm.getDataVector().removeAllElements();
    }

    private void showPanelChange(JPanel a, JPanel b) {
        a.removeAll();
        a.add(b);
        a.repaint();
        a.revalidate();
    }

    // Hàm tự động tạo mã khách hàng    VD: KH01012023-000001
    public String autoCreateMaKhachHang() throws RemoteException {
//        LocalDate d = LocalDate.of(2023, 11, 9);
        LocalDate d = LocalDate.now();
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("ddMMyyyy");
        String format = d.format(myFormatDate);
        Integer count = 1;
        String cusID = "";

        do {
            String tempID = count.toString().length() == 1 ? ("KH" + format + "-00000" + count)
                    : count.toString().length() == 2 ? ("KH" + format + "-0000" + count)
                    : count.toString().length() == 3 ? ("KH" + format + "-000" + count)
                    : count.toString().length() == 4 ? ("KH" + format + "-00" + count)
                    : count.toString().length() == 5 ? ("KH" + format + "-0" + count)
                    : ("KH" + format + "-" + count);

            KhachHang existingCustomer = dao_KhachHang.getKHTheoMa(tempID);
            if (existingCustomer == null) {
                cusID = tempID;
                break;
            }
            count++;
        } while (true);

        return cusID;
    }
    
    
    
    private void locDuLieu() {
        String dataFind = txtTimKH.getText();
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();

        int stt = 1;
        try {
            model.setRowCount(0);
            jTable2.clearSelection();
            for (KhachHang cus : dao_KhachHang.locDuLieuKhachHang(dataFind)) {
                String[] rowdata = {String.valueOf(stt), cus.getMaKhachHang(),
                    cus.getTenKhachHang(),
                    cus.getSoDienThoai(),
                    String.valueOf(cus.getNhomKhachHang()),
                    String.valueOf(cus.getTongTienMua()),
                    String.valueOf(cus.getSoLuongHoaDon())};

                model.addRow(rowdata);
                stt++;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    // Cập nhật thông tin khách hàng
    public void capNhatKhachHang() throws RemoteException {
        String cusID = txtTimKH11.getText();
        String cusName = txtTimKH3.getText();
        String cusPhone = txtTimKH4.getText();
        String selectedGroup = (String) jComboBox3.getSelectedItem();
        NhomKhachHang cusGR = null;
        if (selectedGroup.equals("Khách bình thường")) {
            cusGR = NhomKhachHang.KHACHBT;
        } else if (selectedGroup.equals("Khách vip")){
            cusGR = NhomKhachHang.KHACHVIP;
        }else
        	cusGR = NhomKhachHang.KHACHLE;
        double cusTotal = Double.parseDouble(txtTongChiTieu1.getText());
        int cusQuantity = Integer.parseInt(txtTongDonHang1.getText());
        KhachHang newCus = new KhachHang(cusID, cusName, cusPhone, cusGR, cusTotal, cusQuantity);
        dao_KhachHang.updateKhachHang(newCus);

    }

    //Hàm Thêm khách hàng.
    public void themKhachHang() throws RemoteException {
    	
        String cusID = txtTimKH11.getText();
        String cusName = txtTimKH3.getText();
        String cusPhone = txtTimKH4.getText();
        String selectedGroup = (String) jComboBox3.getSelectedItem();
        NhomKhachHang cusGR = null;
        if (selectedGroup.equals("Khách bình thường")) {
            cusGR = NhomKhachHang.KHACHBT;
        } else if (selectedGroup.equals("Khách vip")){
            cusGR = NhomKhachHang.KHACHVIP;
        }else
        	cusGR = NhomKhachHang.KHACHLE;
        
        
//        //Test Khách Vip
//        txtTongChiTieu1.setText("500000");
//        txtTongDonHang1.setText("99");
        
         //Mặc định
        txtTongChiTieu1.setText("0");
        txtTongDonHang1.setText("0");
        
        double cusTotal = Double.parseDouble(txtTongChiTieu1.getText());
        int cusQuantity = Integer.parseInt(txtTongDonHang1.getText());
        KhachHang newCus = new KhachHang(cusID, cusName, cusPhone, cusGR, cusTotal, cusQuantity);
        dao_KhachHang.themKhachHang(newCus);

    }

    //Hàm để Xóa rỗng các field
    private void clearn() throws RemoteException {
        txtTimKH11.setText(autoCreateMaKhachHang()); // khi làm mới thì set lại ô mã Khách hàng để chuẩn bị thêm khách hàng 
        txtTimKH3.setText("");
        txtTimKH4.setText("");
        txtTongDonHang1.setText("0");
        txtTongChiTieu1.setText("0");
        jLabel31.setText("(*)");
        jLabel31.setForeground(Color.BLACK);
        jLabel28.setText("(*)");
        jLabel28.setForeground(Color.BLACK);
        jLabel30.setText("(*)");
        jLabel30.setForeground(Color.BLACK);
        checkMa = true;
    }
    //hàm lọc khách hàng theo nhóm
    private void locKhachHangTheoNhom() throws RemoteException {
    	 DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    	    model.setRowCount(0); // Xóa tất cả các hàng hiện có trong bảng

    	    List<KhachHang> dsDaLoc = dao_KhachHang.getAllKhachHang();
    	    int stt = 1;
    	    if (nhomKhachHangFilter != null) {
    	        dsDaLoc = dsDaLoc.stream()
    	                .filter(customer -> customer.getNhomKhachHang() == nhomKhachHangFilter)
    	                .collect(Collectors.toCollection(ArrayList::new));
    	    }

    	    for (KhachHang cus : dsDaLoc) {
    	        Object[] rowData = {
    	        	stt,	
    	            cus.getMaKhachHang(),
    	            cus.getTenKhachHang(),
    	            cus.getSoDienThoai(),
    	            cus.getNhomKhachHang(),
    	            cus.getTongTienMua(),
    	            cus.getSoLuongHoaDon()
    	        };
    	        model.addRow(rowData);
    	        stt++;
    	    }
	}
  //import Danh sách KhachHang- Gui>FrmKhachHang
    public void importExcelKhachHang() {
                  Runnable importTask = new Runnable() {
                @Override
                public void run() {
                    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                    File excelFile;
                    FileInputStream excelFIS = null;
                    BufferedInputStream excelBIS = null;
                    XSSFWorkbook workBook = null;
                    String currentPath = "excel";
                    JFileChooser excelFileChooser = new JFileChooser(currentPath);
                    excelFileChooser.setDialogTitle("Chọn file Excel");
                    FileNameExtensionFilter fnef = new FileNameExtensionFilter("TẤT CẢ CÁC FILE EXCEL", "xls", "xlsx", "xlsm");
                    excelFileChooser.setFileFilter(fnef);
                    int excelChooser = excelFileChooser.showOpenDialog(null);
                    if (excelChooser == JFileChooser.APPROVE_OPTION) {
                        try {
                            excelFile = excelFileChooser.getSelectedFile();
                            excelFIS = new FileInputStream(excelFile);
                            excelBIS = new BufferedInputStream(excelFIS);

                            workBook = new XSSFWorkbook(excelBIS);
                            XSSFSheet excelSheet = workBook.getSheetAt(0);

                            Iterator<Row> rowIterator = excelSheet.iterator();
                            if (rowIterator.hasNext()) {
                                Row head = rowIterator.next();
                                int columnCount = head.getPhysicalNumberOfCells();
                                if (columnCount == 5) {
                                    while (rowIterator.hasNext()) {
                                        Row currentRow = rowIterator.next();
                                        Iterator<Cell> cellIterator = currentRow.cellIterator();
                                        DataFormatter dataFormatter = new DataFormatter();
                                        if (cellIterator.hasNext()) {
                                            String ten = dataFormatter.formatCellValue(cellIterator.next());
                                            String sdt = dataFormatter.formatCellValue(cellIterator.next());
                                            String nhomStr = dataFormatter.formatCellValue(cellIterator.next());
                                            double tongChi = Double.parseDouble(dataFormatter.formatCellValue(cellIterator.next()));
                                            int soLuongHD = Integer.parseInt(dataFormatter.formatCellValue(cellIterator.next()));
                                            NhomKhachHang nhom = NhomKhachHang.valueOf(nhomStr);
                                            
                                            KhachHang khachHang = new KhachHang();
                                            khachHang.setMaKhachHang(autoCreateMaKhachHang());
                                            khachHang.setTenKhachHang(ten);
                                            khachHang.setSoDienThoai(sdt);
                                            khachHang.setNhomKhachHang(nhom);
                                            khachHang.setTongTienMua(tongChi);
                                            khachHang.setSoLuongHoaDon(soLuongHD);

                                            
                                            dao_KhachHang.themKhachHang(khachHang);
													JOptionPane.showMessageDialog(null, "Thêm Khách Hàng Thành Công",
															"Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                                        }
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "File Excel không đúng định dạng cho Khách Hàng.",
                                            "Lỗi Định Dạng", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            workBook.close();

                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "Lỗi Đọc File", JOptionPane.ERROR_MESSAGE);
                        } finally {
                            try {
                                if (excelFIS != null) {
                                    excelFIS.close();
                                }
                                if (excelBIS != null) {
                                    excelBIS.close();
                                }
                                if (workBook != null) {
                                    workBook.close();
                                }
                            } catch (IOException e2) {
                                JOptionPane.showMessageDialog(null, e2.getMessage(), "Lỗi Đóng File", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            };

            Thread importThread = new Thread(importTask);
            importThread.start();
            }
   

    
  //Export Danh sách KhachHang - Gui>FrmKhachHang
    public void exportKhachHangToExcel() {
            Runnable exportTask = new Runnable() {
                @Override
                public void run() {
                    // Mã của hàm exportKhachHangToExcel()
                    Workbook workbook = new XSSFWorkbook();
                    Sheet sheet = workbook.createSheet("DanhSachKhachHang");

                    // Tạo hàng đầu tiên (Header)
                    Row headerRow = sheet.createRow(0);
                    String[] headers = {"STT","Mã Khách Hàng", "Tên Khách Hàng", "Số Điện Thoại", "Nhóm Khách Hàng",
                            "Tổng Chi Tiêu", "Số Lượng Hóa Đơn"};
                    for (int i = 0; i < headers.length; i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellValue(headers[i]);
                    }

                    // Lấy dữ liệu từ JTable và điền vào sheet
                    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                    int rowCount = model.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        Row row = sheet.createRow(i + 1); // Bắt đầu từ dòng thứ 2

                        for (int j = 0; j < model.getColumnCount(); j++) {
                            Cell cell = row.createCell(j);
                            cell.setCellValue(String.valueOf(model.getValueAt(i, j)));
                        }
                    }

                    try {
                        JFileChooser fileChooser = new JFileChooser("excel");
                        fileChooser.setDialogTitle("Chọn nơi lưu và đặt tên file");
                        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xlsx");
                        fileChooser.setFileFilter(filter);

                        int userSelection = fileChooser.showSaveDialog(null);

                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                                filePath += ".xlsx";  // Đảm bảo có đuôi .xlsx
                            }

                            FileOutputStream fileOut = new FileOutputStream(filePath);
                            workbook.write(fileOut);
                            fileOut.close();

                            JOptionPane.showMessageDialog(null, "Xuất file Excel thành công: " + filePath,
                                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Lỗi khi xuất file Excel",
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };

            Thread exportThread = new Thread(exportTask);
            exportThread.start();
        }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlChange = new javax.swing.JPanel();
        pnlCenter = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        pnlTopLeft = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtTimKH = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        pnlCenterKHchange = new javax.swing.JPanel();
        pnlInit = new javax.swing.JPanel();
        menuScrollPane2 = new menuGui.MenuScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel25 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtTimKH11 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtTimKH3 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtTimKH4 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        lblTongChiTieu1 = new javax.swing.JLabel();
        txtTongChiTieu1 = new javax.swing.JTextField();
        lblTongDonHang1 = new javax.swing.JLabel();
        txtTongDonHang1 = new javax.swing.JTextField();
        btnThemKH = new javax.swing.JButton();
        btnSuaKH2 = new javax.swing.JButton();
        btnSuaKH3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnNhapFile = new javax.swing.JLabel();
        btnXuatFile = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        lblNameLogin = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1690, 787));
        setLayout(new java.awt.BorderLayout());

        pnlChange.setLayout(new java.awt.BorderLayout());

        pnlCenter.setBackground(new java.awt.Color(153, 153, 153));
        pnlCenter.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(240, 242, 245));
        jPanel8.setPreferredSize(new java.awt.Dimension(1291, 843));

        pnlTopLeft.setBackground(new java.awt.Color(204, 204, 255));
        pnlTopLeft.setPreferredSize(new java.awt.Dimension(1268, 721));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setPreferredSize(new java.awt.Dimension(1268, 756));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setPreferredSize(new java.awt.Dimension(1268, 91));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel5.setText("Thông tin khách hàng");

        txtTimKH.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH.setText("Nhập vào thông tin tìm kiếm...");
        txtTimKH.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimKHFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTimKHFocusLost(evt);
            }
        });
        txtTimKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKHActionPerformed(evt);
            }
        });
        txtTimKH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKHKeyReleased(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Khách bình thường", "Khách vip" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/magnifying-glass.png"))); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimKH, javax.swing.GroupLayout.PREFERRED_SIZE, 953, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(txtTimKH, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        pnlCenterKHchange.setBackground(new java.awt.Color(250, 250, 250));
        pnlCenterKHchange.setPreferredSize(new java.awt.Dimension(1268, 622));
        pnlCenterKHchange.setLayout(new java.awt.BorderLayout());

        pnlInit.setBackground(new java.awt.Color(250, 250, 250));

        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Nhóm khách hàng", "Tổng chi tiêu", "Tổng số lượng đơn hàng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setRowHeight(60);
        jTable2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable2.setShowHorizontalLines(true);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        menuScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        jPanel25.setBackground(new java.awt.Color(250, 250, 250));
        jPanel25.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel25.setPreferredSize(new java.awt.Dimension(1256, 285));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel25.setText("Mã khách hàng");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel31.setText("(*)");
        jLabel31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel31MouseClicked(evt);
            }
        });

        txtTimKH11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH11ActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel26.setText("Tên khách hàng");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel30.setText("(*)");

        txtTimKH3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH3ActionPerformed(evt);
            }
        });
        txtTimKH3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH3KeyReleased(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel24.setText("Số điện thoại");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel28.setText("(*)");

        txtTimKH4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH4ActionPerformed(evt);
            }
        });
        txtTimKH4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH4KeyReleased(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel27.setText("Nhóm khách hàng");

        jComboBox3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Khách bình thường", "Khách vip" }));

        lblTongChiTieu1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblTongChiTieu1.setForeground(new java.awt.Color(3, 136, 253));
        lblTongChiTieu1.setText("Tổng chi tiêu");

        txtTongChiTieu1.setEditable(false);
        txtTongChiTieu1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTongChiTieu1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTongChiTieu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTongChiTieu1ActionPerformed(evt);
            }
        });

        lblTongDonHang1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblTongDonHang1.setForeground(new java.awt.Color(3, 136, 253));
        lblTongDonHang1.setText("Tổng số lượng đơn hàng");

        txtTongDonHang1.setEditable(false);
        txtTongDonHang1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTongDonHang1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTongDonHang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTongDonHang1ActionPerformed(evt);
            }
        });

        btnThemKH.setBackground(new java.awt.Color(3, 136, 253));
        btnThemKH.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemKH.setForeground(new java.awt.Color(255, 255, 255));
        btnThemKH.setText("Thêm khách hàng(F1)");
        btnThemKH.setFocusable(false);
        btnThemKH.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemKH.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnThemKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemKHActionPerformed(evt);
            }
        });

        btnSuaKH2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuaKH2.setForeground(new java.awt.Color(3, 136, 253));
        btnSuaKH2.setText("Sửa khách hàng(F2)");
        btnSuaKH2.setFocusable(false);
        btnSuaKH2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSuaKH2.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnSuaKH2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaKH2ActionPerformed(evt);
            }
        });

        btnSuaKH3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuaKH3.setForeground(new java.awt.Color(3, 136, 253));
        btnSuaKH3.setText("Làm mới(F3)");
        btnSuaKH3.setFocusable(false);
        btnSuaKH3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSuaKH3.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnSuaKH3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaKH3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTimKH4, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTimKH11)
                    .addComponent(txtTimKH3))
                .addGap(84, 84, 84)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, 0, 424, Short.MAX_VALUE)
                    .addComponent(lblTongChiTieu1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTongChiTieu1)
                    .addComponent(lblTongDonHang1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTongDonHang1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnThemKH, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(btnSuaKH2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSuaKH3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(56, 56, 56))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel31)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKH11, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemKH, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel30)
                    .addComponent(lblTongChiTieu1))
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTimKH3, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                            .addComponent(txtTongChiTieu1, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(btnSuaKH2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel28)
                    .addComponent(lblTongDonHang1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTimKH4, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                            .addComponent(txtTongDonHang1, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                        .addGap(48, 48, 48))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                        .addComponent(btnSuaKH3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(45, 45, 45))))
        );

        javax.swing.GroupLayout pnlInitLayout = new javax.swing.GroupLayout(pnlInit);
        pnlInit.setLayout(pnlInitLayout);
        pnlInitLayout.setHorizontalGroup(
            pnlInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInitLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pnlInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(menuScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1257, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlInitLayout.setVerticalGroup(
            pnlInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInitLayout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlCenterKHchange.add(pnlInit, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlCenterKHchange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCenterKHchange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        javax.swing.GroupLayout pnlTopLeftLayout = new javax.swing.GroupLayout(pnlTopLeft);
        pnlTopLeft.setLayout(pnlTopLeftLayout);
        pnlTopLeftLayout.setHorizontalGroup(
            pnlTopLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 1269, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        pnlTopLeftLayout.setVerticalGroup(
            pnlTopLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1268, 40));

        btnNhapFile.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNhapFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/down-arrow.png"))); // NOI18N
        btnNhapFile.setText("Nhập file");
        btnNhapFile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNhapFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNhapFileMouseClicked(evt);
            }
        });

        btnXuatFile.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXuatFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/up-arrow.png"))); // NOI18N
        btnXuatFile.setText("Xuất file");
        btnXuatFile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXuatFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnXuatFileMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNhapFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXuatFile)
                .addContainerGap(1074, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNhapFile)
                    .addComponent(btnXuatFile))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTopLeft, 1269, 1269, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(405, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel8, java.awt.BorderLayout.CENTER);

        pnlCenter.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(250, 250, 250));
        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setText("Khách hàng");

        date.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

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

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/icons8-user-50.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1289, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNameLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel35)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblNameLogin)
                        .addGap(2, 2, 2)
                        .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCenter.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        pnlChange.add(pnlCenter, java.awt.BorderLayout.CENTER);

        add(pnlChange, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    
// -------------------------------------------------------------Sự Kiện nút thêm Khách Hàng -------------------------------------------------------
    private void btnThemKHActionPerformed(java.awt.event.ActionEvent evt) 
    {//GEN-FIRST:event_btnThemKHActionPerformed
    	
    	try {
    		if(!(txtTimKH3.getText().isEmpty()||txtTimKH4.getText().isEmpty()))
        	{
        		if (checkSDT && checkTenKH &&checkMa) {
    	            themKhachHang();
    	            showPanelChange(pnlChange, pnlCenter);
    	            loadDataKhachHang(); 
    	            clearn();
        		}
        	}else
        	{
        		JOptionPane.showMessageDialog(this, "Chưa Nhập Thông Tin Khách Hàng");
        	}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
       
//        showPanelChange(pnlChange, pnlCenterThem);
    }//GEN-LAST:event_btnThemKHActionPerformed

    
    private void txtTimKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKHActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_txtTimKHActionPerformed

    private void btnNhapFileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNhapFileMouseClicked
        // TODO add your handling code here:
    	importExcelKhachHang();
    	try {
			loadDataKhachHang();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_btnNhapFileMouseClicked

    private void btnXuatFileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnXuatFileMouseClicked
        // TODO add your handling code here:
    	exportKhachHangToExcel();
    }//GEN-LAST:event_btnXuatFileMouseClicked

    private void txtTimKHFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKHFocusGained
        // TODO add your handling code here:
        frm.placeHoderTextGianed(txtTimKH);

    }//GEN-LAST:event_txtTimKHFocusGained

    private void txtTimKHFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKHFocusLost
        // TODO add your handling code here:
        frm.placeHoderTextLost(txtTimKH);

        
    }//GEN-LAST:event_txtTimKHFocusLost

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1 && !evt.isConsumed()) {
            evt.consume();
            jComboBox3.setEnabled(false);
//        showPanelChange(pnlChange, pnlCenterSua);
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        String maCUS = model.getValueAt(jTable2.getSelectedRow(), 1).toString();
        KhachHang cus;
		try {
			cus = dao_KhachHang.getKHTheoMa(maCUS);
			txtTimKH11.setText(model.getValueAt(jTable2.getSelectedRow(), 1).toString());
	        txtTimKH3.setText(model.getValueAt(jTable2.getSelectedRow(), 2).toString());
	        txtTimKH4.setText(model.getValueAt(jTable2.getSelectedRow(), 3).toString());

	        if (cus.getNhomKhachHang() == NhomKhachHang.KHACHBT) {
	            jComboBox3.setSelectedItem("Khách bình thường");
	        } else if (cus.getNhomKhachHang() == NhomKhachHang.KHACHVIP) {
	            jComboBox3.setSelectedItem("Khách vip");
	        } else {
	            jComboBox3.setSelectedItem(null);
	        }

	        txtTongChiTieu1.setText(model.getValueAt(jTable2.getSelectedRow(), 5).toString());
	        txtTongDonHang1.setText(model.getValueAt(jTable2.getSelectedRow(), 6).toString());
	        jLabel31.setText("Cần làm mới trước khi thêm");
	        jLabel31.setForeground(Color.RED);
	        checkMa =false; // Mã khách hàng đã tồn tại trong danh sách.
	        checkSDT= true;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void txtTimKH3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH3ActionPerformed

    private void txtTimKH4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH4ActionPerformed

    private void txtTongChiTieu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTongChiTieu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTongChiTieu1ActionPerformed

    private void txtTongDonHang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTongDonHang1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTongDonHang1ActionPerformed

    private void txtTimKH11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH11ActionPerformed

    private void txtTimKHKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKHKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKHKeyReleased
    
    
    
    private void jLabel31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel31MouseClicked
    
  //-------------------------------------------------sự kiện text mã khách hàng-----------------------------------------------------
    private void txtTimKH11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKH11KeyReleased
        // TODO add your handling code here:
        	String maKhachHang = txtTimKH11.getText();
            List<KhachHang> ds;
			try {
				ds = dao_KhachHang.getAllKhachHang();
				for(KhachHang cus: ds )
	            {
	            	if (cus.getMaKhachHang().equals(maKhachHang)) {
	                    jLabel31.setText("Cần làm mới trước khi thêm");
	                    jLabel31.setForeground(Color.RED);
	                    checkMa =false; // Mã khách hàng đã tồn tại trong danh sách.
	                }else {
	                	 jLabel31.setText("(*)");
	                     jLabel31.setForeground(Color.BLACK);
	                	 checkMa = true;
	                }
	            }
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
           
        
    }//GEN-LAST:event_txtTimKH11KeyReleased

    private void lblNameLoginAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblNameLoginAncestorAdded
        // TODO add your handling code here:
        lblNameLogin.setText(gui.FrmLogin.tenNguoiDung);
    }//GEN-LAST:event_lblNameLoginAncestorAdded

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        String selectedOption = (String) jComboBox1.getSelectedItem();
        if ("Tất cả".equals(selectedOption)) {
            nhomKhachHangFilter = null; // Hiển thị toàn bộ danh sách
        } else {
            nhomKhachHangFilter = convertToNhomKhachHang(selectedOption);
        }

        // Gọi hàm để cập nhật hiển thị danh sách khách hàng dựa trên lựa chọn mới
        try {
			locKhachHangTheoNhom();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_jComboBox1ActionPerformed

        // Sửa (F2)
    private void btnSuaKH2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaKH2ActionPerformed
        // TODO add your handling code here:
        if (checkSDT && checkTenKH) {
//          if(true) { 
    		 try {
				capNhatKhachHang();
				 showPanelChange(pnlChange, pnlCenter);
	             loadDataKhachHang();
	             clearn();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
             
    	 }
	
    }//GEN-LAST:event_btnSuaKH2ActionPerformed
        // làm mới (F3)
    private void btnSuaKH3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaKH3ActionPerformed
        // TODO add your handling code here:
            try {
				clearn();
				loadDataKhachHang();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
    }//GEN-LAST:event_btnSuaKH3ActionPerformed
//--------------------------------------------------sự kiện của text Tên KHÁCH HÀNG ------------------------------------------------
    private void txtTimKH3KeyReleased(java.awt.event.KeyEvent evt) {
        String ten = txtTimKH3.getText().trim();
        if (ten.isEmpty()) {
            jLabel30.setText("Tên không được để trống");
            jLabel30.setForeground(Color.RED);
            checkTenKH = false;
        } else if (!ten.matches("^(?!.*[\\d!@#$%^&*()_+={}\\[\\]:;\"'<>,.?/~\\\\|]).{1,50}$")) {
            jLabel30.setText("Không chứa kí tự đặc biệt và số ");
            jLabel30.setForeground(Color.RED);
            checkTenKH = false;
        } else if (!ten.matches("^(?:[A-ZÀ-ỸẠ-Ỵ][a-zà-ỹạ-ỵ]*\\s?)+$")) {
            jLabel30.setText("Viết hoa chữ cái đầu ");
            jLabel30.setForeground(Color.RED);
            checkTenKH = false;
        } else {
            jLabel30.setText("(*)");
            jLabel30.setForeground(Color.BLACK);
            checkTenKH = true;
        }
    }

//--------------------------------------------------sự kiện của text SDT KHÁCH HÀNG ------------------------------------------------
    private void txtTimKH4KeyReleased(java.awt.event.KeyEvent evt) {                                      
    // TODO add your handling code here:
    String sdt = txtTimKH4.getText().trim();

    if (!sdt.isEmpty()) {
        boolean validSDT = sdt.matches("^[0-9]{10}$");
        boolean hasNoChar = sdt.matches(".*\\D.*");

        if (validSDT && !hasNoChar) {
            if (sdt.startsWith("09") || sdt.startsWith("03") || sdt.startsWith("08") || sdt.startsWith("05") || sdt.startsWith("07")) {
                jLabel28.setText("(*)");
                jLabel28.setForeground(Color.BLACK);
                checkSDT = true;
            } else {
                jLabel28.setText("Bắt đầu là '09', '03', '08', '05', '07'");
                jLabel28.setForeground(Color.RED);
                checkSDT = false;
            }
        } else {
            if (!validSDT) {
                jLabel28.setText("Số điện thoại gồm 10 số");
                jLabel28.setForeground(Color.RED);
            } else if (hasNoChar) {
                jLabel28.setText("Số điện thoại không chứa chữ");
                jLabel28.setForeground(Color.RED);
            }
            checkSDT = false;
        }
    } else {
        jLabel28.setText("Không để trống");
        jLabel28.setForeground(Color.RED);
        checkSDT = false;
    }
}


	private NhomKhachHang convertToNhomKhachHang(String selectedOption) {
        switch (selectedOption) {
            case "Khách bình thường":
                return NhomKhachHang.KHACHBT;
            case "Khách vip":
                return NhomKhachHang.KHACHVIP;
            default:
                return null;
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnNhapFile;
    private javax.swing.JButton btnSuaKH2;
    private javax.swing.JButton btnSuaKH3;
    private javax.swing.JButton btnThemKH;
    private javax.swing.JLabel btnXuatFile;
    private javax.swing.JLabel date;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lblNameLogin;
    private javax.swing.JLabel lblTongChiTieu1;
    private javax.swing.JLabel lblTongDonHang1;
    private menuGui.MenuScrollPane menuScrollPane2;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlCenterKHchange;
    private javax.swing.JPanel pnlChange;
    private javax.swing.JPanel pnlInit;
    private javax.swing.JPanel pnlTopLeft;
    private javax.swing.JTextField txtTimKH;
    private javax.swing.JTextField txtTimKH11;
    private javax.swing.JTextField txtTimKH3;
    private javax.swing.JTextField txtTimKH4;
    private javax.swing.JTextField txtTongChiTieu1;
    private javax.swing.JTextField txtTongDonHang1;
    // End of variables declaration//GEN-END:variables
}
