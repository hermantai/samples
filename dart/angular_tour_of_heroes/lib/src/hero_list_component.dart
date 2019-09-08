import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';
import 'package:angular_forms/angular_forms.dart';
import 'hero.dart';
import 'hero_service.dart';
import 'route_paths.dart';

@Component(
  selector: 'my-heroes',
  templateUrl: 'hero_list_component.html',
  styleUrls: ['hero_list_component.css'],
  directives: [coreDirectives, formDirectives],
  pipes: [commonPipes],
)
class HeroListComponent implements OnInit {
  final HeroService _heroService;
  final Router _router;

  List<Hero> heroes;
  Hero selected;

  HeroListComponent(this._heroService, this._router);

  void onSelect(Hero hero) => selected = hero;

  void _fetchHeroes() async {
    heroes = await _heroService.getAll();
    //heroes = await _heroService.getAllSlowly();
  }

  // Learn more about lifecycle hooks: https://angulardart.dev/guide/lifecycle-hooks
  @override
  void ngOnInit() => _fetchHeroes();

  String _heroUrl(int id) => RoutePaths.hero.toUrl(parameters: {idParam: '$id'});

  Future<NavigationResult> gotoDetail() => _router.navigate(_heroUrl(selected.id));

  Future<void> add(String name) async {
    name = name.trim();
    if (name.isEmpty) return null;

    heroes.add(await _heroService.create(name));

    selected = null;
  }

  Future<void> delete(Hero hero) async {
    await _heroService.delete(hero.id);
    heroes.remove(hero);
    if (selected == hero) selected = null;
  }
}
