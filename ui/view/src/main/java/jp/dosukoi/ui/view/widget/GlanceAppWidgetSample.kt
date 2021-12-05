package jp.dosukoi.ui.view.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

class GlanceAppWidgetSample @Inject constructor() : GlanceAppWidget() {

    companion object {
        val state = stringPreferencesKey("state")
    }

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    @Composable
    override fun Content() {
        val prefs = currentState<Preferences>()
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(day = Color.White, night = Color.Black)
                .padding(8.dp)
                .cornerRadius(10.dp)
                .clickable(actionRunCallback<GlanceAppWidgetSampleAction>())
        ) {
            Text(text = prefs[state] ?: "")
        }
    }
}


class GlanceAppWidgetSampleAction : ActionCallback {
    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        GlanceAppWidgetSample().apply {
            updateAppWidgetState<Preferences>(context, glanceId) { prefs ->
                prefs.toMutablePreferences().apply {
                    set(GlanceAppWidgetSample.state, UUID.randomUUID().toString())
                }
            }
            update(context, glanceId)
        }
    }
}

@AndroidEntryPoint
class GlanceAppWidgetProviderSample : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = GlanceAppWidgetSample()
}
