/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package printReport;

import java.util.List;

public class ParameterReport {
    String tongTien;
    String khachTra;
    String chietKhau;
    String ngay;
    String ghiChu;
    String khuyenMai;
    
    List<FieldReport> listFields;
    
    public ParameterReport() {

    }

    public ParameterReport(String tongTien, String khachTra, String chietKhau, String ngay, String ghiChu, String khuyenMai, List<FieldReport> listFields) {
        this.tongTien = tongTien;
        this.khachTra = khachTra;
        this.chietKhau = chietKhau;
        this.ngay = ngay;
        this.ghiChu = ghiChu;
        this.khuyenMai = khuyenMai;
        this.listFields = listFields;
    }

    public String getTongTien() {
        return tongTien;
    }

    public void setTongTien(String tongTien) {
        this.tongTien = tongTien;
    }

    public String getKhachTra() {
        return khachTra;
    }

    public void setKhachTra(String khachTra) {
        this.khachTra = khachTra;
    }

    public String getChietKhau() {
        return chietKhau;
    }

    public void setChietKhau(String chietKhau) {
        this.chietKhau = chietKhau;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(String khuyenMai) {
        this.khuyenMai = khuyenMai;
    }
    
    

    public List<FieldReport> getListFields() {
        return listFields;
    }

    public void setListFields(List<FieldReport> listFields) {
        this.listFields = listFields;
    }

    
    
}
