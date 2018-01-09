
package layouts.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml

object default extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[String,Html,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(title: String)(body: Html):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.29*/("""
"""),format.raw/*2.1*/("""<html>
    <head>
        <title>"""),_display_(/*4.17*/title),format.raw/*4.22*/("""</title>
        <meta charset="utf-8" />
        <meta name="author" content="Nud Teeraworamongkol">
        <meta name="viewport" content="initial-scale=1.0,user-scalable=no,maximum-scale=1,width=device-width">
        <meta name="viewport" content="initial-scale=1.0,user-scalable=no,maximum-scale=1" media="(device-height: 568px)">
        <meta name="apple-mobile-web-app-title" content="Material Console">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
        <meta name="format-detection" content="telephone=no">
        <meta name="HandheldFriendly" content="True">
        <meta http-equiv="cleartype" content="on">
    </head>
    <body style='background-color:#2980b9'>
        """),_display_(/*17.10*/body),format.raw/*17.14*/("""
    """),format.raw/*18.5*/("""</body>
</html>
"""))
      }
    }
  }

  def render(title:String,body:Html): play.twirl.api.HtmlFormat.Appendable = apply(title)(body)

  def f:((String) => (Html) => play.twirl.api.HtmlFormat.Appendable) = (title) => (body) => apply(title)(body)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Wed Nov 22 15:12:30 CET 2017
                  SOURCE: /Users/bertrand/Desktop/SDTD/akka/src/main/twirl/layouts/default.scala.html
                  HASH: b3a2fc10b19ad44a02cb645551ddb85f6648eba9
                  MATRIX: 575->1|697->28|724->29|784->63|809->68|1633->865|1658->869|1690->874
                  LINES: 14->1|19->1|20->2|22->4|22->4|35->17|35->17|36->18
                  -- GENERATED --
              */
          