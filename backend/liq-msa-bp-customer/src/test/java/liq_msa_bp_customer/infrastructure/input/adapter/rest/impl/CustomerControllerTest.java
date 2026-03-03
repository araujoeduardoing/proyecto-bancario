package liq_msa_bp_customer.infrastructure.input.adapter.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import liq_msa_bp_customer.application.input.port.CustomerService;
import liq_msa_bp_customer.domain.Customer;
import liq_msa_bp_customer.infrastructure.input.adapter.rest.bean.CustomerRequest;
import liq_msa_bp_customer.infrastructure.repository.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomerMapper customerMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer testCustomer;
    private CustomerRequest testRequest;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setClientId(1L);
        testCustomer.setPersonId(1L);
        testCustomer.setName("Juan Pérez");
        testCustomer.setGender("M");
        testCustomer.setAge(30);
        testCustomer.setIdentification("1234567890");
        testCustomer.setAddress("Calle Principal 123");
        testCustomer.setPhone("0987654321");
        testCustomer.setPassword("password123");
        testCustomer.setStatus(true);

        testRequest = new CustomerRequest();
        testRequest.setName("Juan Pérez");
        testRequest.setGender("M");
        testRequest.setAge(30);
        testRequest.setIdentification("1234567890");
        testRequest.setAddress("Calle Principal 123");
        testRequest.setPhone("0987654321");
        testRequest.setPassword("password123");
        testRequest.setStatus(true);
    }

    @Test
    void createCustomer_ShouldReturnCreated_WhenValidRequestProvided() throws Exception {
        // Given
        when(customerMapper.customerRequestToCustomerDomain(any(CustomerRequest.class))).thenReturn(testCustomer);
        when(customerService.save(any(Customer.class))).thenReturn(testCustomer);

        // When & Then
        mockMvc.perform(post("/business/retail/v1/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.clientId").value(1L))
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.identification").value("1234567890"))
                .andExpect(jsonPath("$.status").value(true));

        verify(customerMapper, times(1)).customerRequestToCustomerDomain(any(CustomerRequest.class));
        verify(customerService, times(1)).save(any(Customer.class));
    }

    @Test
    void createCustomer_ShouldReturnBadRequest_WhenInvalidRequestProvided() throws Exception {
        // Given - Request with missing required fields
        CustomerRequest invalidRequest = new CustomerRequest();
        invalidRequest.setName(""); // Invalid: empty name
        invalidRequest.setPassword("123"); // Invalid: password too short

        // When & Then
        mockMvc.perform(post("/business/retail/v1/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(customerMapper, never()).customerRequestToCustomerDomain(any(CustomerRequest.class));
        verify(customerService, never()).save(any(Customer.class));
    }

    @Test
    void getCustomerById_ShouldReturnCustomer_WhenCustomerExists() throws Exception {
        // Given
        Long customerId = 1L;
        when(customerService.findById(customerId)).thenReturn(Optional.of(testCustomer));

        // When & Then
        mockMvc.perform(get("/business/retail/v1/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.clientId").value(1L))
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.identification").value("1234567890"));

        verify(customerService, times(1)).findById(customerId);
    }

    @Test
    void getCustomerById_ShouldReturnNotFound_WhenCustomerDoesNotExist() throws Exception {
        // Given
        Long customerId = 999L;
        when(customerService.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/business/retail/v1/customers/{id}", customerId))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).findById(customerId);
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomer_WhenValidRequestProvided() throws Exception {
        // Given
        Long customerId = 1L;
        Customer updatedCustomer = new Customer();
        updatedCustomer.setClientId(customerId);
        updatedCustomer.setName("Juan Pérez Actualizado");
        updatedCustomer.setIdentification("1234567890");

        when(customerMapper.customerRequestToCustomerDomain(any(CustomerRequest.class))).thenReturn(testCustomer);
        when(customerService.save(any(Customer.class))).thenReturn(updatedCustomer);

        // When & Then
        mockMvc.perform(put("/business/retail/v1/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.clientId").value(customerId))
                .andExpect(jsonPath("$.name").value("Juan Pérez Actualizado"));

        verify(customerMapper, times(1)).customerRequestToCustomerDomain(any(CustomerRequest.class));
        verify(customerService, times(1)).save(any(Customer.class));
    }

    @Test
    void updateCustomer_ShouldReturnBadRequest_WhenInvalidRequestProvided() throws Exception {
        // Given
        Long customerId = 1L;
        CustomerRequest invalidRequest = new CustomerRequest();
        invalidRequest.setName(""); // Invalid: empty name
        invalidRequest.setPassword("123"); // Invalid: password too short

        // When & Then
        mockMvc.perform(put("/business/retail/v1/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(customerMapper, never()).customerRequestToCustomerDomain(any(CustomerRequest.class));
        verify(customerService, never()).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_ShouldReturnNoContent_WhenCustomerExists() throws Exception {
        // Given
        Long customerId = 1L;
        doNothing().when(customerService).deleteById(customerId);

        // When & Then
        mockMvc.perform(delete("/business/retail/v1/customers/{id}", customerId))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteById(customerId);
    }

    @Test
    void deleteCustomer_ShouldHandleException_WhenCustomerDoesNotExist() throws Exception {
        // Given
        Long customerId = 999L;
        doThrow(new IllegalArgumentException("Cliente no encontrado con ID: " + customerId))
                .when(customerService).deleteById(customerId);

        // When & Then
        mockMvc.perform(delete("/business/retail/v1/customers/{id}", customerId))
                .andExpect(status().isInternalServerError());

        verify(customerService, times(1)).deleteById(customerId);
    }

    @Test
    void getAllCustomers_ShouldReturnCustomerList_WhenCustomersExist() throws Exception {
        // Given
        Customer customer2 = new Customer();
        customer2.setClientId(2L);
        customer2.setPersonId(2L);
        customer2.setName("María García");
        customer2.setIdentification("0987654321");
        customer2.setStatus(true);

        List<Customer> customerList = Arrays.asList(testCustomer, customer2);
        when(customerService.findAll()).thenReturn(customerList);

        // When & Then
        mockMvc.perform(get("/business/retail/v1/customers/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].clientId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Juan Pérez"))
                .andExpect(jsonPath("$[1].clientId").value(2L))
                .andExpect(jsonPath("$[1].name").value("María García"));

        verify(customerService, times(1)).findAll();
    }

    @Test
    void getAllCustomers_ShouldReturnEmptyList_WhenNoCustomersExist() throws Exception {
        // Given
        when(customerService.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/business/retail/v1/customers/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(customerService, times(1)).findAll();
    }
}