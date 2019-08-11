package io.sapientpants.structurizr.macros.example

import com.structurizr.model.Location
import io.github.sapientpants.structurizr.macros.*

const val JSON_HTTPS = "JSON/HTTPS"
const val SMTP = "SMTP"

fun main(args: Array<String>) {
    // Setup the workspace
    val workspace = StructurizrInitializer.init(
        "Structurizr Macros Example",
        "An example project demonstrating how to use the Structurizr macros",
        "Structurizr Macros Inc."
    )

    val model = workspace.model

    val views = workspace.views

    // Define users

    val user = model.addPerson(
        Location.External,
        "A user",
        "Anyone who uses the Structurizr Macros Example system")

    // Define software systems

    val softwareSystem = model.addSoftwareSystem(
        Location.Internal,
        "Structurizr Macros Example",
        "A system for demonstrating the Structurizr Macros")
    softwareSystem.addTags(Styling.SYSTEM_OF_INTEREST_TAG)

    val emailSystem = model.addSoftwareSystem(
        Location.Internal,
        "Structurizr Macros Email System",
        "The internal email system"
    )

    val emailProvider = model.addSoftwareSystem(
        Location.External,
        "Email Provider",
        "A system for delivering email to users")

    // Define containers

    val mobileFrontend = softwareSystem.addContainer("Mobile Frontend",
        "The mobile frontend for the Structurizr Macros Example system",
        "Android/Kotlin, iOS/Swift")
    mobileFrontend.addTags(Styling.MOBILE_DEVICE_PORTRAIT_TAG)

    val webFrontend = softwareSystem.addContainer("Web Frontend",
        "The web frontend for the Structurizr Macros Example system",
        "Vue.js")
    webFrontend.addTags(Styling.WEB_BROWSER_TAG)

    val backend = softwareSystem.addContainer("Backend",
        "The backend for the Structurizr Macros Example system",
        "Spring Boot, Kotlin")

    val database = softwareSystem.addContainer("Database",
        "The data store for the Structurizr Macros Example system",
        "PostgreSQL")
    database.addTags(Styling.DATABASE_TAG)

    // Define components

    // ... mobile frontend components - omitted for brevity ...

    // ... web frontend components - omitted for brevity  ...

    // backend components

    val api = backend.addComponent("API",
        "The API endpoints",
        "Spring Boot, Kotlin")

    val businessLogic = backend.addComponent("Business Logic",
        "The business logic",
        "Sprint Boot, Kotlin")

    val repository = backend.addComponent("Repository",
        "The repository for accessing data",
        "Sprint Boot, Kotlin")

    // Wire everything up

    user.uses(mobileFrontend, "uses")
    user.uses(webFrontend, "uses")

    mobileFrontend.uses(api, "uses", JSON_HTTPS)

    webFrontend.uses(api, "uses", JSON_HTTPS)

    api.uses(businessLogic, "uses")

    businessLogic.uses(repository, "uses")
    businessLogic.uses(emailSystem, "uses", SMTP)

    emailSystem.uses(emailProvider, "uses", SMTP)

    repository.uses(database, "uses", "SQL")

    model.addImplicitRelationships()

    // Declare the diagrams to render

    SystemLandscapeView.addToViews(model, views)

    SystemContextView.addToViews(softwareSystem, views)

    ContainerView.addToViews(softwareSystem, views)

    ContainerContextViews.addToViews(softwareSystem.containers, views)

    ComponentViews.addToViews(softwareSystem.containers, views)

    AdrDocumentation.addToWorkspace(workspace, softwareSystem)

    Arc42Documentation.addToWorkspace(workspace, softwareSystem)

    // Apply the style
    Styling.apply(views)

    // Render the diagrams
    StructurizrRenderer.render(workspace)
}