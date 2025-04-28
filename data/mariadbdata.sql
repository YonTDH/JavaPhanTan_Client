USE nhasachdb;
-- Chèn dữ liệu vào bảng CaLamViec
INSERT INTO CaLamViec (maCa, tenCa, thoiGianBatDau, thoiGianKetThuc) VALUES
('CA01', 'Sáng', '06:00:00', '11:00:00'),
('CA02', 'Chiều', '11:00:00', '16:00:00'),
('CA03', 'Tối', '16:00:00', '21:00:00');

-- Chèn dữ liệu vào bảng KhuyenMai
INSERT INTO KhuyenMai (maKhuyenMai, ghiChu, giaTriKhuyenMaiToiDa, ngayBatDau, ngayKetThuc, tenKhuyenMai, tienToiThieu, trangThai, tyLeKhuyenMai) VALUES
('aaa', 'aaa', 10000, '2023-12-06 00:11:29', '2023-12-29 00:11:30', 'aaa', 0, 'Đã ngừng', 10),
('C0PSSCAPPO', 'Sale dip dac biet', 100000, '2024-12-13 19:34:01', '2024-12-31 19:34:03', 'Sale 12-12', 0, 'Đang hoạt động', 50),
('FREE', 'aaaa', 10000, '2023-12-02 00:00:00', '2025-12-18 00:00:00', 'aaa', 0, 'Đang hoạt động', 0.6),
('GT4PQ4MKN7', 'Tết', 20000, '2023-12-13 19:37:39', '2025-02-04 19:37:40', 'Sale Tet', 0, 'Đang hoạt động', 70),
('KM06122023-000001', 'Giam gia tet', 100000, '2023-12-06 22:04:22', '2025-12-26 22:04:24', 'Sales', 0, 'Đang hoạt động', 0.5),
('RFNKOAWBNF', 'Noel', 20000, '2023-12-13 19:37:39', '2025-12-27 19:37:40', 'Sale Noel', 0, 'Đang hoạt động', 30);

-- Chèn dữ liệu vào bảng MauSac
INSERT INTO MauSac (maMau, tenMau) VALUES
('BLACK', 'Đen'),
('BLUE', 'Xanh lục'),
('BROWN', 'Nâu'),
('COLORS', 'Nhiều màu'),
('GRAY', 'Xám'),
('GREEN', 'Xanh lục'),
('ORANGE', 'Cam'),
('PINK', 'Hồng'),
('PURPLE', 'Tím'),
('RED', 'Đỏ'),
('SILVER', 'Bạc'),
('WHITE', 'Trắng'),
('YELLOW', 'Vàng');

-- Chèn dữ liệu vào bảng NhaCungCap
INSERT INTO NhaCungCap (maNCC, diachiNCC, email, ghiChu, soDienThoai, tenNCC) VALUES
('NCC23102023-000001', 'Số 9 Lê Văn Thiêm - Thanh Xuân - Hà Nội', 'lienhe@dongtay.vn', '', '02422157878', 'Cty Văn Hóa Đông Tây'),
('NCC23102023-000002', 'Số 55 Quang Trung, Nguyễn Du, Hai Bà Trưng, Hà Nội', 'cskh_online@nxbkimdong.com.vn', '', '1900571595', 'Nhà Xuất Bản Kim Đồng'),
('NCC23102023-000003', 'Số 59, Đỗ Quang, Trung Hoà, Cầu Giấy, Hà Nội', 'info@nhanam.vn', '', '|02435146875', 'Nhã Nam'),
('NCC23102023-000004', '16 Trịnh Hoài Đức, Phường 13, Quận 5, TP. HCM', 'bitex@bitex.com.vn', '', '19002152', 'Bình Tây'),
('NCC23102023-000005', 'Số 65 Đường 9 An Dương Tây Hồ Hà Nội', 'bophanbanle@azbooks.vn', '', '0964484633', 'AZ Việt Nam'),
('NCC23102023-000006', '161B Lý Chính Thắng, Võ Thị Sáu, Quận 3 , TP. HCM', 'hopthubandoc@nxbtre.com.vn', '', '02838437450', 'NXB Trẻ'),
('NCC23102023-000007', '11 Nguyễn Thị Minh Khai, Bến Nghé, Quận 1, TP.HCM', 'triviet@firstnews.com.vn', '', '0238227979', 'FIRST NEWS'),
('NCC24102023-000001', 'Số 78 - Đường số 1, P.4, Gò Vấp, TP. HCM', 'hopthubandoc@nxbtre.com.vn', '', '02838437450', 'Đinh Tị'),
('NCC24102023-000002', 'Tầng 3, Dream Home Center, Thanh Xuân, Hà Nội', 'mkt.alphabooks@gmail.com', '', '0932329959', 'Alpha Books'),
('NCC24102023-000003', 'Số 10 Mai Chí Thọ, Thủ Thiêm, Thủ Đức, TP.HCM', 'info@thienlonggroup.com', '', '02837505555', 'Thiên Long Hoàn Cầu'),
('NCC24102023-000004', '29 DD11, P. TÂN HƯNG THUẬN, Q.12, TPHCM.', 'hoangthienanhcm@gmail.com', '', '0394334199', 'Deli');

