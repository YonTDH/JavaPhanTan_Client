package iuh.fit.gui;

//import dao.DAO_ChiTietHoaDon;
//import dao.DAO_HoaDon;
//import dao.DAO_KhachHang;
//import dao.DAO_KhuyenMai;
//import dao.DAO_MauSac;
//import dao.DAO_NhaCungCap;
//import dao.DAO_NhanVien;
//import dao.DAO_NhomSanPham;
//import dao.DAO_Sach;
//import dao.DAO_VanPhongPham;
//import entity.ChiTietHoaDon;
//import entity.HoaDon;
//import entity.KhachHang;
//import entity.KhuyenMai;
//import entity.NhanVien;
//import entity.NhomKhachHang;
//import entity.NhomSanPham;
//import entity.Sach;
//import entity.SanPham;
//import entity.VanPhongPham;
import iuh.fit.gui.FrmChinh;
import lookup.LookupNaming;
import menuGui.TableActionCellEditor;
import menuGui.TableActionCellRender;
import menuGui.TableActionEvent;
import printReport.FieldReport;
import printReport.ParameterReport;
import printReport.ReportManager;

//import com.barcodelib.barcode.Linear;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class FrmLapHoaDon extends javax.swing.JPanel {

	
	private static final long serialVersionUID = -4571370475634739847L;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	DecimalFormat deciFormat = new DecimalFormat("###.###");
	NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US); 

	int soLuong = 1;
	int sttSP = 1;
	/**
	 * Creates new form FrmDSKhachHang
	 */
	private FrmChinh frm = new FrmChinh();
	private DAO_VanPhongPham dao_vpp = LookupNaming.lookup_VanPhongPham();
	private DAO_Sach dao_sach = LookupNaming.lookup_Sach();
	private DAO_NhomSanPham dao_nsp = LookupNaming.lookup_NhomSanPham();
	private DAO_NhaCungCap dao_ncc = LookupNaming.lookup_NhaCungCap();
	private DAO_MauSac dao_mausac = LookupNaming.lookup_MauSac();
	private DAO_KhachHang dao_kh = LookupNaming.lookup_KhachHang();
	private DAO_HoaDon dao_hd = LookupNaming.lookup_HoaDon();
	private DAO_ChiTietHoaDon dao_cthd = LookupNaming.lookup_ChiTietHoaDon();
	private DAO_NhanVien dao_nv = LookupNaming.lookup_NhanVien();
	private Thread thread = null;
	private DAO_KhuyenMai dao_khuyenMai = LookupNaming.lookup_KhuyenMai();

	public FrmLapHoaDon() throws RemoteException {
		try {
			ReportManager.getInstance().compileReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO Auto-generated catch block
		initComponents();

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				String string = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
				date.setText(string);
			}
		}, 0, 1000);

		thread = new Thread(this::setTimeAuto);
		thread.start();

		lblMaHoaDon.setText(createMaHoaDon());
		lblTenKH.setText("Khách lẻ");
		lblMaKH.setText(createMaKhachHang());

		quickPress();
		loadChonSP();
		tableAction();

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

	public void quickPress() {
		// F1----------
		InputMap inputMap1 = btnThanhToanHD.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap1.put(KeyStroke.getKeyStroke("F8"), "doSomething1");

		Action action1 = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnThanhToanHDActionPerformed(e);
			}
		};

		btnThanhToanHD.getActionMap().put("doSomething1", action1);
		//---------------
		// F2----------
		InputMap inputMap2 = btnInHoaDon.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap2.put(KeyStroke.getKeyStroke("F9"), "doSomething2");

		Action action2 = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnInHoaDonActionPerformed(e);
			}
		};

		btnInHoaDon.getActionMap().put("doSomething2", action2);
		//---------------
		// F3----------
		InputMap inputMap3 = btnThemDSCho.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap3.put(KeyStroke.getKeyStroke("F7"), "doSomething3");

		Action action3 = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnThemDSChoActionPerformed(e);
			}
		};

		btnThemDSCho.getActionMap().put("doSomething3", action3);
		//---------------
		// F4----------
		InputMap inputMap4 = btnHoaDonMoi.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap4.put(KeyStroke.getKeyStroke("F5"), "doSomething4");

		Action action4 = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnHoaDonMoiActionPerformed(e);
			}
		};

		btnHoaDonMoi.getActionMap().put("doSomething4", action4);
		//---------------
		// F4----------
		InputMap inputMap5 = txtTimSanPhamChon.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap5.put(KeyStroke.getKeyStroke("ENTER"), "doSomething5");

		Action action5 = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txtTimSanPhamChon.requestFocus();
			}

		};

		txtTimSanPhamChon.getActionMap().put("doSomething5", action5);
		//---------------
	}

	public void inHoaDon() {
		if (tableInForSP.getRowCount() == 0) {
		} else {
			try {
				List<FieldReport> fields = new ArrayList<>();
				for (int i = 0; i < tableInForSP.getRowCount(); i++) {
					String ten = (String) tableInForSP.getValueAt(i, 2);
					double dg = (double) tableInForSP.getValueAt(i, 3);
					int sl = (int) tableInForSP.getValueAt(i, 4);
					double tien = (double) tableInForSP.getValueAt(i, 5);
					fields.add(new FieldReport(ten, sl, dg, tien));
				}
				double tienThanhToan = Double.parseDouble(lblTongTienDefault.getText());
				double tienChietKhau = Double.parseDouble(txtTienChietKhau.getText());
				double tienKhachTra = tienThanhToan + tienChietKhau - Double.parseDouble(txtTienKhuyenMai.getText());
				LocalDateTime ngay = LocalDateTime.now();
				System.out.println(jTextAreaGhiChu.getText());
				ParameterReport dataprint = new ParameterReport(tienThanhToan + "", tienKhachTra + "", txtTienChietKhau.getText(), formatter.format(ngay), jTextAreaGhiChu.getText(), txtTienKhuyenMai.getText(), fields);
				ReportManager.getInstance().printReportPayment(dataprint);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void sortTableChooseProDuct() throws RemoteException {
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableChonSP.getModel());
		tableChonSP.setRowSorter(sorter);
		DefaultTableModel md = (DefaultTableModel) tableChonSP.getModel();
		md.getDataVector().removeAllElements();
		md.fireTableDataChanged();
		String nhom = (String) cboSortTabelChonSP.getSelectedItem();
		List<Sach> dsSach = dao_sach.getAlltbSach();
		List<VanPhongPham> dsVpp = dao_vpp.getAllVanPhongPhan();
		if (nhom.equalsIgnoreCase("Tất cả")) {
			int stt = 1;
			for (Sach s : dsSach) {
				if (s.getSoLuongTon() == 0) {
					continue;
				} else {
					md.addRow(new Object[]{stt, s.getMaSanPham(), s.getTenSanPham(), s.getDonGiaBan(),
							s.getSoLuongTon(), s.getTinhTrang()

					});
					stt++;
				}

			}
			for (VanPhongPham vpp : dsVpp) {
				if (vpp.getSoLuongTon() == 0) {
					continue;
				} else {
					md.addRow(new Object[]{stt, vpp.getMaSanPham(), vpp.getTenSanPham(), vpp.getDonGiaBan(),
							vpp.getSoLuongTon(), vpp.getTinhTrang()

					});
					stt++;
				}

			}
		} else {
			NhomSanPham nsp = dao_nsp.getNsptheoTen(nhom);
			String ma = nsp.getMaNhomSanPham();
			int stt = 1;
			for (Sach s : dsSach) {
				if (s.getNhomSanPham().getMaNhomSanPham().equalsIgnoreCase(ma) && s.getSoLuongTon() != 0) {
					md.addRow(new Object[]{stt, s.getMaSanPham(), s.getTenSanPham(), s.getDonGiaBan(),
							s.getSoLuongTon(), s.getTinhTrang()

					});
					stt++;
				}

			}
			for (VanPhongPham vpp : dsVpp) {
				if (vpp.getNhomSanPham().getMaNhomSanPham().equalsIgnoreCase(ma) && vpp.getSoLuongTon() != 0) {
					md.addRow(new Object[]{stt, vpp.getMaSanPham(), vpp.getTenSanPham(), vpp.getDonGiaBan(),
							vpp.getSoLuongTon(), vpp.getTinhTrang()

					});
					stt++;
				}

			}

		}

	}

	public void tableAction()  {
		TableActionEvent event = new TableActionEvent() {
			@Override
			public void onPlus(int row) {
				if (tableInForSP.isEditing()) {
					tableInForSP.getCellEditor().stopCellEditing();
				}
				DefaultTableModel model = (DefaultTableModel) tableInForSP.getModel();
				String maSP = (String) tableInForSP.getValueAt(row, 1);
				int sl = (int) tableInForSP.getValueAt(row, 4);
				double dg = (double) tableInForSP.getValueAt(row, 3);
				if (maSP.startsWith("S")) {
					Sach s;
					try {
						s = dao_sach.getSachtheoMa(maSP);
						if (sl < s.getSoLuongTon()) {
							sl++;
							model.setValueAt(sl, row, 4);
							model.setValueAt(tinhThanhTien(sl, dg), row, 5);
							System.out.println("onPlus row : " + row);
							createInit();
						}
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					VanPhongPham vpp;
					try {
						vpp = dao_vpp.getVPPtheoMa(maSP);
						if (sl < vpp.getSoLuongTon()) {
							sl++;
							model.setValueAt(sl, row, 4);
							model.setValueAt(tinhThanhTien(sl, dg), row, 5);
							System.out.println("onPlus row : " + row);
							createInit();
						}
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

			@Override
			public void onDelete(int row) {
				if (tableInForSP.isEditing()) {
					tableInForSP.getCellEditor().stopCellEditing();
				}
				DefaultTableModel model = (DefaultTableModel) tableInForSP.getModel();
				for (int i = row; i < tableInForSP.getRowCount(); i++) {
					int stt = (int) tableInForSP.getValueAt(i, 0);
					stt--;
					tableInForSP.setValueAt(stt, i, 0);
				}
				model.removeRow(row);
				if (tableInForSP.getRowCount() == 0) {
					showPanelChange(pnlSanPhamDaChon, pnlSanPhamDaChonNull);
				}
				//                loadChonSP();
				try {
					createInit();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sttSP--;
			}

			@Override
			public void onMinus(int row) {
				if (tableInForSP.isEditing()) {
					tableInForSP.getCellEditor().stopCellEditing();
				}
				DefaultTableModel model = (DefaultTableModel) tableInForSP.getModel();
				int sl = (int) tableInForSP.getValueAt(row, 4);
				double dg = (double) tableInForSP.getValueAt(row, 3);
				sl--;
				model.setValueAt(sl, row, 4);
				model.setValueAt(tinhThanhTien(sl, dg), row, 5);
				if (sl == 0) {
					onDelete(row);
				}
				System.out.println("onMinus row : " + row);
				try {
					createInit();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		tableInForSP.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
		tableInForSP.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor(event));
		tableInForSP.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
				setHorizontalAlignment(SwingConstants.RIGHT);
				return super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
			}
		});
	}

	public String createMaKhachHang() throws RemoteException {
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

			KhachHang existingCustomer = dao_kh.getKHTheoMa(tempID);
			if (existingCustomer == null) {
				cusID = tempID;
				break;
			}
			count++;
		} while (true);

		return cusID;
	}

	public int tongSanPhamMua() {
		int tong = 0;
		DefaultTableModel model = (DefaultTableModel) tableInForSP.getModel();
		for (int i = 0; i < tableInForSP.getRowCount(); i++) {

			tong += Integer.parseInt((String) tableInForSP.getValueAt(i, 4));
		}
		return tong;
	}

	public String createMaHoaDon() throws RemoteException {
		//        LocalDate d = LocalDate.of(2023, 11, 13);
		LocalDate d = LocalDate.now();
		DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("ddMMyyyy");
		String format = d.format(myFormatDate);
		Integer count = 1;
		String hdID = "";
		do {
			String tempID = count.toString().length() == 1 ? ("HD" + format + "-00000" + count)
					: count.toString().length() == 2 ? ("HD" + format + "-0000" + count)
							: count.toString().length() == 3 ? ("HD" + format + "-000" + count)
									: count.toString().length() == 4 ? ("HD" + format + "-00" + count)
											: count.toString().length() == 5 ? ("HD" + format + "-0" + count)
													: ("HD" + format + "-" + count);

			HoaDon existingBill = dao_hd.getHoaDontheoMa(tempID);
			if (existingBill == null) {
				hdID = tempID;
				break;
			}
			count++;
		} while (true);
		HoaDon hdm = new HoaDon(hdID);
		dao_hd.createHoaDon(hdm);
		return hdID;
	}

	private void showPanelChange(JPanel a, JPanel b) {
		a.removeAll();
		a.add(b);
		a.repaint();
		a.revalidate();
	}

	public void loadChonSP() throws RemoteException {

		List<Sach> dsSach = dao_sach.getAlltbSach();
		List<VanPhongPham> dsVpp = dao_vpp.getAllVanPhongPhan();

		DefaultTableModel tableModal = (DefaultTableModel) tableChonSP.getModel();
		tableModal.getDataVector().removeAllElements();
		tableModal.fireTableDataChanged();

		int stt = 1;
		for (Sach s : dsSach) {
			if (s.getSoLuongTon() <= 0) {
				continue;
			} else {
				tableModal.addRow(new Object[]{stt, s.getMaSanPham(), s.getTenSanPham(), s.getDonGiaBan(),
						s.getSoLuongTon(), s.getTinhTrang()

				});
				stt++;
			}

		}
		for (VanPhongPham vpp : dsVpp) {
			if (vpp.getSoLuongTon() <= 0) {
				continue;
			} else {
				tableModal.addRow(new Object[]{stt, vpp.getMaSanPham(), vpp.getTenSanPham(), vpp.getDonGiaBan(),
						vpp.getSoLuongTon(), vpp.getTinhTrang()

				});
				stt++;
			}

		}

	}

	public void loadChonSPBarcode() throws RemoteException {
		DefaultTableModel tableModal = (DefaultTableModel) tableChonSP.getModel();
		tableModal.getDataVector().removeAllElements();
		tableModal.fireTableDataChanged();
		String ma = txtTimSanPhamChon.getText().trim();
		if (txtTimSanPhamChon.getText().startsWith("S")) {
			Sach s = dao_sach.getSachtheoMa(ma);
			tableModal.addRow(new Object[]{1, s.getMaSanPham(), s.getTenSanPham(), s.getDonGiaBan(),
					s.getSoLuongTon(), s.getTinhTrang()

			});

		} else {
			VanPhongPham vpp = dao_vpp.getVPPtheoMa(ma);
			tableModal.addRow(new Object[]{1, vpp.getMaSanPham(), vpp.getTenSanPham(), vpp.getDonGiaBan(),
					vpp.getSoLuongTon(), vpp.getTinhTrang()

			});
		}

	}

	public double tinhThanhTien(int sl, double dg) {
		return sl * dg;
	}

	public void loadChonKH() throws RemoteException {
		List<KhachHang> dsKH = dao_kh.getAllKhachHang();
		DefaultTableModel modal = (DefaultTableModel) tableChonKH.getModel();
		modal.getDataVector().removeAllElements();
		modal.fireTableDataChanged();
		DefaultTableModel tableModal = (DefaultTableModel) tableChonKH.getModel();
		int stt = 1;
		for (KhachHang kh : dsKH) {
			if (!kh.getTenKhachHang().equals("Khách lẻ")) {
				tableModal.addRow(new Object[]{stt, kh.getMaKhachHang(), kh.getTenKhachHang(), kh.getSoDienThoai()});
				stt++;
			}

		}
	}

	public void timJtableLapHoaDon() {
		DefaultTableModel tableModel = (DefaultTableModel) tableChonSP.getModel();
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
		tableChonSP.setRowSorter(sorter);

		txtTimSanPhamChon.getDocument().addDocumentListener(new DocumentListener() {
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
		String searchTerm = txtTimSanPhamChon.getText().toUpperCase();
		if (searchTerm.isEmpty()) {
			sorter.setRowFilter(RowFilter.regexFilter(txtTimSanPhamChon.getText()));
		} else {
			Pattern pattern = Pattern.compile(Pattern.quote(searchTerm),
					Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			sorter.setRowFilter(new RowFilter<DefaultTableModel, Object>() {
				@Override
				public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
					String maSp = entry.getValue(1).toString();
					String tenSp = entry.getValue(2).toString();

					return pattern.matcher(maSp).lookingAt() || pattern.matcher(tenSp).lookingAt();
				}
			});
		}
	}

	public double apDungKhuyenMai() throws RemoteException {
		double tongTienKM = 0.0;
		if (tableInForSP.getRowCount() != 0) {
			KhuyenMai km = dao_khuyenMai.getKMtheoMa(txtMaKhuyenMai.getText());
			if (km != null && km.getTienToiThieu() <= Double.parseDouble(lblTongTienDefault.getText())) {
				tongTienKM = Double.parseDouble(lblTongTienDefault.getText()) * (km.getTyLeKhuyenMai() / 100);
				if (tongTienKM > km.getGiaTriKhuyenMaiToiDa()) {
					tongTienKM = km.getGiaTriKhuyenMaiToiDa();
					return tongTienKM;
				}
				return tongTienKM;
			}
		}
		return tongTienKM;
	}

	public void loadKhuyenMai() throws RemoteException {
		DefaultTableModel md = (DefaultTableModel) tableChonKH1.getModel();
		md.getDataVector().removeAllElements();
		List<KhuyenMai> listKM = dao_khuyenMai.getAlltbKM();
		for (KhuyenMai km : listKM) {
			if (km.getTrangThai().equalsIgnoreCase("Đang hoạt động")) {
				md.addRow(new Object[]{km.getMaKhuyenMai(), km.getTenKhuyenMai(), km.getTyLeKhuyenMai(), km.getTienToiThieu(), km.getGiaTriKhuyenMaiToiDa(), km.getGhiChu()});
			}

		}
	}

	public void createInit() throws RemoteException {
		DefaultTableModel md = (DefaultTableModel) tableInForSP.getModel();
		double tongTienThanhToan = 0;
		int tongSoLuongSp = 0;
		for (int i = 0; i < md.getRowCount(); i++) {
			tongTienThanhToan += (double) md.getValueAt(i, 5);
			tongSoLuongSp += (int) md.getValueAt(i, 4);
		}
		lblTongTienDefault.setText(deciFormat.format(tongTienThanhToan));
		txtSoLuongSanPhamChon.setText("1");
		KhachHang kh = dao_kh.getKHTheoMa(lblMaKH.getText());
		if (kh == null) {
			double tienKhuyenMai = apDungKhuyenMai();
			double chietKhau = 0.0;
			if (txtTienChietKhau.getText().equalsIgnoreCase("") || Double.parseDouble(txtTienChietKhau.getText()) < 0) {
				chietKhau = 0.0;
			} else {
				chietKhau = Double.parseDouble(txtTienChietKhau.getText());
			}
			double tien = tongTienThanhToan + chietKhau - tienKhuyenMai;
			txtTienKhuyenMai.setText(deciFormat.format(tienKhuyenMai));
			lblTongTienThanhToan.setText(deciFormat.format(tien));
			lblTongSoLuong.setText(tongSoLuongSp + "");
		} else {
			if (kh.getNhomKhachHang().equals(kh.getNhomKhachHang().KHACHVIP)) {
				double tienKhuyenMai = apDungKhuyenMai();
				double chietKhau = 0.0;
				if (txtTienChietKhau.getText().equalsIgnoreCase("") || Double.parseDouble(txtTienChietKhau.getText()) < 0) {
					chietKhau = 0.0;
				} else {
					chietKhau = Double.parseDouble(txtTienChietKhau.getText());
				}
				double tien = tongTienThanhToan + chietKhau - (tongTienThanhToan * 0.05) - tienKhuyenMai;
				txtTienKhuyenMai.setText(deciFormat.format(((tongTienThanhToan * 0.05) + tienKhuyenMai)));
				lblTongTienThanhToan.setText(deciFormat.format(tien));
				lblTongSoLuong.setText(tongSoLuongSp + "");
			} else {
				double tienKhuyenMai = apDungKhuyenMai();
				double chietKhau = 0.0;
				if (txtTienChietKhau.getText().equalsIgnoreCase("") || Double.parseDouble(txtTienChietKhau.getText()) < 0) {
					chietKhau = 0.0;
				} else {
					chietKhau = Double.parseDouble(txtTienChietKhau.getText());
				}
				double tien = tongTienThanhToan + chietKhau - tienKhuyenMai;
				txtTienKhuyenMai.setText(deciFormat.format(tienKhuyenMai));
				lblTongTienThanhToan.setText(deciFormat.format(tien));
				lblTongSoLuong.setText(tongSoLuongSp + "");
			}
		}

	}

	public synchronized void thanhToanHD() throws RemoteException {
		if (true) {
			createInit();
			if (tableInForSP.getRowCount() != 0) {
				LocalDateTime ngayLap = LocalDateTime.now();
				KhachHang kh = null;
				if (lblTenKH.getText().equals("Khách lẻ")) {
					kh = new KhachHang(lblMaKH.getText(), "Khách lẻ", "", NhomKhachHang.KHACHLE, 0, 0);
					dao_kh.themKhachHang(kh);
				} else {
					kh = dao_kh.getKHTheoMa(lblMaKH.getText());
				}
				System.out.println(kh.toString());
				NhanVien nv = dao_nv.getNVTheoMa("QL23102023-000007");
				HoaDon hd = new HoaDon(lblMaHoaDon.getText(), ngayLap, nv, kh, jTextAreaGhiChu.getText(), 1, Double.parseDouble(lblTongTienThanhToan.getText()), Double.parseDouble(txtTienChietKhau.getText()), txtMaKhuyenMai.getText());
				dao_hd.updateHoaDon(hd);
				DefaultTableModel md = (DefaultTableModel) tableInForSP.getModel();
				String ma = "";
				for (int i = 0; i < tableInForSP.getRowCount(); i++) {
					ma = (String) tableInForSP.getValueAt(i, 1);
					if (ma.startsWith("S")) {
						Sach s = dao_sach.getSachtheoMa(ma);
						ChiTietHoaDon cthd = new ChiTietHoaDon(hd, s, (int) tableInForSP.getValueAt(i, 4), (double) tableInForSP.getValueAt(i, 5));
						dao_cthd.createChiTietHoaDon(cthd);
						if (s.getSoLuongTon() == 0 || s.getSoLuongTon() - (int) tableInForSP.getValueAt(i, 4) < 0) {
							JOptionPane.showMessageDialog(null, s.getTenSanPham() + " còn " + s.getSoLuongTon());
							loadChonSP();
							lamMoiHoaDon();
							return;
						}
						s.setSoLuongTon(s.getSoLuongTon() - (int) tableInForSP.getValueAt(i, 4));
						if (s.getSoLuongTon() == 0) {
							s.setTinhTrang("Hết hàng");

						}
						dao_sach.updateSach(s);
					} else {
						VanPhongPham vpp = dao_vpp.getVPPtheoMa(ma);
						ChiTietHoaDon cthd = new ChiTietHoaDon(hd, vpp, (int) tableInForSP.getValueAt(i, 4), (double) tableInForSP.getValueAt(i, 5));
						dao_cthd.createChiTietHoaDon(cthd);
						if (vpp.getSoLuongTon() == 0 || vpp.getSoLuongTon() - (int) tableInForSP.getValueAt(i, 4) < 0) {
							JOptionPane.showMessageDialog(null, vpp.getTenSanPham() + " còn " + vpp.getSoLuongTon());
							loadChonSP();
							lamMoiHoaDon();
							return;
						}
						vpp.setSoLuongTon(vpp.getSoLuongTon() - (int) tableInForSP.getValueAt(i, 4));

						if (vpp.getSoLuongTon() == 0) {
							vpp.setTinhTrang("Hết hàng");
							JOptionPane.showMessageDialog(null, vpp.getTenSanPham() + " Đã hết hàng");

						}
						dao_vpp.update(vpp);
					}
				}
				int soLuongHoaDonKH = kh.getSoLuongHoaDon() + 1;
				double tongTienMua = kh.getTongTienMua();
				kh.setTongTienMua(tongTienMua + Double.parseDouble(lblTongTienThanhToan.getText()));
				kh.setSoLuongHoaDon(soLuongHoaDonKH);
				if (kh.getTongTienMua() >= 500000) {
					kh.setNhomKhachHang(NhomKhachHang.KHACHVIP);
				}
				dao_kh.updateKhachHang(kh);
				int chon = JOptionPane.showConfirmDialog(null, "Bạn có muốn in hóa đơn hay không?", "Thông báo", JOptionPane.YES_OPTION);
				if (chon == JOptionPane.YES_OPTION) {
					inHoaDon();
				}
				loadChonSP();
				lamMoiHoaDon();

			}
		}

		//        showPanelChange(pnlChange, pnlCenter);
	}

	public void lamMoiHoaDon() throws RemoteException {
		DefaultTableModel md = (DefaultTableModel) tableInForSP.getModel();
		md.getDataVector().removeAllElements();
		lblTenKH.setText("Khách lẻ");
		md.fireTableDataChanged();
		lblMaKH.setText(createMaKhachHang());
		txtSoLuongSanPhamChon.setText("1");
		jTextAreaGhiChu.setText("");
		txtTienChietKhau.setText("0.0");
		txtTienKhachDua.setText("0.0");
		lblMaHoaDon.setText(createMaHoaDon());
		lblTienThua.setText("0.0");
		lblTongTienThanhToan.setText("0.0");
		lblTongTienDefault.setText("0.0");
		lblTongSoLuong.setText("0");
		cboSortTabelChonSP.setSelectedItem("Còn hàng");
		txtTimSanPhamChon.setText("");
		tableChonSP.clearSelection();
		timJtableLapHoaDon();
		showPanelChange(pnlSanPhamDaChon, pnlSanPhamDaChonNull);
		showPanelChange(pnlBotLeft, pnlBotLeftReturn);
	}

	public synchronized void themDSCho() throws RemoteException {
		if (true) {
			if (tableInForSP.getRowCount() != 0) {
				LocalDateTime ngayLap = LocalDateTime.now();

				KhachHang kh = null;
				if (lblTenKH.getText().equals("Khách lẻ")) {
					kh = new KhachHang(lblMaKH.getText(), "Khách lẻ", "", NhomKhachHang.KHACHLE, 0, 0);
					dao_kh.updateKhachHang(kh);
				} else {
					kh = dao_kh.getKHTheoMa(lblMaKH.getText());
				}
				System.out.println(kh.toString());
				NhanVien nv = dao_nv.getNVTheoMa("QL23102023-000007");
				HoaDon hd = new HoaDon(lblMaHoaDon.getText(), ngayLap, nv, kh, jTextAreaGhiChu.getText(), 0, Double.parseDouble(lblTongTienThanhToan.getText()), Double.parseDouble(txtTienChietKhau.getText()), txtMaKhuyenMai.getText());
				dao_hd.updateHoaDon(hd);
				DefaultTableModel md = (DefaultTableModel) tableInForSP.getModel();
				String ma = "";
				for (int i = 0; i < tableInForSP.getRowCount(); i++) {
					ma = (String) tableInForSP.getValueAt(i, 1);
					if (ma.startsWith("S")) {
						Sach s = dao_sach.getSachtheoMa(ma);
						ChiTietHoaDon cthd = new ChiTietHoaDon(hd, s, (int) tableInForSP.getValueAt(i, 4), (double) tableInForSP.getValueAt(i, 5));
						dao_cthd.createChiTietHoaDon(cthd);
					} else {
						VanPhongPham vpp = dao_vpp.getVPPtheoMa(ma);
						ChiTietHoaDon cthd = new ChiTietHoaDon(hd, vpp, (int) tableInForSP.getValueAt(i, 4), (double) tableInForSP.getValueAt(i, 5));
						dao_cthd.createChiTietHoaDon(cthd);
					}
				}
				lamMoiHoaDon();
				//                JOptionPane.showConfirmDialog(null, "Đã thêm vào danh sách chờ", "Thông báo", JOptionPane.YES_OPTION);
				JOptionPane.showMessageDialog(null, "Đã thêm vào danh sách chờ");
			}
		}
	}

	public void createBarcode() {
		try {
			Linear barcode = new Linear();
			barcode.setType(Linear.CODE128B);
			barcode.setData(txtTimSanPhamChon.getText());
			barcode.setI(11.0f);
			String fname = txtTimSanPhamChon.getText();
			barcode.renderBarcode("src/img/" + fname + ".png");
			JOptionPane.showMessageDialog(null, "Done");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Bug barcode");

		}
	}

	public boolean themSP() throws RemoteException {
		String maSP = txtTimSanPhamChon.getText().trim();
		DecimalFormat df = new DecimalFormat("#,##0");
		//**********
		DefaultTableModel modelInfo = (DefaultTableModel) tableInForSP.getModel();
		for (int i = 0; i < tableInForSP.getRowCount(); i++) {
			if (tableInForSP.getValueAt(i, 1).equals(maSP)) {
				int sl = (int) tableInForSP.getValueAt(i, 4);
				double gia = (double) tableInForSP.getValueAt(i, 3);
				tableInForSP.setValueAt(++sl, i, 4);
				tableInForSP.setValueAt(tinhThanhTien(sl, gia), i, 5);
				createInit();
				showPanelChange(pnlSanPhamDaChon, pnlSanPhamDaChonNotNull);
				return true;
			}

		}

		if (maSP.startsWith("S")) {
			Sach s = dao_sach.getSachtheoMa(maSP);
			modelInfo.addRow(new Object[]{sttSP, s.getMaSanPham(), s.getTenSanPham(),
					s.getDonGiaBan(), 1, tinhThanhTien(1, s.getDonGiaBan())});
			sttSP++;
		} else {
			VanPhongPham vpp = dao_vpp.getVPPtheoMa(maSP);
			modelInfo.addRow(new Object[]{sttSP, vpp.getMaSanPham(), vpp.getTenSanPham(),
					vpp.getDonGiaBan(), 1, tinhThanhTien(1, vpp.getDonGiaBan())});
			sttSP++;
		}
		createInit();
		showPanelChange(pnlSanPhamDaChon, pnlSanPhamDaChonNotNull);
		return false;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 * @throws RemoteException 
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() throws RemoteException {

		pnlBotLeftChange = new javax.swing.JPanel();
		jPanel14 = new javax.swing.JPanel();
		jPanel15 = new javax.swing.JPanel();
		jButton4 = new javax.swing.JButton();
		jLabel13 = new javax.swing.JLabel();
		jButton9 = new javax.swing.JButton();
		jPanel16 = new javax.swing.JPanel();
		lblTongTienMua = new javax.swing.JLabel();
		jLabel16 = new javax.swing.JLabel();
		jLabel19 = new javax.swing.JLabel();
		jLabel21 = new javax.swing.JLabel();
		jLabel17 = new javax.swing.JLabel();
		lblSDTKH = new javax.swing.JLabel();
		lblTongDonMua = new javax.swing.JLabel();
		lblTenKH = new javax.swing.JLabel();
		jLabel27 = new javax.swing.JLabel();
		lblMaKH = new javax.swing.JLabel();
		lblNhomKhachHang = new javax.swing.JLabel();
		lblMaKH2 = new javax.swing.JLabel();
		pnlSanPhamDaChonNotNull = new javax.swing.JPanel();
		jPanel19 = new javax.swing.JPanel();
		jPanel21 = new javax.swing.JPanel();
		menuScrollPane8 = new menuGui.MenuScrollPane();
		tableInForSP = new javax.swing.JTable();
		jDialogChonKH = new javax.swing.JDialog(frm,"Chọn",false);
		pnlChonNCC1 = new javax.swing.JPanel();
		menuScrollPane10 = new menuGui.MenuScrollPane();
		tableChonKH = new javax.swing.JTable();
		jPanel50 = new javax.swing.JPanel();
		jLabel149 = new javax.swing.JLabel();
		txtTimKH = new javax.swing.JTextField();
		pnlBotLeftNonKH = new javax.swing.JPanel();
		jPanel48 = new javax.swing.JPanel();
		jPanel51 = new javax.swing.JPanel();
		jLabel48 = new javax.swing.JLabel();
		jPanel12 = new javax.swing.JPanel();
		jLabel25 = new javax.swing.JLabel();
		jLabel26 = new javax.swing.JLabel();
		jDialogGhiSoLuong = new javax.swing.JDialog(frm,"Chọn",false);
		jPanel1 = new javax.swing.JPanel();
		jLabel113 = new javax.swing.JLabel();
		txtSoLuongSanPhamChon = new javax.swing.JTextField();
		btnXacNhanSoLuong = new javax.swing.JButton();
		lblTenSanPhamChon = new javax.swing.JLabel();
		jSeparator1 = new javax.swing.JSeparator();
		jDialogChonKhuyenMai = new javax.swing.JDialog(frm,"Chọn",false);
		pnlChonNCC2 = new javax.swing.JPanel();
		menuScrollPane12 = new menuGui.MenuScrollPane();
		tableChonKH1 = new javax.swing.JTable();
		jPanel52 = new javax.swing.JPanel();
		jLabel150 = new javax.swing.JLabel();
		txtTimNCC1 = new javax.swing.JTextField();
		pnlChange = new javax.swing.JPanel();
		pnlCenter = new javax.swing.JPanel();
		pnlCenter_Bottom = new javax.swing.JPanel();
		jPanel8 = new javax.swing.JPanel();
		pnlRight = new javax.swing.JPanel();
		jLabel12 = new javax.swing.JLabel();
		jPanel5 = new javax.swing.JPanel();
		jLabel29 = new javax.swing.JLabel();
		txtTienKhachDua = new javax.swing.JTextField();
		jLabel34 = new javax.swing.JLabel();
		menuScrollPane2 = new menuGui.MenuScrollPane();
		jTextAreaGhiChu = new javax.swing.JTextArea();
		jLabel111 = new javax.swing.JLabel();
		lblMaHoaDon = new javax.swing.JLabel();
		btnThanhToanHD = new javax.swing.JButton();
		btnInHoaDon = new javax.swing.JButton();
		btnThemDSCho = new javax.swing.JButton();
		jLabel52 = new javax.swing.JLabel();
		lblTongSoLuong = new javax.swing.JLabel();
		jLabel38 = new javax.swing.JLabel();
		lblTongTienThanhToan = new javax.swing.JLabel();
		jLabel42 = new javax.swing.JLabel();
		lblTienThua = new javax.swing.JLabel();
		jLabel59 = new javax.swing.JLabel();
		btnHoaDonMoi = new javax.swing.JButton();
		txtTienKhuyenMai = new javax.swing.JTextField();
		pnlSanPhamDaChon = new javax.swing.JPanel();
		pnlSanPhamDaChonNull = new javax.swing.JPanel();
		jLabel28 = new javax.swing.JLabel();
		jLabel30 = new javax.swing.JLabel();
		jLabel1 = new javax.swing.JLabel();
		jLabel60 = new javax.swing.JLabel();
		txtTienChietKhau = new javax.swing.JTextField();
		jLabel63 = new javax.swing.JLabel();
		lblTongTienDefault = new javax.swing.JLabel();
		btnChonKhuyenMai = new javax.swing.JButton();
		txtMaKhuyenMai = new javax.swing.JTextField();
		btnXoaKhuyenMai = new javax.swing.JButton();
		pnlBotLeft = new javax.swing.JPanel();
		pnlBotLeftReturn = new javax.swing.JPanel();
		jPanel10 = new javax.swing.JPanel();
		jLabel7 = new javax.swing.JLabel();
		btnKhachLe = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();
		jPanel11 = new javax.swing.JPanel();
		jLabel22 = new javax.swing.JLabel();
		jLabel24 = new javax.swing.JLabel();
		pnlTopLeft = new javax.swing.JPanel();
		pnlNonInFor = new javax.swing.JPanel();
		jPanel13 = new javax.swing.JPanel();
		jLabel5 = new javax.swing.JLabel();
		pnlNonInfo = new javax.swing.JPanel();
		pnlChonNCC = new javax.swing.JPanel();
		jPanel49 = new javax.swing.JPanel();
		jLabel148 = new javax.swing.JLabel();
		txtTimSanPhamChon = new javax.swing.JTextField();
		cboSortTabelChonSP = new javax.swing.JComboBox<>();
		menuScrollPane11 = new menuGui.MenuScrollPane();
		tableChonSP = new javax.swing.JTable();
		pnlCenter_Top = new javax.swing.JPanel();
		jLabel6 = new javax.swing.JLabel();
		lblNameLogin = new javax.swing.JLabel();
		date = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();

		pnlBotLeftChange.setBackground(new java.awt.Color(255, 255, 255));
		pnlBotLeftChange.setPreferredSize(new java.awt.Dimension(756, 268));
		pnlBotLeftChange.setLayout(new java.awt.BorderLayout());

		jPanel14.setBackground(new java.awt.Color(255, 255, 255));
		jPanel14.setPreferredSize(new java.awt.Dimension(756, 268));

		jPanel15.setBackground(new java.awt.Color(255, 255, 255));

		jButton4.setBackground(new java.awt.Color(3, 136, 253));
		jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
		jButton4.setForeground(new java.awt.Color(255, 255, 255));
		jButton4.setText("Chọn khách hàng");
		jButton4.setFocusable(false);
		jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButton4.setMargin(new java.awt.Insets(2, 10, 3, 10));
		jButton4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton4ActionPerformed(evt);
			}
		});

		jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
		jLabel13.setText("Thông tin khách hàng");

		jButton9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
		jButton9.setForeground(new java.awt.Color(3, 136, 253));
		jButton9.setText("Khách lẻ");
		jButton9.setFocusable(false);
		jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButton9.setMargin(new java.awt.Insets(2, 10, 3, 10));
		jButton9.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton9ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
		jPanel15.setLayout(jPanel15Layout);
		jPanel15Layout.setHorizontalGroup(
				jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel13)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
						.addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
				);
		jPanel15Layout.setVerticalGroup(
				jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel15Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jButton4)
										.addComponent(jButton9)))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);

		jPanel16.setBackground(new java.awt.Color(250, 250, 250));
		jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		lblTongTienMua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		lblTongTienMua.setText("500000000");

		jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel16.setText("Số điện thoại:                 ");

		jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel19.setText("Tổng số đơn hàng:        ");

		jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel21.setText("Mã khách hàng:");

		jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel17.setText("Họ và tên: ");

		lblSDTKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		lblSDTKH.setText("0965180325");

		lblTongDonMua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		lblTongDonMua.setText("20");

		lblTenKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		lblTenKH.setText("Nguyễn Châu Tình");

		jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel27.setText("Tổng số tiền đã mua: ");

		lblMaKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		lblMaKH.setText("500000000");

		lblNhomKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		lblNhomKhachHang.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		lblNhomKhachHang.setText("Nhóm khách hàng:");

		lblMaKH2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		lblMaKH2.setText("Nhóm khách hàng:");

		javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
		jPanel16.setLayout(jPanel16Layout);
		jPanel16Layout.setHorizontalGroup(
				jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel16Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
										.addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addComponent(lblMaKH2))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(lblSDTKH, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTongDonMua, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTongTienMua, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(lblMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNhomKhachHang))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		jPanel16Layout.setVerticalGroup(
				jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel16Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel17)
								.addComponent(lblTenKH))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel16)
								.addComponent(lblSDTKH))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel19)
								.addComponent(lblTongDonMua))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel27)
								.addComponent(lblTongTienMua))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel21)
								.addComponent(lblMaKH))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(lblMaKH2)
								.addComponent(lblNhomKhachHang))
						.addContainerGap(24, Short.MAX_VALUE))
				);

		javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
		jPanel14.setLayout(jPanel14Layout);
		jPanel14Layout.setHorizontalGroup(
				jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel14Layout.createSequentialGroup()
						.addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel14Layout.createSequentialGroup()
										.addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(0, 0, Short.MAX_VALUE))
								.addGroup(jPanel14Layout.createSequentialGroup()
										.addContainerGap()
										.addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addContainerGap())
				);
		jPanel14Layout.setVerticalGroup(
				jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel14Layout.createSequentialGroup()
						.addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap())
				);

		pnlBotLeftChange.add(jPanel14, java.awt.BorderLayout.CENTER);

		pnlSanPhamDaChonNotNull.setBackground(new java.awt.Color(204, 204, 255));
		pnlSanPhamDaChonNotNull.setPreferredSize(new java.awt.Dimension(475, 297));
		pnlSanPhamDaChonNotNull.setLayout(new java.awt.BorderLayout());

		jPanel19.setBackground(new java.awt.Color(255, 255, 255));
		jPanel19.setLayout(new java.awt.BorderLayout());

		menuScrollPane8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		menuScrollPane8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

		tableInForSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
		tableInForSP.setModel(new javax.swing.table.DefaultTableModel(
				new Object [][] {

				},
				new String [] {
						"STT", "Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền", ""
				}
				) {
			Class[] types = new Class [] {
					java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class
			};
			boolean[] canEdit = new boolean [] {
					false, false, false, false, false, false, true
			};

			public Class getColumnClass(int columnIndex) {
				return types [columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit [columnIndex];
			}
		});
		tableInForSP.setFocusable(false);
		tableInForSP.setGridColor(new java.awt.Color(255, 255, 255));
		tableInForSP.setRowHeight(50);
		tableInForSP.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		tableInForSP.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		tableInForSP.getTableHeader().setReorderingAllowed(false);
		menuScrollPane8.setViewportView(tableInForSP);
		if (tableInForSP.getColumnModel().getColumnCount() > 0) {
			tableInForSP.getColumnModel().getColumn(0).setMinWidth(0);
			tableInForSP.getColumnModel().getColumn(0).setMaxWidth(0);
			tableInForSP.getColumnModel().getColumn(1).setMinWidth(0);
			tableInForSP.getColumnModel().getColumn(1).setMaxWidth(0);
			tableInForSP.getColumnModel().getColumn(3).setMinWidth(0);
			tableInForSP.getColumnModel().getColumn(3).setMaxWidth(0);
			tableInForSP.getColumnModel().getColumn(4).setMinWidth(70);
			tableInForSP.getColumnModel().getColumn(4).setMaxWidth(70);
			tableInForSP.getColumnModel().getColumn(5).setMinWidth(80);
			tableInForSP.getColumnModel().getColumn(5).setMaxWidth(80);
			tableInForSP.getColumnModel().getColumn(6).setMinWidth(80);
			tableInForSP.getColumnModel().getColumn(6).setMaxWidth(80);
		}

		javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
		jPanel21.setLayout(jPanel21Layout);
		jPanel21Layout.setHorizontalGroup(
				jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 475, Short.MAX_VALUE)
				.addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(menuScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE))
				);
		jPanel21Layout.setVerticalGroup(
				jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 297, Short.MAX_VALUE)
				.addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(menuScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
				);

		jPanel19.add(jPanel21, java.awt.BorderLayout.CENTER);

		pnlSanPhamDaChonNotNull.add(jPanel19, java.awt.BorderLayout.CENTER);

		jDialogChonKH.setBackground(new java.awt.Color(250, 250, 250));
		jDialogChonKH.setResizable(false);
		jDialogChonKH.setSize(new java.awt.Dimension(786, 437));
		jDialogChonKH.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
			public void windowGainedFocus(java.awt.event.WindowEvent evt) {
			}
			public void windowLostFocus(java.awt.event.WindowEvent evt) {
				jDialogChonKHWindowLostFocus(evt);
			}
		});

		pnlChonNCC1.setBackground(new java.awt.Color(250, 250, 250));
		pnlChonNCC1.setLayout(new java.awt.BorderLayout());

		menuScrollPane10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		menuScrollPane10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

		tableChonKH.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
		tableChonKH.setModel(new javax.swing.table.DefaultTableModel(
				new Object [][] {
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null},
					{null, null, null, null}
				},
				new String [] {
						"STT", "Mã khách hàng", "Tên khách hàng", "Số điện thoại"
				}
				) {
			boolean[] canEdit = new boolean [] {
					false, false, false, false
			};

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit [columnIndex];
			}
		});
		tableChonKH.setGridColor(new java.awt.Color(255, 255, 255));
		tableChonKH.setRowHeight(60);
		tableChonKH.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		tableChonKH.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		tableChonKH.getTableHeader().setReorderingAllowed(false);
		tableChonKH.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				try {
					tableChonKHMouseClicked(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		menuScrollPane10.setViewportView(tableChonKH);
		if (tableChonKH.getColumnModel().getColumnCount() > 0) {
			tableChonKH.getColumnModel().getColumn(0).setMinWidth(40);
			tableChonKH.getColumnModel().getColumn(0).setMaxWidth(40);
		}

		pnlChonNCC1.add(menuScrollPane10, java.awt.BorderLayout.CENTER);

		jPanel50.setBackground(new java.awt.Color(255, 255, 255));

		jLabel149.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel149.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/magnifying-glass.png"))); // NOI18N

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

		javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
		jPanel50.setLayout(jPanel50Layout);
		jPanel50Layout.setHorizontalGroup(
				jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel50Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel149, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(847, Short.MAX_VALUE))
				.addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel50Layout.createSequentialGroup()
								.addGap(87, 87, 87)
								.addComponent(txtTimKH, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
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
								.addComponent(txtTimKH, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
				);

		pnlChonNCC1.add(jPanel50, java.awt.BorderLayout.PAGE_START);

		jDialogChonKH.getContentPane().add(pnlChonNCC1, java.awt.BorderLayout.CENTER);

		pnlBotLeftNonKH.setBackground(new java.awt.Color(255, 255, 255));
		pnlBotLeftNonKH.setLayout(new java.awt.BorderLayout());

		jPanel48.setBackground(new java.awt.Color(255, 255, 255));

		jPanel51.setBackground(new java.awt.Color(255, 255, 255));

		jLabel48.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
		jLabel48.setText("Thông tin khách hàng");

		javax.swing.GroupLayout jPanel51Layout = new javax.swing.GroupLayout(jPanel51);
		jPanel51.setLayout(jPanel51Layout);
		jPanel51Layout.setHorizontalGroup(
				jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel51Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel48)
						.addContainerGap(614, Short.MAX_VALUE))
				);
		jPanel51Layout.setVerticalGroup(
				jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel51Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);

		jPanel12.setBackground(new java.awt.Color(250, 250, 250));
		jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
		jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/id-card.png"))); // NOI18N

		jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
		jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel26.setText("Chưa có thông tin khách hàng");

		javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
		jPanel12.setLayout(jPanel12Layout);
		jPanel12Layout.setHorizontalGroup(
				jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel12Layout.createSequentialGroup()
						.addGap(352, 352, 352)
						.addComponent(jLabel25)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jLabel26)
						.addGap(220, 220, 220))
				);
		jPanel12Layout.setVerticalGroup(
				jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel12Layout.createSequentialGroup()
						.addGap(43, 43, 43)
						.addComponent(jLabel25)
						.addGap(18, 18, 18)
						.addComponent(jLabel26)
						.addContainerGap(38, Short.MAX_VALUE))
				);

		javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
		jPanel48.setLayout(jPanel48Layout);
		jPanel48Layout.setHorizontalGroup(
				jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jPanel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(jPanel48Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap())
				);
		jPanel48Layout.setVerticalGroup(
				jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel48Layout.createSequentialGroup()
						.addComponent(jPanel51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(16, 16, 16)
						.addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap())
				);

		pnlBotLeftNonKH.add(jPanel48, java.awt.BorderLayout.CENTER);

		jDialogGhiSoLuong.setBackground(new java.awt.Color(250, 250, 250));
		jDialogGhiSoLuong.setUndecorated(true);
		jDialogGhiSoLuong.setResizable(false);
		jDialogGhiSoLuong.setSize(new java.awt.Dimension(800, 300));
		jDialogGhiSoLuong.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
			public void windowGainedFocus(java.awt.event.WindowEvent evt) {
			}
			public void windowLostFocus(java.awt.event.WindowEvent evt) {
				jDialogGhiSoLuongWindowLostFocus(evt);
			}
		});

		jPanel1.setBackground(new java.awt.Color(250, 250, 250));
		jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel1.setPreferredSize(new java.awt.Dimension(440, 200));

		jLabel113.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel113.setText("Số lượng:");

		txtSoLuongSanPhamChon.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		txtSoLuongSanPhamChon.setText("1");
		txtSoLuongSanPhamChon.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				txtSoLuongSanPhamChonActionPerformed(evt);
			}
		});
		txtSoLuongSanPhamChon.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				txtSoLuongSanPhamChonKeyReleased(evt);
			}
		});

		btnXacNhanSoLuong.setBackground(new java.awt.Color(3, 136, 253));
		btnXacNhanSoLuong.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
		btnXacNhanSoLuong.setForeground(new java.awt.Color(255, 255, 255));
		btnXacNhanSoLuong.setText("Xác nhận");
		btnXacNhanSoLuong.setFocusable(false);
		btnXacNhanSoLuong.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnXacNhanSoLuong.setMargin(new java.awt.Insets(2, 10, 3, 10));
		btnXacNhanSoLuong.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					btnXacNhanSoLuongActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		lblTenSanPhamChon.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		lblTenSanPhamChon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lblTenSanPhamChon.setText(" ");

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(lblTenSanPhamChon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(jPanel1Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel1Layout.createSequentialGroup()
										.addComponent(jSeparator1)
										.addContainerGap())
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
										.addGap(0, 0, Short.MAX_VALUE)
										.addComponent(jLabel113)
										.addGap(46, 46, 46)
										.addComponent(txtSoLuongSanPhamChon, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(240, 240, 240))))
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnXacNhanSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(27, 27, 27))
				);
		jPanel1Layout.setVerticalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
						.addGap(18, 18, 18)
						.addComponent(lblTenSanPhamChon, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(4, 4, 4)
						.addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel113, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(txtSoLuongSanPhamChon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
						.addComponent(btnXacNhanSoLuong)
						.addGap(26, 26, 26))
				);

		jDialogGhiSoLuong.getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

		jDialogChonKhuyenMai.setBackground(new java.awt.Color(250, 250, 250));
		jDialogChonKhuyenMai.setResizable(false);
		jDialogChonKhuyenMai.setSize(new java.awt.Dimension(786, 437));
		jDialogChonKhuyenMai.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
			public void windowGainedFocus(java.awt.event.WindowEvent evt) {
			}
			public void windowLostFocus(java.awt.event.WindowEvent evt) {
				jDialogChonKhuyenMaiWindowLostFocus(evt);
			}
		});

		pnlChonNCC2.setBackground(new java.awt.Color(250, 250, 250));
		pnlChonNCC2.setLayout(new java.awt.BorderLayout());

		menuScrollPane12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		menuScrollPane12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

		tableChonKH1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
		tableChonKH1.setModel(new javax.swing.table.DefaultTableModel(
				new Object [][] {
					{null, null, null, null, null, null},
					{null, null, null, null, null, null},
					{null, null, null, null, null, null},
					{null, null, null, null, null, null},
					{null, null, null, null, null, null},
					{null, null, null, null, null, null},
					{null, null, null, null, null, null}
				},
				new String [] {
						"Mã khuyến mãi", "Tên khuyến mãi", "Tỉ lệ", "Tiền tối thiểu", "Khuyến mãi tối đa", "Ghi chú"
				}
				) {
			boolean[] canEdit = new boolean [] {
					false, false, false, false, false, false
			};

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit [columnIndex];
			}
		});
		tableChonKH1.setGridColor(new java.awt.Color(255, 255, 255));
		tableChonKH1.setRowHeight(40);
		tableChonKH1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		tableChonKH1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		tableChonKH1.getTableHeader().setReorderingAllowed(false);
		tableChonKH1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				tableChonKH1MouseClicked(evt);
			}
		});
		menuScrollPane12.setViewportView(tableChonKH1);

		pnlChonNCC2.add(menuScrollPane12, java.awt.BorderLayout.CENTER);

		jPanel52.setBackground(new java.awt.Color(255, 255, 255));

		jLabel150.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel150.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/magnifying-glass.png"))); // NOI18N

		txtTimNCC1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
		txtTimNCC1.setText("Nhập vào thông tin tìm kiếm...");
		txtTimNCC1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		txtTimNCC1.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				txtTimNCC1FocusGained(evt);
			}
			public void focusLost(java.awt.event.FocusEvent evt) {
				txtTimNCC1FocusLost(evt);
			}
		});
		txtTimNCC1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				txtTimNCC1ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
		jPanel52.setLayout(jPanel52Layout);
		jPanel52Layout.setHorizontalGroup(
				jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel150, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(935, Short.MAX_VALUE))
				.addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel52Layout.createSequentialGroup()
								.addGap(87, 87, 87)
								.addComponent(txtTimNCC1, javax.swing.GroupLayout.DEFAULT_SIZE, 916, Short.MAX_VALUE)
								.addContainerGap()))
				);
		jPanel52Layout.setVerticalGroup(
				jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jLabel150, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
						.addContainerGap())
				.addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel52Layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(txtTimNCC1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
				);

		pnlChonNCC2.add(jPanel52, java.awt.BorderLayout.PAGE_START);

		jDialogChonKhuyenMai.getContentPane().add(pnlChonNCC2, java.awt.BorderLayout.CENTER);

		setBackground(new java.awt.Color(255, 255, 255));
		setPreferredSize(new java.awt.Dimension(1690, 787));

		pnlChange.setLayout(new java.awt.BorderLayout());

		pnlCenter.setBackground(new java.awt.Color(153, 153, 153));
		pnlCenter.setLayout(new java.awt.BorderLayout());

		pnlCenter_Bottom.setBackground(new java.awt.Color(153, 153, 153));

		jPanel8.setBackground(new java.awt.Color(240, 242, 245));

		pnlRight.setBackground(new java.awt.Color(255, 255, 255));

		jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
		jLabel12.setText("Thông tin hóa đơn");

		jPanel5.setBackground(new java.awt.Color(250, 250, 250));
		jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel29.setText("Tiền khách đưa:");

		txtTienKhachDua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		txtTienKhachDua.setText("0.0");
		txtTienKhachDua.setPreferredSize(new java.awt.Dimension(250, 31));
		txtTienKhachDua.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				txtTienKhachDuaFocusLost(evt);
			}
		});
		txtTienKhachDua.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				txtTienKhachDuaActionPerformed(evt);
			}
		});
		txtTienKhachDua.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				txtTienKhachDuaKeyReleased(evt);
			}
		});

		jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel34.setText("Ghi chú:");

		jTextAreaGhiChu.setColumns(20);
		jTextAreaGhiChu.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
		jTextAreaGhiChu.setRows(5);
		menuScrollPane2.setViewportView(jTextAreaGhiChu);

		jLabel111.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel111.setText("Mã hóa đơn: ");

		lblMaHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		lblMaHoaDon.setText("6546451646");

		btnThanhToanHD.setBackground(new java.awt.Color(3, 136, 253));
		btnThanhToanHD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
		btnThanhToanHD.setForeground(new java.awt.Color(255, 255, 255));
		btnThanhToanHD.setText("Thanh toán (F8)");
		btnThanhToanHD.setFocusable(false);
		btnThanhToanHD.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnThanhToanHD.setMargin(new java.awt.Insets(2, 10, 3, 10));
		btnThanhToanHD.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnThanhToanHDActionPerformed(evt);
			}
		});
		btnThanhToanHD.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				btnThanhToanHDKeyPressed(evt);
			}
			public void keyTyped(java.awt.event.KeyEvent evt) {
				btnThanhToanHDKeyTyped(evt);
			}
		});

		btnInHoaDon.setBackground(new java.awt.Color(3, 136, 253));
		btnInHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
		btnInHoaDon.setForeground(new java.awt.Color(255, 255, 255));
		btnInHoaDon.setText("In hóa đơn (F9)");
		btnInHoaDon.setFocusable(false);
		btnInHoaDon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnInHoaDon.setMargin(new java.awt.Insets(2, 10, 3, 10));
		btnInHoaDon.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnInHoaDonActionPerformed(evt);
			}
		});

		btnThemDSCho.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
		btnThemDSCho.setForeground(new java.awt.Color(3, 136, 253));
		btnThemDSCho.setText("Thêm vào danh sách đơn (F7)");
		btnThemDSCho.setFocusable(false);
		btnThemDSCho.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnThemDSCho.setMargin(new java.awt.Insets(2, 5, 3, 5));
		btnThemDSCho.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnThemDSChoActionPerformed(evt);
			}
		});

		jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel52.setText("Tổng số lượng sản phẩm:");

		lblTongSoLuong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		lblTongSoLuong.setText("0");

		jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel38.setText("Tổng tiền cần thanh toán:");

		lblTongTienThanhToan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		lblTongTienThanhToan.setText("0.0");

		jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel42.setText("Tiền thừa:");

		lblTienThua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		lblTienThua.setText("0.0");

		jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel59.setText("Tổng tiền:");

		btnHoaDonMoi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
		btnHoaDonMoi.setForeground(new java.awt.Color(3, 136, 253));
		btnHoaDonMoi.setText("Làm mới (F5)");
		btnHoaDonMoi.setFocusable(false);
		btnHoaDonMoi.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnHoaDonMoi.setMargin(new java.awt.Insets(2, 10, 3, 10));
		btnHoaDonMoi.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnHoaDonMoiActionPerformed(evt);
			}
		});

		txtTienKhuyenMai.setEditable(false);
		txtTienKhuyenMai.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		txtTienKhuyenMai.setText("0.0");

		pnlSanPhamDaChon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		pnlSanPhamDaChon.setLayout(new java.awt.BorderLayout());

		pnlSanPhamDaChonNull.setBackground(new java.awt.Color(255, 255, 255));

		jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
		jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/box.png"))); // NOI18N

		jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
		jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel30.setText("Chưa có thông tin sản phẩm");

		javax.swing.GroupLayout pnlSanPhamDaChonNullLayout = new javax.swing.GroupLayout(pnlSanPhamDaChonNull);
		pnlSanPhamDaChonNull.setLayout(pnlSanPhamDaChonNullLayout);
		pnlSanPhamDaChonNullLayout.setHorizontalGroup(
				pnlSanPhamDaChonNullLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlSanPhamDaChonNullLayout.createSequentialGroup()
						.addGroup(pnlSanPhamDaChonNullLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(pnlSanPhamDaChonNullLayout.createSequentialGroup()
										.addGap(117, 117, 117)
										.addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(pnlSanPhamDaChonNullLayout.createSequentialGroup()
										.addGap(200, 200, 200)
										.addComponent(jLabel28)))
						.addContainerGap(142, Short.MAX_VALUE))
				);
		pnlSanPhamDaChonNullLayout.setVerticalGroup(
				pnlSanPhamDaChonNullLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSanPhamDaChonNullLayout.createSequentialGroup()
						.addContainerGap(81, Short.MAX_VALUE)
						.addComponent(jLabel28)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(72, 72, 72))
				);

		pnlSanPhamDaChon.add(pnlSanPhamDaChonNull, java.awt.BorderLayout.CENTER);

		jLabel1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
		jLabel1.setText("Sản phẩm đã chọn");

		jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel60.setText("Khuyến mãi:");

		txtTienChietKhau.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		txtTienChietKhau.setText("0.0");
		txtTienChietKhau.setPreferredSize(new java.awt.Dimension(250, 31));
		txtTienChietKhau.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				txtTienChietKhauFocusLost(evt);
			}
		});
		txtTienChietKhau.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				txtTienChietKhauKeyReleased(evt);
			}
		});

		jLabel63.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		jLabel63.setText("Chiết khấu:");

		lblTongTienDefault.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
		lblTongTienDefault.setText("0.0");

		btnChonKhuyenMai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
		btnChonKhuyenMai.setForeground(new java.awt.Color(3, 136, 253));
		btnChonKhuyenMai.setText("Chọn");
		btnChonKhuyenMai.setFocusable(false);
		btnChonKhuyenMai.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnChonKhuyenMai.setMargin(new java.awt.Insets(2, 5, 3, 5));
		btnChonKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnChonKhuyenMaiActionPerformed(evt);
			}
		});

		txtMaKhuyenMai.setEditable(false);
		txtMaKhuyenMai.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

		btnXoaKhuyenMai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
		btnXoaKhuyenMai.setForeground(new java.awt.Color(3, 136, 253));
		btnXoaKhuyenMai.setText("Xóa");
		btnXoaKhuyenMai.setFocusable(false);
		btnXoaKhuyenMai.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnXoaKhuyenMai.setMargin(new java.awt.Insets(2, 5, 3, 5));
		btnXoaKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnXoaKhuyenMaiActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
		jPanel5.setLayout(jPanel5Layout);
		jPanel5Layout.setHorizontalGroup(
				jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel5Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(pnlSanPhamDaChon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(jPanel5Layout.createSequentialGroup()
										.addComponent(jLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(lblMaHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
										.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(btnThemDSCho, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
												.addComponent(btnHoaDonMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(btnInHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(btnThanhToanHD, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)))
								.addGroup(jPanel5Layout.createSequentialGroup()
										.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel29))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(lblTongTienThanhToan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(lblTienThua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
														.addGap(0, 0, Short.MAX_VALUE)
														.addComponent(txtTienKhachDua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
								.addGroup(jPanel5Layout.createSequentialGroup()
										.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jLabel34)
												.addComponent(jLabel1))
										.addGap(45, 45, 45)
										.addComponent(menuScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGroup(jPanel5Layout.createSequentialGroup()
										.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(jLabel60, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(jPanel5Layout.createSequentialGroup()
														.addComponent(txtTienKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(txtMaKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addComponent(lblTongTienDefault, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(btnChonKhuyenMai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(btnXoaKhuyenMai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addGroup(jPanel5Layout.createSequentialGroup()
										.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(jPanel5Layout.createSequentialGroup()
														.addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(lblTongSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGroup(jPanel5Layout.createSequentialGroup()
														.addComponent(jLabel63)
														.addGap(139, 139, 139)
														.addComponent(txtTienChietKhau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addGap(0, 0, Short.MAX_VALUE)))
						.addContainerGap())
				);
		jPanel5Layout.setVerticalGroup(
				jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel5Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(lblMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addGroup(jPanel5Layout.createSequentialGroup()
										.addComponent(jLabel34)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jLabel1))
								.addComponent(menuScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(pnlSanPhamDaChon, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(txtTienKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(txtMaKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addComponent(btnChonKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(lblTongTienDefault, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
								.addComponent(btnXoaKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTongSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel63)
								.addComponent(txtTienChietKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblTienThua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblTongTienThanhToan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(btnInHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(btnThemDSCho, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(btnThanhToanHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnHoaDonMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap())
				);

		javax.swing.GroupLayout pnlRightLayout = new javax.swing.GroupLayout(pnlRight);
		pnlRight.setLayout(pnlRightLayout);
		pnlRightLayout.setHorizontalGroup(
				pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlRightLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(pnlRightLayout.createSequentialGroup()
										.addComponent(jLabel12)
										.addGap(0, 0, Short.MAX_VALUE))
								.addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap())
				);
		pnlRightLayout.setVerticalGroup(
				pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlRightLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap())
				);

		pnlBotLeft.setBackground(new java.awt.Color(255, 255, 255));
		pnlBotLeft.setLayout(new java.awt.BorderLayout());

		pnlBotLeftReturn.setBackground(new java.awt.Color(255, 255, 255));

		jPanel10.setBackground(new java.awt.Color(255, 255, 255));

		jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
		jLabel7.setText("Thông tin khách hàng");

		btnKhachLe.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
		btnKhachLe.setForeground(new java.awt.Color(3, 136, 253));
		btnKhachLe.setText("Khách lẻ");
		btnKhachLe.setFocusable(false);
		btnKhachLe.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnKhachLe.setMargin(new java.awt.Insets(2, 10, 3, 10));
		btnKhachLe.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnKhachLeActionPerformed(evt);
			}
		});

		jButton3.setBackground(new java.awt.Color(3, 136, 253));
		jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
		jButton3.setForeground(new java.awt.Color(255, 255, 255));
		jButton3.setText("Chọn khách hàng");
		jButton3.setFocusable(false);
		jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButton3.setMargin(new java.awt.Insets(2, 10, 3, 10));
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
		jPanel10.setLayout(jPanel10Layout);
		jPanel10Layout.setHorizontalGroup(
				jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel7)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 240, Short.MAX_VALUE)
						.addComponent(btnKhachLe, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
				);
		jPanel10Layout.setVerticalGroup(
				jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel10Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(btnKhachLe)
								.addComponent(jButton3))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);

		jPanel11.setBackground(new java.awt.Color(250, 250, 250));
		jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
		jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/id-card.png"))); // NOI18N

		jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
		jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel24.setText("Chưa có thông tin khách hàng");

		javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
		jPanel11.setLayout(jPanel11Layout);
		jPanel11Layout.setHorizontalGroup(
				jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jLabel24)
						.addGap(220, 220, 220))
				.addGroup(jPanel11Layout.createSequentialGroup()
						.addGap(326, 326, 326)
						.addComponent(jLabel22)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		jPanel11Layout.setVerticalGroup(
				jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel11Layout.createSequentialGroup()
						.addGap(43, 43, 43)
						.addComponent(jLabel22)
						.addGap(18, 18, 18)
						.addComponent(jLabel24)
						.addContainerGap(53, Short.MAX_VALUE))
				);

		javax.swing.GroupLayout pnlBotLeftReturnLayout = new javax.swing.GroupLayout(pnlBotLeftReturn);
		pnlBotLeftReturn.setLayout(pnlBotLeftReturnLayout);
		pnlBotLeftReturnLayout.setHorizontalGroup(
				pnlBotLeftReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlBotLeftReturnLayout.createSequentialGroup()
						.addGroup(pnlBotLeftReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(pnlBotLeftReturnLayout.createSequentialGroup()
										.addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(0, 0, Short.MAX_VALUE))
								.addGroup(pnlBotLeftReturnLayout.createSequentialGroup()
										.addContainerGap()
										.addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addContainerGap())
				);
		pnlBotLeftReturnLayout.setVerticalGroup(
				pnlBotLeftReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlBotLeftReturnLayout.createSequentialGroup()
						.addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap())
				);

		pnlBotLeft.add(pnlBotLeftReturn, java.awt.BorderLayout.CENTER);

		pnlTopLeft.setBackground(new java.awt.Color(204, 204, 255));
		pnlTopLeft.setPreferredSize(new java.awt.Dimension(786, 496));
		pnlTopLeft.setLayout(new java.awt.BorderLayout());

		pnlNonInFor.setBackground(new java.awt.Color(255, 255, 255));

		jPanel13.setBackground(new java.awt.Color(255, 255, 255));

		jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
		jLabel5.setText("Thông tin sản phẩm");

		javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
		jPanel13.setLayout(jPanel13Layout);
		jPanel13Layout.setHorizontalGroup(
				jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel13Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel5)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		jPanel13Layout.setVerticalGroup(
				jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel13Layout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
				);

		pnlNonInfo.setBackground(new java.awt.Color(250, 250, 250));
		pnlNonInfo.setLayout(new java.awt.BorderLayout());

		pnlChonNCC.setBackground(new java.awt.Color(250, 250, 250));
		pnlChonNCC.setLayout(new java.awt.BorderLayout());

		jPanel49.setBackground(new java.awt.Color(255, 255, 255));

		jLabel148.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel148.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/magnifying-glass.png"))); // NOI18N

		txtTimSanPhamChon.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
		txtTimSanPhamChon.setText("Nhập vào thông tin tìm kiếm...");
		txtTimSanPhamChon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		txtTimSanPhamChon.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				txtTimSanPhamChonFocusGained(evt);
			}
			public void focusLost(java.awt.event.FocusEvent evt) {
				txtTimSanPhamChonFocusLost(evt);
			}
		});
		txtTimSanPhamChon.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				txtTimSanPhamChonActionPerformed(evt);
			}
		});
		txtTimSanPhamChon.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				txtTimSanPhamChonKeyReleased(evt);
			}
		});

		cboSortTabelChonSP.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
		cboSortTabelChonSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
		cboSortTabelChonSP.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cboSortTabelChonSPActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
		jPanel49.setLayout(jPanel49Layout);
		jPanel49Layout.setHorizontalGroup(
				jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel49Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel148, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 519, Short.MAX_VALUE)
						.addComponent(cboSortTabelChonSP, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel49Layout.createSequentialGroup()
								.addContainerGap(87, Short.MAX_VALUE)
								.addComponent(txtTimSanPhamChon, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(182, 182, 182)))
				);
		jPanel49Layout.setVerticalGroup(
				jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel49Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel148, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
								.addComponent(cboSortTabelChonSP))
						.addContainerGap())
				.addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel49Layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(txtTimSanPhamChon, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
				);

		List<NhomSanPham> listNhomSP = dao_nsp.getAllNhomSanPham();
		for(NhomSanPham nsp : listNhomSP) {
			cboSortTabelChonSP.addItem(nsp.getTenNhomSanPham());
		}

		pnlChonNCC.add(jPanel49, java.awt.BorderLayout.PAGE_START);

		menuScrollPane11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		menuScrollPane11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

		tableChonSP.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
		tableChonSP.setModel(new javax.swing.table.DefaultTableModel(
				new Object [][] {
					{null, null, null, null, null, null},
					{null, null, null, null, null, null},
					{null, null, null, null, null, null}
				},
				new String [] {
						"STT", "Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng", "Tình trạng"
				}
				) {
			Class[] types = new Class [] {
					java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class
			};
			boolean[] canEdit = new boolean [] {
					false, false, false, false, false, false
			};

			public Class getColumnClass(int columnIndex) {
				return types [columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit [columnIndex];
			}
		});
		tableChonSP.setGridColor(new java.awt.Color(255, 255, 255));
		tableChonSP.setRowHeight(60);
		tableChonSP.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		tableChonSP.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		tableChonSP.setShowHorizontalLines(true);
		tableChonSP.getTableHeader().setReorderingAllowed(false);
		tableChonSP.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				tableChonSPMouseClicked(evt);
			}
		});
		menuScrollPane11.setViewportView(tableChonSP);
		if (tableChonSP.getColumnModel().getColumnCount() > 0) {
			tableChonSP.getColumnModel().getColumn(0).setMinWidth(40);
			tableChonSP.getColumnModel().getColumn(0).setMaxWidth(40);
			tableChonSP.getColumnModel().getColumn(1).setMinWidth(170);
			tableChonSP.getColumnModel().getColumn(1).setMaxWidth(170);
			tableChonSP.getColumnModel().getColumn(3).setMinWidth(100);
			tableChonSP.getColumnModel().getColumn(3).setMaxWidth(100);
			tableChonSP.getColumnModel().getColumn(4).setMinWidth(80);
			tableChonSP.getColumnModel().getColumn(4).setMaxWidth(80);
			tableChonSP.getColumnModel().getColumn(5).setMinWidth(0);
			tableChonSP.getColumnModel().getColumn(5).setMaxWidth(0);
		}

		pnlChonNCC.add(menuScrollPane11, java.awt.BorderLayout.CENTER);

		pnlNonInfo.add(pnlChonNCC, java.awt.BorderLayout.CENTER);

		javax.swing.GroupLayout pnlNonInForLayout = new javax.swing.GroupLayout(pnlNonInFor);
		pnlNonInFor.setLayout(pnlNonInForLayout);
		pnlNonInForLayout.setHorizontalGroup(
				pnlNonInForLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(pnlNonInForLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(pnlNonInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap())
				);
		pnlNonInForLayout.setVerticalGroup(
				pnlNonInForLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlNonInForLayout.createSequentialGroup()
						.addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(pnlNonInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
						.addContainerGap())
				);

		pnlTopLeft.add(pnlNonInFor, java.awt.BorderLayout.CENTER);

		javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
		jPanel8.setLayout(jPanel8Layout);
		jPanel8Layout.setHorizontalGroup(
				jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel8Layout.createSequentialGroup()
						.addContainerGap(16, Short.MAX_VALUE)
						.addComponent(pnlBotLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(pnlRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(19, 19, 19))
				.addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
								.addContainerGap(15, Short.MAX_VALUE)
								.addComponent(pnlTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(539, Short.MAX_VALUE)))
				);
		jPanel8Layout.setVerticalGroup(
				jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel8Layout.createSequentialGroup()
						.addGap(15, 15, 15)
						.addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(pnlBotLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(pnlRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(61, Short.MAX_VALUE))
				.addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
								.addContainerGap(294, Short.MAX_VALUE)
								.addComponent(pnlTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(43, Short.MAX_VALUE)))
				);

		javax.swing.GroupLayout pnlCenter_BottomLayout = new javax.swing.GroupLayout(pnlCenter_Bottom);
		pnlCenter_Bottom.setLayout(pnlCenter_BottomLayout);
		pnlCenter_BottomLayout.setHorizontalGroup(
				pnlCenter_BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlCenter_BottomLayout.createSequentialGroup()
						.addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(0, 0, 0))
				);
		pnlCenter_BottomLayout.setVerticalGroup(
				pnlCenter_BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlCenter_BottomLayout.createSequentialGroup()
						.addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(0, 0, Short.MAX_VALUE))
				);

		pnlCenter.add(pnlCenter_Bottom, java.awt.BorderLayout.CENTER);

		pnlCenter_Top.setBackground(new java.awt.Color(250, 250, 250));
		pnlCenter_Top.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
		pnlCenter_Top.setPreferredSize(new java.awt.Dimension(1690, 56));

		jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
		jLabel6.setText("Lập hóa đơn");

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

		jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/icons8-user-50.png"))); // NOI18N

		javax.swing.GroupLayout pnlCenter_TopLayout = new javax.swing.GroupLayout(pnlCenter_Top);
		pnlCenter_Top.setLayout(pnlCenter_TopLayout);
		pnlCenter_TopLayout.setHorizontalGroup(
				pnlCenter_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCenter_TopLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel6)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 900, Short.MAX_VALUE)
						.addGroup(pnlCenter_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
								.addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNameLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jLabel4)
						.addContainerGap())
				);
		pnlCenter_TopLayout.setVerticalGroup(
				pnlCenter_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pnlCenter_TopLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
						.addContainerGap())
				.addGroup(pnlCenter_TopLayout.createSequentialGroup()
						.addGroup(pnlCenter_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(pnlCenter_TopLayout.createSequentialGroup()
										.addComponent(lblNameLogin)
										.addGap(2, 2, 2)
										.addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(0, 0, Short.MAX_VALUE))
				);

		pnlCenter.add(pnlCenter_Top, java.awt.BorderLayout.PAGE_START);

		pnlChange.add(pnlCenter, java.awt.BorderLayout.CENTER);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 1309, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								.addGap(0, 0, Short.MAX_VALUE)
								.addComponent(pnlChange, javax.swing.GroupLayout.PREFERRED_SIZE, 1309, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, Short.MAX_VALUE)))
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 895, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								.addGap(0, 0, Short.MAX_VALUE)
								.addComponent(pnlChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, Short.MAX_VALUE)))
				);
	}// </editor-fold>//GEN-END:initComponents

	private void btnInHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInHoaDonActionPerformed
		// TODO add your handling code here:
		inHoaDon();
	}//GEN-LAST:event_btnInHoaDonActionPerformed

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
		// TODO add your handling code here:
		jDialogChonKH.setLocationRelativeTo(null);
		jDialogChonKH.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jDialogChonKH.dispose();
		jDialogChonKH.setVisible(true);
		try {
			loadChonKH();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//GEN-LAST:event_jButton3ActionPerformed

	private void txtTimSanPhamChonFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimSanPhamChonFocusGained
		// TODO add your handling code here:
		frm.placeHoderTextGianed(txtTimSanPhamChon);
		//        tableChooseProductactions();
	}//GEN-LAST:event_txtTimSanPhamChonFocusGained

	private void txtTimSanPhamChonFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimSanPhamChonFocusLost
		// TODO add your handling code here:
		frm.placeHoderTextLost(txtTimSanPhamChon);
	}//GEN-LAST:event_txtTimSanPhamChonFocusLost

	private void txtTimSanPhamChonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimSanPhamChonActionPerformed
		// TODO add your handling code here:

	}//GEN-LAST:event_txtTimSanPhamChonActionPerformed

	private void txtTimKHFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKHFocusGained
		// TODO add your handling code here:
		frm.placeHoderTextGianed(txtTimKH);
	}//GEN-LAST:event_txtTimKHFocusGained

	private void txtTimKHFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKHFocusLost
		// TODO add your handling code here:
		frm.placeHoderTextLost(txtTimKH);
	}//GEN-LAST:event_txtTimKHFocusLost

	private void txtTimKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKHActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_txtTimKHActionPerformed

	private void tableChonSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableChonSPMouseClicked
		// TODO add your handling code here:
		if (evt.getClickCount() == 1 && !evt.isConsumed()) {
			evt.consume();
			int row = tableChonSP.getSelectedRow();
			int sl = (int) tableChonSP.getValueAt(row, 4);
			if (sl != 0) {
				lblTenSanPhamChon.setText((String) tableChonSP.getValueAt(row, 2));
				String maSP = (String) tableChonSP.getValueAt(row, 1);
				String ma = "";
				boolean value = false;
				for (int i = 0; i < tableInForSP.getRowCount(); i++) {
					ma = (String) tableInForSP.getValueAt(i, 1);
					if (ma.equalsIgnoreCase(maSP)) {
						value = true;
					}
				}
				if (value == false) {
					jDialogGhiSoLuong.setLocationRelativeTo(null);
					jDialogGhiSoLuong.setVisible(true);
				}
			}

		}
	}//GEN-LAST:event_tableChonSPMouseClicked

	private void btnThanhToanHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanHDActionPerformed
		// TODO add your handling code here:
		try {
			thanhToanHD();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//GEN-LAST:event_btnThanhToanHDActionPerformed

	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
		// TODO add your handling code here:
		jDialogChonKH.setLocationRelativeTo(null);
		jDialogChonKH.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jDialogChonKH.dispose();
		jDialogChonKH.setVisible(true);
		try {
			loadChonKH();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//GEN-LAST:event_jButton4ActionPerformed

	private void tableChonKHMouseClicked(java.awt.event.MouseEvent evt) throws RemoteException {//GEN-FIRST:event_tableChonKHMouseClicked
		// TODO add your handling code here:
		if (evt.getClickCount() == 1 && !evt.isConsumed()) {
			evt.consume();
			int row = tableChonKH.getSelectedRow();
			int rowChooser = tableChonKH.convertRowIndexToModel(row);
			DefaultTableModel modelChonKH = (DefaultTableModel) tableChonKH.getModel();
			String maKH = modelChonKH.getValueAt(rowChooser, 1).toString();
			KhachHang kh = dao_kh.getKHTheoMa(maKH);
			//********
			lblTenKH.setText(kh.getTenKhachHang());
			lblSDTKH.setText(kh.getSoDienThoai());
			lblTongDonMua.setText(kh.getSoLuongHoaDon() + "");
			lblTongTienMua.setText(kh.getTongTienMua() + "");
			lblMaKH.setText(maKH);
			lblNhomKhachHang.setText(kh.getNhomKhachHang() + "");
			if (kh.getNhomKhachHang() == kh.getNhomKhachHang().KHACHVIP) {
				createInit();
			}
			jDialogChonKH.setVisible(false);
			showPanelChange(pnlBotLeft, pnlBotLeftChange);
		}
	}//GEN-LAST:event_tableChonKHMouseClicked

	private void btnKhachLeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhachLeActionPerformed
		// TODO add your handling code here:
		showPanelChange(pnlBotLeft, pnlBotLeftReturn);
		lblTenKH.setText("Khách lẻ");
		try {
			lblMaKH.setText(createMaKhachHang());
			createInit();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}//GEN-LAST:event_btnKhachLeActionPerformed

	private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
		// TODO add your handling code here:
		showPanelChange(pnlBotLeft, pnlBotLeftReturn);
		lblTenKH.setText("Khách lẻ");
		try {
			lblMaKH.setText(createMaKhachHang());
			createInit();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}//GEN-LAST:event_jButton9ActionPerformed

	private void btnThemDSChoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemDSChoActionPerformed
		// TODO add your handling code here:
		if (tableInForSP.getRowCount() == 0) {
		} else {
			try {
				themDSCho();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}//GEN-LAST:event_btnThemDSChoActionPerformed

	private void btnHoaDonMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoaDonMoiActionPerformed
		// TODO add your handling code here:
		try {
			lamMoiHoaDon();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//GEN-LAST:event_btnHoaDonMoiActionPerformed

	private void txtTimSanPhamChonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimSanPhamChonKeyReleased
		// TODO add your handling code here:     
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
				themSP();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			txtTimSanPhamChon.setText("");
		}
		timJtableLapHoaDon();

	}//GEN-LAST:event_txtTimSanPhamChonKeyReleased

	private void txtTienKhachDuaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienKhachDuaKeyReleased
		// TODO add your handling code here:
		if (txtTienKhachDua.getText().equals("")) {
			txtTienKhachDua.setText("");
		}
		try {
			double tienKhach = Double.parseDouble(txtTienKhachDua.getText());
			double tienThua = tienKhach - Double.parseDouble(lblTongTienThanhToan.getText());
			if (tienThua < 0) {
				lblTienThua.setText("0.0");
			} else {
				lblTienThua.setText(deciFormat.format(tienThua));
			}
		} catch (Exception e) {
		}
	}//GEN-LAST:event_txtTienKhachDuaKeyReleased

	private void btnXacNhanSoLuongActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btnXacNhanSoLuongActionPerformed
		// TODO add your handling code here:
		if (!txtSoLuongSanPhamChon.getText().equalsIgnoreCase("")) {
			int row = tableChonSP.getSelectedRow();
			int rowChooser = tableChonSP.convertRowIndexToModel(row);
			DefaultTableModel modelChonSP = (DefaultTableModel) tableChonSP.getModel();
			String maSP = modelChonSP.getValueAt(rowChooser, 1).toString();

			//**********
			DefaultTableModel modelInfo = (DefaultTableModel) tableInForSP.getModel();
			DecimalFormat df = new DecimalFormat("#,##0");
			int sl = Integer.parseInt(txtSoLuongSanPhamChon.getText());
			if (maSP.startsWith("S")) {
				Sach s = dao_sach.getSachtheoMa(maSP);
				if (sl >= s.getSoLuongTon()) {
					sl = s.getSoLuongTon();
				}
				modelInfo.addRow(new Object[]{sttSP, s.getMaSanPham(), s.getTenSanPham(),
						s.getDonGiaBan(), sl, tinhThanhTien(sl, s.getDonGiaBan())});
				sttSP++;
			} else {
				VanPhongPham vpp = dao_vpp.getVPPtheoMa(maSP);
				if (sl >= vpp.getSoLuongTon()) {
					sl = vpp.getSoLuongTon();
				}
				modelInfo.addRow(new Object[]{sttSP, vpp.getMaSanPham(), vpp.getTenSanPham(),
						vpp.getDonGiaBan(), sl, tinhThanhTien(sl, vpp.getDonGiaBan())});
				sttSP++;
			}
			createInit();
			txtSoLuongSanPhamChon.setText("1");
			showPanelChange(pnlSanPhamDaChon, pnlSanPhamDaChonNotNull);

		}
		jDialogGhiSoLuong.setVisible(false);
	}//GEN-LAST:event_btnXacNhanSoLuongActionPerformed

	private void jDialogGhiSoLuongWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialogGhiSoLuongWindowLostFocus
		// TODO add your handling code here:
		jDialogGhiSoLuong.setVisible(false);
	}//GEN-LAST:event_jDialogGhiSoLuongWindowLostFocus

	private void jDialogChonKHWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialogChonKHWindowLostFocus
		// TODO add your handling code here:
		jDialogChonKH.setVisible(false);
	}//GEN-LAST:event_jDialogChonKHWindowLostFocus

	private void cboSortTabelChonSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSortTabelChonSPActionPerformed
		// TODO add your handling code here:
		try {
			sortTableChooseProDuct();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//GEN-LAST:event_cboSortTabelChonSPActionPerformed

	private void btnThanhToanHDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnThanhToanHDKeyPressed
		// TODO add your handling code here:
	}//GEN-LAST:event_btnThanhToanHDKeyPressed

	private void btnThanhToanHDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnThanhToanHDKeyTyped
		// TODO add your handling code here:
	}//GEN-LAST:event_btnThanhToanHDKeyTyped

	private void txtTienChietKhauKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienChietKhauKeyReleased
		// TODO add your handling code here:
		if (txtTienChietKhau.getText().equals("")) {
			txtTienChietKhau.setText("");
		}
		try {
			createInit();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//GEN-LAST:event_txtTienChietKhauKeyReleased

	private void txtSoLuongSanPhamChonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoLuongSanPhamChonKeyReleased
		// TODO add your handling code here:
		if (txtSoLuongSanPhamChon.getText().equals("")) {
			txtSoLuongSanPhamChon.setText("");
		}
	}//GEN-LAST:event_txtSoLuongSanPhamChonKeyReleased

	private void txtTimKHKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKHKeyReleased
		DefaultTableModel tableModel = (DefaultTableModel) tableChonKH.getModel();
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
		tableChonKH.setRowSorter(sorter);

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
					String maKH = entry.getValue(1).toString();
					String tenKH = entry.getValue(2).toString();
					String sdt = entry.getValue(3).toString();

					return pattern.matcher(maKH).lookingAt() || pattern.matcher(tenKH).lookingAt() || pattern.matcher(sdt).lookingAt();
				}
			});
		}
	}//GEN-LAST:event_txtTimKHKeyReleased

	private void txtTienKhachDuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienKhachDuaActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_txtTienKhachDuaActionPerformed

	private void lblNameLoginAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblNameLoginAncestorAdded
		// TODO add your handling code here:
		lblNameLogin.setText(gui.FrmLogin.tenNguoiDung);
	}//GEN-LAST:event_lblNameLoginAncestorAdded

	private void tableChonKH1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableChonKH1MouseClicked
		// TODO add your handling code here:
		int row = tableChonKH1.getSelectedRow();
		String ma = (String) tableChonKH1.getValueAt(row, 0);
		txtMaKhuyenMai.setText(ma);
		try {
			createInit();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jDialogChonKhuyenMai.setVisible(false);
	}//GEN-LAST:event_tableChonKH1MouseClicked

	private void txtTimNCC1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimNCC1FocusGained
		// TODO add your handling code here:
		frm.placeHoderTextGianed(txtTimNCC1);
	}//GEN-LAST:event_txtTimNCC1FocusGained

	private void txtTimNCC1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimNCC1FocusLost
		// TODO add your handling code here:
		frm.placeHoderTextLost(txtTimNCC1);
	}//GEN-LAST:event_txtTimNCC1FocusLost

	private void txtTimNCC1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimNCC1ActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_txtTimNCC1ActionPerformed

	private void btnChonKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonKhuyenMaiActionPerformed
		// TODO add your handling code here:
		try {
			loadKhuyenMai();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jDialogChonKhuyenMai.setLocationRelativeTo(null);
		jDialogChonKhuyenMai.setVisible(true);

	}//GEN-LAST:event_btnChonKhuyenMaiActionPerformed

	private void jDialogChonKhuyenMaiWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialogChonKhuyenMaiWindowLostFocus
		// TODO add your handling code here:
		jDialogChonKhuyenMai.setVisible(false);
	}//GEN-LAST:event_jDialogChonKhuyenMaiWindowLostFocus

	private void btnXoaKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaKhuyenMaiActionPerformed
		// TODO add your handling code here:
		txtMaKhuyenMai.setText("");
		try {
			createInit();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//GEN-LAST:event_btnXoaKhuyenMaiActionPerformed

	private void txtSoLuongSanPhamChonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoLuongSanPhamChonActionPerformed
		// TODO add your handling code here:
		txtSoLuongSanPhamChon.selectAll();
	}//GEN-LAST:event_txtSoLuongSanPhamChonActionPerformed

	private void txtTienChietKhauFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTienChietKhauFocusLost
		// TODO add your handling code here:
		if (txtTienChietKhau.getText().equals("")) {
			txtTienChietKhau.setText("0.0");
			if(Double.parseDouble(txtTienChietKhau.getText()) < 0) {
				txtTienChietKhau.setText("0.0");
			}
		}
	}//GEN-LAST:event_txtTienChietKhauFocusLost

	private void txtTienKhachDuaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTienKhachDuaFocusLost
		// TODO add your handling code here:
		if (txtTienKhachDua.getText().equals("")) {
			txtTienKhachDua.setText("0.0");
			if(Double.parseDouble(txtTienKhachDua.getText()) < 0) {
				txtTienKhachDua.setText("0.0");
			}
		}
	}//GEN-LAST:event_txtTienKhachDuaFocusLost


	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnChonKhuyenMai;
	private javax.swing.JButton btnHoaDonMoi;
	private javax.swing.JButton btnInHoaDon;
	private javax.swing.JButton btnKhachLe;
	private javax.swing.JButton btnThanhToanHD;
	private javax.swing.JButton btnThemDSCho;
	private javax.swing.JButton btnXacNhanSoLuong;
	private javax.swing.JButton btnXoaKhuyenMai;
	private javax.swing.JComboBox<String> cboSortTabelChonSP;
	private javax.swing.JLabel date;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JButton jButton9;
	private javax.swing.JDialog jDialogChonKH;
	private javax.swing.JDialog jDialogChonKhuyenMai;
	private javax.swing.JDialog jDialogGhiSoLuong;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel111;
	private javax.swing.JLabel jLabel113;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel148;
	private javax.swing.JLabel jLabel149;
	private javax.swing.JLabel jLabel150;
	private javax.swing.JLabel jLabel16;
	private javax.swing.JLabel jLabel17;
	private javax.swing.JLabel jLabel19;
	private javax.swing.JLabel jLabel21;
	private javax.swing.JLabel jLabel22;
	private javax.swing.JLabel jLabel24;
	private javax.swing.JLabel jLabel25;
	private javax.swing.JLabel jLabel26;
	private javax.swing.JLabel jLabel27;
	private javax.swing.JLabel jLabel28;
	private javax.swing.JLabel jLabel29;
	private javax.swing.JLabel jLabel30;
	private javax.swing.JLabel jLabel34;
	private javax.swing.JLabel jLabel38;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel42;
	private javax.swing.JLabel jLabel48;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel52;
	private javax.swing.JLabel jLabel59;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel60;
	private javax.swing.JLabel jLabel63;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel10;
	private javax.swing.JPanel jPanel11;
	private javax.swing.JPanel jPanel12;
	private javax.swing.JPanel jPanel13;
	private javax.swing.JPanel jPanel14;
	private javax.swing.JPanel jPanel15;
	private javax.swing.JPanel jPanel16;
	private javax.swing.JPanel jPanel19;
	private javax.swing.JPanel jPanel21;
	private javax.swing.JPanel jPanel48;
	private javax.swing.JPanel jPanel49;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel50;
	private javax.swing.JPanel jPanel51;
	private javax.swing.JPanel jPanel52;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JTextArea jTextAreaGhiChu;
	private javax.swing.JLabel lblMaHoaDon;
	private javax.swing.JLabel lblMaKH;
	private javax.swing.JLabel lblMaKH2;
	private javax.swing.JLabel lblNameLogin;
	private javax.swing.JLabel lblNhomKhachHang;
	private javax.swing.JLabel lblSDTKH;
	private javax.swing.JLabel lblTenKH;
	private javax.swing.JLabel lblTenSanPhamChon;
	private javax.swing.JLabel lblTienThua;
	private javax.swing.JLabel lblTongDonMua;
	private javax.swing.JLabel lblTongSoLuong;
	private javax.swing.JLabel lblTongTienDefault;
	private javax.swing.JLabel lblTongTienMua;
	private javax.swing.JLabel lblTongTienThanhToan;
	private menuGui.MenuScrollPane menuScrollPane10;
	private menuGui.MenuScrollPane menuScrollPane11;
	private menuGui.MenuScrollPane menuScrollPane12;
	private menuGui.MenuScrollPane menuScrollPane2;
	private menuGui.MenuScrollPane menuScrollPane8;
	private javax.swing.JPanel pnlBotLeft;
	private javax.swing.JPanel pnlBotLeftChange;
	private javax.swing.JPanel pnlBotLeftNonKH;
	private javax.swing.JPanel pnlBotLeftReturn;
	private javax.swing.JPanel pnlCenter;
	private javax.swing.JPanel pnlCenter_Bottom;
	private javax.swing.JPanel pnlCenter_Top;
	private javax.swing.JPanel pnlChange;
	private javax.swing.JPanel pnlChonNCC;
	private javax.swing.JPanel pnlChonNCC1;
	private javax.swing.JPanel pnlChonNCC2;
	private javax.swing.JPanel pnlNonInFor;
	private javax.swing.JPanel pnlNonInfo;
	private javax.swing.JPanel pnlRight;
	private javax.swing.JPanel pnlSanPhamDaChon;
	private javax.swing.JPanel pnlSanPhamDaChonNotNull;
	private javax.swing.JPanel pnlSanPhamDaChonNull;
	private javax.swing.JPanel pnlTopLeft;
	private javax.swing.JTable tableChonKH;
	private javax.swing.JTable tableChonKH1;
	private javax.swing.JTable tableChonSP;
	private javax.swing.JTable tableInForSP;
	private javax.swing.JTextField txtMaKhuyenMai;
	private javax.swing.JTextField txtSoLuongSanPhamChon;
	private javax.swing.JTextField txtTienChietKhau;
	private javax.swing.JTextField txtTienKhachDua;
	private javax.swing.JTextField txtTienKhuyenMai;
	public javax.swing.JTextField txtTimKH;
	public javax.swing.JTextField txtTimNCC1;
	public javax.swing.JTextField txtTimSanPhamChon;
	// End of variables declaration//GEN-END:variables

}
