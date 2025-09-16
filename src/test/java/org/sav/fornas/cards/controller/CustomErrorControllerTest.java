package org.sav.fornas.cards.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomErrorControllerTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CustomErrorController controller;

    @Test
    void error_WithAllAttributes_ReturnsCompleteMap() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(404);
        when(request.getAttribute(RequestDispatcher.ERROR_MESSAGE)).thenReturn("Not Found");
        when(request.getAttribute(RequestDispatcher.ERROR_EXCEPTION)).thenReturn(new RuntimeException("Test"));

        Map<String, String> result = controller.error(request);

        assertEquals("404", result.get("status"));
        assertEquals("Not Found", result.get("message"));
        assertTrue(result.get("trace").contains("RuntimeException"));
    }

    @Test
    void error_WithNullAttributes_ReturnsUnavailable() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(null);
        when(request.getAttribute(RequestDispatcher.ERROR_MESSAGE)).thenReturn(null);
        when(request.getAttribute(RequestDispatcher.ERROR_EXCEPTION)).thenReturn(null);

        Map<String, String> result = controller.error(request);

        assertEquals("Unavailable", result.get("status"));
        assertEquals("Unavailable", result.get("message"));
        assertEquals("Unavailable", result.get("trace"));
    }
}