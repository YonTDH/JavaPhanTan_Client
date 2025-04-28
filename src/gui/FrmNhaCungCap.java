/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import lookup.LookupNaming;

import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dao.DAO_NhaCungCap;
import entity.HoaDon;
import entity.NhaCungCap;





/**
 *
 * @author nguyen chau tai
 */
public class FrmNhaCungCap extends javax.swing.JPanel implements MouseListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4553049342019672118L;
	/**
     * Creates new form FrmDSKhachHang
     */
    private DAO_NhaCungCap dao_ncc = LookupNaming.lookup_NhaCungCap();
    private List<NhaCungCap> data = new ArrayList<>();

    private FrmChinh frm = new FrmChinh();
    private Thread thread = null;
    
    public FrmNhaCungCap() throws RemoteException {
        initComponents();
        kiemTraThem();
        loadData();
        thietLapMaNCC();
        
        thread = new Thread(this::setTimeAuto);
        thread.start();
        
        txtTimKH.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                try {
					locNhaCungCap();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }

            @Override
            public void keyPressed(KeyEvent e) {
                try {
					locNhaCungCap();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }

            @Override
            public void keyReleased(KeyEvent e) {
                try {
					locNhaCungCap();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        });
        
        //F1
        InputMap inputMap1 = btnThemSP.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap1.put(KeyStroke.getKeyStroke("F1"), "doSomething1");

        Action action1 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnThemSPActionPerformed(e);
            }
        };

        btnThemSP.getActionMap().put("doSomething1", action1);
        
        //F2
        InputMap inputMap2 = btnSuaKH2.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap2.put(KeyStroke.getKeyStroke("F2"), "doSomething2");

        Action action2 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSuaKH2ActionPerformed(e);
            }
        };
        btnSuaKH2.getActionMap().put("doSomething2", action2);
        
        // F3
        InputMap inputMap3 = btnSuaKH3.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap3.put(KeyStroke.getKeyStroke("F3"), "doSomething3");

        Action action3 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSuaKH3ActionPerformed(e);
            }
        };
        btnSuaKH3.getActionMap().put("doSomething3", action3);
        
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
    

    public void showPanelChange(JPanel a, JPanel b) {
        a.removeAll();
        a.add(b);
        a.repaint();
        a.revalidate();
    }

    public void updateData() throws RemoteException {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
        loadData();
    }

    public void deleteTable() {
        DefaultTableModel dm = (DefaultTableModel) jTable2.getModel();
        dm.getDataVector().removeAllElements();
    }

    public void loadData() throws RemoteException {
        deleteTable();
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        data = dao_ncc.getALLNhaCungCap_20();
        int stt = 1;
        for (NhaCungCap ncc : data) {
            String[] newRow = {String.format("%s", stt),
                String.format("%s", ncc.getMaNCC()),
                String.format("%s", ncc.getTenNCC()),
                String.format("%s", ncc.getEmail()),
                String.format("%s", ncc.getSoDienThoai())
            };
            stt++;
            model.addRow(newRow);
        }
    }

