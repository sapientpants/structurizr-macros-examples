package io.sapientpants.structurizr.macros.examples

import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder

fun main() {
    StructurizrBuilder(
        "structurizr-macros-examples",
        "Name",
        "Description"
    ).buildAndRender { _, _ ->
    }
}
