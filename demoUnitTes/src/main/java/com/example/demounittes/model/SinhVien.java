package com.example.demounittes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author thangdt
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
@Entity
@Builder
public class SinhVien {

    @Id
    private String id;
    private String name;
    private String email;

}
