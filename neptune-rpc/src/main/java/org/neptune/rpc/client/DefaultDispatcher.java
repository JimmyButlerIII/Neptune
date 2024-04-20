/*
 * Copyright (c) 2022 The Neptune Project
 *
 * Licensed under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neptune.rpc.client;

import org.neptune.common.util.ReaderHelper;
import org.neptune.registry.ServiceMeta;
import org.neptune.transport.ServiceConnectionHolder;
import org.neptune.rpc.*;
import org.neptune.rpc.client.lb.LoadBalancer;
import org.neptune.rpc.client.lb.LoadBalancerFactory;
import org.neptune.rpc.factories.SerializerFactory;
import org.neptune.rpc.seialize.Serializer;
import org.neptune.transport.ConnectionGroup;
import org.neptune.transport.RequestPayload;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

/**
 * org.neptune.rpc.consumer - DefaultDispatcher
 *
 * @author tony-is-coding
 * @date 2021/12/26 15:09
 */
public class DefaultDispatcher implements Dispatcher {
    /*
        考虑共用性

        README: 在这一层完成
            1. 负载均衡
            2. 拦截器拓展
            3. 业务数据序列化 && send 数据
            4. 超时控制?
     */

    private LoadBalancer loadBalancer;
    private Serializer serializer;
    private Client client;

    public DefaultDispatcher(LoadBalancer.LoadBalancerType loadBalancerType, Serializer.SerializerType serializerType, Client client) {
        this.loadBalancer = LoadBalancerFactory.create(loadBalancerType);
        this.serializer = SerializerFactory.getSerializer(serializerType);
        this.client = client;
    }

    @Override
    public <T> InvokeFuture<T> dispatch(Request request, Class<T> returnType) {
        return send(request, returnType);
    }

    // 匹配一个目标连接来
    private Channel select(ServiceMeta serviceMeta) {
        ServiceConnectionHolder groups = new ServiceConnectionHolder(); // TODO: 组化管理
        ConnectionGroup group = loadBalancer.select(groups);
        return group.next().channel();
    }


    private <T> InvokeFuture<T> send(Request request, Class<T> returnType) {
        final long invokeId = request.getInvokeId();

        // 对象序列化
        RequestPayload payload = new RequestPayload(invokeId);
        payload.setBytes(serializer.writeObject(request.getBody()));
        Channel ch = select(null);

        DefaultInvokeFuture<T> future = new DefaultInvokeFuture<>(ch, invokeId, returnType);
        ch.writeAndFlush(payload).addListener(
                // TODO:加入发送超时监控, writeAndFlush
                (ChannelFutureListener) cf -> {
                    if (cf.isSuccess()) { // success
                        future.sentSuccess();
                    } else { // fail
                        future.sentFailure();
                    }
                });
        return future;
    }

}