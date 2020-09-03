package io.sapientpants.structurizr.macros.examples

import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder

fun main() {
    StructurizrBuilder(
        "structurizr-macros-examples",
        "Getting Started",
        "This is a model of my software system."
    ).buildAndRender { model, _ ->
        val user =
            model.addPerson("User", "A user of my software system.")
        val softwareSystem =
            model.addSoftwareSystem("Software System", "My software system.")
        user.uses(softwareSystem, "Uses")
    }
}
