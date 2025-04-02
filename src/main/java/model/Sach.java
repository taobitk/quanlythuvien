package model;

public class Sach {

    private int maSach;
    private String tenSach;
    private String tacGia;
    private String moTa;
    private int soLuong;

    public Sach() {
    }

    public Sach(String tenSach, String tacGia, String moTa, int soLuong) {
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.moTa = moTa;
        this.soLuong = soLuong;
    }

    public Sach(int maSach, String tenSach, String tacGia, String moTa, int soLuong) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.moTa = moTa;
        this.soLuong = soLuong;
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

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public String toString() {
        return "Sach{" +
                "maSach=" + maSach +
                ", tenSach='" + tenSach + '\'' +
                ", tacGia='" + tacGia + '\'' +
                ", moTa='" + moTa + '\'' +
                ", soLuong=" + soLuong +
                '}';
    }
}