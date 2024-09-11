plugins {
    id("fpgradle-minecraft") version ("0.7.5")
}

group = "divinewarning"

minecraft_fp {
    mod {
        modid = "divinewarning"
        name = "Divine Warning"
        rootPkg = "$group"
    }
    tokens {
        tokenClass = "Tags"
    }
}