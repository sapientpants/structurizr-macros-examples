package io.sapientpants.structurizr.macros.examples

import com.structurizr.view.FilterMode
import com.structurizr.view.Shape
import com.structurizr.view.SystemLandscapeView
import com.structurizr.view.ViewSet
import io.github.sapientpants.structurizr.macros.Tags
import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder
import io.github.sapientpants.structurizr.macros.styles.Style

private const val CURRENT_STATE = "Current State"
private const val FUTURE_STATE = "Future State"

class FilteredViewsStyle : Style() {
    override fun applyToViews(views: ViewSet) {
        val styles = views.configuration.styles
        styles.addElementStyle(Tags.ELEMENT)
            .color("#ffffff")
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM)
            .background("#91a437")
            .shape(Shape.RoundedBox)
        styles.addElementStyle(Tags.PERSON)
            .background("#6a7b15")
            .shape(Shape.Person)
    }
}

fun main(args: Array<String>) {
    StructurizrBuilder(
        "structurizr-macros-examples",
        "Filtered Views",
        "An example of using filtered views."
    ).style(FilteredViewsStyle())
        .buildAndRender { model, views ->
            val user = model.addPerson("User", "A description of the user.")
            val softwareSystemA = model.addSoftwareSystem(
                "Software System A",
                "A description of software system A."
            )
            val softwareSystemB = model.addSoftwareSystem(
                "Software System B",
                "A description of software system B."
            )
            softwareSystemB.addTags(FUTURE_STATE)

            user.uses(softwareSystemA, "Uses for tasks 1 and 2")
                ?.addTags(CURRENT_STATE)
            user.uses(softwareSystemA, "Uses for task 1")!!.addTags(FUTURE_STATE)
            user.uses(softwareSystemB, "Uses for task 2")!!.addTags(FUTURE_STATE)

            val systemLandscapeView: SystemLandscapeView =
                views.createSystemLandscapeView(
                    "SystemLandscape",
                    "An example System Landscape diagram."
                )
            systemLandscapeView.addAllElements()

            views.createFilteredView(
                systemLandscapeView,
                "CurrentState",
                "The current system landscape.",
                FilterMode.Exclude,
                FUTURE_STATE
            )
            views.createFilteredView(
                systemLandscapeView,
                "FutureState",
                "The future state system landscape after Software System B is live.",
                FilterMode.Exclude,
                CURRENT_STATE
            )
    }
}
