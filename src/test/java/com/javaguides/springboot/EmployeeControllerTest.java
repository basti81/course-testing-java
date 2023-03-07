package com.javaguides.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaguides.springboot.employeeTestToController.Employee;
import com.javaguides.springboot.employeeTestToController.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;
    private Employee employee;

    @Test
    public void createObjectEmployee_returnSavedEmployee() throws Exception {

        // Given - precondition or setup
        employee = Employee.builder()
                .name("Ramesh")
                .email("ramesh@gmail.com")
                .password("1234")
                .build();

        //Method return - saveEmployee
        given(employeeService.saveEmployee(any(Employee.class))).willAnswer((invocation)-> invocation.getArgument(0));

        // When - action or method
        ResultActions resultActions = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // Then - verify the output
        resultActions.andDo(print()).
                andExpect(status().isCreated())  // given status of controller request https
                .andExpect(jsonPath("$.name",is(employee.getName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())))
                .andExpect(jsonPath("$.password", is(employee.getPassword())));
    }

    @Test
    public void givenListOfEmployee_whenAllEmployees_thenReturnEmployeeList() throws Exception {
        //Given - precondition or setup
        List<Employee> listOfEmployee = new ArrayList<>();
        listOfEmployee.add(Employee.builder().name("Alex").email("alex@mail.com").password("1234").build());
        listOfEmployee.add(Employee.builder().name("Joe ").email("joe@mail.com").password("123456").build());
        given(employeeService.getAllEmployees()).willReturn(listOfEmployee); //Method return - getlistOfEmployee
        //When - action or method
        ResultActions resultActions = mockMvc.perform(get("/api/employees/listOfEmployee"));
        //Then - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(listOfEmployee.size())));

    }

    @Test
    public void givenEmployeeId_whengetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //Given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder().name("Alex").email("alex@mail.com").password("1234").build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //When - action or method
        ResultActions resultActions = mockMvc.perform(get("/api/employees/{id}",employeeId));

        //Then - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name",is(employee.getName())))
                .andExpect(jsonPath("$.email",is(employee.getEmail())))
                .andExpect(jsonPath("$.password",is(employee.getPassword())));
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnEmployeeObject() throws Exception {
        //Given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder().name("Alex").email("alex@mail.com").password("1234").build();
        Employee updatedEmployee = Employee.builder().name("Remy").email("remy@mail.com").password("1234").build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation)-> invocation.getArgument(0));

        //When - action or method
        ResultActions resultActions = mockMvc.perform(put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //Then - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name",is(updatedEmployee.getName())))
                .andExpect(jsonPath("$.email",is(updatedEmployee.getEmail())))
                .andExpect(jsonPath("$.password",is(updatedEmployee.getPassword())));

    }
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnError404() throws Exception {
        //Given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder().name("Alex").email("alex@mail.com").password("1234").build();
        Employee updatedEmployee = Employee.builder().name("Alex").email("alex@mail.com").password("1234").build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

        //When - action or method
        ResultActions resultActions = mockMvc.perform(delete("/api/employees/delete/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //Then - verify the output
        resultActions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnError202() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());

    }
}