//    public boolean validData() {
//        String tenNCC = txtTimKH5.getText().trim();
//        String soDienThoai = txtTimKH9.getText().trim();
//        String email = txtTimKH6.getText().trim();
//        String diaChi = jTextArea5.getText();
//        String ghiChu = jTextArea6.getText();
//
//        Pattern pTenNCC = Pattern.compile("^[\\p{L} .'-]+$");
//        Matcher mTenNCC = pTenNCC.matcher(tenNCC);
//        if (!(mTenNCC.matches())) {
//            return false;
//        }
//
//        Pattern pSDT = Pattern.compile("^\\d{8,11}$");
//        Matcher mSDT = pSDT.matcher(soDienThoai);
//        if (!(mSDT.matches())) {
//            return false;
//        }
//
//        Pattern pEmail = Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+$");
//        Matcher mEmail = pEmail.matcher(email);
//        if (!(mEmail.matches())) {
//            return false;
//        }
//
//        if (diaChi.length() == 0) {
//            return false;
//        }
//        return true;
//    }

    public boolean validData() {
        String tenNCC = txtTimKH5.getText().trim();
        String soDienThoai = txtTimKH9.getText().trim();
        String email = txtTimKH6.getText().trim();
        String diaChi = jTextArea5.getText();
        String ghiChu = jTextArea6.getText();

        Pattern pTenNCC = Pattern.compile("^\\p{Lu}[\\p{L} .'-]*$");
        Matcher mTenNCC = pTenNCC.matcher(tenNCC);
        if (!(mTenNCC.matches())) {
            return false;
        }

        Pattern pSDT = Pattern.compile("^\\d{8,11}$");
        Matcher mSDT = pSDT.matcher(soDienThoai);
        if (!(mSDT.matches())) {
            return false;
        }

        Pattern pEmail = Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+$");
        Matcher mEmail = pEmail.matcher(email);
        if (!(mEmail.matches())) {
            return false;
        }

        if (diaChi.length() == 0) {
            return false;
        }
        return true;
    }

    public void kiemTraThem(){
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
                thietLapLabel(e);
            }

            private void thietLapLabel(KeyEvent e) {
                String tenNCC = txtTimKH5.getText().trim();
                String soDienThoai = txtTimKH9.getText().trim();
                String email = txtTimKH6.getText().trim();
                String diaChi = jTextArea5.getText();
                String ghiChu = jTextArea6.getText();

                Pattern pTenNCC = Pattern.compile("^\\p{Lu}[\\p{L} .'-]*$");
                Matcher mTenNCC = pTenNCC.matcher(tenNCC);
                if(txtTimKH5.getText().length()==0){
                    jLabel56.setText("Tên NCC không được rỗng");
                    jLabel56.setForeground(Color.RED);
                }
                else if (!(mTenNCC.matches())) {
                    jLabel56.setText("Tên NCC phải có dạng X X ...");
                    jLabel56.setForeground(Color.RED);
                }
                else{
                    jLabel56.setText("(*)");
                }

                Pattern pSDT = Pattern.compile("^\\d{8,11}$");
                Matcher mSDT = pSDT.matcher(soDienThoai);
                if(txtTimKH9.getText().length()==0){
                    jLabel43.setText("SDT không được rỗng");
                    jLabel43.setForeground(Color.RED);
                }
                else if (!(mSDT.matches())) {
                     jLabel43.setText("SDT phải từ 8-11 số");
                     jLabel56.setForeground(Color.RED);
                }
                else{
                    jLabel43.setText("(*)");
                }

                Pattern pEmail = Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+$");
                Matcher mEmail = pEmail.matcher(email);
                if(txtTimKH6.getText().length()==0){
                    jLabel57.setText("Email không được rỗng");
                    jLabel57.setForeground(Color.RED);
                }
                else if (!(mEmail.matches())) {
                    jLabel57.setText("Email phải có dạng exam@domainName");
                    jLabel57.setForeground(Color.RED);
                }
                else{
                    jLabel57.setText("(*)");
                }

                if (diaChi.length() == 0) {
                   jLabel58.setText("Địa chỉ không được rỗng");
                   jLabel58.setForeground(Color.RED);
                }
                else{
                    jLabel58.setText("(*)");
                }
            }
        };

        // Áp dụng KeyListener cho mỗi JTextField
        txtTimKH5.addKeyListener(keyListener);
        txtTimKH9.addKeyListener(keyListener);
        txtTimKH6.addKeyListener(keyListener);
        jTextArea5.addKeyListener(keyListener);
        
    }
    
    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
    
    private String getStringCellValue(Cell cell) {
		return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "";
    }

    private Double getNumericCellValue(Cell cell) {
            return cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0;
    }
    
