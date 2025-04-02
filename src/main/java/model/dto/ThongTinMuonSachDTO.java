package model.dto;

import java.sql.Date;

public class ThongTinMuonSachDTO {
    private int maMuonSach;
    private int maSach;
    private String tenSach;
    private String tacGia;
    private String hoTenHocSinh;
    private String lopHocSinh;
    private Date ngayMuon;
    private Date ngayTra;

    public ThongTinMuonSachDTO() {
    }

    public ThongTinMuonSachDTO(int maMuonSach, int maSach, String tenSach, String tacGia, String hoTenHocSinh, String lopHocSinh, Date ngayMuon, Date ngayTra) {
        this.maMuonSach = maMuonSach;
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.hoTenHocSinh = hoTenHocSinh;
        this.lopHocSinh = lopHocSinh;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
    }

    public int getMaMuonSach() {
        return maMuonSach;
    }

    public void setMaMuonSach(int maMuonSach) {
        this.maMuonSach = maMuonSach;
    }

    public int getMaSach() {
        return maSach;
    }

    public void setMaSach(int maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getHoTenHocSinh() {
        return hoTenHocSinh;
    }

    public void setHoTenHocSinh(String hoTenHocSinh) {
        this.hoTenHocSinh = hoTenHocSinh;
    }

    public String getLopHocSinh() {
        return lopHocSinh;
    }

    public void setLopHocSinh(String lopHocSinh) {
        this.lopHocSinh = lopHocSinh;
    }

    public Date getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(Date ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public Date getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(Date ngayTra) {
        this.ngayTra = ngayTra;
    }
}