package com.seah.specialsteel.controller;

import com.seah.specialsteel.dto.IpDTO;
import com.seah.specialsteel.dto.ResultDTO;
import com.seah.specialsteel.entity.Ip;
import com.seah.specialsteel.repository.IpRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@AllArgsConstructor
public class MainController {

    private final IpRepository ipRepository;
    @GetMapping({"/", "/result"})
    public String main(Model model) {
        List<Ip> ipList =  ipRepository.findAll();
        List<IpDTO> ipDTOList =
                ipList.stream()
                        .map(ip -> ip.entityToDTO())
                        .collect(Collectors.toList());

//        List<String> ipAddresses = ipDTOList.stream()
//                .map(ip -> ip.getAddress())
//                .collect(Collectors.toList());

        model.addAttribute("ipDTOList",ipDTOList);

        return "result";
    }
    @PostMapping("/add_ip")
    public ResponseEntity<List<IpDTO>> addIp(@RequestParam String ipAddress){
        Ip ip2 = new Ip();
        ip2.setAddress(ipAddress);
        ipRepository.save(ip2);

        List<Ip> ipList =  ipRepository.findAll();
        List<IpDTO> ipDTOList =
                ipList.stream()
                        .map(ip -> ip.entityToDTO())
                        .collect(Collectors.toList());
        return new ResponseEntity<>(ipDTOList, HttpStatus.OK);
    }


    @PostMapping("/deleteIp/{id}")
    public ResponseEntity<List<IpDTO>> deleteIp(@PathVariable Long id){
        ipRepository.delete(ipRepository.findById(id).orElseThrow());

        List<Ip> ipList =  ipRepository.findAll();
        List<IpDTO> ipDTOList =
                ipList.stream()
                        .map(ip -> ip.entityToDTO())
                        .collect(Collectors.toList());
        return new ResponseEntity<>(ipDTOList, HttpStatus.OK);
    }


    @GetMapping("/encoding")
    public String encoding(Model model) {
        return "encoding";
    }

}