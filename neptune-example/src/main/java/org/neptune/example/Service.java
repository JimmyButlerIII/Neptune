/*
 * Copyright (c) 2015 The Neptune Project
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
package org.neptune.example;

import org.neptune.core.core.annotation.RpcService;

/**
 * org.neptune.example - ServiceRegistry
 *
 * @author tony-is-coding
 * @date 2021/12/20 15:37
 */
@RpcService(name = "service")
public interface Service {
    String call(String input);
}