package iuh.fit.lookup;

import iuh.fit.dao.*;

import java.rmi.Naming;

public class LookupNaming {
	private static final String URL = "rmi://LAPTOP-LEGION:1099/"; // đúng port 1099 như server

	public static DAO_ChiTietHoaDon lookup_ChiTietHoaDon() {
		try {
			return (DAO_ChiTietHoaDon) Naming.lookup(URL + "ChiTietHoaDonService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_ChiTietHoaDonDoi lookup_ChiTietHoaDonDoi() {
		try {
			return (DAO_ChiTietHoaDonDoi) Naming.lookup(URL + "ChiTietHoaDonDoiService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_ChiTietHoaDonHoanTra lookup_ChiTietHoaDonHoanTra() {
		try {
			return (DAO_ChiTietHoaDonHoanTra) Naming.lookup(URL + "ChiTietHoaDonHoanTraService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_HoaDon lookup_HoaDon() {
		try {
			return (DAO_HoaDon) Naming.lookup(URL + "HoaDonService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_HoaDonDoiHang lookup_HoaDonDoiHang() {
		try {
			return (DAO_HoaDonDoiHang) Naming.lookup(URL + "HoaDonDoiHangService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_HoaDonHoanTra lookup_HoaDonHoanTra() {
		try {
			return (DAO_HoaDonHoanTra) Naming.lookup(URL + "HoaDonHoanTraService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_KhachHang lookup_KhachHang() {
		try {
			return (DAO_KhachHang) Naming.lookup(URL + "KhachHangService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_KhuyenMai lookup_KhuyenMai() {
		try {
			return (DAO_KhuyenMai) Naming.lookup(URL + "KhuyenMaiService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_MauSac lookup_MauSac() {
		try {
			return (DAO_MauSac) Naming.lookup(URL + "MauSacService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_NhaCungCap lookup_NhaCungCap() {
		try {
			return (DAO_NhaCungCap) Naming.lookup(URL + "NhaCungCapService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_NhanVien lookup_NhanVien() {
		try {
			return (DAO_NhanVien) Naming.lookup(URL + "NhanVienService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_NhomKhuyenMai lookup_NhomKhuyenMai() {
		try {
			return (DAO_NhomKhuyenMai) Naming.lookup(URL + "NhomKhuyenMaiService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_NhomSanPham lookup_NhomSanPham() {
		try {
			return (DAO_NhomSanPham) Naming.lookup(URL + "NhomSanPhamService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_Sach lookup_Sach() {
		try {
			return (DAO_Sach) Naming.lookup(URL + "SachService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_SanPham lookup_SanPham() {
		try {
			return (DAO_SanPham) Naming.lookup(URL + "SanPhamService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_TaiKhoan lookup_TaiKhoan() {
		try {
			return (DAO_TaiKhoan) Naming.lookup(URL + "TaiKhoanService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_ThongKe lookup_ThongKe() {
		try {
			return (DAO_ThongKe) Naming.lookup(URL + "ThongKeService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DAO_VanPhongPham lookup_VanPhongPham() {
		try {
			return (DAO_VanPhongPham) Naming.lookup(URL + "VanPhongPhamService");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
