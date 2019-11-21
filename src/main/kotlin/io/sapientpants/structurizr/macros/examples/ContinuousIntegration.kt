package io.sapientpants.structurizr.macros.examples

import com.structurizr.model.Location
import io.github.sapientpants.structurizr.macros.Tags
import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder
import io.github.sapientpants.structurizr.macros.documentation.ArchitectureDocumentation

const val JSON_HTTPS = "JSON/HTTPS"
const val SMTP = "SMTP"

fun main(args: Array<String>) {
    StructurizrBuilder(
        "Structurizr Macros Inc.",
        "Structurizr Macros Example",
        "An example project demonstrating how to use the Structurizr macros"
    ).architectureDocumentation(ArchitectureDocumentation.ARC_42)
        .adrSourcePath("./src/main/markdown/adr")
        .buildAndRender { model, _ ->
            // Define users

            val admin = model.addPerson(
                Location.Internal,
                "An Administrator",
                "Any administrator who administers the Structurizr Macros Example system"
            )

            val customer = model.addPerson(
                Location.External,
                "A customer",
                "Any customer who uses the Structurizr Macros Example system")

            // Define software systems

            val softwareSystem = model.addSoftwareSystem(
                Location.Internal,
                "Structurizr Macros Example",
                "A system for demonstrating the Structurizr Macros")
            softwareSystem.addTags(Tags.SYSTEM_OF_INTEREST)

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
            mobileFrontend.addTags(Tags.MOBILE_DEVICE_PORTRAIT)

            val webFrontend = softwareSystem.addContainer("Web Frontend",
                "The web frontend for the Structurizr Macros Example system",
                "Vue.js")
            webFrontend.addTags(Tags.WEB_BROWSER)

            val backend = softwareSystem.addContainer("API",
                "The API for the Structurizr Macros Example system",
                "Spring Boot, Kotlin")

            val database = softwareSystem.addContainer("Database",
                "The data store for the Structurizr Macros Example system",
                "PostgreSQL")
            database.addTags(Tags.DATABASE)

            // Define components

            // ... mobile frontend components - omitted for brevity ...

            // ... web frontend components - omitted for brevity  ...

            // backend components

            val adminViews = backend.addComponent("Admin Views",
                "The Administrator views",
                "Spring Boot, Kotlin, HTML, CSS")

            val adminPresenters = backend.addComponent("Admin Presenters",
                "The Administrator presenters",
                "Spring Boot, Kotlin")

            val adminInteractors = backend.addComponent("Admin Interactors",
                "The Administrator interactors",
                "Spring Boot, Kotlin")

            val adminDataGateway = backend.addComponent("Admin Data Gateway",
                "The Admin Data Gateway",
                "Spring Boot, Kotlin")

            val adminControllers = backend.addComponent("Admin Controllers",
                "The Administrator controllers",
                "Spring Boot, Kotlin")

            val productDetailsViews = backend.addComponent("Product Details Views",
                "The Product Details views",
                "Spring Boot, Kotlin, HTML, CSS")

            val productDetailsPresenters = backend.addComponent("Product Details Presenters",
                "The Product Details presenters",
                "Spring Boot, Kotlin")

            val productDetailsInteractors = backend.addComponent("Product Details Interactors",
                "The Product Details interactors",
                "Spring Boot, Kotlin")

            val productDetailsDataGateway = backend.addComponent("Product Details Data Gateway",
                "The Product Details Data Gateway",
                "Spring Boot, Kotlin")

            val productDetailsControllers = backend.addComponent("Product Details Controllers",
                "The Product Details controllers",
                "Spring Boot, Kotlin")

            val searchViews = backend.addComponent("Search Views",
                "The Search views",
                "Spring Boot, Kotlin, HTML, CSS")

            val searchPresenters = backend.addComponent("Search Presenters",
                "The Search presenters",
                "Spring Boot, Kotlin")

            val searchInteractors = backend.addComponent("Search Interactors",
                "The Search interactors",
                "Spring Boot, Kotlin")

            val searchDataGateway = backend.addComponent("Search Data Gateway",
                "The Search Data Gateway",
                "Spring Boot, Kotlin")

            val searchControllers = backend.addComponent("Search Controllers",
                "The Search controllers",
                "Spring Boot, Kotlin")

            val shoppingCartViews = backend.addComponent("Shopping Cart Views",
                "The Shopping Cart views",
                "Spring Boot, Kotlin, HTML, CSS")

            val shoppingCartPresenters = backend.addComponent("Shopping Cart Presenters",
                "The Shopping Cart presenters",
                "Spring Boot, Kotlin")

            val shoppingCartInteractors = backend.addComponent("Shopping Cart Interactors",
                "The Shopping Cart interactors",
                "Spring Boot, Kotlin")

            val shoppingCartDataGateway = backend.addComponent("Shopping Cart Data Gateway",
                "The Shopping Cart Data Gateway",
                "Spring Boot, Kotlin")

            val shoppingCartControllers = backend.addComponent("Shopping Cart Controllers",
                "The Shopping Cart controllers",
                "Spring Boot, Kotlin")

            // Wire everything up

            customer.uses(mobileFrontend, "uses")
            customer.uses(webFrontend, "uses")

            admin.uses(adminControllers, "uses")

            adminControllers.uses(adminInteractors, "uses")
            adminControllers.uses(emailSystem, "uses")
            adminInteractors.uses(adminPresenters, "uses")
            adminInteractors.uses(adminDataGateway, "uses")
            adminPresenters.uses(adminViews, "uses")

            customer.uses(productDetailsControllers, "uses")
            customer.uses(searchControllers, "uses")
            customer.uses(shoppingCartControllers, "uses")

            adminDataGateway.uses(database, "uses")
            productDetailsDataGateway.uses(database, "uses")
            searchDataGateway.uses(database, "uses")
            shoppingCartDataGateway.uses(database, "uses")

            productDetailsControllers.uses(productDetailsInteractors, "uses")
            productDetailsInteractors.uses(productDetailsPresenters, "uses")
            productDetailsInteractors.uses(productDetailsDataGateway, "uses")
            productDetailsPresenters.uses(productDetailsViews, "uses")

            searchControllers.uses(searchInteractors, "uses")
            searchInteractors.uses(searchPresenters, "uses")
            searchInteractors.uses(searchDataGateway, "uses")
            searchPresenters.uses(searchViews, "uses")

            shoppingCartControllers.uses(shoppingCartInteractors, "uses")
            shoppingCartInteractors.uses(shoppingCartPresenters, "uses")
            shoppingCartInteractors.uses(shoppingCartDataGateway, "uses")
            shoppingCartPresenters.uses(shoppingCartViews, "uses")

            emailSystem.uses(emailProvider, "uses", SMTP)
        }
}