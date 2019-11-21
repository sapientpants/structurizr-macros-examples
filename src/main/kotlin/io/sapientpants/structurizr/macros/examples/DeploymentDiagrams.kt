package io.sapientpants.structurizr.macros.examples

import com.structurizr.model.Location
import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder
import com.structurizr.util.MapUtils
import io.github.sapientpants.structurizr.macros.Tags

private const val WEB_BROWSER_TAG = "Web Browser"
private const val MOBILE_APP_TAG = "Mobile App"
private const val DATABASE_TAG = "Database"
private const val FAILOVER_TAG = "Failover"

fun main(args: Array<String>) {
    StructurizrBuilder(
        "Structurizr Macros Inc.",
        "Structurizr Macros Example",
        "An example project demonstrating how to use the Structurizr macros"
    ).addImplicitRelationships(false)
        .buildAndRender { model, _ ->
        val internetBankingSystem = model.addSoftwareSystem(
            Location.Internal,
            "Internet Banking System",
            "Allows customers to view information about their bank accounts, and make payments."
        )
        internetBankingSystem.addTags(Tags.SYSTEM_OF_INTEREST)

        // containers
        val singlePageApplication = internetBankingSystem.addContainer(
            "Single-Page Application",
            "Provides all of the Internet banking functionality to customers via their web browser.",
            "JavaScript and Angular"
        )
        singlePageApplication.addTags(WEB_BROWSER_TAG)
        val mobileApp = internetBankingSystem.addContainer(
            "Mobile App",
            "Provides a limited subset of the Internet banking functionality to customers via their mobile device.",
            "Xamarin"
        )
        mobileApp.addTags(MOBILE_APP_TAG)
        val webApplication = internetBankingSystem.addContainer(
            "Web Application",
            "Delivers the static content and the Internet banking single page application.",
            "Java and Spring MVC"
        )
        val apiApplication = internetBankingSystem.addContainer(
            "API Application",
            "Provides Internet banking functionality via a JSON/HTTPS API.",
            "Java and Spring MVC"
        )
        val database = internetBankingSystem.addContainer(
            "Database",
            "Stores user registration information, hashed authentication credentials, access logs, etc.",
            "Relational Database Schema"
        )
        database.addTags(DATABASE_TAG)

        val developerLaptop = model.addDeploymentNode(
            "Development",
            "Developer Laptop",
            "A developer laptop.",
            "Microsoft Windows 10 or Apple macOS"
        )
        val apacheTomcat =
            developerLaptop.addDeploymentNode("Docker Container - Web Server", "A Docker container.", "Docker")
                .addDeploymentNode(
                    "Apache Tomcat",
                    "An open source Java EE web server.",
                    "Apache Tomcat 8.x",
                    1,
                    MapUtils.create("Xmx=512M", "Xms=1024M", "Java Version=8")
                )
        apacheTomcat.add(webApplication)
        apacheTomcat.add(apiApplication)

        developerLaptop.addDeploymentNode("Docker Container - Database Server", "A Docker container.", "Docker")
            .addDeploymentNode("Database Server", "A development database.", "Oracle 12c")
            .add(database)

        developerLaptop.addDeploymentNode(
            "Web Browser",
            "",
            "Google Chrome, Mozilla Firefox, Apple Safari or Microsoft Edge"
        ).add(singlePageApplication)

        val customerMobileDevice =
            model.addDeploymentNode("Production", "Customer's mobile device", "", "Apple iOS or Android")
        customerMobileDevice.add(mobileApp)

        val customerComputer =
            model.addDeploymentNode("Production", "Customer's computer", "", "Microsoft Windows or Apple macOS")
        customerComputer.addDeploymentNode(
            "Web Browser",
            "",
            "Google Chrome, Mozilla Firefox, Apple Safari or Microsoft Edge"
        ).add(singlePageApplication)

        val bigBankDataCenter = model.addDeploymentNode("Production", "Big Bank plc", "", "Big Bank plc data center")

        val liveWebServer = bigBankDataCenter.addDeploymentNode(
            "bigbank-web***",
            "A web server residing in the web server farm, accessed via F5 BIG-IP LTMs.",
            "Ubuntu 16.04 LTS",
            4,
            MapUtils.create("Location=London and Reading")
        )
        liveWebServer.addDeploymentNode(
            "Apache Tomcat",
            "An open source Java EE web server.",
            "Apache Tomcat 8.x",
            1,
            MapUtils.create("Xmx=512M", "Xms=1024M", "Java Version=8")
        )
            .add(webApplication)

        val liveApiServer = bigBankDataCenter.addDeploymentNode(
            "bigbank-api***",
            "A web server residing in the web server farm, accessed via F5 BIG-IP LTMs.",
            "Ubuntu 16.04 LTS",
            8,
            MapUtils.create("Location=London and Reading")
        )
        liveApiServer.addDeploymentNode(
            "Apache Tomcat",
            "An open source Java EE web server.",
            "Apache Tomcat 8.x",
            1,
            MapUtils.create("Xmx=512M", "Xms=1024M", "Java Version=8")
        )
            .add(apiApplication)

        val primaryDatabaseServer = bigBankDataCenter.addDeploymentNode(
            "bigbank-db01",
            "The primary database server.",
            "Ubuntu 16.04 LTS",
            1,
            MapUtils.create("Location=London")
        )
            .addDeploymentNode("Oracle - Primary", "The primary, live database server.", "Oracle 12c")
        primaryDatabaseServer.add(database)

        val secondaryDatabaseServer = bigBankDataCenter.addDeploymentNode(
            "bigbank-db02",
            "The secondary database server.",
            "Ubuntu 16.04 LTS",
            1,
            MapUtils.create("Location=Reading")
        )
            .addDeploymentNode(
                "Oracle - Secondary",
                "A secondary, standby database server, used for failover purposes only.",
                "Oracle 12c"
            )
        val secondaryDatabase = secondaryDatabaseServer.add(database)

        model.relationships.stream().filter { r -> r.destination.equals(secondaryDatabase) }
            .forEach { r -> r.addTags(FAILOVER_TAG) }
        primaryDatabaseServer.uses(secondaryDatabaseServer, "Replicates data to", "")
        secondaryDatabase.addTags(FAILOVER_TAG)
    }
}