-- Chèn dữ liệu vào bảng NhomSanPham
INSERT INTO NhomSanPham (maNhomSanPham, tenNhomSanPham) VALUES
('NSP001', 'Sách'),
('NSP002', 'Dụng Cụ Học Sinh'),
('NSP003', 'Văn Phòng Phẩm'),
('NSP004', 'SGK'),
('NSP005', 'Truyện');

-- Chèn dữ liệu vào bảng TaiKhoan
INSERT INTO TaiKhoan (tenDangNhap, email, matKhau) VALUES
('A@a', 'A@a', '123456'),
('A@q', 'A@q', '123456'),
('QL23102023000001', 'nguyentanloc1108@gmail.com', 'NTL@11082003'),
('QL23102023000007', 'chautinh05122@gmail.com', 'NCT@123456'),
('QL23102023000012', 'tdh1503@gmail.com', 'TDH123456'),
('TN23102023000002', 'kimanhvo@gmail.com', 'VKA@11031990'),
('TN23102023000003', 'thanhnhan@gmail.com', 'NTN@21092000'),
('TN23102023000004', 'hoangvanthuan@gmail.com', 'HVT@10112001'),
('TN23102023000005', 'dangquyettranwork@gmail.com', 'TDQ@12072000'),
('TN23102023000006', 'thanhnam@gmail.com', 'DTN@25122000'),
('TN23102023000008', 'nhiwork@gmail.com', 'TTN@12052001'),
('TN23102023000009', 'dangkhoawork@gmail.com', 'NTT@21092000'),
('TN23102023000010', 'namphidoan@gmail.com', 'DNP@19042001'),
('TN23102023000011', 'phuongvo@gmail.com', 'VTP@16032000');

