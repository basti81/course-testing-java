package com.javaguides.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
    @BeforeEach
    public void setUp() throws Exception{
        System.out.println("entre al beforeach");
        // Given - Peticion o configuracion
        employee = Employee.builder()
                .name("Ramesh")
                .email("ramesh@gmail.com")
                .password("1234")
                .build();
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));
        given(employeeService.saveEmployee(any(Employee.class))).willAnswer((invocation)-> invocation.getArgument(0));
        System.out.println("Clase creada -> " + employee.toString());
    }

    @Test
    public void createObjectEmployee_returnSavedEmployee() throws Exception {


        // When - Comportamiento que vamos a probar
        ResultActions resultActions = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // Then - Verificar el resultado de la peticion
        resultActions.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",is(employee.getName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())))
                .andExpect(jsonPath("$.password", is(employee.getPassword())));
    }

    @Test
    public void getAllEmployeeByList(){
        /* ResultActions resultActions = mockMvc.perform(post("/api/employees"))
                .ContentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
           */
        System.out.println("Entre al test getallemployeebylist");
    }
}