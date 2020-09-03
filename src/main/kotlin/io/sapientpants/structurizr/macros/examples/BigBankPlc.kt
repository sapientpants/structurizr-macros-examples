package io.sapientpants.structurizr.macros.examples

import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.Location
import com.structurizr.util.MapUtils
import com.structurizr.view.PaperSize
import io.github.sapientpants.structurizr.macros.Tags
import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder

private const val EXISTING_SYSTEM_TAG = "Existing System"
private const val BANK_STAFF_TAG = "Bank Staff"
private const val FAILOVER_TAG = "Failover"

fun main() {
    val enterpriseName = "Big Bank plc"

    StructurizrBuilder(
        enterpriseName,
        enterpriseName,
        """|This is an example workspace to illustrate the key features of
           |Structurizr, based around a fictional online banking system.""".trimMargin()
    ).buildAndRender { model, views ->

        // people and software systems
        val customer = model.addPerson(
            Location.External,
            "Personal Banking Customer",
            "A customer of the bank, with personal bank accounts."
        )

        val internetBankingSystem = model.addSoftwareSystem(
            Location.Internal,
            "Internet Banking System",
            "Allows customers to view information about their bank accounts, and make payments."
        )
        customer.uses(internetBankingSystem, "Views account balances, and makes payments using")

        val mainframeBankingSystem = model.addSoftwareSystem(
            Location.Internal,
            "Mainframe Banking System",
            "Stores all of the core banking information about customers, accounts, transactions, etc."
        )
        mainframeBankingSystem.addTags(EXISTING_SYSTEM_TAG)
        internetBankingSystem.uses(mainframeBankingSystem, "Gets account information from, and makes payments using")

        val emailSystem = model.addSoftwareSystem(
            Location.Internal,
            "E-mail System",
            "The internal Microsoft Exchange e-mail system."
        )
        internetBankingSystem.uses(emailSystem, "Sends e-mail using")
        emailSystem.addTags(EXISTING_SYSTEM_TAG)
        emailSystem.delivers(customer, "Sends e-mails to")

        val atm =
            model.addSoftwareSystem(Location.Internal, "ATM", "Allows customers to withdraw cash.")
        atm.addTags(EXISTING_SYSTEM_TAG)
        atm.uses(mainframeBankingSystem, "Uses")
        customer.uses(atm, "Withdraws cash using")

        val customerServiceStaff =
            model.addPerson(Location.Internal, "Customer Service Staff", "Customer service staff within the bank.")
        customerServiceStaff.addTags(BANK_STAFF_TAG)
        customerServiceStaff.uses(mainframeBankingSystem, "Uses")
        customer.interactsWith(customerServiceStaff, "Asks questions to", "Telephone")

        val backOfficeStaff =
            model.addPerson(Location.Internal, "Back Office Staff", "Administration and support staff within the bank.")
        backOfficeStaff.addTags(BANK_STAFF_TAG)
        backOfficeStaff.uses(mainframeBankingSystem, "Uses")

        // containers
        val singlePageApplication: Container = internetBankingSystem.addContainer(
            "Single-Page Application",
            "Provides all of the Internet banking functionality to customers via their web browser.",
            "JavaScript and Angular"
        )
        singlePageApplication.addTags(Tags.WEB_BROWSER)
        val mobileApp: Container = internetBankingSystem.addContainer(
            "Mobile App",
            "Provides a limited subset of the Internet banking functionality to customers via their mobile device.",
            "Xamarin"
        )
        mobileApp.addTags(Tags.MOBILE_DEVICE_PORTRAIT)
        val webApplication: Container = internetBankingSystem.addContainer(
            "Web Application",
            "Delivers the static content and the Internet banking single page application.",
            "Java and Spring MVC"
        )
        val apiApplication: Container = internetBankingSystem.addContainer(
            "API Application",
            "Provides Internet banking functionality via a JSON/HTTPS API.",
            "Java and Spring MVC"
        )
        val database: Container = internetBankingSystem.addContainer(
            "Database",
            "Stores user registration information, hashed authentication credentials, access logs, etc.",
            "Oracle Database Schema"
        )
        database.addTags(Tags.DATABASE)

        customer.uses(webApplication, "Visits bigbank.com/ib using", "HTTPS")
        customer.uses(singlePageApplication, "Views account balances, and makes payments using", "")
        customer.uses(mobileApp, "Views account balances, and makes payments using", "")
        webApplication.uses(singlePageApplication, "Delivers to the customer's web browser", "")
        apiApplication.uses(database, "Reads from and writes to", "JDBC")
        apiApplication.uses(mainframeBankingSystem, "Makes API calls to", "XML/HTTPS")
        apiApplication.uses(emailSystem, "Sends e-mail using", "SMTP")

        // components
        // - for a real-world software system, you would probably want to extract the components using
        // - static analysis/reflection rather than manually specifying them all
        val signinController = apiApplication.addComponent(
            "Sign In Controller",
            "Allows users to sign in to the Internet Banking System.",
            "Spring MVC Rest Controller"
        )
        val accountsSummaryController = apiApplication.addComponent(
            "Accounts Summary Controller",
            "Provides customers with a summary of their bank accounts.",
            "Spring MVC Rest Controller"
        )
        val resetPasswordController = apiApplication.addComponent(
            "Reset Password Controller",
            "Allows users to reset their passwords with a single use URL.",
            "Spring MVC Rest Controller"
        )
        val securityComponent = apiApplication.addComponent(
            "Security Component",
            "Provides functionality related to signing in, changing passwords, etc.",
            "Spring Bean"
        )
        val mainframeBankingSystemFacade = apiApplication.addComponent(
            "Mainframe Banking System Facade",
            "A facade onto the mainframe banking system.",
            "Spring Bean"
        )
        val emailComponent =
            apiApplication.addComponent("E-mail Component", "Sends e-mails to users.", "Spring Bean")

        apiApplication.components.stream()
            .filter { c: Component -> "Spring MVC Rest Controller" == c.technology }
            .forEach { c: Component? ->
                singlePageApplication.uses(
                    c!!,
                    "Makes API calls to",
                    "JSON/HTTPS"
                )
            }
        apiApplication.components.stream()
            .filter { c: Component -> "Spring MVC Rest Controller" == c.technology }
            .forEach { c: Component? ->
                mobileApp.uses(
                    c!!,
                    "Makes API calls to",
                    "JSON/HTTPS"
                )
            }
        signinController.uses(securityComponent, "Uses")
        accountsSummaryController.uses(mainframeBankingSystemFacade, "Uses")
        resetPasswordController.uses(securityComponent, "Uses")
        resetPasswordController.uses(emailComponent, "Uses")
        securityComponent.uses(database, "Reads from and writes to", "JDBC")
        mainframeBankingSystemFacade.uses(mainframeBankingSystem, "Uses", "XML/HTTPS")
        emailComponent.uses(emailSystem, "Sends e-mail using")

        // deployment nodes and container instances
        // deployment nodes and container instances
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
            model.addDeploymentNode("Live", "Customer's mobile device", "", "Apple iOS or Android")
        customerMobileDevice.add(mobileApp)

        val customerComputer =
            model.addDeploymentNode("Live", "Customer's computer", "", "Microsoft Windows or Apple macOS")
        customerComputer.addDeploymentNode(
            "Web Browser",
            "",
            "Google Chrome, Mozilla Firefox, Apple Safari or Microsoft Edge"
        ).add(singlePageApplication)

        val bigBankDataCenter =
            model.addDeploymentNode("Live", enterpriseName, "", "Big Bank plc data center")

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

        model.relationships
            .filter { it.getDestination().equals(secondaryDatabase) }
            .forEach { it.addTags(FAILOVER_TAG) }
        primaryDatabaseServer.uses(secondaryDatabaseServer, "Replicates data to", "")
        secondaryDatabase.addTags(FAILOVER_TAG)

        val dynamicView = views.createDynamicView(
            apiApplication,
            "SignIn",
            "Summarises how the sign in feature works in the single-page application."
        )
        dynamicView.add(singlePageApplication, "Submits credentials to", signinController)
        dynamicView.add(signinController, "Calls isAuthenticated() on", securityComponent)
        dynamicView.add(securityComponent, "select * from users where username = ?", database)
        dynamicView.paperSize = PaperSize.A5_Landscape
    }
}
