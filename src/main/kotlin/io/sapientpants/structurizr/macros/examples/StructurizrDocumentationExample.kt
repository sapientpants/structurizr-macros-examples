package io.sapientpants.structurizr.macros.examples

import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder
import io.github.sapientpants.structurizr.macros.documentation.ArchitectureDocumentation

fun main() {
    StructurizrBuilder(
        "structurizr-macros-examples",
        "Documentation - Structurizr",
        "An empty software architecture document using the Structurizr template."
    ).architectureDocumentation(ArchitectureDocumentation.STRUCTURIZR)
        .documentationSourcePath("./src/main/markdown/structurizr")
        .buildAndRender { model, _ ->
            val user =
                model.addPerson("User", "A user of my software system.")
            val softwareSystem =
                model.addSoftwareSystem("Software System", "My software system.")
            user.uses(softwareSystem, "Uses")
        }
}
