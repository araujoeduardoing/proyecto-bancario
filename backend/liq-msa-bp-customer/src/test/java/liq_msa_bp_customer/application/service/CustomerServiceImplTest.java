package liq_msa_bp_customer.application.service;

import liq_msa_bp_customer.application.output.port.CustomerOutputService;
import liq_msa_bp_customer.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerOutputService customerOutputService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer testCustomer;

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
    }

    @Test
    void save_ShouldReturnSavedCustomer_WhenValidCustomerProvided() {
        when(customerOutputService.save(testCustomer)).thenReturn(testCustomer);

        Customer savedCustomer = customerService.save(testCustomer);

        assertNotNull(savedCustomer);
        assertEquals(testCustomer.getClientId(), savedCustomer.getClientId());
        assertEquals(testCustomer.getName(), savedCustomer.getName());
        assertEquals(testCustomer.getIdentification(), savedCustomer.getIdentification());
        
        verify(customerOutputService, times(1)).save(testCustomer);
    }

    @Test
    void findById_ShouldReturnCustomer_WhenCustomerExists() {
        Long customerId = 1L;
        when(customerOutputService.findById(customerId)).thenReturn(Optional.of(testCustomer));

        Optional<Customer> foundCustomer = customerService.findById(customerId);

        assertTrue(foundCustomer.isPresent());
        assertEquals(testCustomer.getClientId(), foundCustomer.get().getClientId());
        assertEquals(testCustomer.getName(), foundCustomer.get().getName());
        
        verify(customerOutputService, times(1)).findById(customerId);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenCustomerDoesNotExist() {
        Long customerId = 999L;
        when(customerOutputService.findById(customerId)).thenReturn(Optional.empty());

        Optional<Customer> foundCustomer = customerService.findById(customerId);

        assertTrue(foundCustomer.isEmpty());
        
        verify(customerOutputService, times(1)).findById(customerId);
    }

    @Test
    void deleteById_ShouldDeleteCustomer_WhenCustomerExists() {
        Long customerId = 1L;
        when(customerOutputService.findById(customerId)).thenReturn(Optional.of(testCustomer));
        doNothing().when(customerOutputService).deleteById(customerId);

        assertDoesNotThrow(() -> customerService.deleteById(customerId));
        
        verify(customerOutputService, times(1)).findById(customerId);
        verify(customerOutputService, times(1)).deleteById(customerId);
    }

    @Test
    void deleteById_ShouldThrowException_WhenCustomerDoesNotExist() {
        Long customerId = 999L;
        when(customerOutputService.findById(customerId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> customerService.deleteById(customerId)
        );
        
        assertEquals("Cliente no encontrado con ID: " + customerId, exception.getMessage());
        
        verify(customerOutputService, times(1)).findById(customerId);
        verify(customerOutputService, never()).deleteById(customerId);
    }

    @Test
    void findAll_ShouldReturnListOfCustomers_WhenCustomersExist() {
        Customer customer2 = new Customer();
        customer2.setClientId(2L);
        customer2.setPersonId(2L);
        customer2.setName("María García");
        customer2.setGender("F");
        customer2.setAge(25);
        customer2.setIdentification("0987654321");
        customer2.setAddress("Avenida Central 456");
        customer2.setPhone("0123456789");
        customer2.setPassword("password456");
        customer2.setStatus(true);

        List<Customer> expectedCustomers = Arrays.asList(testCustomer, customer2);
        when(customerOutputService.findAll()).thenReturn(expectedCustomers);

        List<Customer> actualCustomers = customerService.findAll();

        assertNotNull(actualCustomers);
        assertEquals(2, actualCustomers.size());
        assertEquals(expectedCustomers.get(0).getClientId(), actualCustomers.get(0).getClientId());
        assertEquals(expectedCustomers.get(1).getClientId(), actualCustomers.get(1).getClientId());
        
        verify(customerOutputService, times(1)).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoCustomersExist() {
        List<Customer> emptyList = Arrays.asList();
        when(customerOutputService.findAll()).thenReturn(emptyList);

        List<Customer> actualCustomers = customerService.findAll();

        assertNotNull(actualCustomers);
        assertTrue(actualCustomers.isEmpty());
        
        verify(customerOutputService, times(1)).findAll();
    }
}