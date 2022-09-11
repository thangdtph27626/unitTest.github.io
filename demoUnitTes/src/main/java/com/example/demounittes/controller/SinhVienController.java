package com.example.demounittes.controller;

import com.example.demounittes.model.SinhVien;
import com.example.demounittes.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author thangdt
 */
@RestController
public class SinhVienController {


    @Autowired
    private SinhVienService sinhVienService;

    @PostMapping("/add")
    public SinhVien addNew(@RequestBody SinhVien sinhVien) {
        return sinhVienService.addNew(sinhVien);
    }

    @GetMapping("/{id}")
    public SinhVien DetailSinhVien(@PathVariable("id") String id) {
        SinhVien sinhVien = null;
        try {
            sinhVien = sinhVienService.findById(id);
        } catch (Exception e) {
            System.out.println(e);
        }
        return sinhVien;
    }

    @PutMapping("/{id}")
    public SinhVien update(@PathVariable("id") String id, @RequestBody SinhVien sinhVienRequest) {
        return sinhVienService.update(id, sinhVienRequest);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") String id) {
        return sinhVienService.delete(id);
    }

    @GetMapping("/list-sinh-vien")
    public List<SinhVien> listSinhVien() {
        List<SinhVien> list = sinhVienService.getList();
        return list;
    }

}
