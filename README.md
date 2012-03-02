Description
-----------

[SLF4j][1] Gossip Provider

Features
--------

* Small footprint ~75k (even smaller for gossip-bootstrap-slf4j ~20k)
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

Extra Components
----------------

To avoid bloating out core with optional/extra components, [gossip-extra](https://github.com/jdillon/gossip/blob/master/gossip-extra)
is here to hold on to stuff which you might want, but which is not required by the core.

General Support and Helpers
---------------------------

In addition to providing SLF4j bindings, Gossip also contains some helpers classes for using SLF4j in general.

These can be found in [gossip-support](https://github.com/jdillon/gossip/blob/master/gossip-support).

Building
--------

### Requirements

* [Maven](http://maven.apache.org) 3+
* [Java](http://java.sun.com/) 5+

Check out and build:

    git clone git://github.com/jdillon/gossip.git
    cd gossip
    mvn install

[1]: http://slf4j.org
[2]: http://logging.apache.org/log4j
[3]: http://download.oracle.com/javase/1.5.0/docs/api/java/util/logging/package-summary.html
[4]: http://jansi.fusesource.org/
[5]: http://logback.qos.ch
