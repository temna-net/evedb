package lv.odylab.evedb.servlet;

import lv.odylab.evedb.client.BadRequestException;
import lv.odylab.evedb.client.EveDbWsClient;
import lv.odylab.evedb.client.HttpRequestSenderWithRetryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpRequestSenderWithRetryImplTest {
    @Test
    public void none() {
    }

    @Mock
    private EveDbWsClient.HttpRequestSender httpRequestSender;
    private HttpRequestSenderWithRetryImpl httpRequestSenderWithRetry;

    @Before
    public void setUp() {
        httpRequestSenderWithRetry = new HttpRequestSenderWithRetryImpl(3, httpRequestSender);
    }

    @Test
    public void testNoException() {
        when(httpRequestSender.doGet("urlString", "acceptHeader")).thenReturn("result");
        assertEquals("result", httpRequestSenderWithRetry.doGet("urlString", "acceptHeader"));
    }

    @Test
    public void testRetry_AllFailed() {
        when(httpRequestSender.doGet("urlString", "acceptHeader")).thenThrow(new RuntimeException(new IOException()));
        try {
            httpRequestSenderWithRetry.doGet("urlString", "acceptHeader");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof IOException);
        }
        verify(httpRequestSender, times(3)).doGet("urlString", "acceptHeader");
    }

    @Test
    public void testRetry_OneFailed() {
        when(httpRequestSender.doGet("urlString", "acceptHeader")).thenThrow(new RuntimeException(new IOException()))
                .thenReturn("result");
        String result = httpRequestSenderWithRetry.doGet("urlString", "acceptHeader");
        assertEquals("result", result);
        verify(httpRequestSender, times(2)).doGet("urlString", "acceptHeader");
    }

    @Test
    public void testRetry_TwoFailed() {
        when(httpRequestSender.doGet("urlString", "acceptHeader")).thenThrow(new RuntimeException(new IOException()))
                .thenThrow(new RuntimeException(new IOException()))
                .thenReturn("result");
        String result = httpRequestSenderWithRetry.doGet("urlString", "acceptHeader");
        assertEquals("result", result);
        verify(httpRequestSender, times(3)).doGet("urlString", "acceptHeader");
    }

    @Test
    public void testRetry_ThreeFailed() {
        when(httpRequestSender.doGet("urlString", "acceptHeader")).thenThrow(new RuntimeException(new IOException()))
                .thenThrow(new RuntimeException(new IOException()))
                .thenThrow(new RuntimeException(new IOException()))
                .thenReturn("result");
        try {
            httpRequestSenderWithRetry.doGet("urlString", "acceptHeader");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof IOException);
        }
        verify(httpRequestSender, times(3)).doGet("urlString", "acceptHeader");
    }

    @Test
    public void testAnotherException() {
        when(httpRequestSender.doGet("urlString", "acceptHeader")).thenThrow(new RuntimeException(new BadRequestException()))
                .thenReturn("result");
        try {
            httpRequestSenderWithRetry.doGet("urlString", "acceptHeader");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof BadRequestException);
        }
        verify(httpRequestSender, times(1)).doGet("urlString", "acceptHeader");
    }
}
