package com.catpeds.rest.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParamUtilsServiceTest {

    private ParamUtilsService paramUtilsService;


    @Before
    public void setup() {
        paramUtilsService = new ParamUtilsService();
    }


    /**
     * Test that {@link ParamUtilsService#escapeParam(String)} returns null for empty parameter value.
     */
    @Test
    public void testEmptyParameterEscape() {
        // Given
        String param = "";

        // When
        String result = paramUtilsService.escapeParam(param);

        // Then
        assertNull("Expecting null response", result);
    }


    /**
     * Test that {@link ParamUtilsService#escapeParam(String)} escapes parameter.
     */
    @Test
    public void testParameterEscape() {
        // Given
        String param = "A\nB\rC\tD";

        // When
        String result = paramUtilsService.escapeParam(param);

        // Then
        assertEquals("Expecting escaped response", "A_B_C_D", result);
    }
}
