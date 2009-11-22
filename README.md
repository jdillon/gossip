Description
-----------

[SLF4j][1] Gossip Provider

Features
--------

* Small footprint ~77k (or ~58k shrunk with [ProGuard][5])
* Profile-based configuration
* Profile activation triggers
* Multiple source inputs
* Console and rolling file listeners
* ANSI color rendering

Synopsis
--------

Need a small fast logging provider for [SLF4j][1]?
Don't want to pull in the kitchen sink with [Log4j][2]?
Hate [JUL][3]?

If you answered yes to any (or all) of the questions above, then Gossip might be the logging provider for you!

License
-------

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

Optional Dependencies
---------------------

* [jANSI][4] - For ANSI colored output

Configuration
-------------

Configuration of Gossip is handled via standard property files.

The [bootstrap configuration](http://github.com/jdillon/gossip/blob/master/src/main/resources/org/sonatype/gossip/bootstrap.properties)
attempts to read:

1. META-INF/org.sonatype.gossip/config.properties (as resource)
2. -Dgossip.configuration (value of as file/url)
3. ~/.gossip/config.properties (as file)

For example, to configure Gossip for your application on a per-user level to enable DEBUG or TRACE, 
create a file named ~/.gossip/config.properties containing:

    version=1.0.0
    profiles=myapp-common, myapp-debug, myapp-trace
    profile.myapp-common.listeners=console
    profile.myapp-common.listener.console=org.sonatype.gossip.listener.ConsoleListener

    ## -Dmyapp.logging=DEBUG
    profile.myapp-debug.includes=myapp-common    
    profile.myapp-debug.triggers=default
    profile.myapp-debug.trigger.default=org.sonatype.gossip.trigger.SystemPropertyTrigger
    profile.myapp-debug.trigger.default.name=myapp.logging
    profile.myapp-debug.trigger.default.value=DEBUG
    profile.myapp-debug.logger.org.mycompany.myapp=DEBUG

    ## -Dmyapp.logging=TRACE
    profile.myapp-trace.includes=myapp-common
    profile.myapp-trace.triggers=default
    profile.myapp-trace.trigger.default=org.sonatype.gossip.trigger.SystemPropertyTrigger
    profile.myapp-trace.trigger.default.name=myapp.logging
    profile.myapp-trace.trigger.default.value=TRACE
    profile.myapp-trace.logger.org.mycompany.myapp=TRACE

If you want ANSI colors on the console, replace the __myapp-common__ profile with:

    profile.myapp-common.listeners=console
    profile.myapp-common.listener.console=org.sonatype.gossip.listener.ConsoleListener
    profile.myapp-common.listener.console.renderer=org.sonatype.gossip.render.ColorRenderer

Want to save log events to a file to?  Well then change the __myapp-common__ profile to:

    profile.myapp-common.listeners=console,file
    profile.myapp-common.listener.console=org.sonatype.gossip.listener.ConsoleListener
    profile.myapp-common.listener.console.renderer=org.sonatype.gossip.render.ColorRenderer
    profile.myapp-common.listener.file=org.sonatype.gossip.listener.FileListener
    profile.myapp-common.listener.file.file=${some.sysproperty}/myapp.log

Need your log file rolled when it gets too big?  Then use something like:

    profile.myapp-common.listeners=console,file
    profile.myapp-common.listener.console=org.sonatype.gossip.listener.ConsoleListener
    profile.myapp-common.listener.console.renderer=org.sonatype.gossip.render.ColorRenderer
    profile.myapp-common.listener.file=org.sonatype.gossip.listener.FileListener
    profile.myapp-common.listener.file.file=${some.sysproperty}/myapp.log
    profile.myapp-common.listener.file.rollingStrategy=org.sonatype.gossip.listener.FileSizeRollingStrategy
    profile.myapp-common.listener.file.rollingStrategy.maximumFileSize=10240
    profile.myapp-common.listener.file.rollingStrategy.maximumBackupIndex=5

When the log file exceed 10mb the file will be rolled.  At anyone time only 5 files will be preserved.
Older files will be renamed myapp.log-1, myapp.log-2, etc.
    
Building
--------

### Requirements

* [Maven](http://maven.apache.org) 2.x
* [Java](http://java.sun.com/) 5

Check out and build:

    git clone git://github.com/jdillon/gossip.git
    cd gossip
    mvn install

To build the jdk14 jar:

    mvn install -Dretro

To build the shrunk jar:

    mvn install -Dshrink

[1]: http://slf4j.org
[2]: http://logging.apache.org/log4j
[3]: http://java.sun.com/j2se/1.5.0/docs/api/java/util/logging/package-summary.html
[4]: http://github.com/chirino/jansi
[5]: http://proguard.sourceforge.net
