package com.clinic.appointment.test.service;

import com.clinic.appointment.dto.DepartmentDTO;
import com.clinic.appointment.dto.DepartmentResponse;
import com.clinic.appointment.model.Department;
import com.clinic.appointment.repository.DepartmentRepository;
import com.clinic.appointment.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    public void testCreateDepartment_Success() {
        Department department = new Department();
        department.setDepartmentName("Cardiology");
        department.setDepartmentDescription("Heart and vascular treatment");

        when(departmentRepository.findDepartmentByNameIgnoreCase(department.getDepartmentName())).thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Department result = departmentService.createDepartment(department, null);

        assertNotNull(result);
        assertEquals("Cardiology", result.getDepartmentName());
    }

    @Test
    public void testUpdateDepartment_Success() {
        Department existing = new Department();
        existing.setId(1L);
        existing.setDepartmentName("Old Name");
        existing.setDepartmentDescription("Old Desc");

        Department department = new Department();
        department.setId(1L);
        department.setDepartmentName("New Name");
        department.setDepartmentDescription("New Desc");

        when(departmentRepository.findDepartmentByName(1L, "New Name")).thenReturn(Optional.empty());
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(departmentRepository.save(existing)).thenReturn(department);

        Department updated = departmentService.updateDepartment(1L, department, null);

        assertNotNull(updated);
        assertEquals("New Name", updated.getDepartmentName());
    }

    @Test
    public void testGetAllDepartmentsPagination() {
        Department department1 = new Department();
        department1.setDepartmentName("Cardiology");
        department1.setDepartmentDescription("Heart");

        Department department2 = new Department();
        department2.setDepartmentName("Neurology");
        department2.setDepartmentDescription("Brain");

        List<Department> departments = List.of(department1, department2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Department> page = new PageImpl<>(departments, pageable, departments.size());

        when(departmentRepository.findAll(pageable)).thenReturn(page);

        DepartmentResponse response = departmentService.getAllDepartments(0, 10, "id", "asc");

        assertNotNull(response);
        assertEquals(2, response.getDepartments().size());
        assertEquals(2, response.getTotalElements());
    }

    @Test
    public void testFindDepartmentById_Success() {
        Department department = new Department();
        department.setId(1L);
        department.setDepartmentName("Radiology");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Department result = departmentService.findDepartmentById(1L);

        assertNotNull(result);
        assertEquals("Radiology", result.getDepartmentName());
    }

    @Test
    public void testDeleteDepartmentById() {
        Department department = new Department();
        department.setId(1L);
        department.setDepartmentName("Lab");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        departmentService.deleteDepartment(1L);

        verify(departmentRepository).deleteById(1L);
    }
}
