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
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

//import com.toedter.calendar.JDateChooser;
//
//import dao.DAO_KhuyenMai;
//import dao.DAO_NhanVien;
//import dao.DAO_NhomKhuyenMai;
//import dao.DAO_NhomSanPham;
//import entity.KhuyenMai;
//import entity.NhanVien;
//import entity.NhomKhuyenMai;
//import entity.NhomSanPham;
import iuh.fit.gui.FrmChinh;
import lookup.LookupNaming;

import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellType;
//import org.apache.poi.xssf.streaming.SXSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FrmKhuyenMai extends javax.swing.JPanel {

	private static final long serialVersionUID = -6086519692885478868L;
	private DAO_NhomSanPham dao_nsp = LookupNaming.lookup_NhomSanPham();
	private DAO_KhuyenMai dao_km = LookupNaming.lookup_KhuyenMai();
	private DAO_NhomKhuyenMai dao_nkm = LookupNaming.lookup_NhomKhuyenMai();
	private int add_update = 1; // thêm
	/**
	 * Creates new form FrmDSKhachHang
	 */
	private FrmChinh frm = new FrmChinh();
	private Thread thread = null;

	public FrmKhuyenMai() throws SQLException {
//		ConnectDB.getInstance().connect();
		initComponents();
		thread = new Thread(this::setTimeAuto);
		thread.start();
		thietLapMaKhuyenMai();
		try {
			loadJtable2();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// F1
		InputMap inputMap1 = btnSuaNhanVien3.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap1.put(KeyStroke.getKeyStroke("F1"), "doSomething1");

		Action action1 = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnSuaNhanVien3ActionPerformed(e);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		};

		btnSuaNhanVien3.getActionMap().put("doSomething1", action1);

		// F2
		InputMap inputMap2 = btnSuaNhanVien1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap2.put(KeyStroke.getKeyStroke("F2"), "doSomething2");

		Action action2 = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnSuaNhanVien1ActionPerformed(e);
				} catch (Exception e1) {
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
				btnSuaNhanVien2ActionPerformed(e);
			}
		};
		btnSuaNhanVien2.getActionMap().put("doSomething3", action3);
	}

	public ArrayList<String> layDanhSachCheckbox(JTable table) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		ArrayList<String> danhSachCheckbox = new ArrayList<>();

		for (int row = 0; row < model.getRowCount(); row++) {
			// Lấy giá trị của ô checkbox từ cột 2
			Boolean isChecked = (Boolean) model.getValueAt(row, 2);
			if (isChecked) {
				String maNhomSanPham = (String) model.getValueAt(row, 0);
				danhSachCheckbox.add(maNhomSanPham);
			}
		}

		return danhSachCheckbox;
	}

	public void timJtable2() {
		DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
		jTable2.setRowSorter(sorter);

		txtTimKH.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				tableModel.fireTableDataChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				tableModel.fireTableDataChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				tableModel.fireTableDataChanged();
			}
		});
		String searchTerm = txtTimKH.getText().toUpperCase();
		if (searchTerm.isEmpty()) {
			sorter.setRowFilter(RowFilter.regexFilter(txtTimKH.getText()));
		} else {
			Pattern pattern = Pattern.compile(Pattern.quote(searchTerm),
					Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			sorter.setRowFilter(new RowFilter<DefaultTableModel, Object>() {
				@Override
				public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Object> entry) {
					String maKm = entry.getValue(1).toString();
					String tenKm = entry.getValue(2).toString();

					return pattern.matcher(maKm).lookingAt() || pattern.matcher(tenKm).lookingAt();
				}
			});
		}
	}

	public void thietLapMaKhuyenMai() {
		String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int CODE_LENGTH = 10;
		SecureRandom random = new SecureRandom();
		StringBuilder codeBuilder = new StringBuilder();

		for (int i = 0; i < 10; i++) {
			int randomIndex = random.nextInt(CHARACTERS.length());
			char randomChar = CHARACTERS.charAt(randomIndex);
			codeBuilder.append(randomChar);
		}

		txtTimKH11.setText(codeBuilder.toString());
		txtTimKH11.setEnabled(false);
	}

	public void loadDataNhomSP1() throws RemoteException {
		DefaultTableModel dm = (DefaultTableModel) tableNhomSP1.getModel();
		dm.getDataVector().removeAllElements();
		dm.fireTableDataChanged();

		TableColumnModel columnModel = tableNhomSP1.getColumnModel();
		columnModel.getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JCheckBox checkBox = new JCheckBox();
				checkBox.setSelected((Boolean) value);
				return checkBox;
			}
		});
		columnModel.getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));

		List<NhomSanPham> dsNsp = dao_nsp.getAllNhomSanPham();
		for (NhomSanPham nsp : dsNsp) {
			dm.addRow(new Object[] { nsp.getMaNhomSanPham(), nsp.getTenNhomSanPham(), false });
		}
	}

	public void loadDataSua() throws RemoteException {
		DefaultTableModel dm = (DefaultTableModel) tableNhomSP1.getModel();
		dm.getDataVector().removeAllElements();
		dm.fireTableDataChanged();

		TableColumnModel columnModel = tableNhomSP1.getColumnModel();
		columnModel.getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JCheckBox checkBox = new JCheckBox();
				checkBox.setSelected((Boolean) value);
				return checkBox;
			}
		});
		columnModel.getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));

		List<NhomSanPham> dsNsp = dao_nsp.getAllNhomSanPham();
		for (NhomSanPham nsp : dsNsp) {
			if (xemNhomKm(nsp.getMaNhomSanPham())) {
				dm.addRow(new Object[] { nsp.getMaNhomSanPham(), nsp.getTenNhomSanPham(), true });
			} else
				dm.addRow(new Object[] { nsp.getMaNhomSanPham(), nsp.getTenNhomSanPham(), false });
		}
	}

	public boolean xemNhomKm(String maNhomSanPham) throws RemoteException {
		List<NhomKhuyenMai> dsNkm = dao_nkm.getAllNhomKM();
		for (NhomKhuyenMai nkm : dsNkm) {
			if (maNhomSanPham.equals(nkm.getNhomSanPham().getMaNhomSanPham())) {
				return true;
			}
		}
		return false;
	}

	public void timCbKM() {
		DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
		jTable2.setRowSorter(sorter);
		String searchTerm = jComboBox1.getSelectedItem().toString();

		RowFilter<DefaultTableModel, Object> rowFilter;
		if (searchTerm.equals("Đang hoạt động")) {
			rowFilter = RowFilter.regexFilter("Đang hoạt động");
		} else {
			rowFilter = RowFilter.regexFilter("Đã ngừng");
		}

		sorter.setRowFilter(rowFilter);
	}

	public void ChonTatCaCheckBox() {
		DefaultTableModel dm = (DefaultTableModel) tableNhomSP1.getModel();
		int rowCount = dm.getRowCount();

		for (int i = 0; i < rowCount; i++) {
			dm.setValueAt(true, i, 2); // 2 là chỉ số cột chứa checkbox trong JTable của bạn
		}
	}

	public void BoChonTatCaCheckBox() {
		DefaultTableModel dm = (DefaultTableModel) tableNhomSP1.getModel();
		int rowCount = dm.getRowCount();

		for (int i = 0; i < rowCount; i++) {
			dm.setValueAt(false, i, 2); // 2 là chỉ số cột chứa checkbox trong JTable của bạn
		}
	}

	public void setListData(JCheckBox[] newData) {
		jPanel1.setLayout(new FlowLayout());
		for (JCheckBox checkBox : newData) {
			jPanel1.add(checkBox);
			System.out.println(checkBox.getText());
		}
		jPanel1.revalidate();
		jPanel1.repaint();
	}

	public void deleteTable() {
		DefaultTableModel dm = (DefaultTableModel) jTable2.getModel();
		dm.getDataVector().removeAllElements();
		dm.fireTableDataChanged();
	}

	public void loadJtable2() throws RemoteException {
		deleteTable();
		List<KhuyenMai> dsKM = dao_km.getAlltbKM();

		DefaultTableModel tableModal = (DefaultTableModel) jTable2.getModel();
		int stt = 1;
		String tinhTrang = "";
		for (KhuyenMai km : dsKM) {
			dao_km.updateTinhTrang(km);
			tableModal.addRow(new Object[] { stt, km.getMaKhuyenMai(), km.getTenKhuyenMai(), km.getGhiChu(),
					km.getTyLeKhuyenMai(), km.getTrangThai() });
			stt++;
		}
	}

	public void addCheckbox() throws RemoteException {
		List<NhomSanPham> currentList = dao_nsp.getAllNhomSanPham();
		JCheckBox[] newList = new JCheckBox[currentList.size()];
		for (int i = 0; i < currentList.size(); i++) {
			NhomSanPham nsp = currentList.get(i);
			JCheckBox newCheckBox = new JCheckBox(nsp.getTenNhomSanPham());
			newList[i] = newCheckBox;
		}
		setListData(newList);
	}

	public void setTimeAuto() {
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
				date.setText(sdf_Gio.format(thoiGianHienTai) + " " + ngayTrongTuan + sdf_Ngay.format(thoiGianHienTai));

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

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogChonNSP = new javax.swing.JDialog(frm,"Chọn",false);
        pnlChonNCC1 = new javax.swing.JPanel();
        menuScrollPane10 = new menuGui.MenuScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        jLabel149 = new javax.swing.JLabel();
        txtTimKH10 = new javax.swing.JTextField();
        btnThemNV3 = new javax.swing.JButton();
        btnThemNV4 = new javax.swing.JButton();
        btnThemNV5 = new javax.swing.JButton();
        jDialogChonNSPTest = new javax.swing.JDialog(frm,"Chọn",false);
        pnlChonNCC2 = new javax.swing.JPanel();
        menuScrollPane12 = new menuGui.MenuScrollPane();
        tableNhomSP1 = new javax.swing.JTable();
        jPanel51 = new javax.swing.JPanel();
        jLabel150 = new javax.swing.JLabel();
        txtTimKH12 = new javax.swing.JTextField();
        btnThemNV2 = new javax.swing.JButton();
        btnThemNV6 = new javax.swing.JButton();
        btnThemNV7 = new javax.swing.JButton();
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
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel55 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        btnSuaNhanVien1 = new javax.swing.JButton();
        btnSuaNhanVien2 = new javax.swing.JButton();
        jTextAreaGhiChu = new javax.swing.JTextArea();
        txtTimKH13 = new javax.swing.JTextField();
        txtTimKH14 = new javax.swing.JTextField();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jLabel64 = new javax.swing.JLabel();
        txtTimKH16 = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        jComboBox12 = new javax.swing.JComboBox<>();
        btnSuaNhanVien3 = new javax.swing.JButton();
        jLabel54 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblNameLogin = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        jDialogChonNSP.setBackground(new java.awt.Color(250, 250, 250));
        jDialogChonNSP.setResizable(false);
        jDialogChonNSP.setSize(new java.awt.Dimension(786, 437));
        jDialogChonNSP.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                jDialogChonNSPWindowLostFocus(evt);
            }
        });

        pnlChonNCC1.setBackground(new java.awt.Color(250, 250, 250));

        menuScrollPane10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menuScrollPane10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 917, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 352, Short.MAX_VALUE)
        );

        menuScrollPane10.setViewportView(jPanel1);

        jPanel50.setBackground(new java.awt.Color(255, 255, 255));

        jLabel149.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel149.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/magnifying-glass.png"))); // NOI18N

        txtTimKH10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH10.setText("Nhập vào thông tin tìm kiếm...");
        txtTimKH10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH10.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimKH10FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTimKH10FocusLost(evt);
            }
        });
        txtTimKH10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH10ActionPerformed(evt);
            }
        });
        txtTimKH10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH10KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel50Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel149, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel50Layout.createSequentialGroup()
                    .addGap(87, 87, 87)
                    .addComponent(txtTimKH10, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel50Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel149, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel50Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(txtTimKH10, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        btnThemNV3.setBackground(new java.awt.Color(3, 136, 253));
        btnThemNV3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemNV3.setForeground(new java.awt.Color(255, 255, 255));
        btnThemNV3.setText("Chọn tất cả");
        btnThemNV3.setFocusable(false);
        btnThemNV3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemNV3.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnThemNV3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNV3ActionPerformed(evt);
            }
        });

        btnThemNV4.setBackground(new java.awt.Color(3, 136, 253));
        btnThemNV4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemNV4.setForeground(new java.awt.Color(255, 255, 255));
        btnThemNV4.setText("Xóa tất cả");
        btnThemNV4.setFocusable(false);
        btnThemNV4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemNV4.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnThemNV4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNV4ActionPerformed(evt);
            }
        });

        btnThemNV5.setBackground(new java.awt.Color(3, 136, 253));
        btnThemNV5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemNV5.setForeground(new java.awt.Color(255, 255, 255));
        btnThemNV5.setText("Lưu");
        btnThemNV5.setFocusable(false);
        btnThemNV5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemNV5.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnThemNV5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNV5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlChonNCC1Layout = new javax.swing.GroupLayout(pnlChonNCC1);
        pnlChonNCC1.setLayout(pnlChonNCC1Layout);
        pnlChonNCC1Layout.setHorizontalGroup(
            pnlChonNCC1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChonNCC1Layout.createSequentialGroup()
                .addGroup(pnlChonNCC1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlChonNCC1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(menuScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlChonNCC1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(btnThemNV3, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btnThemNV4, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(btnThemNV5, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlChonNCC1Layout.setVerticalGroup(
            pnlChonNCC1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChonNCC1Layout.createSequentialGroup()
                .addComponent(jPanel50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlChonNCC1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemNV3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemNV4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemNV5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(menuScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jDialogChonNSP.getContentPane().add(pnlChonNCC1, java.awt.BorderLayout.CENTER);

        jDialogChonNSPTest.setBackground(new java.awt.Color(250, 250, 250));
        jDialogChonNSPTest.setResizable(false);
        jDialogChonNSPTest.setSize(new java.awt.Dimension(786, 437));
        jDialogChonNSPTest.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                jDialogChonNSPTestWindowLostFocus(evt);
            }
        });

        pnlChonNCC2.setBackground(new java.awt.Color(250, 250, 250));

        menuScrollPane12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menuScrollPane12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tableNhomSP1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tableNhomSP1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã nhóm", "Tên nhóm", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableNhomSP1.setGridColor(new java.awt.Color(255, 255, 255));
        tableNhomSP1.setRowHeight(60);
        tableNhomSP1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableNhomSP1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableNhomSP1.getTableHeader().setReorderingAllowed(false);
        tableNhomSP1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableNhomSP1MouseClicked(evt);
            }
        });
        menuScrollPane12.setViewportView(tableNhomSP1);

        jPanel51.setBackground(new java.awt.Color(255, 255, 255));

        jLabel150.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel150.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/magnifying-glass.png"))); // NOI18N

        txtTimKH12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH12.setText("Nhập vào thông tin tìm kiếm...");
        txtTimKH12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH12.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimKH12FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTimKH12FocusLost(evt);
            }
        });
        txtTimKH12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH12ActionPerformed(evt);
            }
        });
        txtTimKH12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH12KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel51Layout = new javax.swing.GroupLayout(jPanel51);
        jPanel51.setLayout(jPanel51Layout);
        jPanel51Layout.setHorizontalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel51Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel150, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(847, Short.MAX_VALUE))
            .addGroup(jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel51Layout.createSequentialGroup()
                    .addGap(87, 87, 87)
                    .addComponent(txtTimKH12, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel51Layout.setVerticalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel51Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel150, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel51Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(txtTimKH12, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        btnThemNV2.setBackground(new java.awt.Color(3, 136, 253));
        btnThemNV2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemNV2.setForeground(new java.awt.Color(255, 255, 255));
        btnThemNV2.setText("Chọn tất cả");
        btnThemNV2.setFocusable(false);
        btnThemNV2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemNV2.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnThemNV2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNV2ActionPerformed(evt);
            }
        });

        btnThemNV6.setBackground(new java.awt.Color(3, 136, 253));
        btnThemNV6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemNV6.setForeground(new java.awt.Color(255, 255, 255));
        btnThemNV6.setText("Bỏ chọn tất cả");
        btnThemNV6.setFocusable(false);
        btnThemNV6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemNV6.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnThemNV6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNV6ActionPerformed(evt);
            }
        });

        btnThemNV7.setBackground(new java.awt.Color(3, 136, 253));
        btnThemNV7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemNV7.setForeground(new java.awt.Color(255, 255, 255));
        btnThemNV7.setText("Lưu");
        btnThemNV7.setFocusable(false);
        btnThemNV7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemNV7.setMargin(new java.awt.Insets(2, 10, 3, 10));

        javax.swing.GroupLayout pnlChonNCC2Layout = new javax.swing.GroupLayout(pnlChonNCC2);
        pnlChonNCC2.setLayout(pnlChonNCC2Layout);
        pnlChonNCC2Layout.setHorizontalGroup(
            pnlChonNCC2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChonNCC2Layout.createSequentialGroup()
                .addGroup(pnlChonNCC2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlChonNCC2Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(menuScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(87, 87, 87)
                        .addGroup(pnlChonNCC2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnThemNV6, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnThemNV2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnThemNV7, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        pnlChonNCC2Layout.setVerticalGroup(
            pnlChonNCC2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChonNCC2Layout.createSequentialGroup()
                .addComponent(jPanel51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlChonNCC2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(menuScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlChonNCC2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnThemNV2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnThemNV6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnThemNV7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jDialogChonNSPTest.getContentPane().add(pnlChonNCC2, java.awt.BorderLayout.CENTER);

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
        pnlTopLeft.setPreferredSize(new java.awt.Dimension(1285, 764));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setPreferredSize(new java.awt.Dimension(1288, 764));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setPreferredSize(new java.awt.Dimension(1288, 91));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel5.setText("Thông tin khuyến mãi");

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
        txtTimKH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKHKeyReleased(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang hoạt động", "Đã ngừng" }));
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
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã khuyến mãi", "Tên khuyến mãi", "Ghi chú", "Tỉ lệ", "Tình trạng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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

        txtTimKH11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH11ActionPerformed(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel51.setText("Mã khuyến mãi");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel50.setText("(*)");

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel48.setText("Tên khuyến mãi");

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 0, 0));
        jLabel52.setText("(*)");

        txtTimKH9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH9ActionPerformed(evt);
            }
        });
        txtTimKH9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH9KeyReleased(evt);
            }
        });

        jDateChooser3.setDateFormatString("dd-MM-yyyy");
        jDateChooser3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jDateChooser3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateChooser3MouseClicked(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel55.setText("Tiền đặt tối thiểu");

        jLabel47.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel47.setText("Ghi chú");

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel53.setText("Tỉ lệ khuyến mãi(%)");

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel60.setText("Khuyến mãi tối đa");

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel49.setText("Ngày kết thúc");

        btnSuaNhanVien1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuaNhanVien1.setForeground(new java.awt.Color(3, 136, 253));
        btnSuaNhanVien1.setText("Sửa khuyến mãi (F2)");
        btnSuaNhanVien1.setFocusable(false);
        btnSuaNhanVien1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSuaNhanVien1.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnSuaNhanVien1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnSuaNhanVien1ActionPerformed(evt);
				} catch (Exception e) {
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
                btnSuaNhanVien2ActionPerformed(evt);
            }
        });

        jTextAreaGhiChu.setColumns(20);
        jTextAreaGhiChu.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextAreaGhiChu.setRows(5);
        jTextAreaGhiChu.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtTimKH13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH13ActionPerformed(evt);
            }
        });
        txtTimKH13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH13KeyReleased(evt);
            }
        });

        txtTimKH14.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH14ActionPerformed(evt);
            }
        });
        txtTimKH14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH14KeyReleased(evt);
            }
        });

        jDateChooser4.setDateFormatString("dd-MM-yyyy");
        jDateChooser4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jDateChooser4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateChooser4MouseClicked(evt);
            }
        });

        jLabel64.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel64.setText("Ngày bắt đầu");

        txtTimKH16.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH16.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH16ActionPerformed(evt);
            }
        });
        txtTimKH16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH16KeyReleased(evt);
            }
        });

        jLabel65.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel65.setText("Tình trạng");

        jComboBox12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang hoạt động", "Đã ngừng" }));
        jComboBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox12ActionPerformed(evt);
            }
        });

        btnSuaNhanVien3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuaNhanVien3.setForeground(new java.awt.Color(3, 136, 253));
        btnSuaNhanVien3.setText("Thêm Khuyễn Mãi (F1)");
        btnSuaNhanVien3.setFocusable(false);
        btnSuaNhanVien3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSuaNhanVien3.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnSuaNhanVien3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnSuaNhanVien3ActionPerformed(evt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        jLabel54.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 0, 0));
        jLabel54.setText("(*)");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel25Layout.createSequentialGroup()
                                            .addComponent(jLabel51)
                                            .addGap(49, 49, 49)
                                            .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel25Layout.createSequentialGroup()
                                            .addComponent(jLabel48)
                                            .addGap(45, 45, 45)
                                            .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(txtTimKH9)
                                        .addGroup(jPanel25Layout.createSequentialGroup()
                                            .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(104, 104, 104)
                                            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtTimKH16, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(txtTimKH11))
                                    .addComponent(txtTimKH14, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(107, 107, 107)
                                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel47)
                                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jDateChooser4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBox12, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextAreaGhiChu, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
                                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtTimKH13, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSuaNhanVien1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSuaNhanVien2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSuaNhanVien3, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                        .addGap(50, 50, 50))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel53)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(jLabel50)
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(txtTimKH11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel48)
                                    .addComponent(jLabel52)
                                    .addComponent(jLabel65))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                                .addComponent(btnSuaNhanVien3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)))
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTimKH9, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSuaNhanVien1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jTextAreaGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(79, 79, 79)))
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel53)
                    .addComponent(jLabel64))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSuaNhanVien2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimKH13, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel55)
                            .addComponent(jLabel49)
                            .addComponent(jLabel60)))
                    .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTimKH14, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTimKH16, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel54)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlTableInitLayout = new javax.swing.GroupLayout(pnlTableInit);
        pnlTableInit.setLayout(pnlTableInitLayout);
        pnlTableInitLayout.setHorizontalGroup(
            pnlTableInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableInitLayout.createSequentialGroup()
                .addComponent(menuScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(pnlTableInitLayout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlTableInitLayout.setVerticalGroup(
            pnlTableInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableInitLayout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        pnlCenterKHchange.add(pnlTableInit, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 1283, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCenterKHchange, javax.swing.GroupLayout.PREFERRED_SIZE, 661, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addContainerGap(172, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(pnlTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel8, java.awt.BorderLayout.CENTER);

        pnlCenter.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(250, 250, 250));
        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setText("Chương trình khuyến mãi");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 906, Short.MAX_VALUE)
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

    private void jDateChooser4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateChooser4MouseClicked
        Instant instantNgayBD = jDateChooser4.getDate().toInstant();
	LocalDateTime ngayBD = instantNgayBD.atZone(ZoneId.systemDefault()).toLocalDateTime();
        
	Instant instantNgayKT = jDateChooser3.getDate().toInstant();
	LocalDateTime ngayKT = instantNgayKT.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }//GEN-LAST:event_jDateChooser4MouseClicked

    private void jDateChooser3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateChooser3MouseClicked
        Instant instantNgayBD = jDateChooser4.getDate().toInstant();
	LocalDateTime ngayBD = instantNgayBD.atZone(ZoneId.systemDefault()).toLocalDateTime();
        
	Instant instantNgayKT = jDateChooser3.getDate().toInstant();
	LocalDateTime ngayKT = instantNgayKT.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }//GEN-LAST:event_jDateChooser3MouseClicked

    private void btnSuaNhanVien3ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {//GEN-FIRST:event_btnSuaNhanVien3ActionPerformed
        if (!(jLabel52.getText().equals("")) || !(jLabel54.getText().equals(""))) {
			JOptionPane.showMessageDialog(null, "Chưa nhập đúng định dạng");
		} else {
			String maKhuyenMai = txtTimKH11.getText();
			String tenKhuyenMai = txtTimKH9.getText();
			String ghiChu = jTextAreaGhiChu.getText();
			String trangThai = jComboBox12.getSelectedItem().toString();
			double tiLeKM = Double.parseDouble(txtTimKH13.getText());
			double tienToiThieu = Double.parseDouble(txtTimKH14.getText());
			double giaTriMAX = Double.parseDouble(txtTimKH16.getText());

			Instant instantNgayBD = jDateChooser4.getDate().toInstant();
			LocalDateTime ngayBD = instantNgayBD.atZone(ZoneId.systemDefault()).toLocalDateTime();

			Instant instantNgayKT = jDateChooser3.getDate().toInstant();
			LocalDateTime ngayKT = instantNgayKT.atZone(ZoneId.systemDefault()).toLocalDateTime();

			KhuyenMai km = new KhuyenMai(maKhuyenMai, tenKhuyenMai, ghiChu, trangThai, tiLeKM, tienToiThieu, giaTriMAX,
					ngayBD, ngayKT);
			dao_km.createKhuyenMai(km);
			dao_km.updateTinhTrang(km);
                        thietLapMaKhuyenMai();
			loadJtable2();
		}
    }//GEN-LAST:event_btnSuaNhanVien3ActionPerformed

    private void btnSuaNhanVien1ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {//GEN-FIRST:event_btnSuaNhanVien1ActionPerformed
        if (!(jLabel52.getText().equals("")) || !(jLabel54.getText().equals(""))){
			JOptionPane.showMessageDialog(null, "Chưa nhập đúng định dạng");
		} else {
			String maKhuyenMai = txtTimKH11.getText();
			String tenKhuyenMai = txtTimKH9.getText();
			String ghiChu = jTextAreaGhiChu.getText();
			String trangThai = jComboBox12.getSelectedItem().toString();
			double tiLeKM = Double.parseDouble(txtTimKH13.getText());
			double tienToiThieu = Double.parseDouble(txtTimKH14.getText());
			double giaTriMAX = Double.parseDouble(txtTimKH16.getText());

			Instant instantNgayBD = jDateChooser4.getDate().toInstant();
			LocalDateTime ngayBD = instantNgayBD.atZone(ZoneId.systemDefault()).toLocalDateTime();

			Instant instantNgayKT = jDateChooser3.getDate().toInstant();
			LocalDateTime ngayKT = instantNgayKT.atZone(ZoneId.systemDefault()).toLocalDateTime();

			KhuyenMai km = new KhuyenMai(maKhuyenMai, tenKhuyenMai, ghiChu, trangThai, tiLeKM, tienToiThieu, giaTriMAX,
					ngayBD, ngayKT);
			dao_km.updateKhuyenMai(km);
			loadJtable2();
		}
    }//GEN-LAST:event_btnSuaNhanVien1ActionPerformed

    private void btnSuaNhanVien2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaNhanVien2ActionPerformed
        thietLapMaKhuyenMai();
        jLabel52.setText("(*)");
		txtTimKH9.setText("");
		jTextAreaGhiChu.setText("");
		txtTimKH13.setText("");
		txtTimKH14.setText("");
		txtTimKH16.setText("");
    }//GEN-LAST:event_btnSuaNhanVien2ActionPerformed

	private void btnLuuNVActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLuuNVActionPerformed
		// TODO add your handling code here:

	}// GEN-LAST:event_btnLuuNVActionPerformed

	private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHuyActionPerformed
		// TODO add your handling code here:

	}// GEN-LAST:event_btnHuyActionPerformed

	private void btnQuayLaiActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnQuayLaiActionPerformed
		// TODO add your handling code here:nl

	}// GEN-LAST:event_btnQuayLaiActionPerformed

	private void txtTimKH1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH1ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH1ActionPerformed

	private void txtTimKH3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH3ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH3ActionPerformed

	private void txtTimKH4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH4ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH4ActionPerformed

	private void txtTimKH6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH6ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH6ActionPerformed

	private void txtTimKHActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKHActionPerformed
		// TODO add your handling code here:

	}// GEN-LAST:event_txtTimKHActionPerformed

	private void txtTimKHFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtTimKHFocusGained
		// TODO add your handling code here:
		frm.placeHoderTextGianed(txtTimKH);
	}// GEN-LAST:event_txtTimKHFocusGained

	private void txtTimKHFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtTimKHFocusLost
		// TODO add your handling code here:
		frm.placeHoderTextLost(txtTimKH);
	}// GEN-LAST:event_txtTimKHFocusLost

	private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable3MouseClicked
		// TODO add your handling code here:

	}// GEN-LAST:event_jTable3MouseClicked

	private void btnQuayLai1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnQuayLai1ActionPerformed
		// TODO add your handling code here:

	}// GEN-LAST:event_btnQuayLai1ActionPerformed

	private void btnSuaNhanVienActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSuaNhanVienActionPerformed
		// TODO add your handling code here:

	}// GEN-LAST:event_btnSuaNhanVienActionPerformed

	private void txtTimKH2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH2ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH2ActionPerformed

	private void txtTimKH5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH5ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH5ActionPerformed

	private void txtTimKH8ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH8ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH8ActionPerformed

	private void txtTimKH7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH7ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH7ActionPerformed

	private void txtTimKH9ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH9ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH9ActionPerformed

	private void txtTimKH11ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH11ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH11ActionPerformed

	private void btnLuuNV1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLuuNV1ActionPerformed
		// TODO add your handling code here:

	}// GEN-LAST:event_btnLuuNV1ActionPerformed

	private void btnHuy1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHuy1ActionPerformed
		// TODO add your handling code here:

	}// GEN-LAST:event_btnHuy1ActionPerformed

	private void btnQuayLai2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnQuayLai2ActionPerformed
		// TODO add your handling code here:

	}// GEN-LAST:event_btnQuayLai2ActionPerformed

	private void txtTimKH4ComponentAdded(java.awt.event.ContainerEvent evt) {// GEN-FIRST:event_txtTimKH4ComponentAdded
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH4ComponentAdded

	private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBox1ActionPerformed
		// TODO add your handling code here:
		timCbKM();
	}// GEN-LAST:event_jComboBox1ActionPerformed

	private void lblNameLoginAncestorAdded(javax.swing.event.AncestorEvent evt) {// GEN-FIRST:event_lblNameLoginAncestorAdded
		// TODO add your handling code here:

	}// GEN-LAST:event_lblNameLoginAncestorAdded

	private void txtTimKH13ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH13ActionPerformed

	}// GEN-LAST:event_txtTimKH13ActionPerformed

	private void txtTimKH14ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH14ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH14ActionPerformed

	private void txtTimKH16ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH16ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH16ActionPerformed

	private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable2MouseClicked
		// TODO add your handling code here:
		if (evt.getClickCount() == 1 && !evt.isConsumed()) {
			evt.consume();
			jLabel52.setText("");
			jLabel54.setText("");
			DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
			String maKM = model.getValueAt(jTable2.getSelectedRow(), 1).toString();
			KhuyenMai km;
			try {
				km = dao_km.getKmTheoMa(maKM);
				txtTimKH11.setText(model.getValueAt(jTable2.getSelectedRow(), 1).toString());
				txtTimKH9.setText(model.getValueAt(jTable2.getSelectedRow(), 2).toString());
				jTextAreaGhiChu.setText(model.getValueAt(jTable2.getSelectedRow(), 3).toString());
				txtTimKH13.setText(model.getValueAt(jTable2.getSelectedRow(), 4).toString());
				jComboBox12.setSelectedItem(model.getValueAt(jTable2.getSelectedRow(), 5).toString());
				txtTimKH14.setText(Double.toString(km.getTienToiThieu()));
				txtTimKH16.setText(Double.toString(km.getGiaTriKhuyenMaiToiDa()));
				LocalDateTime ngayBD = km.getNgayBatDau();
				Date date = Date.from(ngayBD.atZone(ZoneId.systemDefault()).toInstant());
				jDateChooser4.setDate(date);

				LocalDateTime ngayKT = km.getNgayKetThuc();
				Date date2 = Date.from(ngayKT.atZone(ZoneId.systemDefault()).toInstant());
				jDateChooser3.setDate(date2);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}// GEN-LAST:event_jTable2MouseClicked

	private void txtTimKH10FocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtTimKH10FocusGained
		// TODO add your handling code here:
		frm.placeHoderTextGianed(txtTimKH);
	}// GEN-LAST:event_txtTimKH10FocusGained

	private void txtTimKH10FocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtTimKH10FocusLost
		// TODO add your handling code here:
		frm.placeHoderTextLost(txtTimKH);
	}// GEN-LAST:event_txtTimKH10FocusLost

	private void txtTimKH10ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH10ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH10ActionPerformed

	private void txtTimKH10KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH10KeyReleased

	}// GEN-LAST:event_txtTimKH10KeyReleased

	private void jDialogChonNSPWindowLostFocus(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_jDialogChonNSPWindowLostFocus
		// TODO add your handling code here:
		jDialogChonNSP.setVisible(false);
	}// GEN-LAST:event_jDialogChonNSPWindowLostFocus

	private void tableNhomSP1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tableNhomSP1MouseClicked
		// TODO add your handling code here:
	}// GEN-LAST:event_tableNhomSP1MouseClicked

	private void txtTimKH12FocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtTimKH12FocusGained
		// TODO add your handling code here:
		frm.placeHoderTextGianed(txtTimKH12);
	}// GEN-LAST:event_txtTimKH12FocusGained

	private void txtTimKH12FocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtTimKH12FocusLost
		// TODO add your handling code here:
		frm.placeHoderTextLost(txtTimKH12);
	}// GEN-LAST:event_txtTimKH12FocusLost

	private void txtTimKH12ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH12ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH12ActionPerformed

	private void txtTimKH12KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH12KeyReleased
		// TODO add your handling code here:
	}// GEN-LAST:event_txtTimKH12KeyReleased

	private void btnThemNV2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemNV2ActionPerformed
		// TODO add your handling code here:
		ChonTatCaCheckBox();
	}// GEN-LAST:event_btnThemNV2ActionPerformed

	private void jDialogChonNSPTestWindowLostFocus(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_jDialogChonNSPTestWindowLostFocus
		// TODO add your handling code here:
	}// GEN-LAST:event_jDialogChonNSPTestWindowLostFocus

	private void btnThemNV3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemNV3ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_btnThemNV3ActionPerformed

	private void btnThemNV4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemNV4ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_btnThemNV4ActionPerformed

	private void btnThemNV5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemNV5ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_btnThemNV5ActionPerformed

	private void btnThemNV6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemNV6ActionPerformed
		BoChonTatCaCheckBox();
	}// GEN-LAST:event_btnThemNV6ActionPerformed

	private void btnThemNV7ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {// GEN-FIRST:event_btnThemNV7ActionPerformed
		String maKhuyenMai = txtTimKH11.getText();
		String tenKhuyenMai = txtTimKH9.getText();
		String ghiChu = jTextAreaGhiChu.getText();
		String trangThai = jComboBox12.getSelectedItem().toString();
		double tiLeKM = Double.parseDouble(txtTimKH13.getText());
		double tienToiThieu = Double.parseDouble(txtTimKH14.getText());
		double giaTriMAX = Double.parseDouble(txtTimKH16.getText());

		Instant instantNgayBD = jDateChooser4.getDate().toInstant();
		LocalDateTime ngayBD = instantNgayBD.atZone(ZoneId.systemDefault()).toLocalDateTime();

		Instant instantNgayKT = jDateChooser3.getDate().toInstant();
		LocalDateTime ngayKT = instantNgayKT.atZone(ZoneId.systemDefault()).toLocalDateTime();

		KhuyenMai km = new KhuyenMai(maKhuyenMai, tenKhuyenMai, ghiChu, trangThai, tiLeKM, tienToiThieu, giaTriMAX,
				ngayBD, ngayKT);
		dao_km.createKhuyenMai(km);
		dao_km.updateTinhTrang(km);

		ArrayList<String> trangThaiCheckbox = layDanhSachCheckbox(tableNhomSP1);
		for (int i = 0; i < trangThaiCheckbox.size(); i++) {
			NhomSanPham nsp = dao_nsp.getNspTheoMa(trangThaiCheckbox.get(i));
			dao_nkm.createNhomKM(km, nsp);
		}
		loadJtable2();
		showPanelChange(pnlChange, pnlCenter);
		jDialogChonNSPTest.setVisible(false);
	}// GEN-LAST:event_btnThemNV7ActionPerformed

	private void txtTimKH9KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH9KeyReleased
		try {
			String nhaXB = txtTimKH9.getText().trim();
			if (!nhaXB.isEmpty()) {
				if (!(nhaXB.length() > 0 && nhaXB.matches("^\\p{Lu}[\\p{L} .'-[0-9]*]*$"))) {
					jLabel52.setText("chuỗi bắt đầu bằng chữ hoa không chứa kí tự đặc biệ");
				} else {
					jLabel52.setText("");
				}
			} else {
				jLabel52.setText("nhà xuất bản không được để trống");
			}
		} catch (NumberFormatException e) {
			jLabel52.setText("không phải là số");
		}
	}// GEN-LAST:event_txtTimKH9KeyReleased

	private void jComboBox12ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBox12ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_jComboBox12ActionPerformed

	private void txtTimKHKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKHKeyReleased
		timJtable2();
	}// GEN-LAST:event_txtTimKHKeyReleased

	private void txtTimKH13KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH13KeyReleased
		try {
			String giamGia = txtTimKH13.getText().trim();
			if (!giamGia.isEmpty()) {
				double giam = Double.parseDouble(giamGia);
				if (!(giam >= 0 && giam <= 100)) {
					jLabel54.setText("Tỉ lệ khuyến mãi phải >= 0 va <= 100");
					txtTimKH14.setEditable(false);
					txtTimKH16.setEditable(false);
				} else {
					jLabel54.setText("");
					txtTimKH14.setEditable(true);
					txtTimKH16.setEditable(true);
				}
			} else {
				jLabel54.setText("Tỉ lệ khuyến mãi không được để trống");
				txtTimKH14.setEditable(false);
				txtTimKH16.setEditable(false);
			}
		} catch (NumberFormatException e) {
			jLabel54.setText("Tỉ lệ khuyến mãi không chứa kí tự");
			txtTimKH14.setEditable(false);
			txtTimKH16.setEditable(false);
		}
	}// GEN-LAST:event_txtTimKH13KeyReleased

	private void txtTimKH14KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH14KeyReleased
		try {
			String giamGia = txtTimKH14.getText().trim();
			if (!giamGia.isEmpty()) {
				double giam = Double.parseDouble(giamGia);
				if (!(giam >= 0)) {
					jLabel54.setText("Tiền đặt tối thiểu phải >= 0");
					txtTimKH13.setEditable(false);
					txtTimKH16.setEditable(false);
				} else {
					jLabel54.setText("");
					txtTimKH13.setEditable(true);
					txtTimKH16.setEditable(true);
				}
			} else {
				jLabel54.setText("Tiền đặt tối thiểu không được để trống");
				txtTimKH13.setEditable(false);
				txtTimKH16.setEditable(false);
			}
		} catch (NumberFormatException e) {
			jLabel54.setText("Tiền đặt tối thiểu không chứa kí tự");
			txtTimKH13.setEditable(false);
			txtTimKH16.setEditable(false);
		}
	}// GEN-LAST:event_txtTimKH14KeyReleased

	private void txtTimKH16KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH16KeyReleased
		try {
			String giamGia = txtTimKH16.getText().trim();
			if (!giamGia.isEmpty()) {
				double giam = Double.parseDouble(giamGia);
				if (!(giam >= 0)) {
					jLabel54.setText("Khuyến mãi tối đa nhận phải >= 0");
					txtTimKH14.setEditable(false);
					txtTimKH13.setEditable(false);
				} else {
					jLabel54.setText("");
					txtTimKH14.setEditable(true);
					txtTimKH13.setEditable(true);
				}
			} else {
				jLabel54.setText("Khuyến mãi tối đa nhận không được để trống");
				txtTimKH14.setEditable(false);
				txtTimKH13.setEditable(false);
			}
		} catch (NumberFormatException e) {
			jLabel54.setText("Khuyến mãi tối đa nhận không chứa kí tự");
			txtTimKH14.setEditable(false);
			txtTimKH13.setEditable(false);
		}
	}// GEN-LAST:event_txtTimKH16KeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSuaNhanVien1;
    private javax.swing.JButton btnSuaNhanVien2;
    private javax.swing.JButton btnSuaNhanVien3;
    private javax.swing.JButton btnThemNV2;
    private javax.swing.JButton btnThemNV3;
    private javax.swing.JButton btnThemNV4;
    private javax.swing.JButton btnThemNV5;
    private javax.swing.JButton btnThemNV6;
    private javax.swing.JButton btnThemNV7;
    private javax.swing.JLabel date;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox12;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private javax.swing.JDialog jDialogChonNSP;
    private javax.swing.JDialog jDialogChonNSPTest;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextAreaGhiChu;
    private javax.swing.JLabel lblNameLogin;
    private menuGui.MenuScrollPane menuScrollPane10;
    private menuGui.MenuScrollPane menuScrollPane12;
    private menuGui.MenuScrollPane menuScrollPane2;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlCenterKHchange;
    private javax.swing.JPanel pnlChange;
    private javax.swing.JPanel pnlChonNCC1;
    private javax.swing.JPanel pnlChonNCC2;
    private javax.swing.JPanel pnlTableInit;
    private javax.swing.JPanel pnlTopLeft;
    private javax.swing.JTable tableNhomSP1;
    private javax.swing.JTextField txtTimKH;
    public javax.swing.JTextField txtTimKH10;
    private javax.swing.JTextField txtTimKH11;
    public javax.swing.JTextField txtTimKH12;
    private javax.swing.JTextField txtTimKH13;
    private javax.swing.JTextField txtTimKH14;
    private javax.swing.JTextField txtTimKH16;
    private javax.swing.JTextField txtTimKH9;
    // End of variables declaration//GEN-END:variables
	private ArrayList<NhanVien> data;
	private DAO_NhanVien dao_nv = LookupNaming.lookup_NhanVien();
}
