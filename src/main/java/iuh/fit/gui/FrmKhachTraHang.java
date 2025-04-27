package iuh.fit.gui;

import iuh.fit.dao.*;
import iuh.fit.lookup.LookupNaming;
import iuh.fit.models.*;

import iuh.fit.gui.FrmChinh;
import menuGui.TableActionCellEditor;
import menuGui.TableActionCellRender;
import menuGui.TableActionEvent;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
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
import javax.swing.table.TableRowSorter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FrmKhachTraHang extends javax.swing.JPanel {

	private static final long serialVersionUID = 304896240406715141L;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    DecimalFormat deciFormat = new DecimalFormat("###.###");
    int soLuong = 1;
    int sttSP = 1;

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
    private DAO_KhuyenMai dao_khuyenMai = LookupNaming.lookup_KhuyenMai();
    private DAO_HoaDonHoanTra dao_hdht = LookupNaming.lookup_HoaDonHoanTra();
    private DAO_ChiTietHoaDonHoanTra dao_ctht = LookupNaming.lookup_ChiTietHoaDonHoanTra();
    private DAO_HoaDonDoiHang dao_hddh = LookupNaming.lookup_HoaDonDoiHang();
    private DAO_ChiTietHoaDonDoi dao_ctdd = LookupNaming.lookup_ChiTietHoaDonDoi();
    private Thread thread = null;
    private Thread thread1 = null;

    public FrmKhachTraHang() throws RemoteException {
        initComponents();
        thread = new Thread(this::setTimeAuto);
        thread.start();
        thread1 = new Thread(this::setTimeAuto1);
        thread1.start();
        quickPress();
        try {
			loadDataHDTraHang();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    public void setTimeAuto1() {
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
                thread1.sleep(1);
                // lấy thời gian hiện tại vào giờ vào
                date1.setText(
                        sdf_Gio.format(thoiGianHienTai) + " " + ngayTrongTuan
                        + sdf_Ngay.format(thoiGianHienTai)
                );

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        InputMap inputMap1 = btnHoanTien.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap1.put(KeyStroke.getKeyStroke("F8"), "doSomething1");

        Action action1 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnHoanTienActionPerformed(e);
            }
        };

        btnHoanTien.getActionMap().put("doSomething1", action1);

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

    }

    public void loadDataSanPhamDoiHang() throws RemoteException {
        int row = tableDSDonTra.getSelectedRow();
        String maHD = (String) tableDSDonTra.getValueAt(row, 3);
        DefaultTableModel mdSP = (DefaultTableModel) tableInForSPDoi.getModel();
        mdSP.getDataVector().removeAllElements();
        mdSP.fireTableDataChanged();
        if (!maHD.equalsIgnoreCase("")) {
            List<ChiTietHoaDonDoi> dsCTHD = dao_ctdd.getAllChiTietDonDoi();
            List<ChiTietHoaDonDoi> dsCTHD_get = new ArrayList<ChiTietHoaDonDoi>();
            HoaDonDoiHang hd = dao_hddh.getHoaDonDoiHangtheoMa(maHD);

            for (ChiTietHoaDonDoi cthd : dsCTHD) {
                if (maHD.equalsIgnoreCase(cthd.getHoaDonDoiHang().getMaHoaDonDoi())) {
                    dsCTHD_get.add(cthd);
                }
            }

            int stt = 1;
            for (ChiTietHoaDonDoi cthd : dsCTHD_get) {
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
            lblMaHoaDonDoiHang.setText(maHD);
            jTextAreaGhiChuDoiHang.setText(hd.getGhiChu());
            createInit1();
        }

    }

    public void loadDataHDTraHang() throws RemoteException {
        DefaultTableModel dm = (DefaultTableModel) tableDSDonTra.getModel();
        dm.getDataVector().removeAllElements();
        List<HoaDonHoanTra> dsHD = dao_hdht.getAllHoaDonHoanTra_20();
        int stt = 1;
        String trangThai = "";
        HoaDon hd = null;
        KhachHang kh = null;
        HoaDonDoiHang hddh = null;
        for (HoaDonHoanTra hdht : dsHD) {
            if (hdht.getTinhTrangHoaDon() == 1) {
                trangThai = "Đã hoàn tiền";
            }
            hd = dao_hd.getHoaDontheoMa(hdht.getHoaDon().getMaHoaDon());
            kh = dao_kh.getKHTheoMa(hd.getKhachHang().getMaKhachHang());
            hddh = dao_hddh.getHoaDonDoiHangtheoMaHT(hdht.getMaHoaDonHoanTra());
            if (hddh != null) {
                dm.addRow(new Object[]{stt, hd.getMaHoaDon(), hdht.getMaHoaDonHoanTra(), hddh.getMaHoaDonDoi(), formatter.format(hdht.getNgayHoan()), kh.getHoTenKH(), trangThai, deciFormat.format(hdht.getTienHoanTra()-hddh.getTienHoanTra())});
                stt++;
            } else {
                dm.addRow(new Object[]{stt, hd.getMaHoaDon(), hdht.getMaHoaDonHoanTra(), "", formatter.format(hdht.getNgayHoan()), kh.getTenKhachHang(), trangThai, deciFormat.format(hdht.getTienHoanTra())});
                stt++;
            }

        }
    }

    public synchronized void doiHang() throws RemoteException {
        if (true) {
            if (tableInForSPDoi.getRowCount() != 0) {
                HoaDonHoanTra hdht = dao_hdht.getHoaDonHoanTratheoMa(lblMaHoaDonHoanTra.getText());
                HoaDonDoiHang hddh = new HoaDonDoiHang(lblMaHoaDonDoiHang.getText(), hdht, jTextAreaGhiChuDoiHang.getText(), Double.parseDouble(lblTongTienThanhToanDoi.getText()), Double.parseDouble(txtTienChietKhau.getText()), txtMaKhuyenMai.getText());
                dao_hddh.createHoaDonDoiHang(hddh);
                DefaultTableModel md = (DefaultTableModel) tableInForSPDoi.getModel();
                String ma = "";
                for (int i = 0; i < tableInForSPDoi.getRowCount(); i++) {
                    ma = (String) tableInForSPDoi.getValueAt(i, 1);
                    if (ma.startsWith("S")) {
                        Sach s = dao_sach.getSachtheoMa(ma);
                        ChiTietHoaDonDoi ctdd = new ChiTietHoaDonDoi(hddh, s, (int) tableInForSPDoi.getValueAt(i, 4), (double) tableInForSPDoi.getValueAt(i, 5));
                        dao_ctdd.createChiTietDonDoi(ctdd);
                        s.setSoLuongTon(s.getSoLuongTon() - (int) tableInForSPDoi.getValueAt(i, 4));
                        if (s.getSoLuongTon() == 0) {
                            s.setTinhTrang("Hết hàng");
                        }
                        dao_sach.updateSach(s);
                    } else {
                        VanPhongPham vpp = dao_vpp.getVPPtheoMa(ma);
                        ChiTietHoaDonDoi ctdd = new ChiTietHoaDonDoi(hddh, vpp, (int) tableInForSPDoi.getValueAt(i, 4), (double) tableInForSPDoi.getValueAt(i, 5));
                        dao_ctdd.createChiTietDonDoi(ctdd);
                        vpp.setSoLuongTon(vpp.getSoLuongTon() - (int) tableInForSPDoi.getValueAt(i, 4));
                        if (vpp.getSoLuongTon() == 0) {
                            vpp.setTinhTrang("Hết hàng");
                        }
                        dao_vpp.update(vpp);
                    }
                }
                KhachHang kh = dao_kh.getKHTheoMa(lblMaKH.getText());
                if (kh != null) {
                    kh.setTongTienMua(kh.getTongTienMua() + hddh.getTienHoanTra());
                    if (kh.getTongTienMua() >= 500000) {
                        kh.setNhomKhachHang(NhomKhachHang.KHACHVIP);
                    }
                    dao_kh.updateKhachHang(kh);
                }
            }
        }
    }

    public synchronized void hoanTienSanPhamTra() throws RemoteException {
        if (true) {
            if (tableInForSP.getRowCount() != 0) {
                LocalDateTime ngayLap = LocalDateTime.now();
                NhanVien nv = dao_nv.getNVTheoMa("QL23102023-000007");
                HoaDon hd = dao_hd.getHoaDontheoMa(lblMaHoaDon.getText());
                HoaDonHoanTra hdht = new HoaDonHoanTra(lblMaHoaDonHoanTra.getText(), ngayLap, nv, hd, jTextAreaGhiChuTraHang.getText(), 1, Double.parseDouble(lblTongTienHoan.getText()));
                dao_hdht.createHoaDonHoanTra(hdht);
                System.out.println(lblMaHoaDonHoanTra.getText());
                DefaultTableModel md = (DefaultTableModel) tableInForSP.getModel();
                String ma = "";
                for (int i = 0; i < tableInForSP.getRowCount(); i++) {
                    ma = (String) tableInForSP.getValueAt(i, 1);
                    if (ma.startsWith("S")) {
                        Sach s = dao_sach.getSachtheoMa(ma);
                        ChiTietHoanTra ctht = new ChiTietHoanTra(hdht, s, (int) tableInForSP.getValueAt(i, 4), (double) tableInForSP.getValueAt(i, 5));
                        dao_ctht.createChiTietHoanTra(ctht);
                        s.setSoLuongTon(s.getSoLuongTon() + (int) tableInForSP.getValueAt(i, 4));
                        dao_sach.updateSach(s);
                    } else {
                        VanPhongPham vpp = dao_vpp.getVPPtheoMa(ma);
                        ChiTietHoanTra ctht = new ChiTietHoanTra(hdht, vpp, (int) tableInForSP.getValueAt(i, 4), (double) tableInForSP.getValueAt(i, 5));
                        dao_ctht.createChiTietHoanTra(ctht);
                        vpp.setSoLuongTon(vpp.getSoLuongTon() + (int) tableInForSP.getValueAt(i, 4));
                        dao_vpp.update(vpp);
                    }
                }
                KhachHang kh = dao_kh.getKHTheoMa(lblMaKH.getText());
                if (kh != null) {
                    if (hd.getTongTien() == hdht.getTienHoanTra()) {
                        kh.setTongTienMua(kh.getTongTienMua() - hd.getTongTien());
                        kh.setSoLuongHoaDon(kh.getSoLuongHoaDon() - 1);
                    } else {
                        kh.setTongTienMua(kh.getTongTienMua() - Double.parseDouble(lblTongTienHoan.getText()));
                    }
                    if (kh.getTongTienMua() < 500000) {
                        kh.setNhomKhachHang(NhomKhachHang.KHACHBT);
                    }
                    dao_kh.updateKhachHang(kh);
                }
                hd.setTinhTrangHoaDon(-1);
                dao_hd.updateHoaDon(hd);
                showPanelChange(pnlChange, pnlCenter);
                doiHang();
                loadDataHDTraHang();
                lamMoiHoanTra();
            }
        }

    }

    public void loadSanPhamTra() throws RemoteException {
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

        DefaultTableModel mdSPBan = (DefaultTableModel) tableSanPhamDaBan.getModel();
        mdSPBan.getDataVector().removeAllElements();

        int stt = 1;
        for (ChiTietHoaDon cthd : dsCTHD_get) {
            if (cthd.getSanPham().getMaSanPham().startsWith("S")) {
                Sach s = dao_sach.getSachtheoMa(cthd.getSanPham().getMaSanPham());
                mdSPBan.addRow(new Object[]{stt, cthd.getSanPham().getMaSanPham(), s.getTenSanPham(), s.getDonGiaBan(), cthd.getSoLuong(), cthd.getThanhTien()});
            } else {
                VanPhongPham vpp = dao_vpp.getVPPtheoMa(cthd.getSanPham().getMaSanPham());
                mdSPBan.addRow(new Object[]{stt, cthd.getSanPham().getMaSanPham(), vpp.getTenSanPham(), vpp.getDonGiaBan(), cthd.getSoLuong(), cthd.getThanhTien()});
            }
            stt++;
        }
    }

    public void loadSanPhamTraTuDSDonTra() throws RemoteException {
        int row = tableDSDonTra.getSelectedRow();
        String maHD = (String) tableDSDonTra.getValueAt(row, 1);
        List<ChiTietHoaDon> dsCTHD = dao_cthd.getAllChiTietHoaDon();
        List<ChiTietHoaDon> dsCTHD_get = new ArrayList<ChiTietHoaDon>();
        HoaDon hd = dao_hd.getHoaDontheoMa(maHD);

        for (ChiTietHoaDon cthd : dsCTHD) {
            if (maHD.equalsIgnoreCase(cthd.getHoaDon().getMaHoaDon())) {
                dsCTHD_get.add(cthd);
            }
        }

        DefaultTableModel mdSPBan = (DefaultTableModel) tableSanPhamDaBan.getModel();
        mdSPBan.getDataVector().removeAllElements();

        int stt = 1;
        for (ChiTietHoaDon cthd : dsCTHD_get) {
            if (cthd.getSanPham().getMaSanPham().startsWith("S")) {
                Sach s = dao_sach.getSachtheoMa(cthd.getSanPham().getMaSanPham());
                mdSPBan.addRow(new Object[]{stt, cthd.getSanPham().getMaSanPham(), s.getTenSanPham(), s.getDonGiaBan(), cthd.getSoLuong(), cthd.getThanhTien()});
            } else {
                VanPhongPham vpp = dao_vpp.getVPPtheoMa(cthd.getSanPham().getMaSanPham());
                mdSPBan.addRow(new Object[]{stt, cthd.getSanPham().getMaSanPham(), vpp.getTenSanPham(), vpp.getDonGiaBan(), cthd.getSoLuong(), cthd.getThanhTien()});
            }
            stt++;
        }
    }

    public void loadSanPhamDaTra() throws RemoteException {
        int row = tableDSDonTra.getSelectedRow();
        String maHT = (String) tableDSDonTra.getValueAt(row, 2);
        List<ChiTietHoanTra> dsCTHT = dao_ctht.getAllChiTietHoanTra();
        List<ChiTietHoanTra> dsCTHT_get = new ArrayList<ChiTietHoanTra>();
        HoaDonHoanTra hdht = dao_hdht.getHoaDonHoanTratheoMa(maHT);

        jTextAreaGhiChuTraHang.setText(hdht.getGhiChu());

        for (ChiTietHoanTra ctht : dsCTHT) {
            if (maHT.equalsIgnoreCase(ctht.getHoaDonHoanTra().getMaHoaDonHoanTra())) {
                dsCTHT_get.add(ctht);
            }
        }

        DefaultTableModel mdSPBan = (DefaultTableModel) tableInForSP.getModel();
        mdSPBan.getDataVector().removeAllElements();

        int stt = 1;
        for (ChiTietHoanTra ctht : dsCTHT_get) {
            if (ctht.getSanPham().getMaSanPham().startsWith("S")) {
                Sach s = dao_sach.getSachtheoMa(ctht.getSanPham().getMaSanPham());
                mdSPBan.addRow(new Object[]{stt, ctht.getSanPham().getMaSanPham(), s.getTenSanPham(), s.getDonGiaBan(), ctht.getSoLuong(), ctht.getThanhTien()});
            } else {
                VanPhongPham vpp = dao_vpp.getVPPtheoMa(ctht.getSanPham().getMaSanPham());
                mdSPBan.addRow(new Object[]{stt, ctht.getSanPham().getMaSanPham(), vpp.getTenSanPham(), vpp.getDonGiaBan(), ctht.getSoLuong(), ctht.getThanhTien()});
            }
            stt++;
        }
    }

    private void showPanelChange(JPanel a, JPanel b) {
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
                String maSP = (String) tableInForSP.getValueAt(row, 1);
//                String maHT = lblMaHoaDonHoanTra.getText();
//                ChiTietHoanTra ctht = dao_ctht.getHoaDontheoMa(maHT, maSP);
                String maHD = lblMaHoaDon.getText();
                ChiTietHoaDon cthd;
				try {
					cthd = dao_cthd.getCTHoaDontheoMa(maHD, maSP);
					DefaultTableModel model = (DefaultTableModel) tableInForSP.getModel();
	                int sl = (int) tableInForSP.getValueAt(row, 4);
	                double dg = (double) tableInForSP.getValueAt(row, 3);
	                if (sl < cthd.getSoLuong()) {
	                    sl++;
	                    model.setValueAt(sl, row, 4);
	                    model.setValueAt(tinhThanhTien(sl, dg), row, 5);
	                    System.out.println("onPlus row : " + row);
	                    createInit();
	                    createInit1();
	                }
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

                try {
					createInit();
					createInit1();
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
					createInit1();
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

    public void tableAction1() {
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onPlus(int row) {
                if (tableInForSPDoi.isEditing()) {
                    tableInForSPDoi.getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) tableInForSPDoi.getModel();
                String maSP = (String) tableInForSPDoi.getValueAt(row, 1);
                int sl = (int) tableInForSPDoi.getValueAt(row, 4);
                double dg = (double) tableInForSPDoi.getValueAt(row, 3);
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
	                        createInit1();
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
	                        createInit1();
	                    }
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    
                }

            }

            @Override
            public void onDelete(int row) {
                if (tableInForSPDoi.isEditing()) {
                    tableInForSPDoi.getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) tableInForSPDoi.getModel();
                for (int i = row; i < tableInForSPDoi.getRowCount(); i++) {
                    int stt = (int) tableInForSPDoi.getValueAt(i, 0);
                    stt--;
                    tableInForSPDoi.setValueAt(stt, i, 0);
                }
                model.removeRow(row);
                if (tableInForSPDoi.getRowCount() == 0) {
                    showPanelChange(pnlSanPhamDaChon, pnlSanPhamDaChonNull);
                }
                try {
					createInit();
					createInit1();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               

                sttSP--;
            }

            @Override
            public void onMinus(int row) {
                if (tableInForSPDoi.isEditing()) {
                    tableInForSPDoi.getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) tableInForSPDoi.getModel();
                int sl = (int) tableInForSPDoi.getValueAt(row, 4);
                double dg = (double) tableInForSPDoi.getValueAt(row, 3);
                sl--;
                model.setValueAt(sl, row, 4);
                model.setValueAt(tinhThanhTien(sl, dg), row, 5);
                if (sl == 0) {
                    onDelete(row);
                }
                System.out.println("onMinus row : " + row);
                try {
					createInit();
					createInit1();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            }
        };

        tableInForSPDoi.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        tableInForSPDoi.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor(event));
        tableInForSPDoi.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                setHorizontalAlignment(SwingConstants.RIGHT);
                return super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
            }
        });
    }

    public void loadDataHD() throws RemoteException {
        DefaultTableModel dm = (DefaultTableModel) tableDSHoaDon.getModel();
        dm.getDataVector().removeAllElements();
        List<HoaDon> dsHD = dao_hd.getAllHoaDon();
        KhachHang kh = null;
        NhanVien nv = null;
        DefaultTableModel tableModal = (DefaultTableModel) tableDSHoaDon.getModel();
        int stt = 1;
        String trangThai = "";
        LocalDateTime curDate = LocalDateTime.now();
        for (HoaDon hd : dsHD) {
            if (hd.getTinhTrangHoaDon() == 1 && hd.getNgayLap().isAfter(curDate.minusDays(7))) {
                nv = dao_nv.getNVTheoMa(hd.getNhanVien().getMaNhanVien());
                kh = dao_kh.getKHTheoMa(hd.getKhachHang().getMaKhachHang());
                tableModal.addRow(new Object[]{stt, hd.getMaHoaDon(), formatter.format(hd.getNgayLap()), kh.getTenKhachHang(), nv.getHoTenNV(), deciFormat.format(hd.getTongTien())});
                stt++;
            }

        }
    }

    public void timTableSPdaBan() {
        DefaultTableModel tableModel = (DefaultTableModel) tableSanPhamDaBan.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tableSanPhamDaBan.setRowSorter(sorter);

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

    public void timTableDSDonTra() {
        DefaultTableModel tableModel = (DefaultTableModel) tableDSDonTra.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tableDSDonTra.setRowSorter(sorter);

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
                    String tenSp = entry.getValue(5).toString();

                    return pattern.matcher(maSp).lookingAt() || pattern.matcher(tenSp).lookingAt();
                }
            });
        }
    }

    public void changePanelChuaThanhToan() throws RemoteException {
        lblMaHoaDonHoanTra.setText(createMaHoaDonHoanTra());
        btnHoanTien.setEnabled(true);
        btnHoanTien1.setEnabled(true);
        btnChonKH.setEnabled(false);
        btnKhachLe.setEnabled(false);
        btnChonKH1.setEnabled(false);
        btnKhachLe1.setEnabled(false);
        tableSanPhamDaBan.setEnabled(true);
        tableInForSP.setEnabled(true);
        jTextAreaGhiChuTraHang.setEditable(true);
        cboSortTabelChonSP.setEnabled(true);
        txtTimSanPhamChon.setEditable(true);
        txtTimSanPhamDoi.setEditable(true);
        txtTienChietKhau.setEditable(true);
        txtTienKhuyenMai.setEditable(false);
        tableChonSPDoiHang.setEnabled(true);
        tableInForSPDoi.setEnabled(true);
        jTextAreaGhiChuDoiHang.setEditable(true);
        btnChonKhuyenMai.setEnabled(true);
        btnXoaKhuyenMai.setEnabled(true);
        //******** load San pham
        int row = tableDSHoaDon.getSelectedRow();
        String maHD = (String) tableDSHoaDon.getValueAt(row, 1);
        HoaDon hd = dao_hd.getHoaDontheoMa(maHD);
//            //******** load Thong tin hoa don
        lblMaHoaDon.setText(maHD);
        jTextAreaGhiChuTraHang.setText("");

        DefaultTableModel md = (DefaultTableModel) tableSanPhamDaBan.getModel();
        double tongTienThanhToan = 0.0;
        int tongSoLuongSp = 0;
        for (int i = 0; i < md.getRowCount(); i++) {
            tongTienThanhToan += (double) md.getValueAt(i, 5);
            tongSoLuongSp += (int) md.getValueAt(i, 4);
        }
        String maKhuyenMai = hd.getKhuyenMai();
        double tongTienKM = 0.0;
        if (tableSanPhamDaBan.getRowCount() != 0) {
            KhuyenMai km = dao_khuyenMai.getKMtheoMa(maKhuyenMai);
            if (km != null && km.getTienToiThieu() <= tongTienThanhToan) {
                tongTienKM = tongTienThanhToan * (km.getTyLeKhuyenMai() / 100);
                if (tongTienKM > km.getGiaTriKhuyenMaiToiDa()) {
                    tongTienKM = km.getGiaTriKhuyenMaiToiDa();
                }
            }
        }
        KhachHang kh = dao_kh.getKHTheoMa(hd.getKhachHang().getMaKhachHang());
        if (!kh.getTenKhachHang().equalsIgnoreCase("Khách lẻ")) {
            tongTienKM = tongTienThanhToan * 0.05 + tongTienKM;
        }
        txtTienKhuyenMaiBanDau.setText(tongTienKM + "");
        txtMaKhuyenMaiBanDau.setText(hd.getKhuyenMai());
//            //******* load Khach hang
        lblTenKH.setText(kh.getTenKhachHang());
        lblSDTKH.setText(kh.getSoDienThoai());
        lblTongDonMua.setText(kh.getSoLuongHoaDon() + "");
//            lblTongDonTra.setText("0");
        lblTongTienMua.setText(kh.getTongTienMua() + "");
        lblMaKH.setText(kh.getMaKhachHang());
        lblNhomKhachHang.setText(kh.getNhomKhachHang() + "");
        if (lblTenKH.getText().equals("Khách lẻ")) {
            showPanelChange(pnlBotLeft, pnlBotLeftReturn);
        } else {
            showPanelChange(pnlBotLeft, pnlBotLeftChange);
        }
        showPanelChange(pnlSanPhamDaChon, pnlSanPhamDaChonNotNull);
//        showPanelChange(pnlBotLeft, pnlBotLeftChange);
        //*******
        showPanelChange(pnlChange, pnlCenterTra);

    }

    public void createInit() throws RemoteException {
        DefaultTableModel md = (DefaultTableModel) tableInForSP.getModel();
        DefaultTableModel md1 = (DefaultTableModel) tableSanPhamDaBan.getModel();
        double tongTienThanhToan = 0.0;
        double tongTienHoan = 0.0;
        int tongSoLuongSp = 0;
        int tongSoLuongSpDaBan = 0;
        for (int i = 0; i < md.getRowCount(); i++) {
            tongTienHoan += (double) md.getValueAt(i, 5);
            tongSoLuongSp += (int) md.getValueAt(i, 4);
        }
        for (int i = 0; i < md1.getRowCount(); i++) {
            tongTienThanhToan += (double) md1.getValueAt(i, 5);
            tongSoLuongSpDaBan += (int) md1.getValueAt(i, 4);
        }
        // Tính tiền hoàn
        HoaDon hd = dao_hd.getHoaDontheoMa(lblMaHoaDon.getText());
        double tienHoan = 0.0;
        String maKhuyenMai = hd.getKhuyenMai();
        double tienKhuyenMai = khuyenMaiBanDau(maKhuyenMai, tongTienThanhToan);
        KhachHang kh = dao_kh.getKHTheoMa(hd.getKhachHang().getMaKhachHang());
        if (tongSoLuongSp == tongSoLuongSpDaBan) {
            tienHoan = hd.getTongTien();
        } else {
            tienHoan = tongTienHoan;
        }
        if (md.getRowCount() == 0) {
            lblTongTienHoan.setText("0.0");
            lblTongSoLuongSPTra.setText("0");
        }
        lblTongTienHoan.setText(deciFormat.format(tienHoan));
        lblTongSoLuongSPTra.setText(tongSoLuongSp + "");
    }

    public void createInit1() throws RemoteException {
        DefaultTableModel md = (DefaultTableModel) tableInForSPDoi.getModel();
        double tongTienThanhToan = 0;
        int tongSoLuongSp = 0;
        double tienHoan = Double.parseDouble(lblTongTienHoan.getText());
        for (int i = 0; i < md.getRowCount(); i++) {
            tongTienThanhToan += (double) md.getValueAt(i, 5);
            tongSoLuongSp += (int) md.getValueAt(i, 4);
        }
        lblTongTien.setText(deciFormat.format(tongTienThanhToan));
        lblTongTienThanhToanDoi.setText(deciFormat.format(tongTienThanhToan));
        lblTongSoLuongSanPhamDoi.setText(tongSoLuongSp + "");
//        double khachTra = tongTienThanhToan - tienHoan;
        lblConPhaiHoanChoKhach.setText("0.0");
        KhachHang kh = dao_kh.getKHTheoMa(lblMaKH.getText());
        double tienTraKhach = 0;
        if (kh == null) {
            double tienKhuyenMai = apDungKhuyenMai();
            double chietKhau = 0.0;
            if (txtTienChietKhau.getText().equalsIgnoreCase("") || Double.parseDouble(txtTienChietKhau.getText()) < 0) {
                chietKhau = 0.0;
            } else {
                chietKhau = Double.parseDouble(txtTienChietKhau.getText());
            }
            double tien = tongTienThanhToan + chietKhau - tienKhuyenMai;
            double khachTra = tien - tienHoan;
            txtTienKhuyenMai.setText("0.0");
            if (khachTra <= 0) {
                tienTraKhach = tienHoan - tien;
                lblKhachConPhaiTra.setText("0.0");
            } else {
                lblKhachConPhaiTra.setText(khachTra + "");
            }
            lblTongTienThanhToanDoi.setText(deciFormat.format(tien));
            lblTongTien.setText(deciFormat.format(tongTienThanhToan));
            lblConPhaiHoanChoKhach.setText(tienTraKhach + "");
            lblTongSoLuongSanPhamDoi.setText(tongSoLuongSp + "");
            txtTienKhuyenMai.setText(tienKhuyenMai + "");
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
                double khachTra = tien - tienHoan;
                txtTienKhuyenMai.setText(((tongTienThanhToan * 0.05) + tienKhuyenMai) + "");
                if (khachTra <= 0) {
                    tienTraKhach = tienHoan - tien;
                    lblKhachConPhaiTra.setText("0.0");
                } else {
                    lblKhachConPhaiTra.setText(khachTra + "");
                }
                lblTongTienThanhToanDoi.setText(deciFormat.format(tien));
                lblTongTien.setText(deciFormat.format(tongTienThanhToan));
                lblConPhaiHoanChoKhach.setText(deciFormat.format(tienTraKhach));
                lblTongSoLuongSanPhamDoi.setText(tongSoLuongSp + "");
            } else {
                double tienKhuyenMai = apDungKhuyenMai();
                double chietKhau = 0.0;
                if (txtTienChietKhau.getText().equalsIgnoreCase("") || Double.parseDouble(txtTienChietKhau.getText()) < 0) {
                    chietKhau = 0.0;
                } else {
                    chietKhau = Double.parseDouble(txtTienChietKhau.getText());
                }
                double tien = tongTienThanhToan + chietKhau - tienKhuyenMai;
                double khachTra = tien - tienHoan;
                txtTienKhuyenMai.setText("0.0");
                if (khachTra <= 0) {
                    tienTraKhach = tienHoan - tien;
                    lblKhachConPhaiTra.setText("0.0");
                } else {
                    lblKhachConPhaiTra.setText(khachTra + "");
                }
                lblTongTienThanhToanDoi.setText(deciFormat.format(tien));
                lblTongTien.setText(deciFormat.format(tongTienThanhToan));
                lblConPhaiHoanChoKhach.setText(deciFormat.format(tienTraKhach));
                lblTongSoLuongSanPhamDoi.setText(tongSoLuongSp + "");
                txtTienKhuyenMai.setText(tienKhuyenMai + "");
            }
        }

    }

    public void loadChonSP() throws RemoteException {
        List<Sach> dsSach = dao_sach.getAlltbSach();
        List<VanPhongPham> dsVpp = dao_vpp.getAllVanPhongPhan();

        DefaultTableModel tableModal = (DefaultTableModel) tableChonSPDoiHang.getModel();
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

    public void sortTableChooseProDuct() throws RemoteException {
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableChonSPDoiHang.getModel());
        tableChonSPDoiHang.setRowSorter(sorter);
        DefaultTableModel md = (DefaultTableModel) tableChonSPDoiHang.getModel();
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

    public String createMaHoaDonHoanTra() throws RemoteException {
//        LocalDate d = LocalDate.of(2023, 11, 13);
        LocalDate d = LocalDate.now();
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("ddMMyyyy");
        String format = d.format(myFormatDate);
        Integer count = 1;
        String hdID = "";
        do {
            String tempID = count.toString().length() == 1 ? ("HT" + format + "-00000" + count)
                    : count.toString().length() == 2 ? ("HT" + format + "-0000" + count)
                    : count.toString().length() == 3 ? ("HT" + format + "-000" + count)
                    : count.toString().length() == 4 ? ("HT" + format + "-00" + count)
                    : count.toString().length() == 5 ? ("HT" + format + "-0" + count)
                    : ("HT" + format + "-" + count);

            HoaDonHoanTra existingBill = dao_hdht.getHoaDonHoanTratheoMa(tempID);
            if (existingBill == null) {
                hdID = tempID;
                break;
            }
            count++;
        } while (true);

        return hdID;
    }

    public void timTableSPdoiHang() {
        DefaultTableModel tableModel = (DefaultTableModel) tableChonSPDoiHang.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tableChonSPDoiHang.setRowSorter(sorter);
    }

    public String createMaHoaDonDoiHang() throws RemoteException {
//        LocalDate d = LocalDate.of(2023, 11, 13);
        LocalDate d = LocalDate.now();
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("ddMMyyyy");
        String format = d.format(myFormatDate);
        Integer count = 1;
        String hdID = "";
        do {
            String tempID = count.toString().length() == 1 ? ("HDD" + format + "-00000" + count)
                    : count.toString().length() == 2 ? ("HDD" + format + "-0000" + count)
                    : count.toString().length() == 3 ? ("HDD" + format + "-000" + count)
                    : count.toString().length() == 4 ? ("HDD" + format + "-00" + count)
                    : count.toString().length() == 5 ? ("HDD" + format + "-0" + count)
                    : ("HT" + format + "-" + count);

            HoaDonDoiHang existingBill = dao_hddh.getHoaDonDoiHangtheoMa(tempID);
            if (existingBill == null) {
                hdID = tempID;
                break;
            }
            count++;
        } while (true);

        return hdID;
    }

    public void loadChonSPDoiHang() throws RemoteException {
        List<Sach> dsSach = dao_sach.getAlltbSach();
        List<VanPhongPham> dsVpp = dao_vpp.getAllVanPhongPhan();

        DefaultTableModel tableModal = (DefaultTableModel) tableChonSPDoiHang.getModel();
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

    public void changePanelDaHoanTien() throws RemoteException {
        btnHoanTien.setEnabled(false);
        btnHoanTien1.setEnabled(false);
        btnChonKH.setEnabled(false);
        btnKhachLe.setEnabled(false);
        btnChonKH1.setEnabled(false);
        btnKhachLe1.setEnabled(false);
        tableSanPhamDaBan.setEnabled(false);
        tableInForSP.setEnabled(false);
        jTextAreaGhiChuTraHang.setEditable(false);
        cboSortTabelChonSP.setEnabled(false);
        txtTimSanPhamChon.setEditable(false);
        txtTimSanPhamDoi.setEditable(false);
        txtTienChietKhau.setEditable(false);
        txtTienKhuyenMai.setEditable(false);
        tableChonSPDoiHang.setEnabled(false);
        tableInForSPDoi.setEnabled(false);
        jTextAreaGhiChuDoiHang.setEditable(false);
        btnChonKhuyenMai.setEnabled(false);
        btnXoaKhuyenMai.setEnabled(false);
        //******** load San pham
        int row = tableDSDonTra.getSelectedRow();
        String maHD = (String) tableDSDonTra.getValueAt(row, 1);
        String maHDDoi = (String) tableDSDonTra.getValueAt(row, 3);
        String maHDHT = (String) tableDSDonTra.getValueAt(row, 2);
        HoaDon hd = dao_hd.getHoaDontheoMa(maHD);
        HoaDonDoiHang hddh = null;
        HoaDonHoanTra hdht = dao_hdht.getHoaDonHoanTratheoMa(maHDHT);
        if (!maHDDoi.equalsIgnoreCase("")) {
            hddh = dao_hddh.getHoaDonDoiHangtheoMa(maHDDoi);
            jTextAreaGhiChuDoiHang.setText(hddh.getGhiChu());
            txtMaKhuyenMai.setText(hddh.getKhuyenMai());
            txtTienChietKhau.setText(hddh.getChietKhau() + "");
            jTextAreaGhiChuDoiHang.setText(hddh.getGhiChu());
        }
//            //******** load Thong tin hoa don
        lblMaHoaDon.setText(maHD);
        lblMaHoaDonHoanTra.setText(hdht.getMaHoaDonHoanTra());
        jTextAreaGhiChuTraHang.setText(hdht.getGhiChu());

//            //******* load Khach hang
        KhachHang kh = dao_kh.getKHTheoMa(hd.getKhachHang().getMaKhachHang());
        lblTenKH.setText(kh.getTenKhachHang());
        lblSDTKH.setText(kh.getSoDienThoai());
        lblTongDonMua.setText(kh.getSoLuongHoaDon() + "");
//            lblTongDonTra.setText("0");
        lblTongTienMua.setText(kh.getTongTienMua() + "");
        lblMaKH.setText(kh.getMaKhachHang());
        lblNhomKhachHang.setText(kh.getNhomKhachHang() + "");
        //--------
//        txtMaKhuyenMai.setText(hd.getKhuyenMai());
        createInit();
        createInit1();
        if (lblTenKH.getText().equals("")) {
            showPanelChange(pnlBotLeft, pnlBotLeftReturn);
        } else {
            showPanelChange(pnlBotLeft, pnlBotLeftChange);
        }
        showPanelChange(pnlSanPhamDaChon, pnlSanPhamDaChonNotNull);
//        showPanelChange(pnlBotLeft, pnlBotLeftChange);
        //*******
        showPanelChange(pnlChange, pnlCenterTra);

    }
    
  //export đơn hoàn trả - Gui>FrmKhachTraHang
    public void exportHoaDonHoanTraToExcel() {
           Runnable exportTask = new Runnable() {
               @Override
               public void run() {
                   // Mã của hàm exportHoaDonHoanTraToExcel()
                   Workbook workbook = new XSSFWorkbook();
                   Sheet sheet = workbook.createSheet("DanhSachHoaDonHoanTra");

                   // Tạo hàng đầu tiên (Header)
                   Row headerRow = sheet.createRow(0);
                   String[] headers = {"STT", "Mã Hóa Đơn", "Mã Đơn Trả", "Mã Hóa Đơn Đổi", "Ngày Hoàn",
                           "Tên Khách Hàng", "Trạng Thái", "Tổng Tiền Hoàn"};
                   for (int i = 0; i < headers.length; i++) {
                       Cell cell = headerRow.createCell(i);
                       cell.setCellValue(headers[i]);
                   }

                   // Lấy dữ liệu từ JTable và điền vào sheet
                   DefaultTableModel model = (DefaultTableModel) tableDSDonTra.getModel();
                   int rowCount = model.getRowCount();
                   for (int i = 0; i < rowCount; i++) {
                       Row row = sheet.createRow(i + 1); // Bắt đầu từ dòng thứ 2

                       for (int j = 0; j < model.getColumnCount(); j++) {
                           Cell cell = row.createCell(j);

                           // Định dạng kiểu dữ liệu ngày tháng
                           if (model.getColumnName(j).equals("Ngày Hoàn")) {
                               CellStyle dateCellStyle = workbook.createCellStyle();
                               CreationHelper createHelper = workbook.getCreationHelper();
                               dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm:ss"));
                               cell.setCellStyle(dateCellStyle);

                               // Chuyển LocalDateTime thành chuỗi và ghi vào cell
                               LocalDateTime date = (LocalDateTime) model.getValueAt(i, j);
                               cell.setCellValue(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                           } else {
                               // Ghi dữ liệu từ JTable vào cell
                               cell.setCellValue(String.valueOf(model.getValueAt(i, j)));
                           }
                       }
                   }

                   try {
                       JFileChooser fileChooser = new JFileChooser();
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

    // export đơn hoàn trả
//    public void exportHoaDonHoanTraToExcel() {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("DanhSachHoaDonHoanTra");
//
//        // Tạo hàng đầu tiên (Header)
//        Row headerRow = sheet.createRow(0);
//        String[] headers = {"STT", "Mã Hóa Đơn", "Mã Đơn Trả", "Mã Hóa Đơn Đổi", "Ngày Hoàn",
//            "Tên Khách Hàng", "Trạng Thái", "Tổng Tiền Hoàn"};
//        for (int i = 0; i < headers.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(headers[i]);
//        }
//
//        // Lấy dữ liệu từ JTable và điền vào sheet
//        DefaultTableModel model = (DefaultTableModel) tableDSDonTra.getModel();
//        int rowCount = model.getRowCount();
//        for (int i = 0; i < rowCount; i++) {
//            Row row = sheet.createRow(i + 1); // Bắt đầu từ dòng thứ 2
//
//            for (int j = 0; j < model.getColumnCount(); j++) {
//                Cell cell = row.createCell(j);
//
//                // Định dạng kiểu dữ liệu ngày tháng
//                if (model.getColumnName(j).equals("Ngày Hoàn")) {
//                    CellStyle dateCellStyle = workbook.createCellStyle();
//                    CreationHelper createHelper = workbook.getCreationHelper();
//                    dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm:ss"));
//                    cell.setCellStyle(dateCellStyle);
//
//                    // Chuyển LocalDateTime thành chuỗi và ghi vào cell
//                    LocalDateTime date = (LocalDateTime) model.getValueAt(i, j);
//                    cell.setCellValue(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
//                } else {
//                    // Ghi dữ liệu từ JTable vào cell
//                    cell.setCellValue(String.valueOf(model.getValueAt(i, j)));
//                }
//            }
//        }
//
//        try {
//            JFileChooser fileChooser = new JFileChooser();
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

    public double apDungKhuyenMai() throws RemoteException {
        double tongTienKM = 0.0;
        if (tableInForSPDoi.getRowCount() != 0) {
            KhuyenMai km = dao_khuyenMai.getKMtheoMa(txtMaKhuyenMai.getText());
            if (km != null && km.getTienToiThieu() <= Double.parseDouble(lblTongTien.getText())) {
                tongTienKM = Double.parseDouble(lblTongTien.getText()) * (km.getTyLeKhuyenMai() / 100);
                if (tongTienKM > km.getGiaTriKhuyenMaiToiDa()) {
                    tongTienKM = km.getGiaTriKhuyenMaiToiDa();
                    return tongTienKM;
                }
                return tongTienKM;
            }
        }
        return tongTienKM;
    }

    public double khuyenMaiBanDau(String ma, double tongTien) throws RemoteException {
        double tongTienKM = 0.0;
        if (tableSanPhamDaBan.getRowCount() != 0) {
            KhuyenMai km = dao_khuyenMai.getKMtheoMa(ma);
            if (km != null && km.getTienToiThieu() <= tongTien) {
                tongTienKM = tongTien * (km.getTyLeKhuyenMai() / 100);
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

    public void lamMoiHoanTra() {
        DefaultTableModel md = (DefaultTableModel) tableInForSP.getModel();
        md.getDataVector().removeAllElements();
        md.fireTableDataChanged();
        DefaultTableModel md1 = (DefaultTableModel) tableInForSPDoi.getModel();
        md1.getDataVector().removeAllElements();
        md1.fireTableDataChanged();
        txtMaKhuyenMaiBanDau.setText("");
        txtTienKhuyenMaiBanDau.setText("0.0");
        txtTienKhuyenMai.setText("0.0");
        txtMaKhuyenMai.setText("");
        lblConPhaiHoanChoKhach.setText("0.0");
        lblKhachConPhaiTra.setText("0.0");
        lblTongSoLuongSanPhamDoi.setText("0");
        lblTongSoLuongSPTra.setText("0");
        lblTongTien.setText("0.0");
        lblTongTienHoan.setText("0.0");
        lblTongTienThanhToanDoi.setText("0.0");
    }
    
    public void timJtableLapHoaDon() {
        DefaultTableModel tableModel = (DefaultTableModel) tableChonSPDoiHang.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tableChonSPDoiHang.setRowSorter(sorter);

        txtTimSanPhamDoi.getDocument().addDocumentListener(new DocumentListener() {
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
        String searchTerm = txtTimSanPhamDoi.getText().toUpperCase();
        if (searchTerm.isEmpty()) {
            sorter.setRowFilter(RowFilter.regexFilter(txtTimSanPhamDoi.getText()));
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     * @throws RemoteException 
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() throws RemoteException {

        pnlCenterTra = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        menuScrollPane13 = new menuGui.MenuScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        pnlRight = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        menuScrollPane7 = new menuGui.MenuScrollPane();
        jTextAreaGhiChuTraHang = new javax.swing.JTextArea();
        jLabel135 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        lblTongSoLuongSPTra = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        lblTongTienHoan = new javax.swing.JLabel();
        pnlSanPhamDaChon = new javax.swing.JPanel();
        pnlSanPhamDaChonNull = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        lblMaHoaDonHoanTra = new javax.swing.JLabel();
        lblMaHoaDon = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        txtMaKhuyenMaiBanDau = new javax.swing.JTextField();
        txtTienKhuyenMaiBanDau = new javax.swing.JTextField();
        btnHoanTien = new javax.swing.JButton();
        btnQuayLai = new javax.swing.JButton();
        pnlBotLeft = new javax.swing.JPanel();
        pnlBotLeftReturn = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btnKhachLe = new javax.swing.JButton();
        btnChonKH = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        pnlTopLeft3 = new javax.swing.JPanel();
        pnlNonInFor = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        pnlNonInfo = new javax.swing.JPanel();
        pnlChonNCC = new javax.swing.JPanel();
        jPanel56 = new javax.swing.JPanel();
        jLabel148 = new javax.swing.JLabel();
        txtTimSanPhamChon = new javax.swing.JTextField();
        menuScrollPane12 = new menuGui.MenuScrollPane();
        tableSanPhamDaBan = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        pnlTopLeft2 = new javax.swing.JPanel();
        pnlNonInFor1 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        pnlNonInfo1 = new javax.swing.JPanel();
        pnlChonNCC2 = new javax.swing.JPanel();
        jPanel57 = new javax.swing.JPanel();
        jLabel150 = new javax.swing.JLabel();
        txtTimSanPhamDoi = new javax.swing.JTextField();
        cboSortTabelChonSP = new javax.swing.JComboBox<>();
        menuScrollPane14 = new menuGui.MenuScrollPane();
        tableChonSPDoiHang = new javax.swing.JTable();
        pnlSanPhamDaChonNotNull1 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        menuScrollPane15 = new menuGui.MenuScrollPane();
        tableInForSPDoi = new javax.swing.JTable();
        jLabel138 = new javax.swing.JLabel();
        lblMaHoaDonDoiHang = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        menuScrollPane8 = new menuGui.MenuScrollPane();
        jTextAreaGhiChuDoiHang = new javax.swing.JTextArea();
        jLabel60 = new javax.swing.JLabel();
        txtTienKhuyenMai = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        txtTienChietKhau = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        lblTongSoLuongSanPhamDoi = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        lblTongTienThanhToanDoi = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        lblKhachConPhaiTra = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        lblConPhaiHoanChoKhach = new javax.swing.JLabel();
        btnHoanTien1 = new javax.swing.JButton();
        btnQuayLai1 = new javax.swing.JButton();
        btnChonKhuyenMai = new javax.swing.JButton();
        txtMaKhuyenMai = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        btnXoaKhuyenMai = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel87 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lblNameLogin1 = new javax.swing.JLabel();
        date1 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        pnlTableInit1 = new javax.swing.JPanel();
        menuScrollPane5 = new menuGui.MenuScrollPane();
        jTable3 = new javax.swing.JTable();
        jDialogChonDonTraHang = new javax.swing.JDialog(frm,"",false);
        pnlChonNCC1 = new javax.swing.JPanel();
        menuScrollPane10 = new menuGui.MenuScrollPane();
        tableDSHoaDon = new javax.swing.JTable();
        jPanel55 = new javax.swing.JPanel();
        jLabel149 = new javax.swing.JLabel();
        txtTimNCC1 = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnChonDonDoiHang = new javax.swing.JButton();
        pnlBotLeftChange = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        btnChonKH1 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        btnKhachLe1 = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        lblTongTienMua = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        lblSDTKH = new javax.swing.JLabel();
        lblTongDonMua = new javax.swing.JLabel();
        lblTenKH = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        lblMaKH = new javax.swing.JLabel();
        lblNhomKhachHang = new javax.swing.JLabel();
        lblMaKH2 = new javax.swing.JLabel();
        pnlSanPhamDaChonNotNull = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        menuScrollPane9 = new menuGui.MenuScrollPane();
        tableInForSP = new javax.swing.JTable();
        jDialogGhiSoLuongTra = new javax.swing.JDialog(frm,"Chọn",false);
        jPanel21 = new javax.swing.JPanel();
        jLabel136 = new javax.swing.JLabel();
        txtSoLuongSanPhamChon = new javax.swing.JTextField();
        btnXacNhanSoLuong = new javax.swing.JButton();
        lblTenSanPhamChon = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jDialogGhiSoLuongDoi = new javax.swing.JDialog(frm,"Chọn",false);
        jPanel28 = new javax.swing.JPanel();
        jLabel139 = new javax.swing.JLabel();
        txtSoLuongSanPhamChon1 = new javax.swing.JTextField();
        btnXacNhanSoLuong1 = new javax.swing.JButton();
        lblTenSanPhamChon1 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jDialogChonKhuyenMai = new javax.swing.JDialog(frm,"Chọn",false);
        pnlChonNCC3 = new javax.swing.JPanel();
        menuScrollPane16 = new menuGui.MenuScrollPane();
        tableChonKH1 = new javax.swing.JTable();
        jPanel58 = new javax.swing.JPanel();
        jLabel151 = new javax.swing.JLabel();
        txtTimNCC2 = new javax.swing.JTextField();
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
        btnXemDonTraHang = new javax.swing.JButton();
        pnlCenterKHchange = new javax.swing.JPanel();
        pnlTableInit = new javax.swing.JPanel();
        menuScrollPane2 = new menuGui.MenuScrollPane();
        tableDSDonTra = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblNameLogin = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        pnlCenterTra.setBackground(new java.awt.Color(153, 153, 153));
        pnlCenterTra.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));
        jPanel4.setLayout(new java.awt.BorderLayout());

        menuScrollPane13.setBorder(null);
        menuScrollPane13.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        menuScrollPane13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));

        jPanel9.setBackground(new java.awt.Color(240, 242, 245));

        pnlRight.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel12.setText("Thông tin hóa đơn");

        jPanel10.setBackground(new java.awt.Color(250, 250, 250));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel34.setText("Lí do trả hàng:");

        jTextAreaGhiChuTraHang.setColumns(20);
        jTextAreaGhiChuTraHang.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextAreaGhiChuTraHang.setRows(5);
        menuScrollPane7.setViewportView(jTextAreaGhiChuTraHang);

        jLabel135.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel135.setText("Mã hóa đơn trả: ");

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel52.setText("Tổng sản phẩm trả:");

        lblTongSoLuongSPTra.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTongSoLuongSPTra.setText("0");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel38.setText("Tổng tiền cần hoàn:");

        lblTongTienHoan.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTongTienHoan.setText("0.0");

        pnlSanPhamDaChon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlSanPhamDaChon.setLayout(new java.awt.BorderLayout());

        pnlSanPhamDaChonNull.setBackground(new java.awt.Color(255, 255, 255));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/box.png"))); // NOI18N

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel31.setText("Chưa có thông tin sản phẩm");

        javax.swing.GroupLayout pnlSanPhamDaChonNullLayout = new javax.swing.GroupLayout(pnlSanPhamDaChonNull);
        pnlSanPhamDaChonNull.setLayout(pnlSanPhamDaChonNullLayout);
        pnlSanPhamDaChonNullLayout.setHorizontalGroup(
            pnlSanPhamDaChonNullLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSanPhamDaChonNullLayout.createSequentialGroup()
                .addGroup(pnlSanPhamDaChonNullLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSanPhamDaChonNullLayout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addComponent(jLabel30))
                    .addGroup(pnlSanPhamDaChonNullLayout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(134, Short.MAX_VALUE))
        );
        pnlSanPhamDaChonNullLayout.setVerticalGroup(
            pnlSanPhamDaChonNullLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSanPhamDaChonNullLayout.createSequentialGroup()
                .addContainerGap(129, Short.MAX_VALUE)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(136, 136, 136))
        );

        pnlSanPhamDaChon.add(pnlSanPhamDaChonNull, java.awt.BorderLayout.CENTER);

        jLabel137.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel137.setText("Mã hóa đơn: ");

        lblMaHoaDonHoanTra.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblMaHoaDonHoanTra.setText("6546451646");

        lblMaHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblMaHoaDon.setText("6546451646");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel1.setText("Sản phẩm trả");

        jLabel54.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel54.setText("Tiền khuyến mãi:");

        jLabel55.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel55.setText("Mã khuyến mãi:");

        txtMaKhuyenMaiBanDau.setEditable(false);
        txtMaKhuyenMaiBanDau.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtTienKhuyenMaiBanDau.setEditable(false);
        txtTienKhuyenMaiBanDau.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTienKhuyenMaiBanDau.setText("0.0");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(menuScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel137, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblMaHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlSanPhamDaChon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel135, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblMaHoaDonHoanTra, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addGap(18, 18, 18)
                                .addComponent(lblTongTienHoan, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel52)
                                    .addComponent(jLabel54)
                                    .addComponent(jLabel55))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addComponent(lblTongSoLuongSPTra, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtMaKhuyenMaiBanDau)
                                    .addComponent(txtTienKhuyenMaiBanDau))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel137, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel135, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaHoaDonHoanTra, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34)
                    .addComponent(menuScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaKhuyenMaiBanDau, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTienKhuyenMaiBanDau, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTongSoLuongSPTra, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTongTienHoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlSanPhamDaChon, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnHoanTien.setBackground(new java.awt.Color(3, 136, 253));
        btnHoanTien.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHoanTien.setForeground(new java.awt.Color(255, 255, 255));
        btnHoanTien.setText("Hoàn tiền");
        btnHoanTien.setFocusable(false);
        btnHoanTien.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHoanTien.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnHoanTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoanTienActionPerformed(evt);
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

        javax.swing.GroupLayout pnlRightLayout = new javax.swing.GroupLayout(pnlRight);
        pnlRight.setLayout(pnlRightLayout);
        pnlRightLayout.setHorizontalGroup(
            pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRightLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnQuayLai, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnHoanTien, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlRightLayout.setVerticalGroup(
            pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHoanTien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnQuayLai))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlBotLeft.setBackground(new java.awt.Color(255, 255, 255));
        pnlBotLeft.setLayout(new java.awt.BorderLayout());

        pnlBotLeftReturn.setBackground(new java.awt.Color(255, 255, 255));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel7.setText("Thông tin khách hàng");

        btnKhachLe.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnKhachLe.setForeground(new java.awt.Color(3, 136, 253));
        btnKhachLe.setText("Khách lẻ");
        btnKhachLe.setEnabled(false);
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
        btnChonKH.setEnabled(false);
        btnChonKH.setFocusable(false);
        btnChonKH.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChonKH.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnChonKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonKHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 241, Short.MAX_VALUE)
                .addComponent(btnKhachLe, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnChonKH, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKhachLe)
                    .addComponent(btnChonKH))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.setBackground(new java.awt.Color(250, 250, 250));
        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/id-card.png"))); // NOI18N

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel33.setText("Chưa có thông tin khách hàng");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel33)
                .addGap(220, 220, 220))
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(326, 326, 326)
                .addComponent(jLabel32)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel32)
                .addGap(18, 18, 18)
                .addComponent(jLabel33)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlBotLeftReturnLayout = new javax.swing.GroupLayout(pnlBotLeftReturn);
        pnlBotLeftReturn.setLayout(pnlBotLeftReturnLayout);
        pnlBotLeftReturnLayout.setHorizontalGroup(
            pnlBotLeftReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotLeftReturnLayout.createSequentialGroup()
                .addGroup(pnlBotLeftReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBotLeftReturnLayout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlBotLeftReturnLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlBotLeftReturnLayout.setVerticalGroup(
            pnlBotLeftReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotLeftReturnLayout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlBotLeft.add(pnlBotLeftReturn, java.awt.BorderLayout.CENTER);

        pnlTopLeft3.setBackground(new java.awt.Color(204, 204, 255));
        pnlTopLeft3.setPreferredSize(new java.awt.Dimension(786, 496));
        pnlTopLeft3.setLayout(new java.awt.BorderLayout());

        pnlNonInFor.setBackground(new java.awt.Color(255, 255, 255));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel8.setText("Thông tin sản phẩm đã mua");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlNonInfo.setBackground(new java.awt.Color(250, 250, 250));
        pnlNonInfo.setLayout(new java.awt.BorderLayout());

        pnlChonNCC.setBackground(new java.awt.Color(250, 250, 250));
        pnlChonNCC.setLayout(new java.awt.BorderLayout());

        jPanel56.setBackground(new java.awt.Color(255, 255, 255));

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

        javax.swing.GroupLayout jPanel56Layout = new javax.swing.GroupLayout(jPanel56);
        jPanel56.setLayout(jPanel56Layout);
        jPanel56Layout.setHorizontalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel56Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel148, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(672, Short.MAX_VALUE))
            .addGroup(jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel56Layout.createSequentialGroup()
                    .addContainerGap(90, Short.MAX_VALUE)
                    .addComponent(txtTimSanPhamChon, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel56Layout.setVerticalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel56Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel148, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel56Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(txtTimSanPhamChon, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pnlChonNCC.add(jPanel56, java.awt.BorderLayout.PAGE_START);

        menuScrollPane12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menuScrollPane12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tableSanPhamDaBan.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tableSanPhamDaBan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableSanPhamDaBan.setGridColor(new java.awt.Color(255, 255, 255));
        tableSanPhamDaBan.setRowHeight(60);
        tableSanPhamDaBan.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableSanPhamDaBan.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableSanPhamDaBan.getTableHeader().setReorderingAllowed(false);
        tableSanPhamDaBan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableSanPhamDaBanMouseClicked(evt);
            }
        });
        menuScrollPane12.setViewportView(tableSanPhamDaBan);
        if (tableSanPhamDaBan.getColumnModel().getColumnCount() > 0) {
            tableSanPhamDaBan.getColumnModel().getColumn(0).setMinWidth(40);
            tableSanPhamDaBan.getColumnModel().getColumn(0).setMaxWidth(40);
            tableSanPhamDaBan.getColumnModel().getColumn(1).setMinWidth(150);
            tableSanPhamDaBan.getColumnModel().getColumn(1).setMaxWidth(150);
            tableSanPhamDaBan.getColumnModel().getColumn(3).setMinWidth(80);
            tableSanPhamDaBan.getColumnModel().getColumn(3).setMaxWidth(80);
            tableSanPhamDaBan.getColumnModel().getColumn(4).setMinWidth(80);
            tableSanPhamDaBan.getColumnModel().getColumn(4).setMaxWidth(80);
            tableSanPhamDaBan.getColumnModel().getColumn(5).setMinWidth(100);
            tableSanPhamDaBan.getColumnModel().getColumn(5).setMaxWidth(100);
        }

        pnlChonNCC.add(menuScrollPane12, java.awt.BorderLayout.CENTER);

        pnlNonInfo.add(pnlChonNCC, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout pnlNonInForLayout = new javax.swing.GroupLayout(pnlNonInFor);
        pnlNonInFor.setLayout(pnlNonInForLayout);
        pnlNonInForLayout.setHorizontalGroup(
            pnlNonInForLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlNonInForLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlNonInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlNonInForLayout.setVerticalGroup(
            pnlNonInForLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNonInForLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlNonInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlTopLeft3.add(pnlNonInFor, java.awt.BorderLayout.CENTER);

        jPanel6.setBackground(new java.awt.Color(240, 242, 245));

        pnlTopLeft2.setBackground(new java.awt.Color(204, 204, 255));
        pnlTopLeft2.setPreferredSize(new java.awt.Dimension(786, 496));
        pnlTopLeft2.setLayout(new java.awt.BorderLayout());

        pnlNonInFor1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel9.setText("Thông tin sản phẩm đổi");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlNonInfo1.setBackground(new java.awt.Color(250, 250, 250));
        pnlNonInfo1.setLayout(new java.awt.BorderLayout());

        pnlChonNCC2.setBackground(new java.awt.Color(250, 250, 250));
        pnlChonNCC2.setLayout(new java.awt.BorderLayout());

        jPanel57.setBackground(new java.awt.Color(255, 255, 255));

        jLabel150.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel150.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/magnifying-glass.png"))); // NOI18N

        txtTimSanPhamDoi.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimSanPhamDoi.setText("Nhập vào thông tin tìm kiếm...");
        txtTimSanPhamDoi.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimSanPhamDoi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimSanPhamDoiFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTimSanPhamDoiFocusLost(evt);
            }
        });
        txtTimSanPhamDoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimSanPhamDoiActionPerformed(evt);
            }
        });
        txtTimSanPhamDoi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimSanPhamDoiKeyReleased(evt);
            }
        });

        cboSortTabelChonSP.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cboSortTabelChonSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        cboSortTabelChonSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSortTabelChonSPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel57Layout = new javax.swing.GroupLayout(jPanel57);
        jPanel57.setLayout(jPanel57Layout);
        jPanel57Layout.setHorizontalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel57Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel150, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 521, Short.MAX_VALUE)
                .addComponent(cboSortTabelChonSP, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel57Layout.createSequentialGroup()
                    .addContainerGap(89, Short.MAX_VALUE)
                    .addComponent(txtTimSanPhamDoi, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(182, 182, 182)))
        );
        jPanel57Layout.setVerticalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel57Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel150, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(cboSortTabelChonSP))
                .addContainerGap())
            .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel57Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(txtTimSanPhamDoi, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        List<NhomSanPham> listNhomSP = dao_nsp.getAllNhomSanPham();
        for(NhomSanPham nsp : listNhomSP) {
            cboSortTabelChonSP.addItem(nsp.getTenNhomSanPham());
        }

        pnlChonNCC2.add(jPanel57, java.awt.BorderLayout.PAGE_START);

        menuScrollPane14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menuScrollPane14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tableChonSPDoiHang.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tableChonSPDoiHang.setModel(new javax.swing.table.DefaultTableModel(
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
        tableChonSPDoiHang.setGridColor(new java.awt.Color(255, 255, 255));
        tableChonSPDoiHang.setRowHeight(60);
        tableChonSPDoiHang.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableChonSPDoiHang.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableChonSPDoiHang.getTableHeader().setReorderingAllowed(false);
        tableChonSPDoiHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableChonSPDoiHangMouseClicked(evt);
            }
        });
        menuScrollPane14.setViewportView(tableChonSPDoiHang);
        if (tableChonSPDoiHang.getColumnModel().getColumnCount() > 0) {
            tableChonSPDoiHang.getColumnModel().getColumn(0).setMinWidth(40);
            tableChonSPDoiHang.getColumnModel().getColumn(0).setMaxWidth(40);
            tableChonSPDoiHang.getColumnModel().getColumn(1).setMinWidth(150);
            tableChonSPDoiHang.getColumnModel().getColumn(1).setMaxWidth(150);
            tableChonSPDoiHang.getColumnModel().getColumn(3).setMinWidth(80);
            tableChonSPDoiHang.getColumnModel().getColumn(3).setMaxWidth(80);
            tableChonSPDoiHang.getColumnModel().getColumn(4).setMinWidth(80);
            tableChonSPDoiHang.getColumnModel().getColumn(4).setMaxWidth(80);
            tableChonSPDoiHang.getColumnModel().getColumn(5).setMinWidth(0);
            tableChonSPDoiHang.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        pnlChonNCC2.add(menuScrollPane14, java.awt.BorderLayout.CENTER);

        pnlNonInfo1.add(pnlChonNCC2, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout pnlNonInFor1Layout = new javax.swing.GroupLayout(pnlNonInFor1);
        pnlNonInFor1.setLayout(pnlNonInFor1Layout);
        pnlNonInFor1Layout.setHorizontalGroup(
            pnlNonInFor1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlNonInFor1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlNonInfo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlNonInFor1Layout.setVerticalGroup(
            pnlNonInFor1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNonInFor1Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlNonInfo1, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlTopLeft2.add(pnlNonInFor1, java.awt.BorderLayout.CENTER);

        pnlSanPhamDaChonNotNull1.setBackground(new java.awt.Color(255, 255, 255));
        pnlSanPhamDaChonNotNull1.setPreferredSize(new java.awt.Dimension(475, 297));

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setLayout(new java.awt.BorderLayout());

        menuScrollPane15.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menuScrollPane15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tableInForSPDoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableInForSPDoi.setModel(new javax.swing.table.DefaultTableModel(
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
        tableInForSPDoi.setFocusable(false);
        tableInForSPDoi.setGridColor(new java.awt.Color(255, 255, 255));
        tableInForSPDoi.setRowHeight(50);
        tableInForSPDoi.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableInForSPDoi.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableInForSPDoi.getTableHeader().setReorderingAllowed(false);
        tableInForSPDoi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableInForSPDoiMouseClicked(evt);
            }
        });
        menuScrollPane15.setViewportView(tableInForSPDoi);
        if (tableInForSPDoi.getColumnModel().getColumnCount() > 0) {
            tableInForSPDoi.getColumnModel().getColumn(0).setMinWidth(0);
            tableInForSPDoi.getColumnModel().getColumn(0).setMaxWidth(0);
            tableInForSPDoi.getColumnModel().getColumn(1).setMinWidth(0);
            tableInForSPDoi.getColumnModel().getColumn(1).setMaxWidth(0);
            tableInForSPDoi.getColumnModel().getColumn(3).setMinWidth(0);
            tableInForSPDoi.getColumnModel().getColumn(3).setMaxWidth(0);
            tableInForSPDoi.getColumnModel().getColumn(4).setMinWidth(80);
            tableInForSPDoi.getColumnModel().getColumn(4).setMaxWidth(80);
            tableInForSPDoi.getColumnModel().getColumn(5).setMinWidth(80);
            tableInForSPDoi.getColumnModel().getColumn(5).setMaxWidth(80);
            tableInForSPDoi.getColumnModel().getColumn(6).setMinWidth(80);
            tableInForSPDoi.getColumnModel().getColumn(6).setMaxWidth(80);
        }

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 491, Short.MAX_VALUE)
            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(menuScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 415, Short.MAX_VALUE)
            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(menuScrollPane15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE))
        );

        jPanel24.add(jPanel25, java.awt.BorderLayout.CENTER);

        jLabel138.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel138.setText("Mã hóa đơn đổi: ");

        lblMaHoaDonDoiHang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblMaHoaDonDoiHang.setText("6546451646");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel4.setText("Sản phẩm đổi");

        javax.swing.GroupLayout pnlSanPhamDaChonNotNull1Layout = new javax.swing.GroupLayout(pnlSanPhamDaChonNotNull1);
        pnlSanPhamDaChonNotNull1.setLayout(pnlSanPhamDaChonNotNull1Layout);
        pnlSanPhamDaChonNotNull1Layout.setHorizontalGroup(
            pnlSanPhamDaChonNotNull1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSanPhamDaChonNotNull1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSanPhamDaChonNotNull1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlSanPhamDaChonNotNull1Layout.createSequentialGroup()
                        .addComponent(jLabel138)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMaHoaDonDoiHang, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4))
                .addContainerGap(8, Short.MAX_VALUE))
        );
        pnlSanPhamDaChonNotNull1Layout.setVerticalGroup(
            pnlSanPhamDaChonNotNull1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSanPhamDaChonNotNull1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSanPhamDaChonNotNull1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel138, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaHoaDonDoiHang))
                .addGap(31, 31, 31)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(pnlTopLeft2, javax.swing.GroupLayout.PREFERRED_SIZE, 757, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 530, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                    .addContainerGap(768, Short.MAX_VALUE)
                    .addComponent(pnlSanPhamDaChonNotNull1, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(14, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(pnlTopLeft2, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(pnlSanPhamDaChonNotNull1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addComponent(pnlBotLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(pnlRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jSeparator3))
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 1274, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                    .addContainerGap(15, Short.MAX_VALUE)
                    .addComponent(pnlTopLeft3, javax.swing.GroupLayout.PREFERRED_SIZE, 758, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(537, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlBotLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                    .addContainerGap(290, Short.MAX_VALUE)
                    .addComponent(pnlTopLeft3, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(560, Short.MAX_VALUE)))
        );

        jPanel26.setBackground(new java.awt.Color(240, 242, 245));

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel10.setText("Thông tin thanh toán");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel40.setText("Ghi chú đơn hàng:");

        jTextAreaGhiChuDoiHang.setColumns(20);
        jTextAreaGhiChuDoiHang.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextAreaGhiChuDoiHang.setRows(5);
        menuScrollPane8.setViewportView(jTextAreaGhiChuDoiHang);

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel60.setText("Mã khuyến mãi");

        txtTienKhuyenMai.setEditable(false);
        txtTienKhuyenMai.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTienKhuyenMai.setText("0.0");

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel59.setText("Chiết khấu:");

        txtTienChietKhau.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTienChietKhau.setText("0.0");
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

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel53.setText("Tổng số lượng sản phẩm:");

        lblTongSoLuongSanPhamDoi.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTongSoLuongSanPhamDoi.setText("0");

        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel41.setText("Tổng tiền cần thanh toán:");

        lblTongTienThanhToanDoi.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTongTienThanhToanDoi.setText("0.0");

        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel42.setText("Khách còn phải trả:");

        lblKhachConPhaiTra.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblKhachConPhaiTra.setText("0.0");

        jLabel43.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel43.setText("Tổng tiền:");

        lblTongTien.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTongTien.setText("0.0");

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel44.setText("Còn phải hoàn tiền cho khách:");

        lblConPhaiHoanChoKhach.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblConPhaiHoanChoKhach.setText("0.0");

        btnHoanTien1.setBackground(new java.awt.Color(3, 136, 253));
        btnHoanTien1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHoanTien1.setForeground(new java.awt.Color(255, 255, 255));
        btnHoanTien1.setText("Hoàn tiền");
        btnHoanTien1.setFocusable(false);
        btnHoanTien1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHoanTien1.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnHoanTien1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoanTien1ActionPerformed(evt);
            }
        });

        btnQuayLai1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnQuayLai1.setForeground(new java.awt.Color(3, 136, 253));
        btnQuayLai1.setText("Quay lại(ESC)");
        btnQuayLai1.setFocusable(false);
        btnQuayLai1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnQuayLai1.setMargin(new java.awt.Insets(2, 5, 3, 5));
        btnQuayLai1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuayLai1ActionPerformed(evt);
            }
        });

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

        jLabel61.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel61.setText("Tiền khuyến mãi:");

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

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGap(177, 177, 177)
                                .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel61))
                                .addGap(2, 2, 2)))
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGap(191, 191, 191)
                                .addComponent(btnChonKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTienChietKhau)
                                    .addGroup(jPanel27Layout.createSequentialGroup()
                                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtMaKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtTienKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnXoaKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52)
                                .addComponent(lblTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTongSoLuongSanPhamDoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblKhachConPhaiTra, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTongTienThanhToanDoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jLabel44)
                                .addGap(18, 18, 18)
                                .addComponent(lblConPhaiHoanChoKhach, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnQuayLai1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnHoanTien1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel27Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(menuScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(940, Short.MAX_VALUE)))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(15, 15, 15)
                        .addComponent(jLabel40)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel43)
                            .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel60)
                            .addComponent(btnChonKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMaKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel53)
                                    .addComponent(lblTongSoLuongSanPhamDoi, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTienKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel61)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnXoaKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel41)
                                    .addComponent(lblTongTienThanhToanDoi, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE))
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTienChietKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel59))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblKhachConPhaiTra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                            .addComponent(lblConPhaiHoanChoKhach, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnHoanTien1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnQuayLai1))
                        .addGap(9, 9, 9))))
            .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                    .addGap(76, 76, 76)
                    .addComponent(menuScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 379, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 1329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        menuScrollPane13.setViewportView(jPanel5);

        jPanel4.add(menuScrollPane13, java.awt.BorderLayout.CENTER);

        pnlCenterTra.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel7.setBackground(new java.awt.Color(250, 250, 250));
        jPanel7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel7.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel87.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel87.setText("Danh sách đơn trả hàng");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel22.setText("> Đơn trả hàng");

        lblNameLogin1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblNameLogin1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameLogin1.setText("Nguyễn Châu Tình ");
        lblNameLogin1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblNameLogin1AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        date1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/icons8-user-50.png"))); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel87)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1030, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(date1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNameLogin1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lblNameLogin1)
                                .addGap(2, 2, 2)
                                .addComponent(date1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel87, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                        .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCenterTra.add(jPanel7, java.awt.BorderLayout.PAGE_START);

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
                "STT", "Mã hóa đơn", "Mã đơn trả", "Ngày tạo", "Tên khách hàng", "Trạng thái đơn hàng", "Tổng tiền hoàn"
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
        menuScrollPane5.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        javax.swing.GroupLayout pnlTableInit1Layout = new javax.swing.GroupLayout(pnlTableInit1);
        pnlTableInit1.setLayout(pnlTableInit1Layout);
        pnlTableInit1Layout.setHorizontalGroup(
            pnlTableInit1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableInit1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(menuScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        pnlTableInit1Layout.setVerticalGroup(
            pnlTableInit1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableInit1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(menuScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jDialogChonDonTraHang.setBackground(new java.awt.Color(250, 250, 250));
        jDialogChonDonTraHang.setResizable(false);
        jDialogChonDonTraHang.setSize(new java.awt.Dimension(1163, 649));
        jDialogChonDonTraHang.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                jDialogChonDonTraHangWindowLostFocus(evt);
            }
        });

        pnlChonNCC1.setBackground(new java.awt.Color(250, 250, 250));
        pnlChonNCC1.setLayout(new java.awt.BorderLayout());

        menuScrollPane10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menuScrollPane10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

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
                "STT", "Mã hóa đơn", "Ngày tạo đơn", "Tên khách hàng", "Nhân viên", "Tổng tiền"
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
        tableDSHoaDon.setShowHorizontalLines(true);
        tableDSHoaDon.getTableHeader().setReorderingAllowed(false);
        tableDSHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDSHoaDonMouseClicked(evt);
            }
        });
        menuScrollPane10.setViewportView(tableDSHoaDon);
        if (tableDSHoaDon.getColumnModel().getColumnCount() > 0) {
            tableDSHoaDon.getColumnModel().getColumn(0).setMinWidth(40);
            tableDSHoaDon.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        pnlChonNCC1.add(menuScrollPane10, java.awt.BorderLayout.CENTER);

        jPanel55.setBackground(new java.awt.Color(255, 255, 255));

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

        jLabel75.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel75.setText("Chọn đơn hàng để trả");

        btnChonDonDoiHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnChonDonDoiHang.setForeground(new java.awt.Color(3, 136, 253));
        btnChonDonDoiHang.setText("Trả hàng");
        btnChonDonDoiHang.setFocusable(false);
        btnChonDonDoiHang.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChonDonDoiHang.setMargin(new java.awt.Insets(2, 5, 3, 5));
        btnChonDonDoiHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonDonDoiHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel55Layout = new javax.swing.GroupLayout(jPanel55);
        jPanel55.setLayout(jPanel55Layout);
        jPanel55Layout.setHorizontalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel55Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel55Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel75)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel55Layout.createSequentialGroup()
                        .addComponent(jLabel149, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(txtTimNCC1, javax.swing.GroupLayout.PREFERRED_SIZE, 885, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnChonDonDoiHang, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)))
                .addContainerGap())
        );
        jPanel55Layout.setVerticalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel55Layout.createSequentialGroup()
                .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtTimNCC1)
                        .addComponent(jLabel149, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
                    .addComponent(btnChonDonDoiHang, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        pnlChonNCC1.add(jPanel55, java.awt.BorderLayout.PAGE_START);

        jDialogChonDonTraHang.getContentPane().add(pnlChonNCC1, java.awt.BorderLayout.CENTER);

        pnlBotLeftChange.setBackground(new java.awt.Color(255, 255, 255));
        pnlBotLeftChange.setLayout(new java.awt.BorderLayout());

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setPreferredSize(new java.awt.Dimension(756, 268));

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));

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

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                .addComponent(btnKhachLe1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnChonKH1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnChonKH1)
                        .addComponent(btnKhachLe1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel20.setBackground(new java.awt.Color(250, 250, 250));
        jPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblTongTienMua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTongTienMua.setText("500000000");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel23.setText("Số điện thoại:                 ");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel35.setText("Tổng số đơn hàng:        ");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel36.setText("Mã khách hàng:");

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel37.setText("Họ và tên: ");

        lblSDTKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSDTKH.setText("0965180325");

        lblTongDonMua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTongDonMua.setText("20");

        lblTenKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTenKH.setText("Nguyễn Châu Tình");

        jLabel39.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel39.setText("Tổng số tiền đã mua: ");

        lblMaKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblMaKH.setText("500000000");

        lblNhomKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNhomKhachHang.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNhomKhachHang.setText("Nhóm khách hàng:");

        lblMaKH2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblMaKH2.setText("Nhóm khách hàng:");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                        .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblMaKH2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSDTKH, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTongDonMua, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTongTienMua, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNhomKhachHang))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(lblTenKH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(lblSDTKH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(lblTongDonMua))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(lblTongTienMua))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(lblMaKH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaKH2)
                    .addComponent(lblNhomKhachHang))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlBotLeftChange.add(jPanel17, java.awt.BorderLayout.CENTER);

        pnlSanPhamDaChonNotNull.setBackground(new java.awt.Color(204, 204, 255));
        pnlSanPhamDaChonNotNull.setPreferredSize(new java.awt.Dimension(475, 297));
        pnlSanPhamDaChonNotNull.setLayout(new java.awt.BorderLayout());

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setLayout(new java.awt.BorderLayout());

        menuScrollPane9.setBorder(null);
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
                false, false, false, false, true, false, true
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
            tableInForSP.getColumnModel().getColumn(3).setMaxWidth(0);
            tableInForSP.getColumnModel().getColumn(4).setMinWidth(70);
            tableInForSP.getColumnModel().getColumn(4).setMaxWidth(70);
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

        jDialogGhiSoLuongTra.setBackground(new java.awt.Color(250, 250, 250));
        jDialogGhiSoLuongTra.setUndecorated(true);
        jDialogGhiSoLuongTra.setResizable(false);
        jDialogGhiSoLuongTra.setSize(new java.awt.Dimension(800, 300));
        jDialogGhiSoLuongTra.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                jDialogGhiSoLuongTraWindowLostFocus(evt);
            }
        });

        jPanel21.setBackground(new java.awt.Color(250, 250, 250));
        jPanel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel21.setPreferredSize(new java.awt.Dimension(440, 200));

        jLabel136.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel136.setText("Số lượng:");

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
                btnXacNhanSoLuongActionPerformed(evt);
            }
        });

        lblTenSanPhamChon.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTenSanPhamChon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTenSanPhamChon.setText(" ");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTenSanPhamChon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jSeparator2)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel136)
                        .addGap(46, 46, 46)
                        .addComponent(txtSoLuongSanPhamChon, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(240, 240, 240))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnXacNhanSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblTenSanPhamChon, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel136, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoLuongSanPhamChon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                .addComponent(btnXacNhanSoLuong)
                .addGap(26, 26, 26))
        );

        jDialogGhiSoLuongTra.getContentPane().add(jPanel21, java.awt.BorderLayout.CENTER);

        jDialogGhiSoLuongDoi.setBackground(new java.awt.Color(250, 250, 250));
        jDialogGhiSoLuongDoi.setUndecorated(true);
        jDialogGhiSoLuongDoi.setResizable(false);
        jDialogGhiSoLuongDoi.setSize(new java.awt.Dimension(800, 300));
        jDialogGhiSoLuongDoi.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                jDialogGhiSoLuongDoiWindowLostFocus(evt);
            }
        });

        jPanel28.setBackground(new java.awt.Color(250, 250, 250));
        jPanel28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel28.setPreferredSize(new java.awt.Dimension(440, 200));

        jLabel139.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel139.setText("Số lượng:");

        txtSoLuongSanPhamChon1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSoLuongSanPhamChon1.setText("1");
        txtSoLuongSanPhamChon1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSoLuongSanPhamChon1KeyReleased(evt);
            }
        });

        btnXacNhanSoLuong1.setBackground(new java.awt.Color(3, 136, 253));
        btnXacNhanSoLuong1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXacNhanSoLuong1.setForeground(new java.awt.Color(255, 255, 255));
        btnXacNhanSoLuong1.setText("Xác nhận");
        btnXacNhanSoLuong1.setFocusable(false);
        btnXacNhanSoLuong1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnXacNhanSoLuong1.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnXacNhanSoLuong1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanSoLuong1ActionPerformed(evt);
            }
        });

        lblTenSanPhamChon1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTenSanPhamChon1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTenSanPhamChon1.setText(" ");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTenSanPhamChon1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jSeparator5)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel139)
                        .addGap(46, 46, 46)
                        .addComponent(txtSoLuongSanPhamChon1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(240, 240, 240))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnXacNhanSoLuong1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblTenSanPhamChon1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel139, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoLuongSanPhamChon1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                .addComponent(btnXacNhanSoLuong1)
                .addGap(26, 26, 26))
        );

        jDialogGhiSoLuongDoi.getContentPane().add(jPanel28, java.awt.BorderLayout.CENTER);

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

        pnlChonNCC3.setBackground(new java.awt.Color(250, 250, 250));
        pnlChonNCC3.setLayout(new java.awt.BorderLayout());

        menuScrollPane16.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menuScrollPane16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

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
                "Mã khuyến mãi", "Tên khuyến mãi", "Ghi chú", "Tỉ lệ", "Tiền tối thiểu", "Khuyến mãi tối đa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true
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
        menuScrollPane16.setViewportView(tableChonKH1);

        pnlChonNCC3.add(menuScrollPane16, java.awt.BorderLayout.CENTER);

        jPanel58.setBackground(new java.awt.Color(255, 255, 255));

        jLabel151.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel151.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/magnifying-glass.png"))); // NOI18N

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

        javax.swing.GroupLayout jPanel58Layout = new javax.swing.GroupLayout(jPanel58);
        jPanel58.setLayout(jPanel58Layout);
        jPanel58Layout.setHorizontalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel58Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel151, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(847, Short.MAX_VALUE))
            .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel58Layout.createSequentialGroup()
                    .addGap(87, 87, 87)
                    .addComponent(txtTimNCC2, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel58Layout.setVerticalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel58Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel151, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel58Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(txtTimNCC2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pnlChonNCC3.add(jPanel58, java.awt.BorderLayout.PAGE_START);

        jDialogChonKhuyenMai.getContentPane().add(pnlChonNCC3, java.awt.BorderLayout.CENTER);

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

        btnXemDonTraHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXemDonTraHang.setForeground(new java.awt.Color(3, 136, 253));
        btnXemDonTraHang.setText("Xem");
        btnXemDonTraHang.setFocusable(false);
        btnXemDonTraHang.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnXemDonTraHang.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnXemDonTraHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXemDonTraHangActionPerformed(evt);
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
                .addGap(18, 18, 18)
                .addComponent(btnXemDonTraHang, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTimKH, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                            .addComponent(btnXemDonTraHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCenterKHchange.setBackground(new java.awt.Color(250, 250, 250));
        pnlCenterKHchange.setLayout(new java.awt.BorderLayout());

        pnlTableInit.setBackground(new java.awt.Color(250, 250, 250));

        tableDSDonTra.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tableDSDonTra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã hóa đơn", "Mã đơn trả", "Mã hóa đơn đổi", "Ngày hoàn", "Tên khách hàng", "Trạng thái", "Tổng tiền hoàn"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableDSDonTra.setRowHeight(60);
        tableDSDonTra.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableDSDonTra.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableDSDonTra.setShowHorizontalLines(true);
        tableDSDonTra.getTableHeader().setReorderingAllowed(false);
        tableDSDonTra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDSDonTraMouseClicked(evt);
            }
        });
        menuScrollPane2.setViewportView(tableDSDonTra);
        if (tableDSDonTra.getColumnModel().getColumnCount() > 0) {
            tableDSDonTra.getColumnModel().getColumn(0).setMaxWidth(40);
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
                .addComponent(menuScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
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
                .addComponent(pnlCenterKHchange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        pnlTopLeft.add(jPanel12, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/up-arrow.png"))); // NOI18N
        jLabel2.setText("Xuất file");

        jButton9.setBackground(new java.awt.Color(3, 136, 253));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Tạo đơn trả hàng");
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setMargin(new java.awt.Insets(2, 10, 3, 10));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
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
        jLabel6.setText("Danh sách đơn trả hàng");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1148, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNameLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
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

    private void btnXemDonTraHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXemDonTraHangActionPerformed
        // TODO add your handling code here:
        if (tableDSDonTra.getSelectedRow() == -1) {

        } else {
//            loadChonSP();
            int row = tableDSDonTra.getSelectedRow();
            String trangThai = (String) tableDSDonTra.getValueAt(row, 6);
            String ma = (String) tableDSDonTra.getValueAt(row, 3);
            try {
            	if (trangThai.equalsIgnoreCase("Đã hoàn tiền")) {
                    if (!ma.startsWith("H")) {
                        lblMaHoaDonDoiHang.setText(createMaHoaDonDoiHang());
                    }
                    loadSanPhamTraTuDSDonTra();
                    loadSanPhamDaTra();
                    loadChonSPDoiHang();
                    loadDataSanPhamDoiHang();
                    changePanelDaHoanTien();

                } else {
                    changePanelChuaThanhToan();
                }
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
            
        }
    }//GEN-LAST:event_btnXemDonTraHangActionPerformed

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
        txtTimKH.selectAll();
    }//GEN-LAST:event_txtTimKHActionPerformed

    private void tableDSDonTraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDSDonTraMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
//            showPanelChange(pnlChange, pnlCenterXem);
        }
    }//GEN-LAST:event_tableDSDonTraMouseClicked

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        try {
			loadDataHD();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        jDialogChonDonTraHang.setVisible(true);
        jDialogChonDonTraHang.setLocationRelativeTo(null);
//        showPanelChange(pnlChange, pnlCenterTra);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable3MouseClicked

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

    private void jDialogChonDonTraHangWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialogChonDonTraHangWindowLostFocus
        // TODO add your handling code here:
        jDialogChonDonTraHang.setVisible(false);
    }//GEN-LAST:event_jDialogChonDonTraHangWindowLostFocus

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

    private void btnChonDonDoiHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonDonDoiHangActionPerformed
        // TODO add your handling code here:
    	try {
    		if (tableDSHoaDon.getSelectedRow() == -1) {
            } else {
                jDialogChonDonTraHang.setVisible(false);
                loadChonSP();
                loadChonSPDoiHang();
                loadSanPhamTra();
                changePanelChuaThanhToan();
                lblMaHoaDonDoiHang.setText(createMaHoaDonDoiHang());

            }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	
        
    }//GEN-LAST:event_btnChonDonDoiHangActionPerformed

    private void btnHoanTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoanTienActionPerformed
        // TODO add your handling code here:
        try {
			hoanTienSanPhamTra();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_btnHoanTienActionPerformed

    private void btnQuayLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuayLaiActionPerformed
        // TODO add your handling code here:
        DefaultTableModel md = (DefaultTableModel) tableInForSP.getModel();
        DefaultTableModel md1 = (DefaultTableModel) tableSanPhamDaBan.getModel();
        md.getDataVector().removeAllElements();
        md.fireTableDataChanged();
        md1.getDataVector().removeAllElements();
        md1.fireTableDataChanged();
        lamMoiHoanTra();
        showPanelChange(pnlChange, pnlCenter);
    }//GEN-LAST:event_btnQuayLaiActionPerformed

    private void btnKhachLeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhachLeActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlBotLeft, pnlBotLeftReturn);
//        lblTenKH.setText("");
//        lblMaKH.setText("");
    }//GEN-LAST:event_btnKhachLeActionPerformed

    private void btnChonKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonKHActionPerformed
        // TODO add your handling code here:
//        loadChonKH();
//        jDialogChonKH.setLocationRelativeTo(null);
//        jDialogChonKH.setVisible(true);
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
        timTableSPdaBan();
    }//GEN-LAST:event_txtTimSanPhamChonKeyReleased

    private void tableSanPhamDaBanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSanPhamDaBanMouseClicked
        // TODO add your handling code here:
        //        if (evt.getClickCount() == 1 && !evt.isConsumed()) {
        //            evt.consume();
        //            int row = tableChonSP.getSelectedRow();
        //            lblTenSanPhamChon.setText((String) tableChonSP.getValueAt(row, 2));
        //            jDialogGhiSoLuong.setLocationRelativeTo(null);
        //            jDialogGhiSoLuong.setVisible(true);
        //        }
        if (tableSanPhamDaBan.getSelectedRow() == -1) {

        } else {
            int row = tableSanPhamDaBan.getSelectedRow();
            String maSP = (String) tableSanPhamDaBan.getValueAt(row, 1);
            String ma = "";
            boolean value = false;
            for (int i = 0; i < tableInForSP.getRowCount(); i++) {
                ma = (String) tableInForSP.getValueAt(i, 1);
                if (ma.equalsIgnoreCase(maSP)) {
                    value = true;
                }
            }
            if (value == false) {
                lblTenSanPhamChon.setText((String) tableSanPhamDaBan.getValueAt(row, 2));
                jDialogGhiSoLuongTra.setLocationRelativeTo(null);
                jDialogGhiSoLuongTra.setVisible(true);
            }

        }
    }//GEN-LAST:event_tableSanPhamDaBanMouseClicked

    private void btnChonKH1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonKH1ActionPerformed
        // TODO add your handling code here:
//        loadChonKH();
//        jDialogChonKH.setLocationRelativeTo(null);
//        jDialogChonKH.setVisible(true);
    }//GEN-LAST:event_btnChonKH1ActionPerformed

    private void btnKhachLe1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhachLe1ActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlBotLeft, pnlBotLeftReturn);
        lblTenKH.setText("");
        lblMaKH.setText("");
    }//GEN-LAST:event_btnKhachLe1ActionPerformed

    private void tableInForSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableInForSPMouseClicked
        // TODO add your handling code here:
        if (tableInForSP.getSelectedRow() == -1) {

        } else {
            tableAction();
        }
    }//GEN-LAST:event_tableInForSPMouseClicked

    private void btnXacNhanSoLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanSoLuongActionPerformed
        // TODO add your handling code here:
    	try {
    		if (!txtSoLuongSanPhamChon.getText().equalsIgnoreCase("")) {
                int row = tableSanPhamDaBan.getSelectedRow();
                DefaultTableModel modelChonSP = (DefaultTableModel) tableSanPhamDaBan.getModel();
                String maSP = modelChonSP.getValueAt(row, 1).toString();
                int soLuong = (int) tableSanPhamDaBan.getValueAt(row, 4);

                //**********
                DefaultTableModel modelInfo = (DefaultTableModel) tableInForSP.getModel();
                DecimalFormat df = new DecimalFormat("#,##0");
                int sl = Integer.parseInt(txtSoLuongSanPhamChon.getText());
                if (sl >= soLuong) {
                    sl = soLuong;
                }
                if (maSP.startsWith("S")) {
                    Sach s = dao_sach.getSachtheoMa(maSP);
                    modelInfo.addRow(new Object[]{sttSP, s.getMaSanPham(), s.getTenSanPham(),
                        s.getDonGiaBan(), sl, tinhThanhTien(sl, s.getDonGiaBan())});
                    sttSP++;
                } else {
                    VanPhongPham vpp = dao_vpp.getVPPtheoMa(maSP);
                    modelInfo.addRow(new Object[]{sttSP, vpp.getMaSanPham(), vpp.getTenSanPham(),
                        vpp.getDonGiaBan(), sl, tinhThanhTien(sl, vpp.getDonGiaBan())});
                    sttSP++;
                }

                createInit();
                createInit1();
                showPanelChange(pnlSanPhamDaChon, pnlSanPhamDaChonNotNull);
                tableAction();
            }
            jDialogGhiSoLuongTra.setVisible(false);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	
        
    }//GEN-LAST:event_btnXacNhanSoLuongActionPerformed

    private void jDialogGhiSoLuongTraWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialogGhiSoLuongTraWindowLostFocus
        // TODO add your handling code here:
        jDialogGhiSoLuongTra.setVisible(false);
    }//GEN-LAST:event_jDialogGhiSoLuongTraWindowLostFocus

    private void txtTimSanPhamDoiFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimSanPhamDoiFocusGained
        // TODO add your handling code here:
        frm.placeHoderTextGianed(txtTimSanPhamDoi);
        //        tableChooseProductactions();
    }//GEN-LAST:event_txtTimSanPhamDoiFocusGained

    private void txtTimSanPhamDoiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimSanPhamDoiFocusLost
        // TODO add your handling code here:
        frm.placeHoderTextLost(txtTimSanPhamDoi);
    }//GEN-LAST:event_txtTimSanPhamDoiFocusLost

    private void txtTimSanPhamDoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimSanPhamDoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimSanPhamDoiActionPerformed

    private void txtTimSanPhamDoiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimSanPhamDoiKeyReleased
        timTableSPdoiHang();
    }//GEN-LAST:event_txtTimSanPhamDoiKeyReleased

    private void tableChonSPDoiHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableChonSPDoiHangMouseClicked
        if (tableChonSPDoiHang.getSelectedRow() == -1) {

        } else {
            int row = tableChonSPDoiHang.getSelectedRow();
            String maSP = (String) tableChonSPDoiHang.getValueAt(row, 1);
            String ma = "";
            boolean value = false;
            for (int i = 0; i < tableInForSPDoi.getRowCount(); i++) {
                ma = (String) tableInForSPDoi.getValueAt(i, 1);
                if (ma.equalsIgnoreCase(maSP)) {
                    value = true;
                }
            }
            if (value == false) {
                lblTenSanPhamChon1.setText((String) tableChonSPDoiHang.getValueAt(row, 2));
                jDialogGhiSoLuongDoi.setLocationRelativeTo(null);
                jDialogGhiSoLuongDoi.setVisible(true);
            }

        }
    }//GEN-LAST:event_tableChonSPDoiHangMouseClicked

    private void tableInForSPDoiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableInForSPDoiMouseClicked
        // TODO add your handling code here:
        if (tableInForSP.getSelectedRow() == -1) {

        } else {
            //            tableAction();
        }
    }//GEN-LAST:event_tableInForSPDoiMouseClicked

    private void cboSortTabelChonSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSortTabelChonSPActionPerformed
        // TODO add your handling code here:
        try {
			sortTableChooseProDuct();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_cboSortTabelChonSPActionPerformed

    private void btnXacNhanSoLuong1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanSoLuong1ActionPerformed
        // TODO add your handling code here:
    	try {
    		if (!txtSoLuongSanPhamChon1.getText().equalsIgnoreCase("")) {
                int row = tableChonSPDoiHang.getSelectedRow();
                DefaultTableModel modelChonSP = (DefaultTableModel) tableChonSPDoiHang.getModel();
                String maSP = modelChonSP.getValueAt(row, 1).toString();

                //**********
                DefaultTableModel modelInfo = (DefaultTableModel) tableInForSPDoi.getModel();
                DecimalFormat df = new DecimalFormat("#,##0");
                int sl = Integer.parseInt(txtSoLuongSanPhamChon1.getText());
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
                createInit1();
                tableAction1();
            }
//            showPanelChange(pnlSanPhamDaChon, pnlSanPhamDaChonNotNull);
            jDialogGhiSoLuongDoi.setVisible(false);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	
        
    }//GEN-LAST:event_btnXacNhanSoLuong1ActionPerformed

    private void jDialogGhiSoLuongDoiWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialogGhiSoLuongDoiWindowLostFocus
        // TODO add your handling code here:
        jDialogGhiSoLuongDoi.setVisible(false);
    }//GEN-LAST:event_jDialogGhiSoLuongDoiWindowLostFocus

    private void txtTienChietKhauKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienChietKhauKeyReleased
        // TODO add your handling code here:
        if (txtTienChietKhau.getText().equals("")) {
            txtTienChietKhau.setText("");
        }
        
        try {
        	createInit();
			createInit1();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_txtTienChietKhauKeyReleased

    private void txtTimKHKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKHKeyReleased
        timTableDSDonTra();
    }//GEN-LAST:event_txtTimKHKeyReleased

    private void btnHoanTien1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoanTien1ActionPerformed
        // TODO add your handling code here:
        try {
			hoanTienSanPhamTra();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//GEN-LAST:event_btnHoanTien1ActionPerformed

    private void btnQuayLai1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuayLai1ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel md = (DefaultTableModel) tableInForSP.getModel();
        DefaultTableModel md1 = (DefaultTableModel) tableSanPhamDaBan.getModel();
        md.getDataVector().removeAllElements();
        md.fireTableDataChanged();
        md1.getDataVector().removeAllElements();
        md1.fireTableDataChanged();
        lamMoiHoanTra();
        showPanelChange(pnlChange, pnlCenter);
    }//GEN-LAST:event_btnQuayLai1ActionPerformed

    private void txtSoLuongSanPhamChonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoLuongSanPhamChonKeyReleased
        // TODO add your handling code here:
        if (txtSoLuongSanPhamChon.getText().equals("")) {
            txtSoLuongSanPhamChon.setText("");
        }
    }//GEN-LAST:event_txtSoLuongSanPhamChonKeyReleased

    private void txtSoLuongSanPhamChon1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoLuongSanPhamChon1KeyReleased
        // TODO add your handling code here:
        if (txtSoLuongSanPhamChon1.getText().equals("")) {
            txtSoLuongSanPhamChon1.setText("");
        }
    }//GEN-LAST:event_txtSoLuongSanPhamChon1KeyReleased

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
			createInit1();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        jDialogChonKhuyenMai.setVisible(false);
    }//GEN-LAST:event_tableChonKH1MouseClicked

    private void txtTimNCC2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimNCC2FocusGained
        // TODO add your handling code here:
        frm.placeHoderTextGianed(txtTimNCC2);
    }//GEN-LAST:event_txtTimNCC2FocusGained

    private void txtTimNCC2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimNCC2FocusLost
        // TODO add your handling code here:
        frm.placeHoderTextLost(txtTimNCC2);
    }//GEN-LAST:event_txtTimNCC2FocusLost

    private void txtTimNCC2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimNCC2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimNCC2ActionPerformed

    private void jDialogChonKhuyenMaiWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialogChonKhuyenMaiWindowLostFocus
        // TODO add your handling code here:
        jDialogChonKhuyenMai.setVisible(false);
    }//GEN-LAST:event_jDialogChonKhuyenMaiWindowLostFocus

    private void lblNameLogin1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblNameLogin1AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_lblNameLogin1AncestorAdded

    private void btnXoaKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaKhuyenMaiActionPerformed
        // TODO add your handling code here:
        txtMaKhuyenMai.setText("");
        try {
			createInit();
			createInit1();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }//GEN-LAST:event_btnXoaKhuyenMaiActionPerformed

    private void txtTienChietKhauFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTienChietKhauFocusLost
        // TODO add your handling code here:
        if (txtTienChietKhau.getText().equals("")) {
            txtTienChietKhau.setText("0.0");
            if (Double.parseDouble(txtTienChietKhau.getText()) < 0) {
                txtTienChietKhau.setText("0.0");
            }
        }
    }//GEN-LAST:event_txtTienChietKhauFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChonDonDoiHang;
    private javax.swing.JButton btnChonKH;
    private javax.swing.JButton btnChonKH1;
    private javax.swing.JButton btnChonKhuyenMai;
    private javax.swing.JButton btnHoanTien;
    private javax.swing.JButton btnHoanTien1;
    private javax.swing.JButton btnKhachLe;
    private javax.swing.JButton btnKhachLe1;
    private javax.swing.JButton btnQuayLai;
    private javax.swing.JButton btnQuayLai1;
    private javax.swing.JButton btnXacNhanSoLuong;
    private javax.swing.JButton btnXacNhanSoLuong1;
    private javax.swing.JButton btnXemDonTraHang;
    private javax.swing.JButton btnXoaKhuyenMai;
    private javax.swing.JComboBox<String> cboSortTabelChonSP;
    private javax.swing.JLabel date;
    private javax.swing.JLabel date1;
    private javax.swing.JButton jButton9;
    private javax.swing.JDialog jDialogChonDonTraHang;
    private javax.swing.JDialog jDialogChonKhuyenMai;
    private javax.swing.JDialog jDialogGhiSoLuongDoi;
    private javax.swing.JDialog jDialogGhiSoLuongTra;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel87;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextArea jTextAreaGhiChuDoiHang;
    private javax.swing.JTextArea jTextAreaGhiChuTraHang;
    private javax.swing.JLabel lblConPhaiHoanChoKhach;
    private javax.swing.JLabel lblKhachConPhaiTra;
    private javax.swing.JLabel lblMaHoaDon;
    private javax.swing.JLabel lblMaHoaDonDoiHang;
    private javax.swing.JLabel lblMaHoaDonHoanTra;
    private javax.swing.JLabel lblMaKH;
    private javax.swing.JLabel lblMaKH2;
    private javax.swing.JLabel lblNameLogin;
    private javax.swing.JLabel lblNameLogin1;
    private javax.swing.JLabel lblNhomKhachHang;
    private javax.swing.JLabel lblSDTKH;
    private javax.swing.JLabel lblTenKH;
    private javax.swing.JLabel lblTenSanPhamChon;
    private javax.swing.JLabel lblTenSanPhamChon1;
    private javax.swing.JLabel lblTongDonMua;
    private javax.swing.JLabel lblTongSoLuongSPTra;
    private javax.swing.JLabel lblTongSoLuongSanPhamDoi;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JLabel lblTongTienHoan;
    private javax.swing.JLabel lblTongTienMua;
    private javax.swing.JLabel lblTongTienThanhToanDoi;
    private menuGui.MenuScrollPane menuScrollPane10;
    private menuGui.MenuScrollPane menuScrollPane12;
    private menuGui.MenuScrollPane menuScrollPane13;
    private menuGui.MenuScrollPane menuScrollPane14;
    private menuGui.MenuScrollPane menuScrollPane15;
    private menuGui.MenuScrollPane menuScrollPane16;
    private menuGui.MenuScrollPane menuScrollPane2;
    private menuGui.MenuScrollPane menuScrollPane5;
    private menuGui.MenuScrollPane menuScrollPane7;
    private menuGui.MenuScrollPane menuScrollPane8;
    private menuGui.MenuScrollPane menuScrollPane9;
    private javax.swing.JPanel pnlBotLeft;
    private javax.swing.JPanel pnlBotLeftChange;
    private javax.swing.JPanel pnlBotLeftReturn;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlCenterKHchange;
    private javax.swing.JPanel pnlCenterTra;
    private javax.swing.JPanel pnlChange;
    private javax.swing.JPanel pnlChonNCC;
    private javax.swing.JPanel pnlChonNCC1;
    private javax.swing.JPanel pnlChonNCC2;
    private javax.swing.JPanel pnlChonNCC3;
    private javax.swing.JPanel pnlNonInFor;
    private javax.swing.JPanel pnlNonInFor1;
    private javax.swing.JPanel pnlNonInfo;
    private javax.swing.JPanel pnlNonInfo1;
    private javax.swing.JPanel pnlRight;
    private javax.swing.JPanel pnlSanPhamDaChon;
    private javax.swing.JPanel pnlSanPhamDaChonNotNull;
    private javax.swing.JPanel pnlSanPhamDaChonNotNull1;
    private javax.swing.JPanel pnlSanPhamDaChonNull;
    private javax.swing.JPanel pnlTableInit;
    private javax.swing.JPanel pnlTableInit1;
    private javax.swing.JPanel pnlTopLeft;
    private javax.swing.JPanel pnlTopLeft2;
    private javax.swing.JPanel pnlTopLeft3;
    private javax.swing.JTable tableChonKH1;
    private javax.swing.JTable tableChonSPDoiHang;
    private javax.swing.JTable tableDSDonTra;
    private javax.swing.JTable tableDSHoaDon;
    private javax.swing.JTable tableInForSP;
    private javax.swing.JTable tableInForSPDoi;
    private javax.swing.JTable tableSanPhamDaBan;
    private javax.swing.JTextField txtMaKhuyenMai;
    private javax.swing.JTextField txtMaKhuyenMaiBanDau;
    private javax.swing.JTextField txtSoLuongSanPhamChon;
    private javax.swing.JTextField txtSoLuongSanPhamChon1;
    private javax.swing.JTextField txtTienChietKhau;
    private javax.swing.JTextField txtTienKhuyenMai;
    private javax.swing.JTextField txtTienKhuyenMaiBanDau;
    private javax.swing.JTextField txtTimKH;
    public javax.swing.JTextField txtTimNCC1;
    public javax.swing.JTextField txtTimNCC2;
    public javax.swing.JTextField txtTimSanPhamChon;
    public javax.swing.JTextField txtTimSanPhamDoi;
    // End of variables declaration//GEN-END:variables
}