-- Chèn dữ liệu vào bảng KhachHang
INSERT INTO KhachHang (maKhachHang, nhomKhachHang, soDienThoai, soLuongHoaDon, tenKhachHang, tongTienMua) VALUES
('KH01042021-000001', 'KHACHBT', '0809909865', 1, 'Phan Văn Quyết', 320000),
('KH01102021-000030', 'KHACHBT', '0981353188', 0, 'Lê Văn Vinh', 0),
('KH02022021-000049', 'KHACHBT', '0751681684', 0, 'Nguyễn Bảo Nhật Lệ', 0),
('KH03042021-000012', 'KHACHBT', '0723031756', 0, 'Nguyễn Viết Tịnh', 0),
('KH03122023-000001', 'KHACHVIP', '0965180419', 9, 'A', 1751289),
('KH03122023-000002', 'KHACHLE', '', 1, 'Khách lẻ', 56160),
('KH03122023-000003', 'KHACHLE', '', 1, 'Khách lẻ', 133000),
('KH03122023-000004', 'KHACHLE', '', 0, 'Khách lẻ', 0),
('KH03122023-000005', 'KHACHLE', '', 1, 'Khách lẻ', 143000),
('KH03122023-000006', 'KHACHLE', '', 1, 'Khách lẻ', 56160),
('KH03122023-000007', 'KHACHLE', '', 1, 'Khách lẻ', 142142),
('KH04122023-000001', 'KHACHLE', '', 1, 'Khách lẻ', 56160),
('KH04122023-000002', 'KHACHLE', '', 1, 'Khách lẻ', 356340),
('KH04122023-000003', 'KHACHLE', '', 1, 'Khách lẻ', 267800),
('KH04122023-000004', 'KHACHLE', '', 1, 'Khách lẻ', 56160),
('KH05022021-000003', 'KHACHBT', '0987486132', 0, 'Phạm Ái Linh', 0),
('KH05032021-000057', 'KHACHBT', '0716161681', 0, 'Lê Nhật Minh', 0),
('KH05062021-000022', 'KHACHBT', '0784651113', 0, 'Hoàng Bảo Ngọc', 0),
('KH05092021-000040', 'KHACHBT', '0354654677', 0, 'Nguyễn Huyền Trang', 0),
('KH05092021-000042', 'KHACHBT', '0971231234', 0, 'Lê Quốc Bình', 0),
('KH05122023-000001', 'KHACHLE', '', 1, 'Khách lẻ', 227250),
('KH05122023-000002', 'KHACHLE', '', 1, 'Khách lẻ', 61750),
('KH05122023-000003', 'KHACHLE', '', 1, 'Khách lẻ', 143000),
('KH05122023-000004', 'KHACHLE', '', 1, 'Khách lẻ', 143000),
('KH05122023-000005', 'KHACHLE', '', 0, 'Khách lẻ', 0),
('KH06122023-000001', 'KHACHLE', '', 0, 'Khách lẻ', 0),
('KH06122023-000002', 'KHACHLE', '', 0, 'Khách lẻ', 0),
('KH06122023-000003', 'KHACHLE', '', 0, 'Khách lẻ', 0),
('KH06122023-000004', 'KHACHLE', '', 1, 'Khách lẻ', 303940),
('KH06122023-000005', 'KHACHLE', '', 1, 'Khách lẻ', 304590),
('KH06122023-000006', 'KHACHLE', '', 1, 'Khách lẻ', 307190),
('KH06122023-000007', 'KHACHLE', '', 1, 'Khách lẻ', 204750),
('KH06122023-000008', 'KHACHLE', '', 1, 'Khách lẻ', 99190),
('KH06122023-000009', 'KHACHLE', '', 1, 'Khách lẻ', 242190),
('KH06122023-000010', 'KHACHLE', '', 1, 'Khách lẻ', 304590),
('KH06122023-000011', 'KHACHLE', '', 0, 'Khách lẻ', 0),
('KH06122023-000012', 'KHACHLE', '', 1, 'Khách lẻ', 657540),
('KH07062021-000023', 'KHACHBT', '0987411444', 0, 'La Vân Hy', 0),
('KH08052021-000026', 'KHACHBT', '0988545716', 0, 'Anh Ðức', 0),
('KH08082021-000047', 'KHACHBT', '0323031645', 0, 'Phạm Thị Mỹ Tuyền', 0),
('KH09022021-000014', 'KHACHBT', '0934553252', 0, 'Phạm Hùng Cường', 0),
('KH09032021-000059', 'KHACHBT', '0756235633', 0, 'Trần Hải Bằng', 0),
('KH09082021-000009', 'KHACHBT', '0792030217', 0, 'Nguyễn Hồng Huy', 0),
('KH10032021-000013', 'KHACHBT', '0872030020', 0, 'Phạm Anh Khoa', 0),
('KH10082021-000052', 'KHACHBT', '0976016351', 0, 'Võ Đức Hòa', 0),
('KH12082021-000010', 'KHACHBT', '0564464168', 0, 'Đỗ Phúc Hưng ', 0),
('KH12092021-000063', 'KHACHBT', '0798548646', 0, 'Phạm Gia Phúc', 0),
('KH13062021-000039', 'KHACHBT', '0317861122', 0, 'Nguyễn Khánh Ngọc', 0),
('KH13102021-000008', 'KHACHBT', '0393536342', 0, 'Huỳnh Việt Nhật', 0),
('KH13122023-000001', 'KHACHBT', '0965180417', 0, 'Nguyen Chau Tinh', 0),
('KH14032021-000050', 'KHACHBT', '0584613815', 0, 'Nguyễn Thùy Chi', 0),
('KH14062021-000033', 'KHACHBT', '0965161213', 0, 'Phạm Hoàng', 0),
('KH15032021-000007', 'KHACHBT', '0981272137', 0, 'Đỗ Quỳnh Chi', 0),
('KH15052021-000060', 'KHACHBT', '0968416013', 0, 'Cao Khai Minh', 0),
('KH15092021-000032', 'KHACHBT', '0325489494', 0, 'Đỗ Song Oanh', 0),
('KH15102021-000037', 'KHACHBT', '0509554546', 0, 'Phạm Duy Thắng', 0),
('KH16012021-000061', 'KHACHBT', '0550955128', 0, 'Hồ Ðức Phong', 0),
('KH16022021-000017', 'KHACHBT', '0795164813', 0, 'Phạm An Hạ ', 0),
('KH16072021-000027', 'KHACHBT', '0787210218', 0, 'Ngô Kỳ Anh', 0),
('KH16072021-000053', 'KHACHBT', '0632361019', 0, 'Hồ Viết Vũ', 0),
('KH17042021-000019', 'KHACHBT', '0501298742', 0, 'Lê Minh Giang', 0),
('KH17062021-000036', 'KHACHBT', '0844447144', 0, 'Hồ Ngọc Thiên Kim', 0),
('KH18122021-000011', 'KHACHBT', '0312346584', 0, 'Hoàng Húc Hi', 0),
('KH19012021-000058', 'KHACHBT', '0315932603', 0, 'Nguyễn Thị Hiên', 0),
('KH19062021-000046', 'KHACHBT', '0561235290', 0, 'Bình Dân', 0),
('KH20012021-000006', 'KHACHBT', '0731186168', 0, 'Lê Bảo Trân', 0),
('KH20032021-000016', 'KHACHBT', '0894684141', 0, 'Chu Bạch Liên', 0),
('KH20032021-000056', 'KHACHBT', '0885492588', 0, 'Lê Minh Quân', 0),
('KH20092021-000044', 'KHACHBT', '0566194647', 0, 'Hà Hữu Châu', 0),
('KH20112021-000055', 'KHACHBT', '0381646423', 0, 'Hà Hùng Anh', 0),
('KH22022021-000015', 'KHACHBT', '0916351811', 0, 'Nguyễn Diệp Chi', 0),
('KH22042024-000001', 'KHACHBT', '', 0, 'Khách lẻ', 7.5),
('KH22042024-000002', 'KHACHBT', '', 0, 'Khách lẻ', 0),
('KH22042024-000003', 'KHACHBT', '', 0, 'Khách lẻ', 6),
('KH22042024-000004', 'KHACHBT', '', 0, 'Khách lẻ', 1.5),
('KH22042024-000005', 'KHACHLE', '', 1, 'Khách lẻ', 1.5),
('KH22042024-000006', 'KHACHLE', '', 1, 'Khách lẻ', 12),
('KH22042024-000007', 'KHACHLE', '', 1, 'Khách lẻ', 150),
('KH22042024-000008', 'KHACHLE', '', 1, 'Khách lẻ', 150),
('KH22042024-000009', 'KHACHBT', '0934534566', 0, 'AAAAA', 0),
('KH22062021-000021', 'KHACHBT', '0311368411', 0, 'Cao Hoàng', 0),
('KH23042024-000001', 'KHACHLE', '', 1, 'Khách lẻ', 144000),
('KH23042024-000002', 'KHACHLE', '', 1, 'Khách lẻ', 36000),
('KH23042024-000003', 'KHACHLE', '0987654321', 1, 'KhachMot', 10000),
('KH23042024-000004', 'KHACHBT', '0321654897', 30, 'KhachHai', 300000),
('KH23042024-000005', 'KHACHVIP', '0897465321', 70, 'KhachBa', 700000),
('KH23042024-000006', 'KHACHLE', '', 0, 'Khách lẻ', 0),
('KH23062021-000051', 'KHACHBT', '0500350308', 0, 'Đỗ Quang Khải', 0),
('KH23082021-000024', 'KHACHBT', '0916843158', 0, 'Phạm Đức Duy', 0),
('KH23082021-000045', 'KHACHBT', '0804076830', 0, 'Võ Đức Hùng', 0),
('KH23102021-000028', 'KHACHBT', '0596709190', 0, 'Võ Văn Thiên', 0),
('KH24052021-000035', 'KHACHBT', '0754623462', 0, 'Trần Hải Ðăng', 0),
('KH24102021-000029', 'KHACHBT', '0515136816', 0, 'Hồ Tuệ Nhi', 0),
('KH24102023-000003', 'KHACHLE', '', 10, '', 10),
('KH24102023-000004', 'KHACHLE', '', 11, '', 11),
('KH25082021-000043', 'KHACHBT', '0579999861', 0, 'Trần Đức Huy', 0),
('KH25082021-000048', 'KHACHBT', '0798824883', 0, 'Phạm Ðại Dương', 0),
('KH25102021-000004', 'KHACHBT', '0979812341', 0, 'Lại Dương Hoa', 0),
('KH25102023-000001', 'KHACHBT', '0875849592', 2, 'Randal Ondra', 62401),
('KH25102023-000002', 'KHACHBT', '0804076819', 2, 'Huberto MacGillacolm', 2),
('KH25102023-000003', 'KHACHBT', '0804076819', 2, 'Huberto MacGillacolm', 2),
('KH25102023-000004', 'KHACHBT', '0315932603', 3, 'Cad Upson', 3),
('KH25102023-000005', 'KHACHBT', '0787210218', 4, 'Waite Whates', 4),
('KH25102023-000006', 'KHACHBT', '0783186012', 6, 'Bernette Krienke', 6),
('KH25102023-000007', 'KHACHBT', '0393536342', 7, 'Kip Schimek', 7),
('KH26102023-000001', 'KHACHLE', '', 8, '', 8),
('KH26102023-000002', 'KHACHBT', '0632361019', 9, 'Helga Stuffins', 9),
('KH26122021-000005', 'KHACHBT', '0783186012', 0, 'Trần Cao Chiến', 0),
('KH27012021-000062', 'KHACHBT', '0744628620', 0, 'Lê Lâm Oanh', 0),
('KH27072021-000041', 'KHACHBT', '0523030882', 0, 'Phan Thi Trân', 0),
('KH27102023-000005', 'KHACHBT', '0579999861', 12, 'Alexine Newart', 12),
('KH27102023-000006', 'KHACHBT', '0596709190', 13, 'Costanza Kick', 13),
('KH28012021-000018', 'KHACHBT', '0762143613', 0, 'Trần Thu Nguyệt', 0),
('KH28032021-000002', 'KHACHBT', '0841651681', 0, 'Phạm Diệu Anh', 0),
('KH28082021-000054', 'KHACHBT', '0360971756', 0, 'Phạm Tú Quyên', 0),
('KH29062021-000025', 'KHACHBT', '0523031435', 1, 'Võ Thị Ngọc Lan', 416000),
('KH29062021-000038', 'KHACHBT', '0513518381', 1, 'Đỗ Quốc Bảo', 22500),
('KH30062021-000020', 'KHACHBT', '0784564616', 0, 'Phạm Nhật Minh', 0),
('KH30102021-000034', 'KHACHBT', '0364894351', 0, 'Võ Tấn Lộc', 0),
('KH30122021-000031', 'KHACHBT', '0568438718', 1, 'Chu Ðoan Trang', 32500);

