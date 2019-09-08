import 'dart:async';
import 'dart:convert';

import 'package:http/http.dart';

import 'hero.dart';
import 'mock_heroes.dart';

const USE_MOCKS = false;

class HeroService {
  static const _heroesUrl = 'api/heroes';
  static final _headers = {'Content-Type': 'application/json'};

  final Client _http;

  HeroService(this._http);

  Future<List<Hero>> getAll() async {
    print("Get all heroes");

    if (USE_MOCKS) {
      return mockHeroes;
    }

    try {
      final response = await _http.get(_heroesUrl);
      final heroes = (_extractData(response) as List)
          .map((json) => Hero.fromJson(json))
          .toList();
      return heroes;
    } catch (e) {
      throw _handleError(e);
    }
  }

  Future<Hero> get(int id) async {
    print("Get hero '$id'");
    try {
      final response = await _http.get('$_heroesUrl/$id');
      return Hero.fromJson(_extractData(response));
    } catch (e) {
      throw _handleError(e);
    }
  }

  Future<Hero> update(Hero hero) async {
    print("Update hero '{$hero.toJson()}'");

    try {
      final response = await _http.put('$_heroesUrl/${hero.id}',
          headers: _headers, body: json.encode(hero));
      return Hero.fromJson(_extractData(response));
    } catch (e) {
      throw _handleError(e);
    }
  }

  Future<Hero> create(String name) async {
    try {
      print("Create hero '$name'");
      final response = await _http.post(_heroesUrl,
          headers: _headers, body: json.encode({'name': name}));
      return Hero.fromJson(_extractData(response));
    } catch (e) {
      throw _handleError(e);
    }
  }

  Future<void> delete(int id) async {
    print("Update hero with id '$id'");
    try {
      await _http.delete('$_heroesUrl/$id', headers: _headers);
    } catch (e) {
      throw _handleError(e);
    }
  }

  dynamic _extractData(Response resp) => json.decode(resp.body)['data'];

  Exception _handleError(dynamic e) {
    print(e); // for demo purposes only
    return Exception('Server error; cause: $e');
  }

  Future<List<Hero>> getAllSlowly() {
    return Future.delayed(Duration(seconds: 2), getAll);
  }

  Future<Hero> getOneInefficient(int id) async =>
      (await getAll()).firstWhere((hero) => hero.id == id);
}
