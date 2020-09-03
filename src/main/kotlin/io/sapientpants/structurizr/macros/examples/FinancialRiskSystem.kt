package io.sapientpants.structurizr.macros.examples

import com.structurizr.model.InteractionStyle
import com.structurizr.view.Border
import com.structurizr.view.Shape
import com.structurizr.view.ViewSet
import io.github.sapientpants.structurizr.macros.Tags
import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder
import io.github.sapientpants.structurizr.macros.styles.Style

private const val TAG_ALERT = "Alert"

class FinancialRiskSystemStyle : Style() {
    override fun applyToViews(views: ViewSet) {
        val styles = views.configuration.styles

        styles.addElementStyle(Tags.ELEMENT).color("#ffffff").fontSize(34)
        styles.addElementStyle("Risk System").background("#550000")
            .color("#ffffff")
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).width(650).height(400)
            .background("#801515").shape(Shape.RoundedBox)
        styles.addElementStyle(Tags.PERSON).width(550).background("#d46a6a")
            .shape(Shape.Person)

        styles.addRelationshipStyle(Tags.RELATIONSHIP).thickness(4)
            .dashed(false).fontSize(32).width(400)
        styles.addRelationshipStyle(Tags.SYNCHRONOUS).dashed(false)
        styles.addRelationshipStyle(Tags.ASYNCHRONOUS).dashed(true)
        styles.addRelationshipStyle(TAG_ALERT).color("#ff0000")

        styles.addElementStyle("Future State").opacity(30).border(Border.Dashed)
        styles.addRelationshipStyle("Future State").opacity(30).dashed(true)
    }
}

fun main() {
    StructurizrBuilder(
        "structurizr-macros-examples",
        "Financial Risk System",
        """|This is a simple (incomplete) example C4 model based upon the
        |financial risk system architecture kata, which can be found at 
|       |http://bit.ly/sa4d-risksystem""".trimMargin()
    ).buildAndRender { model, _ ->
        val financialRiskSystem = model.addSoftwareSystem(
            "Financial Risk System",
            "Calculates the bank's exposure to risk for product X."
        )
        financialRiskSystem.addTags("Risk System")

        val businessUser =
            model.addPerson("Business User", "A regular business user.")
        businessUser.uses(financialRiskSystem, "Views reports using")

        val configurationUser = model.addPerson(
            "Configuration User",
            "A regular business user who can also configure the parameters used in the risk calculations."
        )
        configurationUser.uses(
            financialRiskSystem,
            "Configures parameters using"
        )

        val tradeDataSystem = model.addSoftwareSystem(
            "Trade Data System",
            "The system of record for trades of type X."
        )
        financialRiskSystem.uses(tradeDataSystem, "Gets trade data from")

        val referenceDataSystem = model.addSoftwareSystem(
            "Reference Data System",
            "Manages reference data for all counterparties the bank interacts with."
        )
        financialRiskSystem.uses(
            referenceDataSystem,
            "Gets counterparty data from"
        )

        val referenceDataSystemV2 = model.addSoftwareSystem(
            "Reference Data System v2.0",
            "Manages reference data for all counterparties the bank interacts with."
        )
        referenceDataSystemV2.addTags("Future State")
        financialRiskSystem.uses(
            referenceDataSystemV2,
            "Gets counterparty data from"
        )!!.addTags("Future State")

        val emailSystem = model.addSoftwareSystem(
            "E-mail system",
            "The bank's Microsoft Exchange system."
        )
        financialRiskSystem.uses(
            emailSystem,
            "Sends a notification that a report is ready to"
        )
        emailSystem.delivers(
            businessUser,
            "Sends a notification that a report is ready to",
            "E-mail message",
            InteractionStyle.Asynchronous
        )

        val centralMonitoringService = model.addSoftwareSystem(
            "Central Monitoring Service",
            "The bank's central monitoring and alerting dashboard."
        )
        financialRiskSystem.uses(
            centralMonitoringService,
            "Sends critical failure alerts to",
            "SNMP",
            InteractionStyle.Asynchronous
        )!!.addTags(TAG_ALERT)

        val activeDirectory = model.addSoftwareSystem(
            "Active Directory",
            "The bank's authentication and authorisation system."
        )
        financialRiskSystem.uses(
            activeDirectory,
            "Uses for user authentication and authorisation"
        )
    }
}
