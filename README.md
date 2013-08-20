Wannacode - Search
==================

A search module for GitHub issue recommendation service [Wannacode](http://wannacode.herokuapp.com).

Installation Guide
==================
* Unpack an [Apache Solr distribution](http://lucene.apache.org/solr/mirrors-solr-latest-redir.html?) (>= 4.4.0 version is required)
* Replace the following configuration files from this project:
  * _etc/schema.xml_ -> _$SOLR_DIR/example/solr/collection1/conf/schema.xml_
  * _etc/solrconfig.xml_ -> _$SOLR_DIR/example/solr/collection1/conf/solrconfig.xml_
* Build a JAR file of the project, you may do that via [sbt](http://www.scala-sbt.org):
<pre><code>sbt</code></pre>
<pre><code>> package</code></pre>

* Put the resulting JAR file as well as _scala-library.jar_ into _$SOLR_DIR/example/solr/lib_
* Run Solr and have fun!
