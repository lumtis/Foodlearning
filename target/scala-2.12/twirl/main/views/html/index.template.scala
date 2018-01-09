
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml

object index extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[String,String,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(container: String, model: String):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.36*/("""
"""),_display_(/*2.2*/layouts/*2.9*/.html.default("Welcome to Akka-HTTP")/*2.46*/{_display_(Seq[Any](format.raw/*2.47*/("""
    """),format.raw/*3.5*/("""<div id="container">"""),_display_(/*3.26*/Html(container)),format.raw/*3.41*/("""</div>
    <script type="text/javascript" src="./js/bundle.js"></script>
    <script type="text/javascript">
        var frontend = new com.nudemeth.example.web.Frontend();
        frontend.renderClient("""),_display_(/*7.32*/Html(model)),format.raw/*7.43*/(""");
    </script>
""")))}))
      }
    }
  }

  def render(container:String,model:String): play.twirl.api.HtmlFormat.Appendable = apply(container,model)

  def f:((String,String) => play.twirl.api.HtmlFormat.Appendable) = (container,model) => apply(container,model)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Wed Nov 22 14:29:14 CET 2017
                  SOURCE: /Users/bertrand/Desktop/SDTD/akka/src/main/twirl/views/index.scala.html
                  HASH: 2da60315c0dd715e1a17002583a3aeb232e5b6ce
                  MATRIX: 573->1|702->35|729->37|743->44|788->81|826->82|857->87|904->108|939->123|1169->327|1200->338
                  LINES: 14->1|19->1|20->2|20->2|20->2|20->2|21->3|21->3|21->3|25->7|25->7
                  -- GENERATED --
              */
          