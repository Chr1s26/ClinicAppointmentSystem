package com.clinic.appointment.service;

import com.clinic.appointment.dto.department.DepartmentCreateDTO;
import com.clinic.appointment.dto.department.DepartmentDTO;
import com.clinic.appointment.dto.department.DepartmentUpdateDTO;
import com.clinic.appointment.exception.DuplicateException;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.model.constant.StatusType;
import com.clinic.appointment.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final AuthService authService;

    public DepartmentCreateDTO createDepartment(DepartmentCreateDTO dto) {

        departmentRepository.findByDepartmentNameIgnoreCase(dto.getDepartmentName())
                .ifPresent(d -> {throw new DuplicateException("department",dto,"departmentName","departments/create","Department with this name already exists");});

        Department department = new Department();
        department.setDepartmentName(dto.getDepartmentName());
        department.setDepartmentDescription(dto.getDepartmentDescription());
        department.setStatus(StatusType.ACTIVE);
        department.setCreatedAt(LocalDateTime.now());
        department.setCreatedBy(authService.getCurrentUser());

        departmentRepository.save(department);

        return entityToCreateDTO(department);
    }

    public DepartmentUpdateDTO updateDepartment(Long id, DepartmentUpdateDTO dto) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("department",dto,"name","departments/edit","Department with this id does not exist"));

        departmentRepository.findByDepartmentNameIgnoreCaseAndIdNot(dto.getDepartmentName(), id)
                .ifPresent(d -> {throw new DuplicateException("department",dto,"name","departments/edit","Department with this name already exists");});

        department.setDepartmentName(dto.getDepartmentName());
        department.setDepartmentDescription(dto.getDepartmentDescription());
        department.setUpdatedAt(LocalDateTime.now());
        department.setUpdatedBy(authService.getCurrentUser());

        departmentRepository.save(department);

        return entityToUpdateDTO(department);
    }

    public DepartmentUpdateDTO findById(Long id) {
        Optional<Department> departmentOp = departmentRepository.findById(id);
        if(departmentOp.isEmpty()) {
            throw new ResourceNotFoundException("department",departmentOp.get(),"id","departments","Department with this id does not exist");
        }
        return entityToUpdateDTO(departmentOp.get());
    }

    public List<DepartmentDTO> findAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return entityToDTOList(departments);
    }

    private List<DepartmentDTO> entityToDTOList(List<Department> departments) {
        List<DepartmentDTO> dtos = new ArrayList<>();
        for(Department department : departments) {
            DepartmentDTO dto = new DepartmentDTO();
            dto.setId(department.getId());
            dto.setDepartmentName(department.getDepartmentName());
            dto.setDepartmentDescription(department.getDepartmentDescription());
            dto.setCreatedAt(department.getCreatedAt());
            dto.setCreatedBy(department.getCreatedBy());
            dto.setUpdatedAt(department.getUpdatedAt());
            dto.setUpdatedBy(department.getUpdatedBy());
            dto.setStatus(department.getStatus());
            dtos.add(dto);
        }
        return dtos;
    }

    public DepartmentDTO findDepartmentById(Long id) {
        Optional<Department> departmentOp = departmentRepository.findById(id);
        if(departmentOp.isEmpty()) {
            throw new ResourceNotFoundException("department",departmentOp.get(),"id","departments","Department with this id does not exist");
        }
        return entityToDTO(departmentOp.get());
    }

    private DepartmentDTO entityToDTO(Department department) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(department.getId());
        departmentDTO.setDepartmentName(department.getDepartmentName());
        departmentDTO.setDepartmentDescription(department.getDepartmentDescription());
        departmentDTO.setStatus(department.getStatus());
        departmentDTO.setCreatedAt(LocalDateTime.now());
        departmentDTO.setUpdatedAt(LocalDateTime.now());
        departmentDTO.setCreatedBy(department.getCreatedBy());
        departmentDTO.setUpdatedBy(department.getUpdatedBy());
        return departmentDTO;
    }

    public void deleteDepartment(Long id) {
        Optional<Department> departmentOp = departmentRepository.findById(id);
        if (departmentOp.isPresent()) {
            departmentRepository.delete(departmentOp.get());
        }else{
            throw new ResourceNotFoundException("department",departmentOp.get(),"id","departments","Department with this id does not exist");
        }

    }

    private DepartmentCreateDTO entityToCreateDTO(Department department) {
        DepartmentCreateDTO departmentCreateDTO = new DepartmentCreateDTO();
        departmentCreateDTO.setDepartmentName(department.getDepartmentName());
        departmentCreateDTO.setDepartmentDescription(department.getDepartmentDescription());
        return departmentCreateDTO;
    }

    private DepartmentUpdateDTO entityToUpdateDTO(Department department) {
        DepartmentUpdateDTO departmentUpdateDTO = new DepartmentUpdateDTO();
        departmentUpdateDTO.setDepartmentName(department.getDepartmentName());
        departmentUpdateDTO.setDepartmentDescription(department.getDepartmentDescription());
        return departmentUpdateDTO;
    }

}
