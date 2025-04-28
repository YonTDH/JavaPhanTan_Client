/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import java.awt.Color;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import dao.DAO_NhanVien;
import entity.CaLamViec;
import entity.ChucVu;
import entity.KhachHang;
import entity.NhanVien;
import entity.TaiKhoan;
import lookup.LookupNaming;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nguyen chau tai
 */
public class FrmNhanVien extends javax.swing.JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5934997334794234352L;
	/**
     * Creates new form FrmDSKhachHang
     */
    private FrmChinh frm = new FrmChinh();
    private Thread thread = null;
    private DAO_NhanVien dao_nhanvien = LookupNaming.lookup_NhanVien();
    
    public FrmNhanVien() throws SQLException, RemoteException {
        initComponents();
        kiemTra();
        loadData();
        thietLapMaNhanVienTN();
        thread = new Thread(this::setTimeAuto);
        thread.start();
        // Su Kiem Loc theo Chuc Vu
        jComboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    try {
						locTheoChucVu();
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            }
        });
        
        // Su Kien Tim Kiem
        txtTimKH.selectAll();
        txtTimKH.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                try {
					locNhanVien();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }

            @Override
            public void keyPressed(KeyEvent e) {
                try {
					locNhanVien();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }

            @Override
            public void keyReleased(KeyEvent e) {
                try {
					locNhanVien();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        
        //
        jComboBox11.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    if(jComboBox11.getSelectedIndex()==0){
                        try {
							thietLapMaNhanVienTN();
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    }
                    else{
                        try {
							thietLapMaNhanVienQL();
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    }
                }
            }
        });
        
        //F1
        InputMap inputMap1 = btnThemNV.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap1.put(KeyStroke.getKeyStroke("F1"), "doSomething1");

        Action action1 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					btnThemNVActionPerformed(e);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        };

        btnThemNV.getActionMap().put("doSomething1", action1);
        
        //F2
        InputMap inputMap2 = btnSuaNhanVien1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap2.put(KeyStroke.getKeyStroke("F2"), "doSomething2");

        Action action2 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					btnSuaNhanVien1ActionPerformed(e);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        };
        btnSuaNhanVien1.getActionMap().put("doSomething2", action2);
        
        // F3
        InputMap inputMap3 = btnSuaNhanVien2.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap3.put(KeyStroke.getKeyStroke("F3"), "doSomething3");

        Action action3 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					btnSuaNhanVien2ActionPerformed(e);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        };
        btnSuaNhanVien2.getActionMap().put("doSomething3", action3);
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
                    thread.sleep(1); // cho phép ngủ trong khoảng 1000 mns =1s
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
    
    private void showPanelChange(JPanel a, JPanel b) {
        a.removeAll();
        a.add(b);
        a.repaint();
        a.revalidate();
    }

    public void kiemTra(){
        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Không sử dụng keyTyped trong trường hợp này
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Không sử dụng keyPressed trong trường hợp này
            }

            @Override
            public void keyReleased(KeyEvent e) {
                thietLapLabel();
            }

            private void thietLapLabel() {
                String tenNhanVien = txtTimKH9.getText().trim();
                String soDienThoai = txtTimKH12.getText().trim();
                String email = txtTimKH10.getText().trim();
                Date ngaySinh = jDateChooser3.getDate();

                Pattern pTenNV = Pattern.compile("^[\\p{L}][\\p{L} .'-]*$");
                Matcher mTenNV = pTenNV.matcher(tenNhanVien);
                if(txtTimKH9.getText().length()==0){
                    jLabel52.setText("Tên nhân viên không được để trống");
                    jLabel52.setForeground(Color.RED);
                }
                else if (!(mTenNV.matches())) {
                    jLabel52.setText("Tên NCC phải có dạng X X ...");
                    jLabel52.setForeground(Color.RED);
                }
                else{
                    jLabel52.setText("(*)");
                }

                if(!ngaySinh.before(new Date())){
                    jLabel56.setText("Ngày sinh phải trước ngày hiện tại");
                    jLabel56.setForeground(Color.RED);
                }
                else if(ngaySinh.equals(null)){
                    jLabel56.setText("Ngày sinh không được để trống");
                    jLabel56.setForeground(Color.RED);
                }
                else{
                    jLabel56.setText("(*)");
                }
                
                Pattern pSDT = Pattern.compile("^[0]\\d{9}$");
                Matcher mSDT = pSDT.matcher(soDienThoai);
                if(txtTimKH12.getText().length()==0){
                    jLabel59.setText("SDT không được để trống");
                    jLabel59.setForeground(Color.RED);
                }
                else if (!(mSDT.matches())) {
                     jLabel59.setText("SDT phải bắt đầu là 0 và có 10 số");
                     jLabel59.setForeground(Color.RED);
                }
                else{
                    jLabel59.setText("(*)");
                }

                Pattern pEmail = Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+$");
                Matcher mEmail = pEmail.matcher(email);
                if(txtTimKH10.getText().length()==0){
                    jLabel54.setText("Email không được để trống");
                    jLabel54.setForeground(Color.RED);
                }
                else if (!(mEmail.matches())) {
                    jLabel54.setText("Email phải có dạng exam@domainName");
                    jLabel54.setForeground(Color.RED);
                }
                else{
                    jLabel54.setText("(*)");
                }

                
            }
        };

        // Áp dụng KeyListener cho mỗi JTextField
        txtTimKH9.addKeyListener(keyListener);
        txtTimKH12.addKeyListener(keyListener);
        txtTimKH10.addKeyListener(keyListener);
        jDateChooser3.addKeyListener(keyListener);
    }
    
    
    public boolean validData() {
        String tenNhanVien = txtTimKH9.getText().trim();
        String soDienThoai = txtTimKH12.getText().trim();
        String email = txtTimKH10.getText().trim();
        Date ngaySinh = jDateChooser3.getDate();

        Pattern pTenNV = Pattern.compile("^[\\p{L}][\\p{L} .'-]*$");
        Matcher mTenNV = pTenNV.matcher(tenNhanVien);
        if (!(mTenNV.matches())) {
            JOptionPane.showMessageDialog(null, "Tên nhân viên phải có định dạng X X ...");
            return false;
        }

        Pattern pSDT = Pattern.compile("^0\\d{9}$");
        Matcher mSDT = pSDT.matcher(soDienThoai);
        if (!(mSDT.matches())) {
            JOptionPane.showMessageDialog(null, "Số điện thoại phải bắt đầu là 0 và có 10 chữ số");
            return false;
        }

        Pattern pEmail = Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+$");
        Matcher mEmail = pEmail.matcher(email);
        if (!(mEmail.matches())) {
            JOptionPane.showMessageDialog(null, "Email phải có dạng example@domainName");
            return false;
        }

        if (!ngaySinh.before(new Date())) {
            JOptionPane.showMessageDialog(null, "Ngày sinh phải trước ngày hiện tại");
            return false;
        }
        return true;
    }

