package iuh.fit.gui;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

//import dao.DAO_MauSac;
//import dao.DAO_NhaCungCap;
//import dao.DAO_NhomSanPham;
//import dao.DAO_Sach;
//import dao.DAO_SanPham;
//import dao.DAO_VanPhongPham;
//import entity.MauSac;
//import entity.NhaCungCap;
//import entity.NhomSanPham;
//import entity.Sach;
//import entity.SanPham;
//import entity.VanPhongPham;
//import gui.FrmChinh;
import lookup.LookupNaming;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Timestamp;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

//import com.barcodelib.barcode.Linear;

import java.awt.Desktop;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellType;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FrmDSSanPham extends JPanel {


	private static final long serialVersionUID = 3938805931453096359L;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private String MANCC = "";
    private String MAMAU = "";

    private FrmChinh frm = new FrmChinh();
    private DAO_VanPhongPham dao_vpp = LookupNaming.lookup_VanPhongPham();
    private DAO_Sach dao_sach = LookupNaming.lookup_Sach();
    private DAO_NhomSanPham dao_nsp = LookupNaming.lookup_NhomSanPham();
    private DAO_NhaCungCap dao_ncc = LookupNaming.lookup_NhaCungCap();
    private DAO_MauSac dao_mausac = LookupNaming.lookup_MauSac();
    private DAO_SanPham dao_sp = LookupNaming.lookup_SanPham();
    private Thread thread = null;

    public FrmDSSanPham() throws RemoteException {
//        ConnectDB.getInstance().connect();
        initComponents();
        // TODO Auto-generated catch block

        // doc vao table
        try {
			loadData();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        thread = new Thread(this::setTimeAuto);
        thread.start();

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
                thread.sleep(1); // cho phép ngủ trong khoảng 1000 mns =1s
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

    public void updateVpp(VanPhongPham sp) throws RemoteException {
        String maVpp = txtTimKH62.getText();
        String tenVpp = txtTimKH58.getText();
        int sl = Integer.parseInt(txtTimKH61.getText());
        NhomSanPham nsp = dao_nsp.getNsptheoTen(jComboBox9.getSelectedItem().toString());
        NhaCungCap ncc = dao_ncc.getNCCTheoTen(txtSuaVPPChonNCC.getText());
        double donGiaNhap = Double.parseDouble(txtTimKH66.getText());
        String moTa = jTextArea6.getText();
        String noiSx = txtTimKH63.getText();
        double donGiaBan = Double.parseDouble(txtTimKH65.getText());
        double vat = Double.parseDouble(txtTimKH64.getText());
        double giamGia = sp.getGiamGia();
        LocalDateTime ngayTao = sp.getNgayTao();
//        MauSac mau = new MauSac(txtSuaVPPChonMau.getText());
        MauSac mau = dao_mausac.getMauSactheoMa(txtSuaVPPChonMau.getText());
        String tinhTrang = sl > 0 ? "Còn hàng" : "Hết hàng";
//        VanPhongPham vppNew = new VanPhongPham(maVpp, tenVpp, nsp, ncc, sl, donGiaNhap, moTa, tinhTrang, donGiaBan, vat,
//                ngayTao, giamGia, mau, noiSx);
        SanPham spNew = new VanPhongPham(maVpp, tenVpp, nsp, ncc, sl, donGiaNhap, moTa, tinhTrang, donGiaBan, vat, ngayTao, giamGia, mau, noiSx);
        try {
            dao_sp.updateSanPham(spNew);
            dao_vpp.update((VanPhongPham)spNew);
        } catch (Exception e) {
            // TODO: handle exception
        	e.printStackTrace();
        }
    }

    public void updateS(Sach s) throws RemoteException {
        String maS = txtTimKH49.getText();
        String tenS = txtTimKH3.getText();
        int sl = Integer.parseInt(txtTimKH48.getText());
        NhomSanPham nsp = dao_nsp.getNsptheoTen(jComboBox8.getSelectedItem().toString());
//        NhaCungCap ncc = new NhaCungCap(txtSuaSachChonNCC.getText());
        NhaCungCap ncc = dao_ncc.getNCCTheoTen(txtSuaSachChonNCC.getText());
        double donGiaNhap = Double.parseDouble(txtTimKH54.getText());
        String moTa = jTextArea5.getText();
        String tinhTrang = sl > 0 ? "Còn hàng" : "Hết hàng";
        double donGiaBan = Double.parseDouble(txtTimKH56.getText());
        double vat = Double.parseDouble(txtTimKH53.getText());
        LocalDateTime ngayTao = s.getNgayTao();
        double giamGia = Double.parseDouble(txtTimKH52.getText());
        String tacGia = txtTimKH51.getText();
        int namXB = Integer.parseInt(txtTimKH5.getText());
        String nhaXB = txtTimKH50.getText();
        int soTrang = Integer.parseInt(txtTimKH57.getText());
//        Sach sNew = new Sach(maS, tenS, nsp, ncc, sl, donGiaNhap, moTa, tinhTrang, donGiaBan, vat, ngayTao, giamGia,
//                tacGia, namXB, nhaXB, soTrang);
        
        SanPham spNew = new Sach(maS, tenS, nsp, ncc, sl, donGiaNhap, moTa, tinhTrang, donGiaBan, vat, ngayTao, giamGia, tacGia, namXB, nhaXB, soTrang);
        try {
            dao_sp.updateSanPham(spNew);
            dao_sach.updateSach((Sach) spNew);
        } catch (Exception e) {
            // TODO: handle exception
        	e.printStackTrace();
        }
    }

    public void loadDataNsp() throws RemoteException {
        DefaultTableModel modelNsp = (DefaultTableModel) jTable4.getModel();
        modelNsp.getDataVector().removeAllElements();
        modelNsp.fireTableDataChanged();
        List<NhomSanPham> dsNsp = dao_nsp.getAllNhomSanPham();
        int stt = 1;
        for (NhomSanPham nsp : dsNsp) {
            modelNsp.addRow(new Object[]{stt, nsp.getTenNhomSanPham(), nsp.getMaNhomSanPham(),});
            stt++;
        }
    }

    public void timJtableChonMau() {
        DefaultTableModel tableModel = (DefaultTableModel) tableChonMau.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tableChonMau.setRowSorter(sorter);

        txtTimKH2.getDocument().addDocumentListener(new DocumentListener() {
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
        String searchTerm = txtTimKH2.getText().toUpperCase();
        if (searchTerm.isEmpty()) {
            sorter.setRowFilter(RowFilter.regexFilter(txtTimKH2.getText()));
        } else {
            Pattern pattern = Pattern.compile(Pattern.quote(searchTerm),
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            sorter.setRowFilter(new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    String maMau = entry.getValue(1).toString();
                    String tenMau = entry.getValue(2).toString();

                    return pattern.matcher(maMau).lookingAt() || pattern.matcher(tenMau).lookingAt();
                }
            });
        }
    }

    public void timJtableChonNCC() {
        DefaultTableModel tableModel = (DefaultTableModel) tableChonNCC.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tableChonNCC.setRowSorter(sorter);

        txtTimNCC.getDocument().addDocumentListener(new DocumentListener() {
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
        String searchTerm = txtTimNCC.getText().toUpperCase();
        if (searchTerm.isEmpty()) {
            sorter.setRowFilter(RowFilter.regexFilter(txtTimNCC.getText()));
        } else {
            Pattern pattern = Pattern.compile(Pattern.quote(searchTerm),
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            sorter.setRowFilter(new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    String maNcc = entry.getValue(1).toString();
                    String tenNcc = entry.getValue(2).toString();
                    String sdt = entry.getValue(3).toString();
                    return pattern.matcher(maNcc).lookingAt() || pattern.matcher(tenNcc).lookingAt()
                            || pattern.matcher(sdt).lookingAt();
                }
            });
        }
    }

    public boolean valiDataThemSach() {
        if (!(jLabel24.getText().equals("") && jLabel161.getText().equals("") && jLabel155.getText().equals("")
                && jLabel158.getText().equals("") && jLabel156.getText().equals("") && jLabel160.getText().equals("")
                && jLabel157.getText().equals("") && jLabel159.getText().equals(""))) {
            return false;
        }
        return true;
    }

    public boolean valiDataSuaSach() {
        if (!(jLabel154.getText().equals("") && jLabel111.getText().equals("") && jLabel162.getText().equals("")
                && jLabel164.getText().equals("") && jLabel153.getText().equals("") && jLabel166.getText().equals("")
                && jLabel165.getText().equals("") && jLabel163.getText().equals(""))) {
            return false;
        }
        return true;
    }

    public boolean valiDataThemVpp() {
        if (!(jLabel52.getText().equals("") && jLabel59.getText().equals("") && jLabel50.getText().equals("")
                && jLabel151.getText().equals(""))) {
            return false;
        }
        return true;
    }

    public boolean valiDataSuaVpp() {
        if (!(jLabel132.getText().equals("") && jLabel139.getText().equals("") && jLabel167.getText().equals("")
                && jLabel168.getText().equals(""))) {
            return false;
        }
        return true;
    }

    public int selectRow(int rowSelect) {
        return rowSelect;
    }

    public void timJtable2() {
        DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        jTable2.setRowSorter(sorter);

        txtTimSP.getDocument().addDocumentListener(new DocumentListener() {
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
        String searchTerm = txtTimSP.getText().toUpperCase();
        if (searchTerm.isEmpty()) {
            sorter.setRowFilter(RowFilter.regexFilter(txtTimSP.getText()));
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

    public void timCbSp() {
        DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        jTable2.setRowSorter(sorter);
        
        String searchTerm = jComboBox1.getSelectedItem().toString();

        RowFilter<DefaultTableModel, Object> rowFilter;
        if (searchTerm.equals("Tất cả")) {
            rowFilter = RowFilter.regexFilter("");
        } else if (searchTerm.equals("Còn hàng")) {
            rowFilter = RowFilter.regexFilter("Còn hàng");
        } else {
            rowFilter = RowFilter.regexFilter("Hết hàng");
        }

        sorter.setRowFilter(rowFilter);
    }

    public void loadData() throws RemoteException {
        deleteTable();
//        List<SanPham> dsSanPham = dao_sp.getAllSanPham();
        List<Sach> dsSach = dao_sach.getAlltbSach_20();
        List<VanPhongPham> dsVpp = dao_vpp.getAllVanPhongPhan_20();

        DefaultTableModel tableModal = (DefaultTableModel) jTable2.getModel();
        tableModal.getDataVector().removeAllElements();
        tableModal.fireTableDataChanged();
        int stt = 1;
        String tinhTrang = "";
        for (Sach s : dsSach) {
//			if (s.getTinhTrang().equals("1")) {
//				tinhTrang = "Còn hàng";
//			} else {
//				tinhTrang = "Hết hàng";
//			}
            NhomSanPham nsp = dao_nsp.getNspTheoMa(s.getNhomSanPham().getMaNhomSanPham());
            tableModal.addRow(new Object[]{stt, s.getMaSanPham(), s.getTenSanPham(), nsp.getTenNhomSanPham(),
                s.getSoLuongTon(), formatter.format(s.getNgayTao()), s.getTinhTrang()

            });
            stt++;
        }
        for (VanPhongPham vpp : dsVpp) {
            NhomSanPham nsp = dao_nsp.getNspTheoMa(vpp.getNhomSanPham().getMaNhomSanPham());
            tableModal.addRow(new Object[]{stt, vpp.getMaSanPham(), vpp.getTenSanPham(), nsp.getTenNhomSanPham(),
                vpp.getSoLuongTon(), formatter.format(vpp.getNgayTao()), vpp.getTinhTrang()

            });
            stt++;
        }

    }

    public void loadDataChonNCC() throws RemoteException {
        DefaultTableModel dm = (DefaultTableModel) tableChonNCC.getModel();
        dm.getDataVector().removeAllElements();
        List<NhaCungCap> dsNCC = dao_ncc.getALLNhaCungCap_20();
        DefaultTableModel tableModal = (DefaultTableModel) tableChonNCC.getModel();
        int stt = 1;
        for (NhaCungCap ncc : dsNCC) {
            tableModal.addRow(new Object[]{stt, ncc.getMaNCC(), ncc.getTenNCC(), ncc.getSoDienThoai()});
            stt++;
        }
    }

    public void loadDataChonMau() throws RemoteException {
        DefaultTableModel dm = (DefaultTableModel) tableChonMau.getModel();
        dm.getDataVector().removeAllElements();
        List<MauSac> dsMau = dao_mausac.getAlltbMauSac();
        DefaultTableModel tableModal = (DefaultTableModel) tableChonMau.getModel();
        int stt = 1;
        for (MauSac mau : dsMau) {
            tableModal.addRow(new Object[]{stt, mau.getMaMau(), mau.getTenMau()});
            stt++;
        }
    }

    public void deleteTable() {
        DefaultTableModel dm = (DefaultTableModel) jTable2.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();
    }

    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    public void readFile(String fileName) throws FileNotFoundException, IOException {
        FileInputStream fis;
        Sach s = new Sach();
        XSSFRow row;
        try {
            System.out.println(
                    "-------------------------------READING THE SPREADSHEET-------------------------------------");
            fis = new FileInputStream(fileName);
            XSSFWorkbook workbookRead = new XSSFWorkbook(fis);
            XSSFSheet spreadsheetRead = workbookRead.getSheetAt(0);

            Iterator<Row> rowIterator = spreadsheetRead.iterator();
            while (rowIterator.hasNext()) {
                row = (XSSFRow) rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    cell.setCellType(CellType.STRING);
                    switch (cell.getColumnIndex()) {
                        case 0:
                            System.out.print(cell.getStringCellValue() + " \t\t");
                            break;
                        case 1:
                            System.out.print(cell.getStringCellValue() + " \t\t");
                            break;
                        case 2:
                            System.out.print(cell.getStringCellValue() + " \t\t");
                            break;
                        case 3:
                            System.out.print(cell.getStringCellValue() + " \t\t");
                            break;
                        case 4:
                            System.out.print(cell.getStringCellValue() + " \t\t");
                            break;
                    }
                }
                System.out.println();
//                e.empId = Integer.parseInt(row.getCell(0).getStringCellValue());
//                e.empName = row.getCell(1).getStringCellValue();
//                e.gender = row.getCell(2).getStringCellValue();
//                e.salary = row.getCell(3).getStringCellValue();

//                InsertRowInDB(e.empId, e.empName, e.gender, e.salary);
            }
            System.out.println("Values Inserted Successfully");

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
  //Import Danh sách SanPham - Gui>FrmDSSanPham
    public void importExcel() {
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
                excelFileChooser.setDialogTitle("Select excel file");
                FileNameExtensionFilter fnef = new FileNameExtensionFilter("EXCEL FILES", "xls", "xlsx", "xlsm");
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
                            if (columnCount == 13) {
                                while (rowIterator.hasNext()) {
                                    Row firstRow = rowIterator.next();
                                    Iterator<Cell> cellIterator = firstRow.cellIterator();
                                    if (cellIterator.hasNext()) {
                                        String maSp = createMaVpp();
                                        String tenSp = getStringCellValue(cellIterator.next());
                                        String strNsp = getStringCellValue(cellIterator.next());
                                        NhomSanPham nsp = new NhomSanPham(strNsp);
                                        String strNcc = getStringCellValue(cellIterator.next());
                                        NhaCungCap ncc = new NhaCungCap(strNcc);
                                        int sl = getNumericCellValue(cellIterator.next()).intValue();
                                        double donGia = getNumericCellValue(cellIterator.next());
                                        String moTa = getStringCellValue(cellIterator.next());
                                        double donGiaBan = getNumericCellValue(cellIterator.next());
                                        double vat = getNumericCellValue(cellIterator.next());
                                        String maMau = getStringCellValue(cellIterator.next());
                                        MauSac ms = new MauSac(maMau);
                                        String noiSx = getStringCellValue(cellIterator.next());

                                        Date DngayTao = cellIterator.next().getDateCellValue();
                                        Instant instantNgayTao = DngayTao.toInstant();
                                        LocalDateTime ngayTao = instantNgayTao.atZone(ZoneId.systemDefault()).toLocalDateTime();
                                        double giamGia = getNumericCellValue(cellIterator.next());
                                        String tinhTrang = getStringCellValue(cellIterator.next());
                                        VanPhongPham vpp = new VanPhongPham(maSp, tenSp, nsp, ncc, sl, donGia, moTa, tinhTrang,
                                                donGiaBan, vat, ngayTao, giamGia, ms, noiSx);
                                        dao_vpp.insertVpp(vpp);
                                        
                                       
                                       
                                    }
                                }
                                JOptionPane.showMessageDialog(null, "Nhập thành công");
                                loadData();
                            } else if (columnCount == 15) {
                                while (rowIterator.hasNext()) {
                                    Row firstRow = rowIterator.next();
                                    Iterator<Cell> cellIterator = firstRow.cellIterator();
                                    if (cellIterator.hasNext()) {
                                        String maSp = createMaSach();
                                        String tenSp = getStringCellValue(cellIterator.next());
                                        String strNsp = getStringCellValue(cellIterator.next());
                                        NhomSanPham nsp = new NhomSanPham(strNsp);
                                        String strNcc = getStringCellValue(cellIterator.next());
                                        NhaCungCap ncc = new NhaCungCap(strNcc);
                                        int sl = getNumericCellValue(cellIterator.next()).intValue();
                                        double donGia = getNumericCellValue(cellIterator.next());
                                        String moTa = getStringCellValue(cellIterator.next());
                                        String tinhTrang = getStringCellValue(cellIterator.next());
                                        double donGiaBan = getNumericCellValue(cellIterator.next());
                                        double vat = getNumericCellValue(cellIterator.next());
                                        String tacGia = getStringCellValue(cellIterator.next());
                                        int namXB = getNumericCellValue(cellIterator.next()).intValue();
                                        String nhaXB = getStringCellValue(cellIterator.next());
                                        int soTrang = getNumericCellValue(cellIterator.next()).intValue();

                                        Date DngayTao = cellIterator.next().getDateCellValue();
                                        Instant instantNgayTao = DngayTao.toInstant();
                                        LocalDateTime ngayTao = instantNgayTao.atZone(ZoneId.systemDefault()).toLocalDateTime();
                                        double giamGia = getNumericCellValue(cellIterator.next());

                                        Sach s = new Sach(maSp, tenSp, nsp, ncc, sl, donGia, moTa, tinhTrang, donGiaBan, vat,
                                                ngayTao, giamGia, tacGia, namXB, nhaXB, soTrang);
                                        dao_sach.createSach(s);
                                        
                                        
                                    }
                                }
                                JOptionPane.showMessageDialog(null, "Nhập thành công");
                                loadData();
                            }
                        }
                        workBook.close();

                    } catch (IOException e) {
                        // TODO: handle exception
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
                            // TODO: handle exception
                            JOptionPane.showMessageDialog(null, e2.getMessage());
                        }
                    }
                }
            }
        };

        Thread importThread = new Thread(importTask);
        importThread.start();
    }

    private String getStringCellValue(Cell cell) {
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "";
    }

    private Double getNumericCellValue(Cell cell) {
        return cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0;
    }
    
  //Export Danh sách SanPham - Gui>FrmDSSanPham
    public void exportExcel() {
           Runnable exportTask = new Runnable() {
               @Override
               public void run() {
                   
                   try {
                	   List<Sach> dsSach = dao_sach.getAlltbSach();
                       List<VanPhongPham> dsVPP = dao_vpp.getAllVanPhongPhan();
                       String dataFolderPath = "excel";
                       JFileChooser jFileChooser = new JFileChooser(dataFolderPath);
                       jFileChooser.showSaveDialog(null);
                       jFileChooser.setDialogTitle("Select Folder");
                       File saveFile = jFileChooser.getSelectedFile();
                       if (saveFile != null) {
                           saveFile = new File(saveFile.toString() + ".xlsx");
                           XSSFWorkbook wordbook = new XSSFWorkbook();
                           XSSFSheet sheet = wordbook.createSheet("Danhsach");
                           XSSFRow row = null;
                           Cell cell = null;

                           row = sheet.createRow(1);

                           cell = row.createCell(0, CellType.STRING);
                           cell.setCellValue("Dach sách sản phẩm");

                           row = sheet.createRow(2);

                           cell = row.createCell(0, CellType.STRING);
                           cell.setCellValue("STT");
                           cell = row.createCell(1, CellType.STRING);
                           cell.setCellValue("Mã sản phẩm");
                           cell = row.createCell(2, CellType.STRING);
                           cell.setCellValue("Tên sản phẩm");
                           cell = row.createCell(3, CellType.STRING);
                           cell.setCellValue("Nhóm sản phẩm");
                           cell = row.createCell(4, CellType.STRING);
                           cell.setCellValue("Nhà cung cấp");
                           cell = row.createCell(5, CellType.STRING);
                           cell.setCellValue("Số lượng tồn");
                           cell = row.createCell(6, CellType.STRING);
                           cell.setCellValue("Đơn giá nhập");
                           cell = row.createCell(7, CellType.STRING);
                           cell.setCellValue("Mô tả");
                           cell = row.createCell(8, CellType.STRING);
                           cell.setCellValue("Tình trạng");
                           cell = row.createCell(9, CellType.STRING);
                           cell.setCellValue("Đơn giá bán");
                           cell = row.createCell(10, CellType.STRING);
                           cell.setCellValue("VAT");
                           cell = row.createCell(11, CellType.STRING);
                           cell.setCellValue("Ngày tạo");
                           cell = row.createCell(12, CellType.STRING);
                           cell.setCellValue("Giảm giá");
                           cell = row.createCell(13, CellType.STRING);
                           cell.setCellValue("Tác giả");
                           cell = row.createCell(14, CellType.STRING);
                           cell.setCellValue("Năm xuất bản");
                           cell = row.createCell(15, CellType.STRING);
                           cell.setCellValue("Nhà xuất bản");
                           cell = row.createCell(16, CellType.STRING);
                           cell.setCellValue("Số trang");
                           cell = row.createCell(17, CellType.STRING);
                           cell.setCellValue("Màu sắc");
                           cell = row.createCell(18, CellType.STRING);
                           cell.setCellValue("Nơi sản xuất");
                           for (int i = 0; i < dsSach.size(); i++) {
                               row = sheet.createRow(3 + i);
                               cell = row.createCell(0, CellType.NUMERIC);
                               cell.setCellValue(i + 1);
                               cell = row.createCell(1, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getMaSanPham());
                               cell = row.createCell(2, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getTenSanPham());
                               cell = row.createCell(3, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getNhomSanPham().getMaNhomSanPham());
                               cell = row.createCell(4, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getNhaCungCap().getMaNCC());
                               cell = row.createCell(5, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getSoLuongTon());
                               cell = row.createCell(6, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getDonGiaNhap());
                               cell = row.createCell(7, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getMoTa());
                               cell = row.createCell(8, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getTinhTrang());
                               cell = row.createCell(9, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getDonGiaBan());
                               cell = row.createCell(10, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getVAT());
                               cell = row.createCell(11, CellType.STRING);
                               cell.setCellValue(formatter.format(dsSach.get(i).getNgayTao()));
                               cell = row.createCell(12, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getGiamGia());
                               cell = row.createCell(13, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getTacGia());
                               cell = row.createCell(14, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getNamXuatBan());
                               cell = row.createCell(15, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getNhaXuatBan());
                               cell = row.createCell(16, CellType.STRING);
                               cell.setCellValue(dsSach.get(i).getSoTrang());
                           }
                           for (int i = 0; i < dsVPP.size(); i++) {
                               row = sheet.createRow(dsSach.size() + 3 + i);
                               cell = row.createCell(0, CellType.NUMERIC);
                               cell.setCellValue(dsSach.size() + i + 1);
                               cell = row.createCell(1, CellType.STRING);
                               cell.setCellValue(dsVPP.get(i).getMaSanPham());
                               cell = row.createCell(2, CellType.STRING);
                               cell.setCellValue(dsVPP.get(i).getTenSanPham());
                               cell = row.createCell(3, CellType.STRING);
                               cell.setCellValue(dsVPP.get(i).getNhomSanPham().getMaNhomSanPham());
                               cell = row.createCell(4, CellType.STRING);
                               cell.setCellValue(dsVPP.get(i).getNhaCungCap().getMaNCC());
                               cell = row.createCell(5, CellType.STRING);
                               cell.setCellValue(dsVPP.get(i).getSoLuongTon());
                               cell = row.createCell(6, CellType.STRING);
                               cell.setCellValue(dsVPP.get(i).getDonGiaNhap());
                               cell = row.createCell(7, CellType.STRING);
                               cell.setCellValue(dsVPP.get(i).getMoTa());
                               cell = row.createCell(8, CellType.STRING);
                               cell.setCellValue(dsVPP.get(i).getTinhTrang());
                               cell = row.createCell(9, CellType.STRING);
                               cell.setCellValue(dsVPP.get(i).getDonGiaBan());
                               cell = row.createCell(10, CellType.STRING);
                               cell.setCellValue(dsVPP.get(i).getVAT());
                               cell = row.createCell(11, CellType.STRING);
                               cell.setCellValue(formatter.format(dsVPP.get(i).getNgayTao()));
                               cell = row.createCell(12, CellType.STRING);
                               cell.setCellValue(dsVPP.get(i).getGiamGia());
                               cell = row.createCell(13, CellType.STRING);
                               cell.setCellValue("");
                               cell = row.createCell(14, CellType.STRING);
                               cell.setCellValue("");
                               cell = row.createCell(15, CellType.STRING);
                               cell.setCellValue("");
                               cell = row.createCell(16, CellType.STRING);
                               cell.setCellValue("");
                               cell = row.createCell(17, CellType.STRING);
                               cell.setCellValue(dsVPP.get(i).getMauSac().getMaMau());
                               cell = row.createCell(18, CellType.STRING);
                               cell.setCellValue(dsVPP.get(i).getNoiSanXuat());
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



    public String createMaVpp() throws RemoteException {
//		LocalDate d = LocalDate.of(2023, 11, 9);
        LocalDate d = LocalDate.now();
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("ddMMyyyy");
        String format = d.format(myFormatDate);
        List<VanPhongPham> dsVpp = dao_vpp.getAllVanPhongPhan();
        Integer count = 1;
        String cusID = "";

        do {
            String tempID = count.toString().length() == 1 ? ("V" + format + "-000000" + count)
                    : count.toString().length() == 2 ? ("V" + format + "-0000" + count)
                    : count.toString().length() == 3 ? ("V" + format + "-000" + count)
                    : count.toString().length() == 4 ? ("V" + format + "-00" + count)
                    : count.toString().length() == 5 ? ("V" + format + "-0" + count)
                    : ("V" + format + "-" + count);

            VanPhongPham existingCustomer = dao_vpp.getVPPtheoMa(tempID);
            if (existingCustomer == null) {
                cusID = tempID;
                break;
            }
            count++;
        } while (true);

        return cusID;
    }

    public String createMaSach() throws RemoteException {
//		LocalDate d = LocalDate.of(2023, 11, 9);
        LocalDate d = LocalDate.now();
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("ddMMyyyy");
        String format = d.format(myFormatDate);
        List<Sach> dsSach = dao_sach.getAlltbSach();
        Integer count = 1;
        String cusID = "";

        do {
            String tempID = count.toString().length() == 1 ? ("S" + format + "-00000" + count)
                    : count.toString().length() == 2 ? ("S" + format + "-0000" + count)
                    : count.toString().length() == 3 ? ("S" + format + "-000" + count)
                    : count.toString().length() == 4 ? ("S" + format + "-00" + count)
                    : count.toString().length() == 5 ? ("S" + format + "-0" + count)
                    : ("S" + format + "-" + count);

            Sach existingCustomer = dao_sach.getSachtheoMa(tempID);
            if (existingCustomer == null) {
                cusID = tempID;
                break;
            }
            count++;
        } while (true);

        return cusID;
    }

    public void showPanelChange(JPanel a, JPanel b) {
        a.removeAll();
        a.add(b);
        a.repaint();
        a.revalidate();
    }

    public Image fitimage(Image img, int w, int h) {
        BufferedImage resizedimage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedimage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();
        return resizedimage;
    }

//    public void clearTextVPP() {
//        txtTimKH39.setText("");
//        txtTimKH34.setText("");
//        txtTimKH38.setText("");
//        Date dateNgayTao = new Date();
//        jDateChooser2.setDate(dateNgayTao);
//        ArrayList<NhomSanPham> dsNsp = dao_nsp.getAllNhomSanPham();
//        for (NhomSanPham nsp : dsNsp) {
//            jComboBox7.addItem(nsp.getMaNhomSanPham());
//        }
//        jTextArea4.setText("");
//        txtTimKH37.setText("");
//        txtTimKH40.setText("");
//        txtTimKH46.setText("");
//        txtTimKH36.setText("");
//        txtTimKH42.setText("");
//        txtTimKH41.setText("");
//    }
//
//    public void clearTextSach() {
//        txtXemMaSPSach.setText("");
//        txtXemTenSPSach.setText("");
//        txtXemSLSach.setText("");
//        Date dateNgayTao = new Date();
//        jDateXemNgaySach.setDate(dateNgayTao);
//        ArrayList<NhomSanPham> dsNsp = dao_nsp.getAllNhomSanPham();
//        for (NhomSanPham nsp : dsNsp) {
//            cboXemNhomSPSach.addItem(nsp.getMaNhomSanPham());
//        }
//        jTextXemMoTaSach.setText("");
//        txtXemNhaCCSach.setText("");
//        txtXemTGSach.setText("");
//        txtXemDGNSach.setText("");
//        txtXemDGBSach.setText("");
//        txtXemNSBSach.setText("");
//        txtXemSoTrangSach.setText("");
//        txtXemVATSach.setText("");
//        txtXemNhaXBSach.setText("");
//        txtXemGiamGiaSach.setText("");
//    }
//    public void createBarcode() {
//        try {
//            Linear barcode = new Linear();
//            barcode.setType(Linear.CODE128B);
//            barcode.setData(txtTimSanPhamChon.getText());
//            barcode.setI(11.0f);
//            String fname = txtTimSanPhamChon.getText();
//            barcode.renderBarcode("src/img/" + fname + ".png");
//            JOptionPane.showMessageDialog(null, "Done");
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Bug barcode");
//
//        }
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     * @throws RemoteException
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() throws RemoteException {

        pnlNull = new JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        pnlInit1 = new JPanel();
        menuScrollPane3 = new menuGui.MenuScrollPane();
        jTable3 = new JTable();
        pnlCenterThemSach = new JPanel();
        jPanel4 = new JPanel();
        jPanel9 = new JPanel();
        pnlTopLeft1 = new JPanel();
        jPanel14 = new JPanel();
        pnlCenterKHchange1 = new JPanel();
        jPanel6 = new JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtThemTenSPSach = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtThemNSBSach = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtThemNCCSach = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        cboThemSach = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextThemMoTaSach = new javax.swing.JTextArea();
        txtThemSLSach = new javax.swing.JTextField();
        txtThemMaSPSach = new javax.swing.JTextField();
        txtThemNhaXBSach = new javax.swing.JTextField();
        txtThemTGSach = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        txtThemGiamGiaSach = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtThemVATSach = new javax.swing.JTextField();
        txtThemDGNSach = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        txtThemDGBSach = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        txtThemSoTrangSach = new javax.swing.JTextField();
        jDateThemSach = new com.toedter.calendar.JDateChooser();
        btnLuuNV3 = new javax.swing.JButton();
        jLabel106 = new javax.swing.JLabel();
        jLabel155 = new javax.swing.JLabel();
        jLabel156 = new javax.swing.JLabel();
        jLabel157 = new javax.swing.JLabel();
        jLabel158 = new javax.swing.JLabel();
        jLabel159 = new javax.swing.JLabel();
        jLabel160 = new javax.swing.JLabel();
        jLabel161 = new javax.swing.JLabel();
        jPanel7 = new JPanel();
        btnLuuNV = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        btnQuayLai = new javax.swing.JButton();
        jPanel10 = new JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        pnlCenterThemVPP = new JPanel();
        jPanel19 = new JPanel();
        jPanel20 = new JPanel();
        pnlTopLeft3 = new JPanel();
        jPanel21 = new JPanel();
        pnlCenterKHchange3 = new JPanel();
        jPanel22 = new JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        txtTimKH18 = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        txtMauSac = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        txtThemNCCVPP = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        txtTimKH22 = new javax.swing.JTextField();
        btnLuuNV4 = new javax.swing.JButton();
        txtTimKH23 = new javax.swing.JTextField();
        txtTimKH25 = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        txtTimKH27 = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        txtTimKH29 = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        btnChonMau = new javax.swing.JButton();
        txtTimKH35 = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel150 = new javax.swing.JLabel();
        jLabel151 = new javax.swing.JLabel();
        jPanel23 = new JPanel();
        btnLuuNV5 = new javax.swing.JButton();
        btnHuy2 = new javax.swing.JButton();
        btnQuayLai2 = new javax.swing.JButton();
        jPanel24 = new JPanel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        jDialogChon = new JDialog(frm,"Chọn",true);
        btnThemSP1 = new javax.swing.JButton();
        btnSuaKH2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        pnlCenterNhomSP = new JPanel();
        jPanel37 = new JPanel();
        jPanel38 = new JPanel();
        pnlTopLeft6 = new JPanel();
        jPanel39 = new JPanel();
        jPanel40 = new JPanel();
        jLabel114 = new javax.swing.JLabel();
        txtTimKH55 = new javax.swing.JTextField();
        jLabel115 = new javax.swing.JLabel();
        btnTatCa1 = new javax.swing.JButton();
        pnlCenterKHchange6 = new JPanel();
        pnlInit2 = new JPanel();
        menuScrollPane4 = new menuGui.MenuScrollPane();
        jTable4 = new JTable();
        jPanel41 = new JPanel();
        btnThemSP2 = new javax.swing.JButton();
        btnQuayLai6 = new javax.swing.JButton();
        jPanel42 = new JPanel();
        jLabel116 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jDialogThemNhomSP = new JDialog(frm,"Thêm nhóm sản phẩm",true);
        btnThemSP3 = new javax.swing.JButton();
        btnSuaKH3 = new javax.swing.JButton();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        txtTimKH43 = new javax.swing.JTextField();
        txtTimKH44 = new javax.swing.JTextField();
        pnlCenterSuaSach = new JPanel();
        jPanel5 = new JPanel();
        jPanel32 = new JPanel();
        pnlTopLeft5 = new JPanel();
        jPanel33 = new JPanel();
        pnlCenterKHchange5 = new JPanel();
        jPanel34 = new JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtTimKH3 = new javax.swing.JTextField();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        txtTimKH5 = new javax.swing.JTextField();
        jLabel107 = new javax.swing.JLabel();
        txtSuaSachChonNCC = new javax.swing.JTextField();
        jLabel108 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox<>();
        jLabel109 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        txtTimKH48 = new javax.swing.JTextField();
        txtTimKH49 = new javax.swing.JTextField();
        txtTimKH50 = new javax.swing.JTextField();
        txtTimKH51 = new javax.swing.JTextField();
        jLabel112 = new javax.swing.JLabel();
        jLabel113 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        txtTimKH52 = new javax.swing.JTextField();
        jLabel119 = new javax.swing.JLabel();
        txtTimKH53 = new javax.swing.JTextField();
        txtTimKH54 = new javax.swing.JTextField();
        jLabel120 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        txtTimKH56 = new javax.swing.JTextField();
        jLabel122 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        txtTimKH57 = new javax.swing.JTextField();
        jDateChooser5 = new com.toedter.calendar.JDateChooser();
        btnLuuNV7 = new javax.swing.JButton();
        jLabel152 = new javax.swing.JLabel();
        jLabel153 = new javax.swing.JLabel();
        jLabel154 = new javax.swing.JLabel();
        jLabel162 = new javax.swing.JLabel();
        jLabel163 = new javax.swing.JLabel();
        jLabel164 = new javax.swing.JLabel();
        jLabel165 = new javax.swing.JLabel();
        jLabel166 = new javax.swing.JLabel();
        jPanel35 = new JPanel();
        btnLuuNV1 = new javax.swing.JButton();
        btnHuy1 = new javax.swing.JButton();
        btnQuayLai7 = new javax.swing.JButton();
        btnTaoMa2 = new javax.swing.JButton();
        jPanel36 = new JPanel();
        jLabel124 = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        jLabel126 = new javax.swing.JLabel();
        pnlCenterSuaVPP = new JPanel();
        jPanel43 = new JPanel();
        jPanel44 = new JPanel();
        pnlTopLeft7 = new JPanel();
        jPanel45 = new JPanel();
        pnlCenterKHchange7 = new JPanel();
        jPanel46 = new JPanel();
        jLabel127 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        jLabel129 = new javax.swing.JLabel();
        txtTimKH58 = new javax.swing.JTextField();
        jLabel130 = new javax.swing.JLabel();
        jLabel131 = new javax.swing.JLabel();
        jLabel132 = new javax.swing.JLabel();
        txtSuaVPPChonMau = new javax.swing.JTextField();
        jLabel133 = new javax.swing.JLabel();
        txtSuaVPPChonNCC = new javax.swing.JTextField();
        jLabel135 = new javax.swing.JLabel();
        jLabel136 = new javax.swing.JLabel();
        jComboBox9 = new javax.swing.JComboBox<>();
        jLabel137 = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        jLabel139 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea6 = new javax.swing.JTextArea();
        txtTimKH61 = new javax.swing.JTextField();
        btnLuuNV8 = new javax.swing.JButton();
        txtTimKH62 = new javax.swing.JTextField();
        txtTimKH63 = new javax.swing.JTextField();
        jLabel140 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        txtTimKH64 = new javax.swing.JTextField();
        jLabel142 = new javax.swing.JLabel();
        txtTimKH65 = new javax.swing.JTextField();
        jLabel143 = new javax.swing.JLabel();
        btnChonMau2 = new javax.swing.JButton();
        txtTimKH66 = new javax.swing.JTextField();
        jLabel144 = new javax.swing.JLabel();
        jDateChooser6 = new com.toedter.calendar.JDateChooser();
        jLabel167 = new javax.swing.JLabel();
        jLabel168 = new javax.swing.JLabel();
        jPanel47 = new JPanel();
        btnLuuNV9 = new javax.swing.JButton();
        btnHuy3 = new javax.swing.JButton();
        btnQuayLai8 = new javax.swing.JButton();
        btnTaoMa3 = new javax.swing.JButton();
        jPanel48 = new JPanel();
        jLabel145 = new javax.swing.JLabel();
        jLabel146 = new javax.swing.JLabel();
        jLabel147 = new javax.swing.JLabel();
        jDialogChonNCC = new JDialog(frm,"Chọn",true);
        pnlChonNCC = new JPanel();
        menuScrollPane9 = new menuGui.MenuScrollPane();
        tableChonNCC = new JTable();
        jPanel49 = new JPanel();
        jLabel148 = new javax.swing.JLabel();
        txtTimNCC = new javax.swing.JTextField();
        jDialogChonMau = new JDialog(frm,"Chọn",true);
        pnlChonNCC2 = new JPanel();
        menuScrollPane11 = new menuGui.MenuScrollPane();
        tableChonMau = new JTable();
        jPanel50 = new JPanel();
        jLabel149 = new javax.swing.JLabel();
        txtTimKH2 = new javax.swing.JTextField();
        pnlChange = new JPanel();
        pnlCenter = new JPanel();
        jPanel2 = new JPanel();
        jPanel8 = new JPanel();
        pnlTopLeft = new JPanel();
        jPanel12 = new JPanel();
        jPanel13 = new JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtTimSP = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        btnTatCa = new javax.swing.JButton();
        pnlCenterKHchange = new JPanel();
        pnlInit = new JPanel();
        menuScrollPane2 = new menuGui.MenuScrollPane();
        jTable2 = new JTable();
        jPanel1 = new JPanel();
        btnThemSP = new javax.swing.JButton();
        btnNhapFile = new javax.swing.JLabel();
        btnXuatFile = new javax.swing.JLabel();
        btnSuaKH1 = new javax.swing.JButton();
        jPanel3 = new JPanel();
        jLabel6 = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        lblNameLogin = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        pnlNull.setBackground(new Color(250, 250, 250));
        pnlNull.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setText("Không tìm thấy dữ liệu phù hợp với kết quả tìm kiếm");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel7.setText("Thử thay đổi điều kiện lọc hoặc từ khóa tìm kiếm");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new ImageIcon(getClass().getResource("/IconMenu/searchnull.png"))); // NOI18N

        javax.swing.GroupLayout pnlNullLayout = new javax.swing.GroupLayout(pnlNull);
        pnlNull.setLayout(pnlNullLayout);
        pnlNullLayout.setHorizontalGroup(
            pnlNullLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNullLayout.createSequentialGroup()
                .addGroup(pnlNullLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlNullLayout.createSequentialGroup()
                        .addGap(333, 333, 333)
                        .addComponent(jLabel4))
                    .addGroup(pnlNullLayout.createSequentialGroup()
                        .addGap(581, 581, 581)
                        .addComponent(jLabel8)))
                .addContainerGap(364, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNullLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(434, 434, 434))
        );
        pnlNullLayout.setVerticalGroup(
            pnlNullLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNullLayout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addContainerGap(345, Short.MAX_VALUE))
        );

        pnlInit1.setBackground(new Color(250, 250, 250));

        jTable3.setModel(new DefaultTableModel(
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
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Nhóm sản phẩm", "Số lượng tồn", "Ngày tạo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
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

        javax.swing.GroupLayout pnlInit1Layout = new javax.swing.GroupLayout(pnlInit1);
        pnlInit1.setLayout(pnlInit1Layout);
        pnlInit1Layout.setHorizontalGroup(
            pnlInit1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInit1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(menuScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        pnlInit1Layout.setVerticalGroup(
            pnlInit1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInit1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(menuScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pnlCenterThemSach.setBackground(new Color(153, 153, 153));
        pnlCenterThemSach.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new Color(153, 153, 153));

        jPanel9.setBackground(new Color(240, 242, 245));

        pnlTopLeft1.setBackground(new Color(204, 204, 255));
        pnlTopLeft1.setLayout(new java.awt.BorderLayout());

        jPanel14.setBackground(new Color(255, 255, 255));

        pnlCenterKHchange1.setBackground(new Color(250, 250, 250));
        pnlCenterKHchange1.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new Color(250, 250, 250));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel9.setText("Thông tin sản phẩm");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel10.setText("Số lượng");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel12.setText("Tên sản phẩm");

        txtThemTenSPSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtThemTenSPSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtThemTenSPSach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtThemTenSPSachKeyReleased(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel23.setText("(*)");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel16.setText("Mã sản phẩm");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel24.setForeground(new Color(255, 0, 0));
        jLabel24.setText("(*)");

        txtThemNSBSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtThemNSBSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtThemNSBSach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtThemNSBSachKeyReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel13.setForeground(new Color(3, 136, 253));
        jLabel13.setText("Nhà cung cấp");

        txtThemNCCSach.setEditable(false);
        txtThemNCCSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtThemNCCSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtThemNCCSach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtThemNCCSachKeyReleased(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel25.setText("Nhóm sản phẩm");

        cboThemSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel27.setText("Ngày tạo");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel28.setText("Mô tả");

        jTextThemMoTaSach.setColumns(20);
        jTextThemMoTaSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextThemMoTaSach.setRows(5);
        jScrollPane1.setViewportView(jTextThemMoTaSach);

        txtThemSLSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtThemSLSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtThemSLSach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtThemSLSachKeyReleased(evt);
            }
        });

        txtThemMaSPSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtThemMaSPSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtThemNhaXBSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtThemNhaXBSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtThemNhaXBSach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtThemNhaXBSachKeyReleased(evt);
            }
        });

        txtThemTGSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtThemTGSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtThemTGSach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtThemTGSachKeyReleased(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel40.setText("Tác giả");

        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel41.setText("Năm xuất bản");

        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel42.setText("Nhà xuất bản");

        txtThemGiamGiaSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtThemGiamGiaSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtThemGiamGiaSach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtThemGiamGiaSachKeyReleased(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel43.setText("Số trang");

        txtThemVATSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtThemVATSach.setText("0.08");
        txtThemVATSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtThemDGNSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtThemDGNSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtThemDGNSach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtThemDGNSachKeyReleased(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel44.setForeground(new Color(3, 136, 253));
        jLabel44.setText("Đơn giá bán");

        jLabel45.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel45.setText("Giảm giá");

        txtThemDGBSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtThemDGBSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel46.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel46.setForeground(new Color(3, 136, 253));
        jLabel46.setText("VAT");

        jLabel98.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel98.setText("Đơn giá nhập");

        txtThemSoTrangSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtThemSoTrangSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtThemSoTrangSach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtThemSoTrangSachKeyReleased(evt);
            }
        });

        jDateThemSach.setDateFormatString("dd-MM-yyyy HH:mm:ss");
        jDateThemSach.setEnabled(false);
        jDateThemSach.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        btnLuuNV3.setBackground(new Color(3, 136, 253));
        btnLuuNV3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLuuNV3.setForeground(new Color(255, 255, 255));
        btnLuuNV3.setText("Chọn");
        btnLuuNV3.setFocusable(false);
        btnLuuNV3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLuuNV3.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnLuuNV3.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuNV3ActionPerformed(evt);
            }
        });

        jLabel106.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel106.setText("(*)");

        jLabel155.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel155.setForeground(new Color(255, 0, 0));
        jLabel155.setText("(*)");

        jLabel156.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel156.setForeground(new Color(255, 0, 0));
        jLabel156.setText("(*)");

        jLabel157.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel157.setForeground(new Color(255, 0, 0));
        jLabel157.setText("(*)");

        jLabel158.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel158.setForeground(new Color(255, 0, 0));
        jLabel158.setText("(*)");

        jLabel159.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel159.setForeground(new Color(255, 0, 0));
        jLabel159.setText("(*)");

        jLabel160.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel160.setForeground(new Color(255, 0, 0));
        jLabel160.setText("(*)");

        jLabel161.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel161.setForeground(new Color(255, 0, 0));
        jLabel161.setText("(*)");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtThemNSBSach, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                            .addComponent(txtThemTGSach)
                            .addComponent(txtThemNhaXBSach)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52)
                                .addComponent(jLabel155, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel42)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel157, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel41)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel156, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(78, 78, 78)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(txtThemGiamGiaSach)
                                        .addGap(101, 101, 101))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtThemSoTrangSach, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(43, 43, 43)
                                                .addComponent(jLabel160, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(jLabel45)
                                                .addGap(47, 47, 47)
                                                .addComponent(jLabel159, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtThemVATSach, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(txtThemDGNSach, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtThemDGBSach, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel98)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel158, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(246, 246, 246)))
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtThemSLSach, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtThemMaSPSach, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                                .addComponent(txtThemNCCSach, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnLuuNV3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cboThemSach, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel161, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 44, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtThemTenSPSach, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jDateThemSach, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel106, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(23, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel16)
                    .addComponent(jLabel12)
                    .addComponent(jLabel24))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtThemMaSPSach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtThemTenSPSach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel27)
                        .addComponent(jLabel161))
                    .addComponent(jLabel10))
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateThemSach, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtThemSLSach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel28)
                    .addComponent(jLabel106))
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(cboThemSach, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtThemNCCSach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLuuNV3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jLabel44)
                    .addComponent(jLabel98)
                    .addComponent(jLabel155)
                    .addComponent(jLabel158))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtThemTGSach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtThemDGBSach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtThemDGNSach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel46)
                    .addComponent(jLabel43)
                    .addComponent(jLabel156)
                    .addComponent(jLabel160))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtThemNSBSach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtThemVATSach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtThemSoTrangSach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(jLabel45)
                    .addComponent(jLabel157)
                    .addComponent(jLabel159))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtThemNhaXBSach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtThemGiamGiaSach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        List<NhomSanPham> dsNhomSP = dao_nsp.getAllNhomSanPham();
        for(NhomSanPham nsp : dsNhomSP) {
            cboThemSach.addItem(nsp.getTenNhomSanPham());
        }

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

        jPanel7.setBackground(new Color(255, 255, 255));

        btnLuuNV.setBackground(new Color(3, 136, 253));
        btnLuuNV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLuuNV.setForeground(new Color(255, 255, 255));
        btnLuuNV.setText("Lưu");
        btnLuuNV.setFocusable(false);
        btnLuuNV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLuuNV.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnLuuNV.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnLuuNVActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnHuy.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHuy.setForeground(new Color(255, 77, 77));
        btnHuy.setText("Hủy");
        btnHuy.setFocusable(false);
        btnHuy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHuy.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnHuy.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        btnQuayLai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnQuayLai.setForeground(new Color(3, 136, 253));
        btnQuayLai.setText("Quay lại danh sách sản phẩm");
        btnQuayLai.setFocusable(false);
        btnQuayLai.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnQuayLai.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnQuayLai.addActionListener(new ActionListener() {
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLuuNV, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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

        pnlCenterThemSach.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel10.setBackground(new Color(250, 250, 250));
        jPanel10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel10.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel14.setText("Danh sách sản phẩm");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel15.setText("Nguyễn Châu Tình - DESGIN");

        jLabel99.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel99.setText("> Thêm sản phẩm");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel99)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 969, Short.MAX_VALUE)
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
                    .addComponent(jLabel99, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlCenterThemSach.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        pnlCenterThemVPP.setBackground(new Color(153, 153, 153));
        pnlCenterThemVPP.setLayout(new java.awt.BorderLayout());

        jPanel19.setBackground(new Color(153, 153, 153));

        jPanel20.setBackground(new Color(240, 242, 245));

        pnlTopLeft3.setBackground(new Color(204, 204, 255));
        pnlTopLeft3.setLayout(new java.awt.BorderLayout());

        jPanel21.setBackground(new Color(255, 255, 255));

        pnlCenterKHchange3.setBackground(new Color(250, 250, 250));
        pnlCenterKHchange3.setLayout(new java.awt.BorderLayout());

        jPanel22.setBackground(new Color(250, 250, 250));
        jPanel22.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel47.setText("Thông tin sản phẩm");

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel48.setText("Số lượng");

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel49.setText("Tên sản phẩm");

        txtTimKH18.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH18.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH18.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH18ActionPerformed(evt);
            }
        });
        txtTimKH18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtTimKH18KeyReleased(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel50.setForeground(new Color(255, 0, 0));
        jLabel50.setText("(*)");

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel51.setText("Mã sản phẩm");

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel52.setForeground(new Color(255, 0, 0));
        jLabel52.setText("(*)");

        txtMauSac.setEditable(false);
        txtMauSac.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtMauSac.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel53.setForeground(new Color(3, 136, 253));
        jLabel53.setText("Nhà cung cấp");

        txtThemNCCVPP.setEditable(false);
        txtThemNCCVPP.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtThemNCCVPP.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel55.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel55.setText("Nhóm sản phẩm");

        jLabel56.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel56.setText("(*)");

        jComboBox6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel57.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel57.setText("Ngày tạo");

        jLabel58.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel58.setText("Mô tả");

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel59.setForeground(new Color(255, 0, 0));
        jLabel59.setText("(*)");

        jTextArea3.setColumns(20);
        jTextArea3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextArea3.setRows(5);
        jScrollPane3.setViewportView(jTextArea3);

        txtTimKH22.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH22.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH22.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH22ActionPerformed(evt);
            }
        });
        txtTimKH22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtTimKH22KeyReleased(evt);
            }
        });

        btnLuuNV4.setBackground(new Color(3, 136, 253));
        btnLuuNV4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLuuNV4.setForeground(new Color(255, 255, 255));
        btnLuuNV4.setText("Chọn");
        btnLuuNV4.setFocusable(false);
        btnLuuNV4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLuuNV4.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnLuuNV4.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuNV4ActionPerformed(evt);
            }
        });

        txtTimKH23.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH23.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH23.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH23ActionPerformed(evt);
            }
        });

        txtTimKH25.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH25.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH25.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH25ActionPerformed(evt);
            }
        });
        txtTimKH25.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtTimKH25KeyReleased(evt);
            }
        });

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel60.setText("Nơi sản xuất");

        jLabel61.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel61.setForeground(new Color(3, 136, 253));
        jLabel61.setText("Màu sắc");

        txtTimKH27.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH27.setText("0.08");
        txtTimKH27.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH27.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH27ActionPerformed(evt);
            }
        });

        jLabel64.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel64.setForeground(new Color(3, 136, 253));
        jLabel64.setText("Đơn giá bán");

        txtTimKH29.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH29.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH29.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH29ActionPerformed(evt);
            }
        });

        jLabel66.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel66.setForeground(new Color(3, 136, 253));
        jLabel66.setText("VAT");

        btnChonMau.setBackground(new Color(3, 136, 253));
        btnChonMau.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnChonMau.setForeground(new Color(255, 255, 255));
        btnChonMau.setText("Chọn");
        btnChonMau.setFocusable(false);
        btnChonMau.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChonMau.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnChonMau.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonMauActionPerformed(evt);
            }
        });

        txtTimKH35.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH35.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH35.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH35ActionPerformed(evt);
            }
        });
        txtTimKH35.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                txtTimKH35KeyReleased(evt);
            }
        });

        jLabel97.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel97.setText("Đơn giá nhập");

        jDateChooser3.setDateFormatString("dd-MM-yyyy HH:mm:ss");
        jDateChooser3.setEnabled(false);
        jDateChooser3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel150.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel150.setText("(*)");

        jLabel151.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel151.setForeground(new Color(255, 0, 0));
        jLabel151.setText("(*)");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel22Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(btnLuuNV4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtTimKH22, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel22Layout.createSequentialGroup()
                                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtThemNCCVPP, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(60, 60, 60)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTimKH29, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTimKH27, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel22Layout.createSequentialGroup()
                                        .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(82, 82, 82)
                                        .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTimKH18, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(277, 277, 277))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel22Layout.createSequentialGroup()
                                    .addComponent(txtMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnChonMau, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel22Layout.createSequentialGroup()
                                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(48, 48, 48)
                                    .addComponent(jLabel151, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel22Layout.createSequentialGroup()
                                    .addComponent(jLabel97, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(50, 50, 50)
                                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtTimKH35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTimKH25, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)
                                .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel47)
                                    .addGroup(jPanel22Layout.createSequentialGroup()
                                        .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(53, 53, 53)
                                        .addComponent(jLabel150, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtTimKH23, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(267, 267, 267)
                                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel51)
                                .addComponent(jLabel150))
                            .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTimKH23, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimKH18, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel57)
                            .addComponent(jLabel59)
                            .addComponent(jLabel48)))
                    .addComponent(jLabel52))
                .addGap(12, 12, 12)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(txtTimKH22, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel58)
                                .addComponent(jLabel56))
                            .addComponent(jLabel55))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel53)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtThemNCCVPP, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnLuuNV4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(jLabel97)
                    .addComponent(jLabel50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKH29, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKH35, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(jLabel60)
                    .addComponent(jLabel151))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKH27, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKH25, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jLabel61)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChonMau, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        List<NhomSanPham> dsNhomSP4 = dao_nsp.getAllNhomSanPham();
        for(NhomSanPham nsp : dsNhomSP4) {
            jComboBox6.addItem(nsp.getTenNhomSanPham());
        }

        pnlCenterKHchange3.add(jPanel22, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange3, javax.swing.GroupLayout.PREFERRED_SIZE, 1262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlTopLeft3.add(jPanel21, java.awt.BorderLayout.CENTER);

        jPanel23.setBackground(new Color(255, 255, 255));

        btnLuuNV5.setBackground(new Color(3, 136, 253));
        btnLuuNV5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLuuNV5.setForeground(new Color(255, 255, 255));
        btnLuuNV5.setText("Lưu");
        btnLuuNV5.setFocusable(false);
        btnLuuNV5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLuuNV5.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnLuuNV5.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnLuuNV5ActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnHuy2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHuy2.setForeground(new Color(255, 77, 77));
        btnHuy2.setText("Hủy");
        btnHuy2.setFocusable(false);
        btnHuy2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHuy2.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnHuy2.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuy2ActionPerformed(evt);
            }
        });

        btnQuayLai2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnQuayLai2.setForeground(new Color(3, 136, 253));
        btnQuayLai2.setText("Quay lại danh sách sản phẩm");
        btnQuayLai2.setFocusable(false);
        btnQuayLai2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnQuayLai2.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnQuayLai2.addActionListener(new ActionListener() {
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
                .addComponent(btnHuy2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLuuNV5, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLuuNV5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHuy2)
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
                    .addComponent(pnlTopLeft3, javax.swing.GroupLayout.PREFERRED_SIZE, 1273, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(61, Short.MAX_VALUE))
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
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCenterThemVPP.add(jPanel19, java.awt.BorderLayout.CENTER);

        jPanel24.setBackground(new Color(250, 250, 250));
        jPanel24.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel24.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel67.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel67.setText("Danh sách sản phẩm");

        jLabel68.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel68.setText("Nguyễn Châu Tình - DESGIN");

        jLabel100.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel100.setText("> Thêm sản phẩm");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel67)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel100)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 969, Short.MAX_VALUE)
                .addComponent(jLabel68)
                .addGap(19, 19, 19))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel68, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel100, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlCenterThemVPP.add(jPanel24, java.awt.BorderLayout.PAGE_START);

        jDialogChon.setSize(new java.awt.Dimension(462, 300));

        btnThemSP1.setBackground(new Color(3, 136, 253));
        btnThemSP1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemSP1.setForeground(new Color(255, 255, 255));
        btnThemSP1.setText("Sách");
        btnThemSP1.setFocusable(false);
        btnThemSP1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemSP1.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnThemSP1.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSP1ActionPerformed(evt);
            }
        });

        btnSuaKH2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuaKH2.setForeground(new Color(3, 136, 253));
        btnSuaKH2.setText("Văn phòng phẩm");
        btnSuaKH2.setFocusable(false);
        btnSuaKH2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSuaKH2.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnSuaKH2.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaKH2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 16)); // NOI18N
        jLabel1.setText("Chọn sản phẩm");

        javax.swing.GroupLayout jDialogChonLayout = new javax.swing.GroupLayout(jDialogChon.getContentPane());
        jDialogChon.getContentPane().setLayout(jDialogChonLayout);
        jDialogChonLayout.setHorizontalGroup(
            jDialogChonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogChonLayout.createSequentialGroup()
                .addGroup(jDialogChonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialogChonLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(btnSuaKH2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnThemSP1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialogChonLayout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jDialogChonLayout.setVerticalGroup(
            jDialogChonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogChonLayout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(62, 62, 62)
                .addGroup(jDialogChonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSuaKH2)
                    .addComponent(btnThemSP1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(125, 125, 125))
        );

        pnlCenterNhomSP.setBackground(new java.awt.Color(153, 153, 153));
        pnlCenterNhomSP.setLayout(new java.awt.BorderLayout());

        jPanel37.setBackground(new java.awt.Color(153, 153, 153));

        jPanel38.setBackground(new java.awt.Color(240, 242, 245));

        pnlTopLeft6.setBackground(new java.awt.Color(204, 204, 255));
        pnlTopLeft6.setLayout(new java.awt.BorderLayout());

        jPanel39.setBackground(new java.awt.Color(255, 255, 255));

        jPanel40.setBackground(new java.awt.Color(255, 255, 255));

        jLabel114.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel114.setText("Thông tin nhóm sản phẩm");

        txtTimKH55.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH55.setText("Nhập vào thông tin tìm kiếm...");
        txtTimKH55.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH55.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimKH55FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTimKH55FocusLost(evt);
            }
        });
        txtTimKH55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH55ActionPerformed(evt);
            }
        });

        jLabel115.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel115.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/magnifying-glass.png"))); // NOI18N

        btnTatCa1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTatCa1.setForeground(new java.awt.Color(3, 136, 253));
        btnTatCa1.setText("Tất cả");
        btnTatCa1.setFocusable(false);
        btnTatCa1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTatCa1.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnTatCa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTatCa1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addComponent(jLabel114)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addComponent(jLabel115, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimKH55, javax.swing.GroupLayout.PREFERRED_SIZE, 962, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTatCa1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel114, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel115, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTatCa1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimKH55, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCenterKHchange6.setBackground(new java.awt.Color(250, 250, 250));
        pnlCenterKHchange6.setLayout(new java.awt.BorderLayout());

        pnlInit2.setBackground(new java.awt.Color(250, 250, 250));
        pnlInit2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTable4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Tên nhóm sản phẩm", "Mã nhóm sản phẩm"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.setRowHeight(60);
        jTable4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable4.setShowHorizontalLines(true);
        jTable4.getTableHeader().setReorderingAllowed(false);
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTable4MouseExited(evt);
            }
        });
        menuScrollPane4.setViewportView(jTable4);
        if (jTable4.getColumnModel().getColumnCount() > 0) {
            jTable4.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        javax.swing.GroupLayout pnlInit2Layout = new javax.swing.GroupLayout(pnlInit2);
        pnlInit2.setLayout(pnlInit2Layout);
        pnlInit2Layout.setHorizontalGroup(
            pnlInit2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInit2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(menuScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        pnlInit2Layout.setVerticalGroup(
            pnlInit2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInit2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(menuScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pnlCenterKHchange6.add(pnlInit2, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCenterKHchange6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        pnlTopLeft6.add(jPanel39, java.awt.BorderLayout.CENTER);

        jPanel41.setBackground(new java.awt.Color(255, 255, 255));

        btnThemSP2.setBackground(new java.awt.Color(3, 136, 253));
        btnThemSP2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemSP2.setForeground(new java.awt.Color(255, 255, 255));
        btnThemSP2.setText("Thêm nhóm sản phẩm");
        btnThemSP2.setFocusable(false);
        btnThemSP2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemSP2.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnThemSP2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSP2ActionPerformed(evt);
            }
        });

        btnQuayLai6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnQuayLai6.setForeground(new java.awt.Color(3, 136, 253));
        btnQuayLai6.setText("Quay lại danh sách sản phẩm");
        btnQuayLai6.setFocusable(false);
        btnQuayLai6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnQuayLai6.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnQuayLai6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuayLai6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnQuayLai6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnThemSP2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemSP2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnQuayLai6))
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTopLeft6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(401, Short.MAX_VALUE))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlTopLeft6, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCenterNhomSP.add(jPanel37, java.awt.BorderLayout.CENTER);

        jPanel42.setBackground(new java.awt.Color(250, 250, 250));
        jPanel42.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel42.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel116.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel116.setText("Danh sách sản phẩm");

        jLabel117.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel117.setText("Nguyễn Châu Tình - DESGIN");

        jLabel103.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel103.setText("> Nhóm sản phẩm");

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel116)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel103)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 965, Short.MAX_VALUE)
                .addComponent(jLabel117)
                .addGap(19, 19, 19))
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel116, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel117, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlCenterNhomSP.add(jPanel42, java.awt.BorderLayout.PAGE_START);

        jDialogThemNhomSP.setBackground(new java.awt.Color(250, 250, 250));
        jDialogThemNhomSP.setSize(new java.awt.Dimension(400, 300));

        btnThemSP3.setBackground(new java.awt.Color(3, 136, 253));
        btnThemSP3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemSP3.setForeground(new java.awt.Color(255, 255, 255));
        btnThemSP3.setText("Lưu");
        btnThemSP3.setFocusable(false);
        btnThemSP3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemSP3.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnThemSP3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSP3ActionPerformed(evt);
            }
        });

        btnSuaKH3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuaKH3.setForeground(new java.awt.Color(3, 136, 253));
        btnSuaKH3.setText("Thoát");
        btnSuaKH3.setFocusable(false);
        btnSuaKH3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSuaKH3.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnSuaKH3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaKH3ActionPerformed(evt);
            }
        });

        jLabel93.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel93.setText("Mã nhóm sản phẩm");

        jLabel94.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel94.setText("Tên nhóm sản phẩm");

        txtTimKH43.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH43.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH43ActionPerformed(evt);
            }
        });

        txtTimKH44.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH44.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH44ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogThemNhomSPLayout = new javax.swing.GroupLayout(jDialogThemNhomSP.getContentPane());
        jDialogThemNhomSP.getContentPane().setLayout(jDialogThemNhomSPLayout);
        jDialogThemNhomSPLayout.setHorizontalGroup(
            jDialogThemNhomSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogThemNhomSPLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jDialogThemNhomSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTimKH44)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogThemNhomSPLayout.createSequentialGroup()
                        .addGap(0, 169, Short.MAX_VALUE)
                        .addComponent(btnSuaKH3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnThemSP3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKH43))
                .addGap(15, 15, 15))
        );
        jDialogThemNhomSPLayout.setVerticalGroup(
            jDialogThemNhomSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogThemNhomSPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel93)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTimKH44, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel94)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTimKH43, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addGroup(jDialogThemNhomSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemSP3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSuaKH3))
                .addGap(14, 14, 14))
        );

        pnlCenterSuaSach.setBackground(new java.awt.Color(153, 153, 153));
        pnlCenterSuaSach.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));

        jPanel32.setBackground(new java.awt.Color(240, 242, 245));

        pnlTopLeft5.setBackground(new java.awt.Color(204, 204, 255));
        pnlTopLeft5.setLayout(new java.awt.BorderLayout());

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));

        pnlCenterKHchange5.setBackground(new java.awt.Color(250, 250, 250));
        pnlCenterKHchange5.setLayout(new java.awt.BorderLayout());

        jPanel34.setBackground(new java.awt.Color(250, 250, 250));
        jPanel34.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel17.setText("Thông tin sản phẩm");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel20.setText("Số lượng");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel26.setText("Tên sản phẩm");

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

        jLabel104.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel104.setText("(*)");

        jLabel105.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel105.setText("Mã sản phẩm");

        txtTimKH5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH5ActionPerformed(evt);
            }
        });
        txtTimKH5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH5KeyReleased(evt);
            }
        });

        jLabel107.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel107.setForeground(new java.awt.Color(3, 136, 253));
        jLabel107.setText("Nhà cung cấp");

        txtSuaSachChonNCC.setEditable(false);
        txtSuaSachChonNCC.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSuaSachChonNCC.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel108.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel108.setText("Nhóm sản phẩm");

        jComboBox8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel109.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel109.setText("Ngày tạo");

        jLabel110.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel110.setText("Mô tả");

        jLabel111.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel111.setForeground(new java.awt.Color(255, 0, 0));
        jLabel111.setText("(*)");

        jTextArea5.setColumns(20);
        jTextArea5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextArea5.setRows(5);
        jScrollPane5.setViewportView(jTextArea5);

        txtTimKH48.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH48.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH48ActionPerformed(evt);
            }
        });
        txtTimKH48.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH48KeyReleased(evt);
            }
        });

        txtTimKH49.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH49.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH49ActionPerformed(evt);
            }
        });

        txtTimKH50.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH50.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH50ActionPerformed(evt);
            }
        });
        txtTimKH50.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH50KeyReleased(evt);
            }
        });

        txtTimKH51.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH51.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH51ActionPerformed(evt);
            }
        });
        txtTimKH51.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH51KeyReleased(evt);
            }
        });

        jLabel112.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel112.setText("Tác giả");

        jLabel113.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel113.setText("Năm xuất bản");

        jLabel118.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel118.setText("Nhà xuất bản");

        txtTimKH52.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH52.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH52ActionPerformed(evt);
            }
        });
        txtTimKH52.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH52KeyReleased(evt);
            }
        });

        jLabel119.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel119.setText("Số trang");

        txtTimKH53.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH53.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH53ActionPerformed(evt);
            }
        });

        txtTimKH54.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH54.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH54ActionPerformed(evt);
            }
        });
        txtTimKH54.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH54KeyReleased(evt);
            }
        });

        jLabel120.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel120.setForeground(new java.awt.Color(3, 136, 253));
        jLabel120.setText("Đơn giá bán");

        jLabel121.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel121.setText("Giảm giá");

        txtTimKH56.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH56.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH56ActionPerformed(evt);
            }
        });

        jLabel122.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel122.setForeground(new java.awt.Color(3, 136, 253));
        jLabel122.setText("VAT");

        jLabel123.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel123.setText("Đơn giá nhập");

        txtTimKH57.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH57.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH57ActionPerformed(evt);
            }
        });
        txtTimKH57.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH57KeyReleased(evt);
            }
        });

        jDateChooser5.setDateFormatString("dd-MM-yyyy HH:mm:ss");
        jDateChooser5.setEnabled(false);
        jDateChooser5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        btnLuuNV7.setBackground(new java.awt.Color(3, 136, 253));
        btnLuuNV7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLuuNV7.setForeground(new java.awt.Color(255, 255, 255));
        btnLuuNV7.setText("Chọn");
        btnLuuNV7.setFocusable(false);
        btnLuuNV7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLuuNV7.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnLuuNV7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuNV7ActionPerformed(evt);
            }
        });

        jLabel152.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel152.setText("(*)");

        jLabel153.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel153.setForeground(new java.awt.Color(255, 0, 0));
        jLabel153.setText("(*)");

        jLabel154.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel154.setForeground(new java.awt.Color(255, 0, 0));
        jLabel154.setText("(*)");

        jLabel162.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel162.setForeground(new java.awt.Color(255, 0, 0));
        jLabel162.setText("(*)");

        jLabel163.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel163.setForeground(new java.awt.Color(255, 0, 0));
        jLabel163.setText("(*)");

        jLabel164.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel164.setForeground(new java.awt.Color(255, 0, 0));
        jLabel164.setText("(*)");

        jLabel165.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel165.setForeground(new java.awt.Color(255, 0, 0));
        jLabel165.setText("(*)");

        jLabel166.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel166.setForeground(new java.awt.Color(255, 0, 0));
        jLabel166.setText("(*)");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtTimKH5, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                                .addComponent(txtTimKH51)
                                .addComponent(txtTimKH50)
                                .addGroup(jPanel34Layout.createSequentialGroup()
                                    .addComponent(jLabel113)
                                    .addGap(36, 36, 36)
                                    .addComponent(jLabel153, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel34Layout.createSequentialGroup()
                                    .addComponent(jLabel118)
                                    .addGap(40, 40, 40)
                                    .addComponent(jLabel165, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addComponent(jLabel112)
                                .addGap(87, 87, 87)
                                .addComponent(jLabel162, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(78, 78, 78)
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel34Layout.createSequentialGroup()
                                        .addComponent(txtTimKH52)
                                        .addGap(101, 101, 101))
                                    .addGroup(jPanel34Layout.createSequentialGroup()
                                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtTimKH57, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel34Layout.createSequentialGroup()
                                                .addComponent(jLabel121)
                                                .addGap(43, 43, 43)
                                                .addComponent(jLabel163, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel34Layout.createSequentialGroup()
                                                .addComponent(jLabel119)
                                                .addGap(47, 47, 47)
                                                .addComponent(jLabel166, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel122, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTimKH53, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                                .addComponent(txtTimKH54, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtTimKH56, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                                .addComponent(jLabel123)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel164, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel120, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(246, 246, 246))))
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel108, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel107, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtTimKH48, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel34Layout.createSequentialGroup()
                                    .addComponent(jLabel105, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(62, 62, 62)
                                    .addComponent(jLabel104, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtTimKH49, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                                .addComponent(txtSuaSachChonNCC, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnLuuNV7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)
                                .addComponent(jLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel109, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtTimKH3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jDateChooser5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addComponent(jLabel110, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(jLabel152, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addGap(65, 65, 65)
                                .addComponent(jLabel154, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel104)
                    .addComponent(jLabel105)
                    .addComponent(jLabel26)
                    .addComponent(jLabel154))
                .addGap(18, 18, 18)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKH49, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKH3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel111)
                        .addComponent(jLabel109))
                    .addComponent(jLabel20))
                .addGap(12, 12, 12)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTimKH48, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel108)
                    .addComponent(jLabel110)
                    .addComponent(jLabel152))
                .addGap(12, 12, 12)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel107)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSuaSachChonNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLuuNV7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel112)
                    .addComponent(jLabel120)
                    .addComponent(jLabel123)
                    .addComponent(jLabel162)
                    .addComponent(jLabel164))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKH51, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKH56, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKH54, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel113)
                    .addComponent(jLabel122)
                    .addComponent(jLabel119)
                    .addComponent(jLabel153)
                    .addComponent(jLabel166))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKH5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKH53, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKH57, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel118)
                    .addComponent(jLabel121)
                    .addComponent(jLabel163)
                    .addComponent(jLabel165))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKH50, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKH52, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        List<NhomSanPham> dsNhomSP1 = dao_nsp.getAllNhomSanPham();
        for(NhomSanPham nsp : dsNhomSP1) {
            jComboBox8.addItem(nsp.getTenNhomSanPham());
        }

        pnlCenterKHchange5.add(jPanel34, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlTopLeft5.add(jPanel33, java.awt.BorderLayout.CENTER);

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));

        btnLuuNV1.setBackground(new java.awt.Color(3, 136, 253));
        btnLuuNV1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLuuNV1.setForeground(new java.awt.Color(255, 255, 255));
        btnLuuNV1.setText("Lưu");
        btnLuuNV1.setFocusable(false);
        btnLuuNV1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLuuNV1.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnLuuNV1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnLuuNV1ActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

        btnQuayLai7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnQuayLai7.setForeground(new java.awt.Color(3, 136, 253));
        btnQuayLai7.setText("Quay lại danh sách sản phẩm");
        btnQuayLai7.setFocusable(false);
        btnQuayLai7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnQuayLai7.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnQuayLai7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuayLai7ActionPerformed(evt);
            }
        });

        btnTaoMa2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTaoMa2.setForeground(new java.awt.Color(3, 136, 253));
        btnTaoMa2.setText("Tạo barcode");
        btnTaoMa2.setFocusable(false);
        btnTaoMa2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTaoMa2.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnTaoMa2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoMa2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnQuayLai7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHuy1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTaoMa2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLuuNV1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLuuNV1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHuy1)
                    .addComponent(btnQuayLai7)
                    .addComponent(btnTaoMa2))
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTopLeft5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(401, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlTopLeft5, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCenterSuaSach.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel36.setBackground(new java.awt.Color(250, 250, 250));
        jPanel36.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel36.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel124.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel124.setText("Danh sách sản phẩm");

        jLabel125.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel125.setText("Nguyễn Châu Tình - DESGIN");

        jLabel126.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel126.setText("> Sửa sản phẩm");

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel124)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel126)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 983, Short.MAX_VALUE)
                .addComponent(jLabel125)
                .addGap(19, 19, 19))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel124, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel125, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel126, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlCenterSuaSach.add(jPanel36, java.awt.BorderLayout.PAGE_START);

        pnlCenterSuaVPP.setBackground(new java.awt.Color(153, 153, 153));
        pnlCenterSuaVPP.setLayout(new java.awt.BorderLayout());

        jPanel43.setBackground(new java.awt.Color(153, 153, 153));

        jPanel44.setBackground(new java.awt.Color(240, 242, 245));

        pnlTopLeft7.setBackground(new java.awt.Color(204, 204, 255));
        pnlTopLeft7.setLayout(new java.awt.BorderLayout());

        jPanel45.setBackground(new java.awt.Color(255, 255, 255));

        pnlCenterKHchange7.setBackground(new java.awt.Color(250, 250, 250));
        pnlCenterKHchange7.setLayout(new java.awt.BorderLayout());

        jPanel46.setBackground(new java.awt.Color(250, 250, 250));
        jPanel46.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel127.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel127.setText("Thông tin sản phẩm");

        jLabel128.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel128.setText("Số lượng");

        jLabel129.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel129.setText("Tên sản phẩm");

        txtTimKH58.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH58.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH58ActionPerformed(evt);
            }
        });
        txtTimKH58.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH58KeyReleased(evt);
            }
        });

        jLabel130.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel130.setText("(*)");

        jLabel131.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel131.setText("Mã sản phẩm");

        jLabel132.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel132.setForeground(new java.awt.Color(255, 0, 0));
        jLabel132.setText("(*)");

        txtSuaVPPChonMau.setEditable(false);
        txtSuaVPPChonMau.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSuaVPPChonMau.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel133.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel133.setForeground(new java.awt.Color(3, 136, 253));
        jLabel133.setText("Nhà cung cấp");

        txtSuaVPPChonNCC.setEditable(false);
        txtSuaVPPChonNCC.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSuaVPPChonNCC.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel135.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel135.setText("Nhóm sản phẩm");

        jLabel136.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel136.setText("(*)");

        jComboBox9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel137.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel137.setText("Ngày tạo");

        jLabel138.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel138.setText("Mô tả");

        jLabel139.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel139.setForeground(new java.awt.Color(255, 0, 0));
        jLabel139.setText("(*)");

        jTextArea6.setColumns(20);
        jTextArea6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextArea6.setRows(5);
        jScrollPane6.setViewportView(jTextArea6);

        txtTimKH61.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH61.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH61ActionPerformed(evt);
            }
        });
        txtTimKH61.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH61KeyReleased(evt);
            }
        });

        btnLuuNV8.setBackground(new java.awt.Color(3, 136, 253));
        btnLuuNV8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLuuNV8.setForeground(new java.awt.Color(255, 255, 255));
        btnLuuNV8.setText("Chọn");
        btnLuuNV8.setFocusable(false);
        btnLuuNV8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLuuNV8.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnLuuNV8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuNV8ActionPerformed(evt);
            }
        });

        txtTimKH62.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH62.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH62ActionPerformed(evt);
            }
        });

        txtTimKH63.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH63.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH63ActionPerformed(evt);
            }
        });
        txtTimKH63.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH63KeyReleased(evt);
            }
        });

        jLabel140.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel140.setText("Nơi sản xuất");

        jLabel141.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel141.setForeground(new java.awt.Color(3, 136, 253));
        jLabel141.setText("Màu sắc");

        txtTimKH64.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH64.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH64ActionPerformed(evt);
            }
        });

        jLabel142.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel142.setForeground(new java.awt.Color(3, 136, 253));
        jLabel142.setText("Đơn giá bán");

        txtTimKH65.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH65.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH65.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH65ActionPerformed(evt);
            }
        });

        jLabel143.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel143.setForeground(new java.awt.Color(3, 136, 253));
        jLabel143.setText("VAT");

        btnChonMau2.setBackground(new java.awt.Color(3, 136, 253));
        btnChonMau2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnChonMau2.setForeground(new java.awt.Color(255, 255, 255));
        btnChonMau2.setText("Chọn");
        btnChonMau2.setFocusable(false);
        btnChonMau2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnChonMau2.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnChonMau2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonMau2ActionPerformed(evt);
            }
        });

        txtTimKH66.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH66.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH66ActionPerformed(evt);
            }
        });
        txtTimKH66.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH66KeyReleased(evt);
            }
        });

        jLabel144.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel144.setText("Đơn giá nhập");

        jDateChooser6.setDateFormatString("dd-MM-yyyy HH:mm:ss");
        jDateChooser6.setEnabled(false);
        jDateChooser6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel167.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel167.setForeground(new java.awt.Color(255, 0, 0));
        jLabel167.setText("(*)");

        jLabel168.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel168.setForeground(new java.awt.Color(255, 0, 0));
        jLabel168.setText("(*)");

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel46Layout.createSequentialGroup()
                        .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel46Layout.createSequentialGroup()
                                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel46Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(btnLuuNV8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtTimKH61, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnChonMau2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jComboBox9, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel46Layout.createSequentialGroup()
                                        .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel133, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtSuaVPPChonNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(60, 60, 60)
                                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                                    .addComponent(jDateChooser6, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                                    .addComponent(txtTimKH65, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                                    .addComponent(txtTimKH64, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                                    .addComponent(jLabel142, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel143, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel137, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel46Layout.createSequentialGroup()
                                        .addComponent(jLabel138, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(82, 82, 82)
                                        .addComponent(jLabel136, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtTimKH58)))
                            .addGroup(jPanel46Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel129, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(467, 467, 467)))
                        .addGap(277, 277, 277))
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel46Layout.createSequentialGroup()
                                    .addComponent(jLabel140, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(52, 52, 52)
                                    .addComponent(jLabel168, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel46Layout.createSequentialGroup()
                                    .addComponent(jLabel144, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(56, 56, 56)
                                    .addComponent(jLabel167, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtTimKH66, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTimKH63, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtSuaVPPChonMau, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel141, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel135, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel46Layout.createSequentialGroup()
                                .addComponent(jLabel128, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)
                                .addComponent(jLabel139, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel46Layout.createSequentialGroup()
                                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel127)
                                    .addGroup(jPanel46Layout.createSequentialGroup()
                                        .addComponent(jLabel131, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(62, 62, 62)
                                        .addComponent(jLabel130, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtTimKH62, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(267, 267, 267)
                                .addComponent(jLabel132, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel127, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel130)
                                .addComponent(jLabel131))
                            .addComponent(jLabel129, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addComponent(txtTimKH62, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addComponent(jLabel132)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTimKH58, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel137)
                    .addComponent(jLabel139)
                    .addComponent(jLabel128))
                .addGap(12, 12, 12)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addComponent(txtTimKH61, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel138)
                                .addComponent(jLabel136))
                            .addComponent(jLabel135))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel46Layout.createSequentialGroup()
                                .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel133)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtSuaVPPChonNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnLuuNV8, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jDateChooser6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel142)
                    .addComponent(jLabel144)
                    .addComponent(jLabel167))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKH65, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKH66, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel143)
                    .addComponent(jLabel140)
                    .addComponent(jLabel168))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKH64, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKH63, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jLabel141)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSuaVPPChonMau, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChonMau2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        List<NhomSanPham> dsNhomSP5 = dao_nsp.getAllNhomSanPham();
        for(NhomSanPham nsp : dsNhomSP5) {
            jComboBox9.addItem(nsp.getTenNhomSanPham());
        }

        pnlCenterKHchange7.add(jPanel46, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange7, javax.swing.GroupLayout.PREFERRED_SIZE, 1262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCenterKHchange7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlTopLeft7.add(jPanel45, java.awt.BorderLayout.CENTER);

        jPanel47.setBackground(new java.awt.Color(255, 255, 255));

        btnLuuNV9.setBackground(new java.awt.Color(3, 136, 253));
        btnLuuNV9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLuuNV9.setForeground(new java.awt.Color(255, 255, 255));
        btnLuuNV9.setText("Lưu");
        btnLuuNV9.setFocusable(false);
        btnLuuNV9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLuuNV9.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnLuuNV9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnLuuNV9ActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnHuy3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHuy3.setForeground(new java.awt.Color(255, 77, 77));
        btnHuy3.setText("Hủy");
        btnHuy3.setFocusable(false);
        btnHuy3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHuy3.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnHuy3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuy3ActionPerformed(evt);
            }
        });

        btnQuayLai8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnQuayLai8.setForeground(new java.awt.Color(3, 136, 253));
        btnQuayLai8.setText("Quay lại danh sách sản phẩm");
        btnQuayLai8.setFocusable(false);
        btnQuayLai8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnQuayLai8.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnQuayLai8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuayLai8ActionPerformed(evt);
            }
        });

        btnTaoMa3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTaoMa3.setForeground(new java.awt.Color(3, 136, 253));
        btnTaoMa3.setText("Tạo barcode");
        btnTaoMa3.setFocusable(false);
        btnTaoMa3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTaoMa3.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnTaoMa3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoMa3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnQuayLai8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHuy3, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTaoMa3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLuuNV9, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLuuNV9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHuy3)
                    .addComponent(btnQuayLai8)
                    .addComponent(btnTaoMa3))
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTopLeft7, javax.swing.GroupLayout.PREFERRED_SIZE, 1273, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlTopLeft7, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCenterSuaVPP.add(jPanel43, java.awt.BorderLayout.CENTER);

        jPanel48.setBackground(new java.awt.Color(250, 250, 250));
        jPanel48.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel48.setPreferredSize(new java.awt.Dimension(1690, 56));

        jLabel145.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel145.setText("Danh sách sản phẩm");

        jLabel146.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel146.setText("Nguyễn Châu Tình - DESGIN");

        jLabel147.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel147.setText("> Sửa sản phẩm");

        javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel48Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel145)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel147)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 983, Short.MAX_VALUE)
                .addComponent(jLabel146)
                .addGap(19, 19, 19))
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel145, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel146, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel147, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlCenterSuaVPP.add(jPanel48, java.awt.BorderLayout.PAGE_START);

        jDialogChonNCC.setBackground(new java.awt.Color(250, 250, 250));
        jDialogChonNCC.setResizable(false);
        jDialogChonNCC.setSize(new java.awt.Dimension(786, 437));

        pnlChonNCC.setBackground(new java.awt.Color(250, 250, 250));
        pnlChonNCC.setLayout(new java.awt.BorderLayout());

        menuScrollPane9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menuScrollPane9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tableChonNCC.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableChonNCC.setModel(new javax.swing.table.DefaultTableModel(
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
                "STT", "Mã nhà cung cấp", "Tên nhà cung cấp", "Số điện thoại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableChonNCC.setGridColor(new java.awt.Color(255, 255, 255));
        tableChonNCC.setRowHeight(40);
        tableChonNCC.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableChonNCC.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableChonNCC.getTableHeader().setReorderingAllowed(false);
        tableChonNCC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableChonNCCMouseClicked(evt);
            }
        });
        menuScrollPane9.setViewportView(tableChonNCC);
        if (tableChonNCC.getColumnModel().getColumnCount() > 0) {
            tableChonNCC.getColumnModel().getColumn(0).setMinWidth(40);
            tableChonNCC.getColumnModel().getColumn(0).setMaxWidth(40);
            tableChonNCC.getColumnModel().getColumn(3).setHeaderValue("Số điện thoại");
        }

        pnlChonNCC.add(menuScrollPane9, java.awt.BorderLayout.CENTER);

        jPanel49.setBackground(new java.awt.Color(255, 255, 255));

        jLabel148.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel148.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/magnifying-glass.png"))); // NOI18N

        txtTimNCC.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimNCC.setText("Nhập vào thông tin tìm kiếm...");
        txtTimNCC.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimNCC.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimNCCFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTimNCCFocusLost(evt);
            }
        });
        txtTimNCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimNCCActionPerformed(evt);
            }
        });
        txtTimNCC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimNCCKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel49Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel148, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(847, Short.MAX_VALUE))
            .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel49Layout.createSequentialGroup()
                    .addGap(87, 87, 87)
                    .addComponent(txtTimNCC, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel49Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel148, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel49Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(txtTimNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pnlChonNCC.add(jPanel49, java.awt.BorderLayout.PAGE_START);

        jDialogChonNCC.getContentPane().add(pnlChonNCC, java.awt.BorderLayout.CENTER);

        jDialogChonMau.setResizable(false);
        jDialogChonMau.setSize(new java.awt.Dimension(462, 300));

        pnlChonNCC2.setLayout(new java.awt.BorderLayout());

        menuScrollPane11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menuScrollPane11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tableChonMau.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableChonMau.setModel(new javax.swing.table.DefaultTableModel(
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
                "STT", "Mã màu", "Tên màu"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableChonMau.setGridColor(new java.awt.Color(255, 255, 255));
        tableChonMau.setRowHeight(40);
        tableChonMau.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableChonMau.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableChonMau.setShowGrid(false);
        tableChonMau.getTableHeader().setReorderingAllowed(false);
        tableChonMau.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableChonMauMouseClicked(evt);
            }
        });
        menuScrollPane11.setViewportView(tableChonMau);
        if (tableChonMau.getColumnModel().getColumnCount() > 0) {
            tableChonMau.getColumnModel().getColumn(0).setMinWidth(40);
            tableChonMau.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        pnlChonNCC2.add(menuScrollPane11, java.awt.BorderLayout.CENTER);

        jPanel50.setBackground(new java.awt.Color(250, 250, 250));

        jLabel149.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel149.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/magnifying-glass.png"))); // NOI18N

        txtTimKH2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKH2.setText("Nhập vào thông tin tìm kiếm...");
        txtTimKH2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimKH2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimKH2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTimKH2FocusLost(evt);
            }
        });
        txtTimKH2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKH2ActionPerformed(evt);
            }
        });
        txtTimKH2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKH2KeyReleased(evt);
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
                    .addGap(89, 89, 89)
                    .addComponent(txtTimKH2, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
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
                    .addComponent(txtTimKH2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pnlChonNCC2.add(jPanel50, java.awt.BorderLayout.PAGE_START);

        jDialogChonMau.getContentPane().add(pnlChonNCC2, java.awt.BorderLayout.CENTER);

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
        jLabel5.setText("Thông tin sản phẩm");

        txtTimSP.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimSP.setText("Nhập vào thông tin tìm kiếm...");
        txtTimSP.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTimSP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimSPFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTimSPFocusLost(evt);
            }
        });
        txtTimSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimSPActionPerformed(evt);
            }
        });
        txtTimSP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimSPKeyReleased(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Còn hàng", "Hết hàng" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/magnifying-glass.png"))); // NOI18N

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
                        .addComponent(txtTimSP, javax.swing.GroupLayout.PREFERRED_SIZE, 746, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTatCa, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnTatCa, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTimSP, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCenterKHchange.setBackground(new java.awt.Color(250, 250, 250));
        pnlCenterKHchange.setLayout(new java.awt.BorderLayout());

        pnlInit.setBackground(new java.awt.Color(250, 250, 250));

        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Nhóm sản phẩm", "Số lượng tồn", "Ngày tạo", "Tình trạng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
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

        javax.swing.GroupLayout pnlInitLayout = new javax.swing.GroupLayout(pnlInit);
        pnlInit.setLayout(pnlInitLayout);
        pnlInitLayout.setHorizontalGroup(
            pnlInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInitLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(menuScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        pnlInitLayout.setVerticalGroup(
            pnlInitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInitLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(menuScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pnlCenterKHchange.add(pnlInit, java.awt.BorderLayout.CENTER);

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

        btnThemSP.setBackground(new java.awt.Color(3, 136, 253));
        btnThemSP.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemSP.setForeground(new java.awt.Color(255, 255, 255));
        btnThemSP.setText("Thêm sản phẩm");
        btnThemSP.setFocusable(false);
        btnThemSP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemSP.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnThemSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSPActionPerformed(evt);
            }
        });

        btnNhapFile.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNhapFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/down-arrow.png"))); // NOI18N
        btnNhapFile.setText("Nhập file");
        btnNhapFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNhapFileMouseClicked(evt);
            }
        });

        btnXuatFile.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXuatFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconMenu/up-arrow.png"))); // NOI18N
        btnXuatFile.setText("Xuất file");
        btnXuatFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnXuatFileMouseClicked(evt);
            }
        });

        btnSuaKH1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuaKH1.setForeground(new java.awt.Color(3, 136, 253));
        btnSuaKH1.setText("Nhóm sản phẩm");
        btnSuaKH1.setFocusable(false);
        btnSuaKH1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSuaKH1.setMargin(new java.awt.Insets(2, 10, 3, 10));
        btnSuaKH1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaKH1ActionPerformed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSuaKH1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(btnThemSP, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNhapFile)
                    .addComponent(btnXuatFile)
                    .addComponent(btnSuaKH1))
                .addGap(8, 8, 8))
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
        jLabel6.setText("Danh sách sản phẩm");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1186, Short.MAX_VALUE)
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
                    .addComponent(jLabel11))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCenter.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        pnlChange.add(pnlCenter, java.awt.BorderLayout.CENTER);

        add(pnlChange, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void lblNameLoginAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblNameLoginAncestorAdded
        // TODO add your handling code here:
        lblNameLogin.setText(gui.FrmLogin.tenNguoiDung);
    }//GEN-LAST:event_lblNameLoginAncestorAdded

    private void btnTaoMa2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoMa2ActionPerformed
        // TODO add your handling code here:
        try {
            Linear barcode = new Linear();
            barcode.setType(Linear.CODE128B);
            barcode.setData(txtTimKH49.getText());
            barcode.setI(11.0f);
            String fname = txtTimKH49.getText();
            barcode.renderBarcode("src/img/" + fname + ".png");
            JOptionPane.showMessageDialog(null, "Tạo thành công");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Tạo thất bại");
        }
    }//GEN-LAST:event_btnTaoMa2ActionPerformed

    private void btnTaoMa3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoMa3ActionPerformed
        // TODO add your handling code here:
        try {
            Linear barcode = new Linear();
            barcode.setType(Linear.CODE128B);
            barcode.setData(txtTimKH62.getText());
            barcode.setI(11.0f);
            String fname = txtTimKH62.getText();
            barcode.renderBarcode("src/img/" + fname + ".png");
            JOptionPane.showMessageDialog(null, "Tạo thành công");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Tạo thất bại");
        }
    }//GEN-LAST:event_btnTaoMa3ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBox1ActionPerformed
        timCbSp();
    }// GEN-LAST:event_jComboBox1ActionPerformed

    private void txtThemNCCSachKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtThemNCCSachKeyReleased
        try {
            String inputText = txtThemDGNSach.getText().trim();
            if (!inputText.isEmpty()) {
                double d = Double.parseDouble(inputText);
                double result = d * 1.5;
                String b = Double.toString(result);
                txtThemDGBSach.setText(b);
            } else {
                txtThemDGBSach.setText("");
            }
        } catch (NumberFormatException e) {
            txtThemDGBSach.setText("");
        }
    }// GEN-LAST:event_txtThemNCCSachKeyReleased

    private void txtThemDGNSachKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtThemDGNSachKeyReleased
        try {
            String dgn = txtThemDGNSach.getText().trim();
            if (!dgn.isEmpty()) {
                double donGia = Double.parseDouble(dgn);
                if (!(donGia > 0)) {
                    jLabel158.setText("đơn giá nhập phải > 0");
                } else {
                    double d = Double.parseDouble(dgn);
                    double result = d * 1.5;
                    String b = Double.toString(result);
                    txtThemDGBSach.setText(b);
                    jLabel158.setText("");
                }
            } else {
                txtThemDGBSach.setText("");
                jLabel158.setText("không được để trống");
            }
        } catch (NumberFormatException e) {
            txtThemDGBSach.setText("");
            jLabel158.setText("không chứa kí tự");
        }
    }// GEN-LAST:event_txtThemDGNSachKeyReleased

    private void txtThemTenSPSachKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtThemTenSPSachKeyReleased
        try {
            String ten = txtThemTenSPSach.getText().trim();
            if (!ten.isEmpty()) {
                if (!(ten.length() > 0 && ten.matches("^\\p{Lu}[\\p{L} .'-[0-9]*]*$"))) {
                    jLabel24.setText("chuỗi bắt đầu bằng chữ hoa không chứa kí tự đặc biệt");
                } else {
                    jLabel24.setText("");
                }
            } else {
                jLabel24.setText("tên sản phẩm không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel24.setText("không phải là số");
        }
    }// GEN-LAST:event_txtThemTenSPSachKeyReleased

    private void txtThemSLSachKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtThemSLSachKeyReleased
        try {
            String txtSluong = txtThemSLSach.getText().trim();
            if (!txtSluong.isEmpty()) {
                int sl = Integer.parseInt(txtSluong);
                if (!(sl > 0)) {
                    jLabel161.setText("số lượng phải > 0");
                } else {
                    jLabel161.setText("");
                }
            } else {
                jLabel161.setText("số lượng không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel161.setText(" số lượng không chứa kí tự");
        }
    }// GEN-LAST:event_txtThemSLSachKeyReleased

    private void txtThemTGSachKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtThemTGSachKeyReleased
        try {
            String tacGia = txtThemTGSach.getText().trim();
            if (!tacGia.isEmpty()) {
                if (!(tacGia.length() > 0 && tacGia.matches("^\\p{Lu}[\\p{L} ,.'-[0-9]*]*$"))) {
                    jLabel155.setText("chuỗi bắt đầu bằng chữ hoa không chứa kí tự đặc biệ");
                } else {
                    jLabel155.setText("");
                }
            } else {
                jLabel155.setText("tên sản phẩm không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel155.setText("không phải là số");
        }
    }// GEN-LAST:event_txtThemTGSachKeyReleased

    private void txtThemNSBSachKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtThemNSBSachKeyReleased
        try {
            String nsb = txtThemNSBSach.getText().trim();
            if (!nsb.isEmpty()) {
                int nam = Integer.parseInt(nsb);
                if (!(nam > 0 && nam <= LocalDate.now().getYear())) {
                    jLabel156.setText("năm xuất bản phải > 0 và nhỏ hơn năm hiện tại");
                } else {
                    jLabel156.setText("");
                }
            } else {
                jLabel156.setText("năm xuất bản không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel156.setText("năm xuất bản không chứa kí tự");
        }
    }// GEN-LAST:event_txtThemNSBSachKeyReleased

    private void txtThemSoTrangSachKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtThemSoTrangSachKeyReleased
        try {
            String soTrang = txtThemSoTrangSach.getText().trim();
            if (!soTrang.isEmpty()) {
                int trang = Integer.parseInt(soTrang);
                if (!(trang > 0)) {
                    jLabel160.setText("số trang phải > 0");
                } else {
                    jLabel160.setText("");
                }
            } else {
                jLabel160.setText("số trang không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel160.setText("số trang không chứa kí tự");
        }
    }// GEN-LAST:event_txtThemSoTrangSachKeyReleased

    private void txtThemNhaXBSachKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtThemNhaXBSachKeyReleased
        try {
            String nhaXB = txtThemNhaXBSach.getText().trim();
            if (!nhaXB.isEmpty()) {
                if (!(nhaXB.length() > 0 && nhaXB.matches("^\\p{Lu}[\\p{L} .'-]*$"))) {
                    jLabel157.setText("chuỗi bắt đầu bằng chữ hoa không chứa kí tự đặc biệ");
                } else {
                    jLabel157.setText("");
                }
            } else {
                jLabel157.setText("nhà xuất bản không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel157.setText("không phải là số");
        }
    }// GEN-LAST:event_txtThemNhaXBSachKeyReleased

    private void txtThemGiamGiaSachKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtThemGiamGiaSachKeyReleased
        try {
            String giamGia = txtThemGiamGiaSach.getText().trim();
            if (!giamGia.isEmpty()) {
                double giam = Double.parseDouble(giamGia);
                if (!(giam >= 0 && giam <= 1)) {
                    jLabel159.setText("giảm giá phải [0 -> 1]");
                } else {
                    jLabel159.setText("");
                }
            } else {
                jLabel159.setText("giảm giá không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel159.setText("giảm giá không chứa kí tự");
        }
    }// GEN-LAST:event_txtThemGiamGiaSachKeyReleased

    private void txtTimKH18KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH18KeyReleased
        try {
            String ten = txtTimKH18.getText().trim();
            if (!ten.isEmpty()) {
                if (!(ten.length() > 0 && ten.matches("^\\p{Lu}[\\p{L} .'-[0-9]*]*$"))) {
                    jLabel52.setText("chuỗi bắt đầu bằng chữ hoa không chứa kí tự đặc biệ");
                } else {
                    jLabel52.setText("");
                }
            } else {
                jLabel52.setText("tên sản phẩm không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel52.setText("không phải là số");
        }
    }// GEN-LAST:event_txtTimKH18KeyReleased

    private void txtTimKH22KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH22KeyReleased
        try {
            String txtSluong = txtTimKH22.getText().trim();
            if (!txtSluong.isEmpty()) {
                int sl = Integer.parseInt(txtSluong);
                if (!(sl > 0)) {
                    jLabel59.setText("số lượng phải > 0");
                } else {
                    jLabel59.setText("");
                }
            } else {
                jLabel59.setText("số lượng không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel59.setText(" số lượng không chứa kí tự");
        }
    }// GEN-LAST:event_txtTimKH22KeyReleased

    private void txtTimKH25KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH25KeyReleased
        try {
            String noiSx = txtTimKH25.getText().trim();
            if (!noiSx.isEmpty()) {
                if (!(noiSx.length() > 0 && noiSx.matches("^\\p{Lu}[\\p{L} .'-]*$"))) {
                    jLabel151.setText("chuỗi bắt đầu bằng chữ hoa không chứa kí tự đặc biệ");
                } else {
                    jLabel151.setText("");
                }
            } else {
                jLabel151.setText("nơi sản xuất không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel151.setText("không phải là số");
        }
    }// GEN-LAST:event_txtTimKH25KeyReleased

    private void txtTimKH3KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH3KeyReleased
        try {
            String ten = txtTimKH3.getText().trim();
            if (!ten.isEmpty()) {
                if (!(ten.length() > 0 && ten.matches("^\\p{Lu}[\\p{L} .'-[0-9]*]*$"))) {
                    jLabel154.setText("chuỗi bắt đầu bằng chữ hoa không chứa kí tự đặc biệ");
                } else {
                    jLabel154.setText("");
                }
            } else {
                jLabel154.setText("tên sản phẩm không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel154.setText("không phải là số");
        }
    }// GEN-LAST:event_txtTimKH3KeyReleased

    private void txtTimKH48KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH48KeyReleased
        try {
            String txtSluong = txtTimKH48.getText().trim();
            if (!txtSluong.isEmpty()) {
                int sl = Integer.parseInt(txtSluong);
                if (!(sl > 0)) {
                    jLabel111.setText("số lượng phải > 0");
                } else {
                    jLabel111.setText("");
                }
            } else {
                jLabel111.setText("số lượng không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel111.setText(" số lượng không chứa kí tự");
        }
    }// GEN-LAST:event_txtTimKH48KeyReleased

    private void txtTimKH51KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH51KeyReleased
        try {
            String tacGia = txtTimKH51.getText().trim();
            if (!tacGia.isEmpty()) {
                if (!(tacGia.length() > 0 && tacGia.matches("^\\p{Lu}[\\p{L} .'-[0-9]*]*$"))) {
                    jLabel162.setText("chuỗi bắt đầu bằng chữ hoa không chứa kí tự đặc biệ");
                } else {
                    jLabel162.setText("");
                }
            } else {
                jLabel162.setText("tên sản phẩm không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel162.setText("không phải là số");
        }
    }// GEN-LAST:event_txtTimKH51KeyReleased

    private void txtTimKH54KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH54KeyReleased
        try {
            String dgn = txtTimKH54.getText().trim();
            if (!dgn.isEmpty()) {
                double donGia = Double.parseDouble(dgn);
                if (!(donGia > 0)) {
                    jLabel164.setText("đơn giá nhập phải > 0");
                } else {
                    double d = Double.parseDouble(dgn);
                    double result = d * 1.5;
                    String b = Double.toString(result);
                    txtTimKH56.setText(b);
                    jLabel164.setText("");
                }
            } else {
                txtTimKH56.setText("");
                jLabel164.setText("không được để trống");
            }
        } catch (NumberFormatException e) {
            txtTimKH56.setText("");
            jLabel164.setText("không chứa kí tự");
        }
    }// GEN-LAST:event_txtTimKH54KeyReleased

    private void txtTimKH5KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH5KeyReleased
        try {
            String nsb = txtTimKH5.getText().trim();
            if (!nsb.isEmpty()) {
                int nam = Integer.parseInt(nsb);
                if (!(nam > 0 && nam <= LocalDate.now().getYear())) {
                    jLabel153.setText("năm xuất bản phải > 0 và nhỏ hơn năm hiện tại");
                } else {
                    jLabel153.setText("");
                }
            } else {
                jLabel153.setText("năm xuất bản không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel153.setText("năm xuất bản không chứa kí tự");
        }
    }// GEN-LAST:event_txtTimKH5KeyReleased

    private void txtTimKH57KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH57KeyReleased
        try {
            String soTrang = txtTimKH57.getText().trim();
            if (!soTrang.isEmpty()) {
                int trang = Integer.parseInt(soTrang);
                if (!(trang > 0)) {
                    jLabel166.setText("số trang phải > 0");
                } else {
                    jLabel166.setText("");
                }
            } else {
                jLabel166.setText("số trang không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel166.setText("số trang không chứa kí tự");
        }
    }// GEN-LAST:event_txtTimKH57KeyReleased

    private void txtTimKH50KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH50KeyReleased
        try {
            String nhaXB = txtTimKH50.getText().trim();
            if (!nhaXB.isEmpty()) {
                if (!(nhaXB.length() > 0 && nhaXB.matches("^(?:[A-ZÀ-ỸẠ-Ỵ][a-zà-ỹạ-ỵ]*\\s?)+$"))) {
                    jLabel165.setText("chuỗi bắt đầu bằng chữ hoa không chứa kí tự đặc biệ");
                } else {
                    jLabel165.setText("");
                }
            } else {
                jLabel165.setText("nhà xuất bản không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel165.setText("không phải là số");
        }
    }// GEN-LAST:event_txtTimKH50KeyReleased

    private void txtTimKH52KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH52KeyReleased
        try {
            String giamGia = txtTimKH52.getText().trim();
            if (!giamGia.isEmpty()) {
                double giam = Double.parseDouble(giamGia);
                if (!(giam >= 0 && giam <= 1)) {
                    jLabel163.setText("giảm giá phải [0 -> 1]");
                } else {
                    jLabel163.setText("");
                }
            } else {
                jLabel163.setText("giảm giá không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel163.setText("giảm giá không chứa kí tự");
        }
    }// GEN-LAST:event_txtTimKH52KeyReleased

    private void txtTimKH58KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH58KeyReleased
        try {
            String ten = txtTimKH58.getText().trim();
            if (!ten.isEmpty()) {
                if (!(ten.length() > 0 && ten.matches("^\\p{Lu}[\\p{L} .'-[0-9]*]*$"))) {
                    jLabel132.setText("chuỗi bắt đầu bằng chữ hoa không chứa kí tự đặc biệ");
                } else {
                    jLabel132.setText("");
                }
            } else {
                jLabel132.setText("tên sản phẩm không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel132.setText("không phải là số");
        }
    }// GEN-LAST:event_txtTimKH58KeyReleased

    private void txtTimKH61KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH61KeyReleased
        try {
            String txtSluong = txtTimKH61.getText().trim();
            if (!txtSluong.isEmpty()) {
                int sl = Integer.parseInt(txtSluong);
                if (!(sl > 0)) {
                    jLabel139.setText("số lượng phải > 0");
                } else {
                    jLabel139.setText("");
                }
            } else {
                jLabel139.setText("số lượng không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel139.setText(" số lượng không chứa kí tự");
        }
    }// GEN-LAST:event_txtTimKH61KeyReleased

    private void txtTimKH66KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH66KeyReleased
        try {
            String dgn = txtTimKH66.getText().trim();
            if (!dgn.isEmpty()) {
                double donGia = Double.parseDouble(dgn);
                if (!(donGia > 0)) {
                    jLabel167.setText("đơn giá nhập phải > 0");
                } else {
                    double d = Double.parseDouble(dgn);
                    double result = d * 1.5;
                    String b = Double.toString(result);
                    txtTimKH65.setText(b);
                    jLabel167.setText("");
                }
            } else {
                txtTimKH65.setText("");
                jLabel164.setText("không được để trống");
            }
        } catch (NumberFormatException e) {
            txtTimKH65.setText("");
            jLabel167.setText("không chứa kí tự");
        }
    }// GEN-LAST:event_txtTimKH66KeyReleased

    private void txtTimKH63KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH63KeyReleased
        try {
            String noiSx = txtTimKH63.getText().trim();
            if (!noiSx.isEmpty()) {
                if (!(noiSx.length() > 0 && noiSx.matches("^(?:[A-ZÀ-ỸẠ-Ỵ][a-zà-ỹạ-ỵ]*\\s?)+$"))) {
                    jLabel168.setText("chuỗi bắt đầu bằng chữ hoa không chứa kí tự đặc biệ");
                } else {
                    jLabel168.setText("");
                }
            } else {
                jLabel168.setText("nơi sản xuất không được để trống");
            }
        } catch (NumberFormatException e) {
            jLabel168.setText("không phải là số");
        }
    }// GEN-LAST:event_txtTimKH63KeyReleased

    private void btnNhapFileMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_btnNhapFileMouseClicked
        importExcel();
        try {
			loadData();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }// GEN-LAST:event_btnNhapFileMouseClicked

    private void txtTimSPMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_txtTimSPMouseClicked
        if (evt.getClickCount() == 1 && !evt.isConsumed()) {
            evt.consume();
//            timJtable2();
        }
    }// GEN-LAST:event_txtTimSPMouseClicked

    private void txtTimKH2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH2ActionPerformed
        // TODO add your handling code here
    }// GEN-LAST:event_txtTimKH2ActionPerformed

    private void txtTimKH2FocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtTimKH2FocusGained
        // TODO add your handling code here:
        frm.placeHoderTextGianed(txtTimKH2);
    }// GEN-LAST:event_txtTimKH2FocusGained

    private void txtTimKH2FocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtTimKH2FocusLost
        // TODO add your handling code here:
        frm.placeHoderTextLost(txtTimKH2);
    }// GEN-LAST:event_txtTimKH2FocusLost

    private void txtTimNCCFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtTimNCCFocusGained
        // TODO add your handling code here:
        frm.placeHoderTextGianed(txtTimNCC);
    }// GEN-LAST:event_txtTimNCCFocusGained

    private void txtTimNCCFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtTimNCCFocusLost
        // TODO add your handling code here:
        frm.placeHoderTextLost(txtTimNCC);
    }// GEN-LAST:event_txtTimNCCFocusLost

    private void txtTimNCCActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimNCCActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimNCCActionPerformed

    private void tableChonNCCMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tableChonNCCMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            int row = tableChonNCC.getSelectedRow();
            NhaCungCap ncc;
			try {
				ncc = dao_ncc.getNCCTheoMa(tableChonNCC.getValueAt(row, 1).toString());
				txtThemNCCSach.setText(ncc.getTenNCC());
	            txtThemNCCVPP.setText(ncc.getTenNCC());
	            txtSuaSachChonNCC.setText(ncc.getTenNCC());
	            txtSuaVPPChonNCC.setText(ncc.getTenNCC());
	            jDialogChonNCC.setVisible(false);
	            MANCC = tableChonNCC.getValueAt(row, 1).toString();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
    }// GEN-LAST:event_tableChonNCCMouseClicked

    private void txtTimSPKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimSPKeyReleased
        timJtable2();
    }// GEN-LAST:event_txtTimSPKeyReleased

    private void btnXuatFileMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_btnXuatFileMouseClicked
        exportExcel();
    }// GEN-LAST:event_btnXuatFileMouseClicked

    private void tableChonMauMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tableChonMauMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            int row = tableChonMau.getSelectedRow();
            MauSac mau;
			try {
				mau = dao_mausac.getMauSactheoMa(tableChonMau.getValueAt(row, 1).toString());
				txtMauSac.setText(mau.getMaMau());
	            txtSuaVPPChonMau.setText(mau.getMaMau());
	            jDialogChonMau.setVisible(false);
	            MAMAU = tableChonMau.getValueAt(row, 1).toString();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            

        }
    }// GEN-LAST:event_tableChonMauMouseClicked

    private void txtTimKH35KeyReleased(java.awt.event.KeyEvent evt) {
        try {
            String dgn = txtTimKH35.getText().trim();
            if (!dgn.isEmpty()) {
                double donGia = Double.parseDouble(dgn);
                if (!(donGia > 0)) {
                    jLabel50.setText("đơn giá nhập phải > 0");
                } else {
                    double d = Double.parseDouble(dgn);
                    double result = d * 1.5;
                    String b = Double.toString(result);
                    txtTimKH29.setText(b);
                    jLabel50.setText("");
                }
            } else {
                txtTimKH29.setText("");
                jLabel50.setText("không được để trống");
            }
        } catch (NumberFormatException e) {
            txtTimKH29.setText("");
            jLabel50.setText("không chứa kí tự");
        }
    }// GEN-LAST:event_txtTimKH35KeyReleased

    private void txtTimNCCKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimNCCKeyReleased
        timJtableChonNCC();
    }// GEN-LAST:event_txtTimNCCKeyReleased

    private void txtTimKH2KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKH2KeyReleased
        timJtableChonMau();
    }// GEN-LAST:event_txtTimKH2KeyReleased

    private void txtTimSPActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimSPActionPerformed
        // TODO add your handling code here:

    }// GEN-LAST:event_txtTimSPActionPerformed

    private void btnTatCaActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnTatCaActionPerformed
        // TODO add your handling code here:

    	
        int row = jTable2.getSelectedRow();
        int	rowChooser = jTable2.convertRowIndexToModel(row);
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();

        String maSP = model.getValueAt(rowChooser, 1).toString();
        if (maSP.startsWith("S")) {
            showPanelChange(pnlChange, pnlCenterSuaSach);
            Sach s = dao_sach.getSachtheoMa(maSP);
//            SanPham s = dao_sp.getSanPhamtheoMa(maSP);
            txtTimKH49.setText(s.getMaSanPham());
            txtTimKH3.setText(s.getTenSanPham());
            txtTimKH48.setText(s.getSoLuongTon() + "");
            Instant inStantDate = s.getNgayTao().atZone(ZoneId.systemDefault()).toInstant();
            Date dateNgayTao = Date.from(inStantDate);
            jDateChooser5.setDate(dateNgayTao);
            NhomSanPham nsp = dao_nsp.getNsptheoTen(model.getValueAt(rowChooser, 3).toString());
            jComboBox8.setSelectedItem(nsp.getTenNhomSanPham());
            jTextArea5.setText(s.getMoTa());
            
            NhaCungCap ncc = dao_ncc.getNCCTheoMa(s.getNhaCungCap().getMaNCC());
            txtSuaSachChonNCC.setText(ncc.getTenNCC());
            
            
            txtTimKH51.setText(s.getTacGia());
            txtTimKH54.setText(Double.toString(s.getDonGiaNhap()));
            txtTimKH56.setText(Double.toString(s.getDonGiaBan()));
            txtTimKH5.setText(Integer.toString(s.getNamXuatBan()));
            txtTimKH57.setText(Integer.toString(s.getSoTrang()));
            txtTimKH53.setText(Double.toString(s.getVAT()));
            txtTimKH50.setText(s.getNhaXuatBan());
            txtTimKH52.setText(Double.toString(s.getGiamGia()));
            // set (*) -> ""
            jLabel154.setText("");
            jLabel111.setText("");
            jLabel162.setText("");
            jLabel164.setText("");
            jLabel153.setText("");
            jLabel166.setText("");
            jLabel165.setText("");
            jLabel163.setText("");
        } else {
            showPanelChange(pnlChange, pnlCenterSuaVPP);
            VanPhongPham vpp = dao_vpp.getVPPtheoMa(maSP);
            txtTimKH62.setText(vpp.getMaSanPham());
            txtTimKH62.setEditable(false);
            txtTimKH58.setText(vpp.getTenSanPham());
            txtTimKH61.setText(vpp.getSoLuongTon() + "");
            Instant inStantDate = vpp.getNgayTao().atZone(ZoneId.systemDefault()).toInstant();
            Date dateNgayTao = Date.from(inStantDate);
            jDateChooser6.setDate(dateNgayTao);
            NhomSanPham nsp = dao_nsp.getNsptheoTen(model.getValueAt(rowChooser, 3).toString());
            jComboBox9.setSelectedItem(nsp.getTenNhomSanPham());
            jTextArea6.setText(vpp.getMoTa());
            
            NhaCungCap ncc = dao_ncc.getNCCTheoMa(vpp.getNhaCungCap().getMaNCC());
            txtSuaVPPChonNCC.setText(ncc.getTenNCC());
            
            txtTimKH66.setText(Double.toString(vpp.getDonGiaNhap()));
            txtTimKH63.setText(vpp.getNoiSanXuat());
            
            txtSuaVPPChonMau.setText(vpp.getMauSac().getMaMau());
            
            txtTimKH65.setText(vpp.getDonGiaBan() + "");
            txtTimKH64.setText(Double.toString(vpp.getVAT()));
            // set (*) -> ""
            jLabel132.setText("");
            jLabel139.setText("");
            jLabel167.setText("");
            jLabel168.setText("");
        }

    }// GEN-LAST:event_btnTatCaActionPerformed

    private void btnThemSPActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemSPActionPerformed
        // TODO add your handling code here:
//        JDialogThemSP d = new JDialogThemSP(frm, true);
//        d.setVisible(true);
        jDialogChon.setLocationRelativeTo(null);
        jDialogChon.setVisible(true);
        try {
			loadDataChonMau();
			loadDataChonNCC();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }// GEN-LAST:event_btnThemSPActionPerformed

    private void btnSuaKH1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSuaKH1ActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlChange, pnlCenterNhomSP);
        try {
			loadDataNsp();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }// GEN-LAST:event_btnSuaKH1ActionPerformed

    private void txtTimKH1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH1ActionPerformed

    private void txtTimKH4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH4ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH4ActionPerformed

    private void txtTimKH6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH6ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH6ActionPerformed

    private void btnLuuNVActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnLuuNVActionPerformed
        // TODO add your handling code here:
        String maS = txtThemMaSPSach.getText();
        String tenS = txtThemTenSPSach.getText();
        int sl = Integer.parseInt(txtThemSLSach.getText());
        Instant instantNgayTao = jDateThemSach.getDate().toInstant();
        LocalDateTime ngayTao = instantNgayTao.atZone(ZoneId.systemDefault()).toLocalDateTime();
        NhomSanPham nsp = dao_nsp.getNsptheoTen(cboThemSach.getSelectedItem().toString());
        NhaCungCap ncc = dao_ncc.getNCCTheoTen(txtThemNCCSach.getText());
        String moTa = jTextThemMoTaSach.getText();
        String tacGia = txtThemTGSach.getText();
        double donGiaNhap = Double.parseDouble(txtThemDGNSach.getText());
        double donGiaBan = Double.parseDouble(txtThemDGBSach.getText());
        int namXB = Integer.parseInt(txtThemNSBSach.getText());
        int soStrang = Integer.parseInt(txtThemSoTrangSach.getText());
        double vat = Double.parseDouble(txtThemVATSach.getText());
        String nhaXB = txtThemNhaXBSach.getText();
        double giamGia = Double.parseDouble(txtThemGiamGiaSach.getText());
        String tinhTrang = sl > 0 ? "Còn hàng" : "Hết hàng";
		SanPham sp = new Sach(maS, tenS, nsp, ncc, sl, donGiaNhap, moTa, tinhTrang, donGiaBan, vat, ngayTao, giamGia,
				tacGia, namXB, nhaXB, soStrang);
//		Sach s = new Sach(maS, tenS, nsp, ncc, sl, donGiaNhap, moTa, tinhTrang, donGiaBan, vat, ngayTao, giamGia,
//				tacGia, namXB, nhaXB, soStrang);
        
//        System.out.println(sp.toString());
        if (valiDataThemSach()) {
        	dao_sp.createSanPham(sp);
//        	dao_sach.updateSach(s);
//        	dao_sp.updateSanPham(sp);
        	
//        	sp.setNhomSanPham(nsp);
//        	sp.setNhaCungCap(ncc);
        	
//        	dao_sp.updateSanPham(sp);
            loadData();
            showPanelChange(pnlChange, pnlCenter);
        }
    }// GEN-LAST:event_btnLuuNVActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHuyActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlChange, pnlCenter);
    }// GEN-LAST:event_btnHuyActionPerformed

    private void btnQuayLaiActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnQuayLaiActionPerformed
        // TODO add your handling code here:nl
        showPanelChange(pnlChange, pnlCenter);
    }// GEN-LAST:event_btnQuayLaiActionPerformed

    private void txtTimKH7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH7ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH7ActionPerformed

    private void txtTimKH11ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH11ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH11ActionPerformed

    private void txtTimKH12ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH12ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH12ActionPerformed

    private void txtTimKH13ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH13ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH13ActionPerformed

    private void txtTimKH14ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH14ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH14ActionPerformed

    private void txtTimKH15ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH15ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH15ActionPerformed

    private void txtTimKH16ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH16ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH16ActionPerformed

    private void txtTimKH17ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH17ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH17ActionPerformed

    private void txtTimKH18ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH18ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH18ActionPerformed

    private void txtTimKH21ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH21ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH21ActionPerformed

    private void txtTimKH22ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH22ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH22ActionPerformed

    private void btnLuuNV4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLuuNV4ActionPerformed
        // TODO add your handling code here:
        jDialogChonNCC.setLocationRelativeTo(null);
        jDialogChonNCC.setVisible(true);
    }// GEN-LAST:event_btnLuuNV4ActionPerformed

    private void txtTimKH23ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH23ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH23ActionPerformed

    private void txtTimKH25ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH25ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH25ActionPerformed

    private void txtTimKH27ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH27ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH27ActionPerformed

    private void txtTimKH29ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH29ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH29ActionPerformed

    private void btnLuuNV5ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        String maVpp = txtTimKH23.getText();
        String tenVpp = txtTimKH18.getText();
        int sl = Integer.parseInt(txtTimKH22.getText());
        Instant instantNgayTao = jDateChooser3.getDate().toInstant();
        LocalDateTime ngayTao = instantNgayTao.atZone(ZoneId.systemDefault()).toLocalDateTime();
        NhomSanPham nsp = dao_nsp.getNsptheoTen(jComboBox6.getSelectedItem().toString());
        NhaCungCap ncc = dao_ncc.getNCCTheoTen(txtThemNCCVPP.getText());
        String moTa = jTextArea6.getText();
        double donGiaNhap = Double.parseDouble(txtTimKH35.getText());
        double donGiaBan = Double.parseDouble(txtTimKH29.getText());
        String noiSx = txtTimKH25.getText();
        double vat = Double.parseDouble(txtTimKH27.getText());