-- Chèn dữ liệu vào bảng NhanVien
INSERT INTO NhanVien (maNhanVien, chucVu, email, gioiTinh, hoTenNV, ngaySinh, soDienThoaiNV, tinhTrangLamViec, caLamViec, taiKhoan) VALUES
('QL22042024-000001', 'QUANLY', 'A@a', 'Nữ', 'B', '2024-04-22', '0983576463', 1, 'CA02', 'A@a'),
('QL23102023-000001', 'QUANLY', 'nguyentanloc1108@gmail.com', 'Nam', 'Nguyễn Tấn Lộc', '2003-12-04', '0362447457', 1, 'CA01', 'QL23102023000001'),
('QL23102023-000007', 'QUANLY', 'chautinh05122@gmail.com', 'Nam', 'Nguyễn Châu Tình', '2003-01-18', '0909546226', 1, 'CA02', 'QL23102023000007'),
('QL23102023-000012', 'QUANLY', 'tdh1503@gmail.com', 'Nam', 'Trần Đăng Hiếu', '2003-03-15', '0955698563', 1, 'CA03', 'QL23102023000012'),
('TN22042024-000001', 'THUNGAN', 'A@q', 'Nam', 'A', '2024-04-22', '0946664673', 1, 'CA01', 'A@q'),
('TN23102023-000002', 'THUNGAN', 'kimanhvo@gmail.com', 'Nữ', 'Võ Thị Kim Anh', '1990-03-11', '0365221423', 1, 'CA01', 'TN23102023000002'),
('TN23102023-000003', 'THUNGAN', 'thanhnhanwork@gmail.com', 'Nam', 'Nguyễn Thành Nhân', '2000-09-21', '0707569255', 1, 'CA01', 'TN23102023000003'),
('TN23102023-000004', 'THUNGAN', 'hoangvanthuan@gmail.com', 'Nam', 'Hoàng Văn Thuận', '2001-11-10', '0886245365', 1, 'CA01', 'TN23102023000004'),
('TN23102023-000005', 'THUNGAN', 'dangquyettranwork@gmail.com', 'Nam', 'Trần Đăng Quyết', '2000-07-12', '09882146325', 1, 'CA02', 'TN23102023000005'),
('TN23102023-000006', 'THUNGAN', 'thanhnam@gmail.com', 'Nam', 'Đặng Thanh Nam', '2000-12-25', '0325226985', 1, 'CA02', 'TN23102023000006'),
('TN23102023-000008', 'THUNGAN', 'nhiwork@gmail.com', 'Nữ', 'Trần Thị Nhi', '2001-05-12', '0778985665', 1, 'CA02', 'TN23102023000008'),
('TN23102023-000009', 'THUNGAN', 'thanhthuan@gmail.com', 'Nam', 'Nguyễn Thành Thuận', '2000-09-21', '0907599155', 1, 'CA03', 'TN23102023000009'),
('TN23102023-000010', 'THUNGAN', 'namphidoan@gmail.com', 'Nam', 'Đoàn Nam Phi', '2001-04-19', '0566789452', 1, 'CA03', 'TN23102023000010'),
('TN23102023-000011', 'THUNGAN', 'phuongvo@gmail.com', 'Nữ', 'Võ Thị Phương', '2000-03-16', '0965251364', 1, 'CA03', 'TN23102023000011');

