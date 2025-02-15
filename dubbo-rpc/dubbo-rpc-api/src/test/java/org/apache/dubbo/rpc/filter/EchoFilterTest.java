/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.rpc.filter;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.AppResponse;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcInvocation;
import org.apache.dubbo.rpc.support.DemoService;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class EchoFilterTest {

    Filter echoFilter = new EchoFilter();

    @SuppressWarnings("unchecked")
    @Test
    void testEcho() {
        Invocation invocation = createMockRpcInvocation();
        Invoker<DemoService> invoker = createMockInvoker(invocation);
        given(invocation.getMethodName()).willReturn("$echo");

        Result filterResult = echoFilter.invoke(invoker, invocation);
        assertEquals("hello", filterResult.getValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testNonEcho() {
        Invocation invocation = createMockRpcInvocation();
        Invoker<DemoService> invoker = createMockInvoker(invocation);
        given(invocation.getMethodName()).willReturn("echo");

        Result filterResult = echoFilter.invoke(invoker, invocation);
        assertEquals("High", filterResult.getValue());
    }

    Invocation createMockRpcInvocation() {
        Invocation invocation = mock(RpcInvocation.class);
        given(invocation.getParameterTypes()).willReturn(new Class<?>[]{Enum.class});
        given(invocation.getArguments()).willReturn(new Object[]{"hello"});
        given(invocation.getObjectAttachments()).willReturn(null);
        return invocation;
    }
    Invoker<DemoService> createMockInvoker(Invocation invocation){
        Invoker<DemoService> invoker = mock(Invoker.class);
        given(invoker.isAvailable()).willReturn(true);
        given(invoker.getInterface()).willReturn(DemoService.class);

        AppResponse result = new AppResponse();
        result.setValue("High");
        given(invoker.invoke(invocation)).willReturn(result);
        URL url = URL.valueOf("test://test:11/test?group=dubbo&version=1.1");
        given(invoker.getUrl()).willReturn(url);
        return invoker;
    }
}
