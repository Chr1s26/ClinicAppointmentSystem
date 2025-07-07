package com.clinic.appointment.service;

import com.clinic.appointment.dto.department.DepartmentDTO;
import com.clinic.appointment.dto.department.DepartmentResponse;
import com.clinic.appointment.exception.CommonException;
import com.clinic.appointment.exception.ErrorMessage;
import com.clinic.appointment.helper.StringUtil;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public DepartmentResponse getAllDepartments(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder){
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Department> departmentPage = departmentRepository.findAll(pageable);
        List<Department> departments = departmentPage.getContent();
        List<DepartmentDTO> departmentDTOS = departments.stream().map(department -> modelMapper.map(department,DepartmentDTO.class)).toList();
        DepartmentResponse departmentResponse = new DepartmentResponse();
        departmentResponse.setDepartments(departmentDTOS);
        departmentResponse.setTotalElements(departmentPage.getTotalElements());
        departmentResponse.setTotalPages(departmentPage.getTotalPages());
        departmentResponse.setPageNumber(departmentPage.getNumber());
        departmentResponse.setPageSize(departmentPage.getSize());
        departmentResponse.setLastPage(departmentPage.isLast());
        return departmentResponse;
    }

    public List<Department> getAllDepartments(){
        List<Department> departments = departmentRepository.findAll();
        return departments;
    }

    public Department createDepartment(Department department, Model model){
        List<ErrorMessage> errorMessages = new ArrayList<>();
        validate(department,errorMessages);

        if(!errorMessages.isEmpty()) {
            model.addAttribute("department", department);
            throw new CommonException(errorMessages,"departments/create",model);
        }
        department.setCreatedAt(LocalDate.now());
        return this.departmentRepository.save(department);
    }

    public Department updateDepartment(Long id, Department department,Model model){
        List<ErrorMessage> errorMessages = new ArrayList<>();
        validate(department,errorMessages);

        if(!errorMessages.isEmpty()) {
            model.addAttribute("department", department);
            throw new CommonException(errorMessages,"departments/edit",model);
        }

        Optional<Department> departmentOptional = this.departmentRepository.findById(id);
        if(departmentOptional.isPresent()){
            Department newDepartment = departmentOptional.get();
            newDepartment.setUpdatedAt(LocalDate.now());
            newDepartment.setDepartmentName(department.getDepartmentName());
            newDepartment.setDepartmentDescription(department.getDepartmentDescription());
            return this.departmentRepository.save(newDepartment);
        }
        return null;
    }

    public void deleteDepartment(Long id){
        Optional<Department> departmentOptional = this.departmentRepository.findById(id);
        if(departmentOptional.isPresent()){
            this.departmentRepository.deleteById(id);
        }
    }

    public Department findDepartmentById(Long id){
        Optional<Department> departmentOptional = this.departmentRepository.findById(id);
        return departmentOptional.orElse(null);
    }

    //validation
    private void validate(Department department, List<ErrorMessage> errorMessages){
        validateField(department.getDepartmentName(),"departmentNameError","Department Name can't be empty",errorMessages);
        validateField(department.getDepartmentDescription(),"departmentDescriptionError","Department description can't be empty",errorMessages);
        checkDuplicateName(department, "departmentNameError", "Department name already exists", errorMessages);
    }

    //Not Null Method
    private void validateField(String value,String fieldName,String message,List<ErrorMessage> errorMessages) {
        if(StringUtil.isEmpty(value)) {
            addError(fieldName,message,errorMessages);
        }
    }

    //Duplicate Method
    private void checkDuplicateName(Department department, String fieldName, String message, List<ErrorMessage> errorMessages) {
        Optional<Department> existingDepartment;
        if(department.getDepartmentName() == null){
            return;
        }
        if (department.getId() == null) {
            existingDepartment = departmentRepository.findDepartmentByNameIgnoreCase(department.getDepartmentName());
        } else {
            existingDepartment = departmentRepository.findDepartmentByName(department.getId(), department.getDepartmentName());
        }

        existingDepartment.ifPresent(d -> addError(fieldName, message, errorMessages));
    }

    //adding Error
    private void addError(String field, String msg, List<ErrorMessage> list) {
        list.add(new ErrorMessage(field, msg));
    }

}