-- Chèn dữ liệu vào bảng SanPham
INSERT INTO SanPham (maSanPham, VAT, donGiaBan, donGiaNhap, giamGia, moTa, ngayTao, soLuongTon, tenSanPham, tinhTrang, nhaCungCap, nhomSanPham) VALUES
('S01122022-000001', 0.08, 32500, 25000, 0, 'Fushiguro cũng đã thâm nhập và đang trên đường tới căn cứ, nhưng lại bị một thuật sư năm 3 đi cùng Hakari cản trở!!?', '2022-12-01 16:44:17', 51, 'Chú Thuật Hồi Chiến', 'Còn hàng', 'NCC23102023-000002', 'NSP005'),
('S01122022-000002', 0.08, 40000, 32000, 0, 'Các lính trinh sát đối đầu với kẻ thù cuối cùng trong một cuộc chiến sống còn cho sự tồn vong của loài người.', '2023-06-20 11:30:00', 22, 'Attack on Titan: Sự kết thúc', 'Còn hàng', 'NCC23102023-000002', 'NSP005'),
('S01122022-000004', 0.08, 34000, 27000, 0, 'Naruto và các bạn bắt đầu một cuộc hành trình mới để bảo vệ làng Lá và thế giới ninja khỏi hiểm nguy mới.', '2023-04-10 09:18:00', 35, 'Naruto: Cuộc phiêu lưu mới', 'Còn hàng', 'NCC23102023-000002', 'NSP005'),
('S10052023-000004', 0.08, 36000, 24000, 0, '', '2023-05-10 01:17:46', 495, 'Tiếng Việt Lớp 2-  Tập 2', 'Còn hàng', 'NCC23102023-000005', 'NSP004'),
('S13122023-000001', 0.08, 32500, 25000, 0, 'Fushiguro cũng đã thâm nhập và đang trên đường tới căn cứ, nhưng lại bị một thuật sư năm 3 đi cùng Hakari cản trở!!?', '2022-12-01 16:44:17', 47, 'Chú Thuật Hồi Chiến', 'Còn hàng', 'NCC23102023-000002', 'NSP005'),
('S13122023-000002', 0.08, 35000, 28000, 0, 'Yuuji và nhóm của mình tiếp tục hành trình để điều tra các vụ án ma quỷ và đối đầu với các hắc pháp sư.', '2023-01-15 10:22:30', 30, 'Nguyên Thủy Triệu Hồi Sư', 'Còn hàng', 'NCC23102023-000002', 'NSP005'),
('S13122023-000003', 0.08, 38000, 30000, 0, 'Ichigo và các bạn đối đầu với tên thần chết mạnh mẽ nhất và cuối cùng.', '2023-02-28 14:05:45', 20, 'Bleach: Trận chiến cuối cùng', 'Còn hàng', 'NCC23102023-000002', 'NSP005'),
('S22042024-000001', 0.1, 1.5, 1, 1, 'A', '2024-04-22 15:08:01', 9999993, 'A', 'Còn hàng', 'NCC23102023-000006', 'NSP001'),
('S22042024-000002', 0.1, 1.5, 1, 1, 'A', '2024-04-22 15:20:49', 888, 'A', 'Còn hàng', 'NCC23102023-000007', 'NSP004'),
('S22042024-000003', 0.1, 1.5, 1, 1, 'A', '2024-04-22 15:23:23', 99900, 'B', 'Còn hàng', 'NCC23102023-000006', 'NSP001'),
('S22042024-000004', 0.1, 1.5, 1, 1, 'A', '2024-04-22 15:58:27', 10000, 'C', 'Còn hàng', 'NCC24102023-000001', 'NSP001'),
('S23042024-000001', 0.1, 1500, 1000, 1, 'A', '2024-04-23 01:21:31', 100, 'TIEN', 'Còn hàng', 'NCC23102023-000006', 'NSP001'),
('S23042024-000002', 0.08, 20000, 10000, 0, 'không', '2023-11-10 19:41:57', 1000, 'Sách TV', 'Còn hàng', 'NCC23102023-000004', 'NSP001'),
('S23042024-000003', 0.08, 30000, 10000, 0, 'có', '2023-11-10 19:41:57', 1000, 'Sách TA', 'Còn hàng', 'NCC23102023-000005', 'NSP002'),
('V23042024-0000001', 0.08, 20000, 10000, 0, 'khong', '2023-11-10 19:41:57', 100, 'But Chi', 'Còn hàng', 'NCC23102023-000004', 'NSP001'),
('V23042024-0000002', 0.08, 30000, 10000, 0, 'co', '2023-11-10 19:41:57', 100, 'But Muc', 'Còn hàng', 'NCC23102023-000005', 'NSP002'),
('VPP23102023-000005', 0.08, 11570, 8900, 0, 'Loại bút lông bảng thân cỡ vừa, được thiết kế phù hợp với giáo viên, NVVP', '2023-11-29 23:48:06', 72, 'BÚT LÔNG BẢNG WB-03', 'Còn hàng', 'NCC24102023-000003', 'NSP001');

