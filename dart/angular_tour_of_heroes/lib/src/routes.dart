import 'package:angular_router/angular_router.dart';

import 'route_paths.dart';
import 'hero_component.template.dart' as hero_template;
import 'hero_list_component.template.dart' as hero_list_template;
import 'dashboard_component.template.dart' as dashboard_template;

export 'route_paths.dart';

class Routes {
  static final heroes = RouteDefinition(
      routePath: RoutePaths.heroes,
      component: hero_list_template.HeroListComponentNgFactory);
  static final dashboard = RouteDefinition(
      routePath: RoutePaths.dashboard,
      component: dashboard_template.DashboardComponentNgFactory);
  static final hero = RouteDefinition(
      routePath: RoutePaths.hero,
      component: hero_template.HeroComponentNgFactory);

  static final all = <RouteDefinition>[
    heroes,
    dashboard,
    hero,
    // This allows the browser to show the dashboard when the path is '/'
    RouteDefinition.redirect(
      path: '',
      redirectTo: RoutePaths.dashboard.toUrl(),
    ),
  ];
}
