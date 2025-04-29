package lookup;

import java.rmi.Naming;

import dao.DAO_ChiTietHoaDon;
import dao.DAO_ChiTietHoaDonDoi;
import dao.DAO_ChiTietHoaDonHoanTra;
import dao.DAO_HoaDon;
import dao.DAO_HoaDonDoiHang;
import dao.DAO_HoaDonHoanTra;
import dao.DAO_KhachHang;
import dao.DAO_KhuyenMai;
import dao.DAO_MauSac;
import dao.DAO_NhaCungCap;
import dao.DAO_NhanVien;
import dao.DAO_NhomKhuyenMai;
import dao.DAO_NhomSanPham;
import dao.DAO_Sach;
import dao.DAO_SanPham;
import dao.DAO_TaiKhoan;
import dao.DAO_ThongKe;
import dao.DAO_VanPhongPham;
import entity.NhaCungCap;
import entity.SanPham;

public class LookupNaming {
	private static final String URL = "rmi://172.20.81.56:9090/";
	
	
	public static DAO_SanPham lookup_SanPham() {
		try {
			// Lookup DAO_SanPham
			DAO_SanPham sanPham = (DAO_SanPham) Naming.lookup(URL + "sanPham");
			return sanPham;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_NhanVien lookup_NhanVien() {
		try {
			// Lookup DAO_NhanVien
			DAO_NhanVien nhanVien = (DAO_NhanVien) Naming.lookup(URL + "nhanVien");
			return nhanVien;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_NhaCungCap lookup_NhaCungCap() {
		try {
			// Lookup DAO_NhaCungCap
			DAO_NhaCungCap nhaCungCap = (DAO_NhaCungCap) Naming.lookup(URL + "nhaCungCap");
			return nhaCungCap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_TaiKhoan lookup_TaiKhoan() {
		try {
			// Lookup DAO_TaiKhoan
			DAO_TaiKhoan taiKhoan = (DAO_TaiKhoan) Naming.lookup(URL + "taiKhoan");
			return taiKhoan;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_KhachHang lookup_KhachHang() {
		try {
			// Lookup DAO_KhachHang
			DAO_KhachHang khachHang = (DAO_KhachHang) Naming.lookup(URL + "khachHang");
			return khachHang;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_NhomSanPham lookup_NhomSanPham() {
		try {
			// Lookup DAO_NhomSanPham
			DAO_NhomSanPham nhomSanPham = (DAO_NhomSanPham) Naming.lookup(URL + "nhomSanPham");
			return nhomSanPham;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_Sach lookup_Sach() {
		try {
			// Lookup DAO_Sach
			DAO_Sach sach = (DAO_Sach) Naming.lookup(URL + "sach");
			return sach;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_MauSac lookup_MauSac() {
		try {
			// Lookup DAO_MauSac
			DAO_MauSac mauSac = (DAO_MauSac) Naming.lookup(URL + "mauSac");
			return mauSac;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_VanPhongPham lookup_VanPhongPham() {
		try {
			// Lookup DAO_VanPhongPham
			DAO_VanPhongPham vanPhongPham = (DAO_VanPhongPham) Naming.lookup(URL + "vanPhongPham");
			return vanPhongPham;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_KhuyenMai lookup_KhuyenMai() {
		try {
			// Lookup DAO_KhuyenMai
			DAO_KhuyenMai khuyenMai = (DAO_KhuyenMai) Naming.lookup(URL + "khuyenMai");
			return khuyenMai;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_NhomKhuyenMai lookup_NhomKhuyenMai() {
		try {
			// Lookup DAO_NhomKhuyenMai
			DAO_NhomKhuyenMai nhomKhuyenMai = (DAO_NhomKhuyenMai) Naming.lookup(URL + "nhomKhuyenMai");
			return nhomKhuyenMai;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_HoaDon lookup_HoaDon() {
		try {
			// Lookup DAO_HoaDon
			DAO_HoaDon hoaDon = (DAO_HoaDon) Naming.lookup(URL + "hoaDon");
			return hoaDon;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_ChiTietHoaDon lookup_ChiTietHoaDon() {
		try {
			// Lookup DAO_ChiTietHoaDon
			DAO_ChiTietHoaDon chiTietHoaDon = (DAO_ChiTietHoaDon) Naming.lookup(URL + "chiTietHoaDon");
			return chiTietHoaDon;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_HoaDonHoanTra lookup_HoaDonHoanTra() {
		try {
			// Lookup DAO_HoaDonHoanTra
			DAO_HoaDonHoanTra hoaDonHoanTra = (DAO_HoaDonHoanTra) Naming.lookup(URL + "hoaDonHoanTra");
			return hoaDonHoanTra;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_ChiTietHoaDonHoanTra lookup_ChiTietHoaDonHoanTra() {
		try {
			// Lookup DAO_ChiTietHoaDonHoanTra
			DAO_ChiTietHoaDonHoanTra chiTietHoaDonHoanTra = (DAO_ChiTietHoaDonHoanTra) Naming
					.lookup(URL + "chiTietHoanTra");
			return chiTietHoaDonHoanTra;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_HoaDonDoiHang lookup_HoaDonDoiHang() {
		try {
			// Lookup DAO_HoaDonDoiHang
			DAO_HoaDonDoiHang hoaDonDoiHang = (DAO_HoaDonDoiHang) Naming.lookup(URL + "hoaDonDoiHang");
			return hoaDonDoiHang;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static DAO_ChiTietHoaDonDoi lookup_ChiTietHoaDonDoi() {
		try {
			// Lookup DAO_ChiTietHoaDonDoi
			DAO_ChiTietHoaDonDoi chiTietHoaDonDoi = (DAO_ChiTietHoaDonDoi) Naming.lookup(URL + "chiTietHoaDonDoi");
			return chiTietHoaDonDoi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
    
	public static DAO_ThongKe lookup_ThongKe() {
		try {
			// Lookup DAO_ThongKe
			DAO_ThongKe thongKe = (DAO_ThongKe) Naming.lookup(URL + "thongKe");
			return thongKe;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	

	
	
	
}
