package com.testeyuri.roleService.unit.helper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.testeyuri.roleService.helper.GetObjectFromURL;

public class GetObjectFromURLTest {

    @Test
    public void testGetObjectFromURL_IsNotNull() throws Exception {
        GetObjectFromURL getObjectFromURL = new GetObjectFromURL();
        assertNotNull(getObjectFromURL);
    }
}
