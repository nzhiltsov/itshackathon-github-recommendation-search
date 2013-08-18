package ru.ksu.niimm.cll.github.search.util

import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.index.Term

import scala.collection.JavaConversions._
import org.apache.lucene.document.Document
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.util.Version
import org.apache.lucene.search.Query
import java.util
import org.slf4j.LoggerFactory
import java.util.Locale


/**
 * @author Nikita Zhiltsov 
 */
object LuceneUtil {
  val logger = LoggerFactory.getLogger("search.LuceneUtil")

  def read(tokenStream: TokenStream): List[String] =
    if (tokenStream.incrementToken) tokenStream.getAttribute(classOf[CharTermAttribute]).toString :: read(tokenStream)
    else List.empty

  def readStream(tokenStream: TokenStream): Set[String] = {
    tokenStream.reset
    val tokens = read(tokenStream)
    tokenStream.end
    tokenStream.close
    tokens.toSet
  }

  def extract(term: Term): String = term.text

  def tokens(terms: java.util.Set[Term]): Set[String] = terms.toSet map extract

  def isInField(fieldName: String)(term: Term): Boolean = term.field.equals(fieldName)

  def getQueryTerms(query: Query, fieldName: String): Set[String] = {
    val terms = new util.HashSet[Term]()
    query.extractTerms(terms)
    def isInFieldFunc: Term => Boolean = isInField(fieldName)
    tokens(terms filter isInFieldFunc)
  }

  def jaccardSimilarity(document: Document, fieldName: String, query: Query): Float = {
    val queryTerms = getQueryTerms(query, fieldName)
    val fieldValues = document.getValues(fieldName).map(_ toLowerCase).toSet
    if (fieldValues.union(queryTerms).size != 0)
      fieldValues.intersect(queryTerms).size.toFloat / fieldValues.union(queryTerms).size
    else 0
  }

  val tagMap = Map("newbie" -> 1f, "bug" -> 0.9f, "enhancement" -> 0.3f, "feature" -> 0.1f, "task" -> 0.1f, "wontfix" -> 0f)

  def tagScore(document: Document, fieldName: String): Float = {
    val fieldValues = document.getValues("tags").toSet
    val commonTags: Set[String] = tagMap.keySet.intersect(fieldValues)
    if (commonTags.isEmpty) 0.5f else tagMap(commonTags.head)
  }

}
