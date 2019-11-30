package io.sapientpants.structurizr.macros.examples

import com.structurizr.model.Container
import com.structurizr.model.InteractionStyle.Asynchronous
import com.structurizr.model.InteractionStyle.Synchronous
import com.structurizr.view.Routing
import com.structurizr.view.Shape
import com.structurizr.view.ViewSet
import io.github.sapientpants.structurizr.macros.Tags
import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder
import io.github.sapientpants.structurizr.macros.styles.Style

private const val MICROSERVICE_TAG = "Microservice"
private const val MESSAGE_BUS_TAG = "Message Bus"
private const val DATASTORE_TAG = "Database"

class MicroservicesExampleStyle : Style() {
    override fun applyToViews(views: ViewSet) {
        val styles = views.configuration.styles
        styles.addElementStyle(Tags.ELEMENT).color("#000000")
        styles.addElementStyle(Tags.PERSON).background("#ffbf00")
            .shape(Shape.Person)
        styles.addElementStyle(Tags.CONTAINER).background("#facc2E")
        styles.addElementStyle(MESSAGE_BUS_TAG).width(1600).shape(Shape.Pipe)
        styles.addElementStyle(MICROSERVICE_TAG).shape(Shape.Hexagon)
        styles.addElementStyle(DATASTORE_TAG).background("#f5da81")
            .shape(Shape.Cylinder)
        styles.addRelationshipStyle(Tags.RELATIONSHIP)
            .routing(Routing.Orthogonal)

        styles.addRelationshipStyle(Tags.ASYNCHRONOUS).dashed(true)
        styles.addRelationshipStyle(Tags.SYNCHRONOUS).dashed(false)
    }
}

fun main(args: Array<String>) {
    StructurizrBuilder(
        "structurizr-macros-examples",
        "Microservices example",
        """|An example of a microservices architecture, which includes asynchronous 
        |and parallel behaviour.""".trimMargin()
    ).style(MicroservicesExampleStyle())
        .buildAndRender { model, views ->
        val mySoftwareSystem = model.addSoftwareSystem(
            "Customer Information System",
            "Stores information "
        )
        val customer = model.addPerson("Customer", "A customer")
        val customerApplication: Container = mySoftwareSystem.addContainer(
            "Customer Application",
            "Allows customers to manage their profile.",
            "Angular"
        )

        val customerService: Container = mySoftwareSystem.addContainer(
            "Customer Service",
            "The point of access for customer information.",
            "Java and Spring Boot"
        )
        customerService.addTags(MICROSERVICE_TAG)
        val customerDatabase: Container = mySoftwareSystem.addContainer(
            "Customer Database",
            "Stores customer information.",
            "Oracle 12c"
        )
        customerDatabase.addTags(DATASTORE_TAG)

        val reportingService: Container = mySoftwareSystem.addContainer(
            "Reporting Service",
            "Creates normalised data for reporting purposes.",
            "Ruby"
        )
        reportingService.addTags(MICROSERVICE_TAG)
        val reportingDatabase: Container = mySoftwareSystem.addContainer(
            "Reporting Database",
            "Stores a normalised version of all business data for ad hoc reporting purposes.",
            "MySQL"
        )
        reportingDatabase.addTags(DATASTORE_TAG)

        val auditService: Container = mySoftwareSystem.addContainer(
            "Audit Service",
            "Provides organisation-wide auditing facilities.",
            "C# .NET"
        )
        auditService.addTags(MICROSERVICE_TAG)
        val auditStore: Container = mySoftwareSystem.addContainer(
            "Audit Store",
            "Stores information about events that have happened.",
            "Event Store"
        )
        auditStore.addTags(DATASTORE_TAG)

        val messageBus: Container = mySoftwareSystem.addContainer(
            "Message Bus",
            "Transport for business events.",
            "RabbitMQ"
        )
        messageBus.addTags(MESSAGE_BUS_TAG)

        customer.uses(customerApplication, "Uses")
        customerApplication.uses(
            customerService,
            "Updates customer information using",
            "JSON/HTTPS",
            Synchronous
        )
        customerService.uses(
            messageBus,
            "Sends customer update events to",
            "",
            Asynchronous
        )
        customerService.uses(
            customerDatabase,
            "Stores data in",
            "JDBC",
            Synchronous
        )
        customerService.uses(
            customerApplication,
            "Sends events to",
            "WebSocket",
            Asynchronous
        )
        messageBus.uses(
            reportingService,
            "Sends customer update events to",
            "",
            Asynchronous
        )
        messageBus.uses(
            auditService,
            "Sends customer update events to",
            "",
            Asynchronous
        )
        reportingService.uses(
            reportingDatabase,
            "Stores data in",
            "",
            Synchronous
        )
        auditService.uses(
            auditStore,
            "Stores events in",
            "",
            Synchronous
        )

        val dynamicView = views.createDynamicView(
            mySoftwareSystem,
            "CustomerUpdateEvent",
            "This diagram shows what happens when a customer updates their details."
        )
        dynamicView.add(customer, customerApplication)
        dynamicView.add(customerApplication, customerService)

        dynamicView.add(customerService, customerDatabase)
        dynamicView.add(customerService, messageBus)

        dynamicView.startParallelSequence()
        dynamicView.add(messageBus, reportingService)
        dynamicView.add(reportingService, reportingDatabase)
        dynamicView.endParallelSequence()

        dynamicView.startParallelSequence()
        dynamicView.add(messageBus, auditService)
        dynamicView.add(auditService, auditStore)
        dynamicView.endParallelSequence()

        dynamicView.startParallelSequence()
        dynamicView.add(
            customerService,
            "Confirms update to",
            customerApplication
        )
        dynamicView.endParallelSequence()
    }
}
