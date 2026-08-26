package com.github.unclepomedev.blenderprobeforpycharm.run.app

import com.intellij.execution.Executor
import com.intellij.execution.configuration.EnvironmentVariablesData
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project

/**
 * Run configuration for running Blender with the Probe Server.
 * The Blender executable is taken from the project-level Blender settings; this
 * configuration additionally lets the user define environment variables for the
 * launched Blender process.
 */
class BlenderRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<BlenderRunConfigurationOptions>(project, factory, name) {

    override fun getOptions(): BlenderRunConfigurationOptions {
        return super.getOptions() as BlenderRunConfigurationOptions
    }

    /**
     * User-defined environment variables (and parent-environment inheritance)
     * for the Blender process.
     */
    var envData: EnvironmentVariablesData
        get() = EnvironmentVariablesData.create(options.envVars, options.passParentEnvs)
        set(value) {
            options.envVars = LinkedHashMap(value.envs)
            options.passParentEnvs = value.isPassParentEnvs
        }

    /**
     * Returns the editor for this configuration.
     *
     * @return The configuration editor.
     */
    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return BlenderRunConfigurationEditor()
    }

    /**
     * Prepares the run profile state for execution.
     *
     * @param executor The executor (Run or Debug).
     * @param environment The execution environment.
     * @return The run profile state.
     */
    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return BlenderRunningState(environment).also {
            it.envData = envData
        }
    }
}
