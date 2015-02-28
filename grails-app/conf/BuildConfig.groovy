grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.7
grails.project.source.level = 1.7
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
  // inherit Grails' default dependencies
  inherits("global") {
    // uncomment to disable ehcache
    // excludes 'ehcache'
  }
  log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
  checksums true // Whether to verify checksums on resolve

  repositories {
    //inherits true // Whether to inherit repository definitions from plugins
    mavenRepo "http://dl.bintray.com/zkgrails/release"
    grailsPlugins()
    grailsHome()
    grailsCentral()
    mavenCentral()

    // uncomment these to enable remote dependency resolution from public Maven repositories
    //mavenCentral()
    //mavenLocal()
    //mavenRepo "http://snapshots.repository.codehaus.org"
    //mavenRepo "http://repository.codehaus.org"
    //mavenRepo "http://download.java.net/maven/2/"
    //mavenRepo "http://repository.jboss.com/maven2/"
    mavenRepo "http://repo.grails.org/grails/plugins"
  }
  dependencies {
    // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
    compile 'com.google.guava:guava:r05'
    compile 'postgresql:postgresql:9.1-901.jdbc3'
    compile 'org.apache.poi:poi-ooxml:3.9'
    compile 'com.mortennobel:java-image-scaling:0.8.4'
    compile 'org.imgscalr:imgscalr-lib:4.2'
    compile 'com.jhlabs:filters:2.0.235-1'
    compile 'javax.mail:mail:1.4'
    compile 'org.codehaus.groovy:groovy-templates:2.3.8'
    //compile 'commons-codec:commons-codec:1.3'
    //runtime 'mysql:mysql-connector-java:5.1.16'
  }

  plugins {
    runtime ":hibernate:3.6.10.13"
    //runtime ":jquery:1.11.0.1"
    //compile ":jquery-ui:1.10.3"
    runtime ":resources:1.2.7"
    compile ":executor:0.3"
    compile ":i18n-templates:1.1.0.1"
    // Uncomment these (or add new ones) to enable additional resources capabilities
    //runtime ":zipped-resources:1.0"
    //runtime ":cached-resources:1.0"
    //runtime ":yui-minify-resources:0.1.4"
    compile ":zk:2.4.0"
    compile ":zk-bootstrap:1.0.0" // ZK Bootstrap support
    compile ":zk-atlantic:1.0.1"     // ZK Atlantic flat theme
    //compile ":zkui:0.5.4"
    //compile ":zk-ee:1.0.6"
    build ":tomcat:7.0.52.1"
  }
}

forkConfig = [maxMemory: 1024, minMemory: 64, debug: false, maxPerm: 256]
grails.project.fork = [
    test: false, // configure settings for the test-app JVM
    run: false, // configure settings for the run-app JVM
    war: false, // configure settings for the run-war JVM
    console: false // configure settings for the Swing console JVM
]

grails.reload.enabled = false
