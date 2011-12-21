/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.integtests.tooling.m7

import org.gradle.tooling.GradleConnectionException
import org.gradle.tooling.model.BuildEnvironment
import org.gradle.integtests.tooling.fixture.*

@MinToolingApiVersion('1.0-milestone-7')
@MinTargetGradleVersion('1.0-milestone-3')
@MaxTargetGradleVersion('1.0-milestone-6')
@IncludeAllPermutations
class BuildEnvironmentHandledSafelyIntegrationTest extends ToolingApiSpecification {

    def "fails gracefully when building build environment with an older provider version"() {
        //this test exposes a daemon issue that appears in M5 and M6
        //basically when we ask to build a model for a an unknown type for given provider
        //the daemon breaks and does not return anything to the client making the client waiting forever.
        when:
        def e = toolingApi.maybeFailWithConnection { it.getModel(BuildEnvironment.class) }

        then:
        e instanceof GradleConnectionException
    }
}
