Description
-----------

[SLF4j][1] Gossip Provider

Features
--------

* Small footprint ~75k (event smaller for gossip-bootstrap-slf4j ~20k)
* Profile-based configuration
* Profile activation triggers
* Multiple source inputs
* Console and rolling file listeners
* ANSI color rendering
* Internal logging
* SLF4j support, helpers and utilities

Synopsis
--------

Need a small fast logging provider for [SLF4j][1]?
Don't want to pull in the kitchen sink with [Log4j][2]?
Hate [JUL][3]?

If you answered yes to any (or all) of the questions above, then Gossip might be the logging provider for you!

If you don't mind some additional byte-code weight, then you should probably look at [LOGBack][5].  This
is the recommended SLF4j provider when byte-code size is not an issue.

If you think that [JUL][3] is the best logging system ever, please exit the universe as soon as possible.

License
-------

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

Optional Dependencies
---------------------

* [jANSI][4] - For ANSI colored output

Configuration
-------------

Configuration of Gossip is handled via standard property files.

The [bootstrap configuration](http://github.com/jdillon/gossip/blob/master/gossip-core/src/main/resources/org/sonatype/gossip/bootstrap.properties)
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

Bootstrap or Internal Logging
-----------------------------

Gossip provides some minimal SLF4j components to be used:

* Before the proper logging was created
* In environments where only the very basic control over logging is required

The gossip-bootstrap artifact contains, [org.sonatype.gossip.Log](https://github.com/jdillon/gossip/blob/master/gossip-bootstrap/src/main/java/org/sonatype/gossip/Log.java)
which is a SLF4j LoggerFactory-like class that produces real SLF4j Logger instances.
This can be used in cases where the configuration/installation of SLF4j is not ready yet,
and can be converted to use the desired SLF4j provider using Log.configure(ILoggerFactory).  Gossip internal loggers
are delegates, so once the "real" SLF4j provider is configured they will be updated to use the target logging
systems Logger implementations.

For users that want to use standard SLF4j but need a little more control over the logging system than slf4j-simple provides,
the gossip-bootstrap-slf4j module provides bindings to use the internal Log as a real SLF4j binding.

Log has a few parameters controlled by system properties:

* org.sonatype.gossip.Log.threshold = [TRACE|DEBUG|INFO|WARN|ERROR]
* org.sonatype.gossip.Log.stream = [OUT|ERR]
* org.sonatype.gossip.Log.pattern (see [PatternRenderer](https://github.com/jdillon/gossip/blob/master/gossip-bootstrap/src/main/java/org/sonatype/gossip/render/PatternRenderer.java) for details)

In addition, these are configurable runtime via method calls.

Gossip uses this internal logging system when booting up to provide its gossip-slf4j bindings.

General Support and Helpers
---------------------------

In addition to providing SLF4j bindings, Gossip also contains some helpers classes for using SLF4j in general.

These can be found in [gossip-support](https://github.com/jdillon/gossip/blob/master/gossip-support).

Building
--------

### Requirements

* [Maven](http://maven.apache.org) 2+
* [Java](http://java.sun.com/) 5+

Check out and build:

    git clone git://github.com/jdillon/gossip.git
    cd gossip
    mvn install

[1]: http://slf4j.org
[2]: http://logging.apache.org/log4j
[3]: http://java.sun.com/j2se/1.5.0/docs/api/java/util/logging/package-summary.html
[4]: http://github.com/chirino/jansi
[5]: http://logback.qos.ch
