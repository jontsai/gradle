/*
 * Copyright 2013 the original author or authors.
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

package org.gradle.nativebinaries.internal;

import org.gradle.api.Action;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.internal.DefaultDomainObjectSet;
import org.gradle.api.internal.notations.api.NotationParser;
import org.gradle.language.base.LanguageSourceSet;
import org.gradle.language.base.internal.AbstractBuildableModelElement;
import org.gradle.language.base.internal.BinaryNamingScheme;
import org.gradle.language.base.internal.DefaultBinaryNamingScheme;
import org.gradle.nativebinaries.*;

import java.io.File;
import java.util.Collection;
import java.util.Set;

public abstract class DefaultNativeBinary extends AbstractBuildableModelElement implements NativeBinaryInternal {
    private final NotationParser<Set<LanguageSourceSet>> sourcesNotationParser = SourceSetNotationParser.parser();
    private final ResolvableNativeDependencySet libs = new ResolvableNativeDependencySet();
    private final DomainObjectSet<LanguageSourceSet> source = new DefaultDomainObjectSet<LanguageSourceSet>(LanguageSourceSet.class);
    private final ToolChainTool linker = new ToolChainTool();
    private final NativeBinaryTasks tasks = new DefaultNativeBinaryTasks();
    private final BinaryNamingScheme namingScheme;
    private final Flavor flavor;
    private final ToolChainInternal toolChain;
    private final Platform targetPlatform;
    private final BuildType buildType;
    private File outputFile;

    protected DefaultNativeBinary(NativeComponent owner, Flavor flavor, ToolChainInternal toolChain, Platform targetPlatform, BuildType buildType, DefaultBinaryNamingScheme namingScheme) {
        this.namingScheme = namingScheme;
        this.flavor = flavor;
        this.toolChain = toolChain;
        this.targetPlatform = targetPlatform;
        this.buildType = buildType;
        owner.getSource().all(new Action<LanguageSourceSet>() {
            public void execute(LanguageSourceSet sourceSet) {
                source.add(sourceSet);
            }
        });
    }

    @Override
    public String toString() {
        return namingScheme.getDescription();
    }

    public String getName() {
        return namingScheme.getLifecycleTaskName();
    }

    public Flavor getFlavor() {
        return flavor;
    }

    public ToolChainInternal getToolChain() {
        return toolChain;
    }

    public Platform getTargetPlatform() {
        return targetPlatform;
    }

    public BuildType getBuildType() {
        return buildType;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public DomainObjectSet<LanguageSourceSet> getSource() {
        return source;
    }

    public void source(Object sources) {
        source.addAll(sourcesNotationParser.parseNotation(sources));
    }

    public ToolChainTool getLinker() {
        return linker;
    }

    public NativeBinaryTasks getTasks() {
        return tasks;
    }

    public BinaryNamingScheme getNamingScheme() {
        return namingScheme;
    }

    public Collection<NativeDependencySet> getLibs() {
        return libs.resolve(this);
    }

    public void lib(Object notation) {
        libs.add(notation);
    }

    public abstract String getOutputFileName();

    protected abstract NativeComponent getComponent();

}
