package io.sapientpants.structurizr.macros.examples

import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder
import io.github.sapientpants.structurizr.macros.documentation.ArchitectureDocumentation

fun main(args: Array<String>) {
    StructurizrBuilder(
        "structurizr-macros-examples",
        "Documentation - arc42",
        "An empty software architecture document using the arc42 template."
    ).architectureDocumentation(ArchitectureDocumentation.ARC_42)
        .documentationSourcePath("./src/main/markdown/arc42")
        .buildAndRender { model, _ ->

        val user = model.addPerson("User", "A user of my software system.")
        val softwareSystem = model.addSoftwareSystem("Software System", "My software system.")
        user.uses(softwareSystem, "Uses")
    }
}