//    public void exportExcel() throws RemoteException {
//    	List<NhaCungCap> dsNCC = dao_ncc.getALLNhaCungCap();
//        try {
//                JFileChooser jFileChooser = new JFileChooser();
//                jFileChooser.showSaveDialog(this);
//                jFileChooser.setDialogTitle("Select Folder");
//                File saveFile = jFileChooser.getSelectedFile();
//                if (saveFile != null) {
//                    saveFile = new File(saveFile.toString() + ".xlsx");
//                    XSSFWorkbook wordbook = new XSSFWorkbook();
//                    XSSFSheet sheet = wordbook.createSheet("DSNhaCungCap");
//                    XSSFRow row = null;
//                    Cell cell = null;
//                    row = sheet.createRow(1);
//                    cell = row.createCell(0, CellType.STRING);
//                    cell.setCellValue("Dach sách Nhà cung cấp");
//                    row = sheet.createRow(2);
//                    cell = row.createCell(0, CellType.STRING);
//                    cell.setCellValue("STT");
//                    cell = row.createCell(1, CellType.STRING);
//                    cell.setCellValue("Mã NCC");
//                    cell = row.createCell(2, CellType.STRING);
//                    cell.setCellValue("Tên Nhà cung cấp");
//                    cell = row.createCell(3, CellType.STRING);
//                    cell.setCellValue("Số điện thoại");
//                    cell = row.createCell(4, CellType.STRING);
//                    cell.setCellValue("Email");
//                    cell = row.createCell(5, CellType.STRING);
//                    cell.setCellValue("Địa chỉ");
//                    cell = row.createCell(6, CellType.STRING);
//                    cell.setCellValue("Ghi chú");
//                    cell = row.createCell(7, CellType.STRING);
//
//                    for (int i = 0; i < dsNCC.size(); i++) {
//                        row = sheet.createRow(3 + i);
//                        cell = row.createCell(0, CellType.NUMERIC);
//                        cell.setCellValue(i + 1);
//                        cell = row.createCell(1, CellType.STRING);
//                        cell.setCellValue(dsNCC.get(i).getMaNCC());
//                        cell = row.createCell(2, CellType.STRING);
//                        cell.setCellValue(dsNCC.get(i).getTenNCC());
//                        cell = row.createCell(3, CellType.STRING);
//                        cell.setCellValue(dsNCC.get(i).getSoDienThoai());
//                        cell = row.createCell(4, CellType.STRING);
//                        cell.setCellValue(dsNCC.get(i).getEmail());
//                        cell = row.createCell(5, CellType.STRING);
//                        cell.setCellValue(dsNCC.get(i).getDiaChiNCC());
//                        cell = row.createCell(6, CellType.STRING);
//                        cell.setCellValue(dsNCC.get(i).getGhiChu());
//                        cell = row.createCell(7, CellType.STRING);
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
                	List<NhaCungCap> dsNCC = dao_ncc.getALLNhaCungCap();
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.showSaveDialog(null);
                    jFileChooser.setDialogTitle("Select Folder");
                    File saveFile = jFileChooser.getSelectedFile();
                    if (saveFile != null) {
                        saveFile = new File(saveFile.toString() + ".xlsx");
                        XSSFWorkbook wordbook = new XSSFWorkbook();
                        XSSFSheet sheet = wordbook.createSheet("DSNhaCungCap");
                        XSSFRow row = null;
                        Cell cell = null;
                        row = sheet.createRow(1);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellValue("Danh sách Nhà cung cấp");
                        row = sheet.createRow(2);
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellValue("STT");
                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellValue("Mã NCC");
                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellValue("Tên Nhà cung cấp");
                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellValue("Số điện thoại");
                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellValue("Email");
                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellValue("Địa chỉ");
                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellValue("Ghi chú");
                        cell = row.createCell(7, CellType.STRING);

                        for (int i = 0; i < dsNCC.size(); i++) {
                            row = sheet.createRow(3 + i);
                            cell = row.createCell(0, CellType.NUMERIC);
                            cell.setCellValue(i + 1);
                            cell = row.createCell(1, CellType.STRING);
                            cell.setCellValue(dsNCC.get(i).getMaNCC());
                            cell = row.createCell(2, CellType.STRING);
                            cell.setCellValue(dsNCC.get(i).getTenNCC());
                            cell = row.createCell(3, CellType.STRING);
                            cell.setCellValue(dsNCC.get(i).getSoDienThoai());
                            cell = row.createCell(4, CellType.STRING);
                            cell.setCellValue(dsNCC.get(i).getEmail());
                            cell = row.createCell(5, CellType.STRING);
                            cell.setCellValue(dsNCC.get(i).getDiaChiNCC());
                            cell = row.createCell(6, CellType.STRING);
                            cell.setCellValue(dsNCC.get(i).getGhiChu());
                            cell = row.createCell(7, CellType.STRING);
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
    
    private void locNhaCungCap() throws RemoteException {
        String duLieuTim = txtTimKH.getText();
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        data = dao_ncc.locNhaCungCap(duLieuTim);
        int iD = 1;
        try {
            model.setRowCount(0);
            jTable2.clearSelection();
            for (NhaCungCap ncc : data) {
                model.addRow(new String[]{String.valueOf(iD),
                    ncc.getMaNCC(),
                    ncc.getTenNCC(),
                    ncc.getEmail(),
                    ncc.getSoDienThoai()
                });
                iD++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
     }
    
    
    

//    public String createMaNCC() {
//    	ArrayList<HoaDon> listHD = hd_dao.getAllHoaDons();
//    	Integer count = 1;
//    	Integer countNew = 0;
//    	for(HoaDon hd : listHD) {
//    		countNew = Integer.parseInt(hd.getMaHD().substring(2));
//    		if(countNew > count) {
//    			break;
//    		}
//    		if(countNew >= count) {
//    			count = (++count);
//    		}
//    	}
//    	String res5 = count.toString().length() == 1 ? ("HD0000" + count)
//    			: count.toString().length() == 2 ? ("HD000" + count) 
//    					: count.toString().length() == 3 ? ("HD00" + count)
//    							: count.toString().length() == 4 ? ("HD0" + count)
//    									: ("HD" + count);
//    	return res5;
//    }
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
        jLabel3 = new javax.swing.JLabel();
        pnlCenterKHchange = new javax.swing.JPanel();
        pnlInit = new javax.swing.JPanel();
        menuScrollPane2 = new menuGui.MenuScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel25 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtTimKH13 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        txtTimKH5 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        txtTimKH9 = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        txtTimKH6 = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jLabel50 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea6 = new javax.swing.JTextArea();
        btnThemSP = new javax.swing.JButton();
        btnSuaKH2 = new javax.swing.JButton();
        btnSuaKH3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblNameLogin = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1690, 787));
        setLayout(new java.awt.BorderLayout());

        pnlChange.setLayout(new java.awt.BorderLayout());

        pnlCenter.setBackground(new java.awt.Color(153, 153, 153));
        pnlCenter.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(240, 242, 245));

        pnlTopLeft.setBackground(new java.awt.Color(204, 204, 255));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel5.setText("Thông tin nhà cung cấp");

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

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/magnifying-glass.png"))); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimKH, javax.swing.GroupLayout.DEFAULT_SIZE, 1173, Short.MAX_VALUE)))
                .addGap(6, 6, 6))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(txtTimKH, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCenterKHchange.setBackground(new java.awt.Color(250, 250, 250));

        pnlInit.setBackground(new java.awt.Color(250, 250, 250));
        pnlInit.setPreferredSize(new java.awt.Dimension(1265, 621));

        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã nhà cung cấp", "Tên nhà cung cấp", "Email", "Số điện thoại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel44.setText("Mã nhà cung cấp");

        jLabel43.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel43.setText("(*)");

        txtTimKH13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH13ActionPerformed(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel42.setText("Tên nhà cung cấp");

        jLabel55.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel55.setText("(*)");

        txtTimKH5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH5ActionPerformed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel41.setText("Số điện thoại");

        jLabel56.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel56.setText("(*)");

        txtTimKH9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH9ActionPerformed(evt);
            }
        });

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel49.setText("Email");

        jLabel57.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel57.setText("(*)");

        txtTimKH6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH6ActionPerformed(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel46.setText("Địa chỉ");

        jLabel58.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel58.setText("(*)");

        jTextArea5.setColumns(20);
        jTextArea5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextArea5.setRows(5);
        jScrollPane5.setViewportView(jTextArea5);

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel50.setText("Ghi chú");

        jTextArea6.setColumns(20);
        jTextArea6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextArea6.setRows(5);
        jScrollPane6.setViewportView(jTextArea6);

        btnThemSP.setBackground(new java.awt.Color(3, 136, 253));
        btnThemSP.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemSP.setForeground(new java.awt.Color(255, 255, 255));
        btnThemSP.setText("Thêm nhà cung cấp (F1)");
        btnThemSP.setFocusable(false);
        btnThemSP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemSP.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnThemSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSPActionPerformed(evt);
            }
        });

        btnSuaKH2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuaKH2.setForeground(new java.awt.Color(3, 136, 253));
        btnSuaKH2.setText("Sửa nhà cung cấp (F2)");
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
        btnSuaKH3.setText("Làm mới (F3)");
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
                    .addComponent(txtTimKH13, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTimKH5)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTimKH9)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTimKH6))
                .addGap(73, 73, 73)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                    .addComponent(jScrollPane6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnThemSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSuaKH2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSuaKH3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(54, 54, 54))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(jLabel55)
                    .addComponent(jLabel46)
                    .addComponent(jLabel58))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(txtTimKH13, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(jLabel56))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimKH5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel25Layout.createSequentialGroup()
                            .addComponent(btnThemSP, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSuaKH2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel43)
                    .addComponent(jLabel50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(txtTimKH9, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel49)
                            .addComponent(jLabel57))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimKH6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSuaKH3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlInitLayout = new javax.swing.GroupLayout(pnlInit);
        pnlInit.setLayout(pnlInitLayout);
        pnlInitLayout.setHorizontalGroup(
            pnlInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInitLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(menuScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlInitLayout.setVerticalGroup(
            pnlInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInitLayout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlCenterKHchangeLayout = new javax.swing.GroupLayout(pnlCenterKHchange);
        pnlCenterKHchange.setLayout(pnlCenterKHchangeLayout);
        pnlCenterKHchangeLayout.setHorizontalGroup(
            pnlCenterKHchangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlInit, javax.swing.GroupLayout.DEFAULT_SIZE, 1249, Short.MAX_VALUE)
        );
        pnlCenterKHchangeLayout.setVerticalGroup(
            pnlCenterKHchangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCenterKHchangeLayout.createSequentialGroup()
                .addComponent(pnlInit, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(6, 7, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCenterKHchange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        javax.swing.GroupLayout pnlTopLeftLayout = new javax.swing.GroupLayout(pnlTopLeft);
        pnlTopLeft.setLayout(pnlTopLeftLayout);
        pnlTopLeftLayout.setHorizontalGroup(
            pnlTopLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlTopLeftLayout.setVerticalGroup(
            pnlTopLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

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
                jLabel2MouseClicked(evt);
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
                .addContainerGap(1087, Short.MAX_VALUE))
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
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(393, Short.MAX_VALUE))
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
        jLabel6.setText("Nhà cung cấp");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1266, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNameLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
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
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCenter.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        pnlChange.add(pnlCenter, java.awt.BorderLayout.CENTER);

        add(pnlChange, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtTimKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKHActionPerformed
        // TODO add your handling code here
        
    }//GEN-LAST:event_txtTimKHActionPerformed

    public void thietLapMaNCC() throws RemoteException {
        // Lấy ngày hiện tại và lấy mã nhân viên từ DAO
//        LocalDate ngayHienTai = LocalDate.now();
//        String ngayHienTaiStr = ngayHienTai.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
//        String maNCCDB = dao_ncc.getMaNhaCungCapDB();
//        System.out.println(maNCCDB);
//
//        String phanSTT = maNCCDB.substring(maNCCDB.length() - 6, maNCCDB.length()); // Lấy 6 kí tự cuối của maNhanVien
//        int index = Integer.parseInt(phanSTT); // Chuyển 6 kí tự cuối về int
//
//        // Tạo stt tăng nếu ngày là ngày hôm sau và set 6 kí số phía sau về dạng "-000001"
//        int iStart = 1;
//        String sttStart = String.format("%06d", iStart);
//
//        String check = "NCC" + ngayHienTaiStr + "-" + String.format("%06d", index);
//        // Mã đã có trong db thì tăng mã lên 1
//        if (check.equalsIgnoreCase(maNCCDB)) {
//            index++;
//            String stt = String.format("%06d", index);
//            String sttTangTN = "NCC" + ngayHienTaiStr + "-" + stt;
//            txtTimKH13.setText(sttTangTN);
//        } // Chưa có thì set là 1
//        else {
//            txtTimKH13.setText("NCC" + ngayHienTaiStr + "-" + sttStart);
//        }
//        txtTimKH13.setEnabled(false);
        
        
        LocalDate d = LocalDate.now();
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("ddMMyyyy");
        String format = d.format(myFormatDate);
        Integer count = 1;
        String hdID = "";
        do {
            String tempID = count.toString().length() == 1 ? ("NCC" + format + "-00000" + count)
                    : count.toString().length() == 2 ? ("NCC" + format + "-0000" + count)
                    : count.toString().length() == 3 ? ("NCC" + format + "-000" + count)
                    : count.toString().length() == 4 ? ("NCC" + format + "-00" + count)
                    : count.toString().length() == 5 ? ("NCC" + format + "-0" + count)
                    : ("NCC" + format + "-" + count);

            NhaCungCap existingBill = dao_ncc.getNCCTheoMa(tempID);
            if (existingBill == null) {
                hdID = tempID;
                break;
            }
            count++;
        } while (true);

        txtTimKH13.setText(hdID);
        txtTimKH13.setEnabled(false);
    }
    
    public String thietLapMaNCCStr() throws RemoteException {
        // Lấy ngày hiện tại và lấy mã nhân viên từ DAO
        LocalDate ngayHienTai = LocalDate.now();
        String ngayHienTaiStr = ngayHienTai.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String maNCCDB = dao_ncc.getMaNhaCungCapDB();
        System.out.println(maNCCDB);

        String phanSTT = maNCCDB.substring(maNCCDB.length() - 6, maNCCDB.length()); // Lấy 6 kí tự cuối của maNhanVien
        int index = Integer.parseInt(phanSTT); // Chuyển 6 kí tự cuối về int

        // Tạo stt tăng nếu ngày là ngày hôm sau và set 6 kí số phía sau về dạng "-000001"
        int iStart = 1;
        String sttStart = String.format("%06d", iStart);

        String check = "NCC" + ngayHienTaiStr + "-" + String.format("%06d", index);
        // Mã đã có trong db thì tăng mã lên 1
        if (check.equalsIgnoreCase(maNCCDB)) {
            index++;
            String stt = String.format("%06d", index);
            String sttTangTN = "NCC" + ngayHienTaiStr + "-" + stt;
            return sttTangTN;
        } // Chưa có thì set là 1
        return String.format("%s", "NCC" + ngayHienTaiStr + "-" + sttStart);
    }
 
    private void btnThemSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSPActionPerformed
        // TODO add your handling code here:
        
//        thietLapMaNCC();
//        txtTimKH13.setEnabled(false);
        String maNCC = txtTimKH13.getText();
        String tenNCC = txtTimKH5.getText();
        String diaChi = jTextArea5.getText();
        String soDienThoai = txtTimKH9.getText();
        String email = txtTimKH6.getText();
        String ghiChu = jTextArea6.getText();

        if ((tenNCC.length()==0) || (diaChi.length()==0) || (soDienThoai.length()==0) || (email.length()==0)) {
            JOptionPane.showMessageDialog(null, "Chưa nhập đầy đủ dữ liệu");
        }
        else if(validData()){
            NhaCungCap nccThem = new NhaCungCap(maNCC, tenNCC, diaChi, soDienThoai, email, ghiChu);
            try {
				dao_ncc.themNhaCungCap(nccThem);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

    }//GEN-LAST:event_btnThemSPActionPerformed

    
    
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
//            showPanelChange(pnlChange, pnlCenterSua);
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            String maNCC = model.getValueAt(jTable2.getSelectedRow(), 1).toString();
            NhaCungCap ncc;
			try {
				ncc = dao_ncc.getNCCTheoMa(maNCC);
				txtTimKH13.setText(model.getValueAt(jTable2.getSelectedRow(), 1).toString());
	            txtTimKH5.setText(model.getValueAt(jTable2.getSelectedRow(), 2).toString());
	            txtTimKH6.setText(model.getValueAt(jTable2.getSelectedRow(), 3).toString());
	            txtTimKH9.setText(model.getValueAt(jTable2.getSelectedRow(), 4).toString());
	            jTextArea5.setText(ncc.getDiaChiNCC());
	            jTextArea6.setText(ncc.getGhiChu());
	            txtTimKH13.setEnabled(false);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
//                DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
//                dao_ncc = new DAO_NhaCungCap();
//                String maNCC = model.getValueAt(jTable2.getSelectedRow(), 1).toString();
//                NhaCungCap ncc = dao_ncc.getNCCTheoMa(maNCC);
//             
//                showPanelChange(pnlChange, pnlCenterXemNCC);
//                Object o = evt.getSource();
//            if(o.equals(jTable2)){
//                txtTimKH12.setText(model.getValueAt(jTable2.getSelectedRow(), 1).toString());
//                txtTimKH2.setText(model.getValueAt(jTable2.getSelectedRow(), 2).toString());
//                txtTimKH4.setText(model.getValueAt(jTable2.getSelectedRow(), 3).toString());
//                txtTimKH8.setText(model.getValueAt(jTable2.getSelectedRow(), 4).toString());
//                jTextArea3.setText(ncc.getDiaChiNCC());
//                jTextArea4.setText(ncc.getGhiChu());
//            }
        }


    }//GEN-LAST:event_jTable2MouseClicked

    private void txtTimKH5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH5ActionPerformed

    private void txtTimKH6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH6ActionPerformed

    private void txtTimKH9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKH9ActionPerformed

    private void txtTimKH13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKH13ActionPerformed
        // TODO add your handling code here:
        try {
			thietLapMaNCC();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }//GEN-LAST:event_txtTimKH13ActionPerformed

    private void btnSuaKH2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaKH2ActionPerformed
        // TODO add your handling code here:
        
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        String maNCCSua = model.getValueAt(jTable2.getSelectedRow(), 1).toString();
        String tenNCCMoi = txtTimKH5.getText();
        String soDienThoaiMoi = txtTimKH9.getText();
        String emailMoi = txtTimKH6.getText();
        String diaChiMoi = jTextArea5.getText();
        String ghiChuMoi = jTextArea6.getText();
        NhaCungCap nccMoi = new NhaCungCap(maNCCSua, tenNCCMoi, diaChiMoi, soDienThoaiMoi, emailMoi, ghiChuMoi);
        if(jTable2.getSelectedRow()<0)
            JOptionPane.showMessageDialog(null, "Hãy chọn dòng cần sửa");
        else if (validData()) {
            try {
				dao_ncc.updateNhaCungCap(maNCCSua, nccMoi);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println("NCC: "+nccMoi);
        }

    }//GEN-LAST:event_btnSuaKH2ActionPerformed

    public void xoaRong(){
        txtTimKH5.setText("");
        txtTimKH9.setText("");
        txtTimKH6.setText("");
        jTextArea5.setText("");
        jTextArea6.setText("");
    }
    
    private void btnSuaKH3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaKH3ActionPerformed
        // TODO add your handling code here:
        
        try {
			thietLapMaNCC();
			loadData();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        xoaRong();
    }//GEN-LAST:event_btnSuaKH3ActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        exportExcel();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        //importExcel();
        //loadData();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void lblNameLoginAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblNameLoginAncestorAdded
        // TODO add your handling code here:
        lblNameLogin.setText(gui.FrmLogin.tenNguoiDung);
    }//GEN-LAST:event_lblNameLoginAncestorAdded


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSuaKH2;
    private javax.swing.JButton btnSuaKH3;
    private javax.swing.JButton btnThemSP;
    private javax.swing.JLabel date;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JTextArea jTextArea6;
    private javax.swing.JLabel lblNameLogin;
    private menuGui.MenuScrollPane menuScrollPane2;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlCenterKHchange;
    private javax.swing.JPanel pnlChange;
    private javax.swing.JPanel pnlInit;
    private javax.swing.JPanel pnlTopLeft;
    private javax.swing.JTextField txtTimKH;
    private javax.swing.JTextField txtTimKH13;
    private javax.swing.JTextField txtTimKH5;
    private javax.swing.JTextField txtTimKH6;
    private javax.swing.JTextField txtTimKH9;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
