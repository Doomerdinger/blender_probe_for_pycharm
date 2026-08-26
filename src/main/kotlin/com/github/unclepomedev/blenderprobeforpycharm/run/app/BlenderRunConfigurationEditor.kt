package com.github.unclepomedev.blenderprobeforpycharm.run.app

import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.openapi.options.SettingsEditor
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JLabel

/**
 * Editor for the Blender Run Configuration.
 * Lets the user configure environment variables for the Blender process.
 */
class BlenderRunConfigurationEditor : SettingsEditor<BlenderRunConfiguration>() {

    private val environmentVariables = EnvironmentVariablesComponent()

    /**
     * Creates the editor component.
     *
     * @return The editor component.
     */
    override fun createEditor(): JComponent {
        return FormBuilder.createFormBuilder()
            .addComponent(JLabel("Blender path is configured in Settings > Tools > Blender Probe."))
            .addComponent(environmentVariables)
            .panel
    }

    override fun resetEditorFrom(s: BlenderRunConfiguration) {
        environmentVariables.envData = s.envData
    }

    override fun applyEditorTo(s: BlenderRunConfiguration) {
        s.envData = environmentVariables.envData
    }
}
