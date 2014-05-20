class ZkUrlMappings {

    static excludes = ['/zkau/*', '/zkcomet/*']

    static mappings = {
        "/"(view:"shop", controller: "shop", action: "index")
    }

}