//        MauSac ms = dao_mausac.getMauSactheoTen(txtMauSac.getText());
        MauSac ms = dao_mausac.getMauSactheoMa(txtMauSac.getText());
        double giamGia = 0;
        String tinhTrang = sl > 0 ? "Còn hàng" : "Hết hàng";
        SanPham vpp = new VanPhongPham(maVpp, tenVpp, nsp, ncc, sl, donGiaNhap, moTa, tinhTrang, donGiaBan, vat,
                ngayTao, giamGia, ms, noiSx);
        if (valiDataThemVpp()) {
//            dao_vpp.insertVpp(vpp);
        	dao_sp.createSanPham(vpp);
            loadData();
            showPanelChange(pnlChange, pnlCenter);
        }

    }// GEN-LAST:event_btnLuuNV5ActionPerformed

    private void btnHuy2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHuy2ActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlChange, pnlCenter);
    }// GEN-LAST:event_btnHuy2ActionPerformed

    private void btnQuayLai2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnQuayLai2ActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlChange, pnlCenter);
    }// GEN-LAST:event_btnQuayLai2ActionPerformed

    private void txtTimKH20ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH20ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH20ActionPerformed

    private void btnQuayLai1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnQuayLai1ActionPerformed
        // TODO add your handling code here:
        try {
			loadData();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        showPanelChange(pnlChange, pnlCenter);
    }// GEN-LAST:event_btnQuayLai1ActionPerformed

    private void btnQuayLai3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnQuayLai3ActionPerformed
        // TODO add your handling code here:
        try {
			loadData();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        showPanelChange(pnlChange, pnlCenter);
    }// GEN-LAST:event_btnQuayLai3ActionPerformed

    private void btnQuayLai4ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnQuayLai4ActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlChange, pnlCenterSuaSach);
        int row = jTable2.getSelectedRow();
        DefaultTableModel modelS = (DefaultTableModel) jTable2.getModel();
        String maSP = modelS.getValueAt(row, 1).toString();

        Sach s = dao_sach.getSachtheoMa(maSP);
        txtTimKH49.setText(modelS.getValueAt(row, 1).toString());
        txtTimKH3.setText(modelS.getValueAt(row, 2).toString());
        txtTimKH48.setText(modelS.getValueAt(row, 4).toString());
        Instant inStantDate = s.getNgayTao().atZone(ZoneId.systemDefault()).toInstant();
        Date dateNgayTao = Date.from(inStantDate);
        jDateChooser5.setDate(dateNgayTao);
        NhomSanPham nsp = dao_nsp.getNspTheoMa(modelS.getValueAt(row, 3).toString());
        jComboBox8.setSelectedItem(nsp.getTenNhomSanPham());
        jTextArea5.setText(s.getMoTa());
        
        NhaCungCap ncc = dao_ncc.getNCCTheoMa(s.getNhaCungCap().getMaNCC());
        txtSuaSachChonNCC.setText(ncc.getTenNCC());
        
        txtTimKH51.setText(s.getTacGia());
        txtTimKH54.setText(Double.toString(s.getDonGiaNhap()));
        txtTimKH56.setText(Double.toString(s.getDonGiaBan()));
        txtTimKH5.setText(Integer.toString(s.getNamXuatBan()));
        txtTimKH57.setText(Integer.toString(s.getSoTrang()));
        txtTimKH53.setText(Double.toString(s.getVAT()));
        txtTimKH50.setText(s.getNhaXuatBan());
        txtTimKH52.setText(Double.toString(s.getGiamGia()));
    }// GEN-LAST:event_btnQuayLai4ActionPerformed

    private void btnQuayLai5ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnQuayLai5ActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlChange, pnlCenterSuaVPP);
        int row = jTable2.getSelectedRow();
        DefaultTableModel modelVpp = (DefaultTableModel) jTable2.getModel();
        String maSP = modelVpp.getValueAt(row, 1).toString();
        VanPhongPham vpp = dao_vpp.getVPPtheoMa(maSP);
        txtTimKH62.setText(modelVpp.getValueAt(row, 1).toString());
        txtTimKH62.setEditable(false);
        txtTimKH58.setText(modelVpp.getValueAt(row, 2).toString());
        txtTimKH61.setText(modelVpp.getValueAt(row, 4).toString());
        Instant inStantDate = vpp.getNgayTao().atZone(ZoneId.systemDefault()).toInstant();
        Date dateNgayTao = Date.from(inStantDate);
        jDateChooser6.setDate(dateNgayTao);
        NhomSanPham nsp = dao_nsp.getNspTheoMa(modelVpp.getValueAt(row, 3).toString());
        jComboBox9.setSelectedItem(nsp.getTenNhomSanPham());
        jTextArea6.setText(vpp.getMoTa());
        
        NhaCungCap ncc = dao_ncc.getNCCTheoMa(vpp.getNhaCungCap().getMaNCC());
        txtSuaVPPChonNCC.setText(ncc.getTenNCC());
        
        txtTimKH66.setText(Double.toString(vpp.getDonGiaNhap()));
        txtTimKH63.setText(vpp.getNoiSanXuat());
        txtSuaVPPChonMau.setText(vpp.getMauSac().getMaMau());
        txtTimKH65.setText(Double.toString(vpp.getDonGiaBan()));
        txtTimKH64.setText(Double.toString(vpp.getVAT()));
    }// GEN-LAST:event_btnQuayLai5ActionPerformed

    private void btnSuaKH2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSuaKH2ActionPerformed
        // TODO add your handling code here:
        showPanelChange(pnlChange, pnlCenterThemVPP);
        jDialogChon.setVisible(false);

        try {
			txtTimKH23.setText(createMaVpp());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        Instant inStantDate = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
        Date dateNgayTao = Date.from(inStantDate);
        jDateChooser3.setDate(dateNgayTao);
//        ArrayList<NhomSanPham> dsNsp = dao_nsp.getAllNhomSanPham();
//        for (NhomSanPham nsp : dsNsp) {
//            jComboBox6.addItem(nsp.getMaNhomSanPham());
//        }
        txtTimKH27.setText("0.05");
    }// GEN-LAST:event_btnSuaKH2ActionPerformed

    private void btnThemSP1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemSP1ActionPerformed
        showPanelChange(pnlChange, pnlCenterThemSach);
        Date d = new Date();
        jDateThemSach.setDate(d);
        jDialogChon.setVisible(false);
        try {
			txtThemMaSPSach.setText(createMaSach());
			txtThemVATSach.setText("0.1");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }// GEN-LAST:event_btnThemSP1ActionPerformed

    private void txtTimKH55ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH55ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH55ActionPerformed

    private void btnTatCa1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTatCa1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_btnTatCa1ActionPerformed

    private void jTable4MouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable4MouseExited
        // TODO add your handling code here:
    }// GEN-LAST:event_jTable4MouseExited

    private void btnThemSP2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemSP2ActionPerformed
        // TODO add your handling code here:
        jDialogThemNhomSP.setLocationRelativeTo(null);
        jDialogThemNhomSP.setVisible(true);
    }// GEN-LAST:event_btnThemSP2ActionPerformed

    private void btnQuayLai6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnQuayLai6ActionPerformed
        // TODO add your handling code here:
        try {
			loadData();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        showPanelChange(pnlChange, pnlCenter);
    }// GEN-LAST:event_btnQuayLai6ActionPerformed

    private void btnThemSP3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemSP3ActionPerformed
        // TODO add your handling code here:
        jDialogThemNhomSP.setVisible(false);
    }// GEN-LAST:event_btnThemSP3ActionPerformed

    private void btnSuaKH3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSuaKH3ActionPerformed
        // TODO add your handling code here:
        jDialogThemNhomSP.setVisible(false);
    }// GEN-LAST:event_btnSuaKH3ActionPerformed

    private void txtTimKH43ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH43ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH43ActionPerformed

    private void txtTimKH44ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH44ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH44ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1 && !evt.isConsumed()) {
            evt.consume();
//            int row = jTable2.getSelectedRow();
//            DefaultTableModel modelSP = (DefaultTableModel) jTable2.getModel();
//            String maSP = modelSP.getValueAt(row, 1).toString();
//            if (maSP.startsWith("VPP")) {
//                VanPhongPham vpp = dao_vpp.getVPPtheoMa(maSP);
//                showPanelChange(pnlChange, pnlCenterXemVPP);
//                //
//                txtTimKH39.setText(modelSP.getValueAt(row, 1).toString());
//                txtTimKH34.setText(modelSP.getValueAt(row, 2).toString());
//                txtTimKH38.setText(modelSP.getValueAt(row, 4).toString());
//                Instant inStantDate = vpp.getNgayTao().atZone(ZoneId.systemDefault()).toInstant();
//                Date dateNgayTao = Date.from(inStantDate);
//                jDateChooser2.setDate(dateNgayTao);
//                NhomSanPham nsp = dao_nsp.getNspTheoMa(modelSP.getValueAt(row, 3).toString());
//                jComboBox7.setSelectedItem(nsp.getTenNhomSanPham());
//                jTextArea4.setText(vpp.getMoTa());
//                txtTimKH37.setText(vpp.getNhaCungCap().getMaNCC());
//                txtTimKH40.setText(Double.toString(vpp.getDonGiaNhap()));
//                txtTimKH46.setText(vpp.getNoiSanXuat());
//                txtTimKH36.setText(vpp.getMauSac().getMaMau());
//                txtTimKH42.setText(Double.toString(vpp.getDonGiaBan()));
//                txtTimKH41.setText(Double.toString(vpp.getVAT()));
//
//            } else {
//                showPanelChange(pnlChange, pnlCenterXemSach);
//                //
//                Sach s = dao_sach.getSachtheoMa(maSP);
//                txtXemMaSPSach.setText(modelSP.getValueAt(row, 1).toString());
//                txtXemTenSPSach.setText(modelSP.getValueAt(row, 2).toString());
//                txtXemSLSach.setText(modelSP.getValueAt(row, 4).toString());
//                Instant inStantDate = s.getNgayTao().atZone(ZoneId.systemDefault()).toInstant();
//                Date dateNgayTao = Date.from(inStantDate);
//                jDateXemNgaySach.setDate(dateNgayTao);
//                NhomSanPham nsp = dao_nsp.getNspTheoMa(modelSP.getValueAt(row, 3).toString());
//                cboXemNhomSPSach.setSelectedItem(nsp.getTenNhomSanPham());
//                jTextXemMoTaSach.setText(s.getMoTa());
//                txtXemNhaCCSach.setText(s.getNhaCungCap().getMaNCC());
//                txtXemTGSach.setText(s.getTacGia());
//                txtXemDGNSach.setText(Double.toString(s.getDonGiaNhap()));
//                txtXemDGBSach.setText(Double.toString(s.getDonGiaBan()));
//                txtXemNSBSach.setText(Integer.toString(s.getNamXuatBan()));
//                txtXemSoTrangSach.setText(Integer.toString(s.getSoTrang()));
//                txtXemVATSach.setText(Double.toString(s.getVAT()));
//                txtXemNhaXBSach.setText(s.getNhaXuatBan());
//                txtXemGiamGiaSach.setText(Double.toString(s.getGiamGia()));
//            }

        }
    }// GEN-LAST:event_jTable2MouseClicked

    private void txtTimSPFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtTimSPFocusGained
        // TODO add your handling code here:
//        if(txtTimSP.getText().equals("Nhập vào thông tin tìm kiếm...")) {
//            txtTimSP.setText("");
//        }
        frm.placeHoderTextGianed(txtTimSP);
    }// GEN-LAST:event_txtTimSPFocusGained

    private void txtTimSPFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtTimSPFocusLost
        // TODO add your handling code here:
//        if(txtTimSP.getText().equals("")) {
//            txtTimSP.setText("Nhập vào thông tin tìm kiếm...");
//        }
        frm.placeHoderTextLost(txtTimSP);
    }// GEN-LAST:event_txtTimSPFocusLost

    private void txtTimKH55FocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtTimKH55FocusGained
        // TODO add your handling code here:
        frm.placeHoderTextGianed(txtTimKH55);
    }// GEN-LAST:event_txtTimKH55FocusGained

    private void txtTimKH55FocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtTimKH55FocusLost
        // TODO add your handling code here:
        frm.placeHoderTextLost(txtTimKH55);
    }// GEN-LAST:event_txtTimKH55FocusLost

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:

    }// GEN-LAST:event_jTable3MouseClicked

    private void txtTimKH35ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH35ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH35ActionPerformed

    private void txtTimKH19ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH19ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH19ActionPerformed

    private void btnLuuNV3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLuuNV3ActionPerformed
        // TODO add your handling code here:
        jDialogChonNCC.setLocationRelativeTo(null);
        jDialogChonNCC.setVisible(true);
    }// GEN-LAST:event_btnLuuNV3ActionPerformed

    private void txtTimKH46ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH46ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH46ActionPerformed

    private void btnChonMau1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnChonMau1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_btnChonMau1ActionPerformed

    private void txtTimKH42ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH42ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH42ActionPerformed

    private void txtTimKH41ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH41ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH41ActionPerformed

    private void txtTimKH40ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH40ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH40ActionPerformed

    private void txtTimKH39ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH39ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH39ActionPerformed

    private void btnLuuNV6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLuuNV6ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_btnLuuNV6ActionPerformed

    private void txtTimKH38ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH38ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH38ActionPerformed

    private void txtTimKH37ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH37ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH37ActionPerformed

    private void txtTimKH36ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH36ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH36ActionPerformed

    private void txtTimKH34ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH34ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH34ActionPerformed

    private void txtTimKH45ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH45ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH45ActionPerformed

    private void txtTimKH33ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH33ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH33ActionPerformed

    private void txtTimKH32ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH32ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH32ActionPerformed

    private void txtTimKH31ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH31ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH31ActionPerformed

    private void txtTimKH30ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH30ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH30ActionPerformed

    private void txtTimKH28ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH28ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH28ActionPerformed

    private void txtTimKH26ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH26ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH26ActionPerformed

    private void txtTimKH24ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH24ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH24ActionPerformed

    private void btnLuuNV2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLuuNV2ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_btnLuuNV2ActionPerformed

    private void txtTimKH10ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH10ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH10ActionPerformed

    private void txtTimKH9ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH9ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH9ActionPerformed

    private void txtTimKH8ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH8ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH8ActionPerformed

    private void btnChonMauActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnChonMauActionPerformed
        // TODO add your handling code here:
        jDialogChonMau.setLocationRelativeTo(null);
        jDialogChonMau.setVisible(true);
    }// GEN-LAST:event_btnChonMauActionPerformed

    private void txtTimKH3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH3ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH3ActionPerformed

    private void txtTimKH5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH5ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH5ActionPerformed

    private void txtTimKH47ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH47ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH47ActionPerformed

    private void txtTimKH48ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH48ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH48ActionPerformed

    private void txtTimKH49ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH49ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH49ActionPerformed

    private void txtTimKH50ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH50ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH50ActionPerformed

    private void txtTimKH51ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH51ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH51ActionPerformed

    private void txtTimKH52ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH52ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH52ActionPerformed

    private void txtTimKH53ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH53ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH53ActionPerformed

    private void txtTimKH54ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH54ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH54ActionPerformed

    private void txtTimKH56ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH56ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH56ActionPerformed

    private void txtTimKH57ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH57ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH57ActionPerformed

    private void btnLuuNV7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLuuNV7ActionPerformed
        // TODO add your handling code here:
        try {
			loadDataChonNCC();
			jDialogChonNCC.setLocationRelativeTo(null);
	        jDialogChonNCC.setVisible(true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }// GEN-LAST:event_btnLuuNV7ActionPerformed

    private void btnLuuNV1ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnLuuNV1ActionPerformed
        // TODO add your handling code here:
        Sach s = dao_sach.getSachtheoMa(txtTimKH49.getText());
//    	SanPham sp = dao_sp.getSanPhamtheoMa(txtTimKH49.getText());
        if (valiDataSuaSach()) {
            updateS(s);
            loadData();
            showPanelChange(pnlChange, pnlCenter);
        }
    }// GEN-LAST:event_btnLuuNV1ActionPerformed

    private void btnHuy1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHuy1ActionPerformed
        // TODO add your handling code here:
        try {
			loadData();
			 showPanelChange(pnlChange, pnlCenter);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }// GEN-LAST:event_btnHuy1ActionPerformed

    private void btnQuayLai7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnQuayLai7ActionPerformed
        // TODO add your handling code here:
        try {
			loadData();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        showPanelChange(pnlChange, pnlCenter);
    }// GEN-LAST:event_btnQuayLai7ActionPerformed

    private void txtTimKH58ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH58ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH58ActionPerformed

    private void txtTimKH59ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH59ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH59ActionPerformed

    private void txtTimKH60ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH60ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH60ActionPerformed

    private void txtTimKH61ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH61ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH61ActionPerformed

    private void btnLuuNV8ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLuuNV8ActionPerformed
        // TODO add your handling code here:
        try {
			loadDataChonNCC();
	        jDialogChonNCC.setLocationRelativeTo(null);
	        jDialogChonNCC.setVisible(true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }// GEN-LAST:event_btnLuuNV8ActionPerformed

    private void txtTimKH62ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH62ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH62ActionPerformed

    private void txtTimKH63ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH63ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH63ActionPerformed

    private void txtTimKH64ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH64ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH64ActionPerformed

    private void txtTimKH65ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH65ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH65ActionPerformed

    private void btnChonMau2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnChonMau2ActionPerformed
        // TODO add your handling code here:
        try {
			loadDataChonMau();
	        jDialogChonMau.setLocationRelativeTo(null);
	        jDialogChonMau.setVisible(true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }// GEN-LAST:event_btnChonMau2ActionPerformed

    private void txtTimKH66ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKH66ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTimKH66ActionPerformed

    private void btnLuuNV9ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnLuuNV9ActionPerformed
        // TODO add your handling code here:
        VanPhongPham sp = dao_vpp.getVPPtheoMa(txtTimKH62.getText());
//    	SanPham sp = dao_sp.getSanPhamtheoMa(txtTimKH62.getText());
        if (valiDataSuaVpp()) {
            updateVpp(sp);
            loadData();
            showPanelChange(pnlChange, pnlCenter);
        }
    }// GEN-LAST:event_btnLuuNV9ActionPerformed

    private void btnHuy3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHuy3ActionPerformed
        // TODO add your handling code here:
        try {
			loadData();
			showPanelChange(pnlChange, pnlCenter);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }// GEN-LAST:event_btnHuy3ActionPerformed

    private void btnQuayLai8ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnQuayLai8ActionPerformed
        // TODO add your handling code here:
        try {
			loadData();
			showPanelChange(pnlChange, pnlCenter);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }// GEN-LAST:event_btnQuayLai8ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChonMau;
    private javax.swing.JButton btnChonMau2;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnHuy1;
    private javax.swing.JButton btnHuy2;
    private javax.swing.JButton btnHuy3;
    private javax.swing.JButton btnLuuNV;
    private javax.swing.JButton btnLuuNV1;
    private javax.swing.JButton btnLuuNV3;
    private javax.swing.JButton btnLuuNV4;
    private javax.swing.JButton btnLuuNV5;
    private javax.swing.JButton btnLuuNV7;
    private javax.swing.JButton btnLuuNV8;
    private javax.swing.JButton btnLuuNV9;
    private javax.swing.JLabel btnNhapFile;
    private javax.swing.JButton btnQuayLai;
    private javax.swing.JButton btnQuayLai2;
    private javax.swing.JButton btnQuayLai6;
    private javax.swing.JButton btnQuayLai7;
    private javax.swing.JButton btnQuayLai8;
    private javax.swing.JButton btnSuaKH1;
    private javax.swing.JButton btnSuaKH2;
    private javax.swing.JButton btnSuaKH3;
    private javax.swing.JButton btnTaoMa2;
    private javax.swing.JButton btnTaoMa3;
    private javax.swing.JButton btnTatCa;
    private javax.swing.JButton btnTatCa1;
    private javax.swing.JButton btnThemSP;
    private javax.swing.JButton btnThemSP1;
    private javax.swing.JButton btnThemSP2;
    private javax.swing.JButton btnThemSP3;
    private javax.swing.JLabel btnXuatFile;
    private javax.swing.JComboBox<String> cboThemSach;
    private javax.swing.JLabel date;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser5;
    private com.toedter.calendar.JDateChooser jDateChooser6;
    private com.toedter.calendar.JDateChooser jDateThemSach;
    private javax.swing.JDialog jDialogChon;
    private javax.swing.JDialog jDialogChonMau;
    private javax.swing.JDialog jDialogChonNCC;
    private javax.swing.JDialog jDialogThemNhomSP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel166;
    private javax.swing.JLabel jLabel167;
    private javax.swing.JLabel jLabel168;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JTextArea jTextArea6;
    private javax.swing.JTextArea jTextThemMoTaSach;
    private javax.swing.JLabel lblNameLogin;
    private menuGui.MenuScrollPane menuScrollPane11;
    private menuGui.MenuScrollPane menuScrollPane2;
    private menuGui.MenuScrollPane menuScrollPane3;
    private menuGui.MenuScrollPane menuScrollPane4;
    private menuGui.MenuScrollPane menuScrollPane9;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlCenterKHchange;
    private javax.swing.JPanel pnlCenterKHchange1;
    private javax.swing.JPanel pnlCenterKHchange3;
    private javax.swing.JPanel pnlCenterKHchange5;
    private javax.swing.JPanel pnlCenterKHchange6;
    private javax.swing.JPanel pnlCenterKHchange7;
    private javax.swing.JPanel pnlCenterNhomSP;
    private javax.swing.JPanel pnlCenterSuaSach;
    private javax.swing.JPanel pnlCenterSuaVPP;
    private javax.swing.JPanel pnlCenterThemSach;
    private javax.swing.JPanel pnlCenterThemVPP;
    private javax.swing.JPanel pnlChange;
    private javax.swing.JPanel pnlChonNCC;
    private javax.swing.JPanel pnlChonNCC2;
    private javax.swing.JPanel pnlInit;
    private javax.swing.JPanel pnlInit1;
    private javax.swing.JPanel pnlInit2;
    private javax.swing.JPanel pnlNull;
    private javax.swing.JPanel pnlTopLeft;
    private javax.swing.JPanel pnlTopLeft1;
    private javax.swing.JPanel pnlTopLeft3;
    private javax.swing.JPanel pnlTopLeft5;
    private javax.swing.JPanel pnlTopLeft6;
    private javax.swing.JPanel pnlTopLeft7;
    private javax.swing.JTable tableChonMau;
    private javax.swing.JTable tableChonNCC;
    private javax.swing.JTextField txtMauSac;
    private javax.swing.JTextField txtSuaSachChonNCC;
    private javax.swing.JTextField txtSuaVPPChonMau;
    private javax.swing.JTextField txtSuaVPPChonNCC;
    private javax.swing.JTextField txtThemDGBSach;
    private javax.swing.JTextField txtThemDGNSach;
    private javax.swing.JTextField txtThemGiamGiaSach;
    private javax.swing.JTextField txtThemMaSPSach;
    private javax.swing.JTextField txtThemNCCSach;
    private javax.swing.JTextField txtThemNCCVPP;
    private javax.swing.JTextField txtThemNSBSach;
    private javax.swing.JTextField txtThemNhaXBSach;
    private javax.swing.JTextField txtThemSLSach;
    private javax.swing.JTextField txtThemSoTrangSach;
    private javax.swing.JTextField txtThemTGSach;
    private javax.swing.JTextField txtThemTenSPSach;
    private javax.swing.JTextField txtThemVATSach;
    private javax.swing.JTextField txtTimKH18;
    private javax.swing.JTextField txtTimKH2;
    private javax.swing.JTextField txtTimKH22;
    private javax.swing.JTextField txtTimKH23;
    private javax.swing.JTextField txtTimKH25;
    private javax.swing.JTextField txtTimKH27;
    private javax.swing.JTextField txtTimKH29;
    private javax.swing.JTextField txtTimKH3;
    private javax.swing.JTextField txtTimKH35;
    private javax.swing.JTextField txtTimKH43;
    private javax.swing.JTextField txtTimKH44;
    private javax.swing.JTextField txtTimKH48;
    private javax.swing.JTextField txtTimKH49;
    private javax.swing.JTextField txtTimKH5;
    private javax.swing.JTextField txtTimKH50;
    private javax.swing.JTextField txtTimKH51;
    private javax.swing.JTextField txtTimKH52;
    private javax.swing.JTextField txtTimKH53;
    private javax.swing.JTextField txtTimKH54;
    private javax.swing.JTextField txtTimKH55;
    private javax.swing.JTextField txtTimKH56;
    private javax.swing.JTextField txtTimKH57;
    private javax.swing.JTextField txtTimKH58;
    private javax.swing.JTextField txtTimKH61;
    private javax.swing.JTextField txtTimKH62;
    private javax.swing.JTextField txtTimKH63;
    private javax.swing.JTextField txtTimKH64;
    private javax.swing.JTextField txtTimKH65;
    private javax.swing.JTextField txtTimKH66;
    public javax.swing.JTextField txtTimNCC;
    private javax.swing.JTextField txtTimSP;
    // End of variables declaration//GEN-END:variables
}
