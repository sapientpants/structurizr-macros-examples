package io.sapientpants.structurizr.macros.examples

import com.structurizr.encryption.AesEncryptionStrategy
import io.github.sapientpants.structurizr.macros.builder.StructurizrBuilder

fun main(args: Array<String>) {
    StructurizrBuilder(
        "structurizr-macros-examples",
        "Client-side encrypted workspace",
        "This is a client-side encrypted workspace. The passphrase is 'password'."
    ).encryptionStrategy(AesEncryptionStrategy("password"))
        .buildAndRender { model, _ ->
        val user =
            model.addPerson("User", "A user of my software system.")
        val softwareSystem =
            model.addSoftwareSystem("Software System", "My software system.")
        user.uses(softwareSystem, "Uses")
    }
}
