class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"500"(view:'/error')

    "/" {
      controller = "shop"
      action = "index"
    }

    "/shop1" {
      controller = "shop"
      action = "index"
    }

	}
}
