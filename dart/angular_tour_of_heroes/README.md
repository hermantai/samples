## Setup for Development

Welcome to the example app used in the
[Setup for Development](https://webdev.dartlang.org/angular/guide/setup) page
of [Dart for the web](https://webdev.dartlang.org).

You can run a [hosted copy](https://webdev.dartlang.org/examples/quickstart) of this
sample. Or run your own copy:

1. Create a local copy of this repo (use the "Clone or download" button above).
2. Get the dependencies: `pub get`
3. Get the webdev tool: `pub global activate webdev`
4. Launch a development server: `webdev serve`
5. In a browser, open [http://localhost:8080](http://localhost:8080)

---

*Note:* The content of this repository is generated from the
[Angular docs repository][docs repo] by running the
[dart-doc-syncer](//github.com/dart-lang/dart-doc-syncer) tool.
If you find a problem with this sample's code, please open an [issue][].

[docs repo]: //github.com/dart-lang/site-webdev/tree/master/examples/ng/doc/quickstart
[issue]: //github.com/dart-lang/site-webdev/issues/new?title=[master]%20examples/ng/doc/quickstart

## How this project was built
This project basically follows https://angulardart.dev/tutorial/toh-pt0

1) Follow https://angulardart.dev/guide/setup#create-a-starter-project

2) Download the quickstart: https://github.com/angular-examples/quickstart/archive/master.zip

3) Rename the project name __angular_app__ to __angular_tour_of_heroes__ in
the following files:

    $ find . -type f ! -name "README.md" -exec grep -H angular_app {} \;

Optional)

    $ codemod.py --extensions yaml,dart angular_app angular_tour_of_heroes

4) Modify _pubsub.yaml_ as needed.

## Issues

### Import a new file in package:angular_tour_of_heroes fails

The following failed:

    import 'package:angular_tour_of_heroes/in_memory_data_service.dart';

```
[SEVERE]build_web_compilers:ddc on web/main.ddc.module: Unable to find modules for some sources, this is usually the result of either a
bad import, a missing dependency in a package (or possibly a dev_dependency
needs to move to a real dependency), or a build failure (if importing a
generated file).

Please check the following imports:

`import 'package:angular_tour_of_heroes/in_memory_data_service.dart';` from angular_tour_of_heroes|web/main.dart at 4:1
```

The solution is from: https://github.com/dart-lang/angular/issues/1606

    $ pub upgrade # upgrade dependencies
    $ pub run build_runner clean # clean out the generated code
    $ pub run build_runner serve # see if there are any new errors

May control-c and "webdev serve" again after the last command.