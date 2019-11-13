package io.sapientpants.structurizr.macros.examples

import com.structurizr.model.Location
import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder
import com.structurizr.view.PaperSize
import io.github.sapientpants.structurizr.macros.Tags
import com.structurizr.model.Person



fun main(args: Array<String>) {
    StructurizrBuilder.build(
        "Structurizr Macros Inc.",
        "Structurizr Macros Example",
        "An example project demonstrating how to use the Structurizr macros"
    ) { model, views ->
        model.addPerson(
            Location.External,
            "Personal Banking Customer",
            "A customer of the bank, with personal bank accounts."
        )

        val internetBankingSystem = model.addSoftwareSystem(
            Location.Internal,
            "Internet Banking System",
            "Allows customers to view information about their bank accounts, and make payments."
        )
        internetBankingSystem.addTags(Tags.SYSTEM_OF_INTEREST)

        val apiApplication = internetBankingSystem.addContainer(
            "API Application",
            "Provides Internet banking functionality via a JSON/HTTPS API.",
            "Java and Spring MVC"
        )

        val singlePageApplication = internetBankingSystem.addContainer(
            "Single-Page Application",
            "Provides all of the Internet banking functionality to customers via their web browser.",
            "JavaScript and Angular"
        )
        singlePageApplication.addTags(Tags.WEB_BROWSER)
        val database = internetBankingSystem.addContainer(
            "Database",
            "Stores user registration information, hashed authentication credentials, access logs, etc.",
            "Relational Database Schema"
        )
        database.addTags(Tags.DATABASE)

        val signinController = apiApplication.addComponent(
            "Sign In Controller",
            "Allows users to sign in to the Internet Banking System.",
            "Spring MVC Rest Controller"
        )

        val securityComponent = apiApplication.addComponent(
            "Security Component",
            "Provides functionality related to signing in, changing passwords, etc.",
            "Spring Bean"
        )

        apiApplication.components.filter { c ->
            "Spring MVC Rest Controller" == c.technology
        }.forEach { c ->
            singlePageApplication.uses(c, "Makes API calls to", "JSON/HTTPS")
        }
        signinController.uses(securityComponent, "Uses")
        securityComponent.uses(database, "Reads from and writes to", "JDBC")

        val dynamicView = views.createDynamicView(
            apiApplication,
            "SignIn",
            "Summarises how the sign in feature works in the single-page application."
        )
        dynamicView.add(singlePageApplication, "Submits credentials to", signinController)
        dynamicView.add(signinController, "Calls isAuthenticated() on", securityComponent)
        dynamicView.add(securityComponent, "select * from users where username = ?", database)
        dynamicView.setPaperSize(PaperSize.A5_Landscape)
    }
}
