package com.nouni.fluentPdfBox;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

/**
 * Unit test for the App class.
 */
@RunWith(MockitoJUnitRunner.class)
public class AppTest {

    /**
     * Insert here your mocks
     */
    @Mock
    private List<Object> mockList;

    /**
     * Sample test with given-when-then structure using MockitoBDD and AssertJ
     */
    @Test
    public void listDoesWorkWithMocks() {
        given(mockList.size()).willReturn(10);

        final int sizeOfTheList = mockList.size();

        assertThat(sizeOfTheList).isEqualTo(10);
    }
}
