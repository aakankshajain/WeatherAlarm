/*
 * Copyright 2015 John Scattergood
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package weatherAlarm.endpoints;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import rx.Observable;
import weatherAlarm.model.WeatherAlarm;
import weatherAlarm.services.IWeatherAlarmService;
import weatherAlarm.util.TestUtils;

public class WeatherAlarmEndpointTest {

    @Test
    public void testHandle() throws Exception {
        IWeatherAlarmService alarmService = TestUtils.getMockAlarmService();
        WeatherAlarm alarm = alarmService.getAlarms().get(0);
        WeatherAlarmEndpoint alarmEndpoint = new WeatherAlarmEndpoint();
        alarmEndpoint.setAlarmService(alarmService);

        Capture<byte[]> written = EasyMock.newCapture();
        HttpServerRequest<ByteBuf> mockServerRequest = createMockHttpServerRequest(HttpMethod.GET, "/weatherAlarm");
        HttpServerResponse<ByteBuf> mockHttpResponse = createMockHttpResponse(written);
        alarmEndpoint.handle(mockServerRequest, mockHttpResponse);
        Assert.assertTrue("Expected not null value", written.getValue().length > 0);
    }

    @SuppressWarnings("unchecked")
    private HttpServerRequest<ByteBuf> createMockHttpServerRequest(HttpMethod method, String uri) {
        HttpServerRequest<ByteBuf> mockServerRequest = EasyMock.createMock(HttpServerRequest.class);
        EasyMock.expect(mockServerRequest.getUri()).andReturn(uri).anyTimes();
        EasyMock.expect(mockServerRequest.getHttpMethod()).andReturn(method).anyTimes();
        EasyMock.replay(mockServerRequest);
        return mockServerRequest;
    }

    @SuppressWarnings("unchecked")
    private HttpServerResponse<ByteBuf> createMockHttpResponse(Capture<byte[]> capture) {
        HttpServerResponse<ByteBuf> mockResponse = EasyMock.createMock(HttpServerResponse.class);
        mockResponse.writeBytes(EasyMock.capture(capture));
        EasyMock.expect(mockResponse.close()).andReturn(Observable.empty()).anyTimes();
        EasyMock.replay(mockResponse);
        return mockResponse;
    }
}