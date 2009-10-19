Description
-----------

[SLF4j][1] Gossip Provider

Features
--------

* Small footprint
* Profile-based configuration
* Profile activation triggers
* Multiple source inputs

Synopsis
--------

Need a small fast logging provider for [SLF4j][1]?  Don't want to pull in the kitchen sink with [Log4j][2]?  Hate [JUL][3]?

If you answered yes to any (or all) of the questions above, then Gossip might be the logging provider for you!

License
-------

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

Optional Dependencies
---------------------

When using the ANSI coloring renderer, Gossip needs to have a few more goodies:

* [jline2][4]
* [jansi][5]

Building
--------

### Requirements

* [Maven](http://maven.apache.org) 2.x
* [Java](http://java.sun.com/) 5

Check out and build:

    git clone git://github.com/jdillon/gossip.git
    cd gossip
    mvn install

[1]: http://slf4j.org
[2]: http://logging.apache.org/log4j
[3]: http://java.sun.com/j2se/1.5.0/docs/api/java/util/logging/package-summary.html
[4]: http://github.com/jdillon/jline2
[5]: http://github.com/chirino/jansi
