package filters

import javax.inject.Inject
import play.api.http.{DefaultHttpFilters, EnabledFilters}

/**
 * Declares all the filters used in the app.
 *
 * @see https://www.playframework.com/documentation/2.6.x/ScalaHttpFilters#Using-filters
 */
class Filters @Inject()(defaultFilters: EnabledFilters, log: LoggingFilter)
  extends DefaultHttpFilters(defaultFilters.filters :+ log: _*)
