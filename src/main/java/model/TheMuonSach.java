package model;

import java.sql.Date;

public class TheMuonSach {

    private int maMuonSach;
    private int maSach;
    private int maHocSinh;
    private boolean trangThai;
    private Date ngayMuon;
    private Date ngayTra;

    public TheMuonSach() {
    }

    public TheMuonSach(int maSach, int maHocSinh, boolean trangThai, Date ngayMuon, Date ngayTra) {
        this.maSach = maSach;
        this.maHocSinh = maHocSinh;
        this.trangThai = trangThai;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
    }

    public TheMuonSach(int maMuonSach, int maSach, int maHocSinh, boolean trangThai, Date ngayMuon, Date ngayTra) {
        this.maMuonSach = maMuonSach;
        this.maSach = maSach;
        this.maHocSinh = maHocSinh;
        this.trangThai = trangThai;
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

    public int getMaHocSinh() {
        return maHocSinh;
    }

    public void setMaHocSinh(int maHocSinh) {
        this.maHocSinh = maHocSinh;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
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

    @Override
    public String toString() {
        return "TheMuonSach{" +
                "maMuonSach=" + maMuonSach +
                ", maSach=" + maSach +
                ", maHocSinh=" + maHocSinh +
                ", trangThai=" + trangThai +
                ", ngayMuon=" + ngayMuon +
                ", ngayTra=" + ngayTra +
                '}';
    }
}