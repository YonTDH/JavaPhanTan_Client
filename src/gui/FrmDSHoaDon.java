/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import dao.DAO_ChiTietHoaDon;
import dao.DAO_HoaDon;
import dao.DAO_KhachHang;
import dao.DAO_KhuyenMai;
import dao.DAO_MauSac;
import dao.DAO_NhaCungCap;
import dao.DAO_NhanVien;
import dao.DAO_NhomSanPham;
import dao.DAO_Sach;
import dao.DAO_SanPham;
import dao.DAO_VanPhongPham;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhaCungCap;
import entity.NhanVien;
import entity.NhomKhachHang;
import entity.NhomSanPham;
import entity.Sach;
import entity.SanPham;
import entity.VanPhongPham;
import lookup.LookupNaming;
import menuGui.TableActionCellEditor;
import menuGui.TableActionCellRender;
import menuGui.TableActionEvent;
import printReport.FieldReport;
import printReport.ParameterReport;
import printReport.ReportManager;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nguyen chau tai
 */
public class FrmDSHoaDon extends javax.swing.JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2503547149428430751L;
	/**
     * Creates new form FrmDSKhachHang
     */
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    DecimalFormat deciFormat = new DecimalFormat("###.###");
    int soLuong = 1;
    int sttSP = 1;
    /**
     * Creates new form FrmDSKhachHang
     */
    private FrmChinh frm = new FrmChinh();
    private DAO_SanPham dao_sp = LookupNaming.lookup_SanPham();
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

    public FrmDSHoaDon() throws RemoteException {
        try {
            ReportManager.getInstance().compileReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        ConnectDB.getInstance().connect();
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

        jComboBox1.setSelectedIndex(0);

        quickPress();
        loadDataHD();
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
        // ----------
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
        InputMap inputMap3 = btnQuayLai.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap3.put(KeyStroke.getKeyStroke("ESCAPE"), "doSomething3");

        Action action3 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnQuayLaiActionPerformed(e);
            }
        };

        btnQuayLai.getActionMap().put("doSomething3", action3);
        //---------------
        InputMap inputMap4 = btnHuyHoaDon.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap4.put(KeyStroke.getKeyStroke("F1"), "doSomething4");

        Action action4 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnHuyHoaDonActionPerformed(e);
            }
        };

        btnHuyHoaDon.getActionMap().put("doSomething4", action4);
        //---------------
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
                public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    String maSp = entry.getValue(1).toString();
                    String tenSp = entry.getValue(2).toString();

                    return pattern.matcher(maSp).lookingAt() || pattern.matcher(tenSp).lookingAt();
                }
            });
        }
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
                double tienKhachTra = tienThanhToan + tienChietKhau - Double.parseDouble(txtTienKhuyenMai.getText());;
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
        String nhom = (String) cboSortTabelChonSP.getSelectedItem();
        List<Sach> dsSach = dao_sach.getAlltbSach();
        List<VanPhongPham> dsVpp = dao_vpp.getAllVanPhongPhan();
        if (nhom.equalsIgnoreCase("Tất cả")) {
            int stt = 1;
            for (Sach s : dsSach) {
                md.addRow(new Object[]{stt, s.getMaSanPham(), s.getTenSanPham(), s.getDonGiaBan(),
                    s.getSoLuongTon(), s.getTinhTrang()

                });
                stt++;
            }
            for (VanPhongPham vpp : dsVpp) {
                md.addRow(new Object[]{stt, vpp.getMaSanPham(), vpp.getTenSanPham(), vpp.getDonGiaBan(),
                    vpp.getSoLuongTon(), vpp.getTinhTrang()

                });
                stt++;
            }
        } else {
            NhomSanPham nsp = dao_nsp.getNsptheoTen(nhom);
            String ma = nsp.getMaNhomSanPham();
            int stt = 1;
            for (Sach s : dsSach) {
                if (s.getNhomSanPham().getMaNhomSanPham().equalsIgnoreCase(ma)) {
                    md.addRow(new Object[]{stt, s.getMaSanPham(), s.getTenSanPham(), s.getDonGiaBan(),
                        s.getSoLuongTon(), s.getTinhTrang()

                    });
                    stt++;
                }

            }
            for (VanPhongPham vpp : dsVpp) {
                if (vpp.getNhomSanPham().getMaNhomSanPham().equalsIgnoreCase(ma)) {
                    md.addRow(new Object[]{stt, vpp.getMaSanPham(), vpp.getTenSanPham(), vpp.getDonGiaBan(),
                        vpp.getSoLuongTon(), vpp.getTinhTrang()

                    });
                    stt++;
                }

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
            txtTienKhuyenMai.setText(tienKhuyenMai + "");
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
                txtTienKhuyenMai.setText(((tongTienThanhToan * 0.05) + tienKhuyenMai) + "");
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
                txtTienKhuyenMai.setText(tienKhuyenMai + "");
                lblTongTienThanhToan.setText(deciFormat.format(tien));
                lblTongSoLuong.setText(tongSoLuongSp + "");
            }
        }

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

    public double tinhThanhTien(int sl, double dg) {
        return sl * dg;
    }

    public void loadChonKH() throws RemoteException {
        List<KhachHang> dsKH = dao_kh.getAllKhachHang();
        DefaultTableModel tableModal = (DefaultTableModel) tableChonKH.getModel();
        tableModal.getDataVector().removeAllElements();
        int stt = 1;
        for (KhachHang kh : dsKH) {
            if (!kh.getTenKhachHang().equals("Khách lẻ")) {
                tableModal.addRow(new Object[]{stt, kh.getMaKhachHang(), kh.getTenKhachHang(), kh.getSoDienThoai()});
            }
        }
    }

    public void loadDataHD() throws RemoteException {
        DefaultTableModel dm = (DefaultTableModel) tableDSHoaDon.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();
        List<HoaDon> dsHD = dao_hd.getAllHoaDon();
        KhachHang kh = null;
        DefaultTableModel tableModal = (DefaultTableModel) tableDSHoaDon.getModel();
        int stt = 1;
        String trangThai = "";
        for (HoaDon hd : dsHD) {
            if (hd.getTinhTrangHoaDon() == 1) {
                trangThai = "Đã thanh toán";
            } else if (hd.getTinhTrangHoaDon() == 0) {
                trangThai = "Đang chờ xử lí";
            } else {
                trangThai = "Hoàn tiền";
            }
            kh = dao_kh.getKHTheoMa(hd.getKhachHang().getMaKhachHang());
            tableModal.addRow(new Object[]{stt, hd.getMaHoaDon(), formatter.format(hd.getNgayLap()), kh.getTenKhachHang(), trangThai, deciFormat.format(hd.getTongTien())});
            stt++;
        }
    }

    public void timJtableDSHD() {
        DefaultTableModel tableModel = (DefaultTableModel) tableDSHoaDon.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tableDSHoaDon.setRowSorter(sorter);

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
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    String maSp = entry.getValue(1).toString();
                    String tenSp = entry.getValue(3).toString();

                    return pattern.matcher(maSp).lookingAt() || pattern.matcher(tenSp).lookingAt();
                }
            });
        }
    }

    public void changePanelChuaThanhToan() throws RemoteException {
        btnHuyHoaDon.setEnabled(true);
        btnThanhToanHD.setEnabled(true);
        btnChonKH.setEnabled(false);
        btnKhachLe.setEnabled(false);
        btnChonKH1.setEnabled(false);
        btnKhachLe1.setEnabled(false);
        tableChonSP.setEnabled(true);
        tableInForSP.setEnabled(true);
        jTextAreaGhiChu.setEnabled(true);
        cboSortTabelChonSP.setEnabled(true);
        txtTimSanPhamChon.setEnabled(true);
        txtTienChietKhau.setEnabled(true);
        txtTienKhuyenMai.setEnabled(true);
        txtTienKhachDua.setEnabled(true);
        btnChonKhuyenMai.setEnabled(true);
        btnXoaKhuyenMai.setEnabled(true);
        //******** load San pham
        int row = tableDSHoaDon.getSelectedRow();
        String maHD = (String) tableDSHoaDon.getValueAt(row, 1);
        List<ChiTietHoaDon> dsCTHD = dao_cthd.getAllChiTietHoaDon();
        List<ChiTietHoaDon> dsCTHD_get = new ArrayList<ChiTietHoaDon>();
        HoaDon hd = dao_hd.getHoaDontheoMa(maHD);

        for (ChiTietHoaDon cthd : dsCTHD) {
            if (maHD.equalsIgnoreCase(cthd.getHoaDon().getMaHoaDon())) {
                dsCTHD_get.add(cthd);
            }
        }

        DefaultTableModel mdSP = (DefaultTableModel) tableInForSP.getModel();
        mdSP.getDataVector().removeAllElements();

        int stt = 1;
        for (ChiTietHoaDon cthd : dsCTHD_get) {
            if (cthd.getSanPham().getMaSanPham().startsWith("S")) {
                Sach s = dao_sach.getSachtheoMa(cthd.getSanPham().getMaSanPham());
                mdSP.addRow(new Object[]{stt, cthd.getSanPham().getMaSanPham(), s.getTenSanPham(), s.getDonGiaBan(), cthd.getSoLuong(), cthd.getThanhTien()});
            } else {
                VanPhongPham vpp = dao_vpp.getVPPtheoMa(cthd.getSanPham().getMaSanPham());
                mdSP.addRow(new Object[]{stt, cthd.getSanPham().getMaSanPham(), vpp.getTenSanPham(), vpp.getDonGiaBan(), cthd.getSoLuong(), cthd.getThanhTien()});
            }
            stt++;
        }
//            //******** load Thong tin hoa don
        lblMaHoaDon.setText(maHD);
        jTextAreaGhiChu.setText(hd.getGhiChu());
//            dateNgayLap.setText(hd.getNgayLap().format(formatter));
        txtTienKhachDua.setText("0.0");

        DefaultTableModel md = (DefaultTableModel) tableInForSP.getModel();
        double tongTienThanhToan = 0;
        int tongSoLuongSp = 0;
        for (int i = 0; i < md.getRowCount(); i++) {
            tongTienThanhToan += (double) md.getValueAt(i, 5);
            tongSoLuongSp += (int) md.getValueAt(i, 4);
        }

        lblTongTienThanhToan.setText(hd.getTongTien() + "");
        lblTongTienDefault.setText(tongTienThanhToan + "");
        lblTongSoLuong.setText(tongSoLuongSp + "");
        double tienKhach = Double.parseDouble(txtTienKhachDua.getText());
        double tienThua = tienKhach - tongTienThanhToan;
        if (tienThua < 0) {
            lblTienThua.setText("0.0");
        } else {
            lblTienThua.setText(tienThua + "");
        }
        txtTienChietKhau.setText(deciFormat.format(hd.getChietKhau()));
        txtMaKhuyenMai.setText(hd.getKhuyenMai());
//            //******* load Khach hang
        KhachHang kh = dao_kh.getKHTheoMa(hd.getKhachHang().getMaKhachHang());
        lblTenKH.setText(kh.getTenKhachHang());
        lblSDTKH.setText(kh.getSoDienThoai());
        lblTongDonMua.setText(kh.getSoLuongHoaDon() + "");
//            lblTongDonTra.setText("0");
        lblTongTienMua.setText(kh.getTongTienMua() + "");
        lblMaKH.setText(kh.getMaKhachHang());
        lblNhomKhachHang.setText(kh.getNhomKhachHang() + "");
        createInit();
        if (lblTenKH.getText().equals("Khách lẻ")) {
            showPanelChange(pnlBotLeft, pnlBotLeftReturn);
        } else {
            showPanelChange(pnlBotLeft, pnlBotLeftChange);
        }
        showPanelChange(pnlSanPhamDaChon, pnlSanPhamDaChonNotNull);
//        showPanelChange(pnlBotLeft, pnlBotLeftChange);
        //*******
        showPanelChange(pnlChange, pnlCenterChange);

    }

    public void changePanelDaThanhToan() throws RemoteException {
        btnHuyHoaDon.setEnabled(false);
        btnThanhToanHD.setEnabled(false);
        btnChonKH.setEnabled(false);
        btnKhachLe.setEnabled(false);
        btnChonKH1.setEnabled(false);
        btnKhachLe1.setEnabled(false);
        tableChonSP.setEnabled(false);
        tableInForSP.setEnabled(false);
        jTextAreaGhiChu.setEnabled(false);
        cboSortTabelChonSP.setEnabled(false);
        txtTimSanPhamChon.setEnabled(false);
        txtTienChietKhau.setEnabled(false);
        txtTienKhuyenMai.setEnabled(false);
        txtTienKhachDua.setEnabled(false);
        btnChonKhuyenMai.setEnabled(false);
        btnXoaKhuyenMai.setEnabled(false);
        //******** load San pham
        int row = tableDSHoaDon.getSelectedRow();
        String maHD = (String) tableDSHoaDon.getValueAt(row, 1);
        List<ChiTietHoaDon> dsCTHD = dao_cthd.getAllChiTietHoaDon();
        List<ChiTietHoaDon> dsCTHD_get = new ArrayList<ChiTietHoaDon>();
        HoaDon hd = dao_hd.getHoaDontheoMa(maHD);

        for (ChiTietHoaDon cthd : dsCTHD) {
            if (maHD.equalsIgnoreCase(cthd.getHoaDon().getMaHoaDon())) {
                dsCTHD_get.add(cthd);
            }
        }

        DefaultTableModel mdSP = (DefaultTableModel) tableInForSP.getModel();
        mdSP.getDataVector().removeAllElements();

        int stt = 1;
        for (ChiTietHoaDon cthd : dsCTHD_get) {
            if (cthd.getSanPham().getMaSanPham().startsWith("S")) {
                Sach s = dao_sach.getSachtheoMa(cthd.getSanPham().getMaSanPham());
                mdSP.addRow(new Object[]{stt, cthd.getSanPham().getMaSanPham(), s.getTenSanPham(), s.getDonGiaBan(), cthd.getSoLuong(), cthd.getThanhTien()});
            } else {
                VanPhongPham vpp = dao_vpp.getVPPtheoMa(cthd.getSanPham().getMaSanPham());
                mdSP.addRow(new Object[]{stt, cthd.getSanPham().getMaSanPham(), vpp.getTenSanPham(), vpp.getDonGiaBan(), cthd.getSoLuong(), cthd.getThanhTien()});
            }
            stt++;
        }
//            //******** load Thong tin hoa don
        lblMaHoaDon.setText(maHD);
        jTextAreaGhiChu.setText(hd.getGhiChu());
//            dateNgayLap.setText(hd.getNgayLap().format(formatter));
        txtTienKhachDua.setText("0");

        DefaultTableModel md = (DefaultTableModel) tableInForSP.getModel();
        double tongTienThanhToan = 0;
        int tongSoLuongSp = 0;
        for (int i = 0; i < md.getRowCount(); i++) {
            tongTienThanhToan += (double) md.getValueAt(i, 5);
            tongSoLuongSp += (int) md.getValueAt(i, 4);
        }

        lblTongTienThanhToan.setText(hd.getTongTien() + "");
        lblTongTienDefault.setText(tongTienThanhToan + "");
        lblTongSoLuong.setText(tongSoLuongSp + "");
        double tienKhach = Double.parseDouble(txtTienKhachDua.getText());
        double tienThua = tienKhach - tongTienThanhToan;
        if (tienThua < 0) {
            lblTienThua.setText("0");
        } else {
            lblTienThua.setText(tienThua + "");
        }
        txtTienChietKhau.setText(deciFormat.format(hd.getChietKhau()));
        txtMaKhuyenMai.setText(hd.getKhuyenMai());
//            //******* load Khach hang
        KhachHang kh = dao_kh.getKHTheoMa(hd.getKhachHang().getMaKhachHang());
        lblTenKH.setText(kh.getTenKhachHang());
        lblSDTKH.setText(kh.getSoDienThoai());
        lblTongDonMua.setText(kh.getSoLuongHoaDon() + "");
//            lblTongDonTra.setText("0");
        lblTongTienMua.setText(kh.getTongTienMua() + "");
        lblMaKH.setText(kh.getMaKhachHang());
        lblNhomKhachHang.setText(kh.getNhomKhachHang() + "");
        createInit();
        if (lblTenKH.getText().equals("Khách lẻ")) {
            showPanelChange(pnlBotLeft, pnlBotLeftReturn);
        } else {
            showPanelChange(pnlBotLeft, pnlBotLeftChange);
        }
        showPanelChange(pnlSanPhamDaChon, pnlSanPhamDaChonNotNull);
//        showPanelChange(pnlBotLeft, pnlBotLeftChange);
        //*******
        showPanelChange(pnlChange, pnlCenterChange);

    }

    public synchronized void thanhToanHD() throws RemoteException {
        if (true) {
            if (tableInForSP.getRowCount() != 0) {
                LocalDateTime ngayLap = LocalDateTime.now();
                KhachHang kh = dao_kh.getKHTheoMa(lblMaKH.getText());
                NhanVien nv = dao_nv.getNVTheoMa("QL23102023-000007");
//                HoaDon hd = new HoaDon(lblMaHoaDon.getText(), ngayLap, nv, kh, jTextAreaGhiChu.getText(), 1, Double.parseDouble(lblTongTienThanhToan.getText()), Double.parseDouble(txtTienChietKhau.getText()), Double.parseDouble(txtTienKhuyenMai.getText()));
                HoaDon hd = dao_hd.getHoaDontheoMa(lblMaHoaDon.getText());
                hd.setNgayLap(ngayLap);
                hd.setNhanVien(nv);
                hd.setKhachHang(kh);
                hd.setGhiChu(jTextAreaGhiChu.getText());
                hd.setTinhTrangHoaDon(1);
                hd.setTongTien(Double.parseDouble(lblTongTienThanhToan.getText()));
                hd.setChietKhau(Double.parseDouble(txtTienChietKhau.getText()));
                hd.setKhuyenMai(txtMaKhuyenMai.getText());
                dao_hd.updateHoaDon(hd);
                List<ChiTietHoaDon> list = dao_cthd.getAllChiTietHoaDon();
                for (ChiTietHoaDon cthd : list) {
                    if (cthd.getHoaDon().getMaHoaDon().equals(hd.getMaHoaDon())) {
                        dao_cthd.deleteChiTietHoaDon(hd.getMaHoaDon());
                    }
                }
                DefaultTableModel md = (DefaultTableModel) tableInForSP.getModel();
                String ma = "";
                for (int i = 0; i < tableInForSP.getRowCount(); i++) {
                    ma = (String) tableInForSP.getValueAt(i, 1);
                    if (ma.startsWith("S")) {
                        Sach s = dao_sach.getSachtheoMa(ma);
                        ChiTietHoaDon cthd = new ChiTietHoaDon(hd, s, (int) tableInForSP.getValueAt(i, 4), (double) tableInForSP.getValueAt(i, 5));
                        dao_cthd.createChiTietHoaDon(cthd);
                        s.setSoLuongTon(s.getSoLuongTon() - (int) tableInForSP.getValueAt(i, 4));
                        if (s.getSoLuongTon() == 0) {
                            s.setTinhTrang("Hết hàng");
                        }
                        dao_sach.updateSach(s);
                    } else {
                        VanPhongPham vpp = dao_vpp.getVPPtheoMa(ma);
                        ChiTietHoaDon cthd = new ChiTietHoaDon(hd, vpp, (int) tableInForSP.getValueAt(i, 4), (double) tableInForSP.getValueAt(i, 5));
                        dao_cthd.createChiTietHoaDon(cthd);
                        vpp.setSoLuongTon(vpp.getSoLuongTon() - (int) tableInForSP.getValueAt(i, 4));
                        if (vpp.getSoLuongTon() == 0) {
                            vpp.setTinhTrang("Hết hàng");
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

        showPanelChange(pnlChange, pnlCenter);
        loadDataHD();
    }

    public void huyDonHang() throws RemoteException {
    	int row = JOptionPane.showConfirmDialog(null, "Đơn hàng sẽ bị mất đi sau khi hủy, bạn có chắc chắn hủy không?", "Hủy đơn hàng", JOptionPane.YES_NO_OPTION);
    	if (row == JOptionPane.YES_OPTION) {
    		DefaultTableModel md = (DefaultTableModel) tableInForSP.getModel();
    		md.getDataVector().removeAllElements(); 
//    		List<ChiTietHoaDon> list = dao_cthd.getAllChiTietHoaDon();
//    		for (ChiTietHoaDon cthd : list) {
//  
////    				if (cthd.getHoaDon().getMaHoaDon().equals(lblMaHoaDon.getText())) {
////    					dao_cthd.deleteChiTietHoaDonVaSanPham(lblMaHoaDon.getText(), cthd.getSanPham().getMaSanPham());
////    				}
//    			
//    			if (cthd.getHoaDon().getMaHoaDon().equals(lblMaHoaDon.getText())) {
//    				dao_cthd.deleteChiTietHoaDon(lblMaHoaDon.getText());
//    			}
//
//    		}
    		dao_hd.deleteHoaDon(lblMaHoaDon.getText());
    		loadDataHD();
    		showPanelChange(pnlChange, pnlCenter);
    	} else {

    	}
    }

    public void showPanelChange(JPanel a, JPanel b) {
        a.removeAll();
        a.add(b);
        a.repaint();
        a.revalidate();
    }

    public void tableAction() {
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

    public void lamMoiHoaDon() {
        DefaultTableModel md = (DefaultTableModel) tableInForSP.getModel();
        md.getDataVector().removeAllElements();
        lblTenKH.setText("");
        lblMaKH.setText("");
        jTextAreaGhiChu.setText("");
        txtTienKhachDua.setText("0");
    }
    
  //Export Danh sách Hóa đơn - Gui>FrmDSHoaDon
    public void exportDanhSachHoaDon() {
        Runnable exportTask = new Runnable() {
            @Override
            public void run() {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("DanhSachHoaDon");

                // Tạo hàng đầu tiên (Header)
                Row headerRow = sheet.createRow(0);
                String[] headers = {"STT", "Mã Hóa Đơn", "Ngày Tạo Đơn", "Tên Khách Hàng", "Trạng Thái", "Tổng Tiền"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }

                // Lấy dữ liệu từ JTable (hoặc từ đối tượng khác) và điền vào sheet
                DefaultTableModel model = (DefaultTableModel) tableDSHoaDon.getModel();
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

//   Export Danh sách Hóa đơn
//    public void exportDanhSachHoaDon() {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("DanhSachHoaDon");
//
//        // Tạo hàng đầu tiên (Header)
//        Row headerRow = sheet.createRow(0);
//        String[] headers = {"STT", "Mã Hóa Đơn", "Ngày Tạo Đơn", "Tên Khách Hàng", "Trạng Thái", "Tổng Tiền"};
//        for (int i = 0; i < headers.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(headers[i]);
//        }
//
//        // Lấy dữ liệu từ JTable (hoặc từ đối tượng khác) và điền vào sheet
//        DefaultTableModel model = (DefaultTableModel) tableDSHoaDon.getModel();
//        int rowCount = model.getRowCount();
//        for (int i = 0; i < rowCount; i++) {
//            Row row = sheet.createRow(i + 1); // Bắt đầu từ dòng thứ 2
//
//            for (int j = 0; j < model.getColumnCount(); j++) {
//                Cell cell = row.createCell(j);
//                cell.setCellValue(String.valueOf(model.getValueAt(i, j)));
//            }
//        }
//
//        try {
//            JFileChooser fileChooser = new JFileChooser("excel");
//            fileChooser.setDialogTitle("Chọn nơi lưu và đặt tên file");
//            FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xlsx");
//            fileChooser.setFileFilter(filter);
//
//            int userSelection = fileChooser.showSaveDialog(null);
//
//            if (userSelection == JFileChooser.APPROVE_OPTION) {
//                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
//
//                if (!filePath.toLowerCase().endsWith(".xlsx")) {
//                    filePath += ".xlsx";  // Đảm bảo có đuôi .xlsx
//                }
//
//                FileOutputStream fileOut = new FileOutputStream(filePath);
//                workbook.write(fileOut);
//                fileOut.close();
//
//                JOptionPane.showMessageDialog(null, "Xuất file Excel thành công: " + filePath,
//                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Lỗi khi xuất file Excel",
//                    "Lỗi", JOptionPane.ERROR_MESSAGE);
//        }
//    }

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
                md.addRow(new Object[]{km.getMaKhuyenMai(), km.getTenKhuyenMai(), km.getGhiChu()});
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

        pnlTopLeftChange = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        menuScrollPane8 = new menuGui.MenuScrollPane();
        table = new javax.swing.JTable();
        pnlCenterChange = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        pnlRight = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        txtTienKhachDua = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        menuScrollPane6 = new menuGui.MenuScrollPane();
        jTextAreaGhiChu = new javax.swing.JTextArea();
        jLabel111 = new javax.swing.JLabel();
        lblMaHoaDon = new javax.swing.JLabel();
        btnThanhToanHD = new javax.swing.JButton();
        btnInHoaDon = new javax.swing.JButton();
        btnQuayLai = new javax.swing.JButton();
        jLabel52 = new javax.swing.JLabel();
        lblTongTienDefault = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        lblTongTienThanhToan = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        lblTienThua = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        btnHuyHoaDon = new javax.swing.JButton();
        txtTienKhuyenMai = new javax.swing.JTextField();
        pnlSanPhamDaChon = new javax.swing.JPanel();
        pnlSanPhamDaChonNull = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        txtTienChietKhau = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        lblTongSoLuong = new javax.swing.JLabel();
        btnChonKhuyenMai = new javax.swing.JButton();
        txtMaKhuyenMai = new javax.swing.JTextField();
        btnXoaKhuyenMai = new javax.swing.JButton();
        pnlBotLeft = new javax.swing.JPanel();
        pnlBotLeftReturn = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btnKhachLe = new javax.swing.JButton();
        btnChonKH = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        pnlTopLeft2 = new javax.swing.JPanel();
        pnlNonInFor = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        pnlNonInfo = new javax.swing.JPanel();
        pnlChonNCC = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jLabel148 = new javax.swing.JLabel();
        txtTimSanPhamChon = new javax.swing.JTextField();
        cboSortTabelChonSP = new javax.swing.JComboBox<>();
        menuScrollPane11 = new menuGui.MenuScrollPane();
        tableChonSP = new javax.swing.JTable();
        pnlTableInit1 = new javax.swing.JPanel();
        menuScrollPane3 = new menuGui.MenuScrollPane();
        jTable3 = new javax.swing.JTable();
        pnlBotLeftChange = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        btnChonKH1 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        btnKhachLe1 = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        lblTongTienMua = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblSDTKH = new javax.swing.JLabel();
        lblTongDonMua = new javax.swing.JLabel();
        lblTenKH = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        lblMaKH = new javax.swing.JLabel();
        lblNhomKhachHang = new javax.swing.JLabel();
        lblMaKH2 = new javax.swing.JLabel();
        jDialogChonKH = new javax.swing.JDialog(frm,"Chọn",true);
        pnlChonNCC1 = new javax.swing.JPanel();
        menuScrollPane10 = new menuGui.MenuScrollPane();
        tableChonKH = new javax.swing.JTable();
        jPanel50 = new javax.swing.JPanel();
        jLabel149 = new javax.swing.JLabel();
        txtTimNCC1 = new javax.swing.JTextField();
        jDialogGhiSoLuong = new javax.swing.JDialog(frm,"Chọn",false);
        jPanel6 = new javax.swing.JPanel();
        jLabel113 = new javax.swing.JLabel();
        txtSoLuongSanPhamChon = new javax.swing.JTextField();
        btnXacNhanSoLuong = new javax.swing.JButton();
        lblTenSanPhamChon = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        pnlBotLeftNonKH = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        pnlSanPhamDaChonNotNull = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        menuScrollPane9 = new menuGui.MenuScrollPane();
        tableInForSP = new javax.swing.JTable();
        jOptionPane1 = new javax.swing.JOptionPane();
        jDialogChonKhuyenMai = new javax.swing.JDialog(frm,"Chọn",false);
        pnlChonNCC2 = new javax.swing.JPanel();
        menuScrollPane12 = new menuGui.MenuScrollPane();
        tableChonKH1 = new javax.swing.JTable();
        jPanel52 = new javax.swing.JPanel();
        jLabel150 = new javax.swing.JLabel();
        txtTimNCC2 = new javax.swing.JTextField();
        pnlChange = new javax.swing.JPanel();
        pnlCenter = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        pnlTopLeft = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnTatCa = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtTimKH = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        pnlCenterKHchange = new javax.swing.JPanel();
        pnlTableInit = new javax.swing.JPanel();
        menuScrollPane2 = new menuGui.MenuScrollPane();
        tableDSHoaDon = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        lblNameLogin = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        pnlTopLeftChange.setBackground(new java.awt.Color(204, 204, 255));
        pnlTopLeftChange.setLayout(new java.awt.BorderLayout());

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel14.setText("Thông tin sản phẩm");

        jButton6.setBackground(new java.awt.Color(3, 136, 253));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Chọn sản phẩm");
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setMargin(new java.awt.Insets(2, 10, 3, 10));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuScrollPane8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menuScrollPane8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        table.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setFocusable(false);
        table.setGridColor(new java.awt.Color(255, 255, 255));
        table.setRowHeight(40);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        menuScrollPane8.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setMaxWidth(40);
            table.getColumnModel().getColumn(1).setMinWidth(100);
            table.getColumnModel().getColumn(1).setMaxWidth(100);
            table.getColumnModel().getColumn(3).setMinWidth(80);
            table.getColumnModel().getColumn(3).setMaxWidth(80);
            table.getColumnModel().getColumn(4).setMinWidth(80);
            table.getColumnModel().getColumn(4).setMaxWidth(80);
            table.getColumnModel().getColumn(5).setMinWidth(120);
            table.getColumnModel().getColumn(5).setMaxWidth(120);
        }

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 771, Short.MAX_VALUE)
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(menuScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 432, Short.MAX_VALUE)
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(menuScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlTopLeftChange.add(jPanel19, java.awt.BorderLayout.CENTER);

        pnlCenterChange.setBackground(new java.awt.Color(153, 153, 153));
        pnlCenterChange.setLayout(new java.awt.BorderLayout());

        jPanel30.setBackground(new java.awt.Color(250, 250, 250));
        jPanel30.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel30.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel64.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel64.setText("Danh sách hóa đơn");

        jLabel65.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel65.setText("Nguyễn Châu Tình - DESGIN");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel16.setText("> Thanh toán");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel64)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1018, Short.MAX_VALUE)
                .addComponent(jLabel65)
                .addGap(19, 19, 19))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlCenterChange.add(jPanel30, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));

        jPanel9.setBackground(new java.awt.Color(240, 242, 245));

        pnlRight.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel12.setText("Thông tin hóa đơn");

        jPanel5.setBackground(new java.awt.Color(250, 250, 250));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel29.setText("Tiền khách đưa:");

        txtTienKhachDua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTienKhachDua.setText("0.0");
        txtTienKhachDua.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTienKhachDuaFocusLost(evt);
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
        menuScrollPane6.setViewportView(jTextAreaGhiChu);

        jLabel111.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel111.setText("Mã hóa đơn: ");

        lblMaHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblMaHoaDon.setText("6546451646");

        btnThanhToanHD.setBackground(new java.awt.Color(3, 136, 253));
        btnThanhToanHD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThanhToanHD.setForeground(new java.awt.Color(255, 255, 255));
        btnThanhToanHD.setText("Thanh toán(F8)");
        btnThanhToanHD.setFocusable(false);
        btnThanhToanHD.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThanhToanHD.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnThanhToanHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanHDActionPerformed(evt);
            }
        });

        btnInHoaDon.setBackground(new java.awt.Color(3, 136, 253));
        btnInHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnInHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        btnInHoaDon.setText("In hóa đơn(F9)");
        btnInHoaDon.setFocusable(false);
        btnInHoaDon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnInHoaDon.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnInHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInHoaDonActionPerformed(evt);
            }
        });

        btnQuayLai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnQuayLai.setForeground(new java.awt.Color(3, 136, 253));
        btnQuayLai.setText("Quay lại(ESC)");
        btnQuayLai.setFocusable(false);
        btnQuayLai.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnQuayLai.setMargin(new java.awt.Insets(2, 5, 3, 5));
        btnQuayLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuayLaiActionPerformed(evt);
            }
        });

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel52.setText("Tổng số lượng sản phẩm:");

        lblTongTienDefault.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTongTienDefault.setText("0.0");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel38.setText("Tổng tiền cần thanh toán:");

        lblTongTienThanhToan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTongTienThanhToan.setText("0.0");

        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel42.setText("Tiền thừa:");

        lblTienThua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTienThua.setText("0.0");

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel59.setText("Khuyến mãi:");

        btnHuyHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHuyHoaDon.setForeground(new java.awt.Color(255, 102, 102));
        btnHuyHoaDon.setText("Hủy đơn hàng(F1)");
        btnHuyHoaDon.setFocusable(false);
        btnHuyHoaDon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHuyHoaDon.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnHuyHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyHoaDonActionPerformed(evt);
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
                .addContainerGap(91, Short.MAX_VALUE)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72))
        );

        pnlSanPhamDaChon.add(pnlSanPhamDaChonNull, java.awt.BorderLayout.CENTER);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel1.setText("Sản phẩm đã chọn");

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel60.setText("Chiết khấu:");

        txtTienChietKhau.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTienChietKhau.setText("0.0");
        txtTienChietKhau.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTienChietKhauFocusLost(evt);
            }
        });
        txtTienChietKhau.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTienChietKhauKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTienChietKhauKeyReleased(evt);
            }
        });

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel53.setText("Tổng tiền");

        lblTongSoLuong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTongSoLuong.setText("0");

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
                            .addComponent(btnQuayLai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnHuyHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnInHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnThanhToanHD, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTienKhachDua)
                            .addComponent(lblTongTienThanhToan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTienThua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addComponent(jLabel1))
                        .addGap(45, 45, 45)
                        .addComponent(menuScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel60)
                        .addGap(139, 139, 139)
                        .addComponent(txtTienChietKhau))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(lblTongSoLuong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblTongTienDefault, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel59)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTienKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMaKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnChonKhuyenMai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXoaKhuyenMai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                    .addComponent(menuScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlSanPhamDaChon, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTienKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtMaKhuyenMai, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnChonKhuyenMai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTongTienDefault, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel52)
                            .addComponent(lblTongSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnXoaKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(35, 35, 35)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTienChietKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTienThua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTongTienThanhToan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnQuayLai, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThanhToanHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHuyHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlRightLayout = new javax.swing.GroupLayout(pnlRight);
        pnlRight.setLayout(pnlRightLayout);
        pnlRightLayout.setHorizontalGroup(
            pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlRightLayout.setVerticalGroup(
            pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRightLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        btnChonKH.setBackground(new java.awt.Color(3, 136, 253));
        btnChonKH.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnChonKH.setForeground(new java.awt.Color(255, 255, 255));
        btnChonKH.setText("Chọn khách hàng");
        btnChonKH.setFocusable(false);
        btnChonKH.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChonKH.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnChonKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonKHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 241, Short.MAX_VALUE)
                .addComponent(btnKhachLe, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnChonKH, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKhachLe)
                    .addComponent(btnChonKH))
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

        pnlTopLeft2.setBackground(new java.awt.Color(204, 204, 255));
        pnlTopLeft2.setPreferredSize(new java.awt.Dimension(786, 496));
        pnlTopLeft2.setLayout(new java.awt.BorderLayout());

        pnlNonInFor.setBackground(new java.awt.Color(255, 255, 255));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel8.setText("Thông tin sản phẩm");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng", "Tình trạng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableChonSP.setGridColor(new java.awt.Color(255, 255, 255));
        tableChonSP.setRowHeight(60);
        tableChonSP.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableChonSP.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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
            tableChonSP.getColumnModel().getColumn(3).setMinWidth(80);
            tableChonSP.getColumnModel().getColumn(3).setMaxWidth(80);
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
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlNonInForLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlNonInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlNonInForLayout.setVerticalGroup(
            pnlNonInForLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNonInForLayout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlNonInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlTopLeft2.add(pnlNonInFor, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(397, Short.MAX_VALUE)
                .addComponent(pnlBotLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                    .addContainerGap(15, Short.MAX_VALUE)
                    .addComponent(pnlTopLeft2, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(539, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlBotLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                    .addContainerGap(290, Short.MAX_VALUE)
                    .addComponent(pnlTopLeft2, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(38, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        pnlCenterChange.add(jPanel4, java.awt.BorderLayout.CENTER);

        pnlTableInit1.setBackground(new java.awt.Color(250, 250, 250));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
                "STT", "Mã hóa đơn", "Ngày tạo đơn", "Tên khách hàng", "Trạng thái đơn hàng", "Tổng tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
                .addComponent(menuScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pnlBotLeftChange.setBackground(new java.awt.Color(255, 255, 255));
        pnlBotLeftChange.setLayout(new java.awt.BorderLayout());

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setPreferredSize(new java.awt.Dimension(756, 268));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        btnChonKH1.setBackground(new java.awt.Color(3, 136, 253));
        btnChonKH1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnChonKH1.setForeground(new java.awt.Color(255, 255, 255));
        btnChonKH1.setText("Chọn khách hàng");
        btnChonKH1.setFocusable(false);
        btnChonKH1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChonKH1.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnChonKH1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonKH1ActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel13.setText("Thông tin khách hàng");

        btnKhachLe1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnKhachLe1.setForeground(new java.awt.Color(3, 136, 253));
        btnKhachLe1.setText("Khách lẻ");
        btnKhachLe1.setFocusable(false);
        btnKhachLe1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKhachLe1.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnKhachLe1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhachLe1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                .addComponent(btnKhachLe1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnChonKH1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnChonKH1)
                        .addComponent(btnKhachLe1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.setBackground(new java.awt.Color(250, 250, 250));
        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblTongTienMua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTongTienMua.setText("500000000");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel17.setText("Số điện thoại:                 ");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel19.setText("Tổng số đơn hàng:        ");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel21.setText("Mã khách hàng:");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel18.setText("Họ và tên: ");

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

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblMaKH2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSDTKH, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTongDonMua, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTongTienMua, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNhomKhachHang))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(lblTenKH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblSDTKH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(lblTongDonMua))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(lblTongTienMua))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(lblMaKH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaKH2)
                    .addComponent(lblNhomKhachHang))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlBotLeftChange.add(jPanel15, java.awt.BorderLayout.CENTER);

        jDialogChonKH.setBackground(new java.awt.Color(250, 250, 250));
        jDialogChonKH.setResizable(false);
        jDialogChonKH.setSize(new java.awt.Dimension(786, 437));

        pnlChonNCC1.setBackground(new java.awt.Color(250, 250, 250));
        pnlChonNCC1.setLayout(new java.awt.BorderLayout());

        menuScrollPane10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menuScrollPane10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tableChonKH.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
        tableChonKH.setRowHeight(40);
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
                    .addComponent(txtTimNCC1, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
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
                    .addComponent(txtTimNCC1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pnlChonNCC1.add(jPanel50, java.awt.BorderLayout.PAGE_START);

        jDialogChonKH.getContentPane().add(pnlChonNCC1, java.awt.BorderLayout.CENTER);

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

        jPanel6.setBackground(new java.awt.Color(250, 250, 250));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setPreferredSize(new java.awt.Dimension(440, 200));

        jLabel113.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel113.setText("Số lượng:");

        txtSoLuongSanPhamChon.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSoLuongSanPhamChon.setText("1");
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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTenSanPhamChon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel113)
                        .addGap(46, 46, 46)
                        .addComponent(txtSoLuongSanPhamChon, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(240, 240, 240))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnXacNhanSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblTenSanPhamChon, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel113, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoLuongSanPhamChon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                .addComponent(btnXacNhanSoLuong)
                .addGap(26, 26, 26))
        );

        jDialogGhiSoLuong.getContentPane().add(jPanel6, java.awt.BorderLayout.CENTER);

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

        jPanel18.setBackground(new java.awt.Color(250, 250, 250));
        jPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/id-card.png"))); // NOI18N

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel26.setText("Chưa có thông tin khách hàng");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(352, 352, 352)
                .addComponent(jLabel25)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel26)
                .addGap(220, 220, 220))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
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
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addComponent(jPanel51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlBotLeftNonKH.add(jPanel48, java.awt.BorderLayout.CENTER);

        pnlSanPhamDaChonNotNull.setBackground(new java.awt.Color(204, 204, 255));
        pnlSanPhamDaChonNotNull.setPreferredSize(new java.awt.Dimension(475, 297));
        pnlSanPhamDaChonNotNull.setLayout(new java.awt.BorderLayout());

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setLayout(new java.awt.BorderLayout());

        menuScrollPane9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menuScrollPane9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tableInForSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableInForSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class
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
        tableInForSP.setRowHeight(40);
        tableInForSP.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableInForSP.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableInForSP.getTableHeader().setReorderingAllowed(false);
        tableInForSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableInForSPMouseClicked(evt);
            }
        });
        menuScrollPane9.setViewportView(tableInForSP);
        if (tableInForSP.getColumnModel().getColumnCount() > 0) {
            tableInForSP.getColumnModel().getColumn(0).setMinWidth(0);
            tableInForSP.getColumnModel().getColumn(0).setMaxWidth(0);
            tableInForSP.getColumnModel().getColumn(1).setMinWidth(0);
            tableInForSP.getColumnModel().getColumn(1).setMaxWidth(0);
            tableInForSP.getColumnModel().getColumn(3).setMinWidth(0);
            tableInForSP.getColumnModel().getColumn(3).setPreferredWidth(0);
            tableInForSP.getColumnModel().getColumn(3).setMaxWidth(0);
            tableInForSP.getColumnModel().getColumn(4).setMinWidth(80);
            tableInForSP.getColumnModel().getColumn(4).setMaxWidth(80);
            tableInForSP.getColumnModel().getColumn(5).setMinWidth(80);
            tableInForSP.getColumnModel().getColumn(5).setMaxWidth(80);
            tableInForSP.getColumnModel().getColumn(6).setMinWidth(80);
            tableInForSP.getColumnModel().getColumn(6).setMaxWidth(80);
        }

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 475, Short.MAX_VALUE)
            .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(menuScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 297, Short.MAX_VALUE)
            .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(menuScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
        );

        jPanel22.add(jPanel23, java.awt.BorderLayout.CENTER);

        pnlSanPhamDaChonNotNull.add(jPanel22, java.awt.BorderLayout.CENTER);

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
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã khuyến mãi", "Tên khuyến mãi", "Ghi chú"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
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

        txtTimNCC2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimNCC2.setText("Nhập vào thông tin tìm kiếm...");
        txtTimNCC2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimNCC2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimNCC2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTimNCC2FocusLost(evt);
            }
        });
        txtTimNCC2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimNCC2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
        jPanel52.setLayout(jPanel52Layout);
        jPanel52Layout.setHorizontalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel150, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(847, Short.MAX_VALUE))
            .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel52Layout.createSequentialGroup()
                    .addGap(87, 87, 87)
                    .addComponent(txtTimNCC2, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
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
                    .addComponent(txtTimNCC2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pnlChonNCC2.add(jPanel52, java.awt.BorderLayout.PAGE_START);

        jDialogChonKhuyenMai.getContentPane().add(pnlChonNCC2, java.awt.BorderLayout.CENTER);

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
        jLabel5.setText("Thông tin hóa đơn");

        btnTatCa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTatCa.setForeground(new java.awt.Color(3, 136, 253));
        btnTatCa.setText("Xem");
        btnTatCa.setFocusable(false);
        btnTatCa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTatCa.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnTatCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnTatCaActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

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
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang chờ xử lí", "Đã thanh toán", "Tất cả" }));
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
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimKH, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btnTatCa, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTatCa, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTimKH, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pnlCenterKHchange.setBackground(new java.awt.Color(250, 250, 250));
        pnlCenterKHchange.setLayout(new java.awt.BorderLayout());

        pnlTableInit.setBackground(new java.awt.Color(250, 250, 250));

        tableDSHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tableDSHoaDon.setModel(new javax.swing.table.DefaultTableModel(
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
                "STT", "Mã hóa đơn", "Ngày tạo đơn", "Tên khách hàng", "Trạng thái đơn hàng", "Tổng tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableDSHoaDon.setRowHeight(60);
        tableDSHoaDon.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableDSHoaDon.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableDSHoaDon.setShowGrid(false);
        tableDSHoaDon.setShowHorizontalLines(true);
        tableDSHoaDon.getTableHeader().setReorderingAllowed(false);
        tableDSHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDSHoaDonMouseClicked(evt);
            }
        });
        menuScrollPane2.setViewportView(tableDSHoaDon);
        if (tableDSHoaDon.getColumnModel().getColumnCount() > 0) {
            tableDSHoaDon.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        javax.swing.GroupLayout pnlTableInitLayout = new javax.swing.GroupLayout(pnlTableInit);
        pnlTableInit.setLayout(pnlTableInitLayout);
        pnlTableInitLayout.setHorizontalGroup(
            pnlTableInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableInitLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(menuScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        pnlTableInitLayout.setVerticalGroup(
            pnlTableInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableInitLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(menuScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pnlCenterKHchange.add(pnlTableInit, java.awt.BorderLayout.CENTER);

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
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCenterKHchange, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        pnlTopLeft.add(jPanel12, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(98, 47));

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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTopLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1269, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlTopLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 729, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlCenter.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(250, 250, 250));
        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setText("Danh sách hóa đơn");

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

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/icons8-user-50.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1206, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lblNameLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
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
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCenter.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        pnlChange.add(pnlCenter, java.awt.BorderLayout.CENTER);

        add(pnlChange, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTatCaActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btnTatCaActionPerformed
        // TODO add your handling code here:
        if (tableDSHoaDon.getSelectedRow() == -1) {

        } else {
            loadChonSP();
            int row = tableDSHoaDon.getSelectedRow();
            String trangThai = (String) tableDSHoaDon.getValueAt(row, 4);
            if (trangThai.equalsIgnoreCase("Đã thanh toán") || trangThai.equalsIgnoreCase("Hoàn tiền")) {
                changePanelDaThanhToan();
            } else {
                changePanelChuaThanhToan();
            }
        }

//        showPanelChange(pnlCenterKHchange, pnlTableInit1);
    }//GEN-LAST:event_btnTatCaActionPerformed

    private void txtTimKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKHActionPerformed
        // TODO add your handling code here:
        txtTimKH.selectAll();
    }//GEN-LAST:event_txtTimKHActionPerformed

    private void txtTimKHFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKHFocusGained
        // TODO add your handling code here:
        frm.placeHoderTextGianed(txtTimKH);
    }//GEN-LAST:event_txtTimKHFocusGained

    private void txtTimKHFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKHFocusLost
        // TODO add your handling code here:
        frm.placeHoderTextLost(txtTimKH);
    }//GEN-LAST:event_txtTimKHFocusLost

    private void tableDSHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDSHoaDonMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
//            int row = tableDSHoaDon.getSelectedRow();
//            String trangThai = (String) tableDSHoaDon.getValueAt(row, 4);
//            if (trangThai.equalsIgnoreCase("Đã thanh toán")) {
//
//            } else {
//                changePanelChuaThanhToan();
//            }
        }
    }//GEN-LAST:event_tableDSHoaDonMouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable3MouseClicked

    private void txtTienKhachDuaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienKhachDuaKeyReleased
        // TODO add your handling code here:
        if (txtTienKhachDua.getText().equals("")) {
            txtTienKhachDua.setText("");
        }
        try {
            double tienKhach = Double.parseDouble(txtTienKhachDua.getText());
            double tienThua = tienKhach - Double.parseDouble(deciFormat.format(lblTongTienThanhToan.getText()));
            if (tienThua < 0) {
                lblTienThua.setText("0.0");
            } else {
                lblTienThua.setText(tienThua + "");
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_txtTienKhachDuaKeyReleased

    private void btnThanhToanHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanHDActionPerformed
        // TODO add your handling code here:
        try {
			thanhToanHD();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_btnThanhToanHDActionPerformed

    private void btnInHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInHoaDonActionPerformed
        // TODO add your handling code here:
        inHoaDon();
    }//GEN-LAST:event_btnInHoaDonActionPerformed

    private void btnQuayLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuayLaiActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlChange, pnlCenter);
        try {
			loadDataHD();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_btnQuayLaiActionPerformed

    private void btnHuyHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyHoaDonActionPerformed
        // TODO add your handling code here:
        try {
			huyDonHang();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_btnHuyHoaDonActionPerformed

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

    private void btnChonKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonKHActionPerformed
        // TODO add your handling code here:
        try {
			loadChonKH();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        jDialogChonKH.setLocationRelativeTo(null);
        jDialogChonKH.setVisible(true);
    }//GEN-LAST:event_btnChonKHActionPerformed

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

    private void tableChonSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableChonSPMouseClicked
        // TODO add your handling code here:
//        if (evt.getClickCount() == 1 && !evt.isConsumed()) {
//            evt.consume();
//            int row = tableChonSP.getSelectedRow();
//            lblTenSanPhamChon.setText((String) tableChonSP.getValueAt(row, 2));
//            jDialogGhiSoLuong.setLocationRelativeTo(null);
//            jDialogGhiSoLuong.setVisible(true);
//        }
        if (tableChonSP.getSelectedRow() == -1) {

        } else {
            int row = tableChonSP.getSelectedRow();
            lblTenSanPhamChon.setText((String) tableChonSP.getValueAt(row, 2));
            jDialogGhiSoLuong.setLocationRelativeTo(null);
            jDialogGhiSoLuong.setVisible(true);
        }
    }//GEN-LAST:event_tableChonSPMouseClicked

    private void btnChonKH1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonKH1ActionPerformed
        // TODO add your handling code here:
        try {
			loadChonKH();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        jDialogChonKH.setLocationRelativeTo(null);
        jDialogChonKH.setVisible(true);
    }//GEN-LAST:event_btnChonKH1ActionPerformed

    private void btnKhachLe1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhachLe1ActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlBotLeft, pnlBotLeftReturn);
        lblTenKH.setText("");
        lblMaKH.setText("");
        try {
			createInit();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_btnKhachLe1ActionPerformed

    private void tableChonKHMouseClicked(java.awt.event.MouseEvent evt) throws RemoteException {//GEN-FIRST:event_tableChonKHMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1 && !evt.isConsumed()) {
            evt.consume();
            int row = tableChonKH.getSelectedRow();
            DefaultTableModel modelChonKH = (DefaultTableModel) tableChonKH.getModel();
            String maKH = modelChonKH.getValueAt(row, 1).toString();
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
            showPanelChange(pnlSanPhamDaChon, pnlSanPhamDaChonNotNull);
        }
        jDialogGhiSoLuong.setVisible(false);
    }//GEN-LAST:event_btnXacNhanSoLuongActionPerformed

    private void jDialogGhiSoLuongWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialogGhiSoLuongWindowLostFocus
        // TODO add your handling code here:
        jDialogGhiSoLuong.setVisible(false);
    }//GEN-LAST:event_jDialogGhiSoLuongWindowLostFocus

    private void tableInForSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableInForSPMouseClicked
        // TODO add your handling code here:
        if (tableInForSP.getSelectedRow() == -1) {

        } else {
//            tableAction();
        }
    }//GEN-LAST:event_tableInForSPMouseClicked

    private void txtTienChietKhauKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienChietKhauKeyPressed
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
    }//GEN-LAST:event_txtTienChietKhauKeyPressed

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

    private void txtTimKHKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKHKeyReleased
        // TODO add your handling code here:
        timJtableDSHD();
    }//GEN-LAST:event_txtTimKHKeyReleased

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        DefaultTableModel tableModel = (DefaultTableModel) tableDSHoaDon.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tableDSHoaDon.setRowSorter(sorter);
        String searchTerm = jComboBox1.getSelectedItem().toString();

        RowFilter<DefaultTableModel, Object> rowFilter;
        if (searchTerm.equals("Tất cả")) {
            rowFilter = RowFilter.regexFilter("");
        } else if (searchTerm.equals("Đang chờ xử lí")) {
            rowFilter = RowFilter.regexFilter("Đang chờ xử lí");
        } else if (searchTerm.equals("Đã thanh toán")) {
            rowFilter = RowFilter.regexFilter("Đã thanh toán");
        } else {
            rowFilter = RowFilter.regexFilter("Hoàn tiền");
        }
        sorter.setRowFilter(rowFilter);
    }//GEN-LAST:event_jComboBox1ActionPerformed


    private void txtSoLuongSanPhamChonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoLuongSanPhamChonKeyReleased
        // TODO add your handling code here:
        if (txtSoLuongSanPhamChon.getText().equals("")) {
            txtSoLuongSanPhamChon.setText("");
        }
    }//GEN-LAST:event_txtSoLuongSanPhamChonKeyReleased

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        exportDanhSachHoaDon();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void lblNameLoginAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblNameLoginAncestorAdded
        // TODO add your handling code here:
        lblNameLogin.setText(gui.FrmLogin.tenNguoiDung);
    }//GEN-LAST:event_lblNameLoginAncestorAdded

    private void btnChonKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonKhuyenMaiActionPerformed
        // TODO add your handling code here:
        try {
			loadKhuyenMai();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        jDialogChonKhuyenMai.setLocationRelativeTo(btnChonKhuyenMai);
        jDialogChonKhuyenMai.setVisible(true);
    }//GEN-LAST:event_btnChonKhuyenMaiActionPerformed

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

    private void txtTimNCC2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimNCC2FocusGained
        // TODO add your handling code here:
        frm.placeHoderTextGianed(txtTimNCC1);
    }//GEN-LAST:event_txtTimNCC2FocusGained

    private void txtTimNCC2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimNCC2FocusLost
        // TODO add your handling code here:
        frm.placeHoderTextLost(txtTimNCC1);
    }//GEN-LAST:event_txtTimNCC2FocusLost

    private void txtTimNCC2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimNCC2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimNCC2ActionPerformed

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
            if(Double.parseDouble(txtTienKhachDua.getText())< 0) {
                txtTienKhachDua.setText("0.0");
            }
        }
    }//GEN-LAST:event_txtTienKhachDuaFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChonKH;
    private javax.swing.JButton btnChonKH1;
    private javax.swing.JButton btnChonKhuyenMai;
    private javax.swing.JButton btnHuyHoaDon;
    private javax.swing.JButton btnInHoaDon;
    private javax.swing.JButton btnKhachLe;
    private javax.swing.JButton btnKhachLe1;
    private javax.swing.JButton btnQuayLai;
    private javax.swing.JButton btnTatCa;
    private javax.swing.JButton btnThanhToanHD;
    private javax.swing.JButton btnXacNhanSoLuong;
    private javax.swing.JButton btnXoaKhuyenMai;
    private javax.swing.JComboBox<String> cboSortTabelChonSP;
    private javax.swing.JLabel date;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JDialog jDialogChonKH;
    private javax.swing.JDialog jDialogChonKhuyenMai;
    private javax.swing.JDialog jDialogGhiSoLuong;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
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
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JOptionPane jOptionPane1;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable3;
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
    private menuGui.MenuScrollPane menuScrollPane3;
    private menuGui.MenuScrollPane menuScrollPane6;
    private menuGui.MenuScrollPane menuScrollPane8;
    private menuGui.MenuScrollPane menuScrollPane9;
    private javax.swing.JPanel pnlBotLeft;
    private javax.swing.JPanel pnlBotLeftChange;
    private javax.swing.JPanel pnlBotLeftNonKH;
    private javax.swing.JPanel pnlBotLeftReturn;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlCenterChange;
    private javax.swing.JPanel pnlCenterKHchange;
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
    private javax.swing.JPanel pnlTableInit;
    private javax.swing.JPanel pnlTableInit1;
    private javax.swing.JPanel pnlTopLeft;
    private javax.swing.JPanel pnlTopLeft2;
    private javax.swing.JPanel pnlTopLeftChange;
    private javax.swing.JTable table;
    private javax.swing.JTable tableChonKH;
    private javax.swing.JTable tableChonKH1;
    private javax.swing.JTable tableChonSP;
    private javax.swing.JTable tableDSHoaDon;
    private javax.swing.JTable tableInForSP;
    private javax.swing.JTextField txtMaKhuyenMai;
    private javax.swing.JTextField txtSoLuongSanPhamChon;
    private javax.swing.JTextField txtTienChietKhau;
    private javax.swing.JTextField txtTienKhachDua;
    private javax.swing.JTextField txtTienKhuyenMai;
    private javax.swing.JTextField txtTimKH;
    public javax.swing.JTextField txtTimNCC1;
    public javax.swing.JTextField txtTimNCC2;
    public javax.swing.JTextField txtTimSanPhamChon;
    // End of variables declaration//GEN-END:variables
}
