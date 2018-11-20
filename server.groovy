@GrabResolver(name="OJO", root="https://oss.jfrog.org/artifactory/repo")
@GrabConfig(systemClassLoader = true)
@Grab("io.ratpack:ratpack-groovy:1.3.3")
@Grab(group = 'org.postgresql', module = 'postgresql', version = '9.4-1205-jdbc41')
@Grab(group = 'ch.qos.logback', module = 'logback-classic', version = '1.0.13')

import static ratpack.groovy.Groovy.ratpack
import groovy.sql.Sql
import groovy.time.TimeCategory
import ratpack.http.Status


ratpack {
    handlers {
		get("infr/k8s/configmaps") {
			def body = '{}'
			
			switch (request.queryParams.namespace) {
				case "validNamespace":
				case "noConfigMapsNamespace":
				case "emptyNamespace":
					body = '{}'
					break
				case "notExistingNamespace":					
					body = '''{  
		                "data": [], 
		                "meta": { 
		                    "items": 0 
		                },
		                "errors": [
		                    {
		                        "code": "error.validation.notExists",
		                        "field": "namespace"
		                    }
		                ]
		            }'''
		            break

			}
			
			response.status 200
			response.headers.set('Content-Type', 'application/json')
            render body
        }
    }
}