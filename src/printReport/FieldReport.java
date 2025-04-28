/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package printReport;

public class FieldReport {
    String ten;
    int soL;
    double gia;
    double tien;
    
    public FieldReport() {
    }

    public FieldReport(String ten, int soL, double gia, double tien) {
        this.ten = ten;
        this.soL = soL;
        this.gia = gia;
        this.tien = tien;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getSoL() {
        return soL;
    }

    public void setSoL(int soL) {
        this.soL = soL;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public double getTien() {
        return tien;
    }

    public void setThanhTien(double tien) {
        this.tien = tien;
    }
    
    
}
