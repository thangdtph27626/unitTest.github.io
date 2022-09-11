package com.example.demounittes.service;

import com.example.demounittes.model.SinhVien;

import java.util.List;

/**
 * @author thangdt
 */

public interface SinhVienService {

    List<SinhVien> getList();

    SinhVien addNew(SinhVien sinhVien);

    boolean delete(String id);

    SinhVien update(String id, SinhVien sinhVien);

    SinhVien findById(String id);
}