//    public boolean validDataSua() {
//        String tenNhanVien = txtTimKH9.getText().trim();
//        String soDienThoai = txtTimKH12.getText().trim();
//        String email = txtTimKH10.getText().trim();
//        Date ngaySinh = jDateChooser3.getDate();
//
//        Pattern pTenNV = Pattern.compile("^[\\p{L} .'-]+$");
//        Matcher mTenNV = pTenNV.matcher(tenNhanVien);
//        if (!(mTenNV.matches())) {
//            JOptionPane.showMessageDialog(null, "Tên nhân viên phải có định dạng X X ...");
//            return false;
//        }
//
//        Pattern pSDT = Pattern.compile("^0\\d{9}$");
//        Matcher mSDT = pSDT.matcher(soDienThoai);
//        if (!(mSDT.matches())) {
//            JOptionPane.showMessageDialog(null, "Số điện thoại phải bắt đầu là 0 và có 10 chữ số");
//            return false;
//        }
//
//        Pattern pEmail = Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+$");
//        Matcher mEmail = pEmail.matcher(email);
//        if (!(mEmail.matches())) {
//            JOptionPane.showMessageDialog(null, "Email phải có dạng example@domainName");
//            return false;
//        }
//
//        if (!ngaySinh.before(new Date())) {
//            JOptionPane.showMessageDialog(null, "Ngày sinh phải trước ngày hiện tại");
//            return false;
//        }
//        return true;
//    }

    
    
    public void deleteTable() {
        DefaultTableModel dm = (DefaultTableModel) jTable2.getModel();
        dm.getDataVector().removeAllElements();
    }

    private LocalDateTime today = LocalDateTime.now();
    
    public String thietLapCaLamViec(){
        if(today.getHour()>=6 && (today.getHour()<=11 && today.getMinute()==0)){
            return "CA01";
        }
        else if(today.getHour()<=16 ){
            return "CA02";
        }
        return "CA03";
    }
    
    public void loadData() throws SQLException, RemoteException {
        deleteTable();
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
//        data = dao_nhanvien.getAllNhanVienTheoCa(thietLapCaLamViec());
        data = dao_nhanvien.getAllNhanVien_20();
        jDateChooser3.setDate(new Date());
        int stt = 1;
        for (NhanVien nv : data) {
//            if (nv.getCaLamViec().getMaCa().equalsIgnoreCase("CA01")) {
//                nv.getCaLamViec().setTenCa("Sáng");
//            } else if (nv.getCaLamViec().getMaCa().equalsIgnoreCase("CA02")) {
//                nv.getCaLamViec().setTenCa("Chiều");
//            } else {
//                nv.getCaLamViec().setTenCa("Tối");
//            }

            if (nv.getTinhTrangLamViec() == 1) {
                String[] newRow = {String.format("%s", stt),
                    String.format("%s", nv.getMaNhanVien()),
                    String.format("%s", nv.getHoTenNV()),
                    String.format("%s", nv.getChucVu()),
                    String.format("%s", nv.getGioiTinh()),
//                    String.format("%s", nv.getCaLamViec().getTenCa()),
                    String.format("%s", "CA1"),
                    String.format("%s", "Đang làm việc")
                };
                model.addRow(newRow);
            } else {
                String[] newRow = {String.format("%s", stt),
                    String.format("%s", nv.getMaNhanVien()),
                    String.format("%s", nv.getHoTenNV()),
                    String.format("%s", nv.getChucVu()),
                    String.format("%s", nv.getGioiTinh()),
//                    String.format("%s", nv.getCaLamViec().getTenCa()),
                    String.format("%s", "CA1"),
                    String.format("%s", "Đã nghỉ việc")
                };
                model.addRow(newRow);
            }
            stt++;
        }
//        System.out.println(data);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlCenterThem = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        pnlTopLeft1 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        pnlCenterKHchange1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtTimKH1 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtTimKH3 = new javax.swing.JTextField();
        txtTimKH4 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtTimKH6 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnLuuNV = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        btnQuayLai = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        pnlTableInit1 = new javax.swing.JPanel();
        menuScrollPane3 = new menuGui.MenuScrollPane();
        jTable3 = new javax.swing.JTable();
        pnlCenterXem = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        pnlTopLeft2 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        pnlCenterKHchange2 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtTimKH2 = new javax.swing.JTextField();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtTimKH5 = new javax.swing.JTextField();
        txtTimKH7 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        txtTimKH8 = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox<>();
        jComboBox8 = new javax.swing.JComboBox<>();
        jLabel38 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jComboBox9 = new javax.swing.JComboBox<>();
        jLabel41 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        btnQuayLai1 = new javax.swing.JButton();
        btnSuaNhanVien = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        pnlCenterSua = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        pnlTopLeft3 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        pnlCenterKHchange3 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        btnLuuNV1 = new javax.swing.JButton();
        btnHuy1 = new javax.swing.JButton();
        btnQuayLai2 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        pnlChange = new javax.swing.JPanel();
        pnlCenter = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        pnlTopLeft = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTimKH = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        pnlCenterKHchange = new javax.swing.JPanel();
        pnlTableInit = new javax.swing.JPanel();
        menuScrollPane2 = new menuGui.MenuScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel25 = new javax.swing.JPanel();
        txtTimKH11 = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        txtTimKH9 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel55 = new javax.swing.JLabel();
        jComboBox12 = new javax.swing.JComboBox<>();
        jLabel47 = new javax.swing.JLabel();
        txtTimKH12 = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        txtTimKH10 = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jComboBox11 = new javax.swing.JComboBox<>();
        jLabel60 = new javax.swing.JLabel();
        jComboBox13 = new javax.swing.JComboBox<>();
        jComboBox10 = new javax.swing.JComboBox<>();
        jLabel49 = new javax.swing.JLabel();
        btnThemNV = new javax.swing.JButton();
        btnSuaNhanVien1 = new javax.swing.JButton();
        btnSuaNhanVien2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblNameLogin = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        pnlCenterThem.setBackground(new java.awt.Color(153, 153, 153));
        pnlCenterThem.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));

        jPanel9.setBackground(new java.awt.Color(240, 242, 245));

        pnlTopLeft1.setBackground(new java.awt.Color(204, 204, 255));
        pnlTopLeft1.setLayout(new java.awt.BorderLayout());

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        pnlCenterKHchange1.setBackground(new java.awt.Color(250, 250, 250));
        pnlCenterKHchange1.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(250, 250, 250));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel9.setText("Thông tin nhân viên");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel10.setText("Số điện thoại");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel12.setText("Tên nhân viên");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel17.setText("Tình trạng làm việc");

        txtTimKH1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH1ActionPerformed(evt);
            }
        });

        jComboBox2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel23.setText("(*)");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel16.setText("Mã nhân viên");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel24.setText("(*)");

        txtTimKH3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH3ActionPerformed(evt);
            }
        });

        txtTimKH4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH4.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                txtTimKH4ComponentAdded(evt);
            }
        });
        txtTimKH4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH4ActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel13.setText("Chức vụ");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel20.setText("(*)");

        txtTimKH6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH6ActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel25.setText("Giới tính");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel26.setText("(*)");

        jComboBox3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel27.setText("Email");

        jDateChooser1.setDateFormatString("dd-MM-yyyy");
        jDateChooser1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel28.setText("Ngày sinh");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel29.setText("(*)");

        jComboBox5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel18.setText("Ca làm việc");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(62, 62, 62)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTimKH4, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(54, 54, 54)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtTimKH1, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTimKH6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(58, 58, 58)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTimKH3, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(80, 80, 80)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(261, 261, 261))
                            .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel24))
                        .addGap(18, 18, 18)
                        .addComponent(txtTimKH1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addComponent(txtTimKH4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel29))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTimKH6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTimKH3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(jLabel13)
                            .addComponent(jLabel28)
                            .addComponent(jLabel26))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel18))
                .addContainerGap(292, Short.MAX_VALUE))
        );

        pnlCenterKHchange1.add(jPanel6, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlTopLeft1.add(jPanel14, java.awt.BorderLayout.CENTER);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        btnLuuNV.setBackground(new java.awt.Color(3, 136, 253));
        btnLuuNV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLuuNV.setForeground(new java.awt.Color(255, 255, 255));
        btnLuuNV.setText("Lưu");
        btnLuuNV.setFocusable(false);
        btnLuuNV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLuuNV.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnLuuNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuNVActionPerformed(evt);
            }
        });

        btnHuy.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHuy.setForeground(new java.awt.Color(255, 77, 77));
        btnHuy.setText("Hủy");
        btnHuy.setFocusable(false);
        btnHuy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHuy.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        btnQuayLai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnQuayLai.setForeground(new java.awt.Color(3, 136, 253));
        btnQuayLai.setText("Quay lại danh sách nhân viên");
        btnQuayLai.setFocusable(false);
        btnQuayLai.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnQuayLai.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnQuayLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuayLaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnQuayLai)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnLuuNV, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLuuNV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHuy)
                    .addComponent(btnQuayLai))
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTopLeft1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(401, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlTopLeft1, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCenterThem.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel10.setBackground(new java.awt.Color(250, 250, 250));
        jPanel10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel10.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel14.setText("Nhân viên");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel15.setText("Nguyễn Châu Tình - DESGIN");

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel44.setText("> Thêm nhân viên");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel44)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1082, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addGap(19, 19, 19))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlCenterThem.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        pnlTableInit1.setBackground(new java.awt.Color(250, 250, 250));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
                "STT", "Mã nhân viên", "Tên nhân viên", "Chức vụ", "Giới tính", "Ca làm việc", "Tình trạng làm việc"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.setRowHeight(40);
        jTable3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable3.getTableHeader().setReorderingAllowed(false);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        menuScrollPane3.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        javax.swing.GroupLayout pnlTableInit1Layout = new javax.swing.GroupLayout(pnlTableInit1);
        pnlTableInit1.setLayout(pnlTableInit1Layout);
        pnlTableInit1Layout.setHorizontalGroup(
            pnlTableInit1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableInit1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(menuScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        pnlTableInit1Layout.setVerticalGroup(
            pnlTableInit1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableInit1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(menuScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pnlCenterXem.setBackground(new java.awt.Color(153, 153, 153));
        pnlCenterXem.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));

        jPanel11.setBackground(new java.awt.Color(240, 242, 245));

        pnlTopLeft2.setBackground(new java.awt.Color(204, 204, 255));
        pnlTopLeft2.setLayout(new java.awt.BorderLayout());

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        pnlCenterKHchange2.setBackground(new java.awt.Color(250, 250, 250));
        pnlCenterKHchange2.setLayout(new java.awt.BorderLayout());

        jPanel16.setBackground(new java.awt.Color(250, 250, 250));
        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel19.setText("Thông tin nhân viên");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel21.setText("Số điện thoại");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel22.setText("Tên nhân viên");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel30.setText("Tình trạng làm việc");

        txtTimKH2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH2.setEnabled(false);
        txtTimKH2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH2ActionPerformed(evt);
            }
        });

        jComboBox6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));
        jComboBox6.setEnabled(false);

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel31.setText("(*)");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel32.setText("Mã nhân viên");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel33.setText("(*)");

        txtTimKH5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH5.setEnabled(false);
        txtTimKH5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH5ActionPerformed(evt);
            }
        });

        txtTimKH7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH7.setEnabled(false);
        txtTimKH7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH7ActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel34.setText("Chức vụ");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel35.setText("(*)");

        txtTimKH8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH8.setEnabled(false);
        txtTimKH8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH8ActionPerformed(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel36.setText("Giới tính");

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel37.setText("(*)");

        jComboBox7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox7.setEnabled(false);

        jComboBox8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox8.setEnabled(false);

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel38.setText("Email");

        jDateChooser2.setDateFormatString("dd-MM-yyyy");
        jDateChooser2.setEnabled(false);
        jDateChooser2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel39.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel39.setText("Ngày sinh");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel40.setText("(*)");

        jComboBox9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));
        jComboBox9.setEnabled(false);

        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel41.setText("Ca làm việc");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(62, 62, 62)
                                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTimKH7, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel16Layout.createSequentialGroup()
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(54, 54, 54)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtTimKH2, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTimKH8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(58, 58, 58)
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTimKH5, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(80, 80, 80)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(261, 261, 261))
                            .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jLabel33))
                        .addGap(18, 18, 18)
                        .addComponent(txtTimKH2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(jLabel32))
                        .addGap(18, 18, 18)
                        .addComponent(txtTimKH7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel38)
                                    .addComponent(jLabel40))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTimKH8, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTimKH5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel35))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(jLabel34)
                            .addComponent(jLabel39)
                            .addComponent(jLabel37))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel21)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel41))
                .addContainerGap(292, Short.MAX_VALUE))
        );

        pnlCenterKHchange2.add(jPanel16, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlTopLeft2.add(jPanel15, java.awt.BorderLayout.CENTER);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));

        btnQuayLai1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnQuayLai1.setForeground(new java.awt.Color(3, 136, 253));
        btnQuayLai1.setText("Quay lại danh sách nhân viên");
        btnQuayLai1.setFocusable(false);
        btnQuayLai1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnQuayLai1.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnQuayLai1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuayLai1ActionPerformed(evt);
            }
        });

        btnSuaNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuaNhanVien.setForeground(new java.awt.Color(3, 136, 253));
        btnSuaNhanVien.setText("Sửa nhân viên");
        btnSuaNhanVien.setFocusable(false);
        btnSuaNhanVien.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSuaNhanVien.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnSuaNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnSuaNhanVienActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnQuayLai1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSuaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnQuayLai1)
                    .addComponent(btnSuaNhanVien))
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTopLeft2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(401, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlTopLeft2, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCenterXem.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel18.setBackground(new java.awt.Color(250, 250, 250));
        jPanel18.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel18.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel42.setText("Nhân viên");

        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel43.setText("Nguyễn Châu Tình - DESGIN");

        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel45.setText("> Xem nhân viên");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1091, Short.MAX_VALUE)
                .addComponent(jLabel43)
                .addGap(19, 19, 19))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlCenterXem.add(jPanel18, java.awt.BorderLayout.PAGE_START);

        pnlCenterSua.setBackground(new java.awt.Color(153, 153, 153));
        pnlCenterSua.setLayout(new java.awt.BorderLayout());

        jPanel19.setBackground(new java.awt.Color(153, 153, 153));

        jPanel20.setBackground(new java.awt.Color(240, 242, 245));

        pnlTopLeft3.setBackground(new java.awt.Color(204, 204, 255));
        pnlTopLeft3.setLayout(new java.awt.BorderLayout());

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        pnlCenterKHchange3.setBackground(new java.awt.Color(250, 250, 250));
        pnlCenterKHchange3.setLayout(new java.awt.BorderLayout());

        jPanel22.setBackground(new java.awt.Color(250, 250, 250));
        jPanel22.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel46.setText("Thông tin nhân viên");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel46)
                .addContainerGap(1098, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(668, Short.MAX_VALUE))
        );

        pnlCenterKHchange3.add(jPanel22, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlTopLeft3.add(jPanel21, java.awt.BorderLayout.CENTER);

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));

        btnLuuNV1.setBackground(new java.awt.Color(3, 136, 253));
        btnLuuNV1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLuuNV1.setForeground(new java.awt.Color(255, 255, 255));
        btnLuuNV1.setText("Lưu");
        btnLuuNV1.setFocusable(false);
        btnLuuNV1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLuuNV1.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnLuuNV1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuNV1ActionPerformed(evt);
            }
        });

        btnHuy1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHuy1.setForeground(new java.awt.Color(255, 77, 77));
        btnHuy1.setText("Hủy");
        btnHuy1.setFocusable(false);
        btnHuy1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHuy1.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnHuy1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuy1ActionPerformed(evt);
            }
        });

        btnQuayLai2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnQuayLai2.setForeground(new java.awt.Color(3, 136, 253));
        btnQuayLai2.setText("Quay lại danh sách nhân viên");
        btnQuayLai2.setFocusable(false);
        btnQuayLai2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnQuayLai2.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnQuayLai2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuayLai2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnQuayLai2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHuy1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnLuuNV1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLuuNV1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHuy1)
                    .addComponent(btnQuayLai2))
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTopLeft3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(401, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlTopLeft3, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCenterSua.add(jPanel19, java.awt.BorderLayout.CENTER);

        jPanel24.setBackground(new java.awt.Color(250, 250, 250));
        jPanel24.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel24.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel61.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel61.setText("Nhân viên");

        jLabel62.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel62.setText("Nguyễn Châu Tình - DESGIN");

        jLabel63.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel63.setText("> Sửa nhân viên");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel61)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel63)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1097, Short.MAX_VALUE)
                .addComponent(jLabel62)
                .addGap(19, 19, 19))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlCenterSua.add(jPanel24, java.awt.BorderLayout.PAGE_START);

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1690, 787));
        setLayout(new java.awt.BorderLayout());

        pnlChange.setLayout(new java.awt.BorderLayout());

        pnlCenter.setBackground(new java.awt.Color(153, 153, 153));
        pnlCenter.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jPanel8.setBackground(new java.awt.Color(240, 242, 245));

        pnlTopLeft.setBackground(new java.awt.Color(204, 204, 255));
        pnlTopLeft.setLayout(new java.awt.BorderLayout());

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel5.setText("Thông tin nhân viên");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/magnifying-glass.png"))); // NOI18N

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

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Thu ngân", "Quản lý" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

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
                        .addComponent(txtTimKH)))
                .addGap(12, 12, 12)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        pnlCenterKHchange.setLayout(new java.awt.BorderLayout());

        pnlTableInit.setBackground(new java.awt.Color(250, 250, 250));

        menuScrollPane2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

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
                "STT", "Mã nhân viên", "Tên nhân viên", "Chức vụ", "Giới tính", "Ca làm việc", "Tình trạng làm việc"
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
                try {
					jTable2MouseClicked(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        menuScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        jPanel25.setBackground(new java.awt.Color(250, 250, 250));
        jPanel25.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtTimKH11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH11ActionPerformed(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel51.setText("Mã nhân viên");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel50.setText("(*)");

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel48.setText("Tên nhân viên");

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel52.setText("(*)");

        txtTimKH9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH9ActionPerformed(evt);
            }
        });

        jLabel58.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel58.setText("Ngày sinh");

        jLabel56.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel56.setText("(*)");

        jDateChooser3.setDateFormatString("dd-MM-yyyy");
        jDateChooser3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel55.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel55.setText("Giới tính");

        jComboBox12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));

        jLabel47.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel47.setText("Số điện thoại");

        txtTimKH12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH12ActionPerformed(evt);
            }
        });

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel59.setText("(*)");

        jLabel57.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel57.setText("Email");

        jLabel54.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel54.setText("(*)");

        txtTimKH10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH10ActionPerformed(evt);
            }
        });

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel53.setText("Chức vụ");

        jComboBox11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Thu ngân", "Quản lý" }));
        jComboBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox11ActionPerformed(evt);
            }
        });

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel60.setText("Ca làm việc");

        jComboBox13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox13.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sáng", "Chiều", "Tối" }));

        jComboBox10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang làm việc", "Nghỉ việc" }));

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel49.setText("Tình trạng làm việc");

        btnThemNV.setBackground(new java.awt.Color(3, 136, 253));
        btnThemNV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemNV.setForeground(new java.awt.Color(255, 255, 255));
        btnThemNV.setText("Thêm nhân viên (F1)");
        btnThemNV.setFocusable(false);
        btnThemNV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemNV.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnThemNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnThemNVActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnSuaNhanVien1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuaNhanVien1.setForeground(new java.awt.Color(3, 136, 253));
        btnSuaNhanVien1.setText("Sửa nhân viên (F2)");
        btnSuaNhanVien1.setFocusable(false);
        btnSuaNhanVien1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSuaNhanVien1.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnSuaNhanVien1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnSuaNhanVien1ActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnSuaNhanVien2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuaNhanVien2.setForeground(new java.awt.Color(3, 136, 253));
        btnSuaNhanVien2.setText("Làm mới (F3)");
        btnSuaNhanVien2.setFocusable(false);
        btnSuaNhanVien2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSuaNhanVien2.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnSuaNhanVien2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnSuaNhanVien2ActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)
                        .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel51)
                                .addGap(49, 49, 49)
                                .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel48)
                                .addGap(45, 45, 45)
                                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(211, 211, 211)
                                .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel58)
                                .addGap(69, 69, 69)
                                .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTimKH9)
                            .addComponent(txtTimKH11))
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel25Layout.createSequentialGroup()
                                .addGap(107, 107, 107)
                                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox11, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTimKH10, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtTimKH12, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel25Layout.createSequentialGroup()
                                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel25Layout.createSequentialGroup()
                                                .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(85, 85, 85)
                                                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel25Layout.createSequentialGroup()
                                        .addComponent(jLabel47)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(62, 62, 62)))
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnThemNV, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(btnSuaNhanVien1, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(btnSuaNhanVien2, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                .addGap(50, 50, 50))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel51)
                            .addComponent(jLabel50)
                            .addComponent(jLabel47)
                            .addComponent(jLabel59))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTimKH11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimKH12, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel48)
                                .addComponent(jLabel52)
                                .addComponent(jLabel57))
                            .addComponent(jLabel54))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTimKH9, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimKH10, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSuaNhanVien1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(btnThemNV, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel53)
                            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel58)
                                .addComponent(jLabel56)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSuaNhanVien2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(jLabel60)
                    .addComponent(jLabel49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlTableInitLayout = new javax.swing.GroupLayout(pnlTableInit);
        pnlTableInit.setLayout(pnlTableInitLayout);
        pnlTableInitLayout.setHorizontalGroup(
            pnlTableInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableInitLayout.createSequentialGroup()
                .addGroup(pnlTableInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(menuScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1257, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlTableInitLayout.setVerticalGroup(
            pnlTableInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableInitLayout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlCenterKHchange.add(pnlTableInit, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCenterKHchange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        pnlTopLeft.add(jPanel12, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/down-arrow.png"))); // NOI18N
        jLabel1.setText("Nhập file");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/up-arrow.png"))); // NOI18N
        jLabel2.setText("Xuất file");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
					jLabel2MouseClicked(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTopLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(405, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCenter.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(250, 250, 250));
        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setText("Nhân viên");

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

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/icons8-user-50.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1296, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNameLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addGap(14, 14, 14))
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
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCenter.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        pnlChange.add(pnlCenter, java.awt.BorderLayout.CENTER);

        add(pnlChange, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    
    public void thietLapMaNhanVienQL() throws RemoteException {
        // Lấy ngày hiện tại và lấy mã nhân viên từ DAO
//        LocalDate ngayHienTai = LocalDate.now();
//        String ngayHienTaiStr = ngayHienTai.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
//        String maNhanVienQLDB = dao_nhanvien.getMaNhanVienQLDB();
//        String maNhanVienTNDB = dao_nhanvien.getMaNhanVienTNDB();
//
//        String phanSTTQL = maNhanVienQLDB.substring(maNhanVienQLDB.length() - 6, maNhanVienQLDB.length()); // Lấy 6 kí tự cuối của maNhanVien
//        int index = Integer.parseInt(phanSTTQL); // Chuyển 6 kí tự cuối về int
//
//        // Tạo stt tăng nếu ngày là ngày hôm sau và set 6 kí số phía sau về dạng "-000001"
//        int iStart = 1;
//        String sttStart = String.format("%06d", iStart);
//
//        String check = "QL" + ngayHienTaiStr + "-" + String.format("%06d", index);
//        // Mã đã có trong db thì tăng mã lên 1
//        if (check.equalsIgnoreCase(maNhanVienQLDB)) {
//            index++;
//            String stt = String.format("%06d", index);
//            String sttTangTN = "QL" + ngayHienTaiStr + "-" + stt;
//            txtTimKH11.setText(sttTangTN);
//        } // Chưa có thì set là 1
//        else {
//            txtTimKH11.setText("QL" + ngayHienTaiStr + "-" + sttStart);
//        }
//        txtTimKH11.setEnabled(false);
        LocalDate d = LocalDate.now();
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("ddMMyyyy");
        String format = d.format(myFormatDate);
        Integer count = 1;
        String cusID = "";

        do {
            String tempID = count.toString().length() == 1 ? ("QL" + format + "-00000" + count)
                    : count.toString().length() == 2 ? ("QL" + format + "-0000" + count)
                    : count.toString().length() == 3 ? ("QL" + format + "-000" + count)
                    : count.toString().length() == 4 ? ("QL" + format + "-00" + count)
                    : count.toString().length() == 5 ? ("QL" + format + "-0" + count)
                    : ("QL" + format + "-" + count);

            NhanVien existingCustomer = dao_nhanvien.getNVTheoMa(tempID);
            if (existingCustomer == null) {
                cusID = tempID;
                break;
            }
            count++;
        } while (true);
        txtTimKH11.setText(cusID);
        txtTimKH11.setEnabled(false);
    }
    
    public void thietLapMaNhanVienTN() throws RemoteException {
        // Lấy ngày hiện tại và lấy mã nhân viên từ DAO
//        LocalDate ngayHienTai = LocalDate.now();
//        String ngayHienTaiStr = ngayHienTai.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
//        
//        String maNhanVienTNDB = dao_nhanvien.getMaNhanVienTNDB();
//
//        String phanSTTQL = maNhanVienTNDB.substring(maNhanVienTNDB.length() - 6, maNhanVienTNDB.length()); // Lấy 6 kí tự cuối của maNhanVien
//        int index = Integer.parseInt(phanSTTQL); // Chuyển 6 kí tự cuối về int
//
//        // Tạo stt tăng nếu ngày là ngày hôm sau và set 6 kí số phía sau về dạng "-000001"
//        int iStart = 1;
//        String sttStart = String.format("%06d", iStart);
//
//        String check = "TN" + ngayHienTaiStr + "-" + String.format("%06d", index);
//        // Mã đã có trong db thì tăng mã lên 1
//        if (check.equalsIgnoreCase(maNhanVienTNDB)) {
//            index++;
//            String stt = String.format("%06d", index);
//            String sttTangTN = "TN" + ngayHienTaiStr + "-" + stt;
//            txtTimKH11.setText(sttTangTN);
//        } // Chưa có thì set là 1
//        else {
//            txtTimKH11.setText("TN" + ngayHienTaiStr + "-" + sttStart);
//        }
//        txtTimKH11.setEnabled(false);
    	LocalDate d = LocalDate.now();
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("ddMMyyyy");
        String format = d.format(myFormatDate);
        Integer count = 1;
        String cusID = "";

        do {
            String tempID = count.toString().length() == 1 ? ("TN" + format + "-00000" + count)
                    : count.toString().length() == 2 ? ("TN" + format + "-0000" + count)
                    : count.toString().length() == 3 ? ("TN" + format + "-000" + count)
                    : count.toString().length() == 4 ? ("TN" + format + "-00" + count)
                    : count.toString().length() == 5 ? ("TN" + format + "-0" + count)
                    : ("TN" + format + "-" + count);

            NhanVien existingCustomer = dao_nhanvien.getNVTheoMa(tempID);
            if (existingCustomer == null) {
                cusID = tempID;
                break;
            }
            count++;
        } while (true);
        txtTimKH11.setText(cusID);
        txtTimKH11.setEnabled(false);
    }

    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
    
    public void importExcelNhanVien() {
    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    File excelFile;
    FileInputStream excelFIS = null;
    BufferedInputStream excelBIS = null;
    XSSFWorkbook workBook = null;
    String currentPath = "C:\\User\\Authentic\\Desktop";
    JFileChooser excelFileChooser = new JFileChooser(currentPath);
    excelFileChooser.setDialogTitle("Chọn file Excel");
    FileNameExtensionFilter fnef = new FileNameExtensionFilter("FILE EXCEL", "xls", "xlsx", "xlsm");
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
                // Kiểm tra số lượng cột trong file Excel có khớp với định dạng không
                if (columnCount == 9) {
                    while (rowIterator.hasNext()) {
                        Row currentRow = rowIterator.next();
                        Iterator<Cell> cellIterator = currentRow.cellIterator();
                        DataFormatter dataFormatter = new DataFormatter();
                        if (cellIterator.hasNext()) {
                            // Thêm xử lý dữ liệu tương ứng với các cột trong file Excel
                            int stt = (int) cellIterator.next().getNumericCellValue();
                            String maNhanVien = dataFormatter.formatCellValue(cellIterator.next());
                            String tenNhanVien = dataFormatter.formatCellValue(cellIterator.next());
                            String ngaySinh = dataFormatter.formatCellValue(cellIterator.next());
                            String soDienThoai = dataFormatter.formatCellValue(cellIterator.next());
                            String gioiTinh = dataFormatter.formatCellValue(cellIterator.next());
                            String email = dataFormatter.formatCellValue(cellIterator.next());
                            String taiKhoan = dataFormatter.formatCellValue(cellIterator.next());

                            int tinhTrangLamViec = 0; // Mặc định là Nghỉ việc
                            String tinhTrangStr = dataFormatter.formatCellValue(cellIterator.next());
                            if ("Đang làm việc".equalsIgnoreCase(tinhTrangStr)) {
                                tinhTrangLamViec = 1;
                            }

                            String caLamViec = dataFormatter.formatCellValue(cellIterator.next());
                            // Xử lý dữ liệu cho caLamViec, có thể là "Sáng", "Chiều", "Tối"

                            String chucVu = dataFormatter.formatCellValue(cellIterator.next());
                            // Xử lý dữ liệu cho chucVu, có thể là "Nhân viên", "Quản lý",...
                            
                            ChucVu cVu = null;
                            if(chucVu.equalsIgnoreCase("THUNGAN"))
                               cVu = cVu.THUNGAN;
                            else
                               cVu = cVu.QUANLY;
//                            NhanVien nhanVien = new NhanVien(maNhanVien, gioiTinh, LocalDate.parse(ngaySinh), soDienThoai, gioiTinh, email, taiKhoan, 
//                                    tinhTrangLamViec, caLamViec, cVu);\
                            NhanVien nhanVien = new NhanVien(maNhanVien);
                            
                            dao_nhanvien.themNhanVien(nhanVien);
                            System.out.println("NhanVien: "+nhanVien);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "File Excel không đúng định dạng cho Nhân Viên.",
                            "Lỗi Định Dạng", JOptionPane.ERROR_MESSAGE);
                    
                }
            }
            workBook.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Lỗi Đọc File", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, e.getMessage());
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


    
//    public void exportExcel() throws RemoteException {
//        List<NhanVien> dsNhanVien = dao_nhanvien.getAllNhanVien();
//        try {
//                JFileChooser jFileChooser = new JFileChooser("excel");
//                jFileChooser.showSaveDialog(this);
//                jFileChooser.setDialogTitle("Select Folder");
//                File saveFile = jFileChooser.getSelectedFile();
//                if (saveFile != null) {
//                    saveFile = new File(saveFile.toString() + ".xlsx");
//                    XSSFWorkbook wordbook = new XSSFWorkbook();
//                    XSSFSheet sheet = wordbook.createSheet("DSNhanVien");
//                    XSSFRow row = null;
//                    Cell cell = null;
//                    row = sheet.createRow(1);
//                    cell = row.createCell(0, CellType.STRING);
//                    cell.setCellValue("Danh sách Nhân viên");
//                    row = sheet.createRow(2);
//                    cell = row.createCell(0, CellType.STRING);
//                    cell.setCellValue("STT");
//                    cell = row.createCell(1, CellType.STRING);
//                    cell.setCellValue("Mã Nhân viên");
//                    cell = row.createCell(2, CellType.STRING);
//                    cell.setCellValue("Tên Nhân viên");
//                    cell = row.createCell(3, CellType.STRING);
//                    cell.setCellValue("Số điện thoại");
//                    cell = row.createCell(4, CellType.STRING);
//                    cell.setCellValue("Giới tính");
//                    cell = row.createCell(5, CellType.STRING);
//                    cell.setCellValue("Email");
//                    cell = row.createCell(6, CellType.STRING);
//                    cell.setCellValue("Tài khoản");
//                    cell = row.createCell(7, CellType.STRING);
//                    cell.setCellValue("Tình trạng làm việc");
//                    cell = row.createCell(8, CellType.STRING);
//                    cell.setCellValue("Ca làm việc");
//                    cell = row.createCell(9, CellType.STRING);
//                    cell.setCellValue("Chức vụ");
//                    cell = row.createCell(10, CellType.STRING);
//                    
//
//                    for (int i = 0; i < dsNhanVien.size(); i++) {
//                        row = sheet.createRow(3 + i);
//                        cell = row.createCell(0, CellType.NUMERIC);
//                        cell.setCellValue(i + 1);
//                        cell = row.createCell(1, CellType.STRING);
//                        cell.setCellValue(dsNhanVien.get(i).getMaNhanVien());
//                        cell = row.createCell(2, CellType.STRING);
//                        cell.setCellValue(dsNhanVien.get(i).getHoTenNV());
//                        cell = row.createCell(3, CellType.STRING);
//                        cell.setCellValue(dsNhanVien.get(i).getSoDienThoai());
//                        cell = row.createCell(4, CellType.STRING);
//                        cell.setCellValue(dsNhanVien.get(i).getGioiTinh());
//                        cell = row.createCell(5, CellType.STRING);
//                        cell.setCellValue(dsNhanVien.get(i).getEmail());
//                        cell = row.createCell(6, CellType.STRING);
//                        cell.setCellValue(dsNhanVien.get(i).getTaiKhoan().getTenDangNhap());
//                        
//                        if(dsNhanVien.get(i).getTinhTrangLamViec()==1){
//                            cell = row.createCell(7, CellType.STRING);
//                            cell.setCellValue("Đang làm việc");
//                        }
//                        else{
//                            cell = row.createCell(7, CellType.STRING);
//                            cell.setCellValue("Nghỉ việc");
//                        }
//                        
//                        if(dsNhanVien.get(i).getCaLamViec().getMaCa().equalsIgnoreCase("CA01")){
//                            cell = row.createCell(8, CellType.STRING);
//                            cell.setCellValue("Sáng");
//                        }
//                        else if(dsNhanVien.get(i).getCaLamViec().getMaCa().equalsIgnoreCase("CA02")){
//                            cell = row.createCell(8, CellType.STRING);
//                            cell.setCellValue("Chiều");
//                        }
//                        else{
//                            cell = row.createCell(8, CellType.STRING);
//                            cell.setCellValue("Tối");
//                        }
// 
//                        cell = row.createCell(9, CellType.STRING);
//                        cell.setCellValue(dsNhanVien.get(i).getChucVu().toString());
//                        cell = row.createCell(10, CellType.STRING);
//                    }
//
//                    FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
//                    wordbook.write(out);
//                    wordbook.close();
//                    out.close();
//                    openFile(saveFile.toString());
//                } else {
//                        System.out.println("Error");
//                }
//        } catch (FileNotFoundException e) {
//                System.out.println(e);
//        } catch (IOException io) {
//                System.out.println(io);
//        }
//    }
    
    public void exportExcel() {
        Runnable exportTask = new Runnable() {
            @Override
            public void run() {
                
                try {
                	List<NhanVien> dsNhanVien = dao_nhanvien.getAllNhanVien();
                    JFileChooser jFileChooser = new JFileChooser("excel");
                    jFileChooser.showSaveDialog(null);
                    jFileChooser.setDialogTitle("Select Folder");
                    File saveFile = jFileChooser.getSelectedFile();
                    if (saveFile != null) {
                        saveFile = new File(saveFile.toString() + ".xlsx");
                        XSSFWorkbook wordbook = new XSSFWorkbook();
                        XSSFSheet sheet = wordbook.createSheet("DSNhanVien");
                        XSSFRow row = null;
                        Cell cell = null;
                        row = sheet.createRow(1);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellValue("Danh sách Nhân viên");
                        row = sheet.createRow(2);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellValue("STT");
                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellValue("Mã Nhân viên");
                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellValue("Tên Nhân viên");
                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellValue("Số điện thoại");
                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellValue("Giới tính");
                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellValue("Email");
                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellValue("Tài khoản");
                        cell = row.createCell(7, CellType.STRING);
                        cell.setCellValue("Tình trạng làm việc");
                        cell = row.createCell(8, CellType.STRING);
                        cell.setCellValue("Ca làm việc");
                        cell = row.createCell(9, CellType.STRING);
                        cell.setCellValue("Chức vụ");
                        cell = row.createCell(10, CellType.STRING);

                        for (int i = 0; i < dsNhanVien.size(); i++) {
                            row = sheet.createRow(3 + i);
                            cell = row.createCell(0, CellType.NUMERIC);
                            cell.setCellValue(i + 1);
                            cell = row.createCell(1, CellType.STRING);
                            cell.setCellValue(dsNhanVien.get(i).getMaNhanVien());
                            cell = row.createCell(2, CellType.STRING);
                            cell.setCellValue(dsNhanVien.get(i).getHoTenNV());
                            cell = row.createCell(3, CellType.STRING);
                            cell.setCellValue(dsNhanVien.get(i).getSoDienThoai());
                            cell = row.createCell(4, CellType.STRING);
                            cell.setCellValue(dsNhanVien.get(i).getGioiTinh());
                            cell = row.createCell(5, CellType.STRING);
                            cell.setCellValue(dsNhanVien.get(i).getEmail());
                            cell = row.createCell(6, CellType.STRING);
                            cell.setCellValue(dsNhanVien.get(i).getTaiKhoan().getTenDangNhap());
                            
                            if(dsNhanVien.get(i).getTinhTrangLamViec()==1){
                                cell = row.createCell(7, CellType.STRING);
                                cell.setCellValue("Đang làm việc");
                            }
                            else{
                                cell = row.createCell(7, CellType.STRING);
                                cell.setCellValue("Nghỉ việc");
                            }
                            
                            if(dsNhanVien.get(i).getCaLamViec().getMaCa().equalsIgnoreCase("CA01")){
                                cell = row.createCell(8, CellType.STRING);
                                cell.setCellValue("Sáng");
                            }
                            else if(dsNhanVien.get(i).getCaLamViec().getMaCa().equalsIgnoreCase("CA02")){
                                cell = row.createCell(8, CellType.STRING);
                                cell.setCellValue("Chiều");
                            }
                            else{
                                cell = row.createCell(8, CellType.STRING);
                                cell.setCellValue("Tối");
                            }
     
                            cell = row.createCell(9, CellType.STRING);
                            cell.setCellValue(dsNhanVien.get(i).getChucVu().toString());
                            cell = row.createCell(10, CellType.STRING);
                        }

                        FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
                        wordbook.write(out);
                        wordbook.close();
                        out.close();
                        openFile(saveFile.toString());
                    } else {
                            System.out.println("Error");
                    }
                } catch (FileNotFoundException e) {
                    System.out.println(e);
                } catch (IOException io) {
                    System.out.println(io);
                }
            }
        };

        Thread exportThread = new Thread(exportTask);
        exportThread.start();
    }

    
    
    
    private void locNhanVien() throws RemoteException {
        String duLieuTim = txtTimKH.getText();
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        data = dao_nhanvien.locNhanVien(duLieuTim);
        int iD = 1;
        try {
            model.setRowCount(0);
            jTable2.clearSelection();
            for (NhanVien nv : data) {
//                if (nv.getCaLamViec().getMaCa().equalsIgnoreCase("CA01")) {
//                    nv.getCaLamViec().setTenCa("Sáng");
//                } else if (nv.getCaLamViec().getMaCa().equalsIgnoreCase("CA02")) {
//                    nv.getCaLamViec().setTenCa("Chiều");
//                } else {
//                    nv.getCaLamViec().setTenCa("Tối");
//                }
                if (nv.getTinhTrangLamViec() == 1) {
                    String[] newRow = {String.format("%s", iD),
                        String.format("%s", nv.getMaNhanVien()),
                        String.format("%s", nv.getHoTenNV()),
                        String.format("%s", nv.getChucVu()),
                        String.format("%s", nv.getGioiTinh()),
                        String.format("%s", nv.getCaLamViec().getMaCa()),
                        String.format("%s", "Đang làm việc")
                    };
                    model.addRow(newRow);
                } else {
                    String[] newRow = {String.format("%s", iD),
                        String.format("%s", nv.getMaNhanVien()),
                        String.format("%s", nv.getHoTenNV()),
                        String.format("%s", nv.getChucVu()),
                        String.format("%s", nv.getGioiTinh()),
                        String.format("%s", nv.getCaLamViec().getMaCa()),
                        String.format("%s", "Đã nghỉ việc")
                    };
                    model.addRow(newRow);
                }
                iD++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void locTheoChucVu() throws RemoteException{
        if(jComboBox1.getSelectedIndex()==0){
            try {
                loadData();
            } catch (SQLException ex) {
                Logger.getLogger(FrmNhanVien.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(jComboBox1.getSelectedIndex()==1){
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            data = dao_nhanvien.locNhanVienTheoChucVu(ChucVu.THUNGAN);
            int iD = 1;
            try {
                model.setRowCount(0);
                jTable2.clearSelection();
                for (NhanVien nv : data) {
//                    if (nv.getCaLamViec().getMaCa().equalsIgnoreCase("CA01")) {
//                        nv.getCaLamViec().setTenCa("Sáng");
//                    } else if (nv.getCaLamViec().getMaCa().equalsIgnoreCase("CA02")) {
//                        nv.getCaLamViec().setTenCa("Chiều");
//                    } else {
//                        nv.getCaLamViec().setTenCa("Tối");
//                    }
                    if (nv.getTinhTrangLamViec() == 1) {
                        String[] newRow = {String.format("%s", iD),
                            String.format("%s", nv.getMaNhanVien()),
                            String.format("%s", nv.getHoTenNV()),
                            String.format("%s", nv.getChucVu()),
                            String.format("%s", nv.getGioiTinh()),
                            String.format("%s", nv.getCaLamViec().getMaCa()),
                            String.format("%s", "Đang làm việc")
                        };
                        model.addRow(newRow);
                    } else {
                        String[] newRow = {String.format("%s", iD),
                            String.format("%s", nv.getMaNhanVien()),
                            String.format("%s", nv.getHoTenNV()),
                            String.format("%s", nv.getChucVu()),
                            String.format("%s", nv.getGioiTinh()),
                            String.format("%s", nv.getCaLamViec().getMaCa()),
                            String.format("%s", "Đã nghỉ việc")
                        };
                        model.addRow(newRow);
                    }
                    iD++;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        else{
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            data = dao_nhanvien.locNhanVienTheoChucVu(ChucVu.QUANLY);
            int iD = 1;
            try {
                model.setRowCount(0);
                jTable2.clearSelection();
                for (NhanVien nv : data) {
//                    if (nv.getCaLamViec().getMaCa().equalsIgnoreCase("CA01")) {
//                        nv.getCaLamViec().setTenCa("Sáng");
//                    } else if (nv.getCaLamViec().getMaCa().equalsIgnoreCase("CA02")) {
//                        nv.getCaLamViec().setTenCa("Chiều");
//                    } else {
//                        nv.getCaLamViec().setTenCa("Tối");
//                    }
                    if (nv.getTinhTrangLamViec() == 1) {
                        String[] newRow = {String.format("%s", iD),
                            String.format("%s", nv.getMaNhanVien()),
                            String.format("%s", nv.getHoTenNV()),
                            String.format("%s", nv.getChucVu()),
                            String.format("%s", nv.getGioiTinh()),
                            String.format("%s", nv.getCaLamViec().getMaCa()),
                            String.format("%s", "Đang làm việc")
                        };
                        model.addRow(newRow);
                    } else {
                        String[] newRow = {String.format("%s", iD),
                            String.format("%s", nv.getMaNhanVien()),
                            String.format("%s", nv.getHoTenNV()),
                            String.format("%s", nv.getChucVu()),
                            String.format("%s", nv.getGioiTinh()),
                            String.format("%s", nv.getCaLamViec().getMaCa()),
                            String.format("%s", "Đã nghỉ việc")
                        };
                        model.addRow(newRow);
                    }
                    iD++;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }
    
    
    private void btnThemNVActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btnThemNVActionPerformed
        // TODO add your handling code here:
  
        NhanVien nv = new NhanVien();

        String maNhanVien = txtTimKH11.getText();
        String tenNhanVien = txtTimKH9.getText();
        
        Date selectedDate = jDateChooser3.getDate();
        LocalDate ngaySinh = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        String soDienThoai = txtTimKH12.getText();
        String gioiTinh = String.valueOf(jComboBox12.getSelectedItem());
        String email = txtTimKH10.getText();
        TaiKhoan taiKhoan = new TaiKhoan(email, "123456", email);

        int tinhTrangLamViec = Integer.parseInt(String.valueOf(jComboBox2.getSelectedItem()));
        String caLv = String.valueOf(jComboBox5.getSelectedItem());
        
        String caLamViecStr = String.valueOf(jComboBox13.getSelectedItem());
        CaLamViec caLamViec;
        if(caLamViecStr.equalsIgnoreCase("Sáng")){
            caLamViec = new CaLamViec("CA01");
        }
        else if(caLamViecStr.equalsIgnoreCase("Chiều")){
            caLamViec = new CaLamViec("CA02");
        }
        else{
            caLamViec = new CaLamViec("CA03");
        }
               
        String cVu = String.valueOf(jComboBox11.getSelectedItem());
        ChucVu chucVu;
        if(cVu.equalsIgnoreCase("Thu ngân")){
            chucVu = ChucVu.THUNGAN;
        }
        else{
            chucVu = ChucVu.QUANLY;
        }
       
        if (evt.getSource().equals(btnThemNV)) {
            if ((tenNhanVien.length()==0) || (soDienThoai.length()==0) || (email.length()==0)) 
                JOptionPane.showMessageDialog(null, "Chưa nhập đầy đủ dữ liệu");
            
            else if (validData()) {
                NhanVien nvThem = new NhanVien(maNhanVien, tenNhanVien, ngaySinh, soDienThoai, gioiTinh, email, taiKhoan,
                        tinhTrangLamViec, caLamViec.getMaCa(), chucVu);
                dao_nhanvien.themNhanVien(nvThem);
               
            } 
        } 
    }//GEN-LAST:event_btnThemNVActionPerformed

    private void txtTimKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKHActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtTimKHActionPerformed

    private void txtTimKHFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKHFocusGained
        // TODO add your handling code here:
        frm.placeHoderTextGianed(txtTimKH);
    }//GEN-LAST:event_txtTimKHFocusGained

    private void txtTimKHFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKHFocusLost
        // TODO add your handling code here:
        frm.placeHoderTextLost(txtTimKH);
    }//GEN-LAST:event_txtTimKHFocusLost

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) throws RemoteException {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1 && !evt.isConsumed()) {
            evt.consume();
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            String maNhanVien = model.getValueAt(jTable2.getSelectedRow(), 1).toString();
            NhanVien nv = dao_nhanvien.getNVTheoMa(maNhanVien);

            txtTimKH11.setText(model.getValueAt(jTable2.getSelectedRow(), 1).toString());
            txtTimKH11.setEnabled(false);
            txtTimKH9.setText(model.getValueAt(jTable2.getSelectedRow(), 2).toString());
            txtTimKH12.setText(nv.getSoDienThoai());
            txtTimKH10.setText(nv.getEmail());
            
            LocalDate ngaySinh = nv.getNgaySinh();
            // Chuyen tu LocalDate sang Date
            Date date = Date.from(ngaySinh.atStartOfDay(ZoneId.systemDefault()).toInstant());
            jDateChooser3.setDate(date);
            
            // Set ngày làm việc
            if(model.getValueAt(jTable2.getSelectedRow(), 3).toString().equalsIgnoreCase("THUNGAN")){
                jComboBox11.setSelectedItem("Thu ngân");
            }
            else{
                jComboBox11.setSelectedItem("Quản lý");
            }
            // Set giới tính
            if(model.getValueAt(jTable2.getSelectedRow(), 4).toString().equalsIgnoreCase("Nam")){
                jComboBox12.setSelectedItem("Nam");
            }
            else{
                jComboBox12.setSelectedItem("Nữ");
            }
            // Set ca làm việc
            if(model.getValueAt(jTable2.getSelectedRow(), 5).toString().equalsIgnoreCase("Sáng")){
                jComboBox13.setSelectedItem("Sáng");
            }
            else if(model.getValueAt(jTable2.getSelectedRow(), 5).toString().equalsIgnoreCase("Chiều")){
                jComboBox13.setSelectedItem("Chiều");
            }
            else{
                jComboBox13.setSelectedItem("Tối");
            }
            //Set tình trạng làm việc
            if(model.getValueAt(jTable2.getSelectedRow(), 6).toString().equalsIgnoreCase("Đang làm việc")){
                jComboBox10.setSelectedItem("Đang làm việc");
            }
            else{
                jComboBox10.setSelectedItem("Nghỉ việc");
            }
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void txtTimKH9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH9ActionPerformed

    private void txtTimKH10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH10ActionPerformed

    private void txtTimKH11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH11ActionPerformed

    private void txtTimKH12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH12ActionPerformed

    private void btnSuaNhanVien1ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btnSuaNhanVien1ActionPerformed
        // TODO add your handling code here:
        String maNVSua = txtTimKH11.getText();
        String tenNVMoi = txtTimKH9.getText();
        String soDienThoaiMoi = txtTimKH12.getText();
        String emailMoi = txtTimKH10.getText();

        Date selectedDate = jDateChooser3.getDate();
        LocalDate ngaySinhMoi = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        String gioiTinh = String.valueOf(jComboBox12.getSelectedItem());
        
        String caLamViecStr = String.valueOf(jComboBox13.getSelectedItem());
        CaLamViec caLamViec;
        if(caLamViecStr.equalsIgnoreCase("Sáng")){
            caLamViec = new CaLamViec("CA01");
        }
        else if(caLamViecStr.equalsIgnoreCase("Chiều")){
            caLamViec = new CaLamViec("CA02");
        }
        else{
            caLamViec = new CaLamViec("CA03");
        }
               
        String cVu = String.valueOf(jComboBox11.getSelectedItem());
        ChucVu chucVu;
        if(cVu.equalsIgnoreCase("Thu ngân")){
            chucVu = ChucVu.THUNGAN;
        }
        else{
            chucVu = ChucVu.QUANLY;
        }
        
        int tinhTrangLamViec;
        if(String.valueOf(jComboBox10.getSelectedItem()).equalsIgnoreCase("Đang làm việc")){
            tinhTrangLamViec = 1;
        }
        else{
            tinhTrangLamViec = 0;
        }
        
        TaiKhoan taiKhoan = TaiKhoan.ChuyenString(maNVSua);
        NhanVien nvMoi = new NhanVien(maNVSua, tenNVMoi, ngaySinhMoi, soDienThoaiMoi, 
                gioiTinh, emailMoi, tinhTrangLamViec, caLamViec.getMaCa(), chucVu);
        if(jTable2.getSelectedRow()<0){
            JOptionPane.showMessageDialog(null, "Hãy chọn dòng cần sửa");
        }
        else if (validData()) {
            dao_nhanvien.updateNhanVien(maNVSua, nvMoi);
            System.out.println("Update: "+nvMoi);
        }
        
    }//GEN-LAST:event_btnSuaNhanVien1ActionPerformed

    public void xoaRong(){
        txtTimKH9.setText("");
        txtTimKH12.setText("");
        txtTimKH10.setText("");
        jComboBox10.setSelectedIndex(0);
        jComboBox11.setSelectedIndex(0);
        jComboBox12.setSelectedIndex(0);
        jComboBox13.setSelectedIndex(0); 
    }
    
    private void btnSuaNhanVien2ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btnSuaNhanVien2ActionPerformed

        try {
            loadData();
        } catch (SQLException ex) {
            Logger.getLogger(FrmNhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(String.valueOf(jComboBox11.getSelectedItem()).equalsIgnoreCase("Thu ngân")){
            thietLapMaNhanVienTN();
        }
        else{
            thietLapMaNhanVienQL();
        }
        txtTimKH11.setEnabled(false);
        xoaRong();
            
    }//GEN-LAST:event_btnSuaNhanVien2ActionPerformed

    private void jComboBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox11ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jComboBox11ActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) throws RemoteException {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        exportExcel();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void lblNameLoginAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblNameLoginAncestorAdded
        // TODO add your handling code here:
        lblNameLogin.setText(gui.FrmLogin.tenNguoiDung);
    }//GEN-LAST:event_lblNameLoginAncestorAdded

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
       
    }//GEN-LAST:event_jLabel1MouseClicked

    private void btnQuayLai2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuayLai2ActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlChange, pnlCenter);
    }//GEN-LAST:event_btnQuayLai2ActionPerformed

    private void btnHuy1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuy1ActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlChange, pnlCenter);
    }//GEN-LAST:event_btnHuy1ActionPerformed

    private void btnLuuNV1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuNV1ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnLuuNV1ActionPerformed

    private void btnSuaNhanVienActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btnSuaNhanVienActionPerformed
        // TODO add your handling code here:

        showPanelChange(pnlChange, pnlCenterSua);
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        String maNhanVien = model.getValueAt(jTable2.getSelectedRow(), 1).toString();
        NhanVien nv = dao_nhanvien.getNVTheoMa(maNhanVien);

        txtTimKH11.setText(model.getValueAt(jTable2.getSelectedRow(), 1).toString());
        txtTimKH9.setText(model.getValueAt(jTable2.getSelectedRow(), 2).toString());
        txtTimKH12.setText(nv.getSoDienThoai());
        txtTimKH10.setText(nv.getEmail());
        txtTimKH11.setEnabled(false);
        LocalDate ngaySinh = nv.getNgaySinh();
        // Chuyen tu LocalDate sang Date
        Date date = Date.from(ngaySinh.atStartOfDay(ZoneId.systemDefault()).toInstant());
        jDateChooser3.setDate(date);
    }//GEN-LAST:event_btnSuaNhanVienActionPerformed

    private void btnQuayLai1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuayLai1ActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlChange, pnlCenter);
    }//GEN-LAST:event_btnQuayLai1ActionPerformed

    private void txtTimKH8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH8ActionPerformed

    private void txtTimKH7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH7ActionPerformed

    private void txtTimKH5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH5ActionPerformed

    private void txtTimKH2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH2ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            showPanelChange(pnlChange, pnlCenterXem);
        }
    }//GEN-LAST:event_jTable3MouseClicked

    private void btnQuayLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuayLaiActionPerformed
        // TODO add your handling code here:nl
        showPanelChange(pnlChange, pnlCenter);
    }//GEN-LAST:event_btnQuayLaiActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlChange, pnlCenter);
    }//GEN-LAST:event_btnHuyActionPerformed

    private void btnLuuNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuNVActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnLuuNVActionPerformed

    private void txtTimKH6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH6ActionPerformed

    private void txtTimKH4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH4ActionPerformed

    private void txtTimKH4ComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_txtTimKH4ComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH4ComponentAdded

    private void txtTimKH3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH3ActionPerformed

    private void txtTimKH1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH1ActionPerformed
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnHuy1;
    private javax.swing.JButton btnLuuNV;
    private javax.swing.JButton btnLuuNV1;
    private javax.swing.JButton btnQuayLai;
    private javax.swing.JButton btnQuayLai1;
    private javax.swing.JButton btnQuayLai2;
    private javax.swing.JButton btnSuaNhanVien;
    private javax.swing.JButton btnSuaNhanVien1;
    private javax.swing.JButton btnSuaNhanVien2;
    private javax.swing.JButton btnThemNV;
    private javax.swing.JLabel date;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox10;
    private javax.swing.JComboBox<String> jComboBox11;
    private javax.swing.JComboBox<String> jComboBox12;
    private javax.swing.JComboBox<String> jComboBox13;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
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
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel lblNameLogin;
    private menuGui.MenuScrollPane menuScrollPane2;
    private menuGui.MenuScrollPane menuScrollPane3;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlCenterKHchange;
    private javax.swing.JPanel pnlCenterKHchange1;
    private javax.swing.JPanel pnlCenterKHchange2;
    private javax.swing.JPanel pnlCenterKHchange3;
    private javax.swing.JPanel pnlCenterSua;
    private javax.swing.JPanel pnlCenterThem;
    private javax.swing.JPanel pnlCenterXem;
    private javax.swing.JPanel pnlChange;
    private javax.swing.JPanel pnlTableInit;
    private javax.swing.JPanel pnlTableInit1;
    private javax.swing.JPanel pnlTopLeft;
    private javax.swing.JPanel pnlTopLeft1;
    private javax.swing.JPanel pnlTopLeft2;
    private javax.swing.JPanel pnlTopLeft3;
    private javax.swing.JTextField txtTimKH;
    private javax.swing.JTextField txtTimKH1;
    private javax.swing.JTextField txtTimKH10;
    private javax.swing.JTextField txtTimKH11;
    private javax.swing.JTextField txtTimKH12;
    private javax.swing.JTextField txtTimKH2;
    private javax.swing.JTextField txtTimKH3;
    private javax.swing.JTextField txtTimKH4;
    private javax.swing.JTextField txtTimKH5;
    private javax.swing.JTextField txtTimKH6;
    private javax.swing.JTextField txtTimKH7;
    private javax.swing.JTextField txtTimKH8;
    private javax.swing.JTextField txtTimKH9;
    // End of variables declaration//GEN-END:variables
    private List<NhanVien> data = new ArrayList<>();
    
}