-- Chèn dữ liệu vào bảng Sach
-- Chèn dữ liệu vào bảng Sach
INSERT INTO Sach (namXuatBan, nhaXuatBan, soTrang, tacGia, maSanPham) VALUES
(2023, 'NXB Kim Đồng', 192, 'Gege Akutami', 'S01122022-000001'),
(2021, 'NXB Kim Đồng', 320, 'Hajime Isayama', 'S01122022-000002'),
(2023, 'NXB Trẻ', 224, 'Masashi Kishimoto', 'S01122022-000004'),
(2023, 'AZ Việt Nam', 128, 'Bộ Giáo dục', 'S10052023-000004'),
(2022, 'NXB Kim Đồng', 192, 'Gege Akutami', 'S13122023-000001'),
(2023, 'NXB Kim Đồng', 200, 'Tetsuya Nomura', 'S13122023-000002'),
(2022, 'NXB Kim Đồng', 208, 'Tite Kubo', 'S13122023-000003'),
(2024, 'NXB Trẻ', 100, 'Không có', 'S22042024-000001'),
(2024, 'First News', 100, 'Không có', 'S22042024-000002'),
(2024, 'NXB Trẻ', 100, 'Không có', 'S22042024-000003'),
(2024, 'Đinh Tị', 100, 'Không có', 'S22042024-000004'),
(2024, 'NXB Trẻ', 100, 'Không có', 'S23042024-000001'),
(2023, 'Bình Tây', 150, 'Bộ Giáo dục', 'S23042024-000002'),
(2023, 'AZ Việt Nam', 150, 'Bộ Giáo dục', 'S23042024-000003');

