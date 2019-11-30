package io.sapientpants.structurizr.macros.examples

import com.structurizr.model.Container
import io.github.sapientpants.structurizr.macros.Tags
import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder

fun main(args: Array<String>) {
    StructurizrBuilder(
        "structurizr-macros-examples",
        "HTTP-based health checks example",
        "An example of how to use the HTTP-based health checks feature"
    ).buildAndRender { model, views ->
        val structurizr = model.addSoftwareSystem(
            "Structurizr",
            "A publishing platform for software architecture diagrams and documentation based upon the C4 model."
        )
        val webApplication: Container = structurizr.addContainer(
            "structurizr.com",
            "Provides all of the server-side functionality of Structurizr, serving static and dynamic content to users.",
            "Java and Spring MVC"
        )
        val database: Container = structurizr.addContainer(
            "Database",
            "Stores information about users, workspaces, etc.",
            "Relational Database Schema"
        )
        database.addTags(Tags.DATABASE)
        webApplication.uses(database, "Reads from and writes to", "JDBC")

        val amazonWebServices =
            model.addDeploymentNode("Amazon Web Services", "", "us-east-1")
        val pivotalWebServices =
            amazonWebServices.addDeploymentNode(
                "Pivotal Web Services",
                "Platform as a Service provider.",
                "Cloud Foundry"
            )
        val liveWebApplication =
            pivotalWebServices.addDeploymentNode(
                "www.structurizr.com",
                "An open source Java EE web server.",
                "Apache Tomcat"
            )
                .add(webApplication)
        val liveDatabaseInstance =
            amazonWebServices.addDeploymentNode(
                "Amazon RDS",
                "Database as a Service provider.",
                "MySQL"
            )
                .add(database)

        // add health checks to the container instances, which return a simple HTTP 200 to say everything is okay
        liveWebApplication.addHealthCheck(
            "Web Application is running",
            "https://www.structurizr.com/health"
        )
        liveDatabaseInstance.addHealthCheck(
            "Database is accessible from Web Application",
            "https://www.structurizr.com/health/database"
        )

        // the pass/fail status from the health checks is used to supplement any deployment views that include the container instances that have health checks defined
        val deploymentView = views.createDeploymentView(
            structurizr,
            "Deployment",
            "A deployment diagram showing the live environment."
        )
        deploymentView.environment = "Live"
        deploymentView.addAllDeploymentNodes()
    }
}
