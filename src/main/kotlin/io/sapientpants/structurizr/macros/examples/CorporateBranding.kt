package io.sapientpants.structurizr.macros.examples

import com.structurizr.util.ImageUtils
import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder
import io.github.sapientpants.structurizr.macros.documentation.ArchitectureDocumentation
import java.io.File

fun main() {
    StructurizrBuilder(
        "structurizr-macros-examples",
        "Corporate Branding",
        "This is a model of my software system."
    ).architectureDocumentation(ArchitectureDocumentation.STRUCTURIZR)
        .documentationSourcePath("./src/main/markdown/structurizr")
        .buildAndRender { model, views ->
            val user =
                model.addPerson("User", "A user of my software system.")
            val softwareSystem = model.addSoftwareSystem(
                "Software System",
                "My software system."
            )
            user.uses(softwareSystem, "Uses")

            val branding = views.configuration.branding
            branding.logo =
                ImageUtils.getImageAsDataUri(File("./src/main/resources/structurizr-logo.png"))
        }
}