-- Chèn dữ liệu vào bảng VanPhongPham
INSERT INTO VanPhongPham (chatLieu, kichThuoc, maSanPham, mauSac) VALUES
('Nhựa', '10cm', 'V23042024-0000001', 'BLACK'),
('Nhựa', '12cm', 'V23042024-0000002', 'BLUE'),
('Nhựa và kim loại', '15cm', 'VPP23102023-000005', 'BLUE');

-- Chèn dữ liệu vào bảng HoaDon
INSERT INTO HoaDon (maHoaDon, chietKhau, ghiChu, khuyenMai, ngayLap, tinhTrangHoaDon, tongTien, khachHang, nhanVien) VALUES
('HD22042024-000001', 0, '', '', '2024-04-22 15:08:23', -1, 7.5, 'KH22042024-000001', 'QL23102023-000007'),
('HD22042024-000002', 0, '', '', '2024-04-22 15:18:10', -1, 1.5, 'KH22042024-000002', 'QL23102023-000007'),
('HD22042024-000003', 0, '', '', '2024-04-22 15:18:17', -1, 6, 'KH22042024-000003', 'QL23102023-000007'),
('HD22042024-000004', 0, '', '', '2024-04-22 15:18:22', -1, 3, 'KH22042024-000004', 'QL23102023-000007'),
('HD22042024-000005', 0, '', '', '2024-04-22 15:21:23', -1, 1.5, 'KH22042024-000005', 'QL23102023-000007'),
('HD22042024-000006', 0, '', '', '2024-04-22 15:23:27', -1, 12, 'KH22042024-000006', 'QL23102023-000007'),
('HD22042024-000007', 0, '', '', '2024-04-22 15:58:31', -1, 150, 'KH22042024-000007', 'QL23102023-000007'),
('HD22042024-000008', 0, '', '', '2024-04-22 16:02:45', -1, 150, 'KH22042024-000008', 'QL23102023-000007'),
('HD23042024-000001', 0, '', '', '2024-04-23 01:21:45', -1, 144000, 'KH23042024-000001', 'QL23102023-000007'),
('HD23042024-000002', 0, '', '', '2024-04-23 01:22:31', -1, 36000, 'KH23042024-000002', 'QL23102023-000007'),
('HD23042024-000004', 0, '', '', '2024-04-23 22:04:52', -1, 420000, 'KH23042024-000004', 'QL23102023-000007');

