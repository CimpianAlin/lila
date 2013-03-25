package lila.game

import lila.db.DbApi
import lila.db.Types._
import lila.common.PimpedJson._

import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits._

import play.modules.reactivemongo.Implicits.{ JsObjectWriter ⇒ _, _ }
import lila.db.PlayReactiveMongoPatch._

final class PgnRepo(coll: ReactiveColl) extends DbApi {

  def get(id: String): Fu[String] = query(select(id)).one map { objOption ⇒
    ~(objOption flatMap (_ str "p"))
  }

  def save(id: String, pgn: String): Funit = 
    coll.update(select(id), $set("p" -> pgn), upsert = true).void

  def removeIds(ids: List[String]): Funit = coll.remove(select byIds ids).void

  private def query(selector: JsObject) = coll.genericQueryBuilder query selector
}
