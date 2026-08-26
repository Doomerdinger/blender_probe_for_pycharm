package com.github.unclepomedev.blenderprobeforpycharm.run.app

import com.github.unclepomedev.blenderprobeforpycharm.BaseBlenderTest
import com.github.unclepomedev.blenderprobeforpycharm.settings.BlenderSettings
import com.intellij.execution.configuration.EnvironmentVariablesData

class BlenderRunningStateTest : BaseBlenderTest() {

    fun testUserEnvironmentVariablesAreIncluded() {
        val env = BlenderRunningState.buildEnvironment(
            envData = EnvironmentVariablesData.create(mapOf("MY_ADDON_SETTING" to "42"), true),
            sourceRoot = "/src",
            addonName = "my_addon",
            debugPort = null,
            pydevdPath = null,
        )

        assertEquals("42", env["MY_ADDON_SETTING"])
        assertEquals("/src", env["BLENDER_PROBE_PROJECT_ROOT"])
        assertEquals("my_addon", env["BLENDER_PROBE_ADDON_NAME"])
        assertEquals("1", env["PYTHONUNBUFFERED"])
    }

    fun testPluginEnvironmentVariablesTakePrecedenceOverUserVariables() {
        val env = BlenderRunningState.buildEnvironment(
            envData = EnvironmentVariablesData.create(
                mapOf("BLENDER_PROBE_ADDON_NAME" to "hijacked"),
                true
            ),
            sourceRoot = "/src",
            addonName = "real_addon",
            debugPort = null,
            pydevdPath = null,
        )

        assertEquals(
            "Plugin-managed variables must not be overridden by user variables",
            "real_addon",
            env["BLENDER_PROBE_ADDON_NAME"]
        )
    }

    fun testDebugEnvironmentVariablesIncludedWhenDebugging() {
        val env = BlenderRunningState.buildEnvironment(
            envData = EnvironmentVariablesData.DEFAULT,
            sourceRoot = "/src",
            addonName = "my_addon",
            debugPort = 5678,
            pydevdPath = "/path/to/pydevd",
        )

        assertEquals("5678", env["BLENDER_PROBE_DEBUG_PORT"])
        assertEquals("/path/to/pydevd", env["BLENDER_PROBE_PYDEVD_PATH"])
    }

    fun testFactoryStartupFlagIncludedWhenEnabled() {
        assertFactoryStartupFlag(enabled = true, expected = true)
    }

    fun testFactoryStartupFlagOmittedWhenDisabled() {
        assertFactoryStartupFlag(enabled = false, expected = false)
    }

    private fun assertFactoryStartupFlag(enabled: Boolean, expected: Boolean) {
        val settings = BlenderSettings.getInstance(project)
        settings.state.useFactoryStartup = enabled

        val params = BlenderRunningState.buildParameters(
            useFactoryStartup = settings.state.useFactoryStartup,
            scriptPath = "/tmp/probe_server.py"
        )

        assertEquals(
            "--factory-startup presence should match the setting (enabled=$enabled)",
            expected,
            "--factory-startup" in params
        )
    }
}