-- Chèn dữ liệu vào bảng ChiTietHoaDon
INSERT INTO ChiTietHoaDon (soLuong, thanhTien, hoaDon, sanPham) VALUES
(5, 7.5, 'HD22042024-000001', 'S22042024-000001'),
(1, 1.5, 'HD22042024-000002', 'S22042024-000001'),
(4, 6, 'HD22042024-000003', 'S22042024-000001'),
(2, 3, 'HD22042024-000004', 'S22042024-000001'),
(1, 1.5, 'HD22042024-000005', 'S22042024-000002'),
(1, 1.5, 'HD22042024-000006', 'S22042024-000001'),
(7, 10.5, 'HD22042024-000006', 'S22042024-000002'),
(100, 150, 'HD22042024-000007', 'S22042024-000002'),
(100, 150, 'HD22042024-000008', 'S22042024-000003'),
(4, 144000, 'HD23042024-000001', 'S10052023-000004'),
(1, 36000, 'HD23042024-000002', 'S10052023-000004'),
(3, 120000, 'HD23042024-000004', 'S01122022-000002'),
(5, 170000, 'HD23042024-000004', 'S01122022-000004'),
(4, 130000, 'HD23042024-000004', 'S13122023-000001');

-- Chèn dữ liệu vào bảng HoaDonHoanTra
INSERT INTO HoaDonHoanTra (maHoaDonHoanTra, lyDoHoanTra, ngayHoanTra, tinhTrangHoaDon, tienHoanTra, hoaDon, nhanVien) VALUES
('HT22042024-000001', 'Sản phẩm lỗi', '2024-04-22 15:08:34', -1, 7.5, 'HD22042024-000001', 'QL23102023-000007'),
('HT22042024-000002', 'Khách không muốn', '2024-04-22 15:18:34', -1, 3, 'HD22042024-000004', 'QL23102023-000007'),
('HT23042024-000001', 'Sản phẩm lỗi', '2024-04-23 01:22:34', -1, 1.5, 'HD22042024-000002', 'QL23102023-000007'),
('HT23042024-000002', 'Khách đổi ý', '2024-04-23 01:22:45', -1, 6, 'HD22042024-000003', 'QL23102023-000007');

-- Chèn dữ liệu vào bảng ChiTietHoanTra
INSERT INTO ChiTietHoanTra (soLuong, thanhTien, sanPham, hoaDonHoanTra) VALUES
(5, 7.5, 'S22042024-000001', 'HT22042024-000001'),
(2, 3, 'S22042024-000001', 'HT22042024-000002'),
(1, 1.5, 'S22042024-000001', 'HT23042024-000001'),
(4, 6, 'S22042024-000001', 'HT23042024-000002');

