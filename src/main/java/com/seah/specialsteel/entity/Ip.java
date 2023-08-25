package com.seah.specialsteel.entity;

import com.seah.specialsteel.dto.IpDTO;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
public class Ip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String address;

    public IpDTO entityToDTO(){
        IpDTO ipDTO = new IpDTO();
        ipDTO.setAddress(address);
        ipDTO.setId(id);

        return ipDTO;
    }
}
