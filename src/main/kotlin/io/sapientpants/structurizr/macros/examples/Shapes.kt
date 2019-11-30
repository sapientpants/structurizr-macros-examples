package io.sapientpants.structurizr.macros.examples

import com.structurizr.view.PaperSize
import com.structurizr.view.Shape
import com.structurizr.view.SystemLandscapeView
import com.structurizr.view.ViewSet
import io.github.sapientpants.structurizr.macros.Tags
import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder
import io.github.sapientpants.structurizr.macros.styles.Style

class ShapesStyle : Style() {
    override fun applyToViews(views: ViewSet) {
        val styles = views.configuration.styles

        styles.addElementStyle(Tags.ELEMENT).color("#ffffff")
            .background("#438dd5").fontSize(34).width(650).height(400)
        styles.addElementStyle("Box").shape(Shape.Box)
        styles.addElementStyle("RoundedBox").shape(Shape.RoundedBox)
        styles.addElementStyle("Ellipse").shape(Shape.Ellipse)
        styles.addElementStyle("Circle").shape(Shape.Circle)
        styles.addElementStyle("Cylinder").shape(Shape.Cylinder)
        styles.addElementStyle("Web Browser").shape(Shape.WebBrowser)
        styles.addElementStyle("Mobile Device Portrait")
            .shape(Shape.MobileDevicePortrait).width(400).height(650)
        styles.addElementStyle("Mobile Device Landscape")
            .shape(Shape.MobileDeviceLandscape)
        styles.addElementStyle("Pipe").shape(Shape.Pipe)
        styles.addElementStyle("Folder").shape(Shape.Folder)
        styles.addElementStyle("Hexagon").shape(Shape.Hexagon)
        styles.addElementStyle("Robot").shape(Shape.Robot).width(550)
        styles.addElementStyle("Person").shape(Shape.Person).width(550)
    }
}

fun main(args: Array<String>) {
    StructurizrBuilder(
        "structurizr-macros-examples",
        "Shapes",
        "An example of all shapes available in Structurizr."
    ).style(ShapesStyle())
        .buildAndRender { model, views ->
        model.addSoftwareSystem("Box", "Description").addTags("Box")
        model.addSoftwareSystem("RoundedBox", "Description").addTags("RoundedBox")
        model.addSoftwareSystem("Ellipse", "Description").addTags("Ellipse")
        model.addSoftwareSystem("Circle", "Description").addTags("Circle")
        model.addSoftwareSystem("Hexagon", "Description").addTags("Hexagon")
        model.addSoftwareSystem("Cylinder", "Description").addTags("Cylinder")
        model.addSoftwareSystem("WebBrowser", "Description").addTags("Web Browser")
        model.addSoftwareSystem("Mobile Device Portrait", "Description").addTags("Mobile Device Portrait")
        model.addSoftwareSystem("Mobile Device Landscape", "Description").addTags("Mobile Device Landscape")
        model.addSoftwareSystem("Pipe", "Description").addTags("Pipe")
        model.addSoftwareSystem("Folder", "Description").addTags("Folder")
        model.addSoftwareSystem("Robot", "Description").addTags("Robot")
        model.addPerson("Person", "Description").addTags("Person")

        val view: SystemLandscapeView = views.createSystemLandscapeView(
            "shapes",
            "An example of all shapes available in Structurizr."
        )
        view.addAllElements()
        view.setPaperSize(PaperSize.A5_Landscape)
    }
}
