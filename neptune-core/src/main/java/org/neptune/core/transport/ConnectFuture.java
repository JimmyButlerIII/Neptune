/*
 * Copyright (c) 2015 The Jupiter Project
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
package org.neptune.core.transport;

import io.netty.channel.ChannelFutureListener;

/**
 * org.neptune.core.transportLayer - ConnectFuture
 *
 * @author tony-is-coding
 * @date 2021/12/25 15:41
 */
public interface ConnectFuture {
    void onConnectCompleted(ChannelFutureListener listener);
}
