package io.sapientpants.structurizr.macros.examples

import com.structurizr.model.Location
import com.structurizr.view.Border
import com.structurizr.view.Shape
import com.structurizr.view.ViewSet
import io.github.sapientpants.structurizr.macros.Tags
import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder
import io.github.sapientpants.structurizr.macros.styles.Style

class WidgetsLimitedStyle : Style() {
    override fun applyToViews(views: ViewSet) {
        val styles = views.configuration.styles

        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).shape(Shape.RoundedBox)
        styles.addElementStyle(Tags.PERSON).shape(Shape.Person)

        styles.addElementStyle(Tags.ELEMENT).color("#ffffff")
        styles.addElementStyle(Tags.EXTERNAL).background("#EC5381")
            .border(Border.Dashed)
        styles.addElementStyle(Tags.INTERNAL).background("#B60037")
    }
}

fun main() {
    StructurizrBuilder(
        "Widgets Limited",
        "Widgets Limited",
        "Sells widgets to customers online."
    ).style(WidgetsLimitedStyle())
        .buildAndRender { model, views ->
            val customer = model.addPerson(
                Location.External,
                "Customer",
                "A customer of Widgets Limited."
            )
            val customerServiceUser = model.addPerson(
                Location.Internal,
                "Customer Service Agent",
                "Deals with customer enquiries."
            )
            val ecommerceSystem = model.addSoftwareSystem(
                Location.Internal,
                "E-commerce System",
                "Allows customers to buy widgets online via the widgets.com website."
            )
            val fulfilmentSystem = model.addSoftwareSystem(
                Location.Internal,
                "Fulfilment System",
                "Responsible for processing and shipping of customer orders."
            )
            val taxamo = model.addSoftwareSystem(
                Location.External,
                "Taxamo",
                "Calculates local tax (for EU B2B customers) and acts as a front-end for Braintree Payments."
            )
            taxamo.url = "https://www.taxamo.com"
            val braintreePayments = model.addSoftwareSystem(
                Location.External,
                "Braintree Payments",
                "Processes credit card payments on behalf of Widgets Limited."
            )
            braintreePayments.url = "https://www.braintreepayments.com"
            val jerseyPost = model.addSoftwareSystem(
                Location.External,
                "Jersey Post",
                "Calculates worldwide shipping costs for packages."
            )

            customer.interactsWith(customerServiceUser, "Asks questions to", "Telephone")
            customerServiceUser.uses(ecommerceSystem, "Looks up order information using")
            customer.uses(ecommerceSystem, "Places orders for widgets using")
            ecommerceSystem.uses(fulfilmentSystem, "Sends order information to")
            fulfilmentSystem.uses(jerseyPost, "Gets shipping charges from")
            ecommerceSystem.uses(taxamo, "Delegates credit card processing to")
            taxamo.uses(braintreePayments, "Uses for credit card processing")

            val systemLandscapeView =
                views.createSystemLandscapeView(
                    "SystemLandscape",
                    "The system landscape for Widgets Limited."
                )
            systemLandscapeView.addAllElements()

            val ecommerceSystemContext =
                views.createSystemContextView(
                    ecommerceSystem,
                    "EcommerceSystemContext",
                    "The system context diagram for the Widgets Limited e-commerce system."
                )
            ecommerceSystemContext.addNearestNeighbours(ecommerceSystem)
            ecommerceSystemContext.remove(
                customer.getEfferentRelationshipWith(
                    customerServiceUser
                )
            )

            val fulfilmentSystemContext =
                views.createSystemContextView(
                    fulfilmentSystem,
                    "FulfilmentSystemContext",
                    "The system context diagram for the Widgets Limited fulfilment system."
                )
            fulfilmentSystemContext.addNearestNeighbours(fulfilmentSystem)

            val dynamicView = views.createDynamicView(
                "CustomerSupportCall",
                "A high-level overview of the customer support call process."
            )
            dynamicView.add(customer, customerServiceUser)
            dynamicView.add(customerServiceUser, ecommerceSystem)
        }
}
