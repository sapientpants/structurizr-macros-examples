package io.sapientpants.structurizr.macros.examples

import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder

fun main(args: Array<String>) {
    StructurizrBuilder(
        "structurizr-macros-examples",
        "Name",
        "Description"
    ).buildAndRender { _, _ ->
    }
}
