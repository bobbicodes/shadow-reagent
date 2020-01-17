# shadow-reagent

Like the [official quickstart](https://github.com/thheller/shadow-cljs), but without the extra steps.

## Development

You'll likely want to change the name from `shadow-reagent` to whatever your project is called. Here's where you need to do that:

1. In the `shadow-cljs.edn` file in the project root (so your `init` fn will be called)
2. Rename the directory in the `src` path (inside the project root)- *make sure to change hyphens (-) to underscores (_).*
3. In the `ns` macro at the top of `app.cljs`

Now you can do the thing:

```bash
$ npm install
added 97 packages from 106 contributors in 5.984s
```

Start the development process by running:

```bash
$ npx shadow-cljs watch app
...
[:app] Build completed. (134 files, 35 compiled, 0 warnings, 5.80s)
```

Or simply `jack-in` from your editor. Your app will be served at: at [http://localhost:8080](http://localhost:8080).

## Production build

```bash
npx shadow-cljs release app
```
