package com.example.demounittes.service.impl;

import com.example.demounittes.model.SinhVien;
import com.example.demounittes.repository.SinhVienRepository;
import com.example.demounittes.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author thangdt
 */

@Service
@Component
public class SinhVienSericeImpl implements SinhVienService {

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Override
    public List<SinhVien> getList() {
        return sinhVienRepository.findAll();
    }

    @Override
    public SinhVien addNew(SinhVien sinhVien) {
        if (sinhVienRepository.selectExistsEmail(sinhVien.getEmail()) == 0) {
            throw new IllegalArgumentException("email da ton tai");
        }
        return sinhVienRepository.save(sinhVien);
    }

    @Override
    public boolean delete(String id) {
        Optional<SinhVien> sinhVien = sinhVienRepository.findById(id);
        if (sinhVien.isPresent()) {
            sinhVienRepository.deleteById(id);
            return true;
        } else {
            throw new IllegalArgumentException("id not exists");
        }
    }

    @Override
    public SinhVien update(String id, SinhVien sinhVien) {
        SinhVien updateSinhVien = sinhVienRepository.findById(id).orElse(null);
        if (updateSinhVien == null) {
            throw new IllegalArgumentException("id not exists");
        }
        if (sinhVienRepository.selectExistsEmail(sinhVien.getEmail()) == 0) {
            throw new IllegalArgumentException("email da ton tai");
        }
        updateSinhVien.setId(sinhVien.getId());
        updateSinhVien.setName(sinhVien.getName());
        updateSinhVien.setEmail(sinhVien.getEmail());
        return sinhVienRepository.save(updateSinhVien);
    }

    @Override
    public SinhVien findById(String id) {
        return sinhVienRepository.findById(id).orElse(null);
    }
}
