package com.clinic.appointment.service;

import com.clinic.appointment.model.Department;
import com.clinic.appointment.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments(){
        return this.departmentRepository.findAll();
    }

    public Department createDepartment(Department department){
        department.setCreatedAt(LocalDate.now());
        return this.departmentRepository.save(department);
    }

    public Department updateDepartment(Long id, Department department){
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
}
