package com.clinic.appointment.controller;

import com.clinic.appointment.dto.PatientDTO;
import com.clinic.appointment.dto.PatientResponse;
import com.clinic.appointment.model.Patient;
import com.clinic.appointment.model.constant.PatientType;
import com.clinic.appointment.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("patientType", PatientType.values());
        return "patients/create";
    }

    @PostMapping("/create")
    public String createPatient(@ModelAttribute Patient patient,Model model) {
        model.addAttribute("patient", patient);
        patientService.create(patient,model);
        return "redirect:/patients";
    }

    @GetMapping
    public String getPatients(Model model,
                              @RequestParam(defaultValue = "0",required = false) Integer pageNumber,
                              @RequestParam(defaultValue = "9",required = false) Integer pageSize,
                              @RequestParam(defaultValue = "name",required = false) String sortBy,
                              @RequestParam(defaultValue = "asc",required = false) String sortOrder, Sort sort) {
        PatientResponse patientResponse = patientService.getAllPatients(pageNumber,pageSize,sortBy,sortOrder);
        List<PatientDTO> patientDTOList = new ArrayList<>(patientResponse.getPatients());
        model.addAttribute("patients",patientDTOList);
        model.addAttribute("response",patientResponse);
        model.addAttribute("sortBy",sortBy);
        model.addAttribute("sortOrder",sortOrder);
        return "patients/listing";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Patient patient = this.patientService.findById(id);
        if(patient == null) return "redirect:/patients";
        model.addAttribute("patient",patient);
        model.addAttribute("patientType", PatientType.values());
        return "patients/edit";
    }

    @PostMapping("/update/{id}")
    public String updatePatient(@PathVariable("id") Long id,@ModelAttribute Patient patient,Model model) {
        model.addAttribute("patient",patient);
        this.patientService.update(id,patient,model);
        return "redirect:/patients";
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable("id") Long id) {
        this.patientService.deleteById(id);
        return "redirect:/patients";
    }
}
